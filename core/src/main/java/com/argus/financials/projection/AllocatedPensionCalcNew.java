/*
 * AllocatedPensionCalcNew.java
 *
 * Created on 30 August 2002, 14:21
 */

package com.argus.financials.projection;

/**
 * 
 * @author shibaevv
 * @version
 */

import java.math.BigDecimal;
import java.util.Date;

import com.argus.beans.format.CurrencyLabelGenerator;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.code.APRelationshipCode;
import com.argus.financials.code.DeathBenefitCode;
import com.argus.financials.code.FinancialTypeID;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.GeneralTaxExemptionCode;
import com.argus.financials.code.IReportFields;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.InvestmentStrategyData;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.code.SelectedAnnualPensionCode;
import com.argus.financials.code.SelectedAnnualPensionCodeID;
import com.argus.financials.code.SexCode;
import com.argus.financials.etc.BadArgumentException;
import com.argus.financials.projection.data.APConstants;
import com.argus.financials.projection.data.LifeExpectancy;
import com.argus.financials.projection.data.PensionValuation;
import com.argus.financials.report.ReportFields;
import com.argus.financials.report.Reportable;
import com.argus.financials.swing.table.UpdateableTableModel;
import com.argus.financials.swing.table.UpdateableTableRow;
import com.argus.financials.tax.au.ITaxConstants;
import com.argus.format.Percent;
import com.argus.io.JPEGFileFilter;
import com.argus.util.DateTimeUtils;

public final class AllocatedPensionCalcNew extends ETPCalcNew implements
        Reportable {
    private java.util.Date pensionStartDate;

    private java.util.Date firstPaymentDate;

    private Boolean ro;

    private Boolean flatRate;

    private Integer generalTaxExemptionID;

    private Integer deathBenefitID;

    private Integer pensionFrequencyID;

    private Integer selectedAnnualPensionType;

    private BigDecimal entryFee;

    private BigDecimal ongoingFee;

    private BigDecimal projectToAge;

    // private BigDecimal total;
    private Integer invStrategyCodeID;

    private BigDecimal netEarningRate;

    private BigDecimal otherValue;

    private BigDecimal annualIncreasePayments;

    // variables for ETP Details input
    private BigDecimal pre;

    private BigDecimal postTaxed;

    // variables for Partner
    private Integer partnerSexCodeID;

    private Integer relationshipID;

    private com.argus.math.Money money = new com.argus.math.Money();

    // variables for projections
    private int[] age;

    private double[] openingBalance;

    private double[] minimum;

    private double[] maximum;

    private double[] others;

    private double[] selectedPension;

    private double[] assessableIncome;

    private double[] netTaxPayable;

    private double[] rebate;

    private double[] excessRebate;

    private double[] medicareLevy;

    private double[] netPension;

    private double[] endBalance;

    private double[] netEarnings;

    private double[] annualCapitalGrowthRates;

    /** Creates new AllocatedPensionCalcNew */
    public AllocatedPensionCalcNew() {
        super();
        _clear();
    }

    /*
     * public AllocatedPensionCalcNew(AllocatedPensionCalcNew obj) { this();
     * 
     * this.assign(obj); // call in top derived class }
     */

    public Integer getDefaultModelType() {
        return ModelType.ALLOCATED_PENSION;
    }

    public String getDefaultTitle() {
        return "Allocated Pension";
    }

    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        if (property.equals(PENSION_START_DATE)) {
            setPensionStartDate(DateTimeUtils.getDate(value));
            setCalculationDate(DateTimeUtils.getDate(value));

        } else if (property.equals(FIRST_PAYMENT_DATE)) {
            setFirstPaymentDate(DateTimeUtils.getDate(value));

        } else if (property.equals(RO_YES)) {
            if (new String("Yes").equalsIgnoreCase(value)
                    || new String("true").equalsIgnoreCase(value)) {
                setRO(Boolean.TRUE);
            }
        } else if (property.equals(RO_NO)) {
            if (new String("No").equalsIgnoreCase(value)
                    || new String("true").equalsIgnoreCase(value)) {
                setRO(Boolean.FALSE);

            }
        } else if (property.equals(GENERAL_TAX_EXEMPTION)) {
            if (getNumberInstance().isValid(value))
                setGeneralTaxExemptionOptionID(new Integer(value));
            else
                setGeneralTaxExemptionOptionID(new GeneralTaxExemptionCode()
                        .getCode(value).getCodeIDInteger());

        } else if (property.equals(DEATH_BENEFIT)) {
            if (getNumberInstance().isValid(value))
                setDeathBenefitID(new Integer(value));
            else
                setDeathBenefitID(new DeathBenefitCode().getCode(value)
                        .getCodeIDInteger());

        } else if (property.equals(PENSION_FREQUENCY)) {
            if (getNumberInstance().isValid(value))
                setPensionFrequencyCodeID(new Integer(value));
            else
                setPensionFrequencyCodeID(new FrequencyCode().getCodeID(value));

        } else if (property.equals(ENTRY_FEE)) {
            setEntryFee(getPercentInstance().getBigDecimalValue(value));

        } else if (property.equals(ONGOING_FEE)) {
            setOngoingFee(getPercentInstance().getBigDecimalValue(value));

        } else if (property.equals(PROJECT_TO_AGE)) {
            setProjectToAge(getNumberInstance().getBigDecimalValue(value));

        } else if (property.equals(SELECTED_ANNUAL_PENSION)) {
            if (getNumberInstance().isValid(value))
                setSelectedAnnualPensionType(new Integer(value));
            else
                setSelectedAnnualPensionType(new SelectedAnnualPensionCode()
                        .getCode(value).getCodeIDInteger());

        } else if (property.equals(OTHER_VALUE)) {
            setOtherValue(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(ANNUAL_INCREASE_PAYMENTS)) {
            setAnnualIncreasePayments(getPercentInstance().getBigDecimalValue(
                    value));

        } else if (property.equals(PRE)) {
            setPre(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(POST_TAXED)) {
            setPostTaxed(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(RELATIONSHIP)) {
            if (getNumberInstance().isValid(value))
                setRelationshipID(new Integer(value));
            else
                setRelationshipID(new APRelationshipCode().getCode(value)
                        .getCodeIDInteger());

        } else if (property.equals(PARTNER_SEX_CODE)) {
            if (getNumberInstance().isValid(value))
                setPartnerSexCodeID(new Integer(value));
            else
                setPartnerSexCodeID(new SexCode().getCodeID(value));

            // } else if ( property.equals( TOTAL ) ) {
            // setTotal( getCurrencyInstance().getBigDecimalValue( value ) );

        } else if (property.equals(NET_EARNING_RATE)) {
            setNetEarningRate(getPercentInstance().getBigDecimalValue(value));

        } else if (property.equals(INV_STRATEGY)) {
            if (getNumberInstance().isValid(value))
                setInvStrategyCodeID(new Integer(value));
            else
                setInvStrategyCodeID(new InvestmentStrategyCode()
                        .getCodeID(value));

        } else if (property.equals(VARIABLE_CAPITAL_GROWTH_RATES)) {
            if (new String("Variable").equalsIgnoreCase(value)
                    || new String("true").equalsIgnoreCase(value)) {
                setFlatRate(Boolean.FALSE);
                annualCapitalGrowthRates = new double[getSize()]; // 0 or
                                                                    // years
                updateCapitalGrowthRates();
            }
            // setModified();

        } else if (property.equals(FLAT_CAPITAL_GROWTH_RATES)) {
            if (new String("Flat").equalsIgnoreCase(value)
                    || new String("true").equalsIgnoreCase(value)) {
                setFlatRate(Boolean.TRUE);

                annualCapitalGrowthRates = null;
                // setModified();
            }
        } else
            return false;

        return true;
    }

    /***************************************************************************
     * 
     */
    public void assign(MoneyCalc obj) {

        super.assign(obj);

        if (!(obj instanceof AllocatedPensionCalcNew))
            return;

        pensionStartDate = ((AllocatedPensionCalcNew) obj).pensionStartDate;
        firstPaymentDate = ((AllocatedPensionCalcNew) obj).firstPaymentDate;

        ro = ((AllocatedPensionCalcNew) obj).ro;
        flatRate = ((AllocatedPensionCalcNew) obj).flatRate;

        generalTaxExemptionID = ((AllocatedPensionCalcNew) obj).generalTaxExemptionID;
        deathBenefitID = ((AllocatedPensionCalcNew) obj).deathBenefitID;
        pensionFrequencyID = ((AllocatedPensionCalcNew) obj).pensionFrequencyID;

        entryFee = ((AllocatedPensionCalcNew) obj).entryFee;
        ongoingFee = ((AllocatedPensionCalcNew) obj).ongoingFee;
        projectToAge = ((AllocatedPensionCalcNew) obj).projectToAge;

        // total = ((AllocatedPensionCalcNew)obj).total;
        invStrategyCodeID = ((AllocatedPensionCalcNew) obj).invStrategyCodeID;
        netEarningRate = ((AllocatedPensionCalcNew) obj).netEarningRate;

        selectedAnnualPensionType = ((AllocatedPensionCalcNew) obj).selectedAnnualPensionType;
        otherValue = ((AllocatedPensionCalcNew) obj).otherValue;
        annualIncreasePayments = ((AllocatedPensionCalcNew) obj).annualIncreasePayments;

        pre = ((AllocatedPensionCalcNew) obj).pre;
        postTaxed = ((AllocatedPensionCalcNew) obj).postTaxed;

        partnerSexCodeID = ((AllocatedPensionCalcNew) obj).partnerSexCodeID;
        relationshipID = ((AllocatedPensionCalcNew) obj).relationshipID;

        if (this.getClass().equals(obj.getClass()))
            setModified();

        // do not do calc, just copy data
        /*
         * if ( ((AllocatedPensionCalcNew)obj).requiredIncomeValues != null ) {
         * requiredIncomeValues = new double [
         * ((AllocatedPensionCalcNew)obj).requiredIncomeValues.length ];
         * //System.arraycopy(
         * ((AllocatedPensionCalcNew)obj).requiredIncomeValues, 0,
         * requiredIncomeValues, 0,
         * ((AllocatedPensionCalcNew)obj).requiredIncomeValues.length );
         *  }
         */
    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {

        pensionStartDate = null;
        firstPaymentDate = null;

        ro = Boolean.FALSE;
        flatRate = Boolean.TRUE;

        generalTaxExemptionID = new Integer(0);
        deathBenefitID = new Integer(0);
        pensionFrequencyID = new Integer(0);
        invStrategyCodeID = new Integer(0);

        entryFee = ZERO;
        ongoingFee = ZERO;
        projectToAge = ZERO;

        // total = null;
        netEarningRate = ZERO;
        invStrategyCodeID = new Integer(0);

        selectedAnnualPensionType = new Integer(0);
        otherValue = ZERO;
        annualIncreasePayments = ZERO;

        pre = ZERO;
        postTaxed = ZERO;

        partnerSexCodeID = new Integer(0);
        relationshipID = new Integer(0);

        initArrays(0);
    }

    /***************************************************************************
     * 
     */
    public boolean isReady() {

        return ((getPartnerDOB() != null && getPartnerSexCodeID() != null) || (getDateOfBirth() != null && getSexCodeID() != null))
                && getEntryFee() != null
                && getNetEarningRate() != null
                && getOngoingFee() != null
                && getPensionStartDate() != null
                && getFirstPaymentDate() != null
                && getEligibleServiceDate() != null
                && getDeathBenefitID() != null
                && getGeneralTaxExemptionOptionID() != null
                && getSelectedAnnualPensionType() != null;

    }

    public int[] getAgeArray() {
        return age;
    }

    public double[] getOpeningBalanceArray() {
        return openingBalance;
    }

    public double[] getMinimumArray() {
        return minimum;
    }

    public double[] getMaximumArray() {
        return maximum;
    }

    public double[] getOthersArray() {
        return others;
    }

    public double[] getSelectedPensionArray() {
        return selectedPension;
    }

    public double[] getAssessableIncomeArray() {
        return assessableIncome;
    }

    public double[] getNetTaxPayableArray() {
        return netTaxPayable;
    }

    public double[] getRebateArray() {
        return rebate;
    }

    public double[] getExcessRebateArray() {
        return excessRebate;
    }

    public double[] getMedicareLevyArray() {
        return medicareLevy;
    }

    public double[] getNetPensionArray() {
        return netPension;
    }

    public double[] getEndBalanceArray() {
        return endBalance;
    }

    public double[] getNetEarningsArray() {
        return netEarnings;
    }

    /**
     * get/set for ETP Details input
     */

    public BigDecimal getPre() {
        return pre;
    }

    public void setPre(BigDecimal value) {
        if (equals(pre, value))
            return;

        pre = value;
        setModified();
    }

    public BigDecimal getPostTaxed() {
        return postTaxed;
    }

    public void setPostTaxed(BigDecimal value) {
        if (equals(postTaxed, value))
            return;

        postTaxed = value;
        setModified();
    }

    public BigDecimal getETPDetailsTotal() {
        return (getPre() == null ? ZERO : getPre()).add(
                getPostTaxed() == null ? ZERO : getPostTaxed()).add(
                getPost061983UntaxedTotal() == null ? ZERO
                        : getPost061983UntaxedTotal()).add(
                getUndeductedTotal() == null ? ZERO : getUndeductedTotal())
                .add(getCGTExemptTotal() == null ? ZERO : getCGTExemptTotal())
                .add(getExcessTotal() == null ? ZERO : getExcessTotal()).add(
                        getConcessionalTotal() == null ? ZERO
                                : getConcessionalTotal()).add(
                        getInvalidityTotal() == null ? ZERO
                                : getInvalidityTotal());
    }

    /**
     * get/set
     */

    /*
     * public String getClientSexCodeDesc() { return new
     * SexCode().getCodeDescription( getSexCodeID() ); }
     */
    public Integer getPartnerSexCodeID() {
        return partnerSexCodeID;
    }

    public void setPartnerSexCodeID(Integer value) {
        if (equals(partnerSexCodeID, value))
            return;

        partnerSexCodeID = value;
        setModified();
    }

    public String getPartnerSexCodeDesc() {
        return new SexCode().getCodeDescription(getPartnerSexCodeID());
    }

    public Integer getRelationshipID() {
        return relationshipID;
    }

    public void setRelationshipID(Integer value) {
        if (equals(relationshipID, value))
            return;

        relationshipID = value;
        setModified();

    }

    public java.util.Date getPensionStartDate() {
        return pensionStartDate;
    }

    public void setPensionStartDate(java.util.Date value) {
        if (equals(pensionStartDate, value))
            return;

        pensionStartDate = value;
        setModified();
    }

    public java.util.Date getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(java.util.Date value) {
        if (equals(firstPaymentDate, value))
            return;

        firstPaymentDate = value;
        setModified();
    }

    public Boolean getRO() {
        return ro;
    }

    public void setRO(Boolean value) {
        if (equals(ro, value))
            return;

        ro = value;
        setModified();

    }

    public Boolean getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(Boolean value) {
        if (equals(flatRate, value))
            return;

        flatRate = value;
        setModified();

    }

    public Integer getGeneralTaxExemptionOptionID() {
        return generalTaxExemptionID;
    }

    public void setGeneralTaxExemptionOptionID(Integer value) {
        if (equals(generalTaxExemptionID, value))
            return;

        generalTaxExemptionID = value;
        setModified();
    }

    public Integer getDeathBenefitID() {
        return deathBenefitID;
    }

    public void setDeathBenefitID(Integer value) {
        if (equals(deathBenefitID, value))
            return;

        deathBenefitID = value;
        setModified();

    }

    public Integer getPensionFrequencyCodeID() {
        return pensionFrequencyID;
    }

    public void setPensionFrequencyCodeID(Integer value) {
        if (equals(pensionFrequencyID, value))
            return;

        pensionFrequencyID = value;
        setModified();
    }

    public BigDecimal getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(BigDecimal value) {
        if (equals(entryFee, value))
            return;

        entryFee = value;
        setModified();

    }

    public BigDecimal getOngoingFee() {
        return ongoingFee == null ? ZERO : ongoingFee;
    }

    public void setOngoingFee(BigDecimal value) {
        if (equals(ongoingFee, value))
            return;

        ongoingFee = value;
        setModified();

    }

    public BigDecimal getProjectToAge() {
        return projectToAge;
    }

    public void setProjectToAge(BigDecimal value) {
        if (equals(projectToAge, value))
            return;

        projectToAge = value;
        setModified();

    }

    public Integer getSelectedAnnualPensionType() {
        return selectedAnnualPensionType;
    }

    public void setSelectedAnnualPensionType(Integer value) {
        if (equals(selectedAnnualPensionType, value))
            return;
        selectedAnnualPensionType = value;

        setModified();

    }

    public BigDecimal getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(BigDecimal value) {
        if (equals(otherValue, value))
            return;

        otherValue = value;
        setModified();

    }

    public BigDecimal getAnnualIncreasePayments() {
        return annualIncreasePayments;
    }

    public void setAnnualIncreasePayments(BigDecimal value) {
        if (equals(annualIncreasePayments, value))
            return;

        annualIncreasePayments = value;
        setModified();

    }

    /***************************************************************************
     * Calculation function
     **************************************************************************/
    public BigDecimal getTotalAmount() {
        // =IF(D53>0,D53,IF(AND(ETP_Opt2=FALSE,ETP_Opt3=FALSE),0,VLOOKUP($A16,APetpsource,D$14,0)))
        return null;
    }

    public BigDecimal getAnnualMinimumAmount() {
        if (getTotalETPAmount() == null || getAge() == (int) UNKNOWN_VALUE)
            return ZERO;

        try {

            double d = getPurchasePrice(getEntryFee()).doubleValue()
                    / PensionValuation.getValue(getAge(), true);
            // =ROUNDUP(H10/H11,-1)
            return new BigDecimal(d / 10).setScale(0, BigDecimal.ROUND_UP)
                    .multiply(new BigDecimal(10));
        } catch (BadArgumentException e) {
            return ZERO;
        }
    }

    public BigDecimal getAnnualMaximumAmount() {
        if (getTotalETPAmount() == null || getAge() == (int) UNKNOWN_VALUE)
            return ZERO;

        try {
            double d = getPurchasePrice(getEntryFee()).doubleValue()
                    / PensionValuation.getValue(getAge(), false);
            return new BigDecimal(d);
        } catch (BadArgumentException e) {
            return ZERO;
        }
    }

    /*
     * public BigDecimal getTotal() { return total; } public void setTotal(
     * BigDecimal value ) { if ( equals( total, value ) ) return;
     * 
     * total = value; setModified();
     *  }
     */
    public BigDecimal getNetEarningRate() {
        return netEarningRate == null ? ZERO : netEarningRate;
    }

    public void setNetEarningRate(BigDecimal value) {
        if (equals(netEarningRate, value))
            return;

        netEarningRate = value;
        setModified();

    }

    //
    public Integer getInvStrategyCodeID() {
        return invStrategyCodeID;
    }

    public void setInvStrategyCodeID(Integer value) {
        if (equals(invStrategyCodeID, value))
            return;

        invStrategyCodeID = value;

        updateCapitalGrowthRates();
        setNetEarningRate(new java.math.BigDecimal(getTotalReturnRate()));

        setModified();
    }

    public String getInvestmentStrategyCodeDesc() {
        return new InvestmentStrategyCode()
                .getCodeDescription(getInvStrategyCodeID());
    }

    public double getIncomeRate() {
        return getInvStrategyCodeID() == null
                || getInvStrategyCodeID().equals(new Integer(0)) ? 0
                : InvestmentStrategyCode.getGrowthRate(getInvStrategyCodeID())
                        .getIncomeRate();
    }

    // same capital growth rate 0..years-1
    public double getGrowthRate() {
        return getInvStrategyCodeID() == null
                || getInvStrategyCodeID().equals(new Integer(0)) ? 0
                : InvestmentStrategyCode.getGrowthRate(getInvStrategyCodeID())
                        .getGrowthRate();
    }

    public double getTotalReturnRate() {
        return getInvStrategyCodeID() == null
                || getInvStrategyCodeID().equals(new Integer(0)) ? 0
                : InvestmentStrategyCode.getGrowthRate(getInvStrategyCodeID())
                        .getRate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    // varying capital growth rate
    public double[] getAnnualCapitalGrowthRates() {
        return annualCapitalGrowthRates;
    }

    private void updateCapitalGrowthRates() {
        if (annualCapitalGrowthRates == null)
            return;

        Integer strategy = getInvStrategyCodeID();
        if (strategy == null || strategy.equals(new Integer(0)))
            return;

        double[] rates = InvestmentStrategyData
                .getAllocationHistDataFull(strategy);
        double incomeRate = getIncomeRate();

        int length = rates.length;
        for (int i = 0; i < annualCapitalGrowthRates.length; i++)
            annualCapitalGrowthRates[i] = incomeRate + rates[i % length];

    }

    /***************************************************************************
     * methods for input verifier
     **************************************************************************/
    public boolean isValidOtherAnnualPensionInputVerifier(double value) {

        if (value < getAnnualMaximumAmount().doubleValue()
                && value > getAnnualMinimumAmount().doubleValue()) {
            return true;
        }

        String msg = "ANNUAL PENSION PAYMENTS MUST FALL BETWEEN MAX & MIN PENSION LEVELS";
        javax.swing.JOptionPane.showMessageDialog(null, msg, "Error!",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;

    }

    /***************************************************************************
     * help methods
     **************************************************************************/

    public int getSize() {

        if (getProjectToAge() == null)
            return 0;

        int years = getProjectToAge().intValue() - getAge();
        return years + 1 < 0 ? 0 : years + (firstLineHide() ? 1 : 2);

    }

    public boolean firstLineHide() {
        if (getPensionStartDate() == null || getFirstPaymentDate() == null)
            return false;
        return getPensionStartDate().equals(getFirstPaymentDate());
    }

    private BigDecimal getPurchasePrice(BigDecimal fee) {
        return new BigDecimal((getTotalETPAmount() == null ? 0
                : getTotalETPAmount().doubleValue())
                * (100. - (fee == null ? 0 : fee.doubleValue())) / 100.);
    }

    public int getAge(java.util.Date laterDate, java.util.Date earlierDate) {
        Double ageD = DateTimeUtils.getAgeDouble(laterDate, earlierDate);
        if (ageD == null)
            return (int) UNKNOWN_VALUE;
        return new BigDecimal(ageD.doubleValue()).setScale(0,
                BigDecimal.ROUND_DOWN).intValue();
    }

    public int getAge() {
        int age = (int) UNKNOWN_VALUE;
        if (isClient == null || isClient.booleanValue()) {
            age = getAge(getPensionStartDate(), getDateOfBirth());
            return age == UNKNOWN_VALUE ? super.getAge(getDateOfBirth()) : age;
        } else {
            age = getAge(getPensionStartDate(), getPartnerDOB());
            return age == UNKNOWN_VALUE ? super.getAge(getPartnerDOB()) : age;
        }

    }

    public int getAge(java.util.Date dob) {
        int age = getAge(getPensionStartDate(), dob);

        return age == UNKNOWN_VALUE ? super.getAge(dob) : age;

    }

    public Date getAprilRuleEffectiveDate(Date pensionStartDate) {
        // =DATE(YEAR(H6),IF(I6>6,15,3),31)
        int effectiveYear_year = 0;
        int pensionStartDate_year = 0;
        int pensionStartDate_month = 0;

        if (pensionStartDate != null) {
            // create a new calendar object with the pensionStartDate
            java.util.Calendar pensionStartDate_calendar = java.util.GregorianCalendar
                    .getInstance();
            pensionStartDate_calendar.setTime(pensionStartDate);
            pensionStartDate_year = pensionStartDate_calendar
                    .get(java.util.Calendar.YEAR);
            pensionStartDate_month = pensionStartDate_calendar
                    .get(java.util.Calendar.MONTH);

            // check if we need to use the same year or not
            if (pensionStartDate_month >= java.util.Calendar.JULY
                    && pensionStartDate_month <= java.util.Calendar.DECEMBER) {
                effectiveYear_year = pensionStartDate_year + 1;
            } else {
                effectiveYear_year = pensionStartDate_year;
            }

            // set date for the end of financial year
            java.util.Calendar effectiveDate_calendar = java.util.GregorianCalendar
                    .getInstance();
            effectiveDate_calendar.setTime(DateTimeUtils.getDate("31/3/"
                    + effectiveYear_year));
            return effectiveDate_calendar.getTime();
        }

        return null;
    }

    /**
     * ATO UPP/Deductible Amount
     */
    public BigDecimal getATOUPPAmount() {
        // =IF(Links!B25=2,'AP Printout 1'!E15+'AP Printout 1'!E16+
        // 'AP Printout 1'!E19,'AP Printout 1'!E15+'AP Printout 1'!E16+
        // 'AP Printout 1'!E19+'AP Printout 1'!E12)
        // nicole said it should also include concessional
        if (getRO() == null || getRO().equals(Boolean.FALSE)) {

            return (getUndeductedTotal() == null ? ZERO : getUndeductedTotal())
                    .add(
                            getCGTExemptTotal() == null ? ZERO
                                    : getCGTExemptTotal()).add(
                            getInvalidityTotal() == null ? ZERO
                                    : getInvalidityTotal())
            // .add(getConcessionalTotal() == null? ZERO :
            // getConcessionalTotal())
            ;
        }

        BigDecimal pre = getPre071983Total();
        return (getUndeductedTotal() == null ? ZERO : getUndeductedTotal())
                .add(
                // getCGTExemptTotal() == null? ZERO : getCGTExemptTotal()
                // ).add(
                        // getInvalidityTotal() == null? ZERO :
                        // getInvalidityTotal() ).add(
                        (getPre() == null || ZERO.equals(getPre())) ? (pre == null ? ZERO
                                : pre)
                                : getPre()).add(
                        getConcessionalTotal() == null ? ZERO
                                : getConcessionalTotal());
    }

    public double getLifeExpectancy() {
        double le = 0;
        if (isClient == null || isClient.booleanValue()) {
            le = LifeExpectancy.getValue(getAge(), getSexCodeID());
            if (getDeathBenefitID() != null
                    && getDeathBenefitID()
                            .equals(DeathBenefitCode.REVERSIONARY))
                le = Math.max(le, LifeExpectancy.getValue(
                        getAge(getPartnerDOB()), getPartnerSexCodeID()));
        } else {
            le = LifeExpectancy.getValue(getAge(), getPartnerSexCodeID());
            if (getDeathBenefitID() != null
                    && getDeathBenefitID()
                            .equals(DeathBenefitCode.REVERSIONARY))
                le = Math.max(le, LifeExpectancy.getValue(
                        getAge(getDateOfBirth()), getSexCodeID()));

        }

        return le;
    }

    public double getATOAnnualDeductibleAmount() {
        if (getLifeExpectancy() == MoneyCalc.UNKNOWN_VALUE)
            return 0;

        return getATOUPPAmount().doubleValue() / getLifeExpectancy();
    }

    public double getFirstYearDeductibleAmountATO() {
        // =(H5-H6)/365*H24
        int daysLeft = DateTimeUtils
                .getDaysToEndOfFinancialYear(getPensionStartDate());
        return ((daysLeft - 1) / 365.) * getATOAnnualDeductibleAmount();
    }

    public double getProRataDatePercentage() {
        // =(H5-H6)/365.25
        int daysLeft = DateTimeUtils
                .getDaysToEndOfFinancialYear(getPensionStartDate());
        return (daysLeft - 1) / 365.25;
    }

    public double getProRataMinPension() {
        // =(H5-(H6-1))/365*H13
        int daysLeft = DateTimeUtils
                .getDaysToEndOfFinancialYear(getPensionStartDate());
        return ((daysLeft) / 365.) * (getAnnualMinimumAmount().doubleValue());
    }

    public double getProRataMaxPension() {
        // =(H5-(H6-1))/365*H14

        int daysLeft = DateTimeUtils
                .getDaysToEndOfFinancialYear(getPensionStartDate());
        return ((daysLeft) / 365.) * getAnnualMaximumAmount().doubleValue();
    }

    public boolean getAprilRuleApply() {
        // =IF(AND((H5+1)>H6,H6>I4),"Yes","No")
        Date aprilEffectiveDate = getAprilRuleEffectiveDate(getPensionStartDate());
        Date startOfFinancialYear = DateTimeUtils
                .getStartOfFinancialYearDate(getPensionStartDate());

        if (getPensionStartDate() == null || aprilEffectiveDate == null
                || startOfFinancialYear == null)
            return false;

        if (startOfFinancialYear.after(getPensionStartDate())
                && getPensionStartDate().after(aprilEffectiveDate))
            return true;

        return false;
    }

    /***************************************************************************
     * methods for projections
     **************************************************************************/
    /**
     * label/legend generators for graphView
     */
    private String[] labels;

    public String[] getLabels() {
        int[] age = getAgeArray();

        labels = new String[getSize()];
        for (int i = 0; i < getSize(); i++)
            labels[i] = String.valueOf(age[i]);

        return labels;
    }

    private void initArrays(int value) {

        if (value != 0)
            value = getSize();

        if (value > 0) {
            age = new int[value];
            openingBalance = new double[value];
            minimum = new double[value];
            maximum = new double[value];
            others = new double[value];
            selectedPension = new double[value];
            assessableIncome = new double[value];
            netTaxPayable = new double[value];
            rebate = new double[value];
            excessRebate = new double[value];
            medicareLevy = new double[value];
            netPension = new double[value];
            endBalance = new double[value];
            netEarnings = new double[value];

        } else {
            age = null;
            openingBalance = null;
            minimum = null;
            maximum = null;
            others = null;
            selectedPension = null;
            assessableIncome = null;
            netTaxPayable = null;
            rebate = null;
            excessRebate = null;
            medicareLevy = null;
            netPension = null;
            endBalance = null;
            netEarnings = null;

            annualCapitalGrowthRates = null;

        }

    }

    public boolean calculate() {

        pensionStartDate = pensionStartDate == null ? new java.util.Date()
                : pensionStartDate;
        firstPaymentDate = firstPaymentDate == null ? new java.util.Date()
                : firstPaymentDate;

        int size = getSize();
        initArrays(size);

        int ageAmount = 0;
        double openingBalanceAmount = 0;
        double minimumAmount = 0;
        double maximumAmount = 0;
        double othersAmount = 0;
        double selectedPensionAmount = 0;
        double assessableIncomeAmount = 0;
        double netTaxPayableAmount = 0;
        double rebateAmount = 0;
        double excessRebateAmount = 0;
        double medicareLevyAmount = 0;
        double netPensionAmount = 0;
        double endBalanceAmount = 0;
        double netEarningsAmount = 0;
        // help variables
        double purchasePriceLessEntryFee = getPurchasePrice(getEntryFee())
                .doubleValue();
        double annualDeductibleAmountATO = getATOAnnualDeductibleAmount();
        double pensionEarningsLessReviewFee = (getNetEarningRate()
                .subtract(getOngoingFee())).doubleValue() / 100.;
        double otherInputValue = getOtherValue() == null ? 0 : getOtherValue()
                .doubleValue();
        int generalTaxExemptionOptionID = 
            getGeneralTaxExemptionOptionID() == null ? -1 :
                getGeneralTaxExemptionOptionID().intValue();

        Integer sexID;
        if (isClient == null || isClient.booleanValue())
            sexID = getSexCodeID();
        else
            sexID = getPartnerSexCodeID();

        boolean firstLineHide = firstLineHide();
        int selectedPensionType = getSelectedAnnualPensionType() == null ? 0
                : getSelectedAnnualPensionType().intValue();

        double[] growthRates = getAnnualCapitalGrowthRates();
        if (growthRates != null && growthRates.length == 0) { // variable
                                                                // rates
            annualCapitalGrowthRates = new double[size]; // 0 or years
            updateCapitalGrowthRates();
            growthRates = getAnnualCapitalGrowthRates();
        }

        for (int i = 0; i < size; i++) {

            if (growthRates != null && growthRates.length > i) // variable
                                                                // rates
                pensionEarningsLessReviewFee = (growthRates[i] - getOngoingFee()
                        .doubleValue()) / 100.;

            if (!firstLineHide && i == 0) {
                // =IF($H$6=$H$7,"",ROUNDDOWN((H6-'AP Input data'!D4)/365.25,0))
                if (getPensionStartDate() != null
                        && !getPensionStartDate().equals(getFirstPaymentDate())) {
                    ageAmount = getAge();
                }

                // =IF($H$6=$H$7,0,H10)
                if (getPensionStartDate() != null
                        && !getPensionStartDate().equals(getFirstPaymentDate()))
                    openingBalanceAmount = purchasePriceLessEntryFee;
                else
                    openingBalanceAmount = 0;

                // =IF($H$4="yes",0,H15)
                if (!getAprilRuleApply()) {
                    minimumAmount = getProRataMinPension();
                } else
                    minimumAmount = 0;

                // =IF($H$4="yes",0,H16)
                if (!getAprilRuleApply())
                    maximumAmount = getProRataMaxPension();
                else
                    maximumAmount = 0;

                // =IF($H$4="Yes",0,'AP Input data'!G23*H8)
                if (!getAprilRuleApply())
                    othersAmount = otherInputValue * getProRataDatePercentage();

                // =IF(Links!$B$40=1,N7,IF(Links!$B$40=2,O7,'AP engine'!P7))

                if (selectedPensionType == SelectedAnnualPensionCodeID.iMIN)
                    selectedPensionAmount = minimumAmount;
                else if (selectedPensionType == SelectedAnnualPensionCodeID.iMAX)
                    selectedPensionAmount = maximumAmount;
                else if (selectedPensionType == SelectedAnnualPensionCodeID.iOTHER)
                    selectedPensionAmount = othersAmount;

                // =IF((Q7-$H$26)<=0,0,Q7-$H$26)
                double tempAssessableIncome = selectedPensionAmount
                        - getFirstYearDeductibleAmountATO();
                if (tempAssessableIncome > 0)
                    assessableIncomeAmount = tempAssessableIncome;
                else
                    assessableIncomeAmount = 0;

                // =IF((Q7-$H$24)*15%+(VLOOKUP(R7,LIRebate,2,TRUE)-((R7-20700)*VLOOKUP(R7,LIRebate,3,TRUE)))<0.001,0,
                // (Q7-$H$24)*15%+(VLOOKUP(R7,LIRebate,2,TRUE)-((R7-20700)*VLOOKUP(R7,LIRebate,3,TRUE))))
                double tempRebateAmount = (selectedPensionAmount - annualDeductibleAmountATO)
                        * TaxUtils.SUPER_TAX_RATE
                        / 100
                        + (APConstants.vLookUpLI(assessableIncomeAmount, 2,
                                true))
                        - ((assessableIncomeAmount - APConstants.liTag3) * (APConstants
                                .vLookUpLI(assessableIncomeAmount, 3, true)));
                rebateAmount = tempRebateAmount < 0.001 ? 0 : tempRebateAmount;

                // =IF((VLOOKUP(R8,TaxTabletoUse,2,TRUE)+(R8-VLOOKUP(R8,TaxTabletoUse,1,TRUE))*
                // VLOOKUP(R8,TaxTabletoUse,3,TRUE)-T8)<0,0,(VLOOKUP(R8,TaxTabletoUse,2,TRUE))+
                // ((R8-VLOOKUP(R8,TaxTabletoUse,1,TRUE))*VLOOKUP(R8,TaxTabletoUse,3,TRUE))-T8)
                double tempNetTaxPayable = APConstants.vLookUp(
                        assessableIncomeAmount, generalTaxExemptionOptionID, 2,
                        true)
                        + (assessableIncomeAmount - APConstants.vLookUp(
                                assessableIncomeAmount,
                                generalTaxExemptionOptionID, 1, true))
                        * APConstants.vLookUp(assessableIncomeAmount,
                                generalTaxExemptionOptionID, 3, true)
                        - rebateAmount;
                if (tempNetTaxPayable > 0)
                    netTaxPayableAmount = tempNetTaxPayable;
                else
                    netTaxPayableAmount = 0;

                // =IF((VLOOKUP(R7,TaxTabletoUse,2,TRUE))+((R7-VLOOKUP(R7,TaxTabletoUse,1,TRUE))*
                // VLOOKUP(R7,TaxTabletoUse,3,TRUE)-T7)>0,0,(VLOOKUP(R7,TaxTabletoUse,2,TRUE))+
                // ((R7-VLOOKUP(R7,TaxTabletoUse,1,TRUE))*VLOOKUP(R7,TaxTabletoUse,3,TRUE)-T7))
                double tempExcessRebate = APConstants.vLookUp(
                        assessableIncomeAmount, generalTaxExemptionOptionID, 2,
                        true)
                        + (assessableIncomeAmount - APConstants.vLookUp(
                                assessableIncomeAmount,
                                generalTaxExemptionOptionID, 1, true))
                        * APConstants.vLookUp(assessableIncomeAmount,
                                generalTaxExemptionOptionID, 3, true)
                        - rebateAmount;
                if (tempExcessRebate > 0)
                    excessRebateAmount = 0;
                else
                    excessRebateAmount = tempExcessRebate;

                // =IF(Links!$B$18=3,0,
                // IF(AND(Links!$B$22=1,'AP
                // engine'!L7<65),(R7*VLOOKUP(R7,ML,2,TRUE))+
                // ((R7-13807)*VLOOKUP(R7,ML,3,TRUE)),IF(AND(Links!$B$22=2,'AP
                // engine'!L7<62.5),
                // (R7*VLOOKUP(R7,ML,2,TRUE))+((R7-13807)*VLOOKUP(R7,ML,3,TRUE)),(R7*VLOOKUP(R7,MLAgePA,2,TRUE))+
                // ((R7-20000)*VLOOKUP(R7,MLAgePA,3,TRUE)))))
                if (generalTaxExemptionOptionID != APConstants.fr) {
                    if ((SexCode.MALE.equals(sexID) && ageAmount < APConstants.maleRetireAge)
                            || (SexCode.FEMALE.equals(sexID) && ageAmount < APConstants.femaleRetireAge))
                        medicareLevyAmount = assessableIncomeAmount
                                * APConstants.vLookUpML(assessableIncomeAmount,
                                        2, true)
                                + (assessableIncomeAmount - APConstants.mlTag3)
                                * APConstants.vLookUpML(assessableIncomeAmount,
                                        3, true);
                    else
                        medicareLevyAmount = assessableIncomeAmount
                                * APConstants.vLookUpMLAgePA(
                                        assessableIncomeAmount, 2, true)
                                + (assessableIncomeAmount - APConstants.mlpaTag3)
                                * APConstants.vLookUpMLAgePA(
                                        assessableIncomeAmount, 3, true);

                } else
                    medicareLevyAmount = 0;

                // =Q8-S8-V8
                netPensionAmount = selectedPensionAmount - netTaxPayableAmount
                        - medicareLevyAmount;
                // =M7*(1+((H7-H6)/365.25)*H36)
                // =M7*(1+((H7-H6)/365.25)*H36)
                int days = new DateTimeUtils().getDateDiff(
                        java.util.Calendar.DATE, getPensionStartDate(),
                        getFirstPaymentDate());
                endBalanceAmount = openingBalanceAmount
                        * (1 + (days / 365.25) * pensionEarningsLessReviewFee);
                netEarningsAmount = openingBalanceAmount * (days / 365.25)
                        * pensionEarningsLessReviewFee;

            } else if ((!firstLineHide && i == 1) || (firstLineHide && i == 0)) {
                // =ROUNDDOWN((H7-'AP Input data'!D4)/365.25,0)
                if (isClient == null || isClient.booleanValue())
                    ageAmount = getAge(getFirstPaymentDate(), getDateOfBirth());
                else
                    ageAmount = getAge(getFirstPaymentDate(), getPartnerDOB());

                // =IF(X7=0,H10,X7)
                openingBalanceAmount = endBalance[0] == 0 ? purchasePriceLessEntryFee
                        : endBalance[0];

                // =M8/VLOOKUP(L8,PVF,2,TRUE)
                try {
                    minimumAmount = openingBalanceAmount
                            / PensionValuation.getValue(ageAmount, true);
                } catch (BadArgumentException e) {
                    minimumAmount = 0;
                }

                // =M8/VLOOKUP(L8,PVF,3,TRUE)
                try {
                    maximumAmount = openingBalanceAmount
                            / PensionValuation.getValue(ageAmount, false);
                } catch (BadArgumentException e) {
                    maximumAmount = 0;
                }

                // =IF(P7<'AP Input data'!G23,'AP Input
                // data'!G23,IF(P7*(1+AnnualIncPay)>O8,O8,
                // IF(P7*(1+AnnualIncPay)<N8,N8,P7*(1+AnnualIncPay))))
                if (others[0] < otherInputValue)
                    othersAmount = otherInputValue;
                else {
                    double annualIncPay = others[0]
                            * (1 + (getAnnualIncreasePayments() == null ? 0
                                    : getAnnualIncreasePayments().doubleValue() / 100));
                    othersAmount = annualIncPay > maximumAmount ? maximumAmount
                            : (annualIncPay < minimumAmount ? minimumAmount
                                    : annualIncPay);

                }

                // =IF(Links!B41=1,N8,IF(Links!B41=2,O8,'AP engine'!P8))
                if (selectedPensionType == SelectedAnnualPensionCodeID.iMIN)
                    selectedPensionAmount = minimumAmount;
                else if (selectedPensionType == SelectedAnnualPensionCodeID.iMAX)
                    selectedPensionAmount = maximumAmount;
                else if (selectedPensionType == SelectedAnnualPensionCodeID.iOTHER)
                    selectedPensionAmount = othersAmount;

                // =IF((Q8-$H$24)>0,Q8-$H$24,0)
                double tempAssessableIncome = selectedPensionAmount
                        - annualDeductibleAmountATO;
                if (tempAssessableIncome > 0)
                    assessableIncomeAmount = tempAssessableIncome;
                else
                    assessableIncomeAmount = 0;

                double tempRebateAmount = (selectedPensionAmount - annualDeductibleAmountATO)
                        * TaxUtils.SUPER_TAX_RATE
                        / 100
                        + (APConstants.vLookUpLI(assessableIncomeAmount, 2,
                                true))
                        - ((assessableIncomeAmount - APConstants.liTag3) * (APConstants
                                .vLookUpLI(assessableIncomeAmount, 3, true)));
                rebateAmount = tempRebateAmount < 0.001 ? 0 : tempRebateAmount;

                // =IF((VLOOKUP(R8,TaxTabletoUse,2,TRUE)+(R8-VLOOKUP(R8,TaxTabletoUse,1,TRUE))*
                // VLOOKUP(R8,TaxTabletoUse,3,TRUE)-T8)<0,0,(VLOOKUP(R8,TaxTabletoUse,2,TRUE))+
                // ((R8-VLOOKUP(R8,TaxTabletoUse,1,TRUE))*VLOOKUP(R8,TaxTabletoUse,3,TRUE))-T8)
                double tempNetTaxPayable = APConstants.vLookUp(
                        assessableIncomeAmount, generalTaxExemptionOptionID, 2,
                        true)
                        + (assessableIncomeAmount - APConstants.vLookUp(
                                assessableIncomeAmount,
                                generalTaxExemptionOptionID, 1, true))
                        * APConstants.vLookUp(assessableIncomeAmount,
                                generalTaxExemptionOptionID, 3, true)
                        - rebateAmount;
                if (tempNetTaxPayable > 0)
                    netTaxPayableAmount = tempNetTaxPayable;
                else
                    netTaxPayableAmount = 0;

                double tempExcessRebate = APConstants.vLookUp(
                        assessableIncomeAmount, generalTaxExemptionOptionID, 2,
                        true)
                        + (assessableIncomeAmount - APConstants.vLookUp(
                                assessableIncomeAmount,
                                generalTaxExemptionOptionID, 1, true))
                        * APConstants.vLookUp(assessableIncomeAmount,
                                generalTaxExemptionOptionID, 3, true)
                        - rebateAmount;
                if (tempExcessRebate <= 0)
                    excessRebateAmount = tempExcessRebate;
                else
                    excessRebateAmount = 0;

                if (generalTaxExemptionOptionID != APConstants.fr) {
                    if ((SexCode.MALE.equals(sexID) && ageAmount < APConstants.maleRetireAge)
                            || (SexCode.FEMALE.equals(sexID) && ageAmount < APConstants.femaleRetireAge))
                        medicareLevyAmount = assessableIncomeAmount
                                * APConstants.vLookUpML(assessableIncomeAmount,
                                        2, true)
                                + (assessableIncomeAmount - APConstants.mlTag3)
                                * APConstants.vLookUpML(assessableIncomeAmount,
                                        3, true);
                    else
                        medicareLevyAmount = assessableIncomeAmount
                                * APConstants.vLookUpMLAgePA(
                                        assessableIncomeAmount, 2, true)
                                + (assessableIncomeAmount - APConstants.mlpaTag3)
                                * APConstants.vLookUpMLAgePA(
                                        assessableIncomeAmount, 3, true);
                } else
                    medicareLevyAmount = 0;

                netPensionAmount = selectedPensionAmount - netTaxPayableAmount
                        - medicareLevyAmount;

                // =(M8-Q8)*(1+('AP Input data'!$D$20-'AP Input data'!$G$11))
                endBalanceAmount = (openingBalanceAmount - selectedPensionAmount)
                        * (1 + pensionEarningsLessReviewFee);
                netEarningsAmount = (openingBalanceAmount - selectedPensionAmount)
                        * pensionEarningsLessReviewFee;

            } else {
                ageAmount = age[i - 1] + 1;
                openingBalanceAmount = endBalance[i - 1];

                try {
                    minimumAmount = openingBalanceAmount
                            / PensionValuation.getValue(ageAmount, true);
                } catch (BadArgumentException e) {
                }

                try {
                    maximumAmount = openingBalanceAmount
                            / PensionValuation.getValue(ageAmount, false);
                } catch (BadArgumentException e) {
                }

                double annualIncPay = others[i - 1]
                        * (1 + (getAnnualIncreasePayments() == null ? 0
                                : getAnnualIncreasePayments().doubleValue() / 100));
                // =IF(P8*(1+AnnualIncPay)>O9,O9,IF(P8*(1+AnnualIncPay)<N9,N9,P8*(1+AnnualIncPay)))
                othersAmount = annualIncPay > maximumAmount ? maximumAmount
                        : (annualIncPay < minimumAmount ? minimumAmount
                                : annualIncPay);

                if (selectedPensionType == SelectedAnnualPensionCodeID.iMIN)
                    selectedPensionAmount = minimumAmount;
                else if (selectedPensionType == SelectedAnnualPensionCodeID.iMAX)
                    selectedPensionAmount = maximumAmount;
                else if (selectedPensionType == SelectedAnnualPensionCodeID.iOTHER)
                    selectedPensionAmount = othersAmount;

                // =IF((Q8-$H$24)>0,Q8-$H$24,0)
                double tempAssessableIncome = selectedPensionAmount
                        - annualDeductibleAmountATO;
                if (tempAssessableIncome > 0)
                    assessableIncomeAmount = tempAssessableIncome;
                else
                    assessableIncomeAmount = 0;

                double tempRebateAmount = (selectedPensionAmount - annualDeductibleAmountATO)
                        * TaxUtils.SUPER_TAX_RATE
                        / 100
                        + (APConstants.vLookUpLI(assessableIncomeAmount, 2,
                                true))
                        - ((assessableIncomeAmount - APConstants.liTag3) * (APConstants
                                .vLookUpLI(assessableIncomeAmount, 3, true)));
                rebateAmount = tempRebateAmount < 0.001 ? 0 : tempRebateAmount;

                double tempNetTaxPayable = APConstants.vLookUp(
                        assessableIncomeAmount, generalTaxExemptionOptionID, 2,
                        true)
                        + (assessableIncomeAmount - APConstants.vLookUp(
                                assessableIncomeAmount,
                                generalTaxExemptionOptionID, 1, true))
                        * APConstants.vLookUp(assessableIncomeAmount,
                                generalTaxExemptionOptionID, 3, true)
                        - rebateAmount;

                if (tempNetTaxPayable > 0)
                    netTaxPayableAmount = tempNetTaxPayable;
                else
                    netTaxPayableAmount = 0;

                double tempExcessRebate = APConstants.vLookUp(
                        assessableIncomeAmount, generalTaxExemptionOptionID, 2,
                        true)
                        + (assessableIncomeAmount - APConstants.vLookUp(
                                assessableIncomeAmount,
                                generalTaxExemptionOptionID, 1, true))
                        * APConstants.vLookUp(assessableIncomeAmount,
                                generalTaxExemptionOptionID, 3, true)
                        - rebateAmount;
                if (tempExcessRebate <= 0)
                    excessRebateAmount = tempExcessRebate;
                else
                    excessRebateAmount = 0;

                if (generalTaxExemptionOptionID != APConstants.fr) {
                    if ((SexCode.MALE.equals(sexID) && ageAmount < APConstants.maleRetireAge)
                            || (SexCode.FEMALE.equals(sexID) && ageAmount < APConstants.femaleRetireAge))
                        medicareLevyAmount = assessableIncomeAmount
                                * APConstants.vLookUpML(assessableIncomeAmount,
                                        2, true)
                                + (assessableIncomeAmount - APConstants.mlTag3)
                                * APConstants.vLookUpML(assessableIncomeAmount,
                                        3, true);
                    else
                        medicareLevyAmount = assessableIncomeAmount
                                * APConstants.vLookUpMLAgePA(
                                        assessableIncomeAmount, 2, true)
                                + (assessableIncomeAmount - APConstants.mlpaTag3)
                                * APConstants.vLookUpMLAgePA(
                                        assessableIncomeAmount, 3, true);
                } else
                    medicareLevyAmount = 0;

                netPensionAmount = selectedPensionAmount - netTaxPayableAmount
                        - medicareLevyAmount;

                endBalanceAmount = (openingBalanceAmount - selectedPensionAmount)
                        * (1 + pensionEarningsLessReviewFee);
                netEarningsAmount = (openingBalanceAmount - selectedPensionAmount)
                        * pensionEarningsLessReviewFee;

            }

            age[i] = ageAmount;
            openingBalance[i] = openingBalanceAmount;
            minimum[i] = minimumAmount;
            maximum[i] = maximumAmount;
            others[i] = othersAmount;
            selectedPension[i] = selectedPensionAmount;
            assessableIncome[i] = assessableIncomeAmount;
            rebate[i] = rebateAmount;
            netTaxPayable[i] = netTaxPayableAmount;
            excessRebate[i] = excessRebateAmount;
            medicareLevy[i] = medicareLevyAmount;
            netPension[i] = netPensionAmount;
            endBalance[i] = endBalanceAmount;
            netEarnings[i] = netEarningsAmount;

        }

        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        calculate();
        financials.addAll(getGeneratedIncomes(desc));
        financials.addAll(getGeneratedExpenses(desc));
        // financials.addAll( getGeneratedLiabilities(desc) );

        return financials;
    }

    private java.util.Collection getGeneratedIncomes(String desc) {

        double[] amounts = getSelectedPensionArray();
        java.util.Date[] dates = getDates(FrequencyCode.YEARLY);

        java.util.Collection financials = new java.util.ArrayList();
        for (int i = 0; i < (amounts == null ? 0 : amounts.length); i++) {
            if (amounts[i] == 0.)
                continue;

            RegularIncome income = new RegularIncome();
            income.setFinancialDesc("Income from " + desc + " on "
                    + DateTimeUtils.asString(dates[i]));
            income.setRegularAmount(new java.math.BigDecimal(amounts[i]));
            income.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
            income.setStartDate(dates[i]);
            income.setEndDate(DateTimeUtils.getEndOfFinancialYearDate(income
                    .getStartDate()));
            income.setTaxable(true);
            income.setOwnerCodeID(OwnerCode.CLIENT);
            income.setFinancialTypeID(FinancialTypeID.INCOME_RETIREMENT);
            income.setRegularTaxType(ITaxConstants.I_OTHER_PENSIONS);
            financials.add(income);
        }

        return financials;
    }

    private java.util.Collection getGeneratedExpenses(String desc) {

        java.util.Date[] dates = getDates(FrequencyCode.YEARLY);
        int[] ages = getAgeArray();
        double upp = getATOUPPAmount().doubleValue();

        java.util.Collection financials = new java.util.ArrayList();
        for (int i = 0; i < ages.length; i++) {
            double le = 0;
            if (isClient == null || isClient.booleanValue())
                le = LifeExpectancy.getValue(ages[i], getSexCodeID());
            else
                le = LifeExpectancy.getValue(ages[i], getPartnerSexCodeID());

            double amount = le <= 0 ? 0. : upp / le;
            if (amount == 0.)
                continue;

            RegularExpense expense = new RegularExpense();
            expense.setFinancialDesc("Expense from " + desc + " on "
                    + DateTimeUtils.asString(dates[i]));
            expense.setRegularAmount(new java.math.BigDecimal(amount));
            expense.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
            expense.setStartDate(dates[i]);
            expense.setEndDate(DateTimeUtils
                    .getEndOfFinancialYearDate(dates[i]));
            expense.setTaxable(true);
            expense.setOwnerCodeID(OwnerCode.CLIENT);
            expense.setFinancialTypeID(FinancialTypeID.EXPENSE_OTHER);
            expense.setRegularTaxType(ITaxConstants.D_GENERAL);
            financials.add(expense);
        }

        return financials;
    }

    private java.util.Collection getGeneratedLiabilities(String desc) {
        return null;
    }

    /***************************************************************************
     * help methods
     **************************************************************************/
    public java.util.Date[] getDates(Integer frequencyCodeID) {
        java.util.Date date = getPensionStartDate();
        int size = getSize();
        java.util.Date[] data = new java.util.Date[size];
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        if (date == null) {
            calendar.setTime(date);
            int year = calendar.get(java.util.Calendar.YEAR);
            date = DateTimeUtils.getDate("30/6/" + year);
        }

        data[0] = date;
        for (int i = 1; i < size; i++) {
            calendar.setTime(data[i - 1]);
            calendar.set(java.util.Calendar.YEAR, calendar
                    .get(java.util.Calendar.YEAR) + 1);
            data[i] = calendar.getTime();
        }
        return data;
    }

    public void initializeReportData(ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields,
                com.argus.financials.service.ServiceLocator.getInstance()
                        .getClientPerson());

    }

    public void initializeReportData(ReportFields reportFields,
            com.argus.financials.service.PersonService person)
            throws java.io.IOException {

        if (person != null)
            reportFields.initialize(person);

        /*
         * Identify the client/partner details depending on if calculator is run
         * for existing client or as stand-alone
         */
        if (person == null) {
            reportFields.setValue(IReportFields.DateMedium, DateTimeUtils
                    .formatAsMEDIUM(new java.util.Date()));
            reportFields.setValue(IReportFields.AP_IsClient, "None");

            if (reportFields.getValue(IReportFields.Client_FullName) == null)
                reportFields.setValue(IReportFields.Client_FullName,
                        getClientName());
            if (reportFields.getValue(IReportFields.Client_DOB) == null)
                reportFields.setValue(IReportFields.Client_DOB, DateTimeUtils
                        .asString(getDateOfBirth()));
            if (reportFields.getValue(IReportFields.Client_Sex) == null)
                reportFields.setValue(IReportFields.Client_Sex, new SexCode()
                        .getCodeDescription(getSexCodeID()));

            /*
             * this is defaults if initialized to null reportFields.setValue(
             * IReportFields.Client_MaritalCode,"None");
             * 
             * reportFields.setValue( IReportFields.Partner_FullName, "");
             * reportFields.setValue( IReportFields.Partner_DOB, "");
             * reportFields.setValue( IReportFields.Partner_Sex, "None");
             * reportFields.setValue( IReportFields.Partner_MaritalCode,"None");
             * 
             * reportFields.setValue( IReportFields.Adviser_FullName, "");
             */
        } else if (getIsClient() != null && getIsClient().booleanValue()) {
            reportFields.setValue(IReportFields.AP_IsClient, "Client");

        } else {
            reportFields.setValue(IReportFields.AP_IsClient, "Partner");

        }

        // data.init( person, apCalc );
        // Build table Assumptions

        calculate();

        // Build calc assumptions table

        UpdateableTableModel utm = new UpdateableTableModel(new String[] {
                "Assumptions", "" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");

        values.add("Pension Start Date");
        values.add(DateTimeUtils.asString(getPensionStartDate()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Eligible Service Date");
        values.add(DateTimeUtils.asString(getEligibleServiceDate()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Death Benefit Option");
        values.add(new DeathBenefitCode().getCode(getDeathBenefitID())
                .getCodeDesc());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(IReportFields.AP_Assumptions, utm);

        // Pension Payment Levels
        utm = new UpdateableTableModel(new String[] { "Pension Payment Levels",
                "" });

        values.add("Pension Payment Option");
        values.add(new SelectedAnnualPensionCode().getCode(
                getSelectedAnnualPensionType()).getCodeDesc());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        String selectedAnnualPensionAmount = "";
        if (APConstants.OTHER.equals(getSelectedAnnualPensionType()))
            selectedAnnualPensionAmount = money.roundToTen(getOtherValue())
                    .toString();
        else if (APConstants.MAX.equals(getSelectedAnnualPensionType()))
            selectedAnnualPensionAmount = money.roundToTen(
                    getAnnualMaximumAmount()).toString();
        else if (APConstants.MIN.equals(getSelectedAnnualPensionType()))
            selectedAnnualPensionAmount = money.roundToTen(
                    getAnnualMinimumAmount()).toString();

        values.add("Annual Payment");
        values.add(selectedAnnualPensionAmount);
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Minimum Payment");
        values.add(money.roundToTen(getAnnualMinimumAmount()).toString());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Maximum Payment");
        values.add(money.roundToTen(getAnnualMaximumAmount()).toString());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Deductible Amount");
        values.add(currency.toString(getATOAnnualDeductibleAmount()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("First Payment Date");
        values.add(DateTimeUtils.asString(getFirstPaymentDate()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Net Earning Rate");
        values.add(Percent.getPercentInstance().toString(getNetEarningRate()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Net Earning Rate Type");
        values
                .add((getFlatRate() != null && getFlatRate().booleanValue()) ? "Flat"
                        : "Variable");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(IReportFields.AP_Payment_Level, utm);

        // Components

        // Pension Payment Levels
        utm = new UpdateableTableModel(new String[] { "Components", "" });

        values.add("Pre 1 July 1983");
        values.add(currency.toString(getPre071983Total()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30 June 1983 - Taxed ");
        values.add(currency.toString(getPost061983TaxedTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        values.add("Post 30 June 1983 - Untaxed ");
        values.add(currency.toString(getPost061983UntaxedTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Undeducted ");
        values.add(currency.toString(getUndeductedTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt ");
        values.add(currency.toString(getCGTExemptTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess Component");
        values.add(currency.toString(getExcessTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Concessional");
        values.add(currency.toString(getConcessionalTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity");
        values.add(currency.toString(getInvalidityTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add(currency.toString(getTotalETPAmount()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(IReportFields.AP_Components, utm);

        // Allocated Pension Projection

        utm = new UpdateableTableModel(new String[] { "Age", "Opening Balance",
                "Selected Pension", "Assessable Income", "Pension Rebate",
                "Earnings", "End Balance", "Net Pension" });

        columClasses.clear();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        if (age != null) {

            for (int i = 0; i < age.length; i++) {
                values.add(new Integer(age[i]).toString());
                values.add(money.roundToTen(openingBalance[i]).toString());
                values.add(money.roundToTen(selectedPension[i]).toString());
                values.add(money.roundToTen(assessableIncome[i]).toString());
                values.add(money.roundToTen(rebate[i]).toString());
                values.add(money.roundToTen(netEarnings[i]).toString());
                values.add(money.roundToTen(endBalance[i]).toString());
                values.add(money.roundToTen(netPension[i]).toString());

                row = new UpdateableTableRow(values, columClasses);
                utm.addRow(row);
                values.clear();

            }

        }

        reportFields.setValue(IReportFields.AP_Projection, utm);

        // Initialize graph function
        com.argus.financials.GraphView graphView = new APGraphViewNew();
        // SwingUtils.setDefaultFont( graphView );
        graphView.setPreferredSize(new java.awt.Dimension(700, 500));

        graphView.customizeChart(new double[][] { getEndBalanceArray() },
                getLabels(),
                new String[] { "<html>Assets <i>(end-balance)</i></html>" },
                new java.awt.Color[] { java.awt.Color.blue }, true);
        graphView.setTitleAxisY1("$");
        graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator.getInstance());

        java.io.File file = java.io.File.createTempFile("tmp", "."
                + JPEGFileFilter.JPG);
        file.deleteOnExit();
        graphView.encodeAsJPEG(file);

        reportFields.setValue(IReportFields.AP_GraphName,
                "Assets (estimated closing balances)");
        reportFields.setValue(IReportFields.AP_Graph, file.getCanonicalPath());

    }

}