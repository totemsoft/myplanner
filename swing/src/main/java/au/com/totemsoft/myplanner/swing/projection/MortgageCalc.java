/*
 * MortgageCalc.java
 *
 * Created on 18 December 2001, 10:40
 */

package au.com.totemsoft.myplanner.swing.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.2
 */

import java.util.Vector;

import javax.swing.JOptionPane;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.io.JPEGFileFilter;
import au.com.totemsoft.myplanner.api.code.FinancialTypeEnum;
import au.com.totemsoft.myplanner.bean.RegularExpense;
import au.com.totemsoft.myplanner.chart.IGraphView;
import au.com.totemsoft.myplanner.code.FrequencyCode;
import au.com.totemsoft.myplanner.code.ModelType;
import au.com.totemsoft.myplanner.code.OwnerCode;
import au.com.totemsoft.myplanner.projection.IMortgageCalcParams;
import au.com.totemsoft.myplanner.projection.IMortgageCalcResults;
import au.com.totemsoft.myplanner.projection.MoneyCalc;
import au.com.totemsoft.myplanner.projection.data.MortgageScheduleData;
import au.com.totemsoft.myplanner.projection.data.MortgageScheduleTableModel;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.report.Reportable;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.table.swing.UpdateableTableModel;
import au.com.totemsoft.myplanner.table.swing.UpdateableTableRow;
import au.com.totemsoft.myplanner.tax.au.ITaxConstants;
import au.com.totemsoft.util.DateTimeUtils;
import au.com.totemsoft.util.RateUtils;

public class MortgageCalc extends MoneyCalc implements IMortgageCalcParams,
        IMortgageCalcResults, Reportable {

    // private double loanAmount; // = initialValue
    private Integer paymentFrequencyCodeID;

    private MortgageView view = null;

    private boolean readyToSave = false;

    private boolean hasValidData = false;

    private boolean isPlanWriter = false;

    // inputs
    private double purchasePrice = 0.0;

    private double depositAmount = 0.0;

    private double amountBorrowed = 0.0;

    private double loanTermYears = 0.0;

    // variables for CashFlow
    private Vector interests = new Vector();

    private double annualInterestRate = 0.;

    private boolean additioinalPayment = false;

    private double additionalMonthlyPayment = 0.0;

    // outputs
    // Standard Repayment
    private double stdMonthlyPayment = 0.0;

    private double stdTotalInterestPaid = 0.0;

    private double stdTotalCapitalPaid = 0.0;

    private double stdTotalAmountRepaid = 0.0;

    private double stdYearsToRepay = 0.0;

    private double stdTotalNoPayments = 0.0;

    // Additional Repayment
    private double addtnlMonthlyPayment = 0.0;

    private double addtnlTotalInterestPaid = 0.0;

    private double addtnlTotalCapitalPaid = 0.0;

    private double addtnlTotalAmountRepaid = 0.0;

    private double addtnlYearsToRepay = 0.0;

    private double addtnlTotalNoPayments = 0.0;

    private double addtnlInterestSaved = 0.0;

    // private double addtnlRevisedLoanTerm = 0.0;
    private double addtnlYearsReduced = 0.0;

    // Payment schedules
    private java.util.Vector stdMnthlySchedule = new java.util.Vector();

    private MortgageScheduleTableModel stdScheduleModel = new MortgageScheduleTableModel(
            this.stdMnthlySchedule);

    private java.util.Vector addtnlMnthlySchedule = new java.util.Vector();

    private MortgageScheduleTableModel addtnlScheduleModel = new MortgageScheduleTableModel(
            this.addtnlMnthlySchedule);

    private Vector stdAnnualSchedule = new Vector();

    private Vector addtnlAnnualSchedule = new Vector();

    /**
     * This method part of the methods used in restoring saved data/model. It is
     * called by the ancestor method - setModel to update fields in the
     * assoicated view. The property value used to look up a compontent within a
     * view is the 'accessibleName' property of the component.
     * 
     */
    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = 0.;// UNKNOWN_VALUE;
        int n = 0;// (int)UNKNOWN_VALUE;

        if (property.equals(PURCHASE_PRICE)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);

            if (view != null)
                view.setPurchasePrice(d);
            this.purchasePrice = d;

        } else if (property.equals(DEPOSIT)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);

            if (view != null)
                view.setDeposit(d);
            this.depositAmount = d;

        } else if (property.equals(AMOUNT_BORROWED)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);

            if (view != null)
                view.setAmountBorrowed(d);
            this.amountBorrowed = d;

        } else if (property.equals(LOAN_TERM)) {
            if (value != null && value.length() > 0)
                d = getNumberInstance().doubleValue(value);

            if (view != null)
                view.setLoanTerm(d);
            this.loanTermYears = d;

        } else if (property.equals(INTEREST_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);

            if (view != null)
                view.setAnnualInterestRate(d);
            annualInterestRate = d;

        } else if (property.equals(HAS_ADDITIONAL_PAYMENTS_YES)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                if (view != null)
                    view.setAddtnlPaymentOption(true);

        } else if (property.equals(HAS_ADDITIONAL_PAYMENTS_NO)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                if (view != null)
                    view.setAddtnlPaymentOption(false);

        } else if (property.equals(ADDITIONAL_PAYMENT_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);

            if (view != null)
                view.setAddtnlMonthlyPayments(d);
            this.additionalMonthlyPayment = d;

        } else {
            return false;
        }

        return true;

    }

    /*
     * provide the "controller" with a link to the "view".
     */
    public void setView(MortgageView value) {
        view = value;
        if (view != null)
            update(view);
    }

    public void update(IMortgageCalcResults results) {
        calculateStandardRepayments();
        // if ( additioinalPayment )
        calculateAdditionalRepayments();

        // input data
        results.setPurchasePrice(purchasePrice);
        results.setDeposit(depositAmount);
        results.setAmountBorrowed(amountBorrowed);
        results.setLoanTerm(getLoanTerm());
        results.setAnnualInterestRate(annualInterestRate);
        results.setAddtnlMonthlyPayments(additionalMonthlyPayment);

        // Return Standard Repayment results
        results.setStdMonthlyRepayment(stdMonthlyPayment);
        results.setSTdTotalInterestPaid(stdTotalInterestPaid);
        results.setStdTotalCapitalPaid(stdTotalCapitalPaid);
        results.setStdTotalAmountRepaid(stdTotalAmountRepaid);
        results.setStdYearsToRepay(stdYearsToRepay);
        results.setStdNumberOfPayments(stdTotalNoPayments);

        // Return Additonal Repayment Results
        results.setAddtnlMonthlyRepayment(addtnlMonthlyPayment);
        results.setAddtnlTotalInterestPaid(addtnlTotalInterestPaid);
        results.setAddtnlTotalCapitalPaid(addtnlTotalCapitalPaid);
        results.setAddtnlTotalAmountRepaid(addtnlTotalAmountRepaid);
        results.setAddtnlYearsToRepay(addtnlYearsToRepay);
        results.setAddtnlNumberOfPayments(addtnlTotalNoPayments);

        // Return comparison results
        results
                .setInterestSaved((stdTotalInterestPaid - addtnlTotalInterestPaid) <= 0 ? 0
                        : stdTotalInterestPaid - addtnlTotalInterestPaid);
        // results.setRevisedLoanTerm(addtnlYearsToRepay);
        results.setYearsReduced(stdYearsToRepay - addtnlYearsToRepay <= 0 ? 0
                : stdYearsToRepay - addtnlYearsToRepay);

    }

    public double getInitialValue() {
        return purchasePrice;
    }

    public void setInitialValue(double value) {
        if (this.purchasePrice == value)
            return;

        purchasePrice = value;
        // setModified();
    }

    /***************************************************************************
     * IMortgageCalcParams
     **************************************************************************/
    public double getAdditonalMonthlyRepayment() {
        return additionalMonthlyPayment;
    }

    public double getAmountBorrowed() {
        return amountBorrowed;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    // help method
    public double getMonthlyInterestRate() {
        return annualInterestRate / 100. / 12.;
    }

    public double getDeposit() {
        return depositAmount;
    }

    // years
    public double getLoanTerm() {
        return loanTermYears;
    }

    // help method
    public double getLoanTermMonths() {
        return loanTermYears * 12;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public boolean hasAdditionalPayment() {
        return additioinalPayment;
    }

    /***************************************************************************
     * IMortgageCalcResults
     **************************************************************************/
    // private double additionalMonthlyPayment = 0.0;
    public void setAddtnlMonthlyPayments(double value) {
        additionalMonthlyPayment = value;
    }

    // private double addtnlMonthlyPayment = 0.0;
    public void setAddtnlMonthlyRepayment(double value) {
        addtnlMonthlyPayment = value;
    }

    // private double addtnlTotalNoPayments = 0.0;
    public void setAddtnlNumberOfPayments(double value) {
        addtnlTotalNoPayments = value;
    }

    // private boolean additioinalPayment = false;
    public void setAddtnlPaymentOption(boolean value) {
        additioinalPayment = value;
    }

    // private double addtnlTotalAmountRepaid = 0.0;
    public void setAddtnlTotalAmountRepaid(double value) {
        addtnlTotalAmountRepaid = value;
    }

    // private double addtnlTotalCapitalPaid = 0.0;
    public void setAddtnlTotalCapitalPaid(double value) {
        addtnlTotalCapitalPaid = value;
    }

    // private double addtnlTotalInterestPaid =0.0;
    public void setAddtnlTotalInterestPaid(double value) {
        addtnlTotalInterestPaid = value;
    }

    // private double addtnlYearsToRepay = 0.0;
    public void setAddtnlYearsToRepay(double value) {
        addtnlYearsToRepay = value;
    }

    // private double amountBorrowed = 0.0;
    public void setAmountBorrowed(double value) {
        amountBorrowed = value;
    }

    // private double annualInterestRate = 0.;
    public void setAnnualInterestRate(double value) {
        annualInterestRate = value;
    }

    public void setAnnualSchedules(Vector stdData, Vector addtnlData) {
        if (view != null)
            view.setAnnualSchedules(stdData, addtnlData);
    }

    // private double depositAmount =0.0;
    public void setDeposit(double value) {
        depositAmount = value;
    }

    // private double addtnlInterestSaved = 0.0;
    public void setInterestSaved(double value) {
        addtnlInterestSaved = value;
    }

    // private double loanTermYears = 0.0;
    public void setLoanTerm(double value) {
        loanTermYears = value;
    }

    // private double purchasePrice = 0.0;
    public void setPurchasePrice(double value) {
        purchasePrice = value;
    }

    // private double stdTotalInterestPaid = 0.0;
    public void setSTdTotalInterestPaid(double value) {
        stdTotalInterestPaid = value;
    }

    // private double stdMonthlyPayment = 0.0;
    public void setStdMonthlyRepayment(double value) {
        stdMonthlyPayment = value;
    }

    // private double stdTotalNoPayments = 0.0;
    public void setStdNumberOfPayments(double value) {
        stdTotalNoPayments = value;
    }

    // private double stdTotalAmountRepaid = 0.0;
    public void setStdTotalAmountRepaid(double value) {
        stdTotalAmountRepaid = value;
    }

    // private double stdTotalCapitalPaid = 0.0;
    public void setStdTotalCapitalPaid(double value) {
        stdTotalCapitalPaid = value;
    }

    // private double stdYearsToRepay = 0.0;
    public void setStdYearsToRepay(double value) {
        stdYearsToRepay = value;
    }

    // private double addtnlYearsReduced = 0.0;
    public void setYearsReduced(double value) {
        addtnlYearsReduced = value;
    }

    // private double addtnlRevisedLoanTerm = 0.0;

    /***************************************************************************
     * 
     **************************************************************************/
    public Integer getRequiredFrequencyCodeID() {
        return FrequencyCode.YEARLY;
    }

    /** Creates new MortgageCalc */
    public MortgageCalc() {
        super();
    }

    public MortgageCalc(MortgageCalc obj) {
        super();

        this.assign(obj); // call in top derived class
    }

    public Integer getDefaultModelType() {
        return ModelType.LOAN_MORTGAGE_CALC;
    }

    public String getDefaultTitle() {
        return "Loan & Mortgage Calculator";
    }

    /**
     * Not used at the moment, inherit from Valeri's code.
     */
    public void assign(MortgageCalc obj) {
        super.assign(obj);

        // do not copy data if not the same class
        if (!this.getClass().equals(obj.getClass()))
            return;

        modified = true;// obj.modified;
    }

    public void getInputData(IMortgageCalcParams params) {
        try {
            this.purchasePrice = params.getPurchasePrice();
            this.depositAmount = params.getDeposit();
            this.amountBorrowed = params.getAmountBorrowed();
            // this.amountBorrowed = this.purchasePrice - this.depositAmount;
            this.loanTermYears = params.getLoanTerm();
            this.annualInterestRate = params.getAnnualInterestRate();
            this.additioinalPayment = params.hasAdditionalPayment();
            // if (this.additioinalPayment) {
            this.additionalMonthlyPayment = params
                    .getAdditonalMonthlyRepayment() < 0 ? 0 : params
                    .getAdditonalMonthlyRepayment();
            this.addtnlMonthlyPayment = (this.stdMonthlyPayment < 0 ? 0
                    : this.stdMonthlyPayment)
                    + (params.getAdditonalMonthlyRepayment() < 0 ? 0 : params
                            .getAdditonalMonthlyRepayment());
            // }
            // else {
            // this.addtnlMonthlyPayment = 0.0;
            // this.additionalMonthlyPayment = 0.0;
            // }

            hasValidData = true;
        } catch (Exception ex) {
            hasValidData = false;
        }
    }

    public boolean hasValidData() {
        return hasValidData;
    }

    public boolean isPlanWriter() {
        return isPlanWriter;
    }

    public void setIsPlanWriter(boolean value) {
        isPlanWriter = value;
    }

    /**
     * Return true/false on the status of the view for saving.
     */
    public boolean isReady() {
        return true; // initially set to true, but need to add logic lateron.
        // ( getYears() >= 0 );
    }

    /**
     * Reset all outputs to their default values. This is usually called by the
     * calculate method to make sure all outputs are set to their default
     * values.
     */
    private void resetOutputs() {

        // outputs
        // Standard Repayment
        stdMonthlyPayment = 0.0;
        stdTotalInterestPaid = 0.0;
        stdTotalCapitalPaid = 0.0;
        stdTotalAmountRepaid = 0.0;
        stdYearsToRepay = 0.0;
        stdTotalNoPayments = 0.0;

        // Additional Repayment
        // addtnlMonthlyPayment = 0.0; this is actually an derived input
        addtnlTotalInterestPaid = 0.0;
        addtnlTotalCapitalPaid = 0.0;
        addtnlTotalAmountRepaid = 0.0;
        addtnlYearsToRepay = 0.0;
        addtnlTotalNoPayments = 0.0;
        addtnlInterestSaved = 0.0;
        // addtnlRevisedLoanTerm = 0.0;
        addtnlYearsReduced = 0.0;

        // Payment schedules
        stdMnthlySchedule.removeAllElements();
        addtnlMnthlySchedule.removeAllElements();

        // this.stdScheduleModel.fireTableDataChanged();
        // this.addtnlScheduleModel.fireTableDataChanged();
        // stdScheduleModel = new
        // data.MortgageScheduleTableModel(this.stdMnthlySchedule);

        // addtnlScheduleModel = new
        // MortgageScheduleTableModel(this.addtnlMnthlySchedule);

        this.stdAnnualSchedule.removeAllElements();
        this.addtnlAnnualSchedule.removeAllElements();

    }

    public void clear() {
        _clear();
    }

    private void _clear() {
        this.purchasePrice = 0.0;
        this.depositAmount = 0.0;
        this.loanTermYears = 0.0;
        this.annualInterestRate = 0.0;
        this.additioinalPayment = false;
        this.additionalMonthlyPayment = 0.0;
    }

    public MortgageScheduleTableModel getStdScheduleTableModel() {
        return this.stdScheduleModel;
    }

    public MortgageScheduleTableModel getAddScheduleTableModel() {
        return this.addtnlScheduleModel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public boolean calculate() {
        calculateAmountBorrowed(this, this);
        getInputData(this);
        return calculate(this, this);
    }

    /**
     * Calculate the results of the Mortgage.
     */
    public boolean calculate(IMortgageCalcParams params,
            IMortgageCalcResults results) {
        // only proceed if it has valid data to calculate the schedule.
        if (!hasValidData()) {
            readyToSave = false;
            return false;
        }

        // reset output before calculations.
        this.resetOutputs();

        // Inputs
        // amountBorrowed = params.getAmountBorrowed();

        update(results);

        // Calculate the payment schedule for both standard and additional
        // payments.
        this.calculateSchedules(params);

        this.stdScheduleModel.fireTableDataChanged();
        this.addtnlScheduleModel.fireTableDataChanged();
        // results.setStdSchedule(this.stdScheduleModel);
        // results.setAddtnlSchedule(this.addtnlScheduleModel);
        results.setAnnualSchedules(this.stdAnnualSchedule,
                this.addtnlAnnualSchedule);

        return readyToSave = true;
    }

    /**
     * set the results of the Mortgage for report.
     */
    public boolean setReportData(IMortgageCalcResults results) {
        // // if ( !hasValidData() && !isPlanWriter())
        // return false;

        update(results);
        return true;

    }

    private void calculateStandardRepayments() {
        // Standard Repayment Calculation
        // Calculation of monthlyRepayment
        if (getMonthlyInterestRate() != 0.)
            stdMonthlyPayment = (getMonthlyInterestRate() * (Math.pow(
                    (1.0d + getMonthlyInterestRate()), getLoanTermMonths())))
                    / ((((Math.pow((1.0d + getMonthlyInterestRate()),
                            getLoanTermMonths())) - 1.0d))) * amountBorrowed;
        else if (getLoanTermMonths() > 0.)
            stdMonthlyPayment = amountBorrowed / getLoanTermMonths();
        else
            stdMonthlyPayment = 0;
        // Calculation of totalIntPaid
        stdTotalInterestPaid = (this.stdMonthlyPayment * getLoanTermMonths())
                - amountBorrowed;
        // Calculation of totalCapPaid
        stdTotalCapitalPaid = amountBorrowed;
        // Calculation of totalAmtRepaid
        stdTotalAmountRepaid = this.stdMonthlyPayment * getLoanTermMonths();
        // Calculation of yearsRepay
        stdYearsToRepay = getLoanTerm();
        // Calculation of numberPayments
        stdTotalNoPayments = getLoanTermMonths();
    }

    private void calculateAdditionalRepayments() {
        // Additional Repayment Calculation
        this.addtnlTotalCapitalPaid = this.amountBorrowed;
        // this.addtnlMonthlyPayment = this.stdMonthlyPayment +
        // params.getAdditonalMonthlyRepayment();
        this.addtnlMonthlyPayment = this.stdMonthlyPayment
                + this.additionalMonthlyPayment;
        this.addtnlTotalNoPayments = Math.log(1 / (1 - amountBorrowed
                * getMonthlyInterestRate() / this.addtnlMonthlyPayment))
                / Math.log(1 + getMonthlyInterestRate());
        this.addtnlTotalAmountRepaid = this.addtnlTotalNoPayments
                * this.addtnlMonthlyPayment;
        this.addtnlYearsToRepay = this.addtnlTotalNoPayments / 12;
        this.addtnlTotalInterestPaid = this.addtnlTotalAmountRepaid
                - amountBorrowed;
    }

    /**
     * Calculate the amount borrowed. This piece of code is typically called by
     * a listener program responding to changes in the Purchase price <br>
     * and/or deposit.
     */
    public void calculateAmountBorrowed(IMortgageCalcParams params,
            IMortgageCalcResults results) {
        // Add your handling code here:
        try {
            this.amountBorrowed = (params.getPurchasePrice() < 0 ? 0 : params
                    .getPurchasePrice())
                    - (params.getDeposit() < 0 ? 0 : params.getDeposit());
            results.setAmountBorrowed(this.amountBorrowed);
        } catch (Exception ex) {
            // Can ignore this exception as this is thrown when either purchase
            // price or deposit price is null, which is acceptable.
            System.out.println("MortgageCalc::calculateAmtBorrowed:-Exception:"
                    + ex);
        }

    }

    public void calculateSchedules(IMortgageCalcParams params) {
        // variables for CashFlow
        double interest = 0;
        int flag1 = 0;
        int flag2 = 0;

        // reset schedule vectors to store new schedules
        this.stdMnthlySchedule.removeAllElements();
        this.addtnlMnthlySchedule.removeAllElements();

        double _stdOpeningBalance = this.amountBorrowed;
        double _stdClosingBalance = 0.0;
        double _stdCapitalPayment = 0.0;
        double _stdInterestPayment = 0.0;

        double _addOpeningBalance = this.amountBorrowed;
        double _addClosingBalance = 0.0;
        double _addCapitalPayment = 0.0;
        double _addInterestPayment = 0.0;

        // this.stdMnthlySchedule.add( new Double(_stdOpeningBalance));
        // this.addtnlMnthlySchedule.add( new Double(_addOpeningBalance));

        // calculate monthly standard and additional payment schedules for use
        // with graphical chart and JTables
        for (int index = 1, month = 0; month < getLoanTermMonths(); index++) {
            month += 1;
            flag1 += 1;
            flag2 += 1;

            // Standard Repayment schedule
            _stdClosingBalance = RateUtils.calculateClosingBalanceLoanAmount(
                    getMonthlyInterestRate(), month, this.amountBorrowed, this
                            .getLoanTermMonths());
            _stdCapitalPayment = RateUtils.calculateCapitalPayment(
                    getMonthlyInterestRate(), month, this.amountBorrowed, this
                            .getLoanTermMonths());
            _stdInterestPayment = this.stdMonthlyPayment - _stdCapitalPayment;
            // for CashFlow
            java.math.BigDecimal b = new java.math.BigDecimal(0);
            if (!additioinalPayment) {
                interest += _stdInterestPayment;
                if (flag1 == 12) {
                    try {
                        b = new java.math.BigDecimal(interest);
                    } catch (java.lang.NumberFormatException e) {
                    } finally {
                        interests.addElement(b);
                        interest = 0;
                        flag1 = 0;
                    }
                }
            }
            stdMnthlySchedule.addElement(new MortgageScheduleData(month,
                    _stdOpeningBalance, _stdClosingBalance,
                    _stdInterestPayment, _stdCapitalPayment));
            _stdOpeningBalance = _stdClosingBalance;
            // Additional Repayment schedule
            _addClosingBalance = RateUtils.calculateClosingBalanceLoanAmount(
                    getMonthlyInterestRate(), month, amountBorrowed,
                    addtnlTotalNoPayments);
            _addCapitalPayment = RateUtils.calculateCapitalPayment(
                    getMonthlyInterestRate(), month, amountBorrowed,
                    addtnlTotalNoPayments);
            if (_addCapitalPayment >= _addOpeningBalance)
                _addCapitalPayment = _addOpeningBalance;
            _addInterestPayment = addtnlMonthlyPayment - _addCapitalPayment;
            if (additioinalPayment) {
                if (flag2 == 12) {
                    java.math.BigDecimal b1;
                    try {
                        b1 = new java.math.BigDecimal(interest);
                    } catch (Exception e) {
                        b = new java.math.BigDecimal(0);
                        // 2 e.printStackTrace( System.err );
                    }
                    interests.addElement(b);
                    interest = 0;
                    flag2 = 0;
                }

                interest += _addInterestPayment;
            }

            // if opening balance is negative, it means the loan has been paid
            // off due to the additional payments, we don't need to display
            // anymore records
            // after this.
            if (_addOpeningBalance > 0.0) {
                this.addtnlMnthlySchedule.addElement(new MortgageScheduleData(
                        month, _addOpeningBalance, _addClosingBalance,
                        _addInterestPayment, _addCapitalPayment));
                _addOpeningBalance = _addClosingBalance;
            }

            // Create annual payment data for grahic chart by recording every
            // 12th month of data.
            if (month % 12 == 0) {
                this.stdAnnualSchedule.add(new Double(_stdClosingBalance));
                this.addtnlAnnualSchedule.add(new Double(_addClosingBalance));
            }
        }

        java.math.BigDecimal b2 = new java.math.BigDecimal(0);
        try {
            b2 = new java.math.BigDecimal(interest);
        } catch (java.lang.NumberFormatException e) {
        }
        interests.addElement(b2);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        // financials.addAll( getGeneratedIncomes(desc) );
        financials.addAll(getGeneratedExpenses(desc));
        // financials.addAll( getGeneratedLiabilities(desc) );

        return financials;

    }

    /*
     * private java.util.Collection getGeneratedIncomes( String desc ) {
     * java.util.Collection incomes = new java.util.ArrayList();
     * 
     * return incomes; }
     */

    private java.util.Collection getGeneratedExpenses(String desc) {
        java.util.Date[] dates = getDates(FrequencyCode.YEARLY);
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        java.util.Collection financials = new java.util.ArrayList();
        for (int i = 0; i < interests.size(); i++) {
            java.math.BigDecimal amount = (java.math.BigDecimal) interests
                    .elementAt(i);
            if (amount == null || amount.doubleValue() == 0.)
                continue;

            RegularExpense expense = new RegularExpense();
            expense.setFinancialDesc("Expense from " + desc + " on "
                    + DateTimeUtils.asString(dates[i]));
            expense.setRegularAmount(amount);
            expense.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
            expense.setStartDate(dates[i]);
            expense.setEndDate(DateTimeUtils.getEndOfFinancialYearDate(expense
                    .getStartDate()));
            expense.setTaxable(false);
            expense.setOwnerCodeID(OwnerCode.CLIENT);
            expense.setFinancialTypeId(FinancialTypeEnum.EXPENSE_OTHER);
            expense.setRegularTaxType(ITaxConstants.D_INTEREST_DIVIDEND);
            financials.add(expense);
        }

        return financials;

    }

    /*
     * private java.util.Collection getGeneratedLiabilities( String desc ) {
     * java.util.Collection liabilities = new java.util.ArrayList();
     * 
     * return liabilities; }
     */

    /***************************************************************************
     * help methods
     **************************************************************************/
    public java.util.Date[] getDates(Integer frequencyCodeID) {
        int size = interests.size();
        java.util.Date[] data = new java.util.Date[size];
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        data[0] = calendar.getTime();
        for (int i = 1; i < size; i++) {
            calendar.setTime(data[i - 1]);
            calendar.set(java.util.Calendar.YEAR, calendar
                    .get(java.util.Calendar.YEAR) + 1);
            data[i] = calendar.getTime();
        }

        return data;

    }

    /***************************************************************************
     * methods for input verifier
     **************************************************************************/
    public boolean isValidPerchasePriceInputVerifier(double value) {

        if (value >= getDeposit())
            return true;

        String msg = "Please adjust your mortgage components \n"
                + "according to the purchase price you input";
        JOptionPane.showMessageDialog(view, msg, "Error!",
                JOptionPane.ERROR_MESSAGE);
        return false;

    }

    public void initializeReportData(ReportFields reportFields)
            throws Exception {
        initializeReportData(reportFields, 
                clientService);
    }

    public void initializeReportData(ReportFields reportFields, PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        Currency curr = getCurrencyInstance();

        // Awkward reproduction of the graph
        if (view == null)
            view = new MortgageView();

        this.setView(view);
        // Initialize graph function
        IGraphView graphView = view.getGraphView();
        graphView.setPreferredSize(700, 500);

        // setReportData( this );
        calculate();

        // graphView.setGraphData( this.getGraphData() );

        String prefix = this.getModel().getTitle();
        if (prefix == null || prefix.trim().length() < 3)
            prefix = getDefaultTitle();

        java.io.File file = java.io.File.createTempFile(prefix, "."
                + JPEGFileFilter.JPG);
        file.deleteOnExit();
        graphView.encodeAsJPEG(file);

        reportFields.setValue(reportFields.Mortgage_GraphName, "");
        reportFields.setValue(reportFields.Mortgage_Graph, file
                .getCanonicalPath());

        // Build calc assumptions table

        UpdateableTableModel utm = new UpdateableTableModel(new String[] {
                "DETAILS", "", "Amount ($)" });

        au.com.totemsoft.format.Currency currency = au.com.totemsoft.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("Purchase Price");
        values.add("");
        values.add(currency.toString(getPurchasePrice(), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Deposit Amount");
        values.add("");
        values.add(currency.toString(getDeposit(), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Amount Borrowed");
        values.add("");
        values.add(currency.toString(getAmountBorrowed(), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Loan Term (Years)");
        values.add("");
        values.add(currency.toString(this.getLoanTerm(), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Interest Rate");
        values.add("");
        values.add(currency.toString(this.getAnnualInterestRate(), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Monthly Addition");
        values.add("");
        values
                .add(currency.toString(this.getAdditonalMonthlyRepayment(),
                        true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        reportFields.setValue(reportFields.Mortgage_Assumptions, utm);

        // Build calc result table
        utm = new UpdateableTableModel(new String[] { "",
                "Standard Repayments", "Additional Repayments" });

        values.add("Monthly Payment");
        values.add(currency.toString(this.stdMonthlyPayment, true));
        values.add(currency.toString(this.addtnlMonthlyPayment, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total Interest Paid");
        values.add(currency.toString(this.stdTotalInterestPaid, true));
        values.add(currency.toString(this.addtnlTotalInterestPaid, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total Capital Paid");
        values.add(currency.toString(this.stdTotalCapitalPaid, true));
        values.add(currency.toString(this.addtnlTotalCapitalPaid, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total Amount Repaid");
        values.add(currency.toString(this.stdTotalAmountRepaid, true));
        values.add(currency.toString(this.addtnlTotalAmountRepaid, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Number of Payments");
        values.add(currency.toString(this.stdTotalNoPayments, true));
        values.add(currency.toString(this.addtnlTotalNoPayments, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Interest Saved");
        values.add("");
        values.add(currency.toString(this.addtnlInterestSaved, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("Years Reduced");
        values.add("");
        values.add(currency.toString(this.addtnlYearsReduced, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        reportFields.setValue(reportFields.Mortgage_Results, utm);

        reportFields.setValue(reportFields.Mortgage_AdditionalMonthlyPayment,
                currency.toString(this.additionalMonthlyPayment, true)
                        .toString());
        reportFields.setValue(reportFields.Mortgage_YearsReduced, currency
                .toString(this.addtnlYearsReduced, true).toString());
        reportFields.setValue(reportFields.Mortgage_AmountBorrowed, currency
                .toString(this.amountBorrowed, true).toString());

    }

}