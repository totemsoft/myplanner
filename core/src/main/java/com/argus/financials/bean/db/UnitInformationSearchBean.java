/*
 * UnitInformationSearchBean.java
 *
 * Created on 22 July 2002, 08:30
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
 * UnitInformationSearchBean is responsible for the search of units which match
 * given keywords. The beans operates on three database tables at the same time
 * to collect all neccessary data, these tables are: "manager-data",
 * "product-information" and "apir-pic".
 * <P>
 * </P>
 * The following SQL statement is used: <BR>
 * </BR>
 * 
 * <PRE>
 * 
 * SELECT b.code, b.[full-name], a.[full-name] AS Institution, c.[apir-pic] FROM
 * [manager-data] a, [product-information] b, [apir-pic] c
 * 
 * WHERE a.code = b.[manager-code] AND b.code = c.code AND b.[full-name] LIKE
 * '%Questor%'
 * 
 * </PRE>
 * 
 * @author shibaevv
 * @version 0.01
 * @since 22/08/2002
 */

public class UnitInformationSearchBean {

    public static final String MANAGER_DATA_DATABASE_TABLE_NAME = "manager-data";

    public static final String PRODUCT_INFORMATION_DATABASE_TABLE_NAME = "product-information";

    public static final String APIR_PIC_DATABASE_TABLE_NAME = "apir-pic";

    private static final String SEARCH_OPERATOR = "AND";

    // bean properties
    private int code; // length: 4

    private String full_name; // length: 50

    private String institution; // length: 50

    private String apir_pic; // length: 9

    // constants
    private static final int MAX_LENGTH_CODE = 4;

    private static final int MAX_LENGTH_FULL_NAME = 50;

    private static final int MAX_LENGTH_INSTITUTION = 50;

    private static final int MAX_LENGTH_APIR_PIC = 9;

    private static final int INITIAL_VECTOR_SIZE = 64;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 32;

    /** Creates new ApirPicBean */
    public UnitInformationSearchBean() {
    }

    /**
     * Loads an entry. The "apir-pic" column is used for identification. The
     * first matching entry will be loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByApirPic(String apir_pic_id)
            throws java.sql.SQLException {
        return this.findByColumnName("c.[apir-pic]", apir_pic_id);
    }

    /**
     * Search in the "three tables" for all rows whose "full-name" (asset name)
     * contains one of the keywords as part of any word in the column. The
     * returned Vector is sorted by the column "full-name".
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchDescription(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearch(keywords, "b.[full-name]");
    }

    /**
     * Search in the "three tables" for all rows whose "apir-pic" contains one
     * of the keywords as part of any word in the column. The returned Vector is
     * sorted by the column "apir-pic".
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchInvestmentCode(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearch(keywords, "c.[apir-pic]");
    }

    /**
     * Search in the "three tables" for all rows whose column name (given as a
     * parameter) contains at least one of the keywords (given) as part of any
     * word in the column. The returned Vector is sorted by the given column
     * name.
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    private Vector findByKeywordsSearch(String keywords, String column_name)
            throws java.sql.SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        StringTokenizer keywords_StringTokenizer = null;
        int token_count = 0;
        int number_of_tokens = 0;

        Vector table_rows = new Vector(INITIAL_VECTOR_SIZE,
                INITIAL_VECTOR_GROWTH_SIZE);

        if (keywords == null || (keywords = keywords.trim()).length() == 0)
            return new Vector();
        if (column_name == null
                || (column_name = column_name.trim()).length() == 0)
            return new Vector();

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
                        .append("b.code, b.[full-name], a.[full-name] AS institution, c.[apir-pic] ");

                pstmt_StringBuffer.append("FROM ["
                        + MANAGER_DATA_DATABASE_TABLE_NAME + "] a, ");
                pstmt_StringBuffer.append("["
                        + PRODUCT_INFORMATION_DATABASE_TABLE_NAME + "] b, ");
                pstmt_StringBuffer.append("[" + APIR_PIC_DATABASE_TABLE_NAME
                        + "] c ");

                pstmt_StringBuffer.append("WHERE ");
                pstmt_StringBuffer
                        .append("a.code = b.[manager-code] AND b.code = c.code AND ");

                // add all tokens (single keywords) to the query
                while (keywords_StringTokenizer.hasMoreTokens()) {
                    // do we need to add "AND "?
                    if (token_count > 0 && token_count < number_of_tokens) {
                        pstmt_StringBuffer.append(SEARCH_OPERATOR + " ");
                    }

                    pstmt_StringBuffer.append("(" + column_name + " LIKE '%"
                            + keywords_StringTokenizer.nextToken() + "%') ");
                    token_count++;
                }
                // order by description
                pstmt_StringBuffer.append("ORDER BY " + column_name + " ");

                // set and execute query
                pstmt = con.prepareStatement(pstmt_StringBuffer.toString());
                rs = pstmt.executeQuery();

                // have we any result?
                while (rs.next()) {
                    // create new row
                    AvailableInvestmentsTableRow table_row = new AvailableInvestmentsTableRow();

                    // fill it with data
                    table_row.origin = checkString(APIR_PIC_DATABASE_TABLE_NAME);
                    table_row.investmentCode = checkString(rs
                            .getString("apir-pic"));
                    table_row.description = checkString(rs
                            .getString("full-name"));
                    table_row.code = checkString(rs.getString("code"));
                    table_row.institution = checkString(rs
                            .getString("institution"));
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
     * Loads an entry. The given column name and id is used for identification.
     * The first matching (column contains id) entry will be loaded.
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

            // build pstmt query
            pstmt_StringBuffer.append("SELECT DISTINCT ");
            pstmt_StringBuffer
                    .append("b.code, b.[full-name], a.[full-name] AS institution, c.[apir-pic] ");

            pstmt_StringBuffer.append("FROM ["
                    + MANAGER_DATA_DATABASE_TABLE_NAME + "] a, ");
            pstmt_StringBuffer.append("["
                    + PRODUCT_INFORMATION_DATABASE_TABLE_NAME + "] b, ");
            pstmt_StringBuffer.append("[" + APIR_PIC_DATABASE_TABLE_NAME
                    + "] c");

            pstmt_StringBuffer.append("WHERE ");
            pstmt_StringBuffer
                    .append("a.code = b.[manager-code] AND b.code = c.code AND");
            pstmt_StringBuffer.append(column_name + " = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.apir_pic = rs.getString("apir-pic");
                this.full_name = rs.getString("full-name");
                this.institution = rs.getString("institution");
                this.code = rs.getInt("code");
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
    public String getApirPic() {
        return this.apir_pic;
    }

    public String getFullName() {
        return this.full_name;
    }

    public String getInstitution() {
        return this.institution;
    }

    public int getCode() {
        return this.code;
    }

}
