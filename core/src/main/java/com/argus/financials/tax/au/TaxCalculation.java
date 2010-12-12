/*
 * TaxCalculation.java
 *
 * Created on 26 August 2002, 22:55
 */

package com.argus.financials.tax.au;

/**
 * 
 * @author Administrator
 * @version
 */
class TaxCalculation {

    private IncomeRanges incomeRanges = new IncomeRanges();

    /** Creates new TaxCalculation */
    private static TaxCalculation instance = null;

    TaxCalculation() {}

    /**
     * returns taxable portion of value split into different tax levels
     * 
     */
    TaxElement between(double value, double totalTaxable,
            IncomeRanges incomeRanges) {
        java.util.ArrayList ranges = incomeRanges.getIncomeRanges();

        TaxElement te = new TaxElement();

        if (ranges.isEmpty())
            return null;

        for (java.util.Iterator i = ranges.iterator(); i.hasNext();) {
            IncomeRange range = (IncomeRange) i.next();

            double rangeStart = range.getRangeStart();
            double rangeEnd = range.getRangeEnd();

            if (value + 1 >= rangeStart && value + 1 <= rangeEnd) {

                double taxableAmount = 0;
                // if that is the last iteration make rangeEnd as taxable amount
                if (totalTaxable < rangeEnd)
                    rangeEnd = totalTaxable;

                taxableAmount = rangeEnd - value;

                te.setAmount(taxableAmount);
                te.setTaxRate(range.getTaxRate());

                te.setMedicareLevyRate(range.getMedicareLevyRate());
                te.setMedicareLevySurchargeRate(range
                        .getMedicareLevySurchargeRate());
                return te;
            }
        }

        return te;
    }

    /**
     * returns the best tax rate split into different tax levels for given
     * 'value' compare to marginal rate
     */
    TaxElement[] between(double base, double value, double marginalRate,
            IncomeRanges incomeRanges) {

        // temp storage for taxable elements
        java.util.ArrayList taxElements = new java.util.ArrayList();
        // get income range
        java.util.ArrayList ranges = incomeRanges.getIncomeRanges();

        double outstanding = value;

        if (ranges.isEmpty())
            return null;

        for (java.util.Iterator i = ranges.iterator(); i.hasNext();) {

            TaxElement te = new TaxElement();

            IncomeRange range = (IncomeRange) i.next();

            double rangeStart = range.getRangeStart();
            double rangeEnd = range.getRangeEnd();

            if (base + 1 >= rangeStart && base + 1 <= rangeEnd) {

                double taxRate = range.getTaxRate();
                // if there is no tax benefit use marginal tax rate
                if (taxRate >= marginalRate) {
                    te.setAmount(outstanding);
                    te.setTaxRate(marginalRate);
                    te.setMedicareLevyRate(range.getMedicareLevyRate());
                    te.setMedicareLevySurchargeRate(range
                            .getMedicareLevySurchargeRate());
                    taxElements.add(te);
                    outstanding = 0.00;
                } else {
                    double taxableAmount = 0;
                    // if that is the last iteration make rangeEnd as taxable
                    // amount
                    if (base + outstanding < rangeEnd)
                        rangeEnd = base + outstanding;

                    taxableAmount = rangeEnd - base;

                    outstanding = outstanding - taxableAmount;

                    base = base + taxableAmount;

                    te.setAmount(taxableAmount);
                    te.setTaxRate(range.getTaxRate());
                    te.setMedicareLevyRate(range.getMedicareLevyRate());
                    te.setMedicareLevySurchargeRate(range
                            .getMedicareLevySurchargeRate());
                    taxElements.add(te);

                }
                if (outstanding <= 0.00) {
                    break;
                }

            }
        }

        TaxElement[] tes = new TaxElement[taxElements.size()];

        int j = 0;
        for (java.util.Iterator i = taxElements.iterator(); i.hasNext();) {
            tes[j] = (TaxElement) i.next();
            j++;
        }

        return tes;
    }

    double getTotalTax(TaxElement[] te) {
        double totalTax = 0.00;
        if (te == null)
            return totalTax;
        for (int i = 0; i < te.length; i++) {
            totalTax = totalTax + te[i].getAmount() * te[i].getTaxRate()
                    / 100.00;
        }
        return totalTax;
    }

    IncomeRanges getIncomeRanges() {
        return this.incomeRanges;
    }

    static TaxCalculation getInstance() {
        if (instance == null) {
            instance = new TaxCalculation();
        }
        return instance;
    }

}
