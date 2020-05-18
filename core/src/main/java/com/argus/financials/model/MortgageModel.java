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

package com.argus.financials.model;

/*
 * Created on 27/08/2004
 * 
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MortgageModel {

	/**
	 * 
	 */
	public MortgageModel() {
		super();
	}

	public static double calculateLoanTermMonths(
    		double monthlyRate,
			double montlyRepayment,
			double currentValue ) 
	{
		double d = Math.log(1.0 + monthlyRate);
		double delta = montlyRepayment - monthlyRate * currentValue;
		
        if (d == 0. || delta <= 0 )
        	return -1;
        
        double loanTermMonths = Math.log( montlyRepayment / delta ) / d;

        return loanTermMonths;
        
	}
	
    public static double calculateCapitalPayment( 
    		double monthlyRate, 
			double loanMonth, 
			double amtBorrowed, 
			double loanTerm ) 
    {
        if ( loanTerm <= 0. || amtBorrowed <= 0. ) return 0.;
        
        double d = Math.pow( 1 + monthlyRate, loanTerm );
        
        // Fixes if interest rate = 0. d = 1
        double stdCapitalPayment = 0. ;
        if (d != 1.)
            stdCapitalPayment = ( monthlyRate * Math.pow( 1 + monthlyRate, loanMonth-1 ) ) * amtBorrowed / ( d - 1 );
        else
            stdCapitalPayment = amtBorrowed/loanTerm;
            
//        if ( stdCapitalPayment >= calculateClosingBalanceLoanAmount() ) return calculateClosingBalanceLoanAmount();
        return stdCapitalPayment;
    }
    
    public static double calculateClosingBalanceLoanAmount( 
    		double monthlyRate, 
			double loanMonth, 
			double amtBorrowed,  
			double loanTerm ) 
    {
        if ( loanTerm <= 0. || amtBorrowed <= 0. ) return 0.;
        
        double d = Math.pow( 1 + monthlyRate, loanTerm );
        // Fixes if interest rate = 0. d = 1
        double closingBalance = 0. ;
        if (d != 1.)
            closingBalance = ( d - Math.pow( 1 + monthlyRate, loanMonth ) ) * amtBorrowed / ( d - 1 );
        else 
            closingBalance = amtBorrowed - amtBorrowed/loanTerm*loanMonth ;            
        
        if ( closingBalance <= 0. ) return 0.;
        return closingBalance;
    }
    
}
