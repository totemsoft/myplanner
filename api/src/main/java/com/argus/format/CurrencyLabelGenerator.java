/*
 * CurrencyLabelGenerator.java
 *
 * Created on 4 October 2001, 09:32
 */

package com.argus.format;

import com.klg.jclass.chart.JCLabelGenerator;

public class CurrencyLabelGenerator implements JCLabelGenerator {

    private static CurrencyLabelGenerator currencyLabelGenerator;

    private Currency curr;

    private CurrencyLabelGenerator() {
    }

    public static CurrencyLabelGenerator getInstance() {
        if (currencyLabelGenerator == null)
            currencyLabelGenerator = getNewInstance(null);
        return currencyLabelGenerator;
    }

    public static CurrencyLabelGenerator getNewInstance(Currency value) {
        CurrencyLabelGenerator lg = new CurrencyLabelGenerator();
        lg.curr = value == null ? Currency.getCurrencyInstance() : value;
        return lg;
    }

    public java.lang.Object makeLabel(double value, int precision) {
        return curr.toString(value);
    }

}
