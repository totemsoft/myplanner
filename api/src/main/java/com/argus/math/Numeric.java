/*
 * Numeric.java
 *
 * Created on 27 November 2002, 11:38
 */

package com.argus.math;

/**
 *
 * @author  valeri chibaev
 */

public abstract class Numeric implements java.io.Serializable, java.lang.Cloneable {
    
    private double value;
    
    
    /** Creates a new instance of Numeric */
    protected Numeric() {
        this(0.);
    }
    protected Numeric(double value) {
        this.value = value;
    }
    protected Numeric(java.lang.Number value) {
        this( value == null ? 0. : value.doubleValue() );
    }
    protected Numeric( FormatedBigDecimal value ) {
        this( value == null ? 0. : value.doubleValue() );
    }
    
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }
    
    public abstract com.argus.format.Number2 getNumberFormat();
    public abstract void setNumberFormat( com.argus.format.Number2 formatter );
    
    public synchronized void setMaximumFractionDigits( int newValue ) {
        getNumberFormat().setMaximumFractionDigits( newValue );
    }
    public synchronized void setMinimumFractionDigits( int newValue ) {
        getNumberFormat().setMinimumFractionDigits( newValue );
    }

    
    public double doubleValue() {
        return this.value;
    }
    public Numeric setValue( double value ) {
        if ( this.value != value )
            this.value = value;
        return this;
    }
    public Numeric roundToTen( double value ) {
        long temp = java.lang.Math.round(value/10)*10;
        if ( this.value != temp )
            this.value = temp;
        return this;
    }
    public Numeric setValue( java.lang.Number value ) {
        if ( value == null )
            this.value = 0.;
        else if ( this.value != value.doubleValue() )
            this.value = value.doubleValue();
        return this;
    }
    public Numeric roundToTen( java.lang.Number value ) {
        if ( value == null )
            this.value = 0.;
        else if ( this.value != value.doubleValue() )
            this.value = java.lang.Math.round(value.doubleValue()/10)*10;
        return this;
    }
    public Numeric setValue( Numeric value ) {
        if ( value == null )
            this.value = 0.;
        else if ( this.value != value.doubleValue() )
            this.value = value.doubleValue();
        return this;
    }
    
    
    public java.math.BigDecimal asBigDecimal() {
        return new java.math.BigDecimal(this.value);
    }
    
    public abstract double doubleValue( String value );
    public abstract Double asDoubleValue( String value );
    public abstract java.math.BigDecimal asBigDecimal( String value );
    
    
    /***************************************************************************
     *  java.lang.Math functions (change object)
     **************************************************************************/
    public Numeric abs() {
        value = java.lang.Math.abs( value );
        return this;
    }
    public Numeric ceil() {
        value = java.lang.Math.ceil( value );
        return this;
    }
    public Numeric floor() {
        value = java.lang.Math.floor( value );
        return this;
    }
    public Numeric rint() {
        value = java.lang.Math.rint( value );
        return this;
    }
    public Numeric round() {
        value = java.lang.Math.round( value );
        return this;
    }


    /***************************************************************************
     *  other Math functions (change object)
     **************************************************************************/
    public Numeric add( double value ) {
        this.value += value;
        return this;
    }
    public Numeric add( java.lang.Number value ) {
        if ( value != null ) this.value += value.doubleValue();
        return this;
    }
    public Numeric add( Numeric value ) {
        if ( value != null ) this.value += value.doubleValue();
        return this;
    }
    
    public Numeric subtract( double value ) {
        this.value -= value;
        return this;
    }
    public Numeric subtract( java.lang.Number value ) {
        if ( value != null ) this.value -= value.doubleValue();
        return this;
    }
    public Numeric subtract( Numeric value ) {
        if ( value != null ) this.value -= value.doubleValue();
        return this;
    }
    
    public Numeric multiply( double value ) {
        this.value *= value;
        return this;
    }
    public Numeric multiply( java.lang.Number value ) {
        if ( value != null ) this.value *= value.doubleValue();
        return this;
    }
    public Numeric multiply( Numeric value ) {
        if ( value != null ) this.value *= value.doubleValue();
        return this;
    }

    public Numeric divide( double value ) {
        this.value /= value;
        return this;
    }
    public Numeric divide( java.lang.Number value ) {
        if ( value != null ) this.value /= value.doubleValue();
        return this;
    }
    public Numeric divide( Numeric value ) {
        if ( value != null ) this.value /= value.doubleValue();
        return this;
    }

}
