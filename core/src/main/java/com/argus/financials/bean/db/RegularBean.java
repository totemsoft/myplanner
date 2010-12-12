/*
 * RegularBean.java
 *
 * Created on 6 November 2001, 15:58
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
import com.argus.financials.bean.Assets;
import com.argus.financials.bean.IPersistable;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.bean.Regular;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.util.DateTimeUtils;

public abstract class RegularBean extends FinancialBean implements
        IPersistable {

    /** Creates new RegularBean */
    public RegularBean() {
    }

    public RegularBean(Regular value) {
        super(value);
    }

    /**
     * helper methods
     */
    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_REGULAR;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    protected String getSelectFieldsList() {
        return "RegularAmount, FrequencyCodeID, DateNext, AssetID, Taxable, "
                + super.getSelectFieldsList();
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
                                    + " FROM Regular, Financial"
                                    + " WHERE (RegularID = ?) AND (RegularID = FinancialID) AND (NextID IS NULL)",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Regular ID: "
                        + primaryKeyID);

            load(rs);

            // has to be last (to be safe), we are not using primaryKeyID for
            // other queries
            setPrimaryKeyID(primaryKeyID);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        // Regular table
        setRegularAmount(rs.getBigDecimal("RegularAmount"));

        setFrequencyCodeID((Integer) rs.getObject("FrequencyCodeID"));

        setNextDate(rs.getDate("DateNext"));
        setAssetID((Integer) rs.getObject("AssetID"));

        String taxable = rs.getString("Taxable");
        setTaxable(taxable == null || taxable.length() == 0 ? false : true);

        // Financial table
        super.load(rs);

    }

    public int store(Connection con) throws SQLException {

        int primaryKeyID = super.store(con);

        int i = 0;
        PreparedStatement sql = null;

        // if associated asset is not saved - THEN SAVE IT!!!
        Asset asset = getAsset();
        if (asset == null) {
            setAssetID(null);

        } else {

            AssetBean ab = (AssetBean) createNewInstance(asset
                    .getObjectTypeID());

            asset.setOwnerPrimaryKeyID(getOwnerPrimaryKeyID());
            ab.setFinancial(asset);
            ab.store(con);

        }

        try {
            if (getPrimaryKeyID() == null || getPrimaryKeyID().intValue() < 0) {

                // do insert into Regular table
                sql = con
                        .prepareStatement("INSERT INTO Regular"
                                + " (RegularID, RegularAmount, FrequencyCodeID, DateNext, AssetID, Taxable)"
                                + " VALUES" + " (?,?,?,?,?,?)");

                sql.setInt(++i, primaryKeyID);

                if (getRegularAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getRegularAmount().toString());

                sql
                        .setObject(++i, getFrequencyCodeID(),
                                java.sql.Types.INTEGER);

                if (getNextDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql
                            .setString(++i, DateTimeUtils
                                    .getJdbcDate(getNextDate()));

                sql.setObject(++i, getAssetID(), java.sql.Types.INTEGER);

                if (isTaxable())
                    sql.setString(++i, BooleanCode.rcYES.getCode());
                else
                    sql.setNull(++i, java.sql.Types.CHAR);

                try {
                    sql.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("FAILED to store Regular: " + financial
                            + "\tID: " + getPrimaryKeyID() + "\n\tAsset: "
                            + asset + ", AssetID: " + getAssetID());
                    throw e;
                }

                // then create link
                FPSLinkObject.getInstance().link(
                        getOwnerPrimaryKeyID().intValue(), primaryKeyID,
                        getLinkObjectTypeID(), con);

            } else {

                // do update on Regular table
                sql = con
                        .prepareStatement("UPDATE Regular SET"
                                + " RegularAmount=?,FrequencyCodeID=?,DateNext=?,AssetID=?,Taxable=?"
                                + " WHERE RegularID=?");

                // sql.setObject( ++i, getRegularAmount(),
                // java.sql.Types.NUMERIC );
                if (getRegularAmount() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setString(++i, getRegularAmount().toString());

                sql
                        .setObject(++i, getFrequencyCodeID(),
                                java.sql.Types.INTEGER);

                if (getNextDate() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql
                            .setString(++i, DateTimeUtils
                                    .getJdbcDate(getNextDate()));

                sql.setObject(++i, getAssetID(), java.sql.Types.INTEGER);

                if (isTaxable())
                    sql.setString(++i, BooleanCode.rcYES.getCode());
                else
                    sql.setNull(++i, java.sql.Types.CHAR);

                sql.setInt(++i, primaryKeyID);

                sql.executeUpdate();

            }
        } finally {
            close(null, sql);
        }

        return primaryKeyID;

    }

    /**
     * get/set methods
     */
    public java.math.BigDecimal getRegularAmount() {
        return ((Regular) getFinancial()).getRegularAmount();
    }

    public void setRegularAmount(java.math.BigDecimal value) {
        ((Regular) getFinancial()).setRegularAmount(value);
    }

    public Integer getFrequencyCodeID() {
        return ((Regular) getFinancial()).getFrequencyCodeID();
    }

    public void setFrequencyCodeID(Integer value) {
        ((Regular) getFinancial()).setFrequencyCodeID(value);
    }

    public java.util.Date getNextDate() {
        return ((Regular) getFinancial()).getNextDate();
    }

    public void setNextDate(java.util.Date value) {
        ((Regular) getFinancial()).setNextDate(value);
    }

    public Integer getAssetID() {
        return ((Regular) getFinancial()).getAssetID();
    }

    public void setAssetID(Integer value) {
        ((Regular) getFinancial()).setAssetID(value);
    }

    public Asset getAsset() {
        Assets aa = Assets.getCurrentAssets(); // Yes, this bean is working
                                                // only with current assets
        return ((Regular) getFinancial()).getAsset(aa);
    }

    public boolean isTaxable() {
        return ((Regular) getFinancial()).isTaxable();
    }

    public void setTaxable(boolean value) {
        ((Regular) getFinancial()).setTaxable(value);
    }

    public java.math.BigDecimal getIndexation() {
        return ((Regular) getFinancial()).getIndexation();
    }

    public void setIndexation(java.math.BigDecimal value) {
        ((Regular) getFinancial()).setIndexation(value);
    }

}
