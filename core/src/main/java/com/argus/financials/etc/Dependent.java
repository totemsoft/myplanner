/*
 * Dependent.java
 *
 * Created on 22 August 2001, 15:11
 */

package com.argus.financials.etc;

import com.argus.financials.api.bean.IFPSAssignableObject;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.RelationshipCode;
import com.argus.util.DateTimeUtils;

public class Dependent extends Contact {

    public static final String[] COLUMN_NAMES = new String[] { "ID",
            "Full Name", "DOB", "Age", "SupportToAge", "Relationship" };

    protected Integer dobCountryID;

    protected Integer relationshipCodeID;

    protected Integer supportToAge;

    private static Beneficiaries beneficiaries = new Beneficiaries();

    public Dependent() {
    }

    public Dependent(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public static Beneficiaries getBeneficiaries() {
        return beneficiaries;
    }

    /**
     * Assignable methods
     */
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Dependent))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Dependent d = (Dependent) value;

        setDateOfBirth(d.getDateOfBirth());
        setSupportToAge(d.getSupportToAge());
        setDobCountryID(d.getDobCountryID());
        setRelationshipCodeID(d.getRelationshipCodeID());

        // has to be last (to set modified)
        setModified(value.isModified());
    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        dobCountryID = null;
        relationshipCodeID = null;
    }

    public Object[] getData() {

        java.util.Date dob = getDateOfBirth();
        Double age = DateTimeUtils.getAgeDouble(dob);

        return new Object[] {
                getId(),
                name.getFullName(),
                dob == null ? null : DateTimeUtils.asString(dob, null),
                age == null ? null : new Integer(age.intValue()),
                supportToAge,
                new RelationshipCode()
                        .getCodeDescription(getRelationshipCodeID()) };

    }

    /**
     * get/set methods
     */
    public java.util.Date getDateOfBirth() {
        return getName().getDateOfBirth();
    }

    public void setDateOfBirth(java.util.Date value) {
        getName().setDateOfBirth(value);
    }

    public Integer getSupportToAge() {
        return supportToAge;
    }

    public void setSupportToAge(Integer value) {
        if (equals(supportToAge, value))
            return;

        supportToAge = value;
        setModified(true);
    }

    public Integer getDobCountryID() {
        return dobCountryID;
    }

    public void setDobCountryID(Integer value) {
        if (equals(dobCountryID, value))
            return;

        dobCountryID = value;
        setModified(true);
    }

    public Integer getRelationshipCodeID() {
        return relationshipCodeID;
    }

    public void setRelationshipCodeID(Integer value) {
        if (equals(relationshipCodeID, value))
            return;

        relationshipCodeID = value;
        setModified(true);
    }

    public String getRelationshipCode() {
        return new RelationshipCode().getCodeDescription(relationshipCodeID);
    }

}
