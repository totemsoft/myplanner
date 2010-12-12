/*
 * RegularExpense.java
 *
 * Created on 8 October 2001, 10:42
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.FinancialType;
import com.argus.financials.code.FrequencyCode;

public class TaxOffset extends Regular {

    // serialver -classpath . com.argus.financial.TaxOffset

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 2203597048523637390L;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.TAX_OFFSET);

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new RegularExpense */
    public TaxOffset() {
        this(null);
    }

    public TaxOffset(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);

        setTaxable(true); // default for tax offset
    }

    /**
     * use financialTypeID
     */
    public String getGeneratedTypeDesc() {
        return ""
                + FinancialType.getFinancialType(OBJECT_TYPE_ID,
                        getFinancialTypeID());
    }

    public Financial getNewFinancial() {
        return new TaxOffset();
    }

    /**
     * get/set methods
     */
    // we have to override this method because Liability incorrectly inherit
    // from Regular (has to be Financial)
    public void setAmount(java.math.BigDecimal value) { /* do nothing */
    }

    public void setRegularAmount(java.math.BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, java.math.BigDecimal.ROUND_DOWN);

        if (equals(getRegularAmount(), value))
            return;

        super.setRegularAmount(value);
        super.setAmount(FrequencyCode.getAnnualAmount(getFrequencyCodeID(),
                getRegularAmount()));

    }

    public void setFrequencyCodeID(Integer value) {
        if (equals(getFrequencyCodeID(), value))
            return;

        super.setFrequencyCodeID(value);
        super.setAmount(FrequencyCode.getAnnualAmount(getFrequencyCodeID(),
                getRegularAmount()));

    }

    /*
     * public java.math.BigDecimal getAmount( boolean signed ) {
     * java.math.BigDecimal amount = getAmount(); if ( !signed ) return amount;
     * return amount == null ? null : amount.negate(); }
     * 
     * public java.math.BigDecimal getRegularAmount( boolean signed ) {
     * java.math.BigDecimal regularAmount = getRegularAmount(); if ( !signed )
     * return regularAmount; return regularAmount == null ? null :
     * regularAmount.negate(); }
     */

    public boolean isTaxable() {
        return true;
    }

    public String getRegularTaxType() {
        String taxType = super.getRegularTaxType();

        if (taxType == null) {
            taxType = getRegularTaxType(getFinancialTypeID());
            setRegularTaxType(taxType);
        }

        return taxType;

    }

    public static String getRegularTaxType(Integer financialTypeID) {

        String taxType;

        if (TAXOFFSET_IMPUTATION_CREDIT.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.O_IMPUTATION_CREDIT;
        else if (TAXOFFSET_LUMP_SUM.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.O_LUMP_SUM_ETP;
        // else if ( .equals( financialTypeID ) )
        // taxType = com.fiducian.tax.TaxConstants.O_TAX_WITHHELD;
        else if (TAXOFFSET_SUPER.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.O_SUPER;
        else if (TAXOFFSET_LOW_INCOME.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.O_LOW_INCOME;
        else
            taxType = com.argus.financials.tax.au.ITaxConstants.O_OTHER;

        return taxType;

    }

    public static Integer getFinancialTypeID(String taxType) {

        Integer financialTypeID;

        if (com.argus.financials.tax.au.ITaxConstants.O_IMPUTATION_CREDIT
                .equals(taxType))
            financialTypeID = TAXOFFSET_IMPUTATION_CREDIT;
        else if (com.argus.financials.tax.au.ITaxConstants.O_LUMP_SUM_ETP
                .equals(taxType))
            financialTypeID = TAXOFFSET_LUMP_SUM;
        // else if ( com.fiducian.tax.TaxConstants.O_TAX_WITHHELD.equals(
        // taxType ) )
        // financialTypeID = ;
        else if (com.argus.financials.tax.au.ITaxConstants.O_SUPER.equals(taxType))
            financialTypeID = TAXOFFSET_SUPER;
        else if (com.argus.financials.tax.au.ITaxConstants.O_LOW_INCOME
                .equals(taxType))
            financialTypeID = TAXOFFSET_LOW_INCOME;
        else
            financialTypeID = TAXOFFSET_OTHER;

        return financialTypeID;

    }

}
