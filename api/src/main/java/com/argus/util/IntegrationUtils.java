/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.util;

/*
 * Created on 9/04/2006
 * 
 */

public class IntegrationUtils {

    // integral 1/x
    public static double calc_1_div_x(double from, double to) {
        int n = 1000000;
        double dx = (to - from) / n;
        double result = 0.;
        for (int i = 0; i < n; i++) {
            result += dx * ( 1. / (from + i * dx + dx / 2) );
        }
        return result;
    }
    
    //ln(x) + const
    public static double calc2_1_div_x(double from, double to) {
        return Math.log(to) - Math.log(from);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Valera: " + calc_1_div_x(1., Math.E));
        System.out.println("Ilia: " + calc2_1_div_x(1., Math.E));
        System.out.println("Simpson: " + simpson_1_div_x(1., Math.E));
    }
    
    public static double simpson_1_div_x(double from, double to) {
        return
            ((to - from) / 6) * (1./from + 1./to + 4 * 1. / ((from + to) / 2));
    }
    
}

