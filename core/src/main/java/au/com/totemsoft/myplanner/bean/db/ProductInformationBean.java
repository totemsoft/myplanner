/*
 * ProductInformationBean.java
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
 * ApirPicBean is responsible for load and store information form the database
 * table "product_information".
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * @since 15/08/2002
 */
public class ProductInformationBean {

    public static final String DATABASE_TABLE_NAME = "product_information";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        ProductInformationBean.sqlHelper = sqlHelper;
    }

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
     * Loads an entry from the "product_information" table. The "code" column is
     * used for identification. The first matching entry will be loaded.
     * 
     * @param code_id _
     *            use the apir_pic column as identifier
     * @return true = found an entry
     */
    public boolean findByCode(int code_id) throws java.sql.SQLException {
        return this.findByColumnName("code", new Integer(code_id).toString());
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
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [" + column_name + "] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.identifier = rs.getString("identifier");
                this.code = rs.getInt("code");
                this.full_name = rs.getString("full_name");
                this.country_code = rs.getString("country_code");
                this.tax_group_code = rs.getString("tax_group_code");
                this.manager_code = rs.getString("manager_code");
                this.group_code = rs.getInt("group_code");
                this.short_name = rs.getString("short_name");
                this.brief_name = rs.getString("brief_name");
                this.legal_type_code = rs.getString("legal_type_code");
                this.region_code = rs.getString("region_code");
                this.asset_type_code = rs.getString("asset_type_code");
                this.cash_distribution_code = rs.getString("cash_distribution_code");
                this.trustee_code = rs.getString("trustee_code");
                this.custodian_code = rs.getString("custodian_code");
                // this.commencement_date = rs.getTimestamp( "commencement_date");
                // this.data_date = rs.getTimestamp( "data_date" );
                this.stock_exchange = rs.getInt("stock_exchange");
                this.guarantees = rs.getInt("guarantees");
                this.unit_linked = rs.getInt("unit_linked");
                this.valuation_frequency = rs.getString("valuation_frequency");
                this.declared_yield = rs.getInt("declared_yield");
                this.rate_of_return_advance = rs.getInt("rate_of_return_advance");
                this.category = rs.getString("category");
                this.subcategory = rs.getString("subcategory");
                this.manager_group_code = rs.getString("manager_group_code");
                this.gearing = rs.getInt("gearing");
                this.gearing_max = rs.getDouble("gearing_max");
                this.gearing_comments = rs.getString("gearing_comments");
                this.special_features = rs.getString("special_features");
                // this.id = rs.getTimestamp( "id" );
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
     * 
     * @param code
     * @param full_name
     * @return true = found an entry
     */
    public boolean containsCodeFullName(int code, String full_name)
            throws java.sql.SQLException {
        boolean found = false;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + DATABASE_TABLE_NAME + "] ");
        sql.append("WHERE [code] = ? ");
        sql.append("AND ");
        sql.append("[full_name] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setInt(1, code);
            pstmt.setString(2, full_name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
            }
            rs.close();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
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
