/*
 * ETPRolloverView.java
 *
 * Created on 29 November 2001, 11:47
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.Cursor;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import com.argus.financials.api.ETPConstants;
import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.PersonName;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.projection.AllocatedPensionCalcNew;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.DocumentUtils;
import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.report.data.ETPData;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.CalcDateInputVerifier;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.PostThresholdUsedInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.AbstractPanel;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.io.IOUtils2;
import com.argus.util.DateTimeUtils;

public class ETPRolloverViewNew
    extends AbstractPanel
    implements
        ActionEventID, javax.swing.event.ChangeListener,
        com.argus.financials.swing.ICloseDialog {
    static final String STRING_ZERO = "0";

    static final String STRING_EMPTY = "";

    protected ETPCalcNew etpCalc;

    protected ETPData data;

    private java.awt.event.FocusListener[] listeners;

    private boolean newModel;

    /** Creates new form ETPCalc */
    public ETPRolloverViewNew() {
        this(new ETPCalcNew());

    }

    public ETPRolloverViewNew(MoneyCalc calc) {
        etpCalc = (ETPCalcNew) calc;

        initComponents();
        initComponents2();
    }

    protected Integer getDefaultType() {
        return ModelTypeID.rcETP_ROLLOVER.getCodeId();
    }

    protected String getDefaultTitle() {
        return ModelTypeID.rcETP_ROLLOVER.getDescription();
    }

    protected Model getModel() {
        Model model = etpCalc.getModel();
        if (model.getOwner() != null)
            return model;

        PersonService person = clientService;
        if (person != null) {
            try {
                model.setOwner(person.getModels());
            } catch (ServiceException e) {
                e.printStackTrace(System.err);
            }
        }
        return model;
    }

    public MoneyCalc getCalculationModel() {
        return etpCalc;
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void updateTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    public void setTitle(String value) {
        try {
            getModel().setTitle(value);
        } catch (DuplicateException e) {
            String msg = "Failed to set model title.\n  Currency.getCurrencyInstance()ent Title: "
                    + getTitle() + "\n  New Title: " + value;
            JOptionPane.showMessageDialog(this, msg,
                    "Title already used by another model",
                    JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException(msg);
        } catch (ModelTitleRestrictionException me) {
            String msg = "Failed to set model title.\n  Please ensure total characters for model title is 3 or more.";
            JOptionPane.showMessageDialog(this, msg, "Invalid title",
                    JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException(msg);

        }
        SwingUtil.setTitle(this, value);
    }

    public String getViewCaption() {
        return "ETP Rollover";
    }

    private void initComponents2() {

        // for report
        data = new ETPData();
        _setAccessibleContext();

        etpCalc.addChangeListener(this);
        DocumentUtils.addListener(this, etpCalc); // after
                                                    // _setAccessibleContext()
        updateEditable();
        updateRadioButtons();
        updateETPComponents();
        updateComponents();
        _setComponentStatus();

        jButtonDelete.setEnabled(jButtonSave.isEnabled());
        buttonGroup1.setSelected(null, true);
        setActionMap();

    }

    private void _setAccessibleContext() {

        jRadioButtonIsClient.getAccessibleContext().setAccessibleName(
                DocumentNames.IS_CLIENT);
        jTextFieldPartnerName.getAccessibleContext().setAccessibleName(
                DocumentNames.NAME_PARTNER);
        jTextFieldDOBPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.DOB_PARTNER);

        jTextFieldClientName.getAccessibleContext().setAccessibleName(
                DocumentNames.CLIENT_NAME);
        jTextFieldDOB.getDateField().getAccessibleContext().setAccessibleName(
                DocumentNames.DOB);
        jTextFieldEligibleServiceDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.ELIGIBLE_SERVICE_DATE);
        jTextFieldCalculationDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.CALCULATION_DATE);
        jTextFieldTotalETP.getAccessibleContext().setAccessibleName(
                DocumentNames.TOTAL_ETP_AMOUNT);
        jTextFieldTaxRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_RATE);

        jTextFieldPost061983UntaxedTotal.getAccessibleContext()
                .setAccessibleName(DocumentNames.POST_JUNE_1983_UNTAXED);

        jTextFieldPost061983ThresholdUsedEn.getAccessibleContext()
                .setAccessibleName(DocumentNames.POST_061983_THRESHOLD_USED);

        jTextFieldCGTExemptTotal.getAccessibleContext().setAccessibleName(
                DocumentNames.CGT_EXEMPT);
        jTextFieldUndeductedTotal.getAccessibleContext().setAccessibleName(
                DocumentNames.UNDEDUCTED);
        jTextFieldConcessionalTotal.getAccessibleContext().setAccessibleName(
                DocumentNames.CONCESSIONAL);
        jTextFieldInvalidityTotal.getAccessibleContext().setAccessibleName(
                DocumentNames.POST_JUNE_94_INVALIDITY);
        jTextFieldExcessTotal.getAccessibleContext().setAccessibleName(
                DocumentNames.EXCESS);

        jRadioButtonPartialWithdraw.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTIAL_WITHDRAW_OPTION);
        jRadioButtonRolloverAll.getAccessibleContext().setAccessibleName(
                DocumentNames.ROLLOVER_ALL_OPTION);
        jRadioButtonWithdrawAll.getAccessibleContext().setAccessibleName(
                DocumentNames.WITHDRAW_ALL_OPTION);
        jRadioButtonWithdrawUpToPost30June1983Threshold.getAccessibleContext()
                .setAccessibleName(
                        DocumentNames.WITHDRAW_UP_TO_THRESHOLD_OPTION);
        jRadioButton1.getAccessibleContext().setAccessibleName(
                DocumentNames.NO_OPTION);
        jCheckBoxRollover.getAccessibleContext().setAccessibleName(
                DocumentNames.ROLLOVER_TO_ALLOCATED_PENSION);
        jCheckBoxRolloverRecon.getAccessibleContext().setAccessibleName(
                DocumentNames.ROLLOVER_AND_RECONTRIBUTE_TO_ALLOCATED_PENSION);

        // for partial withdraw
        jTextFieldRollover.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_PARTIAL_WITHDRAWAL_ROLLOVER);
        jTextFieldUndeductedPartial.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_PARTIAL_WITHDRAWAL_UNDEDUCTED);
        jTextFieldCGTPartial.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_PARTIAL_WITHDRAWAL_CGT_EXEMPT);
        jTextFieldExcessPartial.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_PARTIAL_WITHDRAWAL_EXCESS);
        jTextFieldConcessionalPartial.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_PARTIAL_WITHDRAWAL_CONCESSIONAL);
        jTextFieldInvalidityPartial.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_PARTIAL_WITHDRAWAL_INVALIDITY);

    }

    private void _setComponentStatus() {
        jPanel3.setVisible(false);
    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        updateNonEditable();
        updateComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanelOriginalData = new javax.swing.JPanel();
        jLabelCalculationDate = new javax.swing.JLabel();
        jTextFieldDOB = new com.argus.bean.FDateChooser();
        jLabelEligibleServiceDate = new javax.swing.JLabel();
        jTextFieldEligibleServiceDate = new com.argus.bean.FDateChooser();
        jLabelTaxRate = new javax.swing.JLabel();
        jTextFieldTaxRate = new javax.swing.JTextField();
        jLabelPost30June1983ThresholdUsed = new javax.swing.JLabel();
        jTextFieldPost061983ThresholdUsedEn = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldCalculationDate = new com.argus.bean.FDateChooser();
        jLabelTotalETP = new javax.swing.JLabel();
        jTextFieldTotalETP = new javax.swing.JTextField();
        Age = new javax.swing.JLabel();
        jTextFieldAge = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPreDays = new javax.swing.JTextField();
        jTextFieldPostDays = new javax.swing.JTextField();
        jTextFieldClientName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldPost061983ThresholdUsedUn = new javax.swing.JPasswordField();
        jTextFieldPartnerName = new javax.swing.JTextField();
        jTextFieldDOBPartner = new com.argus.bean.FDateChooser();
        jPanel19 = new javax.swing.JPanel();
        jRadioButtonIsClient = new javax.swing.JRadioButton();
        jRadioButtonIsPartner = new javax.swing.JRadioButton();
        jPanelFormat = new javax.swing.JPanel();
        jRadioButtonRolloverAll = new javax.swing.JRadioButton();
        jRadioButtonWithdrawAll = new javax.swing.JRadioButton();
        jRadioButtonPartialWithdraw = new javax.swing.JRadioButton();
        jCheckBoxRollover = new javax.swing.JCheckBox();
        jRadioButtonWithdrawUpToPost30June1983Threshold = new javax.swing.JRadioButton();
        jCheckBoxRolloverRecon = new javax.swing.JCheckBox();
        jPanelETPAllocation = new javax.swing.JPanel();
        jLabelPreJuly1983 = new javax.swing.JLabel();
        jLabelPostJune1983Taxed = new javax.swing.JLabel();
        jLabelPostJune1983Untaxed = new javax.swing.JLabel();
        jLabelUndeducted = new javax.swing.JLabel();
        jLabelCGTExempt = new javax.swing.JLabel();
        jLabelExcess = new javax.swing.JLabel();
        jLabelConcessional = new javax.swing.JLabel();
        jLabelInvalidity = new javax.swing.JLabel();
        jLabelTotalAmount = new javax.swing.JLabel();
        jTextFieldPost061983UntaxedTotal = new javax.swing.JTextField();
        jTextFieldUndeductedTotal = new javax.swing.JTextField();
        jTextFieldCGTExemptTotal = new javax.swing.JTextField();
        jTextFieldExcessTotal = new javax.swing.JTextField();
        jTextFieldConcessionalTotal = new javax.swing.JTextField();
        jTextFieldInvalidityTotal = new javax.swing.JTextField();
        jTextFieldPre071983Total = new javax.swing.JTextField();
        jTextFieldPost061983TaxedTotal = new javax.swing.JTextField();
        jTextFieldTotalAmountTotal = new javax.swing.JTextField();
        jTextFieldConcessionalRollover = new javax.swing.JTextField();
        jTextFieldInvalidityRollover = new javax.swing.JTextField();
        jTextFieldPre071983Rollover = new javax.swing.JTextField();
        jTextFieldPost061983TaxedRollover = new javax.swing.JTextField();
        jTextFieldPost061983UntaxedRollover = new javax.swing.JTextField();
        jTextFieldUndeductedRollover = new javax.swing.JTextField();
        jTextFieldCGTExemptRollover = new javax.swing.JTextField();
        jTextFieldExcessRollover = new javax.swing.JTextField();
        jTextFieldTotalAmountRollover = new javax.swing.JTextField();
        jTextFieldConcessionalWithdraw = new javax.swing.JTextField();
        jTextFieldInvalidityWithdraw = new javax.swing.JTextField();
        jTextFieldPre071983Withdraw = new javax.swing.JTextField();
        jTextFieldPost061983TaxedWithdraw = new javax.swing.JTextField();
        jTextFieldPost061983UntaxedWithdraw = new javax.swing.JTextField();
        jTextFieldUndeductedWithdraw = new javax.swing.JTextField();
        jTextFieldCGTExemptWithdraw = new javax.swing.JTextField();
        jTextFieldExcessWithdraw = new javax.swing.JTextField();
        jTextFieldTotalAmountWithdraw = new javax.swing.JTextField();
        jTextFieldConcessionalTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldInvalidityTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldPre071983TaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldPost061983TaxedTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldPost061983UntaxedTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldUndeductedTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldCGTExemptTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldExcessTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldTotalAmountTaxOnWithdrawal = new javax.swing.JTextField();
        jTextFieldConcessionalNetETP = new javax.swing.JTextField();
        jTextFieldInvalidityNetETP = new javax.swing.JTextField();
        jTextFieldPre071983NetETP = new javax.swing.JTextField();
        jTextFieldPost061983TaxedNetETP = new javax.swing.JTextField();
        jTextFieldPost061983UntaxedNetETP = new javax.swing.JTextField();
        jTextFieldUndeductedNetETP = new javax.swing.JTextField();
        jTextFieldCGTExemptNetETP = new javax.swing.JTextField();
        jTextFieldExcessNetETP = new javax.swing.JTextField();
        jTextFieldTotalAmountNetETP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldPreJuly1983TaxOnRollover = new javax.swing.JTextField();
        jTextFieldPostJune1983TaxedTaxOnRollover = new javax.swing.JTextField();
        jTextFieldPostJune1983UntaxedTaxOnRollover = new javax.swing.JTextField();
        jTextFieldUndeductedTaxOnRollover = new javax.swing.JTextField();
        jTextFieldCGTExemptTaxOnRollover = new javax.swing.JTextField();
        jTextFieldExcessTaxOnRollover = new javax.swing.JTextField();
        jTextFieldConcessionalTaxOnRollover = new javax.swing.JTextField();
        jTextFieldInvalidityTaxOnRollover = new javax.swing.JTextField();
        jTextFieldTotalAmountTaxOnRollover = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jPanelControls = new javax.swing.JPanel();
        jPanelCloseSave = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jButtonPensionProjection = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldRollover = new javax.swing.JTextField();
        jTextFieldUndeductedPartial = new javax.swing.JTextField();
        jTextFieldCGTPartial = new javax.swing.JTextField();
        jTextFieldExcessPartial = new javax.swing.JTextField();
        jTextFieldConcessionalPartial = new javax.swing.JTextField();
        jTextFieldInvalidityPartial = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(750, 500));
        setMinimumSize(new java.awt.Dimension(750, 500));
        setMaximumSize(new java.awt.Dimension(750, 500));
        jPanelOriginalData.setLayout(new java.awt.GridBagLayout());

        jPanelOriginalData.setBorder(new javax.swing.border.TitledBorder(
                "ETP Details"));
        jLabelCalculationDate.setText("Calculation Date");
        jLabelCalculationDate.setPreferredSize(new java.awt.Dimension(95, 17));
        jLabelCalculationDate.setMinimumSize(new java.awt.Dimension(90, 17));
        jLabelCalculationDate.setIconTextGap(0);
        jLabelCalculationDate.setMaximumSize(new java.awt.Dimension(90, 17));
        jLabelCalculationDate
                .setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabelCalculationDate, gridBagConstraints);

        jTextFieldDOB
                .setToolTipText("(dd/mm/yyyy)Date could never be before 01/07/1983");
        jTextFieldDOB.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldDOB.setPreferredSize(new java.awt.Dimension(10, 21));
        jTextFieldDOB.setAlignmentY(1.0F);
        jTextFieldDOB.setAlignmentX(1.0F);
        jTextFieldDOB.setMaximumSize(new java.awt.Dimension(10, 10));
        jTextFieldDOB.setRequestFocusEnabled(false);
        jTextFieldDOB.setNextFocusableComponent(jTextFieldTaxRate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldDOB, gridBagConstraints);

        jLabelEligibleServiceDate
                .setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelEligibleServiceDate.setText("Eligible Service Date");
        jLabelEligibleServiceDate.setToolTipText("PreDays: "
                + etpCalc.getPre071983Days() + ",  " + "PostDays: "
                + etpCalc.getPost061983Days());
        jLabelEligibleServiceDate.setIconTextGap(100);
        jLabelEligibleServiceDate
                .setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabelEligibleServiceDate, gridBagConstraints);

        jTextFieldEligibleServiceDate.setInputVerifier(DateInputVerifier
                .getInstance());
        jTextFieldEligibleServiceDate
                .setNextFocusableComponent(jTextFieldPost061983ThresholdUsedEn);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelOriginalData.add(jTextFieldEligibleServiceDate,
                gridBagConstraints);

        jLabelTaxRate.setText("Tax Rate(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jLabelTaxRate, gridBagConstraints);

        jTextFieldTaxRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTaxRate.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldTaxRate.setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldTaxRate.setMinimumSize(new java.awt.Dimension(50, 21));
        jTextFieldTaxRate.setNextFocusableComponent(jTextFieldCalculationDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelOriginalData.add(jTextFieldTaxRate, gridBagConstraints);

        jLabelPost30June1983ThresholdUsed
                .setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelPost30June1983ThresholdUsed
                .setText("Post 30/06/1983 Threshold Used");
        jLabelPost30June1983ThresholdUsed
                .setToolTipText("This is accessible if client's age is over 55!");
        jLabelPost30June1983ThresholdUsed
                .setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabelPost30June1983ThresholdUsed,
                gridBagConstraints);

        jTextFieldPost061983ThresholdUsedEn
                .setToolTipText("This is accessible if client's age is over 55!");
        jTextFieldPost061983ThresholdUsedEn
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPost061983ThresholdUsedEn.setAlignmentX(0.0F);
        jTextFieldPost061983ThresholdUsedEn
                .setInputVerifier(new PostThresholdUsedInputVerifier());
        jTextFieldPost061983ThresholdUsedEn
                .setNextFocusableComponent(jTextFieldTotalETP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelOriginalData.add(jTextFieldPost061983ThresholdUsedEn,
                gridBagConstraints);

        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setText("DOB");
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabel2, gridBagConstraints);

        jTextFieldCalculationDate.setInputVerifier(CalcDateInputVerifier
                .getInstance());
        jTextFieldCalculationDate
                .setNextFocusableComponent(jTextFieldEligibleServiceDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldCalculationDate, gridBagConstraints);

        jLabelTotalETP.setText("Total ETP");
        jLabelTotalETP
                .setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabelTotalETP, gridBagConstraints);

        jTextFieldTotalETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalETP.setInputVerifier(new TotalETPAmountInputVerifier(
                etpCalc));
        jTextFieldTotalETP
                .setNextFocusableComponent(jTextFieldPost061983UntaxedTotal);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldTotalETP, gridBagConstraints);

        Age.setText("Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(Age, gridBagConstraints);

        jTextFieldAge.setEditable(false);
        jTextFieldAge.setFont(new java.awt.Font("SansSerif", 1, 11));
        jTextFieldAge.setBackground(java.awt.Color.lightGray);
        jTextFieldAge.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAge.setPreferredSize(new java.awt.Dimension(40, 21));
        jTextFieldAge.setMinimumSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldAge, gridBagConstraints);

        jLabel3.setText("Pre Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Post Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabel4, gridBagConstraints);

        jTextFieldPreDays.setEditable(false);
        jTextFieldPreDays
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPreDays.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldPreDays.setMinimumSize(new java.awt.Dimension(20, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldPreDays, gridBagConstraints);

        jTextFieldPostDays.setEditable(false);
        jTextFieldPostDays
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldPostDays, gridBagConstraints);

        jTextFieldClientName
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientName.setNextFocusableComponent(jTextFieldDOB);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldClientName, gridBagConstraints);

        jLabel5.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jLabel5, gridBagConstraints);

        jTextFieldPost061983ThresholdUsedUn.setEditable(false);
        jTextFieldPost061983ThresholdUsedUn
                .setBackground(java.awt.Color.lightGray);
        jTextFieldPost061983ThresholdUsedUn
                .setPreferredSize(new java.awt.Dimension(30, 20));
        jTextFieldPost061983ThresholdUsedUn
                .setMinimumSize(new java.awt.Dimension(30, 20));
        jTextFieldPost061983ThresholdUsedUn.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelOriginalData.add(jTextFieldPost061983ThresholdUsedUn,
                gridBagConstraints);

        jTextFieldPartnerName
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerName.setNextFocusableComponent(jTextFieldDOB);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldPartnerName, gridBagConstraints);

        jTextFieldDOBPartner
                .setToolTipText("(dd/mm/yyyy)Date could never be before 01/07/1983");
        jTextFieldDOBPartner.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldDOBPartner.setPreferredSize(new java.awt.Dimension(10, 21));
        jTextFieldDOBPartner.setAlignmentY(1.0F);
        jTextFieldDOBPartner.setAlignmentX(1.0F);
        jTextFieldDOBPartner.setMaximumSize(new java.awt.Dimension(10, 10));
        jTextFieldDOBPartner.setRequestFocusEnabled(false);
        jTextFieldDOBPartner.setNextFocusableComponent(jTextFieldTaxRate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOriginalData.add(jTextFieldDOBPartner, gridBagConstraints);

        jPanel19.setLayout(new java.awt.GridBagLayout());

        jRadioButtonIsClient.setText("For ClientView");
        buttonGroup2.add(jRadioButtonIsClient);
        jRadioButtonIsClient.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonIsClientItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel19.add(jRadioButtonIsClient, gridBagConstraints);

        jRadioButtonIsPartner.setText("For Partner");
        buttonGroup2.add(jRadioButtonIsPartner);
        jRadioButtonIsPartner
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonIsClientItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel19.add(jRadioButtonIsPartner, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelOriginalData.add(jPanel19, gridBagConstraints);

        add(jPanelOriginalData);

        jPanelFormat.setLayout(new java.awt.GridBagLayout());

        jPanelFormat.setBorder(new javax.swing.border.TitledBorder("Options"));
        jRadioButtonRolloverAll.setText("Rollover All");
        buttonGroup1.add(jRadioButtonRolloverAll);
        jRadioButtonRolloverAll
                .setPreferredSize(new java.awt.Dimension(80, 21));
        jRadioButtonRolloverAll
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonRolloverAllItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 10);
        jPanelFormat.add(jRadioButtonRolloverAll, gridBagConstraints);

        jRadioButtonWithdrawAll.setText("Withdraw All");
        buttonGroup1.add(jRadioButtonWithdrawAll);
        jRadioButtonWithdrawAll
                .setPreferredSize(new java.awt.Dimension(88, 25));
        jRadioButtonWithdrawAll
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonWithdrawAllItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelFormat.add(jRadioButtonWithdrawAll, gridBagConstraints);

        jRadioButtonPartialWithdraw.setText("Partial Withdraw");
        buttonGroup1.add(jRadioButtonPartialWithdraw);
        jRadioButtonPartialWithdraw
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPartialWithdrawItemStateChanged(evt);
                    }
                });

        jRadioButtonPartialWithdraw
                .addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jRadioButtonPartialWithdrawMouseClicked(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelFormat.add(jRadioButtonPartialWithdraw, gridBagConstraints);

        jCheckBoxRollover
                .setToolTipText("This is accessible after one of the left options is selected!");
        jCheckBoxRollover.setText("Rollover to Allocated Pension");
        jCheckBoxRollover.setEnabled(false);
        jCheckBoxRollover
                .addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jCheckBoxRolloverStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFormat.add(jCheckBoxRollover, gridBagConstraints);

        jRadioButtonWithdrawUpToPost30June1983Threshold
                .setToolTipText("This is accessible if client's age is over 55!");
        jRadioButtonWithdrawUpToPost30June1983Threshold
                .setText("Withdraw Up to Post 30 June 1983 Threshold");
        buttonGroup1.add(jRadioButtonWithdrawUpToPost30June1983Threshold);
        jRadioButtonWithdrawUpToPost30June1983Threshold
                .setPreferredSize(new java.awt.Dimension(200, 21));
        jRadioButtonWithdrawUpToPost30June1983Threshold
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonWithdrawUpToPost30June1983ThresholdItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        jPanelFormat.add(jRadioButtonWithdrawUpToPost30June1983Threshold,
                gridBagConstraints);

        jCheckBoxRolloverRecon
                .setToolTipText("This is accessible after the left option is selected!");
        jCheckBoxRolloverRecon
                .setText("Rollover & recontribute to Allocated Pension");
        jCheckBoxRolloverRecon.setEnabled(false);
        jCheckBoxRolloverRecon
                .addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jCheckBoxRolloverReconStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFormat.add(jCheckBoxRolloverRecon, gridBagConstraints);

        add(jPanelFormat);

        jPanelETPAllocation.setLayout(new java.awt.GridBagLayout());

        jPanelETPAllocation.setBorder(new javax.swing.border.TitledBorder(
                "ETP Allocation"));
        jLabelPreJuly1983.setText("Pre 1 July 1983");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelPreJuly1983, gridBagConstraints);

        jLabelPostJune1983Taxed.setText("Post 30 June 1983 - (Taxed)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelPostJune1983Taxed, gridBagConstraints);

        jLabelPostJune1983Untaxed
                .setText("                             - (Untaxed)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelPostJune1983Untaxed, gridBagConstraints);

        jLabelUndeducted.setText("Undeducted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelUndeducted, gridBagConstraints);

        jLabelCGTExempt.setText("CGT Exempt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelCGTExempt, gridBagConstraints);

        jLabelExcess.setText("Excess");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelExcess, gridBagConstraints);

        jLabelConcessional.setText("Concessional");
        jLabelConcessional.setPreferredSize(new java.awt.Dimension(140, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelConcessional, gridBagConstraints);

        jLabelInvalidity.setText("Invalidity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelInvalidity, gridBagConstraints);

        jLabelTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelETPAllocation.add(jLabelTotalAmount, gridBagConstraints);

        jTextFieldPost061983UntaxedTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPost061983UntaxedTotal.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldPost061983UntaxedTotal
                .setNextFocusableComponent(jTextFieldUndeductedTotal);
        jTextFieldPost061983UntaxedTotal
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldPost061983UntaxedTotalFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983UntaxedTotal,
                gridBagConstraints);

        jTextFieldUndeductedTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUndeductedTotal.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldUndeductedTotal
                .setNextFocusableComponent(jTextFieldCGTExemptTotal);
        jTextFieldUndeductedTotal
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldUndeductedTotalFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldUndeductedTotal, gridBagConstraints);

        jTextFieldCGTExemptTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCGTExemptTotal.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldCGTExemptTotal
                .setNextFocusableComponent(jTextFieldExcessTotal);
        jTextFieldCGTExemptTotal
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldCGTExemptTotalFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldCGTExemptTotal, gridBagConstraints);

        jTextFieldExcessTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldExcessTotal.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldExcessTotal
                .setNextFocusableComponent(jTextFieldConcessionalTotal);
        jTextFieldExcessTotal
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldExcessTotalFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldExcessTotal, gridBagConstraints);

        jTextFieldConcessionalTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalTotal.setPreferredSize(new java.awt.Dimension(90,
                20));
        jTextFieldConcessionalTotal.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldConcessionalTotal
                .setNextFocusableComponent(jTextFieldInvalidityTotal);
        jTextFieldConcessionalTotal
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldConcessionalTotalFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation
                .add(jTextFieldConcessionalTotal, gridBagConstraints);

        jTextFieldInvalidityTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInvalidityTotal.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldInvalidityTotal
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldInvalidityTotalFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldInvalidityTotal, gridBagConstraints);

        jTextFieldPre071983Total.setEditable(false);
        jTextFieldPre071983Total
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPre071983Total
                .setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldPre071983Total.setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPre071983Total, gridBagConstraints);

        jTextFieldPost061983TaxedTotal.setEditable(false);
        jTextFieldPost061983TaxedTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983TaxedTotal,
                gridBagConstraints);

        jTextFieldTotalAmountTotal.setEditable(false);
        jTextFieldTotalAmountTotal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldTotalAmountTotal, gridBagConstraints);

        jTextFieldConcessionalRollover.setEditable(false);
        jTextFieldConcessionalRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalRollover.setPreferredSize(new java.awt.Dimension(
                90, 20));
        jTextFieldConcessionalRollover.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldConcessionalRollover.setMinimumSize(new java.awt.Dimension(
                90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldConcessionalRollover,
                gridBagConstraints);

        jTextFieldInvalidityRollover.setEditable(false);
        jTextFieldInvalidityRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInvalidityRollover.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldInvalidityRollover,
                gridBagConstraints);

        jTextFieldPre071983Rollover.setToolTipText("");
        jTextFieldPre071983Rollover.setEditable(false);
        jTextFieldPre071983Rollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation
                .add(jTextFieldPre071983Rollover, gridBagConstraints);

        jTextFieldPost061983TaxedRollover.setToolTipText("");
        jTextFieldPost061983TaxedRollover.setEditable(false);
        jTextFieldPost061983TaxedRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983TaxedRollover,
                gridBagConstraints);

        jTextFieldPost061983UntaxedRollover.setToolTipText("");
        jTextFieldPost061983UntaxedRollover.setEditable(false);
        jTextFieldPost061983UntaxedRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPost061983UntaxedRollover
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983UntaxedRollover,
                gridBagConstraints);

        jTextFieldUndeductedRollover.setEditable(false);
        jTextFieldUndeductedRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUndeductedRollover.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldUndeductedRollover,
                gridBagConstraints);

        jTextFieldCGTExemptRollover.setEditable(false);
        jTextFieldCGTExemptRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCGTExemptRollover.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation
                .add(jTextFieldCGTExemptRollover, gridBagConstraints);

        jTextFieldExcessRollover.setEditable(false);
        jTextFieldExcessRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldExcessRollover.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldExcessRollover, gridBagConstraints);

        jTextFieldTotalAmountRollover.setEditable(false);
        jTextFieldTotalAmountRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalAmountRollover.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldTotalAmountRollover,
                gridBagConstraints);

        jTextFieldConcessionalWithdraw.setEditable(false);
        jTextFieldConcessionalWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalWithdraw.setPreferredSize(new java.awt.Dimension(
                90, 20));
        jTextFieldConcessionalWithdraw.setMinimumSize(new java.awt.Dimension(
                90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldConcessionalWithdraw,
                gridBagConstraints);

        jTextFieldInvalidityWithdraw.setEditable(false);
        jTextFieldInvalidityWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldInvalidityWithdraw,
                gridBagConstraints);

        jTextFieldPre071983Withdraw.setEditable(false);
        jTextFieldPre071983Withdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation
                .add(jTextFieldPre071983Withdraw, gridBagConstraints);

        jTextFieldPost061983TaxedWithdraw.setEditable(false);
        jTextFieldPost061983TaxedWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983TaxedWithdraw,
                gridBagConstraints);

        jTextFieldPost061983UntaxedWithdraw.setEditable(false);
        jTextFieldPost061983UntaxedWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983UntaxedWithdraw,
                gridBagConstraints);

        jTextFieldUndeductedWithdraw.setEditable(false);
        jTextFieldUndeductedWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldUndeductedWithdraw,
                gridBagConstraints);

        jTextFieldCGTExemptWithdraw.setEditable(false);
        jTextFieldCGTExemptWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation
                .add(jTextFieldCGTExemptWithdraw, gridBagConstraints);

        jTextFieldExcessWithdraw.setEditable(false);
        jTextFieldExcessWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldExcessWithdraw, gridBagConstraints);

        jTextFieldTotalAmountWithdraw.setEditable(false);
        jTextFieldTotalAmountWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldTotalAmountWithdraw,
                gridBagConstraints);

        jTextFieldConcessionalTaxOnWithdrawal.setEditable(false);
        jTextFieldConcessionalTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalTaxOnWithdrawal
                .setPreferredSize(new java.awt.Dimension(90, 20));
        jTextFieldConcessionalTaxOnWithdrawal
                .setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldConcessionalTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldInvalidityTaxOnWithdrawal.setEditable(false);
        jTextFieldInvalidityTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldInvalidityTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldPre071983TaxOnWithdrawal.setEditable(false);
        jTextFieldPre071983TaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPre071983TaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldPost061983TaxedTaxOnWithdrawal.setEditable(false);
        jTextFieldPost061983TaxedTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983TaxedTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldPost061983UntaxedTaxOnWithdrawal.setEditable(false);
        jTextFieldPost061983UntaxedTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983UntaxedTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldUndeductedTaxOnWithdrawal.setEditable(false);
        jTextFieldUndeductedTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldUndeductedTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldCGTExemptTaxOnWithdrawal.setEditable(false);
        jTextFieldCGTExemptTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldCGTExemptTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldExcessTaxOnWithdrawal.setEditable(false);
        jTextFieldExcessTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldExcessTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldTotalAmountTaxOnWithdrawal.setEditable(false);
        jTextFieldTotalAmountTaxOnWithdrawal
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldTotalAmountTaxOnWithdrawal,
                gridBagConstraints);

        jTextFieldConcessionalNetETP.setEditable(false);
        jTextFieldConcessionalNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalNetETP.setPreferredSize(new java.awt.Dimension(
                90, 20));
        jTextFieldConcessionalNetETP.setMinimumSize(new java.awt.Dimension(90,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldConcessionalNetETP,
                gridBagConstraints);

        jTextFieldInvalidityNetETP.setEditable(false);
        jTextFieldInvalidityNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldInvalidityNetETP, gridBagConstraints);

        jTextFieldPre071983NetETP.setEditable(false);
        jTextFieldPre071983NetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPre071983NetETP, gridBagConstraints);

        jTextFieldPost061983TaxedNetETP.setEditable(false);
        jTextFieldPost061983TaxedNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983TaxedNetETP,
                gridBagConstraints);

        jTextFieldPost061983UntaxedNetETP.setEditable(false);
        jTextFieldPost061983UntaxedNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPost061983UntaxedNetETP,
                gridBagConstraints);

        jTextFieldUndeductedNetETP.setEditable(false);
        jTextFieldUndeductedNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldUndeductedNetETP, gridBagConstraints);

        jTextFieldCGTExemptNetETP.setEditable(false);
        jTextFieldCGTExemptNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldCGTExemptNetETP, gridBagConstraints);

        jTextFieldExcessNetETP.setEditable(false);
        jTextFieldExcessNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldExcessNetETP, gridBagConstraints);

        jTextFieldTotalAmountNetETP.setEditable(false);
        jTextFieldTotalAmountNetETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation
                .add(jTextFieldTotalAmountNetETP, gridBagConstraints);

        jLabel7.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelETPAllocation.add(jLabel7, gridBagConstraints);

        jLabel9.setText("Rollover");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanelETPAllocation.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Withdraw");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanelETPAllocation.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Tax on withdrawal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanelETPAllocation.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Net - ETP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanelETPAllocation.add(jLabel12, gridBagConstraints);

        jTextFieldPreJuly1983TaxOnRollover.setEditable(false);
        jTextFieldPreJuly1983TaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPreJuly1983TaxOnRollover
                .setPreferredSize(new java.awt.Dimension(90, 20));
        jTextFieldPreJuly1983TaxOnRollover
                .setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPreJuly1983TaxOnRollover,
                gridBagConstraints);

        jTextFieldPostJune1983TaxedTaxOnRollover.setEditable(false);
        jTextFieldPostJune1983TaxedTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPostJune1983TaxedTaxOnRollover,
                gridBagConstraints);

        jTextFieldPostJune1983UntaxedTaxOnRollover.setEditable(false);
        jTextFieldPostJune1983UntaxedTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldPostJune1983UntaxedTaxOnRollover,
                gridBagConstraints);

        jTextFieldUndeductedTaxOnRollover.setEditable(false);
        jTextFieldUndeductedTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldUndeductedTaxOnRollover,
                gridBagConstraints);

        jTextFieldCGTExemptTaxOnRollover.setEditable(false);
        jTextFieldCGTExemptTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldCGTExemptTaxOnRollover,
                gridBagConstraints);

        jTextFieldExcessTaxOnRollover.setEditable(false);
        jTextFieldExcessTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldExcessTaxOnRollover,
                gridBagConstraints);

        jTextFieldConcessionalTaxOnRollover.setEditable(false);
        jTextFieldConcessionalTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldConcessionalTaxOnRollover,
                gridBagConstraints);

        jTextFieldInvalidityTaxOnRollover.setEditable(false);
        jTextFieldInvalidityTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldInvalidityTaxOnRollover,
                gridBagConstraints);

        jTextFieldTotalAmountTaxOnRollover.setEditable(false);
        jTextFieldTotalAmountTaxOnRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelETPAllocation.add(jTextFieldTotalAmountTaxOnRollover,
                gridBagConstraints);

        jLabel1.setText("Tax on Rollover");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanelETPAllocation.add(jLabel1, gridBagConstraints);

        add(jPanelETPAllocation);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
                10, 5));

        jButtonReport.setText("Report");
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanel2.add(jButtonReport);

        jPanel1.add(jPanel2);

        jPanelControls.setLayout(new javax.swing.BoxLayout(jPanelControls,
                javax.swing.BoxLayout.X_AXIS));

        jPanelControls.setPreferredSize(new java.awt.Dimension(320, 37));
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

        jPanel1.add(jPanelControls);

        jButtonClear.setText("Clear");
        jButtonClear.setDefaultCapable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonClear);

        jButtonPensionProjection.setText("Pension Projection");
        jButtonPensionProjection.setEnabled(false);
        jButtonPensionProjection
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonPensionProjectionActionPerformed(evt);
                    }
                });

        jPanel1.add(jButtonPensionProjection);

        jPanel3.setEnabled(false);
        jPanel3.add(jTextFieldRollover);

        jPanel3.add(jTextFieldUndeductedPartial);

        jPanel3.add(jTextFieldCGTPartial);

        jPanel3.add(jTextFieldExcessPartial);

        jPanel3.add(jTextFieldConcessionalPartial);

        jPanel3.add(jTextFieldInvalidityPartial);

        jRadioButton1.setText("jRadioButton1");
        buttonGroup1.add(jRadioButton1);
        jPanel3.add(jRadioButton1);

        jPanel1.add(jPanel3);

        add(jPanel1);

    }// GEN-END:initComponents

    private void jRadioButtonIsClientItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonIsClientItemStateChanged
        // Add your handling code here:
        if (evt.getSource() != jRadioButtonIsClient)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        if (jRadioButtonIsClient.isSelected()) {
            etpCalc.setIsClient(Boolean.TRUE);
            jTextFieldPartnerName.setVisible(false);
            jTextFieldDOBPartner.setVisible(false);
            jTextFieldClientName.setVisible(true);
            jTextFieldDOB.setVisible(true);
            updateComponents();
            updateNonEditable();
        } else {
            etpCalc.setIsClient(Boolean.FALSE);
            jTextFieldPartnerName.setVisible(true);
            jTextFieldDOBPartner.setVisible(true);
            jTextFieldClientName.setVisible(false);
            jTextFieldDOB.setVisible(false);
            updateComponents();
            updateNonEditable();
        }

    }// GEN-LAST:event_jRadioButtonIsClientItemStateChanged

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

    private void jRadioButtonPartialWithdrawMouseClicked(
            java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jRadioButtonPartialWithdrawMouseClicked
        // Add your handling code here:
        ETPPartialWithdrawalView pView = new ETPPartialWithdrawalView(etpCalc);
        SwingUtil.add2Dialog(null, "ETP Partial Withdraw", true, pView, true,
                true);
        if (pView.getResult() == ETPConstants.OK_OPTION) {
            updatePartialWithdrawDataForSave(pView);
            enableRollover(true);
            updateForPartialWithdraw();
        }
        jButtonPensionProjection.setEnabled(etpCalc.getRollover()
                && clientService != null);
    }// GEN-LAST:event_jRadioButtonPartialWithdrawMouseClicked

    private void jRadioButtonWithdrawUpToPost30June1983ThresholdItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonWithdrawUpToPost30June1983ThresholdItemStateChanged
        if (evt.getSource() != jRadioButtonWithdrawUpToPost30June1983Threshold)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        if (jRadioButtonWithdrawUpToPost30June1983Threshold.isSelected()) {
            if (etpCalc.getAge() < 55)
                return;
            etpCalc.setOption(etpCalc.WITHDRAW_UP_TO_THRESHOLD);
            enableRollover(false);
            updateNonEditable();
            updateForWithdrawUpToPost061983Threshold();
        }
    }// GEN-LAST:event_jRadioButtonWithdrawUpToPost30June1983ThresholdItemStateChanged

    private void jRadioButtonPartialWithdrawItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonPartialWithdrawItemStateChanged
        if (evt.getSource() != jRadioButtonPartialWithdraw)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        if (jRadioButtonPartialWithdraw.isSelected()) {
            etpCalc.setOption(etpCalc.PARTIAL_WITHDRAW);
            enableRollover(true);
            updateNonEditable();
            updateForPartialWithdraw();
        }

        jButtonPensionProjection.setEnabled(etpCalc.getRollover()
                && clientService != null);
    }// GEN-LAST:event_jRadioButtonPartialWithdrawItemStateChanged

    private void jRadioButtonWithdrawAllItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonWithdrawAllItemStateChanged
        if (evt.getSource() != jRadioButtonWithdrawAll)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        if (jRadioButtonWithdrawAll.isSelected()) {
            enableRollover(true);
            etpCalc.setOption(etpCalc.WITHDRAW_ALL);
            updateNonEditable();
            updateForWithdrawAll();
        }

        jButtonPensionProjection.setEnabled(etpCalc.getRollover()
                && clientService != null);
    }// GEN-LAST:event_jRadioButtonWithdrawAllItemStateChanged

    private void jRadioButtonRolloverAllItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonRolloverAllItemStateChanged
        if (evt.getSource() != jRadioButtonRolloverAll)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        if (jRadioButtonRolloverAll.isSelected()) {
            enableRollover(true);
            etpCalc.setOption(etpCalc.ROLLOVER_ALL);
            updateNonEditable();
            updateForRolloverAll();
        }

        jButtonPensionProjection.setEnabled(etpCalc.getRollover()
                && clientService != null);
    }// GEN-LAST:event_jRadioButtonRolloverAllItemStateChanged

    private void jButtonPensionProjectionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPensionProjectionActionPerformed
        // Add your handling code here:
        displayAP();
    }// GEN-LAST:event_jButtonPensionProjectionActionPerformed

    private void jCheckBoxRolloverReconStateChanged(
            javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jCheckBoxRolloverReconStateChanged
        // Add your handling code here:
        etpCalc.setRolloverAndRecon(jCheckBoxRolloverRecon.isSelected());
        jButtonPensionProjection.setEnabled(jCheckBoxRolloverRecon.isSelected()
                && clientService != null);
    }// GEN-LAST:event_jCheckBoxRolloverReconStateChanged

    private void jCheckBoxRolloverStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jCheckBoxRolloverStateChanged
        // Add your handling code here:
        etpCalc.setRollover(jCheckBoxRollover.isSelected());
        jButtonPensionProjection.setEnabled(jCheckBoxRollover.isSelected()
                && clientService != null);
    }// GEN-LAST:event_jCheckBoxRolloverStateChanged

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doClear(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    private void jTextFieldPost061983UntaxedTotalFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldPost061983UntaxedTotalFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getPost061983UntaxedTotal());

        BigDecimal d = null;
        String value = jTextFieldPost061983UntaxedTotal.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        jTextFieldPost061983UntaxedTotal
                .setText(jTextFieldPost061983UntaxedTotal.getText().trim());
        etpCalc.setPost061983UntaxedTotal(d);

        if (!etpCalc.validateETPComponentInput(d))
            jTextFieldPost061983UntaxedTotal.setText(previousValue);

    }// GEN-LAST:event_jTextFieldPost061983UntaxedTotalFocusLost

    private void jTextFieldInvalidityTotalFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldInvalidityTotalFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getInvalidityTotal());

        BigDecimal d = null;
        String value = jTextFieldInvalidityTotal.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        jTextFieldInvalidityTotal.setText(value);
        etpCalc.setInvalidityTotal(d);

        if (!etpCalc.validateETPComponentInput(d))
            jTextFieldInvalidityTotal.setText(previousValue);

    }// GEN-LAST:event_jTextFieldInvalidityTotalFocusLost

    private void jTextFieldConcessionalTotalFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldConcessionalTotalFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getConcessionalTotal());

        BigDecimal d = null;
        String value = jTextFieldConcessionalTotal.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        jTextFieldConcessionalTotal.setText(value);
        etpCalc.setConcessionalTotal(d);

        if (!etpCalc.validateETPComponentInput(d))
            jTextFieldConcessionalTotal.setText(previousValue);

    }// GEN-LAST:event_jTextFieldConcessionalTotalFocusLost

    private void jTextFieldExcessTotalFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldExcessTotalFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getExcessTotal());

        BigDecimal d = null;
        String value = jTextFieldExcessTotal.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        jTextFieldExcessTotal.setText(value);
        etpCalc.setExcessTotal(d);
        if (!etpCalc.validateETPComponentInput(d))
            jTextFieldExcessTotal.setText(previousValue);

    }// GEN-LAST:event_jTextFieldExcessTotalFocusLost

    private void jTextFieldCGTExemptTotalFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldCGTExemptTotalFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getCGTExemptTotal());

        BigDecimal d = null;
        String value = jTextFieldCGTExemptTotal.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        jTextFieldCGTExemptTotal.setText(value);
        etpCalc.setCGTExemptTotal(d);

        if (!etpCalc.validateETPComponentInput(d))
            jTextFieldCGTExemptTotal.setText(previousValue);

    }// GEN-LAST:event_jTextFieldCGTExemptTotalFocusLost

    private void jTextFieldUndeductedTotalFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldUndeductedTotalFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getUndeductedTotal());

        BigDecimal d = null;
        String value = jTextFieldUndeductedTotal.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        jTextFieldUndeductedTotal.setText(value);
        etpCalc.setUndeductedTotal(d);

        if (!etpCalc.validateETPComponentInput(d))
            jTextFieldUndeductedTotal.setText(previousValue);

    }// GEN-LAST:event_jTextFieldUndeductedTotalFocusLost

    private void jRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonItemStateChanged

    }// GEN-LAST:event_jRadioButtonItemStateChanged

    private void jButtonAllocatedPensionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAllocatedPensionActionPerformed
    /*
     * AllocatedPensionView view = AllocatedPensionView.displayAP(
     * client.MainMenuActionCommand.NEW, listeners );
     * view.etpCalc.assign(etpCalc); view.updateEditable();
     */

    }// GEN-LAST:event_jButtonAllocatedPensionActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        // Add your handling code here:
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

    /***************************************************************************
     * BaseView protected methods
     **************************************************************************/
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
            etpCalc.disableUpdate();
            etpCalc.clear();
            updateTitle();
        } finally {
            updateKnownComponents();
            updateEditable();
            updateRadioButtons();
            updateETPComponents();
            etpCalc.enableUpdate();
            etpCalc.doUpdate();
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return com.argus.financials.config.WordSettings.getInstance()
                .getETPReport();
    }

    public ReportFields getReportData(PersonService person) throws Exception {

        // if ( person == null || !etpCalc.isReady() ) return null;

        ReportFields reportFields = ReportFields.getInstance();
        etpCalc.initializeReportData(reportFields);

        return reportFields;

    }

    protected void doReport() {

        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(clientService),
                    getDefaultReport());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private void updatePartialWithdrawDataForSave(ETPPartialWithdrawalView pView) {
        jTextFieldUndeductedPartial.setText(Currency.getCurrencyInstance()
                .toString(pView.getUndeductedPartial()));
        jTextFieldCGTPartial.setText(Currency.getCurrencyInstance().toString(
                pView.getCGTPartial()));
        jTextFieldExcessPartial.setText(Currency.getCurrencyInstance()
                .toString(pView.getExcessPartial()));
        jTextFieldConcessionalPartial.setText(Currency.getCurrencyInstance()
                .toString(pView.getConcessionalPartial()));
        jTextFieldInvalidityPartial.setText(Currency.getCurrencyInstance()
                .toString(pView.getInvalidityPartial()));
        jTextFieldRollover.setText(Currency.getCurrencyInstance().toString(
                pView.getWithdraw()));
    }

    private void updateForWithdrawUpToPost061983Threshold() {
        clearWithdraw();
        clearRollover();

        BigDecimal d = null;

        d = etpCalc.getConcessionalTotal();
        jTextFieldConcessionalRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.concessionalRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getInvalidityTotal();
        jTextFieldInvalidityRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.invalidityRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getUndeductedTotal();
        jTextFieldUndeductedRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.undeductedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getCGTExemptTotal();
        jTextFieldCGTExemptRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.cgtExemptRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getExcessTotal();
        jTextFieldExcessRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.excessRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983WithdrawWithdrawUpToThreshold();
        jTextFieldPre071983Withdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        jTextFieldPre071983NetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.preNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedWithdrawWithdrawUpToThreshold();
        jTextFieldPost061983TaxedWithdraw.setText(Currency
                .getCurrencyInstance().toString(d));
        jTextFieldPost061983TaxedNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.postTaxedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.postTaxedNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedWithdrawWithdrawUpToThreshold();
        jTextFieldPost061983UntaxedWithdraw.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postUntaxedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedNetETPWithdrawUpToThreshold();
        jTextFieldPost061983UntaxedNetETP.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postUntaxedNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983TaxOnWithdrawalWithdrawUpToThreshold();
        jTextFieldPre071983TaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPre = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedTaxOnWithdrawalWithdrawUpToThreshold();
        jTextFieldPost061983TaxedTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPostTaxed = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold();
        jTextFieldPost061983UntaxedTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPostUntaxed = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalTaxOnWithdrawalWithdrawUpToThreshold();
        jTextFieldTotalAmountTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawTotal = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountWithdrawWithdrawUpToThreshold();
        jTextFieldTotalAmountWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.withdrawAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountNetETPWithdrawUpToThreshold();
        jTextFieldTotalAmountNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.netETPAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountRolloverWithdrawUpToThreshold();
        jTextFieldTotalAmountRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.rolloverAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983RolloverWithdrawUpToThreshold();
        jTextFieldPre071983Rollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedRolloverWithdrawUpToThreshold();
        jTextFieldPost061983TaxedRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postTaxedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold();
        jTextFieldPostJune1983UntaxedTaxOnRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        jTextFieldTotalAmountTaxOnRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnPostUntaxedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
    }

    private void updateForPartialWithdraw() {

        if (etpCalc.getETPPartialWithdrawalRollover() != null)
            updateForPartialWithdrawRollover();
        else
            clearRollover();

        if (etpCalc.getETPPartialWithdrawalWithdraw() != null
                || !((etpCalc.getETPPartialWithdrawalRollover() == null ? 0
                        : etpCalc.getETPPartialWithdrawalRollover()
                                .doubleValue()) == 0))
            updateForPartialWithdrawWithdraw();
        else
            clearWithdraw();

    }

    private void updateForPartialWithdrawWithdraw() {

        clearWithdraw();
        BigDecimal d = null;

        d = etpCalc.getPre071983WithdrawPartialWithdraw();
        jTextFieldPre071983Withdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        jTextFieldPre071983NetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedWithdrawPartialWithdraw();
        jTextFieldPost061983TaxedWithdraw.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postTaxedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getETPPartialWithdrawalWithdraw();
        jTextFieldTotalAmountWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.withdrawAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983TaxOnWithdrawalPartial();
        jTextFieldPre071983TaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPre = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountNetETPPartial();
        jTextFieldTotalAmountNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.netETPAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountTaxOnWithdrawalPartial();
        jTextFieldTotalAmountTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawTotal = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedNetETPPartial();
        jTextFieldPost061983TaxedNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.postTaxedNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedTaxOnWithdrawalPartial();
        jTextFieldPost061983TaxedTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPostTaxed = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedWithdrawPartialWithdraw();
        jTextFieldPost061983UntaxedWithdraw.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postUntaxedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedTaxOnWithdrawPartialWithdraw();
        jTextFieldPost061983UntaxedTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPostUntaxed = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedNetETPPartialWithdraw();
        jTextFieldPost061983UntaxedNetETP.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postUntaxedNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getUndeductedPartial();
        jTextFieldUndeductedWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        jTextFieldUndeductedNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.undeductedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.undeductedNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getCGTExemptPartial();
        jTextFieldCGTExemptWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        jTextFieldCGTExemptNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.cgtExemptWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.cgtExemptNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getExcessPartial();
        jTextFieldExcessWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        jTextFieldExcessNetETP.setText(Currency.getCurrencyInstance().toString(
                d));
        data.etp.excessWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.excessNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getConcessionalPartial();
        jTextFieldConcessionalWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        jTextFieldConcessionalNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.concessionalWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.concessionalNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getInvalidityPartial();
        jTextFieldInvalidityWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        jTextFieldInvalidityNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.invalidityWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
        data.etp.invalidityNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTaxOnWithdrawalExcessPartialWithdraw();
        jTextFieldExcessTaxOnWithdrawal.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.taxOnWithdrawExcess = Currency.getCurrencyInstance().toString(
                d);

        d = etpCalc.getConcessionalTaxOnWithdrawalPartialWithdraw();
        jTextFieldConcessionalTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawConcessional = Currency.getCurrencyInstance()
                .toString(d);

    }

    private void updateForPartialWithdrawRollover() {
        clearRollover();

        BigDecimal d = null;

        d = etpCalc.getPre071983PartialWithdrawRollover();
        jTextFieldPre071983Rollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedPartialWithdrawRollover();
        jTextFieldPost061983TaxedRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postTaxedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedTaxOnRolloverPartialWithdraw();
        jTextFieldPostJune1983UntaxedTaxOnRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        jTextFieldTotalAmountTaxOnRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnPostUntaxedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getETPPartialWithdrawalRolloverAvail();
        jTextFieldTotalAmountRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.rolloverAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getExcessAvail();
        jTextFieldExcessRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.excessRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getConcessionalAvail();
        jTextFieldConcessionalRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.concessionalRollover = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getInvalidityAvail();
        jTextFieldInvalidityRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.invalidityRollover = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getUndeductedAvail();
        jTextFieldUndeductedRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.undeductedRollover = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getCGTExemptAvail();
        jTextFieldCGTExemptRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.cgtExemptRollover = Currency.getCurrencyInstance().toString(d);
    }

    private void updateForWithdrawAll() {

        clearRollover();
        BigDecimal d = null;

        d = etpCalc.getConcessionalTotal();
        jTextFieldConcessionalWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.concessionalWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getInvalidityTotal();
        jTextFieldInvalidityWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.invalidityWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983Total();
        jTextFieldPre071983Withdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedTotal();
        jTextFieldPost061983TaxedWithdraw.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postTaxedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedTotal();
        jTextFieldPost061983UntaxedWithdraw.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postUntaxedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getUndeductedTotal();
        jTextFieldUndeductedWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.undeductedWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getCGTExemptTotal();
        jTextFieldCGTExemptWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.cgtExemptWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getExcessTotal();
        jTextFieldExcessWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.excessWithdraw = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTaxOnWithdrawalExcess();
        jTextFieldExcessTaxOnWithdrawal.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.taxOnWithdrawExcess = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalETPAmount();
        jTextFieldTotalAmountWithdraw.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.withdrawAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983Total();
        jTextFieldPre071983NetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getInvalidityTotal();
        jTextFieldInvalidityNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.invalidityNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getConcessionalTotal();
        jTextFieldConcessionalNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.concessionalNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getUndeductedTotal();
        jTextFieldUndeductedNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.undeductedNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getCGTExemptTotal();
        jTextFieldCGTExemptNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.cgtExemptNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getExcessNetETP();
        jTextFieldExcessNetETP.setText(Currency.getCurrencyInstance().toString(
                d));
        data.etp.excessNetETP = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getConcessionalTaxOnWithdrawal();
        jTextFieldConcessionalTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawConcessional = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getPre071983TaxOnWithdrawal();
        jTextFieldPre071983TaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPre = Currency.getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedTaxOnWithdrawal();
        jTextFieldPost061983TaxedTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPostTaxed = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getPost061983UntaxedTaxOnWithdrawal();
        jTextFieldPost061983UntaxedTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawPostUntaxed = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getPost061983TaxedNetETP();
        jTextFieldPost061983TaxedNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.postTaxedNetETP = Currency.getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983UntaxedNetETP();
        jTextFieldPost061983UntaxedNetETP.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postUntaxedNetETP = Currency.getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountTaxOnWithdrawal();
        jTextFieldTotalAmountTaxOnWithdrawal.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnWithdrawTotal = Currency.getCurrencyInstance()
                .toString(d);

        d = etpCalc.getTotalAmountNetETP();
        jTextFieldTotalAmountNetETP.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.netETPAmount = Currency.getCurrencyInstance().toString(d);
    }

    private void clearWithdraw() {
        clearWithdrawForReport();
        jTextFieldConcessionalWithdraw.setText(null);
        jTextFieldInvalidityWithdraw.setText(null);
        jTextFieldPre071983Withdraw.setText(null);
        jTextFieldPost061983TaxedWithdraw.setText(null);
        jTextFieldPost061983UntaxedWithdraw.setText(null);
        jTextFieldUndeductedWithdraw.setText(null);
        jTextFieldCGTExemptWithdraw.setText(null);
        jTextFieldExcessWithdraw.setText(null);
        jTextFieldTotalAmountWithdraw.setText(null);
        jTextFieldConcessionalTaxOnWithdrawal.setText(null);
        jTextFieldInvalidityTaxOnWithdrawal.setText(null);
        jTextFieldPre071983TaxOnWithdrawal.setText(null);
        jTextFieldPost061983TaxedTaxOnWithdrawal.setText(null);
        jTextFieldPost061983UntaxedTaxOnWithdrawal.setText(null);
        jTextFieldUndeductedTaxOnWithdrawal.setText(null);
        jTextFieldCGTExemptTaxOnWithdrawal.setText(null);
        jTextFieldExcessTaxOnWithdrawal.setText(null);
        jTextFieldTotalAmountTaxOnWithdrawal.setText(null);
        jTextFieldConcessionalNetETP.setText(null);
        jTextFieldInvalidityNetETP.setText(null);
        jTextFieldPre071983NetETP.setText(null);
        jTextFieldPost061983TaxedNetETP.setText(null);
        jTextFieldPost061983UntaxedNetETP.setText(null);
        jTextFieldUndeductedNetETP.setText(null);
        jTextFieldCGTExemptNetETP.setText(null);
        jTextFieldExcessNetETP.setText(null);
        jTextFieldTotalAmountNetETP.setText(null);
    }

    private void updateForRolloverAll() {

        clearWithdraw();
        BigDecimal d = null;

        d = etpCalc.getConcessionalTotal();
        jTextFieldConcessionalRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.concessionalRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getInvalidityTotal();
        jTextFieldInvalidityRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.invalidityRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPre071983RolloverAll();
        jTextFieldPre071983Rollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.preRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getPost061983TaxedRolloverAll();
        jTextFieldPost061983TaxedRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.postTaxedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        jTextFieldPost061983UntaxedRollover.setText(null);

        d = etpCalc.getUndeductedTotal();
        jTextFieldUndeductedRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.undeductedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getCGTExemptTotal();
        jTextFieldCGTExemptRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.cgtExemptRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getExcessTotal();
        jTextFieldExcessRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.excessRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTotalAmountRolloverAll();
        jTextFieldTotalAmountRollover.setText(Currency.getCurrencyInstance()
                .toString(d));
        data.etp.rolloverAmount = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);

        d = etpCalc.getTaxOnPost061983UntaxedForRolloverAll();
        jTextFieldPostJune1983UntaxedTaxOnRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        jTextFieldTotalAmountTaxOnRollover.setText(Currency
                .getCurrencyInstance().toString(d));
        data.etp.taxOnPostUntaxedRollover = d == null ? STRING_ZERO : Currency
                .getCurrencyInstance().toString(d);
    }

    private void clearRollover() {
        clearRolloverForReport();
        jTextFieldConcessionalRollover.setText(null);
        jTextFieldInvalidityRollover.setText(null);
        jTextFieldPre071983Rollover.setText(null);
        jTextFieldPost061983TaxedRollover.setText(null);
        jTextFieldPost061983UntaxedRollover.setText(null);
        jTextFieldUndeductedRollover.setText(null);
        jTextFieldCGTExemptRollover.setText(null);
        jTextFieldExcessRollover.setText(null);
        jTextFieldTotalAmountRollover.setText(null);
        jTextFieldPostJune1983UntaxedTaxOnRollover.setText(null);
        jTextFieldTotalAmountTaxOnRollover.setText(null);

    }

    private static ETPRolloverViewNew view;
    public static ETPRolloverViewNew display(final Model model, FocusListener[] listeners) {

        if (view == null) {
            view = new ETPRolloverViewNew();
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
        } catch (Exception e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            view = null;
        }

        return view;

    }

    public void setClientNameFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTextFieldClientName.requestFocus();
            }
        });
    }

    private void displayAP() {

        jButtonCloseActionPerformed(null);

        AllocatedPensionCalcNew apCalc = new AllocatedPensionCalcNew();
        apCalc.setClientName(etpCalc.getClientName().trim());
        if (etpCalc.getOption() == etpCalc.ROLLOVER_ALL) {
            apCalc.setPre(etpCalc.getPre071983Total());
            apCalc.setPostTaxed(etpCalc.getPost061983TaxedTotal());
            apCalc.setPost061983UntaxedTotal(etpCalc
                    .getPost061983UntaxedTotal());
            apCalc.setUndeductedTotal(etpCalc.getUndeductedTotal());
            apCalc.setCGTExemptTotal(etpCalc.getCGTExemptTotal());
            apCalc.setExcessTotal(etpCalc.getExcessTotal());
            apCalc.setConcessionalTotal(etpCalc.getConcessionalTotal());
            apCalc.setInvalidityTotal(etpCalc.getInvalidityTotal());
            apCalc.setTotalETPAmount(etpCalc.getTotalAmountRolloverAll());
        } else if (etpCalc.getOption() == etpCalc.WITHDRAW_ALL) {
            apCalc.setPre(null);
            apCalc.setPostTaxed(null);
            apCalc.setPost061983UntaxedTotal(null);
            apCalc.setUndeductedTotal(etpCalc.getTotalAmountNetETP());
            apCalc.setCGTExemptTotal(null);
            apCalc.setExcessTotal(null);
            apCalc.setConcessionalTotal(null);
            apCalc.setInvalidityTotal(null);
            apCalc.setTotalETPAmount(etpCalc.getTotalAmountNetETP());
        } else if (etpCalc.getOption() == etpCalc.PARTIAL_WITHDRAW) {
            apCalc.setPre(etpCalc.getPre071983PartialWithdrawRollover());
            apCalc.setPostTaxed(etpCalc
                    .getPost061983TaxedPartialWithdrawRollover());
            apCalc.setPost061983UntaxedTotal(null);
            apCalc.setUndeductedTotal(etpCalc.getUndeductedAvail());
            apCalc.setCGTExemptTotal(etpCalc.getCGTExemptAvail());
            apCalc.setExcessTotal(etpCalc.getExcessAvail());
            apCalc.setConcessionalTotal(etpCalc.getConcessionalAvail());
            apCalc.setInvalidityTotal(etpCalc.getInvalidityAvail());
            apCalc.setTotalETPAmount(etpCalc
                    .getETPPartialWithdrawalRolloverAvail());
        } else if (etpCalc.getOption() == etpCalc.WITHDRAW_UP_TO_THRESHOLD) {
            apCalc.setPre(etpCalc.getPreForWithdrawUpToThresholdAP());
            apCalc.setPostTaxed(etpCalc
                    .getPostTaxedForWithdrawUpToThresholdAP());
            apCalc.setPost061983UntaxedTotal(null);
            apCalc.setUndeductedTotal(etpCalc
                    .getUndeductedForWithdrawUpToThresholdAP());
            apCalc.setCGTExemptTotal(etpCalc.getCGTExemptTotal());
            apCalc.setExcessTotal(etpCalc.getExcessTotal());
            apCalc.setConcessionalTotal(etpCalc.getConcessionalTotal());
            apCalc.setInvalidityTotal(etpCalc.getInvalidityTotal());
            apCalc.setTotalETPAmount(etpCalc
                    .calculateETPTotalForWithdrawUpToThresholdAP());
        }

        apCalc.setEligibleServiceDate(etpCalc.getEligibleServiceDate());
        PersonService person = clientService;
        if (person != null) {
            try {
                IPerson personName = person.getPersonName();
                apCalc.setSexCodeID(personName == null ? null : personName.getSex().getId().intValue());
                apCalc.setDateOfBirth(personName == null ? null : personName.getDateOfBirth());
            } catch (ServiceException e) {
            }
        }
        AllocatedPensionViewNew view = AllocatedPensionViewNew.display(null, listeners);
        ((AllocatedPensionCalcNew) view.getCalculationModel()).assign(apCalc);
        view.updateEditable();

    }

    public boolean isModified() {
        return (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                .isModified())
                || (jButtonSaveAs.isVisible() && jButtonSaveAs.isEnabled() && getModel()
                        .isModified());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jTextFieldCGTExemptRollover;

    private javax.swing.JTextField jTextFieldExcessNetETP;

    private javax.swing.JCheckBox jCheckBoxRollover;

    private javax.swing.JLabel Age;

    private javax.swing.JButton jButtonPensionProjection;

    private javax.swing.JTextField jTextFieldUndeductedTaxOnRollover;

    private javax.swing.JTextField jTextFieldTotalAmountRollover;

    private javax.swing.JLabel jLabelPostJune1983Untaxed;

    private javax.swing.JTextField jTextFieldConcessionalWithdraw;

    private javax.swing.JLabel jLabelExcess;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JTextField jTextFieldPost061983UntaxedTaxOnWithdrawal;

    private javax.swing.JPanel jPanel19;

    private javax.swing.JTextField jTextFieldExcessWithdraw;

    private com.argus.bean.FDateChooser jTextFieldCalculationDate;

    private javax.swing.JTextField jTextFieldPostJune1983UntaxedTaxOnRollover;

    private javax.swing.JTextField jTextFieldPost061983ThresholdUsedEn;

    private javax.swing.JTextField jTextFieldCGTExemptNetETP;

    private javax.swing.JTextField jTextFieldPre071983Total;

    private javax.swing.JPanel jPanelOriginalData;

    private javax.swing.JTextField jTextFieldPost061983TaxedTaxOnWithdrawal;

    private javax.swing.JRadioButton jRadioButtonWithdrawUpToPost30June1983Threshold;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JTextField jTextFieldCGTExemptWithdraw;

    private javax.swing.JTextField jTextFieldExcessTotal;

    private javax.swing.JLabel jLabelConcessional;

    private com.argus.bean.FDateChooser jTextFieldEligibleServiceDate;

    private javax.swing.JTextField jTextFieldConcessionalTaxOnRollover;

    private javax.swing.JLabel jLabelPostJune1983Taxed;

    private javax.swing.JTextField jTextFieldInvalidityTotal;

    private javax.swing.JRadioButton jRadioButtonIsClient;

    private javax.swing.JTextField jTextFieldInvalidityTaxOnWithdrawal;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JTextField jTextFieldPost061983UntaxedNetETP;

    private javax.swing.JTextField jTextFieldPre071983Rollover;

    protected javax.swing.JPanel jPanelCloseSave;

    private javax.swing.JRadioButton jRadioButtonPartialWithdraw;

    private javax.swing.JLabel jLabelPreJuly1983;

    private javax.swing.JTextField jTextFieldTotalAmountWithdraw;

    private javax.swing.JTextField jTextFieldUndeductedNetETP;

    protected javax.swing.JPanel jPanelControls;

    private javax.swing.JTextField jTextFieldPost061983TaxedRollover;

    private javax.swing.JLabel jLabelPost30June1983ThresholdUsed;

    private javax.swing.JTextField jTextFieldPost061983TaxedWithdraw;

    private javax.swing.JTextField jTextFieldInvalidityPartial;

    private javax.swing.JTextField jTextFieldConcessionalNetETP;

    private javax.swing.JLabel jLabelTotalETP;

    private javax.swing.JTextField jTextFieldInvalidityNetETP;

    private javax.swing.JRadioButton jRadioButtonWithdrawAll;

    private javax.swing.JRadioButton jRadioButtonIsPartner;

    private javax.swing.JTextField jTextFieldTotalAmountTaxOnWithdrawal;

    private javax.swing.JPanel jPanelETPAllocation;

    private javax.swing.JTextField jTextFieldTotalAmountTotal;

    private javax.swing.JTextField jTextFieldConcessionalPartial;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JTextField jTextFieldPreDays;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField jTextFieldExcessTaxOnRollover;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JTextField jTextFieldPost061983UntaxedRollover;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField jTextFieldPostJune1983TaxedTaxOnRollover;

    private javax.swing.JLabel jLabelCGTExempt;

    private javax.swing.JTextField jTextFieldExcessTaxOnWithdrawal;

    private javax.swing.JTextField jTextFieldPre071983Withdraw;

    private javax.swing.JTextField jTextFieldInvalidityRollover;

    private javax.swing.JTextField jTextFieldInvalidityWithdraw;

    private javax.swing.JTextField jTextFieldPreJuly1983TaxOnRollover;

    private javax.swing.JLabel jLabelUndeducted;

    private javax.swing.JTextField jTextFieldPostDays;

    private javax.swing.JLabel jLabelEligibleServiceDate;

    private javax.swing.JTextField jTextFieldCGTExemptTotal;

    private javax.swing.JTextField jTextFieldUndeductedTotal;

    private javax.swing.JTextField jTextFieldPartnerName;

    private com.argus.bean.FDateChooser jTextFieldDOBPartner;

    private javax.swing.JTextField jTextFieldTotalETP;

    private javax.swing.JTextField jTextFieldExcessPartial;

    private javax.swing.JTextField jTextFieldCGTExemptTaxOnWithdrawal;

    private javax.swing.JTextField jTextFieldPost061983TaxedTotal;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JTextField jTextFieldPost061983TaxedNetETP;

    private com.argus.bean.FDateChooser jTextFieldDOB;

    private javax.swing.JTextField jTextFieldPost061983UntaxedWithdraw;

    private javax.swing.JPanel jPanelFormat;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JTextField jTextFieldUndeductedRollover;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPasswordField jTextFieldPost061983ThresholdUsedUn;

    private javax.swing.JTextField jTextFieldPost061983UntaxedTotal;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JTextField jTextFieldConcessionalTaxOnWithdrawal;

    private javax.swing.ButtonGroup buttonGroup2;

    private javax.swing.JTextField jTextFieldTotalAmountTaxOnRollover;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JCheckBox jCheckBoxRolloverRecon;

    private javax.swing.JTextField jTextFieldConcessionalTotal;

    private javax.swing.JTextField jTextFieldRollover;

    private javax.swing.JLabel jLabelInvalidity;

    private javax.swing.JTextField jTextFieldUndeductedTaxOnWithdrawal;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JRadioButton jRadioButtonRolloverAll;

    private javax.swing.JTextField jTextFieldUndeductedWithdraw;

    private javax.swing.JTextField jTextFieldInvalidityTaxOnRollover;

    private javax.swing.JLabel jLabelTaxRate;

    private javax.swing.JTextField jTextFieldConcessionalRollover;

    private javax.swing.JTextField jTextFieldCGTExemptTaxOnRollover;

    private javax.swing.JTextField jTextFieldExcessRollover;

    private javax.swing.JTextField jTextFieldUndeductedPartial;

    private javax.swing.JLabel jLabelCalculationDate;

    private javax.swing.JTextField jTextFieldAge;

    private javax.swing.JTextField jTextFieldPre071983TaxOnWithdrawal;

    private javax.swing.JTextField jTextFieldCGTPartial;

    private javax.swing.JTextField jTextFieldPre071983NetETP;

    private javax.swing.JTextField jTextFieldClientName;

    private javax.swing.JTextField jTextFieldTotalAmountNetETP;

    private javax.swing.JTextField jTextFieldTaxRate;

    private javax.swing.JLabel jLabelTotalAmount;

    // End of variables declaration//GEN-END:variables

    public void hideControls() {
        jPanelControls.setVisible(false);
    }

    public void showControls() {
        jPanelControls.setVisible(true);
    }

    /**
     * Viewble interface
     */
    public Integer getObjectType() {
        return null;
    }

    public void updateView(String modelTitle) throws Exception {

        PersonService person = clientService;
        Model m = person == null ? null : person.getModel(getDefaultType(),
                modelTitle);

        if (m == null) {
            updateView(person);
        } else {
            updateView(m);
        }

        updateTitle();

    }

    public void updateView(Model m) throws Exception {

        // saveView();

        if (m == null) {
            updateView(clientService);
        } else {
            // use copy of model
            Integer id = m.getId();
            m = new Model(m);
            m.setId(id);

            try {
                etpCalc.disableUpdate();
    
                etpCalc.setModel(m);
                // etpCalc.setSaved();
            } finally {
                etpCalc.enableUpdate();
                etpCalc.doUpdate();
                updateKnownComponents();
                updateEditable();
                updateRadioButtons();
                updateETPComponents();
                updateComponents();
            }
        }
    }

    public void updateView(PersonService person)
            throws ServiceException {

        // load data from person, e.g. DOB
        // saveView();

        etpCalc.disableUpdate();
        try {
            etpCalc.clear();
            Model m = new Model();
            m.setTypeID(ModelType.ETP_ROLLOVER);
            etpCalc.setModel(m);

        } finally {
            etpCalc.enableUpdate();
            etpCalc.doUpdate();
            updateKnownComponents();
            updateEditable();
            updateRadioButtons();
            updateETPComponents();
            updateComponents();

        }

    }

    private void updateKnownComponents() {
        ClientService client = clientService;
        if (client == null)
            return;

        try {
            IPerson personName = client.getPersonName();
            if (etpCalc.getClientName() == null)
                etpCalc.setClientName(personName == null ? null : personName.getFullName());
            if (etpCalc.getDateOfBirth() == null)
                etpCalc.setDateOfBirth(personName == null ? null : personName.getDateOfBirth());

            PersonService partnerPerson = client.getPartner(false);
            IPerson partnerName = (partnerPerson == null ? null : partnerPerson.getPersonName());
            if (etpCalc.getPartnerName() == null)
                etpCalc.setPartnerName(partnerName == null ? null : partnerName.getFullName());
            if (etpCalc.getPartnerDOB() == null)
                etpCalc.setPartnerDOB(partnerName == null ? null : partnerName.getDateOfBirth());

        } catch (ServiceException e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    private void saveView(boolean newModel) {
        int result = -1;
        String oldTitle = getTitle();
        try {
            if (!newModel)
                result = SaveProjectionDialog.getSaveProjectionInstance().save(
                        etpCalc, this);
            else
                result = SaveProjectionDialog.getSaveProjectionInstance()
                        .saveAs(etpCalc, this);

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

        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (Exception e) { /* ignore by now */
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) { /* ignore by now */
                }
            }
        }

        person.addModel(getModel());

        person.storeModels();
        etpCalc.setSaved();

    }

    /**
     * helper methods
     */
    public void save() {

    }

    public void updateEditable() {

        updateOthers();

        BigDecimal d;
        String s;
        // if etpCalc.getLastPropertyName != getAccessibleContextName()

        jTextFieldEligibleServiceDate.setText(DateTimeUtils.asString(etpCalc
                .getEligibleServiceDate()));
        jTextFieldCalculationDate.setText(DateTimeUtils.asString(etpCalc
                .getCalculationDate()));

        d = etpCalc.getPost061983ThresholdUsed();
        jTextFieldPost061983ThresholdUsedEn.setText(Currency
                .getCurrencyInstance().toString(d));

        d = etpCalc.getTaxRate() <= 0 ? null : new BigDecimal(etpCalc
                .getTaxRate());
        jTextFieldTaxRate.setText(Percent.getPercentInstance().toString(d));

        d = etpCalc.getPost061983UntaxedTotal();
        jTextFieldPost061983UntaxedTotal.setText(Currency.getCurrencyInstance()
                .toString(d));

    }

    public void updateETPComponents() {

        BigDecimal d = null;

        d = etpCalc.getUndeductedTotal();
        jTextFieldUndeductedTotal.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getCGTExemptTotal();
        jTextFieldCGTExemptTotal.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getExcessTotal();
        jTextFieldExcessTotal.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getConcessionalTotal();
        jTextFieldConcessionalTotal.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getInvalidityTotal();
        jTextFieldInvalidityTotal.setText(Currency.getCurrencyInstance()
                .toString(d));
    }

    public void updateNonEditable() {
        BigDecimal d = null;

        if (jRadioButtonIsPartner.isSelected())
            setAge(etpCalc.getPartnerDOB());
        else
            setAge(etpCalc.getDateOfBirth());

        jTextFieldPreDays.setText(etpCalc.getPre071983Days() + "");
        jTextFieldPostDays.setText(etpCalc.getPost061983Days() + "");
        d = etpCalc.getPre071983Total();
        jTextFieldPre071983Total.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getPost061983TaxedTotal();
        jTextFieldPost061983TaxedTotal.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getTotalETPCalculationAmount();
        jTextFieldTotalAmountTotal.setText(Currency.getCurrencyInstance()
                .toString(d));
    }

    protected void updateComponents() {
        // enable/disable save option
        jButtonSave
                .setEnabled(clientService != null);
        jButtonSaveAs
                .setEnabled(clientService != null);

        boolean enable = etpCalc.getAge() >= 55;

        if (!enable)
            jTextFieldPost061983ThresholdUsedEn.setText(null);
        jTextFieldPost061983ThresholdUsedUn.setVisible(!enable);
        jTextFieldPost061983ThresholdUsedEn.setVisible(enable);
        this.validate();
        jRadioButtonWithdrawUpToPost30June1983Threshold.setEnabled(etpCalc
                .getAge() >= 55);

    }

    private void updateOthers() {
        BigDecimal d = etpCalc.getTotalETPAmount();

        if (etpCalc.getIsClient() == null
                || etpCalc.getIsClient().booleanValue()) {
            jRadioButtonIsClient.setSelected(true);
            jTextFieldPartnerName.setVisible(false);
            jTextFieldDOBPartner.setVisible(false);
            jTextFieldClientName.setVisible(true);
            jTextFieldDOB.setVisible(true);
        } else {
            jRadioButtonIsPartner.setSelected(true);
            jTextFieldPartnerName.setVisible(true);
            jTextFieldDOBPartner.setVisible(true);
            jTextFieldClientName.setVisible(false);
            jTextFieldDOB.setVisible(false);

        }

        jTextFieldPartnerName.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldDOBPartner.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldDOB.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldTotalETP.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldClientName.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        try {
            jTextFieldClientName.setText(etpCalc.getClientName() == null ? null
                    : etpCalc.getClientName().trim());
            jTextFieldTotalETP.setText(Currency.getCurrencyInstance().toString(
                    d));
            jTextFieldDOB.setText(DateTimeUtils.asString(etpCalc
                    .getDateOfBirth()));
            jTextFieldPartnerName
                    .setText(etpCalc.getPartnerName() == null ? null : etpCalc
                            .getPartnerName().trim());
            jTextFieldDOBPartner.setText(DateTimeUtils.asString(etpCalc
                    .getPartnerDOB()));

        } finally {
            jTextFieldTotalETP.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldDOB.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldClientName.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldPartnerName.getDocument().putProperty(
                    DocumentNames.READY, Boolean.FALSE);
            jTextFieldDOBPartner.getDocument().putProperty(DocumentNames.READY,
                    Boolean.FALSE);

        }

        jCheckBoxRollover.setSelected(etpCalc.getRollover());
        jCheckBoxRolloverRecon.setSelected(etpCalc.getRolloverAndRecon());

    }

    public void updateRadioButtons() {

        if (etpCalc.getOption() == etpCalc.PARTIAL_WITHDRAW) {
            jRadioButtonPartialWithdraw.setSelected(true);
            enableRollover(true);
            updateForPartialWithdraw();
        } else if (etpCalc.getOption() == etpCalc.WITHDRAW_ALL) {
            jRadioButtonWithdrawAll.setSelected(true);
            enableRollover(true);
            updateForWithdrawAll();
        } else if (etpCalc.getOption() == etpCalc.WITHDRAW_UP_TO_THRESHOLD) {
            jRadioButtonWithdrawUpToPost30June1983Threshold.setSelected(true);
            enableRollover(false);
            updateForWithdrawUpToPost061983Threshold();
        } else if (etpCalc.getOption() == etpCalc.ROLLOVER_ALL) {
            jRadioButtonRolloverAll.setSelected(true);
            enableRollover(true);
            updateForRolloverAll();
        } else {
            jRadioButton1.setSelected(true);
            updateEditable();
            clearRollover();
            clearWithdraw();
            enableRollover(false);

        }

    }

    public void clearWithdrawForReport() {
        data.etp.withdrawAmount = STRING_ZERO;
        data.etp.cgtExemptWithdraw = STRING_ZERO;
        data.etp.concessionalWithdraw = STRING_ZERO;
        data.etp.excessWithdraw = STRING_ZERO;
        data.etp.invalidityWithdraw = STRING_ZERO;
        data.etp.postTaxedWithdraw = STRING_ZERO;
        data.etp.postUntaxedWithdraw = STRING_ZERO;
        data.etp.preWithdraw = STRING_ZERO;
        data.etp.undeductedWithdraw = STRING_ZERO;

        data.etp.taxOnWithdrawExcess = STRING_ZERO;
        data.etp.taxOnWithdrawConcessional = STRING_ZERO;
        data.etp.taxOnWithdrawPre = STRING_ZERO;
        data.etp.taxOnWithdrawPostTaxed = STRING_ZERO;
        data.etp.taxOnWithdrawPostUntaxed = STRING_ZERO;

        data.etp.preNetETP = STRING_ZERO;
        data.etp.postTaxedNetETP = STRING_ZERO;
        data.etp.postUntaxedNetETP = STRING_ZERO;
        data.etp.undeductedNetETP = STRING_ZERO;
        data.etp.cgtExemptNetETP = STRING_ZERO;
        data.etp.excessNetETP = STRING_ZERO;
        data.etp.concessionalNetETP = STRING_ZERO;
        data.etp.invalidityNetETP = STRING_ZERO;
        data.etp.netETPAmount = STRING_ZERO;

    }

    public void clearRolloverForReport() {
        data.etp.rolloverAmount = STRING_ZERO;
        data.etp.cgtExemptRollover = STRING_ZERO;
        data.etp.concessionalRollover = STRING_ZERO;
        data.etp.excessRollover = STRING_ZERO;
        data.etp.invalidityRollover = STRING_ZERO;
        data.etp.postTaxedRollover = STRING_ZERO;
        data.etp.taxOnPostUntaxedRollover = STRING_ZERO;
        data.etp.preRollover = STRING_ZERO;
        data.etp.undeductedRollover = STRING_ZERO;
    }

    public void enableRollover(boolean value) {
        jCheckBoxRollover.setEnabled(value);
        jCheckBoxRolloverRecon.setEnabled(!value);

        if (value == false)
            jCheckBoxRollover.setSelected(value);
        else
            jCheckBoxRolloverRecon.setSelected(!value);

    }

    /**
     * helper method
     */
    private void setAge(java.util.Date dateOfBirth) {
        Double age = DateTimeUtils.getAgeDouble(dateOfBirth);

        if (dateOfBirth == null || age == null) {
            jTextFieldAge.setText(null);
            // etpCalc.setAge( (int)ETPCalcNew.UNKNOWN_VALUE );
        } else {
            jTextFieldAge.setText("" + age.intValue());
            // etpCalc.setAge( age.intValue() );
        }

    }

    private void setAge(String dateOfBirth) {
        setAge(DateTimeUtils.getDate(dateOfBirth));
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

                PersonService person = clientService;
                if (person == null)
                    return;

                try {
                    person.removeModel(getModel());
                    person.storeModels();
                } catch (ServiceException e) {
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
