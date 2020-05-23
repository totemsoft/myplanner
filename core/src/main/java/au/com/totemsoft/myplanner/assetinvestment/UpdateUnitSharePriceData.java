/*
 * UpdateUnitSharePriceData.java
 *
 * Created on 1 August 2002, 14:55
 */

package au.com.totemsoft.myplanner.assetinvestment;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public interface UpdateUnitSharePriceData {

    public void setPriceDateValue(java.math.BigDecimal value);

    public java.math.BigDecimal getClosePrice();

    public java.math.BigDecimal getNumberOfUnitShares();
}
