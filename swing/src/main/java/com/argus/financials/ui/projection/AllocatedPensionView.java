/*
 * AllPenView.java
 *
 * Created on 29 November 2001, 17:15
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Cursor;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.code.PaymentType;
import com.argus.financials.code.ReversionaryCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.projection.AllocatedPensionCalc;
import com.argus.financials.projection.CurrentPositionComment;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.DocumentUtils;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.TaxUtils;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.swing.SwingUtils;
import com.argus.util.DateTimeUtils;

public class AllocatedPensionView extends ETPRolloverView {

    private static final int RESULT_PAGE_ID = 2;

    private APGraphView graphView;

    /** Creates new form AllPencCalc */
    public AllocatedPensionView() {
        super(new AllocatedPensionCalc());

        initComponents();
        initComponents2();
    }

    public AllocatedPensionView(MoneyCalc calc) {
        super(calc);

        initComponents();
        initComponents2();
    }

    public Integer getDefaultType() {
        return ModelTypeID.rcALLOCATED_PENSION.getCodeId();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcALLOCATED_PENSION.getDescription();
    }

    private void initComponents2() {

        graphView = new APGraphView();
        SwingUtils.setDefaultFont(graphView);

        //
        FrequencyCode fc = new FrequencyCode();
        jComboBoxRequiredFrequency.addItem(fc
                .getCodeDescription(FrequencyCode.VALUE_NONE));
        jComboBoxRequiredFrequency.addItem(fc
                .getCodeDescription(FrequencyCode.MONTHLY));
        jComboBoxRequiredFrequency.addItem(fc
                .getCodeDescription(FrequencyCode.EVERY_THREE_MONTHS));
        jComboBoxRequiredFrequency.addItem(fc
                .getCodeDescription(FrequencyCode.HALF_YEARLY));
        jComboBoxRequiredFrequency.addItem(fc
                .getCodeDescription(FrequencyCode.YEARLY));

        //
        _setAccessibleContext();

        if (etpCalc.getClass().equals(AllocatedPensionCalc.class)) {
            etpCalc.addChangeListener(this);
            DocumentUtils.addListener(this, etpCalc); // after
                                                        // _setAccessibleContext()
            updateEditable();
            setActionMap();
        }

    }

    private void _setAccessibleContext() {

        jTextFieldEntryFeeRate.getAccessibleContext().setAccessibleName(
                DocumentNames.REVIEW_FEES);
        jTextFieldRebateableRate.getAccessibleContext().setAccessibleName(
                DocumentNames.REBATE);
        jTextFieldPartnerDOB.getAccessibleContext().setAccessibleName(
                DocumentNames.DOB_PARTNER);
        jTextFieldRequiredPaymentAmount.getAccessibleContext()
                .setAccessibleName(DocumentNames.REQUIRED_PAYMENT_AMOUNT);
        jTextFieldIndexRate.getAccessibleContext().setAccessibleName(
                DocumentNames.INDEX_RATE);
        jCheckBoxIndexRate.getAccessibleContext().setAccessibleName(
                DocumentNames.INDEXED);

        jComboBoxClientSexCode.getAccessibleContext().setAccessibleName(
                DocumentNames.SEX_CODE);
        jComboBoxPensionReversionOption.getAccessibleContext()
                .setAccessibleName(DocumentNames.PENSION_REVERSION_OPTION);
        jComboBoxPartnerSexCode.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SEX_CODE);
        jComboBoxRequiredPaymentType.getAccessibleContext().setAccessibleName(
                DocumentNames.REQUIRED_PAYMENT_TYPE);
        jComboBoxRequiredFrequency.getAccessibleContext().setAccessibleName(
                DocumentNames.REQUIRED_FREQUENCY);
        jComboBoxInvestmentStrategy.getAccessibleContext().setAccessibleName(
                DocumentNames.INV_STRATEGY);

        jRadioButtonPre071983No.getAccessibleContext().setAccessibleName(
                DocumentNames.PRE_071983_CODE_NO);
        jRadioButtonPre071983Yes.getAccessibleContext().setAccessibleName(
                DocumentNames.PRE_071983_CODE_YES);
        jRadioButtonInvalidityNo.getAccessibleContext().setAccessibleName(
                DocumentNames.INVALIDITY_CODE_NO);
        jRadioButtonInvalidityYes.getAccessibleContext().setAccessibleName(
                DocumentNames.INVALIDITY_CODE_YES);

    }

    protected void updateComponents() {
        super.updateComponents();

        jButtonNext
                .setEnabled((jTabbedPane.getSelectedIndex() < RESULT_PAGE_ID - 1)
                        || (jTabbedPane.getSelectedIndex() == RESULT_PAGE_ID - 1 && etpCalc
                                .isReady()));

    }

    private void updateChart() {

        if (!etpCalc.isModified() || !etpCalc.isReady())
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // assets post-retirement
            double[] assets = ((AllocatedPensionCalc) etpCalc)
                    .getTargetValues();
            // update required income (after retirement)
            double[] income = ((AllocatedPensionCalc) etpCalc)
                    .getRequiredIncomeValues();

            // nothing changed
            if (assets == null && income == null)
                return;

            graphView.setComment(CurrentPositionComment
                    .getComment(((AllocatedPensionCalc) etpCalc)));

            // do chart
            graphView.removeChartLabels();

            graphView.addChartLabels(graphView.customizeChart(new double[][] {
                    assets, income }, ((AllocatedPensionCalc) etpCalc)
                    .getLabels(), new String[] {
                    "<html>Assets <i>(post-retirement)</i></html>",
                    "<html>Actual Income</html>" }, new java.awt.Color[] {
                    java.awt.Color.red, java.awt.Color.blue }, true), Currency
                    .getCurrencyInstance());

            // let user to go to chart
            updateComponents();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        super.stateChanged(changeEvent);

        if (((AllocatedPensionCalc) etpCalc).isReady()) {
            if (jTabbedPane.getTabCount() == RESULT_PAGE_ID)
                jTabbedPane.addTab("Projections", graphView);
        } else {
            if (jTabbedPane.getTabCount() > RESULT_PAGE_ID)
                jTabbedPane.removeTabAt(RESULT_PAGE_ID);
        }

        updateChart();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {
        jPanelControls.setVisible(false);

        bgPre071983Pension = new javax.swing.ButtonGroup();
        bgInvalidity = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelAllocatedPension = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jComboBoxClientSexCode = new JComboBox(new SexCode()
                .getCodeDescriptions());
        jTextFieldCommencementDate = new javax.swing.JTextField();
        jTextFieldPurchasePrice = new javax.swing.JTextField();
        jRadioButtonPre071983No = new javax.swing.JRadioButton();
        jRadioButtonPre071983Yes = new javax.swing.JRadioButton();
        jRadioButtonInvalidityNo = new javax.swing.JRadioButton();
        jRadioButtonInvalidityYes = new javax.swing.JRadioButton();
        jTextFieldEntryFeeRate = new javax.swing.JTextField();
        jTextFieldRebateableRate = new javax.swing.JTextField();
        jTextFieldATOUPPAmount = new javax.swing.JTextField();
        jTextFieldAnnualDeductibleAmount = new javax.swing.JTextField();
        jTextFieldFirstYearDeductibleAmount = new javax.swing.JTextField();
        jComboBoxPensionReversionOption = new JComboBox(new ReversionaryCode()
                .getCodeDescriptions());
        jComboBoxPartnerSexCode = new JComboBox(new SexCode()
                .getCodeDescriptions());
        jTextFieldPartnerDOB = new javax.swing.JTextField();
        jTextFieldPartnerAge = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jTextFieldDSSUPPAmount = new javax.swing.JTextField();
        jTextFieldAnnualNonAssesableAmount = new javax.swing.JTextField();
        jTextFieldFirstYearNonAssesableAmount = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jPanelRight = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jComboBoxRequiredPaymentType = new javax.swing.JComboBox(
                new PaymentType().getCodeDescriptions());
        jTextFieldRequiredPaymentAmount = new javax.swing.JTextField();
        jComboBoxRequiredFrequency = new javax.swing.JComboBox();
        jCheckBoxIndexRate = new javax.swing.JCheckBox();
        jTextFieldIndexRate = new javax.swing.JTextField();
        jTextFieldAnnualMinimumAmount = new javax.swing.JTextField();
        jTextFieldAnnualMaximumAmount = new javax.swing.JTextField();
        jTextFieldFirstYearMinimum = new javax.swing.JTextField();
        jTextFieldFirstYearMaximum = new javax.swing.JTextField();
        jTextFieldRebateAmount = new javax.swing.JTextField();
        jTextFieldGrossRegularPaymentAmount = new javax.swing.JTextField();
        jTextFieldPAYGRegularAmount = new javax.swing.JTextField();
        jTextFieldNetRegularPaymentAmount = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRegularRebateAmount = new javax.swing.JTextField();
        jPanelInvestmentStrategy = new javax.swing.JPanel();
        jLabelInvestmentStrategy = new javax.swing.JLabel();
        jLabelAnnualIncomeRate = new javax.swing.JLabel();
        jLabelAnnualGrowthRate = new javax.swing.JLabel();
        jLabelTotalAnnualReturnRate = new javax.swing.JLabel();
        jComboBoxInvestmentStrategy = new javax.swing.JComboBox(
                new InvestmentStrategyCode().getCodeDescriptions());
        jTextFieldAnnualIncomeRate = new javax.swing.JTextField();
        jTextFieldAnnualGrowthRate = new javax.swing.JTextField();
        jTextFieldTotalAnnualReturnRate = new javax.swing.JTextField();
        jPanelControls2 = new javax.swing.JPanel();
        jPanelPrevNext = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(700, 500));
        jTabbedPane.setPreferredSize(new java.awt.Dimension(750, 800));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jTabbedPane.addTab("ETP Rollover", jPanelETPData);

        jPanelAllocatedPension.setLayout(new javax.swing.BoxLayout(
                jPanelAllocatedPension, javax.swing.BoxLayout.X_AXIS));

        jPanelLeft.setLayout(new javax.swing.BoxLayout(jPanelLeft,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        jPanel1
                .setBorder(new javax.swing.border.TitledBorder(
                        "Pension Details"));

        jLabel43.setText("Pension Commencement Date");
        jLabel43.setPreferredSize(NAME_DIM);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(10, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel43, gridBagConstraints1);

        jLabel44.setText("Purchase Price");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel44, gridBagConstraints1);

        jLabel45.setText("Pre July 1983 Pension");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel45, gridBagConstraints1);

        jLabel46.setText("Rebateable %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel46, gridBagConstraints1);

        jLabel47.setText("Undeducted Purchase Price");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel47, gridBagConstraints1);

        jLabel48.setText("Annual Deductible Amount");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel48, gridBagConstraints1);

        jLabel49.setText("1st Year Deductible Amount");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel49, gridBagConstraints1);

        jLabel50.setText("Pension Reversion Option");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel50, gridBagConstraints1);

        jLabel51.setText("Partner Sex");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 11;
        gridBagConstraints1.insets = new java.awt.Insets(10, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel51, gridBagConstraints1);

        jLabel52.setText("Partner DOB");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 12;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel52, gridBagConstraints1);

        jComboBoxClientSexCode
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxClientSexCodeItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jComboBoxClientSexCode, gridBagConstraints1);

        jTextFieldCommencementDate.setEditable(false);
        jTextFieldCommencementDate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(jTextFieldCommencementDate, gridBagConstraints1);

        jTextFieldPurchasePrice.setEditable(false);
        jTextFieldPurchasePrice
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldPurchasePrice, gridBagConstraints1);

        jRadioButtonPre071983No.setText("No");
        bgPre071983Pension.add(jRadioButtonPre071983No);
        jRadioButtonPre071983No
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPre071983NoItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jRadioButtonPre071983No, gridBagConstraints1);

        jRadioButtonPre071983Yes.setText("Yes");
        bgPre071983Pension.add(jRadioButtonPre071983Yes);
        jRadioButtonPre071983Yes
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPre071983YesItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel1.add(jRadioButtonPre071983Yes, gridBagConstraints1);

        jRadioButtonInvalidityNo.setText("No");
        bgInvalidity.add(jRadioButtonInvalidityNo);
        jRadioButtonInvalidityNo
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonInvalidityNoItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jRadioButtonInvalidityNo, gridBagConstraints1);

        jRadioButtonInvalidityYes.setText("Yes");
        bgInvalidity.add(jRadioButtonInvalidityYes);
        jRadioButtonInvalidityYes
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonInvalidityYesItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel1.add(jRadioButtonInvalidityYes, gridBagConstraints1);

        jTextFieldEntryFeeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEntryFeeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldEntryFeeRate, gridBagConstraints1);

        jTextFieldRebateableRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRebateableRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldRebateableRate
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldRebateableRateFocusLost(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldRebateableRate, gridBagConstraints1);

        jTextFieldATOUPPAmount.setEditable(false);
        jTextFieldATOUPPAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldATOUPPAmount, gridBagConstraints1);

        jTextFieldAnnualDeductibleAmount.setEditable(false);
        jTextFieldAnnualDeductibleAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldAnnualDeductibleAmount, gridBagConstraints1);

        jTextFieldFirstYearDeductibleAmount.setEditable(false);
        jTextFieldFirstYearDeductibleAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldFirstYearDeductibleAmount, gridBagConstraints1);

        jComboBoxPensionReversionOption
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxPensionReversionOptionItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jComboBoxPensionReversionOption, gridBagConstraints1);

        jComboBoxPartnerSexCode
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxPartnerSexCodeItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 11;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(jComboBoxPartnerSexCode, gridBagConstraints1);

        jTextFieldPartnerDOB
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerDOB.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 12;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldPartnerDOB, gridBagConstraints1);

        jTextFieldPartnerAge.setEditable(false);
        jTextFieldPartnerAge
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerAge.setPreferredSize(VALUE_DIM);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 13;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextFieldPartnerAge, gridBagConstraints1);

        jLabel3.setText("Partner Age");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 13;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gridBagConstraints1);

        jLabel9.setText("ClientView Sex");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel9, gridBagConstraints1);

        jLabel11.setText("Invalidity");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel11, gridBagConstraints1);

        jLabel6.setText("Entry Fee %");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel6, gridBagConstraints1);

        jPanelLeft.add(jPanel1);

        jPanel5.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        jPanel5.setBorder(new javax.swing.border.TitledBorder("DSS"));// "Non-Assesable
                                                                        // Purchase
                                                                        // Details"));

        jLabel62.setText("Undeducted Purchase Price");
        jLabel62.setPreferredSize(NAME_DIM);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel5.add(jLabel62, gridBagConstraints2);

        jLabel63.setText("Annual Deductible Amount");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel5.add(jLabel63, gridBagConstraints2);

        jTextFieldDSSUPPAmount.setEditable(false);
        jTextFieldDSSUPPAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldDSSUPPAmount.setPreferredSize(VALUE_DIM);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jTextFieldDSSUPPAmount, gridBagConstraints2);

        jTextFieldAnnualNonAssesableAmount.setEditable(false);
        jTextFieldAnnualNonAssesableAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jTextFieldAnnualNonAssesableAmount, gridBagConstraints2);

        jTextFieldFirstYearNonAssesableAmount.setEditable(false);
        jTextFieldFirstYearNonAssesableAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jTextFieldFirstYearNonAssesableAmount, gridBagConstraints2);

        jLabel64.setText("1st Year Deductible Amount");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel5.add(jLabel64, gridBagConstraints2);

        jPanelLeft.add(jPanel5);

        jPanelAllocatedPension.add(jPanelLeft);

        jPanelRight.setLayout(new javax.swing.BoxLayout(jPanelRight,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints3;

        jPanel2
                .setBorder(new javax.swing.border.TitledBorder(
                        "Payment Details"));

        jLabel53.setText("Required Payment Type");
        jLabel53.setPreferredSize(NAME_DIM);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel53, gridBagConstraints3);

        jLabel54.setText("Required Payment Amount");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel54, gridBagConstraints3);

        jLabel55.setText("Required Frequency");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel55, gridBagConstraints3);

        jLabel56.setText("Annual Minimum");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel56, gridBagConstraints3);

        jLabel57.setText("Annual Maximum");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel57, gridBagConstraints3);

        jLabel58.setText("Annual Rebate Amount");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 8;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel58, gridBagConstraints3);

        jLabel59.setText("Gross Regular Payment");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel59, gridBagConstraints3);

        jLabel60.setText("PAYG Regular");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel60, gridBagConstraints3);

        jLabel61.setText("Net Regular Payment");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel61, gridBagConstraints3);

        jComboBoxRequiredPaymentType.setPreferredSize(VALUE_DIM);
        jComboBoxRequiredPaymentType
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxRequiredPaymentTypeItemStateChanged(evt);
                    }
                });

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jComboBoxRequiredPaymentType, gridBagConstraints3);

        jTextFieldRequiredPaymentAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRequiredPaymentAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldRequiredPaymentAmount, gridBagConstraints3);

        jComboBoxRequiredFrequency
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxRequiredFrequencyItemStateChanged(evt);
                    }
                });

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jComboBoxRequiredFrequency, gridBagConstraints3);

        jCheckBoxIndexRate.setText("Index Rate");
        jCheckBoxIndexRate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxIndexRateItemStateChanged(evt);
            }
        });

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jCheckBoxIndexRate, gridBagConstraints3);

        jTextFieldIndexRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIndexRate
                .setInputVerifier(PercentInputVerifier.getInstance());

        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jTextFieldIndexRate, gridBagConstraints3);

        jTextFieldAnnualMinimumAmount.setEditable(false);
        jTextFieldAnnualMinimumAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldAnnualMinimumAmount, gridBagConstraints3);

        jTextFieldAnnualMaximumAmount.setEditable(false);
        jTextFieldAnnualMaximumAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldAnnualMaximumAmount, gridBagConstraints3);

        jTextFieldFirstYearMinimum.setEditable(false);
        jTextFieldFirstYearMinimum
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldFirstYearMinimum, gridBagConstraints3);

        jTextFieldFirstYearMaximum.setEditable(false);
        jTextFieldFirstYearMaximum
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldFirstYearMaximum, gridBagConstraints3);

        jTextFieldRebateAmount.setEditable(false);
        jTextFieldRebateAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 8;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldRebateAmount, gridBagConstraints3);

        jTextFieldGrossRegularPaymentAmount.setEditable(false);
        jTextFieldGrossRegularPaymentAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 10;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldGrossRegularPaymentAmount, gridBagConstraints3);

        jTextFieldPAYGRegularAmount.setEditable(false);
        jTextFieldPAYGRegularAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 11;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldPAYGRegularAmount, gridBagConstraints3);

        jTextFieldNetRegularPaymentAmount.setEditable(false);
        jTextFieldNetRegularPaymentAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 12;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldNetRegularPaymentAmount, gridBagConstraints3);

        jLabel4.setText("First Year Minimum");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel4, gridBagConstraints3);

        jLabel5.setText("First Year Maximum");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel5, gridBagConstraints3);

        jLabel1.setText("Regular Rebate Amount");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 9;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel1, gridBagConstraints3);

        jTextFieldRegularRebateAmount.setEditable(false);
        jTextFieldRegularRebateAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 9;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jTextFieldRegularRebateAmount, gridBagConstraints3);

        jPanelRight.add(jPanel2);

        jPanelInvestmentStrategy.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints4;

        jPanelInvestmentStrategy.setBorder(new javax.swing.border.TitledBorder(
                "Estimated Earnings"));

        jLabelInvestmentStrategy.setText("Investment Strategy");
        jLabelInvestmentStrategy.setPreferredSize(NAME_DIM);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelInvestmentStrategy.add(jLabelInvestmentStrategy,
                gridBagConstraints4);

        jLabelAnnualIncomeRate.setText("Annual Income");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelInvestmentStrategy.add(jLabelAnnualIncomeRate,
                gridBagConstraints4);

        jLabelAnnualGrowthRate.setText("Annual Capital Growth");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelInvestmentStrategy.add(jLabelAnnualGrowthRate,
                gridBagConstraints4);

        jLabelTotalAnnualReturnRate.setText("Total Annual Return");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanelInvestmentStrategy.add(jLabelTotalAnnualReturnRate,
                gridBagConstraints4);

        jComboBoxInvestmentStrategy.setPreferredSize(VALUE_DIM);
        jComboBoxInvestmentStrategy
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxInvestmentStrategyItemStateChanged(evt);
                    }
                });

        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelInvestmentStrategy.add(jComboBoxInvestmentStrategy,
                gridBagConstraints4);

        jTextFieldAnnualIncomeRate.setEditable(false);
        jTextFieldAnnualIncomeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelInvestmentStrategy.add(jTextFieldAnnualIncomeRate,
                gridBagConstraints4);

        jTextFieldAnnualGrowthRate.setEditable(false);
        jTextFieldAnnualGrowthRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelInvestmentStrategy.add(jTextFieldAnnualGrowthRate,
                gridBagConstraints4);

        jTextFieldTotalAnnualReturnRate.setEditable(false);
        jTextFieldTotalAnnualReturnRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelInvestmentStrategy.add(jTextFieldTotalAnnualReturnRate,
                gridBagConstraints4);

        jPanelRight.add(jPanelInvestmentStrategy);

        jPanelAllocatedPension.add(jPanelRight);

        jTabbedPane.addTab("Allocated Pension", jPanelAllocatedPension);

        add(jTabbedPane);

        jPanelControls2.setLayout(new javax.swing.BoxLayout(jPanelControls2,
                javax.swing.BoxLayout.X_AXIS));

        jPanelControls2.add(jPanelCloseSave);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.setDefaultCapable(false);
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

        jPanelControls2.add(jPanelPrevNext);

        add(jPanelControls2);

    }

    private void jCheckBoxIndexRateItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jCheckBoxIndexRate)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            ((AllocatedPensionCalc) etpCalc).setIndexed(true);
            SwingUtil.setEnabled(jTextFieldIndexRate, true);
        } else {
            ((AllocatedPensionCalc) etpCalc).setIndexed(false);
            SwingUtil.setEnabled(jTextFieldIndexRate, false);
        }
    }

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);

        // refresh chart
        if (index == RESULT_PAGE_ID) {
            graphView.jPanelOthers.add(jPanelInvestmentStrategy);
            updateChart();
        } else {
            jPanelRight.add(jPanelInvestmentStrategy);
        }
    }

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }

    private void jRadioButtonInvalidityYesItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jRadioButtonInvalidityYes)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        boolean b = evt.getStateChange() == java.awt.event.ItemEvent.SELECTED;
        ((AllocatedPensionCalc) etpCalc).setInvalidity(b ? Boolean.TRUE
                : Boolean.FALSE);
    }

    private void jRadioButtonInvalidityNoItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jRadioButtonInvalidityNo)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        boolean b = evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED;
        ((AllocatedPensionCalc) etpCalc).setInvalidity(b ? Boolean.TRUE
                : Boolean.FALSE);
    }

    private void jComboBoxClientSexCodeItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jComboBoxClientSexCode)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            ((AllocatedPensionCalc) etpCalc).setSexCodeID(null);
        else
            ((AllocatedPensionCalc) etpCalc).setSexCodeID(new SexCode()
                    .getCodeID(s));
    }

    private void jRadioButtonPre071983YesItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jRadioButtonPre071983Yes)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        boolean b = evt.getStateChange() == java.awt.event.ItemEvent.SELECTED;
        ((AllocatedPensionCalc) etpCalc).setPre071983(b ? Boolean.TRUE
                : Boolean.FALSE);
    }

    private void jRadioButtonPre071983NoItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jRadioButtonPre071983No)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        boolean b = evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED;
        ((AllocatedPensionCalc) etpCalc).setPre071983(b ? Boolean.TRUE
                : Boolean.FALSE);
    }

    private void jComboBoxPensionReversionOptionItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jComboBoxPensionReversionOption)
            return;
        JComboBox cb = (JComboBox) evt.getSource();
        String s = (String) cb.getSelectedItem();
        Integer id = null;
        if (s != null && s.length() > 0)
            id = new ReversionaryCode().getCodeID(s);

        ((AllocatedPensionCalc) etpCalc).setPensionReversionOptionID(id);

        SwingUtil.setEnabled(jComboBoxPartnerSexCode, ReversionaryCode.YES
                .equals(id));
        SwingUtil.setEnabled(jTextFieldPartnerDOB, ReversionaryCode.YES
                .equals(id));
    }

    private void jComboBoxPartnerSexCodeItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jComboBoxPartnerSexCode)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            ((AllocatedPensionCalc) etpCalc).setPartnerSexCodeID(null);
        else
            ((AllocatedPensionCalc) etpCalc).setPartnerSexCodeID(new SexCode()
                    .getCodeID(s));
    }

    private void jComboBoxRequiredPaymentTypeItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jComboBoxRequiredPaymentType)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        Integer id = null;
        if (s != null && s.length() > 0)
            id = new PaymentType().getCodeID(s);

        ((AllocatedPensionCalc) etpCalc).setRequiredPaymentTypeID(id);

        if (PaymentType.NET.equals(id) || PaymentType.GROSS.equals(id)) {
            SwingUtil.setEnabled(jTextFieldRequiredPaymentAmount, true);
        } else if (PaymentType.MINIMUM.equals(id)
                || PaymentType.MAXIMUM.equals(id)) {
            SwingUtil.setEnabled(jTextFieldRequiredPaymentAmount, false);
        }

    }

    private void jComboBoxRequiredFrequencyItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jComboBoxRequiredFrequency)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            ((AllocatedPensionCalc) etpCalc).setRequiredFrequencyCodeID(null);
        else
            ((AllocatedPensionCalc) etpCalc)
                    .setRequiredFrequencyCodeID(new FrequencyCode()
                            .getCodeID(s));
    }

    private void jComboBoxInvestmentStrategyItemStateChanged(
            java.awt.event.ItemEvent evt) {
        if (evt.getSource() != jComboBoxInvestmentStrategy)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            ((AllocatedPensionCalc) etpCalc).setInvestmentStrategyCodeID(null);
        else {
            ((AllocatedPensionCalc) etpCalc)
                    .setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                            .getCodeID(s));
            updateChart();
        }
    }

    private boolean warn = false;

    private void jTextFieldRebateableRateFocusLost(java.awt.event.FocusEvent evt) {
        if (warn)
            return;
        if (etpCalc.getAge() >= TaxUtils.TAX_EFFECTIVE_AGE)
            return;
        if (Percent.getPercentInstance().doubleValue(
                jTextFieldRebateableRate.getText()) <= 0)
            return;
        // if ( ( ( AllocatedPensionCalc ) etpCalc ).getRebateableRate() <= 0 )
        // return;

        warn = true;
        JOptionPane
                .showMessageDialog(
                        this,
                        "The rebate can only be used for those under "
                                + TaxUtils.TAX_EFFECTIVE_AGE
                                + "\nwhere the pension is a disability pension"
                                + "\nor payable because of the death of another pension.",
                        "ClientView is under " + TaxUtils.TAX_EFFECTIVE_AGE,
                        JOptionPane.WARNING_MESSAGE);

        jTextFieldEntryFeeRate.requestFocus();
        // jTextFieldRebateableRate.getNextFocusableComponent().requestFocus();
    }

    // Singleton pattern
    private static AllocatedPensionView view;

    public static AllocatedPensionView displayAP(String title,
            java.awt.event.FocusListener[] listeners) {

        if (view == null) {
            view = new AllocatedPensionView();
            SwingUtil.add2Frame(view, listeners, view.getDefaultTitle(),
                    ViewSettings.getInstance().getViewImage(
                            view.getClass().getName()), true, true, false);
        }

        try {
            view.updateView(title);
            SwingUtil.setVisible(view, true);
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            view = null;
        }

        return view;

    }

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup bgPre071983Pension;

    private javax.swing.ButtonGroup bgInvalidity;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JPanel jPanelAllocatedPension;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JLabel jLabel44;

    private javax.swing.JLabel jLabel45;

    private javax.swing.JLabel jLabel46;

    private javax.swing.JLabel jLabel47;

    private javax.swing.JLabel jLabel48;

    private javax.swing.JLabel jLabel49;

    private javax.swing.JLabel jLabel50;

    private javax.swing.JLabel jLabel51;

    private javax.swing.JLabel jLabel52;

    private javax.swing.JComboBox jComboBoxClientSexCode;

    private javax.swing.JTextField jTextFieldCommencementDate;

    private javax.swing.JTextField jTextFieldPurchasePrice;

    private javax.swing.JRadioButton jRadioButtonPre071983No;

    private javax.swing.JRadioButton jRadioButtonPre071983Yes;

    private javax.swing.JRadioButton jRadioButtonInvalidityNo;

    private javax.swing.JRadioButton jRadioButtonInvalidityYes;

    private javax.swing.JTextField jTextFieldEntryFeeRate;

    private javax.swing.JTextField jTextFieldRebateableRate;

    private javax.swing.JTextField jTextFieldATOUPPAmount;

    private javax.swing.JTextField jTextFieldAnnualDeductibleAmount;

    private javax.swing.JTextField jTextFieldFirstYearDeductibleAmount;

    private javax.swing.JComboBox jComboBoxPensionReversionOption;

    private javax.swing.JComboBox jComboBoxPartnerSexCode;

    private javax.swing.JTextField jTextFieldPartnerDOB;

    private javax.swing.JTextField jTextFieldPartnerAge;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JLabel jLabel62;

    private javax.swing.JLabel jLabel63;

    private javax.swing.JTextField jTextFieldDSSUPPAmount;

    private javax.swing.JTextField jTextFieldAnnualNonAssesableAmount;

    private javax.swing.JTextField jTextFieldFirstYearNonAssesableAmount;

    private javax.swing.JLabel jLabel64;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel jLabel53;

    private javax.swing.JLabel jLabel54;

    private javax.swing.JLabel jLabel55;

    private javax.swing.JLabel jLabel56;

    private javax.swing.JLabel jLabel57;

    private javax.swing.JLabel jLabel58;

    private javax.swing.JLabel jLabel59;

    private javax.swing.JLabel jLabel60;

    private javax.swing.JLabel jLabel61;

    private javax.swing.JComboBox jComboBoxRequiredPaymentType;

    private javax.swing.JTextField jTextFieldRequiredPaymentAmount;

    private javax.swing.JComboBox jComboBoxRequiredFrequency;

    private javax.swing.JCheckBox jCheckBoxIndexRate;

    private javax.swing.JTextField jTextFieldIndexRate;

    private javax.swing.JTextField jTextFieldAnnualMinimumAmount;

    private javax.swing.JTextField jTextFieldAnnualMaximumAmount;

    private javax.swing.JTextField jTextFieldFirstYearMinimum;

    private javax.swing.JTextField jTextFieldFirstYearMaximum;

    private javax.swing.JTextField jTextFieldRebateAmount;

    private javax.swing.JTextField jTextFieldGrossRegularPaymentAmount;

    private javax.swing.JTextField jTextFieldPAYGRegularAmount;

    private javax.swing.JTextField jTextFieldNetRegularPaymentAmount;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField jTextFieldRegularRebateAmount;

    private javax.swing.JPanel jPanelInvestmentStrategy;

    private javax.swing.JLabel jLabelInvestmentStrategy;

    private javax.swing.JLabel jLabelAnnualIncomeRate;

    private javax.swing.JLabel jLabelAnnualGrowthRate;

    private javax.swing.JLabel jLabelTotalAnnualReturnRate;

    private javax.swing.JComboBox jComboBoxInvestmentStrategy;

    private javax.swing.JTextField jTextFieldAnnualIncomeRate;

    private javax.swing.JTextField jTextFieldAnnualGrowthRate;

    private javax.swing.JTextField jTextFieldTotalAnnualReturnRate;

    private javax.swing.JPanel jPanelControls2;

    private javax.swing.JPanel jPanelPrevNext;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.JButton jButtonNext;

    // End of variables declaration

    /**
     * Viewble interface
     */
    public Integer getObjectType() {
        return null;
    }

    public void clearView() {
        super.clearView();

        warn = false;
    }

    public void updateView(PersonService person)
            throws ServiceException {

        super.updateView(person);

        // load data from person, e.g. DOB
        if (person == null)
            return;

        try {
            // etpCalc.disableUpdate();

            // partner
            PersonService partner = ((ClientService) person).getPartner(false);
            if (partner != null) {
                AllocatedPensionCalc apCalc = (AllocatedPensionCalc) etpCalc;
                IPerson personName = partner.getPersonName();
                apCalc.setPartnerDateOfBirth(personName.getDateOfBirth());
                apCalc.setPartnerSexCodeID(personName.getSex().getId().intValue());
            }

        } finally {
            updateEditable();
            etpCalc.doUpdate();
            // etpCalc.enableUpdate();
        }

    }

    /**
     * helper methods
     */
    public void updateEditable() {

        super.updateEditable();

        double d;
        String s;
        Percent percent = Percent.getPercentInstance();
        Currency curr = Currency.getCurrencyInstance();

        updateOthers();

        d = ((AllocatedPensionCalc) etpCalc).getEntryFeeRate();
        jTextFieldEntryFeeRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getRebateableRate();
        jTextFieldRebateableRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = etpCalc.getIndexRate();
        jTextFieldIndexRate.setText(d < 0 ? null : percent.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getRequiredPaymentAmount();
        jTextFieldRequiredPaymentAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        s = DateTimeUtils.asString(((AllocatedPensionCalc) etpCalc)
                .getPartnerDateOfBirth());
        jTextFieldPartnerDOB.setText(s);

    }

    private void updateOthers() {

        jComboBoxClientSexCode.setSelectedItem(((AllocatedPensionCalc) etpCalc)
                .getClientSexCodeDesc());

        if (((AllocatedPensionCalc) etpCalc).getPre071983() == null) {
            jRadioButtonPre071983No.setSelected(false);
            jRadioButtonPre071983Yes.setSelected(false);
        } else {
            if (((AllocatedPensionCalc) etpCalc).getPre071983().booleanValue())
                jRadioButtonPre071983Yes.setSelected(true);
            else
                jRadioButtonPre071983No.setSelected(true);
        }

        if (((AllocatedPensionCalc) etpCalc).getInvalidity() == null) {
            jRadioButtonInvalidityNo.setSelected(false);
            jRadioButtonInvalidityYes.setSelected(false);
        } else {
            if (((AllocatedPensionCalc) etpCalc).getInvalidity().booleanValue())
                jRadioButtonInvalidityYes.setSelected(true);
            else
                jRadioButtonInvalidityNo.setSelected(true);
        }

        jComboBoxPensionReversionOption
                .setSelectedItem(((AllocatedPensionCalc) etpCalc)
                        .getPensionReversionOptionDesc());
        jComboBoxPartnerSexCode
                .setSelectedItem(((AllocatedPensionCalc) etpCalc)
                        .getPartnerSexCodeDesc());

        jComboBoxRequiredPaymentType
                .setSelectedItem(((AllocatedPensionCalc) etpCalc)
                        .getRequiredPaymentTypeDesc());
        Integer paymentTypeID = ((AllocatedPensionCalc) etpCalc)
                .getRequiredPaymentTypeID();
        SwingUtil.setEnabled(jTextFieldRequiredPaymentAmount, PaymentType.NET
                .equals(paymentTypeID)
                || PaymentType.GROSS.equals(paymentTypeID));

        jCheckBoxIndexRate.setSelected(etpCalc.isIndexed());
        SwingUtil.setEnabled(jTextFieldIndexRate, jCheckBoxIndexRate
                .isSelected());

        jComboBoxRequiredFrequency
                .setSelectedItem(((AllocatedPensionCalc) etpCalc)
                        .getRequiredFrequencyCodeDesc());

        jComboBoxInvestmentStrategy
                .setSelectedItem(((AllocatedPensionCalc) etpCalc)
                        .getInvestmentStrategyCodeDesc());

    }

    public void updateNonEditable() {

        super.updateNonEditable();

        int n;
        double d;
        String s = null;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        updateOthers();

        Integer paymentTypeID = ((AllocatedPensionCalc) etpCalc)
                .getRequiredPaymentTypeID();
        if (PaymentType.MAXIMUM.equals(paymentTypeID)
                || PaymentType.MINIMUM.equals(paymentTypeID)) {
            d = ((AllocatedPensionCalc) etpCalc).getRequiredPaymentAmount();
            jTextFieldRequiredPaymentAmount
                    .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                            .toString(d));
        }

        s = DateTimeUtils.asString(((AllocatedPensionCalc) etpCalc)
                .getCommencementDate());
        jTextFieldCommencementDate.setText(s);

        d = ((AllocatedPensionCalc) etpCalc).getPurchasePrice();
        jTextFieldPurchasePrice.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        // partner
        n = ((AllocatedPensionCalc) etpCalc).getPartnerAge();
        jTextFieldPartnerAge.setText(n < 0 ? null : "" + n);

        d = ((AllocatedPensionCalc) etpCalc).getATOUPPAmount();
        jTextFieldATOUPPAmount.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getATOAnnualDeductibleAmount();
        jTextFieldAnnualDeductibleAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).geATOtFirstYearDeductibleAmount();
        jTextFieldFirstYearDeductibleAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getDSSUPPAmount();
        jTextFieldDSSUPPAmount.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getAnnualNonAssesableAmount();
        jTextFieldAnnualNonAssesableAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getFirstYearNonAssesableAmount();
        jTextFieldFirstYearNonAssesableAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        updateAnnualMinMaxAmount();

        d = ((AllocatedPensionCalc) etpCalc).getRegularRebateAmount();
        jTextFieldRegularRebateAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getRebateAmount();
        jTextFieldRebateAmount.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getGrossRegularPaymentAmount();
        jTextFieldGrossRegularPaymentAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getPAYGRegularAmount();
        jTextFieldPAYGRegularAmount.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getNetRegularPaymentAmount();
        jTextFieldNetRegularPaymentAmount
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getAnnualIncomeRate();
        jTextFieldAnnualIncomeRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getAnnualCapitalGrowthRate();
        jTextFieldAnnualGrowthRate.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : percent.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getTotalAnnualReturnRate();
        jTextFieldTotalAnnualReturnRate
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : percent
                        .toString(d));

        // updateComponents();
        // updateChart();

    }

    private void updateAnnualMinMaxAmount() {

        double d;
        Currency curr = Currency.getCurrencyInstance();

        d = ((AllocatedPensionCalc) etpCalc).getAnnualMinimumAmount();
        jTextFieldAnnualMinimumAmount.setText(curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getAnnualMaximumAmount();
        jTextFieldAnnualMaximumAmount.setText(curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getFirstYearMinimumAmount();
        jTextFieldFirstYearMinimum.setText(curr.toString(d));

        d = ((AllocatedPensionCalc) etpCalc).getFirstYearMaximumAmount();
        jTextFieldFirstYearMaximum.setText(curr.toString(d));

    }

}
