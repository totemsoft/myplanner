/*
 * InterfaceAssetAllocationView2.java
 *
 * Created on 10 March 2003, 13:19
 */

package au.com.totemsoft.myplanner.assetallocation;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public interface IAssetAllocation2 extends IAssetAllocation {

    public void setTotalInCash(double value);

    public void setTotalInFixedInterest(double value);

    public void setTotalInAustShares(double value);

    public void setTotalInIntnlShares(double value);

    public void setTotalInProperty(double value);

    public void setTotalInOther(double value);

    public void setTotalTotal(double value);

}
