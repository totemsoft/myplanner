/*
 * CurrencyLabelGenerator.java
 *
 * Created on 4 October 2001, 09:32
 */

package au.com.totemsoft.format;

import com.klg.jclass.chart.JCLabelGenerator;

public class PercentLabelGenerator implements JCLabelGenerator {

    private static PercentLabelGenerator percentLabelGenerator;

    private Percent percent;

    private PercentLabelGenerator() {
    }

    public static PercentLabelGenerator getInstance() {
        if (percentLabelGenerator == null)
            percentLabelGenerator = getNewInstance(null);
        return percentLabelGenerator;
    }

    public static PercentLabelGenerator getNewInstance(Percent value) {
        PercentLabelGenerator lg = new PercentLabelGenerator();
        lg.percent = value == null ? Percent.getPercentInstance() : value;
        return lg;
    }

    public java.lang.Object makeLabel(double value, int precision) {
        return percent.toString(value);
    }

}
