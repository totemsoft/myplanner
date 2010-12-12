/*
 * Occupation.java
 *
 * Created on 30 August 2001, 13:35
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.EmploymentStatusCode;
import com.argus.financials.code.IndustryCode;
import com.argus.financials.code.OccupationCode;

public class Occupation extends FPSAssignableObject {

    private String jobDescription;

    private Integer employmentStatusCodeID;

    private Integer industryCodeID;

    private Integer occupationCodeID;

    public Occupation() {
    }

    public Occupation(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable methods
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Occupation))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Occupation o = (Occupation) value;

        jobDescription = o.jobDescription;
        employmentStatusCodeID = o.employmentStatusCodeID;
        industryCodeID = o.industryCodeID;
        occupationCodeID = o.occupationCodeID;

        // has to be last (to set modified)
        setModified(true);
    }

    /**
     * helper methods
     */
    protected void clear() {
        super.clear();

        jobDescription = null;
        employmentStatusCodeID = null;
        industryCodeID = null;
        occupationCodeID = null;

    }

    /**
     * get/set methods
     */
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String value) {
        if (equals(jobDescription, value))
            return;

        jobDescription = value;
        setModified(true);
    }

    public Integer getEmploymentStatusCodeID() {
        return employmentStatusCodeID;
    }

    public void setEmploymentStatusCodeID(Integer value) {
        if (equals(employmentStatusCodeID, value))
            return;

        employmentStatusCodeID = value;
        setModified(true);
    }

    public String getEmploymentStatus() {
        return new EmploymentStatusCode()
                .getCodeDescription(employmentStatusCodeID);
    }

    public Integer getIndustryCodeID() {
        return industryCodeID;
    }

    public void setIndustryCodeID(Integer value) {
        if (equals(industryCodeID, value))
            return;

        industryCodeID = value;
        setModified(true);
    }

    public String getIndustry() {
        return new IndustryCode().getCodeDescription(industryCodeID);
    }

    public Integer getOccupationCodeID() {
        return occupationCodeID;
    }

    public void setOccupationCodeID(Integer value) {
        if (equals(occupationCodeID, value))
            return;

        occupationCodeID = value;
        setModified(true);
    }

    public String getOccupation() {
        return new OccupationCode().getCodeDescription(occupationCodeID);
    }

}
