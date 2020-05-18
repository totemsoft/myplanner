/*
 * UnitPriceDataBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.dao.SQLHelper;
import com.argus.financials.assetinvestment.UnitSharePrice;
import com.argus.util.DateTimeUtils;

/**
 * UnitPriceDataBean is responsible for load and store information form the
 * database table "apir_pic".
 * 
 * @author shibaevv
 * @version 0.01
 * @since 19/07/2002
 * 
 * @see com.argus.financials.assetinvestment.UnitSharePrice
 */
public class UnitPriceDataBean implements UnitSharePrice {

    public static final String DATABASE_TABLE_NAME = "unit_price_data";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        UnitPriceDataBean.sqlHelper = sqlHelper;
    }

    // bean properties
    private String identifier; // length: 3

    private int code; // length: 4

    private String price_date; // length: 8 type: datetime

    private java.util.Date priceDate;

    private double entry_price; // length: 8

    private double exit_price; // length: 8

    private String id; // length: 8

    // constants
    private static final int MAX_LENGTH_IDENTIFIER = 3;

    private static final int MAX_LENGTH_CODE = 4;

    private static final int MAX_LENGTH_PRICE_DATE = 8;

    private static final int MAX_LENGTH_ENTRY_PRICE = 8;

    private static final int MAX_LENGTH_EXIT_PRICE = 8;

    private static final int MAX_LENGTH_id = 8;

    private static final int INITIAL_VECTOR_SIZE = 32;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 16;

    /** Creates new ApirPicBean */
    public UnitPriceDataBean() {
    }

    /**
     * Creates a new entry in the "apir_pic" table. The properties for the new
     * entry must be set before creating a new entry.
     */
    public void create() throws java.sql.SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;

        try {
            // get connection
            con = sqlHelper.getConnection();

            // build sql query
            pstmt_StringBuffer.append("INSERT INTO ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer
                    .append("(identifier, code, [price_date], [entry_price], [exit_price]) ");
            pstmt_StringBuffer.append("VALUES ( ?, ?, ?, ?, ? )");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.identifier);
            pstmt.setInt(2, this.code);
            pstmt.setString(3, this.price_date);
            pstmt.setDouble(4, this.entry_price);
            pstmt.setDouble(5, this.exit_price);
            // pstmt.setString ( 6, this.id );

            status = pstmt.executeUpdate();

            // autocommit is off
            //con.commit();

        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            //con.rollback();
            throw e;
        } finally {
            sqlHelper.close(null, pstmt, con);
        }
    }

    /**
     * Stores an entry in the "apir_pic" table. The properties for the entry
     * must be set before storing it.
     */
    public void store() throws java.sql.SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;

        try {
            // get connection
            con = sqlHelper.getConnection();

            // build sql query
            pstmt_StringBuffer.append("UPDATE ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("SET ");
            pstmt_StringBuffer.append("identifier = ?, ");
            pstmt_StringBuffer.append("[price_date] = ?, ");
            pstmt_StringBuffer.append("[entry_price] = ?, ");
            pstmt_StringBuffer.append("[exit_price] = ?  ");
            // pstmt_StringBuffer.append ( "id = ? " );
            pstmt_StringBuffer.append("WHERE code = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.identifier);
            pstmt.setString(2, this.price_date);
            pstmt.setDouble(3, this.entry_price);
            pstmt.setDouble(4, this.exit_price);
            // pstmt.setString ( 5, this.id );
            pstmt.setInt(5, this.code);

            status = pstmt.executeUpdate();

            // autocommit is off
            //con.commit();

        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            //con.rollback();
            throw e;
        } finally {
            sqlHelper.close(null, pstmt, con);
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
        Connection con = null;
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // get connection
            con = sqlHelper.getConnection();

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

            // transform String to int
            int code_int = -1;
            try {
                if (id != null && id.length() > 0) {
                    code_int = Integer.parseInt(id);
                }
            } catch (java.lang.NumberFormatException e) {
            }

            pstmt.setInt(1, code_int);
            pstmt.setInt(2, code_int);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                found = true;
                // get the data
                this.identifier = rs.getString("identifier");
                this.code = rs.getInt("code");
                // this.price_date = rs.getString ( "price_date" );
                setPriceDate2(rs.getDate("price_date"));
                this.entry_price = rs.getDouble("entry_price");
                this.exit_price = rs.getDouble("exit_price");
                this.id = rs.getString("id");
            }

            // autocommit is off
            //con.commit();

        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            //con.rollback();
            throw e;
        } finally {
            sqlHelper.close(null, pstmt, con);
        }

        return found;
    }

    /**
     * Loads an entry from the "unit_price_data" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param code_id _
     *            use the code column as identifier
     * @return true = found an entry
     */
    public boolean findByApirPic(String id) throws java.sql.SQLException {
        return findByColumnName("apir_pic", id, "");
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
        Connection con = null;
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // get connection
            con = sqlHelper.getConnection();

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

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.identifier = rs.getString("identifier");
                this.code = rs.getInt("code");
                // this.price_date = rs.getString ( "price_date" );
                setPriceDate2(rs.getDate("price_date"));
                this.entry_price = rs.getDouble("entry_price");
                this.exit_price = rs.getDouble("exit_price");
                this.id = rs.getString("id");

                found = true;
            }

            // autocommit is off
            //con.commit();

        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            //con.rollback();
            throw e;
        } finally {
            sqlHelper.close(null, pstmt, con);
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
    public String getIdentifier() {
        return this.identifier;
    }

    public int getCode() {
        return this.code;
    }

    public String getPrice_date() {
        return this.price_date;
    }

    public double getEntry_price() {
        return this.entry_price;
    }

    public double getExit_price() {
        return this.exit_price;
    }

    public String getId() {
        return this.id;
    }

    /*
     * ----------------------------------------------------------------------------------
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setIdentifier(String new_identifier) {
        // truncate string if it's neccessary
        if (new_identifier.length() >= MAX_LENGTH_IDENTIFIER) {
            this.identifier = new_identifier
                    .substring(0, MAX_LENGTH_IDENTIFIER);
        } else {
            this.identifier = new_identifier;
        }
    }

    public void setCode(int new_code) {
        this.code = new_code;
    }

    public void setPrice_date(String new_price_date) {
        this.price_date = new_price_date;
    }

    public void setEntry_price(double new_entry_price) {
        this.entry_price = new_entry_price;
    }

    public void setExit_price(double new_exit_price) {
        this.exit_price = new_exit_price;
    }

    public void setId(String new_id) {
        this.id = new_id;
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
        return this.getEntry_price();
    }

    /**
     * return the close price
     */
    public double getClosePrice() {
        return this.getExit_price();
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
