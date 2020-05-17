/*
 * Financial.java
 *
 * Created on 8 October 2001, 10:31
 */

package com.argus.financials.bean.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.bean.ICode;
import com.argus.financials.api.bean.hibernate.FinancialCode;
import com.argus.financials.api.bean.hibernate.FinancialType;
import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.assetallocation.AssetAllocation;
import com.argus.financials.bean.Financial;
import com.argus.util.DateTimeUtils;

public abstract class FinancialBean extends AbstractPersistable
// extends Financial // aggregate Financial derived object (do not inherit!!!)
{

    // has to be instanciated in top level (final) derived class
    protected Financial financial; // aggregation

    // (e.g. objectTypeID1 = PERSON or BUSINESS)
    protected int objectTypeID1 = ObjectTypeConstant.PERSON; // first
                                                                // objectID1 in
                                                                // this link

    /** Creates new Financial */
    public FinancialBean() {
        super();
    }

    public FinancialBean(Financial value) {
        this.financial = value;
    }

    /**
     * helper methods
     */
    public abstract Class getFinancialClass();

    public abstract Financial getNewFinancial();

    public abstract int getObjectTypeID();

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_FINANCIAL;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    /**
     * IPersistable methods
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getId(), con);
    }

    protected String getSelectFieldsList() {
        return "FinancialTypeID, FinancialCodeID, InstitutionID, Institution, OwnerCodeID"
                + ", FinancialServiceCode, CountryCodeID, Amount, FinancialDesc, DateStart, DateEnd"
                + ", Franked, TaxFreeDeferred, CapitalGrowth, Income, UpfrontFee, OngoingFee"
                + ", Deductible, DeductibleDSS, ComplyingForDSS, Indexation, Expense"
                + ", Rebateable, DateCreated, StrategyGroupID, AssetAllocationID";
    }

    public void load(ResultSet rs) throws SQLException {

        // Financial table
        Integer financialTypeId = rs.getInt("FinancialTypeID");
        FinancialType financialType = financialService.findFinancialType(financialTypeId);
        setFinancialType(financialType);
        Integer financialCodeId = rs.getInt("FinancialCodeID");
        FinancialCode financialCode = financialService.findFinancialCode(financialCodeId);
        setFinancialCode(financialCode);
        setInstitutionID((Integer) rs.getObject("InstitutionID"));
        setInstitution(rs.getString("Institution"));
        setOwnerCodeID((Integer) rs.getObject("OwnerCodeID"));
        setFinancialServiceCode(rs.getString("FinancialServiceCode"));
        setCountryCodeID((Integer) rs.getObject("CountryCodeID"));

        setAmount(rs.getBigDecimal("Amount"));

        setFinancialDesc(rs.getString("FinancialDesc"));

        setStartDate(rs.getDate("DateStart"));
        setEndDate(rs.getDate("DateEnd"));

        setFranked(rs.getBigDecimal("Franked"));
        setTaxFreeDeferred(rs.getBigDecimal("TaxFreeDeferred"));
        setCapitalGrowth(rs.getBigDecimal("CapitalGrowth"));
        setIncome(rs.getBigDecimal("Income"));
        setUpfrontFee(rs.getBigDecimal("UpfrontFee"));
        setOngoingFee(rs.getBigDecimal("OngoingFee"));
        setDeductible(rs.getBigDecimal("Deductible"));
        setDeductibleDSS(rs.getBigDecimal("DeductibleDSS"));

        setComplyingForDSS("Y"
                .equalsIgnoreCase(rs.getString("ComplyingForDSS")) ? true
                : false);

        setIndexation(rs.getBigDecimal("Indexation"));
        setExpense(rs.getBigDecimal("Expense"));
        setRebateable(rs.getBigDecimal("Rebateable"));

        // setDateCreated();,

        setStrategyGroupID((Integer) rs.getObject("StrategyGroupID"));
        setAssetAllocationID((Integer) rs.getObject("AssetAllocationID"));

    }

    public int store(Connection con) throws SQLException {

        int i = 0;
        Integer primaryKeyID = null;
        PreparedStatement sql = null;

        // store data into table AssetAllocation
        if (getAssetAllocationID() != null) {
            AssetAllocationBean aab = new AssetAllocationBean(
                    getAssetAllocation());
            aab.store(con);
            // setAssetAllocationID(
            // aab.getAssetAllocation().getAssetAllocationID() );
        }

        try {
            if (getId() == null || getId().intValue() < 0) {

                primaryKeyID = new Integer(getNewObjectID(getObjectTypeID(),
                        con));

                // do insert into FINANCIAL table
                sql = con
                        .prepareStatement("INSERT INTO Financial ("
                                + "FinancialID,FinancialTypeID"
                                + ",FinancialCodeID,InstitutionID,Institution,OwnerCodeID"
                                + ",FinancialServiceCode,CountryCodeID,Amount,FinancialDesc,StrategyGroupID,DateStart,DateEnd"
                                + ",Franked,TaxFreeDeferred,CapitalGrowth,Income,UpfrontFee,OngoingFee"
                                + ",Deductible,DeductibleDSS,ComplyingForDSS,Indexation,Expense"
                                + ",Rebateable,AssetAllocationID) VALUES"
                                + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                sql.setObject(++i, primaryKeyID, java.sql.Types.INTEGER);
                sql
                        .setObject(++i, getFinancialTypeID(),
                                java.sql.Types.INTEGER);
                sql
                        .setObject(++i, getFinancialCodeID(),
                                java.sql.Types.INTEGER);
                sql.setObject(++i, getInstitutionID(), java.sql.Types.INTEGER);
                sql.setString(++i, getInstitution());
                sql.setObject(++i, getOwnerCodeID(), java.sql.Types.INTEGER);

                String fsc = getFinancialServiceCode();
                if (fsc == null || fsc.trim().length() == 0)
                    sql.setNull(++i, java.sql.Types.CHAR);
                else
                    sql.setString(++i, fsc);

                sql.setObject(++i, getCountryCodeID(), java.sql.Types.INTEGER);

                if (getAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getAmount().toString());

                sql.setString(++i, getFinancialDesc());
                sql
                        .setObject(++i, getStrategyGroupID(),
                                java.sql.Types.INTEGER);

                if (getStartDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getStartDate()));
                if (getEndDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils.getJdbcDate(getEndDate()));

                if (getFranked() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getFranked().toString());

                if (getTaxFreeDeferred() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getTaxFreeDeferred().toString());

                if (getCapitalGrowth() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getCapitalGrowth().toString());

                if (getIncome() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getIncome().toString());

                if (getUpfrontFee() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getUpfrontFee().toString());

                if (getOngoingFee() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getOngoingFee().toString());

                if (getDeductible() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getDeductible().toString());

                if (getDeductibleDSS() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getDeductibleDSS().toString());

                if (!isComplyingForDSS())
                    sql.setNull(++i, java.sql.Types.CHAR);
                else
                    sql.setString(++i, "Y");

                if (getIndexation() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getIndexation().toString());

                if (getExpense() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getExpense().toString());

                if (getRebateable() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getRebateable().toString());

                if (getAssetAllocationID() == null)
                    sql.setNull(++i, java.sql.Types.INTEGER);
                else
                    sql.setObject(++i, getAssetAllocationID(),
                            java.sql.Types.INTEGER);

                sql.executeUpdate();

                // then create link
                linkObjectDao.link(getOwnerId(),
                        primaryKeyID, getLinkObjectTypeID(), con);

                // has to be in most derived class (to be safe),
                // we are not using primaryKeyID for other queries
                // setId( primaryKeyID );

            } else {

                primaryKeyID = getId();

                // do update on FINANCIAL table
                sql = con
                        .prepareStatement("UPDATE Financial SET"
                                + " FinancialTypeID=?,FinancialCodeID=?,InstitutionID=?,Institution=?,OwnerCodeID=?"
                                + ",FinancialServiceCode=?,CountryCodeID=?,Amount=?,FinancialDesc=?,StrategyGroupID=?,DateStart=?,DateEnd=?"
                                + ",Franked=?,TaxFreeDeferred=?,CapitalGrowth=?,Income=?,UpfrontFee=?,OngoingFee=?"
                                + ",Deductible=?,DeductibleDSS=?,ComplyingForDSS=?,Indexation=?,Expense=?"
                                + ",Rebateable=?,AssetAllocationID=?"
                                + " WHERE FinancialID=?");

                sql
                        .setObject(++i, getFinancialTypeID(),
                                java.sql.Types.INTEGER);
                sql
                        .setObject(++i, getFinancialCodeID(),
                                java.sql.Types.INTEGER);
                sql.setObject(++i, getInstitutionID(), java.sql.Types.INTEGER);
                sql.setString(++i, getInstitution());
                sql.setObject(++i, getOwnerCodeID(), java.sql.Types.INTEGER);

                String fsc = getFinancialServiceCode();
                if (fsc == null || fsc.trim().length() == 0)
                    sql.setNull(++i, java.sql.Types.CHAR);
                else
                    sql.setString(++i, fsc);

                sql.setObject(++i, getCountryCodeID(), java.sql.Types.INTEGER);

                if (getAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getAmount().toString());

                sql.setString(++i, getFinancialDesc());
                sql
                        .setObject(++i, getStrategyGroupID(),
                                java.sql.Types.INTEGER);

                if (getStartDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils
                            .getJdbcDate(getStartDate()));
                if (getEndDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils.getJdbcDate(getEndDate()));

                if (getFranked() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getFranked().toString());

                if (getTaxFreeDeferred() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getTaxFreeDeferred().toString());

                if (getCapitalGrowth() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getCapitalGrowth().toString());

                if (getIncome() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getIncome().toString());

                if (getUpfrontFee() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getUpfrontFee().toString());

                if (getOngoingFee() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getOngoingFee().toString());

                if (getDeductible() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getDeductible().toString());

                if (getDeductibleDSS() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getDeductibleDSS().toString());

                if (!isComplyingForDSS())
                    sql.setNull(++i, java.sql.Types.CHAR);
                else
                    sql.setString(++i, "Y");

                if (getIndexation() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getIndexation().toString());

                if (getExpense() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getExpense().toString());

                if (getRebateable() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getRebateable().toString());

                if (getAssetAllocationID() == null)
                    sql.setNull(++i, java.sql.Types.INTEGER);
                else
                    sql.setObject(++i, getAssetAllocationID(),
                            java.sql.Types.INTEGER);

                sql.setObject(++i, primaryKeyID, java.sql.Types.INTEGER);

                sql.executeUpdate();

            }

        } finally {
            close(null, sql);
        }

        return primaryKeyID.intValue();

    }

    public void remove(Connection con) throws SQLException {

        if (getId() == null)
            return;

        CallableStatement cs = con.prepareCall("{call sp_delete_financial(?)}");
        try {
            cs.setInt(1, getId().intValue());
            cs.execute();

            // return cs.getInt(1) == 0; // 0 (success) or 1 (failure)

        } catch (SQLException e) {
            System.err.println("FAILED {call sp_delete_financial("
                    + getId() + ")}");
            throw e;
        } finally {
            cs.close();
        }

    }

    /**
     * get/set methods
     */
    public void setObjectTypeID1(int value) {
        setModified(objectTypeID1 != value);
        objectTypeID1 = value;
    }

    public Financial getFinancial() {
        if (financial == null) {
            financial = getNewFinancial();

            // Class objClass = getFinancialClass();
            // try { financial = ( Financial ) objClass.getConstructor( null
            // ).newInstance( null ); }
            // catch ( Exception e ) { throw new RuntimeException(
            // e.getMessage() ); }
        }

        return financial;
    }

    public void setFinancial(Financial value) {
        financial = value;
    }

    public boolean isModified() {
        return getFinancial().isModified();
    }

    public void setModified(boolean value) {
        getFinancial().setModified(value);
    }

    public Integer getId() {
        return getFinancial().getId();
    }

    public void setId(Integer value) {
        getFinancial().setId(value);
    }

    public Integer getOwnerId() {
        return getFinancial().getOwnerId();
    }

    public void setOwnerId(Integer value) {
        getFinancial().setOwnerId(value);
    }

    @Deprecated protected Integer getFinancialTypeID() {
        return getFinancial().getFinancialTypeID();
    }
    @Deprecated protected void setFinancialTypeID(Integer value) {
        getFinancial().setFinancialTypeId(value);
    }
    protected void setFinancialType(ICode value) {
        getFinancial().setFinancialType(value);
    }

    @Deprecated protected Integer getFinancialCodeID() {
        return getFinancial().getFinancialCodeId();
    }
    @Deprecated protected void setFinancialCodeID(Integer value) {
        getFinancial().setFinancialCodeId(value);
    }
    protected void setFinancialCode(ICode value) {
        getFinancial().setFinancialCode(value);
    }

    protected Integer getInstitutionID() {
        return getFinancial().getInstitutionID();
    }

    protected void setInstitutionID(Integer value) {
        getFinancial().setInstitutionID(value);
    }

    protected String getInstitution() {
        return getFinancial().getInstitution();
    }

    protected void setInstitution(String value) {
        getFinancial().setInstitution(value);
    }

    protected Integer getOwnerCodeID() {
        return getFinancial().getOwnerCodeID();
    }

    protected void setOwnerCodeID(Integer value) {
        getFinancial().setOwnerCodeID(value);
    }

    protected Integer getCountryCodeID() {
        return getFinancial().getCountryCodeID();
    }

    protected void setCountryCodeID(Integer value) {
        getFinancial().setCountryCodeID(value);
    }

    protected Integer getStrategyGroupID() {
        return getFinancial().getStrategyGroupID();
    }

    protected void setStrategyGroupID(Integer value) {
        getFinancial().setStrategyGroupID(value);
    }

    protected java.math.BigDecimal getAmount() {
        return getFinancial().getAmount();
    }

    protected void setAmount(java.math.BigDecimal value) {
        getFinancial().setAmount(value);
    }

    protected String getFinancialDesc() {
        return getFinancial().getFinancialDesc();
    }

    protected void setFinancialDesc(String value) {
        getFinancial().setFinancialDesc(value);
    }

    protected java.util.Date getStartDate() {
        return getFinancial().getStartDate();
    }

    protected void setStartDate(java.util.Date value) {
        getFinancial().setStartDate(value);
    }

    protected java.util.Date getEndDate() {
        return getFinancial().getEndDate();
    }

    protected void setEndDate(java.util.Date value) {
        getFinancial().setEndDate(value);
    }

    protected java.math.BigDecimal getFranked() {
        return null;
    }

    protected void setFranked(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getTaxFreeDeferred() {
        return null;
    }

    protected void setTaxFreeDeferred(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getCapitalGrowth() {
        return null;
    }

    protected void setCapitalGrowth(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getIncome() {
        return null;
    }

    protected void setIncome(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getUpfrontFee() {
        return null;
    }

    protected void setUpfrontFee(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getOngoingFee() {
        return null;
    }

    protected void setOngoingFee(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getDeductible() {
        return null;
    }

    protected void setDeductible(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getDeductibleDSS() {
        return null;
    }

    protected void setDeductibleDSS(java.math.BigDecimal value) {
    }

    protected boolean isComplyingForDSS() {
        return false;
    }

    protected void setComplyingForDSS(boolean value) {
    }

    protected java.math.BigDecimal getIndexation() {
        return null;
    }

    protected void setIndexation(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getExpense() {
        return null;
    }

    protected void setExpense(java.math.BigDecimal value) {
    }

    protected java.math.BigDecimal getRebateable() {
        return null;
    }

    protected void setRebateable(java.math.BigDecimal value) {
    }

    protected AssetAllocation getAssetAllocation() {
        return getFinancial().getAssetAllocation();
    }

    protected void setAssetAllocation(AssetAllocation value) {
        getFinancial().setAssetAllocation(value);
    }

    protected Integer getAssetAllocationID() {
        return getFinancial().getAssetAllocationID();
    }

    protected void setAssetAllocationID(Integer value) {
        getFinancial().setAssetAllocationID(value);
    }

    public String getFinancialServiceCode() {
        return getFinancial().getFinancialServiceCode();
    }

    public void setFinancialServiceCode(String value) {
        getFinancial().setFinancialServiceCode(value);
    }

}
