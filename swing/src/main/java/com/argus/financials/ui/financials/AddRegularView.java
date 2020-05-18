/*
 * AddRegularView2.java
 *
 * Created on 22 May 2003, 09:03
 */

package com.argus.financials.ui.financials;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.bean.ICode;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.argus.financials.bean.Asset;
import com.argus.financials.bean.Assets;
import com.argus.financials.bean.Regular;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public abstract class AddRegularView extends AddFinancialView {

    private boolean thisCTOR = false;

    /** Creates new form AddRegularView */
    protected AddRegularView() {
        setPreferredSize(new java.awt.Dimension(getDefaultWidth(),
                getDefaultHeight()));
        setMinimumSize(new java.awt.Dimension(getDefaultWidth(),
                getDefaultHeight()));

        thisCTOR = true;
        try {
            initComponents();
            // setLayout(new javax.swing.BoxLayout(this,
            // javax.swing.BoxLayout.Y_AXIS));
        } finally {
            thisCTOR = false;
        }

    }

    protected int getDefaultHeight() {
        return super.getDefaultHeight();
    }

    private Assets assets;

    public void setAssets(Assets assets) {
        this.assets = assets;
        jComboBoxAssociatedAsset.setModel(new javax.swing.DefaultComboBoxModel(
                this.assets.getCodes().toArray()));
        pack();
    }

    public void setVisible(boolean b) {
        /*
         * if ( b ) {
         * 
         * Object item = getRegular().getAsset(); if ( item != null )
         * jComboBoxAssociatedAsset.setSelectedItem( item ); else if (
         * jComboBoxAssociatedAsset.getItemCount() > 0 )
         * jComboBoxAssociatedAsset.setSelectedIndex(0);
         *  }
         */
        super.setVisible(b);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelName = new javax.swing.JPanel();
        jLabelType = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox(financialService.findFinancialTypes(getObjectType()));
        jLabelOwnerCode = new javax.swing.JLabel();
        jComboBoxOwnerCode = new javax.swing.JComboBox(OWNERS);
        jLabelDesc = new javax.swing.JLabel();
        jTextFieldDesc = new javax.swing.JTextField();
        jLabelAssociatedAsset = new javax.swing.JLabel();
        jComboBoxAssociatedAsset = new javax.swing.JComboBox();
        jPanelValue = new javax.swing.JPanel();
        jLabelRegularAmount = new javax.swing.JLabel();
        jTextFieldRegularAmount = new javax.swing.JTextField();
        jLabelStartDate = new javax.swing.JLabel();
        jTextFieldStartDate = new com.argus.bean.FDateChooser();
        jLabelEndDate = new javax.swing.JLabel();
        jTextFieldEndDate = new com.argus.bean.FDateChooser();
        jLabelIndexation = new javax.swing.JLabel();
        jTextFieldIndexation = new javax.swing.JTextField();
        jLabelFrequency = new javax.swing.JLabel();
        jComboBoxFrequency = new javax.swing.JComboBox(new Object[] {
                ReferenceCode.CODE_NONE, FrequencyCode.rcONLY_ONCE,
                FrequencyCode.rcWEEKLY, FrequencyCode.rcFORTNIGHTLY,
                FrequencyCode.rcTWICE_MONTHLY, FrequencyCode.rcMONTHLY,
                FrequencyCode.rcEVERY_OTHER_MONTH,
                FrequencyCode.rcEVERY_THREE_MONTHS,
                FrequencyCode.rcHALF_YEARLY, FrequencyCode.rcYEARLY, });
        jLabelTaxable = new javax.swing.JLabel();
        jCheckBoxTaxable = new javax.swing.JCheckBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new java.awt.GridBagLayout());

        jPanelName.setBorder(new javax.swing.border.TitledBorder("Name"));
        jLabelType.setText("Regular Type *");
        jLabelType.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelType.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxType, gridBagConstraints);

        jLabelOwnerCode.setText("Owner *");
        jLabelOwnerCode.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOwnerCode.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelOwnerCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxOwnerCode, gridBagConstraints);

        jLabelDesc.setText("Notes/Comments");
        jLabelDesc.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDesc.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelName.add(jLabelDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelName.add(jTextFieldDesc, gridBagConstraints);

        jLabelAssociatedAsset.setText("Associated Asset");
        jLabelAssociatedAsset.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAssociatedAsset.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelAssociatedAsset, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxAssociatedAsset, gridBagConstraints);

        add(jPanelName);

        jPanelValue.setLayout(new java.awt.GridBagLayout());

        jPanelValue.setBorder(new javax.swing.border.TitledBorder("Value"));
        jLabelRegularAmount.setText("Amount *");
        jLabelRegularAmount.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelRegularAmount.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelRegularAmount, gridBagConstraints);

        jTextFieldRegularAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRegularAmount
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldRegularAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldRegularAmount.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldRegularAmount, gridBagConstraints);

        jLabelStartDate.setText("Start Date");
        jLabelStartDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelStartDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelStartDate, gridBagConstraints);

        jTextFieldStartDate.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldStartDate.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldStartDate.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldStartDate, gridBagConstraints);

        jLabelEndDate.setText("End Date");
        jLabelEndDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelEndDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelEndDate, gridBagConstraints);

        jTextFieldEndDate.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldEndDate, gridBagConstraints);

        jLabelIndexation.setText("Indexation (% p.a.)");
        jLabelIndexation.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelIndexation.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelIndexation, gridBagConstraints);

        jTextFieldIndexation.setToolTipText("Indexation of Contributions");
        jTextFieldIndexation
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIndexation.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldIndexation, gridBagConstraints);

        jLabelFrequency.setText("Frequency *");
        jLabelFrequency.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelFrequency.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelFrequency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jComboBoxFrequency, gridBagConstraints);

        jLabelTaxable.setText("Taxable");
        jLabelTaxable.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelTaxable.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelTaxable, gridBagConstraints);

        jCheckBoxTaxable.setPreferredSize(new java.awt.Dimension(120, 19));
        jCheckBoxTaxable.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jCheckBoxTaxable, gridBagConstraints);

        add(jPanelValue);

    }// GEN-END:initComponents

    // overrite Container method to allow design time UI development
    public void add(java.awt.Component comp, Object constraints) {
        jPanelDetails.add(comp, constraints);
    }

    public java.awt.Component add(java.awt.Component comp) {
        return thisCTOR ? jPanelDetails.add(comp) : super.add(comp);
        // return jPanelDetails.add( comp ); // ??? called from base class ctor
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelName;

    private javax.swing.JTextField jTextFieldDesc;

    protected javax.swing.JLabel jLabelAssociatedAsset;

    private com.argus.bean.FDateChooser jTextFieldEndDate;

    protected javax.swing.JComboBox jComboBoxAssociatedAsset;

    private javax.swing.JLabel jLabelDesc;

    private javax.swing.JPanel jPanelValue;

    private com.argus.bean.FDateChooser jTextFieldStartDate;

    protected javax.swing.JLabel jLabelIndexation;

    private javax.swing.JComboBox jComboBoxFrequency;

    protected javax.swing.JLabel jLabelType;

    private javax.swing.JLabel jLabelRegularAmount;

    private javax.swing.JLabel jLabelFrequency;

    private javax.swing.JLabel jLabelEndDate;

    protected javax.swing.JCheckBox jCheckBoxTaxable;

    protected javax.swing.JComboBox jComboBoxType;

    private javax.swing.JTextField jTextFieldRegularAmount;

    private javax.swing.JLabel jLabelStartDate;

    private javax.swing.JComboBox jComboBoxOwnerCode;

    private javax.swing.JLabel jLabelOwnerCode;

    protected javax.swing.JTextField jTextFieldIndexation;

    protected javax.swing.JLabel jLabelTaxable;

    // End of variables declaration//GEN-END:variables

    public boolean updateView() {

        if (!super.updateView())
            return false;

        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        Regular regular = getRegular();

        ICode refCode = regular.getFinancialType();
        jComboBoxType.setSelectedItem(refCode);

        jTextFieldDesc.setText(regular.getFinancialDesc());

        Integer id = regular.getOwnerCodeID();
        jComboBoxOwnerCode.setSelectedItem(new OwnerCode()
                .getCodeDescription(id));

        java.util.Date date = regular.getStartDate();
        if (date == null)
            jTextFieldStartDate.setText(null);
        else
            jTextFieldStartDate.setText(DateTimeUtils.asString(date, null));

        date = regular.getEndDate();
        if (date == null)
            jTextFieldEndDate.setText(null);
        else
            jTextFieldEndDate.setText(DateTimeUtils.asString(date, null));

        java.math.BigDecimal amount = regular.getRegularAmount();
        jTextFieldRegularAmount.setText(amount == null ? null : curr
                .toString(amount));

        java.math.BigDecimal indexation = regular.getIndexation();
        jTextFieldIndexation.setText(indexation == null ? null : percent
                .toString(indexation));

        jComboBoxFrequency.setSelectedItem(FrequencyCode.getCode(regular
                .getFrequencyCodeID()));

        Object item = regular.getAsset(assets);
        if (item != null)
            jComboBoxAssociatedAsset.setSelectedItem(item);
        else if (jComboBoxAssociatedAsset.getItemCount() > 0)
            jComboBoxAssociatedAsset.setSelectedIndex(0);

        jCheckBoxTaxable.setSelected(regular.isTaxable());

        SwingUtil.setEnabled(jPanelDetails, !regular.isGenerated());

        return true;

    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {

        String msg = "";

        Currency curr = getCurrencyInstance();

        Integer ownerCodeID = new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem());
        if (ownerCodeID == null)
            msg += "Owner is required.\n";

        java.math.BigDecimal amount = curr
                .getBigDecimalValue(jTextFieldRegularAmount.getText());
        if (amount == null)
            msg += "Amount is required.\n";

        if (jComboBoxFrequency.getSelectedIndex() <= 0)
            msg += "Frequency is required.\n";

        msg += checkDateField(jTextFieldEndDate, "End Date");
        msg += checkDateField(jTextFieldStartDate, "Start Date");

        if (msg.length() == 0)
            return;

        if (showMessage)
            javax.swing.JOptionPane.showMessageDialog(this, msg, "ERROR",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        throw new InvalidCodeException(msg);

    }

    public boolean saveView() throws InvalidCodeException {

        // checkRequiredFields(true); // abstract class

        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        Regular regular = getRegular();

        ReferenceCode refCode = (ReferenceCode) jComboBoxType.getSelectedItem();
        regular.setFinancialType(refCode);

        regular.setFinancialDesc(jTextFieldDesc.getText());

        String s = (String) jComboBoxOwnerCode.getSelectedItem();
        regular.setOwnerCodeID(new OwnerCode().getCodeID(s));

        java.util.Date date = DateTimeUtils.getSqlDate(jTextFieldStartDate
                .getText());
        regular.setStartDate(date);

        date = DateTimeUtils.getSqlDate(jTextFieldEndDate.getText());
        regular.setEndDate(date);

        java.math.BigDecimal amount = curr
                .getBigDecimalValue(jTextFieldRegularAmount.getText());
        regular.setRegularAmount(amount);

        java.math.BigDecimal indexation = percent
                .getBigDecimalValue(jTextFieldIndexation.getText());
        regular.setIndexation(indexation);

        refCode = (ReferenceCode) jComboBoxFrequency.getSelectedItem();
        regular.setFrequencyCodeID(refCode.getCodeId());

        // date = DateTime.getSqlDate( jTextFieldNextDate.getText() );
        // regular.setNextDate( date );

        Object item = jComboBoxAssociatedAsset.getSelectedItem();
        regular.setAsset(item instanceof Asset ? (Asset) item : null);

        regular.setTaxable(jCheckBoxTaxable.isSelected());

        return true;

    }

    public abstract Regular getRegular();

}
