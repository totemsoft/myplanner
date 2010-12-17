/*
 * BaseData.java
 *
 * Created on 16 October 2002, 14:50
 */

package com.argus.financials.report.data;

import com.argus.io.ImageUtils;

/**
 * 
 * @author valeri chibaev
 */

public class BaseData extends java.lang.Object implements java.io.Serializable {
    // serialver -classpath . com.argus.activex.data.BaseData

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    // static final long serialVersionUID = -8045055121179967923L;

    public static final String STRING_EMPTY = "";

    public static final String STRING_ZERO = "0";

    static final String STRING_ZERO_PERCENT = "0.00%";

    static final String STRING_ZERO_DOLLAR = "$0.00";

    static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);

    protected static com.argus.format.Currency currency;

    protected static com.argus.format.Percent percent;

    protected static com.argus.format.Number2 number;

    protected static com.argus.math.Money money;

    static {
        // require special formatting
        currency = com.argus.format.Currency.createCurrencyInstance();
        java.text.DecimalFormat df = (java.text.DecimalFormat) currency
                .getNumberFormatter();

        df.setPositivePrefix("");
        // df.setPositivePrefix( com.argus.format.Percent.DEFAULT_PREFIX );
        // df.setNegativePrefix( "" );
        df.setNegativePrefix(com.argus.format.Percent.DEFAULT_PREFIX_NEG);
        df.setPositiveSuffix("");
        // df.setPositiveSuffix( com.argus.format.Percent.DEFAULT_SUFFIX );
        df.setNegativeSuffix(""); // 
        // df.setNegativeSuffix( com.argus.format.Percent.DEFAULT_SUFFIX_NEG );

        percent = com.argus.format.Percent.createPercentInstance();
        df = (java.text.DecimalFormat) percent.getNumberFormatter();

        // restore defaults
        df.setPositivePrefix(com.argus.format.Percent.DEFAULT_PREFIX);
        df.setNegativePrefix(com.argus.format.Percent.DEFAULT_PREFIX_NEG);
        df.setPositiveSuffix(com.argus.format.Percent.DEFAULT_SUFFIX);
        df.setNegativeSuffix(com.argus.format.Percent.DEFAULT_SUFFIX_NEG);

        number = com.argus.format.Number2.createInstance();
        df = (java.text.DecimalFormat) number.getNumberFormatter();

        money = new com.argus.math.Money();
        // money.setMaximumFractionDigits(2);
        // money.setMinimumFractionDigits(2);

    }

    private boolean modified = false;

    /** Creates a new instance of BaseData */
    protected BaseData() {
    }

    // originally defined on AbstractBase
    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean value) {
        if (modified == value)
            return;

        modified = value;
        // do notification if nessesary

    }

    protected void clear() {
        modified = false;
    }

    public void init(com.argus.financials.service.PersonService person)
            throws Exception {
        if (person == null)
            clear();
    }

    public static String encodeAsJPEG(com.argus.io.ImageEncoder encoder)
            throws Exception {
        return ImageUtils.encodeAsJPEG(encoder);
    }

}
