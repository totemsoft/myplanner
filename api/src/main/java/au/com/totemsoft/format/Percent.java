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

package au.com.totemsoft.format;

/**
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1 $
 */

public class Percent extends Number2 {
                    
    public static String DEFAULT_PREFIX;
    public static String DEFAULT_PREFIX_NEG;
    public static String DEFAULT_SUFFIX;
    public static String DEFAULT_SUFFIX_NEG;

    private static Percent percent;
    
    private java.text.NumberFormat percentNumberFormatter;
    
    protected Percent() {};
    
    public static Percent getPercentInstance() {
        if ( percent == null )
            percent = createPercentInstance();
        return percent;
    }

    public static Percent createPercentInstance() {
        Percent p = new Percent();
        ( (java.text.DecimalFormat) p.getNumberFormatter() ).setMultiplier(1);
        ( (java.text.DecimalFormat) p.getNumberFormatter() ).setMinimumFractionDigits(2);
        ( (java.text.DecimalFormat) p.getNumberFormatter() ).setMaximumFractionDigits(2);
        return p;
    }

    
    public boolean isSingleton() {
        return this == percent;
    }

    
    public java.text.NumberFormat getNumberFormatter() {
        if ( percentNumberFormatter == null ) {
            percentNumberFormatter = java.text.NumberFormat.getPercentInstance( getLocale() );
            
            java.text.DecimalFormat df = (java.text.DecimalFormat) percentNumberFormatter;
            
            if ( DEFAULT_PREFIX == null )
                DEFAULT_PREFIX = df.getPositivePrefix();
            if ( DEFAULT_PREFIX_NEG == null )
                DEFAULT_PREFIX_NEG = df.getNegativePrefix();
            if ( DEFAULT_SUFFIX == null )
                DEFAULT_SUFFIX = df.getPositiveSuffix();
            if ( DEFAULT_SUFFIX_NEG == null )
                DEFAULT_SUFFIX_NEG = df.getNegativeSuffix();
            
            String prefix = 
                ""; // S.P. request to remove % sign from data
                //DEFAULT_PREFIX;
            df.applyPattern( prefix + "#,##0.00;(" + prefix + "#,##0.00)" );
        }
        
        return percentNumberFormatter;
    }
       
}
