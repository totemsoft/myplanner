/*
 * FinancialColumnID.java
 *
 * Created on 14 April 2003, 09:52
 */

package au.com.totemsoft.myplanner.table.model;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public interface FinancialColumnID {

    // columns FROM FINANCIAL CLASS
    public static final int NAME = 0;

    public static final int OBJECT_TYPE = 1;

    public static final int FINANCIAL_TYPE = 2;

    public static final int FINANCIAL_CODE = 3;

    public static final int OWNER = 4;

    public static final int AMOUNT = 5;

    public static final int INSTITUTION = 6;

    public static final int COUNTRY = 7;

    public static final int DESCRIPTION = 8;

    public static final int START_DATE = 9;

    public static final int END_DATE = 10;

    public static final int FRANKED = 11;

    public static final int TAX_FREE_DEFERRED = 12;

    public static final int CAPITAL_GROWTH = 13;

    public static final int INCOME = 14;

    public static final int UPFRONT_FEE = 15;

    public static final int ONGOING_FEE = 16;

    public static final int DEDUCTIBLE = 17;

    public static final int DEDUCTIBLE_DSS = 18;

    public static final int COMPLYING_DSS = 19;

    public static final int INDEXATION = 20;

    public static final int EXPENSE = 21;

    public static final int REBATEABLE = 22;

    // columns FROM ASSET CLASS
    public static final int ACCOUNT_NUMBER = 23;

    public static final int FUND_TYPE = 24;

    public static final int INVESTMENT_TYPE = 25;

    public static final int UNITS_SHARES = 26;

    public static final int UNITS_SHARES_PRICE = 27;

    public static final int UNITS_SHARES_PRICE_DATE = 28;

    public static final int PURCHASE_COST = 29;

    public static final int REPLACEMENT_VALUE = 30;

    public static final int TAX_DEDUCTIBLE = 31;

    public static final int FREQUENCY = 32;

    public static final int REGULAR_AMOUNT = 33;

    public static final int ANNUAL_AMOUNT = 34;

    public static final int CONTRIBUTION = 35;

    public static final int CONTRIBUTION_INDEXATION = 36;

    public static final int CONTRIBUTION_START_DATE = 37;

    public static final int CONTRIBUTION_END_DATE = 38;

    public static final int DRAWDOWN = 39;

    public static final int DRAWDOWN_INDEXATION = 40;

    public static final int DRAWDOWN_START_DATE = 41;

    public static final int DRAWDOWN_END_DATE = 42;

    // columns FROM REGULAR CLASS
    public static final int ASSOCIATED_FINANCIAL = 43;

    public static final int TAXABLE = 44;

    public static final int TAX_TYPE = 45;

    // columns FROM LIABILITY CLASS
    public static final int INTEREST = 46;

    // two extra columns to show client/partner amount
    // public static final int AMOUNT = 5;
    public static final int AMOUNT_CLIENT = 47;

    public static final int AMOUNT_PARTNER = 48;

    // three extra columns to show client/partner financial year amount
    public static final int REAL_AMOUNT = 49;

    public static final int REAL_AMOUNT_CLIENT = 50;

    public static final int REAL_AMOUNT_PARTNER = 51;

    // eight extra columns to show asset allocation
    public static final int AA_CASH = 52;

    public static final int AA_FIXED_INTEREST = 53;

    public static final int AA_AUST_SHARES = 54;

    public static final int AA_INTNL_SHARES = 55;

    public static final int AA_PROPERTY = 56;

    public static final int AA_OTHER = 57;

    public static final int AA_TOTAL = 58;

    public static final int AA_INCLUDE = 59;

    // Combined (Income / Growth)
    public static final int TOTAL_RETURN = 60;

    // generated regular financials
    public static final int GROSS_INCOME = 61;

    public static final int FINANCIAL_SERVICE = 62;

    // generated regular financials
    public static final int GROSS_INCOME_CLIENT = 63;

    public static final int GROSS_INCOME_PARTNER = 64;

    public static final int UNFRANKED_INCOME = 65;

    public static final int UNFRANKED_INCOME_CLIENT = 66;

    public static final int UNFRANKED_INCOME_PARTNER = 67;

    // TOTAL COLUMNS COUNT
    public static final int COUNT = 68;

}
