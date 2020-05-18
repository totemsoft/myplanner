/*
 * PlanWriterTemplate.java
 *
 * Created on 20 February 2002, 11:31
 */

package com.argus.financials.ui.plan;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.DbConstant;
import com.argus.financials.api.bean.IUser;
import com.argus.financials.code.AdviserTypeCode;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.swing.table.JTreeTable;
import com.argus.financials.ui.AbstractPanel;
import com.argus.financials.ui.CheckBoxList;
import com.argus.io.IOUtils;
import com.argus.swing.SwingUtils;
import com.argus.swing.table.TreeTableModel;
import com.argus.util.ReferenceCode;

public class PlanWriterTemplate
// extends client.view.BaseView
    extends AbstractPanel
    implements
        javax.swing.event.TreeSelectionListener,
        javax.swing.event.ListSelectionListener, java.awt.event.MouseListener {

    private static PlanWriterTemplate view;

    private java.util.Vector templatePlans;

    // Model for the JTreeTable.
    protected PlanWriterTemplateModel model;

    // Used to represent the model.
    protected JTreeTable treeTable;

    private CheckBoxList destList;

    private javax.swing.JFileChooser jFileChooser;

    // Row the is being reloaded.
    protected int reloadRow = -1;

    // TreePath being reloaded.
    protected TreePath reloadPath;

    // A counter increment as the Timer fies and the same path is being
    // reloaded.
    protected int reloadCounter;

    // Timer used to update reload state.
    protected Timer timer;

    // True if new document selected/deselected (plan changed)
    private boolean modified = true;

    // flag, define how to do updates (every single document or all documents at
    // once)
    private boolean batchUpdate = true;

    /**
     * 
     */
    private boolean added2frame = false;

    private ListDataListener destListDataListener;

    /**
     * 
     */
    public static boolean exists() {
        return view != null;
    }

    public String getDefaultTitle() {
        return "Template Editor ";
    }

    /** Creates new form PlanWriterTemplate */
    private PlanWriterTemplate() {
        initComponents();
        initComponents2();

        SwingUtils.setDefaultFont(this);

        setModified(false);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        updateControls();
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting())
            return;
        updateControls();
    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        updateControls();
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }

    private void updateControls() {

        if (model == null || model.getPlan() == null)
            return;

        int destSize = destList.getModel().getSize();

        jMenuItemSavePlan.setEnabled(destSize > 0);
        jButtonSavePlan.setEnabled(jMenuItemSavePlan.isEnabled());
        jButtonSave.setEnabled(jMenuItemSavePlan.isEnabled());

        jMenuItemDeletePlan.setEnabled(model.getPlan().getId() > 0); // already
                                                                            // saved
                                                                            // (can
                                                                            // be
                                                                            // deleted)
        jButtonDeletePlan.setEnabled(jMenuItemDeletePlan.isEnabled());
        jButtonDelete.setEnabled(jMenuItemDeletePlan.isEnabled());

        jButtonAddItems.setEnabled(model.getSelectedChildrenCount() > 0);
        jButtonRemoveItems.setEnabled(destList.getSelectedCount() > 0);

        PlanWriterTemplateModel.FileNode node = getSelectedNode();
        jCheckBoxSelectAllSource.setSelected(node != null && node.isSelected());
        jCheckBoxSelectAllSource.setEnabled(node != null
                && node.getChildCount() > 0);

        // jCheckBoxSelectAllDestination.setSelected( );
        jCheckBoxSelectAllDestination.setEnabled(destSize > 0);
        jButtonUp.setEnabled(destSize > 0);
        jButtonDown.setEnabled(destSize > 0);

    }

    /**
     * Set a PlanWriterTemplateModel with the root being <code>rootPath</code>.
     * This does not load it, you should invoke <code>reloadChildren</code>
     * with the root to start loading.
     */
    public void setRootPath(String rootPath) {

        jScrollPaneTree.setViewportView(null);
        model.setRootPath(null);

        if (rootPath == null) {
            setModified(false);

        } else {
            try {
                model.setRootPath(rootPath);
                setTreeTableModel(model);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        reload(model.getRoot());
                        setModified(true);
                    }
                });

            } finally {
                jScrollPaneTree.setViewportView(treeTable);
            }

        }

    }

    /**
     * Creates and returns the instanceof JTreeTable that will be used. This
     * also creates, but does not start, the Timer that is used to update the
     * display as files are loaded.
     */
    private void setTreeTableModel(TreeTableModel treeTableModel) {

        treeTable.setModel(treeTableModel);
        /*
         * if ( treeTable.getColumnCount() >= PlanWriterTemplateModel.SIZE ) {
         * treeTable.getColumnModel().getColumn( PlanWriterTemplateModel.SIZE
         * ).setCellRenderer( new IndicatorRenderer() ); }
         */
        if (treeTable.getColumnCount() >= PlanWriterTemplateModel.SELECTED) {

            javax.swing.JCheckBox cb = new javax.swing.JCheckBox();
            cb.setHorizontalAlignment(SwingConstants.CENTER);
            javax.swing.DefaultCellEditor ce = new javax.swing.DefaultCellEditor(
                    cb);

            treeTable.getColumnModel().getColumn(
                    PlanWriterTemplateModel.SELECTED).setCellEditor(ce);

        }

        //
        setColumnWidth(PlanWriterTemplateModel.NAME, 1000, 100, 300);
        setColumnWidth(PlanWriterTemplateModel.SELECTED, 50, 10, 50);
        // setColumnWidth( PlanWriterTemplateModel.SIZE, 0, 0, 0 );
        // setColumnWidth( PlanWriterTemplateModel.TYPE, 0, 0, 0 );
        // setColumnWidth( PlanWriterTemplateModel.MODIFIED, 0, 0, 0 );

    }

    public void setColumnWidth(int columnID, int maxWidth, int minWidth,
            int preferredWidth) {
        TableColumn column = treeTable.getColumnModel().getColumn(columnID);
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    private void initComponents2() {

        destList = new CheckBoxList();
        destList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // .MULTIPLE_INTERVAL_SELECTION
                                                                        // );
        destList.addListSelectionListener(this);
        destList.addMouseListener(this);
        destList.addMouseListener(jPopupMenuUpDown);
        destList.setDragAndDropEnabled(true);

        destListDataListener = new ListDataListener() {
            public void intervalAdded(ListDataEvent evt) {
                doListChanged(evt);
            }

            public void intervalRemoved(ListDataEvent evt) {
                doListChanged(evt);
            }

            public void contentsChanged(ListDataEvent evt) {
                doListChanged(evt);
            }

            private void doListChanged(ListDataEvent evt) {
                updateControls(); // plan documents changed
            }
        };
        destList.getModel().addListDataListener(destListDataListener);

        jScrollPaneDestination.setViewportView(destList);

        jScrollPaneTree.setPreferredSize(getPreferredSize());

        treeTable = new JTreeTable();
        treeTable.getTree().addTreeSelectionListener(this);

        model = new PlanWriterTemplateModel();

        jFileChooser = new javax.swing.JFileChooser();
        File dir = WordSettings.getInstance().getPlanTemplate();
        jFileChooser.setCurrentDirectory(dir.getParentFile());
        jFileChooser.setSelectedFile(dir);

        // jFileChooser.setFileFilter( new com.argus.io.RTFFileFilter() );
        jFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ; // do what ???
            }
        });
        SwingUtils.setDefaultFont(jFileChooser);

        // treeTable.getTree().addTreeExpansionListener( model.new
        // TreeExpansionListener_impl() );
        // treeTable.getTree().addTreeWillExpandListener( model.new
        // TreeWillExpandListener_impl() );

        Reloader rl = new Reloader();
        timer = new Timer(700, rl);
        timer.setRepeats(true);
        treeTable.getTree().addTreeExpansionListener(rl);

        // set selection mode
        treeTable.getTree().getSelectionModel().setSelectionMode(
                javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);

        boolean adminPerson = false;
        boolean supportPerson = false;
        try {
            IUser user = userPreferences.getUser();
            Integer userTypeId = user == null ? null : user.getTypeId();
            adminPerson = AdviserTypeCode.isAdminPerson(userTypeId);
            supportPerson = AdviserTypeCode.isSupportPerson(userTypeId);
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }
        // jButtonSaveAsTemplate.setVisible( adminPerson || supportPerson );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpenTemplateDirectory = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemSavePlan = new javax.swing.JMenuItem();
        jMenuItemDeletePlan = new javax.swing.JMenuItem();
        jPopupMenuUpDown = new javax.swing.JPopupMenu();
        jMenuItemUp = new javax.swing.JMenuItem();
        jMenuItemDown = new javax.swing.JMenuItem();
        jPanelToolBar = new javax.swing.JPanel();
        jToolBarFile = new javax.swing.JToolBar();
        jButtonOpenTemplateDirectory = new javax.swing.JButton();
        jToolBarTemplate = new javax.swing.JToolBar();
        jComboBoxPlanTemplate = new javax.swing.JComboBox();
        jToolBarControls = new javax.swing.JToolBar();
        jButtonSavePlan = new javax.swing.JButton();
        jButtonDeletePlan = new javax.swing.JButton();
        jSplitPane = new javax.swing.JSplitPane();
        jPanelLeft = new javax.swing.JPanel();
        jScrollPaneTree = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxSelectAllSource = new javax.swing.JCheckBox();
        jPanelRight = new javax.swing.JPanel();
        jPanelAddRemove = new javax.swing.JPanel();
        jButtonAddItems = new javax.swing.JButton();
        jButtonRemoveItems = new javax.swing.JButton();
        jPanelDestination = new javax.swing.JPanel();
        jScrollPaneDestination = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jCheckBoxSelectAllDestination = new javax.swing.JCheckBox();
        jButtonUp = new javax.swing.JButton();
        jButtonDown = new javax.swing.JButton();
        jPanelControls = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();

        jMenuFile.setText("File");
        jMenuItemOpenTemplateDirectory.setText("Open Template Directory");
        jMenuItemOpenTemplateDirectory.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/image/Home16.gif")));
        jMenuItemOpenTemplateDirectory
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemOpenTemplateDirectoryActionPerformed(evt);
                    }
                });

        jMenuFile.add(jMenuItemOpenTemplateDirectory);
        jMenuFile.add(jSeparator1);
        jMenuItemSavePlan.setText("Save Plan Template");
        jMenuItemSavePlan.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/SaveAs16.gif")));
        jMenuItemSavePlan
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemSavePlanActionPerformed(evt);
                    }
                });

        jMenuFile.add(jMenuItemSavePlan);
        jMenuItemDeletePlan.setText("Delete Plan Template");
        jMenuItemDeletePlan.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/Delete16.gif")));
        jMenuItemDeletePlan
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemDeletePlanActionPerformed(evt);
                    }
                });

        jMenuFile.add(jMenuItemDeletePlan);
        jMenuBar.add(jMenuFile);
        jMenuItemUp.setToolTipText("Move Up");
        jMenuItemUp.setText("Move Up");
        jMenuItemUp.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Up16.gif")));
        jMenuItemUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUpActionPerformed(evt);
            }
        });

        jPopupMenuUpDown.add(jMenuItemUp);
        jMenuItemDown.setToolTipText("Move Down");
        jMenuItemDown.setText("Move Down");
        jMenuItemDown.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Down16.gif")));
        jMenuItemDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDownActionPerformed(evt);
            }
        });

        jPopupMenuUpDown.add(jMenuItemDown);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(750, 460));
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jPanelToolBar.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT));

        jPanelToolBar.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.RAISED));
        jButtonOpenTemplateDirectory.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/image/Home24.gif")));
        jButtonOpenTemplateDirectory.setToolTipText("Open Template Directory");
        jButtonOpenTemplateDirectory
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonOpenTemplateDirectoryActionPerformed(evt);
                    }
                });

        jToolBarFile.add(jButtonOpenTemplateDirectory);

        jPanelToolBar.add(jToolBarFile);

        jComboBoxPlanTemplate
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxPlanTemplateItemStateChanged(evt);
                    }
                });

        jToolBarTemplate.add(jComboBoxPlanTemplate);

        jPanelToolBar.add(jToolBarTemplate);

        jButtonSavePlan.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/SaveAs24.gif")));
        jButtonSavePlan.setToolTipText("Save/Save As..");
        jButtonSavePlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSavePlanActionPerformed(evt);
            }
        });

        jToolBarControls.add(jButtonSavePlan);

        jButtonDeletePlan.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/Delete24.gif")));
        jButtonDeletePlan.setToolTipText("Delete");
        jButtonDeletePlan
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonDeletePlanActionPerformed(evt);
                    }
                });

        jToolBarControls.add(jButtonDeletePlan);

        jPanelToolBar.add(jToolBarControls);

        add(jPanelToolBar);

        jSplitPane.setDividerLocation(300);
        jSplitPane.setAlignmentX(0.5F);
        jPanelLeft.setLayout(new javax.swing.BoxLayout(jPanelLeft,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelLeft.setBorder(new javax.swing.border.TitledBorder("Source"));
        jScrollPaneTree.setPreferredSize(new java.awt.Dimension(10, 1000));
        jPanelLeft.add(jScrollPaneTree);

        jCheckBoxSelectAllSource.setToolTipText("Select all under this node");
        jCheckBoxSelectAllSource.setText("Select All");
        jCheckBoxSelectAllSource.setAlignmentX(0.5F);
        jCheckBoxSelectAllSource
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBoxSelectAllSourceActionPerformed(evt);
                    }
                });

        jPanel2.add(jCheckBoxSelectAllSource);

        jPanelLeft.add(jPanel2);

        jSplitPane.setLeftComponent(jPanelLeft);

        jPanelRight.setLayout(new javax.swing.BoxLayout(jPanelRight,
                javax.swing.BoxLayout.X_AXIS));

        jPanelAddRemove.setLayout(new javax.swing.BoxLayout(jPanelAddRemove,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelAddRemove.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        jButtonAddItems.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/ForwardThick16.gif")));
        jButtonAddItems.setToolTipText("Add");
        jButtonAddItems.setDefaultCapable(false);
        jButtonAddItems.setEnabled(false);
        jButtonAddItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddItemsActionPerformed(evt);
            }
        });

        jPanelAddRemove.add(jButtonAddItems);

        jButtonRemoveItems.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/BackThick16.gif")));
        jButtonRemoveItems.setToolTipText("Remove");
        jButtonRemoveItems.setDefaultCapable(false);
        jButtonRemoveItems.setEnabled(false);
        jButtonRemoveItems
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRemoveItemsActionPerformed(evt);
                    }
                });

        jPanelAddRemove.add(jButtonRemoveItems);

        jPanelRight.add(jPanelAddRemove);

        jPanelDestination.setLayout(new javax.swing.BoxLayout(
                jPanelDestination, javax.swing.BoxLayout.Y_AXIS));

        jPanelDestination.setBorder(new javax.swing.border.TitledBorder(
                "Destination"));
        jScrollPaneDestination
                .setPreferredSize(new java.awt.Dimension(10, 1000));
        jPanelDestination.add(jScrollPaneDestination);

        jCheckBoxSelectAllDestination.setText("Select All");
        jCheckBoxSelectAllDestination.setAlignmentX(0.5F);
        jCheckBoxSelectAllDestination
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBoxSelectAllDestinationItemStateChanged(evt);
                    }
                });

        jPanel1.add(jCheckBoxSelectAllDestination);

        jButtonUp.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Up16.gif")));
        jButtonUp.setToolTipText("Move Up");
        jButtonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonUp);

        jButtonDown.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Down16.gif")));
        jButtonDown.setToolTipText("Move Down");
        jButtonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonDown);

        jPanelDestination.add(jPanel1);

        jPanelRight.add(jPanelDestination);

        jSplitPane.setRightComponent(jPanelRight);

        add(jSplitPane);

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setDefaultCapable(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonSave);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonDelete);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        doSave(evt);
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed
        doClose(evt);
    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonDeletePlanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeletePlanActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeletePlanActionPerformed

    private void jMenuItemDeletePlanActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemDeletePlanActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jMenuItemDeletePlanActionPerformed

    private void jButtonSavePlanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSavePlanActionPerformed
        doSave(evt);
    }// GEN-LAST:event_jButtonSavePlanActionPerformed

    private void jComboBoxPlanTemplateItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxPlanTemplateItemStateChanged
        // if ( evt.getSource() != jComboBoxPlanTemplate ) return;
        // if ( evt.getStateChange() != evt.SELECTED ) return; // adjusting

        ReferenceCode templatePlan;
        if (jComboBoxPlanTemplate.getSelectedIndex() == 0)
            templatePlan = new ReferenceCode(
                    PlanWriterTemplateModel.NONE_TEMPLATE_PLAN);
        else
            templatePlan = (ReferenceCode) jComboBoxPlanTemplate
                    .getSelectedItem();

        doPlanChanged(templatePlan);
    }// GEN-LAST:event_jComboBoxPlanTemplateItemStateChanged

    private void jButtonDownActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDownActionPerformed
        destList.moveItemDown(null);
    }// GEN-LAST:event_jButtonDownActionPerformed

    private void jButtonUpActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonUpActionPerformed
        destList.moveItemUp(null);
    }// GEN-LAST:event_jButtonUpActionPerformed

    private void jCheckBoxSelectAllSourceActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBoxSelectAllSourceActionPerformed
        PlanWriterTemplateModel.FileNode node = getSelectedNode();
        if (node == null)
            return;

        node.selectAll(jCheckBoxSelectAllSource.isSelected());
    }// GEN-LAST:event_jCheckBoxSelectAllSourceActionPerformed

    private void jCheckBoxSelectAllDestinationItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxSelectAllDestinationItemStateChanged
        destList.selectAll(evt.getStateChange() == evt.SELECTED);
        updateControls();
    }// GEN-LAST:event_jCheckBoxSelectAllDestinationItemStateChanged

    private void jMenuItemDownActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemDownActionPerformed
        destList.moveItemDown(null);
        updateControls();
    }// GEN-LAST:event_jMenuItemDownActionPerformed

    private void jMenuItemUpActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemUpActionPerformed
        destList.moveItemUp(null);
        updateControls();
    }// GEN-LAST:event_jMenuItemUpActionPerformed

    private void jButtonRemoveItemsActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRemoveItemsActionPerformed
        doRemoveItem(evt);
        updateControls();
    }// GEN-LAST:event_jButtonRemoveItemsActionPerformed

    private void jButtonAddItemsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddItemsActionPerformed
        doAddItem(evt);
        updateControls();
    }// GEN-LAST:event_jButtonAddItemsActionPerformed

    private void jButtonOpenTemplateDirectoryActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOpenTemplateDirectoryActionPerformed
        jMenuItemOpenTemplateDirectoryActionPerformed(null);
    }// GEN-LAST:event_jButtonOpenTemplateDirectoryActionPerformed

    private void jMenuItemSavePlanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSavePlanActionPerformed
        doSave(evt);
    }// GEN-LAST:event_jMenuItemSavePlanActionPerformed

    private void jMenuItemOpenTemplateDirectoryActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemOpenTemplateDirectoryActionPerformed
        openPlan();
    }// GEN-LAST:event_jMenuItemOpenTemplateDirectoryActionPerformed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_formAncestorAdded
        if (added2frame)
            return;

        JInternalFrame iFrame = SwingUtil.getJInternalFrame(this);
        if (iFrame == null) {
            JFrame frame = SwingUtil.getJFrame(this);
            frame.setJMenuBar(jMenuBar);

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    // public void windowClosed(java.awt.event.WindowEvent evt)
                    // {
                }
            });
        } else {
            iFrame.setJMenuBar(jMenuBar);

            iFrame.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent evt) {
                    // public void internalFrameClosed(InternalFrameEvent evt) {
                }
            });
        }

        added2frame = true;
    }// GEN-LAST:event_formAncestorAdded

    private void jRadioButtonPlainItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonPlainItemStateChanged
    }// GEN-LAST:event_jRadioButtonPlainItemStateChanged

    public static void display(java.awt.event.FocusListener[] listeners) {
        try {
            if (!exists()) {
                view = new PlanWriterTemplate();
                view.openPlan(WordSettings.getInstance().getPlanTemplateDirectory());
                // java.awt.Container container =
                SwingUtil.add2Frame(view, listeners, view.getDefaultTitle(),
                    ViewSettings.getInstance().getViewImage(view.getClass().getName()), true, true, false);
            }
            SwingUtil.setVisible(view, true);
        } catch (ServiceException e) {
            view = null;
            SwingUtil.showError(e);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneTree;

    private javax.swing.JCheckBox jCheckBoxSelectAllDestination;

    private javax.swing.JButton jButtonUp;

    private javax.swing.JToolBar jToolBarFile;

    private javax.swing.JMenuBar jMenuBar;

    private javax.swing.JPopupMenu jPopupMenuUpDown;

    private javax.swing.JButton jButtonOpenTemplateDirectory;

    private javax.swing.JMenuItem jMenuItemUp;

    private javax.swing.JMenuItem jMenuItemSavePlan;

    private javax.swing.JMenuItem jMenuItemOpenTemplateDirectory;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JButton jButtonDown;

    private javax.swing.JMenuItem jMenuItemDeletePlan;

    protected javax.swing.JButton jButtonDelete;

    private javax.swing.JScrollPane jScrollPaneDestination;

    protected javax.swing.JButton jButtonClose;

    private javax.swing.JButton jButtonDeletePlan;

    private javax.swing.JSplitPane jSplitPane;

    private javax.swing.JComboBox jComboBoxPlanTemplate;

    private javax.swing.JMenu jMenuFile;

    private javax.swing.JButton jButtonRemoveItems;

    private javax.swing.JButton jButtonAddItems;

    private javax.swing.JPanel jPanelAddRemove;

    private javax.swing.JCheckBox jCheckBoxSelectAllSource;

    private javax.swing.JPanel jPanelDestination;

    private javax.swing.JPanel jPanelToolBar;

    private javax.swing.JButton jButtonSavePlan;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JToolBar jToolBarTemplate;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JToolBar jToolBarControls;

    protected javax.swing.JButton jButtonSave;

    private javax.swing.JMenuItem jMenuItemDown;

    // End of variables declaration//GEN-END:variables

    /**
     * 
     */
    private boolean isModified() {
        return modified;
    }

    private void setModified(boolean value) {
        // if ( modified == value ) return;

        modified = value;
        updateControls();

    }

    /*
     * 
     */
    private void openPlan() {

        jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // jFileChooser.setFileFilter( null );
        jFileChooser
                .setDialogTitle("Select Plan Writer template directory ...");

        if (jFileChooser.showOpenDialog(SwingUtil.getJFrame(this)) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            openPlan(jFileChooser.getSelectedFile().getPath());
        } catch (ServiceException e) {
            SwingUtil.showError(e);
        }
    }

    private void openPlan(String dir) throws ServiceException {

        File d = new File(dir);
        if (!d.exists()) {
            throw new ServiceException("Directory does not exists: " + IOUtils.getCanonicalPath(d));
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            setRootPath(dir);
        } finally {
            setCursor(null);
        }

        try {
            updateView();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    /**
     * Invoked to reload the children of a particular node. This will also
     * restart the timer.
     */
    protected void reload(Object node) {
        model.reloadChildren(node);
        if (!timer.isRunning())
            timer.start();
    }

    /**
     * Reloader is the ActionListener used in the Timer. In response to the
     * timer updating it will reset the reloadRow/reloadPath and generate the
     * necessary event so that the display will update. It also implements the
     * TreeExpansionListener so that if the tree is altered while loading the
     * reloadRow is updated accordingly.
     */
    class Reloader implements ActionListener, TreeExpansionListener {

        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            if (!model.isReloading()) {
                // No longer loading.
                timer.stop();
                if (reloadRow != -1) {
                    generateChangeEvent(reloadRow);
                }
                reloadRow = -1;
                reloadPath = null;

            } else {
                // Still loading, see if paths changed.
                TreePath newPath = model.getPathLoading();

                if (newPath == null) {
                    // Hmm... Will usually indicate the reload thread
                    // completed between time we asked if reloading.
                    if (reloadRow != -1) {
                        generateChangeEvent(reloadRow);
                    }
                    reloadRow = -1;
                    reloadPath = null;

                } else {
                    // Ok, valid path, see if matches last path.
                    int newRow = treeTable.getTree().getRowForPath(newPath);

                    if (newPath.equals(reloadPath)) {
                        reloadCounter = (reloadCounter + 1) % 8;
                        if (newRow != reloadRow) {
                            int lastRow = reloadRow;

                            reloadRow = newRow;
                            generateChangeEvent(lastRow);
                        }
                        generateChangeEvent(reloadRow);
                    } else {
                        int lastRow = reloadRow;

                        reloadCounter = 0;
                        reloadRow = newRow;
                        reloadPath = newPath;
                        if (lastRow != reloadRow) {
                            generateChangeEvent(lastRow);
                        }
                        generateChangeEvent(reloadRow);
                    }
                }
            }
        }

        /**
         * Generates and update event for the specified row. FileSystemModel2
         * could do this, but it would not know when the row has changed as a
         * result of expanding/collapsing nodes in the tree.
         */
        protected void generateChangeEvent(int row) {
            if (row == -1)
                return;

            AbstractTableModel tModel = (AbstractTableModel) treeTable
                    .getModel();
            tModel.fireTableChanged(new TableModelEvent(tModel, row, row, 1));
        }

        //
        // TreeExpansionListener
        //

        /**
         * Invoked when the tree has expanded.
         */
        public void treeExpanded(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent) {
            updateRow();
        }

        /**
         * Invoked when the tree has collapsed.
         */
        public void treeCollapsed(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent) {
            updateRow();
        }

        /**
         * Updates the reloadRow and path, this does not genernate a change
         * event.
         */
        protected void updateRow() {
            reloadPath = model.getPathLoading();
            if (reloadPath != null)
                reloadRow = treeTable.getTree().getRowForPath(reloadPath);
        }

    }

    /**
     * A renderer that will give an indicator when a cell is being reloaded.
     */
    class IndicatorRenderer extends DefaultTableCellRenderer {
        /**
         * Makes sure the number of displayed in an internationalized manner.
         */
        protected NumberFormat formatter;

        /** Row that is currently being painted. */
        protected int lastRow;

        IndicatorRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
            formatter = NumberFormat.getInstance();
        }

        /**
         * Invoked as part of DefaultTableCellRenderers implemention. Sets the
         * text of the label.
         */
        public void setValue(Object value) {
            setText(value == null ? "---" : formatter.format(value));
        }

        /**
         * Returns this.
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            lastRow = row;
            return this;
        }

        /**
         * If the row being painted is also being reloaded this will draw a
         * little indicator.
         */
        public void paint(Graphics g) {
            if (lastRow == reloadRow) {
                int width = getWidth();
                int height = getHeight();

                g.setColor(getBackground());
                g.fillRect(0, 0, width, height);
                g.setColor(getForeground());

                int diameter = Math.min(width, height);

                if (reloadCounter < 5) {
                    g.fillArc((width - diameter) / 2, (height - diameter) / 2,
                            diameter, diameter, 90, -(reloadCounter * 90));
                } else {
                    g.fillArc((width - diameter) / 2, (height - diameter) / 2,
                            diameter, diameter, 90,
                            (4 - reloadCounter % 4) * 90);
                }
            } else {
                super.paint(g);
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void expandPath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().expandPath(selPath);
    }

    public void expandPath() {
        expandPath(treeTable.getTree().getSelectionPath());
    }

    public void collapsePath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().collapsePath(selPath);
    }

    public void collapsePath() {
        collapsePath(treeTable.getTree().getSelectionPath());
    }

    /***************************************************************************
     * 
     **************************************************************************/
    //
    public PlanWriterTemplateModel.FileNode getCurrentCollectionNode() {
        return getSelectedNode();
    }

    private PlanWriterTemplateModel.FileNode getSelectedNode() {
        // Node currently being selected
        return (PlanWriterTemplateModel.FileNode) treeTable.getTree()
                .getLastSelectedPathComponent();
    }

    private void doRemoveItem(java.awt.event.ActionEvent evt) {
        destList.removeCheckedItems();
    }

    private void doAddItem(java.awt.event.ActionEvent evt) {
        java.util.Collection nodes = model.getSelectedChildren();

        if (nodes == null)
            return;

        java.util.Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            PlanWriterTemplateModel.FileNode node = (PlanWriterTemplateModel.FileNode) iter
                    .next();
            java.io.File file = node.getFile();
            if (!destList.contains(file)) {
                boolean valid = file.exists();
                // PlanWriterTemplateModel.isValidFileName( fileName );
                try {
                    destList.addItem(file.getCanonicalPath(), valid);
                } catch (java.io.IOException e) {
                    System.err.println(e.getMessage());
                }

            }

        }

    }

    // ==========================================================================
    //
    // ==========================================================================
    protected void doClose(java.awt.event.ActionEvent evt) {
        if (isModified()
                && JOptionPane.showConfirmDialog(this,
                        "Do you want to save data before closing?",
                        "Close dialog", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            doSave(evt);

        SwingUtil.setVisible(this, false);

    }

    protected void doSave(java.awt.event.ActionEvent evt) {

        Integer planTypeID = DbConstant.TEMPLATE_PLAN;

        String selectedFiles = getSelectedFiles(false, false, planTypeID);
        if (selectedFiles == null)
            return;

        ReferenceCode plan = model.getPlan();
        plan.setObject(selectedFiles);

        if (plan.getId() <= 0) {

            String planDesc = JOptionPane.showInputDialog(this,
                    "--- New Plan Template ---",
                    "Please enter Plan Template Title: ",
                    JOptionPane.QUESTION_MESSAGE);
            if (planDesc == null || planDesc.trim().length() == 0)
                return;

            // Template (save as copy)
            plan = new ReferenceCode(plan);
            plan.setDescription(planDesc);

        } else {
            if (JOptionPane.showConfirmDialog(this, plan.getDescription(),
                    "Do You want to save changes?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                return;

        }

        PersonService person = clientService;
        try {
            boolean newPlan = plan.getId() <= 0;

            int planID = person.storePlan(plan, planTypeID);
            if (planID > 0) {

                // update templates combobox
                if (newPlan) {
                    plan.setId(planID);
                    templatePlans.add(plan);
                    jComboBoxPlanTemplate.setModel(new DefaultComboBoxModel(
                            templatePlans));
                    jComboBoxPlanTemplate.setSelectedItem(plan);
                    // doPlanChanged( plan );
                }

                SwingUtil.setTitle(this, plan.getDescription());

                setModified(false);

            }

        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    // fullPath = true, file name without root template directory, e.g.
    // \\Models\\Gearing.rtf
    private String getSelectedFiles(boolean source, boolean fullPath,
            Integer planTypeID) {
        java.util.Properties selectedFiles = source ? getSelectedFilesSource(fullPath)
                : getSelectedFilesDest(fullPath);

        if (selectedFiles == null)
            selectedFiles = new java.util.Properties();

        java.io.OutputStream os = new java.io.ByteArrayOutputStream();
        try {
            selectedFiles.store(os, null);

            String s = os.toString();

            os.close();
            os = null;

            return s;
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    private java.util.Properties getSelectedFilesSource(boolean fullPath) {
        java.util.Collection nodes = model.getSelectedChildren();
        if (nodes == null || nodes.size() == 0)
            return null;

        java.util.Properties selectedFiles = new java.util.Properties();
        int dirLength = WordSettings.getInstance().getPlanTemplateDirectory().length();

        int count = 0;
        java.util.Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            PlanWriterTemplateModel.FileNode node = (PlanWriterTemplateModel.FileNode) iter
                    .next();
            java.io.File file = node.getFile();

            String name = "" + ++count;
            String value = file.getPath();
            if (!fullPath)
                value = value.substring(dirLength);
            selectedFiles.setProperty(name, value);
        }

        return selectedFiles;

    }

    private java.util.Properties getSelectedFilesDest(boolean fullPath) {

        java.util.Collection listData = destList.getListData();
        if (listData == null || listData.size() == 0)
            return null;

        java.util.Properties selectedFiles = new java.util.Properties();
        String dir = WordSettings.getInstance().getPlanTemplateDirectory();
        int dirLength = dir.length();

        int count = 0;
        java.util.Iterator iter = listData.iterator();
        while (iter.hasNext()) {
            String name = "" + ++count;
            String value = iter.next().toString();
            if (!fullPath) {
                if (value.indexOf(dir) == 0)
                    value = value.substring(dirLength);
            }
            selectedFiles.setProperty(name, value);
        }

        return selectedFiles;

    }

    protected void doDelete(java.awt.event.ActionEvent evt) {

        ReferenceCode plan = model.getPlan();
        if (JOptionPane.showConfirmDialog(this, plan.getDescription(),
                "Do You want to delete this plan?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        try {
            if (!clientService.deletePlan(plan, DbConstant.TEMPLATE_PLAN)) {
                System.err.println("FAILED to delete: " + plan);
                return;
            }

            templatePlans.remove(plan);
            model.setPlan(null);

            jComboBoxPlanTemplate.setModel(new DefaultComboBoxModel(
                    templatePlans));
            jComboBoxPlanTemplate.setSelectedIndex(0);
            doPlanChanged(new ReferenceCode(
                    PlanWriterTemplateModel.NONE_TEMPLATE_PLAN));

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private java.util.Vector getTemplatePlans() {
        return getTemplatePlans(null);
    }

    private java.util.Vector getTemplatePlans(PersonService person) {
        if (templatePlans == null && person != null) {
            java.util.Collection plans = null;
            try {
                plans = person.getPlans(DbConstant.TEMPLATE_PLAN);
            } catch (com.argus.financials.api.ServiceException e) {
                e.printStackTrace(System.err);
            }
            templatePlans = plans == null || plans.size() == 0 ? new java.util.Vector()
                    : new java.util.Vector(plans);
            templatePlans.add(0, PlanWriterTemplateModel.NONE_TEMPLATE_PLAN);
        }
        return templatePlans;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView() throws java.io.IOException {
        updateView(clientService);
    }

    public void updateView(PersonService person) throws java.io.IOException {

        //
        if (person == null)
            person = clientService;
        getTemplatePlans(person);

        jComboBoxPlanTemplate.setModel(new DefaultComboBoxModel(templatePlans));
        jComboBoxPlanTemplate.setSelectedIndex(0);

    }

    private void doPlanChanged(ReferenceCode templatePlan) {

        SwingUtil.setTitle(this, getDefaultTitle() + templatePlan.toString());

        destList.removeAllElements();

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        destList.getModel().removeListDataListener(destListDataListener);
        try {

            model.setPlan(templatePlan);

            String dir = WordSettings.getInstance().getPlanTemplateDirectory();

            java.util.Properties files = model.getPlanFiles();
            int size = files == null ? 0 : files.size();
            for (int i = 1; i <= size; i++) {
                String fileName = files.getProperty("" + i, null);
                if (fileName == null)
                    continue;

                // check for missing files ( FILE_NOT_FOUND + fileName )
                boolean valid = PlanWriterTemplateModel.isValidFileName(fileName);
                if (valid && fileName.indexOf(dir) != 0)
                    fileName = dir + fileName;

                destList.addItem(fileName, valid);
            }
            // doAddItem(null); // trigger dest list update

            // did not trigger TreeSelectionListener, so we have to do it
            // manually
            updateControls();

        } finally {
            destList.getModel().addListDataListener(destListDataListener);
            setCursor(null);
        }

    }

    public void saveView(PersonService person) throws java.io.IOException {

    }

}