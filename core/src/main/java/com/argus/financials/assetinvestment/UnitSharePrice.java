/*
 * UnitSharePrice.java
 *
 * Created on 24 July 2002, 09:01
 */

package com.argus.financials.assetinvestment;

/**
 * Interface to access the price date, open price and close price of a unit or
 * share.
 * 
 * @author shibaevv
 * @version 0.01
 * 
 * @see com.argus.financials.ui.financials.AddAssetInvestmentView
 * @see com.argus.financials.bean.db.ApirPicBean
 * @see com.argus.financials.bean.db.IressAssetNameBean
 */
public abstract interface UnitSharePrice {
    /**
     * return the date for the open and close price
     */
    public String getPriceDate();

    public java.util.Date getPriceDate2();

    /**
     * return the close price
     */
    public double getClosePrice();

    /**
     * return the open price
     */
    public double getOpenPrice();

    /**
     * finds a unit/share in the database
     */
    public boolean findUnitShareByCode(String code_id)
            throws java.sql.SQLException;

    /**
     * get UnitSharePrice object
     */
    public void getUnitSharePrice();

}
