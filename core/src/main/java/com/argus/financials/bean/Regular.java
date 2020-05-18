/*
 * FinancialRegular.java
 *
 * Created on 8 October 2001, 10:35
 */

package com.argus.financials.bean;

import com.argus.financials.api.bean.IFPSAssignableObject;
import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.FrequencyCode;

public abstract class Regular extends Financial {

    static final long serialVersionUID = 4642764639941230126L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.REGULAR;

    private java.math.BigDecimal regularAmount;

    private Integer frequencyCodeID;

    private java.util.Date nextDate;

    private Object asset;

    private Liability liability;

    private Integer liabilityID;

    private Financial temp; // temporary asset/liability

    private boolean taxable;

    // for Cashflow calculations, used in TaxContainer
    private String regularTaxType;

    /** Creates new Regular */
    protected Regular() {
    }

    protected Regular(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Regular) {
            Regular robj = (Regular) obj;
            if (isGenerated()
                    && robj.isGenerated()
                    && ((asset != robj.asset && equals(getAssetID(), robj
                            .getAssetID())) || (liability != robj.liability && equals(
                            getLiabilityID(), robj.getLiabilityID())))
                    && getGeneratedType() == robj.getGeneratedType())
                return true;
        }
        return super.equals(obj);
    }

    public String toString() {
        // associated asset can change its name, lets update this Regular
        // Financial Description
        if (isGenerated()) {
            String s;
            if (getAsset() != null)
                s = getAsset().toString();
            else if (getLiability() != null)
                s = getLiability().toString();
            else
                s = null;
            setFinancialDesc(s == null ? getGeneratedTypeDesc() : s + " ("
                    + getGeneratedTypeDesc() + ")");
        }

        return super.toString();
    }

    /**
     * Assignable methods
     */
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        regularAmount = ((Regular) value).regularAmount;
        frequencyCodeID = ((Regular) value).frequencyCodeID;
        nextDate = ((Regular) value).nextDate;

        if (((Regular) value).asset != null) {
            asset = ((Regular) value).asset;
            liability = null;
            liabilityID = null;
        } else {
            asset = null;
            liability = ((Regular) value).liability;
            liabilityID = ((Regular) value).liabilityID;
        }
        temp = ((Regular) value).temp;

        taxable = ((Regular) value).taxable;
        regularTaxType = ((Regular) value).regularTaxType;

    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        regularAmount = null;
        frequencyCodeID = null;
        nextDate = null;

        asset = null;
        liability = null;
        liabilityID = null;
        temp = null;

        taxable = false;
        regularTaxType = null;

    }

    protected void projectAmount(int year, double inflation) {

        // if ( year <= 0 ) return; // break condition

        int generatedType = getGeneratedType();
        if (generatedType < 0)
            return;

        // project this Asset/Liability till this year (in temp)
        // temp = getAsset() == null ?
        // getLiability().project( year, inflation, temp ) :
        // getAsset().project( year, inflation, temp );

        Financial af = getAssociatedFinancial();
        temp = af.project(year, inflation, temp);

        // get Regular from temp Asset/Liability
        Regular r = temp.getRegular(generatedType);
        setRegularAmount(r == null ? ZERO : r.getRegularAmount());
        setFrequencyCodeID(r == null ? FrequencyCode.YEARLY : r
                .getFrequencyCodeID());

    }

    public static java.math.BigDecimal getTotalAmount4Frequency(
            Integer frequency, java.util.Collection values) {
        java.math.BigDecimal total = new java.math.BigDecimal(0);

        if (frequency != null && values != null && values.size() > 0) {

            java.util.Iterator iter = values.iterator();
            while (iter.hasNext()) {
                Regular r = (Regular) iter.next();
                if (frequency.equals(r.getFrequencyCodeID()))
                    total = total.add(r.getAmount());
            }

        }
        return total;
    }

    /**
     * get/set methods
     */
    public void setAmount(java.math.BigDecimal value) {
        if (equals(getAmount(), value))
            return;

        super.setAmount(value);

    }

    public java.math.BigDecimal getRegularAmount() {
        if (regularAmount == null)
            regularAmount = ZERO;
        return regularAmount;
    }

    public void setRegularAmount(java.math.BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, java.math.BigDecimal.ROUND_DOWN);

        if (equals(regularAmount, value))
            return;

        regularAmount = value;
        setModified(true);
    }

    public java.math.BigDecimal getRegularAmount(boolean signed) {
        return getRegularAmount();
    }

    public Integer getFrequencyCodeID() {
        return frequencyCodeID;
    }

    public void setFrequencyCodeID(Integer value) {
        if (equals(frequencyCodeID, value))
            return;

        frequencyCodeID = value;
        setModified(true);
    }

    public String getFrequencyCode() {
        return new FrequencyCode().getCodeDescription(frequencyCodeID);
    }

    public java.util.Date getNextDate() {
        // try to re-calculate new nextDate
        return getNextDate(new java.util.Date());
    }

    public java.util.Date getNextDate(java.util.Date today) {
        // try to re-calculate new nextDate
        nextDate = FrequencyCode.getNextDate(nextDate, getFrequencyCodeID(),
                today);
        if (FrequencyCode.ONLY_ONCE.equals(getFrequencyCodeID())
                && nextDate == null)
            nextDate = getStartDate();
        return nextDate;
    }

    public void setNextDate(java.util.Date value) {
        if (equals(nextDate, value))
            return;

        nextDate = value;
        setModified(true);
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean value) {
        if (taxable == value)
            return;

        taxable = value;
        setModified(true);
    }

    public String getRegularTaxType() {
        return regularTaxType;
    }

    public void setRegularTaxType(String value) {
        if (regularTaxType == value)
            return;

        regularTaxType = value;
        setModified(true);
    }

    public Financial getAssociatedFinancial() {
        Asset a = getAsset();
        if (a != null)
            return a;
        return getLiability();
    }

    protected Asset getAsset() {
        return getAsset(null);
    }

    public Asset getAsset(Assets source) {
        if (asset instanceof Asset)
            return (Asset) asset;
        if (asset == null || source == null)
            return null;
        return (Asset) source.findByPrimaryKey((Integer) asset);
    }

    public void setAsset(Asset value) {
        if (equals(asset, value))
            return;

        asset = value;
        setModified(true);
    }

    public Integer getAssetID() {
        if (asset == null)
            return null;
        if (asset instanceof Integer)
            return (Integer) asset;
        return ((Asset) asset).getId();
    }

    public void setAssetID(Integer value) {
        if (equals(getAssetID(), value))
            return;

        asset = value;
        setModified(true);
    }

    public Integer getLiabilityID() {
        return liability == null ? liabilityID : liability.getId();
    }

    public Liability getLiability() {
        // if ( liability == null && liabilityID != null )
        // setAssetID( liabilityID ); // select *
        return liability;
    }

    public void setLiability(Liability value) {
        liability = value;
        setModified(true);
    }

    public java.math.BigDecimal getFinancialYearAmount() {
        return isGenerated() ? getFinancialYearAmountFractional(false) : // we
                                                                            // already
                                                                            // applied
                                                                            // fraction
                                                                            // in
                                                                            // Asset.projectAmount(...)
                getFinancialYearAmountFractional(true);
    }

    public java.math.BigDecimal getFinancialYearAmount(boolean sign) {
        return isGenerated() ? getFinancialYearAmountFractional(false, sign) : // we
                                                                                // already
                                                                                // applied
                                                                                // fraction
                                                                                // in
                                                                                // Asset.projectAmount(...)
                getFinancialYearAmountFractional(true, sign);
    }

    public void setIndexation(java.math.BigDecimal value) {
        super.setIndexation(value);
    }

}
