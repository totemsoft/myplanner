/*
 * AssetSuperannuationBean.java
 *
 * Created on 6 November 2001, 16:05
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.AssetSuperannuation;
import com.argus.financials.bean.Financial;

public class AssetSuperannuationBean extends AssetBean {

    /** Creates new AssetSuperannuationBean */
    public AssetSuperannuationBean() {
    }

    public AssetSuperannuationBean(AssetSuperannuation value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return AssetSuperannuation.class;
    }

    public Financial getNewFinancial() {
        return new AssetSuperannuation();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.ASSET_SUPERANNUATION;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_ASSETSUPERANNUATION;
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

    /**
     * get/set methods
     */
    protected Integer getFundTypeID() {
        return ((AssetSuperannuation) getFinancial()).getFundTypeID();
    }

    protected void setFundTypeID(Integer value) {
        ((AssetSuperannuation) getFinancial()).setFundTypeID(value);
    }

    protected java.math.BigDecimal getUpfrontFee() {
        return ((AssetSuperannuation) getFinancial()).getUpfrontFee();
    }

    protected void setUpfrontFee(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setUpfrontFee(value);
    }

    protected java.math.BigDecimal getOngoingFee() {
        return ((AssetSuperannuation) getFinancial()).getOngoingFee();
    }

    protected void setOngoingFee(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setOngoingFee(value);
    }

    protected java.math.BigDecimal getDeductible() {
        return ((AssetSuperannuation) getFinancial()).getDeductible();
    }

    protected void setDeductible(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setDeductible(value);
    }

    protected java.math.BigDecimal getCapitalGrowth() {
        return ((AssetSuperannuation) getFinancial()).getCapitalGrowth();
    }

    protected void setCapitalGrowth(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setCapitalGrowth(value);
    }

    protected java.math.BigDecimal getUnitsSharesPrice() {
        return ((AssetSuperannuation) getFinancial()).getUnitsSharesPrice();
    }

    protected void setUnitsSharesPrice(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setUnitsSharesPrice(value);
    }

    protected java.util.Date getPriceDate() {
        return ((AssetSuperannuation) getFinancial()).getPriceDate();
    }

    protected void setPriceDate(java.util.Date value) {
        ((AssetSuperannuation) getFinancial()).setPriceDate(value);
    }

    protected java.math.BigDecimal getAnnualAmount() {
        return ((AssetSuperannuation) getFinancial()).getAnnualAmount();
    }

    protected void setAnnualAmount(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setAnnualAmount(value);
    }

    protected java.math.BigDecimal getContributionAnnualAmount() {
        return ((AssetSuperannuation) getFinancial())
                .getContributionAnnualAmount();
    }

    protected void setContributionAnnualAmount(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial())
                .setContributionAnnualAmount(value);
    }

    protected java.math.BigDecimal getContributionIndexation() {
        return ((AssetSuperannuation) getFinancial())
                .getContributionIndexation();
    }

    protected void setContributionIndexation(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setContributionIndexation(value);
    }

    protected java.util.Date getContributionStartDate() {
        return ((AssetSuperannuation) getFinancial())
                .getContributionStartDate();
    }

    protected void setContributionStartDate(java.util.Date value) {
        ((AssetSuperannuation) getFinancial()).setContributionStartDate(value);
    }

    protected java.util.Date getContributionEndDate() {
        return ((AssetSuperannuation) getFinancial()).getContributionEndDate();
    }

    protected void setContributionEndDate(java.util.Date value) {
        ((AssetSuperannuation) getFinancial()).setContributionEndDate(value);
    }

    protected java.math.BigDecimal getDrawdownAnnualAmount() {
        return ((AssetSuperannuation) getFinancial()).getDrawdownAnnualAmount();
    }

    protected void setDrawdownAnnualAmount(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setDrawdownAnnualAmount(value);
    }

    protected java.math.BigDecimal getDrawdownIndexation() {
        return ((AssetSuperannuation) getFinancial()).getDrawdownIndexation();
    }

    protected void setDrawdownIndexation(java.math.BigDecimal value) {
        ((AssetSuperannuation) getFinancial()).setDrawdownIndexation(value);
    }

    protected java.util.Date getDrawdownStartDate() {
        return ((AssetSuperannuation) getFinancial()).getDrawdownStartDate();
    }

    protected void setDrawdownStartDate(java.util.Date value) {
        ((AssetSuperannuation) getFinancial()).setDrawdownStartDate(value);
    }

    protected java.util.Date getDrawdownEndDate() {
        return ((AssetSuperannuation) getFinancial()).getDrawdownEndDate();
    }

    protected void setDrawdownEndDate(java.util.Date value) {
        ((AssetSuperannuation) getFinancial()).setDrawdownEndDate(value);
    }

}
