/*
 * AssetPersonalBean.java
 *
 * Created on 6 November 2001, 16:05
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.bean.AssetPersonal;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.ObjectTypeConstant;

public class AssetPersonalBean extends AssetBean {

    /** Creates new AssetPersonalBean */
    public AssetPersonalBean() {
    }

    public AssetPersonalBean(AssetPersonal value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return AssetPersonal.class;
    }

    public Financial getNewFinancial() {
        return new AssetPersonal();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.ASSET_PERSONAL;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_ASSETINVESTMENT;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = super.store(con);

        if (getPrimaryKeyID() == null || getPrimaryKeyID().intValue() < 0) {
            // then create link
            FPSLinkObject.getInstance().link(getOwnerPrimaryKeyID().intValue(),
                    primaryKeyID, getLinkObjectTypeID(), con);

            setPrimaryKeyID(new Integer(primaryKeyID));

        }

        setModified(false);

        return primaryKeyID;

    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * get/set methods
     */
    public java.math.BigDecimal getPurchaseCost() {
        return ((AssetPersonal) getFinancial()).getPurchaseCost();
    }

    public void setPurchaseCost(java.math.BigDecimal value) {
        ((AssetPersonal) getFinancial()).setPurchaseCost(value);
    }

    public java.math.BigDecimal getReplacementValue() {
        return ((AssetPersonal) getFinancial()).getReplacementValue();
    }

    public void setReplacementValue(java.math.BigDecimal value) {
        ((AssetPersonal) getFinancial()).setReplacementValue(value);
    }

}
