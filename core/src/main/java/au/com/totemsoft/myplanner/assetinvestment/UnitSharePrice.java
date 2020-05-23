/*
 * UnitSharePrice.java
 *
 * Created on 24 July 2002, 09:01
 */

package au.com.totemsoft.myplanner.assetinvestment;

/**
 * Interface to access the price date, open price and close price of a unit or
 * share.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * 
 * @see au.com.totemsoft.myplanner.ui.financials.AddAssetInvestmentView
 * @see au.com.totemsoft.myplanner.bean.db.ApirPicBean
 * @see au.com.totemsoft.myplanner.bean.db.IressAssetNameBean
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
