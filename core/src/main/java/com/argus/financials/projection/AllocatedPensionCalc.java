/*
 * AllocatedPensionCalc.java
 *
 * Created on 13 December 2001, 14:21
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.PaymentType;
import com.argus.financials.code.ReversionaryCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.etc.BadArgumentException;
import com.argus.financials.projection.data.LifeExpectancy;
import com.argus.financials.projection.data.PensionValuation;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;

public final class AllocatedPensionCalc extends ETPCalc {

    private Boolean pre071983;

    private Boolean invalidity;

    private double rebateableRate = UNKNOWN_VALUE;

    private double entryFeeRate = UNKNOWN_VALUE;

    private Integer pensionReversionOptionID;

    private Integer partnerSexCodeID;

    private int partnerAge = (int) UNKNOWN_VALUE;

    private java.util.Date partnerDateOfBirth;

    private Integer requiredPaymentTypeID;

    private double requiredPaymentAmount = UNKNOWN_VALUE; // can be GROSS,
                                                            // NET, MIN, MAX

    private Integer requiredFrequencyCodeID;

    private Integer investmentStrategyCodeID;

    private double residual = UNKNOWN_VALUE;

    // calculated
    private double[] requiredIncomeValues;

    // if requiredPaymentTypeID = NET this member will be used to calculate
    // using recursion optimal GROSS payment amount to satisfy our NET
    // requiredPaymentAmount
    private double gross4net_RequiredPaymentAmount;// = UNKNOWN_VALUE;

    /** Creates new AllocatedPensionCalc */
    public AllocatedPensionCalc() {
        super();
        residual = 0; // NO UI !!
    }

    public AllocatedPensionCalc(AllocatedPensionCalc obj) {
        this();

        this.assign(obj); // call in top derived class
    }

    public Integer getDefaultModelType() {
        return ModelType.ALLOCATED_PENSION;
    }

    public String getDefaultTitle() {
        return "Allocated Pension";
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        if (changeEvent.getSource() instanceof ETPCalc) {
            // We have notified that ETPCalc data changed.
            // Now we have to notify this AllocatedPensionCalc change listeners
            super.stateChanged(changeEvent);
            // notifyChangeListeners( DATA_MODIFIED );
        }

    }

    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = UNKNOWN_VALUE;

        if (property.equals(SEX_CODE)) {
            if (Number2.getNumberInstance().isValid(value))
                setSexCodeID(new Integer(value));
            else
                setSexCodeID(new SexCode().getCodeID(value));

        } else if (property.equals(REVIEW_FEES)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);

            if (d != UNKNOWN_VALUE)
                setEntryFeeRate(d);

        } else if (property.equals(REBATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            if (d != UNKNOWN_VALUE)
                setRebateableRateableRate(d);

        } else if (property.equals(DOB_PARTNER)) {
            setPartnerDateOfBirth(DateTimeUtils.getDate(value));

        } else if (property.equals(REQUIRED_PAYMENT_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            if (d != UNKNOWN_VALUE)
                setRequiredPaymentAmount(d);

        } else if (property.equals(PRE_071983_CODE_YES)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setPre071983(Boolean.TRUE);

        } else if (property.equals(PRE_071983_CODE_NO)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setPre071983(Boolean.FALSE);

        } else if (property.equals(INVALIDITY_CODE_YES)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setInvalidity(Boolean.TRUE);

        } else if (property.equals(INVALIDITY_CODE_NO)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setInvalidity(Boolean.FALSE);

        } else if (property.equals(PENSION_REVERSION_OPTION)) {
            if (Number2.getNumberInstance().isValid(value))
                setPensionReversionOptionID(new Integer(value));
            else
                setPensionReversionOptionID(new ReversionaryCode()
                        .getCodeID(value));

        } else if (property.equals(PARTNER_SEX_CODE)) {
            if (Number2.getNumberInstance().isValid(value))
                setPartnerSexCodeID(new Integer(value));
            else
                setPartnerSexCodeID(new SexCode().getCodeID(value));

        } else if (property.equals(REQUIRED_PAYMENT_TYPE)) {
            if (Number2.getNumberInstance().isValid(value))
                setRequiredPaymentTypeID(new Integer(value));
            else
                setRequiredPaymentTypeID(new PaymentType().getCodeID(value));

        } else if (property.equals(REQUIRED_FREQUENCY)) {
            if (Number2.getNumberInstance().isValid(value))
                setRequiredFrequencyCodeID(new Integer(value));
            else
                setRequiredFrequencyCodeID(new FrequencyCode().getCodeID(value));

        } else if (property.equals(INDEXED)) {
            if (value == null)
                setIndexed(false);
            setIndexed(new Boolean(value).booleanValue());

        } else if (property.equals(INV_STRATEGY)) {
            if (Number2.getNumberInstance().isValid(value))
                setInvestmentStrategyCodeID(new Integer(value));
            else
                setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                        .getCodeID(value));

        } else
            return false;

        return true;
    }

    /***************************************************************************
     * 
     */
    public void assign(MoneyCalc obj) {

        super.assign(obj);

        if (!(obj instanceof AllocatedPensionCalc)) {
            setModified();
            return;
        }

        pre071983 = ((AllocatedPensionCalc) obj).pre071983;
        invalidity = ((AllocatedPensionCalc) obj).invalidity;
        rebateableRate = ((AllocatedPensionCalc) obj).rebateableRate;
        entryFeeRate = ((AllocatedPensionCalc) obj).entryFeeRate;
        pensionReversionOptionID = ((AllocatedPensionCalc) obj).pensionReversionOptionID;
        partnerSexCodeID = ((AllocatedPensionCalc) obj).partnerSexCodeID;
        partnerAge = ((AllocatedPensionCalc) obj).partnerAge;
        partnerDateOfBirth = ((AllocatedPensionCalc) obj).partnerDateOfBirth;

        requiredPaymentTypeID = ((AllocatedPensionCalc) obj).requiredPaymentTypeID;
        requiredPaymentAmount = ((AllocatedPensionCalc) obj).requiredPaymentAmount;
        requiredFrequencyCodeID = ((AllocatedPensionCalc) obj).requiredFrequencyCodeID;

        investmentStrategyCodeID = ((AllocatedPensionCalc) obj).investmentStrategyCodeID;

        residual = ((AllocatedPensionCalc) obj).residual;

        setModified();

        // do not do calc, just copy data
        if (((AllocatedPensionCalc) obj).requiredIncomeValues != null) {
            requiredIncomeValues = new double[((AllocatedPensionCalc) obj).requiredIncomeValues.length];
            // System.arraycopy(
            // ((AllocatedPensionCalc)obj).requiredIncomeValues, 0,
            // requiredIncomeValues, 0,
            // ((AllocatedPensionCalc)obj).requiredIncomeValues.length );

        }

    }

    /***************************************************************************
     * 
     */
    public void setModified() {
        super.setModified();

        // etp base class data/view
        if (super.isReady())
            notifyChangeListeners(DATA_MODIFIED);
    }

    public boolean isETPReady() {
        return super.isReady(); // etp only
    }

    public boolean isReady() {
        return super.isReady() // etp
                && (getSexCodeID() != null)
                && (getPre071983() != null)
                && (getInvalidity() != null)
                && (getRebateableRate() >= 0)
                && (getEntryFeeRate() >= 0)
                && (!isIndexed() || (isIndexed() && getIndexRate() >= 0))
                && (getPensionReversionOptionID() != null)
                && (!ReversionaryCode.YES.equals(getPensionReversionOptionID()) || (getPartnerAge() > 0 && getPartnerSexCodeID() != null))
                && (getRequiredPaymentTypeID() != null)
                && (PaymentType.MINIMUM.equals(getRequiredPaymentTypeID())
                        || PaymentType.MAXIMUM
                                .equals(getRequiredPaymentTypeID()) || getRequiredPaymentAmount() >= 0)
                && (getRequiredFrequencyCodeID() != null)
                && (getInvestmentStrategyCodeID() != null) // required to calc
                                                            // graph data
        ;
    }

    /**
     * get/set
     */
    public void setYears(double value) {
        if (getYears() == value)
            return;

        if (value > 0)
            requiredIncomeValues = new double[(int) Math.ceil(value)];
        else
            requiredIncomeValues = null;

        super.setYears(value);
    }

    // public Integer getClientSexCodeID() { return getSexCodeID(); }
    // public void setClientSexCodeID( Integer value ) {
    // setClientSexCodeID( value );
    // }
    public String getClientSexCodeDesc() {
        return new SexCode().getCodeDescription(getSexCodeID());
    }

    public java.util.Date getCommencementDate() {
        return getCalculationDate();
    }

    public double getPurchasePrice() {
        if (getEncashment() == null || getTotalETPAmount() == UNKNOWN_VALUE)
            return 0;

        boolean encashment = getEncashment().booleanValue();
        if (encashment)
            return getTotalETPAmount() - getTotalTaxAmount();
        return getTotalETPAmount();
    }

    public Boolean getPre071983() {
        return pre071983;
    }

    public void setPre071983(Boolean value) {
        if (equals(pre071983, value))
            return;

        pre071983 = value;
        setModified();
    }

    public Boolean getInvalidity() {
        return invalidity;
    }

    public void setInvalidity(Boolean value) {
        if (equals(invalidity, value))
            return;

        invalidity = value;
        setModified();
    }

    public double getRebateableRate() {
        return rebateableRate;
    }

    public void setRebateableRateableRate(double value) {
        if (rebateableRate == value)
            return;

        rebateableRate = value;
        setModified();
    }

    public double getEntryFeeRate() {
        return entryFeeRate;
    }

    public void setEntryFeeRate(double value) {
        if (entryFeeRate == value)
            return;

        entryFeeRate = value;
        setModified();
    }

    public Integer getPensionReversionOptionID() {
        return pensionReversionOptionID;
    }

    public void setPensionReversionOptionID(Integer value) {
        if (equals(pensionReversionOptionID, value))
            return;

        pensionReversionOptionID = value;
        setModified();
    }

    public String getPensionReversionOptionDesc() {
        return new ReversionaryCode()
                .getCodeDescription(getPensionReversionOptionID());
    }

    public java.util.Date getPartnerDateOfBirth() {
        return partnerDateOfBirth;
    }

    public void setPartnerDateOfBirth(java.util.Date value) {
        if (equals(partnerDateOfBirth, value))
            return;

        partnerDateOfBirth = value;
        setModified();
    }

    public int getPartnerAge() {
        return getAge(getPartnerDateOfBirth());
    }

    public void setPartnerAge(int value) {
        if (partnerAge == value)
            return;

        partnerAge = value;
        setModified();
    }

    public Integer getPartnerSexCodeID() {
        return partnerSexCodeID;
    }

    public void setPartnerSexCodeID(Integer value) {
        if (equals(partnerSexCodeID, value))
            return;

        partnerSexCodeID = value;
        setModified();
    }

    public String getPartnerSexCodeDesc() {
        return new SexCode().getCodeDescription(getPartnerSexCodeID());
    }

    public double getLifeExpectancy() {
        double le = LifeExpectancy.getValue(getAge(), getSexCodeID());

        if (ReversionaryCode.YES.equals(getPensionReversionOptionID()))
            le = Math.max(le, LifeExpectancy.getValue(getPartnerAge(),
                    getPartnerSexCodeID()));
        return le;
    }

    /**
     * DSS UPP/Deductible Amount
     */
    public double getDSSUPPAmount() {
        return getPurchasePrice();
    }

    public double getAnnualNonAssesableAmount() {
        if (getLifeExpectancy() == MoneyCalc.UNKNOWN_VALUE)
            return 0;

        return getDSSUPPAmount() / getLifeExpectancy();
    }

    public double getFirstYearNonAssesableAmount() {
        return DateTimeUtils.getFinancialYearFraction(getCommencementDate(),
                true)
                * getAnnualNonAssesableAmount();
    }

    /**
     * 
     */
    public Integer getRequiredPaymentTypeID() {
        return requiredPaymentTypeID;
    }

    public void setRequiredPaymentTypeID(Integer value) {
        if (equals(requiredPaymentTypeID, value))
            return;

        requiredPaymentTypeID = value;
        gross4net_RequiredPaymentAmount = UNKNOWN_VALUE;

        setModified();
    }

    public String getRequiredPaymentTypeDesc() {
        return new PaymentType().getCodeDescription(getRequiredPaymentTypeID());
    }

    public double getRequiredPaymentAmount() {
        if (getRequiredPaymentTypeID() == null)
            requiredPaymentAmount = 0.;// UNKNOWN_VALUE;
        else if (PaymentType.MINIMUM.equals(getRequiredPaymentTypeID()))
            requiredPaymentAmount = getAnnualMinimumAmount();
        else if (PaymentType.MAXIMUM.equals(getRequiredPaymentTypeID()))
            requiredPaymentAmount = getAnnualMaximumAmount();

        return requiredPaymentAmount; // GROSS/NET
    }

    public void setRequiredPaymentAmount(double value) {
        if (requiredPaymentAmount == value)
            return;

        requiredPaymentAmount = value;
        gross4net_RequiredPaymentAmount = UNKNOWN_VALUE;

        setModified();
    }

    public Integer getRequiredFrequencyCodeID() {
        return requiredFrequencyCodeID;
    }

    public void setRequiredFrequencyCodeID(Integer value) {
        if (equals(requiredFrequencyCodeID, value))
            return;

        requiredFrequencyCodeID = value;
        gross4net_RequiredPaymentAmount = UNKNOWN_VALUE;

        setModified();
    }

    public String getRequiredFrequencyCodeDesc() {
        return new FrequencyCode()
                .getCodeDescription(getRequiredFrequencyCodeID());
    }

    /**
     * 
     */
    private double getPurchasePrice(double fee) {
        return getPurchasePrice() * (100. - fee) / 100.;
    }

    public double getAnnualMinimumAmount() {
        if (getPurchasePrice() == UNKNOWN_VALUE
                || getAge() == (int) UNKNOWN_VALUE)
            return 0;

        try {
            double d = getPurchasePrice(getEntryFeeRate())
                    / PensionValuation.getValue(getAge(), true);
            return Math.round(d / 10) * 10;
        } catch (BadArgumentException e) {
            return UNKNOWN_VALUE;
        }
    }

    public double getAnnualMaximumAmount() {
        if (getPurchasePrice() == UNKNOWN_VALUE
                || getAge() == (int) UNKNOWN_VALUE)
            return 0;

        try {
            double d = getPurchasePrice(getEntryFeeRate())
                    / PensionValuation.getValue(getAge(), false);
            return Math.round(d / 10) * 10;
        } catch (BadArgumentException e) {
            return UNKNOWN_VALUE;
        }
    }

    public double getFirstYearMinimumAmount() {
        double d = getAnnualMinimumAmount()
                * DateTimeUtils.getFinancialYearFraction(getCommencementDate(),
                        true);
        return Math.round(d / 10) * 10;
    }

    public double getFirstYearMaximumAmount() {
        double d = getAnnualMaximumAmount()
                * DateTimeUtils.getFinancialYearFraction(getCommencementDate(),
                        true);
        return Math.round(d / 10) * 10;
    }

    /**
     * ATO UPP/Deductible Amount
     */
    public double getATOUPPAmount() {
        if (getPre071983() == null)
            return 0;

        double atoUPP = getUndeductedAmountKnown()
                + getPost061994InvAmountKnown();

        if (getPre071983().booleanValue())
            return atoUPP + getConcessionalAmountKnown() + getPre071983Amount();

        return atoUPP;
    }

    public double getATOAnnualDeductibleAmount() {
        if (getLifeExpectancy() == MoneyCalc.UNKNOWN_VALUE)
            return 0;

        return getATOUPPAmount() / getLifeExpectancy();
    }

    public double geATOtFirstYearDeductibleAmount() {

        double atoFirstYearDeductibleAmount = DateTimeUtils
                .getFinancialYearFraction(getCommencementDate(), true)
                * getATOAnnualDeductibleAmount();

        return atoFirstYearDeductibleAmount;
    }

    public double getATORegularDeductibleAmount() {
        if (getRequiredFrequencyCodeID() == null)
            return 0;

        return FrequencyCode.getPeriodAmount(getRequiredFrequencyCodeID(),
                getATOAnnualDeductibleAmount()).doubleValue();
    }

    // Pension payments are made on 15th of each month,
    // therefore, we need to determine how many 15th's to occur before 30 June.
    public double geATOtFirstYearRegularDeductibleAmount() {
        if (getCommencementDate() == null
                || getRequiredFrequencyCodeID() == null)
            return 0;

        int periods = DateTimeUtils.getNumberOfPeriods(getCommencementDate(),
                getRequiredFrequencyCodeID(), true);
        if (periods == 0)
            periods++;
        return geATOtFirstYearDeductibleAmount() / periods;
    }

    /**
     * 
     */
    private double getWeeklyPaymentAssesibleAmount() {
        return (getGrossPaymentAmount() - getATOAnnualDeductibleAmount())
                / WEEKS_PER_YEAR;
    }

    public double getFirstYearWeeklyPaymentAssesibleAmount() {
        if (getRequiredFrequencyCodeID() == null)
            return 0;
        return FrequencyCode.getAnnualAmount(
                getRequiredFrequencyCodeID(),
                getGrossRegularPaymentAmount()
                        - geATOtFirstYearRegularDeductibleAmount())
                .doubleValue()
                / WEEKS_PER_YEAR;
    }

    /**
     * 
     */
    public double getRebateAmount() {
        if (getRequiredFrequencyCodeID() == null)
            return 0;

        return FrequencyCode.getAnnualAmount(getRequiredFrequencyCodeID(),
                getRegularRebateAmount()).doubleValue();
    }

    public double getRegularRebateAmount() {
        double rebateAmount = 0;
        double Amount2 = getGrossRegularPaymentAmount();
        double Amount3 = geATOtFirstYearRegularDeductibleAmount();

        if (getAge() >= TaxUtils.TAX_EFFECTIVE_AGE
                || (getInvalidity() != null && getInvalidity().booleanValue()))
            rebateAmount = (getGrossRegularPaymentAmount() - geATOtFirstYearRegularDeductibleAmount())
                    * // geATORegularDeductibleAmount
                    ((getRebateableRate() / 100.) * (TaxUtils.SUPER_TAX_RATE / 100.));

        return rebateAmount;
    }

    /**
     * 
     */
    public double getGrossRegularPaymentAmount() {
        if (getRequiredFrequencyCodeID() == null)
            return 0;
        return FrequencyCode.getPeriodAmount(getRequiredFrequencyCodeID(),
                getGrossPaymentAmount()).doubleValue();
    }

    private double getGrossPaymentAmount() {
        if (getRequiredPaymentTypeID() == null)
            return 0.;

        // This amount should be the annual divided by the frequency
        // if a gross/net pension amount is required,
        // if min/max divide 1st year amount by payments left to 30 June.
        if (PaymentType.NET.equals(getRequiredPaymentTypeID())) {
            gross4net_RequiredPaymentAmount = getOptimalRequiredGrossPaymentAmount();
            return gross4net_RequiredPaymentAmount;
        } else
            gross4net_RequiredPaymentAmount = UNKNOWN_VALUE;

        return getRequiredPaymentAmount();
    }

    private double getFirstYearGrossRegularPaymentAmount() {
        return getGrossPaymentAmount()
                * DateTimeUtils.getFinancialYearFraction(getCommencementDate(),
                        true);
    }

    public double getNetRegularPaymentAmount() {

        if (getRequiredFrequencyCodeID() == null)
            return 0;

        if (PaymentType.NET.equals(getRequiredPaymentTypeID()))
            return FrequencyCode.getPeriodAmount(getRequiredFrequencyCodeID(),
                    getRequiredPaymentAmount()).doubleValue();

        return getGrossRegularPaymentAmount() - getPAYGRegularAmount();
    }

    /**
     * PAYG
     */
    private double getPAYGAmount() {
        return TaxUtils.getPAYG(getFirstYearWeeklyPaymentAssesibleAmount())
                * WEEKS_PER_YEAR;
    }

    private double getPAYGRegularAmount_PreRebate() { // PreRebate
        if (getRequiredFrequencyCodeID() == null)
            return 0;
        return FrequencyCode.getPeriodAmount(getRequiredFrequencyCodeID(),
                getPAYGAmount()).doubleValue();
    }

    public double getPAYGRegularAmount() {
        double paygRegularAmount = getPAYGRegularAmount_PreRebate();
        double regularRebateAmount = getRegularRebateAmount();

        return paygRegularAmount < regularRebateAmount ? 0 : paygRegularAmount
                - regularRebateAmount;
    }

    /**
     * 
     */
    public Integer getInvestmentStrategyCodeID() {
        return investmentStrategyCodeID;
    }

    public void setInvestmentStrategyCodeID(Integer value) {
        if (equals(investmentStrategyCodeID, value))
            return;

        investmentStrategyCodeID = value;
        setModified();
    }

    public String getInvestmentStrategyCodeDesc() {
        return new InvestmentStrategyCode()
                .getCodeDescription(getInvestmentStrategyCodeID());
    }

    public double getAnnualIncomeRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getIncomeRate();
    }

    public double getAnnualCapitalGrowthRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getGrowthRate();
    }

    public double getTotalAnnualReturnRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getRate();
    }

    /***************************************************************************
     * label/legend generators
     */
    private String[] labels;

    public String[] getLabels() {
        int offset = getAge();
        int years = getYearsInt();
        years = years >= 0 ? years : 0;

        if (labels != null && labels.length == years)
            return labels;

        labels = null;
        labels = new String[years];
        for (int i = 0; i < years; i++)
            labels[i] = String.valueOf(i + offset);

        return labels;
    }

    public Integer getActualYears() {
        for (int i = 0; i < requiredIncomeValues.length; i++)
            if (requiredIncomeValues[i] <= 0 || requiredIncomeValues[i] == HOLE)
                return new Integer(getAge() + i);

        return null;
    }

    /***************************************************************************
     * calculations
     */
    public double getTargetValue() {

        // recalc targetValues[]
        targetValue = super.getTargetValue();

        if (targetValue > 0)
            return targetValue;

        // lets get last positive value
        for (int i = 0; i < targetValues.length; i++) {
            if (targetValues[i] > 0 && targetValues[i] != HOLE) {
                targetValue = targetValues[i];
                continue;
            } else
                break;
        }

        return targetValue;

    }

    private double getTargetValue(int index, double thisYearValue,
            double deductibleAmount) {

        double taxableIncome = 0;
        double paygValue = 0;
        double rebateValue = 0;
        double taxesFees = 0;

        double thisYearSpendValue = getRequiredPaymentAmount();

        if (getEntryFeeRate() == UNKNOWN_VALUE)
            return 0;
        if (isIndexed())
            thisYearSpendValue = getCompoundedAmount(thisYearSpendValue, index);

        if (thisYearSpendValue >= thisYearValue) // more than we have
            thisYearSpendValue = thisYearValue < 0 ? 0 : thisYearValue;
        requiredIncomeValues[index] = thisYearSpendValue;

        // deduct money first day of period
        thisYearValue -= thisYearSpendValue;

        // apply growth rate to the rest amount
        thisYearValue *= (100 + getTotalAnnualReturnRate()) / 100;

        // calc this year taxable income, PAYG and rebate
        // per year
        taxableIncome = thisYearSpendValue < deductibleAmount ? 0
                : thisYearSpendValue - deductibleAmount;

        paygValue = TaxUtils.getPAYG(taxableIncome / WEEKS_PER_YEAR)
                * WEEKS_PER_YEAR;

        if (getAge() + index >= TaxUtils.TAX_EFFECTIVE_AGE)
            rebateValue = taxableIncome * (TaxUtils.SUPER_TAX_RATE / 100)
                    * (getRebateableRate() / 100);
        else
            rebateValue = 0;

        // calc revision fees and taxes
        taxesFees = thisYearValue * getEntryFeeRate() / 100 + paygValue
                + rebateValue;

        // deduct taxes and fees
        thisYearValue -= taxesFees;

        return thisYearValue;

    }

    public double[] getTargetValues() {

        if (!isReady())
            return null;
        if (!isModified())
            return targetValues;

        if (getYears() <= 0) {
            double le = getLifeExpectancy();
            if (le == MoneyCalc.UNKNOWN_VALUE)
                return null;

            setYears(le);
        }

        double thisYearValue = getPurchasePrice();
        double deductibleAmount = getATOAnnualDeductibleAmount();

        for (int i = 0; i < targetValues.length; i++) {
            // calc this year indexed income
            if (i > 0)
                thisYearValue = targetValues[i - 1];

            thisYearValue = getTargetValue(i, thisYearValue, deductibleAmount);

            if (thisYearValue <= 0) {
                thisYearValue = 0;
            }
            targetValues[i] = thisYearValue;
        }

        modified = false;
        return targetValues;

    }

    public double[] getRequiredIncomeValues() {
        return requiredIncomeValues;
    }

    /***************************************************************************
     * if requiredPaymentTypeID = NET this member will be used to calculate
     * using recursion optimal GROSS payment amount to satisfy our NET
     * requiredPaymentAmount
     **************************************************************************/

    private static int count = 0;

    private static final int MAX_COUNT = 100;

    private static final int accuracy = 2;

    private static double netRegularAmount;

    // getOptimalRequiredGrossPaymentAmount that match to
    // getNetRegularPaymentAmount()
    // play with getRequiredPaymentAmount()
    private double getOptimalRequiredGrossPaymentAmount() {

        if (getRequiredFrequencyCodeID() == null)
            return 0;

        // if ( !isModified() && gross4net_RequiredPaymentAmount > 0 )
        if (gross4net_RequiredPaymentAmount > 0)
            return gross4net_RequiredPaymentAmount;

        if (count == 0) {
            if (PaymentType.NET.equals(getRequiredPaymentTypeID()))
                netRegularAmount = // getNetRegularPaymentAmount();
                FrequencyCode.getPeriodAmount(getRequiredFrequencyCodeID(),
                        getRequiredPaymentAmount()).doubleValue();
        }

        AllocatedPensionCalc ap = new AllocatedPensionCalc(this);
        ap.setRequiredPaymentTypeID(PaymentType.GROSS);

        double netRegularAmount_calc = ap.getNetRegularPaymentAmount();
        if (count == 0) // first copy
            netRegularAmount_calc *= 1.1; // to disbalance

        // money
        double factor = (netRegularAmount_calc - netRegularAmount)
                / netRegularAmount;

        // BREAK Condition
        if ((count > MAX_COUNT)
                || (Math.round(netRegularAmount_calc * 100) == Math
                        .round(netRegularAmount * 100))) {
            gross4net_RequiredPaymentAmount = ap.getRequiredPaymentAmount();

            // reset this to default
            count = 0;

            return gross4net_RequiredPaymentAmount;
        }

        if (factor > 0) // ap.getNetRegularPaymentAmount() > initial
                        // netRegularAmount
            // too much, lets decrease it
            ap.setRequiredPaymentAmount(this.getRequiredPaymentAmount()
                    / (1 + Math.abs(factor) / accuracy));
        else if (factor < 0) // ap.getNetRegularPaymentAmount() < initial
                                // netRegularAmount
            // too small, lets increase it
            ap.setRequiredPaymentAmount(this.getRequiredPaymentAmount()
                    * (1 + Math.abs(factor) / accuracy));

        return ap.getOptimalRequiredGrossPaymentAmount();

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
        // return new java.math.BigDecimal( getNetRegularPaymentAmount() );
        return null;
    }

    private java.util.Collection getGeneratedExpenses(String desc) {
        // return new java.math.BigDecimal( getPAYGRegularAmount() );
        return null;
    }

    private java.util.Collection getGeneratedLiabilities(String desc) {
        // return new java.math.BigDecimal( getPurchasePrice() );
        return null;
    }

}