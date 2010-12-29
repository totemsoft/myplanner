/*
 * NameView.java
 *
 * Created on 1 August 2001, 17:20
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.code.TitleCode;
import com.argus.financials.etc.PersonName;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.LimitedPlainDocument;

public class NameView extends javax.swing.JPanel {

    /** Creates new form NameView */
    public NameView(boolean fullView) {
        initComponents();
        initComponents2();
        jTextFieldFamilyName.setDocument(new LimitedPlainDocument(24));
        jTextFieldFirstName.setDocument(new LimitedPlainDocument(24));
        jTextFieldOtherGivenNames.setDocument(new LimitedPlainDocument(24));

        setFullView(fullView);

    }

    private void initComponents2() {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        buttonGroup = new javax.swing.ButtonGroup();
        jLabelTitle = new javax.swing.JLabel();
        jComboBoxTitle = new javax.swing.JComboBox(new TitleCode()
                .getCodeDescriptions());
        jLabelFamilyName = new javax.swing.JLabel();
        jTextFieldFamilyName = new javax.swing.JTextField();
        jLabelFirstName = new javax.swing.JLabel();
        jTextFieldFirstName = new javax.swing.JTextField();
        jLabelOtherGivenNames = new javax.swing.JLabel();
        jTextFieldOtherGivenNames = new javax.swing.JTextField();
        jLabelSex = new javax.swing.JLabel();
        jRadioButtonSexMale = new javax.swing.JRadioButton();
        jRadioButtonSexFemale = new javax.swing.JRadioButton();
        jLabelMaritalStatus = new javax.swing.JLabel();
        jComboBoxMaritalStatus = new javax.swing.JComboBox(new MaritalCode()
                .getCodeDescriptions());

        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        setName("");
        jLabelTitle.setText("Title");
        jLabelTitle.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabelTitle.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabelTitle.setMaximumSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabelTitle, gridBagConstraints1);

        jComboBoxTitle.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        add(jComboBoxTitle, gridBagConstraints1);

        jLabelFamilyName.setText("Family Name");
        jLabelFamilyName.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabelFamilyName.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabelFamilyName.setMaximumSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabelFamilyName, gridBagConstraints1);

        jTextFieldFamilyName.setPreferredSize(new java.awt.Dimension(150, 20));
        jTextFieldFamilyName.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints1.weightx = 1.0;
        add(jTextFieldFamilyName, gridBagConstraints1);

        jLabelFirstName.setText("First Name");
        jLabelFirstName.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabelFirstName.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabelFirstName.setMaximumSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabelFirstName, gridBagConstraints1);

        jTextFieldFirstName.setPreferredSize(new java.awt.Dimension(150, 20));
        jTextFieldFirstName.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints1.weightx = 1.0;
        add(jTextFieldFirstName, gridBagConstraints1);

        jLabelOtherGivenNames.setText("Other Given Names");
        jLabelOtherGivenNames.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabelOtherGivenNames.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabelOtherGivenNames.setMaximumSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabelOtherGivenNames, gridBagConstraints1);

        jTextFieldOtherGivenNames.setPreferredSize(new java.awt.Dimension(150,
                20));
        jTextFieldOtherGivenNames
                .setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints1.weightx = 1.0;
        add(jTextFieldOtherGivenNames, gridBagConstraints1);

        jLabelSex.setText("Sex");
        jLabelSex.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabelSex.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabelSex.setMaximumSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabelSex, gridBagConstraints1);

        jRadioButtonSexMale.setText("Male");
        buttonGroup.add(jRadioButtonSexMale);
        jRadioButtonSexMale.setPreferredSize(new java.awt.Dimension(59, 20));
        jRadioButtonSexMale.setMinimumSize(new java.awt.Dimension(59, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        add(jRadioButtonSexMale, gridBagConstraints1);

        jRadioButtonSexFemale.setText("Female");
        buttonGroup.add(jRadioButtonSexFemale);
        jRadioButtonSexFemale.setPreferredSize(new java.awt.Dimension(59, 20));
        jRadioButtonSexFemale.setMinimumSize(new java.awt.Dimension(59, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        add(jRadioButtonSexFemale, gridBagConstraints1);

        jLabelMaritalStatus.setText("Marital Status");
        jLabelMaritalStatus.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabelMaritalStatus.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabelMaritalStatus.setMaximumSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabelMaritalStatus, gridBagConstraints1);

        jComboBoxMaritalStatus
                .setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints1.weightx = 1.0;
        add(jComboBoxMaritalStatus, gridBagConstraints1);

    }// GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;

    private javax.swing.JLabel jLabelTitle;

    private javax.swing.JComboBox jComboBoxTitle;

    private javax.swing.JLabel jLabelFamilyName;

    private javax.swing.JTextField jTextFieldFamilyName;

    private javax.swing.JLabel jLabelFirstName;

    private javax.swing.JTextField jTextFieldFirstName;

    private javax.swing.JLabel jLabelOtherGivenNames;

    private javax.swing.JTextField jTextFieldOtherGivenNames;

    private javax.swing.JLabel jLabelSex;

    private javax.swing.JRadioButton jRadioButtonSexMale;

    private javax.swing.JRadioButton jRadioButtonSexFemale;

    private javax.swing.JLabel jLabelMaritalStatus;

    private javax.swing.JComboBox jComboBoxMaritalStatus;

    // End of variables declaration//GEN-END:variables

    private PersonName personName;

    private void setFullView(boolean value) {
        jLabelOtherGivenNames.setVisible(value);
        jTextFieldOtherGivenNames.setVisible(value);
        jLabelMaritalStatus.setVisible(value);
        jComboBoxMaritalStatus.setVisible(value);
    }

    private void updateView() throws com.argus.financials.service.ServiceException {

        if (personName == null) {
            clearView();
            return;
        }

        Integer id = personName.getTitleCodeID();
        jComboBoxTitle.setSelectedItem(new TitleCode().getCodeDescription(id));

        jTextFieldFamilyName.setText(personName.getSurname());
        jTextFieldFirstName.setText(personName.getFirstName());
        jTextFieldOtherGivenNames.setText(personName.getOtherGivenNames());

        id = personName.getSexCodeID();
        jRadioButtonSexMale.setSelected((id != null)
                && (id.equals(SexCode.MALE)));
        jRadioButtonSexFemale.setSelected((id != null)
                && (id.equals(SexCode.FEMALE)));

        id = personName.getMaritalCodeID();
        jComboBoxMaritalStatus.setSelectedItem(new MaritalCode()
                .getCodeDescription(id));

    }

    private void saveView() throws com.argus.financials.service.ServiceException {

        if (personName == null)
            personName = new PersonName();

        String s = (String) jComboBoxTitle.getSelectedItem();
        personName.setTitleCodeID(new TitleCode().getCodeID(s));

        s = jTextFieldFamilyName.getText();
        if (s != null && s.length() == 0)
            s = null;
        personName.setSurname(s);

        s = jTextFieldFirstName.getText();
        if (s != null && s.length() == 0)
            s = null;
        personName.setFirstName(s);

        s = jTextFieldOtherGivenNames.getText();
        if (s != null && s.length() == 0)
            s = null;
        personName.setOtherGivenNames(s);

        Integer id = null;
        if (jRadioButtonSexMale.isSelected())
            id = SexCode.MALE;
        else if (jRadioButtonSexFemale.isSelected())
            id = SexCode.FEMALE;
        personName.setSexCodeID(id);

        s = (String) jComboBoxMaritalStatus.getSelectedItem();
        personName.setMaritalCodeID(new MaritalCode().getCodeID(s));

    }

    /**
     * 
     */
    public void updateView(PersonService person) throws com.argus.financials.service.ServiceException {
        updateView();
    }

    public void saveView(PersonService person) throws com.argus.financials.service.ServiceException {
        saveView();
    }

    public void clearView() {
        SwingUtil.clear(this);

        buttonGroup.setSelected(null, true);

    }

    public Object getObject() {
        return personName;
    }

    public void setObject(Object value) {
        personName = (PersonName) value;

        try {
            updateView();
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.PERSON);
    }

}
