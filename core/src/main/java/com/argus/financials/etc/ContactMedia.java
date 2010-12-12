/*
 * ContactMedia.java
 *
 * Created on November 3, 2001, 10:39 AM
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

/**
 * CREATE TABLE ContactMedia ( ContactMediaID int NOT NULL, ContactMediaCodeID
 * int NULL, Value1 varchar(16) NULL, -- country/area code, email name Value2
 * varchar(32) NULL, -- number, email host name ContactMediaDesc varchar(50)
 * NULL, DateCreated datetime NOT NULL DEFAULT getDate(), DateModified datetime
 * NULL )
 */

import com.argus.financials.code.ContactMediaCode;

public class ContactMedia extends FPSAssignableObject {

    private Integer contactMediaCodeID;

    private String value1;

    private String value2;

    private String desc;

    /** Creates new ContactMedia */
    public ContactMedia() {
    }

    public ContactMedia(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public String toString() {

        if (contactMediaCodeID == null)
            return "";

        char c = ' ';
        if (contactMediaCodeID.equals(ContactMediaCode.EMAIL)
                || contactMediaCodeID.equals(ContactMediaCode.EMAIL_WORK))
            c = '@';

        if (value1 == null)
            return value2 == null ? "" : value2;
        return value1 + c + value2;

    }

    /**
     * Assignable
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof ContactMedia))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        ContactMedia cm = (ContactMedia) value;

        setContactMediaCodeID(cm.getContactMediaCodeID());
        setValue1(cm.getValue1());
        setValue2(cm.getValue2());
        setDesc(cm.getDesc());

        // has to be last (to set modified)
        setModified(value.isModified());
    }

    /**
     * helper methods
     */
    protected void clear() {
        super.clear();

        contactMediaCodeID = null;
        value1 = null;
        value2 = null;
        desc = null;
    }

    public boolean isClear() {
        if (contactMediaCodeID == null && value1 == null && value2 == null
                && desc == null)
            return true;
        return false;
    }

    /**
     * get/set methods
     */
    public Integer getContactMediaCodeID() {
        return contactMediaCodeID;
    }

    public void setContactMediaCodeID(Integer value) {
        if (value == null && contactMediaCodeID == null)
            return;
        if (value != null && value.equals(contactMediaCodeID))
            return;

        contactMediaCodeID = value;
        setModified(true);
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value) {
        if (value == null && value1 == null)
            return;
        if (value != null && value.equals(value1))
            return;

        value1 = value == null ? value : value.trim();
        setModified(true);
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value) {
        if (value == null && value2 == null)
            return;
        if (value != null && value.equals(value2))
            return;

        value2 = value == null ? value : value.trim();
        setModified(true);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String value) {
        if (value == null && desc == null)
            return;
        if (value != null && value.equals(desc))
            return;

        desc = value == null ? value : value.trim();
        setModified(true);
    }

}
