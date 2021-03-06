/*
 * FundTypeBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import au.com.totemsoft.dao.SQLHelper;

/**
 * ApirPicBean is responsible for load and store information form the database
 * table "FundTypeBean".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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
        try (Connection con = sqlHelper.getConnection();) {
            // get max. FinancialCodeID
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT MAX(FundTypeID) ");
            sql.append("FROM ");
            sql.append("[" + DATABASE_TABLE_NAME + "] ");
            // set and execute query
            try (PreparedStatement pstmt = con.prepareStatement(sql.toString());
                    ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.fundTypeID = rs.getInt(1);
                    // create new "unique" FinancialCodeID
                    this.fundTypeID++;
                }
            }

            sql.append("INSERT INTO ");
            sql.append("[" + DATABASE_TABLE_NAME + "] ");
            sql.append("(FundTypeID, FundTypeDesc) ");
            sql.append("VALUES ( ?, ? )");
            try (PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
                pstmt.setInt(1, this.fundTypeID);
                pstmt.setString(2, this.fundTypeDesc);
                int status = pstmt.executeUpdate();
            }
        }
    }

    /**
     * Stores an entry in the "FinancialCodeBean" table. The properties for the
     * entry must be set before storing it.
     */
    public void store() throws java.sql.SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("SET ");
        sql.append("FundTypeDesc = ? ");
        sql.append("WHERE FundTypeID = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, this.fundTypeDesc);
            pstmt.setInt(2, this.fundTypeID);
            int status = pstmt.executeUpdate();
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
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [" + column_name + "] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.fundTypeID = rs.getInt("FundTypeID");
                    this.fundTypeDesc = rs.getString("FundTypeDesc");
                    found = true;
                }
            }
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
