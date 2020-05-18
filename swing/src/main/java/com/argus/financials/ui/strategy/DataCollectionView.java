/*
 * DataCollectionView.java
 *
 * Created on July 3, 2002, 10:33 AM
 */

package com.argus.financials.ui.strategy;

/**
 * 
 * @author valeri chibaev
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import com.argus.financials.bean.Assumptions;
import com.argus.financials.bean.Financial;
import com.argus.financials.config.FPSLocale;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.strategy.model.DataCollectionModel;
import com.argus.financials.strategy.model.StrategyGroupData;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.swing.table.JTreeTable;
import com.argus.financials.ui.assetallocation.CurrentAssetAllocationView;
import com.argus.financials.ui.financials.AddFinancialView;
import com.argus.financials.ui.financials.CashFlowView;
import com.argus.financials.ui.financials.TaxAnalysisView;
import com.argus.financials.ui.financials.WealthView;

public class DataCollectionView extends javax.swing.JPanel implements
        com.argus.financials.swing.IReset {

    private PersonService person;

    private StrategyGroupData sgData;

    // protected
    DataCollectionModel model;

    // Used to represent the model.
    protected JTreeTable treeTable;

    // java.util.EventListener
    protected java.util.Collection listeners;

    /** Creates new form DataCollectionView */
    public DataCollectionView() {
        initComponents();
        initComponents2();
    }

    public void setListener(java.util.EventListener listener) {
        if (listener instanceof javax.swing.event.TreeSelectionListener)
            treeTable.getTree().addTreeSelectionListener(
                    (javax.swing.event.TreeSelectionListener) listener);
        else
            return;

        if (listeners == null)
            listeners = new java.util.ArrayList();
        if (listeners.contains(listener))
            listeners.remove(listener);
        listeners.add(listener);
    }

    private void initComponents2() {

        jButtonRunQuickView.setVisible(false);

        treeTable = new JTreeTable();

        // treeTable.getTree().setRootVisible(false);
        treeTable.getTree().setShowsRootHandles(false);
        treeTable.setShowVerticalLines(true);

        // treeTable.setShowGrid(true);
        // treeTable.getTree().setShowGrid(true);
        // treeTable.setBackground( java.awt.Color.lightGray );

        treeTable.getTree().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    updateFinancial(getCurrentCollectionNode());
                }
            }
        });

        // set selection mode
        treeTable.getTree().getSelectionModel().setSelectionMode(
                javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeTable.getSelectionModel().setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);

        treeTable.getTree().addTreeSelectionListener(
                new TreeSelectionListener() {
                    public void valueChanged(TreeSelectionEvent e) {
                        updateControls();
                    }
                });

        // treeTable.getTree().addTreeExpansionListener( model.new
        // TreeExpansionListener_impl() );
        // treeTable.getTree().addTreeWillExpandListener( model.new
        // TreeWillExpandListener_impl() );

    }

    private void setRoot(PersonService person, DataCollectionModel value)
            throws com.argus.financials.api.ServiceException {

        jScrollPaneTree.setViewportView(null);

        try {
            if (value == null) {

                model = new DataCollectionModel();
                model.setRoot(null);

                // load COPY of current financial data
                model.reload(person);

            } else {
                model = value;
                model.updateChangeListener(true);
            }
            treeTable.setModel(model);

            setCurrencyEditor(treeTable, DataCollectionModel.AMOUNT_2_USE);

            setSelectedRenderer(treeTable, DataCollectionModel.SELECTED);
            setSelectedEditor(treeTable, DataCollectionModel.SELECTED);

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setColumnWidth(treeTable,
                            DataCollectionModel.TREE_ITEM_NAME, 1000, 100, 400);
                    setColumnWidth(treeTable, DataCollectionModel.SERVICE,
                            1000, 30, 50);
                    setColumnWidth(treeTable, DataCollectionModel.AMOUNT, 1000,
                            30, 50);
                    setColumnWidth(treeTable, DataCollectionModel.AMOUNT_2_USE,
                            1000, 30, 50);
                    setColumnWidth(treeTable, DataCollectionModel.BALANCE,
                            1000, 30, 50);
                    setColumnWidth(treeTable, DataCollectionModel.SELECTED, 30,
                            30, 30);
                }
            });

        } finally {
            jScrollPaneTree.setViewportView(treeTable);
        }

        /*
         * if ( listeners != null ) { java.util.Iterator iter =
         * listeners.iterator(); while ( iter.hasNext() ) setListener(
         * (java.util.EventListener) iter.next() );
         *  }
         */

    }

    public void setColumnWidth(JTreeTable treeTable, int columnID,
            int maxWidth, int minWidth, int preferredWidth) {
        TableColumn column = treeTable.getColumnModel().getColumn(columnID);
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    private void updateControls() {
        // DataCollectionModel.Node collectionNode = getCurrentCollectionNode();

        // repaint();
    }

    private void setSelectedRenderer(final JTreeTable treeTable, int column) {

        TableColumn tc = treeTable.getColumnModel().getColumn(column);
        if (tc == null)
            return;

        TableCellRenderer defaultRenderer = tc.getCellRenderer(); // return
                                                                    // null in
                                                                    // 1.3
        if (defaultRenderer == null)
            defaultRenderer = treeTable.getDefaultRenderer(treeTable
                    .getColumnClass(column));

        tc.setCellRenderer(new SelectRenderer(defaultRenderer));

    }

    private void setSelectedEditor(final JTreeTable treeTable, int column) {

        TableColumn tc = treeTable.getColumnModel().getColumn(column);
        if (tc == null)
            return;

        JCheckBox cb = new JCheckBox();
        cb.setHorizontalAlignment(SwingConstants.CENTER);
        javax.swing.DefaultCellEditor ce = new javax.swing.DefaultCellEditor(cb);

        ce.addCellEditorListener(new javax.swing.event.CellEditorListener() {
            public void editingStopped(ChangeEvent e) {
                updateControls();
                // if ( Boolean.TRUE.equals( ( (javax.swing.CellEditor)
                // e.getSource() ).getCellEditorValue() ) ) {
                // } else {
                // }

            }

            public void editingCanceled(ChangeEvent e) {

            }

        }

        );

        tc.setCellEditor(ce);

    }

    private void setCurrencyEditor(final JTreeTable treeTable, int column) {

        TableColumn tc = treeTable.getColumnModel().getColumn(column);
        if (tc == null)
            return;

        JTextField tf = new JTextField();
        tf.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tf.setInputVerifier(CurrencyInputVerifier.getInstance());
        javax.swing.DefaultCellEditor ce = new javax.swing.DefaultCellEditor(tf);
        tc.setCellEditor(ce);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    class SelectRenderer
    // extends javax.swing.table.DefaultTableCellRenderer {
            extends JLabel implements TableCellRenderer {

        private TableCellRenderer defaultRenderer;

        public SelectRenderer(TableCellRenderer defaultRenderer) {
            super();

            this.defaultRenderer = defaultRenderer;

            this.setOpaque(true); // MUST do this for background to show up.
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (value == null) { // SELECTED == null, not financial node
                this.setBackground(hasFocus ? table.getBackground()
                        : (isSelected ? table.getSelectionBackground() : table
                                .getBackground()));

                return this;
            }
            return defaultRenderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPaneTree = new javax.swing.JScrollPane();
        jPanelControls = new javax.swing.JPanel();
        jButtonRunWealth = new javax.swing.JButton();
        jButtonRunTaxAnalysis = new javax.swing.JButton();
        jButtonRunCashFlow = new javax.swing.JButton();
        jButtonRunAssetAllocation = new javax.swing.JButton();
        jButtonRunQuickView = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        jScrollPaneTree
                .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneTree
                .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPaneTree.setPreferredSize(new java.awt.Dimension(1000, 3));
        add(jScrollPaneTree);

        jPanelControls.setLayout(new java.awt.GridBagLayout());

        jPanelControls.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        jButtonRunWealth.setToolTipText("Run Wealth Analysis");
        jButtonRunWealth.setText("Wealth Analysis");
        jButtonRunWealth.setDefaultCapable(false);
        jButtonRunWealth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRunWealthActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelControls.add(jButtonRunWealth, gridBagConstraints);

        jButtonRunTaxAnalysis.setToolTipText("Run Tax Analysis");
        jButtonRunTaxAnalysis.setText("Tax Analysis");
        jButtonRunTaxAnalysis.setDefaultCapable(false);
        jButtonRunTaxAnalysis
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRunTaxAnalysisActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelControls.add(jButtonRunTaxAnalysis, gridBagConstraints);

        jButtonRunCashFlow.setToolTipText("Run CashFlow");
        jButtonRunCashFlow.setText("CashFlow");
        jButtonRunCashFlow.setDefaultCapable(false);
        jButtonRunCashFlow
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRunCashFlowActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelControls.add(jButtonRunCashFlow, gridBagConstraints);

        jButtonRunAssetAllocation.setToolTipText("Run Asset Allocation");
        jButtonRunAssetAllocation.setText("Asset Allocation");
        jButtonRunAssetAllocation.setDefaultCapable(false);
        jButtonRunAssetAllocation
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRunAssetAllocationActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelControls.add(jButtonRunAssetAllocation, gridBagConstraints);

        jButtonRunQuickView.setToolTipText("Run Asset Allocation");
        jButtonRunQuickView.setText("Quick View");
        jButtonRunQuickView.setDefaultCapable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelControls.add(jButtonRunQuickView, gridBagConstraints);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonRunAssetAllocationActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunAssetAllocationActionPerformed
        doAssetAllocation();
    }// GEN-LAST:event_jButtonRunAssetAllocationActionPerformed

    private void jButtonRunCashFlowActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunCashFlowActionPerformed
        doCashFlow();
    }// GEN-LAST:event_jButtonRunCashFlowActionPerformed

    private void jButtonRunTaxAnalysisActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunTaxAnalysisActionPerformed
        doTaxAnalysis();
    }// GEN-LAST:event_jButtonRunTaxAnalysisActionPerformed

    private void jButtonRunWealthActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunWealthActionPerformed
        doWealth();
    }// GEN-LAST:event_jButtonRunWealthActionPerformed

    javax.swing.JPanel getControlPanel() {
        return jPanelControls;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneTree;

    private javax.swing.JButton jButtonRunAssetAllocation;

    private javax.swing.JButton jButtonRunQuickView;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JButton jButtonRunWealth;

    private javax.swing.JButton jButtonRunCashFlow;

    private javax.swing.JButton jButtonRunTaxAnalysis;

    // End of variables declaration//GEN-END:variables

    //
    public DataCollectionModel.Node getCurrentCollectionNode() {
        return getSelectedNode();
    }

    private DataCollectionModel.Node getSelectedNode() {
        // Node currently being selected
        return (DataCollectionModel.Node) treeTable.getTree()
                .getLastSelectedPathComponent();
    }

    public int getSelectedNodeCount() {
        java.util.Collection c = getSelectedNodes();
        return c == null ? 0 : c.size();
    }

    public java.util.Map getFinancials() {
        return model.getFinancials();
    }

    public java.util.Map getFinancials(Boolean generated) {
        return model.getFinancials(generated);
    }

    public java.util.Collection getAllNodes() {
        return model.getChildren(null);
    }

    public java.util.Collection getSelectedNodes() {
        return model.getChildren(Boolean.TRUE);
    }

    public java.util.Collection getNotSelectedNodes() {
        return model.getChildren(Boolean.FALSE);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void updateFinancial(DataCollectionModel.Node node) {
        if (node == null || !node.isFinancial())
            return;

        Financial financial = node.getFinancialObject();
        AddFinancialView view = AddFinancialView.getAddFinancialView(financial
                .getObjectTypeID().intValue());
        if (view == null)
            return;

        // update associated assets combobox model
        view.setAssets(model.getAssets());

        view.setObject(financial);
        view.setOwner(SwingUtilities.getWindowAncestor(this));
        view.setVisible(true); // modal
        if (view.getResult() != AddFinancialView.OK_OPTION)
            return;

        node.nodeChanged();

    }

    public void updateTotal() {
        model.updateTotal();
        repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void expandPath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().expandPath(selPath);
        // for ( int i = 0; i < selPath.getPathCount(); i++ )
        // expandPath( new TreePath( selPath.getPathComponent(i) ) );
    }

    public void expandPath() {
        expandPath(treeTable.getTree().getSelectionPath());
    }

    public void collapsePath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().collapsePath(selPath);
        // for ( int i = 0; i < selPath.getPathCount(); i++ )
        // collapsePath( new TreePath( selPath.getPathComponent(i) ) );

    }

    public void collapsePath() {
        collapsePath(treeTable.getTree().getSelectionPath());
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView(PersonService person, StrategyGroupData sgData)
            throws java.io.IOException {

        this.person = person;
        this.sgData = sgData;
        setRoot(person, sgData.getCollectionModel());

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public void reset() {
        cashFlowView = null;
        wealthView = null;
        taxAnalysisView = null;
    }

    private CashFlowView cashFlowView;

    private void doCashFlow() {

        try {
            Assumptions a = sgData.getCashFlowAssumptions();
            if (a == null) {
                a = new Assumptions();
                a.update(person);
                sgData.setCashFlowAssumptions(a);
            }
            if (cashFlowView == null)
                cashFlowView = new CashFlowView(a, ReportFields.CURRENT_PREFIX);
            cashFlowView.updateView(person, model.getFinancials(),
                    ReportFields.CURRENT_PREFIX);

            SwingUtil.add2Frame(cashFlowView,
                    (java.awt.event.FocusListener[]) null,
                    "Cash Flow Analysis (Current)", ViewSettings.getInstance()
                            .getViewImage(cashFlowView.getClass().getName()),
                    true, true, true);

        } catch (Exception e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
        }

    }

    private WealthView wealthView;

    private void doWealth() {

        try {
            Assumptions a = sgData.getWealthAssumptions();
            if (a == null) {
                a = new Assumptions();
                a.update(person);
                sgData.setWealthAssumptions(a);
            }
            if (wealthView == null)
                wealthView = new WealthView(a, ReportFields.CURRENT_PREFIX);
            wealthView.updateView(person, model.getFinancials(),
                    ReportFields.CURRENT_PREFIX);

            SwingUtil.add2Frame(wealthView,
                    (java.awt.event.FocusListener[]) null,
                    "Wealth Analysis (Current)", ViewSettings.getInstance()
                            .getViewImage(wealthView.getClass().getName()),
                    true, true, true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private CurrentAssetAllocationView assetAllocationView;

    private void doAssetAllocation() {

        try {
            if (assetAllocationView == null)
                assetAllocationView = new CurrentAssetAllocationView();
            assetAllocationView.updateView(model); // WE HAVE TO USE MODEL !!!
                                                    // (otherwise AA will be
                                                    // saved to db)

            SwingUtil.add2Frame(assetAllocationView,
                    (java.awt.event.FocusListener[]) null,
                    "Asset Allocation (Current)", ViewSettings.getInstance()
                            .getViewImage(
                                    assetAllocationView.getClass().getName()),
                    true, true, true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private TaxAnalysisView taxAnalysisView;

    private void doTaxAnalysis() {

        try {
            Assumptions a = null;// sgData.getTaxAnalysisAssumptions()();
            if (a == null) {
                a = new Assumptions();
                // a.update( person );
                // sgData.setTaxAnalysisAssumptions( a );
            }
            if (taxAnalysisView == null)
                taxAnalysisView = new TaxAnalysisView(a,
                        ReportFields.CURRENT_PREFIX);
            taxAnalysisView.updateView(person, model.getFinancials(),
                    ReportFields.CURRENT_PREFIX);

            SwingUtil.add2Frame(taxAnalysisView,
                    (java.awt.event.FocusListener[]) null,
                    "Tax Analysis (Current)",
                    ViewSettings.getInstance().getViewImage(
                            taxAnalysisView.getClass().getName()), true, true,
                    true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}