/*
 * AssetAllocationView.java
 *
 * Created on 26 September 2002, 17:19
 */

package com.argus.financials.assetallocation;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public interface IAssetAllocation {

    public void setTotalInCash(String value);

    public void setTotalInFixedInterest(String value);

    public void setTotalInAustShares(String value);

    public void setTotalInIntnlShares(String value);

    public void setTotalInProperty(String value);

    public void setTotalInOther(String value);

    public void setTotalTotal(String value);

}
