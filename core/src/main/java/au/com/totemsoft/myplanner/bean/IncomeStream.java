/*
 * IncomeStream.java
 *
 * Created on 13 November 2002, 14:43
 */

package au.com.totemsoft.myplanner.bean;

import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.code.FrequencyCode;
import au.com.totemsoft.myplanner.code.FundType;
import au.com.totemsoft.util.DateTimeUtils;
import au.com.totemsoft.util.ReferenceCode;

public class IncomeStream extends AssetSuperannuation {

    static final long serialVersionUID = 3868513980021044262L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.INCOME_STREAM;

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates a new instance of IncomeStream */
    public IncomeStream() {
        this(null);
    }

    public IncomeStream(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Financial getNewFinancial() {
        return new IncomeStream();
    }

    public void setFundTypeID(Integer value) {
        super.setFundTypeID(value);

        if (FundType.rcPENSION.getCodeId().equals(getFundTypeID()))
            setFinancialTypeId(INCOMESTREAM_PENSION_ACCOUNT);
        else
            // if (
            // FundType.rcANNUITY.getCodeIDInteger().equals( getFundTypeID() )
            // ||
            // FundType.rcANNUITY_SHORT.getCodeIDInteger().equals(
            // getFundTypeID() ) ||
            // FundType.rcANNUITY_LONG.getCodeIDInteger().equals(
            // getFundTypeID() ) )
            setFinancialTypeId(INCOMESTREAM_ANNUITY_POLICY);

    }

    public ReferenceCode getFundType() {
        return FundType.getFundType(getFundTypeID());
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Regular getRegular(int type) {

        if (regulars == null)
            regulars = new java.util.TreeMap();

        Regular r = null;

        double taxfree = getDeductible() == null ? 0. : getDeductible()
                .doubleValue();
        double annualDrawdowns = getDrawdownAnnualAmount() == null ? 0.
                : getDrawdownAnnualAmount().doubleValue();

        double taxablePension = annualDrawdowns - taxfree;

        double rebatablePercent = getRebateable() == null ? 0.
                : getRebateable().doubleValue();

        double pensionRebate = taxablePension * rebatablePercent / 100.;

        java.math.BigDecimal grossAmount;
        java.math.BigDecimal taxableAmount;
        switch (type) {
        case (iGROSS_INCOME):
            r = (RegularIncome) regulars.get(GROSS_INCOME);
            if (r == null) {
                r = generateIncome(type);
                r.setFinancialTypeId(INCOME_RETIREMENT);
                regulars.put(GROSS_INCOME, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(annualDrawdowns));
            r.setFrequencyCodeID(FrequencyCode.YEARLY);

            break;

        case (iPENSION_PAYMENT):
            r = (RegularIncome) regulars.get(PENSION_PAYMENT);
            if (r == null) {
                r = generateIncome(type);
                r.setFinancialTypeId(INCOME_RETIREMENT);
                regulars.put(PENSION_PAYMENT, r);
            }
            r.setStartDate(getStartDate());
            r.setEndDate(getEndDate());

            r.setRegularAmount(new java.math.BigDecimal(taxablePension));
            r.setFrequencyCodeID(FrequencyCode.YEARLY);

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
            r.setFrequencyCodeID(FrequencyCode.YEARLY);

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
            r.setFrequencyCodeID(FrequencyCode.YEARLY);

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

        // double growth = getCapitalGrowth() == null ? 0.:
        // getCapitalGrowth().doubleValue() ;
        double growth = getIncome() == null ? 0. : getIncome().doubleValue()
                - inflation;

        double ongoingFee = getOngoingFee() == null ? 0. : getOngoingFee()
                .doubleValue();
        double currentValue = getAmount() == null ? 0. : getAmount()
                .doubleValue();
        // double contributionIndexation = getIndexation()== null ?
        // 0.:getIndexation().doubleValue() ;
        // double contributionAmount = getAnnualAmount()== null ?
        // 0.:getAnnualAmount().doubleValue();
        // double upfrontFee = getUpfrontFee()== null ?
        // 0.:getUpfrontFee().doubleValue();
        double drawdownAmount = getDrawdownAnnualAmount() == null ? 0.
                : getDrawdownAnnualAmount().doubleValue();
        double drawdownIndexation = getIndexation() == null ? 0.
                : getIndexation().doubleValue();

        double ongoingFeeAmount = 0.;
        double newCurrentValue = 0.;
        // double newContributionAmount = contributionAmount;
        double upfrontFeeAmount = 0.;
        double newDrawdownAmount = 0.;
        double newTaxfree = 0.;

        double taxfree = getDeductible() == null ? 0. : getDeductible()
                .doubleValue();

        int financialYearEnd = DateTimeUtils.getFinancialYearEnd();

        // Loop for every year
        for (int i = 0; i <= year; i++) {
            // calculate fraction of the amount dependong on start and end dates
            double fraction = DateTimeUtils.getFinancialYearFraction(
                    financialYearEnd + i, getStartDate(), null);
            if (fraction == 0.)
                continue;

            fraction = DateTimeUtils.getFinancialYearFraction(financialYearEnd
                    + i, getStartDate(), getEndDate());
            newCurrentValue = getFinancialYearAmount(financialYearEnd + i,
                    currentValue, newCurrentValue, growth);

            // Increase regular drawdown value by indexation (skip first year)
            newDrawdownAmount = fraction * drawdownAmount
                    * java.lang.Math.pow(1. + drawdownIndexation / 100., i);
            if (newCurrentValue < newDrawdownAmount)
                newDrawdownAmount = newCurrentValue;

            // Calculate ongoing Fee Amount
            ongoingFeeAmount = newCurrentValue * ongoingFee / 100.;

            // Calculate new current value
            newCurrentValue = newCurrentValue - ongoingFeeAmount
                    - newDrawdownAmount;

            newTaxfree = taxfree * fraction;

            // Negative asset value doesn't make any sense
            if (newCurrentValue <= 0.) {
                newCurrentValue = 0.;
                currentValue = 0.;
            }

        }

        // Negative asset value doesn't make any sense
        if (newCurrentValue <= 0.) {
            newDrawdownAmount = newDrawdownAmount + newCurrentValue;
            newTaxfree = drawdownAmount != 0. ? newDrawdownAmount
                    / drawdownAmount * taxfree : newTaxfree;
            newCurrentValue = 0.;

            if (newDrawdownAmount <= 0.) {
                newDrawdownAmount = 0.;
                newTaxfree = 0.;
            }

        }

        // newCurrentValue = (double) java.lang.Math.round(newCurrentValue) ;
        setAmount(new java.math.BigDecimal(newCurrentValue));
        setDrawdownAnnualAmount(new java.math.BigDecimal(newDrawdownAmount));
        setDeductible(new java.math.BigDecimal(newTaxfree));

    }

    /***************************************************************************
     * get/set methods
     **************************************************************************/
    public java.math.BigDecimal getRebateable() {
        return super.getRebateable();
    }

    public void setRebateable(java.math.BigDecimal value) {
        super.setRebateable(value);
    }

    public java.math.BigDecimal getPurchaseCost() {
        return super.getPurchaseCost();
    }

    public void setPurchaseCost(java.math.BigDecimal value) {
        super.setPurchaseCost(value);
    }

    public java.math.BigDecimal getRegularAmount() {
        return super.getRegularAmount();
    }

    public java.math.BigDecimal getIncome() {
        return super.getIncome();
    }

    public void setIncome(java.math.BigDecimal value) {
        super.setIncome(value);
    }

    public Integer getFrequencyCodeID() {
        return super.getFrequencyCodeID();
    }

    public void setFrequencyCodeID(Integer value) {
        super.setFrequencyCodeID(value);
    }

    public String getFrequencyCode() {
        return super.getFrequencyCode();
    }

    public java.math.BigDecimal getDeductibleDSS() {
        return super.getDeductibleDSS();
    }

    public void setDeductibleDSS(java.math.BigDecimal value) {
        super.setDeductibleDSS(value);
    }

    public boolean isComplyingForDSS() {
        return super.isComplyingForDSS();
    }

    public void setComplyingForDSS(boolean value) {
        super.setComplyingForDSS(value);
    }

    public java.math.BigDecimal getIndexation() {
        return super.getIndexation();
    }

    public void setIndexation(java.math.BigDecimal value) {
        super.setIndexation(value);
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

    public static java.math.BigDecimal getTaxedIncome(
            java.math.BigDecimal annualAmount, java.math.BigDecimal deductible) {
        if (annualAmount == null)
            return ZERO;
        if (deductible == null)
            return annualAmount;
        return annualAmount.subtract(deductible);
    }

    public java.math.BigDecimal getTaxedIncome() {
        return getTaxedIncome(getContributionAnnualAmount(), getDeductible());
    }

    public java.util.Date getEndDate() {
        return super.getEndDate();
    }

    public void setEndDate(java.util.Date value) {
        super.setEndDate(value);
    }

}
