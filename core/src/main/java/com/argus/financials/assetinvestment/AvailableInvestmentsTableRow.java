/*
 * AssetInvestmentAvailableInvestmentsTableRow.java
 *
 * Created on 19 July 2002, 09:28
 */

package com.argus.financials.assetinvestment;

/**
 * Stores an asset/investment description and code.
 * 
 * @author shibaevv
 * @version 0.01
 * 
 * @see com.argus.financial.assetinvestment.AvailableInvestmentsSearch
 * @see com.argus.financial.assetinvestment.AssetInvestmentAvailableInvestmentsTableModel
 * @see com.argus.financial.assetinvestment.AvailableInvestmentsSelectionInvestmentCodeComperator
 * @see com.argus.financial.assetinvestment.AvailableInvestmentsSelectionDescriptionComperator
 */

import com.argus.financials.bean.db.FinancialCodeBean;
import com.argus.financials.code.FinancialType;
import com.argus.financials.code.FinancialTypeID;
import com.argus.util.ReferenceCode;

public class AvailableInvestmentsTableRow {

    public static final String APIR_PIC = "apir-pic";

    public static final String IRESS = "iress-asset-name";

    public String origin; // the name of the database table in which the entry
                            // was found

    public String investmentCode; // the investment code for the asset or
                                    // investment

    public String description; // description of the investment

    public String code; // connects a unit/share with it's price

    // !!! price_code_id = <"code" column in database table> !!!
    public String institution; // institution

    /** Creates new AssetInvestmentAvailableInvestmentsTableRow */
    public AvailableInvestmentsTableRow() {
    }

    /** Creates new AssetInvestmentAvailableInvestmentsTableRow */
    public AvailableInvestmentsTableRow(String new_origin,
            String new_investmentCode, String new_description, String new_code,
            String new_institution) {

        origin = new_origin;
        investmentCode = new_investmentCode;
        description = new_description;
        code = new_code;
        institution = new_institution;
    }

    public String toString() {
        return (investmentCode == null ? "" : investmentCode.trim())
                + (description == null ? "" : ", " + description.trim());
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public int getFinancialTypeID() {
        return getFinancialTypeID(origin);
    }

    public int getFinancialCodeID() {
        return getFinancialCodeID(investmentCode, getFinancialTypeID(),
                description);
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public static int getFinancialTypeID(String origin) {

        if (APIR_PIC.equals(origin))
            return FinancialTypeID.INVESTMENT_LISTED_UNIT_TRUST; // 5 =
                                                                    // Managed
                                                                    // Funds
                                                                    // (Listed
                                                                    // Unit
                                                                    // Trust)
        if (IRESS.equals(origin))
            return FinancialTypeID.INVESTMENT_LISTED_SHARES; // 4 = Listed
                                                                // Shares
        return FinancialTypeID.UNDEFINED;

    }

    /**
     * The methode looks up for a "FinacialCode" in the "FinancialCode" table,
     * if it does not find an entry, it creates a new one.
     * 
     * @param investmentCode -
     *            financial code
     * @param financialTypeID -
     *            financial type id
     * @param description -
     *            financial code description
     * @return the corresponding financial code id
     */
    public static int getFinancialCodeID(String investmentCode,
            int financialTypeID, String description) {
        try {
            FinancialCodeBean financialCodeBean = new FinancialCodeBean();

            // do we have an entry in the "FinancialCode" table
            if (investmentCode != null
                    && !financialCodeBean.findByFinancialCode(investmentCode)) {
                // we don't have an entry for the current unit/share name,
                // create one

                // financialCodeBean.setFinancialTypeID ( financialTypeID );
                financialCodeBean.setFinancialTypeID(-1); // -1 = <NULL> entry
                                                            // in database
                                                            // column
                financialCodeBean.setFinancialCode(investmentCode);
                financialCodeBean.setFinancialCodeDesc(description);

                financialCodeBean.create();

                // add new financial code
                FinancialType.addFinancialCode(
                        // getObjectType(),
                        new Integer(0),
                        // new Integer( financialCodeBean.getFinancialTypeID()
                        // ),
                        new Integer(FinancialTypeID.UNDEFINED),
                        new ReferenceCode(financialCodeBean
                                .getFinancialCodeID(), financialCodeBean
                                .getFinancialCode(), financialCodeBean
                                .getFinancialCodeDesc()));

            }

            return financialCodeBean.getFinancialCodeID();

        } catch (java.sql.SQLException e) {
            e.printStackTrace(System.err);
            return 0;
        }

    }

}
