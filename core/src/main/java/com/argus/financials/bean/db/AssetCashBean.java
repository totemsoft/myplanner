/*
 * AssetCashBean.java
 *
 * Created on 8 October 2001, 17:49
 */

package com.argus.financials.bean.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.AssetCash;
import com.argus.financials.bean.Financial;

public final class AssetCashBean extends AssetBean {

    /** Creates new AssetCashBean */
    public AssetCashBean() {
    }

    public AssetCashBean(AssetCash value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return AssetCash.class;
    }

    public Financial getNewFinancial() {
        return new AssetCash();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.ASSET_CASH;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_ASSETCASH;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = super.store(con);

        if (getId() == null || getId().intValue() < 0) {
            // then create link
            linkObjectDao.link(getOwnerId().intValue(),
                    primaryKeyID, getLinkObjectTypeID(), con);

            setId(new Integer(primaryKeyID));

        }

        setModified(false);

        return primaryKeyID;

    }

    public Integer find() throws SQLException {
        return null;
    }

    /***************************************************************************
     * get/set methods
     **************************************************************************/
    protected java.math.BigDecimal getIncome() {
        return ((AssetCash) getFinancial()).getIncome();
    }

    protected void setIncome(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setIncome(value);
    }

    protected java.math.BigDecimal getUpfrontFee() {
        return ((AssetCash) getFinancial()).getUpfrontFee();
    }

    protected void setUpfrontFee(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setUpfrontFee(value);
    }

    protected java.math.BigDecimal getOngoingFee() {
        return ((AssetCash) getFinancial()).getOngoingFee();
    }

    protected void setOngoingFee(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setOngoingFee(value);
    }

    protected java.math.BigDecimal getContributionAnnualAmount() {
        return ((AssetCash) getFinancial()).getContributionAnnualAmount();
    }

    protected void setContributionAnnualAmount(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setContributionAnnualAmount(value);
    }

    protected java.math.BigDecimal getContributionIndexation() {
        return ((AssetCash) getFinancial()).getContributionIndexation();
    }

    protected void setContributionIndexation(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setContributionIndexation(value);
    }

    protected java.util.Date getContributionStartDate() {
        return ((AssetCash) getFinancial()).getContributionStartDate();
    }

    protected void setContributionStartDate(java.util.Date value) {
        ((AssetCash) getFinancial()).setContributionStartDate(value);
    }

    protected java.util.Date getContributionEndDate() {
        return ((AssetCash) getFinancial()).getContributionEndDate();
    }

    protected void setContributionEndDate(java.util.Date value) {
        ((AssetCash) getFinancial()).setContributionEndDate(value);
    }

    protected java.math.BigDecimal getDrawdownAnnualAmount() {
        return ((AssetCash) getFinancial()).getDrawdownAnnualAmount();
    }

    protected void setDrawdownAnnualAmount(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setDrawdownAnnualAmount(value);
    }

    protected java.math.BigDecimal getDrawdownIndexation() {
        return ((AssetCash) getFinancial()).getDrawdownIndexation();
    }

    protected void setDrawdownIndexation(java.math.BigDecimal value) {
        ((AssetCash) getFinancial()).setDrawdownIndexation(value);
    }

    protected java.util.Date getDrawdownStartDate() {
        return ((AssetCash) getFinancial()).getDrawdownStartDate();
    }

    protected void setDrawdownStartDate(java.util.Date value) {
        ((AssetCash) getFinancial()).setDrawdownStartDate(value);
    }

    protected java.util.Date getDrawdownEndDate() {
        return ((AssetCash) getFinancial()).getDrawdownEndDate();
    }

    protected void setDrawdownEndDate(java.util.Date value) {
        ((AssetCash) getFinancial()).setDrawdownEndDate(value);
    }

    protected boolean isReinvest() {
        return ((AssetCash) getFinancial()).isReinvest();
    }

    protected void setReinvest(boolean value) {
        ((AssetCash) getFinancial()).setReinvest(value);
    }

}
