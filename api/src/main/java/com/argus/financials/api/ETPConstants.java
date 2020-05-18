/*
 * ETPConstants.java
 *
 * Created on 20 August 2002, 11:54
 */

package com.argus.financials.api;

/**
 * 
 * @version
 */

import com.argus.util.DateTimeUtils;

public interface ETPConstants extends CommonConstants {

    public static final java.util.Date TAX_1983_COMMENCE_DATE = DateTimeUtils
            .getDate("1/7/1983");

    // public static final java.util.Date TAX_1983_END_DATE =
    // DateTime.getDate( "1/7/1983" );

    public static final java.util.Date TAX_1994_COMMENCE_DATE = DateTimeUtils
            .getDate("1/7/1994");

    public static final double SUPER_TAX_RATE = 15.; // 15%

    public static final double TAX_FREE_THRESHOLD = 112405.; // 02/03

    public static final double ASSESABLE_INCOME_RATE = 0.05; // Pre 1/7/1983

    public static final int TAX_EFFECTIVE_AGE = 55;

    public static final double EXCESSIVE_TAX_RATE = .485;

    public static final double THRESHOLD_TAX_RATE1 = .315;

    public static final double POST_TAXED_UNDER_55_TAX_RATE = .215;

    public static final double THRESHOLD_TAX_RATE2 = .165;

    // partial withdraw variables
    public static final int UNDEDUCTED_PARTIAL = 1;

    public static final int CGT_EXEMPT_PARTIAL = 2;

    public static final int EXCESS_PARTIAL = 3;

    public static final int CONCESSIONAL_PARTIAL = 4;

    public static final int INVALIDITY_PARTIAL = 5;

}
