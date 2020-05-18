/*
 * Financial.java
 *
 * Created on 8 October 2001, 10:31
 */

package com.argus.financials.bean;

import java.math.BigDecimal;

import com.argus.financials.api.bean.ICode;
import com.argus.financials.api.bean.IFPSAssignableObject;
import com.argus.financials.api.bean.hibernate.FinancialCode;
import com.argus.financials.api.bean.hibernate.FinancialType;
import com.argus.financials.api.code.FinancialTypeEnum;
import com.argus.financials.api.code.FinancialTypeID;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.assetallocation.AssetAllocation;
import com.argus.financials.code.CountryCode;
import com.argus.financials.code.FinancialServiceCode;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.Institution;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.etc.FPSAssignableObject;
import com.argus.format.Currency;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public abstract class Financial extends FPSAssignableObject
    implements IRegularType, java.lang.Cloneable, FinancialTypeID {

    static final long serialVersionUID = -2309311830652434728L;

    transient protected static boolean DISPLAY_PKID = false;

    // true;

    public static final String NONE = "[None]";

    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal HUNDRED = new BigDecimal("100.");
    public static final BigDecimal RATIO_30_70 = new BigDecimal(30. / 70.);

    public static final double HOLE = Double.MAX_VALUE;

    private boolean generated = false;

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean value) {
        generated = value;
    }

    private int generatedType = -1;

    public int getGeneratedType() {
        return generatedType;
    }

    public String getGeneratedTypeDesc() {

        switch (generatedType) {
        case (iGROSS_INCOME):
            return sGROSS_INCOME;
        case (iUNFRANKED_INCOME):
            return sUNFRANKED_INCOME;
        case (iTAXFREE_INCOME):
            return sTAXFREE_INCOME;
        case (iIMPUTATION_CREDIT):
            return sIMPUTATION_CREDIT;
        case (iFRANKED_INCOME):
            return sFRANKED_INCOME;
        case (iTOTAL_EXPENSE):
            return sTOTAL_EXPENSE;
        case (iPENSION_REBATE):
            return sPENSION_REBATE;
        case (iTAX_DEDUCTIBLE_DEPOSIT):
            return sTAX_DEDUCTIBLE_DEPOSIT;
        case (iNON_DEDUCTIBLE_DEPOSIT):
            return sNON_DEDUCTIBLE_DEPOSIT;
        case (iPENSION_PAYMENT):
            return sPENSION_PAYMENT;
        case (iIMPUTATION_CREDIT_REBATE):
            return sIMPUTATION_CREDIT_REBATE;
        case (iTAX_DEDUCTIBLE_EXPENSE):
            return sTAX_DEDUCTIBLE_EXPENSE;
        case (iNON_DEDUCTIBLE_EXPENSE):
            return sNON_DEDUCTIBLE_EXPENSE;
        case (iTAXABLE_DRAWDOWN):
            return sTAXABLE_DRAWDOWN;
        case (iNON_TAXABLE_DRAWDOWN):
            return sNON_TAXABLE_DRAWDOWN;
        default:
            return "No Decription for Generated Type: " + generatedType;
        }

    }

    private int generatedYear = 0;

    public int getGeneratedYear() {
        return generatedYear;
    }

    public void setGeneratedYear(int year) {
        this.generatedYear = year;
    }

    private ICode financialType;

    private ICode financialCode;

    private Integer institutionID;

    private String institution;

    private Integer ownerCodeID;

    private Integer countryCodeID;

    private BigDecimal amount;

    private String financialDesc;

    // for Regular
    private java.util.Date startDate;

    private java.util.Date endDate;

    // for AssetInvestment
    private BigDecimal franked;

    private BigDecimal taxFreeDeferred;

    private BigDecimal capitalGrowth;

    private BigDecimal income;

    private BigDecimal upfrontFee;

    private BigDecimal ongoingFee;

    // for AssetSuperannuation
    private BigDecimal deductible;

    private BigDecimal deductibleDSS;

    private boolean complyingForDSS;

    private BigDecimal indexation;

    private BigDecimal expense;

    private BigDecimal rebateable;

    private Integer strategyGroupID;

    // AssetAllocation
    private Integer assetAllocationID;

    private AssetAllocation assetAllocation;

    private ReferenceCode financialService;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.FINANCIAL);

    public abstract Integer getObjectTypeID();

    protected static Currency curr;
    static {
        curr = Currency.createCurrencyInstance();
        // curr.setMinimumFractionDigits(0);
        // curr.setMaximumFractionDigits(0);
    }

    /** Creates new Financial */
    private static int refCount = 0;

    private int id = 0;

    protected Financial() {
        this(null);
    }

    protected Financial(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * override Object methodes
     */
    public boolean equals(Object obj) {
        if (obj instanceof Financial) // && isGenerated() && ( (Financial) obj
                                        // ).isGenerated() )
            return equals(getId(), ((Financial) obj)
                    .getId());
        return super.equals(obj);
    }

    public String toString() {
        String s = getFinancialDesc();

        if (empty(s))
            s = getFinancialTypeDesc();

        if (empty(s))
            s = NONE;

        if (DISPLAY_PKID)
            s += "(" + getId() + ")";

        return s;
    }

    public String toStringData() {
        return toString() + ", " + curr.toString(getAmount());
    }

    public Financial getNewFinancial() {
        return null;
    }

    public Object clone() throws CloneNotSupportedException {

        Financial f = getNewFinancial();
        try {
            f.assign(this);

            // assign members that are not asignable
            f.setId(getId());

        } catch (ClassCastException e) {
            e.printStackTrace(System.err); // impossible
            throw new CloneNotSupportedException(e.getMessage());
        }
        /*
         * // very heavy operation Financial f = (Financial) super.clone();
         * f.regulars = null; f.assetAllocation = assetAllocation == null ? null :
         * (AssetAllocation) assetAllocation.clone(); f.financialService =
         * financialService == null ? null : (ReferenceCode)
         * financialService.clone();
         */
        return f;

    }

    public Financial project(int year, double inflation) {
        return project(year, inflation, null);
    }

    public Financial project(int year, double inflation, Financial f) {

        if (f == null) {
            try {
                f = (Financial) clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace(System.err);
                return null;
            }

        } else {
            try {
                f.assign(this);
            } catch (ClassCastException e) {
                e.printStackTrace(System.err); // impossible
                return null;
            }

        }

        f.generatedYear = year;
        f.projectAmount(year, inflation);

        return f;

    }

    protected abstract void projectAmount(int year, double inflation);
    
    // setAmount() current value

    public BigDecimal getRegularAmount() {
        return ZERO;
    }

    protected void setRegularAmount(BigDecimal value) {
    }

    public String getTypeDesc() {
        String s = getClass().getName();
        return s.substring(s.lastIndexOf('.') + 1);
    }

    /**
     * Assignable methods
     */
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        financialType = ((Financial) value).financialType;
        financialCode = ((Financial) value).financialCode;
        institutionID = ((Financial) value).institutionID;
        institution = ((Financial) value).institution;
        ownerCodeID = ((Financial) value).ownerCodeID;
        countryCodeID = ((Financial) value).countryCodeID;
        amount = ((Financial) value).amount;
        financialDesc = ((Financial) value).financialDesc;

        startDate = ((Financial) value).startDate;
        endDate = ((Financial) value).endDate;

        franked = ((Financial) value).franked;
        taxFreeDeferred = ((Financial) value).taxFreeDeferred;
        capitalGrowth = ((Financial) value).capitalGrowth;
        income = ((Financial) value).income;
        upfrontFee = ((Financial) value).upfrontFee;
        ongoingFee = ((Financial) value).ongoingFee;

        deductible = ((Financial) value).deductible;
        deductibleDSS = ((Financial) value).deductibleDSS;
        complyingForDSS = ((Financial) value).complyingForDSS;
        indexation = ((Financial) value).indexation;
        expense = ((Financial) value).expense;
        rebateable = ((Financial) value).rebateable;
        // *
        assetAllocationID = ((Financial) value).assetAllocationID;
        try {
            assetAllocation = assetAllocation == null ? null
                    : (AssetAllocation) assetAllocation.clone();
        } catch (java.lang.CloneNotSupportedException e) {
            e.printStackTrace(System.err);
            assetAllocation = null;
        }

        try {
            financialService = ((Financial) value).financialService == null ? null
                    : (ReferenceCode) (((Financial) value).financialService
                            .clone());
        } catch (java.lang.CloneNotSupportedException e) {
            e.printStackTrace(System.err);
            financialService = null;
        }
        // */

        generated = ((Financial) value).generated;
        generatedType = ((Financial) value).generatedType;
        generatedYear = ((Financial) value).generatedYear;

        balanceAmount = ((Financial) value).balanceAmount;
        usedAmount = ((Financial) value).usedAmount;

    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        generatedYear = 0;
        balanceAmount = null;
        usedAmount = null;

        financialType = null;
        financialCode = null;
        institutionID = null;
        institution = null;
        ownerCodeID = null;
        countryCodeID = null;
        amount = ZERO;

        startDate = null;
        endDate = null;

        franked = ZERO;
        taxFreeDeferred = ZERO;
        capitalGrowth = ZERO;
        income = ZERO;
        upfrontFee = ZERO;
        ongoingFee = ZERO;

        deductible = ZERO;
        deductibleDSS = ZERO;
        complyingForDSS = false;
        indexation = ZERO;
        expense = ZERO;
        rebateable = ZERO;

        assetAllocationID = null;
        assetAllocation = null;

        financialService = null;

    }

    public static BigDecimal getTotalAmount(
            java.util.Collection values) {
        BigDecimal total = ZERO;

        if (values != null && values.size() > 0) {

            java.util.Iterator iter = values.iterator();
            while (iter.hasNext()) {
                Financial f = (Financial) iter.next();
                if (f != null && f.amount != null)
                    total = total.add(f.amount);
            }

        }
        return total;
    }

    public static BigDecimal getTotalAmount(Integer financialTypeID,
            java.util.Collection values) {
        BigDecimal total = ZERO;

        if (financialTypeID != null && values != null && values.size() > 0) {

            java.util.Iterator iter = values.iterator();
            while (iter.hasNext()) {
                Financial f = (Financial) iter.next();
                if (f != null && financialTypeID.equals(f.getFinancialTypeID())
                        && f.amount != null)
                    total = total.add(f.amount);
            }

        }

        return total;
    }

    /***************************************************************************
     * Generated Financials (income/expense/offset)
     **************************************************************************/
    // type from IRegularType
    protected java.util.Map regulars; // generated

    public Regular getRegular(int type) {
        return null;
    }

    public void add(java.util.Map map, int type) {
        Regular r = getRegular(type);
        if (r != null && r.getAmount() != null
                && r.getAmount().doubleValue() != 0.)
            map.put(r.getId(), r);
    }

    protected RegularIncome generateIncome(int generatedType) {
        // create new uninitilized object
        Financial r = new RegularIncome();
        r.generatedType = generatedType;
        initGenerated((Regular) r);
        return (RegularIncome) r;
    }

    protected RegularExpense generateExpense(int generatedType) {
        // create new uninitilized object
        Financial r = new RegularExpense();
        r.generatedType = generatedType;
        initGenerated((Regular) r);
        return (RegularExpense) r;
    }

    protected TaxOffset generateTaxOffset(int generatedType) {
        // create new uninitilized object
        Financial r = new TaxOffset();
        r.generatedType = generatedType;
        initGenerated((Regular) r);
        return (TaxOffset) r;
    }

    private void initGenerated(Regular r) {

        r.setGenerated(true);

        // r.setAmount( null ); // calculated
        if (this instanceof Asset) {
            r.setAsset((Asset) this);
            // r.setAssetID( getId() );
        }
        r.setCountryCodeID(getCountryCodeID());
        r.setFinancialCodeId(null);
        r.setFinancialDesc(toString());
        // r.setFinancialTypeID( new Integer( INCOME_INVESTMENT ) );
        r.setFrequencyCodeID(FrequencyCode.YEARLY);
        r.setStartDate(getStartDate());
        r.setEndDate(getEndDate());
        // r.setInstitutionID( getInstitutionID() );
        r.setInstitution(getInstitution());
        r.setOwnerCodeID(getOwnerCodeID());
        r.setStrategyGroupID(getStrategyGroupID());

    }

    /***************************************************************************
     * get/set methods
     **************************************************************************/
    public Integer getId() {
        if (super.getId() == null) {
            int hash = hashCode();
            setId(new Integer(hash < 0 ? hash : -hash));
            setModified(true);
        }
        return super.getId();
    }

    public Integer getOwnerId() {
        if (super.getOwnerId() == null)
            setOwnerId(clientService.getId());
        return super.getOwnerId();
    }

    public ICode getFinancialType() {
        return financialType;
    }

    public void setFinancialType(ICode value) {
        financialType = value;
    }

    public Integer getFinancialTypeID() {
        return financialType == null ? null : financialType.getId();
    }

    public void setFinancialTypeId(Integer value) {
        setFinancialType(value == null || value <= 0 ? null : new FinancialType(value));
    }
    public void setFinancialTypeId(FinancialTypeEnum value) {
        setFinancialTypeId(value.getId());
    }

    public String getFinancialTypeDesc() {
        return financialType == null ? null : financialType.getDescription();
    }

    public ICode getFinancialCode() {
        return financialCode;
    }

    public void setFinancialCode(ICode financialCode) {
        this.financialCode = financialCode;
    }

    public Integer getFinancialCodeId() {
        return financialCode == null ? null : financialCode.getId();
    }

    public void setFinancialCodeId(Integer financialCodeId) {
        setFinancialCode(financialCodeId == null || financialCodeId <= 0 ? null : new FinancialCode(financialCodeId));
    }

    public String getFinancialCodeDesc() {
        return financialCode == null ? null : financialCode.getDescription();
    }

    public Integer getInstitutionID() {
        return institutionID != null ? institutionID : new Institution()
                .getCodeID(institution);
    }

    public void setInstitutionID(Integer value) {
        if (equals(institutionID, value))
            return;

        institutionID = value;
        institution = null;

        setModified(true);
    }

    public String getInstitution() {
        return institution != null ? institution : new Institution()
                .getCodeDescription(institutionID);
    }

    public void setInstitution(String value) {
        if (equals(institution, value))
            return;

        institution = value;
        institutionID = null;

        setModified(true);
    }

    public Integer getOwnerCodeID() {
        return ownerCodeID;
    }

    public void setOwnerCodeID(Integer value) {
        if (equals(ownerCodeID, value))
            return;

        ownerCodeID = value;
        setModified(true);
    }

    public String getOwner() {
        return new OwnerCode().getCodeDescription(ownerCodeID);
    }

    public Integer getCountryCodeID() {
        return countryCodeID;
    }

    public void setCountryCodeID(Integer value) {
        if (equals(countryCodeID, value))
            return;

        countryCodeID = value;
        setModified(true);
    }

    public String getCountry() {
        return new CountryCode().getCodeDescription(countryCodeID);
    }

    public Integer getStrategyGroupID() {
        return strategyGroupID;
    }

    public void setStrategyGroupID(Integer value) {
        if (equals(strategyGroupID, value))
            return;

        strategyGroupID = value;
        setModified(true);
    }

    public BigDecimal getAmount() {
        if (amount == null)
            amount = ZERO;
        return amount;
    }

    // default
    public BigDecimal getFinancialYearAmount() {
        return getAmount();
    }

    public BigDecimal getFinancialYearAmount(boolean sign) {
        return getAmount(sign);
    }

    // default full or zero
    protected BigDecimal getFinancialYearAmountFractional(
            boolean fractional) {
        // return getAmount();
        double fraction = DateTimeUtils.getFinancialYearFraction(DateTimeUtils
                .getFinancialYearEnd()
                + getGeneratedYear(), getStartDate(), getEndDate());
        if (fraction > 0.)
            return new BigDecimal(getAmount().doubleValue()
                    * (fractional ? fraction : 1.));
        return ZERO;
    }

    protected BigDecimal getFinancialYearAmountFractional(
            boolean fractional, boolean sign) {
        // return getAmount(sign);
        double fraction = DateTimeUtils.getFinancialYearFraction(DateTimeUtils
                .getFinancialYearEnd()
                + getGeneratedYear(), getStartDate(), getEndDate());
        if (fraction > 0.)
            return new BigDecimal(getAmount(sign).doubleValue()
                    * (fractional ? fraction : 1.));
        return ZERO;
    }

    protected double getFinancialYearAmount(int financialYearEnd,
            double currentValue, double prevValue, double growth) {

        double fraction = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getStartDate(), getEndDate());
        double fractionStart = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, getStartDate(), null);
        double fractionEnd = DateTimeUtils.getFinancialYearFraction(
                financialYearEnd, null, getEndDate());

        if (prevValue == 0. && fractionStart == 0. && fractionEnd == 0.) // before
                                                                            // start
                                                                            // date
                                                                            // financial
                                                                            // year
            return 0.;

        double newValue;
        if (prevValue == 0. && fractionStart > 0. && fractionEnd > 0.) // on
                                                                        // start
                                                                        // date
                                                                        // financial
                                                                        // year
            newValue = currentValue;
        else
            newValue = prevValue;

        // after start date financial year
        // Increase current value by growth
        newValue *= (1. + fraction * growth / 100.);
        return newValue;

    }

    public void setAmount(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(amount, value))
            return;

        // uninitialized value
        if (balanceAmount == null) {
            amount = value;
            resetBalanceAmount();
        } else {
            if (value != null && amount != null)
                updateBalanceAmount(value.subtract(amount));
            amount = value;
        }

        setModified(true);
    }

    public BigDecimal getAmount(boolean signed) {
        return getAmount();
    }

    public String getFinancialDesc() {
        return financialDesc;
    }

    public void setFinancialDesc(String value) {
        if (value != null)
            value = value.trim();

        if (equals(financialDesc, value))
            return;

        financialDesc = value;
        setModified(true);
    }

    /***************************************************************************
     * for AssetInvestment
     **************************************************************************/
    public BigDecimal getFranked() {
        return franked;
    }

    protected void setFranked(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(franked, value))
            return;

        franked = value;
        setModified(true);
    }

    public BigDecimal getTaxFreeDeferred() {
        return taxFreeDeferred;
    }

    protected void setTaxFreeDeferred(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(taxFreeDeferred, value))
            return;

        taxFreeDeferred = value;
        setModified(true);
    }

    public BigDecimal getCapitalGrowth() {
        return capitalGrowth;
    }

    protected void setCapitalGrowth(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(capitalGrowth, value))
            return;

        capitalGrowth = value;
        setModified(true);
    }

    public BigDecimal getIncome() {
        return income;
    }

    protected void setIncome(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(income, value))
            return;

        income = value;
        setModified(true);
    }

    public BigDecimal getTotalReturn() {
        if (getCapitalGrowth() == null)
            return getIncome();
        if (getIncome() == null)
            return getCapitalGrowth();
        return getCapitalGrowth().add(getIncome());
    }

    public BigDecimal getUpfrontFee() {
        return upfrontFee;
    }

    protected void setUpfrontFee(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(upfrontFee, value))
            return;

        upfrontFee = value;
        setModified(true);
    }

    public BigDecimal getOngoingFee() {
        return ongoingFee;
    }

    protected void setOngoingFee(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(ongoingFee, value))
            return;

        ongoingFee = value;
        setModified(true);
    }

    /***************************************************************************
     * for AssetSuperannuation
     **************************************************************************/
    public BigDecimal getDeductible() {
        return deductible;
    }

    protected void setDeductible(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(deductible, value))
            return;

        deductible = value;
        setModified(true);
    }

    public BigDecimal getDeductibleDSS() {
        return deductibleDSS;
    }

    protected void setDeductibleDSS(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(deductibleDSS, value))
            return;

        deductibleDSS = value;
        setModified(true);
    }

    public boolean isComplyingForDSS() {
        return complyingForDSS;
    }

    protected void setComplyingForDSS(boolean value) {
        if (complyingForDSS == value)
            return;

        complyingForDSS = value;
        setModified(true);
    }

    public BigDecimal getIndexation() {
        return indexation;
    }

    protected void setIndexation(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(indexation, value))
            return;

        indexation = value;
        setModified(true);
    }

    public BigDecimal getExpense() {
        return expense;
    }

    protected void setExpense(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(expense, value))
            return;

        expense = value;
        setModified(true);
    }

    public BigDecimal getRebateable() {
        return rebateable;
    }

    protected void setRebateable(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(rebateable, value))
            return;

        rebateable = value;
        setModified(true);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date value) {
        if (equals(startDate, value))
            return;

        startDate = value;
        setModified(true);
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date value) {
        if (equals(endDate, value))
            return;

        endDate = value;
        setModified(true);
    }

    public long getTerm() {
        return DateTimeUtils.getTimeInYears(startDate, endDate);
    }

    /***************************************************************************
     * Temporary storage, used in Strategy Creator
     **************************************************************************/
    private BigDecimal balanceAmount = null; // uninitialized value

    private BigDecimal usedAmount;

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, BigDecimal.ROUND_DOWN);

        if (equals(getUsedAmount(), value))
            return;

        if (value != null && value.compareTo(getBalanceAmount()) > 0)
            value = getBalanceAmount();

        usedAmount = value;
        setModified(true);
    }

    public BigDecimal getUsedAmount(boolean signed) {
        return getUsedAmount();
    }

    public BigDecimal getBalanceAmount() {
        // uninitialized value
        if (balanceAmount == null)
            resetBalanceAmount();
        return balanceAmount == null ? ZERO : balanceAmount;
    }

    public BigDecimal updateBalanceAmount(BigDecimal value) {
        // uninitialized value
        if (balanceAmount == null)
            resetBalanceAmount(); // balanceAmount = getAmount();

        if (value == null || value.doubleValue() == 0. || balanceAmount == null)
            return balanceAmount;
        balanceAmount = balanceAmount.add(value);
        setModified(true);
        return balanceAmount;
    }

    public void resetBalanceAmount() {
        balanceAmount = getAmount();
        // balanceAmount = getFinancialYearAmount();
    }

    public BigDecimal getBalanceAmount(boolean signed) {
        return getBalanceAmount();
    }

    public AssetAllocation getAssetAllocation() {
        if (assetAllocation == null)
            assetAllocation = new AssetAllocation();
        return assetAllocation;
    }

    public void setAssetAllocation(AssetAllocation value) {
        assetAllocation = value;
    }

    public Integer getAssetAllocationID() {
        // return (assetAllocation == null) ? null :
        // assetAllocation.getAssetAllocationID();
        return (assetAllocation == null) ? assetAllocationID : assetAllocation
                .getAssetAllocationID();
    }

    public void setAssetAllocationID(Integer value) {
        assetAllocationID = value;
        getAssetAllocation().setAssetAllocationID(value);
    }

    public ReferenceCode getFinancialService() {
        return financialService;
    }

    public void setFinancialService(ReferenceCode value) {
        if (equals(financialService, value))
            return;
        financialService = value;
        setModified(true);
    }

    public String getFinancialServiceCode() {
        return financialService == null ? null : financialService.getCode();
    }

    public void setFinancialServiceCode(String value) {
        if (equals(getFinancialServiceCode(), value))
            return;
        ReferenceCode rc = new FinancialServiceCode().getCode(value);
        setFinancialService(rc);
    }

    public String getFinancialServiceCodeDesc() {
        return financialService == null ? null : financialService.getDescription();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getAccountNumber() {
        return null;
    }

    public Integer getFundTypeID() {
        return null;
    }

    public String getFundTypeDesc() {
        return null;
    }

    public Integer getInvestmentTypeID() {
        return null;
    }

    public String getInvestmentTypeDesc() {
        return null;
    }

    public Double getUnitsShares() {
        return null;
    }

    public BigDecimal getUnitsSharesPrice() {
        return null;
    }

    public java.util.Date getPriceDate() {
        return null;
    }

    public BigDecimal getPurchaseCost() {
        return null;
    }

    public BigDecimal getReplacementValue() {
        return null;
    }

    public BigDecimal getTaxDeductibleAnnualAmount() {
        return null;
    }

    public Integer getFrequencyCodeID() {
        return null;
    }

    public String getFrequencyCode() {
        return null;
    }

    public BigDecimal getAnnualAmount() {
        return ZERO;
    }

    public BigDecimal getContributionAnnualAmount() {
        return null;
    }

    public BigDecimal getContributionIndexation() {
        return null;
    }

    public java.util.Date getContributionStartDate() {
        return null;
    }

    public java.util.Date getContributionEndDate() {
        return null;
    }

    public BigDecimal getDrawdownAnnualAmount() {
        return null;
    }

    public BigDecimal getDrawdownIndexation() {
        return null;
    }

    public java.util.Date getDrawdownStartDate() {
        return null;
    }

    public java.util.Date getDrawdownEndDate() {
        return null;
    }

    public Financial getAssociatedFinancial() {
        return null;
    }

    public boolean isTaxable() {
        return false;
    }

    public String getRegularTaxType() {
        return null;
    }

    public Double getInterestRate() {
        return null;
    }

}
