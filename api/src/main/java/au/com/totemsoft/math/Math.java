/*
 * Math.java
 *
 * Created on July 24, 2002, 9:17 AM
 */

package au.com.totemsoft.math;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 
 */

public class Math {

    public static int mod( int num1, int num2 ) {
        return num1 - ( num1 / num2 ) * num2;
    }
    
    public static double mod( double num1, double num2 ) {
        return java.lang.Math.IEEEremainder( num1, num2 );
    }

    public static boolean even( int num ) {
        return java.lang.Math.round( (double) num / 2 ) - num / 2 == 0;
    }
    
    public static boolean odd( int num ) {
        return java.lang.Math.round( (double) num / 2 ) - num / 2 > 0;
    }
    
}
