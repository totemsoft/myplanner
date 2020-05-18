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

package com.argus.util;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 */

public final class NumericUtils {

    private final static int FLOAT_SIZE  = 4;
    private final static int DOUBLE_SIZE = 8;

    // hide ctor
    private NumericUtils() {}

	
	/**
	 * 
	 */
    public static float [] floatArray( byte [] data ) {

    	if (data == null || data.length == 0) return null;
    	
    	float [] values = new float [data.length / FLOAT_SIZE];
    	
    	for (int i = 0; i < values.length; i++) {
    		int intValue = 0;
    		for (int j = i * FLOAT_SIZE; j < (i + 1) * FLOAT_SIZE; j++) {
    			intValue = (intValue << 8) + data[j]; // or other way ???
    		}
    		
    		float floatValue = Float.intBitsToFloat( intValue );
    		values[i] = floatValue;
    	}
    	
    	return values;

    }

    public static double [] doubleArray( byte [] data ) {

    	if (data == null || data.length == 0) return null;
    	
    	double [] values = new double [data.length / DOUBLE_SIZE];
    	
    	for (int i = 0; i < values.length; i++) {
    		int intValue = 0;
    		for (int j = i * DOUBLE_SIZE; j < (i + 1) * DOUBLE_SIZE; j++) {
    			intValue = (intValue << 8) + data[j]; // or other way ???
    		}
    		
    		float floatValue = Float.intBitsToFloat( intValue );
    		values[i] = floatValue;
    	}
    	
    	return values;

    }
	
    public static double [] doubleArray( float [] data ) {

    	if (data == null || data.length == 0) return null;
    	
    	double [] values = new double [data.length];
    	for ( int i = 0; i < data.length; i++ )
    		values[i] = data[i];
    	
    	return values;
    	
    }
    
}
