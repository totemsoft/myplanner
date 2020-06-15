/*
 * IressAssetNameBean.java
 *
 * Created on 19 July 2002, 15:36
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import au.com.totemsoft.dao.SQLHelper;

/**
 * IressAssetNameBean is responsible for loading and storing information form
 * the database table "AssetAllocationData".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 19/07/2002
 */
public class AssetAllocationDataBean {

    public static final String DATABASE_TABLE_NAME = "AssetAllocationData";

    private static final String SEARCH_OPERATOR = "AND";

    // constants
    private static final int INITIAL_VECTOR_SIZE = 64;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 32;

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        AssetAllocationDataBean.sqlHelper = sqlHelper;
    }

    // bean properties
    public int code;

    public double inCash;

    public double inFixedInterest;

    public double inAustShares;

    public double inIntnlShares;

    public double inProperty;

    public double inOther;

    public String dataDate;

    public double id;

    /** Creates new IressAssetNameBean */
    public AssetAllocationDataBean() {
    }

    /**
     * Creates a new entry in the "apir_pic" table. The properties for the new
     * entry must be set before creating a new entry.
     */
    public void create() throws java.sql.SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("(");
        sql.append("code, inCash, inFixedInterest, inAustShares, inIntnlShares,  ");
        sql.append("inProperty, inOther, dataDate, id");
        sql.append(")");
        sql.append("VALUES ");
        sql.append("(");
        sql.append("?, ?, ?, ?, ?, ?, ?, ?, ? ");
        sql.append(")");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setInt(1, this.code);
            pstmt.setDouble(2, this.inCash);
            pstmt.setDouble(3, this.inFixedInterest);
            pstmt.setDouble(4, this.inAustShares);
            pstmt.setDouble(5, this.inIntnlShares);
            pstmt.setDouble(6, this.inProperty);
            pstmt.setDouble(7, this.inOther);
            pstmt.setString(8, this.dataDate);
            pstmt.setDouble(9, this.id);
            int status = pstmt.executeUpdate();
        }
    }

    /**
     * Loads an entry from the "iress_asset_name" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id _
     *            use the apir_pic column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(String code) throws java.sql.SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [code] = ? ");
        sql.append("AND [DataDate] = ");
        sql.append("(");
        sql.append("SELECT max([DataDate]) ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [code] = ? ");
        sql.append(")");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, code);
            pstmt.setString(2, code);
            ResultSet rs = pstmt.executeQuery();
            // do we have any result?
            if (rs.next()) {
                this.code = rs.getInt("code");
                this.inCash = rs.getDouble("InCash");
                this.inFixedInterest = rs.getDouble("InFixedInterest");
                this.inAustShares = rs.getDouble("InAustShares");
                this.inIntnlShares = rs.getDouble("InIntnlShares");
                this.inProperty = rs.getDouble("InProperty");
                this.inOther = rs.getDouble("InOther");
                this.dataDate = rs.getString("DataDate");
                this.id = rs.getDouble("id");
                found = true;
            }
            rs.close();
        }
        return found;
    }

    /**
     * Loads an entry from the "iress_asset_name" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id _
     *            use the apir_pic column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(int code) throws java.sql.SQLException {
        return this.findByCode(new Integer(code).toString());
    }

    /**
     * Loads an entry from the "iress_asset_name" table. The given column name
     * and id is used for identification. The first matching (column contains
     * id) entry will be loaded.
     * 
     * @param column_name _
     *            the column name for the search
     * @param id _
     *            the identification
     * @return true = found an entry
     */
    private boolean findByColumnName(String column_name, String id)
            throws java.sql.SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE ");
        sql.append("[" + column_name + "] = ?");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, id);
            pstmt.setString(2, id);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.code = rs.getInt("code");
                    this.inCash = rs.getDouble("InCash");
                    this.inFixedInterest = rs.getDouble("InFixedInterest");
                    this.inAustShares = rs.getDouble("InAustShares");
                    this.inIntnlShares = rs.getDouble("InIntnlShares");
                    this.inProperty = rs.getDouble("InProperty");
                    this.inOther = rs.getDouble("InOther");
                    this.dataDate = rs.getString("DataDate");
                    this.id = rs.getDouble("id");
                    found = true;
                }
            }
        }
        return found;
    }

    /*
     * ----------------------------------------------------------------------------------
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setCode(int value) {
        this.code = value;
    }

    public void setInCash(double value) {
        this.inCash = value;
    }

    public void setInFixedInterest(double value) {
        this.inFixedInterest = value;
    }

    public void setInAustShares(double value) {
        this.inAustShares = value;
    }

    public void setInIntnlShares(double value) {
        this.inIntnlShares = value;
    }

    public void setInProperty(double value) {
        this.inProperty = value;
    }

    public void setInOther(double value) {
        this.inOther = value;
    }

    public void setDataDate(String value) {
        this.dataDate = value;
    }

    public void setId(double value) {
        this.id = value;
    }

    /*
     * ----------------------------------------------------------------------------------
     * get methods
     * ----------------------------------------------------------------------------------
     */
    public int getCode() {
        return this.code;
    }

    public double getInCash() {
        return this.inCash;
    }

    public double getInFixedInterest() {
        return this.inFixedInterest;
    }

    public double getInAustShares() {
        return this.inAustShares;
    }

    public double getInIntnlShares() {
        return this.inIntnlShares;
    }

    public double getInProperty() {
        return this.inProperty;
    }

    public double getInOther() {
        return this.inOther;
    }

    public String getDataDate() {
        return this.dataDate;
    }

    public double getId() {
        return this.id;
    }

}
