/*
 * FundTypeBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.dao.SQLHelper;

/**
 * ApirPicBean is responsible for load and store information form the database
 * table "FundTypeBean".
 * 
 * @author shibaevv
 * @version 0.01
 * @since 25/07/2002
 */
public class FundTypeBean {

    public static final String DATABASE_TABLE_NAME = "FundType";

    private static final String SEARCH_OPERATOR = "OR";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        FundTypeBean.sqlHelper = sqlHelper;
    }

    // bean properties
    private int fundTypeID; // length: 4

    private String fundTypeDesc; // length: 50

    // constants
    private static final int MAX_LENGTH_FUNDTYPEID = 4;

    private static final int MAX_LENGTH_FUNDTYPEDESC = 50;

    /** Creates new ApirPicBean */
    public FundTypeBean() {
    }

    /**
     * Creates a new entry in the "FinancialCodeBean" table. The properties for
     * the new entry must be set before creating a new entry. (Updates the
     * FinancialCodeID!)
     */
    public void create() throws java.sql.SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;

        try {
            // get connection
            con = sqlHelper.getConnection();

            // get max. FinancialCodeID
            // build sql query
            pstmt_StringBuffer.append("SELECT MAX(FundTypeID) ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());
            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.fundTypeID = rs.getInt(1);
                // create new "unique" FinancialCodeID
                this.fundTypeID++;
            }
            // close ResultSet and PreparedStatement
            sqlHelper.close(rs, pstmt);

            // build sql query
            pstmt_StringBuffer.append("INSERT INTO ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("(FundTypeID, FundTypeDesc) ");
            pstmt_StringBuffer.append("VALUES ( ?, ? )");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, this.fundTypeID);
            pstmt.setString(2, this.fundTypeDesc);

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
     * Stores an entry in the "FinancialCodeBean" table. The properties for the
     * entry must be set before storing it.
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
            pstmt_StringBuffer.append("FundTypeDesc = ? ");
            pstmt_StringBuffer.append("WHERE FundTypeID = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.fundTypeDesc);
            pstmt.setInt(2, this.fundTypeID);

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
     * Loads an entry from the "FinancialCode" table. The "FinancialCodeID"
     * column is used for identification. The first matching entry will be
     * loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByFundTypeID(String id) throws java.sql.SQLException {
        return this.findByColumnName("FundTypeID", id);
    }

    /**
     * Loads an entry from the "FinancialCode" table. The "FinancialTypeID"
     * column is used for identification. The first matching entry will be
     * loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByFundTypeDesc(String id) throws java.sql.SQLException {
        return this.findByColumnName("FundTypeDesc", id);
    }

    /**
     * Loads an entry from the "FinancialCode" table. The given column name and
     * id is used for identification. The first matching (column contains id)
     * entry will be loaded.
     * 
     * @param column_name -
     *            the column name for the search
     * @param id -
     *            the identification
     * @return true = found an entry
     */
    private boolean findByColumnName(String column_name, String id)
            throws java.sql.SQLException {
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

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.fundTypeID = rs.getInt("FundTypeID");
                this.fundTypeDesc = rs.getString("FundTypeDesc");

                found = true;
            }

            // autocommit is off
            //con.commit();

        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            //con.rollback();
            throw e;
        } finally {
            sqlHelper.close(rs, pstmt, con);
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
    public String getFundTypeDesc() {
        return this.fundTypeDesc;
    }

    public int getFundTypeID() {
        return this.fundTypeID;
    }

    /*
     * ----------------------------------------------------------------------------------
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setFundTypeDesc(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_FUNDTYPEDESC) {
            this.fundTypeDesc = value.substring(0, MAX_LENGTH_FUNDTYPEDESC);
        } else {
            this.fundTypeDesc = value;
        }
    }

    public void setFinancialTypeID(int value) {
        this.fundTypeID = value;
    }

}
