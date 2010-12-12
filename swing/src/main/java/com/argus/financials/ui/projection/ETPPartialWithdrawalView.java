/*
 * ETPPartialWithdrawalView.java
 *
 * Created on 15 July 2002, 13:54
 */

package com.argus.financials.ui.projection;

import java.math.BigDecimal;

import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.projection.data.ETPConstants;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.Currency;

/**
 * 
 * @author shibaevv
 */
public class ETPPartialWithdrawalView extends javax.swing.JPanel {

    private ETPCalcNew etpCalc;

    private int result;

    /** Creates new form ETPPartialWithdrawalView */
    public ETPPartialWithdrawalView(ETPCalcNew calc) {
        etpCalc = calc;
        initComponents();
        initComponents2();
    }

    private void initComponents2() {

        updateEditable();
        updateComponents();
    }

    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldWithdraw = new javax.swing.JTextField();
        jTextFieldRollover = new javax.swing.JTextField();
        jTextFieldTotal = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldUndeductedAvail = new javax.swing.JTextField();
        jTextFieldUndeductedPartial = new javax.swing.JTextField();
        jTextFieldCGTAvail = new javax.swing.JTextField();
        jTextFieldCGTPartial = new javax.swing.JTextField();
        jTextFieldExcessAvail = new javax.swing.JTextField();
        jTextFieldExcessPartial = new javax.swing.JTextField();
        jTextFieldConcessionalAvail = new javax.swing.JTextField();
        jTextFieldConcessionalPartial = new javax.swing.JTextField();
        jTextFieldInvalidityAvail = new javax.swing.JTextField();
        jTextFieldInvalidityPartial = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(400, 300));
        setMinimumSize(new java.awt.Dimension(300, 168));
        setMaximumSize(new java.awt.Dimension(300, 168));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(143, 100));
        jPanel1.setMaximumSize(new java.awt.Dimension(300, 168));
        jLabel1.setText("Withdraw");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Rollover");
        jLabel2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Total");
        jLabel3.setPreferredSize(new java.awt.Dimension(102, 17));
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gridBagConstraints);

        jTextFieldWithdraw
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldWithdraw.setPreferredSize(new java.awt.Dimension(136, 21));
        jTextFieldWithdraw
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldWithdraw
                .setNextFocusableComponent(jTextFieldUndeductedPartial);
        jTextFieldWithdraw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldWithdrawFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(jTextFieldWithdraw, gridBagConstraints);

        jTextFieldRollover.setEditable(false);
        jTextFieldRollover
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRollover
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextFieldRolloverActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(jTextFieldRollover, gridBagConstraints);

        jTextFieldTotal.setEditable(false);
        jTextFieldTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(jTextFieldTotal, gridBagConstraints);

        add(jPanel1);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Undeducted contributions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(jLabel4, gridBagConstraints);

        jLabel5.setText("CGT Exempt");
        jLabel5.setPreferredSize(new java.awt.Dimension(146, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Excess Component");
        jLabel6.setPreferredSize(new java.awt.Dimension(146, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Concessional");
        jLabel7.setPreferredSize(new java.awt.Dimension(146, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(jLabel7, gridBagConstraints);

        jLabel8.setText("Invalidity");
        jLabel8.setPreferredSize(new java.awt.Dimension(146, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Avail");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setPreferredSize(new java.awt.Dimension(40, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel3.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Partial");
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setPreferredSize(new java.awt.Dimension(40, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel3.add(jLabel10, gridBagConstraints);

        jTextFieldUndeductedAvail.setEditable(false);
        jTextFieldUndeductedAvail
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUndeductedAvail.setPreferredSize(new java.awt.Dimension(80,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jTextFieldUndeductedAvail, gridBagConstraints);

        jTextFieldUndeductedPartial
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUndeductedPartial.setPreferredSize(new java.awt.Dimension(80,
                21));
        jTextFieldUndeductedPartial.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldUndeductedPartial
                .setNextFocusableComponent(jTextFieldCGTPartial);
        jTextFieldUndeductedPartial
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldUndeductedPartialFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(jTextFieldUndeductedPartial, gridBagConstraints);

        jTextFieldCGTAvail.setEditable(false);
        jTextFieldCGTAvail
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCGTAvail.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel3.add(jTextFieldCGTAvail, gridBagConstraints);

        jTextFieldCGTPartial
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCGTPartial.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldCGTPartial.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldCGTPartial.setNextFocusableComponent(jTextFieldExcessPartial);
        jTextFieldCGTPartial
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldCGTPartialFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel3.add(jTextFieldCGTPartial, gridBagConstraints);

        jTextFieldExcessAvail.setEditable(false);
        jTextFieldExcessAvail
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldExcessAvail.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel3.add(jTextFieldExcessAvail, gridBagConstraints);

        jTextFieldExcessPartial
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldExcessPartial
                .setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldExcessPartial.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldExcessPartial
                .setNextFocusableComponent(jTextFieldConcessionalPartial);
        jTextFieldExcessPartial
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldExcessPartialFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel3.add(jTextFieldExcessPartial, gridBagConstraints);

        jTextFieldConcessionalAvail.setEditable(false);
        jTextFieldConcessionalAvail
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalAvail.setPreferredSize(new java.awt.Dimension(80,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        jPanel3.add(jTextFieldConcessionalAvail, gridBagConstraints);

        jTextFieldConcessionalPartial
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldConcessionalPartial.setPreferredSize(new java.awt.Dimension(
                80, 21));
        jTextFieldConcessionalPartial.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldConcessionalPartial
                .setNextFocusableComponent(jTextFieldInvalidityPartial);
        jTextFieldConcessionalPartial
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldConcessionalPartialFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel3.add(jTextFieldConcessionalPartial, gridBagConstraints);

        jTextFieldInvalidityAvail.setEditable(false);
        jTextFieldInvalidityAvail
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInvalidityAvail.setPreferredSize(new java.awt.Dimension(80,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        jPanel3.add(jTextFieldInvalidityAvail, gridBagConstraints);

        jTextFieldInvalidityPartial
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInvalidityPartial.setPreferredSize(new java.awt.Dimension(80,
                21));
        jTextFieldInvalidityPartial.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldInvalidityPartial
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldInvalidityPartialFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel3.add(jTextFieldInvalidityPartial, gridBagConstraints);

        add(jPanel3);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(61, 40));
        jPanel2.setMaximumSize(new java.awt.Dimension(300, 40));
        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(81, 27));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton2);

        add(jPanel2);

    }// GEN-END:initComponents

    private void jTextFieldRolloverActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextFieldRolloverActionPerformed
        // Add your handling code here:
    }// GEN-LAST:event_jTextFieldRolloverActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // Add your handling code here:
        SwingUtil.setVisible(this, false);
        result = ETPConstants.CANCEL_OPTION;
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jTextFieldInvalidityPartialFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldInvalidityPartialFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getInvalidityPartial());

        BigDecimal d = null;
        BigDecimal d1 = null;
        String value = jTextFieldInvalidityPartial.getText().trim();
        String value1 = jTextFieldWithdraw.getText().trim();

        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);
        if (value1 != null && value1.length() > 0)
            d1 = Currency.getCurrencyInstance().getBigDecimalValue(value1);

        if (!etpCalc.validatePartialWithdrawComponentInput(
                ETPConstants.INVALIDITY_PARTIAL, d, d1))
            jTextFieldInvalidityPartial.setText(previousValue);
        else {
            jTextFieldInvalidityPartial.setText(value);
            BigDecimal withdraw = etpCalc.getInvalidityAvail(d);
            jTextFieldInvalidityAvail.setText(Currency.getCurrencyInstance()
                    .toString(withdraw));

        }

    }// GEN-LAST:event_jTextFieldInvalidityPartialFocusLost

    private void jTextFieldConcessionalPartialFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldConcessionalPartialFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getConcessionalPartial());

        BigDecimal d = null;
        BigDecimal d1 = null;

        String value = jTextFieldConcessionalPartial.getText().trim();
        String value1 = jTextFieldWithdraw.getText().trim();

        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);
        if (value1 != null && value1.length() > 0)
            d1 = Currency.getCurrencyInstance().getBigDecimalValue(value1);

        if (!etpCalc.validatePartialWithdrawComponentInput(
                ETPConstants.CONCESSIONAL_PARTIAL, d, d1))
            jTextFieldConcessionalPartial.setText(previousValue);
        else {
            jTextFieldConcessionalPartial.setText(value);
            BigDecimal withdraw = etpCalc.getConcessionalAvail(d);
            jTextFieldConcessionalAvail.setText(Currency.getCurrencyInstance()
                    .toString(withdraw));
        }
    }// GEN-LAST:event_jTextFieldConcessionalPartialFocusLost

    private void jTextFieldExcessPartialFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldExcessPartialFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getExcessPartial());

        BigDecimal d = null;
        BigDecimal d1 = null;

        String value = jTextFieldExcessPartial.getText().trim();
        String value1 = jTextFieldWithdraw.getText().trim();

        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);
        if (value1 != null && value1.length() > 0)
            d1 = Currency.getCurrencyInstance().getBigDecimalValue(value1);

        if (!etpCalc.validatePartialWithdrawComponentInput(
                ETPConstants.EXCESS_PARTIAL, d, d1))
            jTextFieldExcessPartial.setText(previousValue);
        else {
            jTextFieldExcessPartial.setText(value);
            BigDecimal withdraw = etpCalc.getExcessAvail(d);
            jTextFieldExcessAvail.setText(Currency.getCurrencyInstance()
                    .toString(withdraw));
        }
    }// GEN-LAST:event_jTextFieldExcessPartialFocusLost

    private void jTextFieldCGTPartialFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldCGTPartialFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getCGTExemptPartial());

        BigDecimal d = null;
        BigDecimal d1 = null;

        String value = jTextFieldCGTPartial.getText().trim();
        String value1 = jTextFieldWithdraw.getText().trim();

        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);
        if (value1 != null && value1.length() > 0)
            d1 = Currency.getCurrencyInstance().getBigDecimalValue(value1);

        if (!etpCalc.validatePartialWithdrawComponentInput(
                ETPConstants.CGT_EXEMPT_PARTIAL, d, d1))
            jTextFieldCGTPartial.setText(previousValue);
        else {
            jTextFieldCGTPartial.setText(value);
            BigDecimal withdraw = etpCalc.getCGTExemptAvail(d);
            jTextFieldCGTAvail.setText(Currency.getCurrencyInstance().toString(
                    withdraw));
        }
    }// GEN-LAST:event_jTextFieldCGTPartialFocusLost

    private void jTextFieldUndeductedPartialFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldUndeductedPartialFocusLost
        // Add your handling code here:
        String previousValue = Currency.getCurrencyInstance().toString(
                etpCalc.getUndeductedPartial());

        BigDecimal d = null;
        BigDecimal d1 = null;

        String value = jTextFieldUndeductedPartial.getText().trim();
        String value1 = jTextFieldWithdraw.getText().trim();

        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);
        if (value1 != null && value1.length() > 0)
            d1 = Currency.getCurrencyInstance().getBigDecimalValue(value1);

        if (!etpCalc.validatePartialWithdrawComponentInput(
                ETPConstants.UNDEDUCTED_PARTIAL, d, d1))
            jTextFieldUndeductedPartial.setText(previousValue);
        else {
            jTextFieldUndeductedPartial.setText(value);
            BigDecimal withdraw = etpCalc.getUndeductedAvail(d);
            jTextFieldUndeductedAvail.setText(Currency.getCurrencyInstance()
                    .toString(withdraw));

        }
    }// GEN-LAST:event_jTextFieldUndeductedPartialFocusLost

    private void jTextFieldWithdrawFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldWithdrawFocusLost
        // Add your handling code here:
        BigDecimal d = null;
        String value = jTextFieldWithdraw.getText().trim();
        if (value != null && value.length() > 0)
            d = Currency.getCurrencyInstance().getBigDecimalValue(value);

        if ((d == null ? 0 : d.doubleValue()) <= (etpCalc.getTotalETPAmount() == null ? 0
                : etpCalc.getTotalETPAmount().doubleValue())) {
            jTextFieldWithdraw.setText(jTextFieldWithdraw.getText().trim());
            BigDecimal rollover = etpCalc.getETPPartialWithdrawalRollover(d);
            jTextFieldRollover.setText(Currency.getCurrencyInstance().toString(
                    rollover));
        } else
            jTextFieldWithdraw.setText(Currency.getCurrencyInstance().toString(
                    etpCalc.getTotalETPAmount()));
    }// GEN-LAST:event_jTextFieldWithdrawFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        // Add your handling code here:
        // updateETPCalcValue();
        result = ETPConstants.OK_OPTION;
        SwingUtil.setVisible(this, false);
    }// GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jTextFieldInvalidityPartial;

    private javax.swing.JTextField jTextFieldInvalidityAvail;

    private javax.swing.JTextField jTextFieldConcessionalPartial;

    private javax.swing.JTextField jTextFieldCGTAvail;

    private javax.swing.JTextField jTextFieldExcessPartial;

    private javax.swing.JTextField jTextFieldTotal;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JTextField jTextFieldUndeductedPartial;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JTextField jTextFieldExcessAvail;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField jTextFieldUndeductedAvail;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField jTextFieldWithdraw;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField jTextFieldRollover;

    private javax.swing.JTextField jTextFieldCGTPartial;

    private javax.swing.JTextField jTextFieldConcessionalAvail;

    private javax.swing.JLabel jLabel10;

    // End of variables declaration//GEN-END:variables

    /*
     * public void updateETPCalcValue() { BigDecimal d = null;
     * 
     * String value = jTextFieldRollover.getText().trim(); d =
     * Currency.getCurrencyInstance().getBigDecimalValue( value ) ;
     * etpCalc.setETPPartialWithdrawalRollover( d );
     * 
     * value = jTextFieldUndeductedPartial.getText().trim(); d =
     * Currency.getCurrencyInstance().getBigDecimalValue( value ) ;
     * etpCalc.setUndeductedPartial( d );
     * 
     * value = jTextFieldCGTPartial.getText().trim(); d =
     * Currency.getCurrencyInstance().getBigDecimalValue( value ) ;
     * etpCalc.setCGTExemptPartial( d );
     * 
     * value = jTextFieldExcessPartial.getText().trim(); d =
     * Currency.getCurrencyInstance().getBigDecimalValue( value ) ;
     * etpCalc.setExcessPartial( d );
     * 
     * value = jTextFieldConcessionalPartial.getText().trim(); d =
     * Currency.getCurrencyInstance().getBigDecimalValue( value ) ;
     * etpCalc.setConcessionalPartial( d );
     * 
     * value = jTextFieldInvalidityPartial.getText().trim(); d =
     * Currency.getCurrencyInstance().getBigDecimalValue( value ) ;
     * etpCalc.setInvalidityPartial( d );
     *  }
     */
    public BigDecimal getWithdraw() {
        return Currency.getCurrencyInstance().getBigDecimalValue(
                jTextFieldWithdraw.getText());
    }

    public void setWithdraw(BigDecimal value) {
        jTextFieldWithdraw.setText(Currency.getCurrencyInstance().toString(
                value));
    }

    public BigDecimal getUndeductedPartial() {
        return Currency.getCurrencyInstance().getBigDecimalValue(
                jTextFieldUndeductedPartial.getText());
    }

    public void setUndeductedPartial(BigDecimal value) {
        jTextFieldUndeductedPartial.setText(Currency.getCurrencyInstance()
                .toString(value));
    }

    public BigDecimal getCGTPartial() {
        return Currency.getCurrencyInstance().getBigDecimalValue(
                jTextFieldCGTPartial.getText());
    }

    public void setCGTPartial(BigDecimal value) {
        jTextFieldCGTPartial.setText(Currency.getCurrencyInstance().toString(
                value));
    }

    public BigDecimal getExcessPartial() {
        return Currency.getCurrencyInstance().getBigDecimalValue(
                jTextFieldExcessPartial.getText());
    }

    public void setExcessPartial(BigDecimal value) {
        jTextFieldExcessPartial.setText(Currency.getCurrencyInstance()
                .toString(value));
    }

    public BigDecimal getConcessionalPartial() {
        return Currency.getCurrencyInstance().getBigDecimalValue(
                jTextFieldConcessionalPartial.getText());
    }

    public void setConcessionalPartial(BigDecimal value) {
        jTextFieldConcessionalPartial.setText(Currency.getCurrencyInstance()
                .toString(value));
    }

    public BigDecimal getInvalidityPartial() {
        return Currency.getCurrencyInstance().getBigDecimalValue(
                jTextFieldInvalidityPartial.getText());
    }

    public void setInvalidityPartial(BigDecimal value) {
        jTextFieldInvalidityPartial.setText(Currency.getCurrencyInstance()
                .toString(value));
    }

    public void updateComponents() {

        BigDecimal d = etpCalc.getTotalETPAmount();
        jTextFieldTotal.setText(Currency.getCurrencyInstance().toString(d));

        d = etpCalc.getETPPartialWithdrawalRollover();
        jTextFieldRollover.setText(Currency.getCurrencyInstance().toString(d));

        d = etpCalc.getUndeductedAvail();
        jTextFieldUndeductedAvail.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getCGTExemptAvail();
        jTextFieldCGTAvail.setText(Currency.getCurrencyInstance().toString(d));

        d = etpCalc.getExcessAvail();
        jTextFieldExcessAvail.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getConcessionalAvail();
        jTextFieldConcessionalAvail.setText(Currency.getCurrencyInstance()
                .toString(d));

        d = etpCalc.getInvalidityAvail();
        jTextFieldInvalidityAvail.setText(Currency.getCurrencyInstance()
                .toString(d));

    }

    public void updateEditable() {

        setWithdraw(etpCalc.getETPPartialWithdrawalWithdraw());
        setUndeductedPartial(etpCalc.getUndeductedPartial());
        setCGTPartial(etpCalc.getCGTExemptPartial());
        setExcessPartial(etpCalc.getExcessPartial());
        setConcessionalPartial(etpCalc.getConcessionalPartial());
        setInvalidityPartial(etpCalc.getInvalidityPartial());

    }

    public int getResult() {
        return result;
    }

}
