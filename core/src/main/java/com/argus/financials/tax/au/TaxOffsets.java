/*
 * TaxableIncome.java
 *
 * Created on 5 February 2002, 00:00
 */

package com.argus.financials.tax.au;

/**
 * 
 * @author Serguei Piltiaev
 * @version
 */
class TaxOffsets extends TaxRule {

    /** Creates new TaxableIncome */
    TaxOffsets() {
    }

    void apply() {

        // Get precalculated list of taxable items

        java.util.ArrayList incomeItems = tc.getIncomeCollection();

        // Initialize target variables
        double taxWithHeld = 0;
        double imputationCredit = 0;
        double otherOffset = 0;
        double superOffset = 0;

        for (int i = 0; i < incomeItems.size(); i++) {
            TaxElement te = (TaxElement) incomeItems.get(i);
            taxWithHeld = taxWithHeld + te.getTaxWithHeld();
        }

        imputationCredit = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT })
                + tc.sumCollection(tc.OFFSET,
                        new String[] { ITaxConstants.O_IMPUTATION_CREDIT });
        otherOffset = tc.sumCollection(tc.OFFSET,
                new String[] { ITaxConstants.O_OTHER });
        superOffset = tc.sumCollection(tc.OFFSET,
                new String[] { ITaxConstants.O_SUPER });

        // Create taxable income entry in taxCollection

        tc.add(tc.TAX, ITaxConstants.O_TAX_WITHHELD, taxWithHeld);
        tc.add(tc.TAX, ITaxConstants.O_IMPUTATION_CREDIT, imputationCredit);
        tc.add(tc.TAX, ITaxConstants.O_OTHER, otherOffset);
        tc.add(tc.TAX, ITaxConstants.O_SUPER, superOffset);

        // Calculate low income rebate
        double taxableIncome = tc.sumCollection(tc.TAX,
                new String[] { ITaxConstants.TAXABLE_INCOME });

        if (taxableIncome > 0.00
                && taxableIncome < ITaxConstants.LOW_INCOME_REBATE_MAX) {
            double aboveMin = taxableIncome
                    - ITaxConstants.LOW_INCOME_REBATE_MIN > 0.0 ? taxableIncome
                    - ITaxConstants.LOW_INCOME_REBATE_MIN : 0.0;
            double lowIncomeOffset = ITaxConstants.LOW_INCOME_REBATE
                    - ITaxConstants.LOW_INCOME_REBATE_REDUCTION * aboveMin;
            tc.add(tc.TAX, ITaxConstants.O_LOW_INCOME, Math
                    .round(lowIncomeOffset * 10) / 10.);
        }

        // Implement layering. First - calculate tax on the most inefficent
        // income. Than calculate tax on
        // other income.

        String[] inefficientIncome = { ITaxConstants.I_OTHER,
                ITaxConstants.I_SALARY,
                /* TaxConstants.I_ETP_NON_EXCESSIVE, */
                ITaxConstants.I_PENSIONS_ALLOWANCE,
                ITaxConstants.I_OTHER_PENSIONS, ITaxConstants.I_RFB,
                ITaxConstants.I_GROSS_INTEREST,
                ITaxConstants.I_DIVIDENDS_UNFRANKED,
                ITaxConstants.I_DIVIDENDS_FRANKED,
                ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT,
                ITaxConstants.I_LUMP_SUM_PART_B, ITaxConstants.I_LUMP_SUM_PART_C };

        String[] expenseItems = { ITaxConstants.D_INTEREST_DIVIDEND,
                ITaxConstants.D_GENERAL,
                ITaxConstants.D_UNDEDUCTED_PIRCHASE_PRICE };

        double expense = tc.sumCollection(tc.EXPENSE, expenseItems);

        // Only 5 % of this amount is taxable at MTR
        double partBofLSL = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_LUMP_SUM_PART_B });
        partBofLSL = partBofLSL * 0.95;

        double inefficent = tc.sumCollection(tc.INCOME, inefficientIncome)
                - partBofLSL;

        double imputaionCredit = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT });

        double concessional = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_CONCESSINAL });
        double preJuly1983 = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_PRE_JULY_1983 });
        double postJune1983Taxed = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_POST_JUNE_1983_TAXED });
        double postJune1983UnTaxed = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_POST_JUNE_1983_UNTAXED });
        double excessive = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_ETP_EXCESSIVE });
        double lumpSumPartA = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_LUMP_SUM_PART_A });

        double age = tc.sumCollection(tc.PERSONAL,
                new String[] { ITaxConstants.P_AGE });

        double taxed = 0.00;

        double postJune1983TaxedUnderThreshold = 0.00;
        double postJune1983TaxedOverThreshold = 0.00;
        double TaxRateTaxedUnderThreshold = 0.00;
        double TaxRateTaxedOverThreshold = 0.00;

        double postJune1983UnTaxedUnderThreshold = 0.00;
        double postJune1983UnTaxedOverThreshold = 0.00;
        double TaxRateUnTaxedUnderThreshold = 0.00;
        double TaxRateUnTaxedOverThreshold = 0.00;

        double outstandingThreshold = ITaxConstants.POST_JUNE_83_THRESHOLD;

        inefficent = inefficent + concessional
                * ITaxConstants.R_CONCESSIONAL_TAXABLE / 100.00 + preJuly1983
                * ITaxConstants.R_PRE_JULY_1983_TAXABLE / 100.00;
        /**
         * There is a difference in income tax calculation for residents and
         * non-residents. The following code needs to be adjusted to pick up
         * specific income range for non-residents.
         */

        double taxCharged = 0.00;
        boolean ETPrebate = false;
        if ((postJune1983Taxed + postJune1983UnTaxed + lumpSumPartA) != 0.00)
            ETPrebate = true;

        if (inefficent >= expense) {
            inefficent = inefficent - expense;
            expense = 0.00;
        } else if (inefficent < expense) {
            inefficent = 0.00;
            expense = expense - inefficent;
        }

        if (lumpSumPartA >= expense) {
            lumpSumPartA = lumpSumPartA - expense;
            expense = 0.00;
        } else if (lumpSumPartA < expense) {
            lumpSumPartA = 0.00;
            lumpSumPartA = lumpSumPartA - inefficent;
        }

        if (postJune1983UnTaxed >= expense) {
            postJune1983UnTaxed = postJune1983UnTaxed - expense;
            expense = 0.00;
        } else if (postJune1983UnTaxed < expense) {
            postJune1983UnTaxed = 0.00;
            expense = expense - postJune1983UnTaxed;
        }

        if (postJune1983Taxed >= expense) {
            postJune1983Taxed = postJune1983Taxed - expense;
            expense = 0.00;
        } else if (postJune1983Taxed < expense) {
            postJune1983Taxed = 0.00;
            expense = expense - postJune1983Taxed;
        }

        if (excessive >= expense) {
            excessive = excessive - expense;
            expense = 0.00;
        } else if (excessive < expense) {
            excessive = 0.00;
            expense = expense - excessive;
        }

        TaxCalculation taxCalc = TaxCalculation.getInstance();
        IncomeRanges incomeRanges = taxCalc.getIncomeRanges();
        // Work out the tax on the most inefficient income
        while (taxed < inefficent) {
            TaxElement te = taxCalc.between(taxed, inefficent, incomeRanges);
            taxCharged = taxCharged + te.getAmount() * te.getTaxRate() / 100.00;
            taxed = taxed + te.getAmount();
        }
        // Apply tax offset for inefficient income
        // Get Medicare Levy & Medicare Levy Surcharge
        double medicareLevy = tc.sumCollection(tc.TAX,
                new String[] { ITaxConstants.ML });
        double medicareLevySurchagre = tc.sumCollection(tc.TAX,
                new String[] { ITaxConstants.MLS });

        if (medicareLevy != 0.00)
            taxCharged = taxCharged + taxed * ITaxConstants.MEDICARE_LEVY
                    / 100.00;
        if (medicareLevySurchagre != 0.00)
            taxCharged = taxCharged + taxed
                    * ITaxConstants.MEDICARE_LEVY_SURCHARGE / 100.00;

        // Implemntation of income layring

        // taxCharged = taxCharged +
        // lumpSumPartA*TaxConstants.R_LUMP_SUM_PART_A/100.0 ;
        // taxed = taxed + lumpSumPartA ;

        TaxElement[] lumpSum = taxCalc.between(taxed, lumpSumPartA,
                ITaxConstants.R_LUMP_SUM_PART_A, incomeRanges);
        taxCharged = taxCharged + taxCalc.getTotalTax(lumpSum);
        taxed = taxed + lumpSumPartA;

        // Get gross tax charged
        if (medicareLevy != 0.00)
            taxCharged = taxCharged + lumpSumPartA * ITaxConstants.MEDICARE_LEVY
                    / 100.00;
        if (medicareLevySurchagre != 0.00)
            taxCharged = taxCharged + lumpSumPartA
                    * ITaxConstants.MEDICARE_LEVY_SURCHARGE / 100.00;

        /**
         * Tax on post June 83 taxed components Under age 55 - all at 20 % Over
         * age 55 - First 112405 = 0% Remainder - 15 %
         */
        double mlOnComponents = 0.00;

        if (age >= 55 && postJune1983Taxed <= outstandingThreshold) {
            postJune1983TaxedUnderThreshold = 0.00;
            postJune1983TaxedOverThreshold = 0.00;
            TaxRateTaxedUnderThreshold = 0.00;
            TaxRateTaxedOverThreshold = 0.00;

            outstandingThreshold = outstandingThreshold - postJune1983Taxed;
            taxCharged = taxCharged + 0;
        } else if (age >= 55 && postJune1983Taxed > outstandingThreshold) {
            postJune1983TaxedUnderThreshold = 0.00;
            postJune1983TaxedOverThreshold = postJune1983Taxed
                    - outstandingThreshold;
            TaxRateTaxedUnderThreshold = 0.00;
            TaxRateTaxedOverThreshold = ITaxConstants.R_POST_JUNE_1983_TAXED_OVER55;

            mlOnComponents = (postJune1983Taxed - outstandingThreshold);

            outstandingThreshold = 0.00;
        } else if (age < 55) {
            postJune1983TaxedUnderThreshold = postJune1983Taxed;
            postJune1983TaxedOverThreshold = 0.00;
            TaxRateTaxedUnderThreshold = ITaxConstants.R_POST_JUNE_1983_TAXED_UNDER55;
            TaxRateTaxedOverThreshold = 0.00;

            mlOnComponents = postJune1983Taxed;
        }

        if (medicareLevy != 0.00)
            taxCharged = taxCharged + mlOnComponents
                    * ITaxConstants.MEDICARE_LEVY / 100.00;
        if (medicareLevySurchagre != 0.00)
            taxCharged = taxCharged + mlOnComponents
                    * ITaxConstants.MEDICARE_LEVY_SURCHARGE / 100.00;

        // Implemntation of income layring

        taxCharged = taxCharged + excessive * ITaxConstants.R_ETP_EXCESSIVE
                / 100.0;
        taxed = taxed + excessive;
        // Get gross tax charged
        if (medicareLevy != 0.00)
            taxCharged = taxCharged + excessive * ITaxConstants.MEDICARE_LEVY
                    / 100.00;
        if (medicareLevySurchagre != 0.00)
            taxCharged = taxCharged + excessive
                    * ITaxConstants.MEDICARE_LEVY_SURCHARGE / 100.00;

        /**
         * Tax on post June 83 untaxed components Under age 55 - all at 30 %
         * Over age 55 - First 112405 = 15% Remainder - 30 %
         */
        mlOnComponents = postJune1983UnTaxed;
        // Decrease second level component if no other income available

        if (age >= 55 && postJune1983UnTaxed <= outstandingThreshold) {
            postJune1983UnTaxedUnderThreshold = postJune1983UnTaxed;
            postJune1983UnTaxedOverThreshold = 0.00;
            TaxRateUnTaxedUnderThreshold = ITaxConstants.R_POST_JUNE_1983_UNTAXED_OVER55;
            TaxRateUnTaxedOverThreshold = 0.00;
            outstandingThreshold = outstandingThreshold - postJune1983UnTaxed;

        } else if (age >= 55 && postJune1983UnTaxed > outstandingThreshold) {

            postJune1983UnTaxedUnderThreshold = outstandingThreshold;
            postJune1983UnTaxedOverThreshold = postJune1983UnTaxed
                    - outstandingThreshold;
            TaxRateUnTaxedUnderThreshold = ITaxConstants.R_POST_JUNE_1983_UNTAXED_OVER55;
            TaxRateUnTaxedOverThreshold = ITaxConstants.R_POST_JUNE_1983_UNTAXED_UNDER55;

            outstandingThreshold = 0.00;

        } else if (age < 55) {
            postJune1983UnTaxedUnderThreshold = postJune1983UnTaxed;
            postJune1983UnTaxedOverThreshold = 0.00;
            TaxRateUnTaxedUnderThreshold = ITaxConstants.R_POST_JUNE_1983_UNTAXED_UNDER55;
            TaxRateUnTaxedOverThreshold = 0.00;
        }

        if (medicareLevy != 0.00)
            taxCharged = taxCharged + mlOnComponents
                    * ITaxConstants.MEDICARE_LEVY / 100.00;
        if (medicareLevySurchagre != 0.00)
            taxCharged = taxCharged + mlOnComponents
                    * ITaxConstants.MEDICARE_LEVY_SURCHARGE / 100.00;

        // Get tax on untaxed component over threshold

        TaxElement[] te1 = taxCalc.between(taxed,
                postJune1983UnTaxedOverThreshold, TaxRateUnTaxedOverThreshold,
                incomeRanges);
        taxCharged = taxCharged + taxCalc.getTotalTax(te1);
        taxed = taxed + postJune1983UnTaxedOverThreshold;

        // Get tax on untaxed component under threshold

        TaxElement[] te11 = taxCalc.between(taxed,
                postJune1983UnTaxedUnderThreshold,
                TaxRateUnTaxedUnderThreshold, incomeRanges);
        taxCharged = taxCharged + taxCalc.getTotalTax(te11);
        taxed = taxed + postJune1983UnTaxedUnderThreshold;

        // Get tax on taxed component over threshold

        TaxElement[] te12 = taxCalc.between(taxed,
                postJune1983TaxedOverThreshold, TaxRateTaxedOverThreshold,
                incomeRanges);
        taxCharged = taxCharged + taxCalc.getTotalTax(te12);
        taxed = taxed + postJune1983TaxedOverThreshold;

        // Get tax on taxed component over threshold

        TaxElement[] te13 = taxCalc.between(taxed,
                postJune1983TaxedUnderThreshold, TaxRateTaxedUnderThreshold,
                incomeRanges);
        taxCharged = taxCharged + taxCalc.getTotalTax(te13);
        taxed = taxed + postJune1983TaxedUnderThreshold;

        double taxOnTaxableIncome = tc.sumCollection(tc.TAX, new String[] {
                ITaxConstants.TAX_ON_TAXABLE_INCOME, ITaxConstants.ML,
                ITaxConstants.MLS });

        taxCharged = Math.round(taxCharged);

        // Add Lump sum and ETP rebate
        if ((taxOnTaxableIncome - taxCharged) > 0.00 && ETPrebate == true)
            tc.add(tc.TAX, ITaxConstants.O_LUMP_SUM_ETP, taxOnTaxableIncome
                    - taxCharged);

    }

}
