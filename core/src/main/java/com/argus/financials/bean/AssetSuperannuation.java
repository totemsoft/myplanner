/*
 * AssetSuperannuation.java
 *
 * Created on 8 October 2001, 10:55
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.FundType;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public class AssetSuperannuation extends Asset {

    // serialver -classpath . com.argus.financial.AssetSuperannuation

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = -5812331543957627518L;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.ASSET_SUPERANNUATION);

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new AssetSuperannuation */
    public AssetSuperannuation() {
    }

    public AssetSuperannuation(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Financial getNewFinancial() {
        return new AssetSuperannuation();
    }

    /**
     * override Object methodes
     */
    public String toString() {
        String s = getFinancialCodeDesc();

        if (empty(s))
            s = getFinancialDesc();
        if (empty(s))
            s = getFundTypeDesc();

        if (empty(s))
            s = NONE;

        if (DISPLAY_PKID)
            s += "(" + getPrimaryKeyID() + ")";

        return s;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Regular getRegular(int type) {

        if (regulars == null)
            regulars = new java.util.TreeMap();

        double gross;
        double taxfree;
        double growthPercent;
        double pensionRebate;
        double upfrontFee;
        double ongoingFee;
        double otherDeductibleExpense;
        double regularDrawdowns;
        double regularContributions;
        double regularPersonalContributions;

        int financialYearEnd = this.getGeneratedYear()
                + DateTimeUtils.getFinancialYearEnd(new java.util.Date());
        double fraction = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getStartDate(), getEndDate());
        double fractionCons = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getContributionStartDate(),
                getContributionEndDate());
        double fractionDrawDowns = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getDrawdownStartDate(), getDrawdownEndDate());

        gross = getAmount() == null ? 0. : getAmount().doubleValue();

        gross = gross * fraction;

        regularPersonalContributions = getContributionAnnualAmount() == null ? 0.
                : getContributionAnnualAmount().doubleValue() * fractionCons;

        pensionRebate = getRebateable() == null ? 0. : gross
                * getRebateable().doubleValue() / 100.;

        upfrontFee = getContributionAnnualAmount() == null
                || getUpfrontFee() == null ? 0. : fractionCons
                * getContributionAnnualAmount().doubleValue()
                * getUpfrontFee().doubleValue() / 100.;
        ongoingFee = getOngoingFee() == null || getAmount() == null ? 0.
                : fraction * getAmount().doubleValue()
                        * getOngoingFee().doubleValue() / 100.;

        regularDrawdowns = getDrawdownAnnualAmount() == null ? 0.
                : getDrawdownAnnualAmount().doubleValue() * fractionDrawDowns;

        Regular r = null;
        switch (type) {
        case (iTOTAL_EXPENSE):
            r = (RegularExpense) regulars.get(TOTAL_EXPENSE);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeID(EXPENSE_GENERAL); // EXPENSE_OTHER
                // r.setTaxable(true);

                regulars.put(TOTAL_EXPENSE, r);

            }
            r
                    .setRegularAmount(new java.math.BigDecimal(ongoingFee
                            + upfrontFee));

            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iNON_TAXABLE_DRAWDOWN):
            r = (RegularIncome) regulars.get(NON_TAXABLE_DRAWDOWN);
            if (r == null) {
                r = generateIncome(type);
                r.setTaxable(false);
                r.setFinancialTypeID(INCOME_OTHER_TAX_FREE);
                // if (DEBUG) r.setFinancialDesc( r.getFinancialDesc() + " (" +
                // sNON_TAXABLE_DRAWDOWN + ")" );

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

        case (iNON_DEDUCTIBLE_DEPOSIT):
            r = (RegularExpense) regulars.get(NON_DEDUCTIBLE_DEPOSIT);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeID(EXPENSE_GENERAL); // EXPENSE_OTHER
                // 13.03.2003 Scot advised to get rid of tax deductible flag
                r.setTaxable(false);
                // if (DEBUG) r.setFinancialDesc( r.getFinancialDesc() + " (" +
                // sNON_DEDUCTIBLE_DEPOSIT + ")" );

                regulars.put(NON_DEDUCTIBLE_DEPOSIT, r);

            }
            r.setStartDate(getContributionStartDate());
            r.setEndDate(getContributionEndDate());

            java.math.BigDecimal amountD = new java.math.BigDecimal(
                    regularPersonalContributions);
            // amountD = MoneyCalc.getIndexedValue( amountD,
            // getContributionIndexation(), getGeneratedYear() );
            r.setRegularAmount(amountD);
            r.setFrequencyCodeID(getFrequencyCodeID());
            break;
        default:
            ;
        }

        return r;

    }

    protected void projectAmount(int year, double inflation) {

        // This is the Asset Value increased by Growth %, (less ongoing fees)
        // plus increased by indexed regular contributions (less entry fees),

        double growth = getCapitalGrowth() == null ? 0. : getCapitalGrowth()
                .doubleValue()
                - inflation;
        double ongoingFee = getOngoingFee() == null ? 0. : getOngoingFee()
                .doubleValue();
        double currentValue = getAmount() == null ? 0. : getAmount()
                .doubleValue();
        double contributionIndexation = getContributionIndexation() == null ? 0.
                : getContributionIndexation().doubleValue();
        double contributionAmount = getAnnualAmount() == null ? 0.
                : getAnnualAmount().doubleValue();
        double upfrontFee = getUpfrontFee() == null ? 0. : getUpfrontFee()
                .doubleValue();
        double drawdownAmount = getDrawdownAnnualAmount() == null ? 0.
                : getDrawdownAnnualAmount().doubleValue();
        double drawdownIndexation = getDrawdownIndexation() == null ? 0.
                : getDrawdownIndexation().doubleValue();
        double personalContributionAmount = getContributionAnnualAmount() == null ? 0.
                : getContributionAnnualAmount().doubleValue();

        double ongoingFeeAmount = 0.;
        double newCurrentValue = 0.;
        double newContributionAmount = 0;
        double newPersonalContributionAmount = 0;
        double upfrontFeeAmount = 0.;
        double newDrawdownAmount = 0.;

        int financialYearEnd = DateTimeUtils.getFinancialYearEnd();

        // Loop for every year
        double fractionCons = 1.;
        for (int i = 0; i <= year; i++) {
            double fraction = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getStartDate(), null);
            if (fraction == 0.)
                continue;

            fraction = DateTimeUtils.getFinancialYearFraction(financialYearEnd
                    + i, getStartDate(), getEndDate());
            fractionCons = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getContributionStartDate(),
                    getContributionEndDate());
            double fractionDrawDowns = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getDrawdownStartDate(),
                    getDrawdownEndDate());

            newCurrentValue = getFinancialYearAmount(financialYearEnd + i,
                    currentValue, newCurrentValue, growth);

            // Increase regular contribution value by indexation
            newContributionAmount = newContributionAmount == 0. ? contributionAmount
                    : newContributionAmount
                            * (1. + (i == 0 ? 0 : contributionIndexation / 100.));
            newPersonalContributionAmount = newPersonalContributionAmount == 0. ? personalContributionAmount
                    : newPersonalContributionAmount
                            * (1. + (i == 0 ? 0.
                                    : contributionIndexation / 100.));

            // Increase regular drawdown value by indexation
            newDrawdownAmount = newDrawdownAmount == 0. ? drawdownAmount
                    : newDrawdownAmount
                            * (1. + (i == 0 ? 0. : drawdownIndexation / 100.));
            if (newCurrentValue < newDrawdownAmount)
                newDrawdownAmount = newCurrentValue;

            // Calculate upfront Fee Amount
            upfrontFeeAmount = fractionCons * newContributionAmount
                    * upfrontFee / 100.;

            // Calculate ongoing Fee Amount
            ongoingFeeAmount = newCurrentValue * ongoingFee / 100.;

            // Calculate new current value
            newCurrentValue += fractionCons * newContributionAmount
                    - upfrontFeeAmount - newDrawdownAmount * fractionDrawDowns
                    - ongoingFeeAmount - fractionCons
                    * (newContributionAmount - newPersonalContributionAmount)
                    * 0.15;

            // Negative asset value doesn't make any sense
            if (newCurrentValue <= 0.) {
                newCurrentValue = 0.;
                currentValue = 0.;
            }

        }

        // newCurrentValue = (double) java.lang.Math.round(newCurrentValue) ;
        setAmount(new java.math.BigDecimal(newCurrentValue));
        setDrawdownAnnualAmount(new java.math.BigDecimal(newDrawdownAmount));
        setAnnualAmount(new java.math.BigDecimal(newContributionAmount
                * fractionCons));
        setContributionAnnualAmount(new java.math.BigDecimal(
                newPersonalContributionAmount));

    }

    /***************************************************************************
     * get/set methods
     **************************************************************************/
    public void setAmount(java.math.BigDecimal value) {
        if (equals(getAmount(), value))
            return;

        super.setAmount(value);
    }

    public Integer getFundTypeID() {
        return super.getFundTypeID();
    }

    public String getFundTypeDesc() {
        return super.getFundTypeDesc();
    }

    public void setFundTypeID(Integer value) {
        super.setFundTypeID(value);

        if (FundType.rcSUPERANNUATION.getCodeIDInteger()
                .equals(getFundTypeID()))
            setFinancialTypeID(SUPERANNUATION_ACCOUNT);
        else
            setFinancialTypeID(null);
    }

    public ReferenceCode getFundType() {
        if (getFundTypeID() != null)
            if (getFundTypeID().intValue() == FundType.rcSUPERANNUATION
                    .getCodeID())
                return FundType.rcSUPERANNUATION;
        return ReferenceCode.CODE_NONE;
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

    public java.math.BigDecimal getDeductible() {
        return super.getDeductible();
    }

    public void setDeductible(java.math.BigDecimal value) {
        super.setDeductible(value);
    }

    public java.math.BigDecimal getCapitalGrowth() {
        return super.getCapitalGrowth();
    }

    public void setCapitalGrowth(java.math.BigDecimal value) {
        super.setCapitalGrowth(value);
    }

    public java.math.BigDecimal getAnnualAmount() {
        return super.getAnnualAmount();
    }

    public void setAnnualAmount(java.math.BigDecimal value) {
        super.setAnnualAmount(value);
    }

    public java.math.BigDecimal getRegularAmount() {
        return super.getRegularAmount();
    }

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

}
