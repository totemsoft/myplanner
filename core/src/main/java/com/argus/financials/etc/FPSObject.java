package com.argus.financials.etc;

import java.util.Date;

import com.argus.financials.api.bean.IFPSObject;
import com.argus.financials.bean.AbstractBase;

public abstract class FPSObject extends AbstractBase implements IFPSObject {

    static final long serialVersionUID = 1898595574343770602L;

    private Integer ownerId;

    private Date dateCreated;

    private Date dateModified;

    protected FPSObject() {
        super();
    }

    protected FPSObject(Integer ownerId) {
        if (ownerId != null) {
            if (ownerId.intValue() <= 0)
                throw new IllegalArgumentException("Bad argument "
                        + ownerId + ". " + getClass().getName());
            this.ownerId = ownerId;
        }
    }

    /***************************************************************************
     * helper methods
     */
    public void clear() {
        ownerId = null;
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
            setDateModified(new Date());
            notifyChangeListeners();
        }
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer value) {
        if (equals(ownerId, value))
            return;

        ownerId = value;
        // setModified( true );
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date value) {
        dateCreated = value;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date value) {
        dateModified = value;
    }

}