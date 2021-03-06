/*
 * FinancialView2.java
 *
 * Created on July 23, 2002, 10:59 AM
 */

package au.com.totemsoft.myplanner.swing.financials;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.Cursor;

import javax.swing.BoxLayout;

import au.com.totemsoft.myplanner.config.ViewSettings;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.swing.BaseView;
import au.com.totemsoft.myplanner.swing.data.FinancialData;
import au.com.totemsoft.myplanner.swing.strategy.FinancialView;
import au.com.totemsoft.swing.IReset;
import au.com.totemsoft.swing.SwingUtil;

public class FinancialView2 extends BaseView implements IReset {

    private static FinancialView2 view;

    private java.awt.event.FocusListener[] listeners;

    private FinancialView financialView;

    public static boolean exists() {
        return view != null;
    }

    public static FinancialView2 getInstance() {
        if (view == null)
            view = new FinancialView2();
        return view;
    }

    public String getDefaultTitle() {
        return "Financials";
    }

    /** Creates new FinancialView2 */
    public FinancialView2() {
        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new java.awt.Dimension(650, 420));
        setMinimumSize(this.getPreferredSize());

        jPanelDetails.setLayout(new BoxLayout(jPanelDetails, BoxLayout.Y_AXIS));

        financialView = new FinancialView();
        jPanelDetails.add(financialView);

        jPanelPrevNext.setVisible(false);
        jButtonDelete.setVisible(false);

        updateControls();

    }

    public static void display(PersonService personId,
            java.awt.event.FocusListener[] listeners) {

        boolean exists = exists();
        FinancialView2 view = getInstance();

        view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            view.updateView(personId);
        } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
            e.printStackTrace(System.err);
            return;
        } finally {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        if (!exists) {
            SwingUtil.add2Frame(view, listeners, view.getDefaultTitle(),
                    ViewSettings.getInstance().getViewImage(
                            view.getClass().getName()), true, true, false);
            view.listeners = listeners;
        }

        SwingUtil.setVisible(view, true);

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public void reset() {
        financialView.reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void doNext(java.awt.event.ActionEvent evt) {

    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {

    }

    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(null);
        } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
        }
    }

    public void doClose(java.awt.event.ActionEvent evt) {
        super.doClose(evt);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateControls() {

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {

        if (person == null)
            person = clientService;

        financialView.updateView(person);

    }

    public void saveView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {

        if (person == null)
            person = clientService;

        financialView.saveView(person);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected String getDefaultReport() {
        return au.com.totemsoft.myplanner.config.WordSettings.getInstance()
                .getFinancialReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {

        // new reporting ( using pre-defined map(field,value) )
        ReportFields reportFields = ReportFields.getInstance();
        new FinancialData(person.findFinancials(), null)
            .initializeReportData(reportFields, person);

        return reportFields;

    }

}
