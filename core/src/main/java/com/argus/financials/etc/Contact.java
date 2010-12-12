/*
 * Contact.java
 *
 * Created on 7 September 2001, 13:43
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.RelationshipFinanceCode;

public class Contact extends PersonNameAddress {

    public static final String[] COLUMN_NAMES = new String[] { "ID",
            "Full Name", "Phone", "Type" };

    private Integer contactCodeID;

    private ContactMedia phone;

    private ContactMedia mobile;

    private ContactMedia fax;

    private ContactMedia email;

    private ContactMedia phoneWork;

    private ContactMedia mobileWork;

    private ContactMedia faxWork;

    private ContactMedia emailWork;

    protected Occupation occupation;

    /** Creates new Contact */
    public Contact() {
    }

    public Contact(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable methods
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Contact))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Contact c = (Contact) value;

        contactCodeID = c.contactCodeID;

        setOccupation(c.getOccupation());

        setPhone(c.getPhone());
        setMobile(c.getMobile());
        setFax(c.getFax());
        setEMail(c.getEMail());

        // has to be last (to set modified)
        setModified(true);
    }

    /**
     * helper methods
     */
    protected void clear() {

        super.clear();

        contactCodeID = null;
        if (occupation != null)
            occupation.clear();
        if (phone != null)
            phone.clear();
        if (mobile != null)
            mobile.clear();
        if (fax != null)
            fax.clear();
        if (email != null)
            email.clear();

    }

    public Object[] getData() {

        return new Object[] {
                getPrimaryKeyID(),
                name.getFullName(),
                phone == null ? null : phone.toString(),
                contactCodeID == null ? null : new RelationshipFinanceCode()
                        .getCode(contactCodeID).getCodeDesc() };

    }

    /**
     * get/set methods
     */
    public boolean isModified() {
        if (super.isModified())
            return true;
        if (occupation != null && occupation.isModified())
            return true;
        if (phone != null && phone.isModified())
            return true;
        if (fax != null && fax.isModified())
            return true;
        if (email != null && email.isModified())
            return true;
        return false;
    }

    public void setModified(boolean value) {
        super.setModified(value);
        if (occupation != null)
            occupation.setModified(value);
        if (phone != null)
            phone.setModified(value);
        if (fax != null)
            fax.setModified(value);
    }

    public void setPrimaryKeyID(Integer value) {
        super.setPrimaryKeyID(value);

        if (occupation != null)
            occupation.setOwnerPrimaryKeyID(value);
        if (phone != null)
            phone.setOwnerPrimaryKeyID(value);
        if (fax != null)
            fax.setOwnerPrimaryKeyID(value);
    }

    public Integer getContactCodeID() {
        return contactCodeID;
    }

    public void setContactCodeID(Integer value) {
        if (equals(contactCodeID, value))
            return;

        contactCodeID = value;
        setModified(true);
    }

    // public String getContactDesc() {
    // return new ContactCode().getCodeDescription( contactCodeID );
    // }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation value) {
        if (occupation == null) {
            if (value != null)
                occupation = value;
        } else {
            if (value == null)
                occupation.clear();
            else
                occupation.assign(value);
        }
    }

    public ContactMedia getPhone() {
        return phone;
    }

    public void setPhone(ContactMedia value) {
        if (phone == null) {
            if (value != null)
                phone = value;
        } else {
            if (value == null)
                phone.clear();
            else
                phone.assign(value);
        }
    }

    public void setPhoneWork(ContactMedia value) {
        this.phoneWork = value;
    }

    public ContactMedia getPhoneWork() {
        return this.phoneWork;
    }

    public ContactMedia getMobile() {
        return mobile;
    }

    public void setMobile(ContactMedia value) {
        if (mobile == null) {
            if (value != null)
                mobile = value;
        } else {
            if (value == null)
                mobile.clear();
            else
                mobile.assign(value);
        }
    }

    public void setFaxWork(ContactMedia value) {
        this.faxWork = value;
    }

    public ContactMedia getFaxWork() {
        return this.faxWork;
    }

    public void setEmailWork(ContactMedia value) {
        this.emailWork = value;
    }

    public ContactMedia getEmailWork() {
        return this.emailWork;
    }

    public ContactMedia getFax() {
        return fax;
    }

    public void setFax(ContactMedia value) {
        if (fax == null) {
            if (value != null)
                fax = value;
        } else {
            if (value == null)
                fax.clear();
            else
                fax.assign(value);
        }
    }

    public ContactMedia getEMail() {
        return email;
    }

    public void setEMail(ContactMedia value) {
        if (email == null) {
            if (value != null)
                email = value;
        } else {
            if (value == null)
                email.clear();
            else
                email.assign(value);
        }
    }

    public String getContactAsString() {

        String contactAsString = "";

        if (phone != null && phone.getValue2() != null)
            contactAsString += "\nPhone: " + phone.getValue2();
        if (phoneWork != null && phoneWork.getValue2() != null)
            contactAsString += "\nPhone Work: " + phoneWork.getValue2();
        if (fax != null && fax.getValue2() != null)
            contactAsString += "\nFax: " + fax.getValue2();
        if (faxWork != null && faxWork.getValue2() != null)
            contactAsString += "\nFax Work: " + faxWork.getValue2();
        if (email != null && email.getValue2() != null)
            contactAsString += "\nEmail: " + email.getValue2();
        if (emailWork != null && emailWork.getValue2() != null)
            contactAsString += "\nEmail Work: " + emailWork.getValue2();
        if (mobile != null && mobile.getValue2() != null)
            contactAsString += "\nMobile: " + mobile.getValue2();

        return contactAsString;
    }
}
