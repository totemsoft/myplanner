/*
 * DocumentNames.java
 *
 * Created on 13 February 2002, 11:32
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 * 
 * Names: used in DocumentListener
 * 
 */

public interface DocumentNames {

    //
    public static final String READY = "!!!Ready!!!";

    public static final String WIZARD = "wizard";

    // Property Group/Name
    public static final String GROUP_NAME = "Group";

    public static final String NAME = "Name";

    public static final char DELIM = '=';

    /*
     * getAccessibleContext().setAccessibleName( XXX )
     */

    // MoneyCalc
    public static final String DOB = "DOB"; // client

    public static final String SEX_CODE = "Sex Code"; // client

    public static final String SEX_CODE_MALE = "Sex Code Male"; // client

    public static final String SEX_CODE_FEMALE = "Sex Code Female";

    public static final String TAX_RATE = "Tax Rate"; // client

    public static final String INDEXED = "Indexed";

    public static final String INDEX_RATE = "Index Rate";

    public static final String INITIAL_VALUE = "Initial Value";

    // AssetCalc
    public static final String DELTA = "Delta";

    public static final String ENTRY_FEES = "Entry Fees";

    public static final String REVIEW_FEES = "Review Fees";

    public static final String INV_STRATEGY = "Investment Strategy";

    public static final String CAPITAL_GROWTH_RATE = "Capital Growth Rate";

    public static final String INCOME_RATE = "Income Rate";

    // AssetGrowth
    public static final String INV_STRATEGY_AG = "Investment Strategy Growth";

    public static final String SUR_CHARGE = "Surcharge";

    public static final String ASSET_CODE_ID = "Asset Code";

    public static final String ASSET_CURRENT_VALUE = "Current Value";

    public static final String ASSET_OWNER = "Owner";

    public static final String ASSET_CONTRIBUTION = "Contribution"; // = DELTA

    public static final String ASSET_INCOME_RATE = "Income Rate";

    public static final String ASSET_GROWTH_RATE = "Growth Rate";

    // AssetGrowthLinked

    // AssetSpend
    // BEGIN: BUG FIX 614 - 12/11/2002
    // by shibaevv
    // Added a new constant for AssetSpend
    public static final String CALCULATION_TYPE_SINGLE = "Single";

    // END: BUG FIX 614 - 12/11/2002

    public static final String TARGET_AGE = "Target Age"; // client

    public static final String INV_STRATEGY_AS = "Investment Strategy Retire";

    public static final String DOB_PARTNER = "Partner DOB";

    public static final String TARGET_AGE_PARTNER = "Partner Target Age";

    public static final String TAX_RATE_PARTNER = "Partner Tax Rate";

    public static final String YEARS_PROJECT_RETIRE_INCOME = "Years Project Retire Income";

    public static final String REQUIRED_INCOME_PART = "Required Income Partial"; // client
                                                                                    // or
                                                                                    // partner

    public static final String REQUIRED_INCOME_FULL = "Required Income Full"; // client
                                                                                // and
                                                                                // partner

    public static final String ATO_UPP = "ATO UPP";

    public static final String DSS_UPP = "DSS UPP";

    public static final String REBATE = "Rebate";

    public static final String REQUIRED_RESIDUAL = "Required Residual";

    public static final String INCLUDE_DSS = "Include DSS";

    // SnapshotCalc
    public static final String GOAL_FIN_INDEPENDENCE = "Financial Independence";

    public static final String GOAL_RETIREMENT = "Retirement";

    // ETPRolloverView
    public static final String ETP_TYPE = "ETP Type";

    public static final String ELIGIBLE_SERVICE_DATE = "Eligible Service Date";

    public static final String CALCULATION_DATE = "Calculation Date";

    public static final String TOTAL_ETP_AMOUNT = "Total ETP Amount";

    public static final String ENCASHMENT_CODE_YES = "Encashment Code Yes";

    public static final String ENCASHMENT_CODE_NO = "Encashment Code No";

    public static final String NON_QUALIFYING = "Non-Qualifying";

    public static final String CGT_EXEMPT = "CGT Exempt";

    public static final String UNDEDUCTED = "Undeducted";

    public static final String CONCESSIONAL = "Concessional";

    public static final String POST_JUNE_94_INVALIDITY = "Post June 94 Invalidity";

    public static final String RBL_AMOUNT = "RBL Amounts already counted";

    public static final String IS_CLIENT = "Is ClientView";

    public static final String IS_PARTNER = "Is Partner";

    // ETPRolloverViewNew
    public static final String PRE = "Pre";

    public static final String POST_TAXED = "Post Taxed";

    public static final String POST_JUNE_1983_UNTAXED = "Post June 1983 Untaxed";

    public static final String EXCESS = "Excess";

    public static final String POST_061983_THRESHOLD_USED = "Post June 1983 Threshold Used";

    public static final String PARTIAL_WITHDRAW_OPTION = "Partial Withdraw Option";

    public static final String WITHDRAW_ALL_OPTION = "Withdraw All Option";

    public static final String ROLLOVER_ALL_OPTION = "Rollover All Option";

    public static final String WITHDRAW_UP_TO_THRESHOLD_OPTION = "Withdraw Up To Threshold Option";

    public static final String NO_OPTION = "No Option";

    public static final String NO_OPTION_P = "No Option Partner";

    public static final String ROLLOVER_TO_ALLOCATED_PENSION = "Rollover To Allocated Pension";

    public static final String ROLLOVER_AND_RECONTRIBUTE_TO_ALLOCATED_PENSION = "Rollover & Recontribute To Allocated Pension";

    // ETPPartialWithdrawal
    public static final String ETP_PARTIAL_WITHDRAWAL_ROLLOVER = "ETP Partial Withdrawal Rollover";

    public static final String ETP_PARTIAL_WITHDRAWAL_UNDEDUCTED = "ETP Partial Withdrawal Undeducted";

    public static final String ETP_PARTIAL_WITHDRAWAL_CGT_EXEMPT = "ETP Partial Withdrawal CGT Exempt";

    public static final String ETP_PARTIAL_WITHDRAWAL_EXCESS = "ETP Partial Withdrawal Excess";

    public static final String ETP_PARTIAL_WITHDRAWAL_CONCESSIONAL = "ETP Partial Withdrawal Concessional";

    public static final String ETP_PARTIAL_WITHDRAWAL_INVALIDITY = "ETP Partial Withdrawal Invalidity";

    // AssetAllocationView

    // AllocatedPensionView
    public static final String REQUIRED_PAYMENT_AMOUNT = "Required Payment Amount";

    public static final String PENSION_REVERSION_OPTION = "Pension Reversion Option";

    public static final String PARTNER_SEX_CODE = "Partner Sex Code";

    public static final String PARTNER_SEX_CODE_MALE = "Partner Sex Code Male";

    public static final String PARTNER_SEX_CODE_FEMALE = "Partner Sex Code Female";

    public static final String REQUIRED_PAYMENT_TYPE = "Required Payment Type";

    public static final String REQUIRED_FREQUENCY = "Required Frequency";

    public static final String PRE_071983_CODE_YES = "Pre 071983 Code Yes";

    public static final String PRE_071983_CODE_NO = "Pre 071983 Code No";

    public static final String INVALIDITY_CODE_YES = "Invalidity Code Yes";

    public static final String INVALIDITY_CODE_NO = "Invalidity Code No";

    // AllocatedPensionViewNew
    public static final String PENSION_START_DATE = "Pension Start Date";

    public static final String FIRST_PAYMENT_DATE = "First Payment Date";

    public static final String RO_NO = "R/O from Pre 1994 Income Stream No";

    public static final String RO_YES = "R/O from Pre 1994 Income Stream Yes";

    public static final String GENERAL_TAX_EXEMPTION = "General Tax Exemption";

    public static final String DEATH_BENEFIT = "Death Benefit";

    public static final String PENSION_FREQUENCY = "Pension Frequency";

    public static final String ENTRY_FEE = "Entry Fee";

    public static final String ONGOING_FEE = "Ongoing Fee";

    public static final String PROJECT_TO_AGE = "Project To Age";

    public static final String TOTAL = "Total";

    public static final String NET_EARNING_RATE = "Net Earning Rate";

    public static final String SELECTED_ANNUAL_PENSION = "Selected Annual Pension";

    public static final String OTHER_VALUE = "Other Value";

    public static final String ANNUAL_INCREASE_PAYMENTS = "Annual Increase Payments";

    public static final String RELATIONSHIP = "Relationship";

    public static final String SURNAME_PARTNER = "Partner Surname";

    public static final String OTHER_NAME_PARTNER = "Partner Other Name";

    // GearingView
    public static final String GEARING_TYPE = "Gearing Type";

    public static final String CLIENT_START_LSR_RATE = "ClientView Start LSR %";

    public static final String MAXIMUM_START_LSR_RATE = "Maximum Start LSR %";

    public static final String CREDIT_LIMIT_AMOUNT = "Credit Limit";

    public static final String LOAN_INTEREST_RATE = "Loan Interest Rate %";

    public static final String REGULAR_CONTRIBUTION_AMOUNT = "Total Monthly Contribution";

    public static final String INITIAL_CONTRIBUTION_AMOUNT = "Total Initial Contribution";

    public static final String FRANKING_CREDIT_RATE = "Franking Credit %";

    public static final String TAX_FREE_DEFFERED_RATE = "Tax Free/Deffered %";

    public static final String CAPITAL_GAINS_RATE = "Capital Gains %";

    // GearingView2
    public static final String INITIAL_INVESTOR = "Investor Deposit";

    public static final String INITIAL_LOAN = "Initial Loan";

    public static final String REGULAR_INVESTOR_CONTRIBUTION = "Monthly Investor Contribution";

    public static final String REGULAR_LOAN_ADVANCE = "Monthly Loan Advance";

    public static final String START_DATE = "Contribute from Date";

    public static final String END_DATE = "Contribute till Date";

    public static final String YEARS_PROJECT = "Years to project";

    public static final String REINVEST_INCOME = "Reinvest Income";

    public static final String ADD_INTEREST = "Add Interest";

    public static final String INCOME_FREQUENCY = "Income Frequency";

    public static final String OTHER_TAXABLE_INCOME = "Other Taxable Income";

    public static final String FLAT_CAPITAL_GROWTH_RATES = "Flat Capital Growth Rates";

    public static final String VARIABLE_CAPITAL_GROWTH_RATES = "Variable Capital Growth Rates";

    // RADIO BUTTONS SELECTION GROUP
    public static final String INVESTMENT_VALUE = "Investment Value";

    public static final String POTENTIAL_OUTCOME = "Potential Outcome";

    public static final String CAPITAL_GROWTH_RATES = "Capital Growth Rates";

    // RADIO BUTTONS SELECTION GROUP
    public static final String INVESTMENT_SUMMARY = "Investment Summary";

    public static final String ANNUAL_CASHFLOW = "Annual Cashflow";

    public static final String MONTHLY_CASHFLOW = "Monthly Cashflow";

    // DSSView
    public static final String CLIENT_NAME = " ClientView Name";

    public static final String MARITAL_STATUS = "Marital Status";

    public static final String BENEFIT_TYPE = "Benefit Type";

    public static final String HOME_OWNER = "Home Owner";

    public static final String NO_CHILDREN = "No Children";

    public static final String SHARER = "Sharer";

    public static final String NAME_PARTNER = "Partner Name";

    public static final String CALCULATION_DATE_PARTNER = "Partner Calculation Date";

    public static final String BENEFIT_TYPE_PARTNER = "Partner Benefit Type";

    public static final String RENT = " rent fortnightly ";

    public static final String CHILDREN_AMOUNT = " children amount ";

    // public static final String ELIGIBLE_FOR_PA_PARTNER = "Partner Eligible
    // For Pharmacuetical Allowance";

    public static final String SAVINGS_A = "Savings Assets";

    public static final String MANAGED_FUNDS_A = "Managed Fund Assets";

    public static final String SHARES_A = "Shares Assets";

    public static final String BONDS_A = "Bonds Assets";

    public static final String FIXED_INTEREST_A = "Fixed Interest Assets";

    public static final String HOME_CONTENTS_A = "Home Contents Assets";

    public static final String CARS_ETC_A = "Cars ETC Assets";

    public static final String PROPERTY_A = "Property Assets";

    public static final String GIFTS_A = "Gifts Assets";

    public static final String LOANS_A = "Loans Assets";

    public static final String SUPERANNUATION_A = "Superannuation Assets";

    public static final String COMPLYING_PENSION_A = " Complying Pension Assets ";

    public static final String COMPLYING_PENSION_I = " Complying Pension Income ";

    public static final String ALLOCATED_PENSION_A = " Allocated Pension Assets ";

    public static final String ALLOCATED_PENSION_I = " Allocated Pension Income ";

    public static final String PROPERTY_I = "Property Income";

    public static final String GIFTS_I = "Gifts Income";

    public static final String LOANS_I = "Loans Income";

    /*
     * DSSView: BEGIN: partner details fields
     */
    public static final String SAVINGS_A_PARTNER = "Partner Savings Assets";

    public static final String MANAGED_FUNDS_A_PARTNER = "Partner Managed Fund Assets";

    public static final String SHARES_A_PARTNER = "Partner Shares Assets";

    public static final String BONDS_A_PARTNER = "Partner Bonds Assets";

    public static final String FIXED_INTEREST_A_PARTNER = "Partner Fixed Interest Assets";

    public static final String HOME_CONTENTS_A_PARTNER = "Partner Home Contents Assets";

    public static final String CARS_ETC_A_PARTNER = "Partner Cars ETC Assets";

    public static final String PROPERTY_A_PARTNER = "Partner Property Assets";

    public static final String GIFTS_A_PARTNER = "Partner Gifts Assets";

    public static final String LOANS_A_PARTNER = "Partner Loans Assets";

    public static final String SUPERANNUATION_A_PARTNER = "Partner Superannuation Assets";

    public static final String COMPLYING_PENSION_A_PARTNER = "Partner Complying Pension Assets";

    public static final String COMPLYING_PENSION_I_PARTNER = "Partner Complying Pension Income";

    public static final String ALLOCATED_PENSION_A_PARTNER = "Partner Allocated Pension Assets";

    public static final String ALLOCATED_PENSION_I_PARTNER = "Partner Allocated Pension Income";

    public static final String PROPERTY_I_PARTNER = "Partner Property Income";

    public static final String GIFTS_I_PARTNER = "Partner Gifts Income";

    public static final String LOANS_I_PARTNER = "Partner Loans Income";

    /*
     * DSSView: END: partner details fields
     */

    public static final String CLIENT_SALARY_I = "ClientView Salary Income";

    public static final String PARTNER_SALARY_I = "Partner Salary Income";

    // DSSViewNew
    public static final int CASH_ASSET = 0;

    public static final int MAN_FUNDS = 1;

    public static final int SHARES = 2;

    public static final int OTHER_INV = 3;

    public static final int DEEMED_SUP = 4;

    public static final int STA = 5;

    public static final int AP = 0;

    public static final int ANN = 1;

    public static final int COM_ANN = 2;

    public static final int INV_PRO = 3;

    public static final int HC = 0;

    public static final int VBC = 1;

    public static final int HH = 2;

    public static final int OTHER_PER = 3;

    public static final int GA = 4;

    public static final int LOANS = 5;

    public static final int SAL = 0;

    public static final int OTHER = 1;

    public static final String CASH_ASSET_AA = "Cash Asset AA";

    public static final String CASH_ASSET_TA = "Cash Asset TA";

    public static final String CASH_ASSET_TA_C = "Cash Asset TA ClientView";

    public static final String CASH_ASSET_AA_C = "Cash Asset AA ClientView";

    public static final String CASH_ASSET_AA_P = "Cash Asset AA Partner";

    public static final String CASH_ASSET_AI = "Cash Asset AI";

    public static final String CASH_ASSET_AI_C = "Cash Asset AI ClientView";

    public static final String CASH_ASSET_AI_P = "Cash Asset AI Partner";

    public static final String MAN_FUNDS_AA = "Managed Funds AA";

    public static final String MAN_FUNDS_TA = "Managed Funds TA";

    public static final String MAN_FUNDS_TA_C = "Managed Funds TA ClientView";

    public static final String MAN_FUNDS_AA_C = "Managed Funds AA ClientView";

    public static final String MAN_FUNDS_AA_P = "Managed Funds AA Partner";

    public static final String MAN_FUNDS_AI = "Managed Funds AI";

    public static final String MAN_FUNDS_AI_C = "Managed Funds AI ClientView";

    public static final String MAN_FUNDS_AI_P = "Managed Funds AI Partner";

    public static final String SHARES_AA = "Shares AA";

    public static final String SHARES_TA = "Shares TA";

    public static final String SHARES_TA_C = "Shares TA ClientView";

    public static final String SHARES_AA_C = "Shares AA ClientView";

    public static final String SHARES_AA_P = "Shares AA Partner";

    public static final String SHARES_AI = "Shares AI";

    public static final String SHARES_AI_C = "Shares AI ClientView";

    public static final String SHARES_AI_P = "Shares AI Partner";

    public static final String OTHER_INV_AA = "Other Inv AA";

    public static final String OTHER_INV_TA = "Other Inv TA";

    public static final String OTHER_INV_TA_C = "Other Inv TA ClientView";

    public static final String OTHER_INV_AA_C = "Other Inv AA ClientView";

    public static final String OTHER_INV_AA_P = "Other Inv AA Partner";

    public static final String OTHER_INV_AI = "Other Inv AI";

    public static final String OTHER_INV_AI_C = "Other Inv AI ClientView";

    public static final String OTHER_INV_AI_P = "Other Inv AI Partner";

    public static final String DEEMED_SUP_AA = "Deemed Super AA";

    public static final String DEEMED_SUP_AA_C = "Deemed Super AA ClientView";

    public static final String DEEMED_SUP_AA_P = "Deemed Super AA Partner";

    public static final String DEEMED_SUP_TA = "Deemed Super TA";

    public static final String DEEMED_SUP_TA_C = "Deemed Super TA ClientView";

    public static final String DEEMED_SUP_AI = "Deemed Super AI";

    public static final String DEEMED_SUP_AI_C = "Deemed Super AI ClientView";

    public static final String DEEMED_SUP_AI_P = "Deemed Super AI Partner";

    public static final String STA_AA = "STA AA";

    public static final String STA_AA_C = "STA AA ClientView";

    public static final String STA_AA_P = "STA AA Partner";

    public static final String STA_TA = "STA TA";

    public static final String STA_TA_C = "STA TA ClientView";

    public static final String STA_AI = "STA AI";

    public static final String STA_AI_C = "STA AI ClientView";

    public static final String STA_AI_P = "STA AI Partner";

    public static final String TDI = "TDI";

    public static final String TDI_C = "TDI ClientView";

    public static final String TDI_P = "TDI Partner";

    public static final String AP_AA = "AP AA";

    public static final String AP_AA_C = "AP AA ClientView";

    public static final String AP_AA_P = "AP AA Partner";

    public static final String AP_TA = "AP TA";

    public static final String AP_TA_C = "AP TA ClientView";

    public static final String AP_AI = "AP AI";

    public static final String AP_TI = "AP TI";

    public static final String AP_AI_C = "AP AI ClientView";

    public static final String AP_TI_C = "AP TI ClientView";

    public static final String AP_AI_P = "AP AI Partner";

    public static final String AP_TI_P = "AP TI Partner";

    public static final String ANN_AA = "Annuities AA";

    public static final String ANN_AA_C = "Annuities AA ClientView";

    public static final String ANN_AA_P = "Annuities AA Partner";

    public static final String ANN_TA = "Annuities TA";

    public static final String ANN_TA_C = "Annuities TA ClientView";

    public static final String ANN_AI = "Annuities AI";

    public static final String ANN_TI = "Annuities TI";

    public static final String ANN_AI_C = "Annuities AI ClientView";

    public static final String ANN_TI_C = "Annuities TI ClientView";

    public static final String ANN_AI_P = "Annuities AI Partner";

    public static final String ANN_TI_P = "Annuities TI Partner";

    public static final String COM_ANN_AA = "Com Ann AA";

    public static final String COM_ANN_AA_C = "Com Ann AA ClientView";

    public static final String COM_ANN_AA_P = "Com Ann AA Partner";

    public static final String COM_ANN_TA = "Com Ann TA";

    public static final String COM_ANN_TA_C = "Com Ann TA ClientView";

    public static final String COM_ANN_AI = "Com Ann AI";

    public static final String COM_ANN_TI = "Com Ann TI";

    public static final String COM_ANN_AI_C = "Com Ann AI ClientView";

    public static final String COM_ANN_TI_C = "Com Ann TI ClientView";

    public static final String COM_ANN_AI_P = "Com Ann AI Partner";

    public static final String COM_ANN_TI_P = "Com Ann TI Partner";

    public static final String INV_PRO_AA = "Inv Pro AA";

    public static final String INV_PRO_AA_C = "Inv Pro AA ClientView";

    public static final String INV_PRO_AA_P = "Inv Pro AA Partner";

    public static final String INV_PRO_TA = "Inv Pro TA";

    public static final String INV_PRO_TA_C = "Inv Pro TA ClientView";

    public static final String INV_PRO_AI = "Inv Pro AI";

    public static final String INV_PRO_TI = "Inv Pro TI";

    public static final String INV_PRO_AI_C = "Inv Pro AI ClientView";

    public static final String INV_PRO_TI_C = "Inv Pro TI ClientView";

    public static final String INV_PRO_AI_P = "Inv Pro AI Partner";

    public static final String INV_PRO_TI_P = "Inv Pro TI Partner";

    public static final String HC_AA = "HC AA";

    public static final String HC_AA_C = "HC AA ClientView";

    public static final String HC_AA_P = "HC AA Partner";

    public static final String HC_TA = "HC TA";

    public static final String HC_TA_C = "HC TA ClientView";

    public static final String VBC_AA = "VBC AA";

    public static final String VBC_AA_C = "VBC AA ClientView";

    public static final String VBC_AA_P = "VBC AA Partner";

    public static final String VBC_TA = "VBC TA";

    public static final String VBC_TA_C = "VBC TA ClientView";

    public static final String HH_AA = "HH AA";

    public static final String HH_AA_C = "HH AA ClientView";

    public static final String HH_AA_P = "HH AA Partner";

    public static final String HH_TA = "HH TA";

    public static final String HH_TA_C = "HH TA ClientView";

    public static final String OTHER_PER_AA = "Other Per AA";

    public static final String OTHER_PER_AA_C = "Other Per AA ClientView";

    public static final String OTHER_PER_AA_P = "Other Per AA Partner";

    public static final String OTHER_PER_TA = "Other Per TA";

    public static final String OTHER_PER_TA_C = "Other Per TA ClientView";

    public static final String GA_AA = "GA AA";

    public static final String GA_AA_C = "GA AA ClientView";

    public static final String GA_AA_P = "GA AA Partner";

    public static final String GA_TA = "GA TA";

    public static final String GA_TA_C = "GA TA ClientView";

    public static final String LOANS_AA = "Loans AA";

    public static final String LOANS_AA_C = "Loans AA ClientView";

    public static final String LOANS_AA_P = "Loans AA Partner";

    public static final String LOANS_TA = "Loans TA";

    public static final String LOANS_TA_C = "Loans TA ClientView";

    public static final String SAL_AI = "Salary AI";

    public static final String SAL_TI = "Salary TI";

    public static final String SAL_AI_C = "Salary AI ClientView";

    public static final String SAL_TI_C = "Salary TI ClientView";

    public static final String SAL_AI_P = "Salary AI Partner";

    public static final String SAL_TI_P = "Salary TI Partner";

    public static final String OTHER_AI = "Other AI";

    public static final String OTHER_TI = "Other TI";

    public static final String OTHER_AI_C = "Other AI ClientView";

    public static final String OTHER_TI_C = "Other TI ClientView";

    public static final String OTHER_AI_P = "Other AI Partner";

    public static final String OTHER_TI_P = "Other TI Partner";

    public static final String TOTAL_AA = "Total AA";

    public static final String TOTAL_AA_C = "Total AA ClientView";

    public static final String TOTAL_AA_P = "Total AA Partner";

    public static final String TOTAL_TA = "Total TA";

    public static final String TOTAL_TA_C = "Total TA ClientView";

    public static final String TOTAL_AI = "Total AI";

    public static final String TOTAL_AI_C = "Total AI ClientView";

    public static final String TOTAL_AI_P = "Total AI Partner";

    public static final String TOTAL_TI = "Total TI";

    public static final String TOTAL_TI_C = "Total TI ClientView";

    public static final String TOTAL_TI_P = "Total TI Partner";

    public static final String ACTUAL_ASSET = "Actual Asset";

    public static final String TEST_ASSET = "Test Asset";

    public static final String ACTUAL_ASSET_C = "Actual Asset ClientView";

    public static final String TEST_ASSET_C = "Test Asset ClientView";

    public static final String ACTUAL_ASSET_P = "Actual Asset Partner";

    public static final String TEST_ASSET_P = "Test Asset Partner";

    public static final String ACTUAL_INCOME = "Actual Income";

    public static final String TEST_INCOME = "Test Income";

    public static final String ACTUAL_INCOME_C = "Actual Income ClientView";

    public static final String TEST_INCOME_C = "Test Income ClientView";

    public static final String ACTUAL_INCOME_P = "Actual Income Partner";

    public static final String TEST_INCOME_P = "Test Income Partner";

    public static final String DEEMED_ASSET = "Deemed Asset";

    public static final String NON_DEEMED_ASSET = "Non Deemed Asset";

    public static final String NON_DEEMED_ASSETS_C = "Non Deemed Asset ClientView";

    public static final String NON_DEEMED_ASSETS_P = "Non Deemed Asset Partner";

    public static final String INCOME = "Income";

    public static final String PERSONAL_ASSET = "Personal Asset";

    public static final String ALLOCATED_PENSION = "Allocated Pension";

    public static final String ANNUITIES = "Annuities";

    public static final String COMPLYING_ANNUITY = "Complying Annuity";

    public static final String MAX_BENEFIT = "Max Benefit";

    public static final String ASSET_TEST_RESULT = "Asset Test Result";

    public static final String INCOME_TEST_RESULT = "Income Test Result";

    public static final String BASIC_BENEFIT = "Basic Benefit";

    public static final String PHAR = "Phar";

    public static final String ASSET_THRESHOLD = "Asset Threshold";

    public static final String INCOME_THRESHOLD = "Income Threshold";

    public static final String MAX_BENEFIT_P = "Max Benefit P";

    public static final String ASSET_TEST_RESULT_P = "Asset Test Result P";

    public static final String INCOME_TEST_RESULT_P = "Income Test Result P";

    public static final String BASIC_BENEFIT_P = "Basic Benefit P";

    public static final String PHAR_P = "Phar P";

    public static final String ASSET_THRESHOLD_P = "Asset Threshold P";

    public static final String INCOME_THRESHOLD_P = "Income Threshold ";

    public static final String MAX_BENEFIT_ANN = "Max Benefit Ann";

    public static final String ASSET_TEST_RESULT_ANN = "Asset Test Result Ann";

    public static final String INCOME_TEST_RESULT_ANN = "Income Test Result Ann";

    public static final String BASIC_BENEFIT_ANN = "Basic Benefit Ann";

    public static final String PHAR_ANN = "Phar Ann";

    public static final String MAX_BENEFIT_P_ANN = "Max Benefit P Ann";

    public static final String ASSET_TEST_RESULT_P_ANN = "Asset Test Result P Ann";

    public static final String INCOME_TEST_RESULT_P_ANN = "Income Test Result P Ann";

    public static final String BASIC_BENEFIT_P_ANN = "Basic Benefit P Ann";

    public static final String PHAR_P_ANN = "Phar P Ann";

    public static final String AGE_PENSION = "Age Pension";

    public static final String NEW_START_ALLOWANCE = "New Start Allowance";

    public static final String DISABILITY_SUPPORT_PENSION = "Disability Support Pension";

    public static final String DISABILITY_SUPPORT_PENSION_PARTNER = "DSP Partner";

    public static final String WIDOW_ALLOWANCE = "Widow Allowance";

    public static final String SICKNESS_ALLOWANCE = "Sickness Allowance";

    public static final String SICKNESS_ALLOWANCE_PARTNER = "Sickness Allowance Partner";

    public static final String CARER_ALLOWANCE = "Carer Allowance";

    public static final String CARER_ALLOWANCE_PARTNER = "Carer Allowance Partner";

    public static final String PARTNER_ALLOWANCE = "Partner Allowance";

    public static final String PARTNER_ALLOWANCE_PARTNER = "Partner Allowance Partner";

    public static final String AGE = "Age";

    public static final String YES = "Yes";

    public static final String NO = "No";

    public static final String USER_INFO_PAGE = "User Info Page";

    public static final String VALID = "Valid";

    public static final String INVALID = "Invalid";

    public static final String NON_DEEMED_ASSETS = "Non Deemed Assets";

    public static final String AGE_PARTNER = "Age Partner";

    public static final String AGE_PENSION_PARTNER = "Age Pension Partner";

    public static final String NEW_START_ALLOWANCE_PARTNER = "New Start Allowance Partner";

    public static final String CLIENT_AGE_PENSION_EXCEPTION = " ClientView's Age is still under Age Pension Age.";

    public static final String PARTNER_AGE_PENSION_EXCEPTION = " Partner's Age is still under Age Pension Age.";

    public static final String CLIENT_AGE_PENSION_VALID = " ClientView's Valid.";

    public static final String PARTNER_AGE_PENSION_VALID = " Partner's Valid.";

    public static final String CLIENT_NSA_EXCEPTION = " ClientView's not eligible for NSA.";

    public static final String PARTNER_NSA_EXCEPTION = " Partner's not eligible for NSA.";

    public static final String CLIENT_NSA_VALID = " ClientView's NSA Valid.";

    public static final String PARTNER_NSA_VALID = " Partner's NSA Valid.";

    public static final String CLIENT_PA_VALID = " ClientView's PA Valid.";

    public static final String PARTNER_PA_VALID = " Partner's PA Valid.";

    public static final String CLIENT_DSP_EXCEPTION = " ClientView's not eligible for DSP.";

    public static final String CLIENT_PA_EXCEPTION = " ClientView's not eligible for PA.";

    public static final String PARTNER_PA_EXCEPTION = " Partner's not eligible for PA.";

    public static final String CLIENT_WA_EXCEPTION = " ClientView's not eligible for WA.";

    public static final String CLIENT_SA_EXCEPTION = " ClientView's not eligible for SA.";

    public static final String PARTNER_SA_EXCEPTION = " Partner's not eligible for SA.";

    public static final String PARTNER_DSP_EXCEPTION = " Partner's not eligible for DSP.";

    public static final String CLIENT_DSP_VALID = " ClientView's DSP Valid.";

    public static final String PARTNER_DSP_VALID = " Partner's DSP Valid.";

    public static final String CLIENT_WA_VALID = " ClientView's WA Valid.";

    public static final String CLIENT_SA_VALID = " ClientView's SA Valid.";

    public static final String PARTNER_SA_VALID = " Partner's SA Valid.";

    public static final int COMBINED = 0;

    public static final int CLIENT = 1;

    public static final int PARTNER = 2;

    public static final int A_N = 0;

    public static final int A_A = 1;

    public static final int A_P = 2;

    // MortgageCalc
    public static final String PURCHASE_PRICE = "Purchase Price";

    public static final String DEPOSIT = "Deposit";

    public static final String AMOUNT_BORROWED = "Amount Borrowed";

    public static final String LOAN_TERM = "Loan Term";

    public static final String INTEREST_RATE = "Interest Rate";

    public static final String HAS_ADDITIONAL_PAYMENTS_YES = "Has Additional Payments Yes";

    public static final String HAS_ADDITIONAL_PAYMENTS_NO = "Has Additional Payments No";

    public static final String ADDITIONAL_PAYMENT_AMOUNT = "Additional Payment Amount";

}
