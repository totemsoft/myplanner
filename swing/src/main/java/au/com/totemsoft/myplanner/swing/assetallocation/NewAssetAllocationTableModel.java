/*
 * Current.java
 *
 * Created on 26 September 2002, 12:57
 */

package au.com.totemsoft.myplanner.swing.assetallocation;

import java.util.Vector;

import au.com.totemsoft.myplanner.assetallocation.IAssetAllocation;

/**
 * Represents the asset allocation as a TableModel, which is used in a JTable to
 * display the new asset allocation. The class can load and store the asset
 * allocation from and to a StrategyCollection.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 * 
 * @see au.com.totemsoft.myplanner.ui.assetallocation.AssetAllocationTableModel
 * @see au.com.totemsoft.myplanner.StrategyCollection
 */
public class NewAssetAllocationTableModel extends AssetAllocationTableModel {

    /** Creates new CurrentAssetAllocationTableModel */
    public NewAssetAllocationTableModel(IAssetAllocation view, Vector data) {
        super(view, data);

        _model_name = "New " + DEFAULT_MODELN_NAME + " - " + clientService.getId();
    }

}
