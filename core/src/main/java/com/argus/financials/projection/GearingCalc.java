/*
 * GearingCalc.java
 *
 * Created on 24 December 2001, 15:34
 */

package com.argus.financials.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.argus.financials.bean.Liability;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.GearingType;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.InvestmentStrategyData;
import com.argus.financials.code.ModelType;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;

public class GearingCalc extends AssetCalc {

    private Integer gearingTypeID;

    private Integer requiredFrequencyCodeID = FrequencyCode.MONTHLY;

    private double creditLimit = UNKNOWN_VALUE;

    private double loanInterestRate = UNKNOWN_VALUE;

    private double clientStartLSR = UNKNOWN_VALUE;

    private double maxStartLSR = UNKNOWN_VALUE;

    private double frankedIncomeRate = UNKNOWN_VALUE;

    private double taxFreeDeferredRate = UNKNOWN_VALUE;

    private double capitalGainsRate = UNKNOWN_VALUE;

    private Integer investmentStrategyCodeID;

    // used in gearing/non-gearing calc
    private double[] annualCapitalGrowthRates;

    private Gearing gearing = new Gearing();

    private NonGearing nonGearing = new NonGearing();

    /***************************************************************************
     * Creates new GearingCalc
     */
    public GearingCalc() {
        super();
    }

    /*
     * public GearingCalc( GearingCalc obj ) { super();
     * 
     * this.assign(obj); // call in top derived class }
     */

    public Integer getDefaultModelType() {
        return ModelType.INVESTMENT_GEARING;
    }

    public String getDefaultTitle() {
        return "Investment Gearing";
    }

    /**
     * 
     */
    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = UNKNOWN_VALUE;

        if (property.equals(GEARING_TYPE)) {

            if (Number2.getNumberInstance().isValid(value))
                setGearingTypeID(new Integer(value));
            else
                setGearingTypeID(new GearingType().getCodeID(value));

        } else if (property.equals(CLIENT_START_LSR_RATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            setClientStartLSR(d);

        } else if (property.equals(MAXIMUM_START_LSR_RATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            setMaxStartLSR(d);

        } else if (property.equals(CREDIT_LIMIT_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setCreditLimit(d);

        } else if (property.equals(LOAN_INTEREST_RATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            setLoanInterestRate(d);

        } else if (property.equals(REGULAR_CONTRIBUTION_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setRegularContributionAmount(d);

        } else if (property.equals(INITIAL_CONTRIBUTION_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = Currency.getCurrencyInstance().doubleValue(value);
            setInitialContributionAmount(d);

        } else if (property.equals(FRANKING_CREDIT_RATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            setFrankedIncomeRate(d);

        } else if (property.equals(TAX_FREE_DEFFERED_RATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            setTaxFreeDeferredRate(d);

        } else if (property.equals(CAPITAL_GAINS_RATE)) {
            if (value != null && value.length() > 0)
                d = Percent.getPercentInstance().doubleValue(value);
            setCapitalGainsRate(d);

        } else
            return false;

        return true;
    }

    /***************************************************************************
     * 
     */
    public void assign(GearingCalc obj) {
        super.assign(obj);

        // do not copy data if not the same class
        if (!this.getClass().equals(obj.getClass()))
            return;

        setModified();

    }

    /***************************************************************************
     * override xxxModified(), isReady() here
     */

    public boolean isReady() {
        return super.isReady()// Moneycalc
                && (getInitialContributionAmount() >= 0) // =
                                                            // getInitialValue()
                && (!isIndexed() || (isIndexed() && getIndexRate() >= 0))
                && (getTaxRate() >= 0)
                && (getYears() > 0) // years to project (calculated value)
                // AssetCalc
                && (getRegularContributionAmount() >= 0) // = getDelta()
                && (getEntryFeeRate() >= 0)
                && (getRevisionFeeRate() >= 0)
                // GearingCalc
                && (getGearingTypeID() != null)
                && (getRequiredFrequencyCodeID() != null)
                && (getCreditLimit() >= 0)
                && (getLoanInterestRate() >= 0)
                && (getClientStartLSR() >= 0)
                && (getMaxStartLSR() >= 0)
                && (getInvestmentStrategyCodeID() != null) // required to calc
                                                            // graph data
                && (getFrankedIncomeRate() >= 0)
                && (getTaxFreeDeferredRate() >= 0)
                && (getCapitalGainsRate() >= 0);
    }

    public void setModified() {

        if (isReady()) {
            // recalculate balance/income
            gearing.getBalance();
            nonGearing.getBalance();
        }

        super.setModified();

    }

    /**
     * get/set
     */
    private void setYears() {

        if ((getInitialContributionAmount() >= 0) && (getCreditLimit() >= 0)
                && (getClientStartLSR() >= 0)
                && (getRegularContributionAmount() >= 0)) {

            double years = 0;
            double annualAmount = FrequencyCode.getAnnualAmount(
                    getRequiredFrequencyCodeID(),
                    getRegularContributionGearing()).doubleValue();
            if (annualAmount > 0)
                years = (getCreditLimit() - getInitialInvestmentLoan())
                        / annualAmount;
            else
                return;

            setYears(years);
        }

    }

    public void setYears(double value) {
        // we start calc/display from year 0
        // so 3.45 years will be:
        // 0, 1, 2, 3, 3.45 (double [5])
        // value += 1;

        if (getYears() == value)
            return;

        if (value > 0) {
            int years = (int) Math.ceil(value);

            gearing.balance = new double[years + 1];
            nonGearing.balance = new double[years + 1];

            gearing.income = new double[years + 1];
            nonGearing.income = new double[years + 1];

            gearing.interest = new double[years + 1];
            nonGearing.interest = new double[years + 1];

            gearing.equity = new double[years + 1];

            gearing.loan = new double[years + 1];
            nonGearing.loan = new double[years + 1];

            gearing.units = new double[years + 1];
            nonGearing.units = new double[years + 1];

            annualCapitalGrowthRates = new double[years + 1];

        } else {
            gearing.balance = null;
            nonGearing.balance = null;

            gearing.income = null;
            nonGearing.income = null;

            gearing.interest = null;
            nonGearing.interest = null;

            gearing.equity = null;

            gearing.loan = null;
            nonGearing.loan = null;

            gearing.units = null;
            nonGearing.units = null;

            annualCapitalGrowthRates = null;

        }

        super.setYears(value);

        updateAnnualCapitalGrowthRates();

    }

    public double getInitialContributionAmount() {
        return getInitialValue();
    }

    public void setInitialContributionAmount(double value) {
        if (getInitialContributionAmount() == value)
            return;

        setInitialValue(value);
        setYears();

    }

    public double getRegularContributionAmount() {
        return getDelta();
    }

    public void setRegularContributionAmount(double value) {
        if (getRegularContributionAmount() == value)
            return;

        setDelta(value);
        setYears();
    }

    public Integer getGearingTypeID() {
        return gearingTypeID;
    }

    public void setGearingTypeID(Integer value) {
        if (equals(gearingTypeID, value))
            return;

        gearingTypeID = value;
        setModified();
    }

    public String getGearingTypeDesc() {
        return new GearingType().getCodeDescription(getGearingTypeID());
    }

    public Integer getRequiredFrequencyCodeID() {
        return requiredFrequencyCodeID;
    }

    public void setRequiredFrequencyCodeID(Integer value) {
        if (equals(requiredFrequencyCodeID, value))
            return;

        requiredFrequencyCodeID = value;
        setModified();
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double value) {
        if (creditLimit == value)
            return;

        creditLimit = value;
        setYears();

        setModified();
    }

    public double getLoanInterestRate() {
        return loanInterestRate;
    }

    public void setLoanInterestRate(double value) {
        if (loanInterestRate == value)
            return;

        loanInterestRate = value;
        setModified();
    }

    public double getClientStartLSR() {
        return clientStartLSR;
    }

    public void setClientStartLSR(double value) {
        if (clientStartLSR == value)
            return;

        clientStartLSR = value;
        setYears();

        setModified();
    }

    public double getMaxStartLSR() {
        return maxStartLSR;
    }

    public void setMaxStartLSR(double value) {
        if (maxStartLSR == value)
            return;

        maxStartLSR = value;
        setModified();
    }

    public double getMarginCall() {
        return getMaxStartLSR() == UNKNOWN_VALUE ? 0 : getMaxStartLSR() + 10.;
    }

    public double getFrankedIncomeRate() {
        return frankedIncomeRate;
    }

    public void setFrankedIncomeRate(double value) {
        if (frankedIncomeRate == value)
            return;

        frankedIncomeRate = value;
        setModified();
    }

    public double getTaxFreeDeferredRate() {
        return taxFreeDeferredRate;
    }

    public void setTaxFreeDeferredRate(double value) {
        if (taxFreeDeferredRate == value)
            return;

        taxFreeDeferredRate = value;
        setModified();
    }

    public double getCapitalGainsRate() {
        return capitalGainsRate;
    }

    public void setCapitalGainsRate(double value) {
        if (capitalGainsRate == value)
            return;

        capitalGainsRate = value;
        setModified();
    }

    public double getRegularContributionNonGearing() { // client
        double amount = getRegularContributionAmount();
        double lsr = getClientStartLSR();
        return amount == UNKNOWN_VALUE || lsr == UNKNOWN_VALUE ? 0 : amount
                * (100 - lsr) / 100.;
    }

    public double getRegularContributionGearing() { // loan
        double amount = getRegularContributionAmount();
        double lsr = getClientStartLSR();
        return amount == UNKNOWN_VALUE || lsr == UNKNOWN_VALUE ? 0 : amount
                * lsr / 100.;
    }

    public double getInitialInvestmentLoan() {
        double amount = getInitialContributionAmount();
        double lsr = getClientStartLSR();
        return amount == UNKNOWN_VALUE || lsr == UNKNOWN_VALUE ? 0 : amount
                * lsr / 100.;
    }

    public double getInitialInvestmentClient() {
        double amount = getInitialContributionAmount();
        double lsr = getClientStartLSR();
        return amount == UNKNOWN_VALUE || lsr == UNKNOWN_VALUE ? 0 : amount
                * (100 - lsr) / 100.;
    }

    //
    public Integer getInvestmentStrategyCodeID() {
        return investmentStrategyCodeID;
    }

    public void setInvestmentStrategyCodeID(Integer value) {
        if (equals(investmentStrategyCodeID, value))
            return;

        investmentStrategyCodeID = value;
        updateAnnualCapitalGrowthRates();
        setModified();
    }

    public String getInvestmentStrategyCodeDesc() {
        return new InvestmentStrategyCode()
                .getCodeDescription(getInvestmentStrategyCodeID());
    }

    public double getAnnualIncomeRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getIncomeRate();
    }

    // same capital growth rate 0..years-1
    public double getAnnualCapitalGrowthRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getGrowthRate();
    }

    public double getTotalAnnualReturnRate() {
        return getInvestmentStrategyCodeID() == null ? 0
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeID()).getRate();
    }

    // varying capital growth rate 0..years-1
    // TODO: buildup UI to change array values
    public double[] getAnnualCapitalGrowthRates() {
        updateAnnualCapitalGrowthRates(); // refresh
        return annualCapitalGrowthRates;
    }

    private void updateAnnualCapitalGrowthRates() {
        if (annualCapitalGrowthRates == null)
            return;

        int years = getYearsInt();

        Integer strategy = getInvestmentStrategyCodeID();
        if (strategy == null)
            return;

        double[] rates = InvestmentStrategyData
                .getAllocationHistDataFull(strategy);
        if (rates == null)
            throw new Error(
                    "InvestmentStrategyData.getAllocationHistDataFull() is null");

        annualCapitalGrowthRates[0] = 0;
        for (int i = 1; i < annualCapitalGrowthRates.length; i++)
            annualCapitalGrowthRates[i] = rates[mod(i - 1, rates.length)];

    }

    /***************************************************************************
     * label/legend generators
     */
    private String[] labels;

    private java.util.Date[] dates;

    public String[] getLabels() {
        int offset = 0;

        int years = getYearsInt();
        years = years >= 0 ? years : 0;

        if (labels != null && labels.length == years)
            return labels;

        labels = new String[years + 1];
        for (int i = 0; i < years; i++)
            labels[i] = String.valueOf(i + offset);
        // last year label can be fractional
        labels[labels.length - 1] = String
                .valueOf((int) (getYears() * 100) / 100.);

        return labels;
    }

    public java.util.Date[] getDates() {
        int years = getYearsInt();
        years = years >= 0 ? years : 0;

        if (dates != null && dates.length == years)
            return dates;

        int currentYear = DateTimeUtils.getCurrentYear();
        dates = new java.util.Date[years + 1];
        for (int i = 0; i < years + 1; i++)
            dates[i] = DateTimeUtils.getDate("1/7/" + (currentYear + i)); // first
                                                                            // day
                                                                            // of
                                                                            // financial
                                                                            // year

        return dates;
    }

    /**
     * Gearing
     */
    public double[] getBalanceGearing() {
        return gearing.getBalance();
    }

    public double[] getIncomeGearing() {
        return gearing.getIncome();
    }

    public double[] getLoanInterestGearing() {
        return gearing.getLoanInterest();
    }

    public double[] getLoanGearing() {
        return gearing.getLoan();
    }

    public double[] getUnitsGearing() {
        return gearing.getUnits();
    }

    public double[] getEquityGearing() {
        return gearing.getEquity();
    }

    public double[] getLoan2AssetsGearing() {
        return gearing.getLoan2Assets();
    }

    public double getTotalContributionGearing() {
        return gearing.getTotalContribution();
    }

    public double getAccountBalanceGearing() {
        return gearing.getAccountBalance();
    }

    public double getLoanBalanceGearing() {
        return gearing.getLoanBalance();
    }

    public double getNetCapitalPositionGearing() {
        double returnValue = gearing.getAccountBalance();
        if (returnValue < 0)
            return returnValue;

        returnValue -= gearing.getLoanBalance();
        return returnValue;
    }

    public double getTotalInvestmentIncomeGearing() {
        return gearing.getTotalInvestmentIncome();
    }

    public double getTotalLoanInterestGearing() {
        return gearing.getTotalLoanInterest();
    }

    public double getTotalImputationCreditsGearing() {
        return gearing.getTotalImputationCredits();
    }

    public double getTotalTaxFreeDistributionGearing() {
        return gearing.getTotalTaxFreeDistribution();
    }

    public double getCapitalGainsTaxGearing() {
        return gearing.getCapitalGainsTax();
    }

    public double getTotalTaxPayableGearing() {
        return gearing.getTotalTaxPayable();
    }

    /**
     * NonGearing
     */
    public double[] getBalanceNonGearing() {
        return nonGearing.getBalance();
    }

    public double[] getIncomeNonGearing() {
        return nonGearing.getIncome();
    }

    public double[] getLoanInterestNonGearing() {
        return nonGearing.getLoanInterest();
    }

    public double[] getLoanNonGearing() {
        return nonGearing.getLoan();
    }

    public double[] getUnitsNonGearing() {
        return nonGearing.getUnits();
    }

    public double getTotalContributionNonGearing() {
        return nonGearing.getTotalContribution();
    }

    public double getAccountBalanceNonGearing() {
        return nonGearing.getAccountBalance();
    }

    public double getLoanBalanceNonGearing() {
        return nonGearing.getLoanBalance();
    }

    public double getNetCapitalPositionNonGearing() {
        return nonGearing.getAccountBalance();
    }

    double getTotalInvestmentIncomeNonGearing() {
        return nonGearing.getTotalInvestmentIncome();
    }

    public double getTotalLoanInterestNonGearing() {
        return nonGearing.getTotalLoanInterest();
    }

    public double getTotalImputationCreditsNonGearing() {
        return nonGearing.getTotalImputationCredits();
    }

    public double getTotalTaxFreeDistributionNonGearing() {
        return nonGearing.getTotalTaxFreeDistribution();
    }

    public double getCapitalGainsTaxNonGearing() {
        return nonGearing.getCapitalGainsTax();
    }

    public double getTotalTaxPayableNonGearing() {
        return nonGearing.getTotalTaxPayable();
    }

    /***************************************************************************
     * calculations
     */
    private double initialUnitPrice = 1.;

    private abstract class Base {

        double[] balance;

        double[] income;

        double[] loan;

        double[] units;

        double[] interest;

        double totalContribution;

        double accountBalance;

        double totalInvIncome;

        double totalLoanInterest;

        double totalImputationCredits;

        double totalTaxFreeDistribution;

        double totalTaxPayable;

        double getTotalContribution() {
            return totalContribution;
        }

        double getAccountBalance() {
            return accountBalance;
        }

        // VSH: last year value, has to be equals to creditLimits
        // KM: The Loan Amount should match the Credit Limit amount
        // within the Gearing Details tab, as per the Years to Project fomula
        double getLoanBalance() {
            // return getCreditLimit();

            return loan == null || loan.length == 0 ? UNKNOWN_VALUE
                    : loan[loan.length - 1];
        }

        double getTotalInvestmentIncome() {
            return totalInvIncome;
        }

        double getTotalLoanInterest() {
            return totalLoanInterest;
        }

        double getTotalImputationCredits() {
            return totalImputationCredits;
        }

        double getTotalTaxPayable() {
            return totalTaxPayable;
        }

        double getTotalTaxFreeDistribution() {
            return totalTaxFreeDistribution;
        }

        double getCapitalGainsTax() {
            return getTotalInvestmentIncome() * getCapitalGainsRate() / 100.;
        }

        abstract double[] getBalance();

        // using data from getBalance() !!!
        double[] getIncome() {
            return income;
        }

        double[] getLoanInterest() {
            return interest;
        }

        double[] getLoan() {
            return loan;
        }

        double[] getUnits() {
            return units;
        }

    }

    private class Gearing extends Base {

        double[] equity;

        // call this method first !!!
        double[] getBalance() {
            if (getYears() <= 0)
                return null;
            if (!isReady() || balance == null)
                return null;
            if (!isModified())
                return balance;

            double[] capitalRates = getAnnualCapitalGrowthRates();
            double incomeRate = getAnnualIncomeRate();

            double clientContribution = FrequencyCode.getAnnualAmount(
                    getRequiredFrequencyCodeID(),
                    getRegularContributionNonGearing()).doubleValue();

            double loanContribution = FrequencyCode.getAnnualAmount(
                    getRequiredFrequencyCodeID(),
                    getRegularContributionGearing()).doubleValue();
            double contribution = 0;

            double unitPrice = initialUnitPrice;

            double unitBalance = 0; // number of units
            double loanBalance = 0;
            double loanInterest = 0;
            double entryFee = 0;
            double mgmtFee = 0;

            double invIncome = 0;

            double taxFreeDistribution = 0;

            double imputationCredits = 0;
            double netTaxPayable = 0;

            // set start values for the first year
            balance[0] = getInitialInvestmentLoan()
                    + getInitialInvestmentClient();
            income[0] = MoneyCalc.HOLE;
            interest[0] = MoneyCalc.HOLE;
            loan[0] = getInitialInvestmentLoan();
            equity[0] = (1 - loan[0] / balance[0]) * 100.;
            units[0] = unitPrice * getInitialInvestmentClient();

            // end of the first year .. last year
            for (int i = 1; i < balance.length; i++) {
                if (i == 1) { // first year
                    accountBalance = 0;
                    totalContribution = 0;
                    loanBalance = getInitialInvestmentLoan();
                    totalInvIncome = 0;
                    totalLoanInterest = 0;
                    totalTaxPayable = 0;
                    totalImputationCredits = 0;
                    totalTaxFreeDistribution = 0;

                    contribution = getInitialInvestmentClient()
                            + getInitialInvestmentLoan() + accountBalance;
                } else if (i == balance.length - 1) { // last year
                    loanContribution *= getYears() - (int) getYears();
                } else { // full year
                    contribution = 0;
                    if (isIndexed()) {
                        clientContribution *= (100. + getIndexRate()) / 100.;
                        loanContribution *= (100. + getIndexRate()) / 100.;
                    }
                }

                // capitalRates {0, -12, 25, ...}
                unitPrice *= 1 + capitalRates[i - 1] / 100.;

                contribution += clientContribution + loanContribution;
                totalContribution += contribution;

                loanBalance += loanContribution;
                loan[i] = loanBalance;
                loanInterest = loanBalance * getLoanInterestRate() / 100.;
                interest[i] = loanInterest;
                totalLoanInterest += loanInterest;

                entryFee = contribution * getEntryFeeRate() / 100.;

                unitBalance += (contribution - entryFee) / unitPrice;
                units[i] = unitBalance;

                if (i == 1) {
                    // if ( capitalRates.length > 1 )
                    // unitPrice *= 1 + capitalRates[1] / 100.;
                    mgmtFee = unitBalance * unitPrice * getRevisionFeeRate()
                            / 100.;
                } else
                    mgmtFee = (accountBalance + contribution - entryFee)
                            * getRevisionFeeRate() / 100.;

                accountBalance = unitBalance * unitPrice - mgmtFee;
                balance[i] = accountBalance;

                invIncome = accountBalance * incomeRate / 100.;

                income[i] = invIncome;

                totalInvIncome += invIncome;

                imputationCredits = ((invIncome * getFrankedIncomeRate() / 100.) / 0.66) * 0.34;
                totalImputationCredits += imputationCredits;

                taxFreeDistribution = invIncome * getTaxFreeDeferredRate()
                        / 100.;
                totalTaxFreeDistribution += taxFreeDistribution;

                netTaxPayable = (invIncome + imputationCredits - (loanInterest + taxFreeDistribution))
                        * getTaxRate() / 100. - imputationCredits;
                totalTaxPayable += netTaxPayable;

                equity[i] = (1 - loan[i] / accountBalance) * 100.;
            }

            return balance;
        }

        double[] getEquity() {
            return equity;
        }

        double[] getLoan2Assets() { // ratio
            if (equity == null)
                return null;

            double[] loan2Assets = new double[equity.length];
            System.arraycopy(equity, 0, loan2Assets, 0, equity.length);

            for (int i = 0; i < loan2Assets.length; i++)
                loan2Assets[i] = 100. - loan2Assets[i];

            return loan2Assets;
        }

    }

    private class NonGearing extends Base {

        // call this method first !!!
        double[] getBalance() {
            if (getYears() <= 0)
                return null;
            if (!isReady() || balance == null)
                return null;
            if (!isModified())
                return balance;

            double[] capitalRates = getAnnualCapitalGrowthRates();
            double incomeRate = getAnnualIncomeRate();

            double clientContribution = FrequencyCode.getAnnualAmount(
                    getRequiredFrequencyCodeID(),
                    getRegularContributionNonGearing()).doubleValue();
            double loanContribution = 0;
            double contribution = 0;

            double unitPrice = initialUnitPrice;

            double unitBalance = 0; // number of units
            double loanInterest = 0;
            double entryFee = 0;
            double mgmtFee = 0;

            double invIncome = 0;

            double taxFreeDistribution = 0;

            double imputationCredits = 0;
            double netTaxPayable = 0;

            // set start values for the first year
            balance[0] = getInitialInvestmentLoan()
                    + getInitialInvestmentClient();
            income[0] = MoneyCalc.HOLE;
            interest[0] = MoneyCalc.HOLE;
            loan[0] = getInitialInvestmentLoan();
            units[0] = unitPrice * getInitialInvestmentClient();

            // end of the first year .. last year
            for (int i = 1; i < balance.length; i++) {
                if (i == 1) {
                    accountBalance = 0;
                    totalContribution = 0;
                    loan[i] = getInitialInvestmentLoan();
                    totalInvIncome = 0;
                    totalLoanInterest = 0;
                    totalTaxPayable = 0;
                    totalImputationCredits = 0;
                    totalTaxFreeDistribution = 0;

                    contribution = getInitialInvestmentClient() +
                    // getInitialInvestmentLoan() +
                            accountBalance;
                } else if (i == balance.length - 1) { // last year
                    loanContribution *= getYears() - (int) getYears();
                } else {
                    contribution = 0;
                    if (isIndexed()) {
                        clientContribution *= (100. + getIndexRate()) / 100.;
                        loanContribution *= (100. + getIndexRate()) / 100.;
                    }
                }

                unitPrice *= 1 + capitalRates[i - 1] / 100.;

                contribution += clientContribution + loanContribution;
                totalContribution += contribution;

                loan[i] += loanContribution;
                loanInterest = loan[i] * getLoanInterestRate() / 100.;
                interest[i] = loanInterest;
                totalLoanInterest += loanInterest;

                entryFee = contribution * getEntryFeeRate() / 100.;

                unitBalance += (contribution - entryFee) / unitPrice;
                units[i] = unitBalance;

                if (i == 1) {
                    // if ( capitalRates.length > 1 )
                    // unitPrice *= 1 + capitalRates[1] / 100.;
                    mgmtFee = unitBalance * unitPrice * getRevisionFeeRate()
                            / 100.;
                } else
                    mgmtFee = (accountBalance + contribution - entryFee)
                            * getRevisionFeeRate() / 100.;

                accountBalance = unitBalance * unitPrice - mgmtFee;
                balance[i] = accountBalance;

                invIncome = accountBalance * incomeRate / 100.;

                income[i] = invIncome;

                totalInvIncome += invIncome;

                imputationCredits = ((invIncome * getFrankedIncomeRate() / 100.) / 0.66) * 0.34;
                totalImputationCredits += imputationCredits;

                taxFreeDistribution = invIncome * getTaxFreeDeferredRate()
                        / 100.;
                totalTaxFreeDistribution += taxFreeDistribution;

                netTaxPayable = (invIncome + imputationCredits - (loanInterest + taxFreeDistribution))
                        * getTaxRate() / 100. - imputationCredits;
                totalTaxPayable += netTaxPayable;
            }

            return balance;
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        financials.addAll(getGeneratedIncomes(desc));
        financials.addAll(getGeneratedExpenses(desc));
        financials.addAll(getGeneratedLiabilities(desc));

        return financials;

    }

    private java.util.Collection getGeneratedIncomes(String desc) {
        double[] amounts = getIncomeGearing();
        java.util.Date[] dates = getDates();

        java.util.Collection financials = new java.util.ArrayList();
        for (int i = 0; i < (amounts == null ? 0 : amounts.length); i++) {
            try {
                RegularIncome income = new RegularIncome();
                income.setFinancialDesc("Income from " + desc + " on "
                        + DateTimeUtils.asString(dates[i]));
                income.setRegularAmount(amounts[i] == MoneyCalc.HOLE ? null
                        : new java.math.BigDecimal(amounts[i]));
                income.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
                income.setNextDate(dates[i]);
                financials.add(income);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        return financials;
    }

    private java.util.Collection getGeneratedExpenses(String desc) {
        double[] amounts = this.getLoanInterestGearing();
        java.util.Date[] dates = getDates();

        java.util.Collection financials = new java.util.ArrayList();
        for (int i = 0; i < (amounts == null ? 0 : amounts.length); i++) {
            try {
                RegularExpense expense = new RegularExpense();
                expense.setFinancialDesc("Expense from " + desc + " on "
                        + DateTimeUtils.asString(dates[i]));
                expense.setRegularAmount(amounts[i] == MoneyCalc.HOLE ? null
                        : new java.math.BigDecimal(amounts[i]));
                expense.setFrequencyCodeID(FrequencyCode.ONLY_ONCE);
                expense.setNextDate(dates[i]);
                financials.add(expense);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        return financials;

    }

    private java.util.Collection getGeneratedLiabilities(String desc) {
        double[] amounts = new double[] { getCreditLimit() };

        java.util.Collection financials = new java.util.ArrayList();
        for (int i = 0; i < (amounts == null ? 0 : amounts.length); i++) {
            try {
                Liability liability = new Liability();
                liability.setFinancialDesc("Liability from " + desc);
                liability.setAmount(amounts[i] == MoneyCalc.HOLE ? null
                        : new java.math.BigDecimal(amounts[i]));
                financials.add(liability);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        return financials;

    }

}
