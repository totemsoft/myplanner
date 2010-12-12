/*
 * GeneralTaxCalculator.java
 *
 * Created on 11 September 2002, 16:28
 */

package com.argus.financials.projection;

/**
 * 
 * @version
 */

import com.argus.beans.AbstractComponentModel;
import com.argus.beans.MessageSent;
import com.argus.financials.swing.table.UpdateableTableModel;
import com.argus.financials.swing.table.UpdateableTableRow;
import com.argus.financials.tax.au.ITaxConstants;
import com.argus.financials.tax.au.TaxContainer;

public class GeneralTaxCalculatorNew extends AbstractComponentModel implements
        com.argus.financials.report.Reportable {

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

    public static final String I_LUMP_SUM_PART_C = ITaxConstants.I_LUMP_SUM_PART_C;

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

    public static final String LSL_MRT5PERCENT = ITaxConstants.LSL_MRT5PERCENT;

    public static final String LSL_MRT = ITaxConstants.LSL_MRT;

    public static final String LSL_CONCESSIONAL = ITaxConstants.LSL_CONCESSIONAL;

    public static final String AL_MRT = ITaxConstants.AL_MRT;

    public static final String AL_CONCESSIONAL = ITaxConstants.AL_CONCESSIONAL;

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
    public GeneralTaxCalculatorNew() {
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
                && iDividendsFranked / 70 * 30 < iDividendsImputationCredit) {
            sendMessage(this, MessageSent.WARNING,
                    "Imputation Credit can not exceed 30 % of Franked Dividend amount !");
            return false;
        }
        return true;
    }

    public void calculate(String whoIsChanged) {

        double iOther = getDouble(I_OTHER);
        double iSalary = getDouble(I_SALARY);
        double iLumpSumPartA = getDouble(I_LUMP_SUM_PART_A);
        double iLumpSumPartB = getDouble(I_LUMP_SUM_PART_B);
        double iLumpSumPartC = getDouble(I_LUMP_SUM_PART_C);
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

        String lslRedundancy = getValue(ITaxConstants.LSL_REDUNDANCY);

        int resignation = 0;
        if (lslRedundancy.equalsIgnoreCase("Resignation/Retirement"))
            resignation = 1;

        double lslMRT5 = getDouble(ITaxConstants.LSL_MRT5PERCENT);
        double lslConcessional = getDouble(ITaxConstants.LSL_CONCESSIONAL);
        double lslMRT = getDouble(ITaxConstants.LSL_MRT);

        double alMRT = getDouble(ITaxConstants.AL_MRT);
        double alConcessional = getDouble(ITaxConstants.AL_CONCESSIONAL);

        if (resignation == 0) {
            lslMRT = 0.00;
            alMRT = 0.00;
        }

        iLumpSumPartA = lslConcessional + alConcessional;
        iLumpSumPartB = lslMRT5;
        iLumpSumPartC = lslMRT + alMRT;

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
        tc.add(TaxContainer.INCOME, I_LUMP_SUM_PART_C, iLumpSumPartC, 0.00);
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
        setDouble(I_LUMP_SUM_PART_C, iLumpSumPartC);

        setDouble(ITaxConstants.LSL_AL_TOTAL, iLumpSumPartA + iLumpSumPartB
                + iLumpSumPartC);

    }

    public javax.swing.table.TableModel getTaxTableModel() {

        UpdateableTableModel utm = new UpdateableTableModel(new String[] {
                "Description", "", "Income ($)", "Tax withheld ($)" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();

        UpdateableTableRow row;
        java.util.ArrayList values = new java.util.ArrayList();
        java.util.ArrayList columClasses = new java.util.ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        values.add("INCOME");
        values.add("");
        values.add("");
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Salary or Wages");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_SALARY), true));
        values.add(currency.toString(getDouble(ITaxConstants.T_SALARY), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Lump sum (tax 30%) Amount A");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_LUMP_SUM_PART_A),
                true));
        values.add(currency.toString(getDouble(ITaxConstants.T_LUMP_SUM_PART_A),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Lump sum 5 % of Amount B");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_LUMP_SUM_PART_B),
                true));
        values.add(currency.toString(getDouble(ITaxConstants.T_LUMP_SUM_PART_B),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Lump sum at MTR");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_LUMP_SUM_PART_C),
                true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("ETP taxable amount (not excessive)");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.I_ETP_NON_EXCESSIVE), true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("ETP excessive component");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_ETP_EXCESSIVE),
                true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("ETP Tax");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.T_ETP), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Pensions & Allowances");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.I_PENSIONS_ALLOWANCE), true));
        values.add(currency.toString(
                getDouble(ITaxConstants.T_PENSIONS_ALLOWANCE), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Other Pensions");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_OTHER_PENSIONS),
                true));
        values.add(currency.toString(getDouble(ITaxConstants.T_OTHER_PENSIONS),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total RBF");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_RFB), true));
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Gross Interest");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_GROSS_INTEREST),
                true));
        values.add(currency.toString(getDouble(ITaxConstants.T_GROSS_INTEREST),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Dividends - Franked Amount");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.I_DIVIDENDS_FRANKED), true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Dividends - Unfranked Amount");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.I_DIVIDENDS_UNFRANKED), true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Dividends - Imputation Credit");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT), true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Dividends - Tax");
        values.add("");
        values.add("");
        values
                .add(currency.toString(getDouble(ITaxConstants.T_DIVIDENDS),
                        true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_TOTAL_INCOME),
                true));
        values.add(currency.toString(getDouble(ITaxConstants.O_TAX_WITHHELD),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);
        values.add("DEDUCTIONS");
        values.add("");
        values.add("");
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("General deductions");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.D_GENERAL), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Interest and dividend deductions");
        values.add("");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.D_INTEREST_DIVIDEND), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Deductible amount of UPP");
        values.add("");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.D_UNDEDUCTED_PIRCHASE_PRICE), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("TAX CALCULATIONS");
        values.add("");
        values.add("");
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Taxable income");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.TAXABLE_INCOME),
                true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Tax on taxable income");
        values.add("");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.TAX_ON_TAXABLE_INCOME), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Medicare levy");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.ML), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Medicare levy surcharge");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.MLS), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("TAX OFFSETS");
        values.add("");
        values.add("");
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Tax withheld");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.O_TAX_WITHHELD),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Imputation Credit");
        values.add("");
        values.add("");
        values.add(currency.toString(
                getDouble(ITaxConstants.O_IMPUTATION_CREDIT), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("ETP & Lump Sum");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.O_LUMP_SUM_ETP),
                true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Low Income");
        values.add("");
        values.add("");
        values.add(currency
                .toString(getDouble(ITaxConstants.O_LOW_INCOME), true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("ESTIMATED TAX PAYABLE");
        values.add("");
        values.add("");
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Tax payable/Refund");
        values.add("");
        values.add("");
        values
                .add(currency.toString(getDouble(ITaxConstants.TAX_PAYABLE),
                        true));

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("Net Income");
        values.add("");
        values.add(currency.toString(getDouble(ITaxConstants.I_TOTAL_INCOME)
                - getDouble(ITaxConstants.TAX_PAYABLE), true));
        values.add("");

        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);
        /*
         * tax.pSpouseIncome =
         * currency.toString(dm.getDouble(TaxConstants.P_SPOUSE_INCOME));
         * tax.pDependants =
         * currency.toString(dm.getDouble(TaxConstants.P_DEPENDANTS)); tax.pAge =
         * currency.toString(dm.getDouble(TaxConstants.P_AGE));
         * 
         * 
         * String pHospital = (TaxConstants.P_HOSPITAL_COVER == null) ?
         * STRING_EMPTY : dm.getValue(TaxConstants.P_HOSPITAL_COVER); String
         * pMaritalStatus = (TaxConstants.P_MARITAL_STATUS == null) ?
         * STRING_EMPTY : dm.getValue(TaxConstants.P_MARITAL_STATUS);
         * 
         */

        return utm;
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields,
                com.argus.financials.service.ServiceLocator.getInstance()
                        .getClientPerson());
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields,
            com.argus.financials.service.PersonService person)
            throws java.io.IOException {

        if (person != null)
            reportFields.initialize(person);

        reportFields.setValue(reportFields.TaxReport_Table, getTaxTableModel());

    }

}
