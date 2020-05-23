/*
 * DSSUtils.java
 *
 * Created on 6 May 2003, 15:19
 */

package au.com.totemsoft.myplanner.projection;

import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.myplanner.api.code.FinancialTypeID;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.IRegularType;
import au.com.totemsoft.myplanner.bean.IncomeStream;
import au.com.totemsoft.myplanner.code.FundType;
import au.com.totemsoft.myplanner.projection.dss.DSSElement;
import au.com.totemsoft.myplanner.table.model.FinancialColumnID;
import au.com.totemsoft.myplanner.table.model.FinancialTableModel2;
import au.com.totemsoft.myplanner.tree.FinancialTreeFilter;
import au.com.totemsoft.myplanner.tree.FinancialTreeFilters;
import au.com.totemsoft.myplanner.tree.FinancialTreeModel;
import au.com.totemsoft.myplanner.tree.FinancialTreeStructure;
import au.com.totemsoft.util.ReferenceCode;

public class DSSUtils {

    private static final int[] rules = IRegularType.RULES[IRegularType.iTAXANALYSIS];

    private FinancialTreeModel model;

    private DetailsTableModel assetCashTableModel;

    private DetailsTableModel assetPersonalTableModel;

    private DetailsTableModel assetSuperTableModel;

    private DetailsTableModel assetInvestmentTableModel;

    private DetailsTableModel assetIncomeStreamTableModel;

    private DetailsTableModel incomeTableModel;

    /** Creates a new instance of DSSUtils */
    public DSSUtils(java.util.Map financials) {
        updateModels(financials);
    }

    public void updateModels(java.util.Map financials) {

        model = new FinancialTreeModel(financials);

        FinancialTreeStructure fts = new FinancialTreeStructure();
        model.setStructure(fts);
        fts.add(FinancialTreeStructure.FINANCIAL_CLASS);
        fts.add(FinancialTreeStructure.FINANCIAL_TYPE);

        model.setRules(rules);

        assetCashTableModel = new DetailsTableModel(
                FinancialClassID.RC_ASSET_CASH);
        assetPersonalTableModel = new DetailsTableModel(
                FinancialClassID.RC_ASSET_PERSONAL);
        assetSuperTableModel = new DetailsTableModel(
                FinancialClassID.RC_ASSET_SUPERANNUATION);
        assetInvestmentTableModel = new DetailsTableModel(
                FinancialClassID.RC_ASSET_INVESTMENT);
        assetIncomeStreamTableModel = new DetailsTableModel(
                FinancialClassID.RC_INCOME_STREAM);
        incomeTableModel = new DetailsTableModel(
                FinancialClassID.RC_REGULAR_INCOME);

    }

    public java.util.Vector getDSSElements(final ReferenceCode fund,
            Integer owner) {

        // all where FinancialType = annuity, and FundType = fund
        // FinancialTreeFilter filter = new FinancialTreeFilter(
        // FinancialTreeStructure.FINANCIAL_TYPE );
        // filter.add( new Integer( FinancialTypeID.INCOMESTREAM_ANNUITY_POLICY
        // ) );
        // All Financial Types
        FinancialTreeFilter filter = new FinancialTreeFilter(
                FinancialTreeStructure.FUND_TYPE);
        filter.add(fund.getCodeId());

        DSSElement elem;
        java.util.Vector data = new java.util.Vector();
        Financial[] financials = assetIncomeStreamTableModel
                .getFinancials(filter);
        for (int i = 0; i < financials.length; i++) {
            IncomeStream f = (IncomeStream) financials[i];
            if (!owner.equals(f.getOwnerCodeID()))
                continue;

            java.math.BigDecimal amount1 = f.getFinancialYearAmount();
            java.math.BigDecimal amount2 = f.getDrawdownAnnualAmount();
            java.math.BigDecimal amount3 = f.getDeductibleDSS();
            data
                    .add(new DSSElement(f.toString(), fund, f.getOwnerCodeID()
                            .intValue(), amount1 == null ? 0. : amount1
                            .doubleValue(), (FundType.rcANNUITY_LONG
                            .equals(fund) || amount1 == null) ? 0. : amount1
                            .doubleValue(), amount2 == null ? 0. : amount2
                            .doubleValue(), (amount2 == null ? 0. : amount2
                            .doubleValue())
                            - (amount3 == null ? 0. : amount3.doubleValue())));
        }

        return data;

    }

    public double doubleValue(String componentName) {
        return doubleValue(componentName, FinancialColumnID.REAL_AMOUNT);
    }

    public double doubleValue(String componentName, int columnIndex) {

        DetailsTableModel tableModel;
        FinancialTreeFilter filter = null;

        if (DocumentNames.CASH_ASSET_AA.equals(componentName)) {
            tableModel = assetCashTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.ASSET_CASH);

        } else if (DocumentNames.MAN_FUNDS_AA.equals(componentName)) {
            tableModel = assetInvestmentTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter
                    .add(new Integer(
                            FinancialTypeID.INVESTMENT_LISTED_UNIT_TRUST));

        } else if (DocumentNames.SHARES_AA.equals(componentName)) {
            tableModel = assetInvestmentTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(new Integer(FinancialTypeID.INVESTMENT_LISTED_SHARES));

        } else if (DocumentNames.OTHER_INV_AA.equals(componentName)) {
            tableModel = assetInvestmentTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(new Integer(FinancialTypeID.INVESTMENT_OTHER));

        } else if (DocumentNames.INV_PRO_AA.equals(componentName)) {
            tableModel = assetInvestmentTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(new Integer(FinancialTypeID.INVESTMENT_PROPERTY));

        } else if (DocumentNames.DEEMED_SUP_AA.equals(componentName)) {
            tableModel = assetSuperTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.ASSET_SUPERANNUATION);

        } else if (DocumentNames.STA_AA.equals(componentName)) {
            tableModel = assetIncomeStreamTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter
                    .add(new Integer(
                            FinancialTypeID.INCOMESTREAM_ANNUITY_POLICY));

            // all where fund type = FundType.rcANNUITY_SHORT
            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FUND_TYPE);
            filter.add(FundType.rcANNUITY_SHORT.getCodeId());

        } else if (DocumentNames.HC_AA.equals(componentName)) {
            tableModel = assetPersonalTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.PERSONAL_HOME_CONTENTS);

        } else if (DocumentNames.HOME_OWNER.equals(componentName)) {
            tableModel = assetPersonalTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.PERSONAL_FAMILY_HOME);

        } else if (DocumentNames.VBC_AA.equals(componentName)) {
            tableModel = assetPersonalTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.PERSONAL_MOTOR_VEHICLE);
            filter.add(FinancialTypeID.PERSONAL_BOAT);
            filter.add(FinancialTypeID.PERSONAL_CARAVAN);
            filter.add(FinancialTypeID.PERSONAL_MOTOR_BIKE);

        } else if (DocumentNames.HH_AA.equals(componentName)) {
            tableModel = assetPersonalTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.PERSONAL_HOLIDAY_HOME);

        } else if (DocumentNames.OTHER_PER_AA.equals(componentName)) {
            tableModel = assetPersonalTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.PERSONAL_OTHER);
            filter.add(FinancialTypeID.PERSONAL_SPORTING_EQUIPMENT);
            filter.add(FinancialTypeID.PERSONAL_TOOLS);
            filter.add(FinancialTypeID.PERSONAL_JEWELLERY);

        } else if (DocumentNames.SAL_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.INCOME_SALARY);

        } else if (DocumentNames.OTHER_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_TYPE);
            filter.add(FinancialTypeID.INCOME_OTHER);

        } else if (DocumentNames.INV_PRO_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            // all where associated financial type = INVESTMENT_PROPERTY
            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.ASSOCIATED_FINANCIAL_TYPE);
            filter.add(new Integer(FinancialTypeID.INVESTMENT_PROPERTY));

            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.REGULAR_INCOME);

        } else if (DocumentNames.CASH_ASSET_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            // all where associated financial type = INVESTMENT_PROPERTY
            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.ASSOCIATED_FINANCIAL_CLASS);
            filter.add(FinancialClassID.ASSET_CASH);

            // all where TAXABLE = true
            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.TAXABLE);
            filter.add(Boolean.TRUE);

            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.REGULAR_INCOME);

        } else if (DocumentNames.MAN_FUNDS_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            // all where associated financial type = INVESTMENT_PROPERTY
            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.ASSOCIATED_FINANCIAL_TYPE);
            filter
                    .add(new Integer(
                            FinancialTypeID.INVESTMENT_LISTED_UNIT_TRUST));

            // all where fund type = FundType.rcANNUITY_SHORT
            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.TAXABLE);
            filter.add(Boolean.TRUE);

            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.REGULAR_INCOME);

        } else if (DocumentNames.SHARES_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            // all where associated financial type = INVESTMENT_PROPERTY
            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.ASSOCIATED_FINANCIAL_TYPE);
            filter.add(new Integer(FinancialTypeID.INVESTMENT_LISTED_SHARES));

            // all where fund type = FundType.rcANNUITY_SHORT
            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.TAXABLE);
            filter.add(Boolean.TRUE);

            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.REGULAR_INCOME);

        } else if (DocumentNames.STA_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            // all where associated financial type = INVESTMENT_PROPERTY
            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.ASSOCIATED_FUND_TYPE);
            filter.add(FundType.rcANNUITY_SHORT);

            // all where fund type = FundType.rcANNUITY_SHORT
            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.TAXABLE);
            filter.add(Boolean.TRUE);

            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.REGULAR_INCOME);

        } else if (DocumentNames.OTHER_INV_AI.equals(componentName)) {
            tableModel = incomeTableModel;

            // all where associated financial type = INVESTMENT_PROPERTY
            filter = new FinancialTreeFilter(
                    FinancialTreeStructure.ASSOCIATED_FINANCIAL_TYPE);
            filter.add(new Integer(FinancialTypeID.INVESTMENT_OTHER));

            // all where fund type = FundType.rcANNUITY_SHORT
            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.TAXABLE);
            filter.add(Boolean.TRUE);

            filter = new FinancialTreeFilter(filter,
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(FinancialClassID.REGULAR_INCOME);

        } else {
            return 0.;

        }

        java.math.BigDecimal amount = (java.math.BigDecimal) tableModel
                .getValue(columnIndex, filter);
        return amount == null ? 0. : amount.doubleValue();

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private class DetailsTableModel extends FinancialTableModel2 {

        // private FinancialTreeFilter financialTypeFilter;

        private DetailsTableModel(ReferenceCode finClass) {

            super();

            FinancialTreeFilters filters = model.getFilters();
            filters.clear();

            FinancialTreeFilter filter = new FinancialTreeFilter(
                    FinancialTreeStructure.FINANCIAL_CLASS);
            filter.add(finClass);

            // build tree model
            setFinancialTreeModel(model.buildCopy());

            /*
             * // construct financialTypeFilter financialTypeFilter = new
             * FinancialTreeFilter( FinancialTreeStructure.FINANCIAL_CLASS );
             * financialTypeFilter.add( finClass );
             *  // for the financial type specified financialTypeFilter = new
             * FinancialTreeFilter( financialTypeFilter,
             * FinancialTreeStructure.FINANCIAL_TYPE );
             * //financialTypeFilter.add( financialTypeID );
             */
        }

    }

}
