/*
 * PersonName.java
 *
 * Created on 23 July 2000, 11:53
 */

package com.argus.financials.api.bean;

import java.util.Date;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.util.DateTimeUtils;

public class PersonName {

    protected boolean modified = false;

    private ISexCode sex;

    private ITitleCode title;

    private IMaritalCode marital;

    private String surname;

    private String firstname;

    private String otherNames;

    private String preferredName;

    private Date dateOfBirth;

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
    public void clear() {
        sex = null;
        title = null;
        marital = null;
        surname = "";
        firstname = "";
        otherNames = "";
        preferredName = "";
        dateOfBirth = null;
        modified = true;
    }

    public void assign(PersonName value) {
        sex = value.sex;
        title = value.title;
        marital = value.marital;
        surname = value.surname;
        firstname = value.firstname;
        otherNames = value.otherNames;
        preferredName = value.preferredName;
        dateOfBirth = value.dateOfBirth;
        modified = true;
    }

    /**
     * get methods
     */
    public ISexCode getSex() {
        return sex;
    }

    public Integer getSexCodeID() {
        return sex == null ? null : sex.getId();
    }

    public String getSexCode() {
        return sex == null ? null : sex.getDescription();
    }

    public ITitleCode getTitle() {
        return title;
    }

    public Integer getTitleCodeID() {
        return title == null ? null : title.getId();
    }

    public String getTitleCode() {
        return title == null ? null : title.getDescription();
    }

    public IMaritalCode getMarital() {
        return marital;
    }

    public Integer getMaritalCodeID() {
        return marital == null ? null : marital.getId();
    }

    public String getMaritalCode() {
        return marital == null ? null : marital.getDescription();
    }

    public boolean isMarried() {
        return IMaritalCode.isMarried(marital == null ? null : marital.getId());
    }

    public String getSurname() {
        if (surname == null)
            surname = "";
        return surname;
    }

    public String getFirstname() {
        if (firstname == null)
            firstname = "";
        return firstname;
    }

    public String getOtherNames() {
        if (otherNames == null)
            otherNames = "";
        return otherNames;
    }

    public String getPreferredName() {
        if (preferredName == null)
            preferredName = "";
        return preferredName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Number getAge() {
        return DateTimeUtils.getAgeDouble(dateOfBirth);
    }

    public String getShortName() {
        return getSurname() + ", " + getFirstname();
    }

    public String getFullName() {
        return getTitleCode() + " " + firstname + " " + otherNames + " " + surname;
    }

    /**
     * set methods
     */
    public void setSex(ISexCode value) {
        sex = value;
        modified = true;
    }

    public void setTitle(ITitleCode value) {
        title = value;
        modified = true;
    }

    public void setMarital(IMaritalCode value) {
        marital = value;
        modified = true;
    }

    public void setSurname(String value) {
        surname = value;
        modified = true;
    }

    public void setFirstname(String value) {
        firstname = value;
        modified = true;
    }

    public void setOtherNames(String value) {
        otherNames = value;
        modified = true;
    }

    public void setPreferredName(String value) {
        preferredName = value;
        modified = true;
    }

    public void setDateOfBirth(Date value) {
        dateOfBirth = value;
        modified = true;
    }

}