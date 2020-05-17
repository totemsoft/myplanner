/*
 * Money.java
 *
 * Created on 27 November 2002, 11:38
 */

package com.argus.math;

/**
 *
 * @author  valeri chibaev
 */

public final class Decimal extends Numeric {
    
    private com.argus.format.Number2 formatter;// = com.smoothlogic.format.Number2.createInstance();
     
    public com.argus.format.Number2 getNumberFormat() { 
        if ( formatter == null )
            formatter = com.argus.format.Number2.createInstance();
        return formatter; 
    }
    public void setNumberFormat( com.argus.format.Number2 formatter ) { 
        this.formatter = formatter; 
    }

    
    /** Creates a new instance of Decimal */
    public Decimal() {
        super();
    }
    public Decimal(double value) {
        super(value);
    }
    public Decimal(java.lang.Number value) {
        super(value);
    }
    public Decimal( FormatedBigDecimal value ) {
        super(value);
    }

    
    public double doubleValue( String value ) {
        return getNumberFormat().doubleValue( value );
    }
    public Double asDoubleValue( String value ) {
        return getNumberFormat().getDoubleValue( value );
    }
    public java.math.BigDecimal asBigDecimal( String value ) {
        return getNumberFormat().getBigDecimalValue( value );
    }

    
    public String toString() {
        return getNumberFormat().toString( doubleValue() );
    }
    public String toString( com.argus.format.Number2 formatter ) {
        if ( formatter == null ) formatter = this.formatter;
        return getNumberFormat().toString( doubleValue() );
    }

    public String toString( double value ) {
        return getNumberFormat().toString( value );
    }
    public String toString( double value, com.argus.format.Number2 formatter ) {
        if ( formatter == null ) formatter = this.formatter;
        return getNumberFormat().toString( value );
    }
    
    public String toString( java.lang.Number value ) {
        if ( value == null ) return toString( 0. );
        return getNumberFormat().toString( value.doubleValue() );
    }
    public String toString( java.lang.Number value, com.argus.format.Number2 formatter ) {
        if ( formatter == null ) formatter = this.formatter;
        if ( value == null ) return toString( 0., formatter );
        return getNumberFormat().toString( value.doubleValue() );
    }

}
