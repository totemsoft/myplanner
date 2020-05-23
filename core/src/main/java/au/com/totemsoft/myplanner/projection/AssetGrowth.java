/*
 * AssetGrowth.java
 *
 * Created on 12 September 2001, 17:37
 */

package au.com.totemsoft.myplanner.projection;

import au.com.totemsoft.myplanner.code.AssetCode;
import au.com.totemsoft.myplanner.code.InvestmentStrategyCode;
import au.com.totemsoft.myplanner.code.OwnerCode;

public class AssetGrowth extends AssetCalc {

    private Integer assetCodeID;

    private Integer ownerCodeID;

    private boolean surcharge;

    private double incomeRate;

    // calculation value
    private double optimalContributions;

    protected AssetGrowth() {
        this(null);
    }

    public AssetGrowth(Integer assetCodeID) {
        _clear();
        this.assetCodeID = assetCodeID;
    }

    /**
     * 
     */
    public void assign(MoneyCalc mc) {
        super.assign(mc);

        assetCodeID = ((AssetGrowth) mc).assetCodeID;
        ownerCodeID = ((AssetGrowth) mc).ownerCodeID;
        surcharge = ((AssetGrowth) mc).surcharge;
        incomeRate = ((AssetGrowth) mc).incomeRate;

    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        // assetCodeID = null; // do not clear
        ownerCodeID = OwnerCode.CLIENT;
        surcharge = false;
        incomeRate = 0.;

    }

    /**
     * 
     */
    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = 0.;// UNKNOWN_VALUE;

        if (property.equals(ASSET_CURRENT_VALUE)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setInitialValue(d);

        } else if (property.equals(ASSET_OWNER)) {
            if (getNumberInstance().isValid(value))
                setOwnerCodeID(new Integer(value));
            else
                setOwnerCodeID(new OwnerCode().getCodeID(value));

        } else if (property.equals(ASSET_CONTRIBUTION)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setAddValue(d);

        } else if (property.equals(ASSET_INCOME_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setIncomeRate(d);

        } else if (property.equals(INV_STRATEGY_AG)) {
            if (getNumberInstance().isValid(value))
                setInvestmentStrategyCodeID(new Integer(value));
            else
                setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                        .getCodeID(value));

        } else if (property.equals(SUR_CHARGE)) {
            setSurcharge(new Boolean(value).booleanValue());

        } else
            return false;

        return true;

    }

    public String toString() {
        return super.toString() + '\n' + SUR_CHARGE + '=' + getSurcharge()
                + '\n' + ASSET_CODE_ID + '=' + getAssetCode() + '\n'
                + ASSET_OWNER + '=' + getOwnerCodeID() + '\n'
                + ASSET_INCOME_RATE + '=' + getIncomeRate();
    }

    /**
     * 
     */
    private boolean _isReady() {
        return super.isReady() && (getAssetCode() != null)
                && (getTaxRate() >= 0.) && (getEntryFeeRate() >= 0.)
                && (getRevisionFeeRate() >= 0.);
    }

    public boolean isReady() {
        return _isReady();
    }

    public boolean getSaved() {
        System.out.println(super.getSaved());
        return super.getSaved();
    }

    /**
     * 
     */
    public void setDateOfBirth(java.util.Date value) {
        if (equals(getDateOfBirth(), value))
            return;

        super.setDateOfBirth(value);
        updateYears();
    }

    private void updateYears() {
        int age = getAge();
        int value = getTargetAge();

        if (age > 0 && value > 0) {
            if (value == age)
                setYears(0);
            else if (value > age)
                setYears(value - age + 1);
            else
                return;
        }
    }

    public void setTargetAge(int value) {
        if (getTargetAge() == value)
            return;

        super.setTargetAge(value);
        updateYears();
    }

    public Integer getAssetCode() {
        return assetCodeID;
    }

    protected void setAssetCode(Integer value) {
        if (equals(assetCodeID, value))
            return;

        assetCodeID = value;
        setModified();
    }

    public String getAssetCodeDesc() {
        return new AssetCode().getCodeDescription(getAssetCode());
    }

    public Integer getOwnerCodeID() {
        return ownerCodeID;
    }

    public void setOwnerCodeID(Integer value) {
        if (equals(ownerCodeID, value))
            return;

        ownerCodeID = value;
        setModified();
    }

    public double getAddValue() {
        return getDelta();
    }

    public void setAddValue(double addValue) {
        setDelta(addValue);
    }

    public boolean getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(boolean value) {
        if (!ASSET_SUPERANNUATION.equals(getAssetCode())) {
            return;
        }

        if (surcharge == value)
            return;

        surcharge = value;
        setModified();
    }

    public double getIncomeRate() {
        if (ASSET_CASH.equals(assetCodeID))
            return incomeRate;
        else
            return super.getIncomeRate();
    }

    public void setIncomeRate(double value) {
        if (incomeRate == value)
            return;

        incomeRate = value;
        setModified();
    }

    public double getGrowthRate() {
        if (ASSET_CASH.equals(assetCodeID))
            return 0.;
        return super.getGrowthRate();
    }

    public double getTotalReturnRate() {
        if (ASSET_CASH.equals(assetCodeID))
            return incomeRate;
        else
            return super.getTotalReturnRate();
    }

    public void setTaxRate(double value) {
        double taxRate = super.getTaxRate();
        if (taxRate == UNKNOWN_VALUE
                && ASSET_SUPERANNUATION.equals(assetCodeID)) {
            taxRate = TaxUtils.SUPER_TAX_RATE;
            super.setTaxRate(TaxUtils.SUPER_TAX_RATE);
        } else
            super.setTaxRate(value);
    }

    public double[] getTargetValues() {

        // if ( !isReady() ) return null;
        // if ( !isModified() ) return targetValues;

        if (!_isReady())
            return null;
        if (!modified)
            return targetValues;

        double thisYearValue = getInitialValue();
        double thisYearAddValue = getAddValue();
        double taxesFees = 0;
        double income = 0;

        for (int i = 0; i < getYearsInt(); i++) {

            if (i == 0) {

            } else {
                thisYearValue = targetValues[i - 1];
                if (isIndexed())
                    thisYearAddValue *= (100 + getIndexRate()) / 100;
            }

            // income for opening balance
            income = thisYearValue * getIncomeRate() / 100;

            // only 50% on contributions for the first (??? any ???) year
            // if ( i == 0 )
            income += thisYearAddValue * getIncomeRate() / 200;

            if (ASSET_SUPERANNUATION.equals(assetCodeID)) {
                // only 50% on contributions for the first (??? any ???) year
                // if ( i == 0 )
                income += thisYearAddValue * getGrowthRate() / 200;

                taxesFees = (thisYearAddValue + income)
                        * TaxUtils.SUPER_TAX_RATE / 100 + thisYearAddValue
                        * getEntryFeeRate() / 100 + thisYearValue
                        * getRevisionFeeRate() / 100;

                if (surcharge)
                    taxesFees += thisYearAddValue * TaxUtils.SUPER_TAX_RATE
                            / 100;

                thisYearValue += thisYearAddValue + income + thisYearValue
                        * getGrowthRate() / 100;
            } else if (ASSET_CASH.equals(assetCodeID)) {
                taxesFees = income * getTaxRate() / 100;

                thisYearValue += thisYearAddValue + income;
            } else if (ASSET_INVESTMENT.equals(assetCodeID)) {
                // only 50% on contributions for the first (??? any ???) year
                // if ( i == 0 )
                income += thisYearAddValue * getGrowthRate() / 200;

                taxesFees = income * getTaxRate() / 100 + thisYearAddValue
                        * getEntryFeeRate() / 100 + thisYearValue
                        * getRevisionFeeRate() / 100;

                thisYearValue += thisYearAddValue + income + thisYearValue
                        * getGrowthRate() / 100;
            } else {
                System.err
                        .println("AssetGrowth::getTargetValues() Invalid code: "
                                + assetCodeID);
                return null;
            }

            // deduct taxes and fees
            targetValues[i] = thisYearValue - taxesFees;

        }

        modified = false;
        return targetValues;

    }

    /**
     * calculations
     */
    private static int count = 0;

    private static final int MAX_COUNT = 200;

    private static final int accuracy = 2;

    public double getOptimalContributions(boolean refresh,
            double optimalTargetValue) {
        if (refresh) {
            AssetGrowth copyObject = new AssetGrowth();
            copyObject.assign(this);
            double d = 0;
            try {
                d = copyObject.getOptimalContributions(optimalTargetValue);
            } finally {
                optimalContributions = d;
            }
        }
        return optimalContributions;
    }

    private double getOptimalContributions(double optimalTargetValue) {

        // if ( !isModified() && optimalContributions > 0 )
        if (optimalContributions > 0)
            return optimalContributions;

        super.getTargetValue();

        if (count == 0)
            optimalContributions = 0;

        // money
        double targetValue = getTargetValue();
        double factor = (optimalTargetValue - targetValue) / targetValue;
        // double factor = ( optimalTargetValue - targetValue ) /
        // optimalTargetValue;

        // BREAK Condition
        if ((count > MAX_COUNT)
                || (Math.round(optimalTargetValue * 100) == Math
                        .round(targetValue * 100))) // money +/- 1 cent
        {
            optimalContributions = getAddValue();
            // round to nearest 10
            // optimalContributions = Math.round( optimalContributions / 10 ) *
            // 10;

            count = 0;

            return optimalContributions;
        }

        double addValue = this.getAddValue();
        if (addValue <= 0)
            return addValue;

        if (factor > 0) { // optimalTargetValue > getTargetValue()
            // too small, lets increase it
            addValue *= (1 + Math.abs(factor) / accuracy);
        } else if (factor < 0) { // optimalTargetValue < getTargetValue()
            // too much, lets decrease it
            addValue /= (1 + Math.abs(factor) / accuracy);
        }

        assign(this);
        setAddValue(addValue);

        return getOptimalContributions(optimalTargetValue);
    }

}
