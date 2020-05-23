/*
 * PersonHealth.java
 *
 * Created on 17 August 2001, 16:25
 */

package au.com.totemsoft.myplanner.etc.db;

import au.com.totemsoft.myplanner.api.bean.IPersonHealth;
import au.com.totemsoft.myplanner.etc.FPSObject;

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