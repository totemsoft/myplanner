/*
 * AssetManipulation.java
 *
 * Created on September 17, 2001, 8:48 AM
 */

package au.com.totemsoft.myplanner.projection;

import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.myplanner.code.InvestmentStrategyCode;
import au.com.totemsoft.myplanner.code.ModelType;

public abstract class AssetCalc extends MoneyCalc implements FinancialClassID {

    // input params
    private int targetAge;

    private double delta; // additions or spend amount ($ p.a.)

    private Integer investmentStrategyCodeID;

    private double entryFeeRate; // Entry Fees

    private double revisionFeeRate; // Management Fees

    protected AssetCalc() {
        _clear();
    }

    public Integer getDefaultModelType() {
        return ModelType.CURRENT_POSITION_CALC;
    }

    public void assign(MoneyCalc mc) {
        super.assign(mc);

        targetAge = ((AssetCalc) mc).targetAge;

        delta = ((AssetCalc) mc).delta;
        investmentStrategyCodeID = ((AssetCalc) mc).investmentStrategyCodeID;
        entryFeeRate = ((AssetCalc) mc).entryFeeRate;
        revisionFeeRate = ((AssetCalc) mc).revisionFeeRate;
    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        targetAge = (int) UNKNOWN_VALUE;

        delta = 0.;
        investmentStrategyCodeID = null;
        entryFeeRate = 0.;
        revisionFeeRate = 0.;
    }

    public boolean isReady() {
        return (getInitialValue() >= 0.)
                && (!isIndexed() || (isIndexed() && getIndexRate() >= 0.))
                && (getYears() >= 0.) && (getDelta() >= 0.)
                && (getInvestmentStrategyCodeID() != null);
    }

    /**
     * 
     */
    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = 0.;// UNKNOWN_VALUE;
        int n = (int) UNKNOWN_VALUE;

        if (property.equals(TARGET_AGE)) {
            if (value != null && value.length() > 0)
                n = (int) getNumberInstance().doubleValue(value);
            setTargetAge(n);

        } else if (property.equals(DELTA)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setDelta(d);

        } else if (property.equals(ENTRY_FEES)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setEntryFeeRate(d);

        } else if (property.equals(INV_STRATEGY)) {
            if (getNumberInstance().isValid(value))
                setInvestmentStrategyCodeID(new Integer(value));
            else
                setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                        .getCodeID(value));

        } else if (property.equals(REVIEW_FEES)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setRevisionFeeRate(d);

        } else
            return false;

        return true;

    }

    public String toString() {
        return super.toString() + '\n' + TARGET_AGE + '=' + getTargetAge()
                + '\n' + DELTA + '=' + getDelta() + '\n' + ENTRY_FEES + '='
                + getEntryFeeRate() + '\n' + INV_STRATEGY + '='
                + getInvestmentStrategyCodeID() + '\n' + REVIEW_FEES + '='
                + getRevisionFeeRate();
    }

    /**
     * get/set
     */
    public int getTargetAge() {
        return targetAge;
    }

    public void setTargetAge(int value) {
        if (targetAge == value)
            return;

        targetAge = value;
        setModified();
    }

    public java.util.Date getTargetDate() {
        if (getDateOfBirth() == null || getTargetAge() < 0)
            return null;

        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        calendar.setTime(getDateOfBirth());
        calendar.add(java.util.Calendar.YEAR, getTargetAge());
        return calendar.getTime();
    }

    protected double getDelta() {
        return delta;
    }

    protected void setDelta(double value) {
        if (this.delta == value)
            return;

        delta = value;
        setModified();
    }

    public double getEntryFeeRate() {
        return entryFeeRate;
    }

    public void setEntryFeeRate(double value) {
        if (this.entryFeeRate == value)
            return;

        entryFeeRate = value;
        setModified();
    }

    public double getRevisionFeeRate() {
        return revisionFeeRate;
    }

    public void setRevisionFeeRate(double value) {
        if (this.revisionFeeRate == value)
            return;

        revisionFeeRate = value;
        setModified();
    }

    //
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

    public double getIncomeRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getIncomeRate();
    }

    // same capital growth rate 0..years-1
    public double getGrowthRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getGrowthRate();
    }

    public double getTotalReturnRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getRate();
    }

}
