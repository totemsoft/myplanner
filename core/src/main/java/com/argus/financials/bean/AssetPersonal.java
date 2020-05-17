/*
 * AssetPersonal.java
 *
 * Created on 8 October 2001, 10:54
 */

package com.argus.financials.bean;

import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class AssetPersonal extends Asset {

    static final long serialVersionUID = 4626062607026868373L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.ASSET_PERSONAL;

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new AssetPersonal */
    public AssetPersonal() {
    }

    public AssetPersonal(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Financial getNewFinancial() {
        return new AssetPersonal();
    }

    /**
     * get/set methods
     */
    public void setAmount(java.math.BigDecimal value) {
        if (equals(getAmount(), value))
            return;

        super.setAmount(value);
    }

    public java.math.BigDecimal getPurchaseCost() {
        return super.getPurchaseCost();
    }

    public void setPurchaseCost(java.math.BigDecimal value) {
        super.setPurchaseCost(value);
    }

    public java.math.BigDecimal getReplacementValue() {
        return super.getReplacementValue();
    }

    public void setReplacementValue(java.math.BigDecimal value) {
        super.setReplacementValue(value);
    }

    protected void projectAmount(int year, double inflation) {
        
    }
}
