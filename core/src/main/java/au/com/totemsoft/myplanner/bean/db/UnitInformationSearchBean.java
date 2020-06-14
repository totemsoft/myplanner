/*
 * UnitInformationSearchBean.java
 *
 * Created on 22 July 2002, 08:30
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
 * UnitInformationSearchBean is responsible for the search of units which match
 * given keywords. The beans operates on three database tables at the same time
 * to collect all neccessary data, these tables are: "manager_data",
 * "product_information" and "apir_pic".
 * <P>
 * </P>
 * The following SQL statement is used: <BR>
 * </BR>
 * 
 * <PRE>
 * 
 * SELECT b.code, b.[full_name], a.[full_name] AS Institution, c.[apir_pic] FROM
 * [manager_data] a, [product_information] b, [apir_pic] c
 * 
 * WHERE a.code = b.[manager_code] AND b.code = c.code AND b.[full_name] LIKE
 * '%Questor%'
 * 
 * </PRE>
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 22/08/2002
 */

public class UnitInformationSearchBean {

    public static final String MANAGER_DATA_DATABASE_TABLE_NAME = ManagerDataBean.DATABASE_TABLE_NAME;

    public static final String PRODUCT_INFORMATION_DATABASE_TABLE_NAME = ProductInformationBean.DATABASE_TABLE_NAME;

    public static final String APIR_PIC_DATABASE_TABLE_NAME = ApirPicBean.DATABASE_TABLE_NAME;

    private static final String SEARCH_OPERATOR = "AND";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        UnitInformationSearchBean.sqlHelper = sqlHelper;
    }

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
     * Loads an entry. The "apir_pic" column is used for identification. The
     * first matching entry will be loaded.
     * 
     * @param apir_pic_id _
     *            use the apir_pic column as identifier
     * @return true = found an entry
     */
    public boolean findByApirPic(String apir_pic_id)
            throws java.sql.SQLException {
        return this.findByColumnName("c.[apir_pic]", apir_pic_id);
    }

    /**
     * Search in the "three tables" for all rows whose "full_name" (asset name)
     * contains one of the keywords as part of any word in the column. The
     * returned Vector is sorted by the column "full_name".
     * 
     * @param keywords _
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchDescription(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearch(keywords, "b.[full_name]");
    }

    /**
     * Search in the "three tables" for all rows whose "apir_pic" contains one
     * of the keywords as part of any word in the column. The returned Vector is
     * sorted by the column "apir_pic".
     * 
     * @param keywords _
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector findByKeywordsSearchInvestmentCode(String keywords)
            throws java.sql.SQLException {
        return this.findByKeywordsSearch(keywords, "c.[apir_pic]");
    }

    /**
     * Search in the "three tables" for all rows whose column name (given as a
     * parameter) contains at least one of the keywords (given) as part of any
     * word in the column. The returned Vector is sorted by the given column
     * name.
     * 
     * @param keywords _
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    private Vector findByKeywordsSearch(String keywords, String column_name) throws java.sql.SQLException {
        Vector result = new Vector();
        // check if we have some keywords
        if (StringUtils.isBlank(keywords) || StringUtils.isBlank(column_name)) {
            return result;
        }
        // split keywords String into tokens (single keywords)
        final StringTokenizer tok = new StringTokenizer(keywords);
        final int number_of_tokens = tok.countTokens();
        int token_count = 0;
        column_name = column_name.trim();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT ");
        sql.append("b.code, b.[full_name], a.[full_name] AS institution, c.[apir_pic] ");
        sql.append("FROM [" + MANAGER_DATA_DATABASE_TABLE_NAME + "] a, ");
        sql.append("[" + PRODUCT_INFORMATION_DATABASE_TABLE_NAME + "] b, ");
        sql.append("[" + APIR_PIC_DATABASE_TABLE_NAME + "] c ");
        sql.append("WHERE ");
        sql.append("a.code = b.[manager_code] AND b.code = c.code AND ");
        // add all tokens (single keywords) to the query
        sql.append("(");
        while (tok.hasMoreTokens()) {
            // do we need to add "AND "?
            if (token_count > 0 && token_count < number_of_tokens) {
                sql.append(SEARCH_OPERATOR + " ");
            }
            sql.append(column_name + " LIKE '%" + tok.nextToken() + "%' ");
            token_count++;
        }
        sql.append(") ");
        // order by description
        sql.append("ORDER BY " + column_name);
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // create new row
                AvailableInvestmentsTableRow table_row = new AvailableInvestmentsTableRow();
                // fill it with data
                table_row.origin = checkString(APIR_PIC_DATABASE_TABLE_NAME);
                table_row.investmentCode = checkString(rs.getString("apir_pic"));
                table_row.description = checkString(rs.getString("full_name"));
                table_row.code = checkString(rs.getString("code"));
                table_row.institution = checkString(rs.getString("institution"));
                // store row
                result.add(table_row);
            }
            rs.close();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }

        return result;
    }

    /**
     * Loads an entry. The given column name and id is used for identification.
     * The first matching (column contains id) entry will be loaded.
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
        sql.append("SELECT DISTINCT ");
        sql.append("b.code, b.[full_name], a.[full_name] AS institution, c.[apir_pic] ");
        sql.append("FROM [" + MANAGER_DATA_DATABASE_TABLE_NAME + "] a, ");
        sql.append("[" + PRODUCT_INFORMATION_DATABASE_TABLE_NAME + "] b, ");
        sql.append("[" + APIR_PIC_DATABASE_TABLE_NAME + "] c");
        sql.append("WHERE ");
        sql.append("a.code = b.[manager_code] AND b.code = c.code AND");
        sql.append(column_name + " = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.apir_pic = rs.getString("apir_pic");
                this.full_name = rs.getString("full_name");
                this.institution = rs.getString("institution");
                this.code = rs.getInt("code");
                found = true;
            }
            rs.close();
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
