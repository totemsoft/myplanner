/*
 * AssetAllocationTableBean.java
 *
 * Created on 25 Sep 2002, 16:53
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.assetallocation.AssetAllocation;
import au.com.totemsoft.myplanner.code.BooleanCode;

/**
 * AssetAllocationTableBean is responsible for creating,loading and storing
 * information form the database table "AssetAllocation".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class AssetAllocationBean {
    // constants
    public static final String DATABASE_TABLE_NAME = "AssetAllocation";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        AssetAllocationBean.sqlHelper = sqlHelper;
    }

    // bean properties
    private AssetAllocation assetAllocation;

    /** Creates new AssetAllocationBean */
    public AssetAllocationBean() {
        this.assetAllocation = new AssetAllocation();
    }

    public AssetAllocationBean(AssetAllocation new_assetAllocation) {
        if (new_assetAllocation == null) {
            new_assetAllocation = new AssetAllocation();
        }
        this.assetAllocation = new_assetAllocation;
    }

    /**
     */
    public void store(Connection con) throws java.sql.SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;

        try {
            if (assetAllocation.getAssetAllocationID() == null
                    || assetAllocation.getAssetAllocationID().intValue() < 0) {
                // insert new row
                pstmt_StringBuffer = new StringBuffer();

                // build sql query
                pstmt_StringBuffer.append("INSERT INTO ");
                pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
                pstmt_StringBuffer.append("( Amount, InCash, InFixedInterest, "
                        + "InAustShares, InIntnlShares, InProperty, InOther, "
                        + "Include ) ");
                pstmt_StringBuffer.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ");

                // set and execute query
                pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

                if (assetAllocation.getAmount() == null)
                    pstmt.setNull(1, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(1, assetAllocation.getAmount()
                            .doubleValue());

                if (assetAllocation.getInCash() == null)
                    pstmt.setNull(2, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(2, assetAllocation.getInCash()
                            .doubleValue());

                if (assetAllocation.getInFixedInterest() == null)
                    pstmt.setNull(3, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(3, assetAllocation.getInFixedInterest()
                            .doubleValue());

                if (assetAllocation.getInAustShares() == null)
                    pstmt.setNull(4, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(4, assetAllocation.getInAustShares()
                            .doubleValue());

                if (assetAllocation.getInIntnlShares() == null)
                    pstmt.setNull(5, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(5, assetAllocation.getInIntnlShares()
                            .doubleValue());

                if (assetAllocation.getInProperty() == null)
                    pstmt.setNull(6, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(6, assetAllocation.getInProperty()
                            .doubleValue());

                if (assetAllocation.getInOther() == null)
                    pstmt.setNull(7, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(7, assetAllocation.getInOther()
                            .doubleValue());

                if (assetAllocation.getInclude() == null) {
                    pstmt.setNull(8, java.sql.Types.CHAR);
                } else {
                    if (assetAllocation.getInclude().equals(Boolean.TRUE))
                        pstmt.setString(8, BooleanCode.rcYES.getCode());
                    else
                        pstmt.setNull(8, java.sql.Types.CHAR);
                }

                status = pstmt.executeUpdate();
                pstmt.close();

                // build sql query to get ID
                pstmt_StringBuffer = new StringBuffer();
                pstmt_StringBuffer
                        .append("SELECT MAX(AssetAllocationID) FROM ");
                pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "]");

                // set and execute query
                pstmt = con.prepareStatement(pstmt_StringBuffer.toString());
                rs = pstmt.executeQuery();

                // do we have any result?
                if (rs.next()) {
                    // get the data
                    assetAllocation.setAssetAllocationID(new Integer(rs
                            .getInt(1)));
                }

            } else {
                // update row
                pstmt_StringBuffer = new StringBuffer();

                // build sql query
                pstmt_StringBuffer.append("UPDATE ");
                pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
                pstmt_StringBuffer.append("SET ");
                pstmt_StringBuffer.append("Amount = ?, ");
                pstmt_StringBuffer.append("InCash = ?, ");
                pstmt_StringBuffer.append("InFixedInterest = ?, ");
                pstmt_StringBuffer.append("InAustShares = ?, ");
                pstmt_StringBuffer.append("InIntnlShares = ?, ");
                pstmt_StringBuffer.append("InProperty = ?, ");
                pstmt_StringBuffer.append("InOther = ?, ");
                pstmt_StringBuffer.append("Include = ? ");
                pstmt_StringBuffer.append("WHERE AssetAllocationID = ? ");

                // set and execute query
                pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

                if (assetAllocation.getAmount() == null)
                    pstmt.setNull(1, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(1, assetAllocation.getAmount()
                            .doubleValue());

                if (assetAllocation.getInCash() == null)
                    pstmt.setNull(2, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(2, assetAllocation.getInCash()
                            .doubleValue());

                if (assetAllocation.getInFixedInterest() == null)
                    pstmt.setNull(3, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(3, assetAllocation.getInFixedInterest()
                            .doubleValue());

                if (assetAllocation.getInAustShares() == null)
                    pstmt.setNull(4, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(4, assetAllocation.getInAustShares()
                            .doubleValue());

                if (assetAllocation.getInIntnlShares() == null)
                    pstmt.setNull(5, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(5, assetAllocation.getInIntnlShares()
                            .doubleValue());

                if (assetAllocation.getInProperty() == null)
                    pstmt.setNull(6, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(6, assetAllocation.getInProperty()
                            .doubleValue());

                if (assetAllocation.getInOther() == null)
                    pstmt.setNull(7, java.sql.Types.DOUBLE);
                else
                    pstmt.setDouble(7, assetAllocation.getInOther()
                            .doubleValue());

                if (assetAllocation.getInclude() == null) {
                    pstmt.setNull(8, java.sql.Types.CHAR);
                } else {
                    if (assetAllocation.getInclude().equals(Boolean.TRUE))
                        pstmt.setString(8, BooleanCode.rcYES.getCode());
                    else
                        pstmt.setNull(8, java.sql.Types.CHAR);
                }

                pstmt.setInt(9, assetAllocation.getAssetAllocationID()
                        .intValue());

                status = pstmt.executeUpdate();
                pstmt.close();
            }
        } finally {
            sqlHelper.close(rs, pstmt);
        }
    }

    public boolean load(Connection con, Integer id)
            throws java.sql.SQLException, ObjectNotFoundException {
        return findByPrimaryKey(con, id);
    }

    public boolean load(Connection con) throws java.sql.SQLException,
            ObjectNotFoundException {
        return findByPrimaryKey(con);
    }

    /**
     * 
     */
    public boolean findByPrimaryKey(Connection con)
            throws java.sql.SQLException, ObjectNotFoundException {
        return findByPrimaryKey(con, assetAllocation.getId());
    }

    public boolean findByPrimaryKey(Connection con, Integer id)
            throws java.sql.SQLException, ObjectNotFoundException {
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [AssetAllocationID] = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, id.intValue());

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                found = true;
                // get the data
                assetAllocation.setAssetAllocationID(new Integer(rs
                        .getInt("AssetAllocationID")));
                assetAllocation.setAmount(new Double(rs.getDouble("Amount")));
                assetAllocation.setInCash(new Double(rs.getDouble("InCash")));
                assetAllocation.setInFixedInterest(new Double(rs
                        .getDouble("InFixedInterest")));
                assetAllocation.setInAustShares(new Double(rs
                        .getDouble("InAustShares")));
                assetAllocation.setInIntnlShares(new Double(rs
                        .getDouble("InIntnlShares")));
                assetAllocation.setInProperty(new Double(rs
                        .getDouble("InProperty")));
                assetAllocation.setInOther(new Double(rs.getDouble("InOther")));
                assetAllocation.setInclude(new Boolean(((rs
                        .getString("Include") == null) ? false : true)));
            } else {
                throw new ObjectNotFoundException(
                        "Can not find Asset Allocation ID: " + id);
            }

        } finally {
            sqlHelper.close(rs, pstmt);
        }

        return found;
    }

    /**
     */
    public void delete(Integer aaid) throws java.sql.SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;
        try (Connection con = sqlHelper.getConnection();) {
            pstmt_StringBuffer = new StringBuffer();

            // build sql query
            pstmt_StringBuffer
                    .append("UPDATE Financial SET AssetAllocationID = NULL ");
            pstmt_StringBuffer.append("WHERE AssetAllocationID = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, aaid.intValue());

            status = pstmt.executeUpdate();

            pstmt_StringBuffer = new StringBuffer();

            // build sql query
            pstmt_StringBuffer.append("DELETE FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE AssetAllocationID = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, aaid.intValue());

            status = pstmt.executeUpdate();

        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(null, pstmt);
        }
    }

    public AssetAllocation getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(AssetAllocation value) {
        assetAllocation = value;
    }
}
