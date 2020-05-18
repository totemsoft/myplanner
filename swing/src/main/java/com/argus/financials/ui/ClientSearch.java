/*
 * ClientSearch.java
 *
 * Created on August 30, 2001, 11:46 AM
 */

package com.argus.financials.ui;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.Cursor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.bean.DbConstant;
import com.argus.financials.api.bean.IAddress;
import com.argus.financials.api.bean.IClient;
import com.argus.financials.api.bean.IClientView;
import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IUser;
import com.argus.financials.api.bean.PersonName;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.code.AdviserTypeCode;
import com.argus.financials.code.Advisers;
import com.argus.financials.code.CountryCode;
import com.argus.financials.code.StateCode;
import com.argus.financials.config.FPSLocale;
import com.argus.financials.domain.hibernate.ClientView;
import com.argus.financials.etc.Contact;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.swing.table.SortedTableModel;
import com.argus.swing.SwingUtils;
import com.argus.util.DateTimeUtils;

public final class ClientSearch extends AbstractPanel {

    public static final int CANCEL_OPTION = javax.swing.JOptionPane.CANCEL_OPTION;

    public static final int OK_OPTION = javax.swing.JOptionPane.OK_OPTION;

    // default/current countryID
    private Integer countryCodeID = null;

    private static ClientSearch view;

    private int result; // CANCEL_OPTION, OK_OPTION

    /** Creates new form ClientSearch */
    private ClientSearch() {
        initComponents();

        countryCodeID = new CountryCode().getCodeID(FPSLocale.getInstance()
                .getDisplayCountry());
        jComboBoxCountry.setSelectedItem(new CountryCode()
                .getCodeDescription(countryCodeID));

        jComboBoxAdviser.setModel(new javax.swing.DefaultComboBoxModel(
                (Contact[]) new Advisers().getCodes().toArray(new Contact[0])));

        jCheckBoxDisplayAll.setVisible(false);

        // jPanelDetails.setVisible(false);
        // jScrollPaneClientDetails.setVisible(false);

        jTable.getSelectionModel().addListSelectionListener(
            new javax.swing.event.ListSelectionListener()
            {
                public void valueChanged(javax.swing.event.ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting())
                    {
                        ClientView c = getSelectedPerson();
                        jTextPaneClientDetails.setText(c == null ? "" : c.getDetails());
                    }
                }
            }
        );
    }

    public static boolean exists() {
        return view != null;
    }

    public static ClientSearch getInstance() {
        if (view == null)
            view = new ClientSearch();
        return view;
    }

    public String getViewCaption() {
        return "ClientView Search";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelCriteria = new javax.swing.JPanel();
        jCheckBoxDisplayAll = new javax.swing.JCheckBox();
        jComboBoxFamilyName = new javax.swing.JTextField();
        jTextFieldDOB = new com.argus.bean.FDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxFirstName = new javax.swing.JTextField();
        jComboBoxState = new javax.swing.JComboBox();
        jComboBoxCountry = new javax.swing.JComboBox(new CountryCode().getCodeDescriptions());
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxAllUsersClients = new javax.swing.JCheckBox();
        jButtonSearch = new javax.swing.JButton();
        jLabelAdviser = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldPostCode = new javax.swing.JTextField();
        jComboBoxAdviser = new javax.swing.JComboBox();
        jPanelDetails = new javax.swing.JPanel();
        jSplitPaneDetails = new javax.swing.JSplitPane();
        jScrollPaneTable = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        SortedTableModel sortedTableModel = new SortedTableModel(getModel());
        jTable.setModel(sortedTableModel);

        sortedTableModel.addMouseListenerToHeaderInTable(jTable);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        jScrollPaneClientDetails = new javax.swing.JScrollPane();
        jTextPaneClientDetails = new javax.swing.JTextPane();
        jPanelControls = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(400, 550));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanelCriteria.setLayout(new java.awt.GridBagLayout());

        jPanelCriteria.setBorder(new javax.swing.border.TitledBorder(null,
                "Search Criteria",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Arial", 0, 11)));
        jCheckBoxDisplayAll.setText("Display All");
        jCheckBoxDisplayAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxDisplayAllItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        jPanelCriteria.add(jCheckBoxDisplayAll, gridBagConstraints);

        jComboBoxFamilyName.setPreferredSize(new java.awt.Dimension(100, 21));
        jComboBoxFamilyName.setMinimumSize(new java.awt.Dimension(100, 21));
        jComboBoxFamilyName
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBoxFamilyNameActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelCriteria.add(jComboBoxFamilyName, gridBagConstraints);

        jTextFieldDOB.setInputVerifier(new DateInputVerifier());
        jTextFieldDOB.setPreferredSize(new java.awt.Dimension(100, 20));
        jTextFieldDOB.setMinimumSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        jPanelCriteria.add(jTextFieldDOB, gridBagConstraints);

        jLabel1.setText("Family Name");
        jLabel1.setPreferredSize(new java.awt.Dimension(75, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelCriteria.add(jLabel1, gridBagConstraints);

        jLabel2.setText("First Name");
        jLabel2.setPreferredSize(new java.awt.Dimension(75, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelCriteria.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Date of Birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelCriteria.add(jLabel3, gridBagConstraints);

        jLabel6.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelCriteria.add(jLabel6, gridBagConstraints);

        jLabel4.setText("Country");
        jLabel4.setPreferredSize(new java.awt.Dimension(75, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelCriteria.add(jLabel4, gridBagConstraints);

        jComboBoxFirstName.setPreferredSize(new java.awt.Dimension(100, 21));
        jComboBoxFirstName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelCriteria.add(jComboBoxFirstName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        jPanelCriteria.add(jComboBoxState, gridBagConstraints);

        jComboBoxCountry.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxCountryItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelCriteria.add(jComboBoxCountry, gridBagConstraints);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0,
                5));

        jCheckBoxAllUsersClients.setText("All Adviser's Clients");
        jCheckBoxAllUsersClients.setPreferredSize(new java.awt.Dimension(200,
                25));
        jPanel2.add(jCheckBoxAllUsersClients);

        jButtonSearch.setText("Search");
        jButtonSearch.setDefaultCapable(false);
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jPanel2.add(jButtonSearch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelCriteria.add(jPanel2, gridBagConstraints);

        jLabelAdviser.setText("Adviser");
        jLabelAdviser.setPreferredSize(new java.awt.Dimension(75, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelCriteria.add(jLabelAdviser, gridBagConstraints);

        jLabel7.setText("Post Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelCriteria.add(jLabel7, gridBagConstraints);

        jTextFieldPostCode
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPostCode.setPreferredSize(new java.awt.Dimension(70, 20));
        jTextFieldPostCode.setMinimumSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        jPanelCriteria.add(jTextFieldPostCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        jPanelCriteria.add(jComboBoxAdviser, gridBagConstraints);

        add(jPanelCriteria);

        jPanelDetails.setLayout(new javax.swing.BoxLayout(jPanelDetails,
                javax.swing.BoxLayout.Y_AXIS));

        jSplitPaneDetails.setDividerLocation(200);
        jSplitPaneDetails.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneDetails.setAlignmentX(0.5F);
        jTable.setModel(sortedTableModel);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });

        jScrollPaneTable.setViewportView(jTable);

        jSplitPaneDetails.setLeftComponent(jScrollPaneTable);

        jScrollPaneClientDetails
                .setPreferredSize(new java.awt.Dimension(9, 150));
        jScrollPaneClientDetails.setMinimumSize(new java.awt.Dimension(9, 150));
        jTextPaneClientDetails.setEditable(false);
        jScrollPaneClientDetails.setViewportView(jTextPaneClientDetails);

        jSplitPaneDetails.setRightComponent(jScrollPaneClientDetails);

        jPanelDetails.add(jSplitPaneDetails);

        jButtonOK.setText("Open");
        jButtonOK.setDefaultCapable(false);
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonOK);

        jButtonCancel.setText("Cancel");
        jButtonCancel.setDefaultCapable(false);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonCancel);

        jButtonRemove.setText("Remove");
        jButtonRemove.setDefaultCapable(false);
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonRemove);

        jPanelDetails.add(jPanelControls);

        add(jPanelDetails);

    }// GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentShown
        result = CANCEL_OPTION;
    }// GEN-LAST:event_formComponentShown

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCancelActionPerformed
        SwingUtil.setVisible(view, false);
        result = CANCEL_OPTION; // by default
    }// GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOKActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doOpen(evt);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonOKActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTableMouseClicked
        if (evt.getClickCount() == 2) {
            jButtonOKActionPerformed(null);
        }
    }// GEN-LAST:event_jTableMouseClicked

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRemoveActionPerformed
        int selectedRow = jTable.getSelectedRow();

        if (selectedRow < 0)
            return;

        Integer clientId = getSelectedPersonID();

        if (JOptionPane.showConfirmDialog(this,
                "You are about to remove the client " + getClientName()
                        + ", do you want to proceed ?"
                        + "\nNote: You can remove ONLY Your Own Clients!",
                "Remove ClientView", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
            return;

        TableModel tm = jTable.getModel();
        ((SortedTableModel) tm).removeRow(selectedRow);
        try {
            IClient client = userService.findClient(clientId.longValue());
            userService.remove(client);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        ((SortedTableModel) jTable.getModel()).sortByColumn(COLUMN_CLIENT);
        jTable.repaint();
    }// GEN-LAST:event_jButtonRemoveActionPerformed

    private void jComboBoxFamilyNameActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBoxFamilyNameActionPerformed
        // Add your handling code here:
    }// GEN-LAST:event_jComboBoxFamilyNameActionPerformed

    private void jCheckBoxDisplayAllItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxDisplayAllItemStateChanged
        boolean en = !jCheckBoxDisplayAll.isSelected();

        SwingUtil.setEnabled(jComboBoxFamilyName, en);
        SwingUtil.setEnabled(jComboBoxFirstName, en);
        SwingUtil.setEnabled(jTextFieldDOB, en);
        SwingUtil.setEnabled(jComboBoxState, en);
        SwingUtil.setEnabled(jComboBoxCountry, en);
    }// GEN-LAST:event_jCheckBoxDisplayAllItemStateChanged

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSearchActionPerformed

        // init table
        try {
            updateView();
            jPanelDetails.setVisible(true);
            jScrollPaneClientDetails.setVisible(true);
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
            jPanelDetails.setVisible(false);
            jScrollPaneClientDetails.setVisible(false);
        }

    }// GEN-LAST:event_jButtonSearchActionPerformed

    private void jComboBoxCountryItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxCountryItemStateChanged
        String s = (String) jComboBoxCountry.getSelectedItem();
        countryCodeID = new CountryCode().getCodeID(s);

        // load state codes for this country
        jComboBoxState.setModel(new javax.swing.DefaultComboBoxModel(
                new StateCode(countryCodeID).getCodeDescriptions()));
    }// GEN-LAST:event_jComboBoxCountryItemStateChanged

    public static ClientSearch display(java.awt.Frame owner)
    {
        //boolean exists = exists();
        ClientSearch view = getInstance();
        try
        {
            IUser user = userPreferences.getUser();
            Integer userTypeId = user == null ? null : user.getTypeId();
            view.setUserType(userTypeId);
            SwingUtil.add2Dialog(owner, view.getViewCaption(), true, view, true, true);
            return view;
        }
        catch (com.argus.financials.api.ServiceException e)
        {
            e.printStackTrace(System.err);
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxDisplayAll;

    private javax.swing.JButton jButtonCancel;

    private javax.swing.JTextField jComboBoxFirstName;

    private javax.swing.JComboBox jComboBoxAdviser;

    private javax.swing.JScrollPane jScrollPaneClientDetails;

    private javax.swing.JTextField jComboBoxFamilyName;

    private javax.swing.JTextField jTextFieldPostCode;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JSplitPane jSplitPaneDetails;

    private javax.swing.JCheckBox jCheckBoxAllUsersClients;

    private javax.swing.JTable jTable;

    private javax.swing.JButton jButtonOK;

    private javax.swing.JPanel jPanelDetails;

    private javax.swing.JLabel jLabelAdviser;

    private javax.swing.JTextPane jTextPaneClientDetails;

    private com.argus.bean.FDateChooser jTextFieldDOB;

    private javax.swing.JComboBox jComboBoxState;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JComboBox jComboBoxCountry;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JButton jButtonSearch;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanelCriteria;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPaneTable;

    private javax.swing.JButton jButtonRemove;

    // End of variables declaration//GEN-END:variables

    protected void doOpen(java.awt.event.ActionEvent evt) throws Exception {
        setNewClient(getSelectedPersonID());
        SwingUtil.setVisible(view, false);
        result = OK_OPTION;
    }

    public int getResult() {
        return result;
    }

    /***************************************************************************
     * helper methods
     **************************************************************************/
    private boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    private void setNewClient(Integer newClientID) throws Exception {
        Integer oldClientID = clientService.getId();
        if (equals(oldClientID, newClientID))
            return;
        // close all visible forms (visible = false)
        SwingUtil.closeAll();
        clientService.findByPrimaryKey(newClientID);
    }

    private TableModel tableModel;

    private static final int COLUMN_CLIENT = 0;

    private static final int COLUMN_ADVISER = 1;

    private static final int COLUMN_COUNT = 2;

    private Object[] columnNames = new String[] { "ClientView", "Adviser" };

    private DefaultTableModel getModel() {

        // if ( tableModel == null ) {

        DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {},
                columnNames) {
            Class[] types = new Class[] { java.lang.String.class,
                    java.lang.String.class };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

        };

        JTableHeader th = new JTableHeader(jTable.getColumnModel());
        jTable.setTableHeader(th);
        th.setFont(SwingUtils.getDefaultFont());
        // }

        return tableModel;

    }

    private void initTable() {

        Object[][] rowData = getRowData();

        TableModel tm = jTable.getModel();

        tm = ((SortedTableModel) tm).getModel();

        if (tm instanceof DefaultTableModel) {
            if (rowData == null)
                ((DefaultTableModel) tm).setRowCount(0);
            else
                ((DefaultTableModel) tm).setDataVector(rowData, columnNames);
        } else {
            System.err.println("Unhandled TableModel: " + tm);
            return;
        }

        // support person
        if (jCheckBoxAllUsersClients.isVisible()) {
            setColumnWidth(jTable, COLUMN_CLIENT, 1000, 200, 300);
            setColumnWidth(jTable, COLUMN_ADVISER, 1000, 100, 200);
        } else {
            setColumnWidth(jTable, COLUMN_ADVISER, 0, 0, 0);
        }

        ((SortedTableModel) jTable.getModel()).sortByColumn(COLUMN_CLIENT);
    }

    private void setColumnWidth(JTable table, int columnID, int maxWidth,
            int minWidth, int preferredWidth) {
        TableColumn column = table.getColumnModel().getColumn(columnID);
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    private Map<String, Object> getSelectionCriteria() throws com.argus.financials.api.ServiceException {

        Map<String, Object> criteria = new HashMap<String, Object>();
        if (jCheckBoxDisplayAll.isSelected())
            return criteria;

        IUser user = userPreferences.getUser();
        Integer userTypeId = user == null ? null : user.getTypeId();
        boolean supportPerson = AdviserTypeCode.isSupportPerson(userTypeId);
        if (!supportPerson)
        {
            criteria.put(DbConstant.ADVISORID, user.getId());
        }
        else if (jCheckBoxAllUsersClients.isSelected())
        {
            criteria.put(DbConstant.ALL_USERS_CLIENTS, Boolean.TRUE);
        }
        else
        {
            Object obj = jComboBoxAdviser.getSelectedItem();
            Contact cs = (Contact) obj;
            PersonName name = cs == null || cs == Advisers.NONE ? null : cs.getName();
            if (name == null || name.getSurname().trim().length() == 0)
            {
                criteria.put(DbConstant.ADVISORID, user.getId());
            }
            else
            {
                Integer adviserId = cs.getId();
                if (adviserId != null)
                    criteria.put(DbConstant.ADVISORID, adviserId);
            }
        }

        // String s = (String) jComboBoxFamilyName.getSelectedItem();
        // //.getText();
        String s = (String) jComboBoxFamilyName.getText();
        if (s != null && s.length() > 0)
            criteria.put(IPerson.SURNAME, s);

        // s = (String) jComboBoxFirstName.getSelectedItem(); //.getText();
        s = (String) jComboBoxFirstName.getText();
        if (s != null && s.length() > 0)
            criteria.put(IPerson.FIRST_NAME, s);

        s = jTextFieldDOB.getText();
        if (s != null && s.length() > 0) {
            s = DateTimeUtils.getJdbcDate(s);
            criteria.put(IPerson.DATE_OF_BIRTH, s);
        }

        s = (String) jComboBoxCountry.getSelectedItem();
        Integer countryCodeID = new CountryCode().getCodeID(s);
        if (countryCodeID != null
                && !countryCodeID.equals(ICountry.AUSTRALIA_ID))
            criteria.put(IAddress.COUNTRY, countryCodeID);

        s = (String) jTextFieldPostCode.getText();
        s = s.trim();
        if (s != null && s.length() > 0) {
            criteria.put(IAddress.POSTCODE, s);
        }

        s = (String) jComboBoxState.getSelectedItem();
        Integer stateCodeID = new StateCode(countryCodeID).getCodeID(s);
        if (stateCodeID != null)
            criteria.put(IAddress.STATE, stateCodeID);

        return criteria;

    }

    private Object[][] getRowData() {
        try {
            List<? extends IClientView> data = userService.findClients(getSelectionCriteria(), 0, -1);
            if (data == null)
                return new Object[0][COLUMN_COUNT];
            Object[][] rowData = new Object[data.size()][COLUMN_COUNT];
            // "Name", "Adviser"
            for (int i = 0; i < data.size(); i++) {
                IClientView c = data.get(i);
                rowData[i][COLUMN_CLIENT] = c;
                rowData[i][COLUMN_ADVISER] = c.getOwnerShortName();
            }
            return rowData;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null; // throw e;
        }
    }

    private ClientView getSelectedPerson() {
        // no selection
        if (jTable.getSelectedRow() < 0)
            return null;
        return (ClientView) jTable.getModel().getValueAt(jTable.getSelectedRow(),
                COLUMN_CLIENT);
    }

    public Integer getSelectedPersonID() {
        ClientView c = getSelectedPerson();
        return c == null ? null : c.getId().intValue();
    }

    public String getClientName() {
        ClientView c = getSelectedPerson();
        return c == null ? null : c.getShortName();
    }

    public void setUserType(Integer userTypeCodeID) {
        boolean supportPerson = AdviserTypeCode.isSupportPerson(userTypeCodeID);

        jLabelAdviser.setVisible(supportPerson);
        jComboBoxAdviser.setVisible(supportPerson);
        jCheckBoxAllUsersClients.setVisible(supportPerson);

        if (!supportPerson) // reset checkbox
            jCheckBoxAllUsersClients.setSelected(false);
    }

    public void updateView() throws com.argus.financials.api.ServiceException 
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            initTable();
        } finally {
            setCursor(null);
        }
    }

    public void saveView(PersonService person) throws com.argus.financials.api.ServiceException,
            InvalidCodeException {
    }

    public void clearView() {
    }

    public Object getObject() {
        return null;
    }

    public void setObject(Object value) {

    }

    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.CLIENT_PERSON);
    }

}
