/*
 * ETPRolloverView.java
 *
 * Created on 29 November 2001, 11:47
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author kevinm
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import java.awt.Dimension;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import com.argus.financials.code.ETPType;
import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.etc.PersonName;
import com.argus.financials.io.IOUtils2;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.DocumentUtils;
import com.argus.financials.projection.ETPCalc;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.save.Model;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceException;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.financials.ui.IMenuCommand;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;

public class ETPRolloverView extends javax.swing.JPanel implements
        javax.swing.event.ChangeListener, ActionEventID {

    protected static final Dimension NAME_DIM = new java.awt.Dimension(160, 17);

    protected static final Dimension VALUE_DIM = new java.awt.Dimension(130, 20);

    protected ETPCalc etpCalc;

    private java.awt.event.FocusListener[] listeners;

    /** Creates new form ETPCalc */
    public ETPRolloverView() {
        etpCalc = new ETPCalc();

        initComponents();
        initComponents2();
    }

    public ETPRolloverView(MoneyCalc calc) {
        etpCalc = (ETPCalc) calc;

        initComponents();
        initComponents2();
    }

    public Integer getDefaultType() {
        return ModelTypeID.rcETP_ROLLOVER.getCodeIDInteger();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcETP_ROLLOVER.getCodeDesc();
    }

    protected Model getModel() {
        return etpCalc.getModel();
    }

    public MoneyCalc getCalculationModel() {
        return etpCalc;
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void setTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    private void initComponents2() {

        // etpCalc.addChangeListener(this);
        _setAccessibleContext();

        if (etpCalc.getClass().equals(ETPCalc.class)) {
            etpCalc.addChangeListener(this);
            DocumentUtils.addListener(this, etpCalc); // after
                                                        // _setAccessibleContext()
            updateEditable();
            setActionMap();
        }

    }

    private void _setAccessibleContext() {

        jTextFieldDOB.getAccessibleContext().setAccessibleName(
                DocumentNames.DOB);
        jComboBoxETPType.getAccessibleContext().setAccessibleName(
                DocumentNames.ETP_TYPE);

        jTextFieldEligibleServiceDate.getAccessibleContext().setAccessibleName(
                DocumentNames.ELIGIBLE_SERVICE_DATE);
        jTextFieldCalculationDate.getAccessibleContext().setAccessibleName(
                DocumentNames.CALCULATION_DATE);
        jTextFieldTotalETPAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.TOTAL_ETP_AMOUNT);
        jTextFieldTaxRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_RATE);

        jRadioButtonEncashmentYes.getAccessibleContext().setAccessibleName(
                DocumentNames.ENCASHMENT_CODE_YES);
        jRadioButtonEncashmentNo.getAccessibleContext().setAccessibleName(
                DocumentNames.ENCASHMENT_CODE_NO);

        jTextFieldNonQualifyingKnown.getAccessibleContext().setAccessibleName(
                DocumentNames.NON_QUALIFYING);
        jTextFieldCGTExemptKnown.getAccessibleContext().setAccessibleName(
                DocumentNames.CGT_EXEMPT);
        jTextFieldUndeductedKnown.getAccessibleContext().setAccessibleName(
                DocumentNames.UNDEDUCTED);
        jTextFieldConcessionalKnown.getAccessibleContext().setAccessibleName(
                DocumentNames.CONCESSIONAL);
        jTextFieldPost061994InvKnown.getAccessibleContext().setAccessibleName(
                DocumentNames.POST_JUNE_94_INVALIDITY);

        jTextFieldRBLAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.RBL_AMOUNT);
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
        buttonGroupEncashment = new javax.swing.ButtonGroup();
        jPanelETPData = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabelDOB = new javax.swing.JLabel();
        jTextFieldDOB = new javax.swing.JTextField();
        jLabelAge = new javax.swing.JLabel();
        jTextFieldAge = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxETPType = new JComboBox(new ETPType().getCodeDescriptions());
        jLabel5 = new javax.swing.JLabel();
        jTextFieldEligibleServiceDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldCalculationDate = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldPre071983Days = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldPost061983Days = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldTotalETPAmount = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldTaxRate = new javax.swing.JTextField();
        jLabelEncashment = new javax.swing.JLabel();
        jRadioButtonEncashmentNo = new javax.swing.JRadioButton();
        jRadioButtonEncashmentYes = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldNonQualifyingKnown = new javax.swing.JTextField();
        jTextFieldCGTExemptKnown = new javax.swing.JTextField();
        jTextFieldUndeductedKnown = new javax.swing.JTextField();
        jTextFieldConcessionalKnown = new javax.swing.JTextField();
        jTextFieldPost061994InvKnown = new javax.swing.JTextField();
        jPanelRight = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextFieldNonQualifying = new javax.swing.JTextField();
        jTextFieldCGTExempt = new javax.swing.JTextField();
        jTextFieldUndeducted = new javax.swing.JTextField();
        jTextFieldConcessional = new javax.swing.JTextField();
        jTextFieldPre071983 = new javax.swing.JTextField();
        jTextFieldPostJune1983Taxed = new javax.swing.JTextField();
        jTextFieldPostJune1983Untaxed = new javax.swing.JTextField();
        jTextFieldPost061994Inv = new javax.swing.JTextField();
        jTextFieldTotalETPComponents = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextFieldETPTax = new javax.swing.JTextField();
        jTextFieldAssesableIncome = new javax.swing.JTextField();
        jTextFieldAssesableIncomeTax = new javax.swing.JTextField();
        jTextFieldTotalTax = new javax.swing.JTextField();
        jLabelRBLAmount = new javax.swing.JLabel();
        jTextFieldRBLAmount = new javax.swing.JTextField();
        jPanelControls = new javax.swing.JPanel();
        jPanelCloseSave = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButtonAllocatedPension = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(650, 420));
        jPanelETPData.setLayout(new javax.swing.BoxLayout(jPanelETPData,
                javax.swing.BoxLayout.X_AXIS));

        jPanelLeft.setLayout(new javax.swing.BoxLayout(jPanelLeft,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        jPanel7
                .setBorder(new javax.swing.border.TitledBorder("ClientView Details"));
        jLabelDOB.setText("ClientView DOB");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabelDOB, gridBagConstraints1);

        jTextFieldDOB.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldDOB.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldDOB, gridBagConstraints1);

        jLabelAge.setText("ClientView Age");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabelAge, gridBagConstraints1);

        jTextFieldAge.setEditable(false);
        jTextFieldAge.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldAge, gridBagConstraints1);

        jLabel1.setText("Type of ETP");
        jLabel1.setPreferredSize(NAME_DIM);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel1, gridBagConstraints1);

        jComboBoxETPType.setPreferredSize(VALUE_DIM);
        jComboBoxETPType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxETPTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jComboBoxETPType, gridBagConstraints1);

        jLabel5.setText("Eligible Service Date");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel5, gridBagConstraints1);

        jTextFieldEligibleServiceDate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEligibleServiceDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldEligibleServiceDate, gridBagConstraints1);

        jLabel6.setText("Calculation Date");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel6, gridBagConstraints1);

        jTextFieldCalculationDate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCalculationDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldCalculationDate, gridBagConstraints1);

        jLabel7.setText("Pre July 1983 Days");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel7, gridBagConstraints1);

        jTextFieldPre071983Days.setEditable(false);
        jTextFieldPre071983Days
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldPre071983Days, gridBagConstraints1);

        jLabel8.setText("Post June 1983 Days");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel8, gridBagConstraints1);

        jTextFieldPost061983Days.setEditable(false);
        jTextFieldPost061983Days
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldPost061983Days, gridBagConstraints1);

        jLabel9.setText("Total ETP Amount");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel9, gridBagConstraints1);

        jTextFieldTotalETPAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalETPAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldTotalETPAmount, gridBagConstraints1);

        jLabel10.setText("ClientView Tax Rate");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel10, gridBagConstraints1);

        jTextFieldTaxRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTaxRate.setInputVerifier(PercentInputVerifier.getInstance());
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jTextFieldTaxRate, gridBagConstraints1);

        jLabelEncashment.setText("Encashment");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabelEncashment, gridBagConstraints1);

        jRadioButtonEncashmentNo.setText("No");
        buttonGroupEncashment.add(jRadioButtonEncashmentNo);
        jRadioButtonEncashmentNo
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonEncashmentYesItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel7.add(jRadioButtonEncashmentNo, gridBagConstraints1);

        jRadioButtonEncashmentYes.setText("Yes");
        buttonGroupEncashment.add(jRadioButtonEncashmentYes);
        jRadioButtonEncashmentYes
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonEncashmentYesItemStateChanged(evt);
                    }
                });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 10;
        jPanel7.add(jRadioButtonEncashmentYes, gridBagConstraints1);

        jPanelLeft.add(jPanel7);

        jPanel8.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        jPanel8.setBorder(new javax.swing.border.TitledBorder(
                "Known ETP Components"));
        jLabel20.setText("Non-Qualifying");
        jLabel20.setPreferredSize(NAME_DIM);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel20, gridBagConstraints2);

        jLabel21.setText("CGT Exempt");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel21, gridBagConstraints2);

        jLabel25.setText("Undeducted");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel25, gridBagConstraints2);

        jLabel26.setText("Concessional");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel26, gridBagConstraints2);

        jLabel27.setText("Post June 94 Invalidity");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel27, gridBagConstraints2);

        jTextFieldNonQualifyingKnown
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldNonQualifyingKnown.setPreferredSize(VALUE_DIM);
        jTextFieldNonQualifyingKnown.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jTextFieldNonQualifyingKnown, gridBagConstraints2);

        jTextFieldCGTExemptKnown
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCGTExemptKnown.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jTextFieldCGTExemptKnown, gridBagConstraints2);

        jTextFieldUndeductedKnown
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUndeductedKnown.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jTextFieldUndeductedKnown, gridBagConstraints2);

        jTextFieldConcessionalKnown
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalKnown.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jTextFieldConcessionalKnown, gridBagConstraints2);

        jTextFieldPost061994InvKnown
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPost061994InvKnown.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 4;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jTextFieldPost061994InvKnown, gridBagConstraints2);

        jPanelLeft.add(jPanel8);

        jPanelETPData.add(jPanelLeft);

        jPanelRight.setLayout(new javax.swing.BoxLayout(jPanelRight,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints3;

        jPanel9
                .setBorder(new javax.swing.border.TitledBorder("Calculated ETP"));
        jLabel11.setText("Non-Qualifying");
        jLabel11.setPreferredSize(NAME_DIM);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel11, gridBagConstraints3);

        jLabel12.setText("CGT Exempt");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel12, gridBagConstraints3);

        jLabel13.setText("Undeducted");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel13, gridBagConstraints3);

        jLabel14.setText("Concessional");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel14, gridBagConstraints3);

        jLabel15.setText("Pre July 83");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel15, gridBagConstraints3);

        jLabel16.setText("Post June 83 (Taxed)");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel16, gridBagConstraints3);

        jLabel22.setText("Post June 83 (Untaxed)");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel22, gridBagConstraints3);

        jLabel23.setText("Post June 94 Invalidity");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel23, gridBagConstraints3);

        jLabel24.setText("Total ETP Components");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 8;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel24, gridBagConstraints3);

        jTextFieldNonQualifying.setEditable(false);
        jTextFieldNonQualifying
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldNonQualifying.setPreferredSize(VALUE_DIM);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldNonQualifying, gridBagConstraints3);

        jTextFieldCGTExempt.setEditable(false);
        jTextFieldCGTExempt
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldCGTExempt, gridBagConstraints3);

        jTextFieldUndeducted.setEditable(false);
        jTextFieldUndeducted
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldUndeducted, gridBagConstraints3);

        jTextFieldConcessional.setEditable(false);
        jTextFieldConcessional
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldConcessional, gridBagConstraints3);

        jTextFieldPre071983.setEditable(false);
        jTextFieldPre071983
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldPre071983, gridBagConstraints3);

        jTextFieldPostJune1983Taxed.setEditable(false);
        jTextFieldPostJune1983Taxed
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldPostJune1983Taxed, gridBagConstraints3);

        jTextFieldPostJune1983Untaxed.setEditable(false);
        jTextFieldPostJune1983Untaxed
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldPostJune1983Untaxed, gridBagConstraints3);

        jTextFieldPost061994Inv.setEditable(false);
        jTextFieldPost061994Inv
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldPost061994Inv, gridBagConstraints3);

        jTextFieldTotalETPComponents.setEditable(false);
        jTextFieldTotalETPComponents
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 8;
        gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel9.add(jTextFieldTotalETPComponents, gridBagConstraints3);

        jPanelRight.add(jPanel9);

        jPanel10.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints4;

        jPanel10.setBorder(new javax.swing.border.TitledBorder("Tax Payable"));
        jLabel28.setText("Tax on ETP");
        jLabel28.setPreferredSize(NAME_DIM);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel28, gridBagConstraints4);

        jLabel29.setText("Assessable Income");
        jLabel29.setToolTipText("Includes 5% of Pre July 83 and Concessional");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel29, gridBagConstraints4);

        jLabel30.setText("Tax on Ass Income");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel30, gridBagConstraints4);

        jLabel31.setText("Total Tax Payable");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel31, gridBagConstraints4);

        jTextFieldETPTax.setEditable(false);
        jTextFieldETPTax
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldETPTax.setPreferredSize(VALUE_DIM);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextFieldETPTax, gridBagConstraints4);

        jTextFieldAssesableIncome.setEditable(false);
        jTextFieldAssesableIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextFieldAssesableIncome, gridBagConstraints4);

        jTextFieldAssesableIncomeTax.setEditable(false);
        jTextFieldAssesableIncomeTax
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextFieldAssesableIncomeTax, gridBagConstraints4);

        jTextFieldTotalTax.setEditable(false);
        jTextFieldTotalTax
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel10.add(jTextFieldTotalTax, gridBagConstraints4);

        jLabelRBLAmount.setText("RBL Amounts");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabelRBLAmount, gridBagConstraints4);

        jTextFieldRBLAmount.setToolTipText("RBL Amounts already counted");
        jTextFieldRBLAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRBLAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
        jPanel10.add(jTextFieldRBLAmount, gridBagConstraints4);

        jPanelRight.add(jPanel10);

        jPanelETPData.add(jPanelRight);

        add(jPanelETPData);

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
        jButtonSave.setDefaultCapable(false);
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSave);

        jPanelControls.add(jPanelCloseSave);

        jButtonAllocatedPension.setToolTipText("Allocated Pension Calculation");
        jButtonAllocatedPension.setText("Allocated Pension");
        jButtonAllocatedPension.setDefaultCapable(false);
        jButtonAllocatedPension
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonAllocatedPensionActionPerformed(evt);
                    }
                });

        jPanel3.add(jButtonAllocatedPension);

        jPanelControls.add(jPanel3);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonAllocatedPensionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAllocatedPensionActionPerformed
        AllocatedPensionView view = AllocatedPensionView.displayAP(
                IMenuCommand.NEW.getSecond().toString(), listeners);
        view.etpCalc.assign(etpCalc);
        view.updateEditable();
    }// GEN-LAST:event_jButtonAllocatedPensionActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        // Add your handling code here:
        saveView();

    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed
        // Add your handling code here:
        SwingUtil.setVisible(this, false);
    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jRadioButtonEncashmentYesItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonEncashmentYesItemStateChanged
        if (evt.getSource() != jRadioButtonEncashmentYes)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        etpCalc
                .setEncashment(jRadioButtonEncashmentYes.isSelected() ? Boolean.TRUE
                        : Boolean.FALSE);
        SwingUtil.setEnabled(jTextFieldTaxRate, true);
    }// GEN-LAST:event_jRadioButtonEncashmentYesItemStateChanged

    private void jComboBoxETPTypeItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxETPTypeItemStateChanged
        if (evt.getSource() != jComboBoxETPType)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            etpCalc.setETPTypeID(null);
        else
            etpCalc.setETPTypeID(new ETPType().getCodeID(s));
    }// GEN-LAST:event_jComboBoxETPTypeItemStateChanged

    private static ETPRolloverView view;

    public static ETPRolloverView display(String title,
            java.awt.event.FocusListener[] listeners) {

        if (view == null) {
            view = new ETPRolloverView();
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
    private javax.swing.ButtonGroup buttonGroupEncashment;

    protected javax.swing.JPanel jPanelETPData;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JLabel jLabelDOB;

    private javax.swing.JTextField jTextFieldDOB;

    private javax.swing.JLabel jLabelAge;

    private javax.swing.JTextField jTextFieldAge;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JComboBox jComboBoxETPType;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JTextField jTextFieldEligibleServiceDate;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JTextField jTextFieldCalculationDate;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JTextField jTextFieldPre071983Days;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JTextField jTextFieldPost061983Days;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JTextField jTextFieldTotalETPAmount;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JTextField jTextFieldTaxRate;

    private javax.swing.JLabel jLabelEncashment;

    private javax.swing.JRadioButton jRadioButtonEncashmentNo;

    private javax.swing.JRadioButton jRadioButtonEncashmentYes;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JTextField jTextFieldNonQualifyingKnown;

    private javax.swing.JTextField jTextFieldCGTExemptKnown;

    private javax.swing.JTextField jTextFieldUndeductedKnown;

    private javax.swing.JTextField jTextFieldConcessionalKnown;

    private javax.swing.JTextField jTextFieldPost061994InvKnown;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JTextField jTextFieldNonQualifying;

    private javax.swing.JTextField jTextFieldCGTExempt;

    private javax.swing.JTextField jTextFieldUndeducted;

    private javax.swing.JTextField jTextFieldConcessional;

    private javax.swing.JTextField jTextFieldPre071983;

    private javax.swing.JTextField jTextFieldPostJune1983Taxed;

    private javax.swing.JTextField jTextFieldPostJune1983Untaxed;

    private javax.swing.JTextField jTextFieldPost061994Inv;

    private javax.swing.JTextField jTextFieldTotalETPComponents;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel30;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JTextField jTextFieldETPTax;

    private javax.swing.JTextField jTextFieldAssesableIncome;

    private javax.swing.JTextField jTextFieldAssesableIncomeTax;

    private javax.swing.JTextField jTextFieldTotalTax;

    private javax.swing.JLabel jLabelRBLAmount;

    private javax.swing.JTextField jTextFieldRBLAmount;

    protected javax.swing.JPanel jPanelControls;

    protected javax.swing.JPanel jPanelCloseSave;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JButton jButtonAllocatedPension;

    // End of variables declaration//GEN-END:variables

    /**
     * Viewble interface
     */
    public Integer getObjectType() {
        return null;
    }

    public void updateView(String modelTitle) throws java.io.IOException {

        PersonService person = ServiceLocator.getInstance().getClientPerson();
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
            // etpCalc.disableUpdate();

            // clearView();

            etpCalc.setModel(newModel);
            etpCalc.setSaved();
        } finally {
            updateEditable();
            etpCalc.doUpdate();
            // etpCalc.enableUpdate();
        }

    }

    public void updateView(com.argus.financials.service.PersonService person)
            throws ServiceException {

        // load data from person, e.g. DOB
        // saveView();

        if (person == null)
            return;

        try {
            etpCalc.disableUpdate();

            clearView();

            PersonName personName = person.getPersonName();
            etpCalc.setDateOfBirth(personName.getDateOfBirth());
            etpCalc.setSexCodeID(personName.getSexCodeID());

        } finally {
            updateEditable();
            etpCalc.doUpdate();
            etpCalc.enableUpdate();
        }

    }

    private void saveView() {

        String oldTitle = getTitle();
        try {
            int result = SaveProjectionDialog.getSaveProjectionInstance().save(
                    etpCalc, this);
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

            return;
        } catch (ModelTitleRestrictionException me) {

        }

        try {
            saveView(ServiceLocator.getInstance().getClientPerson());
        } catch (Exception e) {
            e.printStackTrace(System.err);
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
        etpCalc.setSaved();

    }

    public void clearView() {
        SwingUtil.clear(this);

        etpCalc.setModel(null);
        setTitle();
    }

    /**
     * helper methods
     */
    public void save() {

    }

    public void updateEditable() {

        double d;
        String s;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        // read/write components
        updateOthers();

        jTextFieldDOB.setText(DateTimeUtils.asString(etpCalc.getDateOfBirth()));

        jComboBoxETPType.setSelectedItem(etpCalc.getETPTypeDesc());

        jTextFieldEligibleServiceDate.setText(DateTimeUtils.asString(etpCalc
                .getEligibleServiceDate()));

        jTextFieldCalculationDate.setText(DateTimeUtils.asString(etpCalc
                .getCalculationDate()));

        d = etpCalc.getTotalETPAmount();
        jTextFieldTotalETPAmount.setText(d < 0 ? null : curr.toString(d));

        d = etpCalc.getTaxRate();
        jTextFieldTaxRate.setText(d < 0 ? null : percent.toString(d));

        d = etpCalc.getNonQualifyingAmountKnown();
        jTextFieldNonQualifyingKnown.setText(curr.toString(d));

        d = etpCalc.getCGTExemptAmountKnown();
        jTextFieldCGTExemptKnown.setText(curr.toString(d));

        d = etpCalc.getUndeductedAmountKnown();
        jTextFieldUndeductedKnown.setText(curr.toString(d));

        d = etpCalc.getConcessionalAmountKnown();
        jTextFieldConcessionalKnown.setText(curr.toString(d));

        d = etpCalc.getPost061994InvAmountKnown();
        // jTextFieldPost061994InvKnown.setText( d < 0 ? null : curr.toString( d
        // ) );
        jTextFieldPost061994InvKnown.setText(curr.toString(d));

        d = etpCalc.getRBLAmount();
        jTextFieldRBLAmount.setText(curr.toString(d));
    }

    private void updateOthers() {

        jComboBoxETPType.setSelectedItem(etpCalc.getETPTypeDesc());

        if (etpCalc.getEncashment() == null) {
            jRadioButtonEncashmentYes.setSelected(false);
            jRadioButtonEncashmentNo.setSelected(false);
        } else {
            if (etpCalc.getEncashment().booleanValue())
                jRadioButtonEncashmentYes.setSelected(true);
            else
                jRadioButtonEncashmentNo.setSelected(true);
        }
        SwingUtil.setEnabled(jTextFieldTaxRate, true);

    }

    public void updateNonEditable() {

        int n;
        double d;
        String s = null;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        updateOthers();

        n = etpCalc.getAge();
        jTextFieldAge.setText(n < 0 ? null : "" + n);

        n = etpCalc.getPre071983Days();
        jTextFieldPre071983Days.setText(n < 0 ? null : "" + n);

        n = etpCalc.getPost061983Days();
        jTextFieldPost061983Days.setText(n < 0 ? null : "" + n);

        // read only components
        d = etpCalc.getNonQualifyingAmount();
        jTextFieldNonQualifying.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = etpCalc.getCGTExemptAmount();
        jTextFieldCGTExempt.setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                .toString(d));

        d = etpCalc.getUndeductedAmount();
        jTextFieldUndeducted.setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                .toString(d));

        d = etpCalc.getConcessionalAmount();
        jTextFieldConcessional.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = etpCalc.getPre071983Amount();
        jTextFieldPre071983.setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                .toString(d));

        d = etpCalc.getPost061983AmountTaxed();
        jTextFieldPostJune1983Taxed.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = etpCalc.getPost061983AmountUntaxed();
        jTextFieldPostJune1983Untaxed
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = etpCalc.getPost061994InvAmount();
        jTextFieldPost061994Inv.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = etpCalc.getTotalETPComponentsAmount();
        jTextFieldTotalETPComponents
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = etpCalc.getETPTaxAmount();
        jTextFieldETPTax.setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                .toString(d));

        d = etpCalc.getAssesableIncomeAmount();
        jTextFieldAssesableIncome.setText(d == MoneyCalc.UNKNOWN_VALUE ? null
                : curr.toString(d));

        d = etpCalc.getAssesableIncomeTaxAmount();
        jTextFieldAssesableIncomeTax
                .setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr.toString(d));

        d = etpCalc.getTotalTaxAmount();
        jTextFieldTotalTax.setText(d == MoneyCalc.UNKNOWN_VALUE ? null : curr
                .toString(d));

    }

    protected void updateComponents() {
        // enable/disable save option
        jButtonSave
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);

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

                PersonService person = ServiceLocator.getInstance().getClientPerson();
                if (person == null)
                    return;

                // remove from menu
                FinancialPlannerApp.getInstance().updateModels();

                try {
                    person.removeModel(getModel());
                    person.storeModels();
                } catch (com.argus.financials.service.ServiceException e) {
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
