/*
 * Occupation.java
 *
 * Created on 30 August 2001, 13:35
 */

package com.argus.financials.domain.hibernate;

import com.argus.financials.api.bean.IFPSAssignableObject;
import com.argus.financials.api.bean.IOccupation;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.EmploymentStatusCode;
import com.argus.financials.code.IndustryCode;
import com.argus.financials.code.OccupationCode;
import com.argus.financials.etc.FPSAssignableObject;

public class Occupation extends FPSAssignableObject implements IOccupation {

    private String jobDescription;

    private Integer employmentStatusCodeId;

    private Integer industryCodeId;

    private Integer occupationCodeId;

    public Occupation() {
    }

    public Occupation(int ownerPrimaryKeyId) {
        super(ownerPrimaryKeyId);
    }

    /**
     * Assignable methods
     */
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Occupation))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Occupation o = (Occupation) value;

        jobDescription = o.jobDescription;
        employmentStatusCodeId = o.employmentStatusCodeId;
        industryCodeId = o.industryCodeId;
        occupationCodeId = o.occupationCodeId;

        // has to be last (to set modified)
        setModified(true);
    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        jobDescription = null;
        employmentStatusCodeId = null;
        industryCodeId = null;
        occupationCodeId = null;

    }

    /**
     * get/set methods
     */
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String value) {
        jobDescription = value;
    }

    public Integer getEmploymentStatusCodeId() {
        return employmentStatusCodeId;
    }

    public void setEmploymentStatusCodeId(Integer value) {
        employmentStatusCodeId = value;
    }

    public String getEmploymentStatus() {
        return new EmploymentStatusCode().getCodeDescription(employmentStatusCodeId);
    }

    public Integer getIndustryCodeId() {
        return industryCodeId;
    }

    public void setIndustryCodeId(Integer value) {
        industryCodeId = value;
    }

    public String getIndustry() {
        return new IndustryCode().getCodeDescription(industryCodeId);
    }

    public Integer getOccupationCodeId() {
        return occupationCodeId;
    }

    public void setOccupationCodeId(Integer value) {
        occupationCodeId = value;
    }

    public String getOccupation() {
        return new OccupationCode().getCodeDescription(occupationCodeId);
    }

}