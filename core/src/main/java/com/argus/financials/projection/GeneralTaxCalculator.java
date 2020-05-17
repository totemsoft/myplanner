/*
 * GeneralTaxCalculator.java
 *
 * Created on 11 September 2002, 16:28
 */

package com.argus.financials.projection;

import com.argus.bean.AbstractComponentModel;
import com.argus.bean.MessageSent;
import com.argus.financials.tax.au.ITaxConstants;
import com.argus.financials.tax.au.TaxContainer;

public class GeneralTaxCalculator extends AbstractComponentModel {

    /**
     * Taxable items as interim by-products of calculation
     * 
     */
    public static final String TAXABLE_INCOME = ITaxConstants.TAXABLE_INCOME;

    public static final String ASSESSABLE_INCOME = "AssesableIncome";

    /**
     * Tax types
     * 
     * 
     */
    public static final String TAX_ON_TAXABLE_INCOME = ITaxConstants.TAX_ON_TAXABLE_INCOME;

    public static final String ML = ITaxConstants.ML;

    public static final String MLS = ITaxConstants.MLS;

    /**
     * Tax offests. Currently only tax withheld and imputation credit are
     * implemented
     */
    public static final String O_TAX_WITHHELD = ITaxConstants.O_TAX_WITHHELD;

    public static final String O_IMPUTATION_CREDIT = ITaxConstants.O_IMPUTATION_CREDIT;

    public static final String O_LUMP_SUM_ETP = ITaxConstants.O_LUMP_SUM_ETP;

    public static final String O_LOW_INCOME = ITaxConstants.O_LOW_INCOME;

    /**
     * By product output
     */
    public static final String O_AVERAGE_TAX_RATE = ITaxConstants.O_AVERAGE_TAX_RATE;

    /**
     * Tax calculation results. Imlemetation includes : Total tax on taxable
     * income , medicare levy and medicare levy surcharge Total tax on
     * non-investment income , medicare levy and medicare levy surcharge
     */
    public static final String TAX_PAYABLE = ITaxConstants.TAX_PAYABLE;

    /**
     * Investment income types. IMORTANT NOTE: INCOME_OTHER - Special type of
     * income to determine the correct tax rate. The resulted tax collection
     * will not include the tax for this type of income.
     */
    public static final String I_OTHER = ITaxConstants.I_OTHER;

    public static final String I_SALARY = ITaxConstants.I_SALARY;

    public static final String I_LUMP_SUM_PART_A = ITaxConstants.I_LUMP_SUM_PART_A;

    public static final String I_LUMP_SUM_PART_B = ITaxConstants.I_LUMP_SUM_PART_B;

    public static final String I_ETP_NON_EXCESSIVE = ITaxConstants.I_ETP_NON_EXCESSIVE;

    public static final String I_ETP_EXCESSIVE = ITaxConstants.I_ETP_EXCESSIVE;

    public static final String I_PENSIONS_ALLOWANCE = ITaxConstants.I_PENSIONS_ALLOWANCE;

    public static final String I_OTHER_PENSIONS = ITaxConstants.I_OTHER_PENSIONS;

    public static final String I_RFB = ITaxConstants.I_RFB;

    public static final String I_GROSS_INTEREST = ITaxConstants.I_GROSS_INTEREST;

    public static final String I_DIVIDENDS_UNFRANKED = ITaxConstants.I_DIVIDENDS_UNFRANKED;

    public static final String I_DIVIDENDS_FRANKED = ITaxConstants.I_DIVIDENDS_FRANKED;

    public static final String I_DIVIDENDS_IMPUTATION_CREDIT = ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT;

    public static final String I_TAX_DEFERRED = ITaxConstants.I_TAX_DEFERRED;

    public static final String I_SUPPLEMENT = ITaxConstants.I_SUPPLEMENT;

    public static final String T_OTHER = ITaxConstants.T_OTHER;

    public static final String T_SALARY = ITaxConstants.T_SALARY;

    public static final String T_LUMP_SUM_PART_A = ITaxConstants.T_LUMP_SUM_PART_A;

    public static final String T_LUMP_SUM_PART_B = ITaxConstants.T_LUMP_SUM_PART_B;

    public static final String T_PENSIONS_ALLOWANCE = ITaxConstants.T_PENSIONS_ALLOWANCE;

    public static final String T_OTHER_PENSIONS = ITaxConstants.T_OTHER_PENSIONS;

    public static final String T_ETP = ITaxConstants.T_ETP;

    public static final String T_GROSS_INTEREST = ITaxConstants.T_GROSS_INTEREST;

    public static final String T_DIVIDENDS = ITaxConstants.T_DIVIDENDS;

    public static final String T_TOTAL_TAX_WITHHELD = "TotalTaxWothHeld";

    public static final String I_TOTAL_INCOME = "TotalIncome";

    /**
     * Deductible expenses IMORTANT NOTE: D_GENERAL - Current version will
     * accumulate all deductions excluding Interest paid and UPP under
     * DeductionsOther section. Future releases will deferentiate between
     * deifferent types of dedudctions if bussines rules require.
     */
    public static final String D_GENERAL = ITaxConstants.D_GENERAL;

    public static final String D_INTEREST_DIVIDEND = ITaxConstants.D_INTEREST_DIVIDEND;

    public static final String D_UNDEDUCTED_PIRCHASE_PRICE = ITaxConstants.D_UNDEDUCTED_PIRCHASE_PRICE;

    /**
     * Personal information reqired for taxation calculations
     * 
     * 
     */
    public static final String P_HOSPITAL_COVER = ITaxConstants.P_HOSPITAL_COVER;

    public static final String P_MARITAL_STATUS = ITaxConstants.P_MARITAL_STATUS;

    public static final String P_DEPENDANTS = ITaxConstants.P_DEPENDANTS;

    public static final String P_SPOUSE_INCOME = ITaxConstants.P_SPOUSE_INCOME;

    public static final String P_AGE = ITaxConstants.P_AGE;

    /*
     * Tax constants: need tobe taken from external source
     * 
     */

    public static final String DATE_1900 = "01/01/1900";

    /** Creates new GeneralTaxCalculator */
    public GeneralTaxCalculator() {
    }

    public boolean validate(String whoIsChanged) {
        double iOther = getDouble(I_OTHER);
        double iSalary = getDouble(I_SALARY);
        double iLumpSumPartA = getDouble(I_LUMP_SUM_PART_A);
        double iLumpSumPartB = getDouble(I_LUMP_SUM_PART_B);
        double iEtpNonExcessive = getDouble(I_ETP_NON_EXCESSIVE);
        double iEtpExcessive = getDouble(I_ETP_EXCESSIVE);
        double iPensionsAllowance = getDouble(I_PENSIONS_ALLOWANCE);
        double iOtherPensions = getDouble(I_OTHER_PENSIONS);
        double iRFB = getDouble(I_RFB);
        double iGrossInterest = getDouble(I_GROSS_INTEREST);
        double iDividendsUnfranked = getDouble(I_DIVIDENDS_UNFRANKED);
        double iDividendsFranked = getDouble(I_DIVIDENDS_FRANKED);
        double iDividendsImputationCredit = getDouble(I_DIVIDENDS_IMPUTATION_CREDIT);
        double iTaxDeferred = getDouble(I_TAX_DEFERRED);
        double iSuppliment = getDouble(I_SUPPLEMENT);
        double tOther = getDouble(T_OTHER);
        double tSalary = getDouble(T_SALARY);
        double tLumpSumPartA = getDouble(T_LUMP_SUM_PART_A);
        double tLumpSumPartB = getDouble(T_LUMP_SUM_PART_B);
        double tPensionsAllowance = getDouble(T_PENSIONS_ALLOWANCE);
        double tOtherPensions = getDouble(T_OTHER_PENSIONS);
        double tETP = getDouble(T_ETP);
        double tGrossInterest = getDouble(T_GROSS_INTEREST);
        double tDividends = getDouble(T_DIVIDENDS);

        double dGeneral = getDouble(D_GENERAL);
        double dInterestDividends = getDouble(D_INTEREST_DIVIDEND);
        double dUPP = getDouble(D_UNDEDUCTED_PIRCHASE_PRICE);

        double pSpouseIncome = getDouble(P_SPOUSE_INCOME);
        double pDependants = getDouble(P_DEPENDANTS);
        double pAge = getDouble(P_AGE);

        double concessional = getDouble(ITaxConstants.I_CONCESSINAL);
        double preJuly1983 = getDouble(ITaxConstants.I_PRE_JULY_1983);
        double postJune1983Taxed = getDouble(ITaxConstants.I_POST_JUNE_1983_TAXED);
        double postJune1983UnTaxed = getDouble(ITaxConstants.I_POST_JUNE_1983_UNTAXED);

        // Validating user input
        if ((whoIsChanged.equals(ITaxConstants.T_SALARY) && iSalary == 0.0 && tSalary != 0)
                || (whoIsChanged.equals(ITaxConstants.T_LUMP_SUM_PART_A)
                        && iLumpSumPartA == 0.0 && tLumpSumPartA != 0)
                || (whoIsChanged.equals(ITaxConstants.T_LUMP_SUM_PART_B)
                        && iLumpSumPartB == 0.0 && tLumpSumPartB != 0)
                || (whoIsChanged.equals(ITaxConstants.T_ETP)
                        && iEtpNonExcessive == 0.0 && tETP != 0 && iEtpExcessive == 0.0)
                || (whoIsChanged.equals(ITaxConstants.T_PENSIONS_ALLOWANCE)
                        && iPensionsAllowance == 0.0 && tPensionsAllowance != 0)
                || (whoIsChanged.equals(ITaxConstants.T_GROSS_INTEREST)
                        && iGrossInterest == 0.0 && tGrossInterest != 0)) {
            sendMessage(this, MessageSent.WARNING,
                    "You have entered tax withheld amount without entering any income amount !");
            return false;

        }

        if (whoIsChanged.equals(ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT)
                && iDividendsFranked * 30 / 70 / 100 * 30 < iDividendsImputationCredit) {
            sendMessage(this, MessageSent.WARNING,
                    "Imputation Credit can not exceed 30 % of Franked Dividend amount !");
            return false;
        }
        java.util.Date lslCalcDate = getDate(ITaxConstants.LSL_CALC_DATE);
        java.util.Date lslStartDate = getDate(ITaxConstants.LSL_START_DATE);

        String slslCalcDate = getValue(ITaxConstants.LSL_CALC_DATE).trim();
        String slslStartDate = getValue(ITaxConstants.LSL_START_DATE).trim();

        java.util.Date date1900 = parseDate(DATE_1900);

        // Validations for lsl Start date. Can't be less than 1900/01/01

        if (whoIsChanged.equals(ITaxConstants.LSL_START_DATE)
                && lslStartDate.before(date1900) && slslStartDate != null
                && slslStartDate.length() > 9) {
            lslStartDate = date1900;

            sendMessage(
                    this,
                    MessageSent.WARNING,
                    "Start Service date is less than 01/01/1900 ! Please enter the date exceeding 01/01/1900.");
            return false;
        }

        if (whoIsChanged.equals(ITaxConstants.LSL_CALC_DATE)
                && lslCalcDate.before(lslStartDate)
                && !lslCalcDate.equals(epochDate)) {

            sendMessage(this, MessageSent.WARNING,
                    "End Service date must be greater than Start service date !");
            return false;

        }
        return true;
    }

    public void calculate(String whoIsChanged) {

        double iOther = getDouble(I_OTHER);
        double iSalary = getDouble(I_SALARY);
        double iLumpSumPartA = getDouble(I_LUMP_SUM_PART_A);
        double iLumpSumPartB = getDouble(I_LUMP_SUM_PART_B);
        double iEtpNonExcessive = getDouble(I_ETP_NON_EXCESSIVE);
        double iEtpExcessive = getDouble(I_ETP_EXCESSIVE);
        double iPensionsAllowance = getDouble(I_PENSIONS_ALLOWANCE);
        double iOtherPensions = getDouble(I_OTHER_PENSIONS);
        double iRFB = getDouble(I_RFB);
        double iGrossInterest = getDouble(I_GROSS_INTEREST);
        double iDividendsUnfranked = getDouble(I_DIVIDENDS_UNFRANKED);
        double iDividendsFranked = getDouble(I_DIVIDENDS_FRANKED);
        double iDividendsImputationCredit = getDouble(I_DIVIDENDS_IMPUTATION_CREDIT);
        double iTaxDeferred = getDouble(I_TAX_DEFERRED);
        double iSuppliment = getDouble(I_SUPPLEMENT);
        double tOther = getDouble(T_OTHER);
        double tSalary = getDouble(T_SALARY);
        double tLumpSumPartA = getDouble(T_LUMP_SUM_PART_A);
        double tLumpSumPartB = getDouble(T_LUMP_SUM_PART_B);
        double tPensionsAllowance = getDouble(T_PENSIONS_ALLOWANCE);
        double tOtherPensions = getDouble(T_OTHER_PENSIONS);
        double tETP = getDouble(T_ETP);
        double tGrossInterest = getDouble(T_GROSS_INTEREST);
        double tDividends = getDouble(T_DIVIDENDS);

        double dGeneral = getDouble(D_GENERAL);
        double dInterestDividends = getDouble(D_INTEREST_DIVIDEND);
        double dUPP = getDouble(D_UNDEDUCTED_PIRCHASE_PRICE);

        double pSpouseIncome = getDouble(P_SPOUSE_INCOME);
        double pDependants = getDouble(P_DEPENDANTS);
        double pAge = getDouble(P_AGE);

        double concessional = getDouble(ITaxConstants.I_CONCESSINAL);
        double preJuly1983 = getDouble(ITaxConstants.I_PRE_JULY_1983);
        double postJune1983Taxed = getDouble(ITaxConstants.I_POST_JUNE_1983_TAXED);
        double postJune1983UnTaxed = getDouble(ITaxConstants.I_POST_JUNE_1983_UNTAXED);

        // LSL calculations

        String areDaysAvailable = getValue(ITaxConstants.LSL_ARE_DAYS_AVAILABLE);

        java.util.Date lslCalcDate = getDate(ITaxConstants.LSL_CALC_DATE);
        java.util.Date lslStartDate = getDate(ITaxConstants.LSL_START_DATE);

        String slslCalcDate = getValue(ITaxConstants.LSL_CALC_DATE).trim();
        String slslStartDate = getValue(ITaxConstants.LSL_START_DATE).trim();

        java.util.Date date1900 = parseDate(DATE_1900);

        // Validations for lsl Start date. Can't be less than 1900/01/01

        java.util.Date date78;
        java.util.Date date93;

        String lslRedundancy = getValue(ITaxConstants.LSL_REDUNDANCY);
        int resignation = 0;
        if (lslRedundancy.equalsIgnoreCase("Yes"))
            resignation = 1;

        double lslUnusedTotal = getDouble(ITaxConstants.LSL_TOTAL_UNUSED);
        double lslUsedAfter78 = getDouble(ITaxConstants.LSL_USED_AFTER_1978);
        double lslUsedAfter93 = getDouble(ITaxConstants.LSL_USED_AFTER_1993);
        double lslAmount = getDouble(ITaxConstants.LSL_AMOUNT);
        double alAmount = getDouble(ITaxConstants.AL_AMOUNT);

        // LSL calculate
        /*
         * Step 1: Calculate the post-15 August 1978 component to be included A /
         * B * [(C * (B + D) / E) - F ]
         * 
         * A is the lump sum amount; B is the number of whole days of long
         * service leave included in the unused long service leave in respect of
         * which the lump sum amount was paid; C is the number of whole days in
         * the eligible service period that occurred after 15 August 1978*; D is
         * the number of whole days of long service leave that accrued in
         * respect of the eligible service period and were used by the taxpayer
         * before the retirement date; E is the number of whole days in the
         * eligible service period; and F is the lesser of the number of days of
         * leave used after 15 August 1978* and the number calculated by the
         * formula
         */

        // Use pro-rata if total unused days is 0.
        if (areDaysAvailable.equalsIgnoreCase("no")) {
            lslUnusedTotal = 0.00;
        }
        if (lslUnusedTotal == 0.00) {
            lslUsedAfter78 = 0.00;
            lslUsedAfter93 = 0.00;
            lslUnusedTotal = 100;
        }

        date78 = parseDate(ITaxConstants.DATE_15_08_1978);
        date93 = parseDate(ITaxConstants.DATE_17_08_1993);

        if (lslStartDate.after(date78))
            date78 = lslStartDate;
        if (lslStartDate.after(date93))
            date93 = lslStartDate;

        double totalDaysInESP = Math.round(countDateDiff(lslCalcDate,
                lslStartDate));
        double totalDaysAfter78 = Math
                .round(countDateDiff(lslCalcDate, date78));
        double totalDaysAfter93 = Math
                .round(countDateDiff(lslCalcDate, date93));

        double A = lslAmount;
        double B = lslUnusedTotal;
        double C = totalDaysAfter78;
        double D = lslUsedAfter78 + lslUsedAfter93;
        double E = totalDaysInESP;
        double F = lslUsedAfter78;

        double post93Concessional = 0.00;
        double post93Marginal = 0.00;

        double post78Concessional = 0.00;
        double post78Marginal = 0.00;

        double pre78component = 0.00;

        double alMarginal = 0.00;
        double alConcesional = 0.00;
        /*
         * System.out.println("A= "+A); System.out.println("B= "+B);
         * System.out.println("C= "+C); System.out.println("D= "+D);
         * System.out.println("E= "+E); System.out.println("F= "+F);
         * System.out.println("post78component= "+post78Concessional);
         * System.out.println("pre78component= "+pre78component);
         * System.out.println("A= "+A); System.out.println("B= "+B);
         * System.out.println("C= "+C); System.out.println("D= "+D);
         * System.out.println("E= "+E); System.out.println("F= "+F);
         * 
         * System.out.println("post93Concessional = "+post93Concessional );
         * System.out.println("post93Marginal = "+post93Marginal );
         * 
         */
        if (lslStartDate.before(parseDate(ITaxConstants.DATE_15_08_1978))) {

            post78Concessional = Math.round(A / B)
                    * Math.round(((C * (B + D) / E) - F));
            pre78component = (lslAmount - post78Concessional) / 100 * 5;
        } else if (lslStartDate.before(parseDate(ITaxConstants.DATE_17_08_1993))) {
            post78Concessional = Math.round(A / B)
                    * Math.round(((C * (B + D) / E) - F));
            pre78component = 0.00;
        } else if (lslStartDate.after(parseDate(ITaxConstants.DATE_17_08_1993))) {
            post78Concessional = Math.round(A / B)
                    * Math.round(((C * (B + D) / E) - F));
        }

        if (resignation == 1) {

            alMarginal = alAmount;
            alConcesional = 0.00;

            A = lslAmount;
            B = lslUnusedTotal;
            C = totalDaysAfter93;
            D = lslUsedAfter78 + lslUsedAfter93;
            E = totalDaysInESP;
            F = lslUsedAfter93;

            post93Marginal = Math.round(A / B)
                    * Math.round(((C * (B + D) / E) - F));

        } else {
            alConcesional = alAmount;
        }

        iLumpSumPartA = post78Concessional
                - (post93Concessional + post93Marginal) + alConcesional;
        iLumpSumPartB = pre78component + post93Marginal + alMarginal;
        // LSL calculations end

        String pHospital = getValue(P_HOSPITAL_COVER);
        String pMaritalStatus = getValue(P_MARITAL_STATUS);

        TaxContainer tc = new TaxContainer();

        tc.add(TaxContainer.INCOME, I_OTHER, iOther, tOther);
        tc.add(TaxContainer.INCOME, I_SALARY, iSalary, tSalary);
        tc.add(TaxContainer.INCOME, I_LUMP_SUM_PART_A, iLumpSumPartA,
                tLumpSumPartA);
        tc.add(TaxContainer.INCOME, I_LUMP_SUM_PART_B, iLumpSumPartB,
                tLumpSumPartB);
        tc.add(TaxContainer.INCOME, I_ETP_NON_EXCESSIVE, iEtpNonExcessive);
        tc.add(TaxContainer.INCOME, I_ETP_EXCESSIVE, iEtpExcessive, tETP);
        tc.add(TaxContainer.INCOME, I_PENSIONS_ALLOWANCE, iPensionsAllowance,
                tPensionsAllowance);
        tc.add(TaxContainer.INCOME, I_OTHER_PENSIONS, iOtherPensions,
                tOtherPensions);
        tc.add(TaxContainer.INCOME, I_RFB, iRFB);
        tc.add(TaxContainer.INCOME, I_GROSS_INTEREST, iGrossInterest,
                tGrossInterest);
        tc.add(TaxContainer.INCOME, I_DIVIDENDS_UNFRANKED, iDividendsUnfranked,
                tDividends);
        tc.add(TaxContainer.INCOME, I_DIVIDENDS_FRANKED, iDividendsFranked);
        tc.add(TaxContainer.INCOME, I_DIVIDENDS_IMPUTATION_CREDIT,
                iDividendsImputationCredit);
        tc.add(TaxContainer.INCOME, I_TAX_DEFERRED, iTaxDeferred);
        tc.add(TaxContainer.INCOME, I_SUPPLEMENT, iSuppliment);

        tc.add(TaxContainer.INCOME, ITaxConstants.I_CONCESSINAL, concessional);
        tc.add(TaxContainer.INCOME, ITaxConstants.I_PRE_JULY_1983, preJuly1983);
        tc.add(TaxContainer.INCOME, ITaxConstants.I_POST_JUNE_1983_TAXED,
                postJune1983Taxed);
        tc.add(TaxContainer.INCOME, ITaxConstants.I_POST_JUNE_1983_UNTAXED,
                postJune1983UnTaxed);

        tc.add(TaxContainer.EXPENSE, ITaxConstants.D_INTEREST_DIVIDEND,
                dInterestDividends);
        tc.add(TaxContainer.EXPENSE, ITaxConstants.D_GENERAL, dGeneral);
        tc.add(TaxContainer.EXPENSE, ITaxConstants.D_UNDEDUCTED_PIRCHASE_PRICE,
                dUPP);

        int hospital = 0;
        int married = 1;
        if (pHospital.equalsIgnoreCase("Yes"))
            hospital = 1;
        if (pMaritalStatus.equalsIgnoreCase("Single"))
            married = 0;

        tc.add(TaxContainer.PERSONAL, ITaxConstants.P_HOSPITAL_COVER, hospital);
        tc.add(TaxContainer.PERSONAL, ITaxConstants.P_MARITAL_STATUS, married);
        tc.add(TaxContainer.PERSONAL, ITaxConstants.P_DEPENDANTS, pDependants);
        tc.add(TaxContainer.PERSONAL, ITaxConstants.P_SPOUSE_INCOME,
                pSpouseIncome);
        tc.add(TaxContainer.PERSONAL, ITaxConstants.P_AGE, pAge);

        tc.calculate();

        setDouble(TAX_ON_TAXABLE_INCOME, tc
                .getResult(ITaxConstants.TAX_ON_TAXABLE_INCOME));
        setDouble(ML, tc.getResult(ITaxConstants.ML));
        setDouble(MLS, tc.getResult(ITaxConstants.MLS));
        setDouble(TAXABLE_INCOME, tc.getResult(ITaxConstants.TAXABLE_INCOME));
        setDouble(TAX_PAYABLE, tc.getResult(ITaxConstants.TAX_PAYABLE));
        setDouble(T_TOTAL_TAX_WITHHELD, tc
                .getResult(ITaxConstants.O_TAX_WITHHELD));
        setDouble(O_TAX_WITHHELD, tc.getResult(ITaxConstants.O_TAX_WITHHELD));
        setDouble(O_IMPUTATION_CREDIT, tc
                .getResult(ITaxConstants.O_IMPUTATION_CREDIT));
        setDouble(O_LUMP_SUM_ETP, tc.getResult(ITaxConstants.O_LUMP_SUM_ETP));
        setDouble(O_LOW_INCOME, tc.getResult(ITaxConstants.O_LOW_INCOME));

        setDouble(I_TOTAL_INCOME, tc.getResult(ITaxConstants.I_TOTAL_INCOME));
        setDouble(I_ETP_NON_EXCESSIVE, tc
                .getResult(ITaxConstants.I_ETP_NON_EXCESSIVE));
        setDouble(I_LUMP_SUM_PART_A, iLumpSumPartA);
        setDouble(I_LUMP_SUM_PART_B, iLumpSumPartB);

    }

}
