package au.com.totemsoft.myplanner.api.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialType;

public interface FinancialCodeDao {

    /**
     * 
     * @param objectTypeId
     * @return
     */
    List<FinancialType> findFinancialTypes(Integer objectTypeId);

    /**
     * 
     * @param financialTypeId
     * @return
     */
    FinancialType findFinancialType(Integer financialTypeId);

    /**
     * 
     * @param financialCodeId
     * @return
     */
    FinancialCode findFinancialCode(Integer financialCodeId);

    /**
     * Loads an entry from the "FinancialCode" table. The given column name and
     * id is used for identification. The first matching (column contains id)
     * entry will be loaded.
     * @param column_name - the column name for the search
     * @param id - the identification
     * @return true = found an entry
     */
    FinancialCode findByColumnName(String column_name, String id) throws SQLException;

    /**
     * Search in database table "FinancialCode" for all rows whose column name
     * (given as a parameter) contains at least one of the keywords (given) as
     * part of any word in the column. The returned Vector is sorted by the
     * given column name.
     * 
     * @param keywords - the keywords which are used for the search
     * @param column_name - the column name to look for
     * @return a java.util.Vector which contains all rows that match the search criteria
     */
    Vector findByKeywords(String keywords, String column_name) throws SQLException;

    /**
     * Creates a new entry in the "FinancialCodeBean" table.
     * The properties for the new entry must be set before creating a new entry.
     * Updates the FinancialCodeID!
     * @param entity
     * @throws SQLException
     */
    public void create(FinancialCode entity) throws SQLException;

    /**
     * Stores an entry in the "FinancialCodeBean" table.
     * The properties for the entry must be set before storing it.
     * @param entity
     * @throws SQLException
     */
    void store(FinancialCode entity) throws SQLException;

}