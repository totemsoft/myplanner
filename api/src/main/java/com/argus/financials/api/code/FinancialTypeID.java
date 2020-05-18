/*
 * FinancialObjectTypeID.java
 *
 * Created on August 8, 2002, 9:11 AM
 */

package com.argus.financials.api.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface FinancialTypeID {

    // INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 0, '[None]', 0 );
    public static final int UNDEFINED = 0;

    // INSERT INTO FinancialCode ( FinancialCodeID, FinancialTypeID, FinancialCodeDesc ) VALUES ( 0, 0, '[None]' );

    /***************************************************************************
     * CashAsset = 10
     **************************************************************************/
    // 1 Savings/Cheque Account 10
    public static final int SAVINGS_CHEQUE_ACCOUNT = 1;

    // 2 Mortgage Account 10
    public static final int MORTGAGE_ACCOUNT = 2;

    // 3 Term Deposit 10
    public static final int TERM_DEPOSIT = 3;

    // 1003 Fixed Deposits 10
    public static final int FIXED_DEPOSITS = TERM_DEPOSIT;

    /***************************************************************************
     * InvestmentAsset == 11
     **************************************************************************/
    // 4 Shares (Listed Shares) 11
    public static final int INVESTMENT_LISTED_SHARES = 4;

    // 5 Managed Funds (Listed Unit Trust) 11
    public static final int INVESTMENT_LISTED_UNIT_TRUST = 5;

    // 6 Other 11
    public static final int INVESTMENT_OTHER = 6;// INVESTMENT_LISTED_SHARES;

    // 7 Investment Property 11
    public static final int INVESTMENT_PROPERTY = 7;

    // 1005 Shares (Australian) 11
    public static final int INVESTMENT_SHARES_AUSTRALIAN = INVESTMENT_LISTED_SHARES;

    // 1006 Shares (Imputation) 11
    // 1007 Shares (Growth) 11
    // 1008 Listed Unit Trusts 11
    public static final int INVESTMENT_LISTED_UNIT_TRUSTS = INVESTMENT_LISTED_UNIT_TRUST;

    /***************************************************************************
     * ASSET_PERSONAL = 12
     **************************************************************************/
    // 13 Family Home
    public static final Integer PERSONAL_FAMILY_HOME = new Integer(13);

    // 14 Motor Vehicle
    public static final Integer PERSONAL_MOTOR_VEHICLE = new Integer(14);

    // 15 Boat
    public static final Integer PERSONAL_BOAT = new Integer(15);

    // 16 Caravan
    public static final Integer PERSONAL_CARAVAN = new Integer(16);

    // 19 Home Contents
    public static final Integer PERSONAL_HOME_CONTENTS = new Integer(19);

    // 20 Holiday Home
    public static final Integer PERSONAL_HOLIDAY_HOME = new Integer(20);

    // 83 Jewellery
    public static final Integer PERSONAL_JEWELLERY = new Integer(83);

    // 84 Motor Bike
    public static final Integer PERSONAL_MOTOR_BIKE = new Integer(84);

    // 85 Sporting Equipment
    public static final Integer PERSONAL_SPORTING_EQUIPMENT = new Integer(85);

    // 86 Tools and Machinery
    public static final Integer PERSONAL_TOOLS = new Integer(86);

    // 87 Other
    public static final Integer PERSONAL_OTHER = new Integer(87);

    /***************************************************************************
     * SuperannuationAsset = 13
     **************************************************************************/
    // 21 Superannuation Account
    public static final int SUPERANNUATION_ACCOUNT = 21;

    // 2001 Bank-Build Societies/Cash Management
    public static final int SUPERANNUATION_LISTED_SHARES = INVESTMENT_LISTED_SHARES;

    // 2002 Fixed Interest - Australian Short Term
    public static final int SUPERANNUATION_LISTED_UNIT_TRUST = INVESTMENT_LISTED_UNIT_TRUST;

    // 2003 Income Funds
    public static final int SUPERANNUATION_INCOME_FUNDS = 2003;

    // 2004 Mortgage Funds
    public static final int SUPERANNUATION_SHARES_AUSTRALIAN = INVESTMENT_LISTED_SHARES;

    // 2005 Property Securities Funds
    public static final int SUPERANNUATION_LISTED_UNIT_TRUSTS = INVESTMENT_LISTED_UNIT_TRUST;

    public static final int SUPERANNUATION_UNKNOWN = SUPERANNUATION_ACCOUNT;// SUPERANNUATION_INCOME_FUNDS;

    /***************************************************************************
     * Income Stream = 19
     **************************************************************************/
    // 22 Pension Account
    public static final int INCOMESTREAM_PENSION_ACCOUNT = 22;

    // 23 Annuity Policy
    public static final int INCOMESTREAM_ANNUITY_POLICY = 23;

    public static final int INCOMESTREAM_UNKNOWN = INCOMESTREAM_PENSION_ACCOUNT;

    /***************************************************************************
     * Liability = 16
     **************************************************************************/
    // Liability Financial Types
    // (51, 'Credit Card', 16);
    public static final Integer LIABILITY_CREDIT_CARD = new Integer(51);

    // (52, 'Personal Loan', 16);
    public static final Integer LIABILITY_PERSONAL_LOAN = new Integer(52);

    // (53, 'Mortgage', 16);
    public static final Integer LIABILITY_MORTGAGE = new Integer(52);

    // 54 Car Loan
    public static final Integer LIABILITY_CAR_LOAN = new Integer(54);

    // 55 Lease
    public static final Integer LIABILITY_LEASE = new Integer(55);

    // 62 Other
    public static final Integer LIABILITY_OTHER = new Integer(62);

    /***************************************************************************
     * REGULAR_INCOME = 14
     **************************************************************************/
    // 24 Salary & Wages
    public static final Integer INCOME_SALARY = new Integer(24);

    // 25 Investment Income
    public static final Integer INCOME_INVESTMENT = new Integer(25);

    // 26 Social Security
    public static final Integer INCOME_SOCIAL_SECURITY = new Integer(26);

    // 27 Retirement Income
    public static final Integer INCOME_RETIREMENT = new Integer(27);

    // 28 Other Income
    public static final Integer INCOME_OTHER = new Integer(28);

    // 63 Other Tax Free Income
    public static final Integer INCOME_OTHER_TAX_FREE = new Integer(63);

    /***************************************************************************
     * REGULAR_EXPENSE = 15
     **************************************************************************/
    // 29 General
    public static final Integer EXPENSE_GENERAL = new Integer(29);

    // 30 Savings/Investment
    public static final Integer EXPENSE_SAVING_INVESTMENT = new Integer(30);

    // 31 Holiday
    public static final Integer EXPENSE_HOLIDAY = new Integer(31);

    // 32 Education
    public static final Integer EXPENSE_EDUCATION = new Integer(32);

    // 33 Other
    public static final Integer EXPENSE_OTHER = new Integer(33);

    /***************************************************************************
     * TAX_OFFSET = 18
     **************************************************************************/
    // 88 Salary and wages income type
    public static final Integer TAXOFFSET_SALARY = new Integer(88);

    // 89 Imputation Credits
    public static final Integer TAXOFFSET_IMPUTATION_CREDIT = new Integer(89);

    // 90 Other
    public static final Integer TAXOFFSET_OTHER = new Integer(90);

    // 91 Early payment interest credit
    public static final Integer TAXOFFSET_EARLY_PAY = new Integer(91);

    // 92 Spouse/child-housekeeper/housekeeper
    public static final Integer TAXOFFSET_HOUSEKEEPER = new Integer(92);

    // 93 Senior Australian (Low income aged persons)
    public static final Integer TAXOFFSET_SENIOR_AUSSIE = new Integer(93);

    // 94 Superannuation
    public static final Integer TAXOFFSET_SUPER = new Integer(94);

    // 95 30% private health insurance
    public static final Integer TAXOFFSET_PRIVATE_HEALTH = new Integer(95);

    // 96 Zone or overseas forces
    public static final Integer TAXOFFSET_ZONE_OVERSEAS = new Integer(96);

    // 97 Medical expenses
    public static final Integer TAXOFFSET_MEDICAL = new Integer(97);

    // 98 Parent/parent in law/invalid relative
    public static final Integer TAXOFFSET_PARENT = new Integer(98);

    // 99 Landcare and water facility
    public static final Integer TAXOFFSET_LANDCARE = new Integer(99);

    // 100 Goverment loan interest
    public static final Integer TAXOFFSET_GOV_LOAN = new Integer(100);

    // 101 Heritage conservation
    public static final Integer TAXOFFSET_HERITAGE = new Integer(101);

    // 102 Beneficiary or pensioner
    public static final Integer TAXOFFSET_BENEFICIARY = new Integer(102);

    // 103 Short term life assurance policies
    public static final Integer TAXOFFSET_LIFE_POLICY = new Integer(103);

    // 104 Lump Sum payments and ETP
    public static final Integer TAXOFFSET_LUMP_SUM = new Integer(104);

    // 105 Foreign tax credit used (Available)
    public static final Integer TAXOFFSET_FOREIGN = new Integer(105);

    // 106 Low income
    public static final Integer TAXOFFSET_LOW_INCOME = new Integer(106);

    // 107 Share of credit for tax paid by trustee
    public static final Integer TAXOFFSET_CREDIT_TAX_PAID = new Integer(107);

    // 108 Baby bonus claim
    public static final Integer TAXOFFSET_BABY_BONUS = new Integer(108);

    //
    public static final Integer[] TAXOFFSET_OTHERS = new Integer[] {
            TAXOFFSET_SALARY,
            // TAXOFFSET_IMPUTATION_CREDIT,
            TAXOFFSET_OTHER,
            TAXOFFSET_EARLY_PAY,
            TAXOFFSET_HOUSEKEEPER,
            TAXOFFSET_SENIOR_AUSSIE,
            // TAXOFFSET_SUPER,
            TAXOFFSET_PRIVATE_HEALTH, TAXOFFSET_ZONE_OVERSEAS,
            TAXOFFSET_MEDICAL, TAXOFFSET_PARENT, TAXOFFSET_LANDCARE,
            TAXOFFSET_GOV_LOAN, TAXOFFSET_HERITAGE, TAXOFFSET_BENEFICIARY,
            TAXOFFSET_LIFE_POLICY,
            // TAXOFFSET_LUMP_SUM,
            TAXOFFSET_FOREIGN,
            // TAXOFFSET_LOW_INCOME,
            TAXOFFSET_CREDIT_TAX_PAID, TAXOFFSET_BABY_BONUS, };

    /***************************************************************************
     * Descriptions
     **************************************************************************/
    public static final String STRING_LISTED_UNIT_TRUST = "Listed Unit Trust";

    public static final String STRING_LISTED_SHARES = "Listed Shares";

}
