/*
 * AssetInvestmentBean.java
 *
 * Created on 6 November 2001, 16:04
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.bean.AssetInvestment;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.ObjectTypeConstant;

public class AssetInvestmentBean extends AssetBean {

    /** Creates new AssetInvestmentBean */
    public AssetInvestmentBean() {
    }

    public AssetInvestmentBean(AssetInvestment value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return AssetInvestment.class;
    }

    public Financial getNewFinancial() {
        return new AssetInvestment();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.ASSET_INVESTMENT;
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
    protected Integer getInvestmentTypeID() {
        return ((AssetInvestment) getFinancial()).getInvestmentTypeID();
    }

    protected void setInvestmentTypeID(Integer value) {
        ((AssetInvestment) getFinancial()).setInvestmentTypeID(value);
    }

    protected java.math.BigDecimal getFranked() {
        return ((AssetInvestment) getFinancial()).getFranked();
    }

    protected void setFranked(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setFranked(value);
    }

    protected java.math.BigDecimal getTaxFreeDeferred() {
        return ((AssetInvestment) getFinancial()).getTaxFreeDeferred();
    }

    protected void setTaxFreeDeferred(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setTaxFreeDeferred(value);
    }

    protected java.math.BigDecimal getCapitalGrowth() {
        return ((AssetInvestment) getFinancial()).getCapitalGrowth();
    }

    protected void setCapitalGrowth(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setCapitalGrowth(value);
    }

    protected java.math.BigDecimal getIncome() {
        return ((AssetInvestment) getFinancial()).getIncome();
    }

    protected void setIncome(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setIncome(value);
    }

    protected java.math.BigDecimal getUpfrontFee() {
        return ((AssetInvestment) getFinancial()).getUpfrontFee();
    }

    protected void setUpfrontFee(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setUpfrontFee(value);
    }

    protected java.math.BigDecimal getOngoingFee() {
        return ((AssetInvestment) getFinancial()).getOngoingFee();
    }

    protected void setOngoingFee(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setOngoingFee(value);
    }

    protected java.math.BigDecimal getPurchaseCost() {
        return ((AssetInvestment) getFinancial()).getPurchaseCost();
    }

    protected void setPurchaseCost(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setPurchaseCost(value);
    }

    protected java.math.BigDecimal getUnitsSharesPrice() {
        return ((AssetInvestment) getFinancial()).getUnitsSharesPrice();
    }

    protected void setUnitsSharesPrice(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setUnitsSharesPrice(value);
    }

    protected java.util.Date getPriceDate() {
        return ((AssetInvestment) getFinancial()).getPriceDate();
    }

    protected void setPriceDate(java.util.Date value) {
        ((AssetInvestment) getFinancial()).setPriceDate(value);
    }

    protected java.math.BigDecimal getRegularAmount() {
        return ((AssetInvestment) getFinancial())
                .getContributionRegularAmount();
    }

    // protected void setRegularAmount( java.math.BigDecimal value ) {
    // ( ( AssetInvestment ) getFinancial()
    // ).setContributionRegularAmount(value);
    // }

    protected java.math.BigDecimal getContributionAnnualAmount() {
        return ((AssetInvestment) getFinancial()).getContributionAnnualAmount();
    }

    protected void setContributionAnnualAmount(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setContributionAnnualAmount(value);
    }

    protected java.math.BigDecimal getContributionIndexation() {
        return ((AssetInvestment) getFinancial()).getContributionIndexation();
    }

    protected void setContributionIndexation(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setContributionIndexation(value);
    }

    protected java.util.Date getContributionStartDate() {
        return ((AssetInvestment) getFinancial()).getContributionStartDate();
    }

    protected void setContributionStartDate(java.util.Date value) {
        ((AssetInvestment) getFinancial()).setContributionStartDate(value);
    }

    protected java.util.Date getContributionEndDate() {
        return ((AssetInvestment) getFinancial()).getContributionEndDate();
    }

    protected void setContributionEndDate(java.util.Date value) {
        ((AssetInvestment) getFinancial()).setContributionEndDate(value);
    }

    protected java.math.BigDecimal getDrawdownAnnualAmount() {
        return ((AssetInvestment) getFinancial()).getDrawdownAnnualAmount();
    }

    protected void setDrawdownAnnualAmount(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setDrawdownAnnualAmount(value);
    }

    protected java.math.BigDecimal getDrawdownIndexation() {
        return ((AssetInvestment) getFinancial()).getDrawdownIndexation();
    }

    protected void setDrawdownIndexation(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setDrawdownIndexation(value);
    }

    protected java.util.Date getDrawdownStartDate() {
        return ((AssetInvestment) getFinancial()).getDrawdownStartDate();
    }

    protected void setDrawdownStartDate(java.util.Date value) {
        ((AssetInvestment) getFinancial()).setDrawdownStartDate(value);
    }

    protected java.util.Date getDrawdownEndDate() {
        return ((AssetInvestment) getFinancial()).getDrawdownEndDate();
    }

    protected void setDrawdownEndDate(java.util.Date value) {
        ((AssetInvestment) getFinancial()).setDrawdownEndDate(value);
    }

    protected java.math.BigDecimal getExpense() {
        return ((AssetInvestment) getFinancial()).getOtherExpenses();
    }

    protected void setExpense(java.math.BigDecimal value) {
        ((AssetInvestment) getFinancial()).setOtherExpenses(value);
    }

    protected boolean isReinvest() {
        return ((AssetInvestment) getFinancial()).isReinvest();
    }

    protected void setReinvest(boolean value) {
        ((AssetInvestment) getFinancial()).setReinvest(value);
    }

}
