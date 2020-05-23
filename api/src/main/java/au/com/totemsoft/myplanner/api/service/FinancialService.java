package au.com.totemsoft.myplanner.api.service;

import java.util.Map;

import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialType;

public interface FinancialService {

    /**
     * 
     * @param objectTypeId
     * @return
     */
    FinancialType[] findFinancialTypes(Integer objectTypeId);

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
     * Loads an entry from the "FinancialCode" table. The "FinancialCode" column
     * is used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id - use the apir-pic column as identifier
     * @return true = found an entry
     */
    FinancialCode findByFinancialCode(String code);

    /**
     * Loads an entry from the "FinancialCode" table. The "FinancialCodeDesc"
     * column is used for identification. The first matching entry will be loaded.
     * 
     * @param apir_pic_id - use the apir-pic column as identifier
     * @return true = found an entry
     */
    FinancialCode findByFinancialCodeDesc(String desc);

    void save(FinancialCode entity);

    <T> T findFinancial(Integer personId, Integer financialId);

    <T> Map<Integer, Map<Integer, T>> findFinancials(Integer personId, Integer strategyGroupId);

}