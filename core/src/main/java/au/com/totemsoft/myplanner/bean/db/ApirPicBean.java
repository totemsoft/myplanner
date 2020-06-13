/*
 * ApirPicBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.assetinvestment.AvailableInvestmentsTableRow;

/**
 * ApirPicBean is responsible for load and store information form the database
 * table "apir_pic".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 19/07/2002
 */
public class ApirPicBean {

    public static final String DATABASE_TABLE_NAME = "apir_pic";

    private static final String SEARCH_OPERATOR = "OR";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        ApirPicBean.sqlHelper = sqlHelper;
    }

    // bean properties
    private String identifier; // length: 3

    private String apir_pic; // length: 9

    private String apir_full_name; // length: 70

    private String apir_short_name; // length: 35

    private String apir_brief_name; // length: 28

    private int code; // length: 4

    private java.sql.Timestamp id_old; // length: 8

    private int id; // length: 9

    // constants
    private static final int MAX_LENGTH_IDENTIFIER = 3;

    private static final int MAX_LENGTH_APIR_PIC = 9;

    private static final int MAX_LENGTH_APIR_FULL_NAME = 70;

    private static final int MAX_LENGTH_APIR_SHORT_NAME = 35;

    private static final int MAX_LENGTH_APIR_BRIEF_NAME = 28;

    private static final int MAX_LENGTH_CODE = 4;

    private static final int MAX_LENGTH_ID_OLD = 8;

    private static final int MAX_LENGTH_id = 9;

    private static final int INITIAL_VECTOR_SIZE = 64;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 32;

    /**
     * Creates a new entry in the "apir_pic" table. The properties for the new
     * entry must be set before creating a new entry.
     */
    public void create() throws java.sql.SQLException {
        StringBuffer sql = new StringBuffer()
            .append("INSERT INTO ")
            .append("[" + DATABASE_TABLE_NAME + "] ")
            .append("(identifier, [apir_pic], [apir_full_name], [apir_short_name], [apir_brief_name], code, id) ")
            .append("VALUES ( ?, ?, ?, ?, ?, ?, ? )");
        try (Connection con = sqlHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, this.identifier);
            pstmt.setString(2, this.apir_pic);
            pstmt.setString(3, this.apir_full_name);
            pstmt.setString(4, this.apir_short_name);
            pstmt.setString(5, this.apir_brief_name);
            pstmt.setInt(6, this.code);
            // pstmt.setTimestamp ( 7, this.id_old );
            pstmt.setInt(7, this.id);

            int status = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    /**
     * Stores an entry in the "apir_pic" table. The properties for the entry
     * must be set before storing it.
     */
    public void store() throws java.sql.SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("SET ");
        sql.append("identifier = ?, ");
        sql.append("[apir_full_name] = ?, ");
        sql.append("[apir_short_name] = ?, ");
        sql.append("[apir_brief_name] = ?, ");
        sql.append("code = ?,  ");
        sql.append("id = ? ");
        sql.append("WHERE [apir_pic] = ? ");
        try (Connection con = sqlHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, this.identifier);
            pstmt.setString(2, this.apir_full_name);
            pstmt.setString(3, this.apir_short_name);
            pstmt.setString(4, this.apir_brief_name);
            pstmt.setInt(5, this.code);
            // pstmt.setTimestamp ( 7, this.id_old );
            pstmt.setInt(6, this.id);
            pstmt.setString(7, this.apir_pic);

            int status = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    /**
     * Loads an entry from the "apir_pic" table. The "apir_pic" column is used
     * for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id _
     *            use the apir_pic column as identifier
     * @return true = found an entry
     */
    public boolean findByApirPic(String apir_pic_id)
            throws java.sql.SQLException {
        return this.findByColumnName("apir_pic", apir_pic_id);
    }

    /**
     * Search in database table "apir_pic" for all rows whose "apir_full_name"
     * contains one of the keywords as part of any word in the column. The
     * returned Vector is sorted by the column "apir_full_name".
     * 
     * @param keywords _
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchDescription(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearch(keywords, "apir_full_name");
    }

    /**
     * Search in database table "apir_pic" for all rows whose "apir_pic"
     * contains one of the keywords as part of any word in the column. The
     * returned Vector is sorted by the column "apir_pic".
     * 
     * @param keywords _
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchInvestmentCode(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearch(keywords, "apir_pic");
    }

    /**
     * Search in database table "apir_pic" for all rows whose column name (given
     * as a parameter) contains at least one of the keywords (given) as part of
     * any word in the column. The returned Vector is sorted by the given column
     * name.
     * 
     * @param keywords _
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    private Vector findByKeywordsSearch(String keywords, String column_name)
            throws java.sql.SQLException {
        Vector result = new Vector();
        // check if we have some keywords
        if (StringUtils.isBlank(keywords)) {
            return result;
        }
        // split keywords String into tokens (single keywords)
        final StringTokenizer keywords_StringTokenizer = new StringTokenizer(keywords);
        final int number_of_tokens = keywords_StringTokenizer.countTokens();
        int token_count = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT ");
        sql.append("[apir_pic], [apir_full_name], code ");
        sql.append("FROM [" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE ");
        // add all tokens (single keywords) to the query
        while (keywords_StringTokenizer.hasMoreTokens()) {
            // do we need to add "AND "?
            if (token_count > 0 && token_count < number_of_tokens) {
                sql.append(SEARCH_OPERATOR + " ");
            }
            sql.append("([" + column_name + " ] LIKE '%" + keywords_StringTokenizer.nextToken() + "%') ");
            token_count++;
        }
        // order by description
        sql.append("ORDER BY [" + column_name + "] ");
        try (Connection con = sqlHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            ResultSet rs = pstmt.executeQuery();
            // have we any result
            while (rs.next()) {
                // create new row
                AvailableInvestmentsTableRow table_row = new AvailableInvestmentsTableRow();

                // fill it with data
                table_row.origin = checkString(DATABASE_TABLE_NAME);
                table_row.investmentCode = checkString(rs
                        .getString("apir_pic"));
                table_row.description = checkString(rs
                        .getString("apir_full_name"));
                table_row.code = checkString(rs.getString("code"));
                // store row
                result.add(table_row);
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }

        return result;
    }

    /**
     * Loads an entry from the "apir_pic" table. The given column name and id is
     * used for identification. The first matching (column contains id) entry
     * will be loaded.
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
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [" + column_name + "] = ? ");
        try (Connection con = sqlHelper.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            // do we have any result?
            if (rs.next()) {
                // get the data
                this.identifier = rs.getString("identifier");
                this.apir_pic = rs.getString("apir_pic");
                this.apir_full_name = rs.getString("apir_full_name");
                this.apir_short_name = rs.getString("apir_short_name");
                this.apir_brief_name = rs.getString("apir_brief_name");
                this.code = rs.getInt("code");
                // this.id = rs.getInt( "id" );
                found = true;
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
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

    public String getApir_pic() {
        return this.apir_pic;
    }

    public String getApir_full_name() {
        return this.apir_full_name;
    }

    public String getApir_short_name() {
        return this.apir_short_name;
    }

    public String getApir_brief_name() {
        return this.apir_brief_name;
    }

    public int getCode() {
        return this.code;
    }

    public java.sql.Timestamp getId_old() {
        return this.id_old;
    }

    public int getId() {
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

    public void setApir_pic(String new_apir_pic) {
        // truncate string if it's neccessary
        if (new_apir_pic.length() >= MAX_LENGTH_APIR_PIC) {
            this.apir_pic = new_apir_pic.substring(0, MAX_LENGTH_APIR_PIC);
        } else {
            this.apir_pic = new_apir_pic;
        }
    }

    public void setApir_full_name(String new_apir_full_name) {
        // truncate string if it's neccessary
        if (new_apir_full_name.length() >= MAX_LENGTH_APIR_FULL_NAME) {
            this.apir_full_name = new_apir_full_name.substring(0,
                    MAX_LENGTH_APIR_FULL_NAME);
        } else {
            this.apir_full_name = new_apir_full_name;
        }
    }

    public void setApir_short_name(String new_apir_short_name) {
        // truncate string if it's neccessary
        if (new_apir_short_name.length() >= MAX_LENGTH_APIR_SHORT_NAME) {
            this.apir_short_name = new_apir_short_name.substring(0,
                    MAX_LENGTH_APIR_SHORT_NAME);
        } else {
            this.apir_short_name = new_apir_short_name;
        }
    }

    public void setApir_brief_name(String new_apir_brief_name) {
        // truncate string if it's neccessary
        if (new_apir_brief_name.length() >= MAX_LENGTH_APIR_BRIEF_NAME) {
            this.apir_brief_name = new_apir_brief_name.substring(0,
                    MAX_LENGTH_APIR_BRIEF_NAME);
        } else {
            this.apir_brief_name = new_apir_brief_name;
        }
    }

    public void setCode(int new_code) {
        this.code = new_code;
    }

    public void setId_old(java.sql.Timestamp new_id_old) {
        this.id_old = new_id_old;
    }

    public void setId(int new_id) {
        this.id = new_id;
    }

}
