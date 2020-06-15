/*
 * FinancialCodeBean.java
 *
 * Created on 19 July 2002, 12:36
 */

package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.code.FinancialTypeID;
import au.com.totemsoft.util.ReferenceCode;

/**
 * ApirPicBean is responsible for load and store information form the database
 * table "FinancialCode".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 25/07/2002
 */
public class FinancialCodeBean implements FinancialTypeID {

    public static final String DATABASE_TABLE_NAME = "FinancialCode";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        FinancialCodeBean.sqlHelper = sqlHelper;
    }

    // bean properties
    private int financialCodeID; // length: 4

    private int financialTypeID; // length: 4

    private String financialCode; // length: 10

    private String financialCodeDesc; // length: 100

    private static final int MAX_LENGTH_FINANCIALCODE = 32;

    private static final int MAX_LENGTH_FINANCIALCODEDESC = 100;

    public FinancialCodeBean() {
    }

    public String toString() {
        return "[" + financialTypeID + "," + financialCodeID + ","
                + financialCode + "," + financialCodeDesc + "]";
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
            throws SQLException {
        return findByFinancialCodeIdAndFinancialTypeId("" + financial_code_id,
                "" + financialTypeID);
    }

    public boolean findByFinancialCodeIdAndFinancialTypeId(
            int financial_code_id, int financialTypeID)
            throws SQLException {
        return findByFinancialCodeIdAndFinancialTypeId("" + financial_code_id,
                "" + financialTypeID);
    }

    public boolean findByFinancialCodeIdAndFinancialTypeId(
            String financial_code_id, String financialTypeID)
            throws SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [FinancialCodeID] = ? ");
        sql.append("AND   [FinancialTypeID] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, financial_code_id);
            pstmt.setString(2, financialTypeID);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.financialCodeID = rs.getInt("FinancialCodeID");
                    this.financialTypeID = rs.getInt("FinancialTypeID");
                    this.financialCode = rs.getString("FinancialCode");
                    this.financialCodeDesc = rs.getString("FinancialCodeDesc");
                    found = true;
                }
            }
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
            throws SQLException {
        return findByFinancialCodeDescAndFinancialTypeId(financialCodeDesc, ""
                + financialTypeID);
    }

    public boolean findByFinancialCodeDescAndFinancialTypeId(
            String financialCodeDesc, String financialTypeID)
            throws SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [FinancialCodeDesc] LIKE '%" + financialCodeDesc + "%'");
        sql.append("AND   [FinancialTypeID] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            // pstmt.setString ( 1, financialCodeDesc );
            pstmt.setString(1, financialTypeID);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.financialCodeID = rs.getInt("FinancialCodeID");
                    this.financialTypeID = rs.getInt("FinancialTypeID");
                    this.financialCode = rs.getString("FinancialCode");
                    this.financialCodeDesc = rs.getString("FinancialCodeDesc");
                    found = true;
                }
            }
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
            throws SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [FinancialCodeDesc] LIKE '%" + financialCodeDesc + "%'");
        sql.append("AND   [FinancialCodeID] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            // pstmt.setString ( 1, financialCodeDesc );
            pstmt.setString(1, financial_code_id);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.financialCodeID = rs.getInt("FinancialCodeID");
                    this.financialTypeID = rs.getInt("FinancialTypeID");
                    this.financialCode = rs.getString("FinancialCode");
                    this.financialCodeDesc = rs.getString("FinancialCodeDesc");
                    found = true;
                }
            }
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
            String financial_code) throws SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT * ");
        sql.append("FROM [" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [FinancialCodeDesc] LIKE '%" + financialCodeDesc + "%'");
        sql.append("AND [FinancialTypeID] = ? ");
        sql.append("AND [FinancialCode] LIKE '%" + financial_code + "%'");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            // pstmt.setString ( 1, financialCodeDesc );
            pstmt.setString(1, financialTypeID);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.financialCodeID = rs.getInt("FinancialCodeID");
                    this.financialTypeID = rs.getInt("FinancialTypeID");
                    this.financialCode = rs.getString("FinancialCode");
                    this.financialCodeDesc = rs.getString("FinancialCodeDesc");
                    found = true;
                }
            }
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
            throws SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT * ");
        sql.append("FROM [" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [FinancialCodeDesc] LIKE '%" + financialCodeDesc + "%'");
        sql.append("AND [FinancialTypeID] = ? ");
        sql.append(financial_code_id > 0 ? "AND [FinancialCodeID] = ? " : "AND [FinancialCodeID] IS NULL ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setInt(1, financialTypeID);
            if (financial_code_id > 0)
                pstmt.setInt(2, financial_code_id);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.financialCodeID = rs.getInt("FinancialCodeID");
                    this.financialTypeID = rs.getInt("FinancialTypeID");
                    this.financialCode = rs.getString("FinancialCode");
                    this.financialCodeDesc = rs.getString("FinancialCodeDesc");
                    found = true;
                }
            }
        }
        return found;
    }

    public boolean find(ReferenceCode financialType, ReferenceCode financialCode)
            throws SQLException {
        return findByFinancialCodeDescAndFinancialTypeIdAndFinancialCodeId(
                financialCode.getDescription(), financialType.getId(),
                financialCode.getId());
    }

    /**
     * 
     */
    public boolean findByFinancialCodeAndFinancialTypeId(String financial_code,
            Integer financialTypeID) throws SQLException {
        return findByFinancialCodeAndFinancialTypeId(financial_code,
                financialTypeID.toString());
    }

    public boolean findByFinancialCodeAndFinancialTypeId(String financial_code,
            String financialTypeID) throws SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [FinancialCode] = ? ");
        sql.append("AND   [FinancialTypeID] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, financial_code);
            pstmt.setString(2, financialTypeID);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    this.financialCodeID = rs.getInt("FinancialCodeID");
                    this.financialTypeID = rs.getInt("FinancialTypeID");
                    this.financialCode = rs.getString("FinancialCode");
                    this.financialCodeDesc = rs.getString("FinancialCodeDesc");
                    found = true;
                }
            }
        }
        return found;
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
