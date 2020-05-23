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

public class TaxOffset extends Regular {

    static final long serialVersionUID = 2203597048523637390L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.TAX_OFFSET;

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
        return getFinancialType().getDescription();
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

    public static String getRegularTaxType(FinancialTypeEnum financialType) {
        return getRegularTaxType(financialType.getId());
    }

    public static String getRegularTaxType(Integer financialTypeID) {
        if (FinancialTypeEnum.TAXOFFSET_IMPUTATION_CREDIT.getId() == financialTypeID)
            return ITaxConstants.O_IMPUTATION_CREDIT;
        if (FinancialTypeEnum.TAXOFFSET_LUMP_SUM.getId() == financialTypeID)
            return ITaxConstants.O_LUMP_SUM_ETP;
        //if ( == financialTypeID)
        //    return TaxConstants.O_TAX_WITHHELD;
        if (FinancialTypeEnum.TAXOFFSET_SUPER.getId() == financialTypeID)
            return ITaxConstants.O_SUPER;
        if (FinancialTypeEnum.TAXOFFSET_LOW_INCOME.getId() == financialTypeID)
            return ITaxConstants.O_LOW_INCOME;
        return ITaxConstants.O_OTHER;
    }

    public static Integer getFinancialTypeID(String taxType) {
        if (ITaxConstants.O_IMPUTATION_CREDIT.equals(taxType))
            return FinancialTypeEnum.TAXOFFSET_IMPUTATION_CREDIT.getId();
        if (ITaxConstants.O_LUMP_SUM_ETP.equals(taxType))
            return FinancialTypeEnum.TAXOFFSET_LUMP_SUM.getId();
        //if (TaxConstants.O_TAX_WITHHELD.equals(taxType))
        //    return ;
        if (ITaxConstants.O_SUPER.equals(taxType))
            return FinancialTypeEnum.TAXOFFSET_SUPER.getId();
        if (ITaxConstants.O_LOW_INCOME.equals(taxType))
            return FinancialTypeEnum.TAXOFFSET_LOW_INCOME.getId();
        return FinancialTypeEnum.TAXOFFSET_OTHER.getId();
    }

}
