/*
 * InstitutionBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.dao.SQLHelper;

/**
 * InstitutionBean is responsible for load and store information form the
 * database table "Institution".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 25/07/2002
 */
public class InstitutionBean {

    public static final String DATABASE_TABLE_NAME = "Institution";

    private static final String SEARCH_OPERATOR = "OR";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        InstitutionBean.sqlHelper = sqlHelper;
    }

    // bean properties
    public int institutionID; // length: 4

    public String institutionName; // length: 100

    // constants
    private static final int MAX_LENGTH_INSTITUTIONID = 4;

    private static final int MAX_LENGTH_INSTITUTIONNAME = 100;

    private static final int INITIAL_VECTOR_SIZE = 32;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 16;

    /** Creates new InstitutionBean */
    public InstitutionBean() {
    }

    /**
     * Creates a new entry in the "FinancialCodeBean" table. The properties for
     * the new entry must be set before creating a new entry. (Updates the
     * FinancialCodeID!)
     */
    public void create() throws java.sql.SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer pstmt_StringBuffer = new StringBuffer();
        int status = 0;
        try (Connection con = sqlHelper.getConnection();) {
            // get max. FinancialCodeID
            // build sql query
            pstmt_StringBuffer.append("SELECT MAX(institutionID) ");
            pstmt_StringBuffer.append("FROM ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());
            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.institutionID = rs.getInt(1);
                // create new "unique" FinancialCodeID
                this.institutionID++;
            }
            // close ResultSet and PreparedStatement
            sqlHelper.close(rs, pstmt);

            // build sql query
            pstmt_StringBuffer.append("INSERT INTO ");
            pstmt_StringBuffer.append("[" + DATABASE_TABLE_NAME + "] ");
            pstmt_StringBuffer.append("(institutionID, institutionName) ");
            pstmt_StringBuffer.append("VALUES ( ?, ? )");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setInt(1, this.institutionID);
            pstmt.setString(2, this.institutionName);

            status = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(rs, pstmt);
        }
    }

    /**
     * Stores an entry in the "FinancialCodeBean" table. The properties for the
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
            pstmt_StringBuffer.append("institutionName = ? ");
            pstmt_StringBuffer.append("WHERE InstitutionID = ? ");

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, this.institutionName);
            pstmt.setInt(2, this.institutionID);

            status = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(null, pstmt);
        }
    }

    /**
     * Loads an entry from the "Institution" table. The "FinancialCodeID" column
     * is used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByInstitutionID(String id) throws java.sql.SQLException {
        return this.findByColumnName("InstitutionID", id);
    }

    /**
     * Loads an entry from the "Institution" table. The "FinancialTypeID" column
     * is used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id -
     *            use the apir-pic column as identifier
     * @return true = found an entry
     */
    public boolean findByInstitutionName(String id)
            throws java.sql.SQLException {
        return this.findByColumnName("InstitutionName", id);
    }

    /**
     * Loads an entry from the "Institution" table. The given column name and id
     * is used for identification. The first matching (column contains id) entry
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

            // set and execute query
            pstmt = con.prepareStatement(pstmt_StringBuffer.toString());

            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            // do we have any result?
            if (rs.next()) {
                // get the data
                this.institutionID = rs.getInt("InstitutionID");
                this.institutionName = rs.getString("InstitutionName");

                found = true;
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        } finally {
            sqlHelper.close(rs, pstmt);
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
    public int getInstitutionID() {
        return this.institutionID;
    }

    public String getInstitutionName() {
        return this.institutionName;
    }

    /*
     * ----------------------------------------------------------------------------------
     * set methods
     * ----------------------------------------------------------------------------------
     */
    public void setInstitutionName(String new_institutionName) {
        // truncate string if it's neccessary
        if (new_institutionName.length() >= MAX_LENGTH_INSTITUTIONNAME) {
            this.institutionName = new_institutionName.substring(0,
                    MAX_LENGTH_INSTITUTIONNAME);
        } else {
            this.institutionName = new_institutionName;
        }
    }

    public void setInstitutionID(int new_institutionID) {
        this.institutionID = new_institutionID;
    }

}
