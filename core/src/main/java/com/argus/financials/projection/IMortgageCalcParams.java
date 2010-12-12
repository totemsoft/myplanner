/*
 * IMortgageParams.java
 *
 * Created on May 22, 2002, 11:30 AM
 */

package com.argus.financials.projection;

/**
 * 
 * @author thomass
 * @version
 */
public interface IMortgageCalcParams {

    public double getPurchasePrice();

    public double getDeposit();

    public double getAmountBorrowed();

    public double getLoanTerm();

    public double getAnnualInterestRate();

    public boolean hasAdditionalPayment();

    public double getAdditonalMonthlyRepayment();
}
