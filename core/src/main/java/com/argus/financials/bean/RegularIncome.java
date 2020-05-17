/*
 * RegularIncome.java
 *
 * Created on 8 October 2001, 10:43
 */

package com.argus.financials.bean;

import com.argus.financials.api.code.FinancialTypeEnum;
import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.FrequencyCode;
import com.argus.financials.tax.au.ITaxConstants;

public class RegularIncome extends Regular {

    static final long serialVersionUID = 7783855274377676337L;

    public static final Integer OBJECT_TYPE_ID = ObjectTypeConstant.REGULAR_INCOME;

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new RegularIncome */
    public RegularIncome() {
        this(null);
    }

    public RegularIncome(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);

        setTaxable(true); // default for income
    }

    public Financial getNewFinancial() {
        return new RegularIncome();
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
        if (FinancialTypeEnum.INCOME_SALARY.getId() == financialTypeID)
            return ITaxConstants.I_SALARY;
        if (FinancialTypeEnum.INCOME_INVESTMENT.getId() == financialTypeID) 
            return ITaxConstants.I_DIVIDENDS_FRANKED;
        //if ( == IRegularType.iIMPUTATION_CREDIT)
        //    return TaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT;
        if (FinancialTypeEnum.INCOME_SOCIAL_SECURITY.getId() == financialTypeID)
            return ITaxConstants.I_PENSIONS_ALLOWANCE;
        if (FinancialTypeEnum.INCOME_RETIREMENT.getId() == financialTypeID)
            return ITaxConstants.I_OTHER_PENSIONS;
        if (FinancialTypeEnum.INCOME_OTHER.getId() == financialTypeID)
            return ITaxConstants.I_SALARY;
        if (FinancialTypeEnum.INCOME_OTHER_TAX_FREE.getId() == financialTypeID)
            return ITaxConstants.I_TAX_DEFERRED;
        return null;
    }

}
