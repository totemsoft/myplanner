/*
 * TotalAssetAllocationView.java
 *
 * Created on 10 October 2002, 11:27
 */

package au.com.totemsoft.myplanner.swing.assetallocation;

import java.awt.Color;

/**
 * This class displays fields for the total asset allocation, it displays the
 * percentage in cash, fixed interest, australian shares, international shares,
 * property, others and the total sum.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class TotalAssetAllocationView extends javax.swing.JPanel {

    /** Creates new form TotalAssetAllocationView */
    public TotalAssetAllocationView() {
        initComponents();
    }

    /** Creates new form TotalAssetAllocationView */
    public TotalAssetAllocationView(String title) {
        initComponents();

        // set border title
        setBorder(new javax.swing.border.TitledBorder(title));

        // set default header
        this.jLabelHeader.setText("   ");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelCash = new javax.swing.JLabel();
        jTextFieldCash = new javax.swing.JTextField();
        jLabelFixedInterest = new javax.swing.JLabel();
        jTextFieldFixedInterest = new javax.swing.JTextField();
        jLabelAustShares = new javax.swing.JLabel();
        jTextFieldAustShares = new javax.swing.JTextField();
        jLabelIntnlShares = new javax.swing.JLabel();
        jTextFieldIntnlShares = new javax.swing.JTextField();
        jLabelProperty = new javax.swing.JLabel();
        jTextFieldProperty = new javax.swing.JTextField();
        jLabelTotal = new javax.swing.JLabel();
        jTextFieldTotal = new javax.swing.JTextField();
        jLabelOther = new javax.swing.JLabel();
        jTextFieldOther = new javax.swing.JTextField();
        jLabelHeader = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.TitledBorder("Asset Allocation (%)"));
        jLabelCash.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelCash, gridBagConstraints);

        jTextFieldCash.setEditable(false);
        jTextFieldCash.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCash.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldCash.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jTextFieldCash, gridBagConstraints);

        jLabelFixedInterest.setText("Fixed Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelFixedInterest, gridBagConstraints);

        jTextFieldFixedInterest.setEditable(false);
        jTextFieldFixedInterest
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldFixedInterest
                .setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldFixedInterest.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jTextFieldFixedInterest, gridBagConstraints);

        jLabelAustShares.setText("Australian Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelAustShares, gridBagConstraints);

        jTextFieldAustShares.setEditable(false);
        jTextFieldAustShares
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAustShares.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldAustShares.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jTextFieldAustShares, gridBagConstraints);

        jLabelIntnlShares.setText("International Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelIntnlShares, gridBagConstraints);

        jTextFieldIntnlShares.setEditable(false);
        jTextFieldIntnlShares
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIntnlShares.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldIntnlShares.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jTextFieldIntnlShares, gridBagConstraints);

        jLabelProperty.setText("Property");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelProperty, gridBagConstraints);

        jTextFieldProperty.setEditable(false);
        jTextFieldProperty
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldProperty.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldProperty.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jTextFieldProperty, gridBagConstraints);

        jLabelTotal.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelTotal, gridBagConstraints);

        jTextFieldTotal.setEditable(false);
        jTextFieldTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotal.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldTotal.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(jTextFieldTotal, gridBagConstraints);

        jLabelOther.setText("Other");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelOther, gridBagConstraints);

        jTextFieldOther.setEditable(false);
        jTextFieldOther.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOther.setPreferredSize(new java.awt.Dimension(80, 21));
        jTextFieldOther.setMinimumSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jTextFieldOther, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabelHeader, gridBagConstraints);

    }// GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jTextFieldAustShares;

    private javax.swing.JLabel jLabelFixedInterest;

    private javax.swing.JTextField jTextFieldProperty;

    private javax.swing.JTextField jTextFieldFixedInterest;

    private javax.swing.JLabel jLabelProperty;

    private javax.swing.JLabel jLabelIntnlShares;

    private javax.swing.JLabel jLabelHeader;

    private javax.swing.JTextField jTextFieldTotal;

    private javax.swing.JTextField jTextFieldIntnlShares;

    private javax.swing.JTextField jTextFieldOther;

    private javax.swing.JLabel jLabelTotal;

    private javax.swing.JLabel jLabelOther;

    private javax.swing.JLabel jLabelAustShares;

    private javax.swing.JTextField jTextFieldCash;

    private javax.swing.JLabel jLabelCash;

    // End of variables declaration//GEN-END:variables

    // get methods for asset allocation values
    public String getTotalInCash() {
        return jTextFieldCash.getText();
    }

    public String getTotalInFixedInterest() {
        return jTextFieldFixedInterest.getText();
    }

    public String getTotalInAustShares() {
        return jTextFieldAustShares.getText();
    }

    public String getTotalInIntnlShares() {
        return jTextFieldIntnlShares.getText();
    }

    public String getTotalInProperty() {
        return jTextFieldProperty.getText();
    }

    public String getTotalInOther() {
        return jTextFieldOther.getText();
    }

    public String getTotalTotal() {
        return jTextFieldTotal.getText();
    }

    // set methods for asset allocation values
    public void setTotalInCash(String value) {
        jTextFieldCash.setText(value);
    }

    public void setTotalInFixedInterest(String value) {
        jTextFieldFixedInterest.setText(value);
    }

    public void setTotalInAustShares(String value) {
        jTextFieldAustShares.setText(value);
    }

    public void setTotalInIntnlShares(String value) {
        jTextFieldIntnlShares.setText(value);
    }

    public void setTotalInProperty(String value) {
        jTextFieldProperty.setText(value);
    }

    public void setTotalInOther(String value) {
        jTextFieldOther.setText(value);
    }

    public void setTotalTotal(String value) {
        jTextFieldTotal.setText(value);

        try {
            double help = Double.parseDouble(deletePercentAndCommaSign(value));

            if (help < 100.0) {
                // display it in green
                jTextFieldTotal.setForeground(new Color(0, 160, 0));
            } else if (help > 100.0) {
                // display it in red
                jTextFieldTotal.setForeground(Color.red);
            } else {
                // display it in black
                jTextFieldTotal.setForeground(Color.black);
            }
        } catch (java.lang.NumberFormatException e) {
        } catch (java.lang.NullPointerException e) {
        }
    }

    /**
     * Deletes the '%' and ',' from a given String.
     * 
     * @param str -
     *            a String
     * @param the
     *            given String without any '%' char
     */
    protected String deletePercentAndCommaSign(String str) {
        String help = "";
        char c;

        if (str != null && str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);
                if (c != '%' && c != ',') {
                    help += c;
                }
            }
        }

        return (help.length() == 0) ? "0.0" : help;
    }

    public void setHeader(String value) {
        this.jLabelHeader.setText(value);
    }
}