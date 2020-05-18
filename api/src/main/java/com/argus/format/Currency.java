/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.format;

/**
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1 $
 */

public final class Currency extends Number2 {

    public static String DEFAULT_PREFIX;
    public static String DEFAULT_PREFIX_NEG;
    public static String DEFAULT_SUFFIX;
    public static String DEFAULT_SUFFIX_NEG;
    
    private static Currency currency;
    private java.text.NumberFormat currencyNumberFormatter;

    protected Currency() {};
    
    public static Currency getCurrencyInstance() {
        if ( currency == null )
            currency = createCurrencyInstance();
        return currency;
    }

    public static Currency createCurrencyInstance() {
        Currency c = new Currency();
        ( (java.text.DecimalFormat) c.getNumberFormatter() ).setMultiplier(1);
        ( (java.text.DecimalFormat) c.getNumberFormatter() ).setMinimumFractionDigits(2);
        ( (java.text.DecimalFormat) c.getNumberFormatter() ).setMaximumFractionDigits(2);
        return c;
    }
    
    
    public boolean isSingleton() {
        return this == currency;
    }
    
    
    // NaN is formatted as a single character, typically \uFFFD
    public java.text.NumberFormat getNumberFormatter() {
        if ( currencyNumberFormatter == null ) {
            currencyNumberFormatter = java.text.NumberFormat.getCurrencyInstance( getLocale() );
            
            java.text.DecimalFormat df = (java.text.DecimalFormat) currencyNumberFormatter;
            
            if ( DEFAULT_PREFIX == null )
                DEFAULT_PREFIX = df.getPositivePrefix();
            if ( DEFAULT_PREFIX_NEG == null )
                DEFAULT_PREFIX_NEG = df.getNegativePrefix();
            if ( DEFAULT_SUFFIX == null )
                DEFAULT_SUFFIX = df.getPositiveSuffix();
            if ( DEFAULT_SUFFIX_NEG == null )
                DEFAULT_SUFFIX_NEG = df.getNegativeSuffix();
            
            String prefix = 
                ""; // S.P. request to remove $ sign from data
                //DEFAULT_PREFIX;
            df.applyPattern( prefix + "#,##0.00;(" + prefix + "#,##0.00)" );
        }
        
        return currencyNumberFormatter;
    }
       
}
