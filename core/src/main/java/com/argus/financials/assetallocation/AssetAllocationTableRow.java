/*
 * AssetAllocationTableRow.java
 *
 * Created on 20 September 2002, 12:23
 */

package com.argus.financials.assetallocation;

import com.argus.math.FormatedBigDecimal;

/**
 * The class stores the asset allocation for one specific object.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 * 
 * @see com.argus.financials.ui.assetallocation.AssetAllocationTableModel
 * @see com.argus.financials.ui.assetallocation.CurrentAssetAllocationTableModel
 * @see com.argus.financials.ui.assetallocation.NewAssetAllocationTableModel
 */
public class AssetAllocationTableRow {

    public String asset_name = new String("");

    public FormatedBigDecimal amount = new FormatedBigDecimal(0.0);

    public FormatedBigDecimal percent_in_cash = new FormatedBigDecimal(0.0);

    public FormatedBigDecimal percent_in_fixed_interest = new FormatedBigDecimal(
            0.0);

    public FormatedBigDecimal percent_in_aust_shares = new FormatedBigDecimal(
            0.0);

    public FormatedBigDecimal percent_in_intnl_shares = new FormatedBigDecimal(
            0.0);

    public FormatedBigDecimal percent_in_property = new FormatedBigDecimal(0.0);

    public FormatedBigDecimal percent_in_other = new FormatedBigDecimal(0.0);

    public FormatedBigDecimal total_in_percent = new FormatedBigDecimal(0.0);

    public Boolean include = new Boolean(false);

    public Integer client_id = new Integer(-1);

    public Integer financial_id = new Integer(-1);

    public Integer asset_allocation_id = new Integer(-1);

    /** Creates new AssetAllocationTableRow */
    public AssetAllocationTableRow() {
    }

    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof AssetAllocationTableRow))
            return false;
        AssetAllocationTableRow aatr = (AssetAllocationTableRow) o;

        if (financial_id.equals(aatr.financial_id))
            return true;
        return false;
    }

    public int hashCode() {
        if (financial_id != null)
            return financial_id.intValue();

        return 0;
    }
}
