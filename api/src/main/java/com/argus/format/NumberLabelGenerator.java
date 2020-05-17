/*
 * CurrencyLabelGenerator.java
 *
 * Created on 4 October 2001, 09:32
 */

package com.argus.format;

import com.klg.jclass.chart.JCLabelGenerator;

public class NumberLabelGenerator implements JCLabelGenerator {

    private static NumberLabelGenerator numberLabelGenerator;

    private Number2 number2;

    private NumberLabelGenerator() {
    }

    public static NumberLabelGenerator getInstance() {
        if (numberLabelGenerator == null)
            numberLabelGenerator = getNewInstance(null);
        return numberLabelGenerator;
    }

    public static NumberLabelGenerator getNewInstance(Number2 value) {
        NumberLabelGenerator lg = new NumberLabelGenerator();
        lg.number2 = value == null ? Number2.getNumberInstance() : value;
        return lg;
    }

    public java.lang.Object makeLabel(double value, int precision) {
        return number2.toString(value);
    }

}
