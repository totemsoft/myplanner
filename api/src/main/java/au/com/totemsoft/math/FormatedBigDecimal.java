/*
 * FormatedBigDecimal.java
 *
 * Created on 14 October 2002, 13:16
 */

package au.com.totemsoft.math;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
/*
 * FormatedBigDecimal.java
 *
 * Created on 14 October 2002, 10:53
 */

import java.math.BigDecimal;

/**
 * This class is used to display a BigDecimal number with a limited precision. 
 * The toString() methode returns a String with limited digits behind the decimal
 * dot. The implementation is based on the java.math.BigDecimal object.
 */
public class FormatedBigDecimal {
    public final static int DEFAULT_SCALE = 2;
    
    private BigDecimal  _bd;
    
    /** Creates a new instance of FormatedBigDecimal */
    public FormatedBigDecimal() {
        this( new BigDecimal( "0.00" ), DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP );             
    }

    public FormatedBigDecimal( String str ) {  
        this( new BigDecimal( str == null || str.length() <= 0 ? "0.00" : str ), DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP );             
    }

    /*
     * double constructors.
     */    
    public FormatedBigDecimal( double value ) {    
        this( new BigDecimal( value ), DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP );             
    }
    public FormatedBigDecimal(double value, int precision) {
        this( new BigDecimal( value ), precision, BigDecimal.ROUND_HALF_UP );             
    }
    public FormatedBigDecimal(double value, int precision, int round) {
        this( new BigDecimal( value ), precision, round );             
    }
    

    /*
     * BigDecimal constructors.
     */
    public FormatedBigDecimal( java.lang.Number value ) {    
        this( value, DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP );
    }
    public FormatedBigDecimal( java.lang.Number value, int precision ) {
        this( value, precision, BigDecimal.ROUND_HALF_UP );             
    }
    public FormatedBigDecimal( java.lang.Number value, int precision, int round) {
        if ( value instanceof BigDecimal )
            this._bd = ( (BigDecimal) value ).setScale( precision, round );
        else
            this._bd = new BigDecimal( value == null ? 0. : value.doubleValue() );
    }    
    
    
    
    /**
     * returns the value of the object as a String.
     *
     * @return a String.
     */
    public String toString() {       
        return this._bd.toString();
    }   
    
    
    /**
     * return the scale of the used BigDecimal.
     *
     * @return the scale (precision of the used BigDecimal).
     */
    public int scale() {
        return this._bd.scale();
    }
    

    /**
     * Returns a FormatedBigDecimal whose scale is the specified value.
     *
     * @param scale.
     * @return a new FormatedBigDecimal with the given scale.
     */
    public FormatedBigDecimal setScale(int scale) {
        return new FormatedBigDecimal( this._bd.doubleValue(), scale );
    }    
    
    
    /**
     * Returns a FormatedBigDecimal whose scale is the specified scale value and 
     * round mode.
     *
     * @param scale
     * @param round - roundingMode.
     * @return a new FormatedBigDecimal with the given scale.
     */    
    public FormatedBigDecimal setScale(int scale, int round) {
        return new FormatedBigDecimal( this._bd.doubleValue(), scale, round );
    }    
    
    
    /**
     * Converts this FormatedBigDecimal to a double. 
     *
     * @return this BigDecimal converted to a double.
     */
    public double doubleValue() {
        return this._bd.doubleValue();
    }
}

