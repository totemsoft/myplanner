/*
 * AvailableInvestmentsSearch.java
 *
 * Created on 19 July 2002, 16:09
 */

package com.argus.financials.assetinvestment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.argus.financials.api.bean.hibernate.FinancialCode;
import com.argus.financials.api.dao.FinancialCodeDao;
import com.argus.financials.bean.db.IressAssetNameBean;
import com.argus.financials.bean.db.UnitInformationSearchBean;

/**
 * AssetInvestmentAvailableInvestmentsSearch is responsible for load and store
 * information form the database tables "apir-pic" and "iress-asset-name".
 * 
 * @author shibaevv
 * @version 0.01
 * 
 * @see com.argus.financials.ui.financials.AddAssetInvestmentView
 * @see com.argus.financials.assetinvestment.AssetInvestmentAvailableInvestmentsTableModel
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSelectionInvestmentCodeComperator
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSelectionDescriptionComperator
 */
public class AvailableInvestmentsSearch {
    private Vector _table_rows_unit_information_search = null;

    private Vector _table_rows_iress_asset_name = null;

    private Vector _table_rows_financial_code = null;

    private UnitInformationSearchBean _unitInformationSearchBean = null;

    private IressAssetNameBean _iressAssetNameBean = null;

    private static FinancialCodeDao financialCodeDao;
    public static void setFinancialCodeDao(FinancialCodeDao financialCodeDao) {
        AvailableInvestmentsSearch.financialCodeDao = financialCodeDao;
    }

    private static final int INITIAL_VECTOR_SIZE = 128;

    private static final int INITIAL_VECTOR_GROWTH_SIZE = 32;

    /** Creates new ApirPicBean */
    public AvailableInvestmentsSearch() {
    }

    /**
     * Search in database tables "iress-asset" and "apir-pic" for all rows whose
     * description contains at least one of the keywords as part of any word in
     * the description column. The returned Vector is sorted by the column
     * description.
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector searchByDescription(String keywords) {
        Vector merge_table_rows = new Vector(INITIAL_VECTOR_SIZE,
                INITIAL_VECTOR_GROWTH_SIZE);

        _unitInformationSearchBean = new UnitInformationSearchBean();
        _iressAssetNameBean = new IressAssetNameBean();

        try {
            _table_rows_unit_information_search = _unitInformationSearchBean
                    .findByKeywordsSearchDescription(keywords);
            _table_rows_iress_asset_name = _iressAssetNameBean
                    .findByKeywordsSearchDescription(keywords);
            _table_rows_financial_code = financialCodeDao.findByKeywords(keywords, FinancialCode.COLUMN_DESC);
        } catch (java.sql.SQLException e) {
            e.printStackTrace(System.err);
        } finally {
        }

        merge_table_rows.addAll(_table_rows_unit_information_search);
        merge_table_rows.addAll(_table_rows_iress_asset_name);
        merge_table_rows.addAll(_table_rows_financial_code);

        // sort Vector by description
        Comparator descriptionCmp = new AvailableInvestmentsSelectionDescriptionComperator();
        Collections.sort((List) merge_table_rows, (Comparator) descriptionCmp);

        return merge_table_rows;
    }

    /**
     * Search in database tables "iress-asset" and "apir-pic" for all rows whose
     * investment code contains at least one of the keywords as part of any word
     * in the description column. The returned Vector is sorted by the column
     * investment code.
     * 
     * @param keywords -
     *            the keywords which are used for the search
     * @return a java.util.Vector which contains all rows that match the search
     *         criteria
     */
    public Vector searchByInvestmentCode(String keywords) {
        Vector merge_table_rows = new Vector(INITIAL_VECTOR_SIZE,
                INITIAL_VECTOR_GROWTH_SIZE);

        _unitInformationSearchBean = new UnitInformationSearchBean();
        _iressAssetNameBean = new IressAssetNameBean();

        try {
            _table_rows_unit_information_search = _unitInformationSearchBean
                    .findByKeywordsSearchInvestmentCode(keywords);
            _table_rows_iress_asset_name = _iressAssetNameBean
                    .findByKeywordsSearchInvestmentCode(keywords);
            _table_rows_financial_code = financialCodeDao.findByKeywords(keywords, FinancialCode.COLUMN_CODE);
        } catch (java.sql.SQLException e) {
            e.printStackTrace(System.err);
        } finally {
        }

        if (_table_rows_unit_information_search != null) merge_table_rows.addAll(_table_rows_unit_information_search);
        if (_table_rows_iress_asset_name != null) merge_table_rows.addAll(_table_rows_iress_asset_name);
        if (_table_rows_financial_code != null) merge_table_rows.addAll(_table_rows_financial_code);

        // sort Vector by description
        Comparator descriptionCmp = new AvailableInvestmentsSelectionInvestmentCodeComperator();
        Collections.sort((List) merge_table_rows, (Comparator) descriptionCmp);

        return merge_table_rows;
    }

}
