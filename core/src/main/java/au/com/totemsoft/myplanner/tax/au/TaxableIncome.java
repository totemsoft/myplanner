/*
 * TaxableIncome.java
 *
 * Created on 5 February 2002, 00:00
 */

package au.com.totemsoft.myplanner.tax.au;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
class TaxableIncome extends TaxRule {

    /** Creates new TaxableIncome */
    TaxableIncome() {
    }

    void apply() {

        /**
         * List of items to define taxable income. Some of them are already
         * taxed or not taxable. But we need them to work out the taxable
         * income.
         */

        String[] incomeTaxedPartially = { ITaxConstants.I_LUMP_SUM_PART_B };

        String[] incomeItems = { ITaxConstants.I_OTHER, ITaxConstants.I_SALARY,
                ITaxConstants.I_LUMP_SUM_PART_A, ITaxConstants.I_LUMP_SUM_PART_B,
                ITaxConstants.I_LUMP_SUM_PART_C,
                ITaxConstants.I_ETP_NON_EXCESSIVE, ITaxConstants.I_ETP_EXCESSIVE,
                ITaxConstants.I_PENSIONS_ALLOWANCE,
                ITaxConstants.I_OTHER_PENSIONS, ITaxConstants.I_RFB,
                ITaxConstants.I_GROSS_INTEREST,
                ITaxConstants.I_DIVIDENDS_UNFRANKED,
                ITaxConstants.I_DIVIDENDS_FRANKED,
                ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT };

        String[] incomeNonTaxable = { ITaxConstants.I_TAX_DEFERRED,
                ITaxConstants.I_RFB };

        String[] expenseItems = { ITaxConstants.D_INTEREST_DIVIDEND,
                ITaxConstants.D_GENERAL,
                ITaxConstants.D_UNDEDUCTED_PIRCHASE_PRICE };

        // There is a special rule to tax the components other then Excessive
        // component.
        double concessional = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_CONCESSINAL });
        double preJuly1983 = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_PRE_JULY_1983 });

        double postJune1983Taxed = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_POST_JUNE_1983_TAXED });

        double postJune1983UnTaxed = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_POST_JUNE_1983_UNTAXED });

        double nonExcessiveComponent = concessional
                * ITaxConstants.R_CONCESSIONAL_TAXABLE / 100.00 + preJuly1983
                * ITaxConstants.R_PRE_JULY_1983_TAXABLE / 100.00
                + postJune1983Taxed + postJune1983UnTaxed;
        // Only 5 % of this amount is taxeable at MTR
        double partBofLSL = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_LUMP_SUM_PART_B });
        partBofLSL = partBofLSL * 0.95;

        // remove old value
        tc.remove(tc.INCOME, ITaxConstants.I_ETP_NON_EXCESSIVE);
        // and add new one
        tc.add(tc.INCOME, ITaxConstants.I_ETP_NON_EXCESSIVE,
                nonExcessiveComponent);

        double taxableIncome = tc.sumCollection(tc.INCOME, incomeItems)
                - tc.sumCollection(tc.EXPENSE, expenseItems)
                - tc.sumCollection(tc.INCOME, incomeNonTaxable) - partBofLSL;

        double totalIncome = tc.sumCollection(tc.INCOME, incomeItems);
        // Create taxable income entry in taxCollection
        tc.add(tc.TAX, ITaxConstants.TAXABLE_INCOME, taxableIncome);
        tc.add(tc.TAX, ITaxConstants.I_TOTAL_INCOME, totalIncome);

        /**
         * Find out the taxable items for assesable income. Generally all income
         * types will be included into consideration. Only otherIncome and any
         * special types of income are excluded from calculation.
         */
        double otherIncome = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_OTHER });

        double assessable = taxableIncome - otherIncome;

        double taxed = 0.00;

        /**
         * There is a difference in income tax calculation for residents and
         * non-residents. The following code needs to be adjusted to pick up
         * specific income range for non-residents.
         */

        TaxCalculation taxCalc = TaxCalculation.getInstance();
        IncomeRanges incomeRanges = taxCalc.getIncomeRanges();
        // Work out taxable components of non-investment income
        while (taxed < otherIncome) {

            TaxElement te = taxCalc.between(taxed, otherIncome, incomeRanges);

            te.setType(ITaxConstants.NON_INVESTMENT_INCOME);
            tc.addTaxable(te);
            taxed = taxed + te.getAmount();

        }

        // There is a special rule to tax the Excessive component. It gets taxed
        // at highest rate
        double excessiveComponent = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_ETP_EXCESSIVE });

        taxableIncome = taxableIncome - excessiveComponent;

        while (taxed < taxableIncome) {

            TaxElement te = taxCalc.between(taxed, taxableIncome, incomeRanges);
            te.setType(ITaxConstants.ASSESSABLE_INCOME);
            tc.addTaxable(te);
            taxed = taxed + te.getAmount();
        }

        TaxElement te = new TaxElement();
        te.setType(ITaxConstants.ASSESSABLE_INCOME);
        te.setAmount(excessiveComponent);
        te.setTaxRate(ITaxConstants.R_ETP_EXCESSIVE);
        tc.addTaxable(te);

    }

}
