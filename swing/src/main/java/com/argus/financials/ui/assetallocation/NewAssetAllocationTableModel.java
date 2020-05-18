/*
 * Current.java
 *
 * Created on 26 September 2002, 12:57
 */

package com.argus.financials.ui.assetallocation;

import java.util.Vector;

import com.argus.financials.assetallocation.IAssetAllocation;

/**
 * Represents the asset allocation as a TableModel, which is used in a JTable to
 * display the new asset allocation. The class can load and store the asset
 * allocation from and to a StrategyCollection.
 * 
 * @author shibaevv
 * @version
 * 
 * @see com.argus.financials.ui.assetallocation.AssetAllocationTableModel
 * @see com.argus.financials.StrategyCollection
 */
public class NewAssetAllocationTableModel extends AssetAllocationTableModel {

    /** Creates new CurrentAssetAllocationTableModel */
    public NewAssetAllocationTableModel(IAssetAllocation view, Vector data) {
        super(view, data);

        _model_name = "New " + DEFAULT_MODELN_NAME + " - " + clientService.getId();
    }

}
