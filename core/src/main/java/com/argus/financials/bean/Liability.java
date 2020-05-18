/*
 * Liability.java
 *
 * Created on 8 October 2001, 10:46
 */

package com.argus.financials.bean;

import com.argus.financials.api.bean.IFPSAssignableObject;
import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.argus.financials.code.FrequencyCode;
import com.argus.util.DateTimeUtils;
import com.argus.util.RateUtils;

public class Liability extends Regular {

    static final long serialVersionUID = 3623694497636426532L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.LIABILITY;

    private String accountNumber;

    private Double interestRate;

    /** Creates new Liability */
    public Liability() {
    }

    public Liability(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Financial getNewFinancial() {
        return new Liability();
    }

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /**
     * Assignable methods
     */
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Liability))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Liability l = (Liability) value;

        accountNumber = l.accountNumber;
        interestRate = l.interestRate;

    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        accountNumber = null;
        interestRate = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static double lastYearBalance = 0.;

    public Regular getRegular(int type) {

        if (regulars == null)
            regulars = new java.util.TreeMap();

        Regular r = null;
        switch (type) {
        case (iTOTAL_EXPENSE):
            r = (RegularExpense) regulars.get(TOTAL_EXPENSE);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeId(EXPENSE_GENERAL); // EXPENSE_OTHER
                r.setLiability(this);
                regulars.put(TOTAL_EXPENSE, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(getRegularAmount());
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;

        default:
            ;

        }

        return r;

    }

    protected void projectAmount(int year, double inflation) {

        // if ( year <= 0 ) return; // break condition

        // This is the Liability Value decreased by repayment amount

        double currentValue = getAmount().doubleValue();
        double annualRepayment = getAnnualRegularAmount() == null ? 0.
                : getAnnualRegularAmount().doubleValue();
        double interestRate = getInterestRate() == null ? 0.
                : getInterestRate().doubleValue();

        double montlyRepaymentAmount = annualRepayment / 12.;

        double newCurrentValue = 0.;

        double monthlyInterestRate = interestRate / 100. / 12.;

        double loanTermMonths = 0.;

        if (Math.log(1.0 + monthlyInterestRate) != 0.
                && (montlyRepaymentAmount > monthlyInterestRate * currentValue))
            loanTermMonths = (int) Math
                    .round(Math
                            .log((montlyRepaymentAmount / (montlyRepaymentAmount - monthlyInterestRate
                                    * currentValue)))
                            / Math.log(1.0 + monthlyInterestRate));

        int financialYearEnd = DateTimeUtils.getFinancialYearEnd();

        // Loop for every year
        double fractionAccumulated = 0.;
        double currentValueAccumulated = currentValue;
        double newAnnualRepayment = 0.;
        for (int i = 0; i <= year; i++) {
            double fraction = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getStartDate(), null);
            if (fraction == 0.)
                continue;

            fraction = DateTimeUtils.getFinancialYearFraction(financialYearEnd
                    + i, getStartDate(), getEndDate());
            fractionAccumulated += fraction;

            // Decrease current value by repayment amount
            newAnnualRepayment = annualRepayment * fraction;

            currentValueAccumulated += currentValueAccumulated * interestRate
                    / 100. - newAnnualRepayment;

        }

        // This code has been taken from mortgage calculator
        // Standard Repayment schedule
        if (fractionAccumulated == 0.) {
            newAnnualRepayment = 0.;
            newCurrentValue = 0.;
            // getAnnualAmounts().clear();

        } else {
            newCurrentValue = loanTermMonths <= 0. ? currentValueAccumulated
                    : RateUtils.calculateClosingBalanceLoanAmount(
                            monthlyInterestRate, fractionAccumulated * 12,
                            currentValue, loanTermMonths);

            if (newCurrentValue < newAnnualRepayment && newCurrentValue <= 0.)
                newAnnualRepayment = lastYearBalance;

        }

        setAmount(new java.math.BigDecimal(newCurrentValue));

        lastYearBalance = newCurrentValue;

        setFrequencyCodeID(FrequencyCode.YEARLY);
        setRegularAmount(new java.math.BigDecimal(newAnnualRepayment));

    }

    /**
     * get/set methods
     */
    public void setAmount(java.math.BigDecimal value) {
        if (equals(getAmount(), value))
            return;

        super.setAmount(value);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String value) {
        if (equals(accountNumber, value))
            return;

        accountNumber = value;
        setModified(true);
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double value) {
        if (equals(interestRate, value))
            return;

        interestRate = value;
        setModified(true);
    }

    public java.math.BigDecimal getAmount(boolean signed) {
        java.math.BigDecimal amount = getAmount();
        if (!signed)
            return amount == null ? ZERO : amount;
        return amount == null ? ZERO : amount.negate();
    }

    public java.math.BigDecimal getRegularAmount(boolean signed) {
        java.math.BigDecimal regularAmount = getRegularAmount();
        if (!signed)
            return regularAmount;
        return regularAmount == null ? null : regularAmount.negate();
    }

    public java.math.BigDecimal getUsedAmount(boolean signed) {
        java.math.BigDecimal amount = getUsedAmount();
        if (!signed)
            return amount;
        return amount == null ? null : amount.negate();
    }

    public java.math.BigDecimal getBalanceAmount(boolean signed) {
        java.math.BigDecimal amount = getBalanceAmount();
        if (!signed)
            return amount;
        return amount == null ? null : amount.negate();
    }

    protected java.math.BigDecimal getAnnualRegularAmount() {
        return FrequencyCode.getAnnualAmount(getFrequencyCodeID(),
                getRegularAmount());
    }

    public java.math.BigDecimal getFinancialYearAmount() {
        return getFinancialYearAmountFractional(false);
    }

    public java.math.BigDecimal getFinancialYearAmount(boolean sign) {
        return getFinancialYearAmountFractional(false, sign);
    }

}