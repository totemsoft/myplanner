package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.bean.AbstractBase;

public abstract class FPSObject extends AbstractBase {

    static final long serialVersionUID = 1898595574343770602L;

    private Integer ownerPrimaryKeyID;

    private java.util.Date dateCreated;

    private java.util.Date dateModified;

    protected FPSObject() {
        super();
    }

    protected FPSObject(Integer ownerPrimaryKeyID) {
        if (ownerPrimaryKeyID != null) {
            if (ownerPrimaryKeyID.intValue() <= 0)
                throw new IllegalArgumentException("Bad argument "
                        + ownerPrimaryKeyID + ". " + getClass().getName());
            this.ownerPrimaryKeyID = ownerPrimaryKeyID;
        }
    }

    /***************************************************************************
     * helper methods
     */
    protected void clear() {
        ownerPrimaryKeyID = null;
        dateCreated = null;
        dateModified = null;

        super.clear();
    }

    /***************************************************************************
     * get/set methods
     */
    public boolean isModified() {
        return super.isModified();
    }

    public void setModified(boolean value) {
        super.setModified(value);

        if (value) {
            setDateModified(new java.util.Date());
            notifyChangeListeners();
        }
    }

    public Integer getOwnerPrimaryKeyID() {
        return ownerPrimaryKeyID;
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        if (equals(ownerPrimaryKeyID, value))
            return;

        ownerPrimaryKeyID = value;
        // setModified( true );
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date value) {
        dateCreated = value;
    }

    public java.util.Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(java.util.Date value) {
        dateModified = value;
    }

}