/*
 * PersonContactsView2.java
 *
 * Created on 30 November 2001, 10:34
 */

package com.argus.financials.ui;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.table.DefaultTableModel;

import com.argus.financials.api.bean.PersonName;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.code.AddressCode;
import com.argus.financials.code.BaseCode;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.code.RelationshipFinanceCode;
import com.argus.financials.domain.hibernate.Occupation;
import com.argus.financials.etc.AddressDto;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.SwingUtil;
import com.argus.util.ReferenceCode;

public class PersonContactsView extends TableEditView {

    private NameView name;

    private AddressView contactAddressView;

    private ContactMediaView phone;

    private ContactMediaView mobile;

    private ContactMediaView fax;

    private ContactMediaView email;

    private static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        PersonContactsView.clientService = clientService;
    }

    /** Creates new PersonContactsView2 */
    public PersonContactsView() {
        // "Full Name", "Occupation"
        COLUMN_NAMES = Contact.COLUMN_NAMES;

        name = new NameView(false);
        contactAddressView = new AddressView(AddressCode.RESIDENTIAL);

        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {
        jPanelPersonNameOccupation = new javax.swing.JPanel();
        jPanelOccupation = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxContactCode = new javax.swing.JComboBox(
                new RelationshipFinanceCode().getCodes().toArray());
        jPanelDetailsLeft = new javax.swing.JPanel();
        jPanelDetailsRight = new javax.swing.JPanel();
        jPanelPhoneFax = new javax.swing.JPanel();

        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new Object[][] {}, COLUMN_NAMES) {
            boolean[] canEdit = new boolean[COLUMN_NAMES.length];

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTable.setModel(model);

        jPanelDetails.setLayout(new java.awt.GridLayout(1, 2));
        // jPanelDetails.setPreferredSize(new java.awt.Dimension(10, 300));

        jPanelDetailsLeft.setLayout(new javax.swing.BoxLayout(
                jPanelDetailsLeft, javax.swing.BoxLayout.Y_AXIS));

        jPanelPersonNameOccupation.setLayout(new javax.swing.BoxLayout(
                jPanelPersonNameOccupation, javax.swing.BoxLayout.Y_AXIS));
        jPanelPersonNameOccupation
                .setBorder(new javax.swing.border.TitledBorder("Name"));
        jPanelPersonNameOccupation.add(name);

        jPanelOccupation.setLayout(new java.awt.GridBagLayout());
        jPanelOccupation.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        java.awt.GridBagConstraints gridBagConstraints1;

        jLabel9.setText("Contact Type");
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelOccupation.add(jLabel9, gridBagConstraints1);

        // jComboBoxContactCode.setPreferredSize(new java.awt.Dimension(150,
        // 20));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0;
        jPanelOccupation.add(jComboBoxContactCode, gridBagConstraints1);

        jPanelPersonNameOccupation.add(jPanelOccupation);

        jPanelDetailsLeft.add(jPanelPersonNameOccupation);

        jPanelPhoneFax.setBorder(new javax.swing.border.TitledBorder(
                "Phone, Fax, E-Mail"));

        phone = new ContactMediaView(ContactMediaCode.PHONE, jPanelPhoneFax, 0);
        phone.setDescription("Phone");
        mobile = new ContactMediaView(ContactMediaCode.MOBILE, jPanelPhoneFax,
                1);
        mobile.setDescription("Mobile");
        fax = new ContactMediaView(ContactMediaCode.FAX, jPanelPhoneFax, 2);
        fax.setDescription("Fax");
        email = new ContactMediaView(ContactMediaCode.EMAIL, jPanelPhoneFax, 3);
        email.setDescription("E-Mail");

        jPanelDetailsLeft.add(jPanelPhoneFax);

        jPanelDetails.add(jPanelDetailsLeft);

        jPanelDetailsRight.setLayout(new javax.swing.BoxLayout(
                jPanelDetailsRight, javax.swing.BoxLayout.Y_AXIS));

        contactAddressView.setBorder(new javax.swing.border.TitledBorder(
                "Address"));
        jPanelDetailsRight.add(contactAddressView);

        jPanelDetails.add(jPanelDetailsRight);
        jPanelDetails.setVisible(false);

        tpgControls.setTitle("Contacts");
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel jPanelPersonNameOccupation;
    private javax.swing.JPanel jPanelOccupation;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox jComboBoxContactCode;
    private javax.swing.JPanel jPanelDetailsLeft;
    private javax.swing.JPanel jPanelDetailsRight;
    private javax.swing.JPanel jPanelPhoneFax;
    // End of variables declaration

    protected Object[][] getRowData() {

        if (details == null)
            return new Object[][] {};

        try {
            Set set = new TreeSet(new Comparator() {
                public int compare(Object o1, Object o2) {
                    Map.Entry e1 = (Map.Entry) o1;
                    Map.Entry e2 = (Map.Entry) o2;

                    Contact d1 = (Contact) e1.getValue();
                    Contact d2 = (Contact) e2.getValue();
                    return ((String) d1.getData()[1]).compareTo((String) d2
                            .getData()[1]);
                }
            });

            set.addAll(details.entrySet());

            AddressDto clientAddress = clientService.getResidentialAddress();

            int i = 0;
            // allocate matrix
            Object[][] rowData = new Object[details.size()][];
            for (Iterator iter = set.iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                Contact c = (Contact) entry.getValue();

                if (c == null)
                    continue;

                AddressDto contactAddress = c.getAddress();
                // same as client
                if (clientAddress.getId().equals(
                        contactAddress.getParentAddressId()))
                    clientAddress.addChangeListener(contactAddress);

                rowData[i++] = c.getData();

            }

            if (i == rowData.length)
                return rowData;

            Object[][] rowData2 = new Object[i][];
            System.arraycopy(rowData, 0, rowData2, 0, i);
            return rowData2;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return new Object[][] {};
            // throw new RuntimeException( e.getMessage() );
        }

    }

    /**
     * Viewable interface
     */
    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.PERSON);
    }

    public void updateView(PersonService person) throws com.argus.financials.api.ServiceException {

        details = person.getContacts();

        super.updateView(person);
        if (jTable.getRowCount() == 0) {
            clearView();
        } else {
            jTable.changeSelection(0, 1, false, false);
            display(getSelectedObject());
        }
    }

    public void saveView(PersonService person) throws com.argus.financials.api.ServiceException {

        person.setContacts((TreeMap) details);

    }

    public void clearView() {
        super.clearView();

        name.clearView();
        contactAddressView.clearView();
        jComboBoxContactCode.setSelectedItem(BaseCode.CODE_NONE);

        phone.clearView();
        fax.clearView();
        email.clearView();

    }

    /**
     * 
     */
    protected void add() {
        // super.add();

        // jTable.clearSelection();
        SwingUtil.clear(jPanelDetails);

        jPanelDetails.setVisible(true);

        Contact obj = new Contact();
        Integer objID = new Integer(--newObjectID);

        if (details == null)
            details = new TreeMap();
        details.put(objID, obj);

        Object[] rowData = obj.getData();
        rowData[COLUMN_OBJECT_ID] = objID;
        ((DefaultTableModel) jTable.getModel()).addRow(rowData);
        jTable.changeSelection(jTable.getRowCount() - 1, 1, false, false);

        detailsEnabled(true); // has to be first
        contactAddressView.setDefaultCountry();
    }

    protected void display(Object obj) throws com.argus.financials.api.ServiceException {

        if (obj == null) {
            clearView();
            return;
        }

        Contact c = (Contact) obj;

        name.setObject(c.getName());

        // contactAddressView
        AddressDto contactAddress = c.getAddress();
        contactAddressView.setObject(contactAddress);
        contactAddress.addChangeListener(contactAddressView); // add new
                                                                // selected
        contactAddressView.setEnabled(false);

        Integer id = c.getContactCodeID();
        jComboBoxContactCode.setSelectedItem(new RelationshipFinanceCode()
                .getCode(id));

        // ContactMedia
        phone.setObject(c.getPhone());

        mobile.setObject(c.getMobile());

        fax.setObject(c.getFax());

        email.setObject(c.getEMail());
        jPanelDetails.setVisible(true);

    }

    protected void update(Object obj) {

        if (obj == null) {
            // add();
            return;
        }

        DefaultTableModel tm = (DefaultTableModel) jTable.getModel();

        try {
            save(obj);

            int id = jTable.getSelectedRow();
            Object[] rowData = ((Contact) obj).getData();
            for (int i = 0; i < rowData.length; i++)
                if (i == COLUMN_OBJECT_ID) {
                    if (rowData[i] != null) {
                        tm.setValueAt(rowData[i], id, i);
                    }
                } else {
                    tm.setValueAt(rowData[i], id, i);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void remove(Integer objID) {
        if (objID == null)
            return;

        if (objID.intValue() > 0) {
            if (details.remove(objID) != null)
                ;
            details.put(objID, null); // replace object with null value
            Object[][] o = new Object[5][];

        } else {
            if (details.remove(objID) != null) // not saved to server yet
                ;
        }

        // remove table row
        int selectedRow = jTable.getSelectedRow();
        if (jTable.getRowCount() > 1)
            jTable.changeSelection(jTable.getRowCount() - 1, 1, false, false);
        else
            clearView();
        ((DefaultTableModel) jTable.getModel()).removeRow(selectedRow);

    }

    private void save(Object obj) throws com.argus.financials.api.ServiceException {

        Contact c = (Contact) obj;

        // name
        name.saveView(null);
        c.setName((PersonName) name.getObject());

        // contactAddressView
        contactAddressView.saveView(null);
        c.setAddress((AddressDto) contactAddressView.getObject());

        // occupation
        Occupation o = c.getOccupation();
        if (o == null) {
            o = new Occupation();
            c.setOccupation(o);
        }
        ReferenceCode refCode = (ReferenceCode) jComboBoxContactCode
                .getSelectedItem();
        c.setContactCodeID(refCode.getCodeId());

        // ContactMedia
        phone.saveView(null);
        c.setPhone((ContactMedia) phone.getObject());

        mobile.saveView(null);
        c.setMobile((ContactMedia) mobile.getObject());

        fax.saveView(null);
        c.setFax((ContactMedia) fax.getObject());

        email.saveView(null);
        c.setEMail((ContactMedia) email.getObject());

    }

}
