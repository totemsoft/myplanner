/*
 * ContactMedia.java
 *
 * Created on 6 September 2001, 11:45
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.PhoneNumberInputVerifier;
import com.argus.format.LimitedPlainDocument;

public class ContactMediaView extends java.awt.Container {

    /** Creates new form PhoneFaxView */
    public ContactMediaView(Integer contactMediaCode,
            javax.swing.JPanel container, int row) {
        initComponents(container, row);

        setContactMediaCodeID(contactMediaCode);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents(javax.swing.JPanel container, int row) {
        jLabel = new javax.swing.JLabel();
        // jTextFieldValue1 = new javax.swing.JTextField();
        jTextFieldValue2 = new javax.swing.JTextField();
        // jLabelAt = new javax.swing.JLabel();
        if (!(container.getLayout() instanceof java.awt.GridBagLayout))
            container.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        jLabel.setPreferredSize(new java.awt.Dimension(60, 16));
        jLabel.setMinimumSize(new java.awt.Dimension(60, 16));
        jLabel.setText("jLabel");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = row;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 5);
        container.add(jLabel, gridBagConstraints1);

        // gridBagConstraints1 = new java.awt.GridBagConstraints();
        // gridBagConstraints1.gridx = 1;
        // gridBagConstraints1.gridy = row;
        // gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        // container.add(jTextFieldValue1, gridBagConstraints1);

        // jLabelAt.setPreferredSize(new java.awt.Dimension(12, 16));
        // jLabelAt.setMinimumSize(new java.awt.Dimension(12, 16));
        // jLabelAt.setText("@");

        // gridBagConstraints1 = new java.awt.GridBagConstraints();
        // gridBagConstraints1.gridx = 2;
        // gridBagConstraints1.gridy = row;
        // gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // container.add(jLabelAt, gridBagConstraints1);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = row;
        gridBagConstraints1.gridwidth = 0;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 10);
        container.add(jTextFieldValue2, gridBagConstraints1);

    }

    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel;

    // private javax.swing.JTextField jTextFieldValue1;
    private javax.swing.JTextField jTextFieldValue2;

    // private javax.swing.JLabel jLabelAt;
    // End of variables declaration

    // my variables
    private Integer contactMediaCodeID;

    private ContactMedia contactMedia;

    /**
     * 
     */
    public void setDescription(String desc) {
        jLabel.setText(desc);
    }

    public Integer getContactMediaCodeID() {
        return contactMediaCodeID;
    }

    private void setContactMediaCodeID(Integer value) {

        setDescription(new ContactMediaCode().getCodeDescription(value));
        contactMediaCodeID = value;

        if ((ContactMediaCode.EMAIL.equals(value))
                || (ContactMediaCode.EMAIL_WORK.equals(value))) {

            // jTextFieldValue1.setToolTipText("login name");
            // jTextFieldValue1.setPreferredSize(new java.awt.Dimension(85,
            // 20));
            // jTextFieldValue1.setMinimumSize(new java.awt.Dimension(65, 20));
            // jTextFieldValue1.setDocument( new LimitedPlainDocument(16) );
            // jTextFieldValue1.setInputVerifier( null );

            // jTextFieldValue2.setToolTipText("service provider");
            jTextFieldValue2.setDocument(new LimitedPlainDocument(50));
            jTextFieldValue2.setPreferredSize(new java.awt.Dimension(200, 20));
            jTextFieldValue2.setMinimumSize(new java.awt.Dimension(110, 20));
            jTextFieldValue2.setInputVerifier(null);

            // jLabelAt.setText( "@" );
            // jLabelAt.setVisible( true );
        } else {

            // jTextFieldValue1.setToolTipText("area code");
            // jTextFieldValue1.setPreferredSize(new java.awt.Dimension(30,
            // 20));
            // jTextFieldValue1.setMinimumSize(new java.awt.Dimension(30, 20));
            // jTextFieldValue1.setDocument( new LimitedPlainDocument(4) );
            // jTextFieldValue1.setInputVerifier(
            // IntegerInputVerifier.getInstance() );

            jLabel.setPreferredSize(new java.awt.Dimension(70, 16));
            jTextFieldValue2.setPreferredSize(new java.awt.Dimension(100, 20));
            jTextFieldValue2.setMinimumSize(new java.awt.Dimension(50, 20));
            jTextFieldValue2.setInputVerifier(PhoneNumberInputVerifier
                    .getInstance());

            // jLabelAt.setText( " " ); // 4 spaces
            // jLabelAt.setVisible( false );
        }

    }

    /**
     * 
     */
    private void updateView() throws com.argus.financials.service.ServiceException {

        if (contactMedia == null) {
            clearView();
            return;
        }

        // jTextFieldValue1.setText( contactMedia.getValue1() );
        jTextFieldValue2.setText(contactMedia.getValue2());
    }

    private void saveView() throws com.argus.financials.service.ServiceException {

        if (contactMedia == null)
            return;

        contactMedia.setContactMediaCodeID(contactMediaCodeID);

        // String s = jTextFieldValue1.getText();
        // if ( s != null && s.length() == 0 ) s = null;
        // contactMedia.setValue1( s );

        String s = jTextFieldValue2.getText();
        if (s != null && s.length() == 0)
            s = null;
        contactMedia.setValue2(s);
    }

    /**
     * 
     */
    public void updateView(PersonService person) throws com.argus.financials.service.ServiceException {

        // updateView( person.getContactMedia(contactMediaCodeID) );
        if (contactMedia == null)
            clearView();
        else
            updateView();
    }

    public void saveView(PersonService person) throws com.argus.financials.service.ServiceException {

        // save anyway
        if (contactMedia == null) {
            contactMedia = new ContactMedia();
            contactMedia.setContactMediaCodeID(contactMediaCodeID);
        }

        saveView();
        // person.setContactMedia( contactMedia );
    }

    public void clearView() {
        // SwingUtils.clear(this);
        // jTextFieldValue1.setText(null);
        jTextFieldValue2.setText(null);
    }

    public Object getObject() {
        return contactMedia;
    }

    public void setObject(Object value) {
        contactMedia = (ContactMedia) value;
        try {
            updateView();
        } catch (com.argus.financials.service.ServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.CONTACT_MEDIA);
    }

}
