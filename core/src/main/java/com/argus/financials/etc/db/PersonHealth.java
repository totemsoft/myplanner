/*
 * PersonHealth.java
 *
 * Created on 17 August 2001, 16:25
 */

package com.argus.financials.etc.db;

import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.etc.FPSObject;

public class PersonHealth extends FPSObject implements IPersonHealth {

    private boolean smoker;

    private Integer healthStateCodeId;

    private boolean hospitalCover;

    public PersonHealth() {
        super();
    }

    public PersonHealth(int ownerId) {
        super(ownerId);
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean value) {
        smoker = value;
    }

    public boolean isHospitalCover() {
        return hospitalCover;
    }

    public void setHospitalCover(boolean value) {
        hospitalCover = value;
    }

    public Integer getHealthStateCodeId() {
        return healthStateCodeId;
    }

    public void setHealthStateCodeId(Integer value) {
        healthStateCodeId = value;
    }

}