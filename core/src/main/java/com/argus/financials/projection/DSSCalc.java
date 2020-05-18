/*
 * DSSCalc.java
 *
 * Created on 17 October 2001, 11:08
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.ModelType;
import com.argus.financials.code.SexCode;
import com.argus.util.DateTimeUtils;

public class DSSCalc extends MoneyCalc {

    private boolean single;

    private boolean homeOwner;

    private int targetAge;

    private IncomeTest incomeTest;

    private AssetTest assetTest;

    /** Creates new DSSCalc */
    public DSSCalc() {
        super();
        _clear();
    }

    public Integer getDefaultModelType() {
        return ModelType.CURRENT_POSITION_CALC;
    }

    public void assign(MoneyCalc obj) {
        super.assign(obj);

        // do not copy data if not the same class
        // if ( !this.getClass().equals( obj.getClass() ) ) return;
        if (!(obj instanceof DSSCalc))
            return;

        single = ((DSSCalc) obj).single;
        homeOwner = ((DSSCalc) obj).homeOwner;
        targetAge = ((DSSCalc) obj).targetAge;

        incomeTest.assign(((DSSCalc) obj).incomeTest);
        assetTest.assign(((DSSCalc) obj).assetTest);

        if (this.getClass().equals(obj.getClass()))
            setModified();

    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        single = true;
        homeOwner = false;

        targetAge = (int) UNKNOWN_VALUE;

        if (incomeTest == null)
            incomeTest = new IncomeTest();
        else
            incomeTest.clear();

        if (assetTest == null)
            assetTest = new AssetTest();
        else
            assetTest.clear();
    }

    public boolean isModified() {
        return super.isModified() || incomeTest.isModified()
                || assetTest.isModified();
    }

    // *
    public void setModified() {
        incomeTest.setModified();
        incomeTest.setModified();

        super.setModified();
    }

    // */
    public boolean isReady() {
        return (getInitialValue() >= 0.)
                && (!isIndexed() || (isIndexed() && getIndexRate() >= 0.))
                && (getYears() >= 0.) && (getTargetAge() >= 0.);
    }

    public static double getPensionQualifyingAge(java.util.Date dateOfBirth,
            Integer sexCodeID) {

        if (dateOfBirth == null || sexCodeID == null)
            return 0;
        // throw new RuntimeException( "DSSCalc.getPensionQualifyingAge(
        // dateOfBirth, sexCodeID == null )" );

        boolean dvaServicePension = false;

        double delta = 0;
        if (dvaServicePension)
            delta = 5;

        if (SexCode.MALE.equals(sexCodeID))
            return 65 - delta;

        // else FEMALE (every 1.5 years increase by 0.5 year)
        if (dateOfBirth.before(DateTimeUtils.getDate("1/7/1935")))
            return 60 - delta;
        else if (dateOfBirth.after(DateTimeUtils.getDate("31/12/1948")))
            return 65 - delta;

        if (dateOfBirth.before(DateTimeUtils.getDate("1/1/1937")))
            return 60.5 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/7/1938")))
            return 61 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/1/1940")))
            return 61.5 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/7/1941")))
            return 62 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/1/1943")))
            return 62.5 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/7/1944")))
            return 63 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/1/1946")))
            return 63.5 - delta;
        else if (dateOfBirth.before(DateTimeUtils.getDate("1/7/1947")))
            return 64 - delta;
        else
            return 64.5 - delta;

    }

    /**
     * get/set
     */
    public boolean getSingle() {
        return single;
    }

    public void setSingle(boolean value) {
        if (single == value)
            return;

        single = value;
        setModified();
    }

    public boolean getHomeOwner() {
        return homeOwner;
    }

    public void setHomeOwner(boolean value) {
        if (homeOwner == value)
            return;

        homeOwner = value;
        setModified();
    }

    public void setYears(int value) {
        // if ( value < 0 ) return;

        incomeTest.setYears(value);
        assetTest.setYears(value);

        super.setYears(value);
    }

    // years to wait before calculation (apply index to ..)
    public int getTargetAge() {
        return targetAge;
    }

    protected void setTargetAge(int value) {
        if (targetAge == value)
            return;

        targetAge = value;
        setModified();
    }

    public void setDateOfBirth(java.util.Date value) {
        incomeTest.setDateOfBirth(value);
        assetTest.setDateOfBirth(value);

        super.setDateOfBirth(value);
    }

    public void setIndexRate(double value) {
        incomeTest.setIndexRate(value);
        assetTest.setIndexRate(value);

        super.setIndexRate(value);
    }

    public void setTaxRate(double value) {
        incomeTest.setTaxRate(value);
        assetTest.setTaxRate(value);

        super.setTaxRate(value);
    }

    public double getMaximumAmount() {
        return single ? TaxUtils.MAX_AGE_PENSION_SINGLE.doubleValue()
                : TaxUtils.MAX_AGE_PENSION_COUPLE.doubleValue();
    }

    public double getTargetValue(int index, double incomeValue,
            double assetValue) {

        if (!isReady())
            return 0;
        if (!isModified())
            return targetValues[index] == HOLE ? 0 : targetValues[index];

        double income = incomeTest.getTargetValue(index, incomeValue);
        double asset = assetTest.getTargetValue(index, assetValue);

        // min only
        targetValues[index] = income > asset ? asset : income;
        return targetValues[index];

    }

    public double[] getTargetValues() {

        if (!isReady())
            return null;
        if (!isModified())
            return targetValues;

        double[] income = incomeTest.getTargetValues();
        double[] asset = assetTest.getTargetValues();

        if (income == null || asset == null) {
            // modified = false;
            return null;
        }

        // min only
        for (int i = 0; i < targetValues.length; i++) {
            targetValues[i] = income[i] > asset[i] ? asset[i] : income[i];
            if (targetValues[i] == 0)
                targetValues[i] = HOLE;
        }

        modified = false;
        return targetValues;

    }

    /**
     * internal classes
     */
    class IncomeTest extends MoneyCalc {

        double[] incomeValues;

        /** Creates new IncomeTest */
        public IncomeTest() {
            _clear();
        }

        public Integer getDefaultModelType() {
            return ModelType.CURRENT_POSITION_CALC;
        }

        public void assign(IncomeTest it) {
            super.assign(it);

            // do not do calc, just copy data
            if (it.incomeValues != null) {
                incomeValues = new double[it.incomeValues.length];
                // System.arraycopy( it.incomeValues, 0, incomeValues, 0,
                // it.incomeValues.length );
            }

            this.modified = true;// it.modified;
        }

        public void clear() {
            super.clear();
            _clear();
        }

        private void _clear() {
            incomeValues = null;
        }

        public boolean isReady() {
            return (!this.isIndexed() || (this.isIndexed() && this
                    .getIndexRate() >= 0.))
                    && (this.getYears() >= 0.);
        }

        public void setYears(int value) {
            if (this.getYears() == value)
                return;

            if (value <= 0)
                incomeValues = null;
            else
                incomeValues = new double[value];

            super.setYears(value);
        }

        // Income Test for pensions
        public double getThresholdAmount() {
            return single ? TaxUtils.MEANS_LIMITS_INCOME_SINGLE.doubleValue()
                    : TaxUtils.MEANS_LIMITS_INCOME_COUPLE.doubleValue();
        }

        // Income over these amounts reduces the rate of pension payable by 40
        // cents in the
        // dollar (single), 20 cents in the dollar each (couple).
        public double getFactor() {
            return single ? TaxUtils.MEANS_LIMITS_INCOME_SINGLE_FACTOR
                    .doubleValue() : TaxUtils.MEANS_LIMITS_INCOME_COUPLE_FACTOR
                    .doubleValue();
        }

        public double getTargetValue(int index, double incomeValue) {
            if (!this.isReady())
                return 0;
            if (!this.isModified() && incomeValues[index] == incomeValue)
                return this.targetValues[index];

            incomeValues[index] = incomeValue;

            double thresholdAmount = this.getCompoundedAmount(
                    getThresholdAmount(), getTargetAge() + index);
            double maximumAmount = this.getCompoundedAmount(getMaximumAmount(),
                    getTargetAge() + index);

            // per fortnight
            double deductibleAmount = (incomeValue / FORTNIGHTS_PER_YEAR - thresholdAmount)
                    * getFactor();
            if (deductibleAmount < 0)
                deductibleAmount = 0;

            double thisYearFNValue = maximumAmount - deductibleAmount;
            if (thisYearFNValue < 0)
                thisYearFNValue = 0;

            // per year
            this.targetValues[index] = thisYearFNValue * FORTNIGHTS_PER_YEAR;

            return this.targetValues[index];
        }

        // have to set incomeValues first
        protected double[] getTargetValues() {
            if (!this.isReady())
                return null;
            if (!this.isModified())
                return this.targetValues;

            // for ( int i = 0; i < this.getYears(); i++ )
            // this.targetValues[i] = this.getTargetValue( i,
            // this.incomeValues[i - 1] );

            this.modified = false;
            return this.targetValues;
        }

    }

    class AssetTest extends MoneyCalc {

        double[] assetValues;

        /** Creates new AssetTest */
        public AssetTest() {
            _clear();
        }

        public Integer getDefaultModelType() {
            return ModelType.CURRENT_POSITION_CALC;
        }

        public void assign(AssetTest at) {
            super.assign(at);

            // do not do calc, just copy data
            if (at.assetValues != null) {
                assetValues = new double[at.assetValues.length];
                // System.arraycopy( at.assetValues, 0, assetValues, 0,
                // at.assetValues.length );
            }

            this.modified = true;// at.modified;
        }

        public void clear() {
            super.clear();
            _clear();
        }

        private void _clear() {
            assetValues = null;
        }

        public boolean isReady() {
            return (!this.isIndexed() || (this.isIndexed() && this
                    .getIndexRate() >= 0.))
                    && (this.getYears() >= 0.);
        }

        public void setYears(int value) {
            if (this.getYears() == value)
                return;

            if (value <= 0)
                assetValues = null;
            else
                assetValues = new double[value];

            super.setYears(value);
        }

        public double getThresholdAmount() {
            return single ? (homeOwner ? TaxUtils.MEANS_LIMITS_ASSETS_HO_SINGLE
                    .doubleValue() : TaxUtils.MEANS_LIMITS_ASSETS_NH_SINGLE
                    .doubleValue())
                    : (homeOwner ? TaxUtils.MEANS_LIMITS_ASSETS_HO_COUPLE
                            .doubleValue()
                            : TaxUtils.MEANS_LIMITS_ASSETS_NH_COUPLE
                                    .doubleValue());
        }

        public double getTargetValue(int index, double assetValue) {
            if (!this.isReady())
                return 0;
            if (!this.isModified() && assetValues[index] == assetValue)
                return this.targetValues[index];

            assetValues[index] = assetValue;

            double thresholdAmount = this.getCompoundedAmount(
                    getThresholdAmount(), getTargetAge() + index);
            double maximumAmount = this.getCompoundedAmount(getMaximumAmount(),
                    getTargetAge() + index);

            double deductibleAmount = assetValue - thresholdAmount;
            if (deductibleAmount < 0)
                deductibleAmount = 0;

            double thisYearFNValue = maximumAmount - (deductibleAmount / 1000)
                    * TaxUtils.MEANS_LIMITS_ASSETS_FACTOR.doubleValue();
            if (thisYearFNValue < 0)
                thisYearFNValue = 0;

            // per year
            this.targetValues[index] = thisYearFNValue * FORTNIGHTS_PER_YEAR;

            return this.targetValues[index];
        }

        // have to set assetValues first
        protected double[] getTargetValues() {
            if (!this.isReady())
                return null;
            if (!this.isModified())
                return this.targetValues;

            // for ( int i = 0; i < this.getYears(); i++ )
            // this.targetValues[i] = this.getTargetValue( i, this.assetValues[i
            // - 1] );

            this.modified = false;
            return this.targetValues;
        }

    }

}
