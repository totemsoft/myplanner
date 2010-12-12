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

import com.argus.financials.code.FrequencyCode;

public class RegularExpense extends Regular {

    // serialver -classpath . com.argus.financial.RegularExpense

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 4975995616063665382L;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.REGULAR_EXPENSE);

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new RegularExpense */
    public RegularExpense() {
    }

    public RegularExpense(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * override Object methodes
     */
    public String toString() {
        if (!isGenerated()
                && EXPENSE_SAVING_INVESTMENT.equals(getFinancialTypeID())
                && getAsset() != null)
            return getFinancialTypeDesc() + " FROM: " + getAsset()
                    + (DISPLAY_PKID ? "(" + getPrimaryKeyID() + ")" : "");

        return super.toString();
    }

    public Financial getNewFinancial() {
        return new RegularExpense();
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

    public java.math.BigDecimal getAmount(boolean signed) {
        java.math.BigDecimal amount = getAmount();
        if (!signed)
            return amount;
        return amount == null ? null : amount.negate();
    }

    public java.math.BigDecimal getRegularAmount(boolean signed) {
        java.math.BigDecimal regularAmount = getRegularAmount();
        if (!signed)
            return regularAmount;
        return regularAmount == null ? null : regularAmount.negate();
    }

    public java.math.BigDecimal getUsedAmount(boolean signed) {
        java.math.BigDecimal amount = getUsedAmount();
        if (!signed)
            return amount;
        return amount == null ? null : amount.negate();
    }

    public java.math.BigDecimal getBalanceAmount(boolean signed) {
        java.math.BigDecimal amount = getBalanceAmount();
        if (!signed)
            return amount;
        return amount == null ? null : amount.negate();
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
        String taxType = null;

        if (EXPENSE_GENERAL.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.D_GENERAL;
        else if (EXPENSE_SAVING_INVESTMENT.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.D_GENERAL;
        else if (EXPENSE_HOLIDAY.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.D_GENERAL;
        else if (EXPENSE_EDUCATION.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.D_GENERAL;
        else if (EXPENSE_OTHER.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.D_GENERAL;

        return taxType;

    }

}
