/*
 * Money.java
 *
 * Created on 27 November 2002, 11:38
 */

package au.com.totemsoft.math;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public final class Percent extends Numeric {
    
    private au.com.totemsoft.format.Percent formatter;// = com.smoothlogic.format.Percent.createPercentInstance();

    public au.com.totemsoft.format.Number2 getNumberFormat() { 
        if ( formatter == null )
            formatter = au.com.totemsoft.format.Percent.createPercentInstance();
        return formatter; 
    }
    public void setNumberFormat( au.com.totemsoft.format.Number2 formatter ) { 
        this.formatter = (au.com.totemsoft.format.Percent) formatter; 
    }

    
    /** Creates a new instance of Percent */
    public Percent() {
        super();
    }
    public Percent(double value) {
        super(value);
    }
    public Percent(java.lang.Number value) {
        super(value);
    }
    public Percent( FormatedBigDecimal value ) {
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
    public String toString( au.com.totemsoft.format.Percent formatter ) {
        if ( formatter == null ) formatter = this.formatter;
        return getNumberFormat().toString( doubleValue() );
    }
    
    public String toString( double value ) {
        return getNumberFormat().toString( value );
    }
    public String toString( double value, au.com.totemsoft.format.Percent formatter ) {
        if ( formatter == null ) formatter = this.formatter;
        return getNumberFormat().toString( value );
    }
    
    public String toString( java.lang.Number value ) {
        if ( value == null ) return toString( 0. );
        return getNumberFormat().toString( value.doubleValue() );
    }
    public String toString( java.lang.Number value, au.com.totemsoft.format.Percent formatter ) {
        if ( formatter == null ) formatter = this.formatter;
        if ( value == null ) return toString( 0., formatter );
        return getNumberFormat().toString( value.doubleValue() );
    }

}
