/*
 * RateUtils.java
 *
 * Created on 3 August 2003, 20:24
 */

/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.util;


/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 *
 */

public class RateUtils {
    
//    // for test only
//    public static void main( String [] args ) {
//        System.out.println( "getCompoundedAmount=" + getCompoundedAmount( 100, .1, 1, 365 ) );
//        System.out.println( "getCompoundedAmount=" + getCompoundedAmount( 100, .1, 1 ) );
//        System.out.println( "getCompoundedAmount=" + getCompoundedAmount( 100, .1, 1, 1 ) );
//        System.out.println( "getIndexedValue=" + getIndexedValue( 100, 10, 1 ) );
//        
//        System.out.println( "getContinuouslyCompoundedRate=" + getContinuouslyCompoundedRate( .1, 2 ) );
//        System.out.println( "getCompoundedRate=" + getCompoundedRate( .08, 4 ) );
//    }
    
    
    /** Creates a new instance of RateUtils */
    private RateUtils() {}
    
    
    /**
     *  Interest Rate with Compounding Frequency
     *
     *  Parameters
     *      amount          invested amount
     *      interestRate    interest rate
     *      years           number of years to invest
     *      m               compound rate per anum
     *  Return
     *      terminal value of investment
     */
    public static double getCompoundedAmount( 
        double amount, 
        double interestRate, 
        double years, 
        double m ) 
    {
        return m <= 0. ? 0. : amount * Math.pow( 1. + interestRate / m, m * years );
    }
    public static double getCompoundedAmount( 
        Number amount, 
        double interestRate, 
        double years, 
        double m ) 
    {
        double _amount = amount == null ? 0. : amount.doubleValue();
        return getCompoundedAmount( _amount, interestRate, years, m );
    }
    
    /**
     *  Rate of Interest with Continuous Compounding (frequency tends to infinity)
     *
     *  Parameters
     *      amount          invested amount
     *      interestRate    interest rate
     *      years           number of years
     *  Return
     *      terminal value of investment
     */
    public static double getCompoundedAmount( 
        double amount, 
        double interestRate, 
        double years ) 
    {
        if (interestRate >= 1.)
            throw new IllegalArgumentException("interestRate range [0. to 1.]");
        return amount * Math.pow( Math.E, interestRate * years );
    }

//    public static double getIndexedValue(
//        double valueNow,
//        double index,
//        double yearsValue )
//    { 
//        return valueNow * Math.pow( ( 100. + index ) / 100., yearsValue );
//    }   

    public static double getCompoundedAmount( 
        Number amount, 
        Number interestRate, 
        double years ) 
    {
        double _amount = amount == null ? 0. : amount.doubleValue();
        double _interestRate = interestRate == null ? 0. : interestRate.doubleValue();
        return getCompoundedAmount( _amount, _interestRate, years );
    }
        
    /**
     *
     *  Parameters
     *      Rm:  Interest Rate with Compounding Frequency or
     *      m:   compound rate per anum
     *  Return
     *      Rc:  Rate of Interest with Continuous Compounding or
     */
    public static double getContinuouslyCompoundedRate(
        double Rm,
        double m ) 
    {
        return m <= 0. ? 0. : m * Math.log( 1. + Rm / m );
    }
    
    /**
     *
     *  Parameters
     *      Rc:  Rate of Interest with Continuous Compounding
     *      m:   compound rate per anum
     *  Return
     *      Rm:  Interest Rate with Compounding Frequency
     */
    public static double getCompoundedRate(
        double Rc,
        double m ) 
    {
        return m <= 0. ? 0. : m * ( Math.pow( Math.E, Rc / m ) - 1. );
    }
    

    /**
     *  Parameters
     *      S0  Price of the asset underlying the forvard or futures contract today
     *      I   Present Value of Dividends
     *      Rc  Risk-free rate of interest per annum, expressed with continuous compounding
     *      T   Life of a forward or future contract
     *  Return
     *      Forward or future price
     */
     public static double getPrice(
        double S0, 
        double I, 
        double Rc, 
        double T ) 
    {
            return ( S0 - I ) * Math.pow( Math.E, Rc * T ); // 3.6
     }
     
     /***************************************************************************
     *
     **************************************************************************/
    public static double calculateCapitalPayment( double mi, double loanMonth, double amtBorrowed, double loanTerm ) {
        if ( loanTerm <= 0. || amtBorrowed <= 0. ) return 0.;
        
        double d = Math.pow( 1 + mi, loanTerm );
        
        // Fixes if interest rate = 0. d = 1
        double stdCapitalPayment = 0. ;
        if (d != 1.)
            stdCapitalPayment = ( mi * Math.pow( 1 + mi, loanMonth-1 ) ) * amtBorrowed / ( d - 1 );
        else
            stdCapitalPayment = amtBorrowed/loanTerm;
            
//        if ( stdCapitalPayment >= calculateClosingBalanceLoanAmount() ) return calculateClosingBalanceLoanAmount();
        return stdCapitalPayment;
    }
    
    public static double calculateClosingBalanceLoanAmount( double mi, double loanMonth, double amtBorrowed,  double loanTerm ) {
        if ( loanTerm <= 0. || amtBorrowed <= 0. ) return 0.;
        
        double d = Math.pow( 1 + mi, loanTerm );
        // Fixes if interest rate = 0. d = 1
        double closingBalance = 0. ;
        if (d != 1.)
            closingBalance = ( d - Math.pow( 1 + mi, loanMonth ) ) * amtBorrowed / ( d - 1 );
        else 
            closingBalance = amtBorrowed - amtBorrowed/loanTerm*loanMonth ;            
        
        if ( closingBalance <= 0. ) return 0.;
        return closingBalance;
    }
    
}
