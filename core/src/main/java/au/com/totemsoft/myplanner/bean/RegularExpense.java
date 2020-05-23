/*
 * RegularExpense.java
 *
 * Created on 8 October 2001, 10:42
 */

package au.com.totemsoft.myplanner.bean;

import au.com.totemsoft.myplanner.api.code.FinancialTypeEnum;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.code.FrequencyCode;
import au.com.totemsoft.myplanner.tax.au.ITaxConstants;

public class RegularExpense extends Regular {

    static final long serialVersionUID = 4975995616063665382L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.REGULAR_EXPENSE;

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
                    + (DISPLAY_PKID ? "(" + getId() + ")" : "");

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

    public static String getRegularTaxType(FinancialTypeEnum financialType) {
        return getRegularTaxType(financialType.getId());
    }

    public static String getRegularTaxType(Integer financialTypeID) {
        if (FinancialTypeEnum.EXPENSE_GENERAL.getId() == financialTypeID)
            return ITaxConstants.D_GENERAL;
        if (FinancialTypeEnum.EXPENSE_SAVING_INVESTMENT.getId() == financialTypeID)
            return ITaxConstants.D_GENERAL;
        if (FinancialTypeEnum.EXPENSE_HOLIDAY.getId() == financialTypeID)
            return ITaxConstants.D_GENERAL;
        if (FinancialTypeEnum.EXPENSE_EDUCATION.getId() == financialTypeID)
            return ITaxConstants.D_GENERAL;
        if (FinancialTypeEnum.EXPENSE_OTHER.getId() == financialTypeID)
            return ITaxConstants.D_GENERAL;
        return null;
    }

}
