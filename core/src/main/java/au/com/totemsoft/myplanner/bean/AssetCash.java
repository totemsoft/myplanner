/*
 * AssetCash.java
 *
 * Created on 8 October 2001, 10:54
 */

package au.com.totemsoft.myplanner.bean;

import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.util.DateTimeUtils;

public class AssetCash extends Asset {

    static final long serialVersionUID = 2553149001369638517L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.ASSET_CASH;

    // has to be non-static
    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new AssetCash */
    public AssetCash() {
    }

    public AssetCash(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Financial getNewFinancial() {
        return new AssetCash();
    }

    /**
     * override Object methodes
     */
    public String toString() {
        String s = getFinancialDesc();

        if (empty(s))
            s = getInstitution();

        if (empty(s)) {
            s = getFinancialTypeDesc();
            if (getAccountNumber() != null)
                s += " ( AccountNumber: " + getAccountNumber() + " )";
        }

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

        int year = getGeneratedYear();
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

        incomePercent = getCapitalGrowth() == null ? 0. : getCapitalGrowth()
                .doubleValue();

        // Get rid of projected income amount and calculate it again
        if (isReinvest())
            // gross = gross/ (100+incomePercent)*100 * (incomePercent/100.) ;
            gross = gross * incomePercent / 100.;
        else
            gross = gross * incomePercent / 100.;

        // if ( year == 0 ) gross = gross*incomePercent/100.;
        // else gross = gross/ (100+incomePercent)*100 * (incomePercent/100.);

        upfrontFee = getContributionAnnualAmount() == null
                || getUpfrontFee() == null ? 0. : fractionCons
                * getContributionAnnualAmount().doubleValue()
                * getUpfrontFee().doubleValue() / 100.;
        ongoingFee = getOngoingFee() == null || getAmount() == null ? 0.
                : fraction * getAmount().doubleValue()
                        * getOngoingFee().doubleValue() / 100.;

        regularDrawdowns = getDrawdownAnnualAmount() == null ? 0.
                : getDrawdownAnnualAmount().doubleValue() * fractionDrawDowns;
        regularContribution = getContributionAnnualAmount() == null ? 0.
                : getContributionAnnualAmount().doubleValue() * fractionCons;

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

            r.setRegularAmount(new java.math.BigDecimal(gross));
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

            r.setRegularAmount(new java.math.BigDecimal(regularDrawdowns));
            r.setFrequencyCodeID(getFrequencyCodeID());

            break;
        case (iNON_DEDUCTIBLE_EXPENSE):
            r = (RegularExpense) regulars.get(NON_DEDUCTIBLE_EXPENSE);
            if (r == null) {
                r = generateExpense(type);
                r.setFinancialTypeId(EXPENSE_SAVING_INVESTMENT); // EXPENSE_SAVING_INVESTMENT
                regulars.put(NON_DEDUCTIBLE_EXPENSE, r);

            }
            // r.setStartDate( getDrawdownStartDate() );
            // r.setEndDate( getDrawdownEndDate() );

            r
                    .setRegularAmount(new java.math.BigDecimal(upfrontFee
                            + ongoingFee));
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

        // This is the Asset Value increased by Growth %, (less ongoing fees)
        // plus increased by indexed regular contributions (less entry fees),
        // less indexed drawdowns

        double growth = 0;
        if (isReinvest())
            growth = getCapitalGrowth() == null ? 0. : getCapitalGrowth()
                    .doubleValue()
                    - inflation;

        double ongoingFee = getOngoingFee() == null ? 0. : getOngoingFee()
                .doubleValue();
        double currentValue = getAmount().doubleValue();
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
            double fractionDrawdns = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getDrawdownStartDate(),
                    getDrawdownEndDate());

            newCurrentValue = getFinancialYearAmount(financialYearEnd + i,
                    currentValue, newCurrentValue, growth);

            // Increase regular Contribution value by indexation
            newContributionAmount = newContributionAmount == 0. ? contributionAmount
                    : newContributionAmount
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
                    - upfrontFeeAmount - fractionDrawdns * newDrawdownAmount
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

    public boolean isReinvest() {
        return super.isReinvest();
    }

    public void setReinvest(boolean value) {
        // if ( isReinvest() == value ) return;

        // if ( regulars != null )
        // regulars.remove( GROSS_INCOME );
        super.setReinvest(value);
    }

}
