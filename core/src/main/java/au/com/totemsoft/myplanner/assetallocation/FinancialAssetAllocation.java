/*
 * FinancialAssetAllocation.java
 *
 * Created on 10 October 2002, 08:58
 */

package au.com.totemsoft.myplanner.assetallocation;

import au.com.totemsoft.math.FormatedBigDecimal;
import au.com.totemsoft.myplanner.api.bean.ICode;
import au.com.totemsoft.myplanner.api.code.FinancialTypeEnum;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.db.ApirPicBean;
import au.com.totemsoft.myplanner.bean.db.AssetAllocationDataBean;
import au.com.totemsoft.myplanner.bean.db.IressAssetNameBean;

/**
 * Finds the asset allocation for a given Financial object. For shares the
 * methode check if it's a australian share or an international share. Todo
 * that, it check the ISIN code, the first 2 letters are the country code. <BR>
 * </BR> For units the method looks into the asset allocation table
 * (AssetAllocationData) to get the allocation for the given Financial object.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * 
 * @see au.com.totemsoft.myplanner.ui.assetallocation.AssetAllocationTableModel
 * @see au.com.totemsoft.myplanner.ui.assetallocation.CurrentAssetAllocationTableModel
 * @see au.com.totemsoft.myplanner.ui.assetallocation.NewAssetAllocationTableModel
 */
public class FinancialAssetAllocation {
// !!! The class used behind the implement keyword contains only constants. !!!

    /** Creates a new instance of FinancialAssetAllocation */
    public FinancialAssetAllocation() {
    }

    /**
     * Finds the asset allocation for a given Financial object. For shares the
     * methode check if it's a australian share or an international share. Todo
     * that, it check the ISIN code, the first 2 letters are the country code.
     * <BR>
     * </BR> For units the method looks into the asset allocation table to get
     * the allocation for the given Financial object.
     * 
     * @param f -
     *            a Financial object
     */
    public AssetAllocationTableRow findAssetAllocation(Financial f) {
        AssetAllocationTableRow aatr = new AssetAllocationTableRow();
        if (f == null) {
            return aatr;
        }
        if (f.getFinancialCode() != null) {
            // do we have a FinancialType?
            Integer financialTypeID = f.getFinancialType() == null ? FinancialTypeEnum.UNDEFINED.getId() : f.getFinancialTypeID();
            // check FinancialTypeID:
            if (financialTypeID == FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId()) {
                // INVESTMENT_SHARES_AUSTRALIAN
                // SUPERANNUATION_LISTED_SHARES
                // SUPERANNUATION_SHARES_AUSTRALIAN
                aatr = findShareAssetAllocation(f.getFinancialCode());
            } else if (financialTypeID == FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getId()) {
                // INVESTMENT_LISTED_UNIT_TRUSTS:
                // SUPERANNUATION_LISTED_UNIT_TRUST
                // SUPERANNUATION_LISTED_UNIT_TRUSTS
                // SUPERANNUATION_INCOME_FUNDS
                aatr = findUnitAssetAllocation(f.getFinancialCode());
            } else if (financialTypeID == FinancialTypeEnum.INVESTMENT_OTHER.getId()) {
                aatr.percent_in_other = new FormatedBigDecimal(100.0);
            } else if (financialTypeID == FinancialTypeEnum.INVESTMENT_PROPERTY.getId()) {
                aatr.percent_in_property = new FormatedBigDecimal(100.0);
            } else if (financialTypeID == FinancialTypeEnum.UNDEFINED.getId()) {
                aatr = findByFinancialCode(f.getFinancialCode());
            } else {
                // for the rest we don't know the asset allocation
            }
        } else if (f.getFinancialType() != null) {
            // check if we have at least an FinancialType, because the financial object could be an "Other" or "Investment
            Integer financialTypeID = f.getFinancialTypeID();
            // check if we have an investment property
            if (financialTypeID == FinancialTypeEnum.INVESTMENT_PROPERTY.getId()) {
                aatr.percent_in_property = new FormatedBigDecimal(100.0);
            }
            else if (financialTypeID == FinancialTypeEnum.INVESTMENT_OTHER.getId()) {
                aatr.percent_in_other = new FormatedBigDecimal(100.0);
            }
        }
        return aatr;
    }

    /**
     * Finds the asset allocation for a given FinancialCode object which is a
     * share. The methode check if it's a australian share or an international
     * share. Todo that, it check the ISIN code, the first 2 letters are the
     * country code.
     * 
     * @param code -
     *            a financial code
     */
    private AssetAllocationTableRow findShareAssetAllocation(ICode code) {
        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

        if (code != null) {
            String financialCodeDesc = code.getCode();

            if (financialCodeDesc != null) {
                try {
                    IressAssetNameBean ianb = new IressAssetNameBean();
                    boolean found = ianb.findByCode(financialCodeDesc);

                    // get ISIN code
                    String isin = ianb.getIsin();
                    // is it an Australian share?
                    if (found && isin != null && isin.startsWith("AU")) {
                        // yes, then set 100%
                        aatr.percent_in_aust_shares = new FormatedBigDecimal(
                                100.0);
                    } else {
                        // no, it's an international share
                        aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                100.0);
                    }
                } catch (java.sql.SQLException e) {
                    e.printStackTrace(System.err);
                }
            } // end if
        } // end if
        return aatr;
    }

    /**
     * Finds the asset allocation for a given FinancialCode object which is a
     * unit. The method looks into the asset allocation table to get the
     * allocation for the given Financial object.
     * 
     * @param code -
     *            a financial code
     */
    private AssetAllocationTableRow findUnitAssetAllocation(ICode code) {
        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

        if (code != null) {
            String financialCodeDesc = code.getCode();
            boolean found = false;
            int code_id = -1;

            try {
                if (financialCodeDesc != null) {
                    // find the code to the given apir-pic
                    ApirPicBean apb = new ApirPicBean();

                    found = apb.findByApirPic(financialCodeDesc);

                    code_id = apb.getCode();

                    // do we have a validate id
                    if (found && code_id > 0) {
                        // yes, let's find asset allocation
                        AssetAllocationDataBean aadb = new AssetAllocationDataBean();

                        found = aadb.findByCode(code_id);

                        if (found) {
                            aatr.percent_in_cash = new FormatedBigDecimal(aadb
                                    .getInCash());
                            aatr.percent_in_fixed_interest = new FormatedBigDecimal(
                                    aadb.getInFixedInterest());
                            aatr.percent_in_aust_shares = new FormatedBigDecimal(
                                    aadb.getInAustShares());
                            aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                    aadb.getInIntnlShares());
                            aatr.percent_in_property = new FormatedBigDecimal(
                                    aadb.getInProperty());
                            aatr.percent_in_other = new FormatedBigDecimal(aadb
                                    .getInOther());
                            aatr.include = new Boolean(true);
                        } // end if
                    } // end if
                } // end if
            } catch (java.sql.SQLException e) {
                e.printStackTrace(System.err);
            }
        } // end if

        return aatr;
    }

    /**
     * Finds the asset allocation for a given FinancialCode object which could
     * be a share or unit.
     * 
     * @param code -
     *            a financial code
     */
    public AssetAllocationTableRow findByFinancialCode(ICode code) {
        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

        if (code != null) {
            String financialCodeDesc = code.getCode();

            if (financialCodeDesc != null) {
                try {
                    // try to find code in iress table
                    IressAssetNameBean ianb = new IressAssetNameBean();
                    boolean found = ianb.findByCode(financialCodeDesc);

                    if (found) {
                        // it's a share
                        aatr = findShareAssetAllocation(code);
                    } else {
                        // not a share, maybe a unit
                        aatr = findUnitAssetAllocation(code);
                    }
                } catch (java.sql.SQLException e) {
                    e.printStackTrace(System.err);
                }
            } // end if
        } // end if
        return aatr;
    }

}
