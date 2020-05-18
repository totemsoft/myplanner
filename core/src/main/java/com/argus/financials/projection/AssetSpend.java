/*
 * AssetSpend.java
 *
 * Created on September 17, 2001, 8:47 AM
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @version
 * 
 * Allocated Pension model
 * 
 */

import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.etc.BadArgumentException;
import com.argus.financials.projection.data.LifeExpectancy;
import com.argus.util.DateTimeUtils;

public final class AssetSpend extends AssetCalc {

    private Integer partnerSexCodeID;

    private java.util.Date partnerDateOfBirth;

    private double partnerTaxRate;

    private int partnerTargetAge;

    private double partSpendValue;

    private double uppATO;

    private double uppDSS;

    private double rebateRate;

    private double residual;

    private boolean includeDSS;

    private int yearsWait;

    // calculated
    private double[] requiredIncomeValues;

    private double actualIncome;

    private double actualYears;

    private double optimalInitialValue;

    // used in calculations (save data only)
    private AssetSpend copyObject;

    private DSSCalc dssCalc;

    public AssetSpend() {
        _clear();
    }

    public void assign(MoneyCalc mc) {
        dssCalc.assign(((AssetSpend) mc).dssCalc);

        super.assign(mc);

        // do not copy data if not the same class
        if (!this.getClass().equals(mc.getClass()))
            return;

        partnerSexCodeID = ((AssetSpend) mc).partnerSexCodeID;
        partnerDateOfBirth = ((AssetSpend) mc).partnerDateOfBirth;
        partnerTaxRate = ((AssetSpend) mc).partnerTaxRate;
        partnerTargetAge = ((AssetSpend) mc).partnerTargetAge;

        partSpendValue = ((AssetSpend) mc).partSpendValue;

        uppATO = ((AssetSpend) mc).uppATO;
        uppDSS = ((AssetSpend) mc).uppDSS;
        rebateRate = ((AssetSpend) mc).rebateRate;
        residual = ((AssetSpend) mc).residual;
        includeDSS = ((AssetSpend) mc).includeDSS;
        yearsWait = ((AssetSpend) mc).yearsWait;

        // do not do calc, just copy data
        if (((AssetSpend) mc).requiredIncomeValues != null) {
            requiredIncomeValues = new double[((AssetSpend) mc).requiredIncomeValues.length];
            System.arraycopy(((AssetSpend) mc).requiredIncomeValues, 0,
                    requiredIncomeValues, 0,
                    ((AssetSpend) mc).requiredIncomeValues.length);

            actualIncome = ((AssetSpend) mc).actualIncome;
            actualYears = ((AssetSpend) mc).actualYears;
            optimalInitialValue = ((AssetSpend) mc).optimalInitialValue;
        }

        setModified();
    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        partnerSexCodeID = null;
        partnerDateOfBirth = null;
        partnerTaxRate = 0.;
        partnerTargetAge = (int) UNKNOWN_VALUE;

        partSpendValue = 0.;

        uppATO = 0.;
        uppDSS = 0.;
        rebateRate = 0.;
        residual = 0.;
        includeDSS = false;
        yearsWait = (int) UNKNOWN_VALUE;

        // calculated
        requiredIncomeValues = null;
        actualIncome = UNKNOWN_VALUE;
        actualYears = UNKNOWN_VALUE;
        optimalInitialValue = UNKNOWN_VALUE;

        // used in calculations (save data only)
        copyObject = null;

        if (dssCalc == null)
            dssCalc = new DSSCalc();
        else
            dssCalc.clear();
    }

    /***************************************************************************
     * javax.swing.text.Document
     */
    public boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = 0.;// UNKNOWN_VALUE;
        int n = (int) UNKNOWN_VALUE;

        /* OLD-VERSION: no code replaced */
        // BEGIN: BUG FIX 614 - 12/07/2002
        // by shibaevv
        // The value for "Single" was never set, so the SnapEntryView always
        // displayed the "Single" radiobutton as selected!!!
        if (property.equals(CALCULATION_TYPE_SINGLE)) {
            boolean helper = value.toUpperCase().equals("TRUE") ? true : false;
            this.setSingle(helper);
        } else
        // END: BUG FIX 614 - 12/07/2002

        if (property.equals(DOB_PARTNER)) {
            setPartnerDateOfBirth(DateTimeUtils.getDate(value));

        } else if (property.equals(PARTNER_SEX_CODE)) {
            if (getNumberInstance().isValid(value))
                setPartnerSexCodeID(new Integer(value));
            else
                setPartnerSexCodeID(new SexCode().getCodeID(value));

        } else if (property.equals(PARTNER_SEX_CODE_FEMALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setPartnerSexCodeID(SexCode.FEMALE);

        } else if (property.equals(PARTNER_SEX_CODE_MALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setPartnerSexCodeID(SexCode.MALE);

        } else if (property.equals(TARGET_AGE_PARTNER)) {
            if (value != null && value.length() > 0)
                n = (int) getNumberInstance().doubleValue(value);
            setPartnerTargetAge(n);

        } else if (property.equals(TAX_RATE_PARTNER)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setPartnerTaxRate(d);

        } else if (property.equals(INCLUDE_DSS)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setIncludeDSS(true);
            else
                setIncludeDSS(false);

        } else if (property.equals(HOME_OWNER)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setHomeOwner(true);
            else
                setHomeOwner(false);

        } else if (property.equals(YEARS_PROJECT_RETIRE_INCOME)) {
            if (value != null && value.length() > 0)
                n = (int) getNumberInstance().doubleValue(value);
            setYears(n);

        } else if (property.equals(REQUIRED_INCOME_PART)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setPartSpendValue(d);

        } else if (property.equals(REQUIRED_INCOME_FULL)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setSpendValue(d);

        } else if (property.equals(ATO_UPP)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setUndeductedPurchasePriceATO(d);

        } else if (property.equals(DSS_UPP)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setUndeductedPurchasePriceDSS(d);

        } else if (property.equals(REBATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setRebateRate(d);

        } else if (property.equals(REQUIRED_RESIDUAL)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setResidualValue(d);

        } else if (property.equals(INV_STRATEGY_AS)) {
            if (getNumberInstance().isValid(value))
                setInvestmentStrategyCodeID(new Integer(value));
            else
                setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                        .getCodeID(value));

        } else
            return false;

        return true;

    }

    public String toString() {
        return super.toString() + '\n' + PARTNER_SEX_CODE + '='
                + getPartnerSexCodeID() + '\n' + DOB_PARTNER + '='
                + DateTimeUtils.getTimeInMillis(getPartnerDateOfBirth()) + '\n'
                + TAX_RATE_PARTNER + '=' + getPartnerTaxRate() + '\n'
                + TARGET_AGE_PARTNER + '=' + getPartnerTargetAge() + '\n' +

                REQUIRED_INCOME_PART + getPartSpendValue() + '\n' +

                ATO_UPP + '=' + getUndeductedPurchasePriceATO() + '\n'
                + DSS_UPP + '=' + getUndeductedPurchasePriceDSS() + '\n'
                + REBATE + '=' + getRebateRate() + '\n' + REQUIRED_RESIDUAL
                + '=' + getResidualValue() + '\n' + INCLUDE_DSS + '='
                + getIncludeDSS() + '\n' + YEARS_PROJECT_RETIRE_INCOME + '='
                + getYears();
    }

    // public DSSCalc getDSSCalc() { return dssCalc; }

    public void setModified() {
        dssCalc.setModified();
        super.setModified();
    }

    public boolean isModified() {
        return (super.isModified() || dssCalc.isModified());
    }

    public boolean isReady() {
        return super.isReady() && (getRevisionFeeRate() >= 0.)
                && (getAge() > 0.) && (getTargetAge() > 0.)
                && (getSexCodeID() != null)
                && (getUndeductedPurchasePriceATO() >= 0.)
                && (getUndeductedPurchasePriceDSS() >= 0.)
                && (getRebateRate() >= 0.) && (getResidualValue() >= 0.);
    }

    public int getPartnerAge() {
        return getAge(getPartnerDateOfBirth());
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

    public java.util.Date getPartnerDateOfBirth() {
        return partnerDateOfBirth;
    }

    public void setPartnerDateOfBirth(java.util.Date value) {
        if (equals(partnerDateOfBirth, value))
            return;

        partnerDateOfBirth = value;
        setModified();
    }

    public double getPartnerTaxRate() {
        return partnerTaxRate;
    }

    public void setPartnerTaxRate(double value) {
        if (this.partnerTaxRate == value)
            return;

        partnerTaxRate = value;
        setModified();
    }

    public void setIndexRate(double value) {
        dssCalc.setIndexRate(value);
        super.setIndexRate(value);
    }

    public boolean getIncludeDSS() {
        return includeDSS;
    }

    public void setIncludeDSS(boolean value) {
        if (includeDSS == value)
            return;

        includeDSS = value;
        setModified();
    }

    public double getResidualValue() {
        return residual;
    }

    public void setResidualValue(double value) {
        if (residual == value)
            return;

        residual = value;
        dssCalc.setModified(); // to change modified = true

        setModified();
    }

    public double getPartSpendValue() {
        return partSpendValue;
    }

    public void setPartSpendValue(double value) {
        if (partSpendValue == value)
            return;

        partSpendValue = value;
        setModified();
    }

    public double getSpendValue() {
        return getDelta();
    }

    public void setSpendValue(double value) {
        if (getSpendValue() == value)
            return;

        dssCalc.setModified(); // to change modified = true
        super.setDelta(value);
    }

    public void setYears(int value) {
        if (getYears() == value)
            return;

        if (value > 0)
            requiredIncomeValues = new double[value];
        else
            requiredIncomeValues = null;

        dssCalc.setYears(value);
        super.setYears(value);
    }

    public void setTargetAge(int value) {
        if (getTargetAge() == value)
            return;

        dssCalc.setTargetAge(value); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        super.setTargetAge(value);
    }

    public int getPartnerTargetAge() {
        return partnerTargetAge;
    }

    public void setPartnerTargetAge(int value) {
        if (partnerTargetAge == value)
            return;

        partnerTargetAge = value;
        // dssCalc.setTargetAge( value ); //
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        setModified();
    }

    public java.util.Date getPartnerTargetDate() {
        if (getPartnerDateOfBirth() == null || getPartnerTargetAge() < 0)
            return null;

        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        calendar.setTime(getPartnerDateOfBirth());
        calendar.add(java.util.Calendar.YEAR, getPartnerTargetAge());
        return calendar.getTime();
    }

    public double getPensionQualifyingAge() {
        if (getSexCodeID() == null)
            return BadArgumentException.BAD_DOUBLE_VALUE;

        return DSSCalc
                .getPensionQualifyingAge(getDateOfBirth(), getSexCodeID());
    }

    public void setSexCodeID(Integer value) {
        if (equals(getSexCodeID(), value))
            return;

        dssCalc.setModified(); // to change modified = true
        super.setSexCodeID(value);
    }

    public double getUndeductedPurchasePriceATO() {
        return uppATO;
    }

    public void setUndeductedPurchasePriceATO(double value) {
        if (this.uppATO == value)
            return;

        uppATO = value;
        dssCalc.setModified(); // to change modified = true

        setModified();
    }

    public double getUndeductedPurchasePriceDSS() {
        return uppDSS;
    }

    public void setUndeductedPurchasePriceDSS(double value) {
        if (this.uppDSS == value)
            return;

        uppDSS = value;
        dssCalc.setModified(); // to change modified = true

        setModified();
    }

    public double getLifeExpectancy() {
        double le = LifeExpectancy.getValue(getAge(), getSexCodeID());

        if (getPartnerAge() > 0 && getPartnerSexCodeID() != null)
            le = Math.max(le, LifeExpectancy.getValue(getPartnerAge(),
                    getPartnerSexCodeID()));

        return le;
    }

    // Deductible Amount
    private double getDeductibleAmount(double upp) {
        return upp / getLifeExpectancy();
    }

    public double getDeductibleAmountATO() {
        return getDeductibleAmount(getUndeductedPurchasePriceATO());
    }

    public double getDeductibleAmountDSS() {
        return getDeductibleAmount(getUndeductedPurchasePriceDSS());
    }

    public double getRebateRate() {
        return rebateRate;
    }

    public void setRebateRate(double value) {
        if (this.rebateRate == value)
            return;

        rebateRate = value;
        dssCalc.setModified(); // to change modified = true

        setModified();
    }

    public boolean getSingle() {
        return dssCalc.getSingle();
    }

    public void setSingle(boolean value) {
        if (getSingle() == value)
            return;

        dssCalc.setSingle(value);

        setModified();
    }

    public boolean getHomeOwner() {
        return dssCalc.getHomeOwner();
    }

    public void setHomeOwner(boolean value) {
        if (getHomeOwner() == value)
            return;

        dssCalc.setHomeOwner(value);

        setModified();
    }

    /**
     * calculations
     */
    public double getTargetValue() {

        if (targetValues == null)
            return 0.;

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

        double taxableIncome = 0.;
        double paygValue = 0.;
        double rebateValue = 0.;
        double taxesFees = 0.;

        double thisYearSpendValue = getSpendValue();
        if (isIndexed())
            thisYearSpendValue = getCompoundedAmount(thisYearSpendValue,
                    getTargetAge() - getAge() + index);

        if (thisYearSpendValue >= thisYearValue) // more than we have
            thisYearSpendValue = thisYearValue < 0. ? 0. : thisYearValue;
        requiredIncomeValues[index] = thisYearSpendValue;

        // deduct money first day of period
        thisYearValue -= thisYearSpendValue;

        // apply growth rate to the rest amount
        thisYearValue *= (100. + getTotalReturnRate()) / 100.;

        // calc this year taxable income, PAYG and rebate
        // per year
        taxableIncome = thisYearSpendValue < deductibleAmount ? 0.
                : thisYearSpendValue - deductibleAmount;

        paygValue = TaxUtils.getPAYG(taxableIncome / WEEKS_PER_YEAR)
                * WEEKS_PER_YEAR;

        if (getAge() + index >= TaxUtils.TAX_EFFECTIVE_AGE)
            rebateValue = taxableIncome * (TaxUtils.SUPER_TAX_RATE / 100.)
                    * (getRebateRate() / 100.);
        else
            rebateValue = 0.;

        // calc revision fees and taxes
        taxesFees = thisYearValue * getRevisionFeeRate() / 100.
                + (paygValue - rebateValue);

        // deduct taxes and fees
        thisYearValue -= taxesFees;

        return thisYearValue;

    }

    public double[] getDSSTargetValues() {
        getTargetValues(); // refresh
        return dssCalc.getTargetValues();
    }

    public double getActualYears() {
        return actualYears;
    }

    public double[] getTargetValues() {

        if (!isReady())
            return null;
        if (!isModified())
            return targetValues;

        double thisYearValue = getInitialValue();
        double deductibleAmount = getDeductibleAmountATO();
        actualYears = -1;

        // copy only common base class properties
        if (includeDSS)
            dssCalc.assign((MoneyCalc) this);

        for (int i = 0; i < getYears(); i++) {
            // calc this year indexed income
            if (i > 0) {
                thisYearValue = targetValues[i - 1];
                // thisYearSpendValue *= ( 100 + getIndexRate() ) / 100;
            }

            thisYearValue = getTargetValue(i, thisYearValue, deductibleAmount);

            if (includeDSS && getTargetAge() + i > getPensionQualifyingAge()) {
                // !!! NOTE (KM/VSH) !!!
                // dss amount may decrease within the projection,
                // as the dssDA is not indexed inline with required income
                double dssEntitlement = dssCalc.getTargetValue(i,
                        requiredIncomeValues[i] - getDeductibleAmountDSS(),
                        thisYearValue);

                // increase thisYearValue for dssEntitlement for this year
                thisYearValue = i == 0 ? getInitialValue()
                        : targetValues[i - 1];

                // recalc thisYearValue
                thisYearValue = getTargetValue(i, thisYearValue
                        + dssEntitlement, deductibleAmount);
            }

            if (thisYearValue - residual <= 0) {
                thisYearValue = 0;
                if (actualYears < 0)
                    actualYears = i; // found
            }
            targetValues[i] = thisYearValue;
        }

        actualIncome = 0;
        if (actualYears < 0)
            actualYears = getYears();

        if (actualYears == getYears()) {
            // last year values
            double lastYearSpendValue = getCompoundedAmount(getSpendValue(),
                    getTargetAge() - getAge() + getYears());
            if (thisYearValue - residual > lastYearSpendValue)
                actualYears += (thisYearValue - residual) / lastYearSpendValue;
        }

        modified = false;
        return targetValues;

    }

    public double[] getRequiredIncomeValues() {
        return requiredIncomeValues;
    }

    /**
     * calculations 2
     */
    private static int count = 0;

    private static final int MAX_COUNT = 100;

    private static final int accuracy = 2;

    public double getActualIncome(boolean refresh) {
        if (refresh && isModified()) {
            if (copyObject == null)
                copyObject = new AssetSpend();
            copyObject.assign(this);
            double d = 0;
            try {
                d = copyObject.getActualIncome();
            } finally {
                actualIncome = d;
            }
        }
        return actualIncome;
    }

    private double getActualIncome() {

        // return this.getSpendValue();

        // *
        getTargetValue();

        // time
        double years = getYears();
        double spendValue = getSpendValue();
        double factor = (actualYears - years) / years;

        System.out.println(++count + "). factor=" + factor + " (actual years: "
                + actualYears + ")");
        System.out.println("\tSpend Value=" + spendValue);

        // BREAK Condition
        if (actualYears == 0. || spendValue < 1. || (count > MAX_COUNT)
                || (Math.round(actualYears) == years))
        // || ( Math.abs( factor ) < 0.01 ) ) // time +/- 1%
        {
            actualIncome = spendValue;
            // round to nearest 10
            // actualIncome = Math.round( actualIncome / 10 ) * 10;

            count = 0;

            // modified = false;
            // setModified( false );

            // round to nearest 10
            return actualIncome;
        }

        if (factor > 0) // actualYears > getYears()
            // too small, lets increase it
            spendValue *= (1 + Math.abs(factor) / accuracy);
        else if (factor < 0) // actualYears < getYears()
            // too much, lets decrease it
            spendValue /= (1 + Math.abs(factor) / accuracy);

        setSpendValue(spendValue);

        return getActualIncome();
        // */
    }

    public double getOptimalInitialValue(boolean refresh) {
        if (refresh) {
            if (copyObject == null)
                copyObject = new AssetSpend();
            copyObject.assign(this);
            double d = 0;
            try {
                d = copyObject.getOptimalInitialValue();
            } finally {
                optimalInitialValue = d;
            }
        }
        return optimalInitialValue;
    }

    private double getOptimalInitialValue() {

        getTargetValue();

        // time
        double years = getYears();
        double factor = (actualYears - years) / years;

        System.out.println(++count + "). factor=" + factor + " (actual years: "
                + actualYears + ")");
        System.out.println("\tInitial Value=" + getInitialValue());

        // BREAK Condition
        if ((count > MAX_COUNT) || (Math.round(actualYears) == years))
        // || ( Math.abs( factor ) < 0.01 ) ) // time +/- 1%
        {
            optimalInitialValue = getInitialValue();
            // round to nearest 10
            // optimalInitialValue = Math.round( optimalInitialValue / 10 ) *
            // 10;

            count = 0;

            // round to nearest 10
            return optimalInitialValue;
        }

        if (factor > 0) // actualYears > getYears()
            // too much, lets decrease it
            setInitialValue(getInitialValue()
                    / (1 + Math.abs(factor) / (accuracy * 1)));
        else if (factor < 0) // actualYears < getYears()
            // too small, lets increase it
            setInitialValue(getInitialValue()
                    * (1 + Math.abs(factor) / (accuracy * 1)));

        return getOptimalInitialValue();

    }

}
