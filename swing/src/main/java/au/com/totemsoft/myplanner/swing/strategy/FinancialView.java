/*
 * DataCollectionView.java
 *
 * Created on July 3, 2002, 10:33 AM
 */

package au.com.totemsoft.myplanner.swing.strategy;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.BorderLayout;

import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;

import com.l2fprod.common.swing.JTaskPaneGroup;

import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.strategy.model.FinancialDataModel;
import au.com.totemsoft.myplanner.swing.IMenuCommand;
import au.com.totemsoft.myplanner.table.swing.JTreeTable;
import au.com.totemsoft.swing.IReset;
import au.com.totemsoft.util.ReferenceCode;

public class FinancialView extends javax.swing.JPanel 
    implements IReset 
{
    // Used to represent the model.
    protected JTreeTable treeTable;

    // java.util.EventListener
    protected java.util.Collection listeners;
    
    private FinancialViewActionMap am;
    
    /** Creates new form DataCollectionView */
    public FinancialView() {
        initComponents();

        am = new FinancialViewActionMap(this, treeTable);
        setActionMap(am);

        JTaskPaneGroup tpgFinancials = new JTaskPaneGroup();
        tpgFinancials.setTitle("Financials");
        tpgFinancials.setSpecial(true);
        tpgFinancials.add((Action) getActionMap().get(IMenuCommand.FINANCIAL_COPY));
        tpgFinancials.add((Action) getActionMap().get(IMenuCommand.FINANCIAL_ADD));
        tpgFinancials.add((Action) getActionMap().get(IMenuCommand.FINANCIAL_EDIT));
        tpgFinancials.add((Action) getActionMap().get(IMenuCommand.FINANCIAL_REMOVE));
        jPanelControls.add(tpgFinancials);       
        tpgFinancials.setExpanded(true);

        JTaskPaneGroup tpgAnalysis = new JTaskPaneGroup();
        tpgAnalysis.setTitle("Analysis");
        tpgAnalysis.setSpecial(true);
        //tpgAnalysis.add((Action) getActionMap().get(IMenuCommand.CURRENT_POSITION_CALC));
        tpgAnalysis.add((Action) getActionMap().get(IMenuCommand.CASHFLOW_ANALYSIS));
        tpgAnalysis.add((Action) getActionMap().get(IMenuCommand.WEALTH_ANALYSIS));
        tpgAnalysis.add((Action) getActionMap().get(IMenuCommand.TAX_ANALYSIS));
        tpgAnalysis.add((Action) getActionMap().get(IMenuCommand.ASSET_ALLOCATION));
        //tpgAnalysis.add((Action) getActionMap().get(IMenuCommand.UPDATE_PRICE));
        jPanelControls.add(tpgAnalysis);       
        tpgAnalysis.setExpanded(false);

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

    private void initComponents() {

        jScrollPaneTree = new javax.swing.JScrollPane();
        jPanelControls = new com.l2fprod.common.swing.JTaskPane();

        setLayout(new BorderLayout());

        jScrollPaneTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneTree.setPreferredSize(new java.awt.Dimension(1000, 3));
        add(jScrollPaneTree, BorderLayout.CENTER);

        add(jPanelControls, BorderLayout.EAST);

        treeTable = new JTreeTable();

        // treeTable.getTree().setRootVisible(false);
        treeTable.getTree().setShowsRootHandles(false);
        treeTable.setShowVerticalLines(true);

        // treeTable.setShowGrid(true);
        // treeTable.getTree().setShowGrid(true);
        // treeTable.setBackground( java.awt.Color.lightGray );

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

    private void setRoot(Integer personId, ReferenceCode root)
            throws au.com.totemsoft.myplanner.api.ServiceException
    {
        am.setRoot(personId, root);
        
        jScrollPaneTree.setViewportView(null);
        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setColumnWidth(treeTable, FinancialDataModel.TREE_ITEM_NAME, 1000, 100, 200);
                    setColumnWidth(treeTable, FinancialDataModel.SERVICE, 1000, 30, 50);
                    setColumnWidth(treeTable, FinancialDataModel.AMOUNT, 1000, 30, 50);
                    setColumnWidth(treeTable, FinancialDataModel.OWNER, 1000, 30, 50);
                }
            });
        } finally {
            jScrollPaneTree.setViewportView(treeTable);
        }
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
        FinancialDataModel.Node node = am.getCurrentCollectionNode();

//        jButtonAddFinancial.setEnabled(node != null && node.isFinancialGroup());
//        jButtonRemoveFinancial.setEnabled(node != null && node.isFinancial()
//                && !node.getFinancialObject().isGenerated());
//        jButtonUpdateFinancial.setEnabled(node != null && node.isFinancial());
//        jButtonCopyFinancial.setEnabled(node != null && node.isFinancial());

        repaint();
    }

    /**
     * 
     * private void updateChart() {
     *  // do chart graphView.removeChartLabels();
     * FinancialDataModel.FinancialItems financialItems =
     * model.getFinancialItems();
     * 
     * graphView.addChartLabels( graphView.customizeChart(
     * financialItems.getData(), financialItems.getLabels(),
     * financialItems.getLegends(), financialItems.getColors(), null, true, //
     * left YAxis true // invert ), model.getCurrency() );
     * graphView.setTitleAxisY1(""); graphView.setLabelGeneratorAxisY1(
     * CurrencyLabelGenerator.getNewInstance( model.getCurrency() ) );
     *  }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneTree;
    private com.l2fprod.common.swing.JTaskPane jPanelControls;
    // End of variables declaration//GEN-END:variables

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateTotal() {
        am.updateTotal();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {
        am.updateView(person);
        setRoot(person.getId(), null);
    }

    public void saveView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {
        am.saveView(person);
    }

    public void reset() {
        am.reset();
    }

}