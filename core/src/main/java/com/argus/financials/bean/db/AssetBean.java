/*
 * AssetBean.java
 *
 * Created on 8 October 2001, 17:24
 */

package com.argus.financials.bean.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.bean.Asset;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.service.client.ObjectNotFoundException;
import com.argus.util.DateTimeUtils;

public abstract class AssetBean extends FinancialBean {

    /** Creates new AssetBean */
    public AssetBean() {
    }

    public AssetBean(Asset value) {
        super(value);
    }

    /**
     * helper methods
     */
    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_ASSET;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    protected String getSelectFieldsList() {
        return "FundTypeID, InvestmentTypeID, UnitsShares, ReplacementValue"
                + ", PurchaseCost, UnitsSharesPrice, PriceDate, AccountNumber"
                + ", TaxDeductibleAnnualAmount, TaxDeductibleRegularAmount"
                + ", FrequencyCodeID, AnnualAmount, Reinvest"
                + ", ContributionAnnualAmount, ContributionIndexation, ContributionStartDate, ContributionEndDate"
                + ", DrawdownAnnualAmount, DrawdownIndexation, DrawdownStartDate, DrawdownEndDate"
                + ", " + super.getSelectFieldsList();
    }

    /**
     * IPersistable methods
     */
    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con
                    .prepareStatement(
                            "SELECT "
                                    + getSelectFieldsList()
                                    + " FROM Asset, Financial"
                                    + " WHERE (AssetID = ?) AND (AssetID = FinancialID) AND (NextID IS NULL)",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Asset ID: "
                        + primaryKeyID);

            load(rs);

            // has to be last (to be safe), we are not using primaryKeyID for
            // other queries
            setPrimaryKeyID(primaryKeyID);

        } finally {
            close(rs, sql);
        }

        try {
            // load data from table AssetAllocation
            if (getAssetAllocationID() != null) {
                AssetAllocationBean aab = new AssetAllocationBean(
                        getAssetAllocation());
                aab.load(con, getAssetAllocationID());
            }
        } finally {
        }

    }

    public void load(ResultSet rs) throws SQLException {

        setFundTypeID((Integer) rs.getObject("FundTypeID"));
        setInvestmentTypeID((Integer) rs.getObject("InvestmentTypeID"));
        setUnitsShares(new Double(rs.getDouble("UnitsShares")));

        setReplacementValue(rs.getBigDecimal("ReplacementValue"));
        setPurchaseCost(rs.getBigDecimal("PurchaseCost"));

        setUnitsSharesPrice(rs.getBigDecimal("UnitsSharesPrice"));
        setPriceDate(rs.getDate("PriceDate"));

        setAccountNumber(rs.getString("AccountNumber"));

        setTaxDeductibleAnnualAmount(rs
                .getBigDecimal("TaxDeductibleAnnualAmount"));
        setTaxDeductibleRegularAmount(rs
                .getBigDecimal("TaxDeductibleRegularAmount")); // do nothing

        setFrequencyCodeID((Integer) rs.getObject("FrequencyCodeID"));

        setAnnualAmount(rs.getBigDecimal("AnnualAmount"));

        setReinvest("Y".equalsIgnoreCase(rs.getString("Reinvest")) ? true
                : false);

        setContributionAnnualAmount(rs
                .getBigDecimal("ContributionAnnualAmount"));
        setContributionIndexation(rs.getBigDecimal("ContributionIndexation"));
        setContributionStartDate(rs.getDate("ContributionStartDate"));
        setContributionEndDate(rs.getDate("ContributionEndDate"));

        setDrawdownAnnualAmount(rs.getBigDecimal("DrawdownAnnualAmount"));
        setDrawdownIndexation(rs.getBigDecimal("DrawdownIndexation"));
        setDrawdownStartDate(rs.getDate("DrawdownStartDate"));
        setDrawdownEndDate(rs.getDate("DrawdownEndDate"));

        // Financial table
        super.load(rs);

    }

    public int store(Connection con) throws SQLException {

        int primaryKeyID = super.store(con);

        int i = 0;
        PreparedStatement sql = null;

        try {
            if (getPrimaryKeyID() == null || getPrimaryKeyID().intValue() < 0) {

                // do insert into ASSET table
                sql = con
                        .prepareStatement("INSERT INTO Asset ("
                                + " AssetID"
                                + ",FundTypeID,InvestmentTypeID,UnitsShares,ReplacementValue"
                                + ",PurchaseCost,UnitsSharesPrice,PriceDate,AccountNumber"
                                + ",FrequencyCodeID,AnnualAmount,Reinvest"
                                + ",TaxDeductibleAnnualAmount,TaxDeductibleRegularAmount"
                                + ",ContributionAnnualAmount,ContributionIndexation,ContributionStartDate,ContributionEndDate"
                                + ",DrawdownAnnualAmount,DrawdownIndexation,DrawdownStartDate,DrawdownEndDate"
                                + " ) VALUES ("
                                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                                + ")");

                sql.setInt(++i, primaryKeyID);

                sql.setObject(++i, getFundTypeID(), java.sql.Types.INTEGER);
                sql.setObject(++i, getInvestmentTypeID(),
                        java.sql.Types.INTEGER);
                sql.setObject(++i, getUnitsShares(), java.sql.Types.DOUBLE);

                if (getReplacementValue() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getReplacementValue().doubleValue());

                if (getPurchaseCost() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getPurchaseCost().doubleValue());

                if (getUnitsSharesPrice() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getUnitsSharesPrice().doubleValue());

                if (getPriceDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getPriceDate()));

                sql.setString(++i, getAccountNumber());

                sql
                        .setObject(++i, getFrequencyCodeID(),
                                java.sql.Types.INTEGER);

                if (getAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getAnnualAmount().doubleValue());

                if (!isReinvest())
                    sql.setNull(++i, java.sql.Types.CHAR);
                else
                    sql.setString(++i, "Y");

                if (getTaxDeductibleAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getTaxDeductibleAnnualAmount()
                            .doubleValue());

                if (getTaxDeductibleRegularAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getTaxDeductibleRegularAmount()
                            .doubleValue());

                if (getContributionAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getContributionAnnualAmount()
                            .doubleValue());

                if (getContributionIndexation() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getContributionIndexation()
                            .doubleValue());

                if (getContributionStartDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getContributionStartDate()));

                if (getContributionEndDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getContributionEndDate()));

                if (getDrawdownAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getDrawdownAnnualAmount().doubleValue());

                if (getDrawdownIndexation() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getDrawdownIndexation().doubleValue());

                if (getDrawdownStartDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getDrawdownStartDate()));

                if (getDrawdownEndDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getDrawdownEndDate()));

                sql.executeUpdate();

                // then create link
                FPSLinkObject.getInstance().link(
                        getOwnerPrimaryKeyID().intValue(), primaryKeyID,
                        getLinkObjectTypeID(), con);

            } else {

                // do update on ASSET table
                sql = con
                        .prepareStatement("UPDATE Asset SET"
                                + " FundTypeID=?,InvestmentTypeID=?,UnitsShares=?,ReplacementValue=?"
                                + ",PurchaseCost=?,UnitsSharesPrice=?,PriceDate=?,AccountNumber=?"
                                + ",FrequencyCodeID=?,AnnualAmount=?,Reinvest=?"
                                + ",TaxDeductibleAnnualAmount=?,TaxDeductibleRegularAmount=?"
                                + ",ContributionAnnualAmount=?,ContributionIndexation=?,ContributionStartDate=?,ContributionEndDate=?"
                                + ",DrawdownAnnualAmount=?,DrawdownIndexation=?,DrawdownStartDate=?,DrawdownEndDate=?"
                                + " WHERE AssetID=?");

                sql.setObject(++i, getFundTypeID(), java.sql.Types.INTEGER);
                sql.setObject(++i, getInvestmentTypeID(),
                        java.sql.Types.INTEGER);
                sql.setObject(++i, getUnitsShares(), java.sql.Types.DOUBLE);

                if (getReplacementValue() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getReplacementValue().doubleValue());

                if (getPurchaseCost() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getPurchaseCost().doubleValue());

                if (getUnitsSharesPrice() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getUnitsSharesPrice().doubleValue());

                if (getPriceDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getPriceDate()));

                sql.setString(++i, getAccountNumber());

                sql
                        .setObject(++i, getFrequencyCodeID(),
                                java.sql.Types.INTEGER);

                if (getAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getAnnualAmount().doubleValue());

                if (!isReinvest())
                    sql.setNull(++i, java.sql.Types.CHAR);
                else
                    sql.setString(++i, "Y");

                if (getTaxDeductibleAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getTaxDeductibleAnnualAmount()
                            .doubleValue());

                if (getTaxDeductibleRegularAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getTaxDeductibleRegularAmount()
                            .doubleValue());

                if (getContributionAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getContributionAnnualAmount()
                            .doubleValue());

                if (getContributionIndexation() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getContributionIndexation()
                            .doubleValue());

                if (getContributionStartDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getContributionStartDate()));

                if (getContributionEndDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getContributionEndDate()));

                if (getDrawdownAnnualAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getDrawdownAnnualAmount().doubleValue());

                if (getDrawdownIndexation() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getDrawdownIndexation().doubleValue());

                if (getDrawdownStartDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getDrawdownStartDate()));

                if (getDrawdownEndDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getDrawdownEndDate()));

                sql.setInt(++i, primaryKeyID);

                sql.executeUpdate();

            }

        } finally {
            close(null, sql);
        }

        return primaryKeyID;

    }

    public void remove(Connection con) throws SQLException {

        super.remove(con);

    }

    /**
     * get/set methods
     */
    public Asset getAsset() {
        return (Asset) getFinancial();
    }

    protected String getAccountNumber() {
        return ((Asset) financial).getAccountNumber();
    }

    protected void setAccountNumber(String value) {
        ((Asset) financial).setAccountNumber(value);
    }

    protected Double getUnitsShares() {
        return ((Asset) getFinancial()).getUnitsShares();
    }

    protected void setUnitsShares(Double value) {
        ((Asset) getFinancial()).setUnitsShares(value);
    }

    protected Integer getFundTypeID() {
        return null;
    }

    protected void setFundTypeID(Integer value) {
        // do nothing
    }

    protected Integer getInvestmentTypeID() {
        return null;
    }

    protected void setInvestmentTypeID(Integer value) {
        // do nothing
    }

    protected java.math.BigDecimal getPurchaseCost() {
        return null;
    }

    protected void setPurchaseCost(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.math.BigDecimal getReplacementValue() {
        return null;
    }

    protected void setReplacementValue(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.math.BigDecimal getUnitsSharesPrice() {
        return null;
    }

    protected void setUnitsSharesPrice(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.util.Date getPriceDate() {
        return null;
    }

    protected void setPriceDate(java.util.Date value) {
        // do nothing
    }

    protected java.math.BigDecimal getAnnualAmount() {
        return null;
    }

    protected void setAnnualAmount(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.math.BigDecimal getTaxDeductibleAnnualAmount() {
        return null;
    }

    protected void setTaxDeductibleAnnualAmount(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.math.BigDecimal getTaxDeductibleRegularAmount() {
        return null;
    }

    protected void setTaxDeductibleRegularAmount(java.math.BigDecimal value) {
        // do nothing
    }

    protected Integer getFrequencyCodeID() {
        return null;
    }

    protected void setFrequencyCodeID(Integer value) {
        // do nothing
    }

    protected java.math.BigDecimal getContributionAnnualAmount() {
        return null;
    }

    protected void setContributionAnnualAmount(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.math.BigDecimal getContributionIndexation() {
        return null;
    }

    protected void setContributionIndexation(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.util.Date getContributionStartDate() {
        return null;
    }

    protected void setContributionStartDate(java.util.Date value) {
        // do nothing
    }

    protected java.util.Date getContributionEndDate() {
        return null;
    }

    protected void setContributionEndDate(java.util.Date value) {
        // do nothing
    }

    protected java.math.BigDecimal getDrawdownAnnualAmount() {
        return null;
    }

    protected void setDrawdownAnnualAmount(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.math.BigDecimal getDrawdownIndexation() {
        return null;
    }

    protected void setDrawdownIndexation(java.math.BigDecimal value) {
        // do nothing
    }

    protected java.util.Date getDrawdownStartDate() {
        return null;
    }

    protected void setDrawdownStartDate(java.util.Date value) {
        // do nothing
    }

    protected java.util.Date getDrawdownEndDate() {
        return null;
    }

    protected void setDrawdownEndDate(java.util.Date value) {
        // do nothing
    }

    protected boolean isReinvest() {
        return false;
    }

    protected void setReinvest(boolean value) {
        // do nothing
    }

}
