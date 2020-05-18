package com.argus.financials.api.code;

/**
 * @see FinancialTypeID
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public enum FinancialTypeEnum {

    UNDEFINED(0),
    /***************************************************************************
     * CashAsset = 10
     **************************************************************************/
    // 1 Savings/Cheque Account 10
    SAVINGS_CHEQUE_ACCOUNT(1),
    // 2 Mortgage Account 10
    MORTGAGE_ACCOUNT(2),
    // 3 Term Deposit 10
    TERM_DEPOSIT(3),
    // 1003 Fixed Deposits 10
    FIXED_DEPOSITS(TERM_DEPOSIT.id),
    /***************************************************************************
     * InvestmentAsset == 11
     **************************************************************************/
    // 4 Shares (Listed Shares) 11
    INVESTMENT_LISTED_SHARES(4, "Listed Shares"),
    // 5 Managed Funds (Listed Unit Trust) 11
    INVESTMENT_LISTED_UNIT_TRUST(5, "Listed Unit Trust"),
    // 6 Other 11
    INVESTMENT_OTHER(6),// INVESTMENT_LISTED_SHARES
    // 7 Investment Property 11
    INVESTMENT_PROPERTY(7),
    // 1005 Shares (Australian) 11
    INVESTMENT_SHARES_AUSTRALIAN(INVESTMENT_LISTED_SHARES.id),
    // 1006 Shares (Imputation) 11
    // 1007 Shares (Growth) 11
    // 1008 Listed Unit Trusts 11
    INVESTMENT_LISTED_UNIT_TRUSTS(INVESTMENT_LISTED_UNIT_TRUST.id),
    /***************************************************************************
     * ASSET_PERSONAL = 12
     **************************************************************************/
    /***************************************************************************
     * SuperannuationAsset = 13
     **************************************************************************/
    // 21 Superannuation Account
    SUPERANNUATION_ACCOUNT(21),
    // 2001 Bank-Build Societies/Cash Management
    SUPERANNUATION_LISTED_SHARES(INVESTMENT_LISTED_SHARES.id, INVESTMENT_LISTED_SHARES.desc),
    // 2002 Fixed Interest - Australian Short Term
    SUPERANNUATION_LISTED_UNIT_TRUST(INVESTMENT_LISTED_UNIT_TRUST.id, INVESTMENT_LISTED_UNIT_TRUST.desc),
    // 2003 Income Funds
    SUPERANNUATION_INCOME_FUNDS(2003),
    // 2004 Mortgage Funds
    SUPERANNUATION_SHARES_AUSTRALIAN(INVESTMENT_LISTED_SHARES.id),
    // 2005 Property Securities Funds
    SUPERANNUATION_LISTED_UNIT_TRUSTS(INVESTMENT_LISTED_UNIT_TRUST.id),
    SUPERANNUATION_UNKNOWN(SUPERANNUATION_ACCOUNT.id),// SUPERANNUATION_INCOME_FUNDS;
    /***************************************************************************
     * REGULAR_INCOME = 14
     **************************************************************************/
    // 24 Salary & Wages
    INCOME_SALARY(24),
    // 25 Investment Income
    INCOME_INVESTMENT(25),
    // 26 Social Security
    INCOME_SOCIAL_SECURITY(26),
    // 27 Retirement Income
    INCOME_RETIREMENT(27),
    // 28 Other Income
    INCOME_OTHER(28),
    // 63 Other Tax Free Income
    INCOME_OTHER_TAX_FREE(63),
    /***************************************************************************
     * REGULAR_EXPENSE = 15
     **************************************************************************/
    // 29 General
    EXPENSE_GENERAL(29),
    // 30 Savings/Investment
    EXPENSE_SAVING_INVESTMENT(30),
    // 31 Holiday
    EXPENSE_HOLIDAY(31),
    // 32 Education
    EXPENSE_EDUCATION(32),
    // 33 Other
    EXPENSE_OTHER(33),
    /***************************************************************************
     * TAX_OFFSET = 18
     **************************************************************************/
    // 88 Salary and wages income type
    TAXOFFSET_SALARY(88),
    // 89 Imputation Credits
    TAXOFFSET_IMPUTATION_CREDIT(89),
    // 90 Other
    TAXOFFSET_OTHER(90),
    // 91 Early payment interest credit
    TAXOFFSET_EARLY_PAY(91),
    // 92 Spouse/child-housekeeper/housekeeper
    TAXOFFSET_HOUSEKEEPER(92),
    // 93 Senior Australian (Low income aged persons)
    TAXOFFSET_SENIOR_AUSSIE(93),
    // 94 Superannuation
    TAXOFFSET_SUPER(94),
    // 95 30% private health insurance
    TAXOFFSET_PRIVATE_HEALTH(95),
    // 96 Zone or overseas forces
    TAXOFFSET_ZONE_OVERSEAS(96),
    // 97 Medical expenses
    TAXOFFSET_MEDICAL(97),
    // 98 Parent/parent in law/invalid relative
    TAXOFFSET_PARENT(98),
    // 99 Landcare and water facility
    TAXOFFSET_LANDCARE(99),
    // 100 Goverment loan interest
    TAXOFFSET_GOV_LOAN(100),
    // 101 Heritage conservation
    TAXOFFSET_HERITAGE(101),
    // 102 Beneficiary or pensioner
    TAXOFFSET_BENEFICIARY(102),
    // 103 Short term life assurance policies
    TAXOFFSET_LIFE_POLICY(103),
    // 104 Lump Sum payments and ETP
    TAXOFFSET_LUMP_SUM(104),
    // 105 Foreign tax credit used (Available)
    TAXOFFSET_FOREIGN(105),
    // 106 Low income
    TAXOFFSET_LOW_INCOME(106),
    // 107 Share of credit for tax paid by trustee
    TAXOFFSET_CREDIT_TAX_PAID(107),
    // 108 Baby bonus claim
    TAXOFFSET_BABY_BONUS(108),
    ;

    public static final FinancialTypeEnum[] TAXOFFSET_OTHERS = new FinancialTypeEnum[] {
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
        TAXOFFSET_CREDIT_TAX_PAID, TAXOFFSET_BABY_BONUS,
    };

    private final int id;
    private final String desc;

    private FinancialTypeEnum(int id) {
        this.id = id;
        this.desc = null;
    }

    private FinancialTypeEnum(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public static final boolean isTermDeposit(Integer financialTypeId) {
        return financialTypeId != null && TERM_DEPOSIT.id == financialTypeId;
    }

}