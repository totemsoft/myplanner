/*
 * Currency.java
 *
 * Created on 22 August 2001, 16:23
 */

package com.argus.swing;

/**
 * @author  Valeri Shibaev
 *
 */

public class NumberFormatter {
    
    public static final double HOLE = 0.;//Double.MIN_VALUE;
    
    public static final int FRACTION_DIGITS = 2;
    
    private static NumberFormatter numberFormatter;
    
    
    private java.text.NumberFormat numberFormat;

    
    protected NumberFormatter() {};
    
    
    public static NumberFormatter getNumberInstance() {
        if ( numberFormatter == null )
            numberFormatter = createInstance();
        return numberFormatter;
    }
    
    public static NumberFormatter createInstance() {
        NumberFormatter n = new NumberFormatter();
        ( (java.text.DecimalFormat) n.getNumberFormat() ).setMultiplier(1);
        ( (java.text.DecimalFormat) n.getNumberFormat() ).setMinimumFractionDigits( FRACTION_DIGITS );
        ( (java.text.DecimalFormat) n.getNumberFormat() ).setMaximumFractionDigits( FRACTION_DIGITS );
        return n;
    }
    
    
    public java.util.Locale getLocale() {
        return java.util.Locale.getDefault();
    }

    public java.text.NumberFormat getNumberFormat() {
        if ( numberFormat == null )
            numberFormat = java.text.NumberFormat.getInstance( getLocale() );
        return numberFormat;
    }


    public boolean isSingleton() {
        return this == numberFormatter;
    }
    
    public void setMinimumFractionDigits( int newValue ) {
        if ( newValue != FRACTION_DIGITS && isSingleton() ) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            throw new java.lang.IllegalArgumentException( 
                "Changing default MinimumFractionDigits on " + getClass().getName() + " Singleton is not allowed.\n" +
                "\tP.S. Create new instance and do whatever you like!" );
        }
        ( (java.text.DecimalFormat) getNumberFormat() ).setMinimumFractionDigits( newValue );
    }
    public void setMaximumFractionDigits( int newValue ) {
        if ( newValue != FRACTION_DIGITS && isSingleton() ) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            throw new java.lang.IllegalArgumentException( 
                "Changing default MaximumFractionDigits on Number2 Singleton is not allowed.\n" +
                "\tP.S. Create new instance and do whatever you like!" );
        }
        ( (java.text.DecimalFormat) getNumberFormat() ).setMaximumFractionDigits( newValue );
    }


    public boolean isNull( String value ) {
        return value == null || value.length() == 0;
    }
    public boolean isNumber( String value ) {
        if ( value == null )
            return false;
        
        for ( int i = 0; i < value.length(); i ++ )
            if ( !Character.isDigit( value.charAt(i) ) )
                return false;
        return true;
    }
    public boolean isFormatedNumber( String value ) {
        try {
            getNumberFormat().parse( value );
            return true; 
        } catch ( java.text.ParseException e ) {
            return false;
        }
    }

    public double doubleValue( String value ) {
        if ( isNull(value) ) return HOLE;
        
        if ( isNumber(value) )
            return new Double( value ).doubleValue();
        
        if ( !isFormatedNumber(value) ) return HOLE;
        
        try { return getNumberFormat().parse( value ).doubleValue(); }
        catch (java.text.ParseException e) { return HOLE; }
    }
    
    public Double getDoubleValue( String value ) {
        if ( isNull(value) ) return null;
        
        if ( isNumber(value) )
            return new Double( value );
        
        if ( !isFormatedNumber(value) ) return null;

        try { return new Double( doubleValue( value ) ); }
        catch ( Exception e ) { return null; }
    }
    
    public java.math.BigDecimal getBigDecimalValue( String value ) {
        if ( isNull(value) ) return null;
        
        if ( isNumber(value) )
            return new java.math.BigDecimal( value );
        
        if ( !isFormatedNumber(value) ) return null;
        
        try { return new java.math.BigDecimal( doubleValue( value ) ); }
        catch ( Exception e ) { return null; } 
    }

    
    public String toString( Object value ) {
        if ( value == null ) 
            return null;
        if ( value instanceof java.lang.Number )
            return toString( ( (java.lang.Number) value ).doubleValue() );
        return toString( doubleValue( value.toString() ) ); // ???
    }

    public String toString( double value ) {
        return getNumberFormat().format( value );
    }

}
