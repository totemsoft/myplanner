/*
 * StrategyCreator2.java
 *
 * Created on July 2, 2002, 4:37 PM
 */

package com.argus.financials.ui.strategy;

/**
 * 
 * @author valeri chibaev
 */

import java.awt.Cursor;

import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.strategy.StrategyGroup;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.projection.StrategyCalc;

public class StrategyCreatorView2 extends BaseView implements
        com.argus.financials.swing.IReset {

    private static StrategyCreatorView2 view;

    private java.awt.event.FocusListener[] listeners;

    private java.awt.CardLayout cardLayout;

    // set of views (cards)
    private DataCollectionRestructureView dataCollectionRestructureView;

    /** Creates new form StrategyCreator2 */
    public StrategyCreatorView2() {
        initComponents();
    }

    public static boolean exists() {
        return view != null;
    }

    public static StrategyCreatorView2 getInstance() {
        if (view == null)
            view = new StrategyCreatorView2();
        return view;
    }

    public String getDefaultTitle() {
        return "Strategy Creator";
    }

    private void initComponents() {

        // setPreferredSize(new java.awt.Dimension(900, 500));
        jButtonSave.setText("Save Strategy");
        jButtonDelete.setText("Delete Strategy");

        jPanelPrevNext.setVisible(false);
        jPanelDetails.setLayout(new java.awt.CardLayout());
        cardLayout = (java.awt.CardLayout) jPanelDetails.getLayout();

        dataCollectionRestructureView = new DataCollectionRestructureView();
        jPanelDetails.add(dataCollectionRestructureView,
                dataCollectionRestructureView.getClass().getName());

        jPanelControls
                .add(dataCollectionRestructureView.getPanelImplRollback());

        updateControls();

    }

    public static void display(java.awt.event.FocusListener[] listeners) {

        boolean exists = exists();
        StrategyCreatorView2 view = getInstance();

        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            view.reset();
            view.updateView(ServiceLocator.getInstance().getClientPerson());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        } finally {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        // if ( !exists ) {
        SwingUtil.add2Frame(view, listeners, view.getDefaultTitle(),
                ViewSettings.getInstance().getViewImage(
                        view.getClass().getName()), true, true, false);
        view.listeners = listeners;
        // }

        SwingUtil.setVisible(view, true);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void doNext(java.awt.event.ActionEvent evt) {
        cardLayout.next(jPanelDetails);
        updateControls();
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        cardLayout.previous(jPanelDetails);
        updateControls();
    }

    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(null);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    protected void doDelete(java.awt.event.ActionEvent evt) {
        dataCollectionRestructureView.doDeleteStrategy();
    }

    public void doClose(java.awt.event.ActionEvent evt) {
        super.doClose(evt);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateControls() {
        jButtonPrevious.setEnabled(!dataCollectionRestructureView.isVisible());
        jButtonNext.setEnabled(false);
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public void reset() {
        dataCollectionRestructureView.reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView(PersonService person) throws Exception {

        if (person == null)
            person = ServiceLocator.getInstance().getClientPerson();

        dataCollectionRestructureView.updateView(person);

    }

    public void saveView(PersonService person) throws Exception {

        if (person == null)
            person = ServiceLocator.getInstance().getClientPerson();

        dataCollectionRestructureView.saveView(person);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected String getDefaultReport() {
        return WordSettings.getInstance().getStrategyReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {

        if (person == null)
            return null;

        ReportFields reportFields = ReportFields.getInstance();
        StrategyGroup sg = dataCollectionRestructureView.getStrategy();
        StrategyCalc sc = new StrategyCalc();
        sc.initializeReportData(reportFields, person);

        return reportFields;

    }

}
