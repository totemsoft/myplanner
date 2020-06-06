/*
 * AddLiabilityView3.java
 *
 * Created on 22 May 2003, 10:07
 */

package au.com.totemsoft.myplanner.swing.financials;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.Percent;
import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.bean.ICode;
import au.com.totemsoft.myplanner.bean.Liability;
import au.com.totemsoft.myplanner.code.FrequencyCode;
import au.com.totemsoft.myplanner.code.Institution;
import au.com.totemsoft.myplanner.code.OwnerCode;
import au.com.totemsoft.swing.CurrencyInputVerifier;
import au.com.totemsoft.swing.DateInputVerifier;
import au.com.totemsoft.swing.PercentInputVerifier;
import au.com.totemsoft.util.DateTimeUtils;
import au.com.totemsoft.util.ReferenceCode;

public class AddLiabilityView extends AddFinancialView {

    private boolean thisCTOR = false;

    private static AddLiabilityView view;

    /** Creates new form AddLiability */
    private AddLiabilityView() {
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
        return 350;
    }

    public static AddLiabilityView getInstance() {
        if (view == null)
            view = new AddLiabilityView();
        return view;
    }

    public static boolean exists() {
        return view != null;
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
        jLabelInstitution = new javax.swing.JLabel();
        jComboBoxInstitution = new javax.swing.JComboBox(new Institution()
                .getCodeDescriptions());
        jLabelOwnerCode = new javax.swing.JLabel();
        jComboBoxOwnerCode = new javax.swing.JComboBox(OWNERS);
        jLabelDesc = new javax.swing.JLabel();
        jTextFieldDesc = new javax.swing.JTextField();
        jLabelAccountNumber = new javax.swing.JLabel();
        jTextFieldAccountNumber = new javax.swing.JTextField();
        jPanelValue = new javax.swing.JPanel();
        jLabelRepaymentAmount = new javax.swing.JLabel();
        jTextFieldRepaymentAmount = new javax.swing.JTextField();
        jLabelStartDate = new javax.swing.JLabel();
        jTextFieldStartDate = new au.com.totemsoft.bean.FDateChooser();
        jLabelEndDate = new javax.swing.JLabel();
        jTextFieldEndDate = new au.com.totemsoft.bean.FDateChooser();
        jLabelInterestRate = new javax.swing.JLabel();
        jTextFieldInterestRate = new javax.swing.JTextField();
        jLabelRepaymentFrequency = new javax.swing.JLabel();
        jComboBoxRepaymentFrequency = new javax.swing.JComboBox(new Object[] {
                ReferenceCode.CODE_NONE, FrequencyCode.rcONLY_ONCE,
                FrequencyCode.rcWEEKLY, FrequencyCode.rcFORTNIGHTLY,
                FrequencyCode.rcTWICE_MONTHLY, FrequencyCode.rcMONTHLY,
                FrequencyCode.rcEVERY_OTHER_MONTH,
                FrequencyCode.rcEVERY_THREE_MONTHS,
                FrequencyCode.rcHALF_YEARLY, FrequencyCode.rcYEARLY, });
        jLabelAmount = new javax.swing.JLabel();
        jTextFieldAmount = new javax.swing.JTextField();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new java.awt.GridBagLayout());

        jPanelName.setBorder(new javax.swing.border.TitledBorder("Name"));
        jLabelType.setText("Liability Type *");
        jLabelType.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelType.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxType, gridBagConstraints);

        jLabelInstitution.setText("Institution");
        jLabelInstitution.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelInstitution.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelName.add(jLabelInstitution, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxInstitution, gridBagConstraints);

        jLabelOwnerCode.setText("Owner *");
        jLabelOwnerCode.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOwnerCode.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelName.add(jLabelOwnerCode, gridBagConstraints);

        jComboBoxOwnerCode.setPreferredSize(new java.awt.Dimension(120, 19));
        jComboBoxOwnerCode.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jComboBoxOwnerCode, gridBagConstraints);

        jLabelDesc.setText("Notes/Comments");
        jLabelDesc.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDesc.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelName.add(jLabelDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jTextFieldDesc, gridBagConstraints);

        jLabelAccountNumber.setText("Account Number");
        jLabelAccountNumber.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAccountNumber.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelName.add(jLabelAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jTextFieldAccountNumber, gridBagConstraints);

        add(jPanelName);

        jPanelValue.setLayout(new java.awt.GridBagLayout());

        jPanelValue.setBorder(new javax.swing.border.TitledBorder("Value"));
        jLabelRepaymentAmount.setText("Repayment Amount");
        jLabelRepaymentAmount.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelRepaymentAmount.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelRepaymentAmount, gridBagConstraints);

        jTextFieldRepaymentAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRepaymentAmount.setPreferredSize(new java.awt.Dimension(120,
                19));
        jTextFieldRepaymentAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldRepaymentAmount
                .setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldRepaymentAmount, gridBagConstraints);

        jLabelStartDate.setText("Start Date");
        jLabelStartDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelStartDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelStartDate, gridBagConstraints);

        jTextFieldStartDate.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldStartDate.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldStartDate.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldStartDate, gridBagConstraints);

        jLabelEndDate.setText("End Date");
        jLabelEndDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelEndDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelEndDate, gridBagConstraints);

        jTextFieldEndDate.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldEndDate, gridBagConstraints);

        jLabelInterestRate.setText("Interest rate (% p.a.)");
        jLabelInterestRate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelInterestRate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelInterestRate, gridBagConstraints);

        jTextFieldInterestRate.setToolTipText("Indexation of Contributions");
        jTextFieldInterestRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInterestRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldInterestRate, gridBagConstraints);

        jLabelRepaymentFrequency.setText("Repayment Frequency");
        jLabelRepaymentFrequency.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelRepaymentFrequency
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelRepaymentFrequency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jComboBoxRepaymentFrequency, gridBagConstraints);

        jLabelAmount.setText("Amount Owing *");
        jLabelAmount.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAmount.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelAmount, gridBagConstraints);

        jTextFieldAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAmount.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldAmount.setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldAmount.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldAmount, gridBagConstraints);

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
    private javax.swing.JLabel jLabelRepaymentAmount;

    private javax.swing.JTextField jTextFieldAccountNumber;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JTextField jTextFieldDesc;

    private au.com.totemsoft.bean.FDateChooser jTextFieldEndDate;

    private javax.swing.JLabel jLabelDesc;

    private javax.swing.JComboBox jComboBoxRepaymentFrequency;

    private javax.swing.JPanel jPanelValue;

    private javax.swing.JComboBox jComboBoxInstitution;

    private au.com.totemsoft.bean.FDateChooser jTextFieldStartDate;

    private javax.swing.JLabel jLabelAmount;

    private javax.swing.JTextField jTextFieldRepaymentAmount;

    private javax.swing.JLabel jLabelType;

    private javax.swing.JLabel jLabelEndDate;

    private javax.swing.JLabel jLabelRepaymentFrequency;

    private javax.swing.JComboBox jComboBoxType;

    private javax.swing.JLabel jLabelStartDate;

    private javax.swing.JTextField jTextFieldAmount;

    private javax.swing.JComboBox jComboBoxOwnerCode;

    private javax.swing.JLabel jLabelOwnerCode;

    protected javax.swing.JTextField jTextFieldInterestRate;

    private javax.swing.JLabel jLabelInstitution;

    protected javax.swing.JLabel jLabelInterestRate;

    private javax.swing.JLabel jLabelAccountNumber;

    // End of variables declaration//GEN-END:variables

    public boolean updateView() {

        if (!super.updateView())
            return false;

        Currency curr = getCurrencyInstance();
        Percent percent = getPercentInstance();

        Liability liability = getLiability();

        ICode refCode = liability.getFinancialType();
        jComboBoxType.setSelectedItem(refCode);

        Integer id = liability.getInstitutionID();
        jComboBoxInstitution.setSelectedItem(new Institution()
                .getCodeDescription(id));

        jTextFieldDesc.setText(liability.getFinancialDesc());

        id = liability.getOwnerCodeID();
        jComboBoxOwnerCode.setSelectedItem(new OwnerCode()
                .getCodeDescription(id));

        jTextFieldAccountNumber.setText(liability.getAccountNumber());

        java.util.Date date = liability.getStartDate();
        if (date == null)
            jTextFieldStartDate.setText(null);
        else
            jTextFieldStartDate.setText(DateTimeUtils.asString(date, null));

        date = liability.getEndDate();
        if (date == null)
            jTextFieldEndDate.setText(null);
        else
            jTextFieldEndDate.setText(DateTimeUtils.asString(date, null));

        java.math.BigDecimal amount = liability.getAmount();
        jTextFieldAmount.setText(amount == null ? null : curr.toString(amount));

        amount = liability.getRegularAmount();
        jTextFieldRepaymentAmount.setText(amount == null ? null : curr
                .toString(amount));

        jComboBoxRepaymentFrequency.setSelectedItem(FrequencyCode
                .getCode(liability.getFrequencyCodeID()));

        Double d = liability.getInterestRate();
        jTextFieldInterestRate.setText(d == null ? null : percent.toString(d));
        /*
         * Object item = liability.getAsset(); if ( item != null )
         * jComboBoxAssociatedAsset.setSelectedItem( item ); else if (
         * jComboBoxAssociatedAsset.getItemCount() > 0 )
         * jComboBoxAssociatedAsset.setSelectedIndex(0);
         */

        return true;

    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {
        String msg = "";

        Currency curr = getCurrencyInstance();

        if (jComboBoxType.getSelectedIndex() <= 0)
            msg += "Liability Type is required.\n";

        Integer ownerCodeID = new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem());
        if (ownerCodeID == null)
            msg += "Owner is required.\n";

        java.math.BigDecimal amount = curr.getBigDecimalValue(jTextFieldAmount
                .getText());
        if (amount == null)
            msg += "Amount Owing is required.\n";

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

        checkRequiredFields(true);

        Currency curr = getCurrencyInstance();
        Percent percent = getPercentInstance();

        Liability liability = getLiability();

        ReferenceCode refCode = (ReferenceCode) jComboBoxType.getSelectedItem();
        liability.setFinancialType(refCode);

        String s = (String) jComboBoxInstitution.getSelectedItem();
        liability.setInstitutionID(new Institution().getCodeID(s));

        liability.setFinancialDesc(jTextFieldDesc.getText());

        s = (String) jComboBoxOwnerCode.getSelectedItem();
        liability.setOwnerCodeID(new OwnerCode().getCodeID(s));

        liability.setAccountNumber(jTextFieldAccountNumber.getText());

        java.util.Date date = DateTimeUtils.getSqlDate(jTextFieldStartDate
                .getText());
        liability.setStartDate(date);

        date = DateTimeUtils.getSqlDate(jTextFieldEndDate.getText());
        liability.setEndDate(date);

        java.math.BigDecimal amount = curr.getBigDecimalValue(jTextFieldAmount
                .getText());
        liability.setAmount(amount);

        amount = curr.getBigDecimalValue(jTextFieldRepaymentAmount.getText());
        liability.setRegularAmount(amount);

        refCode = (ReferenceCode) jComboBoxRepaymentFrequency.getSelectedItem();
        liability.setFrequencyCodeID(refCode.getCodeId());

        Double d = percent.getDoubleValue(jTextFieldInterestRate.getText());
        liability.setInterestRate(d);
        /*
         * Object item = jComboBoxAssociatedAsset.getSelectedItem();
         * liability.setAsset( item instanceof Asset ? (Asset) item : null );
         */

        return true;

    }

    public Liability getLiability() {
        if (getObject() == null)
            setObject(new Liability());
        return (Liability) getObject();
    }

    public Integer getObjectType() {
        return Liability.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_LIABILITY.getDescription();
    }

}