/*
 * ETPCalc.java
 *
 * Created on 12 December 2001, 10:00
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import java.math.BigDecimal;

import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.code.FinancialTypeID;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.projection.data.ETPConstants;
import com.argus.financials.swing.table.UpdateableTableModel;
import com.argus.financials.swing.table.UpdateableTableRow;
import com.argus.financials.tax.au.ITaxConstants;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;

public class ETPCalcNew extends MoneyCalc implements
        com.argus.financials.report.Reportable {
    // constants
    public static final int NO = 0;

    public static final int ROLLOVER_ALL = 1;

    public static final int WITHDRAW_ALL = 2;

    public static final int PARTIAL_WITHDRAW = 3;

    public static final int WITHDRAW_UP_TO_THRESHOLD = 4;

    private BigDecimal DECIMAL_ZERO = new BigDecimal(0.00);

    // option variable
    private int option;

    // details
    private java.util.Date eligibleServiceDate;

    private java.util.Date calculationDate;

    private int age;

    // known ETP components
    private String clientName;

    private java.util.Date partnerDOB;

    private String partnerName;

    protected Boolean isClient;

    private BigDecimal cgtExemptTotal;

    private BigDecimal undeductedTotal;

    private BigDecimal concessionalTotal;

    private BigDecimal invalidityTotal;

    private BigDecimal post061983UntaxedTotal;

    private BigDecimal excessTotal;

    private BigDecimal post061983ThresholdUsed;

    private BigDecimal thresholdLeftPartialWithdraw;

    // partial withdraw variables
    private BigDecimal cgtExemptPartial;

    private BigDecimal undeductedPartial;

    private BigDecimal concessionalPartial;

    private BigDecimal invalidityPartial;

    private BigDecimal excessPartial;

    // private BigDecimal etpPartialWithdrawalRollover;
    private BigDecimal etpPartialWithdrawalWithdraw;

    // variables for checkbox
    private boolean rollover;

    private boolean rolloverAndRecon;

    /** Creates new ETPCalc */
    public ETPCalcNew() {
        super();
        _clear();
    }

    /*
     * public ETPCalcNew(ETPCalc obj) {
     * 
     * this.assign(obj); // call in top derived class
     *  }
     */

    public Integer getDefaultModelType() {
        return ModelType.ETP_ROLLOVER;
    }

    public String getDefaultTitle() {
        return "ETP Rollover";
    }

    /**
     * 
     */
    protected boolean update(Object property, String value) {
        if (super.update(property, value))
            return true;

        if (property.equals(CLIENT_NAME)) {
            setClientName(value == null ? null : value.trim());

        } else if (property.equals(NAME_PARTNER)) {
            setPartnerName(value == null ? null : value.trim());

        } else if (property.equals(DOB_PARTNER)) {

        } else if (property.equals(IS_CLIENT)) {
            if (Boolean.TRUE.equals(Boolean.valueOf(value)))
                setIsClient(Boolean.TRUE);
            else
                setIsClient(Boolean.FALSE);

        } else if (property.equals(ELIGIBLE_SERVICE_DATE)) {
            setEligibleServiceDate(DateTimeUtils.getDate(value));

        } else if (property.equals(CALCULATION_DATE)) {
            setCalculationDate(DateTimeUtils.getDate(value));

        } else if (property.equals(POST_061983_THRESHOLD_USED)) {
            setPost061983ThresholdUsed(getCurrencyInstance()
                    .getBigDecimalValue(value));

        } else if (property.equals(TOTAL_ETP_AMOUNT)) {
            setTotalETPAmount(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(CGT_EXEMPT)) {
            setCGTExemptTotal(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(UNDEDUCTED)) {
            setUndeductedTotal(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(CONCESSIONAL)) {
            setConcessionalTotal(getCurrencyInstance()
                    .getBigDecimalValue(value));

        } else if (property.equals(POST_JUNE_94_INVALIDITY)) {
            setInvalidityTotal(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(POST_JUNE_1983_UNTAXED)) {
            setPost061983UntaxedTotal(getCurrencyInstance().getBigDecimalValue(
                    value));

        } else if (property.equals(EXCESS)) {
            setExcessTotal(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(ETP_PARTIAL_WITHDRAWAL_ROLLOVER)) {
            setETPPartialWithdrawalWithdraw(getCurrencyInstance()
                    .getBigDecimalValue(value));

        } else if (property.equals(ETP_PARTIAL_WITHDRAWAL_UNDEDUCTED)) {
            setUndeductedPartial(getCurrencyInstance()
                    .getBigDecimalValue(value));

        } else if (property.equals(ETP_PARTIAL_WITHDRAWAL_CGT_EXEMPT)) {
            setCGTExemptPartial(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(ETP_PARTIAL_WITHDRAWAL_EXCESS)) {
            setExcessPartial(getCurrencyInstance().getBigDecimalValue(value));

        } else if (property.equals(ETP_PARTIAL_WITHDRAWAL_CONCESSIONAL)) {
            setConcessionalPartial(getCurrencyInstance().getBigDecimalValue(
                    value));

        } else if (property.equals(ETP_PARTIAL_WITHDRAWAL_INVALIDITY)) {
            setInvalidityPartial(getCurrencyInstance()
                    .getBigDecimalValue(value));

        } else if (property.equals(WITHDRAW_ALL_OPTION)) {
            if (value != null && value.equalsIgnoreCase("true"))
                setOption(WITHDRAW_ALL);

        } else if (property.equals(ROLLOVER_ALL_OPTION)) {
            if (value != null && value.equalsIgnoreCase("true"))
                setOption(ROLLOVER_ALL);

        } else if (property.equals(WITHDRAW_UP_TO_THRESHOLD_OPTION)) {
            if (value != null && value.equalsIgnoreCase("true"))
                setOption(WITHDRAW_UP_TO_THRESHOLD);

        } else if (property.equals(PARTIAL_WITHDRAW_OPTION)) {
            if (value != null && value.equalsIgnoreCase("true"))
                setOption(PARTIAL_WITHDRAW);

        } else if (property.equals(NO_OPTION)) {
            if (value != null && value.equalsIgnoreCase("true"))
                setOption(NO);

        } else if (property.equals(ROLLOVER_TO_ALLOCATED_PENSION)) {
            if (value != null)
                setRollover(Boolean.getBoolean(value));
        } else if (property
                .equals(ROLLOVER_AND_RECONTRIBUTE_TO_ALLOCATED_PENSION)) {
            if (value != null)
                setRolloverAndRecon(Boolean.getBoolean(value));
        } else

            return false;

        return true;
    }

    /**
     * 
     */
    public void assign(MoneyCalc obj) {
        super.assign(obj);

        if (!(obj instanceof ETPCalcNew))
            return;

        clientName = ((ETPCalcNew) obj).clientName;
        partnerName = ((ETPCalcNew) obj).partnerName;
        partnerDOB = ((ETPCalcNew) obj).partnerDOB;
        isClient = ((ETPCalcNew) obj).isClient;
        eligibleServiceDate = ((ETPCalcNew) obj).eligibleServiceDate;
        calculationDate = ((ETPCalcNew) obj).calculationDate;
        post061983ThresholdUsed = ((ETPCalcNew) obj).post061983ThresholdUsed;
        cgtExemptTotal = ((ETPCalcNew) obj).cgtExemptTotal;
        undeductedTotal = ((ETPCalcNew) obj).undeductedTotal;
        concessionalTotal = ((ETPCalcNew) obj).concessionalTotal;
        invalidityTotal = ((ETPCalcNew) obj).invalidityTotal;
        post061983UntaxedTotal = ((ETPCalcNew) obj).post061983UntaxedTotal;
        excessTotal = ((ETPCalcNew) obj).excessTotal;

        // partial withdraw
        cgtExemptPartial = ((ETPCalcNew) obj).cgtExemptPartial;
        undeductedPartial = ((ETPCalcNew) obj).undeductedPartial;
        concessionalPartial = ((ETPCalcNew) obj).concessionalPartial;
        invalidityPartial = ((ETPCalcNew) obj).invalidityPartial;
        excessPartial = ((ETPCalcNew) obj).excessPartial;
        etpPartialWithdrawalWithdraw = ((ETPCalcNew) obj).etpPartialWithdrawalWithdraw;

        if (this.getClass().equals(obj.getClass()))
            setModified();

    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        // details
        clientName = null;
        partnerName = null;
        partnerDOB = null;
        option = 0;

        isClient = null;
        eligibleServiceDate = null;
        calculationDate = null;

        // known ETP components
        cgtExemptTotal = null;
        undeductedTotal = null;
        concessionalTotal = null;
        invalidityTotal = null;
        post061983UntaxedTotal = null;
        excessTotal = null;
        post061983ThresholdUsed = null;
        thresholdLeftPartialWithdraw = null;

        // partial withdraw variables
        cgtExemptPartial = null;
        undeductedPartial = null;
        concessionalPartial = null;
        invalidityPartial = null;
        excessPartial = null;
        etpPartialWithdrawalWithdraw = null;
    }

    /**
     * 
     */
    public boolean isReady() {
        return true;
        // getSaved();

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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String value) {
        if (equals(partnerName, value))
            return;

        partnerName = value;
        setModified();
    }

    public java.util.Date getPartnerDOB() {
        return partnerDOB;
    }

    public void setPartnerDOB(java.util.Date value) {
        if (equals(partnerDOB, value))
            return;

        partnerDOB = value;
        setModified();
    }

    public Boolean getIsClient() {
        return isClient;
    }

    public void setIsClient(Boolean value) {
        if (equals(isClient, value))
            return;

        isClient = value;
        isModified();
    }

    public boolean getRollover() {
        return rollover;
    }

    public void setRollover(boolean value) {
        rollover = value;
    }

    public boolean getRolloverAndRecon() {
        return rolloverAndRecon;
    }

    public void setRolloverAndRecon(boolean value) {
        rolloverAndRecon = value;
    }

    public java.util.Date getEligibleServiceDate() {
        return eligibleServiceDate;
    }

    public void setEligibleServiceDate(java.util.Date value) {
        if (equals(eligibleServiceDate, value))
            return;

        eligibleServiceDate = value;

        setModified();
    }

    public java.util.Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(java.util.Date value) {
        if (equals(calculationDate, value))
            return;

        /*
         * if ( value != null && ETPConstants.TAX_1983_COMMENCE_DATE.after(
         * value ) ) { javax.swing.JOptionPane.showMessageDialog( null, "Date
         * could never be before 1/7/1983", "Error",
         * javax.swing.JOptionPane.ERROR_MESSAGE ); throw new
         * IllegalArgumentException( "Date could never be before 1/7/1983" ); //
         * throw new CalcDateException( "Date could never be before 1/7/1983" ); }
         */

        calculationDate = value;

        setModified();
    }

    public BigDecimal getPost061983ThresholdUsed() {
        return post061983ThresholdUsed == null ? DECIMAL_ZERO
                : post061983ThresholdUsed;
    }

    public void setPost061983ThresholdUsed(BigDecimal value) {
        if (equals(post061983ThresholdUsed, value))
            return;

        post061983ThresholdUsed = value;

        setModified();
    }

    public BigDecimal getTotalETPAmount() {
        if (getInitialValue() == UNKNOWN_VALUE || getInitialValue() < 0)
            return DECIMAL_ZERO;

        return new BigDecimal(getInitialValue());
    }

    public void setTotalETPAmount(BigDecimal value) {
        if (value != null)
            setInitialValue(value.doubleValue());
    }

    public BigDecimal getCGTExemptTotal() {
        return cgtExemptTotal == null ? DECIMAL_ZERO : cgtExemptTotal;
    }

    public void setCGTExemptTotal(BigDecimal value) {
        if (equals(cgtExemptTotal, value))
            return;

        cgtExemptTotal = value;
        setModified();
    }

    public BigDecimal getPost061983UntaxedTotal() {
        return post061983UntaxedTotal == null ? DECIMAL_ZERO
                : post061983UntaxedTotal;
    }

    public void setPost061983UntaxedTotal(BigDecimal value) {
        if (equals(post061983UntaxedTotal, value))
            return;

        post061983UntaxedTotal = value;
        setModified();
    }

    public BigDecimal getUndeductedTotal() {
        return undeductedTotal == null ? DECIMAL_ZERO : undeductedTotal;
    }

    public void setUndeductedTotal(BigDecimal value) {
        if (equals(undeductedTotal, value))
            return;

        undeductedTotal = value;
        setModified();
    }

    public BigDecimal getConcessionalTotal() {
        return concessionalTotal == null ? DECIMAL_ZERO : concessionalTotal;
    }

    public void setConcessionalTotal(BigDecimal value) {
        if (equals(concessionalTotal, value))
            return;

        concessionalTotal = value;
        setModified();
    }

    public BigDecimal getInvalidityTotal() {
        return invalidityTotal == null ? DECIMAL_ZERO : invalidityTotal;
    }

    public void setInvalidityTotal(BigDecimal value) {
        if (equals(invalidityTotal, value))
            return;

        invalidityTotal = value;
        setModified();
    }

    public BigDecimal getExcessTotal() {
        return excessTotal == null ? DECIMAL_ZERO : excessTotal;
    }

    public void setExcessTotal(BigDecimal value) {
        if (equals(excessTotal, value))
            return;

        excessTotal = value;
        setModified();
    }

    /**
     * get Partial Withdraw Known value
     */
    public BigDecimal getETPPartialWithdrawalWithdraw() {
        return etpPartialWithdrawalWithdraw == null ? DECIMAL_ZERO
                : etpPartialWithdrawalWithdraw;
    }

    public void setETPPartialWithdrawalWithdraw(BigDecimal value) {
        if (equals(etpPartialWithdrawalWithdraw, value))
            return;
        etpPartialWithdrawalWithdraw = value;

        setModified();
    }

    public BigDecimal getPre071983Total() {
        if (getTotalETPAmount() == null)
            return DECIMAL_ZERO;
        BigDecimal pre = getPreTotal().min(
                getPrePostTotal().subtract(
                        getUndeductedTotal() == null ? ZERO
                                : getUndeductedTotal()));

        return pre.compareTo(new BigDecimal(0.01)) < 0 ? ZERO : pre;
    }

    public BigDecimal getPost061983TaxedTotal() {
        if (getTotalETPAmount() == null)
            return DECIMAL_ZERO;
        // =IF((ETP-UndConts-CGT-Excess-Conces-Invalidity-Pre-Untaxed)<0.001,0,ETP-UndConts-CGT-Excess-Conces-Invalidity-Pre-Untaxed)
        BigDecimal postTaxed = getPrePostTotal().subtract(
                getPre071983Total() == null ? ZERO : getPre071983Total())
                .subtract(
                        getUndeductedTotal() == null ? ZERO
                                : getUndeductedTotal()).subtract(
                        getPost061983UntaxedTotal() == null ? ZERO
                                : getPost061983UntaxedTotal());

        return postTaxed.compareTo(new BigDecimal(0.001)) < 0 ? ZERO
                : postTaxed;

    }

    /***************************************************************************
     * methods for Rollover All
     **************************************************************************/
    public BigDecimal getPre071983RolloverAll() {

        BigDecimal afterTax = getPrePostTotal().subtract(
                getTaxOnPost061983UntaxedForRolloverAll());

        // =IF(Untaxed=0,pre,MIN((ETP-ROTax-CGT-Excess-Conces-Invalidity)*pre%,(ETP-ROTax-CGT-Excess-Conces-Invalidity)-UndConts))
        return getPost061983UntaxedTotal() == null ? getPre071983Total()
                : (afterTax.multiply(new BigDecimal(getPrePercentage())))
                        .min(afterTax
                                .subtract(getUndeductedTotal() == null ? ZERO
                                        : getUndeductedTotal()));

    }

    public BigDecimal getTaxOnPost061983UntaxedForRolloverAll() {
        return getPost061983UntaxedTotal() == null ? ZERO
                : getPost061983UntaxedTotal().multiply(
                        new BigDecimal(ETPConstants.SUPER_TAX_RATE / 100));
    }

    public BigDecimal getPost061983TaxedRolloverAll() {
        // =IF(untaxed=0,postTaxed,ETP-ROTax-UndConts-CGT-Excess-Conces-Invalidity-PreRolloverAll)

        return getPost061983UntaxedTotal() == null ? getPost061983TaxedTotal()
                : getPrePostTotal().subtract(
                        getTaxOnPost061983UntaxedForRolloverAll()).subtract(
                        getUndeductedTotal() == null ? ZERO
                                : getUndeductedTotal()).subtract(
                        getPre071983RolloverAll());
    }

    public BigDecimal getTotalAmountRolloverAll() {
        return getTotalETPAmount() == null ? ZERO : getTotalETPAmount()
                .subtract(getTaxOnPost061983UntaxedForRolloverAll());

    }

    /***************************************************************************
     * methods for Withdraw All
     **************************************************************************/
    public BigDecimal getTaxOnWithdrawalExcess() {
        if (getExcessTotal() == null)
            return ZERO;
        return getExcessTotal().multiply(
                new BigDecimal(ETPConstants.EXCESSIVE_TAX_RATE));
    }

    public BigDecimal getExcessNetETP() {
        if (getExcessTotal() == null)
            return ZERO;
        return getExcessTotal().subtract(getTaxOnWithdrawalExcess());
    }

    public BigDecimal getConcessionalTaxOnWithdrawal() {
        if (getConcessionalTotal() == null || getTaxRate() <= 0)
            return ZERO;
        return getConcessionalTotal().multiply(
                new BigDecimal(ETPConstants.ASSESABLE_INCOME_RATE)).multiply(
                new BigDecimal(getTaxRate() / 100));
    }

    public BigDecimal getPre071983TaxOnWithdrawal() {
        if (getPre071983Total() == null || getTaxRate() <= 0)
            return ZERO;
        return getPre071983Total().multiply(
                new BigDecimal(ETPConstants.ASSESABLE_INCOME_RATE
                        * (getTaxRate() / 100)));
    }

    public BigDecimal getPost061983UntaxedTaxOnWithdrawal() {
        BigDecimal untaxed = getPost061983UntaxedTotal();

        if (untaxed == null)
            return ZERO;

        double thresholdLeftAfter = getThresholdLeftAfterPost061983TaxedDeducted()
                .doubleValue();
        double value = untaxed.doubleValue() - thresholdLeftAfter;

        // =IF(Age<55,UntaxedOpt2*VLOOKUP(UntaxedOpt2,UntaxedTable,2,TRUE),
        // VLOOKUP(UntaxedOpt2,UntaxedTable,3,TRUE)*VLOOKUP($D$18,UntaxedTable,1,FALSE))+
        // VLOOKUP(UntaxedOpt2+D19,UntaxedTable,3,TRUE)*(
        // IF((UntaxedOpt2-VLOOKUP(D18,UntaxedTable,1,FALSE))<0.001,0,(UntaxedOpt2-VLOOKUP(D18,UntaxedTable,1,FALSE))))
        return getAge() < ETPConstants.TAX_EFFECTIVE_AGE ? untaxed
                .multiply(new BigDecimal(vLookUp(this, false, WITHDRAW_ALL,
                        untaxed.doubleValue(), true))) : new BigDecimal(
                vLookUp(this, false, WITHDRAW_ALL, untaxed.doubleValue(), true)
                        * thresholdLeftAfter
                        + vLookUp(this, false, WITHDRAW_ALL, untaxed.add(
                                new BigDecimal(10000000)).doubleValue(), true)
                        * (value < 0.001 ? 0 : value));
    }

    public BigDecimal getPost061983TaxedTaxOnWithdrawal() {

        BigDecimal postTaxed = getPost061983TaxedTotal();

        if (postTaxed == null)
            return ZERO;

        double taxAmount = postTaxed.doubleValue()
                - (getThresholdLeft().doubleValue() <= 0 ? 0
                        : getThresholdLeft().doubleValue());
        // =IF(Age<55,PosttaxedOpt2*VLOOKUP(PosttaxedOpt2,PostTable,2,TRUE),
        // (VLOOKUP(PosttaxedOpt2,PostTable,3,TRUE))*(PosttaxedOpt2-(VLOOKUP(A18,PostTable,1,FALSE))))
        return getAge() < ETPConstants.TAX_EFFECTIVE_AGE ? postTaxed
                .multiply(new BigDecimal(vLookUp(this, true, WITHDRAW_ALL,
                        postTaxed.doubleValue(), true)))
                : new BigDecimal(vLookUp(this, true, WITHDRAW_ALL, postTaxed
                        .doubleValue(), true)
                        * (taxAmount <= 0 ? 0 : taxAmount));

    }

    public BigDecimal getThresholdLeftAfterPost061983TaxedDeducted() {
        // =IF(PosttaxedOpt4>VariablesParameters!F20-Post_ThresholdUsed,0,A32-PosttaxedOpt4)
        if ((getPost061983TaxedTotal() == null ? ZERO
                : getPost061983TaxedTotal()).compareTo(getThresholdLeft()) >= 0)
            return ZERO;

        return getThresholdLeft().subtract(
                getPost061983TaxedTotal() == null ? ZERO
                        : getPost061983TaxedTotal());
    }

    public BigDecimal getPost061983TaxedNetETP() {
        if (getPost061983TaxedTotal() == null)
            return DECIMAL_ZERO;

        return getPost061983TaxedTotal().subtract(
                getPost061983TaxedTaxOnWithdrawal() == null ? ZERO
                        : getPost061983TaxedTaxOnWithdrawal());

    }

    public BigDecimal getPost061983UntaxedNetETP() {
        if (getPost061983UntaxedTotal() == null)
            return DECIMAL_ZERO;

        return getPost061983UntaxedTotal().subtract(
                getPost061983UntaxedTaxOnWithdrawal() == null ? ZERO
                        : getPost061983UntaxedTaxOnWithdrawal());

    }

    public BigDecimal getTotalAmountTaxOnWithdrawal() {
        double concessionalTax = getConcessionalTaxOnWithdrawal() == null ? 0
                : getConcessionalTaxOnWithdrawal().doubleValue();
        double pre071983Tax = getPre071983TaxOnWithdrawal() == null ? 0
                : getPre071983TaxOnWithdrawal().doubleValue();
        double post061983TaxedTax = getPost061983TaxedTaxOnWithdrawal() == null ? 0
                : getPost061983TaxedTaxOnWithdrawal().doubleValue();
        double post061983UntaxedTax = getPost061983UntaxedTaxOnWithdrawal() == null ? 0
                : getPost061983UntaxedTaxOnWithdrawal().doubleValue();
        double excessTax = getTaxOnWithdrawalExcess() == null ? 0
                : getTaxOnWithdrawalExcess().doubleValue();

        double totalTax = concessionalTax + pre071983Tax + post061983TaxedTax
                + post061983UntaxedTax + excessTax;

        return totalTax == 0 ? null : new BigDecimal(totalTax);
    }

    public BigDecimal getTotalAmountNetETP() {

        if (getTotalETPAmount() == null)
            return ZERO;

        return getTotalETPAmount()
                .subtract(getPost061983TaxedTaxOnWithdrawal()).subtract(
                        getPost061983UntaxedTaxOnWithdrawal()).subtract(
                        getTaxOnWithdrawalExcess());
    }

    /***************************************************************************
     * methods for Partial Withdraw
     **************************************************************************/
    public BigDecimal getUndeductedPartial() {
        return undeductedPartial == null ? DECIMAL_ZERO : undeductedPartial;
    }

    public void setUndeductedPartial(BigDecimal value) {
        if (equals(undeductedPartial, value))
            return;

        undeductedPartial = value;

        setModified();
    }

    public BigDecimal getCGTExemptPartial() {
        return cgtExemptPartial == null ? DECIMAL_ZERO : cgtExemptPartial;
    }

    public void setCGTExemptPartial(BigDecimal value) {
        if (equals(cgtExemptPartial, value))
            return;

        cgtExemptPartial = value;

        setModified();
    }

    public BigDecimal getExcessPartial() {
        return excessPartial == null ? DECIMAL_ZERO : excessPartial;
    }

    public void setExcessPartial(BigDecimal value) {
        if (equals(excessPartial, value))
            return;

        excessPartial = value;

        setModified();
    }

    public BigDecimal getConcessionalPartial() {
        return concessionalPartial == null ? DECIMAL_ZERO : concessionalPartial;
    }

    public void setConcessionalPartial(BigDecimal value) {
        if (equals(concessionalPartial, value))
            return;

        concessionalPartial = value;

        setModified();
    }

    public BigDecimal getInvalidityPartial() {
        return invalidityPartial == null ? DECIMAL_ZERO : invalidityPartial;
    }

    public void setInvalidityPartial(BigDecimal value) {
        if (equals(invalidityPartial, value))
            return;

        invalidityPartial = value;

        setModified();
    }

    public BigDecimal getETPPartialWithdrawalRolloverAvail() {

        return getETPPartialWithdrawalRollover() == null ? ZERO
                : getETPPartialWithdrawalRollover().subtract(
                        getPost061983UntaxedTaxOnRolloverPartialWithdraw());

    }

    public BigDecimal getETPPartialWithdrawalRollover() {
        if (getTotalETPAmount() == null)
            return DECIMAL_ZERO;

        return getTotalETPAmount().subtract(
                getETPPartialWithdrawalWithdraw() == null ? ZERO
                        : getETPPartialWithdrawalWithdraw());

    }

    public BigDecimal getETPPartialWithdrawalRollover(BigDecimal value) {
        if (getTotalETPAmount() == null)
            return DECIMAL_ZERO;

        return getTotalETPAmount().subtract(value == null ? ZERO : value);

    }

    public BigDecimal getUndeductedAvail() {
        return (getUndeductedTotal() == null ? ZERO : getUndeductedTotal())
                .subtract(getUndeductedPartial() == null ? ZERO
                        : getUndeductedPartial());
    }

    public BigDecimal getUndeductedAvail(BigDecimal value) {
        return (getUndeductedTotal() == null ? ZERO : getUndeductedTotal())
                .subtract(value == null ? ZERO : value);
    }

    public BigDecimal getCGTExemptAvail() {
        return (getCGTExemptTotal() == null ? ZERO : getCGTExemptTotal())
                .subtract(getCGTExemptPartial() == null ? ZERO
                        : getCGTExemptPartial());
    }

    public BigDecimal getCGTExemptAvail(BigDecimal value) {
        return (getCGTExemptTotal() == null ? ZERO : getCGTExemptTotal())
                .subtract(value == null ? ZERO : value);
    }

    public BigDecimal getExcessAvail() {
        return (getExcessTotal() == null ? ZERO : getExcessTotal())
                .subtract(getExcessPartial() == null ? ZERO
                        : getExcessPartial());
    }

    public BigDecimal getExcessAvail(BigDecimal value) {
        return (getExcessTotal() == null ? ZERO : getExcessTotal())
                .subtract(value == null ? ZERO : value);
    }

    public BigDecimal getConcessionalAvail() {
        return (getConcessionalTotal() == null ? ZERO : getConcessionalTotal())
                .subtract(getConcessionalPartial() == null ? ZERO
                        : getConcessionalPartial());
    }

    public BigDecimal getConcessionalAvail(BigDecimal value) {
        return (getConcessionalTotal() == null ? ZERO : getConcessionalTotal())
                .subtract(value == null ? ZERO : value);
    }

    public BigDecimal getInvalidityAvail() {
        return (getInvalidityTotal() == null ? ZERO : getInvalidityTotal())
                .subtract(getInvalidityPartial() == null ? ZERO
                        : getInvalidityPartial());
    }

    public BigDecimal getInvalidityAvail(BigDecimal value) {
        return (getInvalidityTotal() == null ? ZERO : getInvalidityTotal())
                .subtract(value == null ? ZERO : value);
    }

    public BigDecimal getPre071983TaxOnWithdrawalPartial() {
        if (getPre071983WithdrawPartialWithdraw() == null || getTaxRate() <= 0)
            return ZERO;
        return getPre071983WithdrawPartialWithdraw().multiply(
                new BigDecimal(ETPConstants.ASSESABLE_INCOME_RATE
                        * getTaxRate() / 100));
    }

    public BigDecimal getPre071983WithdrawPartialWithdraw() {
        BigDecimal partialWithdraw = getETPPartialWithdrawalWithdraw();

        if (partialWithdraw == null)
            return ZERO;

        BigDecimal rest = partialWithdraw
                .subtract(getRestTotalPartialWithdraw());
        return new BigDecimal(rest.doubleValue() * getPrePercentage()).min(rest
                .subtract(getUndeductedPartial() == null ? ZERO
                        : getUndeductedPartial()));
    }

    public BigDecimal getPre071983PartialWithdrawRollover() {
        // =IF(K60=0,K58,MIN((K57-F60-K62-K63-K64-K65)*$H$6,(K57-F60-K62-K63-K64-K65)-K61))
        BigDecimal rollover = getETPPartialWithdrawalRollover();
        if (rollover == null)
            return ZERO;
        BigDecimal untaxedTaxOnRollover = getPost061983UntaxedTaxOnRolloverPartialWithdraw();
        BigDecimal restTotalRollover = getRestTotalRolloverPartialWithdraw();
        BigDecimal rest = rollover.subtract(untaxedTaxOnRollover).subtract(
                restTotalRollover);

        return (rest.multiply(new BigDecimal(getPrePercentage()))).min(rest
                .subtract(getUndeductedAvail()));
    }

    public BigDecimal getPost061983TaxedTaxOnWithdrawalPartial() {
        BigDecimal postTaxed = getPost061983TaxedWithdrawPartialWithdraw();

        if (postTaxed == null)
            return ZERO;
        double taxAmount = postTaxed.doubleValue()
                - (getThresholdLeft().doubleValue() <= 0 ? 0
                        : getThresholdLeft().doubleValue());
        // =IF(Age<55,G59*VLOOKUP(G59,PostTable,2,TRUE),(VLOOKUP(G59,PostTable,3,TRUE))*(G59-(VLOOKUP(A18,PostTable,1,FALSE))))
        return getAge() < 55 ? postTaxed.multiply(new BigDecimal(vLookUp(this,
                true, WITHDRAW_ALL, postTaxed.doubleValue(), true)))
                : new BigDecimal(vLookUp(this, true, WITHDRAW_ALL, postTaxed
                        .doubleValue(), true)
                        * (
                        // postTaxed.doubleValue() - vLookUp(this, true,
                        // WITHDRAW_ALL, getThresholdLeft().doubleValue(),
                        // false)));
                        taxAmount <= 0 ? 0 : taxAmount));

    }

    public BigDecimal getPost061983TaxedPartialWithdrawRollover() {
        // =IF(K60=0,K59,K57-F60-K61-K62-K63-K64-K65-E58)
        BigDecimal rollover = getETPPartialWithdrawalRollover();
        if (rollover == null)
            return ZERO;
        BigDecimal untaxedTaxOnRollover = getPost061983UntaxedTaxOnRolloverPartialWithdraw();
        BigDecimal restTotalRollover = getRestTotalRolloverPartialWithdraw();
        BigDecimal rest = rollover.subtract(untaxedTaxOnRollover).subtract(
                restTotalRollover);

        return rest.subtract(getUndeductedAvail()).subtract(
                getPre071983PartialWithdrawRollover());

    }

    public BigDecimal getPost061983TaxedNetETPPartial() {
        if (getPost061983TaxedWithdrawPartialWithdraw() == null)
            return DECIMAL_ZERO;

        return getPost061983TaxedWithdrawPartialWithdraw().subtract(
                getPost061983TaxedTaxOnWithdrawalPartial() == null ? ZERO
                        : getPost061983TaxedTaxOnWithdrawalPartial());

    }

    public BigDecimal getPost061983TaxedWithdrawPartialWithdraw() {

        if (getETPPartialWithdrawalWithdraw() == null)
            return ZERO;
        // =G57-G61-G62-G63-G64-G65-G58
        return getETPPartialWithdrawalWithdraw().subtract(
                getRestTotalPartialWithdraw()).subtract(
                getPre071983WithdrawPartialWithdraw()).subtract(
                getUndeductedPartial() == null ? ZERO : getUndeductedPartial());

    }

    public BigDecimal getPost061983UntaxedTaxOnWithdrawPartialWithdraw() {
        return ZERO;
    }

    public BigDecimal getPost061983UntaxedNetETPPartialWithdraw() {

        if (getPost061983UntaxedWithdrawPartialWithdraw() == null)
            return ZERO;

        return getPost061983UntaxedWithdrawPartialWithdraw()
                .subtract(
                        getPost061983UntaxedTaxOnWithdrawPartialWithdraw() == null ? ZERO
                                : getPost061983UntaxedTaxOnWithdrawPartialWithdraw());
    }

    public BigDecimal getPost061983UntaxedWithdrawPartialWithdraw() {

        return DECIMAL_ZERO;
    }

    public BigDecimal getPost061983UntaxedTaxOnRolloverPartialWithdraw() {
        if (getPost061983UntaxedTotal() == null)
            return ZERO;

        return (getPost061983UntaxedTotal()
                .subtract(getPost061983UntaxedWithdrawPartialWithdraw() == null ? ZERO
                        : getPost061983UntaxedWithdrawPartialWithdraw()))
                .multiply(new BigDecimal(ETPConstants.SUPER_TAX_RATE / 100));

    }

    public BigDecimal getTaxOnWithdrawalExcessPartialWithdraw() {
        if (getExcessPartial() == null)
            return ZERO;
        return getExcessPartial().multiply(
                new BigDecimal(ETPConstants.EXCESSIVE_TAX_RATE));
    }

    public BigDecimal getExcessNetETPPartialWithdraw() {
        if (getExcessPartial() == null)
            return ZERO;
        return getExcessPartial().subtract(
                getTaxOnWithdrawalExcessPartialWithdraw());
    }

    public BigDecimal getConcessionalTaxOnWithdrawalPartialWithdraw() {
        if (getConcessionalPartial() == null || getTaxRate() <= 0)
            return ZERO;
        return getConcessionalPartial().multiply(
                new BigDecimal(ETPConstants.ASSESABLE_INCOME_RATE)).multiply(
                new BigDecimal(getTaxRate() / 100));
    }

    public BigDecimal getTotalAmountTaxOnWithdrawalPartial() {
        return getConcessionalTaxOnWithdrawalPartialWithdraw().add(
                getPre071983TaxOnWithdrawalPartial()).add(
                getPost061983TaxedTaxOnWithdrawalPartial()).add(
                getPost061983UntaxedTaxOnWithdrawPartialWithdraw()).add(
                getTaxOnWithdrawalExcessPartialWithdraw());
    }

    public BigDecimal getTotalAmountNetETPPartial() {
        if (getETPPartialWithdrawalWithdraw() == null)
            return ZERO;

        return getETPPartialWithdrawalWithdraw().subtract(
                getPost061983TaxedTaxOnWithdrawalPartial()).subtract(
                getPost061983UntaxedTaxOnWithdrawPartialWithdraw()).subtract(
                getTaxOnWithdrawalExcessPartialWithdraw());

    }

    /**
     * help method for partial withdraw
     */
    public BigDecimal getRestTotalPartialWithdraw() {
        return // (getUndeductedPartial() == null? ZERO :
                // getUndeductedPartial()).add(
        (getCGTExemptPartial() == null ? ZERO : getCGTExemptPartial()).add(
                getExcessPartial() == null ? ZERO : getExcessPartial()).add(
                getConcessionalPartial() == null ? ZERO
                        : getConcessionalPartial()).add(
                getInvalidityPartial() == null ? ZERO : getInvalidityPartial());
    }

    public BigDecimal getPre071983LeftAfterWithdrawPartialWithdraw() {
        // =MIN((K57-K62-K63-K64-K65)*H6,(K57-K62-K63-K64-K65)-D61-G61)
        BigDecimal rollover = getETPPartialWithdrawalRollover();
        if (rollover == null)
            return ZERO;
        BigDecimal restRollover = getRestTotalRolloverPartialWithdraw();

        return ((rollover.subtract(restRollover)).multiply(new BigDecimal(
                getPrePercentage()))).min(restRollover
                .subtract(getUndeductedAvail()));
    }

    public BigDecimal getRestTotalRolloverPartialWithdraw() {
        return getRestTotal().subtract(getRestTotalPartialWithdraw());
    }

    public BigDecimal getPost061983TaxedLeftAfterWithdrawPartialWithdraw() {
        BigDecimal postTaxed = getPost061983TaxedTotal();
        return postTaxed == null ? ZERO : postTaxed
                .subtract(getPost061983TaxedWithdrawPartialWithdraw());
    }

    /**
     * end of help method
     */

    /***************************************************************************
     * methods for Withdraw Up To Threshold
     **************************************************************************/

    public BigDecimal getPost061983TaxedWithdrawWithdrawUpToThreshold() {
        // =IF(Posttaxed<getThresholdLeft(),Posttaxed,getThresholdLeft())
        double taxedTotal = getPost061983TaxedTotal() == null ? 0
                : getPost061983TaxedTotal().doubleValue();
        double thresholdLeft = getThresholdLeft() == null ? 0
                : getThresholdLeft().doubleValue();
        return new BigDecimal(taxedTotal < thresholdLeft ? taxedTotal
                : thresholdLeft);
    }

    public BigDecimal getPost061983UntaxedWithdrawWithdrawUpToThreshold() {

        // =IF(PosttaxedWithdrawWithdrawUpToThreshold>112405-Post_ThresholdUsed,0,getThresholdLeft()-PosttaxedWithdrawWithdrawUpToThreshold)
        double untaxedTotal = getPost061983UntaxedTotal() == null ? 0
                : getPost061983UntaxedTotal().doubleValue();
        double thresholdLeftAfter = getThresholdLeftAfterPost061983TaxedDeducted()
                .doubleValue();
        return new BigDecimal(untaxedTotal <= thresholdLeftAfter ? untaxedTotal
                : thresholdLeftAfter);

    }

    public BigDecimal getPre071983WithdrawWithdrawUpToThreshold() {

        double postPercentage = getPostPercentage();
        if (postPercentage == 0)
            return ZERO;

        double postTaxed = getPost061983TaxedWithdrawWithdrawUpToThreshold()
                .doubleValue();
        double postUntaxed = getPost061983UntaxedWithdrawWithdrawUpToThreshold()
                .doubleValue();
        // =((PostTaxed/post%)-PostTaxed) + ((postUntaxed/post%)-PostUntaxed)
        return new BigDecimal((postTaxed / postPercentage - postTaxed)
                + (postUntaxed / postPercentage - postUntaxed));

    }

    public BigDecimal getPre071983TaxOnWithdrawalWithdrawUpToThreshold() {

        double pre = getPre071983WithdrawWithdrawUpToThreshold() == null ? 0
                : getPre071983WithdrawWithdrawUpToThreshold().doubleValue();
        double taxRate = getTaxRate() <= 0 ? 0 : getTaxRate();

        return new BigDecimal(pre * ETPConstants.ASSESABLE_INCOME_RATE
                * taxRate / 100);
    }

    public BigDecimal getPost061983TaxedTaxOnWithdrawalWithdrawUpToThreshold() {
        // =IF(Age<55,0,(VLOOKUP(PosttaxedOpt4,PostTableOpt4,2,TRUE))*(PosttaxedOpt4-(VLOOKUP(A32,PostTableOpt4,1,FALSE))))
        double postTaxed = getPost061983TaxedWithdrawWithdrawUpToThreshold()
                .doubleValue();
        double taxAmount = postTaxed
                - (getThresholdLeft().doubleValue() <= 0 ? 0
                        : getThresholdLeft().doubleValue());

        return getAge() < ETPConstants.TAX_EFFECTIVE_AGE ? ZERO
                : new BigDecimal(vLookUp(this, true, WITHDRAW_UP_TO_THRESHOLD,
                        postTaxed, true)
                        * (taxAmount <= 0 ? 0 : taxAmount));
        // vLookUp( this, true, WITHDRAW_UP_TO_THRESHOLD,
        // getThresholdLeft().doubleValue(), false )));
    }

    public BigDecimal getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold() {
        // =IF(Age<55,0,.165*UntaxedOpt4);
        return getAge() < 55 ? ZERO : new BigDecimal(
                (getPost061983UntaxedWithdrawWithdrawUpToThreshold())
                        .doubleValue()
                        * ETPConstants.THRESHOLD_TAX_RATE2);
    }

    public BigDecimal getPost061983UntaxedNetETPWithdrawUpToThreshold() {
        return getPost061983UntaxedWithdrawWithdrawUpToThreshold().subtract(
                getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold());
    }

    public BigDecimal getTotalTaxOnWithdrawalWithdrawUpToThreshold() {
        return getPre071983TaxOnWithdrawalWithdrawUpToThreshold().add(
                getPost061983TaxedTaxOnWithdrawalWithdrawUpToThreshold()).add(
                getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold());
    }

    public BigDecimal getTotalAmountWithdrawWithdrawUpToThreshold() {

        return getPost061983TaxedWithdrawWithdrawUpToThreshold().add(
                getPre071983WithdrawWithdrawUpToThreshold()).add(
                getPost061983UntaxedWithdrawWithdrawUpToThreshold());
    }

    public BigDecimal getTotalAmountNetETPWithdrawUpToThreshold() {
        return getTotalAmountWithdrawWithdrawUpToThreshold()
                .subtract(
                        getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold())
                .subtract(
                        getPost061983TaxedTaxOnWithdrawalWithdrawUpToThreshold());
    }

    public BigDecimal getTotalAmountRolloverWithdrawUpToThreshold() {
        if (getTotalETPAmount() == null)
            return ZERO;

        return getTotalETPAmount().subtract(
                getTotalAmountWithdrawWithdrawUpToThreshold()).subtract(
                getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold());
    }

    /**
     * help method to recalculate pre and post component after withdraw up to
     * post 06/1983 threshold
     */
    public BigDecimal getETPAmountLeftAfterWithdrawUpToThreshold() {

        if (getTotalETPAmount() == null)
            return ZERO;
        return (getTotalETPAmount())
                .subtract(getTotalAmountWithdrawWithdrawUpToThreshold());
    }

    public BigDecimal getPre071983LeftAfterWithdrawUpToThreshold() {
        double etpLeftAfter = getETPAmountLeftAfterWithdrawUpToThreshold()
                .doubleValue();
        double undeducted = getUndeductedTotal() == null ? 0
                : getUndeductedTotal().doubleValue();
        double restTotal = getRestTotal().doubleValue();

        return new BigDecimal((etpLeftAfter - restTotal) * getPrePercentage())
                .min(new BigDecimal(etpLeftAfter - restTotal - undeducted));

    }

    public BigDecimal getPost061983TaxedLeftAfterWithdrawUpToThreshold() {
        if (getPost061983TaxedTotal() == null)
            return ZERO;

        return getPost061983TaxedTotal().subtract(
                getPost061983TaxedWithdrawWithdrawUpToThreshold());
    }

    public BigDecimal getPost061983UntaxedLeftAfterWithdrawUpToThreshold() {
        if (getPost061983UntaxedTotal() == null)
            return ZERO;

        return getPost061983UntaxedTotal().subtract(
                getPost061983UntaxedWithdrawWithdrawUpToThreshold());
    }

    /*
     * end of help method
     */

    /**
     * calculate Rollover for withdrawUpToThreshold
     */
    public BigDecimal getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold() {

        return new BigDecimal(
                getPost061983UntaxedLeftAfterWithdrawUpToThreshold()
                        .doubleValue()
                        * ETPConstants.SUPER_TAX_RATE / 100);

    }

    public BigDecimal getPost061983TaxedRolloverWithdrawUpToThreshold() {
        // =IF(PostUntaxedLeft=0,PostTaxedLeft,ETPLeft-UntaxedTaxOnRollover-UndeductedTotal-RestTotal-preRollover)

        return getPost061983UntaxedLeftAfterWithdrawUpToThreshold()
                .equals(ZERO) ? getPost061983TaxedLeftAfterWithdrawUpToThreshold()
                : getETPAmountLeftAfterWithdrawUpToThreshold()
                        .subtract(
                                getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold())
                        .subtract(
                                getUndeductedTotal() == null ? ZERO
                                        : getUndeductedTotal()).subtract(
                                getRestTotal()).subtract(
                                getPre071983RolloverWithdrawUpToThreshold());

    }

    public BigDecimal getPre071983RolloverWithdrawUpToThreshold() {
        // =IF(PostUntaxedleft=0,preLeft,MIN((ETPLeft-UntaxedTaxOnRollover-getRestTotal())*pre%,
        // (ETPLeft-UntaxedTaxOnRollover-getRestTotal())-UndeductedTotal))

        return getPost061983UntaxedLeftAfterWithdrawUpToThreshold()
                .equals(ZERO) ? getPre071983LeftAfterWithdrawUpToThreshold()
                : (getETPAmountLeftAfterWithdrawUpToThreshold()
                        .subtract(
                                getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold())
                        .subtract(getRestTotal()))
                        .multiply(new BigDecimal(getPrePercentage()))
                        .min(
                                getETPAmountLeftAfterWithdrawUpToThreshold()
                                        .subtract(
                                                getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold())
                                        .subtract(getRestTotal())
                                        .subtract(
                                                getUndeductedTotal() == null ? ZERO
                                                        : getUndeductedTotal()));
    }

    /*
     * end of calculation for WithdrawUpToThreshold Rollover
     */

    /***************************************************************************
     * Validations
     * 
     * Get total amount to check whether it exceeds ETP Total Amount If yes,
     * ignore input
     **************************************************************************/

    public boolean validateETPComponentInput(BigDecimal d) {
        // =IF(Total_ETP_Amount-C20<0,"ERROR REDUCE AMOUNT","")
        if (getTotalETPAmount() == null)
            return false;
        return (getTotalETPAmount().doubleValue()
                - getTotalETPCalculationAmount().doubleValue() >= 0. && (d == null ? ZERO
                : d).doubleValue() >= 0.);
    }

    /*
     * public boolean validatePost061983ThresholdUsed( BigDecimal d ) { return
     * ETPConstants.TAX_FREE_THRESHOLD >= ( d == null? 0 : d.doubleValue() ); }
     */
    /**
     * validate partial withdraw input
     */
    public BigDecimal partialWithdrawTotalCalculationAmount(int id,
            BigDecimal value) {
        switch (id) {
        case ETPConstants.UNDEDUCTED_PARTIAL:
            return (value == null ? ZERO : value).add(
                    getCGTExemptPartial() == null ? ZERO
                            : getCGTExemptPartial()).add(
                    getExcessPartial() == null ? ZERO : getExcessPartial())
                    .add(
                            getConcessionalPartial() == null ? ZERO
                                    : getConcessionalPartial()).add(
                            getInvalidityPartial() == null ? ZERO
                                    : getInvalidityPartial());

        case ETPConstants.CGT_EXEMPT_PARTIAL:
            return (getUndeductedPartial() == null ? ZERO
                    : getUndeductedPartial())
                    .add(value == null ? ZERO : value)
                    .add(getExcessPartial() == null ? ZERO : getExcessPartial())
                    .add(
                            getConcessionalPartial() == null ? ZERO
                                    : getConcessionalPartial()).add(
                            getInvalidityPartial() == null ? ZERO
                                    : getInvalidityPartial());

        case ETPConstants.EXCESS_PARTIAL:
            return (getUndeductedPartial() == null ? ZERO
                    : getUndeductedPartial()).add(
                    getCGTExemptPartial() == null ? ZERO
                            : getCGTExemptPartial()).add(
                    value == null ? ZERO : value).add(
                    getConcessionalPartial() == null ? ZERO
                            : getConcessionalPartial()).add(
                    getInvalidityPartial() == null ? ZERO
                            : getInvalidityPartial());

        case ETPConstants.CONCESSIONAL_PARTIAL:
            return (getUndeductedPartial() == null ? ZERO
                    : getUndeductedPartial()).add(
                    getCGTExemptPartial() == null ? ZERO
                            : getCGTExemptPartial()).add(
                    getExcessPartial() == null ? ZERO : getExcessPartial())
                    .add(value == null ? ZERO : value).add(
                            getInvalidityPartial() == null ? ZERO
                                    : getInvalidityPartial());

        case ETPConstants.INVALIDITY_PARTIAL:
            return (getUndeductedPartial() == null ? ZERO
                    : getUndeductedPartial()).add(
                    getCGTExemptPartial() == null ? ZERO
                            : getCGTExemptPartial()).add(
                    getExcessPartial() == null ? ZERO : getExcessPartial())
                    .add(
                            getConcessionalPartial() == null ? ZERO
                                    : getConcessionalPartial()).add(
                            value == null ? ZERO : value);

        default:
            return ZERO;
        }

    }

    public boolean validatePartialWithdrawComponentInput(int id,
            BigDecimal value, BigDecimal partialWithrawAmount) {
        if (partialWithrawAmount == null)
            partialWithrawAmount = ZERO;

        switch (id) {
        case ETPConstants.UNDEDUCTED_PARTIAL:
            return partialWithdrawTotalCalculationAmount(id, value).compareTo(
                    partialWithrawAmount) <= 0
                    && (value == null ? ZERO : value)
                            .compareTo(getUndeductedTotal() == null ? ZERO
                                    : getUndeductedTotal()) <= 0;

        case ETPConstants.CGT_EXEMPT_PARTIAL:
            return partialWithdrawTotalCalculationAmount(id, value).compareTo(
                    partialWithrawAmount) <= 0
                    && (value == null ? ZERO : value)
                            .compareTo(getCGTExemptTotal() == null ? ZERO
                                    : getCGTExemptTotal()) <= 0;

        case ETPConstants.EXCESS_PARTIAL:
            return partialWithdrawTotalCalculationAmount(id, value).compareTo(
                    partialWithrawAmount) <= 0
                    && (value == null ? ZERO : value)
                            .compareTo(getExcessTotal() == null ? ZERO
                                    : getExcessTotal()) <= 0;

        case ETPConstants.CONCESSIONAL_PARTIAL:
            return partialWithdrawTotalCalculationAmount(id, value).compareTo(
                    partialWithrawAmount) <= 0
                    && (value == null ? ZERO : value)
                            .compareTo(getConcessionalTotal() == null ? ZERO
                                    : getConcessionalTotal()) <= 0;

        case ETPConstants.INVALIDITY_PARTIAL:
            return partialWithdrawTotalCalculationAmount(id, value).compareTo(
                    partialWithrawAmount) <= 0
                    && (value == null ? ZERO : value)
                            .compareTo(getInvalidityTotal() == null ? ZERO
                                    : getInvalidityTotal()) <= 0;

        default:
            return false;

        }

    }

    /***************************************************************************
     * help method for calculation
     **************************************************************************/

    public int getPre071983Days() {
        int pre071983Days = 0;
        // =IF(CalculationDate-EligibleServiceDate<1,0,C9-C6)
        if (getEligibleServiceDate() != null) {// &&
                                                // ETPConstants.TAX_1983_COMMENCE_DATE
                                                // != null ) {
            pre071983Days = (int) Math
                    .ceil(((ETPConstants.TAX_1983_COMMENCE_DATE.getTime() - getEligibleServiceDate()
                            .getTime()) / MILLIS_PER_DAY));
            if (pre071983Days < 1)
                pre071983Days = 0;
        }

        return pre071983Days;
    }

    /**
     * The following calc is correct when 40,000ETP, calcDate is 06/12/2002 and
     * ESD is 10/02/1969 5254 predays /12353 totaldays = 17,012.87 pre compo
     */

    public int getPost061983Days() {

        // =MIN(CalculationDate-30/06/1983,
        // CalculationDate-EligibleServiceDate+1)
        int post061983Days = 0;

        if (getCalculationDate() != null && getEligibleServiceDate() != null) {
            long calcDate = getCalculationDate().getTime();
            double post061983Days1 = (calcDate
                    - getEligibleServiceDate().getTime() + MILLIS_PER_DAY)
                    / (double) MILLIS_PER_DAY;
            double post061983Days2 = (calcDate - ETPConstants.TAX_1983_COMMENCE_DATE
                    .getTime())
                    / (double) MILLIS_PER_DAY + 1;
            post061983Days = (int) Math.ceil(Math.min(post061983Days1,
                    post061983Days2));

            if (post061983Days < 0)
                post061983Days = 0;
        }

        return post061983Days;
    }

    public int getTotalDays() {
        return getPre071983Days() + getPost061983Days();
    }

    public BigDecimal getRestTotal() {

        double excess = getExcessTotal() == null ? 0 : getExcessTotal()
                .doubleValue();
        double concessional = getConcessionalTotal() == null ? 0
                : getConcessionalTotal().doubleValue();
        double invalidity = getInvalidityTotal() == null ? 0
                : getInvalidityTotal().doubleValue();
        double cgtExempt = getCGTExemptTotal() == null ? 0
                : getCGTExemptTotal().doubleValue();

        return new BigDecimal(excess + concessional + invalidity + cgtExempt);
    }

    public BigDecimal getPrePostTotal() {
        return getTotalETPAmount() == null ? ZERO : getTotalETPAmount()
                .subtract(getRestTotal());
    }

    public BigDecimal getPreTotal() {
        return new BigDecimal(getPrePostTotal().doubleValue()
                * getPrePercentage());
    }

    public BigDecimal getPostTotal() {
        return getPrePostTotal().subtract(getPreTotal());
    }

    public BigDecimal getTotalETPCalculationAmount() {

        return (getPost061983UntaxedTotal() == null ? ZERO
                : getPost061983UntaxedTotal()).add(
                getUndeductedTotal() == null ? ZERO : getUndeductedTotal())
                .add(getCGTExemptTotal() == null ? ZERO : getCGTExemptTotal())
                .add(getExcessTotal() == null ? ZERO : getExcessTotal()).add(
                        getConcessionalTotal() == null ? ZERO
                                : getConcessionalTotal()).add(
                        getInvalidityTotal() == null ? ZERO
                                : getInvalidityTotal()).add(
                        getPre071983Total() == null ? ZERO
                                : getPre071983Total()).add(
                        getPost061983TaxedTotal() == null ? ZERO
                                : getPost061983TaxedTotal());
    }

    public BigDecimal getThresholdLeft() {
        return new BigDecimal(ETPConstants.TAX_FREE_THRESHOLD
                - (getPost061983ThresholdUsed() == null ? 0
                        : getPost061983ThresholdUsed().doubleValue()));
    }

    public int getAge() {

        if (isClient == null || isClient.booleanValue()) {
            if (getDateOfBirth() == null)
                return (int) UNKNOWN_VALUE;
            return getAge(getDateOfBirth());
        } else {
            if (getPartnerDOB() == null)
                return (int) UNKNOWN_VALUE;
            return getAge(getPartnerDOB());
        }
    }

    public double getPrePercentage() {
        if (getPre071983Days() == 0
                || (getPre071983Days() + getPost061983Days()) == 0)
            return 0;

        return (double) getPre071983Days()
                / (getPre071983Days() + getPost061983Days());

    }

    public double getPostPercentage() {
        if (getPost061983Days() == 0
                || (getPre071983Days() + getPost061983Days()) == 0)
            return 0;

        return 1 - getPrePercentage();
    }

    public int getOption() {
        return option;
    }

    public void setOption(int value) {
        if (option == value)
            return;

        option = value;
        setModified();
    }

    /***************************************************************************
     * methods for input verifier
     **************************************************************************/
    public boolean isValidETPTotalInputVerifier(double value) {

        if (value >= getTotalETPCalculationAmount().doubleValue())
            return true;

        String msg = "Please adjust ETPComponents amount and partial withdraw amount \n"
                + "according to Total ETP Amount you selected!";
        javax.swing.JOptionPane.showMessageDialog(null, msg, "Error!",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;

    }

    /***************************************************************************
     * for allocatedPension components
     **************************************************************************/
    public BigDecimal getPreForWithdrawUpToThresholdAP() {
        BigDecimal total = calculateETPTotalForWithdrawUpToThresholdAP();
        BigDecimal cgt = getCGTExemptTotal();
        BigDecimal excess = getExcessTotal();
        BigDecimal concessional = getConcessionalTotal();
        BigDecimal invalidity = getInvalidityTotal();
        BigDecimal undeducted = getUndeductedForWithdrawUpToThresholdAP();
        double prePercentage = getPrePercentage();
        return new BigDecimal(calculatePre((total == null ? ZERO : total)
                .doubleValue(), (cgt == null ? ZERO : cgt).doubleValue(),
                (excess == null ? ZERO : excess).doubleValue(),
                (concessional == null ? ZERO : concessional).doubleValue(),
                (invalidity == null ? ZERO : invalidity).doubleValue(),
                (undeducted == null ? ZERO : undeducted).doubleValue(),
                prePercentage));
    }

    public BigDecimal getPostTaxedForWithdrawUpToThresholdAP() {
        BigDecimal total = calculateETPTotalForWithdrawUpToThresholdAP();
        BigDecimal pre = getPreForWithdrawUpToThresholdAP();
        BigDecimal undeducted = getUndeductedForWithdrawUpToThresholdAP();
        BigDecimal cgtExempt = getCGTExemptTotal();
        BigDecimal excess = getExcessTotal();
        BigDecimal concessional = getConcessionalTotal();
        BigDecimal invalidity = getInvalidityTotal();

        return new BigDecimal(calculatePostTaxed((total == null ? ZERO : total)
                .doubleValue(), (pre == null ? ZERO : pre).doubleValue(),
                (undeducted == null ? ZERO : undeducted).doubleValue(),
                (cgtExempt == null ? ZERO : cgtExempt).doubleValue(),
                (excess == null ? ZERO : excess).doubleValue(),
                (concessional == null ? ZERO : concessional).doubleValue(),
                (invalidity == null ? ZERO : invalidity).doubleValue()));
    }

    public double calculatePre(double total, double cgt, double excess,
            double concessional, double invalidity, double undeducted,
            double prePercentage) {
        // =MIN((D49-D45-D46-D47-D48)*'engine
        // Room'!H6,(D49-D45-D46-D47-D48)-D44)

        double min = Math.min(
                (total - cgt - excess - concessional - invalidity)
                        * prePercentage, (total - cgt - excess - concessional
                        - invalidity - undeducted));

        return min < 0 ? 0 : min;
    }

    public double calculatePostTaxed(double total, double pre,
            double undeducted, double cgtExempt, double excess,
            double concessional, double invalidity) {
        // =D49-D41-D44-D45-D46-D47-D48
        double post = total - pre - undeducted - cgtExempt - excess
                - concessional - invalidity;
        return post < 0 ? 0 : post;
    }

    public BigDecimal calculateETPTotalForWithdrawUpToThresholdAP() {
        BigDecimal withdrawNet = getTotalAmountNetETPWithdrawUpToThreshold();
        BigDecimal rolloverNet = getTotalAmountRolloverWithdrawUpToThreshold();
        return (withdrawNet == null ? ZERO : withdrawNet)
                .add((rolloverNet == null ? ZERO : rolloverNet));

    }

    public BigDecimal getUndeductedForWithdrawUpToThresholdAP() {
        // ='engine Room'!I77+'engine Room'!E72
        BigDecimal withdrawETPNet = getTotalAmountNetETPWithdrawUpToThreshold();

        return (withdrawETPNet == null ? ZERO : withdrawETPNet)
                .add(getUndeductedTotal() == null ? ZERO : getUndeductedTotal());
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
    public Integer getRequiredFrequencyCodeID() {
        return FrequencyCode.ONLY_ONCE;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        try {
            financials.addAll(getGeneratedIncomes(desc));
            financials.addAll(getGeneratedExpenses(desc));
            // financials.addAll( getGeneratedLiabilities(desc) );
            return financials;
        } catch (Exception e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            return null;
        }

    }

    private RegularIncome newIncome(String desc, java.math.BigDecimal amount,
            boolean taxable)
            throws com.argus.financials.bean.NegativeAmountException,
            com.argus.financials.code.InvalidCodeException {
        if (amount == null || amount.doubleValue() == 0.)
            return null;

        RegularIncome income = new RegularIncome();
        income.setFinancialDesc("Income from " + desc);
        income.setRegularAmount(amount);
        income.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
        income.setTaxable(taxable);
        income.setStartDate(getCalculationDate());
        income.setEndDate(income.getStartDate());
        income.setOwnerCodeID(OwnerCode.CLIENT);
        income.setFinancialTypeID(FinancialTypeID.INCOME_RETIREMENT);
        income.setRegularTaxType(ITaxConstants.I_OTHER_PENSIONS);

        return income;
    }

    private java.util.Collection getGeneratedIncomes(String desc)
            throws com.argus.financials.bean.NegativeAmountException,
            com.argus.financials.code.InvalidCodeException {
        java.util.Collection incomes = new java.util.ArrayList();
        if (getOption() == ROLLOVER_ALL) {

        } else if (getOption() == WITHDRAW_ALL) {
            // jTextFieldPreJuly1983Withdraw
            incomes.add(newIncome(desc, getPre071983Total(), true));

            // jTextFieldPostJune1983TaxedWithdraw
            incomes.add(newIncome(desc, getPost061983TaxedTotal(), true));

            // jTextFieldPostJune1983UntaxedWithdraw
            incomes.add(newIncome(desc, getPost061983UntaxedTotal(), true));

            // jTextFieldUndeductedWithdraw
            incomes.add(newIncome(desc, getUndeductedTotal(), false));

            // jTextFieldCGTExemptWithdraw
            incomes.add(newIncome(desc, getCGTExemptTotal(), false));

            // jTextFieldExcessWithdraw
            incomes.add(newIncome(desc, getExcessTotal(), true));

            // jTextFieldConcessionalWithdraw
            incomes.add(newIncome(desc, getConcessionalTotal(), false));

            // jTextFieldInvalidityWithdraw
            incomes.add(newIncome(desc, getInvalidityTotal(), false));

        } else if (getOption() == PARTIAL_WITHDRAW
                && (getETPPartialWithdrawalRollover() != null || !((getETPPartialWithdrawalWithdraw() == null ? 0
                        : getETPPartialWithdrawalWithdraw().doubleValue()) == 0))) {
            // jTextFieldPreJuly1983Withdraw
            incomes.add(newIncome(desc, getPre071983WithdrawPartialWithdraw(),
                    true));

            // jTextFieldPostJune1983TaxedWithdraw
            incomes.add(newIncome(desc,
                    getPost061983TaxedWithdrawPartialWithdraw(), true));

            // jTextFieldPostJune1983UntaxedWithdraw
            incomes.add(newIncome(desc,
                    getPost061983UntaxedWithdrawPartialWithdraw(), true));

            // jTextFieldUndeductedWithdraw
            incomes.add(newIncome(desc, getUndeductedPartial(), false));

            // jTextFieldCGTExemptWithdraw
            incomes.add(newIncome(desc, getCGTExemptPartial(), false));

            // jTextFieldExcessWithdraw
            incomes.add(newIncome(desc, getExcessPartial(), true));

            // jTextFieldConcessionalWithdraw
            incomes.add(newIncome(desc, getConcessionalPartial(), false));

            // jTextFieldInvalidityWithdraw
            incomes.add(newIncome(desc, getInvalidityPartial(), false));

        } else if (getOption() == WITHDRAW_UP_TO_THRESHOLD) {
            // jTextFieldPreJuly1983Withdraw
            incomes.add(newIncome(desc,
                    getPre071983WithdrawWithdrawUpToThreshold(), true));

            // jTextFieldPostJune1983TaxedWithdraw
            incomes.add(newIncome(desc,
                    getPost061983TaxedWithdrawWithdrawUpToThreshold(), true));

            // jTextFieldPostJune1983UntaxedWithdraw
            incomes.add(newIncome(desc,
                    getPost061983UntaxedWithdrawWithdrawUpToThreshold(), true));

            // jTextFieldUndeductedWithdraw
            incomes.add(newIncome(desc, getUndeductedTotal(), false));

            // jTextFieldCGTExemptWithdraw
            incomes.add(newIncome(desc, getCGTExemptTotal(), false));

            // jTextFieldExcessWithdraw
            incomes.add(newIncome(desc, getExcessTotal(), true));

            // jTextFieldConcessionalWithdraw
            incomes.add(newIncome(desc, getConcessionalTotal(), false));

            // jTextFieldInvalidityWithdraw
            incomes.add(newIncome(desc, getInvalidityTotal(), false));

        }
        return incomes;
    }

    private RegularExpense newExpense(String desc, java.math.BigDecimal amount,
            boolean taxable)
            throws com.argus.financials.bean.NegativeAmountException,
            com.argus.financials.code.InvalidCodeException {
        if (amount == null || amount.doubleValue() == 0.)
            return null;

        RegularExpense expense = new RegularExpense();
        expense.setFinancialDesc("Expense from " + desc);
        expense.setRegularAmount(amount);
        expense.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
        expense.setTaxable(taxable);
        expense.setStartDate(getCalculationDate());
        expense.setEndDate(DateTimeUtils.getEndOfFinancialYearDate(expense
                .getStartDate()));
        expense.setOwnerCodeID(OwnerCode.CLIENT);
        expense.setFinancialTypeID(FinancialTypeID.EXPENSE_OTHER);
        expense.setRegularTaxType(ITaxConstants.D_GENERAL);

        return expense;
    }

    private java.util.Collection getGeneratedExpenses(String desc)
            throws com.argus.financials.bean.NegativeAmountException,
            com.argus.financials.code.InvalidCodeException {
        java.util.Collection expenses = new java.util.ArrayList();

        java.util.Collection incomes = new java.util.ArrayList();
        if (getOption() == ROLLOVER_ALL) {

        } else if (getOption() == WITHDRAW_ALL) {
            // jTextFieldPreJuly1983TaxOnWithdrawal
            expenses
                    .add(newExpense(desc, getPre071983TaxOnWithdrawal(), false));

            // jTextFieldPostJune1983TaxedTaxOnWithdrawal
            expenses.add(newExpense(desc, getPost061983TaxedTaxOnWithdrawal(),
                    false));

            // jTextFieldPostJune1983UntaxedTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getPost061983UntaxedTaxOnWithdrawal(), false));

            // jTextFieldExcessTaxOnWithdrawal
            expenses.add(newExpense(desc, getTaxOnWithdrawalExcess(), false));

            // jTextFieldConcessionalTaxOnWithdrawal
            expenses.add(newExpense(desc, getConcessionalTaxOnWithdrawal(),
                    false));
        } else if (getOption() == PARTIAL_WITHDRAW) {
            // jTextFieldPreJuly1983TaxOnWithdrawal
            expenses.add(newExpense(desc, getPre071983TaxOnWithdrawalPartial(),
                    false));

            // jTextFieldPostJune1983TaxedTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getPost061983TaxedTaxOnWithdrawalPartial(), false));

            // jTextFieldPostJune1983UntaxedTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getPost061983UntaxedTaxOnWithdrawPartialWithdraw(), false));

            // jTextFieldExcessTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getTaxOnWithdrawalExcessPartialWithdraw(), false));

            // jTextFieldConcessionalTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getConcessionalTaxOnWithdrawalPartialWithdraw(), false));
        } else if (getOption() == WITHDRAW_UP_TO_THRESHOLD) {
            // jTextFieldPreJuly1983TaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getPre071983TaxOnWithdrawalWithdrawUpToThreshold(), false));

            // jTextFieldPostJune1983TaxedTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getPost061983TaxedTaxOnWithdrawalWithdrawUpToThreshold(),
                    false));

            // jTextFieldPostJune1983UntaxedTaxOnWithdrawal
            expenses.add(newExpense(desc,
                    getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold(),
                    false));

            // jTextFieldExcessTaxOnWithdrawal

            // jTextFieldConcessionalTaxOnWithdrawal

        }
        return expenses;
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields)
            throws Exception {
        initializeReportData(reportFields,
                com.argus.financials.service.ServiceLocator.getInstance()
                        .getClientPerson());

    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields,
            com.argus.financials.service.PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        if (person == null) {
            reportFields.setValue(reportFields.ETP_IsClient, "None");
        } else if (this.getIsClient().equals(Boolean.TRUE)) {
            reportFields.setValue(reportFields.ETP_IsClient, "Client");
        } else {
            reportFields.setValue(reportFields.ETP_IsClient, "Partner");
        }

        getETPDetails(reportFields);

        if (getOption() == WITHDRAW_ALL)
            getWithdrawAll(reportFields);
        else if (getOption() == PARTIAL_WITHDRAW)
            getPartialWithdraw(reportFields);
        else if (getOption() == ROLLOVER_ALL)
            getRolloverAll(reportFields);
        else if (getOption() == WITHDRAW_UP_TO_THRESHOLD)
            getWithdrawUpThreshold(reportFields);
    }

    /*
     * private java.util.Collection getGeneratedLiabilities( String desc ) {
     * java.util.Collection liabilities = new java.util.ArrayList();
     * 
     * return liabilities; }
     */

    // Get basic ETP details for all methods of calculation
    private void getETPDetails(
            com.argus.financials.report.ReportFields reportFields) {

        // Termination payment & Rollover details

        UpdateableTableModel utm = new UpdateableTableModel(new String[] { "",
                "", "", "" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("Eligible Service Period ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Commenced ");
        values.add(DateTimeUtils.asString(getEligibleServiceDate()));
        values.add("Pre Days");
        values.add(new Integer(getPre071983Days()).toString());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Ended ");
        values.add(DateTimeUtils.asString(getCalculationDate()));
        values.add("Post Days");
        values.add(new Integer(getPost061983Days()).toString());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Marginal Personal Tax Rate ");
        values.add(Percent.getPercentInstance().toString(getTaxRate()));
        values.add("Total Days");
        values.add(new Integer(getTotalDays()).toString());
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post June 1983 Threshold ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Used ");
        values.add(currency.toString(getPost061983ThresholdUsed()));
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Unused ");
        values.add(currency.toString(getThresholdLeft()));
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(reportFields.ETP_Details, utm);

    }

    private void getWithdrawAll(
            com.argus.financials.report.ReportFields reportFields) {

        // Termination payment & Rollover details

        UpdateableTableModel utm = new UpdateableTableModel(new String[] { "",
                "Total ETP Benefit($)", "Selected Rollover ($)",
                "Retained Benefit ($)" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("Pre 01/07/1983 ");
        values.add(currency.toString(getPre071983Total()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Taxed ");
        values.add(currency.toString(getPost061983TaxedTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Untaxed ");
        values.add(currency.toString(getPost061983UntaxedTotal()));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Undeducted");
        values.add(currency.toString(getUndeductedTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt");
        values.add(currency.toString(getCGTExemptTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess");
        values.add(currency.toString(getExcessTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Concessional");
        values.add(currency.toString(getConcessionalTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity");
        values.add(currency.toString(getInvalidityTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add(currency.toString(getTotalETPCalculationAmount()));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        reportFields.setValue(reportFields.ETP_Payment_Rollover, utm);

        // Taxation details
        utm = new UpdateableTableModel(new String[] { "", "Amount Received($)",
                "Tax ($)", "Net- ETP ($)" });

        // Taxable at Marginal Rates
        values.add("A. Taxable at Marginal Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Concessional Component ");
        values.add(currency.toString(getConcessionalTotal()));
        values.add(currency.toString(getConcessionalTaxOnWithdrawal()));
        values.add(currency.toString(getConcessionalTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Pre July 1983 Component ");
        values.add(currency.toString(getPre071983Total()));
        values.add(currency.toString(getPre071983TaxOnWithdrawal()));
        values.add(currency.toString(getPre071983Total()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess Component ");
        values.add(currency.toString(getExcessTotal()));
        values.add(currency.toString(getTaxOnWithdrawalExcess()));
        values.add(currency.toString(getExcessNetETP()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("B. Taxable at Fixed Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Post 06/1983 Taxed ");
        values.add(currency.toString(getPost061983TaxedTotal()));
        values.add(currency.toString(getPost061983TaxedTaxOnWithdrawal()));
        values.add(currency.toString(getPost061983TaxedNetETP()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 06/1983 Untaxed ");
        values.add(currency.toString(getPost061983UntaxedTotal()));
        values.add(currency.toString(getPost061983UntaxedTaxOnWithdrawal()));
        values.add(currency.toString(getPost061983UntaxedNetETP()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("C. Components Not Taxable");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Undeducted Contribution ");
        values.add(currency.toString(getUndeductedTotal()));
        values.add("");
        values.add(currency.toString(getUndeductedTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity Component ");
        values.add(currency.toString(getInvalidityTotal()));
        values.add("");
        values.add(currency.toString(getInvalidityTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt Component ");
        values.add(currency.toString(getCGTExemptTotal()));
        values.add("");
        values.add(currency.toString(getCGTExemptTotal()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("D. Totals");
        values.add(currency.toString(getTotalETPAmount()));
        values.add("");
        values.add(currency.toString(getTotalAmountNetETP()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Tax on Post 06/1983 Untaxed Rollover ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(reportFields.ETP_Taxation, utm);

    }

    // Get partial withdraw
    private void getPartialWithdraw(
            com.argus.financials.report.ReportFields reportFields) {

        UpdateableTableModel utm = new UpdateableTableModel(new String[] { "",
                "Total ETP Benefit($)", "Selected Rollover ($)",
                "Retained Benefit ($)" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("Pre 01/07/1983 ");
        values.add(currency.toString(getPre071983Total()));
        values.add(currency.toString(getPre071983PartialWithdrawRollover()));
        values.add(currency
                .toString(this.getPre071983WithdrawPartialWithdraw()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Taxed ");
        values.add(currency.toString(getPost061983TaxedTotal()));
        values.add(currency
                .toString(getPost061983TaxedPartialWithdrawRollover()));
        values.add(currency
                .toString(getPost061983TaxedWithdrawPartialWithdraw()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Untaxed ");
        values.add(currency.toString(getPost061983UntaxedTotal()));
        values.add("");
        values.add(currency
                .toString(getPost061983UntaxedWithdrawPartialWithdraw()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Undeducted");
        values.add(currency.toString(getUndeductedTotal()));
        values.add(currency.toString(getUndeductedAvail()));
        values.add(currency.toString(getUndeductedPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt");
        values.add(currency.toString(getCGTExemptTotal()));
        values.add(currency.toString(getCGTExemptAvail()));
        values.add(currency.toString(getCGTExemptPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess");
        values.add(currency.toString(getExcessTotal()));
        values.add(currency.toString(getExcessAvail()));
        values.add(currency.toString(getExcessPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Concessional");
        values.add(currency.toString(getConcessionalTotal()));
        values.add(currency.toString(getConcessionalAvail()));
        values.add(currency.toString(getConcessionalPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity");
        values.add(currency.toString(getInvalidityTotal()));
        values.add(currency.toString(getInvalidityAvail()));
        values.add(currency.toString(getInvalidityPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add(currency.toString(getTotalETPCalculationAmount()));
        values.add(currency.toString(getETPPartialWithdrawalRolloverAvail()));
        values.add(currency.toString(getETPPartialWithdrawalWithdraw()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        reportFields.setValue(reportFields.ETP_Payment_Rollover, utm);

        utm = new UpdateableTableModel(new String[] { "", "Amount Received($)",
                "Tax ($)", "Net- ETP ($)" });

        // Taxable at Marginal Rates
        values.add("A. Taxable at Marginal Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Concessional Component ");
        values.add(currency.toString(getConcessionalPartial()));
        values.add(currency
                .toString(getConcessionalTaxOnWithdrawalPartialWithdraw()));
        values.add(currency.toString(getConcessionalPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Pre July 1983 Component ");
        values.add(currency.toString(getPre071983WithdrawPartialWithdraw()));
        values.add(currency.toString(getPre071983TaxOnWithdrawalPartial()));
        values.add(currency.toString(getPre071983WithdrawPartialWithdraw()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess Component ");
        values.add(currency.toString(getExcessPartial()));
        values
                .add(currency
                        .toString(getTaxOnWithdrawalExcessPartialWithdraw()));
        values.add(currency.toString(getExcessPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("B. Taxable at Fixed Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Post 06/1983 Taxed ");
        values.add(currency
                .toString(getPost061983TaxedWithdrawPartialWithdraw()));
        values.add(currency
                .toString(getPost061983TaxedTaxOnWithdrawalPartial()));
        values.add(currency.toString(getPost061983TaxedNetETPPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 06/1983 Untaxed ");
        values.add(currency
                .toString(getPost061983UntaxedWithdrawPartialWithdraw()));
        values.add(currency
                .toString(getPost061983UntaxedTaxOnWithdrawPartialWithdraw()));
        values.add(currency
                .toString(getPost061983UntaxedNetETPPartialWithdraw()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("C. Components Not Taxable");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Undeducted Contribution ");
        values.add(currency.toString(getUndeductedPartial()));
        values.add("");
        values.add(currency.toString(getUndeductedPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity Component ");
        values.add(currency.toString(getInvalidityPartial()));
        values.add("");
        values.add(currency.toString(getInvalidityPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt Component ");
        values.add(currency.toString(getCGTExemptPartial()));
        values.add("");
        values.add(currency.toString(getCGTExemptPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("D. Totals");
        values.add(currency.toString(getETPPartialWithdrawalWithdraw()));
        values.add("");
        values.add(currency.toString(getTotalAmountNetETPPartial()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Tax on Post 06/1983 Untaxed Rollover ");
        values.add("");
        values.add(currency
                .toString(getPost061983UntaxedTaxOnRolloverPartialWithdraw()));
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(reportFields.ETP_Taxation, utm);

    }

    // Rollover all

    private void getRolloverAll(
            com.argus.financials.report.ReportFields reportFields) {

        UpdateableTableModel utm = new UpdateableTableModel(new String[] { "",
                "Total ETP Benefit($)", "Selected Rollover ($)",
                "Retained Benefit ($)" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("Pre 01/07/1983 ");
        values.add(currency.toString(getPre071983Total()));
        values.add(currency.toString(this.getPre071983RolloverAll()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Taxed ");
        values.add(currency.toString(getPost061983TaxedTotal()));
        values.add(currency.toString(this.getPost061983TaxedRolloverAll()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Untaxed ");
        values.add(currency.toString(getPost061983UntaxedTotal()));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Undeducted");
        values.add(currency.toString(getUndeductedTotal()));
        values.add(currency.toString(this.getUndeductedAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt");
        values.add(currency.toString(getCGTExemptTotal()));
        values.add(currency.toString(getCGTExemptAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess");
        values.add(currency.toString(getExcessTotal()));
        values.add(currency.toString(getExcessAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Concessional");
        values.add(currency.toString(getConcessionalTotal()));
        values.add(currency.toString(getConcessionalAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity");
        values.add(currency.toString(getInvalidityTotal()));
        values.add(currency.toString(getInvalidityAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add(currency.toString(getTotalETPCalculationAmount()));
        values.add(currency.toString(this.getTotalAmountRolloverAll()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        reportFields.setValue(reportFields.ETP_Payment_Rollover, utm);

        utm = new UpdateableTableModel(new String[] { "", "Amount Received($)",
                "Tax ($)", "Net- ETP ($)" });

        // Taxable at Marginal Rates
        values.add("A. Taxable at Marginal Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Concessional Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Pre July 1983 Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("B. Taxable at Fixed Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Post 06/1983 Taxed ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 06/1983 Untaxed ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("C. Components Not Taxable");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Undeducted Contribution ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("D. Totals");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Tax on Post 06/1983 Untaxed Rollover ");
        values.add("");
        values.add(currency.toString(this
                .getPost061983UntaxedTaxOnRolloverPartialWithdraw()));
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(reportFields.ETP_Taxation, utm);

    }

    // Withdraw up to threshold

    private void getWithdrawUpThreshold(
            com.argus.financials.report.ReportFields reportFields) {

        UpdateableTableModel utm = new UpdateableTableModel(new String[] { "",
                "Total ETP Benefit($)", "Selected Rollover ($)",
                "Retained Benefit ($)" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("Pre 01/07/1983 ");
        values.add(currency.toString(this.getPre071983Total()));
        values.add(currency.toString(this
                .getPre071983RolloverWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getPre071983WithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Taxed ");
        values.add(currency.toString(this.getPost061983TaxedTotal()));
        values.add(currency.toString(this
                .getPost061983TaxedRolloverWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getPost061983TaxedWithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 30/06/1983 - Untaxed ");
        values.add(currency.toString(this.getPost061983UntaxedTotal()));
        values.add("");
        values.add(currency.toString(this
                .getPost061983UntaxedWithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Undeducted");
        values.add(currency.toString(this.getUndeductedTotal()));
        values.add(currency.toString(this.getUndeductedTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt");
        values.add(currency.toString(getCGTExemptTotal()));
        values.add(currency.toString(getCGTExemptTotal()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess");
        values.add(currency.toString(getExcessTotal()));
        values.add(currency.toString(getExcessAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Concessional");
        values.add(currency.toString(getConcessionalTotal()));
        values.add(currency.toString(getConcessionalAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity");
        values.add(currency.toString(getInvalidityTotal()));
        values.add(currency.toString(getInvalidityAvail()));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add(currency.toString(getTotalETPCalculationAmount()));
        values.add(currency.toString(this
                .getTotalAmountRolloverWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getTotalAmountWithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        reportFields.setValue(reportFields.ETP_Payment_Rollover, utm);

        utm = new UpdateableTableModel(new String[] { "", "Amount Received($)",
                "Tax ($)", "Net- ETP ($)" });

        // Taxable at Marginal Rates
        values.add("A. Taxable at Marginal Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Concessional Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Pre July 1983 Component ");
        values.add(currency
                .toString(getPre071983WithdrawWithdrawUpToThreshold()));
        values.add(currency
                .toString(getPre071983TaxOnWithdrawalWithdrawUpToThreshold()));
        values.add(currency
                .toString(getPre071983WithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Excess Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("B. Taxable at Fixed Rates ");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Post 06/1983 Taxed ");
        values.add(currency.toString(this
                .getPost061983TaxedWithdrawWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getPost061983TaxedTaxOnWithdrawalWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getPost061983TaxedWithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Post 06/1983 Untaxed ");
        values.add(currency.toString(this
                .getPost061983UntaxedWithdrawWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getPost061983UntaxedTaxOnWithdrawalWithdrawUpToThreshold()));
        values.add(currency.toString(this
                .getPost061983UntaxedWithdrawWithdrawUpToThreshold()));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("C. Components Not Taxable");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Undeducted Contribution ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Invalidity Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("CGT Exempt Component ");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("D. Totals");
        values.add(currency.toString(DECIMAL_ZERO));
        values.add("");
        values.add(currency.toString(DECIMAL_ZERO));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Tax on Post 06/1983 Untaxed Rollover ");
        values.add("");
        values.add(currency.toString(this
                .getPost061983UntaxedTaxOnRolloverWithdrawUpToThreshold()));
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(reportFields.ETP_Taxation, utm);

    }

}