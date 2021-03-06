/*
 * PersonView2.java
 *
 * Created on 25 January 2002, 14:19
 */

package au.com.totemsoft.myplanner.swing;

import au.com.totemsoft.myplanner.service.PersonService;

public abstract class PersonView2 extends PersonView {

    protected static int LAST = PersonView.LAST;

    public static final int PROFESSIONAL_CONTACTS = ++LAST;

    public static final int DEPENDENTS = ++LAST;

    /** Creates new PersonView2 */
    public PersonView2() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {
        jContacts = new PersonContactsView();
        jDependents = new PersonDependentsView();

        getJTabbedPane().addTab("Dependents", jDependents);
        getJTabbedPane().addTab("Professional Contacts", jContacts);
    }

    // Variables declaration - do not modify
    private PersonContactsView jContacts;

    private PersonDependentsView jDependents;

    // End of variables declaration

    public void updateView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {

        super.updateView(person);

        jContacts.updateView(person);
        jDependents.updateView(person);

    }

    public void saveView(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {

        super.saveView(person);

        jContacts.saveView(person);
        jDependents.saveView(person);

    }

}
