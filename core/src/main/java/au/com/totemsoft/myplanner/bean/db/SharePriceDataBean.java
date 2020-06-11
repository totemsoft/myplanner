/*
 * SharePriceDataBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.assetinvestment.UnitSharePrice;
import au.com.totemsoft.util.DateTimeUtils;

/**
 * SharePriceDataBean is responsible for load and store information form the
 * database table "share_price_data".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 19/07/2002
 * 
 * @see au.com.totemsoft.myplanner.assetinvestment.UnitSharePrice
 */
public class SharePriceDataBean implements UnitSharePrice {

    public static final String DATABASE_TABLE_NAME = "share_price_data";

    private static final String SEARCH_OPERATOR = "OR";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        SharePriceDataBean.sqlHelper = sqlHelper;
    }

    // bean properties
    public String code; // length: 10

    public String price_date; // length: 8 type: datetime

    private java.util.Date priceDate;

    public double open_price; // length: 8

    public double close_price; // length: 8

    // constants
    private static final int MAX_LENGTH_CODE = 10;

    private static final int MAX_LENGTH_PRICE_DATE = 8;

    private static final int MAX_LENGTH_ENTRY_PRICE = 8;

    private static final int MAX_LENGTH_EXIT_PRICE = 8;

    private static final int INITIAL_VECTOR_SIZE = 32;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 16;

    /** Creates new ApirPicBean */
    public SharePriceDataBean() {
    }

    /**
     * Creates a new entry in the "share_price_data" table. The properties for
     * the new entry must be set before creating a new entry.
     */
    public void create() throws java.sql.SQLException {
        PreparedStatement pstmt = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;
        try (Connection con = sqlHelper.getConnection();) {
            // build sql query
            pstmt_StringBuffer.append("INSERT INTO ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer
                    .append("(code, [price_date], [open_price], [close_price]) ");
            pstmt_StringBuffer.append("VALUES ( ?, ?, ?, ? )");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.code);
            pstmt.setString(2, this.price_date);
            pstmt.setDouble(3, this.open_price);
            pstmt.setDouble(4, this.close_price);

            status = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(null, pstmt);
        }
    }

    /**
     * Stores an entry in the "share_price_data" table. The properties for the
     * entry must be set before storing it.
     */
    public void store() throws java.sql.SQLException {
        PreparedStatement pstmt = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;
        try (Connection con = sqlHelper.getConnection();) {
            // build sql query
            pstmt_StringBuffer.append("UPDATE ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("SET ");
            pstmt_StringBuffer.append("[price_date] = ?, ");
            pstmt_StringBuffer.append("[open_price] = ?, ");
            pstmt_StringBuffer.append("[close_price] = ?  ");
            pstmt_StringBuffer.append("WHERE code = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.price_date);
            pstmt.setDouble(2, this.open_price);
            pstmt.setDouble(3, this.close_price);
            pstmt.setString(4, this.code);

            status = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(null, pstmt);
        }
    }

    /**
     * Loads an entry from the "unit_price_data" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param code_id _
     *            use the code column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(String id) throws java.sql.SQLException {
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        try (Connection con = sqlHelper.getConnection();) {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [code] = ? ");
            pstmt_StringBuffer.append("AND [price_date] = ");
            pstmt_StringBuffer.append("(");
            pstmt_StringBuffer.append("SELECT max([price_date]) ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [code] = ? ");
            pstmt_StringBuffer.append(")");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, id);
            pstmt.setString(2, id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.code = rs.getString("code");
                // this.price_date = rs.getString ( "price_date" );
                setPriceDate2(rs.getDate("price_date"));
                this.open_price = rs.getDouble("open_price");
                this.close_price = rs.getDouble("close_price");

                found = true;
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(null, pstmt);
        }

        return found;
    }

    /**
     * Loads an entry from the "unit_price_data" table. The given column name
     * and id is used for identification. The first matching (column contains
     * id) entry will be loaded.
     * 
     * @param column_name _
     *            the column name for the search
     * @param id _
     *            the identification
     * @param add_sql_str _
     *            additional sql statment for example: ORDER BY ...
     * @return true = found an entry
     */
    private boolean findByColumnName(String column_name, String id,
            String add_sql_str) throws java.sql.SQLException {
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        try (Connection con = sqlHelper.getConnection();) {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [" + column_name + "] = ? ");

            if (add_sql_str != null) {
                pstmt_StringBuffer.append(" " + add_sql_str);
            }

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, id);
            pstmt.setString(2, id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.code = rs.getString("code");
                // this.price_date = rs.getString ( "price_date" );
                setPriceDate2(rs.getDate("price_date"));
                this.open_price = rs.getDouble("open_price");
                this.close_price = rs.getDouble("close_price");

                found = true;
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(null, pstmt);
        }

        return found;
    }

    /**
     * Check if the given String is null, if so it creates a new String which
     * contains "". If the given String was not null, it creates a new String
     * with the contents of the given one.
     * 
     * @return a new String
     */
    private String checkString(String str) {
        String str_to_return = new String("");

        // check if String is null
        if (str != null) {
            str_to_return = new String(str);
        }

        return str_to_return;
    }

    /*
     * ----------------------------------------------------------------------------------
     * get methods
     * ----------------------------------------------------------------------------------
     */
    public String getCode() {
        return this.code;
    }

    public String getPrice_date() {
        return this.price_date;
    }

    public double getOpen_price() {
        return this.open_price;
    }

    public double getClose_price() {
        return this.close_price;
    }

    /*
     * ----------------------------------------------------------------------------------
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setCode(String new_code) {
        // truncate string if it's neccessary
        if (new_code.length() >= MAX_LENGTH_CODE) {
            this.code = new_code.substring(0, MAX_LENGTH_CODE);
        } else {
            this.code = new_code;
        }
    }

    public void setPrice_date(String new_price_date) {
        this.price_date = new_price_date;
    }

    public void setOpen_price(double new_open_price) {
        this.open_price = new_open_price;
    }

    public void setClose_price(double new_close_price) {
        this.close_price = new_close_price;
    }

    public java.util.Date getPriceDate2() {
        return priceDate;
    }

    public void setPriceDate2(java.util.Date value) {
        priceDate = value;
        price_date = DateTimeUtils.getJdbcDate(priceDate);
    }

    /*
     * ----------------------------------------------------------------------------------
     * implement the "UnitSharePrice" interface methods
     * ----------------------------------------------------------------------------------
     */
    /**
     * return the open price
     */
    public double getOpenPrice() {
        return this.getOpen_price();
    }

    /**
     * return the close price
     */
    public double getClosePrice() {
        return this.getClose_price();
    }

    /**
     * return the date for the open and close price
     */
    public String getPriceDate() {
        return this.getPrice_date();
    }

    /**
     * finds a unit/share in the database
     */
    public boolean findUnitShareByCode(String code_id)
            throws java.sql.SQLException {
        return this.findByCode(code_id);
    }

    /**
     * get UnitSharePrice object
     */
    public void getUnitSharePrice() {
    }

}
