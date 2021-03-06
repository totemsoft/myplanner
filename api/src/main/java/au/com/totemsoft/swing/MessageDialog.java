/*
 * MessageDialog.java
 *
 * Created on 14 March 2003, 07:46
 */

/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.swing;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class MessageDialog extends javax.swing.JDialog {
    
    public static final int CANCEL_OPTION = javax.swing.JOptionPane.CANCEL_OPTION;
    public static final int OK_OPTION     = javax.swing.JOptionPane.OK_OPTION;
    
    
    private int result; // CANCEL_OPTION, OK_OPTION

    
    /** Creates new form MessageDialog */
    public MessageDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton( jButtonOK );
    }
    public MessageDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton( jButtonOK );
    }
    public MessageDialog() {
        super();
        initComponents();
        getRootPane().setDefaultButton( jButtonOK );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPaneMessage = new javax.swing.JScrollPane();
        jLabelMessage = new javax.swing.JLabel();
        jPanelSouth = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabelMessage.setText("jLabel1");
        jLabelMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPaneMessage.setViewportView(jLabelMessage);

        getContentPane().add(jScrollPaneMessage, java.awt.BorderLayout.CENTER);

        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanelSouth.add(jButtonOK);

        jButtonCancel.setText("Cancel");
        jButtonCancel.setDefaultCapable(false);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanelSouth.add(jButtonCancel);

        getContentPane().add(jPanelSouth, java.awt.BorderLayout.SOUTH);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(400, 300));
        setLocation((screenSize.width-400)/2,(screenSize.height-300)/2);
    }//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        result = CANCEL_OPTION;
    }//GEN-LAST:event_formComponentShown

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        doButtonCancelActionPerformed(evt);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        doButtonOKActionPerformed(evt);
    }//GEN-LAST:event_jButtonOKActionPerformed
    
    protected void doButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        result = CANCEL_OPTION;
    }

    protected void doButtonOKActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        result = OK_OPTION;
    }

    /** Closes the dialog */    
    /**
     * @param args the command line arguments
     */
    static void main(String args[]) {
        final MessageDialog dlg = new MessageDialog(new javax.swing.JFrame(), true);
        dlg.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlg.dispose();
                System.exit(0);
            }
        });
        dlg.show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton jButtonCancel;
    private javax.swing.JScrollPane jScrollPaneMessage;
    protected javax.swing.JButton jButtonOK;
    private javax.swing.JPanel jPanelSouth;
    private javax.swing.JLabel jLabelMessage;
    // End of variables declaration//GEN-END:variables
    
    public int getResult() {
        return result;
    }

    public void setMessage( String value ) {
        //if ( value.toLowerCase().indexOf( "<html>" ) != 0 )
        //    value = "<html>" + value + "</html>";
        jLabelMessage.setText( value );
    }

    public void setHTMLMessage( String value ) {
        if ( value.toLowerCase().indexOf( "<html>" ) != 0 )
            value = "<html>" + value + "</html>";
        setMessage( value );
    }
    
}
