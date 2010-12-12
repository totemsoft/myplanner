/*
 * ClientView.java
 *
 * Created on 25 January 2002, 14:33
 */

package com.argus.financials.ui;

import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.code.Advisers;
import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.Contact;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.SwingUtil;

public final class ClientView extends PersonView2 {

    protected static int LAST = PersonView2.LAST;

    public static final int GOALS_INTERESTS = ++LAST;

    private static ClientView view;

    /** Creates new ClientView */
    public ClientView() {
        initComponents();
    }

    public static boolean exists() {
        return view != null;
    }

    public static ClientView getClientView() {
        if (view == null)
            view = new ClientView();
        return view;
    }

    public String getViewCaption() {
        return "Client Details";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {
        jPanelAdvisor.setVisible(true);
        jGoalsAndInterests = new GoalsInterestsView();
        getJTabbedPane().addTab("Goals and Interests", jGoalsAndInterests);

    }

    /*
     * // has to be overriden in derived classes protected boolean isModified() {
     * try { return
     * RmiParams.getInstance().getClientPerson().isModifiedRemote(); } catch (
     * com.argus.financials.service.ServiceException e ) { e.printStackTrace( System.err ); return
     * false; } }
     */

    // Variables declaration - do not modify
    private GoalsInterestsView jGoalsAndInterests;

    // End of variables declaration

    public void display(Integer clientPersonID,
            java.awt.event.FocusListener[] listeners, boolean inFrame) {

        if (clientPersonID == null)
            return;

        if (!exists())
            getClientView();

        if (!clientPersonID.equals(view.getPrimaryKey())) {
            try {
                view.updateView();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }
        }

        // add/show view
        if (inFrame)
            // Component comp, final java.awt.event.FocusListener [] listeners,
            // String title, String iconImage, boolean center, boolean
            // resizable, boolean visible ) {
            SwingUtil.add2Frame(view, listeners, view.getViewCaption(),
                    ViewSettings.getInstance().getViewImage(
                            view.getClass().getName()), true, true, true);
        else
            ; // to JApplet

    }

    public void updateView() throws com.argus.financials.service.ServiceException {

        ClientService person = (ClientService) getPerson();
        if (person == null)
            return;

        updateView(person);
        jGoalsAndInterests.updateView(person);

        jComboBoxAdviser.setSelectedItem(new Advisers().findByPrimaryKey(person
                .getOwnerPrimaryKey()));

        jCheckBoxClientActive.setSelected(person.isActive());

    }

    public void saveView() throws com.argus.financials.service.ServiceException,
            InvalidCodeException {

        ClientService person = (ClientService) getPerson();
        if (person == null)
            return;

        saveView(person);
        jGoalsAndInterests.saveView(person);

        Object obj = jComboBoxAdviser.getSelectedItem();
        person.setOwnerPrimaryKey(((Contact) obj).getPrimaryKeyID());

        person.setActive(jCheckBoxClientActive.isSelected());

        // save changes to database
        person.storePerson();

        // refresh data after store()
        updateView();

        // HACK code -- update user in mainframe
        FinancialPlannerApp.getInstance().updateClient();

    }

    public void clearView() {
        super.clearView();

    }

    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.CLIENT_PERSON);
    }

    protected PersonService getPerson() throws com.argus.financials.service.ServiceException {
        return ServiceLocator.getInstance().getClientPerson();
    }

    protected String getDefaultReport() {
        return WordSettings.getInstance().getClientDetailsReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws java.io.IOException {

        ReportFields reportFields = ReportFields.getInstance();
        reportFields.initialize(person);

        return reportFields;

    }

    protected void updateDOB() {

        jGoalsAndInterests.setDOB(com.argus.util.DateTimeUtils
                .getSqlDate(jTextFieldDateOfBirth.getText()));

    }

}
