/*
 * PersonContactsView.java
 *
 * Created on 22 August 2001, 13:32
 */

package au.com.totemsoft.myplanner.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * 
 * 
 * 
 * 
 * 
 * @version
 */

import java.awt.Component;
import java.awt.ItemSelectable;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.config.FPSLocale;
import au.com.totemsoft.myplanner.etc.ActionEventID;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.swing.SwingUtil;
import au.com.totemsoft.swing.SwingUtils;

public abstract class TableEditView extends javax.swing.JPanel implements
        ActionEventID {

    /**
     * Map( objectID, object ), column names (init by derived class)
     */
    protected Map details; // order by objectID (HashMap/TreeMap ???)

    protected String[] COLUMN_NAMES;

    protected int COLUMN_OBJECT_ID = 0;

    // counter to insert new objects (negative id)
    protected static int newObjectID = 0;

    /** Creates new form PersonContactsView */
    public TableEditView() {
        initComponents();
        SwingUtils.setDefaultFont(jPopupMenuAR);

        setActionMap();

        _addMouseListener(jScrollPane);
        _addMouseListener(jTable);
        _addListSelectionListener(jTable);

    }

    protected void _addMouseListener(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    boolean en = isSelected();
                    jMenuItemRemove.setEnabled(en);
                    jPopupMenuAR.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    protected void _addListSelectionListener(final JTable table) {
        table.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {

                            int row = table.getSelectedRow();
                            if (row == -1)
                                return;

                            detailsEnabled(false); // has to be first
                            fireActionEvent(DATA_UPDATE);
                        }
                    }
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenuAR = new javax.swing.JPopupMenu();
        jMenuItemAdd = new javax.swing.JMenuItem();
        jMenuItemRemove = new javax.swing.JMenuItem();
        jMenuItemUpdate = new javax.swing.JMenuItem();
        jScrollPane = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jPanelDetails = new javax.swing.JPanel();

        jMenuItemAdd.setText("Add");
        jMenuItemAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddActionPerformed(evt);
            }
        });

        jPopupMenuAR.add(jMenuItemAdd);
        jMenuItemRemove.setText("Remove");
        jMenuItemRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRemoveActionPerformed(evt);
            }
        });

        jPopupMenuAR.add(jMenuItemRemove);
        jMenuItemUpdate.setText("Modify");
        jMenuItemUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUpdateActionPerformed(evt);
            }
        });

        jPopupMenuAR.add(jMenuItemUpdate);

        setLayout(new java.awt.BorderLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }

            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });

        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });

        jScrollPane.setViewportView(jTable);

        add(jScrollPane, java.awt.BorderLayout.CENTER);

        tpgControls.setTitle("");
        tpgControls.setSpecial(true);
        tpgControls.add(new AbstractAction((String) IMenuCommand.ADD.getSecond()) {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        tpgControls.add(new AbstractAction((String) IMenuCommand.REMOVE.getSecond()) {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });
        tpgControls.add(new AbstractAction((String) IMenuCommand.UPDATE.getSecond()) {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });
        tpgControls.setExpanded(true);
        jPanelControls.add(tpgControls);       
        add(jPanelControls, java.awt.BorderLayout.EAST);

        jPanelDetails.addContainerListener(new ContainerAdapter() {
            public void componentAdded(ContainerEvent evt) {
                jPanelDetailsComponentAdded(evt);
            }
        });

        add(jPanelDetails, java.awt.BorderLayout.SOUTH);

    }// GEN-END:initComponents

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddActionPerformed
        jMenuItemAddActionPerformed(null);
    }// GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonUpdateActionPerformed
        jMenuItemUpdateActionPerformed(null);
    }// GEN-LAST:event_jButtonUpdateActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTableMouseClicked
        if (evt.getClickCount() == 2) {
            detailsEnabled(true); // has to be first
            fireActionEvent(DATA_UPDATE);
        }
    }// GEN-LAST:event_jTableMouseClicked

    private void jMenuItemUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemUpdateActionPerformed
        detailsEnabled(true); // has to be first
        fireActionEvent(DATA_UPDATE);
    }// GEN-LAST:event_jMenuItemUpdateActionPerformed

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentHidden
        SwingUtil.setActionMap(this, (ActionMap) null);
    }// GEN-LAST:event_formComponentHidden

    private void formComponentShown(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentShown
        SwingUtil.setActionMap(this, getActionMap());
    }// GEN-LAST:event_formComponentShown

    private void jMenuItemRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemRemoveActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Do you really want to delete this item?", "Remove Item",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
            return;

        fireActionEvent(DATA_REMOVE);
    }// GEN-LAST:event_jMenuItemRemoveActionPerformed

    private void jMenuItemAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemAddActionPerformed
        fireActionEvent(DATA_ADD);
    }// GEN-LAST:event_jMenuItemAddActionPerformed

    private void jPanelDetailsComponentAdded(java.awt.event.ContainerEvent evt) {// GEN-FIRST:event_jPanelDetailsComponentAdded
        if (evt.getChild() instanceof JComponent)
            addEventListener((JComponent) evt.getChild());
    }// GEN-LAST:event_jPanelDetailsComponentAdded

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRemoveActionPerformed
        jMenuItemRemoveActionPerformed(null);
    }// GEN-LAST:event_jButtonRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItemAdd;
    private javax.swing.JPopupMenu jPopupMenuAR;
    private javax.swing.JButton jButtonAdd;
    protected JTaskPane jPanelControls = new JTaskPane();
    protected JTaskPaneGroup tpgControls = new JTaskPaneGroup();
    private javax.swing.JScrollPane jScrollPane;
    protected javax.swing.JTable jTable;
    protected javax.swing.JPanel jPanelDetails;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JMenuItem jMenuItemRemove;
    private javax.swing.JMenuItem jMenuItemUpdate;
    // End of variables declaration//GEN-END:variables

    private void addEventListener(JComponent jcomponent) {

        // break conditions
        if (jcomponent instanceof JComboBox) {
            ((JComboBox) jcomponent)
                    .addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(
                                java.awt.event.ItemEvent evt) {
                            JComboBox cb = (JComboBox) evt.getSource();
                            if (cb.getUI().isPopupVisible(cb))
                                update();
                        }
                    });

        } else if (jcomponent instanceof ItemSelectable) { // e.g. JComboBox
            ((ItemSelectable) jcomponent)
                    .addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(
                                java.awt.event.ItemEvent evt) {
                            JComponent c = (JComponent) evt.getSource();
                            if (c.hasFocus())
                                update();
                        }
                    });

        } else if (jcomponent instanceof JTextComponent) {
            jcomponent.addKeyListener(new java.awt.event.KeyAdapter() { // KeyListener
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                            Object source = evt.getSource();
                            if (source instanceof Component
                                    && ((Component) source).hasFocus())
                                ;// update();
                        }
                    });

        } else if (jcomponent instanceof au.com.totemsoft.bean.FDateChooser) {
            addEventListener(((au.com.totemsoft.bean.FDateChooser) jcomponent)
                    .getDateField());

        } else {

            // set for all children
            for (int i = 0; i < jcomponent.getComponentCount(); i++)
                if (jcomponent.getComponent(i) instanceof JComponent)
                    addEventListener((JComponent) jcomponent.getComponent(i));

        }

    }

    /**
     * init table data using details Map
     */
    protected abstract Object[][] getRowData();

    public void initTable() {

        Object[][] rowData = getRowData();
        if (rowData == null) {

            // return;
        }

        // javax.swing.table.DefaultTableModel
        ((DefaultTableModel) (jTable.getModel())).setDataVector(rowData,
                COLUMN_NAMES);

        TableColumn column = jTable.getColumnModel().getColumn(
                COLUMN_OBJECT_ID);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
    }

    /**
     * Viewable interface
     */
    public Integer getObjectType() {
        return null;
    }

    public void updateView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {
        initTable();
    }

    public void saveView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException,
            InvalidCodeException {
        // ( ( ClientService) person ).setContacts( details );
    }

    public void clearView() {
        // clear all controls (text,combobox,.. except table)
        SwingUtil.clear(this);
        // clear table selection
        // jTable.clearSelection();
    }

    public Object getObject() {
        return details;
    }

    public void setObject(Object value) {
        details = (Map) value;
    }

    /**
     * 
     */
    protected void remove() {
        remove(getSelectedObjectID());
    }

    protected abstract void remove(Integer objID);

    /*
     * protected void add() { clearView(); }
     */
    protected abstract void add();

    // update/insert data from details panel to table
    protected void update() {
        update(getSelectedObject());
    }

    protected abstract void update(Object obj);

    // display selected table data id details panel
    protected void display() {
        try {
            display(getSelectedObject());
        } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    protected abstract void display(Object obj) throws au.com.totemsoft.myplanner.api.ServiceException;

    /**
     * Helper methodes
     */
    public boolean isSelected() {

        int selectedRowID = jTable.getSelectedRow();
        if (selectedRowID == -1 || details == null)
            return false;

        return true;

    }

    public Integer getSelectedObjectID() {
        int selectedRowID = jTable.getSelectedRow();
        if (selectedRowID == -1 || details == null)
            return null;

        return (Integer) jTable.getValueAt(selectedRowID, COLUMN_OBJECT_ID);
    }

    public Object getSelectedObject() {
        Integer selectedObjectID = getSelectedObjectID();
        return (selectedObjectID == null) ? null : details
                .get(selectedObjectID);
    }

    protected void detailsEnabled(boolean value) {
        SwingUtil.setEnabled(jPanelDetails, value);
    }

    /**
     * 
     */
    protected void setActionMap() {

        ActionMap am = this.getActionMap();
        if (am == null) {
            am = new ActionMap();
            this.setActionMap(am);
        }

        am.put(DATA_ADD, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // if ( jTable.getSelectedRow() != -1 )
                // update(); // current object
                add();
            }
        });
        am.put(DATA_REMOVE, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (jTable.getSelectedRow() != -1)
                    remove();
                else
                    clearView();
            }
        });
        am.put(DATA_UPDATE, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (jTable.getSelectedRow() != -1) {
                    display();
                }
            }
        });

    }

    private static int eventID;

    private void fireActionEvent(Integer actionID) {

        javax.swing.ActionMap actionMap = getActionMap();
        if (actionMap == null)
            return;

        javax.swing.Action action = (javax.swing.Action) (actionMap
                .get(actionID));
        if (action == null)
            return;

        action.actionPerformed(new java.awt.event.ActionEvent(this, ++eventID,
                this.getClass().getName()));

    }

}
