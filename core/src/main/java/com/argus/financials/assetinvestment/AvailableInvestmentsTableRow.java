/*
 * AssetInvestmentAvailableInvestmentsTableRow.java
 *
 * Created on 19 July 2002, 09:28
 */

package com.argus.financials.assetinvestment;

import com.argus.financials.api.bean.hibernate.FinancialCode;
import com.argus.financials.api.code.FinancialTypeEnum;
import com.argus.financials.api.service.FinancialService;
import com.argus.financials.code.FinancialType;
import com.argus.util.ReferenceCode;

public class AvailableInvestmentsTableRow {

    private static FinancialService financialService;
    public static void setFinancialService(FinancialService financialService) {
        AvailableInvestmentsTableRow.financialService = financialService;
    }

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
            return FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getId(); // 5 = Managed Funds (Listed Unit Trust)
        if (IRESS.equals(origin))
            return FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId(); // 4 = Listed Shares
        return FinancialTypeEnum.UNDEFINED.getId();

    }

    /**
     * The methode looks up for a "FinacialCode" in the "FinancialCode" table,
     * if it does not find an entry, it creates a new one.
     * 
     * @param investmentCode - financial code
     * @param financialTypeID - financial type id
     * @param description - financial code description
     * @return the corresponding financial code id
     */
    public static int getFinancialCodeID(String investmentCode, int financialTypeId, String description) {
        try {
            FinancialCode financialCode = financialService.findByFinancialCode(investmentCode);
            // do we have an entry in the "FinancialCode" table
            if (financialCode == null) {
                // we don't have an entry for the current unit/share name, create one
                financialCode = new FinancialCode();
                //financialCodeBean.setFinancialTypeId(financialTypeId);
                financialCode.setFinancialTypeId(-1); // -1 = <NULL> entry in database column
                financialCode.setCode(investmentCode);
                financialCode.setDescription(description);
                financialService.save(financialCode);
            }
            return financialCode.getId();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return 0;
        }
    }

}