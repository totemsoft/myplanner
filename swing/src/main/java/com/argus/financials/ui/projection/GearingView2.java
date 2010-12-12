/*
 * GearingView.java
 *
 * Created on 12 December 2001, 16:50
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 */

import java.awt.Cursor;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

import com.argus.beans.format.CurrencyLabelGenerator;
import com.argus.beans.format.PercentLabelGenerator;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.GearingType;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.io.IOUtils2;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.GearingCalc2;
import com.argus.financials.projection.GearingGraphView;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceException;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.NumberInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.financials.ui.ListenerUtils;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;
import com.argus.swing.SwingUtils;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public class GearingView2 extends javax.swing.JPanel implements ActionEventID,
        javax.swing.event.ChangeListener, com.argus.financials.swing.ICloseDialog {

    private static final int RESULT_PAGE_ID = 1;

    // public static final double HOLE = MoneyCalc.HOLE;

    public static final int INVESTMENT_VALUE = 1;

    public static final int POTENTIAL_OUTCOME = 2;

    public static final int CAPITAL_GROWTH_RATES = 3;

    private boolean newModel;

    private Number2 number = MoneyCalc.getNumberInstance();

    private Percent percent = MoneyCalc.getPercentInstance();

    private Currency curr = MoneyCalc.getCurrencyInstance();

    private GearingGraphView graphView;

    private GearingCalc2 gCalc;

    /** Creates new form GearingView */
    public GearingView2() {
        this(new GearingCalc2());
    }

    public GearingView2(MoneyCalc calc) {
        gCalc = (GearingCalc2) calc;

        initComponents();
        initComponents2();
    }

    public Integer getDefaultType() {
        return ModelTypeID.rcINVESTMENT_GEARING.getCodeIDInteger();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcINVESTMENT_GEARING.getCodeDesc();
    }

    public String getDateTimeFormat() {
        return jRadioButtonMonthly.isSelected() ? "dd MMM yyyy" : "yyyy";
    }

    private Integer getTableFrequencyCodeID() {
        return jRadioButtonMonthly.isSelected() ? FrequencyCode.MONTHLY
                : FrequencyCode.YEARLY;
    }

    private Model getModel() {
        Model model = gCalc.getModel();
        if (model.getOwner() != null)
            return model;

        PersonService person = ServiceLocator.getInstance().getClientPerson();
        if (person != null) {
            try {
                model.setOwner(person.getModels());
            } catch (com.argus.financials.service.ServiceException e) {
                e.printStackTrace(System.err);
            }
        }
        return model;
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void updateTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    private void initComponents2() {
        // jPanelGraphSelection.setVisible(false);

        graphView = (GearingGraphView) jPanelGraph;
        SwingUtils.setDefaultFont(graphView);

        _setAccessibleContext();

        gCalc.addChangeListener(this);
        ListenerUtils.addListener(this, gCalc); // after _setAccessibleContext()
        updateEditable();

        jButtonSave
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonSaveAs.setEnabled(jButtonSave.isEnabled());
        jButtonDelete.setEnabled(jButtonSave.isEnabled());
        setActionMap();

        //
        setTableModel();

    }

    private void _setAccessibleContext() {

        jComboBoxGearingType.getAccessibleContext().setAccessibleName(
                DocumentNames.GEARING_TYPE);

        jTextFieldInvestorDepositAmount.getAccessibleContext()
                .setAccessibleName(DocumentNames.INITIAL_INVESTOR);
        jTextFieldInitialLoanAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.INITIAL_LOAN);

        jTextFieldRegularInvestorContributionAmount.getAccessibleContext()
                .setAccessibleName(DocumentNames.REGULAR_INVESTOR_CONTRIBUTION);
        jTextFieldRegularLoanAdvanceAmount.getAccessibleContext()
                .setAccessibleName(DocumentNames.REGULAR_LOAN_ADVANCE);

        jTextFieldStartDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.START_DATE);
        jTextFieldEndDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.END_DATE);
        jTextFieldYears2Project.getAccessibleContext().setAccessibleName(
                DocumentNames.YEARS_PROJECT);
        jComboBoxReinvestIncome.getAccessibleContext().setAccessibleName(
                DocumentNames.REINVEST_INCOME);

        jTextFieldCreditLimitAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.CREDIT_LIMIT_AMOUNT);
        jTextFieldLoanInterestRate.getAccessibleContext().setAccessibleName(
                DocumentNames.LOAN_INTEREST_RATE);
        jComboBoxAddInterest.getAccessibleContext().setAccessibleName(
                DocumentNames.ADD_INTEREST);

        jComboBoxInvestmentStrategy.getAccessibleContext().setAccessibleName(
                DocumentNames.INV_STRATEGY);
        // if InvestmentStrategy == null this fields are editable
        jTextFieldCapitalGrowthRate.getAccessibleContext().setAccessibleName(
                DocumentNames.CAPITAL_GROWTH_RATE);
        jTextFieldIncomeRate.getAccessibleContext().setAccessibleName(
                DocumentNames.INCOME_RATE);

        jComboBoxIncomeFrequency.getAccessibleContext().setAccessibleName(
                DocumentNames.INCOME_FREQUENCY);

        jTextFieldFrankingCreditRate.getAccessibleContext().setAccessibleName(
                DocumentNames.FRANKING_CREDIT_RATE);
        jTextFieldTaxFreeDefferedRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_FREE_DEFFERED_RATE);
        jTextFieldCapitalGainsRate.getAccessibleContext().setAccessibleName(
                DocumentNames.CAPITAL_GAINS_RATE);

        jTextFieldEntryFeeRate.getAccessibleContext().setAccessibleName(
                DocumentNames.ENTRY_FEES);
        jTextFieldPortfolioReviewFeeRate.getAccessibleContext()
                .setAccessibleName(DocumentNames.REVIEW_FEES);

        jTextFieldOtherTaxableIncome.getAccessibleContext().setAccessibleName(
                DocumentNames.OTHER_TAXABLE_INCOME);

        jRadioButtonFlatCGR.getAccessibleContext().setAccessibleName(
                DocumentNames.FLAT_CAPITAL_GROWTH_RATES);
        jRadioButtonVariableCGR.getAccessibleContext().setAccessibleName(
                DocumentNames.VARIABLE_CAPITAL_GROWTH_RATES);

        jRadioButtonInvestmentValue.getAccessibleContext().setAccessibleName(
                DocumentNames.INVESTMENT_VALUE);
        jRadioButtonPotentialOutcome.getAccessibleContext().setAccessibleName(
                DocumentNames.POTENTIAL_OUTCOME);
        jRadioButtonCapitalGrowthRates.getAccessibleContext()
                .setAccessibleName(DocumentNames.CAPITAL_GROWTH_RATES);

        jRadioButtonSummary.getAccessibleContext().setAccessibleName(
                DocumentNames.INVESTMENT_SUMMARY);
        jRadioButtonAnnual.getAccessibleContext().setAccessibleName(
                DocumentNames.ANNUAL_CASHFLOW);
        jRadioButtonMonthly.getAccessibleContext().setAccessibleName(
                DocumentNames.MONTHLY_CASHFLOW);

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

        jButtonReport.setEnabled(add);
        jButtonNext.setEnabled(add && jTabbedPane.getSelectedIndex() == 0);
        jButtonSave
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonSaveAs
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
    }

    private void updateChart() {
        if (buttonGroupSelection.getSelection() == null)
            updateChart(-1);
        else
            updateChart(buttonGroupSelection.getSelection().getMnemonic());
    }

    private void updateChart(int selection) {

        if (gCalc == null || !gCalc.isReady())
            return;
        // gCalc.calculate();

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            graphView.removeChartLabels();
            graphView.setDateTimeFormat(getDateTimeFormat());

            Integer frequencyCodeID = FrequencyCode.YEARLY;// getChartFrequencyCodeID();

            // JCLineStyle.NONE, JCLineStyle.SOLID, JCLineStyle.LONG_DASH,
            // JCLineStyle.SHORT_DASH, JCLineStyle.LSL_DASH,
            // JCLineStyle.DASH_DOT
            switch (selection) {
            case (INVESTMENT_VALUE):
                graphView
                        .addChartLabels(
                                graphView
                                        .customizeChart(
                                                new double[][] {
                                                        gCalc
                                                                .getTotalInvested(true),
                                                        gCalc
                                                                .getTotalInvested(false), },
                                                gCalc.getDates(frequencyCodeID),
                                                new String[] {
                                                        "<html>"
                                                                + gCalc
                                                                        .getSummaryColumnNames()[GearingCalc2.S_INVESTMENT]
                                                                + "<i>(Gearing)</i></html>", // 
                                                        "<html>"
                                                                + gCalc
                                                                        .getSummaryColumnNames()[GearingCalc2.S_INVESTMENT]
                                                                + "<i>(Non-Gearing)</i></html>", // 
                                                },
                                                new java.awt.Color[] {
                                                        GearingCalc2.GEARING,
                                                        GearingCalc2.NON_GEARING, },
                                                null, // int [] linePaterns
                                                true, // boolean leftAxisY
                                                false // boolean inverted
                                        ), Currency.getCurrencyInstance());
                graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                        .getInstance());
                graphView.setTitleAxisY1("");

                // remove second data view
                graphView.setTitleAxisY2(null);

                graphView.setTitleAxisX("");

                break;

            case (POTENTIAL_OUTCOME):
                graphView
                        .addChartLabels(
                                graphView
                                        .customizeChart(
                                                new double[][] {
                                                        gCalc
                                                                .getPotentialOutcome(true),
                                                        gCalc
                                                                .getPotentialOutcome(false), },
                                                gCalc.getDates(frequencyCodeID),
                                                new String[] {
                                                        "<html>"
                                                                + gCalc
                                                                        .getSummaryColumnNames()[GearingCalc2.S_POTENTIAL_OUTCOME_GEARED]
                                                                + "<i>(Gearing)</i></html>",
                                                        "<html>"
                                                                + gCalc
                                                                        .getSummaryColumnNames()[GearingCalc2.S_POTENTIAL_OUTCOME_GEARED]
                                                                + "<i>(Non-Gearing)</i></html>", },
                                                new java.awt.Color[] {
                                                        GearingCalc2.GEARING,
                                                        GearingCalc2.NON_GEARING, },
                                                null, // int [] linePaterns
                                                true, // boolean leftAxisY
                                                false // boolean inverted
                                        ), Currency.getCurrencyInstance());
                graphView.setTitleAxisY1("");
                graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                        .getInstance());

                // remove second data view
                graphView.setTitleAxisY2(null);

                graphView.setTitleAxisX("");

                break;

            case (CAPITAL_GROWTH_RATES):
                graphView.addChartLabels(graphView.customizeChart(
                        new double[][] { gCalc.getAnnualCapitalGrowthRates() },
                        gCalc.getDates(frequencyCodeID),
                        new String[] { "<html>Capital Growth Rates</html>" },
                        new java.awt.Color[] { java.awt.Color.red }, null, // int
                                                                            // []
                                                                            // linePaterns
                        true, // boolean leftAxisY
                        false // boolean inverted
                        ), Percent.getPercentInstance());
                graphView.setLabelGeneratorAxisY1(PercentLabelGenerator
                        .getInstance());
                graphView.setTitleAxisY1("");

                // remove second data view
                graphView.setTitleAxisY2(null);

                graphView.setTitleAxisX("");

                break;

            default:
                return;

            }

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
        buttonGroupSummaryType = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelDetails = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jPanelContributions = new javax.swing.JPanel();
        jPanelContributionsLabels = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanelContributionsValues = new javax.swing.JPanel();
        jComboBoxGearingType = new javax.swing.JComboBox(new GearingType()
                .getCodeDescriptions());
        jTextFieldInvestorDepositAmount = new javax.swing.JTextField();
        jTextFieldInitialLoanAmount = new javax.swing.JTextField();
        jTextFieldRegularInvestorContributionAmount = new javax.swing.JTextField();
        jTextFieldRegularLoanAdvanceAmount = new javax.swing.JTextField();
        jTextFieldStartDate = new com.argus.beans.FDateChooser();
        jTextFieldEndDate = new com.argus.beans.FDateChooser();
        jTextFieldYears2Project = new javax.swing.JTextField();
        jComboBoxReinvestIncome = new javax.swing.JComboBox(new BooleanCode()
                .getCodes().toArray());
        jPanelLoan = new javax.swing.JPanel();
        jPanelLoanLabels = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanelLoanValues = new javax.swing.JPanel();
        jTextFieldCreditLimitAmount = new javax.swing.JTextField();
        jTextFieldLoanInterestRate = new javax.swing.JTextField();
        jComboBoxAddInterest = new javax.swing.JComboBox(new BooleanCode()
                .getCodes().toArray());
        jPanelDummyLeft = new javax.swing.JPanel();
        jPanelRight = new javax.swing.JPanel();
        jPanelInvestment = new javax.swing.JPanel();
        jPanelInvestmentLabels = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanelInvestmentValues = new javax.swing.JPanel();
        jComboBoxInvestmentStrategy = new javax.swing.JComboBox(
                new InvestmentStrategyCode().getCodeDescriptions());
        jTextFieldCapitalGrowthRate = new javax.swing.JTextField();
        jTextFieldIncomeRate = new javax.swing.JTextField();
        jTextFieldTotalEarningRate = new javax.swing.JTextField();
        jComboBoxIncomeFrequency = new javax.swing.JComboBox();
        jPanelCapitalGrowthRates = new javax.swing.JPanel();
        jRadioButtonFlatCGR = new javax.swing.JRadioButton();
        jRadioButtonVariableCGR = new javax.swing.JRadioButton();
        jPanelTax = new javax.swing.JPanel();
        jPanelTaxLabels = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanelTaxValues = new javax.swing.JPanel();
        jTextFieldFrankingCreditRate = new javax.swing.JTextField();
        jTextFieldTaxFreeDefferedRate = new javax.swing.JTextField();
        jTextFieldCapitalGainsRate = new javax.swing.JTextField();
        jPanelCharges = new javax.swing.JPanel();
        jPanelChargesLabels = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanelChargesValues = new javax.swing.JPanel();
        jTextFieldEntryFeeRate = new javax.swing.JTextField();
        jTextFieldPortfolioReviewFeeRate = new javax.swing.JTextField();
        jPanelOthers = new javax.swing.JPanel();
        jPanelOthersLabels = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanelOthersData = new javax.swing.JPanel();
        jTextFieldOtherTaxableIncome = new javax.swing.JTextField();
        jPanelDummyRight = new javax.swing.JPanel();
        jPanelResult = new javax.swing.JPanel();
        jSplitPane = new javax.swing.JSplitPane();
        jPanelGraphOptions = new javax.swing.JPanel();
        jPanelGraph = new GearingGraphView();
        jPanelOptions = new javax.swing.JPanel();
        jPanelGraphSelection = new javax.swing.JPanel();
        jRadioButtonInvestmentValue = new javax.swing.JRadioButton();
        jRadioButtonPotentialOutcome = new javax.swing.JRadioButton();
        jRadioButtonCapitalGrowthRates = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jPanelSummaryType = new javax.swing.JPanel();
        jRadioButtonSummary = new javax.swing.JRadioButton();
        jRadioButtonAnnual = new javax.swing.JRadioButton();
        jRadioButtonMonthly = new javax.swing.JRadioButton();
        jScrollPaneData = new javax.swing.JScrollPane();
        jTableData = new javax.swing.JTable();
        jPanelControls = new javax.swing.JPanel();
        jPanelCloseSave = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPanelPrevNext = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(750, 500));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelDetails.setLayout(new java.awt.GridLayout(1, 2));

        jPanelLeft.setLayout(new javax.swing.BoxLayout(jPanelLeft,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelLeft.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(5, 10, 5, 0)));
        jPanelContributions.setLayout(new java.awt.GridLayout(1, 2));

        jPanelContributions.setBorder(new javax.swing.border.TitledBorder(
                "Contributions"));
        jPanelContributions.setPreferredSize(new java.awt.Dimension(10, 243));
        jPanelContributions.setMinimumSize(new java.awt.Dimension(10, 243));
        jPanelContributions.setMaximumSize(new java.awt.Dimension(32767, 243));
        jPanelContributionsLabels
                .setLayout(new java.awt.GridLayout(9, 1, 0, 3));

        jPanelContributionsLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jPanelContributionsLabels.setPreferredSize(new java.awt.Dimension(180,
                149));
        jLabel23.setText("Gearing Type");
        jPanelContributionsLabels.add(jLabel23);

        jLabel16.setText("Investor Deposit");
        jPanelContributionsLabels.add(jLabel16);

        jLabel22.setText("Initial Loan");
        jPanelContributionsLabels.add(jLabel22);

        jLabel8.setText("Monthly Investor Contribution");
        jPanelContributionsLabels.add(jLabel8);

        jLabel7.setText("Monthly Loan Advance");
        jPanelContributionsLabels.add(jLabel7);

        jLabel5.setText("Contribute from Date ");
        jPanelContributionsLabels.add(jLabel5);

        jLabel1.setText("Contribute till Date ");
        jPanelContributionsLabels.add(jLabel1);

        jLabel3.setText("Years to Project");
        jPanelContributionsLabels.add(jLabel3);

        jLabel20.setText("Reinvest Income");
        jPanelContributionsLabels.add(jLabel20);

        jPanelContributions.add(jPanelContributionsLabels);

        jPanelContributionsValues
                .setLayout(new java.awt.GridLayout(9, 1, 0, 3));

        jPanelContributionsValues.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 5)));
        jPanelContributionsValues.setPreferredSize(new java.awt.Dimension(136,
                682));
        jPanelContributionsValues.setMinimumSize(new java.awt.Dimension(132,
                682));
        jComboBoxGearingType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxGearingTypeItemStateChanged(evt);
            }
        });

        jPanelContributionsValues.add(jComboBoxGearingType);

        jTextFieldInvestorDepositAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInvestorDepositAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanelContributionsValues.add(jTextFieldInvestorDepositAmount);

        jTextFieldInitialLoanAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInitialLoanAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanelContributionsValues.add(jTextFieldInitialLoanAmount);

        jTextFieldRegularInvestorContributionAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRegularInvestorContributionAmount
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        jPanelContributionsValues
                .add(jTextFieldRegularInvestorContributionAmount);

        jTextFieldRegularLoanAdvanceAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRegularLoanAdvanceAmount
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        jPanelContributionsValues.add(jTextFieldRegularLoanAdvanceAmount);

        jTextFieldStartDate.setInputVerifier(DateInputVerifier.getInstance());
        jPanelContributionsValues.add(jTextFieldStartDate);

        jTextFieldEndDate.setInputVerifier(DateInputVerifier.getInstance());
        jPanelContributionsValues.add(jTextFieldEndDate);

        jTextFieldYears2Project
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldYears2Project.setInputVerifier(NumberInputVerifier
                .getInstance());
        jPanelContributionsValues.add(jTextFieldYears2Project);

        jPanelContributionsValues.add(jComboBoxReinvestIncome);

        jPanelContributions.add(jPanelContributionsValues);

        jPanelLeft.add(jPanelContributions);

        jPanelLoan.setLayout(new java.awt.GridLayout(1, 2));

        jPanelLoan.setBorder(new javax.swing.border.TitledBorder("Loan Setup"));
        jPanelLoan.setPreferredSize(new java.awt.Dimension(330, 96));
        jPanelLoan.setMinimumSize(new java.awt.Dimension(259, 96));
        jPanelLoan.setMaximumSize(new java.awt.Dimension(32767, 96));
        jPanelLoanLabels.setLayout(new java.awt.GridLayout(3, 0, 0, 3));

        jPanelLoanLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jPanelLoanLabels.setPreferredSize(new java.awt.Dimension(180, 51));
        jLabel9.setText("Credit Limit");
        jPanelLoanLabels.add(jLabel9);

        jLabel12.setText("Loan Interest Rate %");
        jPanelLoanLabels.add(jLabel12);

        jLabel21.setText("Add Interest to loan ?");
        jPanelLoanLabels.add(jLabel21);

        jPanelLoan.add(jPanelLoanLabels);

        jPanelLoanValues.setLayout(new java.awt.GridLayout(3, 0, 0, 3));

        jPanelLoanValues.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 5)));
        jTextFieldCreditLimitAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCreditLimitAmount.setPreferredSize(new java.awt.Dimension(
                120, 20));
        jTextFieldCreditLimitAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanelLoanValues.add(jTextFieldCreditLimitAmount);

        jTextFieldLoanInterestRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoanInterestRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        jTextFieldLoanInterestRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelLoanValues.add(jTextFieldLoanInterestRate);

        jPanelLoanValues.add(jComboBoxAddInterest);

        jPanelLoan.add(jPanelLoanValues);

        jPanelLeft.add(jPanelLoan);

        jPanelLeft.add(jPanelDummyLeft);

        jPanelDetails.add(jPanelLeft);

        jPanelRight.setLayout(new javax.swing.BoxLayout(jPanelRight,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelRight.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(5, 5, 5, 10)));
        jPanelInvestment.setLayout(new java.awt.GridLayout(1, 2));

        jPanelInvestment.setBorder(new javax.swing.border.TitledBorder(
                "Investment Details"));
        jPanelInvestment.setPreferredSize(new java.awt.Dimension(260, 165));
        jPanelInvestment.setMinimumSize(new java.awt.Dimension(260, 165));
        jPanelInvestment
                .setMaximumSize(new java.awt.Dimension(2147483647, 165));
        jPanelInvestmentLabels.setLayout(new java.awt.GridLayout(6, 1, 0, 3));

        jPanelInvestmentLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jPanelInvestmentLabels
                .setPreferredSize(new java.awt.Dimension(150, 289));
        jPanelInvestmentLabels
                .setMaximumSize(new java.awt.Dimension(32767, 68));
        jLabel2.setText("Investment Strategy");
        jPanelInvestmentLabels.add(jLabel2);

        jLabel25.setText("Capital Growth %");
        jPanelInvestmentLabels.add(jLabel25);

        jLabel26.setText("Income %");
        jPanelInvestmentLabels.add(jLabel26);

        jLabel27.setText("Total Earnings %");
        jPanelInvestmentLabels.add(jLabel27);

        jLabel4.setText("Income Frequency");
        jPanelInvestmentLabels.add(jLabel4);

        jLabel10.setText("Capital Growth Rates");
        jPanelInvestmentLabels.add(jLabel10);

        jPanelInvestment.add(jPanelInvestmentLabels);

        jPanelInvestmentValues.setLayout(new java.awt.GridLayout(6, 1, 0, 3));

        jPanelInvestmentValues.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 5)));
        jComboBoxInvestmentStrategy
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxInvestmentStrategyItemStateChanged(evt);
                    }
                });

        jPanelInvestmentValues.add(jComboBoxInvestmentStrategy);

        jTextFieldCapitalGrowthRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCapitalGrowthRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelInvestmentValues.add(jTextFieldCapitalGrowthRate);

        jTextFieldIncomeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIncomeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelInvestmentValues.add(jTextFieldIncomeRate);

        jTextFieldTotalEarningRate.setEditable(false);
        jTextFieldTotalEarningRate.setBackground(java.awt.Color.lightGray);
        jTextFieldTotalEarningRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jPanelInvestmentValues.add(jTextFieldTotalEarningRate);

        jComboBoxIncomeFrequency.setModel(new javax.swing.DefaultComboBoxModel(
                new Object[] { ReferenceCode.CODE_NONE,
                        FrequencyCode.rcMONTHLY,
                        FrequencyCode.rcEVERY_THREE_MONTHS,
                        FrequencyCode.rcHALF_YEARLY, FrequencyCode.rcYEARLY }));

        jPanelInvestmentValues.add(jComboBoxIncomeFrequency);

        jPanelCapitalGrowthRates.setLayout(new java.awt.GridLayout(1, 0));

        jRadioButtonFlatCGR.setSelected(true);
        jRadioButtonFlatCGR.setText("Flat");
        buttonGroup1.add(jRadioButtonFlatCGR);
        jPanelCapitalGrowthRates.add(jRadioButtonFlatCGR);

        jRadioButtonVariableCGR.setText("Variable");
        buttonGroup1.add(jRadioButtonVariableCGR);
        jPanelCapitalGrowthRates.add(jRadioButtonVariableCGR);

        jPanelInvestmentValues.add(jPanelCapitalGrowthRates);

        jPanelInvestment.add(jPanelInvestmentValues);

        jPanelRight.add(jPanelInvestment);

        jPanelTax.setLayout(new javax.swing.BoxLayout(jPanelTax,
                javax.swing.BoxLayout.X_AXIS));

        jPanelTax.setBorder(new javax.swing.border.TitledBorder(
                "Tax effectiveness"));
        jPanelTax.setPreferredSize(new java.awt.Dimension(183, 96));
        jPanelTax.setMinimumSize(new java.awt.Dimension(137, 96));
        jPanelTax.setMaximumSize(new java.awt.Dimension(2147483647, 96));
        jPanelTaxLabels.setLayout(new java.awt.GridLayout(3, 0, 0, 3));

        jPanelTaxLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jPanelTaxLabels.setPreferredSize(new java.awt.Dimension(200, 216));
        jLabel41.setText("% of income that is franked");
        jLabel41.setToolTipText("% of income that is franked");
        jPanelTaxLabels.add(jLabel41);

        jLabel42.setText("% of income that is tax deferred");
        jLabel42.setToolTipText("% of income that is tax deferred");
        jPanelTaxLabels.add(jLabel42);

        jLabel43.setText("% of income that has realised CGT");
        jLabel43.setToolTipText("% of income that has realised CGT");
        jPanelTaxLabels.add(jLabel43);

        jPanelTax.add(jPanelTaxLabels);

        jPanelTaxValues.setLayout(new java.awt.GridLayout(3, 0, 0, 3));

        jPanelTaxValues.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 5)));
        jTextFieldFrankingCreditRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldFrankingCreditRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelTaxValues.add(jTextFieldFrankingCreditRate);

        jTextFieldTaxFreeDefferedRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTaxFreeDefferedRate.setPreferredSize(new java.awt.Dimension(
                50, 21));
        jTextFieldTaxFreeDefferedRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelTaxValues.add(jTextFieldTaxFreeDefferedRate);

        jTextFieldCapitalGainsRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCapitalGainsRate.setPreferredSize(new java.awt.Dimension(50,
                21));
        jTextFieldCapitalGainsRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelTaxValues.add(jTextFieldCapitalGainsRate);

        jPanelTax.add(jPanelTaxValues);

        jPanelRight.add(jPanelTax);

        jPanelCharges.setLayout(new java.awt.GridLayout(1, 2));

        jPanelCharges.setBorder(new javax.swing.border.TitledBorder("Charges"));
        jPanelCharges.setPreferredSize(new java.awt.Dimension(10, 70));
        jPanelCharges.setMinimumSize(new java.awt.Dimension(10, 70));
        jPanelCharges.setMaximumSize(new java.awt.Dimension(2147483647, 70));
        jPanelChargesLabels.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        jPanelChargesLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jPanelChargesLabels.setPreferredSize(new java.awt.Dimension(200, 59));
        jPanelChargesLabels.setMinimumSize(new java.awt.Dimension(116, 59));
        jLabel14.setText("Entry Fees %");
        jPanelChargesLabels.add(jLabel14);

        jLabel15.setText("Portfolio Review Fees %");
        jPanelChargesLabels.add(jLabel15);

        jPanelCharges.add(jPanelChargesLabels);

        jPanelChargesValues.setLayout(new java.awt.GridLayout(2, 0, 0, 3));

        jPanelChargesValues.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 5)));
        jPanelChargesValues.setPreferredSize(new java.awt.Dimension(50, 59));
        jPanelChargesValues.setMinimumSize(new java.awt.Dimension(4, 59));
        jTextFieldEntryFeeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEntryFeeRate.setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldEntryFeeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelChargesValues.add(jTextFieldEntryFeeRate);

        jTextFieldPortfolioReviewFeeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPortfolioReviewFeeRate
                .setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldPortfolioReviewFeeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jPanelChargesValues.add(jTextFieldPortfolioReviewFeeRate);

        jPanelCharges.add(jPanelChargesValues);

        jPanelRight.add(jPanelCharges);

        jPanelOthers.setLayout(new java.awt.GridLayout(1, 2));

        jPanelOthers.setBorder(new javax.swing.border.TitledBorder("Charges"));
        jPanelOthers.setPreferredSize(new java.awt.Dimension(10, 47));
        jPanelOthers.setMinimumSize(new java.awt.Dimension(10, 47));
        jPanelOthers.setMaximumSize(new java.awt.Dimension(2147483647, 47));
        jPanelOthersLabels.setLayout(new java.awt.GridLayout(1, 1, 0, 3));

        jPanelOthersLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jPanelOthersLabels.setPreferredSize(new java.awt.Dimension(200, 59));
        jPanelOthersLabels.setMinimumSize(new java.awt.Dimension(116, 59));
        jLabel6.setText("Other taxable Income");
        jPanelOthersLabels.add(jLabel6);

        jPanelOthers.add(jPanelOthersLabels);

        jPanelOthersData.setLayout(new java.awt.GridLayout(1, 1, 0, 3));

        jPanelOthersData.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 5)));
        jPanelOthersData.setPreferredSize(new java.awt.Dimension(50, 59));
        jPanelOthersData.setMinimumSize(new java.awt.Dimension(4, 59));
        jTextFieldOtherTaxableIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOtherTaxableIncome.setPreferredSize(new java.awt.Dimension(
                50, 21));
        jTextFieldOtherTaxableIncome.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanelOthersData.add(jTextFieldOtherTaxableIncome);

        jPanelOthers.add(jPanelOthersData);

        jPanelRight.add(jPanelOthers);

        jPanelRight.add(jPanelDummyRight);

        jPanelDetails.add(jPanelRight);

        jTabbedPane.addTab("Gearing Details", null, jPanelDetails, "");

        jPanelResult.setLayout(new javax.swing.BoxLayout(jPanelResult,
                javax.swing.BoxLayout.X_AXIS));

        jSplitPane.setDividerLocation(300);
        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jPanelGraphOptions.setLayout(new java.awt.BorderLayout());

        jPanelGraph.setLayout(new javax.swing.BoxLayout(jPanelGraph,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelGraph.setBorder(new javax.swing.border.TitledBorder("Graph"));
        jPanelGraphOptions.add(jPanelGraph, java.awt.BorderLayout.CENTER);

        jPanelOptions.setLayout(new javax.swing.BoxLayout(jPanelOptions,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelGraphSelection.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 20, 5));

        jRadioButtonInvestmentValue.setMnemonic('\u0001');
        jRadioButtonInvestmentValue.setSelected(true);
        jRadioButtonInvestmentValue.setText("Investment Value");
        buttonGroupSelection.add(jRadioButtonInvestmentValue);
        jPanelGraphSelection.add(jRadioButtonInvestmentValue);

        jRadioButtonPotentialOutcome.setMnemonic('\u0002');
        jRadioButtonPotentialOutcome.setText("Potential Outcome");
        buttonGroupSelection.add(jRadioButtonPotentialOutcome);
        jPanelGraphSelection.add(jRadioButtonPotentialOutcome);

        jRadioButtonCapitalGrowthRates.setMnemonic('\u0003');
        jRadioButtonCapitalGrowthRates.setText("Capital Growth Rates");
        buttonGroupSelection.add(jRadioButtonCapitalGrowthRates);
        jPanelGraphSelection.add(jRadioButtonCapitalGrowthRates);

        jPanelOptions.add(jPanelGraphSelection);

        jPanelGraphOptions.add(jPanelOptions, java.awt.BorderLayout.SOUTH);

        jSplitPane.setLeftComponent(jPanelGraphOptions);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2,
                javax.swing.BoxLayout.Y_AXIS));

        jRadioButtonSummary.setSelected(true);
        jRadioButtonSummary.setText("Investment Summary");
        buttonGroupSummaryType.add(jRadioButtonSummary);
        jPanelSummaryType.add(jRadioButtonSummary);

        jRadioButtonAnnual.setText("Annual Cashflow");
        buttonGroupSummaryType.add(jRadioButtonAnnual);
        jPanelSummaryType.add(jRadioButtonAnnual);

        jRadioButtonMonthly.setText("Monthly Cashflow");
        buttonGroupSummaryType.add(jRadioButtonMonthly);
        jPanelSummaryType.add(jRadioButtonMonthly);

        jPanel2.add(jPanelSummaryType);

        jScrollPaneData.setViewportView(jTableData);

        jPanel2.add(jScrollPaneData);

        jSplitPane.setRightComponent(jPanel2);

        jPanelResult.add(jSplitPane);

        jTabbedPane.addTab("Projections", null, jPanelResult, "");

        add(jTabbedPane);

        jPanelControls.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 40, 5));

        jButtonReport.setText("Report");
        jButtonReport.setDefaultCapable(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setDefaultCapable(false);
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSave);

        jButtonSaveAs.setText("Save As");
        jButtonSaveAs.setDefaultCapable(false);
        jButtonSaveAs.setEnabled(false);
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSaveAs);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonDelete);

        jPanelControls.add(jPanelCloseSave);

        jButtonClear.setText("Clear");
        jButtonClear.setDefaultCapable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonClear);

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

    private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveAsActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            newModel = true;
            doSave(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonSaveAsActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doClear(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonClearActionPerformed

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);
    }// GEN-LAST:event_jTabbedPaneStateChanged

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            newModel = false;
            doSave(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int result = BaseView.closeDialog(this);
        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

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
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doNext(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doPrevious(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    // Singleton pattern
    private static GearingView2 view;

    public static GearingView2 display(final Model model, FocusListener[] listeners) {

        if (view == null) {
            view = new GearingView2();
            SwingUtil.add2Frame(view, listeners,
                model == null ? view.getDefaultTitle() : model.getTitle(),
                ViewSettings.getInstance().getViewImage(
                view.getClass().getName()), true, true, false);
        }

        try {
            view.updateView(model);

            String title = model == null ? view.getDefaultTitle() : model.getTitle();
            SwingUtil.setTitle(view, title);
            SwingUtil.setVisible(view, true);
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            view = null;
        }

        return view;

    }

    /***************************************************************************
     * BaseView protected methods
     **************************************************************************/
    protected void doNext(java.awt.event.ActionEvent evt) {
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }

    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(newModel);
            // update menu
            FinancialPlannerApp.getInstance().updateModels();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    public void doDelete(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this,
                "Do You want to delete this projection?",
                "Delete Projection Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        fireActionEvent(DATA_REMOVE);
    }

    protected void doClear(java.awt.event.ActionEvent evt) {

        try {
            gCalc.disableUpdate();
            gCalc.clear();
            updateTitle();
        } finally {
            updateEditable();
            gCalc.enableUpdate();
            gCalc.doUpdate();
        }

    }

    public boolean isModified() {
        // return gCalc.isModified();
        return (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                .isModified())
                || (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                        .isModified());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxReinvestIncome;

    private javax.swing.JPanel jPanelTaxValues;

    private javax.swing.JComboBox jComboBoxInvestmentStrategy;

    private javax.swing.JRadioButton jRadioButtonCapitalGrowthRates;

    private javax.swing.JRadioButton jRadioButtonVariableCGR;

    private javax.swing.JTextField jTextFieldTotalEarningRate;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JPanel jPanelOptions;

    private javax.swing.ButtonGroup buttonGroupSummaryType;

    private javax.swing.JTextField jTextFieldFrankingCreditRate;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JRadioButton jRadioButtonSummary;

    private javax.swing.JTextField jTextFieldInitialLoanAmount;

    private javax.swing.JTextField jTextFieldCreditLimitAmount;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JButton jButtonNext;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JPanel jPanelTaxLabels;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JPanel jPanelCloseSave;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JSplitPane jSplitPane;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JPanel jPanelChargesValues;

    private javax.swing.JPanel jPanelContributionsValues;

    private javax.swing.JPanel jPanelGraphOptions;

    private javax.swing.JPanel jPanelDummyLeft;

    private javax.swing.JPanel jPanelOthersLabels;

    private javax.swing.JPanel jPanelChargesLabels;

    private javax.swing.JPanel jPanelTax;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JPanel jPanelResult;

    private javax.swing.JRadioButton jRadioButtonAnnual;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JTable jTableData;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JPanel jPanelContributions;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JPanel jPanelSummaryType;

    private javax.swing.JPanel jPanelInvestmentValues;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.JRadioButton jRadioButtonMonthly;

    private javax.swing.ButtonGroup buttonGroupSelection;

    private javax.swing.JTextField jTextFieldCapitalGrowthRate;

    private javax.swing.JRadioButton jRadioButtonPotentialOutcome;

    private javax.swing.JPanel jPanelContributionsLabels;

    private javax.swing.JPanel jPanelPrevNext;

    private javax.swing.JComboBox jComboBoxAddInterest;

    private javax.swing.JTextField jTextFieldIncomeRate;

    private com.argus.beans.FDateChooser jTextFieldEndDate;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JPanel jPanelCapitalGrowthRates;

    private javax.swing.JPanel jPanelLoanValues;

    private javax.swing.JTextField jTextFieldPortfolioReviewFeeRate;

    private javax.swing.JScrollPane jScrollPaneData;

    private javax.swing.JPanel jPanelGraph;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel8;

    private com.argus.beans.FDateChooser jTextFieldStartDate;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JComboBox jComboBoxGearingType;

    private javax.swing.JRadioButton jRadioButtonInvestmentValue;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanelInvestmentLabels;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField jTextFieldCapitalGainsRate;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JTextField jTextFieldOtherTaxableIncome;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JTextField jTextFieldEntryFeeRate;

    private javax.swing.JTextField jTextFieldLoanInterestRate;

    private javax.swing.JRadioButton jRadioButtonFlatCGR;

    private javax.swing.JPanel jPanelCharges;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JPanel jPanelInvestment;

    private javax.swing.JPanel jPanelDummyRight;

    private javax.swing.JPanel jPanelLoanLabels;

    private javax.swing.JTextField jTextFieldRegularInvestorContributionAmount;

    private javax.swing.JComboBox jComboBoxIncomeFrequency;

    private javax.swing.JPanel jPanelGraphSelection;

    private javax.swing.JPanel jPanelDetails;

    private javax.swing.JTextField jTextFieldTaxFreeDefferedRate;

    private javax.swing.JPanel jPanelLoan;

    private javax.swing.JPanel jPanelOthersData;

    private javax.swing.JPanel jPanelOthers;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JLabel jLabel42;

    private javax.swing.JLabel jLabel41;

    private javax.swing.JTextField jTextFieldRegularLoanAdvanceAmount;

    private javax.swing.JTextField jTextFieldInvestorDepositAmount;

    private javax.swing.JTextField jTextFieldYears2Project;

    // End of variables declaration//GEN-END:variables

    public void hideControls() {
        jPanelControls.setVisible(false);
    }

    public void showControls() {
        jPanelControls.setVisible(true);
    }

    private void setTableModel() {

        javax.swing.table.TableModel tm = gCalc.getTableModel();
        if (jTableData.getModel() == tm)
            return;

        // jTable.setTableHeader(null);
        jTableData.setModel(tm);

        JTableHeader th = new JTableHeader(jTableData.getColumnModel());
        jTableData.setTableHeader(th);
        th.setFont(SwingUtils.getDefaultFont());

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView(String modelTitle) throws java.io.IOException {

        PersonService person = ServiceLocator.getInstance().getClientPerson();
        Model m = person == null ? null : person.getModel(getDefaultType(),
                modelTitle);

        if (m == null) {
            updateView(person);
        } else {
            updateView(m);
        }
        updateTitle();

    }

    public void updateView(Model m) throws java.io.IOException {

        // saveView();

        // doClear(null);

        if (m == null) {
            updateView(ServiceLocator.getInstance().getClientPerson());
        } else {
            // use copy of model
            Integer id = m.getPrimaryKeyID();
            m = new Model(m);
            m.setPrimaryKeyID(id);
    
            gCalc.disableUpdate();
            try {
    
                gCalc.setModel(m);
                // gCalc.setSaved();
            } finally {
                updateEditable();
                // gCalc.doUpdate();
    
                updateNonEditable();
                graphView.setGraphData(gCalc.getGraphData());
    
                gCalc.enableUpdate();
            }
        }
    }

    public void updateView(PersonService person)
            throws ServiceException {

        gCalc.disableUpdate();
        try {

            gCalc.clear();
            Model m = new Model();
            m.setTypeID(ModelType.INVESTMENT_GEARING);
            gCalc.setModel(m);
            // load data from person, e.g. DOB

        } finally {
            gCalc.enableUpdate();
            gCalc.doUpdate();
        }

    }

    private void saveView(boolean newModel) {

        int result = -1;

        String oldTitle = getTitle();
        try {
            if (!newModel)
                result = SaveProjectionDialog.getSaveProjectionInstance().save(
                        gCalc, this);
            else
                result = SaveProjectionDialog.getSaveProjectionInstance()
                        .saveAs(gCalc, this);

            if (result == SaveProjectionDialog.CANCEL_OPTION)
                return;

            updateTitle();
        } catch (DuplicateException e) {
            String newTitle = getTitle();

            String msg = "Failed to save model.";
            JOptionPane.showMessageDialog(this, msg,
                    "Title already used by another model",
                    JOptionPane.ERROR_MESSAGE);

            return;
        } catch (ModelTitleRestrictionException me) {
            String msg = "Failed to save model.\n  Please ensure total characters for model title is 3 or more.";
            JOptionPane.showMessageDialog(this, msg, "Invalid title",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            saveView(ServiceLocator.getInstance().getClientPerson());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    public void saveView(PersonService person) throws java.io.IOException {

        java.io.StringWriter w = new java.io.StringWriter();
        java.io.BufferedWriter output = new java.io.BufferedWriter(w);

        try {
            IOUtils2.writeHeader(this, output);
            IOUtils2.write(this, output);

            output.flush();

            getModel().setData(w.toString());

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

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        updateNonEditable();
    }

    /*
     * helper methods
     */
    public void updateEditable() {

        updateOthers();

        double d = gCalc.getInitialInvestorAmount();
        jTextFieldInvestorDepositAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = gCalc.getInitialLoanAmount();
        jTextFieldInitialLoanAmount.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = gCalc.getRegularInvestorContributionAmount();
        jTextFieldRegularInvestorContributionAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = gCalc.getRegularLoanAdvanceAmount();
        jTextFieldRegularLoanAdvanceAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = gCalc.getLoanInterestRate();
        jTextFieldLoanInterestRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
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

        d = gCalc.getEntryFeeRate();
        jTextFieldEntryFeeRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = gCalc.getRevisionFeeRate();
        jTextFieldPortfolioReviewFeeRate
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                        .toString(d));

        d = gCalc.getOtherTaxableIncomeAmount();
        jTextFieldOtherTaxableIncome
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

    }

    protected void updateOthers() {

        jComboBoxGearingType.setSelectedItem(gCalc.getGearingTypeDesc());

        Boolean b = gCalc.getReinvestIncome();
        jComboBoxReinvestIncome
                .setSelectedItem(b == null || !b.booleanValue() ? BooleanCode.rcNO
                        : BooleanCode.rcYES);

        // #838
        jComboBoxIncomeFrequency
                .setSelectedItem(b.booleanValue() ? FrequencyCode.rcHALF_YEARLY
                        : // default
                        FrequencyCode.getCode(gCalc.getIncomeFrequencyCodeID()));
        SwingUtil.setEnabled(jComboBoxIncomeFrequency, !b.booleanValue());

        b = gCalc.getAddIterest();
        jComboBoxAddInterest
                .setSelectedItem(b == null || !b.booleanValue() ? BooleanCode.rcNO
                        : BooleanCode.rcYES);

        jComboBoxInvestmentStrategy.setSelectedItem(gCalc
                .getInvestmentStrategyCodeDesc());

        if (gCalc.getFlatRate() == null || gCalc.getFlatRate().booleanValue())
            jRadioButtonFlatCGR.setSelected(true);
        else
            jRadioButtonVariableCGR.setSelected(true);

        if (gCalc.getGraphType().equals(GearingCalc2.INVESTMENT_VALUE))
            jRadioButtonInvestmentValue.setSelected(true);
        else if (gCalc.getGraphType().equals(GearingCalc2.POTENTIAL_OUTCOME))
            jRadioButtonPotentialOutcome.setSelected(true);
        else if (gCalc.getGraphType().equals(GearingCalc2.CAPITAL_GROWTH_RATES))
            jRadioButtonCapitalGrowthRates.setSelected(true);
        else
            ; // deselect all or set default or throw ???

        if (gCalc.isSummary())
            jRadioButtonSummary.setSelected(true);
        else {
            if (gCalc.isMonthly())
                jRadioButtonMonthly.setSelected(true);
            else
                jRadioButtonAnnual.setSelected(true);
        }

        updateComponents();
    }

    public void updateNonEditable() {

        updateOthers();

        Integer invStrCodeID = gCalc.getInvestmentStrategyCodeID();

        Integer gearingTypeID = gCalc.getGearingTypeID();
        SwingUtil.setEnabled(jTextFieldRegularInvestorContributionAmount,
                GearingType.INSTALLMENT.equals(gearingTypeID));
        SwingUtil.setEnabled(jTextFieldRegularLoanAdvanceAmount,
                GearingType.INSTALLMENT.equals(gearingTypeID));

        gCalc.disableUpdate();
        try {
            double d = gCalc.getInitialInvestorAmount();
            jTextFieldInvestorDepositAmount
                    .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                            .toString(d));

            d = gCalc.getInitialLoanAmount();
            jTextFieldInitialLoanAmount
                    .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                            .toString(d));

            if (GearingType.LUMP_SUM.equals(gearingTypeID)) {
                d = gCalc.getRegularInvestorContributionAmount();
                jTextFieldRegularInvestorContributionAmount
                        .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                                .toString(d));

                d = gCalc.getRegularLoanAdvanceAmount();
                jTextFieldRegularLoanAdvanceAmount
                        .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                                .toString(d));
            }

            String s = DateTimeUtils.asString(gCalc.getStartDate());
            jTextFieldStartDate.setText(s);

            s = DateTimeUtils.asString(gCalc.getEndDate());
            jTextFieldEndDate.setText(s);

            d = gCalc.getYears();
            jTextFieldYears2Project.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                    : number.toString(d));

            d = gCalc.getCreditLimit();
            jTextFieldCreditLimitAmount
                    .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                            .toString(d));

            d = gCalc.getGrowthRate();
            jTextFieldCapitalGrowthRate
                    .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                            .toString(d));

            d = gCalc.getIncomeRate();
            jTextFieldIncomeRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                    : percent.toString(d));

            d = gCalc.getTotalReturnRate();
            jTextFieldTotalEarningRate
                    .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                            .toString(d));

        } finally {
            gCalc.enableUpdate();

            Boolean enable = invStrCodeID == null ? Boolean.TRUE
                    : Boolean.FALSE;
            SwingUtil.setEnabled(jTextFieldCapitalGrowthRate, enable
                    .booleanValue());
            jTextFieldCapitalGrowthRate.getDocument().putProperty(
                    DocumentNames.READY, enable);
            SwingUtil.setEnabled(jTextFieldIncomeRate, enable.booleanValue());
            jTextFieldIncomeRate.getDocument().putProperty(DocumentNames.READY,
                    enable);
        }

        updateChart();
        setTableModel();
        updateComponents();

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getGearingReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws java.io.IOException {

        ReportFields reportFields = ReportFields.getInstance();
        gCalc.initializeReportData(reportFields);

        return reportFields;

    }

    protected void doReport() {

        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(ServiceLocator.getInstance().getClientPerson()),
                    getDefaultReport());

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
        }

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
                saveView(false);
                doClear(null);
            }
        });
        am.put(DATA_REMOVE, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                PersonService person = ServiceLocator.getInstance().getClientPerson();
                if (person == null)
                    return;

                try {
                    person.removeModel(getModel());
                    person.storeModels();
                } catch (com.argus.financials.service.ServiceException e) {
                    e.printStackTrace();
                    return;
                }

                // remove from menu
                FinancialPlannerApp.getInstance().updateModels();

                doClose(null);
                // doClear(null);

            }
        });
        /*
         * am.put( DATA_UPDATE, new AbstractAction() { public void
         * actionPerformed(java.awt.event.ActionEvent evt) {
         *  } } );
         */
    }

    private static int eventID;

    private void fireActionEvent(Integer actionID) {

        javax.swing.ActionMap actionMap = getActionMap();
        if (actionMap == null)
            return;

        javax.swing.Action action = (javax.swing.Action) (actionMap
                .get(actionID));
        if (action == null)
            return;

        action.actionPerformed(new java.awt.event.ActionEvent(this, ++eventID,
                this.getClass().getName()));

    }

}
