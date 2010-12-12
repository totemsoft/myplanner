/*
 * FinancialCodeBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.argus.financials.assetinvestment.AvailableInvestmentsTableRow;
import com.argus.financials.code.FinancialTypeID;
import com.argus.util.ReferenceCode;

/**
 * ApirPicBean is responsible for load and store information form the database
 * table "FinancialCode".
 * 
 * @author shibaevv
 * @version 0.01
 * @since 25/07/2002
 */
public class FinancialCodeBean implements FinancialTypeID {

    public static final String DATABASE_TABLE_NAME = "FinancialCode";

    private static final String SEARCH_OPERATOR = "OR";

    // bean properties
    private int financialCodeID; // length: 4

    private int financialTypeID; // length: 4

    private String financialCode; // length: 10

    private String financialCodeDesc; // length: 100

    // constants
    private static final int MAX_LENGTH_FINANCIALCODEID = 4;

    private static final int MAX_LENGTH_FINANCIALTYPEID = 4;

    private static final int MAX_LENGTH_FINANCIALCODE = 32;

    private static final int MAX_LENGTH_FINANCIALCODEDESC = 100;

    private static final int INITIAL_VECTOR_SIZE = 32;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 16;

    /** Creates new ApirPicBean */
    public FinancialCodeBean() {
    }

    public String toString() {
        return "[" + financialTypeID + "," + financialCodeID + ","
                + financialCode + "," + financialCodeDesc + "]";
    }

    /**
     * Creates a new entry in the "FinancialCodeBean" table. The properties for
     * the new entry must be set before creating a new entry. (Updates the
     * FinancialCodeID!)
     */
    public void create() throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;

        try {
            // get max. FinancialCodeID
            // build sql query
            pstmt_StringBuffer.append("SELECT MAX(FinancialCodeID) ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());
            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt(1);
                // create new "unique" FinancialCodeID
                financialCodeID++;
            }
            // close ResultSet and PreparedStatement
            closeRsSql(rs, pstmt);

            // build sql query
            pstmt_StringBuffer.append("INSERT INTO ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer
                    .append("(FinancialCodeID, FinancialTypeID, FinancialCode, FinancialCodeDesc) ");
            pstmt_StringBuffer.append("VALUES ( ?, ?, ?, ? )");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, this.financialCodeID);
            // pstmt.setInt ( 2, this.financialTypeID ); // should already
            // exists in FinancialType table
            if (this.financialTypeID > 0) {
                pstmt.setInt(2, this.financialTypeID);
            } else {
                pstmt.setInt(2, UNDEFINED);
            }

            pstmt.setString(3, this.financialCode);
            pstmt.setString(4, this.financialCodeDesc);

            status = pstmt.executeUpdate();

            // autocommit is off
            con.commit();

            // System.out.println( "FinancialCode created: " + this );

        } catch (SQLException e) {
            printSQLException(e);
            con.rollback();
            throw e;
        } finally {
            closeRsSql(null, pstmt);
        }

    }

    /**
     * Stores an entry in the "FinancialCodeBean" table. The properties for the
     * entry must be set before storing it.
     */
    public void store() throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("UPDATE ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("SET ");
            pstmt_StringBuffer.append("FinancialTypeID = ?, ");
            pstmt_StringBuffer.append("FinancialCode = ?, ");
            pstmt_StringBuffer.append("FinancialCodeDesc = ? ");
            pstmt_StringBuffer.append("WHERE FinancialCodeID = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            // pstmt.setInt ( 1, this.financialTypeID );
            if (this.financialTypeID > 0) {
                pstmt.setInt(1, this.financialTypeID);
            } else {
                pstmt.setInt(1, UNDEFINED);
            }
            pstmt.setString(2, this.financialCode);
            pstmt.setString(3, this.financialCodeDesc);
            pstmt.setInt(4, this.financialCodeID);

            int count = pstmt.executeUpdate();

            // autocommit is off
            con.commit();

            System.out.println("" + count + " FinancialCode updated: " + this);

        } catch (SQLException e) {
            printSQLException(e);
            con.rollback();
            throw e;
        } finally {
            closeRsSql(null, pstmt);
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
    public boolean findByFinancialCodeID(String id)
            throws java.sql.SQLException {
        return this.findByColumnName("FinancialCodeID", id);
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
    public boolean findByFinancialTypeID(String id)
            throws java.sql.SQLException {
        return this.findByColumnName("FinancialTypeID", id);
    }

    /**
     * Loads an entry from the "FinancialCode" table. The "FinancialCode" column
     * is used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByFinancialCode(String id) throws java.sql.SQLException {
        return this.findByColumnName("FinancialCode", id);
    }

    /**
     * Loads an entry from the "FinancialCode" table. The "FinancialCodeDesc"
     * column is used for identification. The first matching entry will be
     * loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByFinancialCodeDesc(String id)
            throws java.sql.SQLException {
        return this.findByColumnName("FinancialCodeDesc", id);
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
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
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
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    /**
     * Loads an entry from the "FinancialCode" table. The given
     * financial_code_id and financialTypeID is used for identification. The
     * first matching entry will be loaded.
     * 
     * @param financial_code_id
     * @param financialTypeID -
     *            the identification
     * @return true = found an entry
     */
    public boolean findByFinancialCodeIdAndFinancialTypeId(
            Integer financial_code_id, Integer financialTypeID)
            throws java.sql.SQLException {
        return findByFinancialCodeIdAndFinancialTypeId("" + financial_code_id,
                "" + financialTypeID);
    }

    public boolean findByFinancialCodeIdAndFinancialTypeId(
            int financial_code_id, int financialTypeID)
            throws java.sql.SQLException {
        return findByFinancialCodeIdAndFinancialTypeId("" + financial_code_id,
                "" + financialTypeID);
    }

    public boolean findByFinancialCodeIdAndFinancialTypeId(
            String financial_code_id, String financialTypeID)
            throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [FinancialCodeID] = ? ");
            pstmt_StringBuffer.append("AND   [FinancialTypeID] = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, financial_code_id);
            pstmt.setString(2, financialTypeID);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    /**
     * Loads an entry from the "FinancialCode" table. The given
     * financial_code_id and financialTypeID is used for identification. The
     * first matching entry will be loaded.
     * 
     * @param financialCodeDesc
     * @param financialTypeID -
     *            the identification
     * @return true = found an entry
     */
    public boolean findByFinancialCodeDescAndFinancialTypeId(
            String financialCodeDesc, int financialTypeID)
            throws java.sql.SQLException {
        return findByFinancialCodeDescAndFinancialTypeId(financialCodeDesc, ""
                + financialTypeID);
    }

    public boolean findByFinancialCodeDescAndFinancialTypeId(
            String financialCodeDesc, String financialTypeID)
            throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [FinancialCodeDesc] LIKE '%"
                    + financialCodeDesc + "%'");
            pstmt_StringBuffer.append("AND   [FinancialTypeID] = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            // pstmt.setString ( 1, financialCodeDesc );
            pstmt.setString(1, financialTypeID);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    /**
     * Loads an entry from the "FinancialCode" table. The given
     * financial_code_id and financialCodeDesc is used for identification. The
     * first matching entry will be loaded.
     * 
     * @param financialCodeDesc
     * @param financial_code_id
     * @return true = found an entry
     */
    public boolean findByFinancialCodeDescAndFinancialCodeId(
            String financialCodeDesc, String financial_code_id)
            throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [FinancialCodeDesc] LIKE '%"
                    + financialCodeDesc + "%'");
            pstmt_StringBuffer.append("AND   [FinancialCodeID] = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            // pstmt.setString ( 1, financialCodeDesc );
            pstmt.setString(1, financial_code_id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    /**
     * Loads an entry from the "FinancialCode" table. The given
     * financial_code_id and financialTypeID and financial_code is used for
     * identification. The first matching entry will be loaded.
     * 
     * @param financialCodeDesc
     * @param financialTypeID
     * @param financial_code
     * @return true = found an entry
     */
    public boolean findByFinancialCodeDescAndFinancialTypeIdAndFinancialCode(
            String financialCodeDesc, String financialTypeID,
            String financial_code) throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT DISTINCT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [FinancialCodeDesc] LIKE '%"
                    + financialCodeDesc + "%'");
            pstmt_StringBuffer.append("AND   [FinancialTypeID] = ? ");
            pstmt_StringBuffer.append("AND   [FinancialCode] LIKE '%"
                    + financial_code + "%'");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            // pstmt.setString ( 1, financialCodeDesc );
            pstmt.setString(1, financialTypeID);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    /**
     * Loads an entry from the "FinancialCode" table. The given
     * financial_code_id and financialTypeID and financial_code is used for
     * identification. The first matching entry will be loaded.
     * 
     * @param financialCodeDesc
     * @param financialTypeID
     * @param financial_code_id
     * @return true = found an entry
     */
    public boolean findByFinancialCodeDescAndFinancialTypeIdAndFinancialCodeId(
            String financialCodeDesc, int financialTypeID, int financial_code_id)
            throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT DISTINCT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [FinancialCodeDesc] LIKE '%"
                    + financialCodeDesc + "%'");
            pstmt_StringBuffer.append("AND   [FinancialTypeID] = ? ");
            pstmt_StringBuffer
                    .append(financial_code_id > 0 ? "AND   [FinancialCodeID] = ? "
                            : "AND   [FinancialCodeID] IS NULL ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, financialTypeID);
            if (financial_code_id > 0)
                pstmt.setInt(2, financial_code_id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    public boolean find(ReferenceCode financialType, ReferenceCode financialCode)
            throws java.sql.SQLException {
        return findByFinancialCodeDescAndFinancialTypeIdAndFinancialCodeId(
                financialCode.getCodeDesc(), financialType.getCodeID(),
                financialCode.getCodeID());
    }

    /**
     * 
     */
    public boolean findByFinancialCodeAndFinancialTypeId(String financial_code,
            Integer financialTypeID) throws java.sql.SQLException {
        return findByFinancialCodeAndFinancialTypeId(financial_code,
                financialTypeID.toString());
    }

    public boolean findByFinancialCodeAndFinancialTypeId(String financial_code,
            String financialTypeID) throws java.sql.SQLException {
        Connection con = DBManager.getInstance().getConnection();
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();

        try {
            // build sql query
            pstmt_StringBuffer.append("SELECT * ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("WHERE [FinancialCode] = ? ");
            pstmt_StringBuffer.append("AND   [FinancialTypeID] = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, financial_code);
            pstmt.setString(2, financialTypeID);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.financialCodeID = rs.getInt("FinancialCodeID");
                this.financialTypeID = rs.getInt("FinancialTypeID");
                this.financialCode = rs.getString("FinancialCode");
                this.financialCodeDesc = rs.getString("FinancialCodeDesc");

                found = true;
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

        return found;
    }

    /**
     * Search in database table "FinancialCode" for all rows whose
     * "asset-full-name" contains at least one of the keywords as part of any
     * word in the column. The returned Vector is sorted by the column
     * "FinancialCodeDesc".
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchDescription(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearchDescription(keywords,
                "FinancialCodeDesc");
    }

    /**
     * Search in database table "FinancialCode" for all rows whose "code"
     * contains at least one of the keywords as part of any word in the column.
     * The returned Vector is sorted by the column "asset-full-name".
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchInvestmentCode(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearchDescription(keywords, "FinancialCode");
    }

    /**
     * Search in database table "FinancialCode" for all rows whose column name
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
                        .append("[FinancialCode], [FinancialCodeDesc]");
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
                // select only user created FinancialCodes, they start with '#'
                pstmt_StringBuffer.append(" AND [FinancialCode] LIKE '#%' ");
                pstmt_StringBuffer.append(" AND LogicallyDeleted IS NULL ");
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
                    table_row.investmentCode = checkString(rs
                            .getString("FinancialCode"));
                    table_row.description = checkString(rs
                            .getString("FinancialCodeDesc"));
                    table_row.code = "";
                    table_row.institution = "";
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
    public String getFinancialCodeDesc() {
        return this.financialCodeDesc;
    }

    public String getFinancialCode() {
        return this.financialCode;
    }

    public int getFinancialCodeID() {
        return this.financialCodeID;
    }

    public int getFinancialTypeID() {
        return this.financialTypeID;
    }

    /*
     * ----------------------------------------------------------------------------------
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setFinancialCode(String new_financialCode) {
        // truncate string if it's neccessary
        if (new_financialCode.length() >= MAX_LENGTH_FINANCIALCODE) {
            this.financialCode = new_financialCode.substring(0,
                    MAX_LENGTH_FINANCIALCODE);
        } else {
            this.financialCode = new_financialCode;
        }
    }

    public void setFinancialCodeDesc(String new_financialCodeDesc) {
        // truncate string if it's neccessary
        if (new_financialCodeDesc.length() >= MAX_LENGTH_FINANCIALCODEDESC) {
            this.financialCodeDesc = new_financialCodeDesc.substring(0,
                    MAX_LENGTH_FINANCIALCODEDESC);
        } else {
            this.financialCodeDesc = new_financialCodeDesc;
        }
    }

    public void setFinancialTypeID(int new_financialTypeID) {
        this.financialTypeID = new_financialTypeID;
    }

    public void setFinancialCodeID(int new_financialCodeID) {
        this.financialCodeID = new_financialCodeID;
    }

}
