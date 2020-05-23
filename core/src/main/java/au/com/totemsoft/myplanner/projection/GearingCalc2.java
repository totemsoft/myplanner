/*
 * java
 *
 * Created on June 14, 2002, 2:53 PM
 */

package au.com.totemsoft.myplanner.projection;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.CurrencyLabelGenerator;
import au.com.totemsoft.format.Percent;
import au.com.totemsoft.format.PercentLabelGenerator;
import au.com.totemsoft.io.ImageUtils;
import au.com.totemsoft.myplanner.chart.GearingGraphView;
import au.com.totemsoft.myplanner.chart.IGraphView;
import au.com.totemsoft.myplanner.code.BooleanCode;
import au.com.totemsoft.myplanner.code.FrequencyCode;
import au.com.totemsoft.myplanner.code.GearingType;
import au.com.totemsoft.myplanner.code.InvestmentStrategyData;
import au.com.totemsoft.myplanner.code.ModelType;
import au.com.totemsoft.myplanner.tax.au.ITaxConstants;
import au.com.totemsoft.myplanner.tax.au.TaxContainer;
import au.com.totemsoft.util.DateTimeUtils;

public final class GearingCalc2 extends AssetCalc implements
        au.com.totemsoft.myplanner.report.Reportable {

    public static double MAX_CREDIT_LIMIT = 2000000.;

    private Integer gearingTypeID;

    private Integer calculateFrequencyCodeID;

    private Integer incomeFrequencyCodeID;

    private double initialInvestorAmount;

    private double initialLoanAmount;

    private double regularInvestorContributionAmount;

    private double regularLoanAdvanceAmount;

    private double creditLimit;

    private double loanInterestRate;

    private java.util.Date startDate;

    private java.util.Date endDate;

    private Boolean reinvestIncome;

    private Boolean addIterest;

    private double incomeRate;

    private double growthRate;

    private Boolean flatRate;

    private double frankedIncomeRate;

    private double taxFreeDeferredRate;

    private double capitalGainsRate;

    private double otherTaxableIncomeAmount;

    private double[] annualCapitalGrowthRates;

    // calculated (default frequency - monthly)
    // dates
    private int[] month;

    private java.util.Date[] dates;

    // Your Investment
    private double[] investment;

    // New Loan
    private double[] newLoan;

    // Total Loan
    private double[] totalLoan;

    // Total Invested
    private double[] totalInvested;

    // Net Growth
    private double[] capitalGrowth;

    // New Balance
    private double[] balance;

    // Income (Reinvested/Not Reinvested)
    private double[] income;

    // Interest Expense
    private double[] interest;

    // Net Cash pre Tax
    // private double [] ;
    // Net Cash Outlay For Investment
    // private double [] ;
    // Entry Fee
    private double[] entryFee;

    /** Creates new GearingCalc2 */
    public GearingCalc2() {
        _clear();
    }

    public Integer getDefaultModelType() {
        return ModelType.INVESTMENT_GEARING;
    }

    public String getDefaultTitle() {
        return "Investment Gearing";
    }

    public void assign(MoneyCalc mc) {
        super.assign(mc);

        gearingTypeID = ((GearingCalc2) mc).gearingTypeID;
        incomeFrequencyCodeID = ((GearingCalc2) mc).incomeFrequencyCodeID;
        calculateFrequencyCodeID = ((GearingCalc2) mc).calculateFrequencyCodeID;

        initialInvestorAmount = ((GearingCalc2) mc).initialInvestorAmount;
        initialLoanAmount = ((GearingCalc2) mc).initialLoanAmount;
        regularInvestorContributionAmount = ((GearingCalc2) mc).regularInvestorContributionAmount;
        regularLoanAdvanceAmount = ((GearingCalc2) mc).regularLoanAdvanceAmount;

        creditLimit = ((GearingCalc2) mc).creditLimit;
        loanInterestRate = ((GearingCalc2) mc).loanInterestRate;
        startDate = ((GearingCalc2) mc).startDate;
        endDate = ((GearingCalc2) mc).endDate;
        reinvestIncome = ((GearingCalc2) mc).reinvestIncome;
        addIterest = ((GearingCalc2) mc).addIterest;

        growthRate = ((GearingCalc2) mc).growthRate;
        incomeRate = ((GearingCalc2) mc).incomeRate;
        flatRate = ((GearingCalc2) mc).flatRate;

        frankedIncomeRate = ((GearingCalc2) mc).frankedIncomeRate;
        taxFreeDeferredRate = ((GearingCalc2) mc).taxFreeDeferredRate;
        capitalGainsRate = ((GearingCalc2) mc).capitalGainsRate;

        otherTaxableIncomeAmount = ((GearingCalc2) mc).otherTaxableIncomeAmount;

        graphType = ((GearingCalc2) mc).graphType;
        summary = ((GearingCalc2) mc).summary;
        monthly = ((GearingCalc2) mc).monthly;

        copyArrays((GearingCalc2) mc);

    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        gearingTypeID = GearingType.INSTALLMENT;
        incomeFrequencyCodeID = FrequencyCode.HALF_YEARLY;
        calculateFrequencyCodeID = FrequencyCode.MONTHLY;

        initialInvestorAmount = UNKNOWN_VALUE;
        initialLoanAmount = UNKNOWN_VALUE;
        regularInvestorContributionAmount = UNKNOWN_VALUE;
        regularLoanAdvanceAmount = UNKNOWN_VALUE;

        creditLimit = MAX_CREDIT_LIMIT;
        loanInterestRate = UNKNOWN_VALUE;
        startDate = new java.util.Date();
        endDate = null;
        reinvestIncome = Boolean.TRUE;
        addIterest = Boolean.FALSE;

        growthRate = 0.;
        incomeRate = 0.;
        flatRate = Boolean.TRUE;

        frankedIncomeRate = 0.;
        taxFreeDeferredRate = 0.;
        capitalGainsRate = 0.;

        otherTaxableIncomeAmount = 0.;

        graphType = INVESTMENT_VALUE;
        summary = true; // summary or cashflow
        monthly = false; // monthly or yearly

        initArrays(0);

    }

    private void copyArrays(GearingCalc2 gc) {
        month = gc.month == null ? null : (int[]) gc.month.clone();
        dates = gc.dates == null ? null : (java.util.Date[]) gc.dates.clone();
        investment = gc.investment == null ? null : (double[]) gc.investment
                .clone();
        newLoan = gc.newLoan == null ? null : (double[]) gc.newLoan.clone();
        totalLoan = gc.totalLoan == null ? null : (double[]) gc.totalLoan
                .clone();
        totalInvested = gc.totalInvested == null ? null
                : (double[]) gc.totalInvested.clone();
        capitalGrowth = gc.capitalGrowth == null ? null
                : (double[]) gc.capitalGrowth.clone();
        balance = gc.balance == null ? null : (double[]) gc.balance.clone();
        income = gc.income == null ? null : (double[]) gc.income.clone();
        interest = gc.interest == null ? null : (double[]) gc.interest.clone();
        entryFee = gc.entryFee == null ? null : (double[]) gc.entryFee.clone();

        annualCapitalGrowthRates = gc.annualCapitalGrowthRates == null ? null
                : (double[]) gc.annualCapitalGrowthRates.clone();

    }

    private void initArrays(double years) {

        if (years > 0) {
            int months = (int) Math.ceil(years
                    * FrequencyCode
                            .getNumberOfPeriods(getCalculateFrequencyCodeID()));
            // months += 1;

            month = new int[months];
            dates = new java.util.Date[months];
            investment = new double[months];
            newLoan = new double[months];
            totalLoan = new double[months];
            totalInvested = new double[months];
            capitalGrowth = new double[months];
            balance = new double[months];
            income = new double[months];
            interest = new double[months];
            entryFee = new double[months];

        } else {
            month = null;
            dates = null;
            investment = null;
            newLoan = null;
            totalLoan = null;
            totalInvested = null;
            capitalGrowth = null;
            balance = null;
            income = null;
            interest = null;
            entryFee = null;

        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected boolean update(Object property, String value) {

        if (super.update(property, value))
            return true;

        double d = 0.;// UNKNOWN_VALUE;

        if (property.equals(GEARING_TYPE)) {
            setGearingTypeID(getNumberInstance().isValid(value) ? new Integer(
                    value) : new GearingType().getCodeID(value));

        } else if (property.equals(INITIAL_INVESTOR)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setInitialInvestorAmount(d);

        } else if (property.equals(INITIAL_LOAN)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setInitialLoanAmount(d);

        } else if (property.equals(REGULAR_INVESTOR_CONTRIBUTION)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setRegularInvestorContributionAmount(d);

        } else if (property.equals(REGULAR_LOAN_ADVANCE)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setRegularLoanAdvanceAmount(d);

        } else if (property.equals(START_DATE)) {
            setStartDate(DateTimeUtils.getDate(value));

        } else if (property.equals(END_DATE)) {
            setEndDate(DateTimeUtils.getDate(value));

        } else if (property.equals(YEARS_PROJECT)) {
            if (value != null && value.length() > 0)
                d = getNumberInstance().doubleValue(value);
            setEndDate(d > 0 ? new java.util.Date(getStartDate().getTime()
                    + (long) (d * MILLIS_PER_YEAR)) : null);

        } else if (property.equals(REINVEST_INCOME)) {
            if (value.equalsIgnoreCase("" + BooleanCode.rcYES.getId())
                    || value.equalsIgnoreCase(BooleanCode.rcYES.getDescription()))
                setReinvestIncome(Boolean.TRUE);
            else if (value.equalsIgnoreCase("" + BooleanCode.rcNO.getId())
                    || value.equalsIgnoreCase(BooleanCode.rcNO.getDescription()))
                setReinvestIncome(Boolean.FALSE);
            else
                setReinvestIncome(null);

        } else if (property.equals(CREDIT_LIMIT_AMOUNT)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setCreditLimit(d);

        } else if (property.equals(LOAN_INTEREST_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setLoanInterestRate(d);

        } else if (property.equals(ADD_INTEREST)) {
            if (value.equalsIgnoreCase("" + BooleanCode.rcYES.getId())
                    || value.equalsIgnoreCase(BooleanCode.rcYES.getDescription()))
                setAddIterest(Boolean.TRUE);
            else if (value.equalsIgnoreCase("" + BooleanCode.rcNO.getId())
                    || value.equalsIgnoreCase(BooleanCode.rcNO.getDescription()))
                setAddIterest(Boolean.FALSE);
            else
                setAddIterest(null);

        } else if (property.equals(INCOME_FREQUENCY)) {
            if (getNumberInstance().isValid(value))
                setIncomeFrequencyCodeID(new Integer(value));
            else
                setIncomeFrequencyCodeID(new FrequencyCode().getCodeID(value));

        } else if (property.equals(FRANKING_CREDIT_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setFrankedIncomeRate(d);

        } else if (property.equals(TAX_FREE_DEFFERED_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setTaxFreeDeferredRate(d);

        } else if (property.equals(CAPITAL_GAINS_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setCapitalGainsRate(d);

        } else if (property.equals(OTHER_TAXABLE_INCOME)) {
            if (value != null && value.length() > 0)
                d = getCurrencyInstance().doubleValue(value);
            setOtherTaxableIncomeAmount(d);

        } else if (property.equals(FLAT_CAPITAL_GROWTH_RATES)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setFlatRate(Boolean.TRUE);

        } else if (property.equals(VARIABLE_CAPITAL_GROWTH_RATES)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setFlatRate(Boolean.FALSE);

        } else if (property.equals(CAPITAL_GROWTH_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setGrowthRate(d);

        } else if (property.equals(INCOME_RATE)) {
            if (value != null && value.length() > 0)
                d = getPercentInstance().doubleValue(value);
            setIncomeRate(d);

        } else if (property.equals(INVESTMENT_VALUE)) {
            setGraphType(INVESTMENT_VALUE);
        } else if (property.equals(POTENTIAL_OUTCOME)) {
            setGraphType(POTENTIAL_OUTCOME);
        } else if (property.equals(CAPITAL_GROWTH_RATES)) {
            setGraphType(CAPITAL_GROWTH_RATES);

        } else if (property.equals(INVESTMENT_SUMMARY)) {
            setSummary(true);

        } else if (property.equals(ANNUAL_CASHFLOW)) {
            setSummary(false);
            setMonthly(false);

        } else if (property.equals(MONTHLY_CASHFLOW)) {
            setSummary(false);
            setMonthly(true);

        } else
            return false;

        return true;

    }

    public boolean isReady() {
        return (getYears() >= 0)
                && (getGearingTypeID() != null)
                && (getIncomeFrequencyCodeID() != null)
                && (getCalculateFrequencyCodeID() != null)
                && ((getInvestmentStrategyCodeID() != null) && (getGrowthRate() >= 0. && getIncomeRate() >= 0.)) // required
                                                                                                                    // to
                                                                                                                    // calc
                                                                                                                    // graph
                                                                                                                    // data
                && (getInitialInvestorAmount() >= 0.)
                && (getInitialLoanAmount() >= 0.)
                && (getRegularInvestorContributionAmount() >= 0.)
                && (getRegularLoanAdvanceAmount() >= 0.)
                && (getCreditLimit() >= 0.) && (getEntryFeeRate() >= 0.)
                && (getRevisionFeeRate() >= 0.)
                && (getLoanInterestRate() >= 0.) && (getStartDate() != null)
                && (getEndDate() != null) && (getReinvestIncome() != null)
                && (getAddIterest() != null) && (getFrankedIncomeRate() >= 0.)
                && (getTaxFreeDeferredRate() >= 0.)
                && (getCapitalGainsRate() >= 0.)
                && (getOtherTaxableIncomeAmount() >= 0.);
    }

    public void setModified() {

        calculate();

        super.setModified();

    }

    /***************************************************************************
     * 
     **************************************************************************/
    // varying capital growth rate
    public double[] getCapitalGrowthRates() {
        return annualCapitalGrowthRates;
    }

    public double[] getAnnualCapitalGrowthRates() {
        double[] data = new double[(int) Math.ceil(dates.length / 12) + 12];

        Integer strategy = getInvestmentStrategyCodeID();
        if (annualCapitalGrowthRates == null) { // flat rate
            for (int i = 0; i < data.length; i++)
                data[i] = getGrowthRate();

        } else if (strategy == null) { // TODO: do mont-carlo
            for (int i = 0; i < data.length; i++)
                data[i] = getGrowthRate();

        } else { // variable rate
            double[] rates = InvestmentStrategyData
                    .getAllocationHistDataFull(strategy);

            int length = rates.length;
            for (int i = 0; i < data.length; i++)
                data[i] = rates[i % length];

        }

        return data;

    }

    private void updateCapitalGrowthRates() {
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

        int length = rates.length;
        for (int i = 0; i < annualCapitalGrowthRates.length; i++) {
            int index = (i / 12) % length;
            annualCapitalGrowthRates[i] = rates[index];
        }

    }

    public void setInvestmentStrategyCodeID(Integer value) {
        if (equals(getInvestmentStrategyCodeID(), value))
            return;

        updateCapitalGrowthRates();
        super.setInvestmentStrategyCodeID(value);

        // if ( getInvestmentStrategyCodeID() == null )
        // setFlatRate( Boolean.TRUE );
    }

    public double getIncomeRate() {
        return getInvestmentStrategyCodeID() == null ? incomeRate : super
                .getIncomeRate();
    }

    public void setIncomeRate(double value) {
        if (getInvestmentStrategyCodeID() != null) {
            growthRate = 0.;
            incomeRate = 0.;
            return;
        }
        if (incomeRate == value)
            return;

        incomeRate = value;
        setModified();
    }

    // same capital growth rate 0..years-1
    public double getGrowthRate() {
        return getInvestmentStrategyCodeID() == null ? growthRate : super
                .getGrowthRate();
    }

    public void setGrowthRate(double value) {
        if (getInvestmentStrategyCodeID() != null) {
            growthRate = 0.;
            incomeRate = 0.;
            return;
        }
        if (growthRate == value)
            return;

        growthRate = value;
        setModified();
    }

    public double getTotalReturnRate() {
        return getInvestmentStrategyCodeID() == null ? incomeRate + growthRate
                : super.getTotalReturnRate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final java.util.Date[] EMPTY_DATES = null;// new
                                                                // java.util.Date
                                                                // [0];

    public java.util.Date[] getDates(Integer frequencyCodeID) {
        if (dates == null)
            return EMPTY_DATES;
        if (frequencyCodeID == null
                || FrequencyCode.MONTHLY.equals(frequencyCodeID))
            return dates;
        if (!FrequencyCode.YEARLY.equals(frequencyCodeID))
            return EMPTY_DATES;

        double months = MONTHS_PER_YEAR
                / FrequencyCode.getNumberOfPeriods(frequencyCodeID);
        java.util.Date[] data = new java.util.Date[(int) Math.ceil(dates.length
                / months)];

        int j = 0;
        for (int i = 0; i < dates.length; i++) {
            if ((month[i] + 7) % months == 0.)
                data[j++] = dates[i];
        }

        j = data.length - 1;
        java.util.Date date = dates[dates.length - 1];
        if (j >= 0 && data[j] == null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(java.util.Calendar.YEAR);
            if (month[month.length - 1] > java.util.Calendar.JUNE)
                year++;
            data[j] = DateTimeUtils.getDate("30/6/" + year);
        }

        return data;
    }

    public double[] getInvestment(Integer frequencyCodeID) {
        return getData(investment, frequencyCodeID);
    }

    public double[] getNewLoan(Integer frequencyCodeID) {
        return getData(newLoan, frequencyCodeID);
    }

    public double[] getTotalLoan(Integer frequencyCodeID) {
        return getData(totalLoan, frequencyCodeID, true);
    }

    public double[] getTotalInvested(Integer frequencyCodeID) {
        return getData(totalInvested, frequencyCodeID, true);
    }

    public double[] getCapitalGrowth(Integer frequencyCodeID) {
        return getData(capitalGrowth, frequencyCodeID);
    }

    public double[] getInterest(Integer frequencyCodeID) {
        return getData(interest, frequencyCodeID);
    }

    public double[] getIncome(Integer frequencyCodeID) {
        return getData(income, frequencyCodeID);
    }

    public double[] getEntryFee(Integer frequencyCodeID) {
        return getData(entryFee, frequencyCodeID);
    }

    private double[] getData(double[] source, Integer frequencyCodeID) {
        return getData(source, frequencyCodeID, false);
    }

    private double[] getData(double[] source, Integer frequencyCodeID,
            boolean isRunningTotal) {
        if (dates == null)
            return null;
        if (frequencyCodeID == null
                || FrequencyCode.MONTHLY.equals(frequencyCodeID))
            return source;
        if (!FrequencyCode.YEARLY.equals(frequencyCodeID))
            return null;

        double months = MONTHS_PER_YEAR
                / FrequencyCode.getNumberOfPeriods(frequencyCodeID); // 12
        double[] data = new double[(int) Math.ceil(dates.length / months)];

        int j = 0;
        double amount = 0.;
        for (int i = 0; i < source.length; i++) {
            if (source[i] != HOLE) {
                if (isRunningTotal)
                    amount = source[i];
                else
                    amount += source[i];
            }

            if ((month[i] + 7) % months == 0.)
                data[j++] = amount;
        }

        j = data.length - 1;
        if (data[j] == 0.)
            data[j] = amount;

        return data;
    }

    private double getFranking(double income) {
        return income * (getFrankedIncomeRate() / 100.) * (30. / 70.);
    }

    /***************************************************************************
     * final annual aggregated data
     **************************************************************************/
    public double[] getOutlay() {

        if (dates == null)
            return null;

        double[] data = new double[(int) Math.ceil(dates.length
                / (double) MONTHS_PER_YEAR)];

        double[] annualTotalInvested = getTotalInvested(FrequencyCode.YEARLY);
        double[] annualInterest = getInterest(FrequencyCode.YEARLY);

        for (int i = 0; i < data.length; i++) {
            data[i] = annualTotalInvested[i] + annualInterest[i];
        }

        return data;

    }

    // Total Invested (with/without gearing)
    public double[] getTotalInvested(boolean gearing) {

        if (dates == null)
            return null;

        double[] data = new double[(int) Math.ceil(dates.length
                / (double) MONTHS_PER_YEAR)];

        GearingCalc2 g = this;
        if (!gearing) {
            g = new GearingCalc2();
            g.assign(this);

            g.initialLoanAmount = 0.;
            g.regularLoanAdvanceAmount = 0.;
            g.setModified();

            if (!g.calculate())
                return null;
        }

        double[] annualTotalInvested = g.getTotalInvested(FrequencyCode.YEARLY);
        double[] annualCapitalGrowth = g.getCapitalGrowth(FrequencyCode.YEARLY);
        double[] annualIncome = null;

        boolean reinvest = g.getReinvestIncome().booleanValue();
        if (reinvest)
            annualIncome = g.getIncome(FrequencyCode.YEARLY);

        for (int i = 0; i < data.length; i++) {
            data[i] = annualTotalInvested[i] + annualCapitalGrowth[i];
            if (reinvest)
                data[i] += annualIncome[i];
        }

        return data;

    }

    public double[] getTotalLoan() {
        return getTotalLoan(FrequencyCode.YEARLY);
    }

    public double[] getCapitalGrowth() {
        return getCapitalGrowth(FrequencyCode.YEARLY);
    }

    public double[] getCGT() {

        if (dates == null)
            return null;

        double[] data = new double[(int) Math.ceil(dates.length
                / (double) MONTHS_PER_YEAR)];

        double[] capitalGrowth = getCapitalGrowth(FrequencyCode.YEARLY);
        double[] taxRates = getTaxRates();

        for (int i = 0; i < data.length; i++) {
            data[i] = (capitalGrowth[i] / 2.) * (taxRates[i] / 100.);
        }

        return data;

    }

    public double[] getPotentialOutcome(boolean gearing) {

        if (dates == null)
            return null;

        double[] data = new double[(int) Math.ceil(dates.length
                / (double) MONTHS_PER_YEAR)];

        GearingCalc2 g = this;
        if (!gearing) {
            g = new GearingCalc2();
            g.assign(this);

            g.initialLoanAmount = 0.;
            g.regularLoanAdvanceAmount = 0.;
            g.setModified();

            if (!g.calculate())
                return null;
        }

        double[] investmentValue = g.getTotalInvested(gearing);
        double[] totalLoan = g.getTotalLoan(FrequencyCode.YEARLY);
        double[] cgt = g.getCGT();

        for (int i = 0; i < data.length; i++) {
            data[i] = investmentValue[i] - totalLoan[i] - cgt[i];
        }

        return data;

    }

    /***************************************************************************
     * do calculations using calculationFrequency ( monthly )
     **************************************************************************/
    public boolean calculate() {
        if (getYears() <= 0)
            return false;
        if (!isReady())
            return false;
        if (!isModified())
            return false;

        double periods = FrequencyCode
                .getNumberOfPeriods(getCalculateFrequencyCodeID()); // usually
                                                                    // 12

        // number of months in one income period ( e.g. 6 )
        double incomePeriods = MONTHS_PER_YEAR
                / FrequencyCode.getNumberOfPeriods(getIncomeFrequencyCodeID());

        // number of calculation periods in one year ( usually 1 )
        int calcPeriods = (int) (MONTHS_PER_YEAR / periods);

        java.util.Calendar calendar = java.util.Calendar.getInstance();

        java.util.Date date = getStartDate();
        int m;
        int year = 0;
        double investmentAmount;
        double newLoanAmount;
        double totalLoanAmount = 0;
        double totalInvestedAmount = 0;
        double capitalGrowthAmount = 0;
        double totalNetGrowth = 0;
        double balanceAmount = 0;
        double interestAmount = 0;
        double incomeAmount = 0;
        double totalIncome = 0;
        double entryFeeAmount = 0;
        double totalEntryFee = 0;

        double[] growthRates = getAnnualCapitalGrowthRates();
        if (growthRates != null && growthRates.length == 0) { // variable
                                                                // rates
            annualCapitalGrowthRates = new double[dates.length];
            updateCapitalGrowthRates();
            growthRates = getAnnualCapitalGrowthRates();
        }

        double incomeRate2 = getIncomeRate()
                * (1 - getRevisionFeeRate() / getTotalReturnRate());

        for (int i = 0; i < dates.length; i++) {

            calendar.setTime(date);
            m = calendar.get(java.util.Calendar.MONTH);
            if (i == 0) {
                investmentAmount = getInitialInvestorAmount();
                newLoanAmount = getInitialLoanAmount();
            } else {
                if (m + calcPeriods >= MONTHS_PER_YEAR)
                    calendar.roll(java.util.Calendar.YEAR, 1);
                calendar.roll(java.util.Calendar.MONTH, calcPeriods);

                m = calendar.get(java.util.Calendar.MONTH);

                investmentAmount = getRegularInvestorContributionAmount();
                newLoanAmount = getRegularLoanAdvanceAmount();
            }
            calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar
                    .getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
            date = calendar.getTime();

            dates[i] = date;
            month[i] = m;

            // check
            if (totalLoanAmount + newLoanAmount > getCreditLimit())
                newLoanAmount = 0.;

            entryFeeAmount = (investmentAmount + newLoanAmount)
                    * getEntryFeeRate() / 100.;
            totalEntryFee += entryFeeAmount;

            totalLoanAmount += newLoanAmount;
            totalInvestedAmount += (investmentAmount + newLoanAmount - entryFeeAmount);

            double growthRate2 = growthRates[year]
                    * (1 - getRevisionFeeRate()
                            / (getIncomeRate() + growthRates[year]));
            // june - switch to a new rate, july - new annualGrowthRates
            if (m == java.util.Calendar.JUNE)
                year++;

            capitalGrowthAmount = investmentAmount + newLoanAmount
                    + balanceAmount; // last period balanceAmount !!!
            capitalGrowthAmount *= ((growthRate2 / 100.) / periods);

            balanceAmount += investmentAmount + newLoanAmount + incomeAmount; // last
                                                                                // period
                                                                                // income
                                                                                // !!!
            balanceAmount *= (1 + (growthRate2 / 100.) / periods);

            /*
             * if ( i == 0 ) { capitalGrowthAmount += ( totalInvestedAmount *
             * growthRate2 / 100. ) / FrequencyCode.getNumberOfPeriods(
             * getCalculateFrequencyCodeID() ); } else { capitalGrowthAmount += ( (
             * investmentAmount + newLoanAmount ) * growthRate2 / 100. ) /
             * FrequencyCode.getNumberOfPeriods( getCalculateFrequencyCodeID() ); }
             */
            totalNetGrowth += capitalGrowthAmount; // used in calculations of
                                                    // incomeAmount

            interestAmount = (totalLoanAmount * getLoanInterestRate() / 100.)
                    / periods;
            if (getAddIterest() != null && getAddIterest().booleanValue())
                totalInvestedAmount += interestAmount;

            investment[i] = investmentAmount;
            newLoan[i] = newLoanAmount;
            totalLoan[i] = totalLoanAmount;
            totalInvested[i] = totalInvestedAmount;
            capitalGrowth[i] = capitalGrowthAmount;
            balance[i] = balanceAmount;
            interest[i] = interestAmount;
            entryFee[i] = entryFeeAmount;

            if ((month[i] + 7) % incomePeriods == 0.) { // time to pay income
                                                        // (dec, jun)
                incomeAmount = totalInvestedAmount + totalNetGrowth;

                if (getReinvestIncome() != null
                        && getReinvestIncome().booleanValue())
                    incomeAmount += totalIncome;

                incomeAmount = (incomeAmount * incomeRate2 / 100.)
                        / FrequencyCode
                                .getNumberOfPeriods(getIncomeFrequencyCodeID());

                totalIncome += incomeAmount;

                income[i] = incomeAmount;
            } else {
                income[i] = HOLE;
            }

        }

        calculateTax();

        modified = false;

        return true;
    }

    /***************************************************************************
     * Tax Calculations do calculations using yearly
     **************************************************************************/
    private double[] assessableIncome;

    private double[] taxOnInvestmentIncome;

    private double[] taxRate;

    public double[] getAssessableIncome() {
        return assessableIncome;
    }

    public double[] getTaxOnInvestmentIncome() {
        return taxOnInvestmentIncome;
    }

    public double[] getTaxRates() {
        return taxRate;
    }

    private void calculateTax() {

        int length = (int) Math.ceil(dates.length / (double) MONTHS_PER_YEAR);
        assessableIncome = new double[length];
        taxOnInvestmentIncome = new double[length];
        taxRate = new double[length];

        double lastIncome = 0.;
        double[] incomes = getIncome(FrequencyCode.YEARLY);
        double[] interest = getInterest(FrequencyCode.YEARLY);

        // TaxContainer tc = new TaxContainer(); // !!! No clear() method
        for (int i = 0; i < length; i++) {
            TaxContainer tc = new TaxContainer(); // !!! No clear() method

            double incomeAmount = incomes[i] - lastIncome;
            double frankingAmount = getFranking(incomeAmount);

            tc.add(TaxContainer.INCOME, ITaxConstants.I_OTHER,
                    getOtherTaxableIncomeAmount());
            tc.add(TaxContainer.INCOME, ITaxConstants.I_DIVIDENDS_FRANKED,
                    incomeAmount - frankingAmount);
            tc.add(TaxContainer.INCOME,
                    ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT, frankingAmount);

            tc.add(TaxContainer.EXPENSE, ITaxConstants.D_INTEREST_DIVIDEND,
                    interest[i]);

            // tc.add(TaxContainer.PERSONAL,TaxConstants.P_HOSPITAL_COVER,1.00);
            // tc.add(TaxContainer.PERSONAL,TaxConstants.P_MARITAL_STATUS,0.00);
            // tc.add(TaxContainer.PERSONAL,TaxConstants.P_DEPENDANTS,0.00);
            // tc.add(TaxContainer.PERSONAL,TaxConstants.P_SPOUSE_INCOME,0.00);
            // tc.add(TaxContainer.PERSONAL,TaxConstants.P_AGE,0.00);

            tc.calculate();

            assessableIncome[i] = tc.getResult(ITaxConstants.ASSESSABLE_INCOME);
            taxOnInvestmentIncome[i] = tc
                    .getResult(ITaxConstants.TAX_ON_INVESTMENT_INCOME);
            taxRate[i] = 48.5;

        }

    }

    /***************************************************************************
     * 
     **************************************************************************/

    public Integer getIncomeFrequencyCodeID() {
        return incomeFrequencyCodeID;
    }

    public void setIncomeFrequencyCodeID(Integer value) {
        if (equals(incomeFrequencyCodeID, value))
            return;

        incomeFrequencyCodeID = value;
        setModified();
    }

    public Integer getCalculateFrequencyCodeID() {
        return calculateFrequencyCodeID;
    }

    public void setCalculateFrequencyCodeID(Integer value) {
        if (equals(calculateFrequencyCodeID, value))
            return;

        calculateFrequencyCodeID = value;
        initArrays(getYears());

        setModified();
    }

    public Integer getGearingTypeID() {
        return gearingTypeID;
    }

    public void setGearingTypeID(Integer value) {
        if (equals(gearingTypeID, value))
            return;

        gearingTypeID = value;
        if (GearingType.LUMP_SUM.equals(gearingTypeID)) {
            regularInvestorContributionAmount = 0.;
            regularLoanAdvanceAmount = 0.;
        }
        setModified();
    }

    public String getGearingTypeDesc() {
        return new GearingType().getCodeDescription(getGearingTypeID());
    }

    public double getInitialInvestorAmount() {
        return initialInvestorAmount;
    }

    public void setInitialInvestorAmount(double value) {
        if (initialInvestorAmount == value)
            return;

        if (value + getInitialLoanAmount() > getCreditLimit())
            initialInvestorAmount = getCreditLimit() - getInitialLoanAmount();
        else
            initialInvestorAmount = value;

        setModified();
    }

    public double getInitialLoanAmount() {
        return initialLoanAmount;
    }

    public void setInitialLoanAmount(double value) {
        if (initialLoanAmount == value)
            return;

        if (value + getInitialInvestorAmount() > getCreditLimit())
            initialLoanAmount = getCreditLimit() - getInitialInvestorAmount();
        else
            initialLoanAmount = value;

        setModified();
    }

    public double getRegularInvestorContributionAmount() {
        return regularInvestorContributionAmount;
    }

    public void setRegularInvestorContributionAmount(double value) {
        if (regularInvestorContributionAmount == value)
            return;

        regularInvestorContributionAmount = value;
        setModified();
    }

    public double getRegularLoanAdvanceAmount() {
        return regularLoanAdvanceAmount;
    }

    public void setRegularLoanAdvanceAmount(double value) {
        if (regularLoanAdvanceAmount == value)
            return;

        regularLoanAdvanceAmount = value;
        setModified();
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double value) {
        if (creditLimit == value)
            return;

        if (value <= 0 || value > MAX_CREDIT_LIMIT)
            value = MAX_CREDIT_LIMIT;

        creditLimit = value;
        if (initialInvestorAmount + initialLoanAmount > creditLimit) {
            initialInvestorAmount = creditLimit / 2.;
            initialLoanAmount = creditLimit / 2.;
        }

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

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date value) {
        if (equals(startDate, value))
            return;

        startDate = value;
        setYears(DateTimeUtils.getYears(getStartDate(), getEndDate()));
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date value) {
        if (equals(endDate, value))
            return;

        endDate = value;
        setYears(DateTimeUtils.getYears(getStartDate(), getEndDate()));
    }

    public void setYears(double value) {
        if (getYears() == value)
            return;

        initArrays(value);
        super.setYears(value);

    }

    public Boolean getReinvestIncome() {
        return reinvestIncome;
    }

    public void setReinvestIncome(Boolean value) {
        if (equals(reinvestIncome, value))
            return;

        reinvestIncome = value;
        setModified();
    }

    public Boolean getAddIterest() {
        return addIterest;
    }

    public void setAddIterest(Boolean value) {
        if (equals(addIterest, value))
            return;

        addIterest = value;
        setModified();
    }

    // #854
    public Boolean getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(Boolean value) {
        if (equals(flatRate, value))
            return;

        flatRate = value;

        if (flatRate == null || flatRate.booleanValue()) {
            annualCapitalGrowthRates = null;

        } else {
            int months = 0;
            if (getYears() > 0)
                months = (int) Math
                        .ceil(getYears()
                                * FrequencyCode
                                        .getNumberOfPeriods(getCalculateFrequencyCodeID()));

            annualCapitalGrowthRates = new double[months];
            updateCapitalGrowthRates();

        }

        setModified();
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

    public double getOtherTaxableIncomeAmount() {
        return otherTaxableIncomeAmount;
    }

    public void setOtherTaxableIncomeAmount(double value) {
        if (otherTaxableIncomeAmount == value)
            return;

        otherTaxableIncomeAmount = value;
        setModified();
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private String graphType;

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String value) {
        if (equals(graphType, value))
            return;

        graphType = value;
        setModified();
    }

    private boolean summary; // summary or cashflow

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean value) {
        if (summary == value)
            return;

        summary = value;
        if (summary)
            monthly = false;
        setModified();
    }

    private boolean monthly; // monthly or yearly

    public boolean isMonthly() {
        return monthly;
    }

    public void setMonthly(boolean value) {
        if (monthly == value)
            return;

        monthly = value;
        setModified();
    }

    private Integer getTableFrequencyCodeID() {
        return monthly ? FrequencyCode.MONTHLY : FrequencyCode.YEARLY;
    }

    private String getDateTimeFormat() {
        return monthly ? "dd MMM yyyy" : "yyyy";
    }

    public javax.swing.table.TableModel getTableModel() {
        return isSummary() ? getSummaryTableModel() : getCashflowTableModel();
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public static final int CF_DATE = 0;

    public static final int CF_INVESTMENT = 1;

    public static final int CF_NEW_LOAN = 2;

    public static final int CF_ENTRY_FEE = 3;

    public static final int CF_TOTAL_INVESTED = 4;

    public static final int CF_CAPITAL_GROWTH = 5;

    public static final int CF_INCOME = 6;

    public static final int CF_INTEREST_EXPENSE = 7;

    public static final int CF_COLUMN_COUNT = 8;

    private static String[] cashflowColumnNames = new String[] { "Date",
            "Your Investment", "New Loan", "Entry Fee", "Total Invested",
            "Capital Growth", "Income", "Interest Expense", };

    private static Class[] cashflowColumnTypes = new Class[] {
            java.lang.String.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class, };

    private javax.swing.table.DefaultTableModel cashflowTableModel = new javax.swing.table.DefaultTableModel(
            new Object[][] {}, cashflowColumnNames) {

        public Class getColumnClass(int columnIndex) {
            return cashflowColumnTypes[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

    };

    public javax.swing.table.TableModel getCashflowTableModel() {
        initCashflowTable();
        return cashflowTableModel;
    }

    public String[] getCashflowColumnNames() {
        return cashflowColumnNames;
    }

    private void initCashflowTable() {

        if (!isReady()) {
            cashflowTableModel.setRowCount(0);
        } else {
            Object[][] rowData = getCashflowRowData();
            if (rowData != null)
                cashflowTableModel.setDataVector(rowData, cashflowColumnNames);
        }

    }

    private Object[][] getCashflowRowData() {

        Integer frequencyCodeID = getTableFrequencyCodeID();

        // dates
        java.util.Date[] dates = getDates(frequencyCodeID);
        // Your Investment
        double[] investment = getInvestment(frequencyCodeID);
        // New Loan
        double[] newLoan = getNewLoan(frequencyCodeID);
        // Entry Fee
        double[] entryFee = getEntryFee(frequencyCodeID);
        // Total Invested
        double[] totalInvested = getTotalInvested(frequencyCodeID);
        // Net Growth
        double[] capitalGrowth = getCapitalGrowth(frequencyCodeID);
        // Income (Reinvested/Not Reinvested)
        double[] income = getIncome(frequencyCodeID);
        // Interest Expense
        double[] interest = getInterest(frequencyCodeID);

        try {

            if (dates == null)
                return null;

            Object[][] rowData = new Object[dates.length][CF_COLUMN_COUNT];

            Currency curr = getCurrencyInstance();
            for (int i = 0; i < dates.length; i++) {
                rowData[i][CF_DATE] = DateTimeUtils.asString(dates[i],
                        getDateTimeFormat());
                rowData[i][CF_INVESTMENT] = curr.toString(investment[i]);
                rowData[i][CF_NEW_LOAN] = curr.toString(newLoan[i]);
                rowData[i][CF_ENTRY_FEE] = curr.toString(entryFee[i]);
                rowData[i][CF_TOTAL_INVESTED] = curr.toString(totalInvested[i]);
                rowData[i][CF_CAPITAL_GROWTH] = curr.toString(capitalGrowth[i]);
                rowData[i][CF_INCOME] = income[i] == MoneyCalc.HOLE ? null
                        : curr.toString(income[i]);
                rowData[i][CF_INTEREST_EXPENSE] = curr.toString(interest[i]);

            }

            return rowData;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public static final java.awt.Color GEARING = java.awt.Color.blue.darker();

    public static final java.awt.Color NON_GEARING = new java.awt.Color(142, 2,
            142);

    public static final java.awt.Color DEPOSITED = java.awt.Color.green;

    public static final java.awt.Color SPENT = java.awt.Color.red;

    public static final java.awt.Color NET_INCOME = java.awt.Color.blue;

    public static final java.awt.Color TAX_BENEFIT = java.awt.Color.yellow;

    public static final int S_DATE = 0;

    public static final int S_OUTLAYS = 1;

    public static final int S_INVESTMENT = 2;

    public static final int S_TOTAL_LOAN = 3;

    public static final int S_CAPITAL_GROWTH = 4;

    public static final int S_CGT = 5;

    public static final int S_POTENTIAL_OUTCOME_GEARED = 6;

    public static final int S_POTENTIAL_OUTCOME_NON_GEARED = 7;

    public static final int S_COLUMN_COUNT = 8;

    private static String[] summaryColumnNames = new String[] { "Date",
            "Outlays", "Investment Value", "Total Loan", "Capital Growth",
            "CGT", "Potential Outcome", "Potential Outcome (non-geared)", };

    private Class[] summaryColumnTypes = new Class[] { java.lang.String.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, };

    private javax.swing.table.DefaultTableModel summaryTableModel = new javax.swing.table.DefaultTableModel(
            new Object[][] {}, summaryColumnNames) {

        public Class getColumnClass(int columnIndex) {
            return summaryColumnTypes[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

    };

    public javax.swing.table.TableModel getSummaryTableModel() {
        initSummaryTable();
        return summaryTableModel;
    }

    public String[] getSummaryColumnNames() {
        return summaryColumnNames;
    }

    private void initSummaryTable() {

        if (!isReady()) {
            summaryTableModel.setRowCount(0);
        } else {
            Object[][] rowData = getSummaryRowData();
            if (rowData != null)
                summaryTableModel.setDataVector(rowData, summaryColumnNames);
        }

    }

    private Object[][] getSummaryRowData() {

        Integer frequencyCodeID = getTableFrequencyCodeID();

        // dates
        java.util.Date[] dates = getDates(frequencyCodeID);
        // Outlay
        double[] outlay = getOutlay();
        // Your Investment
        double[] investmentGeared = getTotalInvested(true);
        // Total Loan
        double[] totalLoan = getTotalLoan();
        // Capital Growth
        double[] capitalGrowth = getCapitalGrowth();
        // CGT
        double[] cgt = getCGT();
        // Potential Outcome
        double[] potentialOutcomeGeared = getPotentialOutcome(true);
        // Potential Outcome (non-geared)
        double[] potentialOutcomeNonGeared = getPotentialOutcome(false);

        try {

            if (dates == null)
                return null;

            Object[][] rowData = new Object[dates.length][S_COLUMN_COUNT];

            Currency curr = getCurrencyInstance();
            for (int i = 0; i < dates.length; i++) {
                rowData[i][S_DATE] = DateTimeUtils.asString(dates[i],
                        getDateTimeFormat());
                rowData[i][S_OUTLAYS] = curr.toString(outlay[i]);
                rowData[i][S_INVESTMENT] = curr.toString(investmentGeared[i]);
                rowData[i][S_TOTAL_LOAN] = curr.toString(totalLoan[i]);
                rowData[i][S_CAPITAL_GROWTH] = curr.toString(capitalGrowth[i]);
                rowData[i][S_CGT] = curr.toString(cgt[i]);
                rowData[i][S_POTENTIAL_OUTCOME_GEARED] = curr
                        .toString(potentialOutcomeGeared[i]);
                rowData[i][S_POTENTIAL_OUTCOME_NON_GEARED] = curr
                        .toString(potentialOutcomeNonGeared[i]);
            }

            return rowData;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    /***************************************************************************
     * au.com.totemsoft.activex.Reportable interface *
     **************************************************************************/
    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields)
            throws Exception {
        initializeReportData(reportFields, clientService);
    }

    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields,
            au.com.totemsoft.myplanner.service.PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        Currency curr = getCurrencyInstance();
        Percent percent = getPercentInstance();

        reportFields.setValue(reportFields.Gearing_Name, getModel().getTitle());
        reportFields.setValue(reportFields.Gearing_Cashflow,
                getCashflowTableModel());
        reportFields.setValue(reportFields.Gearing_Summary,
                getSummaryTableModel());

        reportFields.setValue(reportFields.Gearing_Years2Project, getYears());
        reportFields.setValue(reportFields.Gearing_InvestmentStrategy,
                getInvestmentStrategyCodeDesc());
        reportFields.setValue(reportFields.Gearing_GrowthRate, percent
                .toString(getGrowthRate()));
        reportFields.setValue(reportFields.Gearing_IncomeRate, percent
                .toString(getIncomeRate()));
        reportFields.setValue(reportFields.Gearing_TotalReturnRate, percent
                .toString(getTotalReturnRate()));
        reportFields.setValue(
                reportFields.Gearing_InitialInvestorDepositAmount, curr
                        .toString(getInitialInvestorAmount()));
        reportFields.setValue(reportFields.Gearing_InitialLoanAmount, curr
                .toString(getInitialLoanAmount()));
        reportFields.setValue(
                reportFields.Gearing_RegularInvestorContributionAmount, curr
                        .toString(getRegularInvestorContributionAmount()));
        reportFields.setValue(reportFields.Gearing_RegularLoanAdvanceAmount,
                curr.toString(getRegularLoanAdvanceAmount()));
        reportFields.setValue(reportFields.Gearing_OtherTaxableIncome, curr
                .toString(getOtherTaxableIncomeAmount()));
        Boolean b = getReinvestIncome();
        reportFields.setValue(reportFields.Gearing_ReinvestIncome, b == null
                || !b.booleanValue() ? BooleanCode.rcNO.getDescription()
                : BooleanCode.rcYES.getDescription());
        reportFields.setValue(reportFields.Gearing_CreditLimitAmount, curr
                .toString(getCreditLimit()));
        reportFields.setValue(reportFields.Gearing_Type, getGearingTypeDesc());

        IGraphView graphView = new GearingGraphView();
        graphView.setPreferredSize(800, 500);

        graphView.setDateTimeFormat(getDateTimeFormat());

        // default for report
        setGraphType(INVESTMENT_VALUE);

        Integer frequencyCodeID = FrequencyCode.YEARLY;// getChartFrequencyCodeID();

        // JCLineStyle.NONE, JCLineStyle.SOLID, JCLineStyle.LONG_DASH,
        // JCLineStyle.SHORT_DASH, JCLineStyle.LSL_DASH, JCLineStyle.DASH_DOT
        java.util.Date[] labels = getDates(frequencyCodeID);
        if (labels != null && INVESTMENT_VALUE.equals(getGraphType())) {
            graphView.customizeChart(
                new double[][] { getTotalInvested(true), getTotalInvested(false)}, 
                labels,
                new String[] {
                    "<html>" + getSummaryColumnNames()[S_INVESTMENT] + "<i>(Gearing)</i></html>",
                    "<html>" + getSummaryColumnNames()[S_INVESTMENT] + "<i>(Non-Gearing)</i></html>"}, 
                new java.awt.Color[] { GEARING, NON_GEARING, },
                null, // int
                // []
                // linePaterns
                true, // boolean leftAxisY
                false // boolean inverted
            );
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator.getInstance());
            graphView.setTitleAxisY1("");

            // remove second data view
            graphView.setTitleAxisY2(null);

            graphView.setTitleAxisX("");

        } else if (labels != null && POTENTIAL_OUTCOME.equals(getGraphType())) {
            graphView
                    .customizeChart(
                            new double[][] { getPotentialOutcome(true),
                                    getPotentialOutcome(false), },
                            labels,
                            new String[] {
                                    "<html>"
                                            + getSummaryColumnNames()[S_POTENTIAL_OUTCOME_GEARED]
                                            + "<i>(Gearing)</i></html>",
                                    "<html>"
                                            + getSummaryColumnNames()[S_POTENTIAL_OUTCOME_GEARED]
                                            + "<i>(Non-Gearing)</i></html>", },
                            new java.awt.Color[] { GEARING, NON_GEARING, },
                            null, // int [] linePaterns
                            true, // boolean leftAxisY
                            false // boolean inverted
                    );
            graphView.setTitleAxisY1("");
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator.getInstance());

            // remove second data view
            graphView.setTitleAxisY2(null);

            graphView.setTitleAxisX("");

        } else if (labels != null
                && CAPITAL_GROWTH_RATES.equals(getGraphType())) {
            graphView.customizeChart(
                    new double[][] { getAnnualCapitalGrowthRates() }, labels,
                    new String[] { "<html>Capital Growth Rates</html>" },
                    new java.awt.Color[] { java.awt.Color.red }, null, // int
                                                                        // []
                                                                        // linePaterns
                    true, // boolean leftAxisY
                    false // boolean inverted
                    );
            graphView.setLabelGeneratorAxisY1(PercentLabelGenerator
                    .getInstance());
            graphView.setTitleAxisY1("");

            // remove second data view
            graphView.setTitleAxisY2(null);

            graphView.setTitleAxisX("");

        } else {
            reportFields.setValue(reportFields.Gearing_Summary_GraphName, null);
            reportFields.setValue(reportFields.Gearing_Summary_Graph, null);
            return;
        }

        reportFields.setValue(reportFields.Gearing_Summary_GraphName, getGraphType());
        reportFields.setValue(reportFields.Gearing_Summary_Graph, ImageUtils.encodeAsJPEG(graphView));

    }

}
