/*
 * IRegularType.java
 *
 * Created on 20 November 2002, 11:05
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 */

public interface IRegularType {

    // public static final int dummy = 0;

    // purpose
    public static final int iFINANCIALS = 0;

    public static final int iCASHFLOW = 1;

    public static final int iTAXANALYSIS = 2;

    public static final int iWEALTH = 3;

    public static final int iREPORTS = 4;

    // types of generated income/expense/taxoffset
    public static final int iGROSS_INCOME = 0;

    public static final int iUNFRANKED_INCOME = 1;

    public static final int iTAXFREE_INCOME = 2;

    public static final int iIMPUTATION_CREDIT = 3;

    public static final int iFRANKED_INCOME = 4;

    public static final int iTOTAL_EXPENSE = 5;

    public static final int iPENSION_REBATE = 6;

    public static final int iTAX_DEDUCTIBLE_DEPOSIT = 7;

    public static final int iNON_DEDUCTIBLE_DEPOSIT = 8;

    public static final int iPENSION_PAYMENT = 9;

    public static final int iIMPUTATION_CREDIT_REBATE = 10;

    public static final int iTAX_DEDUCTIBLE_EXPENSE = 11;

    public static final int iNON_DEDUCTIBLE_EXPENSE = 12;

    public static final int iTAXABLE_DRAWDOWN = 13;

    public static final int iNON_TAXABLE_DRAWDOWN = 14;

    public static final Integer GROSS_INCOME = new Integer(iGROSS_INCOME);

    public static final Integer UNFRANKED_INCOME = new Integer(
            iUNFRANKED_INCOME);

    public static final Integer TAXFREE_INCOME = new Integer(iTAXFREE_INCOME);

    public static final Integer IMPUTATION_CREDIT = new Integer(
            iIMPUTATION_CREDIT);

    public static final Integer FRANKED_INCOME = new Integer(iFRANKED_INCOME);

    public static final Integer TOTAL_EXPENSE = new Integer(iTOTAL_EXPENSE);

    public static final Integer TAX_DEDUCTIBLE_EXPENSE = new Integer(
            iTAX_DEDUCTIBLE_EXPENSE);

    public static final Integer NON_DEDUCTIBLE_EXPENSE = new Integer(
            iNON_DEDUCTIBLE_EXPENSE);

    public static final Integer PENSION_REBATE = new Integer(iPENSION_REBATE);

    public static final Integer TAX_DEDUCTIBLE_DEPOSIT = new Integer(
            iTAX_DEDUCTIBLE_DEPOSIT);

    public static final Integer NON_DEDUCTIBLE_DEPOSIT = new Integer(
            iNON_DEDUCTIBLE_DEPOSIT);

    public static final Integer PENSION_PAYMENT = new Integer(iPENSION_PAYMENT);

    public static final Integer IMPUTATION_CREDIT_REBATE = new Integer(
            iIMPUTATION_CREDIT_REBATE);

    public static final Integer TAXABLE_DRAWDOWN = new Integer(
            iTAXABLE_DRAWDOWN);

    public static final Integer NON_TAXABLE_DRAWDOWN = new Integer(
            iNON_TAXABLE_DRAWDOWN);

    public static final String sGROSS_INCOME = "Gross Income";

    public static final String sUNFRANKED_INCOME = "Unfranked Income";

    public static final String sTAXFREE_INCOME = "Taxfree Income";

    public static final String sIMPUTATION_CREDIT = "Imputation Credit";

    public static final String sFRANKED_INCOME = "Franked Income";

    public static final String sTOTAL_EXPENSE = "Total Expense";

    public static final String sPENSION_REBATE = "Offset/Rebate";

    public static final String sTAX_DEDUCTIBLE_DEPOSIT = "TAX_DEDUCTIBLE_DEPOSIT"; // not
                                                                                    // used

    public static final String sNON_DEDUCTIBLE_DEPOSIT = "Non-deductible contribution/deposit";

    public static final String sPENSION_PAYMENT = "Pension Income";

    public static final String sIMPUTATION_CREDIT_REBATE = "Imputation Credit Rebate";

    public static final String sTAX_DEDUCTIBLE_EXPENSE = "Deductible Expense";

    public static final String sNON_DEDUCTIBLE_EXPENSE = "Non deductible Expense";

    public static final String sTAXABLE_DRAWDOWN = "TAXABLE_DRAWDOWN"; // not
                                                                        // used

    public static final String sNON_TAXABLE_DRAWDOWN = "Regular Drawdowns";

    // this table controls which types has to be included for this purpose
    public static final int[][] RULES = new int[][] {
    // 0 = iFINANCIALS
            new int[] { iGROSS_INCOME, iTOTAL_EXPENSE, iPENSION_REBATE,
                    iIMPUTATION_CREDIT_REBATE, iTAXABLE_DRAWDOWN,
                    iNON_TAXABLE_DRAWDOWN, iTAX_DEDUCTIBLE_DEPOSIT,
                    iNON_DEDUCTIBLE_DEPOSIT },
            // 1 = iCASHFLOW
            new int[] { iGROSS_INCOME, iTAX_DEDUCTIBLE_DEPOSIT,
                    iNON_DEDUCTIBLE_DEPOSIT, iTAXABLE_DRAWDOWN,
                    iNON_TAXABLE_DRAWDOWN, iTOTAL_EXPENSE },
            // 2 = iTAXANALYSIS
            new int[] { iUNFRANKED_INCOME, iIMPUTATION_CREDIT, iFRANKED_INCOME,
                    iPENSION_REBATE, iTAX_DEDUCTIBLE_DEPOSIT,
                    iNON_DEDUCTIBLE_DEPOSIT, iPENSION_PAYMENT,
                    iIMPUTATION_CREDIT_REBATE, iTAXFREE_INCOME,
                    iTAX_DEDUCTIBLE_EXPENSE, iNON_DEDUCTIBLE_EXPENSE,
                    iTAXABLE_DRAWDOWN, iNON_TAXABLE_DRAWDOWN },
            // 3 = iWEALTH
            new int[] {},
            // 4 = iREPORTS
            new int[] { iGROSS_INCOME, iUNFRANKED_INCOME, iTAXFREE_INCOME,
                    iIMPUTATION_CREDIT, iFRANKED_INCOME, iTOTAL_EXPENSE,
                    iPENSION_REBATE, iTAX_DEDUCTIBLE_DEPOSIT,
                    iNON_DEDUCTIBLE_DEPOSIT, iPENSION_PAYMENT,
                    iIMPUTATION_CREDIT_REBATE, iTAX_DEDUCTIBLE_EXPENSE,
                    iNON_DEDUCTIBLE_EXPENSE, iTAXABLE_DRAWDOWN,
                    iNON_TAXABLE_DRAWDOWN },

    };

}
