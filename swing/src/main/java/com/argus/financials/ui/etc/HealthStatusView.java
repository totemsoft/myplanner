/*
 * GoalsObjectives.java
 *
 * Created on 8 October 2001, 16:33
 */

package com.argus.financials.ui.etc;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.code.BooleanCode;

public class HealthStatusView extends javax.swing.JPanel {

    /** Creates new form GoalsObjectives */
    public HealthStatusView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        buttonGroupPersonalHealthProblem = new javax.swing.ButtonGroup();
        buttonGroupFamilyHealthProblem = new javax.swing.ButtonGroup();
        jPanel17 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxHealthState = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox(new BooleanCode().getCodes()
                .toArray());
        jPanel10 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
                10, 5));

        jPanel17.setBorder(new javax.swing.border.TitledBorder(
                "Personal Health Status"));
        jLabel1.setText("How would you describe your state of health?");
        jPanel17.add(jLabel1);

        jComboBoxHealthState.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel17.add(jComboBoxHealthState);

        jLabel4.setText("Smoker");
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setPreferredSize(new java.awt.Dimension(130, 16));
        jPanel17.add(jLabel4);

        jComboBox1.setPreferredSize(new java.awt.Dimension(50, 20));
        jComboBox1.setMinimumSize(new java.awt.Dimension(50, 20));
        jPanel17.add(jComboBox1);

        add(jPanel17);

        jPanel10.setLayout(new java.awt.GridLayout(1, 2));

        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel15.setBorder(new javax.swing.border.TitledBorder(
                "Personal health problems"));
        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT,
                10, 5));

        jLabel5.setText("Any history of personal health problems?");
        jPanel19.add(jLabel5);

        jRadioButton3.setText("Yes");
        buttonGroupPersonalHealthProblem.add(jRadioButton3);
        jPanel19.add(jRadioButton3);

        jRadioButton4.setText("No");
        buttonGroupPersonalHealthProblem.add(jRadioButton4);
        jPanel19.add(jRadioButton4);

        jPanel15.add(jPanel19);

        jScrollPane4.setViewportView(jTable1);

        jPanel15.add(jScrollPane4);

        jButton5.setText("Add");
        jButton5.setPreferredSize(new java.awt.Dimension(57, 21));
        jPanel26.add(jButton5);

        jButton6.setText("Remove");
        jButton6.setPreferredSize(new java.awt.Dimension(81, 21));
        jPanel26.add(jButton6);

        jPanel15.add(jPanel26);

        jPanel10.add(jPanel15);

        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel16.setBorder(new javax.swing.border.TitledBorder(
                "Family health problems"));
        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT,
                10, 5));

        jLabel2.setText("Any history of family health problems?");
        jPanel18.add(jLabel2);

        jRadioButton1.setText("Yes");
        buttonGroupFamilyHealthProblem.add(jRadioButton1);
        jPanel18.add(jRadioButton1);

        jRadioButton2.setText("No");
        buttonGroupFamilyHealthProblem.add(jRadioButton2);
        jPanel18.add(jRadioButton2);

        jPanel16.add(jPanel18);

        jScrollPane5.setViewportView(jTable2);

        jPanel16.add(jScrollPane5);

        jButton7.setText("Add");
        jButton7.setPreferredSize(new java.awt.Dimension(57, 21));
        jPanel27.add(jButton7);

        jButton8.setText("Remove");
        jButton8.setPreferredSize(new java.awt.Dimension(81, 21));
        jPanel27.add(jButton8);

        jPanel16.add(jPanel27);

        jPanel10.add(jPanel16);

        add(jPanel10);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7,
                javax.swing.BoxLayout.X_AXIS));

        jPanel7.setBorder(new javax.swing.border.TitledBorder("Comments"));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(380, 80));
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel7.add(jScrollPane1);

        add(jPanel7);

    }// GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupPersonalHealthProblem;

    private javax.swing.ButtonGroup buttonGroupFamilyHealthProblem;

    private javax.swing.JPanel jPanel17;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JComboBox jComboBoxHealthState;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel15;

    private javax.swing.JPanel jPanel19;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JRadioButton jRadioButton3;

    private javax.swing.JRadioButton jRadioButton4;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JTable jTable1;

    private javax.swing.JPanel jPanel26;

    private javax.swing.JButton jButton5;

    private javax.swing.JButton jButton6;

    private javax.swing.JPanel jPanel16;

    private javax.swing.JPanel jPanel18;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JTable jTable2;

    private javax.swing.JPanel jPanel27;

    private javax.swing.JButton jButton7;

    private javax.swing.JButton jButton8;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    // End of variables declaration//GEN-END:variables

    public Integer getObjectType() {
        return null;
    }

    public void updateView(com.argus.financials.service.PersonService person)
            throws com.argus.financials.service.ServiceException {
    }

    public void saveView(com.argus.financials.service.PersonService person)
            throws com.argus.financials.service.ServiceException,
            com.argus.financials.code.InvalidCodeException {
    }

    public void clearView() {
    }

    public Object getObject() {
        return null;
    }

    public void setObject(Object value) {
    }

}
