/*
 * ProductInformationBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ApirPicBean is responsible for load and store information form the database
 * table "product-information".
 * 
 * @author shibaevv
 * @version 0.01
 * @since 15/08/2002
 */
public class ProductInformationBean {

    public static final String DATABASE_TABLE_NAME = "product-information";

    // bean properties
    private String identifier; // length: 3

    private int code; // length: 4

    private String full_name; // length: 50

    private String country_code; // length: 3

    private String tax_group_code; // length: 2

    private String manager_code; // length: 7

    private int group_code; // length: 4

    private String short_name; // length: 30

    private String brief_name; // length: 18

    private String legal_type_code; // length: 2

    private String region_code; // length: 2

    private String asset_type_code; // length: 3

    private String cash_distribution_code; // length: 1

    private String trustee_code; // length: 7

    private String custodian_code; // length: 7

    private java.sql.Timestamp commencement_date; // length: 8

    private java.sql.Timestamp data_date; // length: 8

    private int stock_exchange; // length: 1 (1 bit)

    private int guarantees; // length: 1 (1 bit)

    private int unit_linked; // length: 1 (1 bit)

    private String valuation_frequency; // length: 20

    private int declared_yield; // length: 1 (1 bit)

    private int rate_of_return_advance; // length: 1 (1 bit)

    private String category; // length: 4

    private String subcategory; // length: 4

    private String manager_group_code; // length: 7

    private int gearing; // length: 1 (1 bit)

    private double gearing_max; // length: 8

    private String gearing_comments; // length: 255

    private String special_features; // length: 255

    private java.sql.Timestamp id; // length: 8

    /** Creates new ApirPicBean */
    public ProductInformationBean() {
    }

    /**
     * Loads an entry from the "product-information" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param code_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(int code_id) throws java.sql.SQLException {
        return this.findByColumnName("code", new Integer(code_id).toString());
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
            con = DBManager.getInstance().getConnection();

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
                this.code = rs.getInt("code");
                this.full_name = rs.getString("full-name");
                this.country_code = rs.getString("country-code");
                this.tax_group_code = rs.getString("tax-group-code");
                this.manager_code = rs.getString("manager-code");
                this.group_code = rs.getInt("group-code");
                this.short_name = rs.getString("short-name");
                this.brief_name = rs.getString("brief-name");
                this.legal_type_code = rs.getString("legal-type-code");
                this.region_code = rs.getString("region-code");
                this.asset_type_code = rs.getString("asset-type-code");
                this.cash_distribution_code = rs
                        .getString("cash-distribution-code");
                this.trustee_code = rs.getString("trustee-code");
                this.custodian_code = rs.getString("custodian-code");
                // this.commencement_date = rs.getTimestamp( "commencement-date"
                // );
                // this.data_date = rs.getTimestamp( "data-date" );
                this.stock_exchange = rs.getInt("stock-exchange");
                this.guarantees = rs.getInt("guarantees");
                this.unit_linked = rs.getInt("unit-linked");
                this.valuation_frequency = rs.getString("valuation-frequency");
                this.declared_yield = rs.getInt("declared-yield");
                this.rate_of_return_advance = rs
                        .getInt("rate-of-return-advance");
                this.category = rs.getString("category");
                this.subcategory = rs.getString("subcategory");
                this.manager_group_code = rs.getString("manager-group-code");
                this.gearing = rs.getInt("gearing");
                this.gearing_max = rs.getDouble("gearing-max");
                this.gearing_comments = rs.getString("gearing-comments");
                this.special_features = rs.getString("special-features");
                // this.id = rs.getTimestamp( "id" );

                found = true;
            }

            // autocommit is off
            con.commit();

        } catch (SQLException e) {
            printSQLException(e);
            con.rollback();
            throw e;
        } finally {
            closeRsSql(null, pstmt);
        }

        return found;
    }

    /**
     * 
     * @param code
     * @param full_name
     * @return true = found an entry
     */
    public boolean containsCodeFullName(int code, String full_name)
            throws java.sql.SQLException {
        Connection con = null;
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // get connection
            con = DBManager.getInstance().getConnection();

            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [code] = ? ");
            pstmt_StringBuffer.append("AND ");
            pstmt_StringBuffer.append("[full-name] = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, code);
            pstmt.setString(2, full_name);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                found = true;
            }

            // autocommit is off
            con.commit();

        } catch (SQLException e) {
            printSQLException(e);
            con.rollback();
            throw e;
        } finally {
            closeRsSql(null, pstmt);
        }

        return found;
    }

    /**
     * Closes a given ResultSet and PreparedStatement.
     * 
     * @param rs -
     *            the ResultSet to close
     * @param pstmt -
     *            the PreparedStatement to close
     */
    private void closeRsSql(ResultSet rs, PreparedStatement pstmt) {
        try {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        } catch (java.sql.SQLException e) {
            // do nothing here
        }
    }

    /**
     * Prints the SQLException's messages, SQLStates and ErrorCode to System.err
     * 
     * @param extends -
     *            a SQLException
     */
    private void printSQLException(java.sql.SQLException e) {
        System.err.println("\n--- SQLException caught ---\n");

        while (e != null) {
            e.printStackTrace(System.err);

            System.err.println("Message:   " + e.getMessage());
            System.err.println("SQLState:  " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());

            e = e.getNextException();

        }

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

    public String getFullName() {
        return this.full_name;
    }

    public String getCountryCode() {
        return this.country_code;
    }

    public String getTaxGroupCode() {
        return this.tax_group_code;
    }

    public String getManagerCode() {
        return this.manager_code;
    }

    public int getGroupCode() {
        return this.group_code;
    }

    public String getShortName() {
        return this.short_name;
    }

    public String getBriefName() {
        return this.brief_name;
    }

    public String getLegalTypeCode() {
        return this.legal_type_code;
    }

    public String getRegionCode() {
        return this.region_code;
    }

    public String getAssetTypeCode() {
        return this.asset_type_code;
    }

    public String getCashDistributionCode() {
        return this.cash_distribution_code;
    }

    public String getTrusteeCode() {
        return this.trustee_code;
    }

    public String getCustodianCode() {
        return this.custodian_code;
    }

    public java.sql.Timestamp getCommencementDate() {
        return this.commencement_date;
    }

    public java.sql.Timestamp getDataDate() {
        return this.data_date;
    }

    public int getStockExchange() {
        return this.stock_exchange;
    }

    public int getGuarantees() {
        return this.guarantees;
    }

    public int getUnitLinked() {
        return this.unit_linked;
    }

    public String getValuationFrequency() {
        return this.valuation_frequency;
    }

    public int getDeclaredYield() {
        return this.declared_yield;
    }

    public int getRateOfReturnAdvance() {
        return this.rate_of_return_advance;
    }

    public String getCategory() {
        return this.category;
    }

    public String getSubcategory() {
        return this.subcategory;
    }

    public String getManagerGroupCode() {
        return this.manager_group_code;
    }

    public int getGearing() {
        return this.gearing;
    }

    public double getGearingMax() {
        return this.gearing_max;
    }

    public String getGearingComments() {
        return this.gearing_comments;
    }

    public String getSpecialFeatures() {
        return this.special_features;
    }

    public java.sql.Timestamp getId() {
        return this.id;
    }

}
