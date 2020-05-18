/*
 * ManagerDataBean.java
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
 * ApirPicBean is responsible for load and store information form the database table "manager_data".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 15/08/2002
 */
public class ManagerDataBean {

    public static final String DATABASE_TABLE_NAME = "manager_data";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        ManagerDataBean.sqlHelper = sqlHelper;
    }

    // bean properties
    private String identifier; // length: 2

    private String country_code; // length: 3

    private String code; // length: 7

    private String full_name; // length: 50

    private String status; // length: 30

    private String short_name; // length: 30

    private String brief_name; // length: 11

    private String very_brief_name; // length: 5

    private String group_code; // length: 7

    private String consolidated_code; // length: 7

    private java.sql.Timestamp id; // length: 8

    /** Creates new ApirPicBean */
    public ManagerDataBean() {
    }

    /**
     * Loads an entry from the "product-information" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param code_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(String code_id) throws java.sql.SQLException {
        return this.findByColumnName("code", code_id);
    }

    /**
     * Loads an entry from the "apir-pic" table. The given column name and id is
     * used for identification. The first matching (column contains id) entry
     * will be loaded.
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
                this.identifier = rs.getString("identifier");
                this.country_code = rs.getString("country_code");
                this.code = rs.getString("code");
                this.full_name = rs.getString("full_name");
                this.status = rs.getString("status");
                this.short_name = rs.getString("short_name");
                this.brief_name = rs.getString("brief_name");
                this.very_brief_name = rs.getString("very_brief_name");
                this.group_code = rs.getString("group_code");
                this.consolidated_code = rs.getString("consolidated_code");
                // this.id = rs.getTimestamp( "id" );

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

    /*
     * ----------------------------------------------------------------------------------
     * get methods
     * ----------------------------------------------------------------------------------
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public String getCountryCode() {
        return this.country_code;
    }

    public String getCode() {
        return this.code;
    }

    public String getFullName() {
        return this.full_name;
    }

    public String getStatus() {
        return this.status;
    }

    public String getShortName() {
        return this.short_name;
    }

    public String getBriefName() {
        return this.brief_name;
    }

    public String getVeryBriefName() {
        return this.very_brief_name;
    }

    public String getGroupCode() {
        return this.group_code;
    }

    public String getConsolidatedCode() {
        return this.consolidated_code;
    }

    public java.sql.Timestamp getId() {
        return this.id;
    }

}
