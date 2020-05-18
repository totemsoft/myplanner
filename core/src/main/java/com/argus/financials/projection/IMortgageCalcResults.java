/*
 * IMortgageCalcResults.java
 *
 * Created on May 22, 2002, 11:35 AM
 */

package com.argus.financials.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Vector;

public interface IMortgageCalcResults {

    // Inputs for Mortgage Calculations
    public void setPurchasePrice(double value);

    public void setDeposit(double value);

    public void setAmountBorrowed(double value);

    public void setLoanTerm(double value);

    public void setAnnualInterestRate(double value);

    public void setAddtnlPaymentOption(boolean value);

    public void setAddtnlMonthlyPayments(double value);

    // Results for standard repayments
    public void setStdMonthlyRepayment(double value);

    public void setSTdTotalInterestPaid(double value);

    public void setStdTotalCapitalPaid(double value);

    public void setStdTotalAmountRepaid(double value);

    public void setStdYearsToRepay(double value);

    public void setStdNumberOfPayments(double value);

    // Results with addtitional monthly repayments
    public void setAddtnlMonthlyRepayment(double value);

    public void setAddtnlTotalInterestPaid(double value);

    public void setAddtnlTotalCapitalPaid(double value);

    public void setAddtnlTotalAmountRepaid(double value);

    public void setAddtnlYearsToRepay(double value);

    public void setAddtnlNumberOfPayments(double value);

    // Comparison results
    public void setInterestSaved(double value);

    // public void setRevisedLoanTerm(double value);
    public void setYearsReduced(double value);

    // Mortgage schedules
    // public void setStdSchedule(
    // com.argus.projections.data.MortgageScheduleTableModel model );
    // public void setAddtnlSchedule(
    // com.argus.projections.data.MortgageScheduleTableModel model );
    public void setAnnualSchedules(Vector stdData, Vector addtnlData);

}
