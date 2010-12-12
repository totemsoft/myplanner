/*
 * PersonEstateView.java
 *
 * Created on 30 July 2001, 16:56
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.code.AddressCode;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.code.InsolvencyRiskCode;
import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.code.StatusCode;
import com.argus.financials.code.WillChangeCode;
import com.argus.financials.code.WillStatusCode;
import com.argus.financials.service.PersonService;

public class PersonEstateView extends javax.swing.JPanel {

    private static final boolean DEBUG = true;

    private NameView executorName;

    private AddressView executorAddress;

    private ContactMediaView executorPhone;

    private ContactMediaView executorFax;

    /** Creates new form PersonEstateView */
    public PersonEstateView() {
        initComponents();
        initComponents2();
    }

    private void initComponents2() {

        executorName = new NameView(false);
        jPanelName.add(executorName);

        executorAddress = new AddressView(AddressCode.RESIDENTIAL);
        jPanelAddress.add(executorAddress);

        // javax.swing.JPanel jPanelContactMedia = new javax.swing.JPanel();
        executorPhone = new ContactMediaView(ContactMediaCode.PHONE,
                jPanelContactMedia, 0);
        executorFax = new ContactMediaView(ContactMediaCode.FAX,
                jPanelContactMedia, 1);
        // jPanelName.add( jPanelContactMedia );

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jPanelWill = new javax.swing.JPanel();
        jPanelWillDetails = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxWillStatus = new javax.swing.JComboBox(new WillStatusCode()
                .getCodeDescriptions());
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxWillChangeCode = new javax.swing.JComboBox(
                new WillChangeCode().getCodeDescriptions());
        jLabel7 = new javax.swing.JLabel();
        jComboBoxExecutorStatus = new javax.swing.JComboBox(new StatusCode()
                .getCodeDescriptions());
        jLabel8 = new javax.swing.JLabel();
        jComboBoxAttorneyStatus = new javax.swing.JComboBox(new StatusCode()
                .getCodeDescriptions());
        jTextFieldLastReview = new javax.swing.JTextField();
        jPanelEstateIssues = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxPreNuptual = new javax.swing.JComboBox(new StatusCode()
                .getCodeDescriptions());
        jLabel4 = new javax.swing.JLabel();
        jComboBoxInsolvencyRiskCode = new javax.swing.JComboBox(
                new InsolvencyRiskCode().getCodeDescriptions());
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaComment = new javax.swing.JTextArea();
        jPanelExecutor = new javax.swing.JPanel();
        jPanelName = new javax.swing.JPanel();
        jPanelContactMedia = new javax.swing.JPanel();
        jPanelAddress = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout(1, 2));

        jPanelWill.setLayout(new javax.swing.BoxLayout(jPanelWill,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelWillDetails.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        jPanelWillDetails.setBorder(new javax.swing.border.TitledBorder(
                "Will Details"));
        jPanelWillDetails.setPreferredSize(new java.awt.Dimension(282, 20));
        jPanelWillDetails.setMinimumSize(new java.awt.Dimension(283, 10));
        jLabel1.setText("Status");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        jPanelWillDetails.add(jLabel1, gridBagConstraints1);

        jComboBoxWillStatus.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxWillStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelWillDetails.add(jComboBoxWillStatus, gridBagConstraints1);

        jLabel2.setText("Last Review");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        jPanelWillDetails.add(jLabel2, gridBagConstraints1);

        jLabel6.setText("Expected Changes");
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        jPanelWillDetails.add(jLabel6, gridBagConstraints1);

        jComboBoxWillChangeCode
                .setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxWillChangeCode.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelWillDetails.add(jComboBoxWillChangeCode, gridBagConstraints1);

        jLabel7.setText("Executor Status");
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        jPanelWillDetails.add(jLabel7, gridBagConstraints1);

        jComboBoxExecutorStatus
                .setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxExecutorStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelWillDetails.add(jComboBoxExecutorStatus, gridBagConstraints1);

        jLabel8.setText("Attorney Status");
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        jPanelWillDetails.add(jLabel8, gridBagConstraints1);

        jComboBoxAttorneyStatus
                .setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxAttorneyStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelWillDetails.add(jComboBoxAttorneyStatus, gridBagConstraints1);

        jTextFieldLastReview.setPreferredSize(new java.awt.Dimension(150, 20));
        jTextFieldLastReview.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelWillDetails.add(jTextFieldLastReview, gridBagConstraints1);

        jPanelWill.add(jPanelWillDetails);

        jPanelEstateIssues.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        jPanelEstateIssues.setBorder(new javax.swing.border.TitledBorder(
                "Estate Issues"));
        jPanelEstateIssues.setPreferredSize(new java.awt.Dimension(282, 90));
        jPanelEstateIssues.setMinimumSize(new java.awt.Dimension(267, 10));
        jLabel3.setText("Pre-nuptual");
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        jPanelEstateIssues.add(jLabel3, gridBagConstraints2);

        jComboBoxPreNuptual.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxPreNuptual.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelEstateIssues.add(jComboBoxPreNuptual, gridBagConstraints2);

        jLabel4.setText("Insolvency Risk");
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        jPanelEstateIssues.add(jLabel4, gridBagConstraints2);

        jComboBoxInsolvencyRiskCode.setPreferredSize(new java.awt.Dimension(
                150, 20));
        jComboBoxInsolvencyRiskCode.setMinimumSize(new java.awt.Dimension(150,
                20));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelEstateIssues
                .add(jComboBoxInsolvencyRiskCode, gridBagConstraints2);

        jLabel5.setText("Comments");
        jLabel5.setPreferredSize(new java.awt.Dimension(90, 16));
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        jPanelEstateIssues.add(jLabel5, gridBagConstraints2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 110));
        jTextAreaComment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAreaCommentFocusLost(evt);
            }
        });

        jScrollPane1.setViewportView(jTextAreaComment);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelEstateIssues.add(jScrollPane1, gridBagConstraints2);

        jPanelWill.add(jPanelEstateIssues);

        add(jPanelWill);

        jPanelExecutor.setLayout(new javax.swing.BoxLayout(jPanelExecutor,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new javax.swing.BoxLayout(jPanelName,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setBorder(new javax.swing.border.TitledBorder(
                "Executor Name"));
        jPanelName.setPreferredSize(new java.awt.Dimension(12, 100));
        jPanelExecutor.add(jPanelName);

        jPanelContactMedia.setLayout(new javax.swing.BoxLayout(
                jPanelContactMedia, javax.swing.BoxLayout.Y_AXIS));

        jPanelContactMedia.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Executor Phone/Fax"));
        jPanelContactMedia.setPreferredSize(new java.awt.Dimension(12, 55));
        jPanelExecutor.add(jPanelContactMedia);

        jPanelAddress.setLayout(new javax.swing.BoxLayout(jPanelAddress,
                javax.swing.BoxLayout.X_AXIS));

        jPanelAddress.setBorder(new javax.swing.border.TitledBorder(
                "Executor Address"));
        jPanelAddress.setPreferredSize(new java.awt.Dimension(12, 135));
        jPanelExecutor.add(jPanelAddress);

        add(jPanelExecutor);

    }// GEN-END:initComponents

    private void jTextAreaCommentFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextAreaCommentFocusLost
        // Add your handling code here:
    }// GEN-LAST:event_jTextAreaCommentFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelWill;

    private javax.swing.JPanel jPanelWillDetails;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JComboBox jComboBoxWillStatus;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JComboBox jComboBoxWillChangeCode;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JComboBox jComboBoxExecutorStatus;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JComboBox jComboBoxAttorneyStatus;

    private javax.swing.JTextField jTextFieldLastReview;

    private javax.swing.JPanel jPanelEstateIssues;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JComboBox jComboBoxPreNuptual;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JComboBox jComboBoxInsolvencyRiskCode;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextAreaComment;

    private javax.swing.JPanel jPanelExecutor;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JPanel jPanelContactMedia;

    private javax.swing.JPanel jPanelAddress;

    // End of variables declaration//GEN-END:variables

    // private Estate estate;

    public void updateView(PersonService person) throws com.argus.financials.service.ServiceException {
        /*
         * // display estate info Estate estate = ( (ClientService) person
         * ).getEstate();
         *  // display executor info Contact executor = ( (ClientService) person
         * ).getExecutor();
         * 
         * executorName.setObject( executor == null ? null : executor.getName() );
         * executorName.updateView( null );
         * 
         * executorAddress.setObject( executor == null ? null :
         * executor.getAddress() ); executorAddress.updateView( null );
         * 
         * executorPhone.setObject( executor == null ? null :
         * executor.getPhone() ); executorPhone.updateView( null );
         * 
         * executorFax.setObject( executor == null ? null : executor.getFax() );
         * executorFax.updateView( null );
         */
    }

    public void saveView(PersonService person) throws com.argus.financials.service.ServiceException,
            InvalidCodeException {
        /*
         * // save estate info
         *  // save executor info Contact executor = ( (ClientService) person
         * ).getExecutor(); if ( executor == null ) executor = new Contact(
         * RmiParams.getInstance().getClientPersonID().intValue() );
         * 
         * executorName.saveView( null ); executor.setName( (PersonName)
         * executorName.getObject() );
         * 
         * executorAddress.saveView( person ); executor.setAddress( (Address)
         * executorAddress.getObject() );
         * 
         * executorPhone.saveView( null ); executor.setPhone( (ContactMedia)
         * executorPhone.getObject() );
         * 
         * executorFax.saveView( null ); executor.setFax( (ContactMedia)
         * executorFax.getObject() );
         * 
         * if ( executor.getPrimaryKeyID() == null ) ( (ClientService) person
         * ).setExecutor( executor );
         */
    }

    public void clearView() {
    }

    public Object getObject() {
        return null;// estate;
    }

    public void setObject(Object value) {
        // estate = (Estate) value;
    }

    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.PERSON);
    }

}
