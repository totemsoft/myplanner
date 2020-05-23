/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.myplanner.swing.strategy;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.bean.Assumptions;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.config.ViewSettings;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.strategy.model.FinancialDataModel;
import au.com.totemsoft.myplanner.swing.IMenuCommand;
import au.com.totemsoft.myplanner.swing.assetallocation.CurrentAssetAllocationView;
import au.com.totemsoft.myplanner.swing.financials.AddFinancialView;
import au.com.totemsoft.myplanner.swing.financials.CashFlowView;
import au.com.totemsoft.myplanner.swing.financials.TaxAnalysisView;
import au.com.totemsoft.myplanner.swing.financials.WealthView;
import au.com.totemsoft.myplanner.swing.projection.SnapEntryView;
import au.com.totemsoft.myplanner.table.swing.JTreeTable;
import au.com.totemsoft.swing.SwingUtil;
import au.com.totemsoft.util.ReferenceCode;

/*
 * Created on 7/06/2006
 * 
 */

public class FinancialViewActionMap
    extends ActionMap
    implements IMenuCommand
{

    private Component parent;
    private JTreeTable treeTable;

    private PersonService person;

    // Model for the JTreeTable.
    protected FinancialDataModel model;

    private static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        FinancialViewActionMap.clientService = clientService;
    }

    FinancialViewActionMap(Component parent, JTreeTable treeTable)
    {
        this.parent = parent;
        this.treeTable = treeTable;
        
        initActions();

        treeTable.getTree().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    updateFinancial(getCurrentCollectionNode());
                }
            }
        });

    }

    /***************************************************************************
     * 
     **************************************************************************/
    void setRoot(Integer personId, ReferenceCode root)
        throws ServiceException 
    {
        model = new FinancialDataModel();
        model.setRoot(root);
        treeTable.setModel(model);
        
        // load financial data
        model.reload(personId);
    }
    
    /***************************************************************************
     * 
     **************************************************************************/
    private void initActions() {
        
        put(FINANCIAL_ADD, new AbstractAction(FINANCIAL_ADD.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                addFinancial(getCurrentCollectionNode());
            }
        });
        put(FINANCIAL_EDIT, new AbstractAction(FINANCIAL_EDIT.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                updateFinancial(getCurrentCollectionNode());
            }
        });
        put(FINANCIAL_COPY, new AbstractAction(FINANCIAL_COPY.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                copyFinancial(getCurrentCollectionNode());
            }
        });
        put(FINANCIAL_REMOVE, new AbstractAction(FINANCIAL_REMOVE.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                try {
                    removeFinancial(getCurrentCollectionNode());
                } catch (ServiceException e) {
                    e.printStackTrace(System.err);
                }
            }
        });

        put(WEALTH_ANALYSIS, new AbstractAction(WEALTH_ANALYSIS.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                doWealth();
            }
        });
        put(TAX_ANALYSIS, new AbstractAction(TAX_ANALYSIS.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                doTaxAnalysis();
            }
        });
        put(ASSET_ALLOCATION, new AbstractAction(ASSET_ALLOCATION.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                doAssetAllocation();
            }
        });
        put(CASHFLOW_ANALYSIS, new AbstractAction(CASHFLOW_ANALYSIS.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                doCashFlow();
            }
        });

        put(UPDATE_PRICE, new AbstractAction(UPDATE_PRICE.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                doUpdatePrice();
            }
        });
        put(CURRENT_POSITION_CALC, new AbstractAction(CURRENT_POSITION_CALC.getSecond().toString()) {
            public void actionPerformed(ActionEvent evt) {
                doQuickView();
            }
        });
    }

    /***************************************************************************
     * 
     **************************************************************************/
    void updateView(PersonService person) throws ServiceException {
        this.person = person;
    }

    void saveView(PersonService person) throws ServiceException {
        // if ( currentPerson != person )
        // throw new au.com.totemsoft.myplanner.service.ServiceException( "!!! BUG!!! Wrong PersonService..." );

        // save collection data (can be modified)
        Map financials = model.getDetails();
        person.storeFinancials(financials, person.getFinancialGoal());

    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void addFinancial(FinancialDataModel.Node parentNode) {
        if (parentNode == null || !parentNode.isFinancialGroup())
            return;

        int objectTypeID = ((ReferenceCode) parentNode.getObject()).getId();
        AddFinancialView view = AddFinancialView
                .getAddFinancialView(objectTypeID);
        if (view == null)
            return;

        view.setObject(null);
        view.setOwner(SwingUtilities.getWindowAncestor(parent));
        view.setVisible(true); // modal
        if (view.getResult() != AddFinancialView.OK_OPTION)
            return;

        parentNode.addChild(view.getObject());

    }

    private void copyFinancial(FinancialDataModel.Node node) {
        if (node == null || !node.isFinancial())
            return;

        Financial financial = node.getFinancialObject();
        AddFinancialView view = AddFinancialView.getAddFinancialView(financial
                .getObjectTypeID().intValue());
        if (view == null)
            return;

        try {
            financial = node.copyFinancial(financial);
        } catch (java.lang.CloneNotSupportedException e) {
            e.printStackTrace(System.err);
            return;
        }

        view.setObject(financial);
        view.setOwner(SwingUtilities.getWindowAncestor(parent));
        view.setVisible(true); // modal
        if (view.getResult() != AddFinancialView.OK_OPTION)
            return;

        node.getParent().addChild(financial);

    }

    private void removeFinancial(FinancialDataModel.Node node)
            throws ServiceException {
        if (node == null || !node.isFinancial())
            return;

        Financial financial = node.getFinancialObject();
        if (financial.isGenerated())
            return;

        if (JOptionPane.showConfirmDialog(parent,
                "Do you really want to delete\n" + financial + "???\n",
                "Remove Item", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
            return;

        node.remove();

    }

    protected void updateFinancial(FinancialDataModel.Node node) {
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
        view.setOwner(SwingUtilities.getWindowAncestor(parent));
        view.setVisible(true); // modal
        if (view.getResult() != AddFinancialView.OK_OPTION)
            return;

        node.nodeChanged();

    }

    /***************************************************************************
     * 
     **************************************************************************/
    void updateTotal() {
        model.updateTotal();
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    void reset() {
        cashFlowView = null;
        wealthView = null;
        taxAnalysisView = null;
    }

    transient private CashFlowView cashFlowView;
    private void doCashFlow() {

        // System.out.println( "0 doCashFlow: " + System.currentTimeMillis() );
        try {
            Assumptions a = null;// sgData.getCashFlowAssumptions();
            if (a == null) {
                a = new Assumptions();
                a.update(person);
            }

            // System.out.println( "1 doCashFlow: " + System.currentTimeMillis()
            // );
            // CashFlowData cashFlowData = new CashFlowData(
            // model.getFinancials(), a, ReportFields.CURRENT_PREFIX );
            // *
            if (cashFlowView == null)
                cashFlowView = new CashFlowView(a, ReportFields.CURRENT_PREFIX);
            cashFlowView.updateView(person, model.getFinancials(),
                    ReportFields.CURRENT_PREFIX);

            // System.out.println( "2 doCashFlow: " + System.currentTimeMillis()
            // );
            // */
            SwingUtil.add2Frame(cashFlowView,
                    (java.awt.event.FocusListener[]) null,
                    "Cash Flow Analysis (Current)", ViewSettings.getInstance()
                            .getViewImage(cashFlowView.getClass().getName()),
                    true, true, true);
            // System.out.println( "3 doCashFlow: " + System.currentTimeMillis()
            // );

        } catch (ServiceException e) {
            e.printStackTrace(System.err);
        }

        // System.out.println( "4 doCashFlow (last): " +
        // System.currentTimeMillis() );
    }

    transient private WealthView wealthView;
    private void doWealth() {

        try {
            Assumptions a = null;// sgData.getWealthAssumptions();
            if (a == null) {
                a = new Assumptions();
                a.update(person);
                // sgData.setWealthAssumptions( a );
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

    transient private CurrentAssetAllocationView assetAllocationView;
    private void doAssetAllocation() {

        try {
            if (assetAllocationView == null)
                assetAllocationView = new CurrentAssetAllocationView();
            assetAllocationView.updateView(
                    clientService);

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

    private void doQuickView() {

        try {
            SnapEntryView view = new SnapEntryView();

            // view.updateView( RmiParams.getInstance().getClientPerson() );

            view.updateView(FinancialDataModel.MODEL_NAME);
            view.updateFinancials(model.getDetails());// currentPerson.getFinancials()
                                                        // );

            Window w = SwingUtilities.getWindowAncestor(parent);
            Frame owner = w instanceof Frame ? (Frame) w : null;

            // Frame owner, String title, boolean modal, Component comp, boolean
            // resizable, boolean visible ) {
            SwingUtil.add2Dialog(owner, FinancialDataModel.MODEL_NAME, false,
                    view, true, true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    transient private TaxAnalysisView taxAnalysisView;
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

    private void doUpdatePrice() {
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            model.updatePrice();
        } finally {
            parent.setCursor(null);
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    FinancialDataModel.Node getCurrentCollectionNode() {
        return getSelectedNode();
    }

    private FinancialDataModel.Node getSelectedNode() {
        // Node currently being selected
        return (FinancialDataModel.Node) treeTable.getTree()
                .getLastSelectedPathComponent();
    }

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

}
