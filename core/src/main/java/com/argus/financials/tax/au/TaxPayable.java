/*
 * TaxableIncome.java
 *
 * Created on 5 February 2002, 00:00
 */

package com.argus.financials.tax.au;

/**
 * 
 * @author Administrator
 * @version
 */
class TaxPayable extends TaxRule {

    /** Creates new TaxableIncome */
    TaxPayable() {
    }

    void apply() {

        /**
         * List of items to define taxable income. Some of them are already
         * taxed or not taxable. But we need them to work out the taxable
         * income.
         */

        String[] taxItems = { ITaxConstants.TAX_ON_TAXABLE_INCOME,
                ITaxConstants.ML, ITaxConstants.MLS };

        String[] offsetItems = { ITaxConstants.O_TAX_WITHHELD,
                ITaxConstants.O_IMPUTATION_CREDIT, ITaxConstants.O_LUMP_SUM_ETP,
                ITaxConstants.O_SUPER, ITaxConstants.O_OTHER,
                ITaxConstants.O_LOW_INCOME };

        String[] refundableOffsetItems = { ITaxConstants.O_TAX_WITHHELD,
                ITaxConstants.O_IMPUTATION_CREDIT };

        String[] taxItemsNonInvestment = {
                ITaxConstants.TAX_ON_NON_INVESTMENT_INCOME,
                ITaxConstants.ML_NON_INVESTMENT, ITaxConstants.MLS_NON_INVESTMENT };

        double refundableOffsets = tc.sumCollection(tc.TAX,
                refundableOffsetItems);
        double offsets = tc.sumCollection(tc.TAX, offsetItems);
        double nonRefundableOffsets = offsets - refundableOffsets;
        double items = tc.sumCollection(tc.TAX, taxItems);

        // Tax offestes cannot be used to generate a refund, with the exception
        // of the 30%
        // health Insurance and Baby bonus claim.
        double taxPayable = items;
        if (offsets != 0.0 && offsets < items) {
            taxPayable = items - offsets;
        } else if (offsets != 0.0 && offsets >= items) {

            taxPayable = items - nonRefundableOffsets;
            if (taxPayable < 0.0)
                taxPayable = 0.0;
            taxPayable = taxPayable - refundableOffsets;
        }

        // double taxPayable =tc.sumCollection(tc.TAX,taxItems ) -
        // tc.sumCollection(tc.TAX,offsetItems );

        double taxPayableNonInvestment = tc.sumCollection(tc.TAX,
                taxItemsNonInvestment);

        // Create taxable income entry in taxCollection
        tc.add(tc.TAX, ITaxConstants.TAX_PAYABLE, taxPayable);

        // Create taxable income entry in taxCollection
        tc.add(tc.TAX, ITaxConstants.TAX_PAYABLE_NON_INVESTMENT,
                taxPayableNonInvestment);

    }

}
