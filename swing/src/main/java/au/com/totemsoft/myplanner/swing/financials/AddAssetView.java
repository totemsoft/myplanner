/*
 * AddAssetView.java
 *
 * Created on 13 November 2002, 11:56
 */

package au.com.totemsoft.myplanner.swing.financials;

import org.apache.commons.lang.StringUtils;

import au.com.totemsoft.io.IOUtils;
import au.com.totemsoft.myplanner.api.bean.ICode;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode;
import au.com.totemsoft.myplanner.api.code.FinancialTypeEnum;
import au.com.totemsoft.myplanner.assetinvestment.AvailableInvestmentsTableRow;
import au.com.totemsoft.myplanner.assetinvestment.UnitSharePrice;
import au.com.totemsoft.myplanner.bean.Asset;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.db.ApirPicBean;
import au.com.totemsoft.myplanner.bean.db.AssetAllocationBean;
import au.com.totemsoft.myplanner.bean.db.SharePriceDataBean;
import au.com.totemsoft.myplanner.bean.db.UnitPriceDataBean;
import au.com.totemsoft.swing.SwingUtil;
import au.com.totemsoft.util.ReferenceCode;

public abstract class AddAssetView extends AddFinancialView
// implements UpdateUnitSharePriceData
{
    public static final String UNITS = "UNITS";
    public static final String UNIT_PRICE = "UNIT_PRICE";
    public static final String UNIT_PRICE_DATE = "UNIT_PRICE_DATE";
    public static final String CURRENT_VALUE = "CURRENT_VALUE";

    // remember old FinancialCode, needed to decided if we need to create a new
    // FinancialCode
    protected ICode old_FinancialCode = null;

    protected boolean updated_by_search = false;

    /** Creates a new instance of AddAssetView */
    protected AddAssetView() {
        //super();
    }

    public abstract int getDefaultFinancialTypeID(String source);

    public abstract String getDefaultFinancialTypeDesc(String source);

    public abstract void setAssetInvestmentName(String value);

    public abstract void setInsitutionName(String value);

    public abstract void setAssetInvestmentCode(String value);

    // UpdateUnitSharePriceData
    public abstract java.math.BigDecimal getNumberOfUnitShares();

    public abstract void setNumberOfUnitShares(java.math.BigDecimal value);

    public abstract java.util.Date getPriceDate();

    public abstract void setPriceDate(java.util.Date value);

    public abstract java.math.BigDecimal getPriceDateValue();

    public abstract void setPriceDateValue(java.math.BigDecimal value);

    public abstract java.math.BigDecimal getClosePrice();

    public abstract void setClosePrice(java.math.BigDecimal value);

    protected void doSearch() {
        // get search view
        AssetInvestmentSelectionByView view = AssetInvestmentSelectionByView
                .getInstance();

        // display view, modal
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(this);
        java.awt.Frame frame = null;
        if (w instanceof java.awt.Dialog)
            w = w.getOwner();
        if (w instanceof java.awt.Frame)
            frame = (java.awt.Frame) w;
        SwingUtil.add2Dialog(frame, view.getViewCaption(), true, view, false,
                true);

        // was one row selected and the ok button selected?
        if (view.getSelectedRowNumber() >= 0
                && view.getResult() == javax.swing.JOptionPane.OK_OPTION) {
            updateInputFields(view.getSelectedRow());
            updated_by_search = true;
        }

    }

    public static AvailableInvestmentsTableRow findInvestment(
            java.awt.Component comp) {
        // get search view
        AssetInvestmentSelectionByView view = AssetInvestmentSelectionByView
                .getInstance();

        // display view, modal
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(comp);
        java.awt.Frame frame = null;
        if (w instanceof java.awt.Dialog)
            w = w.getOwner();
        if (w instanceof java.awt.Frame)
            frame = (java.awt.Frame) w;
        SwingUtil.add2Dialog(frame, view.getViewCaption(), true, view, false,
                true);

        // was one row selected and the ok button selected?
        if (view.getSelectedRowNumber() >= 0
                && view.getResult() == javax.swing.JOptionPane.OK_OPTION) {
            return view.getSelectedRow();

        }

        return null;
    }

    /**
     * This method updates all input fields with the selected asset/investment
     * data.
     * 
     * @param a
     *            selected row
     * @see au.com.totemsoft.myplanner.assetinvestment.AvailableInvestmentsTableRow
     */
    protected void updateInputFields(AvailableInvestmentsTableRow row) {

        int financialTypeId = getDefaultFinancialTypeID(row.origin);
        String financialTypeDesc = getDefaultFinancialTypeDesc(row.origin);

        String investmentName = row.description;// .trim();
        String investmentCode = row.investmentCode;// .trim();
        String institutionName = row.institution;// .trim();

        // set new values for input fields
        setAssetInvestmentName(investmentName);
        setInsitutionName(institutionName);
        setAssetInvestmentCode(investmentCode);

        // check if we have an entry in the "FinancialCode" table, if not create
        // one
        int financialCodeID = checkFinancialCode(investmentCode,
                financialTypeId, investmentName);

        // update current price fields
        updateCurrentUnitsSharesPriceFields(row.code, financialTypeId);

        Financial f = getFinancial();
        f.setFinancialTypeId(financialTypeId);

        // store old FinancialCode
        old_FinancialCode = f.getFinancialCode();
        // update FinancialCode
        f.setFinancialCode(new FinancialCode(financialCodeID, investmentCode, investmentName, financialTypeId));

    }

    protected void unitSharePriceChanged(String source) {

        java.math.BigDecimal units = getNumberOfUnitShares();
        java.math.BigDecimal price = getClosePrice();
        java.math.BigDecimal value = getPriceDateValue();

        if (UNITS.equals(source) && units != null && units.doubleValue() > 0.) { // 1st
                                                                                    // priority

            if (price != null && price.doubleValue() > 0.) {
                value = units.multiply(price);
                setPriceDateValue(value);
            }

        } else if (CURRENT_VALUE.equals(source) && value != null
                && value.doubleValue() > 0.) { // 2nd priority

            if (price != null && price.doubleValue() > 0.) {
                units = value.divide(price, 4, java.math.BigDecimal.ROUND_DOWN);
                setNumberOfUnitShares(units);
            }

        } else if (UNIT_PRICE.equals(source) && price != null
                && price.doubleValue() > 0.) { // 3rd priority

            unitSharePriceChanged(UNITS);

        }

    }

    /**
     * Loads the current price for a unit or share and updates the GUI fields.
     * 
     * @param financialCode -
     *            a code out of the database column "code" ("apir-pic" and
     *            "iress-asset-name")
     * @param financialTypeID -
     *            the investment type (units,shares, ...)
     */
    protected void updateCurrentUnitsSharesPriceFields(String financialCode,
            int financialTypeID) {
        UnitSharePrice unitSharePrice = getUnitSharePrice(financialCode,
                financialTypeID);
        if (unitSharePrice == null)
            return;

        // set new values for fields
        setPriceDate(unitSharePrice.getPriceDate2());

        java.math.BigDecimal unitShares = getNumberOfUnitShares();
        setPriceDateValue(unitShares == null ? null : unitShares
                .multiply(new java.math.BigDecimal(unitSharePrice
                        .getClosePrice())));

        if (unitSharePrice.getClosePrice() != 0.)
            setClosePrice(new java.math.BigDecimal(unitSharePrice
                    .getClosePrice()));

    }

    /**
     * Loads the current price for a unit or share and updates the GUI fields.
     * 
     * @param financialCode -
     *            an asset investment code
     * @param financialTypeID -
     *            the investment type (units,shares,...)
     */
    protected void updateCurrentUnitsSharesPrice(ReferenceCode financialCode) {

        // do not do anything if...
        if (getClosePrice() == null || getClosePrice().doubleValue() == 0.)
            return;
        if (getNumberOfUnitShares() == null
                || getNumberOfUnitShares().doubleValue() == 0.)
            return;

        setPriceDate(null);
        setPriceDateValue(null);
        setClosePrice(null);

        if (financialCode == null)
            return;

        UnitSharePrice unitSharePrice = null;

        // we don't know the database table
        try {
            String code = financialCode.getCode();

            // try to find it by investment code
            ApirPicBean apirPicBean = new ApirPicBean();
            if (apirPicBean.findByApirPic(code)) {
                code = Integer.toString(apirPicBean.getCode());
                unitSharePrice = new UnitPriceDataBean();
            } else {
                unitSharePrice = new SharePriceDataBean();
            }

            // find unit/share in database
            unitSharePrice.findUnitShareByCode(code);

            setPriceDate(unitSharePrice.getPriceDate2());

            java.math.BigDecimal unitShares = getNumberOfUnitShares();
            setPriceDateValue(unitShares == null ? null : unitShares
                    .multiply(new java.math.BigDecimal(unitSharePrice
                            .getClosePrice())));

            setClosePrice(new java.math.BigDecimal(unitSharePrice
                    .getClosePrice()));

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    /**
     * The methode looks up for a "FinacialCode" in the "FinancialCode" table,
     * if it does not find an entry, it creates a new one.
     * 
     * @param investmentCode - financial code
     * @param financialTypeId - financial type id
     * @param description - financial code description
     * @return the corresponding financial code id
     */
    protected int checkFinancialCode(String investmentCode, int financialTypeId, String description) {
        try {
            FinancialCode financialCode = financialService.findByFinancialCode(investmentCode);
            // do we have an entry in the "FinancialCode" table
            if (financialCode == null) {
                financialCode = new FinancialCode();
                // financialCodeBean.setFinancialTypeId(financialTypeId);
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

    /**
     * The methode looks up for a "FinacialCodeDesc" in the "FinancialCode"
     * table, if it does not find an entry, it creates a new one.
     * 
     * @param investmentCode - financial code
     * @param financialTypeID - financial type id
     * @param description - financial code description
     * @return the corresponding financial code id
     */
    private int checkFinancialCodeDesc(String investmentCode, int financialTypeId, String description) {
        try {
            FinancialCode financialCode = financialService.findByFinancialCodeDesc(description);
            // do we have an entry in the "FinancialCode" table
            if (financialCode == null) {
                financialCode = new FinancialCode();
                // we don't have an entry for the current unit/share name, create one
                // financialCode.setFinancialTypeID (financialTypeId);
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

    /**
     * Locates the correct bean for a unit or share to get data from database.
     * 
     * @param financialCode -
     *            a investment code
     * @param financialTypeID -
     *            the investment type (units/shares)
     */
    private UnitSharePrice getUnitSharePrice(String financialCode,
            int financialTypeID) {
        UnitSharePrice unitSharePrice = null;

        // in which table? (check origin)
        if (financialTypeID == FinancialTypeEnum.SUPERANNUATION_LISTED_UNIT_TRUST.getId()
         || financialTypeID == FinancialTypeEnum.SUPERANNUATION_LISTED_UNIT_TRUSTS.getId()) {
            // apir-pic table (units)
            // get unit price and price date from database table
            // "unit-price-data"
            unitSharePrice = new UnitPriceDataBean();
        } else {
            // iress-asset-name table (shares)
            // get unit price and price date from database table
            // "share-price-data"
            unitSharePrice = new SharePriceDataBean();
        }

        try {
            // find unit/share in database
            unitSharePrice.findUnitShareByCode(financialCode);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

        return unitSharePrice;
    }

    protected void updateFinancialCode(Asset asset, String investmentName,
            String investmentCode) throws Exception {

        Integer financialTypeId = asset.getFinancialTypeID();

        // has the investment name (asset name) and investment code changed,
        // the user could have searched for a new investment or changed the
        // asset name!?!
        if (investmentName != null && investmentCode != null) {
            // do we have a new name ?
            if (!investmentName.equals(old_FinancialCode == null ? null : old_FinancialCode.getDescription())) {
                // than we should have a new investment code too
                if (investmentCode != null && investmentCode.length() > 0
                        && !investmentCode.equals(old_FinancialCode == null ? null : old_FinancialCode.getCode())) {
                    // yes, we have a new investment code too
                } else {
                    /*
                     * ATTENTION: There are rows in the product-information
                     * table which have the same code, but a different full-name
                     * column!!!
                     */
                    if (!updated_by_search) {
                        // no, only the name has change, than we have to create
                        // a new investment code
                        investmentCode = null;
                    } else {
                        // do nothing, use the existing FinancialCode
                    }
                    updated_by_search = false;
                }
                // we have to delete the connected asset allocation, because the
                // asset has changed!
                deleteAssetAllocation(asset);
            }
        }
        // END

        // BEGIN: check/create investment code, when necessary
        // do we have an investment code?
        if (StringUtils.isBlank(investmentCode)) {
            // no, than create a new one
            String crc32 = createCRC32(investmentName);
            // investmentCode = "#<CRC32>"
            investmentCode = "#" + crc32;
        }
        // END: check/create investment code, when necessary

        if (StringUtils.isNotBlank(investmentCode)) {
            FinancialCode financialCode = financialService.findByFinancialCode(investmentCode);
            if (financialCode == null) {
                financialCode = new FinancialCode();
                financialCode.setFinancialTypeId(-1); // -1 = <NULL> entry in database column
                financialCode.setCode(investmentCode);
                financialCode.setDescription(investmentName);
                financialService.save(financialCode);
            }
            // asset.setFinancialTypeID(financialTypeId);
            // update old FinancialCode
            old_FinancialCode = financialCode;
            asset.setFinancialCode(financialCode);
        }
    }

    /**
     * Creates a CRC32 checksum for a given String.
     * 
     * @param str -
     *            a source String
     * @return crc32_str - the calculated checksum for the given string
     * 
     * @java.util.zip.CRC32
     */
    protected String createCRC32(String str) {

        // now, create unique FinancialCode
        try {
            java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();

            byte[] b_array = IOUtils.writeObjectToByteArray(str);
            crc32.update(b_array);

            Long crc = new Long(crc32.getValue());

            return crc.toString();

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return "";
        }

    }

    /**
     * Deletes the current asset allocation for a given Financial object.
     * 
     * @param f -
     *            a Financial object
     */
    protected void deleteAssetAllocation(Financial f) {

        if (f != null) {
            Integer aaid = f.getAssetAllocationID();

            if (aaid != null) {
                // delete asset allocation from asset and database
                f.setAssetAllocation(null);
                f.setAssetAllocationID(null);

                try {
                    AssetAllocationBean aab = new AssetAllocationBean();
                    aab.delete(aaid);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            } // end if
        } // end if
    }

    protected String deleteParenthesis(String str) {
        String help = "";

        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != '(' && str.charAt(i) != ')') {
                    help = help + str.charAt(i);
                }
            }
        }

        return help;
    }

}
