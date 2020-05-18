/*
 * PersonTrustDIYStatus.java
 *
 * Created on 17 August 2001, 17:08
 */

package com.argus.financials.etc.db;

import com.argus.financials.api.bean.IPersonTrustDIYStatus;
import com.argus.financials.etc.FPSObject;

public class PersonTrustDIYStatus extends FPSObject implements IPersonTrustDIYStatus {

    private Integer trustStatusCodeId;

    private Integer diyStatusCodeId;

    private Integer companyStatusCodeId;

    private String comment;

    public PersonTrustDIYStatus() {
        super();
    }

    public PersonTrustDIYStatus(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Integer getTrustStatusCodeId() {
        return trustStatusCodeId;
    }

    public void setTrustStatusCodeId(Integer value) {
        trustStatusCodeId = value;
    }

    public Integer getDIYStatusCodeId() {
        return diyStatusCodeId;
    }

    public void setDIYStatusCodeId(Integer value) {
        diyStatusCodeId = value;
    }

    public Integer getCompanyStatusCodeId() {
        return companyStatusCodeId;
    }

    public void setCompanyStatusCodeId(Integer value) {
        companyStatusCodeId = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        comment = value.trim();
    }

}