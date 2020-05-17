/*
 * GearingView.java
 *
 * Created on 12 December 2001, 16:50
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author kevinm
 * @author valeri chibaev
 * @author shibaevv
 */

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.chart.GearingGraphView;
import com.argus.financials.code.GearingType;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.DocumentUtils;
import com.argus.financials.projection.GearingCalc;
import com.argus.financials.projection.GearingCalc2;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.save.Model;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.AbstractPanel;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.format.Currency;
import com.argus.format.CurrencyLabelGenerator;
import com.argus.format.Number2;
import com.argus.format.NumberLabelGenerator;
import com.argus.format.Percent;
import com.argus.format.PercentLabelGenerator;
import com.argus.io.IOUtils2;
import com.argus.swing.SwingUtils;
import com.klg.jclass.chart.JCLineStyle;

public class GearingView
    extends AbstractPanel
    implements ActionEventID,
        javax.swing.event.ChangeListener {

    private static final int RESULT_PAGE_ID = 1;

    // public static final double HOLE = MoneyCalc.HOLE;

    public static final int ASSETS = 1;

    public static final int INCOME = 2;

    public static final int EQUITY = 3;

    private GearingGraphView graphView;

    private GearingCalc gCalc;

    // private GearingCalc2 gCalc;

    /** Creates new form GearingView */
    public GearingView() {
        gCalc = new GearingCalc();
        // gCalc = new GearingCalc2();

        initComponents();
        initComponents2();
    }

    public GearingView(MoneyCalc calc) {
        gCalc = (GearingCalc) calc;
        // gCalc = (GearingCalc2) calc;

        initComponents();
        initComponents2();
    }

    public Integer getDefaultType() {
        return ModelTypeID.rcINVESTMENT_GEARING.getCodeId();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcINVESTMENT_GEARING.getDescription();
    }

    private Model getModel() {
        Model model = gCalc.getModel();
        if (model.getOwner() != null)
            return model;

        PersonService person = clientService;
        if (person != null) {
            try {
                model.setOwner(person.getModels());
            } catch (com.argus.financials.api.ServiceException e) {
                e.printStackTrace(System.err);
            }
        }
        return model;
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void setTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    private void initComponents2() {
        jComboBoxGearingType.setSelectedItem(null);
        jComboBoxInvestmentStrategy.setSelectedItem(null);

        graphView = (GearingGraphView) jPanelGraph;
        SwingUtils.setDefaultFont(graphView);

        // invisible by default
        jTabbedPane.removeTabAt(RESULT_PAGE_ID);

        _setAccessibleContext();

        if (!gCalc.getClass().equals(GearingCalc2.class)) {
            gCalc.addChangeListener(this);
            DocumentUtils.addListener(this, gCalc); // after
                                                    // _setAccessibleContext()
            updateEditable();
            setActionMap();
        }

    }

    private void _setAccessibleContext() {

        jComboBoxGearingType.getAccessibleContext().setAccessibleName(
                DocumentNames.GEARING_TYPE);
        jTextFieldClientStartLSRRate.getAccessibleContext().setAccessibleName(
                DocumentNames.CLIENT_START_LSR_RATE);
        jTextFieldMaximumStartLSRRate.getAccessibleContext().setAccessibleName(
                DocumentNames.MAXIMUM_START_LSR_RATE);

        jTextFieldCreditLimitAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.CREDIT_LIMIT_AMOUNT);
        jTextFieldLoanInterestRate.getAccessibleContext().setAccessibleName(
                DocumentNames.LOAN_INTEREST_RATE);
        jTextFieldRegularContributionAmount.getAccessibleContext()
                .setAccessibleName(DocumentNames.REGULAR_CONTRIBUTION_AMOUNT);
        jTextFieldInitialContributionAmount.getAccessibleContext()
                .setAccessibleName(DocumentNames.INITIAL_CONTRIBUTION_AMOUNT);

        jTextFieldEntryFeeRate.getAccessibleContext().setAccessibleName(
                DocumentNames.ENTRY_FEES);
        jTextFieldManagementFeeRate.getAccessibleContext().setAccessibleName(
                DocumentNames.REVIEW_FEES);
        jTextFieldClientTaxRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_RATE);

        jCheckBoxIndexed.getAccessibleContext().setAccessibleName(
                DocumentNames.INDEXED);
        jTextFieldIndexRate.getAccessibleContext().setAccessibleName(
                DocumentNames.INDEX_RATE);

        jComboBoxInvestmentStrategy.getAccessibleContext().setAccessibleName(
                DocumentNames.INV_STRATEGY);

        jTextFieldFrankingCreditRate.getAccessibleContext().setAccessibleName(
                DocumentNames.FRANKING_CREDIT_RATE);
        jTextFieldTaxFreeDefferedRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_FREE_DEFFERED_RATE);
        jTextFieldCapitalGainsRate.getAccessibleContext().setAccessibleName(
                DocumentNames.CAPITAL_GAINS_RATE);
    }

    protected void updateComponents() {
        boolean add = gCalc != null && gCalc.isReady();

        if (add) {
            if (jTabbedPane.getTabCount() == RESULT_PAGE_ID)
                jTabbedPane.addTab("Projections", jPanelResult);
        } else {
            if (jTabbedPane.getTabCount() > RESULT_PAGE_ID)
                jTabbedPane.removeTabAt(RESULT_PAGE_ID);
        }

        jButtonNext.setEnabled(add && jTabbedPane.getSelectedIndex() == 0);
        jButtonSave
                .setEnabled(clientService != null);

    }

    public void setActionListener(java.awt.event.ActionListener al) {
        // to notify main form/applet
        jButtonClose.addActionListener(al);
        jButtonSave.addActionListener(al);
    }

    public static final java.awt.Color GEARING = java.awt.Color.blue.darker();

    public static final java.awt.Color NON_GEARING = new Color(142, 2, 142);

    private void updateChart(int selection) {

        if (gCalc == null || !gCalc.isReady())
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            graphView.removeChartLabels();

            // JCLineStyle.NONE, JCLineStyle.SOLID, JCLineStyle.LONG_DASH,
            // JCLineStyle.SHORT_DASH, JCLineStyle.LSL_DASH,
            // JCLineStyle.DASH_DOT
            switch (selection) {
            case (ASSETS): {
                graphView.addChartLabels(graphView.customizeChart(
                        new double[][] { gCalc.getBalanceGearing(),
                                gCalc.getBalanceNonGearing() }, gCalc
                                .getLabels(), new String[] {
                                "<html>Assets <i>(Gearing)</i></html>",
                                "<html>Assets <i>(Non-Gearing)</i></html>" },
                        new java.awt.Color[] { GEARING, NON_GEARING }, true),
                        Currency.getCurrencyInstance());
                graphView.setTitleAxisY1("Assets");
                graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                        .getInstance());

                graphView.addChartLabels(graphView.customizeChart(
                        new double[][] { gCalc.getUnitsGearing(),
                                gCalc.getUnitsNonGearing() },
                        gCalc.getLabels(), new String[] {
                                "<html>Units <i>(Gearing)</i></html>",
                                "<html>Units <i>(Non-Gearing)</i></html>" },
                        new java.awt.Color[] { GEARING, NON_GEARING },
                        new int[] { JCLineStyle.SHORT_DASH,
                                JCLineStyle.SHORT_DASH }, false), Number2
                        .getNumberInstance());
                graphView.setTitleAxisY2("<html>Units</html>");
                graphView.setLabelGeneratorAxisY2(NumberLabelGenerator
                        .getInstance());

                break;
            }
            case (INCOME): {
                graphView.addChartLabels(graphView.customizeChart(
                        new double[][] { gCalc.getIncomeGearing(),
                                gCalc.getIncomeNonGearing() }, gCalc
                                .getLabels(), new String[] {
                                "<html>Income <i>(Gearing)</i></html>",
                                "<html>Income <i>(Non-Gearing)</i></html>" },
                        new java.awt.Color[] { GEARING, NON_GEARING }, true),
                        Currency.getCurrencyInstance());
                graphView.setTitleAxisY1("Income");
                graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                        .getInstance());

                // remove second data view
                graphView.setTitleAxisY2(null);

                break;
            }
            case (EQUITY): {
                graphView.addChartLabels(graphView.customizeChart(
                        new double[][] { gCalc.getLoanGearing() }, gCalc
                                .getLabels(),
                        new String[] { "<html>Loan (Gearing)</html>" },
                        new java.awt.Color[] { java.awt.Color.red },
                        new int[] { JCLineStyle.SHORT_DASH }, true), Currency
                        .getCurrencyInstance());
                graphView.setTitleAxisY1("Loan");
                graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                        .getInstance());

                graphView
                        .addChartLabels(
                                graphView
                                        .customizeChart(
                                                new double[][] {
                                                        gCalc
                                                                .getEquityGearing(),
                                                        gCalc
                                                                .getLoan2AssetsGearing() },
                                                gCalc.getLabels(),
                                                new String[] {
                                                        "<html>Equity <i>(Gearing)</i></html>",
                                                        "<html>Loan-to-Assets Ratio <i>(Gearing)</i></html>" },
                                                new java.awt.Color[] {
                                                        java.awt.Color.green
                                                                .darker(),
                                                        new java.awt.Color(0,
                                                                73, 147) },
                                                false), Percent
                                        .getPercentInstance());
                graphView.setTitleAxisY2("");
                graphView.setLabelGeneratorAxisY2(PercentLabelGenerator
                        .getInstance());

                break;
            }
            default:
                return;
            }

            // graphView.setComment( SnapshotComment.getComment( gCalc ) );

            // let user to go to chart
            updateComponents();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        buttonGroupSelection = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelDetails = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxGearingType = new javax.swing.JComboBox(new GearingType()
                .getCodeDescriptions());
        jTextFieldClientStartLSRRate = new javax.swing.JTextField();
        jTextFieldMaximumStartLSRRate = new javax.swing.JTextField();
        jTextFieldMarginCallRate = new javax.swing.JTextField();
        jTextFieldCreditLimitAmount = new javax.swing.JTextField();
        jTextFieldLoanInterestRate = new javax.swing.JTextField();
        jTextFieldRegularContributionAmount = new javax.swing.JTextField();
        jTextFieldInitialContributionAmount = new javax.swing.JTextField();
        jTextFieldEntryFeeRate = new javax.swing.JTextField();
        jTextFieldManagementFeeRate = new javax.swing.JTextField();
        jTextFieldClientTaxRate = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldYears = new javax.swing.JTextField();
        jPanelRight = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldClientInitialContribution = new javax.swing.JTextField();
        jTextFieldLoanInitialContribution = new javax.swing.JTextField();
        jTextFieldClientRegularContributionAmount = new javax.swing.JTextField();
        jTextFieldLoanRegularContributionAmount = new javax.swing.JTextField();
        jCheckBoxIndexed = new javax.swing.JCheckBox();
        jTextFieldIndexRate = new javax.swing.JTextField();
        jComboBoxInvestmentStrategy = new javax.swing.JComboBox(
                new InvestmentStrategyCode().getCodeDescriptions());
        jTextFieldCapitalGrowthRate = new javax.swing.JTextField();
        jTextFieldIncomeRate = new javax.swing.JTextField();
        jTextFieldTotalEarningRate = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextFieldFrankingCreditRate = new javax.swing.JTextField();
        jTextFieldTaxFreeDefferedRate = new javax.swing.JTextField();
        jTextFieldCapitalGainsRate = new javax.swing.JTextField();
        jPanelResult = new javax.swing.JPanel();
        jPanelGraph = new GearingGraphView();
        jPanelSelection = new javax.swing.JPanel();
        jRadioButtonAssets = new javax.swing.JRadioButton();
        jRadioButtonIncome = new javax.swing.JRadioButton();
        jRadioButtonEquityLoan = new javax.swing.JRadioButton();
        jPanelResultData = new javax.swing.JPanel();
        jPanelCapitalPosition = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextFieldTotalInvested = new javax.swing.JTextField();
        jTextFieldInvestmentValue = new javax.swing.JTextField();
        jTextFieldLoanAmount = new javax.swing.JTextField();
        jTextFieldNetWealthCreated = new javax.swing.JTextField();
        jTextFieldNetWealthNonGearing = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jPanelIncomeCashflow = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextFieldInvestmentIncome = new javax.swing.JTextField();
        jTextFieldInterestExpense = new javax.swing.JTextField();
        jTextFieldImputationCredits = new javax.swing.JTextField();
        jTextFieldTaxFreeIncome = new javax.swing.JTextField();
        jTextFieldCapitalGainTax = new javax.swing.JTextField();
        jTextFieldTaxPayable = new javax.swing.JTextField();
        jPanelControls = new javax.swing.JPanel();
        jPanelCloseSave = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jPanelPrevNext = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(600, 500));
        jTabbedPane.setPreferredSize(new java.awt.Dimension(600, 2000));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelDetails.setLayout(new javax.swing.BoxLayout(jPanelDetails,
                javax.swing.BoxLayout.X_AXIS));

        jPanelLeft.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        jPanelLeft.setBorder(new javax.swing.border.TitledBorder(
                "Gearing Conditions"));
        jLabel9.setText("Credit Limit");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel9, gridBagConstraints1);

        jLabel12.setText("Loan Interest Rate %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel12, gridBagConstraints1);

        jLabel23.setText("Gearing Type");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel23, gridBagConstraints1);

        jLabel14.setText("Entry Fees %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel14, gridBagConstraints1);

        jLabel15.setText("Management Fees %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel15, gridBagConstraints1);

        jLabel17.setText("ClientView Start LSR %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel17, gridBagConstraints1);

        jLabel18.setText("Maximum Start LSR %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel18, gridBagConstraints1);

        jLabel19.setText("Margin Call %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel19, gridBagConstraints1);

        jLabel8.setText("Total Initial Contribution");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel8, gridBagConstraints1);

        jLabel7.setText("Total Monthly Contribution");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel7, gridBagConstraints1);

        jComboBoxGearingType.setPreferredSize(new java.awt.Dimension(130, 20));
        jComboBoxGearingType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxGearingTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelLeft.add(jComboBoxGearingType, gridBagConstraints1);

        jTextFieldClientStartLSRRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientStartLSRRate.setPreferredSize(new java.awt.Dimension(
                50, 21));
        jTextFieldClientStartLSRRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldClientStartLSRRate, gridBagConstraints1);

        jTextFieldMaximumStartLSRRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMaximumStartLSRRate.setPreferredSize(new java.awt.Dimension(
                50, 21));
        jTextFieldMaximumStartLSRRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldMaximumStartLSRRate, gridBagConstraints1);

        jTextFieldMarginCallRate.setEditable(false);
        jTextFieldMarginCallRate.setBackground(java.awt.Color.lightGray);
        jTextFieldMarginCallRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMarginCallRate
                .setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldMarginCallRate, gridBagConstraints1);

        jTextFieldCreditLimitAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCreditLimitAmount.setPreferredSize(new java.awt.Dimension(
                120, 20));
        jTextFieldCreditLimitAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldCreditLimitAmount, gridBagConstraints1);

        jTextFieldLoanInterestRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoanInterestRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        jTextFieldLoanInterestRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldLoanInterestRate, gridBagConstraints1);

        jTextFieldRegularContributionAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRegularContributionAmount
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jTextFieldRegularContributionAmount
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft
                .add(jTextFieldRegularContributionAmount, gridBagConstraints1);

        jTextFieldInitialContributionAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInitialContributionAmount
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jTextFieldInitialContributionAmount
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft
                .add(jTextFieldInitialContributionAmount, gridBagConstraints1);

        jTextFieldEntryFeeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEntryFeeRate.setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldEntryFeeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldEntryFeeRate, gridBagConstraints1);

        jTextFieldManagementFeeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldManagementFeeRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        jTextFieldManagementFeeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldManagementFeeRate, gridBagConstraints1);

        jTextFieldClientTaxRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientTaxRate
                .setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldClientTaxRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldClientTaxRate, gridBagConstraints1);

        jLabel40.setText("ClientView Tax Rate %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel40, gridBagConstraints1);

        jLabel1.setText("Years to Project");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jLabel1, gridBagConstraints1);

        jTextFieldYears.setEditable(false);
        jTextFieldYears.setBackground(java.awt.Color.lightGray);
        jTextFieldYears.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldYears.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelLeft.add(jTextFieldYears, gridBagConstraints1);

        jPanelDetails.add(jPanelLeft);

        jPanelRight.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        jPanelRight.setBorder(new javax.swing.border.TitledBorder(
                "Investment Details"));
        jLabel13.setText("ClientView Initial Contribution");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel13, gridBagConstraints2);

        jLabel22.setText("Loan Initial Contribution");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel22, gridBagConstraints2);

        jLabel10.setText("ClientView Monthly Contribution");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel10, gridBagConstraints2);

        jLabel11.setText("Loan Monthly Contribution");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel11, gridBagConstraints2);

        jLabel24.setText("Investment Strategy");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel24, gridBagConstraints2);

        jLabel25.setText("Capital Growth %");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel25, gridBagConstraints2);

        jLabel26.setText("Income %");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 8;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel26, gridBagConstraints2);

        jLabel27.setText("Total Earnings %");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 9;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel27, gridBagConstraints2);

        jTextFieldClientInitialContribution.setEditable(false);
        jTextFieldClientInitialContribution
                .setBackground(java.awt.Color.lightGray);
        jTextFieldClientInitialContribution
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientInitialContribution
                .setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldClientInitialContribution,
                gridBagConstraints2);

        jTextFieldLoanInitialContribution.setEditable(false);
        jTextFieldLoanInitialContribution
                .setBackground(java.awt.Color.lightGray);
        jTextFieldLoanInitialContribution
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoanInitialContribution
                .setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldLoanInitialContribution, gridBagConstraints2);

        jTextFieldClientRegularContributionAmount.setEditable(false);
        jTextFieldClientRegularContributionAmount
                .setBackground(java.awt.Color.lightGray);
        jTextFieldClientRegularContributionAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientRegularContributionAmount
                .setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldClientRegularContributionAmount,
                gridBagConstraints2);

        jTextFieldLoanRegularContributionAmount.setEditable(false);
        jTextFieldLoanRegularContributionAmount
                .setBackground(java.awt.Color.lightGray);
        jTextFieldLoanRegularContributionAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoanRegularContributionAmount
                .setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldLoanRegularContributionAmount,
                gridBagConstraints2);

        jCheckBoxIndexed.setText("Index Rate");
        jCheckBoxIndexed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxIndexedItemStateChanged(evt);
            }
        });

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jCheckBoxIndexed, gridBagConstraints2);

        jTextFieldIndexRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIndexRate.setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldIndexRate
                .setInputVerifier(PercentInputVerifier.getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldIndexRate, gridBagConstraints2);

        jComboBoxInvestmentStrategy.setPreferredSize(new java.awt.Dimension(
                130, 20));
        jComboBoxInvestmentStrategy
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxInvestmentStrategyItemStateChanged(evt);
                    }
                });

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 6;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jComboBoxInvestmentStrategy, gridBagConstraints2);

        jTextFieldCapitalGrowthRate.setEditable(false);
        jTextFieldCapitalGrowthRate.setBackground(java.awt.Color.lightGray);
        jTextFieldCapitalGrowthRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCapitalGrowthRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 7;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldCapitalGrowthRate, gridBagConstraints2);

        jTextFieldIncomeRate.setEditable(false);
        jTextFieldIncomeRate.setBackground(java.awt.Color.lightGray);
        jTextFieldIncomeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIncomeRate.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 8;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldIncomeRate, gridBagConstraints2);

        jTextFieldTotalEarningRate.setEditable(false);
        jTextFieldTotalEarningRate.setBackground(java.awt.Color.lightGray);
        jTextFieldTotalEarningRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalEarningRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 9;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldTotalEarningRate, gridBagConstraints2);

        jLabel41.setText("Franking Credit %");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 10;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel41, gridBagConstraints2);

        jLabel42.setText("Tax Free/Deffered %");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 11;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel42, gridBagConstraints2);

        jLabel43.setText("Capital Gains %");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 12;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jLabel43, gridBagConstraints2);

        jTextFieldFrankingCreditRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldFrankingCreditRate.setPreferredSize(new java.awt.Dimension(
                50, 21));
        jTextFieldFrankingCreditRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 10;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldFrankingCreditRate, gridBagConstraints2);

        jTextFieldTaxFreeDefferedRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTaxFreeDefferedRate.setPreferredSize(new java.awt.Dimension(
                50, 21));
        jTextFieldTaxFreeDefferedRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 11;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldTaxFreeDefferedRate, gridBagConstraints2);

        jTextFieldCapitalGainsRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCapitalGainsRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        jTextFieldCapitalGainsRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 12;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelRight.add(jTextFieldCapitalGainsRate, gridBagConstraints2);

        jPanelDetails.add(jPanelRight);

        jTabbedPane.addTab("Gearing Details", jPanelDetails);

        jPanelResult.setLayout(new javax.swing.BoxLayout(jPanelResult,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelGraph.setBorder(new javax.swing.border.TitledBorder("Graph"));
        jPanelGraph.setPreferredSize(new java.awt.Dimension(20, 800));
        jPanelResult.add(jPanelGraph);

        jPanelSelection.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 20, 5));

        jRadioButtonAssets.setMnemonic(1);
        jRadioButtonAssets.setText("Assets");
        buttonGroupSelection.add(jRadioButtonAssets);
        jRadioButtonAssets.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonAssetsItemStateChanged(evt);
            }
        });

        jPanelSelection.add(jRadioButtonAssets);

        jRadioButtonIncome.setMnemonic(2);
        jRadioButtonIncome.setText("Income");
        buttonGroupSelection.add(jRadioButtonIncome);
        jRadioButtonIncome.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonAssetsItemStateChanged(evt);
            }
        });

        jPanelSelection.add(jRadioButtonIncome);

        jRadioButtonEquityLoan.setMnemonic(3);
        jRadioButtonEquityLoan.setText("Loan/Equity");
        buttonGroupSelection.add(jRadioButtonEquityLoan);
        jRadioButtonEquityLoan
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonAssetsItemStateChanged(evt);
                    }
                });

        jPanelSelection.add(jRadioButtonEquityLoan);

        jPanelResult.add(jPanelSelection);

        jPanelResultData.setLayout(new javax.swing.BoxLayout(jPanelResultData,
                javax.swing.BoxLayout.X_AXIS));

        jPanelCapitalPosition.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints3;

        jPanelCapitalPosition.setBorder(new javax.swing.border.TitledBorder(
                "Capital Position"));
        jLabel28.setText("Investment Value");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCapitalPosition.add(jLabel28, gridBagConstraints3);

        jLabel29.setText("Loan Amount");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCapitalPosition.add(jLabel29, gridBagConstraints3);

        jLabel30.setText("Net Wealth Non-Gearing");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCapitalPosition.add(jLabel30, gridBagConstraints3);

        jLabel31.setText("Total Invested");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCapitalPosition.add(jLabel31, gridBagConstraints3);

        jTextFieldTotalInvested.setEditable(false);
        jTextFieldTotalInvested.setBackground(java.awt.Color.lightGray);
        jTextFieldTotalInvested
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalInvested
                .setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelCapitalPosition.add(jTextFieldTotalInvested, gridBagConstraints3);

        jTextFieldInvestmentValue.setEditable(false);
        jTextFieldInvestmentValue.setBackground(java.awt.Color.lightGray);
        jTextFieldInvestmentValue
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelCapitalPosition.add(jTextFieldInvestmentValue,
                gridBagConstraints3);

        jTextFieldLoanAmount.setEditable(false);
        jTextFieldLoanAmount.setBackground(java.awt.Color.lightGray);
        jTextFieldLoanAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelCapitalPosition.add(jTextFieldLoanAmount, gridBagConstraints3);

        jTextFieldNetWealthCreated.setEditable(false);
        jTextFieldNetWealthCreated.setBackground(java.awt.Color.lightGray);
        jTextFieldNetWealthCreated
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelCapitalPosition.add(jTextFieldNetWealthCreated,
                gridBagConstraints3);

        jTextFieldNetWealthNonGearing.setEditable(false);
        jTextFieldNetWealthNonGearing.setBackground(java.awt.Color.lightGray);
        jTextFieldNetWealthNonGearing
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelCapitalPosition.add(jTextFieldNetWealthNonGearing,
                gridBagConstraints3);

        jLabel44.setText("Net Wealth Created");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCapitalPosition.add(jLabel44, gridBagConstraints3);

        jPanelResultData.add(jPanelCapitalPosition);

        jPanelIncomeCashflow.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints4;

        jPanelIncomeCashflow.setBorder(new javax.swing.border.TitledBorder(
                "Income & Cashflow"));
        jLabel33.setText("Investment Income");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelIncomeCashflow.add(jLabel33, gridBagConstraints4);

        jLabel34.setText("Interest Expense");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelIncomeCashflow.add(jLabel34, gridBagConstraints4);

        jLabel35.setText("Imputation Credits");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelIncomeCashflow.add(jLabel35, gridBagConstraints4);

        jLabel36.setText("Tax Free Income");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelIncomeCashflow.add(jLabel36, gridBagConstraints4);

        jLabel37.setText("Tax Payable");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 5;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelIncomeCashflow.add(jLabel37, gridBagConstraints4);

        jLabel38.setText("Capital Gains Tax");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelIncomeCashflow.add(jLabel38, gridBagConstraints4);

        jTextFieldInvestmentIncome.setEditable(false);
        jTextFieldInvestmentIncome.setBackground(java.awt.Color.lightGray);
        jTextFieldInvestmentIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInvestmentIncome.setPreferredSize(new java.awt.Dimension(120,
                20));
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelIncomeCashflow.add(jTextFieldInvestmentIncome,
                gridBagConstraints4);

        jTextFieldInterestExpense.setEditable(false);
        jTextFieldInterestExpense.setBackground(java.awt.Color.lightGray);
        jTextFieldInterestExpense
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelIncomeCashflow
                .add(jTextFieldInterestExpense, gridBagConstraints4);

        jTextFieldImputationCredits.setEditable(false);
        jTextFieldImputationCredits.setBackground(java.awt.Color.lightGray);
        jTextFieldImputationCredits
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelIncomeCashflow.add(jTextFieldImputationCredits,
                gridBagConstraints4);

        jTextFieldTaxFreeIncome.setEditable(false);
        jTextFieldTaxFreeIncome.setBackground(java.awt.Color.lightGray);
        jTextFieldTaxFreeIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelIncomeCashflow.add(jTextFieldTaxFreeIncome, gridBagConstraints4);

        jTextFieldCapitalGainTax.setEditable(false);
        jTextFieldCapitalGainTax.setBackground(java.awt.Color.lightGray);
        jTextFieldCapitalGainTax
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelIncomeCashflow.add(jTextFieldCapitalGainTax, gridBagConstraints4);

        jTextFieldTaxPayable.setEditable(false);
        jTextFieldTaxPayable.setBackground(java.awt.Color.lightGray);
        jTextFieldTaxPayable
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 5;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelIncomeCashflow.add(jTextFieldTaxPayable, gridBagConstraints4);

        jPanelResultData.add(jPanelIncomeCashflow);

        jPanelResult.add(jPanelResultData);

        jTabbedPane.addTab("Projections", jPanelResult);

        add(jTabbedPane);

        jPanelControls.setLayout(new javax.swing.BoxLayout(jPanelControls,
                javax.swing.BoxLayout.X_AXIS));

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSave);

        jPanelControls.add(jPanelCloseSave);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.setDefaultCapable(false);
        jButtonPrevious.setEnabled(false);
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanelPrevNext.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.setDefaultCapable(false);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanelPrevNext.add(jButtonNext);

        jPanelControls.add(jPanelPrevNext);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);
    }// GEN-LAST:event_jTabbedPaneStateChanged

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        // Add your handling code here:
        saveView();
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        // Add your handling code here:
        SwingUtil.setVisible(this, false);

    }// GEN-LAST:event_jButtonCloseActionPerformed

    /*
     * private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {
     * saveView(); }
     */
    private void jRadioButtonAssetsItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonAssetsItemStateChanged
        int selection = ((AbstractButton) evt.getSource()).getMnemonic();
        updateChart(selection);
    }// GEN-LAST:event_jRadioButtonAssetsItemStateChanged

    private void jComboBoxInvestmentStrategyItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxInvestmentStrategyItemStateChanged
        if (evt.getSource() != jComboBoxInvestmentStrategy)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            gCalc.setInvestmentStrategyCodeID(null);
        else {
            gCalc.setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                    .getCodeID(s));
        }
    }// GEN-LAST:event_jComboBoxInvestmentStrategyItemStateChanged

    private void jCheckBoxIndexedItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxIndexedItemStateChanged
        if (evt.getSource() != jCheckBoxIndexed)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        gCalc
                .setIndexed(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED);
        SwingUtil.setEnabled(jTextFieldIndexRate, gCalc.isIndexed());
    }// GEN-LAST:event_jCheckBoxIndexedItemStateChanged

    private void jComboBoxGearingTypeItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxGearingTypeItemStateChanged
        if (evt.getSource() != jComboBoxGearingType)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            gCalc.setGearingTypeID(null);
        else
            gCalc.setGearingTypeID(new GearingType().getCodeID(s));
    }// GEN-LAST:event_jComboBoxGearingTypeItemStateChanged

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    // Singleton pattern
    private static GearingView view;

    public static GearingView display(String title,
            java.awt.event.FocusListener[] listeners) {

        if (view == null) {
            view = new GearingView();
            SwingUtil.add2Frame(view, listeners, view.getDefaultTitle(),
                    ViewSettings.getInstance().getViewImage(
                            view.getClass().getName()), true, true, false);
        }

        try {
            view.updateView(title);
            SwingUtil.setVisible(view, true);
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            view = null;
        }

        return view;

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSelection;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JPanel jPanelDetails;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JComboBox jComboBoxGearingType;

    private javax.swing.JTextField jTextFieldClientStartLSRRate;

    private javax.swing.JTextField jTextFieldMaximumStartLSRRate;

    private javax.swing.JTextField jTextFieldMarginCallRate;

    private javax.swing.JTextField jTextFieldCreditLimitAmount;

    private javax.swing.JTextField jTextFieldLoanInterestRate;

    private javax.swing.JTextField jTextFieldRegularContributionAmount;

    private javax.swing.JTextField jTextFieldInitialContributionAmount;

    private javax.swing.JTextField jTextFieldEntryFeeRate;

    private javax.swing.JTextField jTextFieldManagementFeeRate;

    private javax.swing.JTextField jTextFieldClientTaxRate;

    private javax.swing.JLabel jLabel40;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField jTextFieldYears;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JTextField jTextFieldClientInitialContribution;

    private javax.swing.JTextField jTextFieldLoanInitialContribution;

    private javax.swing.JTextField jTextFieldClientRegularContributionAmount;

    private javax.swing.JTextField jTextFieldLoanRegularContributionAmount;

    private javax.swing.JCheckBox jCheckBoxIndexed;

    private javax.swing.JTextField jTextFieldIndexRate;

    private javax.swing.JComboBox jComboBoxInvestmentStrategy;

    private javax.swing.JTextField jTextFieldCapitalGrowthRate;

    private javax.swing.JTextField jTextFieldIncomeRate;

    private javax.swing.JTextField jTextFieldTotalEarningRate;

    private javax.swing.JLabel jLabel41;

    private javax.swing.JLabel jLabel42;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JTextField jTextFieldFrankingCreditRate;

    private javax.swing.JTextField jTextFieldTaxFreeDefferedRate;

    private javax.swing.JTextField jTextFieldCapitalGainsRate;

    private javax.swing.JPanel jPanelResult;

    private javax.swing.JPanel jPanelGraph;

    private javax.swing.JPanel jPanelSelection;

    private javax.swing.JRadioButton jRadioButtonAssets;

    private javax.swing.JRadioButton jRadioButtonIncome;

    private javax.swing.JRadioButton jRadioButtonEquityLoan;

    private javax.swing.JPanel jPanelResultData;

    private javax.swing.JPanel jPanelCapitalPosition;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel30;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JTextField jTextFieldTotalInvested;

    private javax.swing.JTextField jTextFieldInvestmentValue;

    private javax.swing.JTextField jTextFieldLoanAmount;

    private javax.swing.JTextField jTextFieldNetWealthCreated;

    private javax.swing.JTextField jTextFieldNetWealthNonGearing;

    private javax.swing.JLabel jLabel44;

    private javax.swing.JPanel jPanelIncomeCashflow;

    private javax.swing.JLabel jLabel33;

    private javax.swing.JLabel jLabel34;

    private javax.swing.JLabel jLabel35;

    private javax.swing.JLabel jLabel36;

    private javax.swing.JLabel jLabel37;

    private javax.swing.JLabel jLabel38;

    private javax.swing.JTextField jTextFieldInvestmentIncome;

    private javax.swing.JTextField jTextFieldInterestExpense;

    private javax.swing.JTextField jTextFieldImputationCredits;

    private javax.swing.JTextField jTextFieldTaxFreeIncome;

    private javax.swing.JTextField jTextFieldCapitalGainTax;

    private javax.swing.JTextField jTextFieldTaxPayable;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JPanel jPanelCloseSave;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JPanel jPanelPrevNext;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.JButton jButtonNext;

    // End of variables declaration//GEN-END:variables

    /**
     * Viewble interface
     */
    public Integer getObjectType() {
        return null;
    }

    public void updateView(String modelTitle) throws java.io.IOException {

        PersonService person = clientService;
        Model m = person == null ? null : person.getModel(getDefaultType(),
                modelTitle);

        if (m == null) {
            updateView(person);
        } else {
            updateView(m);
        }
        setTitle();

    }

    public void updateView(Model newModel) throws java.io.IOException {

        // saveView();

        try {
            gCalc.disableUpdate();

            clearView();

            gCalc.setModel(newModel);
            gCalc.setSaved();
        } finally {
            updateEditable();
            gCalc.doUpdate();
            gCalc.enableUpdate();
        }

    }

    public void updateView(com.argus.financials.service.PersonService person)
            throws ServiceException {

        // saveView();

        if (person == null)
            return;

        try {
            gCalc.disableUpdate();

            clearView();

            // load data from person, e.g. DOB

        } finally {
            updateEditable();
            gCalc.doUpdate();
            gCalc.enableUpdate();
        }

    }

    /*
     * public void saveView(com.argus.server.Person person) throws
     * com.argus.financials.service.ServiceException, com.argus.code.InvalidCodeException {
     *  }
     */

    private void saveView() {

        String oldTitle = getTitle();
        try {
            int result = SaveProjectionDialog.getSaveProjectionInstance().save(
                    gCalc, this);
            if (result == SaveProjectionDialog.CANCEL_OPTION)
                return;
            setTitle();
        } catch (DuplicateException e) {
            String newTitle = getTitle();

            String msg = "Failed to save model.\n  Current Title: " + oldTitle
                    + "\n  New Title: " + newTitle;
            JOptionPane.showMessageDialog(this, msg,
                    "Title already used by another model",
                    JOptionPane.ERROR_MESSAGE);

            e.printStackTrace(System.err); // ( msg + '\n' + e.getMessage() );
            return;
        } catch (ModelTitleRestrictionException me) {
            String msg = "Failed to save model.\n  Please ensure total characters for model title is 3 or more.";
            JOptionPane.showMessageDialog(this, msg, "Invalid title",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            saveView(clientService);
        } catch (Exception e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            return;
        }

    }

    public void saveView(PersonService person) throws ServiceException,
            InvalidCodeException {

        java.io.StringWriter w = new java.io.StringWriter();
        java.io.BufferedWriter output = new java.io.BufferedWriter(w);

        try {
            IOUtils2.writeHeader(this, output);
            IOUtils2.write(this, output);

            output.flush();

            getModel().setData(w.toString());

        } catch (java.io.IOException e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (java.io.IOException e) { /* ignore by now */
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (java.io.IOException e) { /* ignore by now */
                }
            }
        }

        person.addModel(getModel());

        person.storeModels();
        gCalc.setSaved();

    }

    public void clearView() {
        SwingUtil.clear(this);

        gCalc.setModel(null);
        setTitle();
    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        GearingCalc g = (GearingCalc) changeEvent.getSource();

        if (gCalc != g)
            throw new RuntimeException("!!!!! BUG !!!!! (apCalc != g)");

        // if ( gCalc.isReady() ) // or modified
        updateNonEditable();
    }

    /**
     * helper methods
     */
    public void updateEditable() {

        if (gCalc == null)
            return;

        double d;
        String s;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        updateOthers();

        d = gCalc.getClientStartLSR();
        jTextFieldClientStartLSRRate
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                        .toString(d));

        d = gCalc.getMaxStartLSR();
        jTextFieldMaximumStartLSRRate
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                        .toString(d));

        d = gCalc.getCreditLimit();
        jTextFieldCreditLimitAmount.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = gCalc.getLoanInterestRate();
        jTextFieldLoanInterestRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = gCalc.getRegularContributionAmount();
        jTextFieldRegularContributionAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = gCalc.getInitialContributionAmount();
        jTextFieldInitialContributionAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = gCalc.getEntryFeeRate();
        jTextFieldEntryFeeRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = gCalc.getRevisionFeeRate();
        jTextFieldManagementFeeRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = gCalc.getTaxRate();
        jTextFieldClientTaxRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = gCalc.getIndexRate();
        jTextFieldIndexRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = gCalc.getFrankedIncomeRate();
        jTextFieldFrankingCreditRate
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                        .toString(d));

        d = gCalc.getTaxFreeDeferredRate();
        jTextFieldTaxFreeDefferedRate
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                        .toString(d));

        d = gCalc.getCapitalGainsRate();
        jTextFieldCapitalGainsRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));
    }

    protected void updateOthers() {
        updateComponents();

        jComboBoxGearingType.setSelectedItem(gCalc.getGearingTypeDesc());

        jCheckBoxIndexed.setSelected(gCalc.isIndexed());
        SwingUtil.setEnabled(jTextFieldIndexRate, gCalc.isIndexed());

        jComboBoxInvestmentStrategy.setSelectedItem(gCalc
                .getInvestmentStrategyCodeDesc());
    }

    public void updateNonEditable() {

        String s = null;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        updateOthers();

        s = percent.toString(gCalc.getMarginCall());
        jTextFieldMarginCallRate.setText(s);

        double years = (int) (gCalc.getYears() * 100) / 100.;
        jTextFieldYears.setText(years == MoneyCalc.UNKNOWN_VALUE ? null : ""
                + years);

        s = curr.toString(gCalc.getInitialInvestmentClient());
        jTextFieldClientInitialContribution.setText(s);

        s = curr.toString(gCalc.getInitialInvestmentLoan());
        jTextFieldLoanInitialContribution.setText(s);

        s = curr.toString(gCalc.getRegularContributionNonGearing());
        jTextFieldClientRegularContributionAmount.setText(s);

        s = curr.toString(gCalc.getRegularContributionGearing());
        jTextFieldLoanRegularContributionAmount.setText(s);

        s = percent.toString(gCalc.getAnnualCapitalGrowthRate());
        jTextFieldCapitalGrowthRate.setText(s);

        s = percent.toString(gCalc.getAnnualIncomeRate());
        jTextFieldIncomeRate.setText(s);

        s = percent.toString(gCalc.getTotalAnnualReturnRate());
        jTextFieldTotalEarningRate.setText(s);

        s = curr.toString(gCalc.getTotalContributionGearing());
        jTextFieldTotalInvested.setText(s);

        s = curr.toString(gCalc.getAccountBalanceGearing());
        jTextFieldInvestmentValue.setText(s);

        s = curr.toString(gCalc.getLoanBalanceGearing());
        jTextFieldLoanAmount.setText(s);

        s = curr.toString(gCalc.getNetCapitalPositionGearing());
        jTextFieldNetWealthCreated.setText(s);

        s = curr.toString(gCalc.getNetCapitalPositionNonGearing());
        jTextFieldNetWealthNonGearing.setText(s);

        s = curr.toString(gCalc.getTotalInvestmentIncomeGearing());
        jTextFieldInvestmentIncome.setText(s);

        s = curr.toString(gCalc.getTotalLoanInterestGearing());
        jTextFieldInterestExpense.setText(s);

        s = curr.toString(gCalc.getTotalImputationCreditsGearing());
        jTextFieldImputationCredits.setText(s);

        s = curr.toString(gCalc.getTotalTaxFreeDistributionGearing());
        jTextFieldTaxFreeIncome.setText(s);

        s = curr.toString(gCalc.getCapitalGainsTaxGearing());
        jTextFieldCapitalGainTax.setText(s);

        s = curr.toString(gCalc.getTotalTaxPayableGearing());
        jTextFieldTaxPayable.setText(s);

        if (buttonGroupSelection.getSelection() != null)
            updateChart(buttonGroupSelection.getSelection().getMnemonic());

    }

    /**
     * 
     */
    protected void setActionMap() {

        ActionMap am = getActionMap();
        if (am == null) {
            am = new ActionMap();
            this.setActionMap(am);
        }

        am.put(DATA_ADD, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveView();
                clearView();
            }
        });
        am.put(DATA_REMOVE, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                PersonService person = clientService;
                if (person == null)
                    return;

                // remove from menu
                FinancialPlannerApp.getInstance().updateModels();

                try {
                    person.removeModel(getModel());
                    person.storeModels();
                } catch (com.argus.financials.api.ServiceException e) {
                    e.printStackTrace();
                    return;
                }

                clearView();

            }
        });
        /*
         * am.put( DATA_UPDATE, new AbstractAction() { public void
         * actionPerformed(java.awt.event.ActionEvent evt) {
         *  } } );
         */
    }

}
