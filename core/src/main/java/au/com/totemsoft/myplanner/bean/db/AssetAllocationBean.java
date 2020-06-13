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
        StringBuffer sql = new StringBuffer();
        int status = 0;
        if (assetAllocation.getAssetAllocationID() == null
         || assetAllocation.getAssetAllocationID().intValue() < 0) {
            // insert new row
            sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append("[" + DATABASE_TABLE_NAME + "] ");
            sql.append("( Amount, InCash, InFixedInterest, "
                    + "InAustShares, InIntnlShares, InProperty, InOther, "
                    + "Include ) ");
            sql.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ");

            // set and execute query
            PreparedStatement pstmt = con.prepareStatement(sql.toString());
            if (assetAllocation.getAmount() == null)
                pstmt.setNull(1, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(1, assetAllocation.getAmount().doubleValue());

            if (assetAllocation.getInCash() == null)
                pstmt.setNull(2, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(2, assetAllocation.getInCash().doubleValue());

            if (assetAllocation.getInFixedInterest() == null)
                pstmt.setNull(3, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(3, assetAllocation.getInFixedInterest().doubleValue());

            if (assetAllocation.getInAustShares() == null)
                pstmt.setNull(4, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(4, assetAllocation.getInAustShares().doubleValue());

            if (assetAllocation.getInIntnlShares() == null)
                pstmt.setNull(5, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(5, assetAllocation.getInIntnlShares().doubleValue());

            if (assetAllocation.getInProperty() == null)
                pstmt.setNull(6, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(6, assetAllocation.getInProperty().doubleValue());

            if (assetAllocation.getInOther() == null)
                pstmt.setNull(7, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(7, assetAllocation.getInOther().doubleValue());

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
            sql = new StringBuffer();
            sql.append("SELECT MAX(AssetAllocationID) FROM ");
            sql.append("[" + DATABASE_TABLE_NAME + "]");
            pstmt = con.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            // do we have any result?
            if (rs.next()) {
                assetAllocation.setAssetAllocationID(rs.getInt(1));
            }
            rs.close();
            pstmt.close();
        } else {
            // update row
            sql = new StringBuffer();
            sql.append("UPDATE ");
            sql.append("[" + DATABASE_TABLE_NAME + "] ");
            sql.append("SET ");
            sql.append("Amount = ?, ");
            sql.append("InCash = ?, ");
            sql.append("InFixedInterest = ?, ");
            sql.append("InAustShares = ?, ");
            sql.append("InIntnlShares = ?, ");
            sql.append("InProperty = ?, ");
            sql.append("InOther = ?, ");
            sql.append("Include = ? ");
            sql.append("WHERE AssetAllocationID = ? ");

            // set and execute query
            PreparedStatement pstmt = con.prepareStatement(sql.toString());
            if (assetAllocation.getAmount() == null)
                pstmt.setNull(1, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(1, assetAllocation.getAmount().doubleValue());

            if (assetAllocation.getInCash() == null)
                pstmt.setNull(2, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(2, assetAllocation.getInCash().doubleValue());

            if (assetAllocation.getInFixedInterest() == null)
                pstmt.setNull(3, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(3, assetAllocation.getInFixedInterest().doubleValue());

            if (assetAllocation.getInAustShares() == null)
                pstmt.setNull(4, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(4, assetAllocation.getInAustShares().doubleValue());

            if (assetAllocation.getInIntnlShares() == null)
                pstmt.setNull(5, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(5, assetAllocation.getInIntnlShares().doubleValue());

            if (assetAllocation.getInProperty() == null)
                pstmt.setNull(6, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(6, assetAllocation.getInProperty().doubleValue());

            if (assetAllocation.getInOther() == null)
                pstmt.setNull(7, java.sql.Types.DOUBLE);
            else
                pstmt.setDouble(7, assetAllocation.getInOther().doubleValue());

            if (assetAllocation.getInclude() == null) {
                pstmt.setNull(8, java.sql.Types.CHAR);
            } else {
                if (assetAllocation.getInclude().equals(Boolean.TRUE))
                    pstmt.setString(8, BooleanCode.rcYES.getCode());
                else
                    pstmt.setNull(8, java.sql.Types.CHAR);
            }

            pstmt.setInt(9, assetAllocation.getAssetAllocationID().intValue());
            status = pstmt.executeUpdate();
            pstmt.close();
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
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [AssetAllocationID] = ? ");
        try (PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setInt(1, id.intValue());
            ResultSet rs = pstmt.executeQuery();
            // do we have any result?
            if (rs.next()) {
                found = true;
                // get the data
                assetAllocation.setAssetAllocationID(rs.getInt("AssetAllocationID"));
                assetAllocation.setAmount(rs.getDouble("Amount"));
                assetAllocation.setInCash(rs.getDouble("InCash"));
                assetAllocation.setInFixedInterest(rs.getDouble("InFixedInterest"));
                assetAllocation.setInAustShares(rs.getDouble("InAustShares"));
                assetAllocation.setInIntnlShares(rs.getDouble("InIntnlShares"));
                assetAllocation.setInProperty(rs.getDouble("InProperty"));
                assetAllocation.setInOther(rs.getDouble("InOther"));
                assetAllocation.setInclude(rs.getString("Include") != null);
            } else {
                throw new ObjectNotFoundException("Can not find Asset Allocation ID: " + id);
            }
            rs.close();
        }
        return found;
    }

    /**
     */
    public void delete(Integer aaid) throws java.sql.SQLException {
        try (Connection con = sqlHelper.getConnection();) {
            //
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE Financial SET AssetAllocationID = NULL ");
            sql.append("WHERE AssetAllocationID = ? ");
            try (PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
                pstmt.setInt(1, aaid.intValue());
                int status = pstmt.executeUpdate();
            }
            //
            sql = new StringBuffer();
            sql.append("DELETE FROM ");
            sql.append("[" + DATABASE_TABLE_NAME + "] ");
            sql.append("WHERE AssetAllocationID = ? ");
            try (PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
                pstmt.setInt(1, aaid.intValue());
                int status = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    public AssetAllocation getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(AssetAllocation value) {
        assetAllocation = value;
    }
}
