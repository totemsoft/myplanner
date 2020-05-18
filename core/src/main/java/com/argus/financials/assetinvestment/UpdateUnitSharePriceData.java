/*
 * UpdateUnitSharePriceData.java
 *
 * Created on 1 August 2002, 14:55
 */

package com.argus.financials.assetinvestment;

/**
 * 
 * @author shibaevv
 * @version
 */
public interface UpdateUnitSharePriceData {

    public void setPriceDateValue(java.math.BigDecimal value);

    public java.math.BigDecimal getClosePrice();

    public java.math.BigDecimal getNumberOfUnitShares();
}
