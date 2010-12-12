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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
import com.argus.financials.code.FinancialClass;
import com.argus.financials.code.FinancialClassID;
import com.argus.financials.code.ModelType;
import com.argus.financials.config.FPSLocale;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.io.IOUtils2;
import com.argus.financials.projection.CurrentPositionCalc;
import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.projection.GearingCalc2;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.strategy.Strategy;
import com.argus.financials.strategy.StrategyFinancial;
import com.argus.financials.strategy.StrategyModel;
import com.argus.financials.strategy.model.BaseDataModel;
import com.argus.financials.strategy.model.DataCollectionModel;
import com.argus.financials.strategy.model.DataRestructureModel;
import com.argus.financials.strategy.model.FinancialDataModel;
import com.argus.financials.strategy.model.StrategyGroupData;
import com.argus.financials.swing.ResultDialog;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.swing.table.JTreeTable;
import com.argus.financials.ui.ListSelection;
import com.argus.financials.ui.assetallocation.NewAssetAllocationView;
import com.argus.financials.ui.financials.AddFinancialView;
import com.argus.financials.ui.financials.CashFlowView;
import com.argus.financials.ui.financials.TaxAnalysisView;
import com.argus.financials.ui.financials.WealthView;
import com.argus.financials.ui.projection.AllocatedPensionViewNew;
import com.argus.financials.ui.projection.DSSView;
import com.argus.financials.ui.projection.ETPRolloverViewNew;
import com.argus.financials.ui.projection.GearingView2;
import com.argus.financials.ui.projection.MortgageCalc;
import com.argus.financials.ui.projection.MortgageView;
import com.argus.financials.ui.projection.SnapEntryView;
import com.argus.util.ReferenceCode;

public class DataRestructureView extends javax.swing.JPanel implements
        FinancialClassID, com.argus.financials.swing.IReset {

    private static boolean DEBUG = false;

    private PersonService person;

    private StrategyGroupData sgData;

    // protected
    DataRestructureModel model;

    // Used to represent the collectionModel.
    protected JTreeTable treeTable;

    // java.util.EventListener
    protected java.util.Collection listeners;

    /** Creates new form DataCollectionView */
    public DataRestructureView() {
        FPSLocale r = com.argus.financials.config.FPSLocale.getInstance();
        DEBUG = Boolean.valueOf(System.getProperty("DEBUG")).booleanValue();

        initComponents();
        initComponents2();
    }

    public com.argus.format.Currency getCurrency() {
        return model.getCurrency();
    }

    public void setListener(java.util.EventListener listener) {
        // to notify owner form
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
        jButtonEditProjection.setVisible(false);
        jButtonRunProjections.setVisible(false);

        treeTable = new JTreeTable();

        // treeTable.getTree().setRootVisible(false);
        treeTable.getTree().setShowsRootHandles(false);
        // treeTable.setShowVerticalLines( true );

        // treeTable.setShowGrid(true);
        // treeTable.getTree().setShowGrid(true);
        // treeTable.setBackground( java.awt.Color.lightGray );

        treeTable.getTree().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    updateFinancial(getCurrentRestructureNode());
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

        // treeTable.getTree().addTreeExpansionListener( collectionModel.new
        // TreeExpansionListener_impl() );
        // treeTable.getTree().addTreeWillExpandListener( collectionModel.new
        // TreeWillExpandListener_impl() );

    }

    private void setRoot(PersonService person, DataRestructureModel value)
            throws com.argus.financials.service.ServiceException {

        jScrollPaneTree.setViewportView(null);

        try {
            if (value == null) {
                model = new DataRestructureModel();
                model.setRoot(null);

                // do nothing, this is new strategy
                model.reload(person);

            } else {
                model = value;
                model.updateChangeListener(true);
                // JOptionPane.showMessageDialog( this, "Value=" +
                // model.getDummy() );
            }
            treeTable.setModel(model);

            // setProjectionRenderer( treeTable, DataRestructureModel.PROJECTION
            // );
            // setProjectionEditor( treeTable, DataRestructureModel.PROJECTION
            // );

            setSelectedRenderer(treeTable, DataRestructureModel.SELECTED);
            setSelectedEditor(treeTable, DataRestructureModel.SELECTED);

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setColumnWidth(treeTable,
                            DataRestructureModel.TREE_ITEM_NAME, 1000, 100, 500);
                    setColumnWidth(treeTable, DataRestructureModel.SERVICE,
                            1000, 30, 50);
                    setColumnWidth(treeTable,
                            DataRestructureModel.AMOUNT_2_USE, 1000, 50, 50);
                    setColumnWidth(treeTable, DataRestructureModel.PROJECTION,
                            0, 0, 0);
                    setColumnWidth(treeTable, DataRestructureModel.SELECTED,
                            50, 50, 50);
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
        if (column == null)
            return;
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    private void updateControls() {
        DataRestructureModel.Node2 node = getCurrentRestructureNode();

        // int count = getSelectedNodeCount();

        jButtonAddItem.setEnabled(node == null ? false : node.isAddable());
        jButtonRemoveItem.setEnabled(node == null ? false : node.isRemovable());

        // jButtonRunProjections.setEnabled( node == null ? false :
        // !node.isStrategyFinancial() );
        // jButtonEditProjection.setEnabled( node == null ? false :
        // node.isStrategyModel() );

        repaint();
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

    private void setProjectionRenderer(final JTreeTable treeTable, int column) {

        TableColumn tc = treeTable.getColumnModel().getColumn(column);
        if (tc == null)
            return;

        TableCellRenderer defaultRenderer = tc.getCellRenderer(); // return
                                                                    // null in
                                                                    // 1.3
        if (defaultRenderer == null)
            defaultRenderer = treeTable.getDefaultRenderer(treeTable
                    .getColumnClass(column));

        tc.setCellRenderer(new ProjectionRenderer(defaultRenderer, false));

    }

    private void setProjectionEditor(final JTreeTable treeTable, int column) {

        TableColumn tc = treeTable.getColumnModel().getColumn(column);
        if (tc == null)
            return;

        // First, set up the button that brings up the dialog.
        JButton button = new JButton("");
        // {
        // public void setText(String s) {
        // Button never shows text -- only color.
        // }
        // };
        // button.setBackground( Color.gray );
        // button.setBorderPainted(true);
        // button.setMargin(new Insets(2,2,2,2));

        tc.setCellEditor(new ProjectionEditor(button));

        // Here's the code that brings up the dialog.
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                changeProjection(getCurrentRestructureNode());
            }
        });

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
            if (value == null) { // SELECTED == null, not financial node or
                                    // not selectable
                this.setBackground(hasFocus ? table.getBackground()
                        : (isSelected ? table.getSelectionBackground() : table
                                .getBackground()));
                return this;
            }
            return defaultRenderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
        }

    }

    /*
     * The editor button that brings up the dialog. We extend DefaultCellEditor
     * for convenience, even though it mean we have to create a dummy check box.
     * Another approach would be to copy the implementation of TableCellEditor
     * methods from the source code for DefaultCellEditor.
     */
    class ProjectionEditor extends DefaultCellEditor {
        Color currentColor = null;

        public ProjectionEditor(JButton button) {
            // Unfortunately, the constructor expects a check box, combo box, or
            // text field.
            super(new JCheckBox());

            editorComponent = button;
            setClickCountToStart(2); // This is usually 1 or 2.

            // Must do this so that editing stops when appropriate.
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        // protected void fireEditingStopped() {
        // super.fireEditingStopped();
        // }

        public Object getCellEditorValue() {
            // return currentColor;
            return super.getCellEditorValue();
        }

        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            ((JButton) editorComponent).setText(value == null ? null : value
                    .toString());
            // currentColor = (Color) value;
            return editorComponent;
        }

    }

    class ProjectionRenderer extends JLabel implements TableCellRenderer {
        private javax.swing.border.Border unselectedBorder = null;

        private javax.swing.border.Border selectedBorder = null;

        private boolean isBordered = true;

        private TableCellRenderer defaultRenderer;

        public ProjectionRenderer(TableCellRenderer defaultRenderer,
                boolean isBordered) {
            super();

            this.defaultRenderer = defaultRenderer;
            this.isBordered = isBordered;

            setOpaque(true); // MUST do this for background to show up.
            // this.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR )
            // );
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            // System.out.println( value == null ? null :
            // value.getClass().getName() + "\n\t" + value.toString() );

            if (value == null) { // SELECTED == null, not financial node
                this.setBackground(hasFocus ? table.getBackground()
                        : (isSelected ? table.getSelectionBackground() : table
                                .getBackground()));

                if (isBordered) {
                    if (isSelected) {
                        if (selectedBorder == null)
                            selectedBorder = javax.swing.BorderFactory
                                    .createMatteBorder(2, 5, 2, 5, table
                                            .getSelectionBackground());
                        setBorder(selectedBorder);
                    } else {
                        if (unselectedBorder == null)
                            unselectedBorder = javax.swing.BorderFactory
                                    .createMatteBorder(2, 5, 2, 5, table
                                            .getBackground());
                        setBorder(unselectedBorder);
                    }
                }
                return this;
            }
            return defaultRenderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void changeProjection(DataRestructureModel.Node2 node) {
        if (node == null || !node.isStrategyModel())
            return;

        StrategyModel sm = (StrategyModel) node.getObject();

        Object model = getProjection(sm.getModel());
        if (model == null)
            return;

        sm.setModel(model.equals(Model.NONE) ? null : (Model) model);
    }

    private void editProjection(DataRestructureModel.Node2 node, boolean show,
            boolean addGeneratedItems) {
        if (node == null || !node.isStrategyModel())
            return;

        StrategyModel sm = (StrategyModel) node.getObject();
        if (sm == null)
            return;

        Model m = sm.getModel();
        if (m == null)
            return;

        // get calculator model
        final MoneyCalc calc = MoneyCalc.getNewCalculator(m.getTypeID());
        if (calc == null)
            return;

        calc.setModel(m);

        // TODO: make it array of Financials ???
        // FinancialTotals totals = node.getTotals();
        java.math.BigDecimal initialAmount = sm.getTotalInitialAmount();
        java.math.BigDecimal contributions = sm.getTotalContributionAmount(); // optional

        // get View if show == true
        javax.swing.JComponent view = null;
        switch (sm.getTypeID().intValue()) {

        case ModelType.iCURRENT_POSITION_CALC:
            // for specified asset type
            calc.setInitialValue(initialAmount == null ? 0. : initialAmount
                    .doubleValue());
            ((CurrentPositionCalc) calc).setAddValue(contributions == null ? 0.
                    : contributions.doubleValue());

            if (show) {
                view = new SnapEntryView(calc);
                ((SnapEntryView) view).hideControls();
            }
            break;
        case ModelType.iINSURANCE_NEEDS:
            break;
        case ModelType.iPREMIUM_CALC:
            break;
        case ModelType.iINVESTMENT_GEARING:
            ((GearingCalc2) calc)
                    .setInitialInvestorAmount(initialAmount == null ? 0.
                            : initialAmount.doubleValue());
            ((GearingCalc2) calc)
                    .setRegularInvestorContributionAmount(contributions == null ? 0.
                            : contributions.doubleValue());

            if (show) {
                view = new GearingView2(calc);
                ((GearingView2) view).hideControls();
            }
            break;
        case ModelType.iPROJECTED_WEALTH:
            break;
        case ModelType.iINVESTMENT_PROPERTIES:
            break;
        case ModelType.iLOAN_MORTGAGE_CALC:
            calc.setInitialValue(initialAmount == null ? 0. : initialAmount
                    .doubleValue());
            ((MortgageCalc) calc).isValidPerchasePriceInputVerifier(calc
                    .getInitialValue());

            if (show) {
                view = new MortgageView((MortgageCalc) calc);
                // calc.setModel( m );

                // calc.calculate();
                ((MortgageView) view).calculate();
                ((MortgageView) view).hideControls();
            }
            break;
        case ModelType.iALLOCATED_PENSION:
            calc.setInitialValue(initialAmount == null ? 0. : initialAmount
                    .doubleValue());

            if (show) {
                view = new AllocatedPensionViewNew(calc);
                ((AllocatedPensionViewNew) view).hideControls();
            }
            break;
        case ModelType.iETP_ROLLOVER:
            calc.setInitialValue(initialAmount == null ? 0. : initialAmount
                    .doubleValue());
            ((ETPCalcNew) calc).isValidETPTotalInputVerifier(calc
                    .getInitialValue());

            if (show) {
                view = new ETPRolloverViewNew(calc);
                ((ETPRolloverViewNew) view).hideControls();
            }
            break;
        case ModelType.iSUPERANNUATION_RBL:
            break;
        case ModelType.iRETIREMENT_CALC:
            break;
        case ModelType.iRETIREMENT_HOME:
            break;
        case ModelType.iPAYG_CALC:
            break;
        case ModelType.iCGT_CALC:
            break;
        case ModelType.iSOCIAL_SECURITY_CALC:

            if (show) {
                view = new DSSView(calc);
                ((DSSView) view).hideControls();
            }
            break;
        default:
            ;
        }

        if (show && view != null) {

            String title = sm.getTitle();
            if (!title.equals(sm.getModelTitle()))
                title += " - " + sm.getModelTitle();

            ResultDialog rd = new ResultDialog(view); // modal
            rd.setTitle(title);
            /*
             * rd.addActionListener( new java.awt.event.ActionListener() {
             * public void actionPerformed( ActionEvent evt ) { if (
             * ResultDialog.OK.equals( evt.getActionCommand() ) ) {
             *  } else if ( ResultDialog.CANCEL.equals( evt.getActionCommand() ) ) {
             *  } } } );
             */

            // to trigger re-calculate and update view/listeners
            calc.setModified();

            rd.setVisible(true);

            if (rd.getResult() != ResultDialog.OK_OPTION)
                return;

            // re-save model data
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.BufferedWriter output = new java.io.BufferedWriter(sw);
            try {
                IOUtils2.writeHeader(view, output);
                IOUtils2.write(view, output);

                output.flush();

                sm.getModel().setData(sw.toString());

            } catch (java.io.IOException e) {
                e.printStackTrace(System.err);
                return;

            } finally {
                if (sw != null) {
                    try {
                        sw.close();
                    } catch (java.io.IOException e) { /* ignore by now */
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (java.io.IOException e) { /* ignore by now */
                    }
                }
            }

        }

        if (addGeneratedItems)
            addGeneratedItems(sm, calc);

    }

    private void runProjections(DataRestructureModel.Node2 node, boolean show) {
        if (node == null)
            return;

        // break condition
        if (node.isStrategyModel()) {
            runProjection(node, show);
            return;
        }

        DataRestructureModel.Node2[] nodes = (DataRestructureModel.Node2[]) node
                .getChildren();
        if (nodes == null)
            return;

        for (int i = 0; i < nodes.length; i++)
            runProjections(nodes[i], show);

    }

    private void runProjection(DataRestructureModel.Node2 node, boolean show) {
        editProjection(node, show, true);
    }

    private void addGeneratedItems(StrategyModel sm, MoneyCalc calc) {
        Financial[] financials = calc.getGeneratedFinancials(sm.getTitle());
        if (financials == null)
            return;

        DataRestructureModel.Node2 rootNode = (DataRestructureModel.Node2) model
                .getRoot();
        Integer strategyGroupID = ((ReferenceCode) rootNode.getObject())
                .getCodeIDInteger();
        if (strategyGroupID == null) {
            JOptionPane
                    .showMessageDialog(
                            this,
                            "You have to save this Strategy before you try to run projections.",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FinancialClass strategyType = new FinancialClass();
        FinancialDataModel.Node nodeCashflow = (FinancialDataModel.Node) rootNode
                .find(RC_CASHFLOW);
        FinancialDataModel.Node nodeAssetsLiabilities = (FinancialDataModel.Node) rootNode
                .find(RC_ASSETS_LIABILITIES);
        BaseDataModel.BaseNode strategyNode;
        BaseDataModel.BaseNode strategyModelNode;

        // remove these items from incomes
        BaseDataModel.BaseNode node = nodeCashflow.find(RC_REGULAR_INCOME);
        node = node.find(sm.getReferenceCode());
        if (node != null)
            node.remove();
        // .. expenses
        node = nodeCashflow.find(RC_REGULAR_EXPENSE);
        node = node.find(sm.getReferenceCode());
        if (node != null)
            node.remove();
        // .. and liabilities
        node = nodeAssetsLiabilities.find(RC_LIABILITY);
        node = node.find(sm.getReferenceCode());
        if (node != null)
            node.remove();

        for (int i = 0; i < financials.length; i++) {
            // Regular: Income, Expense or Liability

            Financial f = financials[i];
            if (f == null)
                continue;

            ReferenceCode code = strategyType.getCode(f.getObjectTypeID());

            strategyNode = nodeCashflow.find(code); // income or expense
            if (strategyNode == null) {
                strategyNode = nodeAssetsLiabilities.find(code); // liability
                if (strategyNode == null)
                    continue; // can not find parent node (impossible)
            }

            // this one we have to create if it does not exist
            strategyModelNode = strategyNode.find(sm.getReferenceCode());
            if (strategyModelNode == null)
                strategyModelNode = strategyNode.addChild(new StrategyModel(
                        new ReferenceCode(0, sm.getTitle())));
            f.setStrategyGroupID(strategyGroupID);
            strategyModelNode.addChild(new StrategyFinancial(f));
        }

    }

    private Object getProjection(Model selected) {

        PersonService person = ServiceLocator.getInstance().getClientPerson();
        java.util.Vector items = Strategy.getAllModels(person); // Model(s)
        if (items == null)
            return null;

        items.add(0, Model.NONE);
        ListSelection listSelection = new ListSelection(new JList());
        listSelection.setSingleSelection();
        listSelection.setListData(items);

        listSelection.setSelectedValue(selected, true);

        // modal
        SwingUtil.add2Dialog(null, "Select Projection ..", true, listSelection);
        if (listSelection.getResult() == ListSelection.OK_OPTION)
            return listSelection.getSelectedValue();
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelTree = new javax.swing.JPanel();
        jScrollPaneTree = new javax.swing.JScrollPane();
        jPanelControls = new javax.swing.JPanel();
        jButtonRunWealth = new javax.swing.JButton();
        jButtonRunTaxAnalysis = new javax.swing.JButton();
        jButtonRunCashFlow = new javax.swing.JButton();
        jButtonRunAssetAllocation = new javax.swing.JButton();
        jButtonRunQuickView = new javax.swing.JButton();
        jPanelTreeControls = new javax.swing.JPanel();
        jButtonAddItem = new javax.swing.JButton();
        jButtonRemoveItem = new javax.swing.JButton();
        jButtonEditProjection = new javax.swing.JButton();
        jButtonRunProjections = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanelTree.setLayout(new javax.swing.BoxLayout(jPanelTree,
                javax.swing.BoxLayout.Y_AXIS));

        jScrollPaneTree
                .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneTree
                .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPanelTree.add(jScrollPaneTree);

        add(jPanelTree, java.awt.BorderLayout.CENTER);

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
        jButtonRunCashFlow.setText("Cash Flow");
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
        jButtonRunQuickView
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRunQuickViewActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelControls.add(jButtonRunQuickView, gridBagConstraints);

        add(jPanelControls, java.awt.BorderLayout.EAST);

        jPanelTreeControls.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT));

        jButtonAddItem
                .setToolTipText("<html>\nAdd New Recommendation,\n<br>\nNew Asset or Liability or Income or Expense\n</html>");
        jButtonAddItem.setText("Add Item");
        jButtonAddItem.setDefaultCapable(false);
        jButtonAddItem.setEnabled(false);
        jButtonAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddItemActionPerformed(evt);
            }
        });

        jPanelTreeControls.add(jButtonAddItem);

        jButtonRemoveItem
                .setToolTipText("<html>\nRemove Recommendation or\n<br>\nAsset or Liability or Income or Expense\n</html>");
        jButtonRemoveItem.setText("Remove Item");
        jButtonRemoveItem.setDefaultCapable(false);
        jButtonRemoveItem.setEnabled(false);
        jButtonRemoveItem
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRemoveItemActionPerformed(evt);
                    }
                });

        jPanelTreeControls.add(jButtonRemoveItem);

        jButtonEditProjection.setText("Edit Projection");
        jButtonEditProjection.setDefaultCapable(false);
        jButtonEditProjection.setEnabled(false);
        jButtonEditProjection
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonEditProjectionActionPerformed(evt);
                    }
                });

        jPanelTreeControls.add(jButtonEditProjection);

        jButtonRunProjections.setToolTipText("Run Projection(s)");
        jButtonRunProjections.setText("Projection(s)");
        jButtonRunProjections.setDefaultCapable(false);
        jButtonRunProjections.setEnabled(false);
        jButtonRunProjections
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRunProjectionsActionPerformed(evt);
                    }
                });

        jPanelTreeControls.add(jButtonRunProjections);

        add(jPanelTreeControls, java.awt.BorderLayout.SOUTH);

    }// GEN-END:initComponents

    private void jButtonRunWealthActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunWealthActionPerformed
        doWealth();
    }// GEN-LAST:event_jButtonRunWealthActionPerformed

    private void jButtonRunQuickViewActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunQuickViewActionPerformed
        doQuickView();
    }// GEN-LAST:event_jButtonRunQuickViewActionPerformed

    private void jButtonRunTaxAnalysisActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunTaxAnalysisActionPerformed
        doTaxAnalysis();
    }// GEN-LAST:event_jButtonRunTaxAnalysisActionPerformed

    private void jButtonEditProjectionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEditProjectionActionPerformed
        DataRestructureModel.Node2 restructureNode = getCurrentRestructureNode();
        editProjection(restructureNode, true, false);
    }// GEN-LAST:event_jButtonEditProjectionActionPerformed

    private void jButtonRunAssetAllocationActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunAssetAllocationActionPerformed
        doAssetAllocation();
    }// GEN-LAST:event_jButtonRunAssetAllocationActionPerformed

    private void jButtonRunCashFlowActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunCashFlowActionPerformed
        doCashFlow();
    }// GEN-LAST:event_jButtonRunCashFlowActionPerformed

    private void jButtonRunProjectionsActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRunProjectionsActionPerformed
        DataRestructureModel.Node2 restructureNode = getCurrentRestructureNode();
        runProjections(restructureNode, true);
    }// GEN-LAST:event_jButtonRunProjectionsActionPerformed

    private void jButtonAddItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddItemActionPerformed
        DataRestructureModel.Node2 restructureNode = getCurrentRestructureNode();
        expandPath();
        addItem(restructureNode);
    }// GEN-LAST:event_jButtonAddItemActionPerformed

    private void jButtonRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRemoveItemActionPerformed
        removeItem(getCurrentRestructureNode());
    }// GEN-LAST:event_jButtonRemoveItemActionPerformed

    javax.swing.JPanel getControlPanel() {
        return jPanelControls;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneTree;

    private javax.swing.JButton jButtonAddItem;

    private javax.swing.JButton jButtonEditProjection;

    private javax.swing.JButton jButtonRunAssetAllocation;

    private javax.swing.JPanel jPanelTreeControls;

    private javax.swing.JButton jButtonRemoveItem;

    private javax.swing.JButton jButtonRunProjections;

    private javax.swing.JButton jButtonRunQuickView;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JPanel jPanelTree;

    private javax.swing.JButton jButtonRunWealth;

    private javax.swing.JButton jButtonRunCashFlow;

    private javax.swing.JButton jButtonRunTaxAnalysis;

    // End of variables declaration//GEN-END:variables

    //
    public DataRestructureModel.Node2 getCurrentRestructureNode() {
        return (DataRestructureModel.Node2) getSelectedNode();
    }

    public DataRestructureModel.Node2 getStrategyNode(Integer objectTypeID) {
        return (DataRestructureModel.Node2) model.getStrategyNode(objectTypeID);
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

    public java.util.Collection getSelectedNodes() {
        return model.getChildren(Boolean.TRUE);
    }

    /*
     * public void addFinancials( java.util.Collection nodes ) {
     * model.addFinancials( nodes ); repaint(); }
     */
    public void addFinancials(java.util.Map financials) {
        model.addFinancials(financials);
        repaint();
    }

    // add children from collectionModel to model
    private void addFinancials(DataRestructureModel.Node2 dest,
            java.util.Collection nodes) {
        if (nodes == null || dest == null || dest.isLeaf())
            return;

        java.util.Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            DataCollectionModel.Node1 node = (DataCollectionModel.Node1) iter
                    .next();
            Financial f = node.getFinancialObject();
            if (f.getUsedAmount() != null) {

                StrategyFinancial sf = new StrategyFinancial(f);
                sf.setAmount(f.getUsedAmount());
                f.setUsedAmount(null);

                dest.addChild(sf);

            }

            node.setSelected(false);

        }

    }

    // add/remove
    private void addItem(DataRestructureModel.Node2 node) {
        if (node == null || !node.isAddable())
            return;

        Boolean recommendation = null;
        if (node.isRecommendations())
            recommendation = Boolean.TRUE;
        else if (node.getParent() != null
                && ((DataRestructureModel.Node2) node.getParent())
                        .isRecommendations())
            recommendation = Boolean.FALSE;

        if (recommendation == null) {
            node = model.addItem(node, null); // add first
        } else {
            AddRecommendationView view = AddRecommendationView
                    .display(recommendation.booleanValue());
            if (view.getResult() != AddRecommendationView.OK_OPTION)
                return;

            node = model.addItem(node, view.getItem());
        }

        if (node != null && node.isFinancial()) {
            updateFinancial(node);
        } else {
            expandPath();
            // ( new TreePath( node ) );
        }

    }

    private void removeItem(DataRestructureModel.Node2 node) {
        if (node == null || !node.isRemovable())
            return;

        model.removeItem(node);
    }

    // work with financials
    protected void updateFinancial(DataRestructureModel.Node2 node) {
        if (node == null || !node.isFinancial())
            return;

        // Reccommendation Source Children are not editable
        if (node.isRecommendationSourceFinancial())
            return;

        Financial f = node.getFinancialObject();
        AddFinancialView view = AddFinancialView.getAddFinancialView(f
                .getObjectTypeID().intValue());
        if (view == null)
            return;

        // update associated assets combobox model
        view.setAssets(model.getAssets());

        view.setObject(f);
        view.setOwner(SwingUtilities.getWindowAncestor(this));
        view.setVisible(true); // modal
        if (view.getResult() != AddFinancialView.OK_OPTION)
            return;

        node.getStrategyFinancialObject().setAmount(f.getAmount());

        node.nodeChanged();
        updateTotal();

    }

    public void updateTotal() {
        model.updateTotal();
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
        TreePath selPath = treeTable.getTree().getSelectionPath();
        if (selPath == null)
            return;

        expandPath(selPath);
        // for ( int i = 0; i < selPath.getPathCount(); i++ )
        // expandPath( new TreePath( selPath.getPathComponent(i) ) );
    }

    public void collapsePath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().collapsePath(selPath);
        // for ( int i = 0; i < selPath.getPathCount(); i++ )
        // collapsePath( new TreePath( selPath.getPathComponent(i) ) );

    }

    public void collapsePath() {
        TreePath selPath = treeTable.getTree().getSelectionPath();
        if (selPath == null)
            return;

        collapsePath(selPath);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView(PersonService person, StrategyGroupData sgData)
            throws java.io.IOException {

        this.person = person;
        this.sgData = sgData;
        setRoot(person, sgData.getRestructureModel());

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

        // run ALL projections again
        // DataRestructureModel.Node2 restructureNode =
        // (DataRestructureModel.Node2) model.getRoot();
        // runProjections( restructureNode, false );
        // runProjections( restructureNode, true );

        // DataRestructureModel.Node2 rootNode = (DataRestructureModel.Node2)
        // model.getRoot();
        // FinancialDataModel.Node nodeCashflow = (FinancialDataModel.Node)
        // rootNode.find( RC_CASHFLOW );

        // java.util.Map financials = model.getCashflowItems();
        /*
         * try { // save generated items to database saveView( currentPerson );
         * financials = currentPerson.getStrategyGroupFinancials(
         * model.getRootObject().getCodeIDInteger(), true ); } catch (
         * com.argus.financials.service.ServiceException e ) { e.printStackTrace( System.err );
         * return; }
         */

        // int years2Project = 10;
        try {
            Assumptions a = sgData.getCashFlowAssumptions();
            if (a == null) {
                a = new Assumptions();
                a.update(person);
                sgData.setCashFlowAssumptions(a);
            }
            if (cashFlowView == null)
                cashFlowView = new CashFlowView(a, ReportFields.PROPOSED_PREFIX);
            cashFlowView.updateView(person, model.getFinancials(),
                    ReportFields.PROPOSED_PREFIX);

            SwingUtil.add2Frame(cashFlowView,
                    (java.awt.event.FocusListener[]) null,
                    "Cash Flow Analysis (Projected)", ViewSettings
                            .getInstance().getViewImage(
                                    cashFlowView.getClass().getName()), true,
                    true, true);

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
                wealthView = new WealthView(a, ReportFields.PROPOSED_PREFIX);
            wealthView.updateView(person, model.getFinancials(),
                    ReportFields.PROPOSED_PREFIX);

            SwingUtil.add2Frame(wealthView,
                    (java.awt.event.FocusListener[]) null,
                    "Wealth Analysis (Projected)", ViewSettings.getInstance()
                            .getViewImage(wealthView.getClass().getName()),
                    true, true, true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private NewAssetAllocationView assetAllocationView;

    private void doAssetAllocation() {
        try {
            if (assetAllocationView == null)
                assetAllocationView = new NewAssetAllocationView();

            DataCollectionModel collectionModel = sgData == null ? null
                    : sgData.getCollectionModel();
            assetAllocationView.updateView(collectionModel == null ? null
                    : collectionModel.getFinancials(), model.getFinancials());

            SwingUtil.add2Frame(assetAllocationView,
                    (java.awt.event.FocusListener[]) null,
                    "Asset Allocation (Projected)", ViewSettings.getInstance()
                            .getViewImage(
                                    assetAllocationView.getClass().getName()),
                    true, true, true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private void doQuickView() {

        // run ALL projections again
        DataRestructureModel.Node2 restructureNode = (DataRestructureModel.Node2) model
                .getRoot();
        runProjections(restructureNode, false);

        try {
            SnapEntryView view = new SnapEntryView();

            view.updateView(DataRestructureModel.MODEL_NAME);
            view.updateFinancials(model.getAssetLiabilityItems());

            Window w = SwingUtilities.getWindowAncestor(this);
            Frame owner = w instanceof Frame ? (Frame) w : null;

            // Frame owner, String title, boolean modal, Component comp, boolean
            // resizable, boolean visible ) {
            SwingUtil.add2Dialog(owner, DataRestructureModel.MODEL_NAME, false,
                    view, true, true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private TaxAnalysisView taxAnalysisView;

    private void doTaxAnalysis() {
        // run ALL projections again
        // DataRestructureModel.Node2 restructureNode =
        // (DataRestructureModel.Node2) model.getRoot();
        // runProjections( restructureNode, false );

        try {
            Assumptions a = null;// sgData.getTaxAnalysisAssumptions()();
            if (a == null) {
                a = new Assumptions();
                // a.update( person );
                // sgData.setTaxAnalysisAssumptions( a );
            }
            if (taxAnalysisView == null)
                taxAnalysisView = new TaxAnalysisView(a,
                        ReportFields.PROPOSED_PREFIX);
            taxAnalysisView.updateView(person, model.getFinancials(),
                    ReportFields.PROPOSED_PREFIX);

            SwingUtil.add2Frame(taxAnalysisView,
                    (java.awt.event.FocusListener[]) null,
                    "Tax Analysis (Proposed)",
                    ViewSettings.getInstance().getViewImage(
                            taxAnalysisView.getClass().getName()), true, true,
                    true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}