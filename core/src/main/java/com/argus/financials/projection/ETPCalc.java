/*
 * ETPCalc.java
 *
 * Created on 12 December 2001, 10:00
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import com.argus.financials.code.ETPType;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.ModelType;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.util.DateTimeUtils;

public class ETPCalc extends MoneyCalc {

    // details
    private Integer etpTypeID;

    private java.util.Date eligibleServiceDate;

    private java.util.Date calculationDate;

    private Boolean encashment;

    // known ETP components
    private double nonQualifyingAmountKnown = 0.; // UNKNOWN_VALUE;

    private double cgtExemptAmountKnown = 0.; // UNKNOWN_VALUE;

    private double undeductedAmountKnown = 0.; // UNKNOWN_VALUE;

    private double concessionalAmountKnown = 0.; // UNKNOWN_VALUE;

    private double post061994InvAmountKnown = 0.; // UNKNOWN_VALUE;

    private double rblAmount = 0.;

    /** Creates new ETPCalc */
    public ETPCalc() {
        super();
    }

    public ETPCalc(ETPCalc obj) {
        super();

        this.assign(obj); // call in top derived class
    }

    public Integer getDefaultModelType() {
        return ModelType.ETP_ROLLOVER;
    }

    public String getDefaultTitle() {
        return "ETP Rollover";
    }

    /**
     * 
     */
    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = UNKNOWN_VALUE;

        if (property.equals(ELIGIBLE_SERVICE_DATE)) {
            setEligibleServiceDate(DateTimeUtils.getDate(value));

        } else if (property.equals(ETP_TYPE)) {
            if (Number2.getNumberInstance().isValid(value))
                setETPTypeID(new Integer(value));
            else
                setETPTypeID(new ETPType().getCodeID(value));

        } else if (property.equals(CALCULATION_DATE)) {
            setCalculationDate(DateTimeUtils.getDate(value));

        } else if (property.equals(TOTAL_ETP_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setTotalETPAmount(d);

        } else if (property.equals(ENCASHMENT_CODE_YES)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setEncashment(Boolean.TRUE);

        } else if (property.equals(ENCASHMENT_CODE_NO)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setEncashment(Boolean.FALSE);

        } else if (property.equals(NON_QUALIFYING)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setNonQualifyingAmountKnown(d < 0 ? 0. : d);

        } else if (property.equals(CGT_EXEMPT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setCGTExemptAmountKnown(d < 0 ? 0. : d);

        } else if (property.equals(UNDEDUCTED)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setUndeductedAmountKnown(d < 0 ? 0. : d);

        } else if (property.equals(CONCESSIONAL)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setConcessionalAmountKnown(d < 0 ? 0. : d);

        } else if (property.equals(POST_JUNE_94_INVALIDITY)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setPost061994InvAmountKnown(d < 0 ? 0. : d);

        } else if (property.equals(RBL_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setRBLAmount(d < 0 ? 0. : d);

        } else
            return false;

        return true;
    }

    /**
     * 
     */
    public void assign(MoneyCalc obj) {
        super.assign(obj);

        if (!(obj instanceof ETPCalc))
            return;

        etpTypeID = ((ETPCalc) obj).etpTypeID;
        eligibleServiceDate = ((ETPCalc) obj).eligibleServiceDate;
        calculationDate = ((ETPCalc) obj).calculationDate;
        encashment = ((ETPCalc) obj).encashment;

        nonQualifyingAmountKnown = ((ETPCalc) obj).nonQualifyingAmountKnown;
        cgtExemptAmountKnown = ((ETPCalc) obj).cgtExemptAmountKnown;
        undeductedAmountKnown = ((ETPCalc) obj).undeductedAmountKnown;
        concessionalAmountKnown = ((ETPCalc) obj).concessionalAmountKnown;
        post061994InvAmountKnown = ((ETPCalc) obj).post061994InvAmountKnown;

        if (this.getClass().equals(obj.getClass()))
            setModified();

    }

    /**
     * 
     */
    public boolean isReady() {
        return (getTotalETPAmount() >= 0) && (getEncashment() != null)
                && (getAge() > 0) && (getTaxRate() >= 0)
                && (getETPTypeID() != null)
                && (getEligibleServiceDate() != null)
                && (getCalculationDate() != null)
                && (getNonQualifyingAmountKnown() >= 0)
                && (getCGTExemptAmountKnown() >= 0)
                && (getUndeductedAmountKnown() >= 0)
                && (getConcessionalAmountKnown() >= 0)
                && (getPost061994InvAmountKnown() >= 0);
    }

    /**
     * get/set
     */
    // ETPType.SUPER_ETP_TAXED;
    // ETPType.SUPER_ETP_UNTAXED;
    public Integer getETPTypeID() {
        return etpTypeID;
    }

    public void setETPTypeID(Integer value) {
        if (equals(etpTypeID, value))
            return;

        etpTypeID = value;
        setModified();
    }

    public String getETPTypeDesc() {
        return new ETPType().getCodeDescription(getETPTypeID());
    }

    public Boolean getEncashment() {
        return encashment;
    }

    public void setEncashment(Boolean value) {
        if (equals(encashment, value))
            return;

        encashment = value;

        if (!encashment.booleanValue())
            if (getTaxRate() != 0) {
                setTaxRate(0);
                return;
            }

        setModified();
    }

    public java.util.Date getEligibleServiceDate() {
        return eligibleServiceDate;
    }

    public void setEligibleServiceDate(java.util.Date value) {
        if (equals(eligibleServiceDate, value))
            return;

        eligibleServiceDate = value;

        setModified();
    }

    public java.util.Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(java.util.Date value) {
        if (equals(calculationDate, value))
            return;

        if (value != null && TaxUtils.TAX_1983_COMMENCE_DATE.after(value))
            throw new IllegalArgumentException(
                    "Date could never be before 1/7/1983");

        calculationDate = value;

        setModified();
    }

    public int getPre071983Days() {
        int pre071983Days = 0;

        if (getEligibleServiceDate() != null) {// &&
                                                // TaxCalc.TAX_1983_COMMENCE_DATE
                                                // != null ) {
            pre071983Days = (int) ((TaxUtils.TAX_1983_COMMENCE_DATE.getTime() - getEligibleServiceDate()
                    .getTime()) / MILLIS_PER_DAY);
            if (pre071983Days < 0)
                pre071983Days = 0;
        }

        return pre071983Days;
    }

    public int getPost061983Days() {
        int post061983Days = 0;

        if (getCalculationDate() != null && getEligibleServiceDate() != null) {
            post061983Days = (int) ((getCalculationDate().getTime() - (getPre071983Days() == 0 ? getEligibleServiceDate()
                    .getTime()
                    : TaxUtils.TAX_1983_COMMENCE_DATE.getTime())) / MILLIS_PER_DAY) + 1;
            if (post061983Days < 0)
                post061983Days = 0;
        }

        return post061983Days;
    }

    public double getTotalETPAmount() {
        return getInitialValue();
    }

    public void setTotalETPAmount(double value) {
        setInitialValue(value);
    }

    //
    public double getNonQualifyingAmountKnown() {
        return nonQualifyingAmountKnown;
    }

    public void setNonQualifyingAmountKnown(double value) {
        if (nonQualifyingAmountKnown == value)
            return;

        nonQualifyingAmountKnown = value;
        setModified();
    }

    public double getCGTExemptAmountKnown() {
        return cgtExemptAmountKnown;
    }

    public void setCGTExemptAmountKnown(double value) {
        if (cgtExemptAmountKnown == value)
            return;

        cgtExemptAmountKnown = value;
        setModified();
    }

    public double getUndeductedAmountKnown() {
        return undeductedAmountKnown;
    }

    public void setUndeductedAmountKnown(double value) {
        if (undeductedAmountKnown == value)
            return;

        undeductedAmountKnown = value;
        setModified();
    }

    public double getConcessionalAmountKnown() {
        return concessionalAmountKnown;
    }

    public void setConcessionalAmountKnown(double value) {
        if (concessionalAmountKnown == value)
            return;

        concessionalAmountKnown = value;
        setModified();
    }

    public double getPost061994InvAmountKnown() {
        return post061994InvAmountKnown;
    }

    public void setPost061994InvAmountKnown(double value) {
        if (post061994InvAmountKnown == value)
            return;

        post061994InvAmountKnown = value;
        setModified();
    }

    public double getRBLAmount() {
        return rblAmount;
    }

    public void setRBLAmount(double value) {
        if (rblAmount == value)
            return;

        rblAmount = value;
        setModified();
    }

    //
    public double getNonQualifyingAmount() {
        if (nonQualifyingAmountKnown == UNKNOWN_VALUE)
            return 0;
        return nonQualifyingAmountKnown;

    }

    public double getCGTExemptAmount() {
        if (cgtExemptAmountKnown == UNKNOWN_VALUE)
            return 0;
        return cgtExemptAmountKnown;

    }

    public double getUndeductedAmount() {
        if (undeductedAmountKnown == UNKNOWN_VALUE)
            return 0;
        return undeductedAmountKnown;

    }

    public double getConcessionalAmount() {
        if (concessionalAmountKnown == UNKNOWN_VALUE)
            return 0;
        return concessionalAmountKnown;

    }

    private double getPre071983AmountKnown() {

        if (getNonQualifyingAmountKnown() == UNKNOWN_VALUE
                || getCGTExemptAmountKnown() == UNKNOWN_VALUE
                || getConcessionalAmountKnown() == UNKNOWN_VALUE
                || getPost061994InvAmountKnown() == UNKNOWN_VALUE)
            return UNKNOWN_VALUE;

        double pre071983AmountKnown = getNonQualifyingAmountKnown()
                + getCGTExemptAmountKnown() + getConcessionalAmountKnown()
                + getPost061994InvAmountKnown();

        if (getTotalETPAmount() <= pre071983AmountKnown)
            return 0;

        return (getTotalETPAmount() - pre071983AmountKnown)
                * ((double) getPre071983Days() / (double) (getPre071983Days() + getPost061983Days()));
    }

    public double getPre071983Amount() {

        if (getNonQualifyingAmount() == UNKNOWN_VALUE
                || getCGTExemptAmount() == UNKNOWN_VALUE
                || getUndeductedAmount() == UNKNOWN_VALUE
                || getConcessionalAmount() == UNKNOWN_VALUE
                || getPost061983AmountTaxed() == UNKNOWN_VALUE
                || getPost061994InvAmount() == UNKNOWN_VALUE
                || getPre071983AmountKnown() == UNKNOWN_VALUE)
            return 0;

        double pre071983Amount = getPre071983AmountKnown();

        double calcAmount = getNonQualifyingAmount() + getCGTExemptAmount()
                + getUndeductedAmount() + getConcessionalAmount()
                + getPost061983AmountTaxed() + getPost061994InvAmount();

        if (pre071983Amount + calcAmount > getTotalETPAmount())
            pre071983Amount = getTotalETPAmount() - calcAmount;

        return pre071983Amount;
    }

    private double _getPost061983AmountTaxed() {

        if (getNonQualifyingAmount() == UNKNOWN_VALUE
                || getCGTExemptAmount() == UNKNOWN_VALUE
                || getUndeductedAmount() == UNKNOWN_VALUE
                || getConcessionalAmount() == UNKNOWN_VALUE
                || getPre071983AmountKnown() == UNKNOWN_VALUE
                || getPost061994InvAmount() == UNKNOWN_VALUE)
            return 0;

        double post061983AmountTaxed = getNonQualifyingAmount()
                + getCGTExemptAmount() + getUndeductedAmount()
                + getConcessionalAmount() + getPre071983AmountKnown()
                + getPost061994InvAmount();

        return getTotalETPAmount() <= post061983AmountTaxed ? 0
                : getTotalETPAmount() - post061983AmountTaxed;
    }

    public double getPost061983AmountTaxed() {

        if (getETPTypeID() == null)
            return 0;

        if (ETPType.SUPER_ETP_UNTAXED.equals(getETPTypeID()))
            return 0;

        if (ETPType.SUPER_ETP_TAXED.equals(getETPTypeID()))
            return _getPost061983AmountTaxed();

        throw new IllegalArgumentException("Unknown ETPType: " + getETPTypeID());
    }

    // SUPER_ETP_TAXED
    // Calculations should be as specified before.
    // SUPER_ETP_UNTAXED
    // The amount that we currently calculate for the Post June 1983 (Taxed)
    // should be placed in the Post June 1983 (Untaxed) field.
    public double getPost061983AmountUntaxed() {
        if (getETPTypeID() == null)
            return 0;

        if (ETPType.SUPER_ETP_TAXED.equals(getETPTypeID()))
            return 0;

        if (ETPType.SUPER_ETP_UNTAXED.equals(getETPTypeID()))
            return _getPost061983AmountTaxed();

        throw new IllegalArgumentException("Unknown ETPType: " + getETPTypeID());
    }

    public double getPost061994InvAmount() {
        if (post061994InvAmountKnown == UNKNOWN_VALUE)
            return 0;
        return post061994InvAmountKnown;
    }

    public double getTotalETPComponentsAmount() {

        if (getNonQualifyingAmount() == UNKNOWN_VALUE
                || getCGTExemptAmount() == UNKNOWN_VALUE
                || getUndeductedAmount() == UNKNOWN_VALUE
                || getConcessionalAmount() == UNKNOWN_VALUE)
            return 0;

        return getNonQualifyingAmount() + getCGTExemptAmount()
                + getUndeductedAmount() + getConcessionalAmount()
                + getPre071983Amount() + getPost061983AmountTaxed()
                + getPost061983AmountUntaxed() + getPost061994InvAmount();
    }

    public double getTotalNetBenefit() {
        return getTotalETPComponentsAmount() - getETPTaxAmount();
    }

    public double getPost061983AmountTaxedNetBenefit() {

        return getPost061983AmountTaxed() - getETPTaxAmount();
    }

    // tax payable
    public double getETPTaxAmount() {

        if (getETPTypeID() == null)
            return 0;

        if (getEncashment() == null || !getEncashment().booleanValue())
            return 0;

        double post061983AmountTaxed = getPost061983AmountTaxed();

        if (ETPType.SUPER_ETP_TAXED.equals(getETPTypeID())) {
            double under = TaxUtils.getTaxRatePost061983UnderThreshold(getAge());
            double over = TaxUtils.getTaxRatePost061983OverThreshold(getAge());

            return post061983AmountTaxed > TaxUtils.TAX_FREE_THRESHOLD ? TaxUtils.TAX_FREE_THRESHOLD
                    * (under - over) + post061983AmountTaxed * over
                    : post061983AmountTaxed * under;

            // before #602
            // If the client is < 55 or >= 55 over the TAX_FREE_THRESHOLD
            // the Post061983AmountUntaxed component should be taxed at 31.5%.
            // If the client is >= 55 and within the TAX_FREE_THRESHOLD
            // the Post061983AmountUntaxed component should be taxed at 16.5%.
            /*
             * } else if ( ETPType.SUPER_ETP_UNTAXED.equals( getETPTypeID() ) ) {
             * double tax; double post061983AmountUntaxed =
             * getPost061983AmountUntaxed();
             * 
             * if ( ( getAge() < TaxCalc.TAX_EFFECTIVE_AGE ) || ( getAge() >=
             * TaxCalc.TAX_EFFECTIVE_AGE && post061983AmountUntaxed >
             * TaxCalc.TAX_FREE_THRESHOLD ) ) tax = .315; else if ( getAge() >=
             * TaxCalc.TAX_EFFECTIVE_AGE && post061983AmountUntaxed <=
             * TaxCalc.TAX_FREE_THRESHOLD ) tax = .165; else tax = 1.;
             * 
             * return post061983AmountTaxed + post061983AmountUntaxed * tax;
             */

            // after #602 requirement
            // Untaxed Element
            // Under 55 31.5%
            // Over 55 First $105843, 16.5%
            // Above $105843, 31.5%
        } else if (ETPType.SUPER_ETP_UNTAXED.equals(getETPTypeID())) {
            double post061983AmountUntaxed = getPost061983AmountUntaxed();

            if ((getAge() < TaxUtils.TAX_EFFECTIVE_AGE))
                return post061983AmountUntaxed * .315;

            if (post061983AmountUntaxed <= TaxUtils.TAX_FREE_THRESHOLD)
                return post061983AmountUntaxed * .165;

            post061983AmountUntaxed -= TaxUtils.TAX_FREE_THRESHOLD;
            return TaxUtils.TAX_FREE_THRESHOLD * .165 + post061983AmountUntaxed
                    * .315;

        } else
            throw new IllegalArgumentException("Unknown ETPType: "
                    + getETPTypeID());

    }

    public double getAssesableIncomeAmount() {
        if (getEncashment() == null || !getEncashment().booleanValue()
                || getConcessionalAmount() == UNKNOWN_VALUE
                || getNonQualifyingAmount() == UNKNOWN_VALUE)
            return 0;

        return (getConcessionalAmount() + getPre071983Amount())
                * TaxUtils.ASSESABLE_INCOME_RATE + getNonQualifyingAmount();
    }

    public double getAssesableIncomeTaxAmount() {
        if (getEncashment() == null || !getEncashment().booleanValue()
                || getNonQualifyingAmount() == UNKNOWN_VALUE
                || getConcessionalAmount() == UNKNOWN_VALUE)

            return 0;

        return (getNonQualifyingAmount() + getConcessionalAmount()
                * TaxUtils.ASSESABLE_INCOME_RATE + getPre071983Amount()
                * TaxUtils.ASSESABLE_INCOME_RATE)
                * getTaxRate() / 100;
    }

    public double getTotalTaxAmount() {
        if (getEncashment() == null || !getEncashment().booleanValue())
            return 0;

        return getETPTaxAmount() + getAssesableIncomeTaxAmount();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Integer getRequiredFrequencyCodeID() {
        return FrequencyCode.YEARLY;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        financials.addAll(getGeneratedIncomes(desc));
        financials.addAll(getGeneratedExpenses(desc));
        financials.addAll(getGeneratedLiabilities(desc));

        return financials;

    }

    private java.util.Collection getGeneratedIncomes(String desc) {
        return null;
    }

    private java.util.Collection getGeneratedExpenses(String desc) {
        return null;
    }

    private java.util.Collection getGeneratedLiabilities(String desc) {
        return null;
    }

}