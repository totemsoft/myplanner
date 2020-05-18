/*
 * ResponsePanel.java
 *
 * Created on 1 April 2003, 12:38
 */

package com.argus.financials.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class ResponsePanel extends javax.swing.JDialog {

    private static ResponsePanel dialog;

    private static String value = "";

    private static Dimension listOfValuesDim;

    /** Creates new form ResponsePanel */
    private ResponsePanel(java.awt.Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        // super(parent, modal);
        initComponents();
        jLabelImage.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/javax/swing/plaf/metal/icons/Error.gif")));
        listOfValuesDim = jScrollPane1.getPreferredSize();
        jButtonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResponsePanel.value = jButtonCancel.getText();
                ResponsePanel.dialog.setVisible(false);
            }
        });
        jButtonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResponsePanel.value = jButtonOk.getText();
                ResponsePanel.dialog.setVisible(false);
            }
        });
        jButtonNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResponsePanel.value = jButtonNo.getText();
                ResponsePanel.dialog.setVisible(false);
            }
        });

        // com.argus.swing.SwingUtils.setDefaultColor(this);
        // com.argus.swing.SwingUtils.setDefaultFont(this);

        jTextAreaMessage.setBackground(jPanelButtons.getBackground());
    }

    public static ResponsePanel initialize(Component comp, boolean modal,
            String message, ArrayList possibleValues, String OkButton,
            String NoButton, String title) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        if (dialog == null)
            dialog = new ResponsePanel(frame, title, modal);

        jButtonCancel.setVisible(false);

        jTextAreaMessage.setText(message);
        if (possibleValues.size() <= 0) {
            jScrollPane1.setPreferredSize(new Dimension(0, 0));
            jScrollPane1.setVisible(false);

        } else {
            jScrollPane1.setVisible(true);
            jScrollPane1.setPreferredSize(listOfValuesDim);
            jListOfItems.setListData(possibleValues.toArray());
        }
        if (OkButton == null || OkButton.trim().equals(""))
            jButtonOk.setVisible(false);
        else {
            jButtonOk.setText(OkButton);
            jButtonOk.setVisible(true);

        }
        if (NoButton == null || NoButton.trim().equals(""))
            jButtonNo.setVisible(false);
        else {
            jButtonNo.setText(NoButton);
            jButtonNo.setVisible(true);
        }
        dialog.setTitle(title);
        dialog.setModal(modal);

        return dialog;
    }

    public static ResponsePanel initialize(Component comp, boolean modal,
            String message, ArrayList possibleValues, String OkButton,
            String NoButton, String CancelButton, String title) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        if (dialog == null)
            dialog = new ResponsePanel(frame, title, modal);

        jTextAreaMessage.setText(message);
        if (possibleValues.size() <= 0) {
            jScrollPane1.setPreferredSize(new Dimension(0, 0));
            jScrollPane1.setVisible(false);

        } else {
            jScrollPane1.setVisible(true);
            jScrollPane1.setPreferredSize(listOfValuesDim);
            jListOfItems.setListData(possibleValues.toArray());
        }

        if (OkButton == null || OkButton.trim().equals(""))
            jButtonOk.setVisible(false);
        else {
            jButtonOk.setText(OkButton);
            jButtonOk.setVisible(true);
        }
        if (NoButton == null || CancelButton.trim().equals(""))
            jButtonNo.setVisible(false);
        else {
            jButtonNo.setText(NoButton);
            jButtonNo.setVisible(true);
        }
        if (CancelButton == null || CancelButton.trim().equals(""))
            jButtonCancel.setVisible(false);
        else {
            jButtonCancel.setText(CancelButton);
            jButtonCancel.setVisible(true);
        }

        dialog.setTitle(title);
        dialog.setModal(modal);

        return dialog;
    }

    public static String showDialog(Component comp) {
        if (dialog != null) {
            dialog.pack();
            dialog.setLocationRelativeTo(comp);
            dialog.setVisible(true);
        } else {
            System.err.println("ListDialog requires you to call initialize "
                    + "before calling showDialog.");
        }
        return value;
    }

    private void initComponents() {
        jPanelButtons = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonNo = new javax.swing.JButton();

        jPanelFields = new javax.swing.JPanel();
        jPanelMessage = new javax.swing.JPanel();
        jLabelImage = new javax.swing.JLabel();
        jTextAreaMessage = new javax.swing.JTextArea();
        jPanelContent = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListOfItems = new javax.swing.JList();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jButtonOk.setText("Ok");
        jPanelButtons.add(jButtonOk);

        jButtonNo.setText("No");
        jPanelButtons.add(jButtonNo);

        jButtonCancel.setText("Cancel");
        jPanelButtons.add(jButtonCancel);

        getContentPane().add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        jPanelFields.setLayout(new java.awt.BorderLayout());

        jPanelMessage.setLayout(new java.awt.BorderLayout(10, 10));

        jLabelImage.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        jPanelMessage.add(jLabelImage, java.awt.BorderLayout.WEST);

        jTextAreaMessage.setWrapStyleWord(true);
        jTextAreaMessage.setLineWrap(true);
        jTextAreaMessage.setBackground(new java.awt.Color(204, 204, 204));
        jTextAreaMessage.setDisabledTextColor(java.awt.Color.black);
        jTextAreaMessage.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jTextAreaMessage.setEnabled(false);
        jPanelMessage.add(jTextAreaMessage, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelMessage, java.awt.BorderLayout.NORTH);

        jPanelContent.setLayout(new java.awt.BorderLayout(0, 10));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 131));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(400, 22));
        jListOfItems.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jListOfItems);

        jPanelContent.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelContent, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelFields, java.awt.BorderLayout.CENTER);

        pack();
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//
    private javax.swing.JPanel jPanelButtons;

    private static javax.swing.JButton jButtonOk;

    private static javax.swing.JButton jButtonCancel;

    private static javax.swing.JButton jButtonNo;

    private javax.swing.JPanel jPanelFields;

    private javax.swing.JPanel jPanelMessage;

    private static javax.swing.JLabel jLabelImage;

    private static javax.swing.JTextArea jTextAreaMessage;

    private javax.swing.JPanel jPanelContent;

    private static javax.swing.JScrollPane jScrollPane1;

    private static javax.swing.JList jListOfItems;
    // End of variables declaration//

}
