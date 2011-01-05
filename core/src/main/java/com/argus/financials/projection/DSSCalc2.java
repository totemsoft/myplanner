/*
 * DSSCalc2.java
 *
 * Created on 4 March 2002, 11:21
 */

package com.argus.financials.projection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.argus.financials.bean.RegularIncome;
import com.argus.financials.code.BenefitTypeCode;
import com.argus.financials.code.FinancialTypeID;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.projection.data.AssessableAssetsIncome;
import com.argus.financials.tax.au.ITaxConstants;
import com.argus.util.DateTimeUtils;

public class DSSCalc2 extends DSSCalc {

    private static final int MINIMUM_AGE_FOR_NEWSTART_ALLOWANCE = 21;

    private static final int BIGDECIMAL_SCALE = 20; // The scale is the number
                                                    // of digits to the right of
                                                    // the decimal point.

    // known components
    private Integer sexCodeID;

    private BigDecimal rent;

    private BigInteger childrenAmount;

    private BigDecimal agePensionFornightly;

    private String clientName;

    private Date calculationDate;

    private Integer maritalStatus;

    private Integer benefitType;

    private Boolean homeOwner;

    private Boolean noChildren;

    private Boolean sharer;

    private Boolean eligibleForPA;

    private String partnerName;

    private Integer partnerSexCodeID;

    private Integer partnerSexCodeMale;

    private Integer partnerSexCodeFemale;

    private Date partnerDateOfBirth;

    private Date partnerCalculationDate;

    private Integer partnerBenefitType;

    private Boolean partnerEligibleForPA;

    /*
     * client's assessable assets/income
     */
    private AssessableAssetsIncome client_ai;

    /*
     * partner's assessable assets/income
     */
    private AssessableAssetsIncome partner_ai;

    /*
     * joint assessable assets/income
     */
    private AssessableAssetsIncome joint_ai;

    /*
     * joint assessable assets/income
     */
    private AssessableAssetsIncome assessable_ai;

    /** Creates new DSSCalc2 */
    public DSSCalc2() {
        super();
        _clear();
    }

    public DSSCalc2(DSSCalc2 obj) {
        super();

        this.assign(obj); // call in top derived class
    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {

        sexCodeID = null;

        rent = null;
        childrenAmount = null;
        agePensionFornightly = null;

        clientName = null;
        calculationDate = null;
        maritalStatus = null;
        benefitType = null;
        homeOwner = null;
        noChildren = null;
        sharer = null;

        eligibleForPA = null;
        partnerName = null;
        partnerSexCodeID = null;
        partnerSexCodeMale = null;
        partnerSexCodeFemale = null;
        partnerDateOfBirth = null;
        partnerCalculationDate = null;
        partnerBenefitType = null;
        partnerEligibleForPA = null;

        client_ai = new AssessableAssetsIncome();
        partner_ai = new AssessableAssetsIncome();
        joint_ai = new AssessableAssetsIncome();
        assessable_ai = new AssessableAssetsIncome();

    }

    public Integer getDefaultModelType() {
        return ModelType.SOCIAL_SECURITY_CALC;
    }

    public String getDefaultTitle() {
        return "Social Security";
    }

    /**
     * 
     */
    private boolean _isReady() {

        if (single() == null)
            return false;

        if (equals(single(), Boolean.TRUE))
            return

            (getDateOfBirth() != null) && (getSexCodeID() != null)
                    && (getCalculationDate() != null)
                    && (getMaritalStatus() != null)
                    && (getBenefitType() != null);

        else {

            return getDateOfBirth() != null && getSexCodeID() != null
                    && getCalculationDate() != null
                    && getMaritalStatus() != null && getBenefitType() != null
                    && getPartnerDateOfBirth() != null
                    && getPartnerSexCodeID() != null
                    && getPartnerCalculationDate() != null
                    && getPartnerBenefitType() != null;

        }
    }

    public boolean isReady() {
        return _isReady();
    }

    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        BigDecimal b = ZERO;

        if (property.equals(CLIENT_NAME)) {
            setClientName(value);

        } else if (property.equals(CALCULATION_DATE)) {
            setCalculationDate(DateTimeUtils.getDate(value));

        } else if (property.equals(MARITAL_STATUS)) {

            if (getNumberInstance().isValid(value))
                setMaritalStatus(new Integer(value));
            else
                setMaritalStatus(new MaritalCode().getCodeID(value));

        } else if (property.equals(BENEFIT_TYPE)) {
            if (getNumberInstance().isValid(value))
                setBenefitType(new Integer(value));
            else
                setBenefitType(new BenefitTypeCode().getCodeID(value));

        } else if (property.equals(HOME_OWNER)) {
            /*
             * OLD-VERSION setHomeOwner( Boolean.getBoolean( value ) );
             */
            // BEGIN: BUG FIX 577 - 11/07/2002
            // by shibaevv
            // Boolean.getBoolean( value ) returns allways false!!!
            // so I use equals to decide which value to set
            setHomeOwner(value.toUpperCase().equals("TRUE"));
            // END: BUG FIX 577 - 11/07/2002

        } else if (property.equals(RENT)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setRent(b);

        } else if (property.equals(NO_CHILDREN)) {
            setNoChildren(new Boolean(value));

        } else if (property.equals(CHILDREN_AMOUNT)) {
            if (value == null || value.length() == 0) {
                setChildrenAmount(null);
            } else {
                try {
                    setChildrenAmount(new BigInteger(value.trim()));
                } catch (Exception e) {
                    setChildrenAmount(null);
                }
            }

        } else if (property.equals(NAME_PARTNER)) {
            setPartnerName(value);

        } else if (property.equals(DOB_PARTNER)) {
            setPartnerDateOfBirth(DateTimeUtils.getDate(value));

        } else if (property.equals(SEX_CODE)) {
            if (getNumberInstance().isValid(value))
                setSexCodeID(new Integer(value));
            else
                setSexCodeID(new SexCode().getCodeID(value));

        } else if (property.equals(SEX_CODE_FEMALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setSexCodeID(SexCode.FEMALE);

        } else if (property.equals(SEX_CODE_MALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setSexCodeID(SexCode.MALE);

        } else if (property.equals(PARTNER_SEX_CODE)) {
            if (getNumberInstance().isValid(value))
                setPartnerSexCodeID(new Integer(value));
            else
                setPartnerSexCodeID(new SexCode().getCodeID(value));

        } else if (property.equals(PARTNER_SEX_CODE_FEMALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setPartnerSexCodeID(SexCode.FEMALE);

        } else if (property.equals(PARTNER_SEX_CODE_MALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setPartnerSexCodeID(SexCode.MALE);

        } else if (property.equals(CALCULATION_DATE_PARTNER)) {
            setPartnerCalculationDate(DateTimeUtils.getDate(value));

        } else if (property.equals(BENEFIT_TYPE_PARTNER)) {
            if (getNumberInstance().isValid(value))
                setPartnerBenefitType(new Integer(value));
            else
                setPartnerBenefitType(new BenefitTypeCode().getCodeID(value));

            // } else if ( property.equals( ELIGIBLE_FOR_PA_PARTNER ) ) {
            // setPartnerEligibleForPA( new Boolean (value) );

        } else if (property.equals(SAVINGS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSavingsA(b);
            // calculate client's income and store it
            this.client_ai.savingsI = this.getSavingsI();

        } else if (property.equals(MANAGED_FUNDS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setManagedFundsA(b);
            // calculate client's income and store it
            this.client_ai.managedFundsI = this.getManagedFundsI();

        } else if (property.equals(SHARES_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSharesA(b);
            // calculate client's income and store it
            this.client_ai.sharesI = this.getSharesI();

        } else if (property.equals(BONDS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setBondsA(b);
            // calculate client's income and store it
            this.client_ai.bondsI = this.getBondsI();

        } else if (property.equals(FIXED_INTEREST_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setFixedInterestA(b);
            // calculate client's income and store it
            this.client_ai.interestI = this.getFixedInterestI();

        } else if (property.equals(HOME_CONTENTS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setHomeContentsA(b);

        } else if (property.equals(CARS_ETC_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setCarsEtcA(b);

        } else if (property.equals(PROPERTY_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPropertyA(b);

        } else if (property.equals(GIFTS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setGiftsA(b);
            this.client_ai.giftsI = this.getGiftsI();

        } else if (property.equals(LOANS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setLoansA(b);

        } else if (property.equals(PROPERTY_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPropertyI(b);

        } else if (property.equals(LOANS_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setLoansI(b);

        } else if (property.equals(SUPERANNUATION_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSuperannuationA(b);
            this.client_ai.superannuationI = this.getSuperannuationI();

        } else if (property.equals(ALLOCATED_PENSION_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setAllocatedPensionA(b);

        } else if (property.equals(ALLOCATED_PENSION_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setAllocatedPensionI(b);

        } else if (property.equals(COMPLYING_PENSION_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setComplyingPensionA(b);

        } else if (property.equals(COMPLYING_PENSION_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setComplyingPensionI(b);

        } else if (property.equals(CLIENT_SALARY_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setClientSalaryI(b);

        } else if (property.equals(PARTNER_SALARY_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPartnerSalaryI(b);

        } else if (property.equals(SAVINGS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSavingsA(b);

        } else if (property.equals(MANAGED_FUNDS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setManagedFundsA(b);

        } else if (property.equals(SHARES_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSharesA(b);

        } else if (property.equals(BONDS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setBondsA(b);

        } else if (property.equals(FIXED_INTEREST_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setFixedInterestA(b);

        } else if (property.equals(HOME_CONTENTS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setHomeContentsA(b);

        } else if (property.equals(CARS_ETC_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setCarsEtcA(b);

        } else if (property.equals(PROPERTY_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPropertyA(b);

        } else if (property.equals(GIFTS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setGiftsA(b);

        } else if (property.equals(LOANS_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setLoansA(b);

        } else if (property.equals(PROPERTY_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPropertyI(b);

        } else if (property.equals(LOANS_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setLoansI(b);

        } else if (property.equals(SUPERANNUATION_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSuperannuationA(b);

        } else if (property.equals(ALLOCATED_PENSION_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setAllocatedPensionA(b);

        } else if (property.equals(ALLOCATED_PENSION_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setAllocatedPensionI(b);

        } else if (property.equals(COMPLYING_PENSION_A)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setComplyingPensionA(b);

        } else if (property.equals(COMPLYING_PENSION_I)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setComplyingPensionI(b);
            /*
             * ============================== BEGIN: PARTNER
             * ==============================
             */
        } else if (property.equals(SAVINGS_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSavingsAPartner(b);
            // calculate client's income and store it
            this.partner_ai.savingsI = this.getSavingsI();

        } else if (property.equals(MANAGED_FUNDS_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setManagedFundsAPartner(b);
            // calculate client's income and store it
            this.partner_ai.managedFundsI = this.getManagedFundsI();

        } else if (property.equals(SHARES_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSharesAPartner(b);
            // calculate client's income and store it
            this.partner_ai.sharesI = this.getSharesI();

        } else if (property.equals(BONDS_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setBondsAPartner(b);
            // calculate client's income and store it
            this.partner_ai.bondsI = this.getBondsI();

        } else if (property.equals(FIXED_INTEREST_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setFixedInterestAPartner(b);
            // calculate client's income and store it
            this.partner_ai.interestI = this.getFixedInterestI();

        } else if (property.equals(HOME_CONTENTS_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setHomeContentsAPartner(b);

        } else if (property.equals(CARS_ETC_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setCarsEtcAPartner(b);

        } else if (property.equals(PROPERTY_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPropertyAPartner(b);

        } else if (property.equals(GIFTS_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setGiftsAPartner(b);

        } else if (property.equals(LOANS_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setLoansAPartner(b);

        } else if (property.equals(PROPERTY_I_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setPropertyIPartner(b);

        } else if (property.equals(LOANS_I_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setLoansIPartner(b);

        } else if (property.equals(SUPERANNUATION_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setSuperannuationAPartner(b);

        } else if (property.equals(ALLOCATED_PENSION_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setAllocatedPensionAPartner(b);

        } else if (property.equals(ALLOCATED_PENSION_I_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setAllocatedPensionIPartner(b);

        } else if (property.equals(COMPLYING_PENSION_A_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setComplyingPensionAPartner(b);

        } else if (property.equals(COMPLYING_PENSION_I_PARTNER)) {
            if (value != null && value.length() > 0)
                b = getCurrencyInstance().getBigDecimalValue(value);
            setComplyingPensionIPartner(b);
            /*
             * ============================== END: PARTNER
             * ==============================
             */
        } else
            return false;

        // update internal data
        this.updateJointAssessableAssetsIncome(); // update joint data
                                                    // (client+partner)
        this.updateAssessableAssetsIncome(); // update assessable data
                                                // (client+partner)

        return true;

    }

    /**
     * 
     */
    public void assign(MoneyCalc obj) {

        super.assign(obj);

        if (!(obj instanceof DSSCalc2))
            return;

        client_ai.savingsA = ((DSSCalc2) obj).client_ai.savingsA;
        client_ai.managedFundsA = ((DSSCalc2) obj).client_ai.managedFundsA;
        client_ai.sharesA = ((DSSCalc2) obj).client_ai.sharesA;
        client_ai.bondsA = ((DSSCalc2) obj).client_ai.bondsA;
        client_ai.interestA = ((DSSCalc2) obj).client_ai.interestA;
        client_ai.homeContentsA = ((DSSCalc2) obj).client_ai.homeContentsA;
        client_ai.carsEtcA = ((DSSCalc2) obj).client_ai.carsEtcA;
        client_ai.propertyA = ((DSSCalc2) obj).client_ai.propertyA;
        client_ai.giftsA = ((DSSCalc2) obj).client_ai.giftsA;
        client_ai.loansA = ((DSSCalc2) obj).client_ai.loansA;
        client_ai.propertyI = ((DSSCalc2) obj).client_ai.propertyI;
        client_ai.loansI = ((DSSCalc2) obj).client_ai.loansI;
        client_ai.superannuationA = ((DSSCalc2) obj).client_ai.superannuationA;
        client_ai.allocatedPensionA = ((DSSCalc2) obj).client_ai.allocatedPensionA;
        client_ai.allocatedPensionI = ((DSSCalc2) obj).client_ai.allocatedPensionI;
        client_ai.complyingPensionA = ((DSSCalc2) obj).client_ai.complyingPensionA;
        client_ai.complyingPensionI = ((DSSCalc2) obj).client_ai.complyingPensionI;

        partner_ai.savingsA = ((DSSCalc2) obj).partner_ai.savingsA;
        partner_ai.managedFundsA = ((DSSCalc2) obj).partner_ai.managedFundsA;
        partner_ai.sharesA = ((DSSCalc2) obj).partner_ai.sharesA;
        partner_ai.bondsA = ((DSSCalc2) obj).partner_ai.bondsA;
        partner_ai.interestA = ((DSSCalc2) obj).partner_ai.interestA;
        partner_ai.homeContentsA = ((DSSCalc2) obj).partner_ai.homeContentsA;
        partner_ai.carsEtcA = ((DSSCalc2) obj).partner_ai.carsEtcA;
        partner_ai.propertyA = ((DSSCalc2) obj).partner_ai.propertyA;
        partner_ai.giftsA = ((DSSCalc2) obj).partner_ai.giftsA;
        partner_ai.loansA = ((DSSCalc2) obj).partner_ai.loansA;
        partner_ai.propertyI = ((DSSCalc2) obj).partner_ai.propertyI;
        partner_ai.loansI = ((DSSCalc2) obj).partner_ai.loansI;
        partner_ai.superannuationA = ((DSSCalc2) obj).partner_ai.superannuationA;
        partner_ai.allocatedPensionA = ((DSSCalc2) obj).partner_ai.allocatedPensionA;
        partner_ai.allocatedPensionI = ((DSSCalc2) obj).partner_ai.allocatedPensionI;
        partner_ai.complyingPensionA = ((DSSCalc2) obj).partner_ai.complyingPensionA;
        partner_ai.complyingPensionI = ((DSSCalc2) obj).partner_ai.complyingPensionI;

        client_ai.salaryWagesI = ((DSSCalc2) obj).client_ai.salaryWagesI;
        partner_ai.salaryWagesI = ((DSSCalc2) obj).partner_ai.salaryWagesI;
        rent = ((DSSCalc2) obj).rent;
        childrenAmount = ((DSSCalc2) obj).childrenAmount;
        clientName = ((DSSCalc2) obj).clientName;
        calculationDate = ((DSSCalc2) obj).calculationDate;
        maritalStatus = ((DSSCalc2) obj).maritalStatus;
        benefitType = ((DSSCalc2) obj).benefitType;
        homeOwner = ((DSSCalc2) obj).homeOwner;
        noChildren = ((DSSCalc2) obj).noChildren;
        sharer = ((DSSCalc2) obj).sharer;
        partnerName = ((DSSCalc2) obj).partnerName;
        partnerSexCodeID = ((DSSCalc2) obj).partnerSexCodeID;
        partnerSexCodeMale = ((DSSCalc2) obj).partnerSexCodeMale;
        partnerSexCodeFemale = ((DSSCalc2) obj).partnerSexCodeFemale;
        partnerDateOfBirth = ((DSSCalc2) obj).partnerDateOfBirth;
        partnerCalculationDate = ((DSSCalc2) obj).partnerCalculationDate;
        partnerBenefitType = ((DSSCalc2) obj).partnerBenefitType;

        if (this.getClass().equals(obj.getClass()))
            return;

        setModified();

    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String value) {
        if (equals(clientName, value))
            return;

        clientName = value;
        setModified();
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer value) {
        if (equals(maritalStatus, value))
            return;

        maritalStatus = value;
        setModified();
    }

    public String getMaritalStatusDesc() {
        return new MaritalCode().getCodeDescription(getMaritalStatus());
    }

    public Integer getBenefitType() {
        return benefitType;
    }

    public void setBenefitType(Integer value) {
        if (equals(benefitType, value))
            return;

        benefitType = value;
        setModified();
    }

    public String getBenefitTypeDesc() {
        return new BenefitTypeCode().getCodeDescription(getBenefitType());
    }

    public java.util.Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(java.util.Date value) {
        if (equals(calculationDate, value))
            return;

        calculationDate = value;

        setModified();
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal value) {
        if (equals(rent, value))
            return;

        rent = value;
        setModified();
    }

    public BigInteger getChildrenAmount() {
        return childrenAmount;
    }

    public void setChildrenAmount(BigInteger value) {
        if (equals(childrenAmount, value))
            return;

        childrenAmount = value;
        setModified();
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String value) {
        if (equals(partnerName, value))
            return;

        partnerName = value;
        setModified();
    }

    public Integer getPartnerSexCodeID() {
        return partnerSexCodeID;
    }

    public void setPartnerSexCodeID(Integer value) {
        if (equals(partnerSexCodeID, value))
            return;

        partnerSexCodeID = value;
        setModified();
    }

    public Date getPartnerDateOfBirth() {
        return partnerDateOfBirth;
    }

    public void setPartnerDateOfBirth(Date value) {
        if (equals(partnerDateOfBirth, value))
            return;

        partnerDateOfBirth = value;
        setModified();
    }

    public Integer getPartnerBenefitType() {
        return partnerBenefitType;
    }

    public void setPartnerBenefitType(Integer value) {
        if (equals(partnerBenefitType, value))
            return;

        partnerBenefitType = value;
        setModified();
    }

    public String getPartnerBenefitTypeDesc() {
        return new BenefitTypeCode()
                .getCodeDescription(getPartnerBenefitType());
    }

    public Date getPartnerCalculationDate() {
        return partnerCalculationDate;
    }

    public void setPartnerCalculationDate(Date value) {
        if (equals(partnerCalculationDate, value))
            return;

        partnerCalculationDate = value;

        setModified();
    }

    public BigDecimal getSavingsA() {
        return client_ai.savingsA;
    }

    public void setSavingsA(BigDecimal value) {
        if (equals(client_ai.savingsA, value))
            return;

        client_ai.savingsA = value;

        setModified();
    }

    public BigDecimal getManagedFundsA() {
        return client_ai.managedFundsA;
    }

    public void setManagedFundsA(BigDecimal value) {
        if (equals(client_ai.managedFundsA, value))
            return;

        client_ai.managedFundsA = value;

        setModified();
    }

    public BigDecimal getSharesA() {
        return client_ai.sharesA;
    }

    public void setSharesA(BigDecimal value) {
        if (equals(client_ai.sharesA, value))
            return;

        client_ai.sharesA = value;

        setModified();
    }

    public BigDecimal getBondsA() {
        return client_ai.bondsA;
    }

    public void setBondsA(BigDecimal value) {
        if (equals(client_ai.bondsA, value))
            return;

        client_ai.bondsA = value;

        setModified();
    }

    public BigDecimal getFixedInterestA() {
        return client_ai.interestA;
    }

    public void setFixedInterestA(BigDecimal value) {
        if (equals(client_ai.interestA, value))
            return;

        client_ai.interestA = value;

        setModified();
    }

    public BigDecimal getHomeContentsA() {
        return client_ai.homeContentsA;
    }

    public void setHomeContentsA(BigDecimal value) {
        if (equals(client_ai.homeContentsA, value))
            return;

        client_ai.homeContentsA = value;

        setModified();
    }

    public BigDecimal getCarsEtcA() {
        return client_ai.carsEtcA;
    }

    public void setCarsEtcA(BigDecimal value) {
        if (equals(client_ai.carsEtcA, value))
            return;

        client_ai.carsEtcA = value;

        setModified();
    }

    public BigDecimal getPropertyA() {
        return client_ai.propertyA;
    }

    public void setPropertyA(BigDecimal value) {
        if (equals(client_ai.propertyA, value))
            return;

        client_ai.propertyA = value;

        setModified();
    }

    public BigDecimal getGiftsA() {
        return client_ai.giftsA;
    }

    public void setGiftsA(BigDecimal value) {
        if (equals(client_ai.giftsA, value))
            return;

        client_ai.giftsA = value;

        setModified();
    }

    public BigDecimal getLoansA() {
        return client_ai.loansA;
    }

    public void setLoansA(BigDecimal value) {
        if (equals(client_ai.loansA, value))
            return;

        client_ai.loansA = value;

        setModified();
    }

    public BigDecimal getPropertyI() {
        return client_ai.propertyI;
    }

    public void setPropertyI(BigDecimal value) {
        if (equals(client_ai.propertyI, value))
            return;

        client_ai.propertyI = value;

        setModified();
    }

    public BigDecimal getLoansI() {
        return client_ai.loansI;
    }

    public void setLoansI(BigDecimal value) {
        if (equals(client_ai.loansI, value))
            return;

        client_ai.loansI = value;

        setModified();
    }

    public Boolean getNoChildren() {
        return noChildren;
    }

    public void setNoChildren(Boolean value) {
        if (equals(noChildren, value))
            return;

        noChildren = value;
        setModified();
    }

    public Boolean getSharer() {
        return sharer;
    }

    public void setSharer(Boolean value) {
        if (equals(sharer, value))
            return;

        sharer = value;
        setModified();
    }

    public BigDecimal getClientSalaryI() {
        return client_ai.salaryWagesI;
    }

    public void setClientSalaryI(BigDecimal value) {
        if (equals(client_ai.salaryWagesI, value))
            return;

        client_ai.salaryWagesI = value;

        setModified();
    }

    public BigDecimal getSalaryIPartner() {
        return partner_ai.salaryWagesI;
    }

    public void setPartnerSalaryI(BigDecimal value) {
        if (equals(partner_ai.salaryWagesI, value))
            return;

        partner_ai.salaryWagesI = value;

        setModified();
    }

    // nonEditable

    public BigDecimal getSuperannuationA() {
        return client_ai.superannuationA;
    }

    public void setSuperannuationA(BigDecimal value) {
        if (equals(client_ai.superannuationA, value))
            return;

        client_ai.superannuationA = value;

        setModified();
    }

    public BigDecimal getSavingsI() {
        if (getSavingsA() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getSavingsA().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? (getSavingsA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW))
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getSavingsA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));
            return getSavingsA().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getSavingsA()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getSavingsA()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getManagedFundsI() {
        if (getManagedFundsA() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getManagedFundsA().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getManagedFundsA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getManagedFundsA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getManagedFundsA().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getManagedFundsA()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getManagedFundsA()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;

    }

    public BigDecimal getSharesI() {
        if (getSharesA() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getSharesA().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getSharesA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getSharesA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getSharesA().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getSharesA()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getSharesA()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;

    }

    public BigDecimal getBondsI() {
        if (getBondsA() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getBondsA().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getBondsA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getBondsA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getBondsA()
                    .compareTo(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getBondsA()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getBondsA()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getFixedInterestI() {
        if (getFixedInterestA() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getFixedInterestA().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getFixedInterestA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getFixedInterestA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getFixedInterestA().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getFixedInterestA()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getFixedInterestA()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getGiftsI() {
        if (getGiftsA() != null
                && TaxUtils.GIFTS_THRESHOLD.compareTo(getGiftsA()) < 0
                && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getGiftsA().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getGiftsA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getGiftsA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getGiftsA()
                    .compareTo(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getGiftsA()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getGiftsA()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getSuperannuationI() {
        if (entitledForAgePension()) {
            if (getSuperannuationA() != null && getMaritalStatus() != null) {

                if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                    return getSuperannuationA().compareTo(
                            TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getSuperannuationA()
                            .multiply(TaxUtils.DEEMING_RATES_LOW)
                            : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                    .multiply(TaxUtils.DEEMING_RATES_LOW))
                                    .add((getSuperannuationA()
                                            .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                            .multiply(TaxUtils.DEEMING_RATES_HIGH)));

                return getSuperannuationA().compareTo(
                        TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getSuperannuationA()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getSuperannuationA()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));
            }
        }
        return null;
    }

    public BigDecimal getAllocatedPensionI() {
        return client_ai.allocatedPensionI;
        // return client_ai.allocatedPensionI;
    }

    public void setAllocatedPensionI(BigDecimal value) {
        if (equals(client_ai.allocatedPensionI, value))
            return;

        client_ai.allocatedPensionI = value;

        setModified();
    }

    public BigDecimal getAllocatedPensionA() {
        return client_ai.allocatedPensionA;
        // return client_ai.allocatedPensionI;
    }

    public void setAllocatedPensionA(BigDecimal value) {
        if (equals(client_ai.allocatedPensionA, value))
            return;

        client_ai.allocatedPensionA = value;

        setModified();
    }

    public BigDecimal getComplyingPensionI() {

        return client_ai.complyingPensionI;
    }

    public void setComplyingPensionI(BigDecimal value) {
        if (equals(client_ai.complyingPensionI, value))
            return;

        client_ai.complyingPensionI = value;

        setModified();
    }

    public BigDecimal getComplyingPensionA() {

        return client_ai.complyingPensionA;
    }

    public void setComplyingPensionA(BigDecimal value) {
        if (equals(client_ai.complyingPensionA, value))
            return;

        client_ai.complyingPensionA = value;

        setModified();
    }

    public BigDecimal getTotalI() {

        double sa = 0, ma = 0, sh = 0, bo = 0, fi = 0, ho = 0, ca = 0, pr = 0, gi = 0, lo = 0, su = 0, al = 0, co = 0, cl = 0, pa = 0, cs = 0, ps = 0;

        boolean none = true;

        if (getSavingsI() != null) {
            sa = getSavingsI().doubleValue();
            none = false;
        }

        if (getManagedFundsI() != null) {
            ma = getManagedFundsI().doubleValue();
            none = false;
        }

        if (getSharesI() != null) {
            sh = getSharesI().doubleValue();
            none = false;
        }

        if (getBondsI() != null) {
            bo = getBondsI().doubleValue();
            none = false;
        }

        if (getFixedInterestI() != null) {
            fi = getFixedInterestI().doubleValue();
            none = false;
        }

        /*
         * if ( getHomeContentsI() != null ) { ho =
         * getHomeContentsI().doubleValue(); none = false; }h
         * 
         * if ( getCarsEtcI() != null ) { ca = getCarsEtcI().doubleValue(); none =
         * false; }
         */
        if (getPropertyI() != null) {
            pr = getPropertyI().doubleValue();
            none = false;
        }

        if (getGiftsI() != null) {
            gi = getGiftsI().doubleValue();
            none = false;
        }

        if (getLoansI() != null) {
            lo = getLoansI().doubleValue();
            none = false;
        }

        if (getSuperannuationI() != null) {
            su = getSuperannuationI().doubleValue();
            none = false;
        }

        if (getAllocatedPensionI() != null) {
            al = getAllocatedPensionI().doubleValue();
            none = false;
        }

        if (getComplyingPensionI() != null) {
            co = getComplyingPensionI().doubleValue();
            none = false;
        }

        if (getClientSalaryI() != null) {
            cs = getClientSalaryI().doubleValue();
            none = false;
        }
        /*
         * if ( getSalaryIPartner() != null ) { ps =
         * getSalaryIPartner().doubleValue(); none = false; }
         */
        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + ho + ca + pr + gi
                    + lo + su + al + co + cl + pa + cs + ps);

        return null;
    }

    public BigDecimal getMaxBenefitC() {

        if (agePension() && entitledForAgePension()) {

            if (equals(getMaritalStatus(), MaritalCode.MARRIED)
                    || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                    || equals(getMaritalStatus(), MaritalCode.PARTNERED))
                return TaxUtils.MAX_AGE_PENSION_COUPLE;
            else
                return TaxUtils.MAX_AGE_PENSION_SINGLE;
        }
        /*
         * if ( newStartAllowance()) { if ( equals (single(), Boolean.TRUE) ) {
         * if ( TaxCalc.MEANS_LIMITS_EIGHTEEN_NSA.compareTo( new BigDecimal
         * (getAge()) ) < 0 && !equals (getNoChildren(), Boolean.TRUE) ) return
         * TaxCalc.MAX_SINGLE_CHILDREN_NSA;
         * 
         * if ( TaxCalc.MEANS_LIMITS_TWENTY_ONE_NSA.compareTo( new BigDecimal(
         * getAge() )) <= 0 ) return TaxCalc.MAX_SINGLE_NO_CHILDREN_NSA; }
         * 
         * if ( !equals (single(), Boolean.TRUE) ) if (
         * TaxCalc.MEANS_LIMITS_EIGHTEEN_NSA.compareTo( new BigDecimal
         * (getAge()) ) < 0 && !equals (getNoChildren(), Boolean.TRUE) ||
         * TaxCalc.MEANS_LIMITS_TWENTY_ONE_NSA.compareTo( new BigDecimal(
         * getAge() ))< 0 ) return TaxCalc.MAX_PARTNERED_NSA;
         *  } // throw new IllegalArgumentException( "Unknown MaritalStatus: " +
         * getMaritalStatus() );
         */
        return ZERO;
    }

    public BigDecimal getAssetTestC() {
        BigDecimal total = null;
        BigDecimal help = null;

        if (!agePension() && !entitledForAgePension())
            return ZERO;

        // IF(B37<=I39,F17,F17-(((B37-I39)/1000)*J39))
        if (getTotalAForAssetsTestJoint() == null || getMeansLimits() == null
                || getMaxBenefitC() == null)
            return ZERO;

        // IF(B37 <= I39, F17
        // - B37 = getTotalAssetsSubjectToDeeming()
        // - I39 = getMeansLimits()
        // - F17 = getMaxBenefitC() (the Maximum Benefit value)
        //
        // !!! I39 check if single/couple and home owner/non home owner !!!
        if (getTotalAForAssetsTestJoint().compareTo(getMeansLimits()) <= 0) {
            // F17
            return getMaxBenefitC();
        }

        // calculate age pension per fortnight for single or couple?
        if (Boolean.TRUE.equals(single())) {
            // single!
            // F17-(((B37-I39)/1000)*J39)
            // F17
            total = getMaxBenefitC();
            // (B37 - I39) / 1000.00 ) * J39
            help = getTotalAForAssetsTestJoint().subtract(getMeansLimits());
            help = help.divide(new BigDecimal(1000.0), BIGDECIMAL_SCALE,
                    BigDecimal.ROUND_HALF_UP);
            help = help.multiply(TaxUtils.MEANS_LIMITS_ASSETS_FACTOR);
            // (F17*2.0)-(((B37-I39)/1000)*J39)
            // age pension single (fortnightly)
            total = total.subtract(help);

        } else {
            // couple!
            // couple age pension = (F17*2.0)-(((B37-I39)/1000)*J39)
            // !!! (F17*2.0), because each person gets age pension !!!
            // (F17 * 2.0)
            total = getMaxBenefitC().multiply(new BigDecimal(2.0));
            // (B37 - I39) / 1000.00 ) * J39
            help = getTotalAForAssetsTestJoint().subtract(getMeansLimits());
            help = help.divide(new BigDecimal(1000.0), BIGDECIMAL_SCALE,
                    BigDecimal.ROUND_HALF_UP);
            help = help.multiply(TaxUtils.MEANS_LIMITS_ASSETS_FACTOR);
            // (F17*2.0)-(((B37-I39)/1000)*J39)
            total = total.subtract(help);

            // age pension per person (fortnightly)
            total = total.divide(new BigDecimal(2.0), BIGDECIMAL_SCALE,
                    BigDecimal.ROUND_HALF_UP);
        }

        if (total == null)
            return ZERO;

        return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;

    }

    public BigDecimal getMeansLimits() {
        return couple() ? (getHomeOwner() ? TaxUtils.MEANS_LIMITS_ASSETS_HO_COUPLE
                : TaxUtils.MEANS_LIMITS_ASSETS_NH_COUPLE)
                : (getHomeOwner() ? TaxUtils.MEANS_LIMITS_ASSETS_HO_SINGLE
                        : TaxUtils.MEANS_LIMITS_ASSETS_NH_SINGLE);
    }

    public Boolean single() {
        if (getMaritalStatus() == null)
            return null;

        if (equals(getMaritalStatus(), MaritalCode.MARRIED)
                || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                || equals(getMaritalStatus(), MaritalCode.SEPARATED_HEALTH)
                || equals(getMaritalStatus(), MaritalCode.PARTNERED))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    public boolean couple() {
        return equals(getMaritalStatus(), MaritalCode.MARRIED)
                || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                || equals(getMaritalStatus(), MaritalCode.PARTNERED);
    }

    public BigDecimal getIncomeTestC() {
        // IF((B39/26)<=I47,F17,F17-(((B39/26)-I47)*J47))
        BigDecimal total = null;

        if (!agePension() && !entitledForAgePension())
            return ZERO;

        if (getIncomeTestFactor() == null || getMaxBenefitC() == null
                || getTotalJointIFortnightly() == null
                || getMeansLimitsIncome() == null)
            return ZERO;

        // (getIncomeFortnightly() = Total Income/26
        if (getTotalJointIFortnightly().compareTo(getPartMeansLimitsIncome()) >= 0)
            return new BigDecimal(0);
        if (getTotalJointIFortnightly().compareTo(getMeansLimitsIncome()) <= 0)
            return getMaxBenefitC(); // return F17

        // BT SUPER BOOK,Social Security, page 208
        if (agePension() && entitledForAgePension()) {
            // <pension income threshold> - <income assessed fortnightly>
            total = getTotalJointIFortnightly()
                    .subtract(getMeansLimitsIncome());
            // * <applicable reduction factor>
            total = total.multiply(getIncomeTestFactor());
            // total contains now "the income test reduction amount"
            // <max pension> - <income test reduction amount>
            total = getMaxBenefitC().subtract(total);
        }

        /*
         * if ( newStartAllowance() ) {
         * 
         * if ( TaxCalc.MEANS_LIMITS_INCOME_LOW_NSA.compareTo(
         * getIncomeFortnightly()) < 0 &&
         * TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA.compareTo(
         * getIncomeFortnightly() ) >= 0 ) total = getMaxBenefitC().subtract ((
         * getIncomeFortnightly().subtract (getMeansLimitsIncome())).multiply (
         * getIncomeTestFactor() ));
         * 
         * 
         * if ( TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA.compareTo(
         * getIncomeFortnightly() ) < 0 ) total = getMaxBenefitC().subtract
         * (((TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA.subtract(
         * TaxCalc.MEANS_LIMITS_INCOME_LOW_NSA )).multiply(
         * TaxCalc.MEANS_LIMITS_INCOME_FIRST_FACTOR_NSA).add((
         * getIncomeFortnightly().subtract
         * (TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA)).multiply (
         * getIncomeTestFactor() ))));
         * 
         *  }
         */
        if (total == null)
            return ZERO;

        return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;

    }

    public BigDecimal getIncomeFortnightly() {

        if (getTotalIncome() == null)
            return null;

        return getTotalIncome().divide(
                new BigDecimal(DateTimeUtils.FORTNIGHTS_PER_YEAR),
                BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);

    }

    public boolean agePension() {
        return equals(BenefitTypeCode.AGE_PENSION, getBenefitType());

    }

    public boolean agePensionPartner() {
        return equals(BenefitTypeCode.AGE_PENSION, getPartnerBenefitType());

    }

    public boolean newStartAllowance() {
        return equals(BenefitTypeCode.NEW_START_ALLOWANCE, getBenefitType());

    }

    public boolean noBenefitType() {
        return equals(BenefitTypeCode.NONE, getBenefitType());

    }

    public boolean newStartAllowancePartner() {
        return equals(BenefitTypeCode.NEW_START_ALLOWANCE,
                getPartnerBenefitType());

    }

    public BigDecimal getIncomeTestFactor() {

        if (agePension() && entitledForAgePension())
            return equals(single(), Boolean.TRUE) ? TaxUtils.MEANS_LIMITS_INCOME_SINGLE_FACTOR
                    : TaxUtils.MEANS_LIMITS_INCOME_COUPLE_FACTOR;

        if (newStartAllowance()) {

            if (getIncomeFortnightly() == null)
                return null;

            if (TaxUtils.MEANS_LIMITS_INCOME_LOW_NSA
                    .compareTo(getIncomeFortnightly()) < 0
                    && TaxUtils.MEANS_LIMITS_INCOME_HIGH_NSA
                            .compareTo(getIncomeFortnightly()) >= 0)
                return TaxUtils.MEANS_LIMITS_INCOME_FIRST_FACTOR_NSA;

            if (TaxUtils.MEANS_LIMITS_INCOME_HIGH_NSA
                    .compareTo(getIncomeFortnightly()) < 0)
                return TaxUtils.MEANS_LIMITS_INCOME_SECOND_FACTOR_NSA;
        }

        return null;
    }

    public BigDecimal getIncomeTestFactorPartner() {

        if (agePensionPartner() && entitledForAgePensionPartner())
            return TaxUtils.MEANS_LIMITS_INCOME_COUPLE_FACTOR;

        if (newStartAllowancePartner()) {

            if (getIncomeFortnightly() == null)
                return null;

            if (TaxUtils.MEANS_LIMITS_INCOME_LOW_NSA
                    .compareTo(getIncomeFortnightly()) < 0
                    && TaxUtils.MEANS_LIMITS_INCOME_HIGH_NSA
                            .compareTo(getIncomeFortnightly()) >= 0)
                return TaxUtils.MEANS_LIMITS_INCOME_FIRST_FACTOR_NSA;

            if (TaxUtils.MEANS_LIMITS_INCOME_HIGH_NSA
                    .compareTo(getIncomeFortnightly()) < 0)
                return TaxUtils.MEANS_LIMITS_INCOME_SECOND_FACTOR_NSA;
        }

        return null;
    }

    public BigDecimal getMeansLimitsIncome() {

        if (agePension() && entitledForAgePension()) {
            if (equals(single(), Boolean.TRUE)) {
                if (getChildrenAmount() != null)
                    return TaxUtils.MEANS_LIMITS_INCOME_SINGLE
                            .add(TaxUtils.MEANS_LIMITS_INCOME_CHILDREN
                                    .multiply(new BigDecimal(
                                            getChildrenAmount())));
                return TaxUtils.MEANS_LIMITS_INCOME_SINGLE;
            }

            return getChildrenAmount() == null ? TaxUtils.MEANS_LIMITS_INCOME_COUPLE
                    : TaxUtils.MEANS_LIMITS_INCOME_COUPLE
                            .add(TaxUtils.MEANS_LIMITS_INCOME_CHILDREN
                                    .multiply(new BigDecimal(
                                            getChildrenAmount())));

        }

        if (newStartAllowance()) {

            if (equals(single(), Boolean.TRUE)) {
                if (TaxUtils.MEANS_LIMITS_TWENTY_ONE_NSA
                        .compareTo(new BigDecimal(getAge())) <= 0
                        && TaxUtils.MEANS_LIMITS_SIXTY_NSA
                                .compareTo(new BigDecimal(getAge())) >= 0)
                    return TaxUtils.MEANS_LIMITS_INCOME_SINGLE_TWENTYFIRST_OR_OVER_NSA;
                if (TaxUtils.MEANS_LIMITS_SIXTY_NSA.compareTo(new BigDecimal(
                        getAge())) < 0)
                    return TaxUtils.MEANS_LIMITS_INCOME_SINGLE_SIXTY_OR_OVER_NSA;
                if (TaxUtils.MEANS_LIMITS_EIGHTEEN_NSA.compareTo(new BigDecimal(
                        getAge())) < 0
                        && !equals(getNoChildren(), Boolean.TRUE))
                    return TaxUtils.MEANS_LIMITS_INCOME_EIGHTEEN_MORE_CHILDREN_NSA;

            } else {
                if ((TaxUtils.MEANS_LIMITS_EIGHTEEN_NSA
                        .compareTo(new BigDecimal(getAge())) < 0 && !equals(
                        getNoChildren(), Boolean.TRUE))
                        || TaxUtils.MEANS_LIMITS_TWENTY_ONE_NSA
                                .compareTo(new BigDecimal(getAge())) < 0)
                    return TaxUtils.MEANS_LIMITS_INCOME_PARTNER_NSA;
            }

        }
        return null;
    }

    public BigDecimal getMeansLimitsIncomePartner() {

        if (agePensionPartner() && entitledForAgePensionPartner()) {

            return getChildrenAmount() == null ? TaxUtils.MEANS_LIMITS_INCOME_COUPLE
                    : TaxUtils.MEANS_LIMITS_INCOME_COUPLE
                            .add(TaxUtils.MEANS_LIMITS_INCOME_CHILDREN
                                    .multiply(new BigDecimal(
                                            getChildrenAmount())));

        }

        if (newStartAllowancePartner())

            if ((TaxUtils.MEANS_LIMITS_EIGHTEEN_NSA.compareTo(new BigDecimal(
                    getAge(getPartnerDateOfBirth()))) < 0 && !equals(
                    getNoChildren(), Boolean.TRUE))
                    || TaxUtils.MEANS_LIMITS_TWENTY_ONE_NSA
                            .compareTo(new BigDecimal(
                                    getAge(getPartnerDateOfBirth()))) < 0)
                return TaxUtils.MEANS_LIMITS_INCOME_PARTNER_NSA;

        return null;
    }

    public BigDecimal getPartMeansLimitsIncome() {

        if (agePension() && entitledForAgePension()) {
            if (equals(single(), Boolean.TRUE)) {
                if (getChildrenAmount() != null)
                    return TaxUtils.MEANS_LIMITS_INCOME_PART_SINGLE
                            .add(TaxUtils.MEANS_LIMITS_INCOME_CHILDREN
                                    .multiply(new BigDecimal(
                                            getChildrenAmount())));
                return TaxUtils.MEANS_LIMITS_INCOME_PART_SINGLE;
            }

            return getChildrenAmount() == null ? TaxUtils.MEANS_LIMITS_INCOME_PART_COUPLE
                    : TaxUtils.MEANS_LIMITS_INCOME_PART_COUPLE
                            .add(TaxUtils.MEANS_LIMITS_INCOME_CHILDREN
                                    .multiply(new BigDecimal(
                                            getChildrenAmount())));

        }

        if (newStartAllowance()) {

            if (equals(single(), Boolean.TRUE)) {
                if (TaxUtils.MEANS_LIMITS_TWENTY_ONE_NSA
                        .compareTo(new BigDecimal(getAge())) <= 0
                        && TaxUtils.MEANS_LIMITS_SIXTY_NSA
                                .compareTo(new BigDecimal(getAge())) >= 0)
                    return TaxUtils.MEANS_LIMITS_INCOME_PART_SINGLE_TWENTYFIRST_OR_OVER_NSA;
                if (TaxUtils.MEANS_LIMITS_SIXTY_NSA.compareTo(new BigDecimal(
                        getAge())) < 0)
                    return TaxUtils.MEANS_LIMITS_INCOME_PART_SINGLE_SIXTY_OR_OVER_NSA;
                if (TaxUtils.MEANS_LIMITS_EIGHTEEN_NSA.compareTo(new BigDecimal(
                        getAge())) < 0
                        && !equals(getNoChildren(), Boolean.TRUE))
                    return TaxUtils.MEANS_LIMITS_INCOME_PART_EIGHTEEN_MORE_CHILDREN_NSA;

            } else {
                if ((TaxUtils.MEANS_LIMITS_EIGHTEEN_NSA
                        .compareTo(new BigDecimal(getAge())) < 0 && !equals(
                        getNoChildren(), Boolean.TRUE))
                        || TaxUtils.MEANS_LIMITS_TWENTY_ONE_NSA
                                .compareTo(new BigDecimal(getAge())) < 0)
                    return TaxUtils.MEANS_LIMITS_INCOME_PART_PARTNER_NSA;
            }

        }
        return null;
    }

    public BigDecimal getPartMeansLimitsIncomePartner() {

        if (agePensionPartner() && entitledForAgePensionPartner()) {

            return getChildrenAmount() == null ? TaxUtils.MEANS_LIMITS_INCOME_PART_COUPLE
                    : TaxUtils.MEANS_LIMITS_INCOME_PART_COUPLE
                            .add(TaxUtils.MEANS_LIMITS_INCOME_CHILDREN
                                    .multiply(new BigDecimal(
                                            getChildrenAmount())));

        }

        if (newStartAllowancePartner()) {

            if ((TaxUtils.MEANS_LIMITS_EIGHTEEN_NSA.compareTo(new BigDecimal(
                    getAge(getPartnerDateOfBirth()))) < 0 && !equals(
                    getNoChildren(), Boolean.TRUE))
                    || TaxUtils.MEANS_LIMITS_TWENTY_ONE_NSA
                            .compareTo(new BigDecimal(
                                    getAge(getPartnerDateOfBirth()))) < 0)
                return TaxUtils.MEANS_LIMITS_INCOME_PART_PARTNER_NSA;

        }
        return null;
    }

    public BigDecimal getBasicBenefitC() {

        if (getIncomeTestC() != null && getAssetTestC() != null)
            return getAssetTestC().min(getIncomeTestC());
        else if (getIncomeTestC() != null)
            return getIncomeTestC();
        else if (getAssetTestC() != null)
            return getAssetTestC();

        return null;

    }

    public BigDecimal getPharmAllowanceC() {

        Integer client_benefit_type = getBenefitType();
        Integer partner_benefit_type = getPartnerBenefitType();

        if (client_benefit_type == null)
            return ZERO;

        // client gets Age Pension and is single
        // ==> client = $5.80, partner = $0.00
        if (BenefitTypeCode.AGE_PENSION.equals(client_benefit_type)) {
            if (getMaritalStatus() == null || !entitledForAgePension())
                return null;

            if (equals(getMaritalStatus(), MaritalCode.SINGLE))
                return TaxUtils.PHARMACUETICAL_ALLOWANCE_SINGLE;
        }

        // client gets Age Pension, is not single and partner's Benefit Type =
        // EMPTY
        // ==> client = $2.90, , partner = $0.00
        if (BenefitTypeCode.AGE_PENSION.equals(client_benefit_type)) {
            if (getMaritalStatus() == null || !entitledForAgePension()
                    || partner_benefit_type == null)
                return null;

            if (!equals(getMaritalStatus(), MaritalCode.SINGLE)
                    && BenefitTypeCode.NONE.equals(partner_benefit_type))
                return TaxUtils.PHARMACUETICAL_ALLOWANCE_COUPLE;
        }

        // client gets Age Pension, is not single and partner's Benefit Type =
        // EMPTY
        // ==> client = $2.90, , partner = $2.90
        if (BenefitTypeCode.AGE_PENSION.equals(client_benefit_type)) {
            if (getMaritalStatus() == null || !entitledForAgePension()
                    || partner_benefit_type == null)
                return null;

            if (!equals(getMaritalStatus(), MaritalCode.SINGLE)
                    && BenefitTypeCode.AGE_PENSION.equals(partner_benefit_type))
                return TaxUtils.PHARMACUETICAL_ALLOWANCE_COUPLE;
        }

        // client gets Age Pension, is not single and partner's Benefit Type =
        // Newstart
        // ==> client = $2.90, , partner = $0.00
        if (BenefitTypeCode.AGE_PENSION.equals(client_benefit_type)) {
            if (getMaritalStatus() == null || !entitledForAgePension()
                    || partner_benefit_type == null)
                return null;

            if (!equals(getMaritalStatus(), MaritalCode.SINGLE)
                    && BenefitTypeCode.NEW_START_ALLOWANCE
                            .equals(partner_benefit_type))
                return TaxUtils.PHARMACUETICAL_ALLOWANCE_COUPLE;
        }
        /*
         * if ( agePension() && entitledForAgePension()) { if (
         * getMaritalStatus() == null || !entitledForAgePension() ) return null;
         * 
         * if ( equals( getMaritalStatus(), MaritalCode.SINGLE ) ) return
         * TaxCalc.PHARMACUETICAL_ALLOWANCE_SINGLE ;
         * 
         * if ( equals( getMaritalStatus(), MaritalCode.MARRIED ) || equals(
         * getMaritalStatus(), MaritalCode.DEFACTO )) return
         * TaxCalc.PHARMACUETICAL_ALLOWANCE_COUPLE ;
         * 
         * throw new IllegalArgumentException( "Unknown MaritalStatus: " +
         * getMaritalStatus() );
         *  }
         */
        return ZERO; // null;

    }

    public BigDecimal getRentAssistanceC() {

        // IF(AND(B14="Yes",B15>=I61),IF(B15>=I65,I57,(B15-I61)*0.75),0)
        // ???
        // =(IF(AND(B14="Yes",B15>=I61),IF(B15>=I65,I57,(B15-I61)*0.75),0))/2
        BigDecimal total = null;

        if (getRent() != null) {

            if ((equals(getMaritalStatus(), MaritalCode.SINGLE) && equals(
                    getNoChildren(), Boolean.TRUE))
                    || (equals(getMaritalStatus(), MaritalCode.SEPARATED_HEALTH) && equals(
                            getNoChildren(), Boolean.TRUE))) {

                if (equals(getSharer(), Boolean.TRUE)) {
                    if (getRent().compareTo(
                            TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER) <= 0) {
                        total = ZERO;
                    } else {
                        total = getRent()
                                .compareTo(
                                        TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER) <= 0 ? (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                                : TaxUtils.MAXIMUM_RENT_ASSISTANCE_SHARER;
                    }
                } else {
                    if (getRent().compareTo(
                            TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD) <= 0) {
                        total = ZERO;
                    } else {
                        total = getRent().compareTo(
                                TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD) <= 0 ? (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                                : TaxUtils.MAXIMUM_RENT_ASSIATANCE;
                    }
                }
            }

            if ((equals(getMaritalStatus(), MaritalCode.MARRIED)
                    || equals(getMaritalStatus(), MaritalCode.DEFACTO) || equals(
                    getMaritalStatus(), MaritalCode.PARTNERED))
                    && equals(getNoChildren(), Boolean.TRUE)) {
                if (getRent().compareTo(
                        TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE) <= 0) {
                    total = ZERO;
                } else {
                    total = getRent().compareTo(
                            TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE) <= 0 ? (getRent()
                            .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE))
                            .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                            : TaxUtils.MAXIMUM_RENT_ASSIATANCE_COUPLE;
                }
            }

            if (equals(getMaritalStatus(), MaritalCode.SEPARATED_HEALTH)
                    && equals(getNoChildren(), Boolean.TRUE)) {
                if (getRent().compareTo(
                        TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED) <= 0) {
                    total = ZERO;
                } else {
                    total = getRent()
                            .compareTo(
                                    TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED) <= 0 ? (getRent()
                            .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED))
                            .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                            : TaxUtils.MAXIMUM_RENT_ASSIATANCE_SEPARATED;
                }
            }

            if (equals(getMaritalStatus(), MaritalCode.SINGLE)
                    && this.getChildrenAmount() != null) {
                // single with 1 or 2 children
                if ((this.getChildrenAmount().compareTo(new BigInteger("1")) >= 0)
                        && (this.getChildrenAmount().compareTo(
                                new BigInteger("2")) <= 0)) {
                    if (getRent()
                            .compareTo(
                                    TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_1_OR_2) <= 0) {
                        total = ZERO;
                    } else {
                        total = getRent()
                                .compareTo(
                                        TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_1_OR_2) <= 0 ? (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_1_OR_2))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                                : TaxUtils.MAXIMUM_RENT_ASSIATANCE_SINGLE_1_OR_2;
                    }

                }

                // single with 3 or more children
                if (this.getChildrenAmount().compareTo(new BigInteger("3")) >= 0) {
                    if (getRent()
                            .compareTo(
                                    TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_3_OR_MORE) <= 0) {
                        total = ZERO;
                    } else {
                        total = getRent()
                                .compareTo(
                                        TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_3_OR_MORE) <= 0 ? (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_3_OR_MORE))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                                : TaxUtils.MAXIMUM_RENT_ASSIATANCE_SINGLE_3_OR_MORE;
                    }
                }
            }

            if ((equals(getMaritalStatus(), MaritalCode.MARRIED)
                    || equals(getMaritalStatus(), MaritalCode.DEFACTO) || equals(
                    getMaritalStatus(), MaritalCode.PARTNERED))
                    && this.getChildrenAmount() != null) {
                // couple with 1 or 2 children
                if ((this.getChildrenAmount().compareTo(new BigInteger("1")) >= 0)
                        && (this.getChildrenAmount().compareTo(
                                new BigInteger("2")) <= 0)) {
                    if (getRent()
                            .compareTo(
                                    TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_1_OR_2) <= 0) {
                        total = ZERO;
                    } else {
                        total = getRent()
                                .compareTo(
                                        TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_1_OR_2) <= 0 ? (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_1_OR_2))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                                : TaxUtils.MAXIMUM_RENT_ASSIATANCE_COUPLE_1_OR_2;
                    }
                }

                // couple with 3 or more children
                if (this.getChildrenAmount().compareTo(new BigInteger("3")) >= 0) {
                    if (getRent()
                            .compareTo(
                                    TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_3_OR_MORE) <= 0) {
                        total = ZERO;
                    } else {
                        total = getRent()
                                .compareTo(
                                        TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_3_OR_MORE) <= 0 ? (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_3_OR_MORE))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR)
                                : TaxUtils.MAXIMUM_RENT_ASSIATANCE_COUPLE_3_OR_MORE;
                    }
                }
            }
        }
        if (total == null)
            return ZERO;

        return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;
        /*
         * BigDecimal total = null;
         * 
         * if ( getRent() != null ) {
         * 
         * if ( (equals( getMaritalStatus(), MaritalCode.SINGLE ) && equals (
         * getNoChildren(), Boolean.TRUE)) || (equals( getMaritalStatus(),
         * MaritalCode.SEPARATED_HEALTH ) && equals ( getNoChildren(),
         * Boolean.TRUE))) {
         * 
         * if ( equals ( getSharer(), Boolean.TRUE ) ) total =
         * (getRent().compareTo(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER ) >= 0? (
         * getRent().compareTo( TaxCalc.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER ) >
         * 0 ? TaxCalc.MAXIMUM_RENT_ASSISTANCE_SHARER : (getRent().subtract(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER )).multiply (
         * TaxCalc.RENT_ASSISTANCE_FACTOR )) : new BigDecimal( 0 )) ;
         * 
         * total = (getRent().compareTo(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD ) >= 0 ? (
         * getRent().compareTo( TaxCalc.MAXIMUM_RENT_ASSISTANCE_THRESHOLD ) > 0 ?
         * TaxCalc.MAXIMUM_RENT_ASSIATANCE : (getRent().subtract(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD)).multiply(
         * TaxCalc.RENT_ASSISTANCE_FACTOR )) : new BigDecimal( 0 )); } if (
         * (equals( getMaritalStatus(), MaritalCode.MARRIED ) || equals(
         * getMaritalStatus(), MaritalCode.DEFACTO ) || equals(
         * getMaritalStatus(), MaritalCode.PARTNERED )) && equals (
         * getNoChildren(), Boolean.TRUE)) { if (getRent().compareTo(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE ) >= 0 ) { if (
         * getRent().compareTo( TaxCalc.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE ) >
         * 0 ) return TaxCalc.MAXIMUM_RENT_ASSIATANCE_COUPLE; else total =
         * (getRent().subtract(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE)).multiply(
         * TaxCalc.RENT_ASSISTANCE_FACTOR ); } //return new BigDecimal( 0 ); }
         * if ( equals( getMaritalStatus(), MaritalCode.SEPARATED ) && equals (
         * getNoChildren(), Boolean.TRUE)) total = (getRent().compareTo(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED ) >= 0 ? (
         * getRent().compareTo(
         * TaxCalc.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED ) > 0 ?
         * TaxCalc.MAXIMUM_RENT_ASSIATANCE_SEPARATED : (getRent().subtract(
         * TaxCalc.MINIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED)).multiply(
         * TaxCalc.RENT_ASSISTANCE_FACTOR )) : new BigDecimal( 0 ));
         *  } if ( total == null ) return ZERO;
         * 
         * return new BigDecimal(0).compareTo( total ) > 0 ? ZERO : total;
         */
    }

    public BigDecimal getMaxAgePensionPerYear() {
        if (equals(getMaritalStatus(), MaritalCode.SINGLE))
            return TaxUtils.MAX_AGE_PENSION_SINGLE.multiply(new BigDecimal(
                    DateTimeUtils.FORTNIGHTS_PER_YEAR));

        if (equals(getMaritalStatus(), MaritalCode.MARRIED)
                || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                || equals(getMaritalStatus(), MaritalCode.PARTNERED))
            return TaxUtils.MAX_AGE_PENSION_COUPLE.multiply(new BigDecimal(
                    DateTimeUtils.FORTNIGHTS_PER_YEAR));

        if (equals(getMaritalStatus(), MaritalCode.SEPARATED_HEALTH))
            return TaxUtils.MAX_AGE_PENSION_COUPLE.multiply(new BigDecimal(
                    DateTimeUtils.FORTNIGHTS_PER_YEAR));

        return null;

    }

    public BigDecimal getPensionerRebateC() {
        // IF(B10="Couple",IF(I23+C35<=J70,I70,I70-((I23+C35-J70)*0.125)),IF(I23+C35<=J71,I71,I71-((I23+C35-J71)*0.125)))

        if (agePension() && entitledForAgePension()) {
            BigDecimal total = null;
            if (getMaritalStatus() == null || getTotalI() == null)
                return ZERO;

            if (equals(getMaritalStatus(), MaritalCode.SINGLE)) {
                if ((getMaxAgePensionPerYear().add(getTotalI()))
                        .compareTo(TaxUtils.PENSIONER_REBATE_SINGLE_THRESHOLD) <= 0)
                    return TaxUtils.PENSIONER_REBATE_SINGLE;
                else
                    total = TaxUtils.PENSIONER_REBATE_SINGLE
                            .subtract(((getMaxAgePensionPerYear()
                                    .add(getTotalI()))
                                    .subtract(TaxUtils.PENSIONER_REBATE_SINGLE_THRESHOLD))
                                    .multiply(TaxUtils.PENSIONER_REBATE_REDUCTION_FACTOR));
            }

            if (equals(getMaritalStatus(), MaritalCode.MARRIED)
                    || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                    || equals(getMaritalStatus(), MaritalCode.PARTNERED)) {
                if ((getMaxAgePensionPerYear().add(getTotalI()))
                        .compareTo(TaxUtils.PENSIONER_REBATE_COUPLE_THRESHOLD) <= 0)
                    return TaxUtils.PENSIONER_REBATE_COUPLE;
                else
                    total = TaxUtils.PENSIONER_REBATE_COUPLE
                            .subtract(((getMaxAgePensionPerYear()
                                    .add(getTotalI()))
                                    .subtract(TaxUtils.PENSIONER_REBATE_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.PENSIONER_REBATE_REDUCTION_FACTOR));
            }
            if (equals(getMaritalStatus(), MaritalCode.SEPARATED_HEALTH)) {
                if ((getMaxAgePensionPerYear().add(getTotalI()))
                        .compareTo(TaxUtils.PENSIONER_REBATE_SEPARATED_HEALTH_THRESHOLD) <= 0)
                    return TaxUtils.PENSIONER_REBATE_SEPARATED_HEALTH;
                else
                    total = TaxUtils.PENSIONER_REBATE_SEPARATED_HEALTH
                            .subtract(((getMaxAgePensionPerYear()
                                    .add(getTotalI()))
                                    .subtract(TaxUtils.PENSIONER_REBATE_SEPARATED_HEALTH_THRESHOLD))
                                    .multiply(TaxUtils.PENSIONER_REBATE_REDUCTION_FACTOR));

            }
            // throw new IllegalArgumentException( "Unknown MaritalStatus: " +
            // getMaritalStatus() );

            if (total == null)
                return ZERO;

            return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;
        }

        return ZERO;

    }

    public BigDecimal getMaxBenefitP() {
        if (agePensionPartner() && entitledForAgePensionPartner()) {
            return TaxUtils.MAX_AGE_PENSION_COUPLE;
        }
        /*
         * // client gets age pension and partner gets none if ( agePension() &&
         * entitledForAgePension() && getPartnerBenefitType() != null &&
         * BenefitTypeCode.NONE.equals( getPartnerBenefitType() ) ) { return
         * TaxCalc.MAX_AGE_PENSION_COUPLE ; }
         */
        /*
         * if ( newStartAllowancePartner()) { if (
         * TaxCalc.MEANS_LIMITS_EIGHTEEN_NSA.compareTo( new BigDecimal
         * (getAge()) ) < 0 && !equals (getNoChildren(), Boolean.TRUE) ||
         * TaxCalc.MEANS_LIMITS_TWENTY_ONE_NSA.compareTo( new BigDecimal(
         * getAge() ))< 0 ) return TaxCalc.MAX_PARTNERED_NSA;
         *  } // throw new IllegalArgumentException( "Unknown MaritalStatus: " +
         * getMaritalStatus() );
         */
        return ZERO;

    }

    public BigDecimal getAssetTestP() {
        BigDecimal total = null;
        BigDecimal help = null;

        if (!agePensionPartner() && !entitledForAgePensionPartner())
            return ZERO;

        // IF(B37<=I39,F17,F17-(((B37-I39)/1000)*J39))
        if (getTotalAForAssetsTestJoint() == null || getMeansLimits() == null
                || getMaxBenefitC() == null || getMaritalStatus() == null)
            return null;

        if (equals(getMaritalStatus(), MaritalCode.MARRIED)
                || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                || equals(getMaritalStatus(), MaritalCode.PARTNERED)) {

            // IF(B37 <= I39, F17
            // - B37 = getTotalAssetsSubjectToDeeming()
            // - I39 = getMeansLimits()
            // - F17 = getMaxBenefitC() (the Maximum Benefit value)
            //
            // !!! I39 check if single/couple and home owner/non home owner !!!
            if (getTotalAForAssetsTestJoint().compareTo(getMeansLimits()) <= 0) {
                // F17
                return getMaxBenefitC();
            }

            // calculate age pension per fortnight for single or couple?
            if (single().equals(Boolean.TRUE)) {
                // single!
                // F17-(((B37-I39)/1000)*J39)
                total = new BigDecimal(
                // F17 -
                        getMaxBenefitC().doubleValue() -
                        // ((B37 - I39) / 1000.00 ) *
                                ((getTotalAForAssetsTestJoint().doubleValue() - getMeansLimits()
                                        .doubleValue()) / 1000.00) *
                                // J39
                                TaxUtils.MEANS_LIMITS_ASSETS_FACTOR
                                        .doubleValue());
                // F17
                total = getMaxBenefitC();
                // (B37 - I39) / 1000.00 ) * J39
                help = getTotalAForAssetsTestJoint().subtract(getMeansLimits());
                help = help.divide(new BigDecimal(1000.0), BIGDECIMAL_SCALE,
                        BigDecimal.ROUND_HALF_UP);
                help = help.multiply(TaxUtils.MEANS_LIMITS_ASSETS_FACTOR);
                // (F17*2.0)-(((B37-I39)/1000)*J39)
                // age pension single (fortnightly)
                total = total.subtract(help);

            } else {
                // couple!
                // couple age pension = (F17*2.0)-(((B37-I39)/1000)*J39)
                // !!! (F17*2.0), because each person gets age pension !!!
                // (F17 * 2.0)
                total = getMaxBenefitC().multiply(new BigDecimal(2.0));
                // (B37 - I39) / 1000.00 ) * J39
                help = getTotalAForAssetsTestJoint().subtract(getMeansLimits());
                help = help.divide(new BigDecimal(1000.0), BIGDECIMAL_SCALE,
                        BigDecimal.ROUND_HALF_UP);
                help = help.multiply(TaxUtils.MEANS_LIMITS_ASSETS_FACTOR);
                // (F17*2.0)-(((B37-I39)/1000)*J39)
                total = total.subtract(help);

                // age pension per person (fortnightly)
                total = total.divide(new BigDecimal(2.0), BIGDECIMAL_SCALE,
                        BigDecimal.ROUND_HALF_UP);
            }

            if (total == null)
                return ZERO;

            return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;
        }
        return ZERO;

    }

    public BigDecimal getIncomeTestP() {
        BigDecimal total = null;

        if (!agePensionPartner() && !entitledForAgePensionPartner())
            return ZERO;

        if (getIncomeTestFactorPartner() == null || getMaxBenefitP() == null
                || getTotalJointIFortnightly() == null
                || getPartMeansLimitsIncomePartner() == null)
            return ZERO;

        // (getIncomeFortnightly() = Total Income/26
        if (getTotalJointIFortnightly().compareTo(
                getPartMeansLimitsIncomePartner()) >= 0)
            return new BigDecimal(0);
        if (getTotalJointIFortnightly()
                .compareTo(getMeansLimitsIncomePartner()) <= 0)
            return getMaxBenefitP(); // return F17

        // BT SUPER BOOK,Social Security, page 208
        if (agePensionPartner() && entitledForAgePensionPartner()) {
            // <pension income threshold> - <income assessed fortnightly>
            total = getTotalJointIFortnightly().subtract(
                    getMeansLimitsIncomePartner());
            // * <applicable reduction factor>
            total = total.multiply(getIncomeTestFactorPartner());
            // total contains now "the income test reduction amount"
            // <max pension> - <income test reduction amount>
            total = getMaxBenefitP().subtract(total);
        }

        /*
         * if ( newStartAllowancePartner() ) {
         * 
         * if ( TaxCalc.MEANS_LIMITS_INCOME_LOW_NSA.compareTo(
         * getIncomeFortnightly()) < 0 &&
         * TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA.compareTo(
         * getIncomeFortnightly() ) >= 0 ) total = getMaxBenefitP().subtract ((
         * getIncomeFortnightly().subtract
         * (getMeansLimitsIncomePartner())).multiply (
         * getIncomeTestFactorPartner() ));
         * 
         * 
         * if ( TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA.compareTo(
         * getIncomeFortnightly() ) < 0 ) total = getMaxBenefitP().subtract
         * (((TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA.subtract(
         * TaxCalc.MEANS_LIMITS_INCOME_LOW_NSA )).multiply(
         * TaxCalc.MEANS_LIMITS_INCOME_FIRST_FACTOR_NSA).add((
         * getIncomeFortnightly().subtract
         * (TaxCalc.MEANS_LIMITS_INCOME_HIGH_NSA)).multiply (
         * getIncomeTestFactorPartner() ))));
         * 
         *  }
         */
        if (total == null)
            return ZERO;

        return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;

    }

    public BigDecimal getBasicBenefitP() {

        if (getIncomeTestP() != null && getAssetTestP() != null)
            return getAssetTestP().min(getIncomeTestP());
        else if (getIncomeTestP() != null)
            return getIncomeTestP();
        else if (getAssetTestP() != null)
            return getAssetTestP();

        return null;

    }

    public BigDecimal getPharmAllowanceP() {
        Integer client_benefit_type = getBenefitType();
        Integer partner_benefit_type = getPartnerBenefitType();

        if (client_benefit_type == null)
            return ZERO;

        // client gets Age Pension, partner gets Age Pension, client is not
        // single
        // ==> client = $2.90, , partner = $2.90
        if (BenefitTypeCode.AGE_PENSION.equals(client_benefit_type)) {
            if (getMaritalStatus() == null || !entitledForAgePension()
                    || partner_benefit_type == null)
                return null;

            if (!equals(getMaritalStatus(), MaritalCode.SINGLE)
                    && BenefitTypeCode.AGE_PENSION.equals(partner_benefit_type))
                return TaxUtils.PHARMACUETICAL_ALLOWANCE_COUPLE;
        }

        // client gets Age Pension, partner gets Age Pension, client is not
        // single
        // ==> client = $2.90, , partner = $2.90
        if (BenefitTypeCode.NEW_START_ALLOWANCE.equals(client_benefit_type)) {
            if (getMaritalStatus() == null || !entitledForAgePensionPartner()
                    || partner_benefit_type == null)
                return null;

            if (!equals(getMaritalStatus(), MaritalCode.SINGLE)
                    && BenefitTypeCode.AGE_PENSION.equals(partner_benefit_type))
                return TaxUtils.PHARMACUETICAL_ALLOWANCE_COUPLE;
        }
        /*
         * if ( agePensionPartner() && entitledForAgePensionPartner()) {
         * 
         * if ( equals( getMaritalStatus(), MaritalCode.MARRIED ) || equals(
         * getMaritalStatus(), MaritalCode.DEFACTO )) return
         * TaxCalc.PHARMACUETICAL_ALLOWANCE_COUPLE ;
         *  }
         */
        return ZERO; // null;

    }

    public BigDecimal getRentAssistanceP() {
        BigDecimal total = null;

        if (getRent() != null) {

            if ((equals(getMaritalStatus(), MaritalCode.MARRIED)
                    || equals(getMaritalStatus(), MaritalCode.DEFACTO) || equals(
                    getMaritalStatus(), MaritalCode.PARTNERED))
                    && equals(getNoChildren(), Boolean.TRUE)) {
                total = (getRent().compareTo(
                        TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE) >= 0 ? (getRent()
                        .compareTo(
                                TaxUtils.MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE) > 0 ? TaxUtils.MAXIMUM_RENT_ASSIATANCE_COUPLE
                        : (getRent()
                                .subtract(TaxUtils.MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE))
                                .multiply(TaxUtils.RENT_ASSISTANCE_FACTOR))
                        : new BigDecimal(0));

                if (total == null)
                    return ZERO;

                return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;

            }
        }
        return ZERO;
    }

    public BigDecimal getPensionerRebateP() {

        if (agePensionPartner() && entitledForAgePensionPartner()) {

            BigDecimal total = null;

            if (getMaritalStatus() == null || getTotalI() == null)
                return ZERO;

            if (equals(getMaritalStatus(), MaritalCode.MARRIED)
                    || equals(getMaritalStatus(), MaritalCode.DEFACTO)
                    || equals(getMaritalStatus(), MaritalCode.PARTNERED)) {
                if ((getMaxAgePensionPerYear().add(getTotalI()))
                        .compareTo(TaxUtils.PENSIONER_REBATE_COUPLE_THRESHOLD) <= 0)
                    return TaxUtils.PENSIONER_REBATE_COUPLE;
                else
                    total = TaxUtils.PENSIONER_REBATE_COUPLE
                            .subtract(((getMaxAgePensionPerYear()
                                    .add(getTotalI()))
                                    .subtract(TaxUtils.PENSIONER_REBATE_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.PENSIONER_REBATE_REDUCTION_FACTOR));

                if (total == null)
                    return ZERO;

                return new BigDecimal(0).compareTo(total) > 0 ? ZERO : total;

            }
        }

        return ZERO;
    }

    public BigDecimal getDeemedIncome() {
        // IF(B37<=H32,B37*I32,((B37-H32)*I33)+J33)
        if (getTotalAssetsSubjectToDeeming() != null
                && getDeemingRates() != null) {
            if (getTotalAssetsSubjectToDeeming().compareTo(getDeemingRates()) <= 0)
                return getTotalAssetsSubjectToDeeming().multiply(
                        new BigDecimal(0.03));

            else
                return new BigDecimal(
                        ((getTotalAssetsSubjectToDeeming().doubleValue() - getDeemingRates()
                                .doubleValue()) * 0.045 + 1674.00));
        }

        return null;
    }

    public BigDecimal getTotalAssetsSubjectToDeeming() {

        if (getYearsToAgePension() != null) {

            double sa = 0, ma = 0, sh = 0, bo = 0, fi = 0, su = 0;

            if (getSavingsA() != null)
                sa = getSavingsA().doubleValue();

            if (getManagedFundsA() != null)
                ma = getManagedFundsA().doubleValue();

            if (getSharesA() != null)
                sh = getSharesA().doubleValue();

            if (getBondsA() != null)
                bo = getBondsA().doubleValue();

            if (getFixedInterestA() != null)
                fi = getFixedInterestA().doubleValue();

            if (getSuperannuationA() != null)
                su = getSuperannuationA().doubleValue();

            if ((getYearsToAgePension().compareTo(new BigDecimal(0))) < 0)

                return new BigDecimal(sa + ma + sh + bo + fi + su);
            else
                return new BigDecimal(sa + ma + sh + bo + fi);
        }

        return null;
    }

    public int getPensionQualifyingYear(java.util.Date dateOfBirth,
            Integer sexCodeID) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int years = calendar.get(java.util.Calendar.YEAR);

        return (int) Math.ceil(getPensionQualifyingAge(dateOfBirth, sexCodeID)
                - getAge(dateOfBirth) + years);
    }

    public BigDecimal getYearsToAgePension() {

        if (getDateOfBirth() != null && getSexCodeID() != null)
            return new BigDecimal(getPensionQualifyingAge(getDateOfBirth(),
                    getSexCodeID())
                    - getAge(getDateOfBirth()));

        return null;
    }

    public BigDecimal getYearsToAgePensionPartner() {

        if (getPartnerDateOfBirth() != null && getPartnerSexCodeID() != null)
            return new BigDecimal(getPensionQualifyingAge(
                    getPartnerDateOfBirth(), getPartnerSexCodeID())
                    - getAge(getPartnerDateOfBirth()));

        return null;
    }

    public BigDecimal getDeemingRates() {

        if (getMaritalStatus() == null)
            return null;

        if (equals(getMaritalStatus(), MaritalCode.SINGLE))
            return TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD;

        if (equals(getMaritalStatus(), MaritalCode.MARRIED))
            return TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD;

        return TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD;

        // throw new IllegalArgumentException( "Unknown MaritalStatus: " +
        // getMaritalStatus() );

    }

    public BigDecimal getTotalIncome() {
        if (getTotalI() != null && getDeemedIncome() != null)
            return getTotalI().add(getDeemedIncome());

        return null;
    }

    public boolean entitledForAgePension() {
        if (getYearsToAgePension() != null)
            return getYearsToAgePension().compareTo(new BigDecimal(0)) <= 0;

        return false;
    }

    public boolean entitledForAgePensionPartner() {
        if (getYearsToAgePensionPartner() != null)
            return getYearsToAgePensionPartner().compareTo(new BigDecimal(0)) <= 0;

        return false;
    }

    public boolean entitledForNewStartAllowance() {
        return (getAge() >= MINIMUM_AGE_FOR_NEWSTART_ALLOWANCE)
                && (!entitledForAgePension());
    }

    public boolean entitledForNewStartAllowancePartner() {
        return (getAge(getPartnerDateOfBirth()) >= MINIMUM_AGE_FOR_NEWSTART_ALLOWANCE)
                && (!entitledForAgePensionPartner());
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Integer getRequiredFrequencyCodeID() {
        return FrequencyCode.FORTNIGHTLY;
    }

    public BigDecimal getAssetsTestAllowableThreshold() {
        if (equals(MaritalCode.DEFACTO, getMaritalStatus())
                || equals(MaritalCode.MARRIED, getMaritalStatus())
                || equals(MaritalCode.PARTNERED, getMaritalStatus())) {
            // couple
            return getHomeOwner() ? TaxUtils.MEANS_LIMITS_ASSETS_HO_COUPLE
                    : TaxUtils.MEANS_LIMITS_ASSETS_NH_COUPLE;
        } else {
            // single
            return getHomeOwner() ? TaxUtils.MEANS_LIMITS_ASSETS_HO_SINGLE
                    : TaxUtils.MEANS_LIMITS_ASSETS_NH_SINGLE;
        }
    }

    public BigDecimal getIndexedThresholdForDeemedIncome() {
        if (equals(MaritalCode.DEFACTO, getMaritalStatus())
                || equals(MaritalCode.MARRIED, getMaritalStatus())
                || equals(MaritalCode.PARTNERED, getMaritalStatus())) {
            // couple
            return TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD;
        } else {
            // single
            return TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD;
        }
    }

    /**
     * Adds up all financial investments subject to deeming income. Currently:
     * 
     * <pre>
     *       Savings + Managed Funds + Shares/Derivatives + Bonds/Debentures 
     *       + Fixed Interest + Gifts Over $10,000 + Loans Owed + 
     *       + Superannuation (only if over age pension age) 
     *       + Investment Property
     * </pre>
     * 
     * return sum of all financial investments subject to deeming incomes
     */
    public BigDecimal getFinancialInvestmentsSubjectToDeemingIncome() {

        double sa = 0.0, // Savings
        ma = 0.0, // Managed Funds
        sh = 0.0, // Shares/Derivatives
        bo = 0.0, // Bonds/Debentures
        fi = 0.0, // Fixed Interest
        gi = 0.0, // Gifts Over $10,000
        lo = 0.0, // Loabs Owed
        pr = 0.0, // Investment Property
        su = 0.0; // Superannuation (only if over age pension age)

        boolean none = true;

        if (getSavingsI() != null) {
            sa = getSavingsI().doubleValue();
            none = false;
        }

        if (getManagedFundsI() != null) {
            ma = getManagedFundsI().doubleValue();
            none = false;
        }

        if (getSharesI() != null) {
            sh = getSharesI().doubleValue();
            none = false;
        }

        if (getBondsI() != null) {
            bo = getBondsI().doubleValue();
            none = false;
        }

        if (getFixedInterestI() != null) {
            fi = getFixedInterestI().doubleValue();
            none = false;
        }

        if (getPropertyI() != null) {
            pr = getPropertyI().doubleValue();
            none = false;
        }

        if (getGiftsI() != null) {
            gi = getGiftsI().doubleValue();
            none = false;
        }

        if (getLoansI() != null) {
            lo = getLoansI().doubleValue();
            none = false;
        }

        if (ageOver55(this.getAge())) {
            if (getSuperannuationI() != null) {
                su = getSuperannuationI().doubleValue();
                none = false;
            }
        }

        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + pr + gi + lo + su);

        return null;

    }

    /**
     * Adds up all financial investments subject to deeming. Currently:
     * 
     * <pre>
     *       Savings + Managed Funds + Shares/Derivatives + Bonds/Debentures 
     *       + Fixed Interest + Gifts Over $10,000 + Loans Owed + 
     *       + Superannuation (only if over age pension age) 
     *       + Investment Property
     * </pre>
     * 
     * return sum of all financial investments subject to deeming
     */
    public BigDecimal getFinancialInvestmentsSubjectToDeeming() {

        double sa = 0.0, // Savings
        ma = 0.0, // Managed Funds
        sh = 0.0, // Shares/Derivatives
        bo = 0.0, // Bonds/Debentures
        fi = 0.0, // Fixed Interest
        gi = 0.0, // Gifts Over $10,000
        lo = 0.0, // Loabs Owed
        pr = 0.0, // Investment Property
        su = 0.0; // Superannuation (only if over age pension age)

        boolean none = true;

        if (getSavingsA() != null) {
            sa = getSavingsA().doubleValue();
            none = false;
        }

        if (getManagedFundsA() != null) {
            ma = getManagedFundsA().doubleValue();
            none = false;
        }

        if (getSharesA() != null) {
            sh = getSharesA().doubleValue();
            none = false;
        }

        if (getBondsA() != null) {
            bo = getBondsA().doubleValue();
            none = false;
        }

        if (getFixedInterestA() != null) {
            fi = getFixedInterestA().doubleValue();
            none = false;
        }

        if (getPropertyA() != null) {
            pr = getPropertyA().doubleValue();
            none = false;
        }

        if (getGiftsA() != null) {
            gi = getGiftsA().doubleValue();
            none = false;
        }

        if (getLoansA() != null) {
            lo = getLoansA().doubleValue();
            none = false;
        }

        if (ageOver55(this.getAge())) {
            if (getSuperannuationA() != null) {
                su = getSuperannuationA().doubleValue();
                none = false;
            }
        }

        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + pr + gi + lo + su);

        return null;

    }

    /*
     * partner fields - get methods
     */
    public BigDecimal getSavingsAPartner() {
        return partner_ai.savingsA;
    }

    public BigDecimal getManagedFundsAPartner() {
        return partner_ai.managedFundsA;
    }

    public BigDecimal getSharesAPartner() {
        return partner_ai.sharesA;
    }

    public BigDecimal getBondsAPartner() {
        return partner_ai.bondsA;
    }

    public BigDecimal getFixedInterestAPartner() {
        return partner_ai.interestA;
    }

    public BigDecimal getHomeContentsAPartner() {
        return partner_ai.homeContentsA;
    }

    public BigDecimal getCarsEtcAPartner() {
        return partner_ai.carsEtcA;
    }

    public BigDecimal getGiftsAPartner() {
        return partner_ai.giftsA;
    }

    public BigDecimal getPropertyAPartner() {
        return partner_ai.propertyA;
    }

    public BigDecimal getLoansAPartner() {
        return partner_ai.loansA;
    }

    public BigDecimal getSuperannuationAPartner() {
        return partner_ai.superannuationA;
    }

    public BigDecimal getAllocatedPensionAPartner() {
        return partner_ai.allocatedPensionA;
    }

    public BigDecimal getComplyingPensionAPartner() {
        return partner_ai.complyingPensionA;
    }

    public BigDecimal getSavingsIPartner() {
        if (getSavingsAPartner() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getSavingsAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? (getSavingsAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW))
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getSavingsAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));
            return getSavingsAPartner().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getSavingsAPartner()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getSavingsAPartner()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getManagedFundsIPartner() {
        if (getManagedFundsAPartner() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getManagedFundsAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getManagedFundsAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getManagedFundsAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getManagedFundsAPartner().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getManagedFundsAPartner()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getManagedFundsAPartner()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;

    }

    public BigDecimal getSharesIPartner() {
        if (getSharesAPartner() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getSharesAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getSharesAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getSharesAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getSharesAPartner().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getSharesAPartner()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getSharesAPartner()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;

    }

    public BigDecimal getBondsIPartner() {
        if (getBondsAPartner() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getBondsAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getBondsAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getBondsAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getBondsAPartner().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getBondsAPartner()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getBondsAPartner()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getFixedInterestIPartner() {
        if (getFixedInterestAPartner() != null && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getFixedInterestAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getFixedInterestAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getFixedInterestAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getFixedInterestAPartner().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getFixedInterestAPartner()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getFixedInterestAPartner()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getGiftsIPartner() {
        if (getGiftsAPartner() != null
                && TaxUtils.GIFTS_THRESHOLD.compareTo(getGiftsA()) < 0
                && getMaritalStatus() != null) {

            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                return getGiftsAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getGiftsAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getGiftsAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            return getGiftsAPartner().compareTo(
                    TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getGiftsAPartner()
                    .multiply(TaxUtils.DEEMING_RATES_LOW)
                    : ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW))
                            .add((getGiftsAPartner()
                                    .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                                    .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    public BigDecimal getSuperannuationIPartner() {
        if (entitledForAgePension()) {
            if (getSuperannuationAPartner() != null
                    && getMaritalStatus() != null) {

                if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                    return getSuperannuationAPartner().compareTo(
                            TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ? getSuperannuationAPartner()
                            .multiply(TaxUtils.DEEMING_RATES_LOW)
                            : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                    .multiply(TaxUtils.DEEMING_RATES_LOW))
                                    .add((getSuperannuationAPartner()
                                            .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                            .multiply(TaxUtils.DEEMING_RATES_HIGH)));

                return getSuperannuationAPartner().compareTo(
                        TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ? getSuperannuationAPartner()
                        .multiply(TaxUtils.DEEMING_RATES_LOW)
                        : ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((getSuperannuationAPartner()
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));
            }
        }
        return null;
    }

    public BigDecimal getPropertyIPartner() {
        return partner_ai.propertyI;
    }

    public BigDecimal getLoansIPartner() {
        return partner_ai.loansI;
    }

    public BigDecimal getAllocatedPensionIPartner() {
        return partner_ai.allocatedPensionI;
    }

    public BigDecimal getComplyingPensionIPartner() {
        return partner_ai.complyingPensionI;
    }

    /*
     * partner fields - set methods
     */
    public void setSavingsAPartner(BigDecimal value) {
        if (equals(partner_ai.savingsA, value))
            return;

        partner_ai.savingsA = value;

        setModified();
    }

    public void setManagedFundsAPartner(BigDecimal value) {
        if (equals(partner_ai.managedFundsA, value))
            return;

        partner_ai.managedFundsA = value;

        setModified();
    }

    public void setSharesAPartner(BigDecimal value) {
        if (equals(partner_ai.sharesA, value))
            return;

        partner_ai.sharesA = value;

        setModified();
    }

    public void setBondsAPartner(BigDecimal value) {
        if (equals(partner_ai.bondsA, value))
            return;

        partner_ai.bondsA = value;

        setModified();
    }

    public void setFixedInterestAPartner(BigDecimal value) {
        if (equals(partner_ai.interestA, value))
            return;

        partner_ai.interestA = value;

        setModified();
    }

    public void setHomeContentsAPartner(BigDecimal value) {
        if (equals(partner_ai.homeContentsA, value))
            return;

        partner_ai.homeContentsA = value;

        setModified();
    }

    public void setCarsEtcAPartner(BigDecimal value) {
        if (equals(partner_ai.carsEtcA, value))
            return;

        partner_ai.carsEtcA = value;

        setModified();
    }

    public void setGiftsAPartner(BigDecimal value) {
        if (equals(partner_ai.giftsA, value))
            return;

        partner_ai.giftsA = value;

        setModified();
    }

    public void setPropertyAPartner(BigDecimal value) {
        if (equals(partner_ai.propertyA, value))
            return;

        partner_ai.propertyA = value;

        setModified();
    }

    public void setLoansAPartner(BigDecimal value) {
        if (equals(partner_ai.loansA, value))
            return;

        partner_ai.loansA = value;

        setModified();
    }

    public void setAllocatedPensionAPartner(BigDecimal value) {
        if (equals(partner_ai.allocatedPensionA, value))
            return;

        partner_ai.allocatedPensionA = value;

        setModified();
    }

    public void setComplyingPensionAPartner(BigDecimal value) {
        if (equals(partner_ai.complyingPensionA, value))
            return;

        partner_ai.complyingPensionA = value;

        setModified();
    }

    public void setSavingsIPartner(BigDecimal value) {
        if (equals(partner_ai.savingsI, value))
            return;

        partner_ai.savingsI = value;

        setModified();
    }

    public void setManagedFundsIPartner(BigDecimal value) {
        if (equals(partner_ai.managedFundsI, value))
            return;

        partner_ai.managedFundsI = value;

        setModified();
    }

    public void setSharesIPartner(BigDecimal value) {
        if (equals(partner_ai.sharesI, value))
            return;

        partner_ai.sharesI = value;

        setModified();
    }

    public void setBondsIPartner(BigDecimal value) {
        if (equals(partner_ai.bondsI, value))
            return;

        partner_ai.bondsI = value;

        setModified();
    }

    public void setInterestIPartner(BigDecimal value) {
        if (equals(partner_ai.interestI, value))
            return;

        partner_ai.interestI = value;

        setModified();
    }

    public void setGiftsIPartner(BigDecimal value) {
        if (equals(partner_ai.giftsI, value))
            return;

        partner_ai.giftsI = value;

        setModified();
    }

    public void setPropertyIPartner(BigDecimal value) {
        if (equals(partner_ai.propertyI, value))
            return;

        partner_ai.propertyI = value;

        setModified();
    }

    public void setLoansIPartner(BigDecimal value) {
        if (equals(partner_ai.loansI, value))
            return;

        partner_ai.loansI = value;

        setModified();
    }

    public void setSuperannuationAPartner(BigDecimal value) {
        if (equals(partner_ai.superannuationA, value))
            return;

        partner_ai.superannuationA = value;

        setModified();
    }

    public void setAllocatedPensionIPartner(BigDecimal value) {
        if (equals(partner_ai.allocatedPensionI, value))
            return;

        partner_ai.allocatedPensionI = value;

        setModified();
    }

    public void setComplyingPensionIPartner(BigDecimal value) {
        if (equals(partner_ai.complyingPensionI, value))
            return;

        partner_ai.complyingPensionI = value;

        setModified();
    }

    /*
     * public BigDecimal getTotalIPartner() {
     * 
     * 
     * double sa = 0 , ma = 0 , sh = 0 , bo = 0 , fi = 0 , ho = 0 , ca = 0 , pr =
     * 0 , gi = 0 , lo = 0 , su = 0 , al = 0 , co = 0 , cl = 0 , pa = 0 , cs = 0 ,
     * ps = 0 ;
     * 
     * boolean none = true;
     * 
     * if ( getSavingsIPartner() != null ) { sa =
     * getSavingsIPartner().doubleValue(); none = false; }
     * 
     * if ( getManagedFundsIPartner() != null ) { ma =
     * getManagedFundsIPartner().doubleValue(); none = false; }
     * 
     * if ( getSharesIPartner() != null ) { sh =
     * getSharesIPartner().doubleValue(); none = false; }
     * 
     * if ( getBondsIPartner() != null ) { bo =
     * getBondsIPartner().doubleValue(); none = false; }
     * 
     * if ( getFixedInterestIPartner() != null ) { fi =
     * getFixedInterestIPartner().doubleValue(); none = false; }
     *  // if ( getHomeContentsIPartner() != null ) { // ho =
     * getHomeContentsIPartner().doubleValue(); // none = false; // }h
     *  // if ( getCarsEtcIPartner() != null ) { // ca =
     * getCarsEtcIPartner().doubleValue(); // none = false; // }
     * 
     * if ( getPropertyIPartner() != null ) { pr =
     * getPropertyIPartner().doubleValue(); none = false; }
     * 
     * if ( getGiftsIPartner() != null ) { gi =
     * getGiftsIPartner().doubleValue(); none = false; }
     * 
     * if ( getLoansIPartner() != null ) { lo =
     * getLoansIPartner().doubleValue(); none = false; }
     * 
     * if ( getSuperannuationIPartner() != null ) { su =
     * getSuperannuationIPartner().doubleValue(); none = false; }
     * 
     * if ( getAllocatedPensionIPartner() != null ) { al =
     * getAllocatedPensionIPartner().doubleValue(); none = false; }
     * 
     * if ( getComplyingPensionIPartner() != null ) { co =
     * getComplyingPensionIPartner().doubleValue(); none = false; }
     *  // if ( getClientSalaryI() != null ) { // cs =
     * getClientSalaryI().doubleValue(); // none = false; // }
     * 
     * if ( getSalaryIPartner() != null ) { ps =
     * getSalaryIPartner().doubleValue(); none = false; }
     * 
     * if ( none == false ) return new BigDecimal ( sa + ma + sh + bo + fi + ho +
     * ca + pr + gi + lo + su + al + co + cl + pa + cs + ps );
     * 
     * return null; }
     */

    /**
     * Calculates the joint assessable assets/income, it adds the client and
     * partner fields.
     * 
     * @retuns joint_ai - an object wihich contains the sum of client and
     *         partner assets/income
     */
    private AssessableAssetsIncome getJointAssessableAssetsIncome() {
        AssessableAssetsIncome joint_ai = new AssessableAssetsIncome();

        // assets
        // Home Contents
        if (client_ai.homeContentsA != null) {
            joint_ai.homeContentsA = joint_ai.homeContentsA
                    .add(client_ai.homeContentsA);
        }
        if (partner_ai.homeContentsA != null) {
            joint_ai.homeContentsA = joint_ai.homeContentsA
                    .add(partner_ai.homeContentsA);
        }
        // Cars/Caravans/Boats
        if (client_ai.carsEtcA != null) {
            joint_ai.carsEtcA = joint_ai.carsEtcA.add(client_ai.carsEtcA);
        }
        if (partner_ai.carsEtcA != null) {
            joint_ai.carsEtcA = joint_ai.carsEtcA.add(partner_ai.carsEtcA);
        }
        // Investment Property
        if (client_ai.propertyA != null) {
            joint_ai.propertyA = joint_ai.propertyA.add(client_ai.propertyA);
        }
        if (partner_ai.propertyA != null) {
            joint_ai.propertyA = joint_ai.propertyA.add(partner_ai.propertyA);
        }
        // Savings
        if (client_ai.savingsA != null) {
            joint_ai.savingsA = joint_ai.savingsA.add(client_ai.savingsA);
        }
        if (partner_ai.savingsA != null) {
            joint_ai.savingsA = joint_ai.savingsA.add(partner_ai.savingsA);
        }
        // Managed Funds
        if (client_ai.managedFundsA != null) {
            joint_ai.managedFundsA = joint_ai.managedFundsA
                    .add(client_ai.managedFundsA);
        }
        if (partner_ai.managedFundsA != null) {
            joint_ai.managedFundsA = joint_ai.managedFundsA
                    .add(partner_ai.managedFundsA);
        }
        // Shares/Derivates
        if (client_ai.sharesA != null) {
            joint_ai.sharesA = joint_ai.sharesA.add(client_ai.sharesA);
        }
        if (partner_ai.sharesA != null) {
            joint_ai.sharesA = joint_ai.sharesA.add(partner_ai.sharesA);
        }
        // Bonds/Debentures
        if (client_ai.bondsA != null) {
            joint_ai.bondsA = joint_ai.bondsA.add(client_ai.bondsA);
        }
        if (partner_ai.bondsA != null) {
            joint_ai.bondsA = joint_ai.bondsA.add(partner_ai.bondsA);
        }
        // Fixed Interest
        if (client_ai.interestA != null) {
            joint_ai.interestA = joint_ai.interestA.add(client_ai.interestA);
        }
        if (partner_ai.interestA != null) {
            joint_ai.interestA = joint_ai.interestA.add(partner_ai.interestA);
        }
        // Gifts Over $10,000
        if (client_ai.giftsA != null) {
            joint_ai.giftsA = joint_ai.giftsA.add(client_ai.giftsA);
        }
        if (partner_ai.giftsA != null) {
            joint_ai.giftsA = joint_ai.giftsA.add(partner_ai.giftsA);
        }
        // Loans Owed
        if (client_ai.loansA != null) {
            joint_ai.loansA = joint_ai.loansA.add(client_ai.loansA);
        }
        if (partner_ai.loansA != null) {
            joint_ai.loansA = joint_ai.loansA.add(partner_ai.loansA);
        }
        // Superannuation
        if (client_ai.superannuationA != null) {
            joint_ai.superannuationA = joint_ai.superannuationA
                    .add(client_ai.superannuationA);
        }
        if (partner_ai.superannuationA != null) {
            joint_ai.superannuationA = joint_ai.superannuationA
                    .add(partner_ai.superannuationA);
        }
        // Allocated Pension
        if (client_ai.allocatedPensionA != null) {
            joint_ai.allocatedPensionA = joint_ai.allocatedPensionA
                    .add(client_ai.allocatedPensionA);
        }
        if (partner_ai.allocatedPensionA != null) {
            joint_ai.allocatedPensionA = joint_ai.allocatedPensionA
                    .add(partner_ai.allocatedPensionA);
        }
        // Complying Pension
        if (client_ai.complyingPensionA != null) {
            joint_ai.complyingPensionA = joint_ai.complyingPensionA
                    .add(client_ai.complyingPensionA);
        }
        if (partner_ai.complyingPensionA != null) {
            joint_ai.complyingPensionA = joint_ai.complyingPensionA
                    .add(partner_ai.complyingPensionA);
        }

        // income
        // public BigDecimal homeContentsI; // Home Contents Income
        // public BigDecimal carsEtcI; // Cars/Caravans/Boats Income
        // Investment Property Income
        if (client_ai.propertyI != null) {
            joint_ai.propertyI = joint_ai.propertyI.add(client_ai.propertyI);
        }
        if (partner_ai.propertyI != null) {
            joint_ai.propertyI = joint_ai.propertyI.add(partner_ai.propertyI);
        }
        /*
         * if ( client_ai.propertyI != null ) { joint_ai.propertyI =
         * joint_ai.propertyI.add( this.getPropertyI() ); } if (
         * partner_ai.propertyI != null ) { joint_ai.propertyI =
         * joint_ai.propertyI.add( this.getPropertyIPartner() ); }
         */
        // Savings Income
        if (client_ai.savingsI != null) {
            joint_ai.savingsI = joint_ai.savingsI.add(client_ai.savingsI);
        }
        if (partner_ai.savingsI != null) {
            joint_ai.savingsI = joint_ai.savingsI.add(partner_ai.savingsI);
        }
        // Managed Funds Income
        if (client_ai.managedFundsI != null) {
            joint_ai.managedFundsI = joint_ai.managedFundsI
                    .add(client_ai.managedFundsI);
        }
        if (partner_ai.managedFundsI != null) {
            joint_ai.managedFundsI = joint_ai.managedFundsI
                    .add(partner_ai.managedFundsI);
        }
        // Shares/Derivates Income
        if (client_ai.sharesI != null) {
            joint_ai.sharesI = joint_ai.sharesI.add(client_ai.sharesI);
        }
        if (partner_ai.sharesI != null) {
            joint_ai.sharesI = joint_ai.sharesI.add(partner_ai.sharesI);
        }
        // Bonds/Debentures Income
        if (client_ai.bondsI != null) {
            joint_ai.bondsI = joint_ai.bondsI.add(client_ai.bondsI);
        }
        if (partner_ai.bondsI != null) {
            joint_ai.bondsI = joint_ai.bondsI.add(partner_ai.bondsI);
        }
        // Fixed Interest Income
        if (client_ai.interestI != null) {
            joint_ai.interestI = joint_ai.interestI.add(client_ai.interestI);
        }
        if (partner_ai.interestI != null) {
            joint_ai.interestI = joint_ai.interestI.add(partner_ai.interestI);
        }
        // Gifts Over $10,000 Income
        if (client_ai.giftsI != null) {
            joint_ai.giftsI = joint_ai.giftsI.add(client_ai.giftsI);
        }
        if (partner_ai.giftsI != null) {
            joint_ai.giftsI = joint_ai.giftsI.add(partner_ai.giftsI);
        }
        // Loans Owed Income
        if (client_ai.loansI != null) {
            joint_ai.loansI = joint_ai.loansI.add(client_ai.loansI);
        }
        if (partner_ai.loansI != null) {
            joint_ai.loansI = joint_ai.loansI.add(partner_ai.loansI);
        }
        // Superannuation Income
        if (client_ai.superannuationI != null) {
            joint_ai.superannuationI = joint_ai.superannuationI
                    .add(client_ai.superannuationI);
        }
        if (partner_ai.superannuationI != null) {
            joint_ai.superannuationI = joint_ai.superannuationI
                    .add(partner_ai.superannuationI);
        }
        // Allocated Pension Income
        if (client_ai.allocatedPensionI != null) {
            joint_ai.allocatedPensionI = joint_ai.allocatedPensionI
                    .add(client_ai.allocatedPensionI);
        }
        if (partner_ai.allocatedPensionI != null) {
            joint_ai.allocatedPensionI = joint_ai.allocatedPensionI
                    .add(partner_ai.allocatedPensionI);
        }
        // Complying Pension Income
        if (client_ai.complyingPensionI != null) {
            joint_ai.complyingPensionI = joint_ai.complyingPensionI
                    .add(client_ai.complyingPensionI);
        }
        if (partner_ai.complyingPensionI != null) {
            joint_ai.complyingPensionI = joint_ai.complyingPensionI
                    .add(partner_ai.complyingPensionI);
        }
        // Salary/Wages Income
        if (client_ai.salaryWagesI != null) {
            joint_ai.salaryWagesI = joint_ai.salaryWagesI
                    .add(client_ai.salaryWagesI);
        }
        if (partner_ai.salaryWagesI != null) {
            joint_ai.salaryWagesI = joint_ai.salaryWagesI
                    .add(partner_ai.salaryWagesI);
        }

        return joint_ai;
    }

    /**
     * Calculates the assessable assets/income, it adds the client and partner
     * fields.
     * 
     * @retuns joint_ai - an object wihich contains the sum of client and
     *         partner assets/income
     */
    private AssessableAssetsIncome getAssessableAssetsIncome() {
        return getJointAssessableAssetsIncome();
    }

    /**
     * Re-calculates the joint assessable assets/income fields.
     */
    private void updateJointAssessableAssetsIncome() {
        this.joint_ai = getJointAssessableAssetsIncome();
    }

    /**
     * Re-calculates the "Assessable" assessable assets/income fields.
     */
    private void updateAssessableAssetsIncome() {
        this.assessable_ai = getAssessableAssetsIncome();
    }

    /**
     * Adds up all CLIENT's financial investments subject to deeming (assets
     * column). Currently:
     * 
     * <pre>
     *       Savings + Managed Funds + Shares/Derivatives + Bonds/Debentures 
     *       + Fixed Interest + Gifts Over $10,000 + Loans Owed + 
     *       + Superannuation (only if over age pension age) 
     *       + Investment Property
     * </pre>
     * 
     * return sum of all financial investments subject to deeming incomes
     */
    public BigDecimal getSubjectsToDeemingClient() {

        double sa = 0.0, // Savings
        ma = 0.0, // Managed Funds
        sh = 0.0, // Shares/Derivatives
        bo = 0.0, // Bonds/Debentures
        fi = 0.0, // Fixed Interest
        gi = 0.0, // Gifts Over $10,000
        lo = 0.0, // Loabs Owed
        pr = 0.0, // Investment Property
        su = 0.0; // Superannuation (only if over age pension age)

        boolean none = true;

        if (getSavingsA() != null) {
            sa = getSavingsA().doubleValue();
            none = false;
        }

        if (getManagedFundsA() != null) {
            ma = getManagedFundsA().doubleValue();
            none = false;
        }

        if (getSharesA() != null) {
            sh = getSharesA().doubleValue();
            none = false;
        }

        if (getBondsA() != null) {
            bo = getBondsA().doubleValue();
            none = false;
        }

        if (getFixedInterestA() != null) {
            fi = getFixedInterestA().doubleValue();
            none = false;
        }

        if (getPropertyA() != null) {
            pr = getPropertyA().doubleValue();
            none = false;
        }

        if (getGiftsA() != null) {
            gi = getGiftsA().doubleValue();
            none = false;
        }

        if (getLoansA() != null) {
            lo = getLoansA().doubleValue();
            none = false;
        }

        if (ageOver55(this.getAge())) {
            if (getSuperannuationA() != null) {
                su = getSuperannuationA().doubleValue();
                none = false;
            }
        }

        if (none == false) {
            return new BigDecimal(sa + ma + sh + bo + fi + pr + gi + lo + su);
        }

        return null;

    }

    /**
     * Adds up all PARTNER's financial investments subject to deeming (assets
     * column). Currently:
     * 
     * <pre>
     *       Savings + Managed Funds + Shares/Derivatives + Bonds/Debentures 
     *       + Fixed Interest + Gifts Over $10,000 + Loans Owed + 
     *       + Superannuation (only if over age pension age) 
     *       + Investment Property
     * </pre>
     * 
     * return sum of all financial investments subject to deeming incomes
     */
    public BigDecimal getSubjectsToDeemingPartner() {

        double sa = 0.0, // Savings
        ma = 0.0, // Managed Funds
        sh = 0.0, // Shares/Derivatives
        bo = 0.0, // Bonds/Debentures
        fi = 0.0, // Fixed Interest
        gi = 0.0, // Gifts Over $10,000
        lo = 0.0, // Loabs Owed
        pr = 0.0, // Investment Property
        su = 0.0; // Superannuation (only if over age pension age)

        boolean none = true;

        if (getSavingsAPartner() != null) {
            sa = getSavingsAPartner().doubleValue();
            none = false;
        }

        if (getManagedFundsAPartner() != null) {
            ma = getManagedFundsAPartner().doubleValue();
            none = false;
        }

        if (getSharesAPartner() != null) {
            sh = getSharesAPartner().doubleValue();
            none = false;
        }

        if (getBondsAPartner() != null) {
            bo = getBondsAPartner().doubleValue();
            none = false;
        }

        if (getFixedInterestAPartner() != null) {
            fi = getFixedInterestAPartner().doubleValue();
            none = false;
        }

        if (getPropertyAPartner() != null) {
            pr = getPropertyAPartner().doubleValue();
            none = false;
        }

        if (getGiftsAPartner() != null) {
            gi = getGiftsAPartner().doubleValue();
            none = false;
        }

        if (getLoansAPartner() != null) {
            lo = getLoansAPartner().doubleValue();
            none = false;
        }

        // if( this.getPartnerDateOfBirth() != null && ageOver55(this.getAge(
        // this.getPartnerDateOfBirth() ) ) ) {
        if (ageOver55(getPartnerAge())) {
            if (getSuperannuationAPartner() != null) {
                su = getSuperannuationAPartner().doubleValue();
                none = false;
            }
        }

        if (none == false) {
            return new BigDecimal(sa + ma + sh + bo + fi + pr + gi + lo + su);
        }

        return null;
    }

    /**
     * Calculates total assets subject to deeming. <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * A = ClientView.Total Assets Subject to Deeming B = Partner.Total Assets
     * Subject to Deeming
     * 
     * A + P = Z
     * 
     * Where calculation = Single, If Z <=33400, then Z*2.50% If Z >33400, then
     * (Z-33400)*4% + (33400*2.5%)
     * 
     * Where calculation = Couple, If Z <=57400, then Z*2.50% If Z >57400, then
     * (Z-57400)*4% + (57400*2.5%)
     * 
     * </PRE>
     * 
     * @return financial assets subject to deeming income
     */
    public BigDecimal getTotalDeemingIncomeClient() {
        if (getMaritalStatus() != null) {

            BigDecimal value = new BigDecimal(0.0); // financial investments
                                                    // subject to deeming
            // add client an partner assets to deeming
            if (getSubjectsToDeemingClient() != null) {
                value = value.add(getSubjectsToDeemingClient());
            }
            // marital status != Single,
            if (!equals(MaritalCode.SINGLE, getMaritalStatus())) {
                // add partner's investment assets subject to deeming
                if (getSubjectsToDeemingPartner() != null) {
                    value = value.add(getSubjectsToDeemingPartner());
                }
            }

            // Where calculation = Single,
            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                // If Z <=33400, then
                return value.compareTo(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ?
                // Z*2.50%
                value.multiply(TaxUtils.DEEMING_RATES_LOW)
                        :
                        // (33400*2.5%) + (Z-33400)*4%
                        ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((value
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            // Where calculation = Couple,
            return value.compareTo(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ?
            // Z*2.50%
            value.multiply(TaxUtils.DEEMING_RATES_LOW)
                    :
                    // (57400*2.5%) + (Z-57400)*4%
                    ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW)).add((value
                            .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                            .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    /**
     * D = ClientView.Assessable Income on Property
     * 
     * C = ClientView.Property Income (Administrator will enter this amount) D =
     * ClientView.Assessable Income on Property = C*(2/3)
     * 
     * @return client assessable income on property
     */
    public BigDecimal getAssessableIncomeOnPropertyClient() {
        if (client_ai.propertyI != null) {
            return client_ai.propertyI.multiply(new BigDecimal(2.0 / 3.0));
        }
        return null;
    }

    /**
     * R = Partner.Assessable Income on Property
     * 
     * Q = Partner.Property Income (Administrator will enter this amount) R =
     * Partner.Assessable Income on Property = q*(2/3)
     * 
     * @return partner assessable income on property
     */
    public BigDecimal getAssessableIncomeOnPropertyPartner() {
        if (partner_ai.propertyI != null) {
            return partner_ai.propertyI.multiply(new BigDecimal(2.0 / 3.0));
        }
        return null;
    }

    /**
     * Adds up all CLIENT's financial investments subject to deeming (assets
     * column). Currently:
     * 
     * <pre>
     *       Savings + Managed Funds + Shares/Derivatives + Bonds/Debentures 
     *       + Fixed Interest + Gifts Over $10,000 + Loans Owed + 
     * </pre>
     * 
     * return sum of all financial investments subject to deeming assets value
     */
    public BigDecimal getTotalAssetsSubjectToDeemingAClient() {

        double sa = 0.0, // Savings
        ma = 0.0, // Managed Funds
        sh = 0.0, // Shares/Derivatives
        bo = 0.0, // Bonds/Debentures
        fi = 0.0, // Fixed Interest
        gi = 0.0, // Gifts Over $10,000
        lo = 0.0, // Loans Owed
        su = 0.0; // Superannuation

        boolean none = true;

        if (getSavingsA() != null) {
            sa = getSavingsA().doubleValue();
            none = false;
        }

        if (getManagedFundsA() != null) {
            ma = getManagedFundsA().doubleValue();
            none = false;
        }

        if (getSharesA() != null) {
            sh = getSharesA().doubleValue();
            none = false;
        }

        if (getBondsA() != null) {
            bo = getBondsA().doubleValue();
            none = false;
        }

        if (getFixedInterestA() != null) {
            fi = getFixedInterestA().doubleValue();
            none = false;
        }

        if (getGiftsA() != null) {
            gi = getGiftsA().doubleValue();
            none = false;
        }

        if (getLoansA() != null) {
            lo = getLoansA().doubleValue();
            none = false;
        }

        if (ageOver55(this.getAge())) {
            if (getSuperannuationA() != null) {
                su = getSuperannuationA().doubleValue();
                none = false;
            }
        }

        if (none == false) {
            return new BigDecimal(sa + ma + sh + bo + fi + gi + lo + su);
        }

        return null;

    }

    /**
     * Adds up all PARTNER's financial investments subject to deeming (assets
     * column). Currently:
     * 
     * <pre>
     *       Savings + Managed Funds + Shares/Derivatives + Bonds/Debentures 
     *       + Fixed Interest + Gifts Over $10,000 + Loans Owed + 
     * </pre>
     * 
     * return sum of all financial investments subject to deeming assets value
     */
    public BigDecimal getTotalAssetsSubjectToDeemingAPartner() {

        double sa = 0.0, // Savings
        ma = 0.0, // Managed Funds
        sh = 0.0, // Shares/Derivatives
        bo = 0.0, // Bonds/Debentures
        fi = 0.0, // Fixed Interest
        gi = 0.0, // Gifts Over $10,000
        lo = 0.0, // Loans Owed
        su = 0.0; // Superannuation

        boolean none = true;

        if (getSavingsAPartner() != null) {
            sa = getSavingsAPartner().doubleValue();
            none = false;
        }

        if (getManagedFundsAPartner() != null) {
            ma = getManagedFundsAPartner().doubleValue();
            none = false;
        }

        if (getSharesAPartner() != null) {
            sh = getSharesAPartner().doubleValue();
            none = false;
        }

        if (getBondsAPartner() != null) {
            bo = getBondsAPartner().doubleValue();
            none = false;
        }

        if (getFixedInterestAPartner() != null) {
            fi = getFixedInterestAPartner().doubleValue();
            none = false;
        }

        if (getGiftsAPartner() != null) {
            gi = getGiftsAPartner().doubleValue();
            none = false;
        }

        if (getLoansAPartner() != null) {
            lo = getLoansAPartner().doubleValue();
            none = false;
        }

        // if( this.getPartnerDateOfBirth() != null && ageOver55(this.getAge(
        // this.getPartnerDateOfBirth() ) ) ) {
        if (ageOver55(getPartnerAge())) {
            if (getSuperannuationAPartner() != null) {
                su = getSuperannuationAPartner().doubleValue();
                none = false;
            }
        }

        if (none == false) {
            return new BigDecimal(sa + ma + sh + bo + fi + gi + lo + su);
        }

        return null;

    }

    /**
     * Calculates total assets subject to deeming. <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * A = ClientView.Total Assets Subject to Deeming B = Partner.Total Assets
     * Subject to Deeming
     * 
     * A + P = Z
     * 
     * Where calculation = Single, If Z <=33400, then Z*2.50% If Z >33400, then
     * (Z-33400)*4% + (33400*2.5%)
     * 
     * Where calculation = Couple, If Z <=57400, then Z*2.50% If Z >57400, then
     * (Z-57400)*4% + (57400*2.5%)
     * 
     * </PRE>
     * 
     * @return financial assets subject to deeming income
     */
    public BigDecimal getTotalAssetsSubjectToDeemingIClient() {
        if (getMaritalStatus() != null) {

            BigDecimal value = new BigDecimal(0.0); // financial investments
                                                    // subject to deeming
            // add client an partner assets to deeming
            if (getTotalAssetsSubjectToDeemingAClient() != null) {
                value = value.add(getTotalAssetsSubjectToDeemingAClient());
            }
            // marital status != Single,
            if (!equals(MaritalCode.SINGLE, getMaritalStatus())) {
                // add partner's investment assets subject to deeming
                if (getTotalAssetsSubjectToDeemingAPartner() != null) {
                    value = value.add(getTotalAssetsSubjectToDeemingAPartner());
                }
            }

            // Where calculation = Single,
            if (equals(MaritalCode.SINGLE, getMaritalStatus()))
                // If Z <=33400, then
                return value.compareTo(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD) <= 0 ?
                // Z*2.50%
                value.multiply(TaxUtils.DEEMING_RATES_LOW)
                        :
                        // (33400*2.5%) + (Z-33400)*4%
                        ((TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD
                                .multiply(TaxUtils.DEEMING_RATES_LOW))
                                .add((value
                                        .subtract(TaxUtils.DEEMING_RATES_SINGLE_THRESHOLD))
                                        .multiply(TaxUtils.DEEMING_RATES_HIGH)));

            // Where calculation = Couple,
            return value.compareTo(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD) <= 0 ?
            // Z*2.50%
            value.multiply(TaxUtils.DEEMING_RATES_LOW)
                    :
                    // (57400*2.5%) + (Z-57400)*4%
                    ((TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD
                            .multiply(TaxUtils.DEEMING_RATES_LOW)).add((value
                            .subtract(TaxUtils.DEEMING_RATES_COUPLE_THRESHOLD))
                            .multiply(TaxUtils.DEEMING_RATES_HIGH)));
        }

        return null;
    }

    /**
     * Total ClientView Assets
     * 
     * @return sum all of partner's assets
     */
    public BigDecimal getTotalAClient() {

        double sa = 0, ma = 0, sh = 0, bo = 0, fi = 0, ho = 0, ca = 0, pr = 0, gi = 0, lo = 0, su = 0, al = 0, co = 0, cl = 0, pa = 0;

        boolean none = true;

        if (getSavingsA() != null) {
            sa = getSavingsA().doubleValue();
            none = false;
        }

        if (getManagedFundsA() != null) {
            ma = getManagedFundsA().doubleValue();
            none = false;
        }

        if (getSharesA() != null) {
            sh = getSharesA().doubleValue();
            none = false;
        }

        if (getBondsA() != null) {
            bo = getBondsA().doubleValue();
            none = false;
        }

        if (getFixedInterestA() != null) {
            fi = getFixedInterestA().doubleValue();
            none = false;
        }

        if (getHomeContentsA() != null) {
            ho = getHomeContentsA().doubleValue();
            none = false;
        }

        if (getCarsEtcA() != null) {
            ca = getCarsEtcA().doubleValue();
            none = false;
        }

        if (getPropertyA() != null) {
            pr = getPropertyA().doubleValue();
            none = false;
        }

        if (getGiftsA() != null) {
            gi = getGiftsA().doubleValue();
            none = false;
        }

        if (getLoansA() != null) {
            lo = getLoansA().doubleValue();
            none = false;
        }

        if (getSuperannuationA() != null) {
            su = getSuperannuationA().doubleValue();
            none = false;
        }

        if (getAllocatedPensionA() != null) {
            al = getAllocatedPensionA().doubleValue();
            none = false;
        }

        if (getComplyingPensionA() != null) {
            co = getComplyingPensionA().doubleValue();
            none = false;
        }

        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + ho + ca + pr + gi
                    + lo + su + al + co + cl + pa);

        return null;
    }

    /**
     * Total Joint Assets For Assets Test (client + partner assets for assets
     * test)
     * 
     * @return total joint assets for assets test
     */
    public BigDecimal getTotalAJoint() {

        BigDecimal value = new BigDecimal(0.0);

        // add client total assets subject to deeming amount
        if (getTotalAClient() != null) {
            value = value.add(getTotalAClient());
        }
        // do we have a partner?
        if (getMaritalStatus() != null) {
            // marital status != Single,
            if (!single().equals(Boolean.TRUE)) {
                // add partner total assets subject to deeming amount
                if (getTotalAPartner() != null) {
                    value = value.add(getTotalAPartner());
                }
            }
        }

        return value;
    }

    /**
     * Total client's Assets for Assets Test
     *  - includes every asset, except complying pension and superannuation.
     * superannuation depends on client's age, if age over age pension age than
     * add superannuation to the total
     * 
     * @return sum all of partner's assets for Assets Test
     */
    public BigDecimal getTotalAForAssetsTestClient() {

        double sa = 0, ma = 0, sh = 0, bo = 0, fi = 0, ho = 0, ca = 0, pr = 0, gi = 0, lo = 0, su = 0, al = 0, co = 0, cl = 0, pa = 0;

        boolean none = true;

        if (getSavingsA() != null) {
            sa = getSavingsA().doubleValue();
            none = false;
        }

        if (getManagedFundsA() != null) {
            ma = getManagedFundsA().doubleValue();
            none = false;
        }

        if (getSharesA() != null) {
            sh = getSharesA().doubleValue();
            none = false;
        }

        if (getBondsA() != null) {
            bo = getBondsA().doubleValue();
            none = false;
        }

        if (getFixedInterestA() != null) {
            fi = getFixedInterestA().doubleValue();
            none = false;
        }

        if (getHomeContentsA() != null) {
            ho = getHomeContentsA().doubleValue();
            none = false;
        }

        if (getCarsEtcA() != null) {
            ca = getCarsEtcA().doubleValue();
            none = false;
        }

        if (getPropertyA() != null) {
            pr = getPropertyA().doubleValue();
            none = false;
        }

        if (getGiftsA() != null) {
            gi = getGiftsA().doubleValue();
            none = false;
        }

        if (getLoansA() != null) {
            lo = getLoansA().doubleValue();
            none = false;
        }

        if (ageOver55(this.getAge())) {
            if (getSuperannuationA() != null) {
                su = getSuperannuationA().doubleValue();
                none = false;
            }
        }

        if (getAllocatedPensionA() != null) {
            al = getAllocatedPensionA().doubleValue();
            none = false;
        }

        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + ho + ca + pr + gi
                    + lo + su + al + co + cl + pa);

        return null;
    }

    /**
     * Total partner's Assets for Assets Test
     *  - includes every asset, except complying pension and superannuation.
     * superannuation depends on client's age, if age over age pension age than
     * add superannuation to the total
     * 
     * @return sum all of partner's assets for Assets Test
     */
    public BigDecimal getTotalAForAssetsTestPartner() {

        double sa = 0, ma = 0, sh = 0, bo = 0, fi = 0, ho = 0, ca = 0, pr = 0, gi = 0, lo = 0, su = 0, al = 0, co = 0, cl = 0, pa = 0;

        boolean none = true;

        if (getSavingsAPartner() != null) {
            sa = getSavingsAPartner().doubleValue();
            none = false;
        }

        if (getManagedFundsAPartner() != null) {
            ma = getManagedFundsAPartner().doubleValue();
            none = false;
        }

        if (getSharesAPartner() != null) {
            sh = getSharesAPartner().doubleValue();
            none = false;
        }

        if (getBondsAPartner() != null) {
            bo = getBondsAPartner().doubleValue();
            none = false;
        }

        if (getFixedInterestAPartner() != null) {
            fi = getFixedInterestAPartner().doubleValue();
            none = false;
        }

        if (getHomeContentsAPartner() != null) {
            ho = getHomeContentsAPartner().doubleValue();
            none = false;
        }

        if (getCarsEtcAPartner() != null) {
            ca = getCarsEtcAPartner().doubleValue();
            none = false;
        }

        if (getPropertyAPartner() != null) {
            pr = getPropertyAPartner().doubleValue();
            none = false;
        }

        if (getGiftsAPartner() != null) {
            gi = getGiftsAPartner().doubleValue();
            none = false;
        }

        if (getLoansAPartner() != null) {
            lo = getLoansAPartner().doubleValue();
            none = false;
        }

        // if ( this.getPartnerDateOfBirth() != null && ageOver55(this.getAge(
        // this.getPartnerDateOfBirth() ) ) ) {
        if (ageOver55(getPartnerAge())) {
            if (getSuperannuationAPartner() != null) {
                su = getSuperannuationAPartner().doubleValue();
                none = false;
            }
        }

        if (getAllocatedPensionAPartner() != null) {
            al = getAllocatedPensionAPartner().doubleValue();
            none = false;
        }

        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + ho + ca + pr + gi
                    + lo + su + al + co + cl + pa);

        return null;
    }

    /**
     * Total Joint Assets For Assets Test (client + partner assets for assets
     * test)
     * 
     * @return total joint assets for assets test
     */
    public BigDecimal getTotalAForAssetsTestJoint() {

        BigDecimal value = new BigDecimal(0.0);

        // add client total assets subject to deeming amount
        if (getTotalAForAssetsTestClient() != null) {
            value = value.add(getTotalAForAssetsTestClient());
        }
        // do we have a partner?
        if (getMaritalStatus() != null) {
            // marital status != Single,
            if (!single().equals(Boolean.TRUE)) {
                // add partner total assets subject to deeming amount
                if (getTotalAForAssetsTestPartner() != null) {
                    value = value.add(getTotalAForAssetsTestPartner());
                }
            }
        }

        return value;
    }

    /**
     * Total Partner Assets
     * 
     * @return sum all of partner's assets
     */
    public BigDecimal getTotalAPartner() {

        double sa = 0, ma = 0, sh = 0, bo = 0, fi = 0, ho = 0, ca = 0, pr = 0, gi = 0, lo = 0, su = 0, al = 0, co = 0, cl = 0, pa = 0;

        boolean none = true;

        if (getSavingsAPartner() != null) {
            sa = getSavingsAPartner().doubleValue();
            none = false;
        }

        if (getManagedFundsAPartner() != null) {
            ma = getManagedFundsAPartner().doubleValue();
            none = false;
        }

        if (getSharesAPartner() != null) {
            sh = getSharesAPartner().doubleValue();
            none = false;
        }

        if (getBondsAPartner() != null) {
            bo = getBondsAPartner().doubleValue();
            none = false;
        }

        if (getFixedInterestAPartner() != null) {
            fi = getFixedInterestAPartner().doubleValue();
            none = false;
        }

        if (getHomeContentsAPartner() != null) {
            ho = getHomeContentsAPartner().doubleValue();
            none = false;
        }

        if (getCarsEtcAPartner() != null) {
            ca = getCarsEtcAPartner().doubleValue();
            none = false;
        }

        if (getPropertyAPartner() != null) {
            pr = getPropertyAPartner().doubleValue();
            none = false;
        }

        if (getGiftsAPartner() != null) {
            gi = getGiftsAPartner().doubleValue();
            none = false;
        }

        if (getLoansAPartner() != null) {
            lo = getLoansAPartner().doubleValue();
            none = false;
        }

        if (getSuperannuationAPartner() != null) {
            su = getSuperannuationAPartner().doubleValue();
            none = false;
        }

        if (getAllocatedPensionAPartner() != null) {
            al = getAllocatedPensionAPartner().doubleValue();
            none = false;
        }

        if (getComplyingPensionAPartner() != null) {
            co = getComplyingPensionAPartner().doubleValue();
            none = false;
        }

        if (none == false)
            return new BigDecimal(sa + ma + sh + bo + fi + ho + ca + pr + gi
                    + lo + su + al + co + cl + pa);

        return null;
    }

    /**
     * Total ClientView Income <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * F = Total ClientView Income B = Total Deemed Income D = ClientView.Assessable
     * Income on Property
     * 
     * F = B + D + Superannuation + Allocated Pension + Complying Pension +
     * Salary/Wages
     * 
     * </PRE>
     * 
     * @return total client income
     */
    public BigDecimal getTotalIClient() {

        BigDecimal value = new BigDecimal(0.0);

        // add client's total deemed income
        if (getTotalAssetsSubjectToDeemingIClient() != null) {
            value = value.add(getTotalAssetsSubjectToDeemingIClient());
        }
        // add client's assessable income on property
        if (getAssessableIncomeOnPropertyClient() != null) {
            value = value.add(getAssessableIncomeOnPropertyClient());
        }
        // add client's superannuation income
        /*
         * if( ageOver55(this.getAge()) { if( getSuperannuationI() != null ) {
         * value = value.add(getSuperannuationI()); } }
         */
        // add client's allocated pension
        if (getAllocatedPensionI() != null) {
            value = value.add(getAllocatedPensionI());
        }
        // add client's complying pension
        if (getComplyingPensionI() != null) {
            value = value.add(getComplyingPensionI());
        }
        // add client's salary/wages
        if (getClientSalaryI() != null) {
            value = value.add(getClientSalaryI());
        }

        return value;
    }

    /**
     * Total Partner Income <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * P = Partner.Assessable Income on Property
     * 
     * sum = P + Superannuation + Allocated Pension + Complying Pension +
     * Salary/Wages
     * 
     * </PRE>
     * 
     * @return total partner income
     */
    public BigDecimal getTotalIPartner() {

        BigDecimal value = new BigDecimal(0.0);

        // add partner's assessable income on property
        if (getAssessableIncomeOnPropertyPartner() != null) {
            value = value.add(getAssessableIncomeOnPropertyPartner());
        }
        // add partner's superannuation income
        /*
         * if ( this.getPartnerDateOfBirth() != null && ageOver55(this.getAge(
         * this.getPartnerDateOfBirth() ) ) ) { if( getSuperannuationIPartner() !=
         * null ) { value = value.add(getSuperannuationIPartner()); } }
         */
        // add partner's allocated pension
        if (getAllocatedPensionIPartner() != null) {
            value = value.add(getAllocatedPensionIPartner());
        }
        // add partner's complying pension
        if (getComplyingPensionIPartner() != null) {
            value = value.add(getComplyingPensionIPartner());
        }
        // add partner's salary/wages
        if (getSalaryIPartner() != null) {
            value = value.add(getSalaryIPartner());
        }

        return value;
    }

    /**
     * Total Joint Income Amount <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * X = Total Joint Income Amount E = Total ClientView Income S = Total Partner
     * Income
     * 
     * X = E + S
     * 
     * </PRE>
     * 
     * @return total client income
     */
    public BigDecimal getTotalIJoint() {

        BigDecimal value = new BigDecimal(0.0);

        // add client total assets
        if (getTotalIClient() != null) {
            value = value.add(getTotalIClient());
        }
        // add partner total assets
        if (getTotalIPartner() != null) {
            value = value.add(getTotalIPartner());
        }

        return value;
    }

    /**
     * Total Joint Assets Subject To Deeming Amount
     * 
     * @return total joint assets subject to deeming amount
     */
    public BigDecimal getTotalAssetsSubjectToDeemingAJoint() {

        BigDecimal value = new BigDecimal(0.0);

        // add client total assets subject to deeming amount
        if (getTotalAssetsSubjectToDeemingAClient() != null) {
            value = value.add(getTotalAssetsSubjectToDeemingAClient());
        }

        if (getMaritalStatus() != null) {
            // marital status != Single,
            if (!single().equals(Boolean.TRUE)) {
                // add partner total assets subject to deeming amount
                if (getTotalAssetsSubjectToDeemingAPartner() != null) {
                    value = value.add(getTotalAssetsSubjectToDeemingAPartner());
                }
            }
        }

        return value;
    }

    /**
     * Total Joint Income Amount per fortnight <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * X = Total Joint Income Amount E = Total ClientView Income S = Total Partner
     * Income
     * 
     * X = E + S
     * 
     * </PRE>
     * 
     * @return total client income per fortnight
     */
    public BigDecimal getTotalJointIFortnightly() {

        BigDecimal value = new BigDecimal(0.0);

        // add total income per annum
        if (getTotalIJoint() != null) {
            value = value.add(getTotalIJoint());
        }
        // divide by 26 (fortnight)
        value = value.divide(new BigDecimal(26.0), BIGDECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);

        return value;
    }

    /*
     * client's assessable assets/income
     */
    public AssessableAssetsIncome getAssessableAssetsIncomeClient() {
        AssessableAssetsIncome copy_ai = new AssessableAssetsIncome(client_ai);
        // update income fields

        // Income CLIENT column
        copy_ai.savingsI = getSavingsI();
        copy_ai.managedFundsI = getManagedFundsI();
        copy_ai.sharesI = getSharesI();
        copy_ai.bondsI = getBondsI();
        copy_ai.interestI = getFixedInterestI();
        // copy_ai.homeContentsI = DSSData.STRING_EMPTY; // doesn't produce
        // income
        // copy_ai.carsETCI = DSSData.STRING_EMPTY; // doesn't produce income
        copy_ai.propertyI = getAssessableIncomeOnPropertyClient();
        copy_ai.giftsI = getGiftsI();
        copy_ai.loansI = getLoansI();
        // client over age pension age
        if (ageOver55(this.getAge())) {
            // yes, then add superannuation
            copy_ai.superannuationI = getSuperannuationI();
        } else {
            // no, ignore superannuation
            copy_ai.superannuationI = null;
        }
        copy_ai.complyingPensionI = getComplyingPensionI();
        copy_ai.allocatedPensionI = getAllocatedPensionI();

        // add client's and partner's complying and allocated pension income
        BigDecimal sum = new java.math.BigDecimal(0.0);

        // add client's allocated pension income
        if (getAllocatedPensionI() != null) {
            sum = sum.add(getAllocatedPensionI());
        }
        // add client's complying pension income
        if (getComplyingPensionI() != null) {
            sum = sum.add(getComplyingPensionI());
        }
        copy_ai.pensionI = sum;

        // com.fiducian.license.FiducianHelper.printFieldNames(copy_ai);
        // System.exit(0);
        return copy_ai;
    }

    /*
     * partner's assessable assets/income
     */
    public AssessableAssetsIncome getAssessableAssetsIncomePartner() {
        AssessableAssetsIncome copy_ai = new AssessableAssetsIncome(partner_ai);

        // update income fields

        // Income JOINT column
        copy_ai.savingsI = getSavingsIPartner();
        copy_ai.managedFundsI = getManagedFundsIPartner();
        copy_ai.sharesI = getSharesIPartner();
        copy_ai.bondsI = getBondsIPartner();
        copy_ai.interestI = getFixedInterestIPartner();
        // copy_ai.homeContentsI = DSSData.STRING_EMPTY; // doesn't produce
        // income
        // copy_ai.carsETCI = DSSData.STRING_EMPTY; // doesn't produce income
        copy_ai.propertyI = getAssessableIncomeOnPropertyPartner();
        copy_ai.giftsI = getGiftsIPartner();
        copy_ai.loansI = getLoansIPartner();
        // client over age pension age
        // if ( this.getPartnerDateOfBirth() != null && ageOver55(this.getAge(
        // this.getPartnerDateOfBirth() ) ) ) {
        if (ageOver55(getPartnerAge())) {
            // yes, then add superannuation
            copy_ai.superannuationI = getSuperannuationIPartner();
        } else {
            // no, ignore superannuation
            copy_ai.superannuationI = null;
        }
        copy_ai.complyingPensionI = getComplyingPensionIPartner();
        copy_ai.allocatedPensionI = getAllocatedPensionIPartner();

        // add client's and partner's complying and allocated pension income
        BigDecimal sum = new java.math.BigDecimal(0.0);

        // add client's allocated pension income
        if (getAllocatedPensionIPartner() != null) {
            sum = sum.add(getAllocatedPensionIPartner());
        }
        // add client's complying pension income
        if (getComplyingPensionIPartner() != null) {
            sum = sum.add(getComplyingPensionIPartner());
        }
        copy_ai.pensionI = sum;

        // com.fiducian.license.FiducianHelper.printFieldNames(copy_ai);
        // System.exit(0);
        return copy_ai;
    }

    /*
     * joint assessable assets/income
     */
    public AssessableAssetsIncome getAssessableAssetsIncomeJoint() {
        AssessableAssetsIncome copy_ai = new AssessableAssetsIncome(joint_ai);

        // calculate joint rental income (property income)
        BigDecimal sum = new java.math.BigDecimal(0.0);

        // add client's rental income
        if (getAssessableIncomeOnPropertyClient() != null) {
            sum = sum.add(getAssessableIncomeOnPropertyClient());
        }

        if (getMaritalStatus() != null) {
            // marital status != Single,
            if (!single().equals(Boolean.TRUE)) {
                // add partner's rental income
                if (getAssessableIncomeOnPropertyPartner() != null) {
                    sum = sum.add(getAssessableIncomeOnPropertyPartner());
                }
            }
        }
        copy_ai.propertyI = sum;

        // add client's and partner's salery/wage
        sum = new java.math.BigDecimal(0.0);

        // add client's saler/wage
        if (getClientSalaryI() != null) {
            sum = sum.add(getClientSalaryI());
        }
        if (getMaritalStatus() != null) {
            // marital status != Single,
            if (!single().equals(Boolean.TRUE)) {
                // add partner's saler/wage
                if (getSalaryIPartner() != null) {
                    sum = sum.add(getSalaryIPartner());
                }
            }
        }
        copy_ai.salaryWagesI = sum;

        // add client's and partner's complying and allocated pension income
        sum = new java.math.BigDecimal(0.0);

        BigDecimal sum_client = new BigDecimal(0.0);
        BigDecimal sum_partner = new BigDecimal(0.0);

        // add client's allocated pension income
        if (getAllocatedPensionI() != null) {
            sum_client = sum_client.add(getAllocatedPensionI());
        }
        // add client's complying pension income
        if (getComplyingPensionI() != null) {
            sum_client = sum_client.add(getComplyingPensionI());
        }
        // client_ai.pensionI = sum_client;

        if (getMaritalStatus() != null) {
            // marital status != Single,
            if (!single().equals(Boolean.TRUE)) {
                // add partner's allocated pension income
                if (getAllocatedPensionIPartner() != null) {
                    sum_partner = sum_partner
                            .add(getAllocatedPensionIPartner());
                }
                // add partner's complying pension income
                if (getComplyingPensionIPartner() != null) {
                    sum_partner = sum_partner
                            .add(getComplyingPensionIPartner());
                }
            }
        }
        // partner_ai.pensionI = sum_client;

        // client
        // joint
        sum = sum.add(sum_client);
        sum = sum.add(sum_partner);
        copy_ai.pensionI = sum;

        // com.fiducian.license.FiducianHelper.printFieldNames(copy_ai);
        // System.exit(0);
        return copy_ai;
    }

    /*
     * "assessable" assessable assets/income
     */
    public AssessableAssetsIncome getAssessableAssetsIncomeAssessable() {
        AssessableAssetsIncome copy_ai = getAssessableAssetsIncomeJoint();
        return copy_ai;
    }

    public boolean ageOver55(int age) {
        return (age > 55) ? true : false;
    }

    public int getPartnerAge() {
        int age = 0;
        if (getPartnerDateOfBirth() != null)
            age = getAge(getPartnerDateOfBirth());

        return age;
    }

    public BigDecimal getActualAgePensionPA() {
        //
        // calculate actual pension
        //
        BigDecimal pension_pa = new java.math.BigDecimal(0.0);
        BigDecimal pension_pf = new java.math.BigDecimal(0.0);

        pension_pf = pension_pf.add(getActualAgePensionPF());

        pension_pa = pension_pa.add(new java.math.BigDecimal(26.0)); // 52
                                                                        // weeks
                                                                        // pa
        pension_pa = pension_pa.multiply(pension_pf);

        return pension_pa;
    }

    public BigDecimal getActualAgePensionPF() {
        //
        // calculate actual pension
        //
        BigDecimal pension_pf_client = new java.math.BigDecimal(0.0);
        BigDecimal pension_pf_partner = new java.math.BigDecimal(0.0);

        // get martial status
        if (getMaritalStatus() != null
                && getMaritalStatus().equals(MaritalCode.SINGLE)) {
            if (agePension() && entitledForAgePension()) {
                pension_pf_client = pension_pf_client.add(getBasicBenefitC());
                if (getBasicBenefitC().compareTo(new BigDecimal(0.0)) > 0
                        && getPharmAllowanceC() != null) {
                    pension_pf_client = pension_pf_client
                            .add(getPharmAllowanceC());
                }
            }
        } else {
            // couple
            if (agePension() && entitledForAgePension()) {
                pension_pf_client = pension_pf_client.add(getBasicBenefitC());
                if (getBasicBenefitC().compareTo(new BigDecimal(0.0)) > 0
                        && getPharmAllowanceC() != null) {
                    pension_pf_client = pension_pf_client
                            .add(getPharmAllowanceC());
                }
            }

            if (agePensionPartner() && entitledForAgePensionPartner()) {
                pension_pf_partner = pension_pf_partner.add(getBasicBenefitP());
                if (getBasicBenefitP().compareTo(new BigDecimal(0.0)) > 0
                        && getPharmAllowanceP() != null) {
                    pension_pf_partner = pension_pf_partner
                            .add(getPharmAllowanceC());
                }
            }
        }
        return pension_pf_client.add(pension_pf_partner);
    }

    public BigDecimal getMaxAgePension() {
        //
        // calculate actual pension
        //
        BigDecimal pension_pa = new java.math.BigDecimal(0.0);
        BigDecimal pension_pf = new java.math.BigDecimal(0.0);

        // get martial status
        if (getMaritalStatus() != null
                && getMaritalStatus().equals(MaritalCode.SINGLE)) {
            // pension_pf =
            // dssCalc2.getMaritalStatus().equals(MaritalCode.SINGLE) ?
            // TaxCalc.MAX_AGE_PENSION_SINGLE : TaxCalc.MAX_AGE_PENSION_COUPLE;
            pension_pf = TaxUtils.MAX_AGE_PENSION_SINGLE;
            pension_pf = pension_pf
                    .add(TaxUtils.PHARMACUETICAL_ALLOWANCE_SINGLE);
            pension_pa = pension_pa.add(new java.math.BigDecimal(26.0)); // 52
                                                                            // weeks
                                                                            // pa
            pension_pa = pension_pa.multiply(pension_pf);
        } else {
            // couple
            // pension_pf =
            // dssCalc2.getMaritalStatus().equals(MaritalCode.SINGLE) ?
            // TaxCalc.MAX_AGE_PENSION_SINGLE : TaxCalc.MAX_AGE_PENSION_COUPLE;
            pension_pf = TaxUtils.MAX_AGE_PENSION_COUPLE;
            pension_pf = pension_pf
                    .add(TaxUtils.PHARMACUETICAL_ALLOWANCE_COUPLE);
            pension_pf = pension_pf.multiply(new BigDecimal(2.0));
            pension_pa = pension_pa.add(new java.math.BigDecimal(26.0)); // 52
                                                                            // weeks
                                                                            // pa
            pension_pa = pension_pa.multiply(pension_pf);
        }

        return pension_pa;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public boolean calculate() {
        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        financials.addAll(getGeneratedIncomes(desc));
        // financials.addAll( getGeneratedExpenses(desc) );
        // financials.addAll( getGeneratedLiabilities(desc) );

        return financials;
    }

    private java.util.Collection getGeneratedIncomes(String desc) {

        java.util.Collection financials = new java.util.ArrayList();

        BigDecimal amount = getActualAgePensionPA().add(getRentAssistanceC());
        if (amount.doubleValue() == 0.)
            return financials;

        RegularIncome income = new RegularIncome();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        income.setFinancialDesc("Income from " + desc + " on "
                + DateTimeUtils.asString(calendar.getTime()));
        income.setRegularAmount(amount);
        income.setFrequencyCodeID(FrequencyCode.YEARLY);
        income.setStartDate(calendar.getTime());
        income.setEndDate(DateTimeUtils.getEndOfFinancialYearDate(income
                .getStartDate()));
        income.setTaxable(true);
        income.setOwnerCodeID(OwnerCode.CLIENT);
        income.setFinancialTypeID(FinancialTypeID.INCOME_RETIREMENT);
        income.setRegularTaxType(ITaxConstants.I_OTHER_PENSIONS);
        financials.add(income);

        return financials;
    }

    private java.util.Collection getGeneratedExpenses(String desc) {
        java.util.Collection financials = new java.util.ArrayList();
        return financials;
    }

    private java.util.Collection getGeneratedLiabilities(String desc) {
        java.util.Collection financials = new java.util.ArrayList();
        return financials;
    }

}