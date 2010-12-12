/*
 * IressAssetNameBean.java
 *
 * Created on 19 July 2002, 15:36
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.argus.financials.assetinvestment.AvailableInvestmentsTableRow;

/**
 * IressAssetNameBean is responsible for load and store information form the
 * database table "iress-asset-name".
 * 
 * @author shibaevv
 * @version 0.01
 * @since 19/07/2002
 */
public class IressAssetNameBean {

    public static final String DATABASE_TABLE_NAME = "iress-asset-name";

    private static final String SEARCH_OPERATOR = "AND";

    // constants
    private static final int INITIAL_VECTOR_SIZE = 64;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 32;

    // bean properties
    private String code;

    private String asset_full_name;

    private String issuerName;

    private String issuerAbbName;

    private String issuerShortName;

    private String issuerType;

    private double securityType;

    private String exchange;

    private double industrySubgroup;

    private String description;

    private String shortDescription;

    private String abbDescription;

    private double assetBacking;

    private String issuerCode;

    private String issuerExchange;

    private String industrySubgroupDesc;

    private String industryGroupDesc;

    private String isin;

    private double gics;

    // constants
    private static final int MAX_LENGTH_CODE = 10;

    private static final int MAX_LENGTH_ASSET_FULL_NAME = 60;

    private static final int MAX_LENGTH_ISSUER_NAME = 60;

    private static final int MAX_LENGTH_ISSUER_ABB_NAME = 40;

    private static final int MAX_LENGTH_ISSUER_SHORT_NAME = 40;

    private static final int MAX_LENGTH_ISSUER_TYPE = 20;

    private static final int MAX_LENGTH_SECURITY_TYPE = 8;

    private static final int MAX_LENGTH_EXCHANGE = 10;

    private static final int MAX_LENGTH_INDUSTRY_SUBGROUP = 8;

    private static final int MAX_LENGTH_DESCRIPTION = 60;

    private static final int MAX_LENGTH_SHORT_DESCRIPTION = 40;

    private static final int MAX_LENGTH_ABB_DESCRIPTION = 20;

    private static final int MAX_LENGTH_ASSET_BACKING = 8;

    private static final int MAX_LENGTH_ISSUER_CODE = 20;

    private static final int MAX_LENGTH_ISSUER_EXCGANGE = 20;

    private static final int MAX_LENGTH_INDUSTRY_SUBGROUP_DESC = 20;

    private static final int MAX_LENGTH_INDUSTRY_GROUP_DESC = 20;

    private static final int MAX_LENGTH_ISIN = 20;

    private static final int MAX_LENGTH_GICS = 8;

    /** Creates new IressAssetNameBean */
    public IressAssetNameBean() {
    }

    /**
     * Creates a new entry in the "apir-pic" table. The properties for the new
     * entry must be set before creating a new entry.
     */
    public void create() throws java.sql.SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;

        try {
            // get connection
            con = DBManager.getInstance().getConnection();

            // build sql query
            pstmt_StringBuffer.append("INSERT INTO ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("(");
            pstmt_StringBuffer.append("code, [asset-full-name], ");
            pstmt_StringBuffer
                    .append("issuerName, issuerAbbName, issuerShortName, issuerType, ");
            pstmt_StringBuffer.append("securityType, ");
            pstmt_StringBuffer.append("exchange, industrySubgroup, ");
            pstmt_StringBuffer
                    .append("description, shortDescription, abbDescription, ");
            pstmt_StringBuffer.append("assetBacking, ");
            pstmt_StringBuffer.append("issuerCode, issuerExchange, ");
            pstmt_StringBuffer
                    .append("industrySubgroupDesc, industryGroupDesc, ");
            pstmt_StringBuffer.append("isin, gics ");
            pstmt_StringBuffer.append(")");
            pstmt_StringBuffer.append("VALUES ");
            pstmt_StringBuffer.append("(");
            pstmt_StringBuffer.append("?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ");
            pstmt_StringBuffer.append("?, ?, ?, ?, ?, ?, ?, ?, ? ");
            pstmt_StringBuffer.append(")");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.code);
            pstmt.setString(2, this.asset_full_name);
            pstmt.setString(3, this.issuerName);
            pstmt.setString(4, this.issuerAbbName);
            pstmt.setString(5, this.issuerShortName);
            pstmt.setString(6, this.issuerType);
            pstmt.setDouble(7, this.securityType);
            pstmt.setString(8, this.exchange);
            pstmt.setDouble(9, this.industrySubgroup);
            pstmt.setString(10, this.description);
            pstmt.setString(11, this.shortDescription);
            pstmt.setString(12, this.abbDescription);
            pstmt.setDouble(13, this.assetBacking);
            pstmt.setString(14, this.issuerCode);
            pstmt.setString(15, this.issuerExchange);
            pstmt.setString(16, this.industrySubgroupDesc);
            pstmt.setString(17, this.industryGroupDesc);
            pstmt.setString(18, this.isin);
            pstmt.setDouble(19, this.gics);

            status = pstmt.executeUpdate();

            // autocommit is off
            con.commit();

        } catch (SQLException e) {
            printSQLException(e);
            con.rollback();
            throw e;
        } finally {
            closeRsSql(null, pstmt);
        }
    }

    /**
     * Loads an entry from the "iress-asset-name" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(String code_id) throws java.sql.SQLException {
        return this.findByColumnName("code", code_id);
    }

    /**
     * Search in database table "iress-asset" for all rows whose
     * "asset-full-name" contains at least one of the keywords as part of any
     * word in the column. The returned Vector is sorted by the column
     * "asset-full-name".
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchDescription(String keywords)
            throws java.sql.SQLException {
        return this
                .findByKeywordsSearchDescription(keywords, "asset-full-name");
    }

    /**
     * Search in database table "iress-asset" for all rows whose "code" contains
     * at least one of the keywords as part of any word in the column. The
     * returned Vector is sorted by the column "asset-full-name".
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchInvestmentCode(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearchDescription(keywords, "code");
    }

    /**
     * Search in database table "iress-asset" for all rows whose column name
     * (given as a parameter) contains at least one of the keywords (given) as
     * part of any word in the column. The returned Vector is sorted by the
     * given column name.
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @param column_name -
     *            the column name to look for
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchDescription(String keywords,
            String column_name) throws java.sql.SQLException {
        if (keywords == null || (keywords = keywords.trim()).length() == 0)
            return new Vector();
        if (column_name == null
                || (column_name = column_name.trim()).length() == 0)
            return new Vector();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        StringTokenizer keywords_StringTokenizer = null;
        int token_count = 0;
        int number_of_tokens = 0;

        Vector table_rows = new Vector(INITIAL_VECTOR_SIZE,
                INITIAL_VECTOR_GROWTH_SIZE);

        try {
            // get connection
            con = DBManager.getInstance().getConnection();

            // check if we have some keywords
            if (keywords != null && keywords.length() > 0) {
                // split keywords String into tokens (single keywords)
                keywords_StringTokenizer = new StringTokenizer(keywords);
                number_of_tokens = keywords_StringTokenizer.countTokens();

                // build pstmt query
                pstmt_StringBuffer.append("SELECT DISTINCT ");
                pstmt_StringBuffer
                        .append("[code], [asset-full-name], [issuerName] ");
                pstmt_StringBuffer
                        .append("FROM [" + DATABASE_TABLE_NAME + "] ");
                pstmt_StringBuffer.append("WHERE ");
                // add all tokens (single keywords) to the query
                while (keywords_StringTokenizer.hasMoreTokens()) {
                    // do we need to add "AND "?
                    if (token_count > 0 && token_count < number_of_tokens) {
                        pstmt_StringBuffer.append(SEARCH_OPERATOR + " ");
                    }

                    pstmt_StringBuffer.append("([" + column_name + "] LIKE '%"
                            + keywords_StringTokenizer.nextToken() + "%') ");
                    token_count++;
                }
                // order by description
                pstmt_StringBuffer.append("ORDER BY [" + column_name + "] ");

                // set and execute query
                pstmt = con.prepareStatement(pstmt_StringBuffer.toString());
                rs = pstmt.executeQuery();

                // have we any result?
                while (rs.next()) {
                    // create new row
                    AvailableInvestmentsTableRow table_row = new AvailableInvestmentsTableRow();

                    // fill it with data
                    table_row.origin = checkString(DATABASE_TABLE_NAME);
                    table_row.investmentCode = checkString(rs.getString("code"));
                    table_row.description = checkString(rs
                            .getString("asset-full-name"));
                    table_row.code = checkString(rs.getString("code"));
                    table_row.institution = checkString(rs
                            .getString("issuerName"));
                    // store row
                    table_rows.add(table_row);
                }
            }
            // autocommit is off
            con.commit();

        } catch (SQLException e) {
            printSQLException(e);
            con.rollback();
            throw e;
        } finally {
            closeRsSql(rs, pstmt);
        }

        return table_rows;
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

    /**
     * Loads an entry from the "iress-asset-name" table. The given column name
     * and id is used for identification. The first matching (column contains
     * id) entry will be loaded.
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
            pstmt_StringBuffer.append("SELECT * FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE ");
            pstmt_StringBuffer.append("[" + column_name + "] = ?");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.code = rs.getString("code");
                this.asset_full_name = rs.getString("asset-full-name");
                this.issuerName = rs.getString("IssuerName");
                this.issuerAbbName = rs.getString("IssuerAbbName");
                this.issuerShortName = rs.getString("IssuerShortName");
                this.issuerType = rs.getString("IssuerType");
                this.securityType = rs.getDouble("SecurityType");
                this.exchange = rs.getString("Exchange");
                this.industrySubgroup = rs.getDouble("IndustrySubgroup");
                this.description = rs.getString("Description");
                this.shortDescription = rs.getString("ShortDescription");
                this.abbDescription = rs.getString("AbbDescription");
                this.assetBacking = rs.getDouble("AssetBacking");
                this.issuerCode = rs.getString("IssuerCode");
                this.issuerExchange = rs.getString("IssuerExchange");
                this.industrySubgroupDesc = rs
                        .getString("IndustrySubgroupDesc");
                this.industryGroupDesc = rs.getString("IndustryGroupDesc");
                this.isin = rs.getString("ISIN");
                this.gics = rs.getDouble("GICS");

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
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setSecurityType(double new_securityType) {
        this.securityType = new_securityType;
    }

    public void setIndustrySubgroup(double new_industrySubgroup) {
        this.industrySubgroup = new_industrySubgroup;
    }

    public void setAssetBacking(double new_assetBacking) {
        this.assetBacking = new_assetBacking;
    }

    public void setGics(double new_gics) {
        this.gics = new_gics;
    }

    public void setCode(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_CODE) {
            this.code = value.substring(0, MAX_LENGTH_CODE);
        } else {
            this.code = value;
        }
    }

    public void setAsset_full_name(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ASSET_FULL_NAME) {
            this.asset_full_name = value.substring(0,
                    MAX_LENGTH_ASSET_FULL_NAME);
        } else {
            this.asset_full_name = value;
        }
    }

    public void setIssuerName(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISSUER_NAME) {
            this.issuerName = value.substring(0, MAX_LENGTH_ISSUER_NAME);
        } else {
            this.issuerName = value;
        }
    }

    public void setIssuerAbbName(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISSUER_ABB_NAME) {
            this.issuerAbbName = value.substring(0, MAX_LENGTH_ISSUER_ABB_NAME);
        } else {
            this.issuerAbbName = value;
        }
    }

    public void setIssuerShortName(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISSUER_SHORT_NAME) {
            this.issuerShortName = value.substring(0,
                    MAX_LENGTH_ISSUER_SHORT_NAME);
        } else {
            this.issuerShortName = value;
        }
    }

    public void setIssuerType(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISSUER_TYPE) {
            this.issuerType = value.substring(0, MAX_LENGTH_ISSUER_TYPE);
        } else {
            this.issuerType = value;
        }
    }

    public void setExchange(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_EXCHANGE) {
            this.exchange = value.substring(0, MAX_LENGTH_EXCHANGE);
        } else {
            this.exchange = value;
        }
    }

    public void setDescription(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_DESCRIPTION) {
            this.description = value.substring(0, MAX_LENGTH_DESCRIPTION);
        } else {
            this.description = value;
        }
    }

    public void setShortDescription(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_SHORT_DESCRIPTION) {
            this.shortDescription = value.substring(0,
                    MAX_LENGTH_SHORT_DESCRIPTION);
        } else {
            this.shortDescription = value;
        }
    }

    public void setAbbDescription(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ABB_DESCRIPTION) {
            this.abbDescription = value
                    .substring(0, MAX_LENGTH_ABB_DESCRIPTION);
        } else {
            this.abbDescription = value;
        }
    }

    public void setIssuerCode(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISSUER_CODE) {
            this.issuerCode = value.substring(0, MAX_LENGTH_ISSUER_CODE);
        } else {
            this.issuerCode = value;
        }
    }

    public void setIssuerExchange(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISSUER_EXCGANGE) {
            this.issuerExchange = value
                    .substring(0, MAX_LENGTH_ISSUER_EXCGANGE);
        } else {
            this.issuerExchange = value;
        }
    }

    public void setIndustrySubgroupDesc(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_INDUSTRY_SUBGROUP_DESC) {
            this.industrySubgroupDesc = value.substring(0,
                    MAX_LENGTH_INDUSTRY_SUBGROUP_DESC);
        } else {
            this.industrySubgroupDesc = value;
        }
    }

    public void setIndustryGroupDesc(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_INDUSTRY_GROUP_DESC) {
            this.industryGroupDesc = value.substring(0,
                    MAX_LENGTH_INDUSTRY_GROUP_DESC);
        } else {
            this.industryGroupDesc = value;
        }
    }

    public void setIsin(String value) {
        // truncate string if it's neccessary
        if (value.length() >= MAX_LENGTH_ISIN) {
            this.isin = value.substring(0, MAX_LENGTH_ISIN);
        } else {
            this.isin = value;
        }
    }

    /*
     * ----------------------------------------------------------------------------------
     * get methods
     * ----------------------------------------------------------------------------------
     */
    public String getCode() {
        return this.code;
    }

    public String getAssetFullName() {
        return this.asset_full_name;
    }

    public String getIssuerName() {
        return this.issuerName;
    }

    public String getIssuerAbbName() {
        return this.issuerAbbName;
    }

    public String getIssuerShortName() {
        return this.issuerShortName;
    }

    public String getIssuerType() {
        return this.issuerType;
    }

    public double getSecurityType() {
        return this.securityType;
    }

    public String getExchange() {
        return this.exchange;
    }

    public double getIndustrySubgroup() {
        return this.industrySubgroup;
    }

    public String getDescription() {
        return this.description;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getAbbDescription() {
        return this.abbDescription;
    }

    public double getAssetBacking() {
        return this.assetBacking;
    }

    public String getIssuerCode() {
        return this.issuerCode;
    }

    public String getIssuerExchange() {
        return this.issuerExchange;
    }

    public String getIndustrySubgroupDesc() {
        return this.industrySubgroupDesc;
    }

    public String getIndustryGroupDesc() {
        return this.industryGroupDesc;
    }

    public String getIsin() {
        return this.isin;
    }

    public double getGics() {
        return this.gics;
    }

}
