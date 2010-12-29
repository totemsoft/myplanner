/*
 * PersonName.java
 *
 * Created on 23 July 2000, 11:53
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.code.TitleCode;
import com.argus.util.DateTimeUtils;

public class PersonName implements java.io.Serializable {

    // used in search person
    public static final String SURNAME = "FamilyName";

    public static final String FIRST_NAME = "FirstName";

    public static final String DATE_OF_BIRTH = "DateOfBirth";

    private boolean modified = false;

    private Integer sexCodeID;

    private Integer titleCodeID;

    private Integer maritalCodeID;

    private String familyName;

    private String firstName;

    private String otherGivenNames;

    private String preferredName;

    private java.util.Date dob;

    /** Creates new PersonName */
    public PersonName() {
        clear();
    }

    public String toString() {
        return getShortName();
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean value) {
        modified = value;
    }

    /***************************************************************************
     * helper methods
     */
    protected void clear() {
        sexCodeID = null;
        titleCodeID = null;
        maritalCodeID = null;
        familyName = "";
        firstName = "";
        otherGivenNames = "";
        preferredName = "";
        dob = null;
    }

    public void assign(PersonName value) {
        sexCodeID = value.sexCodeID;
        titleCodeID = value.titleCodeID;
        maritalCodeID = value.maritalCodeID;
        familyName = value.familyName;
        firstName = value.firstName;
        otherGivenNames = value.otherGivenNames;
        preferredName = value.preferredName;
        dob = value.dob;

        modified = true;
    }

    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    /**
     * get methods
     */
    public Integer getSexCodeID() {
        return sexCodeID;
    }

    public String getSexCode() {
        return new SexCode().getCodeDescription(sexCodeID);
    }

    public Integer getTitleCodeID() {
        return titleCodeID;
    }

    public String getTitleCode() {
        return new TitleCode().getCodeDescription(titleCodeID);
    }

    public Integer getMaritalCodeID() {
        return maritalCodeID;
    }

    public String getMaritalCode() {
        return new MaritalCode().getCodeDescription(maritalCodeID);
    }

    public boolean isMarried() {
        return MaritalCode.MARRIED.equals(maritalCodeID)
                || MaritalCode.DEFACTO.equals(maritalCodeID)
                || MaritalCode.PARTNERED.equals(maritalCodeID)
                || MaritalCode.SEPARATED_HEALTH.equals(maritalCodeID);
    }

    public String getSurname() {
        if (familyName == null)
            familyName = "";
        return familyName;
    }

    public String getFirstName() {
        if (firstName == null)
            firstName = "";
        return firstName;
    }

    public String getOtherGivenNames() {
        if (otherGivenNames == null)
            otherGivenNames = "";
        return otherGivenNames;
    }

    public String getPreferredName() {
        if (preferredName == null)
            preferredName = "";
        return preferredName;
    }

    public java.util.Date getDateOfBirth() {
        return dob;
    }

    public Double getAge() {
        return DateTimeUtils.getAgeDouble(dob);
    }

    public String getShortName() {
        return getSurname() + ", " + getFirstName();
    }

    public String getFullName() {
        return getTitleCode() + " " + firstName + " " + otherGivenNames + " " + familyName;
    }

    /**
     * set methods
     */
    public void setSexCodeID(Integer value) {
        if (equals(sexCodeID, value))
            return;

        sexCodeID = value;
        modified = true;
    }

    public void setTitleCodeID(Integer value) {
        if (equals(titleCodeID, value))
            return;

        titleCodeID = value;
        modified = true;
    }

    public void setMaritalCodeID(Integer value) {
        if (equals(maritalCodeID, value))
            return;

        maritalCodeID = value;
        modified = true;
    }

    public void setSurname(String value) {
        if (equals(familyName, value))
            return;

        familyName = value;
        modified = true;
    }

    public void setFirstName(String value) {
        if (equals(firstName, value))
            return;

        firstName = value;
        modified = true;
    }

    public void setOtherGivenNames(String value) {
        if (equals(otherGivenNames, value))
            return;

        otherGivenNames = value;
        modified = true;
    }

    public void setPreferredName(String value) {
        if (equals(preferredName, value))
            return;

        preferredName = value;
        modified = true;
    }

    public void setDateOfBirth(java.util.Date value) {
        if (equals(dob, value))
            return;

        dob = value;
        modified = true;
    }

}
