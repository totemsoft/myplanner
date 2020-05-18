/*
 * AssetInvestment.java
 *
 * Created on 8 October 2001, 10:54
 */

package com.argus.financials.bean;

import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.argus.util.DateTimeUtils;

public class AssetInvestment extends Asset {

    static final long serialVersionUID = 8463660977009474216L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.ASSET_INVESTMENT;

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new AssetInvestment */
    public AssetInvestment() {
        super();
    }

    public AssetInvestment(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Financial getNewFinancial() {
        return new AssetInvestment();
    }

    /**
     * override Object methodes
     */
    public String toString() {
        String s = getFinancialCodeDesc();

        if (empty(s))
            s = getFinancialDesc();
        if (empty(s))
            s = getFinancialTypeDesc();

        if (empty(s))
            s = NONE;

        if (DISPLAY_PKID)
            s += "(" + getId() + ")";

        return s;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Regular getRegular(int type) {

        if (regulars == null)
            regulars = new java.util.TreeMap();

        Regular r = null;
        double gross;
        double unfranked;
        double impCredit;
        double franked;
        double taxfree;

        double incomePercent;
        double frankedPercent;
        double taxfreePercent;
        double ratio;
        double pensionRebate;
        double upfrontFee;
        double ongoingFee;
        double otherDeductibleExpense;
        double regularDrawdowns;
        double regularContribution;

        int financialYearEnd = this.getGeneratedYear()
                + DateTimeUtils.getFinancialYearEnd(new java.util.Date());
        double fraction = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getStartDate(), getEndDate());
        double fractionCons = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getContributionStartDate(),
                getContributionEndDate());
        double fractionDrawDowns = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getDrawdownStartDate(), getDrawdownEndDate());

        ratio = RATIO_30_70.doubleValue();
        gross = getAmount() == null ? 0. : getAmount().doubleValue();

        gross = gross * fraction;

        frankedPercent = getFranked() == null ? 0. : getFranked().doubleValue();
        taxfreePercent = getTaxFreeDeferred() == null ? 0.
                : getTaxFreeDeferred().doubleValue();
        incomePercent = getIncome() == null ? 0. : getIncome().doubleValue();

        gross = gross * incomePercent / 100.;
        taxfree = gross * taxfreePercent / 100.;
        unfranked = (gross - taxfree);
        franked = unfranked * frankedPercent / 100.;
        unfranked = unfranked - franked;

        impCredit = franked * ratio;
        // franked = franked - impCredit ;
        pensionRebate = getRebateable() == null ? 0. : gross
                * getRebateable().doubleValue() / 100.;

        upfrontFee = getContributionAnnualAmount() == null
                || getUpfrontFee() == null ? 0. : fractionCons
                * getContributionAnnualAmount().doubleValue()
                * getUpfrontFee().doubleValue() / 100.;
        ongoingFee = getOngoingFee() == null || getAmount() == null ? 0.
                : fraction * getAmount().doubleValue()
                        * getOngoingFee().doubleValue() / 100.;

        otherDeductibleExpense = getOtherExpenses() == null ? 0. : fraction
                * getOtherExpenses().doubleValue();

        regularDrawdowns = getDrawdownAnnualAmount() == null ? 0.
                : fractionDrawDowns * getDrawdownAnnualAmount().doubleValue();

        regularContribution = getContributionAnnualAmount() == null ? 0.
                : fractionCons * getContributionAnnualAmount().doubleValue();

        switch (type) {
        case (iGROSS_INCOME):
            r = (RegularIncome) regulars.get(GROSS_INCOME);
            if (isReinvest()) {
                if (r != null)
                    r.setRegularAmount(ZERO);
            } else {
                if (r == null) {
                    r = generateIncome(type);
                    r.setFinancialTypeId(INCOME_INVESTMENT);
                    regulars.put(GROSS_INCOME, r);
                }
                r.setStartDate(getStartDate());
                r.setEndDate(getEndDate());

                r.setRegularAmount(new java.math.BigDecimal(gross));
                r.setFrequencyCodeID(getFrequencyCodeID());
            }

            break;

        case (iUNFRANKED_INCOME):
            r = (RegularIncome) regulars.get(UNFRANKED_INCOME);
            if (r == null) {
                r = generateIncome(type);
                r.setFinancialTypeId(INCOME_INVESTMENT);
                regulars.put(UNFRANKED_INCOME, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(unfranked));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iNON_TAXABLE_DRAWDOWN):
            r = (RegularIncome) regulars.get(NON_TAXABLE_DRAWDOWN);
            if (r == null) {
                r = generateIncome(type);
                r.setTaxable(false);
                r.setFinancialTypeId(INCOME_OTHER_TAX_FREE);
                regulars.put(NON_TAXABLE_DRAWDOWN, r);
            }
            r.setStartDate(getDrawdownStartDate());
            r.setEndDate(getDrawdownEndDate());

            java.math.BigDecimal amountC = new java.math.BigDecimal(
                    regularDrawdowns);
            // amountC = MoneyCalc.getIndexedValue( amountC,
            // getDrawdownIndexation(), getGeneratedYear() );
            r.setRegularAmount(amountC);

            r.setFrequencyCodeID(getFrequencyCodeID());

            break;

        case (iTAXFREE_INCOME):
            r = (RegularIncome) regulars.get(TAXFREE_INCOME);
            if (r == null) {
                r = generateIncome(type);
                r.setTaxable(false);
                r.setFinancialTypeId(INCOME_OTHER_TAX_FREE);
                regulars.put(TAXFREE_INCOME, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(taxfree));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iIMPUTATION_CREDIT):
            r = (RegularIncome) regulars.get(IMPUTATION_CREDIT);
            if (r == null) {
                r = generateIncome(type);
                r.setFinancialTypeId(INCOME_INVESTMENT);
                regulars.put(IMPUTATION_CREDIT, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(impCredit));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iIMPUTATION_CREDIT_REBATE):
            r = (TaxOffset) regulars.get(IMPUTATION_CREDIT_REBATE);
            if (r == null) {
                r = generateTaxOffset(type);
                r.setFinancialTypeId(TAXOFFSET_IMPUTATION_CREDIT);
                regulars.put(IMPUTATION_CREDIT_REBATE, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(impCredit));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;

        case (iFRANKED_INCOME):
            r = (RegularIncome) regulars.get(FRANKED_INCOME);
            if (r == null) {
                r = generateIncome(type);
                r.setFinancialTypeId(INCOME_INVESTMENT);
                regulars.put(FRANKED_INCOME, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(franked));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iPENSION_REBATE):
            r = (TaxOffset) regulars.get(PENSION_REBATE);
            if (r == null) {
                r = generateTaxOffset(type);
                r.setFinancialTypeId(TAXOFFSET_SUPER);
                regulars.put(PENSION_REBATE, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(pensionRebate));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iTOTAL_EXPENSE):
            r = (RegularExpense) regulars.get(TOTAL_EXPENSE);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeId(EXPENSE_GENERAL); // EXPENSE_OTHER
                regulars.put(TOTAL_EXPENSE, r);
            }
            r.setRegularAmount(new java.math.BigDecimal(upfrontFee + ongoingFee
                    + otherDeductibleExpense));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iNON_DEDUCTIBLE_EXPENSE):
            r = (RegularExpense) regulars.get(NON_DEDUCTIBLE_EXPENSE);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeId(EXPENSE_SAVING_INVESTMENT); // EXPENSE_SAVING_INVESTMENT
                regulars.put(NON_DEDUCTIBLE_EXPENSE, r);
            }
            r.setRegularAmount(new java.math.BigDecimal(upfrontFee + ongoingFee));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iTAX_DEDUCTIBLE_EXPENSE):
            r = (RegularExpense) regulars.get(TAX_DEDUCTIBLE_EXPENSE);
            if (r == null) {
                r = generateExpense(type);
                r.setTaxable(true);
                r.setFinancialTypeId(EXPENSE_SAVING_INVESTMENT); // EXPENSE_SAVING_INVESTMENT
                regulars.put(TAX_DEDUCTIBLE_EXPENSE, r);
            }
            r
                    .setRegularAmount(new java.math.BigDecimal(
                            otherDeductibleExpense));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;

        case (iNON_DEDUCTIBLE_DEPOSIT):
            r = (RegularExpense) regulars.get(NON_DEDUCTIBLE_DEPOSIT);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeId(EXPENSE_GENERAL); // EXPENSE_OTHER
                regulars.put(NON_DEDUCTIBLE_DEPOSIT, r);
            }
            r.setStartDate(getContributionStartDate());
            r.setEndDate(getContributionEndDate());

            r.setRegularAmount(new java.math.BigDecimal(regularContribution)); // TaxDeductible
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        default:
            ;
        }

        return r;

    }

    protected void projectAmount(int year, double inflation) {

        // if ( year <= 0 ) return; // break condition

        double growth = getCapitalGrowth() == null ? 0. : getCapitalGrowth()
                .doubleValue();
        if (isReinvest())
            growth += getIncome() == null ? 0. : getIncome().doubleValue();
        growth -= inflation;

        double ongoingFee = getOngoingFee() == null ? 0. : getOngoingFee()
                .doubleValue();
        double currentValue = getAmount() == null ? 0. : getAmount()
                .doubleValue();
        double contributionIndexation = getContributionIndexation() == null ? 0.
                : getContributionIndexation().doubleValue();
        double contributionAmount = getContributionAnnualAmount() == null ? 0.
                : getContributionAnnualAmount().doubleValue();
        double upfrontFee = getUpfrontFee() == null ? 0. : getUpfrontFee()
                .doubleValue();
        double drawdownAmount = getDrawdownAnnualAmount() == null ? 0.
                : getDrawdownAnnualAmount().doubleValue();
        double drawdownIndexation = getDrawdownIndexation() == null ? 0.
                : getDrawdownIndexation().doubleValue();

        double ongoingFeeAmount = 0.;
        double newCurrentValue = 0.;
        double newContributionAmount = 0.;
        double upfrontFeeAmount = 0.;
        double newDrawdownAmount = 0.;

        int financialYearEnd = DateTimeUtils.getFinancialYearEnd();

        // Loop for every year
        for (int i = 0; i <= year; i++) {
            double fraction = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getStartDate(), null);
            if (fraction == 0.)
                continue;

            fraction = DateTimeUtils.getFinancialYearFraction(financialYearEnd
                    + i, getStartDate(), getEndDate());
            double fractionCons = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getContributionStartDate(),
                    getContributionEndDate());
            double fractionDrawDowns = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getDrawdownStartDate(),
                    getDrawdownEndDate());

            newCurrentValue = getFinancialYearAmount(financialYearEnd + i,
                    currentValue, newCurrentValue, growth);

            // Increase regular contribution value by indexation
            newContributionAmount = newContributionAmount == 0. ? contributionAmount
                    : newContributionAmount + newContributionAmount
                            * (i == 0 ? 0. : contributionIndexation) / 100.;

            // Increase regular drawdown value by indexation
            newDrawdownAmount = newDrawdownAmount == 0. ? drawdownAmount
                    : newDrawdownAmount + newDrawdownAmount
                            * (i == 0 ? 0. : drawdownIndexation) / 100.;
            if (newCurrentValue < newDrawdownAmount)
                newDrawdownAmount = newCurrentValue;

            // Calculate upfront Fee Amount
            upfrontFeeAmount = fractionCons * newContributionAmount
                    * upfrontFee / 100.;

            // Calculate ongoing Fee Amount
            ongoingFeeAmount = newCurrentValue * ongoingFee / 100.;

            // Calculate new current value
            newCurrentValue += newContributionAmount * fractionCons
                    - upfrontFeeAmount - newDrawdownAmount * fractionDrawDowns
                    - ongoingFeeAmount;

            // Negative asset value doesn't make any sense
            if (newCurrentValue <= 0.) {
                newCurrentValue = 0.;
                currentValue = 0.;
            }

        }

        // newCurrentValue = (double) java.lang.Math.round(newCurrentValue) ;

        setAmount(new java.math.BigDecimal(newCurrentValue));
        setDrawdownAnnualAmount(new java.math.BigDecimal(newDrawdownAmount));
        setContributionAnnualAmount(new java.math.BigDecimal(
                newContributionAmount));

    }

    /***************************************************************************
     * get/set methods
     **************************************************************************/
    public void setAmount(java.math.BigDecimal value) {
        if (equals(getAmount(), value))
            return;

        super.setAmount(value);
    }

    public Integer getInvestmentTypeID() {
        return super.getInvestmentTypeID();
    }

    public void setInvestmentTypeID(Integer value) {
        super.setInvestmentTypeID(value);
    }

    public String getInvestmentTypeDesc() {
        return super.getInvestmentTypeDesc();
    }

    public java.math.BigDecimal getFranked() {
        return super.getFranked();
    }

    public void setFranked(java.math.BigDecimal value) {
        super.setFranked(value);
    }

    public java.math.BigDecimal getTaxFreeDeferred() {
        return super.getTaxFreeDeferred();
    }

    public void setTaxFreeDeferred(java.math.BigDecimal value) {
        super.setTaxFreeDeferred(value);
    }

    public java.math.BigDecimal getCapitalGrowth() {
        return super.getCapitalGrowth();
    }

    public void setCapitalGrowth(java.math.BigDecimal value) {
        super.setCapitalGrowth(value);
    }

    public java.math.BigDecimal getIncome() {
        return super.getIncome();
    }

    public void setIncome(java.math.BigDecimal value) {
        super.setIncome(value);
    }

    public java.math.BigDecimal getUpfrontFee() {
        return super.getUpfrontFee();
    }

    public void setUpfrontFee(java.math.BigDecimal value) {
        super.setUpfrontFee(value);
    }

    public java.math.BigDecimal getOngoingFee() {
        return super.getOngoingFee();
    }

    public void setOngoingFee(java.math.BigDecimal value) {
        super.setOngoingFee(value);
    }

    public java.math.BigDecimal getPurchaseCost() {
        return super.getPurchaseCost();
    }

    public void setPurchaseCost(java.math.BigDecimal value) {
        super.setPurchaseCost(value);
    }

    public java.math.BigDecimal getContributionRegularAmount() {
        return super.getRegularAmount();
    }

    // public void setContributionRegularAmount( java.math.BigDecimal value ) {
    // super.setRegularAmount( value );
    // }

    public java.math.BigDecimal getContributionAnnualAmount() {
        return super.getContributionAnnualAmount();
    }

    public void setContributionAnnualAmount(java.math.BigDecimal value) {
        super.setContributionAnnualAmount(value);
    }

    public java.math.BigDecimal getContributionIndexation() {
        return super.getContributionIndexation();
    }

    public void setContributionIndexation(java.math.BigDecimal value) {
        super.setContributionIndexation(value);
    }

    public java.util.Date getContributionStartDate() {
        return super.getContributionStartDate();
    }

    public void setContributionStartDate(java.util.Date value) {
        super.setContributionStartDate(value);
    }

    public java.util.Date getContributionEndDate() {
        return super.getContributionEndDate();
    }

    public void setContributionEndDate(java.util.Date value) {
        super.setContributionEndDate(value);
    }

    public java.math.BigDecimal getDrawdownAnnualAmount() {
        return super.getDrawdownAnnualAmount();
    }

    public void setDrawdownAnnualAmount(java.math.BigDecimal value) {
        super.setDrawdownAnnualAmount(value);
    }

    public java.math.BigDecimal getDrawdownIndexation() {
        return super.getDrawdownIndexation();
    }

    public void setDrawdownIndexation(java.math.BigDecimal value) {
        super.setDrawdownIndexation(value);
    }

    public java.util.Date getDrawdownStartDate() {
        return super.getDrawdownStartDate();
    }

    public void setDrawdownStartDate(java.util.Date value) {
        super.setDrawdownStartDate(value);
    }

    public java.util.Date getDrawdownEndDate() {
        return super.getDrawdownEndDate();
    }

    public void setDrawdownEndDate(java.util.Date value) {
        super.setDrawdownEndDate(value);
    }

    public java.math.BigDecimal getOtherExpenses() {
        return super.getExpense();
    }

    public void setOtherExpenses(java.math.BigDecimal value) {
        super.setExpense(value);
    }

    public boolean isReinvest() {
        return super.isReinvest();
    }

    public void setReinvest(boolean value) {
        super.setReinvest(value);
    }

}
