/*
 * CurrencyInputVerifier.java
 *
 * Created on 04 August 2003, 23:05
 */

package com.argus.swing;

/**
 * @author  Valeri Shibaev
 */

import javax.swing.text.JTextComponent;

public class NumberInputVerifier extends javax.swing.InputVerifier {

    private static javax.swing.InputVerifier inputVerifier = new NumberInputVerifier();
    public static javax.swing.InputVerifier getInstance() { return inputVerifier; }

    
    protected int fractionDigits;
    protected NumberFormatter numberFormatter;
    
    
    public NumberInputVerifier() { 
        this( NumberFormatter.FRACTION_DIGITS ); 
    }
    public NumberInputVerifier( int fractionDigits ) { 
        this.fractionDigits = fractionDigits;
        
        if ( this.fractionDigits == NumberFormatter.FRACTION_DIGITS ) {
            numberFormatter = NumberFormatter.getNumberInstance();
        } else {
            numberFormatter = NumberFormatter.createInstance();
            numberFormatter.setMinimumFractionDigits( this.fractionDigits );
            numberFormatter.setMaximumFractionDigits( this.fractionDigits );
        }
        
    }
    
    
    public NumberFormatter getNumberFormatter() { return numberFormatter; }
    
    public double doubleValue( String value ) {
        return getNumberFormatter().doubleValue( value );
    }

    public boolean verify( javax.swing.JComponent input ) {
        
        if ( input instanceof JTextComponent ) {
            
            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            if ( s == null || s.length() == 0 ) return true;
            //if ( s == null || s.trim().equals("") ) return true;
            
            c.setText( getNumberFormatter().toString( doubleValue( s ) ) );
            return true;

        }

        return false;
        
    }

    private double min = NumberFormatter.HOLE;
    private double max = NumberFormatter.HOLE;
    
    public double getMinimumValue() { return min; }
    public void setMinimumValue( double min ) { this.min = min; }
    
    public double getMaximumValue() { return max; }
    public void setMaximumValue( double max ) { this.max = max; }
    
    protected boolean checkLimits( double value ) {
        return 
            getMinimumValue() != NumberFormatter.HOLE && value >= getMinimumValue() && 
            getMaximumValue() != NumberFormatter.HOLE && value <= getMaximumValue();
    }
    
}
