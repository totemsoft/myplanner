/*
 * Constants.java
 *
 * Created on 27 August 2002, 21:56
 */

package au.com.totemsoft.myplanner.tax.au;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface ITaxConstants {

    /**
     * Taxable items as interim by-products of calculation
     * 
     */
    public static final String TAXABLE_INCOME = "TaxableIncome";

    public static final String ASSESSABLE_INCOME = "AssesableIncome";

    public static final String NON_INVESTMENT_INCOME = "NonInvestmentIncome";

    /**
     * Tax types
     * 
     * 
     */
    public static final String TAX_ON_TAXABLE_INCOME = "TaxOnTaxableIncome";

    public static final String ML = "MedicareLevy";

    public static final String MLS = "MedicareLevySurchage";

    public static final String TAX_ON_INVESTMENT_INCOME = "TaxOnInvestmentIncome";

    public static final String ML_INVESTMENT = "MedicareLevyInvestment";

    public static final String MLS_INVESTMENT = "MedicareLevySurchageInvestment";

    public static final String TAX_ON_NON_INVESTMENT_INCOME = "TaxOnNonInvestmentIncome";

    public static final String ML_NON_INVESTMENT = "MedicareLevynonNonInvestment";

    public static final String MLS_NON_INVESTMENT = "MedicareLevySurchageNonInvestment";

    /**
     * Tax offests. Currently only tax withheld and imputation credit are
     * implemented
     */
    public static final String O_TAX_WITHHELD = "TaxOffsetTaxWithHeld";

    public static final String O_IMPUTATION_CREDIT = "ImputationCredit";

    public static final String O_LUMP_SUM_ETP = "LumpSumETP";

    public static final String O_OTHER = "Other";

    public static final String O_SUPER = "Superannuation";

    public static final String O_LOW_INCOME = "LowIncome";

    /**
     * By product output
     */
    public static final String O_AVERAGE_TAX_RATE = "AverageTaxRate";

    /**
     * Tax calculation results. Imlemetation includes : Total tax on taxable
     * income , medicare levy and medicare levy surcharge Total tax on
     * non-investment income , medicare levy and medicare levy surcharge
     */
    public static final String TAX_PAYABLE = "TaxPayable";

    public static final String TAX_PAYABLE_NON_INVESTMENT = "TaxPayableNonInvestment";

    /**
     * Investment income types. IMORTANT NOTE: INCOME_OTHER - Special type of
     * income to determine the correct tax rate. The resulted tax collection
     * will not include the tax for this type of income.
     */
    public static final String T_TOTAL_TAX_WITHHELD = "TotalTaxWothHeld";

    public static final String I_TOTAL_INCOME = "TotalIncome";

    public static final String I_OTHER = "IncomeOther";

    public static final String I_SALARY = "IncomeSalary";

    public static final String I_LUMP_SUM_PART_A = "IncomeLumpSumPartA";

    public static final String I_LUMP_SUM_PART_B = "IncomeLumpSumPartB";

    public static final String I_LUMP_SUM_PART_C = "IncomeLumpSumPartC";

    public static final String I_PENSIONS_ALLOWANCE = "IncomePensionsAllowance";

    public static final String I_OTHER_PENSIONS = "IncomeOtherPensions";

    public static final String I_RFB = "IncomeReportableFringeBenefit";

    public static final String I_GROSS_INTEREST = "IncomeGrossInterest";

    public static final String I_DIVIDENDS_UNFRANKED = "IncomeDividentsUnfranked";

    public static final String I_DIVIDENDS_FRANKED = "IncomeDividentsFranked";

    public static final String I_DIVIDENDS_IMPUTATION_CREDIT = "IncomeDividentsImputationCredit";

    public static final String I_TAX_DEFERRED = "IncomeTaxDeferred";

    public static final String I_SUPPLEMENT = "IncomeSupplement";

    public static final String T_OTHER = "TaxWithHeldOther";

    public static final String T_SALARY = "TaxWithHeldSalary";

    public static final String T_LUMP_SUM_PART_A = "TaxWithHeldLumpSumPartA";

    public static final String T_LUMP_SUM_PART_B = "TaxWithHeldLumpSumPartB";

    public static final String T_PENSIONS_ALLOWANCE = "TaxWithHeldPensionsAllowance";

    public static final String T_OTHER_PENSIONS = "TaxWithHeldOtherPensions";

    public static final String T_ETP = "TaxWithHeldETP";

    public static final String T_GROSS_INTEREST = "TaxWithHeldGrossInterest";

    public static final String T_DIVIDENDS = "TaxWithHeldDividents";

    /**
     * ETP components. Generally all ETP calculations are performed outside the
     * tax component.But for consistency sake tax component implements ETP
     * calculations. ETP components(excessive, pre july 1983 concessinal
     * components ) affect final tax calculations final
     * 
     */
    public static final String I_PRE_JULY_1983 = "PreJuly1983";

    public static final String I_POST_JUNE_1983_TAXED = "PostJune1983Taxed";

    public static final String I_POST_JUNE_1983_UNTAXED = "PostJune1983UnTaxed";

    public static final String I_UNDEDUCTED = "UNDEDUCTED";

    public static final String I_CGT_EXEMPT = "CGTExempt";

    public static final String I_ETP_NON_EXCESSIVE = "IncomeETPNonExcessive";

    public static final String I_ETP_EXCESSIVE = "IncomeETPExcessive";

    public static final String I_CONCESSINAL = "Concessinal";

    public static final String I_INVALIDITY = "Invalidity";

    /**
     * Deductible expenses IMORTANT NOTE: D_GENERAL - Current version will
     * accumulate all deductions excluding Interest paid and UPP under
     * DeductionsOther section. Future releases will deferentiate between
     * deifferent types of dedudctions if bussines rules require.
     */
    public static final String D_GENERAL = "DeductionsOther";

    public static final String D_INTEREST_DIVIDEND = "DeductionsInterestDivided";

    public static final String D_UNDEDUCTED_PIRCHASE_PRICE = "DeductionsUPP";

    /**
     * Personal information reqired for taxation calculations
     * 
     * 
     */
    public static final String P_HOSPITAL_COVER = "HospitalCover";

    public static final String P_MARITAL_STATUS = "MaritalStatus";

    public static final String P_DEPENDANTS = "Dependants";

    public static final String P_SPOUSE_INCOME = "SpouseIncome";

    public static final String P_AGE = "Age";

    /*
     * Tax constants: need to be taken from external source
     * 
     */

    public static final double FAMILY_INCOME_THRESHOLD = 100000.00;

    public static final double SINGLE_INCOME_THRESHOLD = 50000.00;

    public static final double LOW_INCOME_THRESHOLD = 14539.00;

    // public static final double ML_THRESHOLD_MIN_INDIVIDUAL = 14540.00 ;
    // public static final double ML_THRESHOLD_MAX_INDIVIDUAL = 15717.00 ;
    // New rates since 01/07/2003
    public static final double ML_THRESHOLD_MIN_INDIVIDUAL = 15063.00;

    public static final double ML_THRESHOLD_MAX_INDIVIDUAL = 16283.00;

    // public static final double ML_THRESHOLD_MIN_FAMILY = 24535.00 ;
    // public static final double ML_THRESHOLD_MAX_FAMILY = 26523.00 ;
    // New rates since 01/07/2003
    public static final double ML_THRESHOLD_MIN_FAMILY = 25418.00;

    public static final double ML_THRESHOLD_MAX_FAMILY = 27477.00;

    // public static final double ML_THRESHOLD_MIN_STEP = 2253.00 ;
    // public static final double ML_THRESHOLD_MAX_STEP = 2435.00 ;
    // New rates since 01/07/2003
    public static final double ML_THRESHOLD_MIN_STEP = 2334.00;

    public static final double ML_THRESHOLD_MAX_STEP = 2523.00;

    // public static final double ML_THRESHOLD = 15717.00 ;
    // New rates since 01/07/2003
    public static final double ML_THRESHOLD = 16283.00;

    public static final double DEPENDANT_THRESHOLD = 1500.00;

    // public static final double POST_JUNE_83_THRESHOLD = 105843.00 ;
    // new rate since 01/07/2003
    public static final double POST_JUNE_83_THRESHOLD = 117576.00;

    public static final double MEDICARE_LEVY_LOW_INCOME = 20.0;

    public static final double MEDICARE_LEVY = 1.5;

    public static final double MEDICARE_LEVY_SURCHARGE = 1.0;

    // public static final double LOW_INCOME_REBATE = 150.;
    // public static final double LOW_INCOME_REBATE_MIN = 20700.00 ;
    // public static final double LOW_INCOME_REBATE_MAX = 24450.00 ;
    // public static final double LOW_INCOME_REBATE_REDUCTION = 0.04;
    // New rates since 01/07/2003
    public static final double LOW_INCOME_REBATE = 235.;

    public static final double LOW_INCOME_REBATE_MIN = 21600.00;

    public static final double LOW_INCOME_REBATE_MAX = 27475.00;

    public static final double LOW_INCOME_REBATE_REDUCTION = 0.04;

    public static final double R_PRE_JULY_1983_TAXABLE = 5.0;

    public static final double R_CONCESSIONAL_TAXABLE = 5.0;

    public static final double R_POST_JUNE_1983_TAXED_UNDER55 = 20.0;

    public static final double R_POST_JUNE_1983_TAXED_OVER55 = 15.0;

    public static final double R_POST_JUNE_1983_UNTAXED_UNDER55 = 30.0;

    public static final double R_POST_JUNE_1983_UNTAXED_OVER55 = 15.0;

    public static final double R_ETP_EXCESSIVE = 47.0;

    public static final double R_LUMP_SUM_PART_A = 30.00;

    /**
     * Long Service leave constants
     * 
     */

    public static final String LSL_CALC_DATE = "LSL:Date of Calculation";

    public static final String LSL_START_DATE = "LSL:LSL Start Date";

    public static final String LSL_REDUNDANCY = "LSL:Redundancy/Invaliditiy/Early Reterement";

    public static final String LSL_TOTAL_UNUSED = "LSL:Total Unsed Days";

    public static final String LSL_USED_AFTER_1978 = "LSL:Used after 15/08/1978";

    public static final String LSL_USED_AFTER_1993 = "LSL:Used after 17/08/1993";

    public static final String LSL_AMOUNT = "LSL:Amount";

    public static final String LSL_ARE_DAYS_AVAILABLE = "DaysAvailable";

    public static final String LSL_MRT5PERCENT = "LSL:Amount5MRT";

    public static final String LSL_MRT = "LSL:AmountMRT";

    public static final String LSL_CONCESSIONAL = "LSL:AmountConcessional";

    public static final String LSL_AL_TOTAL = "LSL:AL:AmountTotal";

    /**
     * Annual leave constants
     * 
     */

    public static final String AL_AMOUNT = "AL:Amount";

    public static final String DATE_15_08_1978 = "15/08/1978";

    public static final String DATE_17_08_1993 = "17/08/1993";

    public static final String AL_MRT = "AL:AmountMRT";

    public static final String AL_CONCESSIONAL = "AL:AmountConcessional";

}
