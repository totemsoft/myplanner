/*
 * MortgageData.java
 *
 * Created on 11 October 2002, 18:26
 */

package au.com.totemsoft.myplanner.swing.projection;

import java.util.Vector;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.myplanner.projection.IMortgageCalcResults;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.report.data.ClientPersonData;
import au.com.totemsoft.myplanner.service.PersonService;

public class MortgageData extends ClientPersonData implements
        IMortgageCalcResults {

    public Mortgage mortgage = new Mortgage();

    /** Creates a new instance of MortgageData */
    public MortgageData() {
    }

    public MortgageData(PersonService person, MortgageCalc calc)
            throws Exception {
        this();
        init(person, calc);
    }

    public class Mortgage implements java.io.Serializable {
        public Name name = new Name();

        public Data data = new Data();

        public String Graph = STRING_EMPTY;

        public String GraphName = STRING_EMPTY;

        public class Name implements java.io.Serializable {
            public String name = STRING_EMPTY;
        }

        public class Data implements java.io.Serializable {
            // details
            public String purchasePrice = STRING_ZERO;

            public String depositAmount = STRING_ZERO;

            public String amountBorrowed = STRING_ZERO;

            public String loanTerm = STRING_ZERO;

            public String interestRate = STRING_ZERO;

            public String monthlyAddition = STRING_ZERO;

            // standard repayments
            public String stdMonthlyPayment = STRING_ZERO;

            public String stdTotalInterestPaid = STRING_ZERO;

            public String stdTotalCapitalPaid = STRING_ZERO;

            public String stdTotalAmountRepaid = STRING_ZERO;

            public String stdNumberOfPayments = STRING_ZERO;

            // additional repayments
            public String addMonthlyPayment = STRING_ZERO;

            public String addTotalInterestPaid = STRING_ZERO;

            public String addTotalCapitalPaid = STRING_ZERO;

            public String addTotalAmountRepaid = STRING_ZERO;

            public String addNumberOfPayments = STRING_ZERO;

            public String interestSaved = STRING_ZERO;

            public String yearsReduced = STRING_ZERO;

        }

    }

    public void init(PersonService person, Model model) throws Exception {

        if (model == null)
            model = Model.NONE;

        MortgageCalc calc = new MortgageCalc();
        calc.setModel(model);
        init(person, calc);

    }

    public void init(PersonService person, MortgageCalc calc)
            throws Exception {

        super.init(person);

        mortgage.name.name = calc.getModel().getTitle() == null ? STRING_EMPTY
                : calc.getModel().getTitle();

        calc.setReportData(this);

    }

    // Inputs for Mortgage Calculations
    public void setPurchasePrice(double value) {
        mortgage.data.purchasePrice = Currency.getCurrencyInstance().toString(
                value);
    }

    public void setDeposit(double value) {
        mortgage.data.depositAmount = Currency.getCurrencyInstance().toString(
                value);
    }

    public void setAmountBorrowed(double value) {
        mortgage.data.amountBorrowed = Currency.getCurrencyInstance().toString(
                value);
    }

    public void setLoanTerm(double value) {
        mortgage.data.loanTerm = Currency.getCurrencyInstance().toString(value);
    }

    public void setAnnualInterestRate(double value) {
        mortgage.data.interestRate = Currency.getCurrencyInstance().toString(
                value);
    }

    public void setAddtnlPaymentOption(boolean value) {
    }

    public void setAddtnlMonthlyPayments(double value) {
        mortgage.data.monthlyAddition = Currency.getCurrencyInstance()
                .toString(value);
    }

    // Results for standard repayments
    public void setStdMonthlyRepayment(double value) {
        mortgage.data.stdMonthlyPayment = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setSTdTotalInterestPaid(double value) {
        mortgage.data.stdTotalInterestPaid = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setStdTotalCapitalPaid(double value) {
        mortgage.data.stdTotalCapitalPaid = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setStdTotalAmountRepaid(double value) {
        mortgage.data.stdTotalAmountRepaid = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setStdYearsToRepay(double value) {
    }

    public void setStdNumberOfPayments(double value) {
        mortgage.data.stdNumberOfPayments = Currency.getCurrencyInstance()
                .toString(value);
    }

    // Results with addtitional monthly repayments
    public void setAddtnlMonthlyRepayment(double value) {
        mortgage.data.addMonthlyPayment = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setAddtnlTotalInterestPaid(double value) {
        mortgage.data.addTotalInterestPaid = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setAddtnlTotalCapitalPaid(double value) {
        mortgage.data.addTotalCapitalPaid = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setAddtnlTotalAmountRepaid(double value) {
        mortgage.data.addTotalAmountRepaid = Currency.getCurrencyInstance()
                .toString(value);
    }

    public void setAddtnlYearsToRepay(double value) {
    }

    public void setAddtnlNumberOfPayments(double value) {
        mortgage.data.addNumberOfPayments = Currency.getCurrencyInstance()
                .toString(value);
    }

    // Comparison results
    public void setInterestSaved(double value) {
        mortgage.data.interestSaved = Currency.getCurrencyInstance().toString(
                value);
    }

    // public void setRevisedLoanTerm(double value);
    public void setYearsReduced(double value) {
        mortgage.data.yearsReduced = Currency.getCurrencyInstance().toString(
                value);
    }

    // Mortgage schedules
    // public void setStdSchedule(
    // au.com.totemsoft.projections.data.MortgageScheduleTableModel model );
    // public void setAddtnlSchedule(
    // au.com.totemsoft.projections.data.MortgageScheduleTableModel model );
    public void setAnnualSchedules(Vector stdData, Vector addtnlData) {
        ;
    }

}
