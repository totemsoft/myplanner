/*
 * Current.java
 *
 * Created on 26 September 2002, 12:57
 */

package com.argus.financials.ui.assetallocation;

import java.util.Vector;

import com.argus.financials.api.code.FinancialTypeEnum;
import com.argus.financials.assetallocation.AssetAllocation;
import com.argus.financials.assetallocation.AssetAllocationTableRow;
import com.argus.financials.assetallocation.FinancialAssetAllocation;
import com.argus.financials.assetallocation.IAssetAllocation;
import com.argus.financials.bean.AssetCash;
import com.argus.financials.bean.AssetInvestment;
import com.argus.financials.bean.AssetSuperannuation;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.IncomeStream;
import com.argus.financials.strategy.model.DataCollectionModel;
import com.argus.math.FormatedBigDecimal;

/**
 * Represents the asset allocation as a TableModel, which is used in a JTable to
 * display the current asset allocation. The class can load and store the asset
 * allocation from and to a DataCollectionModel.
 * 
 * @author shibaevv
 * @version
 * 
 * @see com.argus.financials.ui.assetallocation.AssetAllocationTableModel
 * @see com.argus.financials.DataCollectionModel
 */
public class CurrentAssetAllocationTableModel extends AssetAllocationTableModel {

    /** Creates new CurrentAssetAllocationTableModel */
    public CurrentAssetAllocationTableModel(IAssetAllocation view, Vector new_data) {
        super(view, new_data);
        _model_name = "Current " + DEFAULT_MODELN_NAME + " - " + clientService.getId();
    }

    /**
     * Updates the view with the assets out of a DataCollection object. It uses
     * all assets of the following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param dcm -
     *            DataCollection object
     */
    public void updateModel(DataCollectionModel dcm) {

        // stores rows for jTable
        Vector new_data = new Vector();

        Double percent_in_cash = null;
        Double percent_in_fixed_interest = null;
        Double percent_in_aust_shares = null;
        Double percent_in_in_intnl_shares = null;
        Double percent_in_property = null;
        Double percent_in_other = null;

        if (dcm != null) {

            java.util.Collection collection = dcm
                    .getChildren(new Boolean(false));
            java.util.Iterator iter = collection.iterator();

            while (iter.hasNext()) {
                DataCollectionModel.Node node = (DataCollectionModel.Node) iter
                        .next();

                if (node.isFinancial()) { // isLeaf()

                    Financial f = node.getFinancialObject();

                    if (f instanceof AssetCash || f instanceof AssetInvestment
                            || f instanceof AssetSuperannuation
                            || f instanceof IncomeStream) {

                        percent_in_cash = new Double(0.0);
                        percent_in_fixed_interest = new Double(0.0);
                        percent_in_aust_shares = new Double(0.0);
                        percent_in_in_intnl_shares = new Double(0.0);
                        percent_in_property = new Double(0.0);
                        percent_in_other = new Double(0.0);

                        if (f instanceof AssetCash) {
                            // check if we have a Term Deposit
                            if (FinancialTypeEnum.isTermDeposit(f.getFinancialTypeID())) {
                                percent_in_fixed_interest = new Double(100.0);
                            } else {
                                percent_in_cash = new Double(100.0);
                            }
                        }

                        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

                        aatr.asset_name = new String(f.toString()); // new
                                                                    // String(
                                                                    // f.getFinancialTypeDesc()
                                                                    // );

                        AssetAllocation aa = null;
                        // do we have information about asset allocation
                        if (f.getAssetAllocationID() == null) {
                            // || f.getAssetAllocationID().intValue() < 0) {
                            // no, we habe to create a new item
                            aa = f.getAssetAllocation();

                            if (f instanceof AssetInvestment
                                    || f instanceof AssetSuperannuation) {
                                // try to find asset allocation in iress or
                                // morning database tables
                                FinancialAssetAllocation faa = new FinancialAssetAllocation();
                                AssetAllocationTableRow faa_aatr = null;

                                if (f instanceof AssetInvestment) {
                                    // we have a financial type
                                    faa_aatr = faa.findAssetAllocation(f);
                                } else {
                                    // we don't know it
                                    faa_aatr = faa.findByFinancialCode(f
                                            .getFinancialCode());
                                }

                                percent_in_cash = new Double(
                                        faa_aatr.percent_in_cash.doubleValue());
                                percent_in_fixed_interest = new Double(
                                        faa_aatr.percent_in_fixed_interest
                                                .doubleValue());
                                percent_in_aust_shares = new Double(
                                        faa_aatr.percent_in_aust_shares
                                                .doubleValue());
                                percent_in_in_intnl_shares = new Double(
                                        faa_aatr.percent_in_intnl_shares
                                                .doubleValue());
                                percent_in_property = new Double(
                                        faa_aatr.percent_in_property
                                                .doubleValue());
                                percent_in_other = new Double(
                                        faa_aatr.percent_in_other.doubleValue());
                            }

                            // and populate it
                            // aa.setAmount ( ( f.getAmount() == null ) ? new
                            // Double( 0.0 ) : new Double(
                            // f.getAmount().doubleValue() ) );
                            aa.setInCash(percent_in_cash);
                            aa.setInFixedInterest(percent_in_fixed_interest);
                            aa.setInAustShares(percent_in_aust_shares);
                            aa.setInIntnlShares(percent_in_in_intnl_shares);
                            aa.setInProperty(percent_in_property);
                            aa.setInOther(percent_in_other);
                            aa.setInclude(new Boolean(true));
                            aa.setAssetAllocationID(new Integer(-1));

                            // f.setAssetAllocation( aa );

                        } else {
                            // yes, than use the existing data
                            aa = f.getAssetAllocation();
                        }
                        // get always the current allocated amount from the
                        // Financial object
                        aa.setAmount((f.getAmount() == null) ? new Double(0.0)
                                : new Double(rint(f.getAmount().doubleValue(),
                                        NUMBER_OF_DIGITS)));

                        aatr.amount = (f.getAmount() == null) ? new FormatedBigDecimal(
                                0.0)
                                : new FormatedBigDecimal(f.getAmount()
                                        .doubleValue());// aa.getAmount();
                        aatr.percent_in_cash = new FormatedBigDecimal(aa
                                .getInCash());
                        aatr.percent_in_fixed_interest = new FormatedBigDecimal(
                                aa.getInFixedInterest());
                        aatr.percent_in_aust_shares = new FormatedBigDecimal(aa
                                .getInAustShares());
                        aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                aa.getInIntnlShares());
                        aatr.percent_in_property = new FormatedBigDecimal(aa
                                .getInProperty());
                        aatr.percent_in_other = new FormatedBigDecimal(aa
                                .getInOther());
                        aatr.total_in_percent = new FormatedBigDecimal("0.00");
                        aatr.include = aa.getInclude();

                        aatr.client_id = f.getOwnerId();
                        aatr.financial_id = f.getId();
                        aatr.asset_allocation_id = aa.getAssetAllocationID(); // (f.getAssetAllocationID()
                                                                                // ==
                                                                                // null)
                                                                                // ?
                                                                                // new
                                                                                // Integer(-1)
                                                                                // :
                                                                                // f.getAssetAllocationID();

                        new_data.add(aatr);

                    } else {
                        continue;
                    }
                }
            }
        }
        // update table rows
        setData(new_data);

        // save
        // saveModel( dcm );
    }

    /**
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param dcm -
     *            DataCollection object
     */
    public void saveModel(DataCollectionModel dcm) {

        if (dcm != null) {

            java.util.Collection collection = dcm.getChildren(Boolean.FALSE);
            java.util.Iterator iter = collection.iterator();

            while (iter.hasNext()) {
                DataCollectionModel.Node node = (DataCollectionModel.Node) iter
                        .next();

                if (node.isFinancial()) { // isLeaf()

                    Financial f = node.getFinancialObject();

                    if (f instanceof AssetCash || f instanceof AssetInvestment
                            || f instanceof AssetSuperannuation
                            || f instanceof IncomeStream) {

                        // search for financial id
                        AssetAllocationTableRow aatr = null;
                        AssetAllocation aa = null;

                        for (int i = 0; i < this._data.size() - 1; i++) { // -1,
                                                                            // because
                                                                            // ignore
                                                                            // last
                                                                            // table
                                                                            // row
                            aatr = (AssetAllocationTableRow) this._data
                                    .elementAt(i);

                            if (f.getId().equals(aatr.financial_id)) {
                                // update asset allocation...
                                // get asset allocation
                                aa = ((Financial) f).getAssetAllocation();
                                // and populate asset allocation
                                aa.setAmount(new Double(aatr.amount
                                        .doubleValue()));
                                aa.setInCash(new Double(aatr.percent_in_cash
                                        .doubleValue()));
                                aa.setInFixedInterest(new Double(
                                        aatr.percent_in_fixed_interest
                                                .doubleValue()));
                                aa.setInAustShares(new Double(
                                        aatr.percent_in_aust_shares
                                                .doubleValue()));
                                aa.setInIntnlShares(new Double(
                                        aatr.percent_in_intnl_shares
                                                .doubleValue()));
                                aa
                                        .setInProperty(new Double(
                                                aatr.percent_in_property
                                                        .doubleValue()));
                                aa.setInOther(new Double(aatr.percent_in_other
                                        .doubleValue()));
                                aa.setInclude(aatr.include);
                                aa
                                        .setAssetAllocationID(aatr.asset_allocation_id);
                                f
                                        .setAssetAllocationID(aatr.asset_allocation_id);
                                f.setModified(true);
                            }
                        }

                    } else {
                        continue;
                    }
                }
            }
        }
        // (mark as stored)
        this.setModified(false);
    }

}
