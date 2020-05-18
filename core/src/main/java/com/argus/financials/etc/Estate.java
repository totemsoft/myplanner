/*
 * Estate.java
 *
 * Created on 21 November 2001, 09:26
 */

package com.argus.financials.etc;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.bean.IFPSAssignableObject;

public class Estate extends FPSAssignableObject {

    //
    private Integer personEstateID;

    private Integer statusCodeID;

    private java.sql.Date dateLastReviewed;

    private Integer expectedChangesCodeID;

    private Integer executorStatusCodeID;

    private Integer attorneyStatusCodeID;

    private Integer prenuptualCodeID;

    private Integer insolvencyRiskCodeID;

    /** Creates new Estate */
    public Estate() {
    }

    public Estate(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable
     */
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Estate))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Estate e = (Estate) value;

        // has to be last (to set modified)
        setModified(value.isModified());
    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

    }

    public boolean isClear() {
        if (true)
            return true;
        return false;
    }

    /**
     * get/set methods
     */
    public boolean isModified() {
        if (super.isModified())
            return true;
        return false;
    }

    public void setModified(boolean value) {
        super.setModified(value);
    }

    public void setId(Integer value) {
        super.setId(value);
    }

    public Integer getStatusCodeID() {
        return statusCodeID;
    }

    public void setStatusCodeID(Integer value) throws InvalidCodeException {
        if (value == null && statusCodeID == null)
            return;
        if (value != null && value.equals(statusCodeID))
            return;

        // new Code().isValidThrow( value );

        statusCodeID = value;
        setModified(true);
    }

    public java.sql.Date getDateLastReviewed() {
        return dateLastReviewed;
    }

    public void setDateLastReviewed(java.sql.Date value) {
        if (value == null && dateLastReviewed == null)
            return;
        if (value != null && value.equals(dateLastReviewed))
            return;

        dateLastReviewed = value;
        setModified(true);
    }

    public Integer getExpectedChangesCodeID() {
        return expectedChangesCodeID;
    }

    public void setExpectedChangesCodeID(Integer value)
            throws InvalidCodeException {
        if (value == null && expectedChangesCodeID == null)
            return;
        if (value != null && value.equals(expectedChangesCodeID))
            return;

        // new Code().isValidThrow( value );

        expectedChangesCodeID = value;
        setModified(true);
    }

    public Integer getExecutorStatusCodeID() {
        return executorStatusCodeID;
    }

    public void setExecutorStatusCodeID(Integer value)
            throws InvalidCodeException {
        if (value == null && executorStatusCodeID == null)
            return;
        if (value != null && value.equals(executorStatusCodeID))
            return;

        // new Code().isValidThrow( value );

        executorStatusCodeID = value;
        setModified(true);
    }

    public Integer getAttorneyStatusCodeID() {
        return attorneyStatusCodeID;
    }

    public void setAttorneyStatusCodeID(Integer value)
            throws InvalidCodeException {
        if (value == null && attorneyStatusCodeID == null)
            return;
        if (value != null && value.equals(attorneyStatusCodeID))
            return;

        // new Code().isValidThrow( value );

        attorneyStatusCodeID = value;
        setModified(true);
    }

    public Integer getPrenuptualCodeID() {
        return prenuptualCodeID;
    }

    public void setPrenuptualCodeID(Integer value) throws InvalidCodeException {
        if (value == null && prenuptualCodeID == null)
            return;
        if (value != null && value.equals(prenuptualCodeID))
            return;

        // new Code().isValidThrow( value );

        prenuptualCodeID = value;
        setModified(true);
    }

    public Integer getInsolvencyRiskCodeID() {
        return insolvencyRiskCodeID;
    }

    public void setInsolvencyRiskCodeID(Integer value)
            throws InvalidCodeException {
        if (value == null && insolvencyRiskCodeID == null)
            return;
        if (value != null && value.equals(insolvencyRiskCodeID))
            return;

        // new Code().isValidThrow( value );

        insolvencyRiskCodeID = value;
        setModified(true);
    }

}
