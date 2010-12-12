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

import java.util.Locale;

/**
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1 $
 */

public class Number2 
    extends java.lang.Number 
    implements Formatable
{

    public static final String NUMBER_CHARACTERS = "0123456789.";

    protected static double HOLE = Double.MAX_VALUE;
    
    // 0 MoneyCalc.UNKNOWN_VALUE Double.NaN BadArgumentException.BAD_DOUBLE_VALUE
    protected double bad_value = 0.00;//MoneyCalc.UNKNOWN_VALUE; 
    
    public static final int FRACTION_DIGITS = 2;
    
    private static Number2 number;
    private java.text.NumberFormat numberFormatter;

    protected Number2() {
        super();
    };
    
    public static Number2 getNumberInstance() {
        if ( number == null )
            number = createInstance();
        return number;
    }
    
    public static Number2 createInstance() {
        Number2 n = new Number2();
        ( (java.text.DecimalFormat) n.getNumberFormatter() ).setMultiplier(1);
        ( (java.text.DecimalFormat) n.getNumberFormatter() ).setMinimumFractionDigits( FRACTION_DIGITS );
        ( (java.text.DecimalFormat) n.getNumberFormatter() ).setMaximumFractionDigits( FRACTION_DIGITS );
        return n;
    }
    
    public boolean isSingleton() {
        return this == number;
    }
    
    public void setMinimumFractionDigits( int newValue ) {
        if ( newValue != FRACTION_DIGITS && isSingleton() ) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            throw new java.lang.IllegalArgumentException( 
                "Changing default MinimumFractionDigits on " + getClass().getName() + " Singleton is not allowed.\n" +
                "\tP.S. Create new instance and do whatever you like!" );
        }
        ( (java.text.DecimalFormat) getNumberFormatter() ).setMinimumFractionDigits( newValue );
    }
    public void setMaximumFractionDigits( int newValue ) {
        if ( newValue != FRACTION_DIGITS && isSingleton() ) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            throw new java.lang.IllegalArgumentException( 
                "Changing default MaximumFractionDigits on Number2 Singleton is not allowed.\n" +
                "\tP.S. Create new instance and do whatever you like!" );
        }
        ( (java.text.DecimalFormat) getNumberFormatter() ).setMaximumFractionDigits( newValue );
    }


    protected Locale getLocale() {
        return Locale.getDefault();
    }

    public java.text.NumberFormat getNumberFormatter() {
        if ( numberFormatter == null )
            numberFormatter = java.text.NumberFormat.getInstance( getLocale() );
        return numberFormatter;
    }

    public boolean isNull( String value ) {
        return value == null || value.length() == 0;
    }
    // Unformated number, ONLY contains 0..9, or . (what about , )
    public boolean isNumber( String value ) {
        if ( value == null )
            return false;
        
        for ( int i = 0; i < value.length(); i ++ )
            if ( NUMBER_CHARACTERS.indexOf( value.charAt(i) ) < 0 )
                return false;
        return true;
    }
    public boolean isFormatedNumber( String value ) {
        try {
            getNumberFormatter().parse( value );
            return true; 
        } catch ( java.text.ParseException e ) {
            return false;
        }
    }
    /**
     *  depreciated
     */
    public boolean isValid( String value ) {
        return isFormatedNumber( value );
    }

    public double doubleValue( String value ) {
        if ( isNull(value) ) return bad_value;
        
        if ( isNumber(value) )
            return new Double( value ).doubleValue();
        
        if ( !isFormatedNumber(value) ) return bad_value;
        
        try { return getNumberFormatter().parse( value ).doubleValue(); }
        catch (java.text.ParseException e) { return bad_value; }
    }
    
    public double doubleValue(javax.swing.text.JTextComponent tc) {
        return doubleValue( tc.getText() ); 
    }
    
    public Double getDoubleValue( String value ) {
        if ( isNull(value) ) return null;
        
        if ( isNumber(value) )
            return new Double( value );
        
        if ( !isFormatedNumber(value) ) return null;

        try { return new Double( doubleValue( value ) ); }
        catch ( RuntimeException e ) { return null; }
    }
    
    public Double getDoubleValue(javax.swing.text.JTextComponent tc) {
        if ( tc == null ) return null;
        return getDoubleValue( tc.getText() );
    }
   
    
    public java.math.BigDecimal getBigDecimalValue( String value ) {
        if ( isNull(value) ) return null;
        
        if ( isNumber(value) )
            return new java.math.BigDecimal( value );
        
        if ( !isFormatedNumber(value) ) return null;
        
        try { return new java.math.BigDecimal( doubleValue( value ) ); }
        catch ( RuntimeException e ) { return null; } 
    }

    
    public String toString( Object value ) {
        if ( value == null ) 
            return null;
        if ( value instanceof java.lang.Number )
            return toString( (java.lang.Number) value );
        return toString( doubleValue( value.toString() ) ); // ???
    }

    public String toString( double value ) {
        try { 
            return value == HOLE ? null : getNumberFormatter().format( value );
        } catch ( RuntimeException e ) { 
            System.out.println( e.getMessage() );
            return null; 
        } 
    }
    /**
     *  Returns empty string if value is 0.0
     *
     */
    public String toString( double value, boolean nonZeroOnly ) {
        try { 
            if (nonZeroOnly && value == 0.0) return "" ;
            return value == HOLE ? "" : getNumberFormatter().format( value );
        } catch ( RuntimeException e ) { 
            System.out.println( e.getMessage() );
            return null; 
        } 
    }
    
    public String toString( java.lang.Number value ) {
        if ( value == null ) return null;
        return toString( value.doubleValue() );
    }
    
    /**
     *  these methods need another ctor or setValue methods
     *  Not implemented !!!
     */
    public double doubleValue() {
        return Double.NaN;
    }
    
    public int intValue() {
        return Integer.MIN_VALUE;
    }
    
    public float floatValue() {
        return java.lang.Float.NaN;
    }
    
    public long longValue() {
        return java.lang.Long.MIN_VALUE;
    }
    
}
