/*
 * BaseData.java
 *
 * Created on 16 October 2002, 14:50
 */

package au.com.totemsoft.myplanner.report.data;

import java.math.BigDecimal;

import au.com.totemsoft.io.ImageUtils;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.service.ServiceAware;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class BaseData
    extends ServiceAware
    implements java.io.Serializable {

    static final long serialVersionUID = -8045055121179967923L;

    public static final String STRING_EMPTY = "";

    public static final String STRING_ZERO = "0";

    static final String STRING_ZERO_PERCENT = "0.00%";

    static final String STRING_ZERO_DOLLAR = "$0.00";

    static final BigDecimal ZERO = BigDecimal.ZERO;

    protected static au.com.totemsoft.format.Currency currency;

    protected static au.com.totemsoft.format.Percent percent;

    protected static au.com.totemsoft.format.Number2 number;

    protected static au.com.totemsoft.math.Money money;

    static {
        // require special formatting
        currency = au.com.totemsoft.format.Currency.createCurrencyInstance();
        java.text.DecimalFormat df = (java.text.DecimalFormat) currency
                .getNumberFormatter();

        df.setPositivePrefix("");
        // df.setPositivePrefix( au.com.totemsoft.format.Percent.DEFAULT_PREFIX );
        // df.setNegativePrefix( "" );
        df.setNegativePrefix(au.com.totemsoft.format.Percent.DEFAULT_PREFIX_NEG);
        df.setPositiveSuffix("");
        // df.setPositiveSuffix( au.com.totemsoft.format.Percent.DEFAULT_SUFFIX );
        df.setNegativeSuffix(""); // 
        // df.setNegativeSuffix( au.com.totemsoft.format.Percent.DEFAULT_SUFFIX_NEG );

        percent = au.com.totemsoft.format.Percent.createPercentInstance();
        df = (java.text.DecimalFormat) percent.getNumberFormatter();

        // restore defaults
        df.setPositivePrefix(au.com.totemsoft.format.Percent.DEFAULT_PREFIX);
        df.setNegativePrefix(au.com.totemsoft.format.Percent.DEFAULT_PREFIX_NEG);
        df.setPositiveSuffix(au.com.totemsoft.format.Percent.DEFAULT_SUFFIX);
        df.setNegativeSuffix(au.com.totemsoft.format.Percent.DEFAULT_SUFFIX_NEG);

        number = au.com.totemsoft.format.Number2.createInstance();
        df = (java.text.DecimalFormat) number.getNumberFormatter();

        money = new au.com.totemsoft.math.Money();
        // money.setMaximumFractionDigits(2);
        // money.setMinimumFractionDigits(2);

    }

    private boolean modified = false;

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
        // do notification if necessary

    }

    protected void clear() {
        modified = false;
    }

    public void init(PersonService person) throws Exception {
        if (person == null)
            clear();
    }

    public static String encodeAsJPEG(au.com.totemsoft.io.ImageEncoder encoder)
            throws Exception {
        return ImageUtils.encodeAsJPEG(encoder);
    }

}
