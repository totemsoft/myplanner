/*
 * RegularIncome.java
 *
 * Created on 8 October 2001, 10:43
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.FrequencyCode;

public class RegularIncome extends Regular {

    // serialver -classpath . com.argus.financial.RegularIncome

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 7783855274377676337L;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.REGULAR_INCOME);

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
            taxType = getRegularTaxType(getFinancialTypeID(),
                    getGeneratedType());
            setRegularTaxType(taxType);
        }

        return taxType;

    }

    public static String getRegularTaxType(Integer financialTypeID) {
        return getRegularTaxType(financialTypeID, 0);
    }

    public static String getRegularTaxType(Integer financialTypeID,
            int generatedType) {
        String taxType = null;

        if (INCOME_SALARY.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.I_SALARY;
        else if (INCOME_INVESTMENT.equals(financialTypeID)) {
            taxType = com.argus.financials.tax.au.ITaxConstants.I_DIVIDENDS_FRANKED;
            // if ( generatedType == IRegularType.iIMPUTATION_CREDIT )
            // taxType =
            // com.fiducian.tax.TaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT;

        } else if (INCOME_SOCIAL_SECURITY.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.I_PENSIONS_ALLOWANCE;
        else if (INCOME_RETIREMENT.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.I_OTHER_PENSIONS;
        else if (INCOME_OTHER.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.I_SALARY;
        else if (INCOME_OTHER_TAX_FREE.equals(financialTypeID))
            taxType = com.argus.financials.tax.au.ITaxConstants.I_TAX_DEFERRED;

        return taxType;

    }

}
