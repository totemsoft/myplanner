/*
 * TaxableElement.java
 *
 * Created on 4 September 2002, 16:49
 */

package com.argus.financials.tax.au;

import java.util.ArrayList;

/**
 * 
 * @version
 */
class IncomeRanges {

    private java.util.ArrayList incomeRanges = new ArrayList();

    /** Creates new TaxableElement */
    IncomeRanges() {

        // this part will be populated from external source e.g. DB table or
        // property file
        // New tax rates since 01/01/2003
        incomeRanges.add(new IncomeRange(0.00, 6000.00, 0.0, 1.5, 1.0));
        incomeRanges.add(new IncomeRange(6001.00, 21600.00, 17.0, 1.5, 1.0));
        incomeRanges.add(new IncomeRange(21601.00, 52000.00, 30.0, 1.5, 1.0));
        incomeRanges.add(new IncomeRange(52001.00, 62500.00, 42.0, 1.5, 1.0));
        incomeRanges.add(new IncomeRange(62501.00, 1000000000000000000000.00,
                47.0, 1.5, 1.0));

    }

    java.util.ArrayList getIncomeRanges() {
        return this.incomeRanges;
    }
}
