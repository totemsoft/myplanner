/*
 * ReportFields_java
 *
 * Created on 3 April 2003, 09:47
 */

package com.argus.financials.code;

/**
 * @author valeri chibaev
 */

public interface IReportFields {

    // //////////////////////////////////////////////////////////////////////////
    // FINANCIALS //
    // //////////////////////////////////////////////////////////////////////////
    public static final String CURRENT_PREFIX = ""; // "Current_";
    public static final String PROPOSED_PREFIX = "Proposed_";
    public static final String RECOMMENDED_PREFIX = "Recommended_";

    // //////////////////////////////////////////////////////////////////////////
    // CURRENT //
    // //////////////////////////////////////////////////////////////////////////
    public static final String FinancialAssetLiability = "FinancialAssetLiability";
    public static final String FinancialAssetLiabilityByService = "FinancialAssetLiabilityByService";
    public static final String FinancialEarningsEstimate = "FinancialEarningsEstimate";
    public static final String FinancialCashFlow = "FinancialCashFlow";

    // //////////////////////////////////////////////////////////////////////////
    // STRATEGY (Proposed) //
    // //////////////////////////////////////////////////////////////////////////
    /*
     * same as current (financials, cashflow, wealth, taxanalysis), plus some
     * extra fields
     */
    public static final String Strategy_Name = "Strategy/Name";
    public static final String Strategy_UnallocatedAmount = "Strategy/UnallocatedAmount";
    public static final String Strategy_Summary = "Strategy/Summary";
    public static final String Strategy_Recommendations = "Strategy/Recommendations";
    public static final String Strategy_Group_Data = "Strategy/Group_Data";

    // //////////////////////////////////////////////////////////////////////////
    // GENERAL //
    // //////////////////////////////////////////////////////////////////////////
    public static final String CURRENT_DATE = "CURRENT_DATE";
    public static final String DateShort = "DateShort";
    public static final String DateMedium = "DateMedium";
    public static final String DateLong = "DateLong";

    // //////////////////////////////////////////////////////////////////////////
    // PERSONAL //
    // //////////////////////////////////////////////////////////////////////////
    public static final String Client_Sex = "ClientView/Sex";
    public static final String Client_Title = "ClientView/Title";
    public static final String Client_IsMarried = "ClientView/IsMarried";
    public static final String Client_MaritalCode = "ClientView/MaritalCode";
    public static final String Client_FamilyName = "ClientView/FamilyName";
    public static final String Client_FirstName = "ClientView/FirstName";
    public static final String Client_OtherGivenNames = "ClientView/OtherGivenNames";
    public static final String Client_FullName = "ClientView/FullName";
    public static final String Client_DOB = "ClientView/DOB";
    public static final String Client_Age = "ClientView/Age";
    public static final String Client_LifeExpectancy = "ClientView/LifeExpectancy";
    public static final String Client_StateOfHealth = "ClientView/StateOfHealth";
    public static final String Client_Resident = "ClientView/Resident";
    public static final String Client_AgePensionQualifyingYear = "ClientView/AgePensionQualifyingYear";
    public static final String Client_AgePensionQualifyingAge = "ClientView/AgePensionQualifyingAge";
    public static final String Client_Dependents = "ClientView/Dependents";
    public static final String Client_PhoneWork = "ClientView/PhoneWork";
    public static final String Client_PhoneHome = "ClientView/PhoneHome";
    public static final String Client_Mobile = "ClientView/Mobile";
    public static final String Client_Fax = "ClientView/Fax";
    public static final String Client_Email = "ClientView/Email";
    public static final String Client_PostalAddress = "ClientView/PostalAddress";
    public static final String Client_PostalAddress_StreetNumber = "ClientView/PostalAddress/StreetNumber";
    public static final String Client_PostalAddress_StreetNumber2 = "ClientView/PostalAddress/StreetNumber2";
    public static final String Client_PostalAddress_Suburb = "ClientView/PostalAddress/Suburb";
    public static final String Client_PostalAddress_Postcode = "ClientView/PostalAddress/Postcode";
    public static final String Client_PostalAddress_State = "ClientView/PostalAddress/State";
    public static final String Client_PostalAddress_Country = "ClientView/PostalAddress/Country";
    public static final String Client_ResidentialAddress = "ClientView/ResidentialAddress";
    public static final String Client_ResidentialAddress_StreetNumber = "ClientView/ResidentialAddress/StreetNumber";
    public static final String Client_ResidentialAddress_StreetNumber2 = "ClientView/ResidentialAddress/StreetNumber2";
    public static final String Client_ResidentialAddress_Suburb = "ClientView/ResidentialAddress/Suburb";
    public static final String Client_ResidentialAddress_Postcode = "ClientView/ResidentialAddress/Postcode";
    public static final String Client_ResidentialAddress_State = "ClientView/ResidentialAddress/State";
    public static final String Client_ResidentialAddress_Country = "ClientView/ResidentialAddress/Country";
    public static final String Client_TargetAge = "ClientView/TargetAge";
    public static final String Client_TargetDate = "ClientView/TargetDate";
    public static final String Client_YearsToTargetAge = "ClientView/YearsToTargetAge";
    public static final String Client_TargetIncome = "ClientView/TargetIncome";
    public static final String Client_LumpSumRequired = "ClientView/LumpSumRequired";
    public static final String Client_Notes = "ClientView/Notes";
    public static final String Client_Employer_LegalName = "ClientView/Employer/LegalName";
    public static final String Client_Employer_TradingName = "ClientView/Employer/TradingName";
    public static final String Client_Occupation_EmploymentStatus = "ClientView/Occupation/EmploymentStatus";
    public static final String Client_Occupation_OccupationName = "ClientView/Occupation/OccupationName";
    public static final String Client_HealthCover = "ClientView/HealthCover";
    public static final String Client_NumberOfDependents = "ClientView/NumberOfDependents";
    public static final String Client_NameAgeOfDependents = "ClientView/NameAgeOfDependents";
    public static final String Client_FeeDate = "ClientView/FeeDate";
    public static final String Client_ReviewDate = "ClientView/ReviewDate";
    public static final String Client_DependentsNameAgeSentence = "ClientView/DependentsNameAgeSentence";
    public static final String Client_EmploymentStatus = "ClientView/EmploymentStatus";
    // Partner
    public static final String Partner_Sex = "Partner/Sex";
    public static final String Partner_Title = "Partner/Title";
    public static final String Partner_MaritalCode = "Partner/MaritalCode";
    public static final String Partner_FamilyName = "Partner/FamilyName";
    public static final String Partner_FirstName = "Partner/FirstName";
    public static final String Partner_OtherGivenNames = "Partner/OtherGivenNames";
    public static final String Partner_FullName = "Partner/FullName";
    public static final String Partner_DOB = "Partner/DOB";
    public static final String Partner_Age = "Partner/Age";
    public static final String Partner_LifeExpectancy = "Partner/LifeExpectancy";
    public static final String Partner_StateOfHealth = "Partner/StateOfHealth";
    public static final String Partner_Resident = "Partner/Resident";
    public static final String Partner_AgePensionQualifyingYear = "Partner/AgePensionQualifyingYear";
    public static final String Partner_AgePensionQualifyingAge = "Partner/AgePensionQualifyingAge";
    public static final String Partner_Dependents = "Partner/Dependents";
    public static final String Partner_PhoneWork = "Partner/PhoneWork";
    public static final String Partner_PhoneHome = "Partner/PhoneHome";
    public static final String Partner_Mobile = "Partner/Mobile";
    public static final String Partner_Fax = "Partner/Fax";
    public static final String Partner_Email = "Partner/Email";
    public static final String Partner_PostalAddress = "Partner/PostalAddress";
    public static final String Partner_PostalAddress_StreetNumber = "Partner/PostalAddress/StreetNumber";
    public static final String Partner_PostalAddress_StreetNumber2 = "Partner/PostalAddress/StreetNumber2";
    public static final String Partner_PostalAddress_Suburb = "Partner/PostalAddress/Suburb";
    public static final String Partner_PostalAddress_Postcode = "Partner/PostalAddress/Postcode";
    public static final String Partner_PostalAddress_State = "Partner/PostalAddress/State";
    public static final String Partner_PostalAddress_Country = "Partner/PostalAddress/Country";
    public static final String Partner_ResidentialAddress = "Partner/ResidentialAddress";
    public static final String Partner_ResidentialAddress_StreetNumber = "Partner/ResidentialAddress/StreetNumber";
    public static final String Partner_ResidentialAddress_StreetNumber2 = "Partner/ResidentialAddress/StreetNumber2";
    public static final String Partner_ResidentialAddress_Suburb = "Partner/ResidentialAddress/Suburb";
    public static final String Partner_ResidentialAddress_Postcode = "Partner/ResidentialAddress/Postcode";
    public static final String Partner_ResidentialAddress_State = "Partner/ResidentialAddress/State";
    public static final String Partner_ResidentialAddress_Country = "Partner/ResidentialAddress/Country";
    public static final String Partner_TargetAge = "Partner/TargetAge";
    public static final String Partner_TargetDate = "Partner/TargetDate";
    public static final String Partner_YearsToTargetAge = "Partner/YearsToTargetAge";
    public static final String Partner_TargetIncome = "Partner/TargetIncome";
    public static final String Partner_LumpSumRequired = "Partner/LumpSumRequired";
    public static final String Partner_Notes = "Partner/Notes";
    public static final String Partner_Employer_LegalName = "Partner/Employer/LegalName";
    public static final String Partner_Employer_TradingName = "Partner/Employer/TradingName";
    public static final String Partner_Occupation_EmploymentStatus = "Partner/Occupation/EmploymentStatus";
    public static final String Partner_Occupation_OccupationName = "Partner/Occupation/OccupationName";
    public static final String Partner_HealthCover = "Partner/HealthCover";
    public static final String Partner_NumberOfDependents = "Partner/NumberOfDependents";
    public static final String Partner_NameAgeOfDependents = "Partner/NameAgeOfDependents";
    public static final String Partner_DependentsNameAgeSentence = "Partner/DependentsNameAgeSentence";
    public static final String Partner_EmploymentStatus = "Partner/EmploymentStatus";
    // Adviser
    public static final String Adviser_Title = "Adviser/Title";
    public static final String Adviser_FamilyName = "Adviser/FamilyName";
    public static final String Adviser_FirstName = "Adviser/FirstName";
    public static final String Adviser_OtherGivenNames = "Adviser/OtherGivenNames";
    public static final String Adviser_FullName = "Adviser/FullName";

    // //////////////////////////////////////////////////////////////////////////
    // GEARING //
    // //////////////////////////////////////////////////////////////////////////
    public static final String Gearing_Name = "Gearing/Name";
    public static final String Gearing_Cashflow = "Gearing/Cashflow";
    public static final String Gearing_Summary = "Gearing/Summary";
    public static final String Gearing_Summary_Graph = "Gearing/Summary_Graph";
    public static final String Gearing_Summary_GraphName = "Gearing/Summary_GraphName";
    public static final String Gearing_Years2Project = "Gearing/Years2Project";
    public static final String Gearing_InvestmentStrategy = "Gearing/InvestmentStrategy";
    public static final String Gearing_GrowthRate = "Gearing/GrowthRate";
    public static final String Gearing_IncomeRate = "Gearing/IncomeRate";
    public static final String Gearing_TotalReturnRate = "Gearing/TotalReturnRate";
    public static final String Gearing_InitialInvestorDepositAmount = "Gearing/InitialInvestorDepositAmount";
    public static final String Gearing_InitialLoanAmount = "Gearing/InitialLoanAmount";
    public static final String Gearing_RegularInvestorContributionAmount = "Gearing/RegularInvestorContributionAmount";
    public static final String Gearing_RegularLoanAdvanceAmount = "Gearing/RegularLoanAdvanceAmount";
    public static final String Gearing_OtherTaxableIncome = "Gearing/OtherTaxableIncome";
    public static final String Gearing_ReinvestIncome = "Gearing/ReinvestIncome";
    public static final String Gearing_CreditLimitAmount = "Gearing/CreditLimitAmount";
    public static final String Gearing_Type = "Gearing/Type";

    // //////////////////////////////////////////////////////////////////////////
    // MORTGAGE //
    // //////////////////////////////////////////////////////////////////////////
    public static final String Mortgage_Assumptions = "Mortgage/Assumptions";
    public static final String Mortgage_Results = "Mortgage/Results";
    public static final String Mortgage_Graph = "Mortgage/Graph";
    public static final String Mortgage_GraphName = "Mortgage/GraphName";
    public static final String Mortgage_AdditionalMonthlyPayment = "Mortgage/AdditionalMonthlyPayment";
    public static final String Mortgage_YearsReduced = "Mortgage/YearsReduced";
    public static final String Mortgage_AmountBorrowed = "Mortgage/AmountBorrowed";

    // //////////////////////////////////////////////////////////////////////////
    // CASHFLOW //
    // //////////////////////////////////////////////////////////////////////////
    // assumptions
    public static final String Cashflow_YearsToProject = "Cashflow/YearsToProject";
    public static final String Cashflow_Inflation = "Cashflow/Inflation";
    public static final String Cashflow_RetirementDate = "Cashflow/RetirementDate";
    public static final String Cashflow_RetirementAge = "Cashflow/RetirementAge";
    public static final String Cashflow_RetirementIncome = "Cashflow/RetirementIncome";
    public static final String Cashflow_RetirementIncomeAtRetirement = "Cashflow/RetirementIncomeAtRetirement";
    public static final String Cashflow_LumpSumRequired = "Cashflow/LumpSumRequired";
    public static final String Cashflow_LumpSumRequiredAtRetirement = "Cashflow/LumpSumRequiredAtRetirement";
    public static final String Cashflow_InvestmentStrategy = "Cashflow/InvestmentStrategy";
    public static final String Cashflow_IncomeRate = "Cashflow/IncomeRate";
    public static final String Cashflow_GrowthRate = "Cashflow/GrowthRate";
    public static final String Cashflow_DebtRepayment = "Cashflow/DebtRepayment";
    // can be CURRENT or PROPOSED
    public static final String Cashflow_Table = "Cashflow/Table";
    public static final String Cashflow_GraphName = "Cashflow/GraphName";
    public static final String Cashflow_Graph = "Cashflow/Graph";

    // //////////////////////////////////////////////////////////////////////////
    // WEALTH //
    // //////////////////////////////////////////////////////////////////////////
    // assumptions
    public static final String Wealth_YearsToProject = "Wealth/YearsToProject";
    public static final String Wealth_Inflation = "Wealth/Inflation";
    public static final String Wealth_RetirementDate = "Wealth/RetirementDate";
    public static final String Wealth_RetirementAge = "Wealth/RetirementAge";
    public static final String Wealth_RetirementIncome = "Wealth/RetirementIncome";
    public static final String Wealth_RetirementIncomeAtRetirement = "Wealth/RetirementIncomeAtRetirement";
    public static final String Wealth_LumpSumRequired = "Wealth/LumpSumRequired";
    public static final String Wealth_LumpSumRequiredAtRetirement = "Wealth/LumpSumRequiredAtRetirement";
    public static final String Wealth_InvestmentStrategy = "Wealth/InvestmentStrategy";
    public static final String Wealth_IncomeRate = "Wealth/IncomeRate";
    public static final String Wealth_GrowthRate = "Wealth/GrowthRate";
    public static final String Wealth_DebtRepayment = "Wealth/DebtRepayment";
    // can be CURRENT or PROPOSED
    public static final String Wealth_Table = "Wealth/Table";
    public static final String Wealth_GraphName = "Wealth/GraphName";
    public static final String Wealth_Graph = "Wealth/Graph";
    public static final String Wealth_RetirementTable = "Wealth/RetirementTable";
    public static final String Wealth_RetirementGraphName = "Wealth/RetirementGraphName";
    public static final String Wealth_RetirementGraph = "Wealth/RetirementGraph";

    // //////////////////////////////////////////////////////////////////////////
    // TAX ANALYSIS //
    // //////////////////////////////////////////////////////////////////////////
    public static final String TaxAnalysis_ClientIncome = "TaxAnalysis/ClientIncome";
    public static final String TaxAnalysis_PartnerIncome = "TaxAnalysis/PartnerIncome";
    public static final String TaxAnalysis_SummaryTable = "TaxAnalysis/SummaryTable";
    public static final String TaxAnalysis_IncomeTable = "TaxAnalysis/IncomeTable";
    public static final String TaxAnalysis_ExpenseTable = "TaxAnalysis/ExpenseTable";
    public static final String TaxAnalysis_OffsetTable = "TaxAnalysis/OffsetTable";
    public static final String TaxAnalysis_DetailsTable = "TaxAnalysis/DetailsTable";

    // //////////////////////////////////////////////////////////////////////////
    // TAX REPORT //
    // //////////////////////////////////////////////////////////////////////////
    public static final String TaxReport_Table = "TaxReport/Table";

    ////////////////////////////////////////////////////////////////////////////
    //                      CurrenPosition                                    //
    ////////////////////////////////////////////////////////////////////////////
    // General
    public static final String CurrenPosition_Name = "CP/Name";
    public static final String CurrenPosition_GoalRetirement = "CP/GoalRetirement";
    // ClientView/Partner
    public static final String CurrenPosition_MaritalCode = "CP/MaritalCode";
    public static final String CurrenPosition_YearsToProject = "CP/YearsToProject";
    public static final String CurrenPosition_LifeExpectancy = "CP/LifeExpectancy";
    // ClientView
    public static final String CurrenPosition_ClientSex = "CP/ClientView/Sex";
    public static final String CurrenPosition_ClientDOB = "CP/ClientView/DOB";
    public static final String CurrenPosition_ClientAge = "CP/ClientView/Age";
    public static final String CurrenPosition_ClientTargetAge = "CP/ClientView/TargetAge";
    public static final String CurrenPosition_ClientTargetDate = "CP/ClientView/TargetDate";
    public static final String CurrenPosition_ClientTaxRate = "CP/ClientView/TaxRate";
    // Partner
    public static final String CurrenPosition_PartnerSex = "CP/Partner/Sex";
    public static final String CurrenPosition_PartnerDOB = "CP/Partner/DOB";
    public static final String CurrenPosition_PartnerAge = "CP/Partner/Age";
    public static final String CurrenPosition_PartnerTargetAge = "CP/Partner/TargetAge";
    public static final String CurrenPosition_PartnerTargetDate = "CP/Partner/TargetDate";
    public static final String CurrenPosition_PartnerTaxRate = "CP/Partner/TaxRate";
    // ??? TableModel ???
    public static final String CurrenPosition_ContributionCash = "CP/ContributionCash";
    public static final String CurrenPosition_ContributionSuper = "CP/ContributionSuper";
    public static final String CurrenPosition_ContributionSavings = "CP/ContributionSavings";
    public static final String CurrenPosition_ProjectedCash = "CP/ProjectedCash";
    public static final String CurrenPosition_ProjectedSuper = "CP/ProjectedSuper";
    public static final String CurrenPosition_ProjectedSavings = "CP/ProjectedSavings";
    public static final String CurrenPosition_ProjectedTotal = "CP/ProjectedTotal";
    // Fees
    public static final String CurrenPosition_Indexation = "CP/Indexation";
    public static final String CurrenPosition_EntryFees = "CP/EntryFees";
    public static final String CurrenPosition_ReviewFees = "CP/ReviewFees";
    public static final String CurrenPosition_RequiredIncome = "CP/RequiredIncome";
    public static final String CurrenPosition_ResidualRequired = "CP/ResidualRequired";
    public static final String CurrenPosition_ATOUPP = "CP/ATOUPP";
    public static final String CurrenPosition_DSSUPP = "CP/DSSUPP";
    public static final String CurrenPosition_PensionRebate = "CP/PensionRebate";
    // BeforeRetirement
    public static final String CurrenPosition_BeforeRetirement_Title = "CP/BeforeRetirement/Title";
    public static final String CurrenPosition_BeforeRetirement_IncomeRate = "CP/BeforeRetirement/IncomeRate";
    public static final String CurrenPosition_BeforeRetirement_GrowthRate = "CP/BeforeRetirement/GrowthRate";
    public static final String CurrenPosition_BeforeRetirement_TotalReturnRate = "CP/BeforeRetirement/TotalReturnRate";
    public static final String CurrenPosition_BeforeRetirement_Graph = "CP/BeforeRetirement/Graph";
    public static final String CurrenPosition_BeforeRetirement_GraphName = "CP/BeforeRetirement/GraphName";
    // AfterRetirement
    public static final String CurrenPosition_AfterRetirement_Title = "CP/AfterRetirement/Title";
    public static final String CurrenPosition_AfterRetirement_IncomeRate = "CP/AfterRetirement/IncomeRate";
    public static final String CurrenPosition_AfterRetirement_GrowthRate = "CP/AfterRetirement/GrowthRate";
    public static final String CurrenPosition_AfterRetirement_TotalReturnRate = "CP/AfterRetirement/TotalReturnRate";
    public static final String CurrenPosition_AfterRetirement_Graph = "CP/AfterRetirement/Graph";
    public static final String CurrenPosition_AfterRetirement_GraphName = "CP/AfterRetirement/GraphName";
    // Result
    public static final String CurrenPosition_Comment = "CP/Comment";
    public static final String CurrenPosition_Graph = "CP/Graph";
    public static final String CurrenPosition_GraphName = "CP/GraphName";
    public static final String CurrenPosition_IncomePayable = "CP/IncomePayable";

    
    // //////////////////////////////////////////////////////////////////////////
    // ETP calculator //
    // //////////////////////////////////////////////////////////////////////////
    public static final String ETP_Details = "ETP/Details";

    public static final String ETP_Payment_Rollover = "ETP/Payment_Rollover";

    public static final String ETP_Taxation = "ETP/Taxation";

    public static final String ETP_IsClient = "ETP/IsClient";

    // //////////////////////////////////////////////////////////////////////////
    // AP calculator //
    // //////////////////////////////////////////////////////////////////////////
    public static final String AP_Assumptions = "AP/Assumptions";

    public static final String AP_Components = "AP/Components";

    public static final String AP_Payment_Level = "AP/Payment_Level";

    public static final String AP_GraphName = "AP/GraphName";

    public static final String AP_Graph = "AP/Graph";

    public static final String AP_Projection = "AP/Projection";

    public static final String AP_IsClient = "AP/IsClient";

    // //////////////////////////////////////////////////////////////////////////
    // Asset Allocation //
    // //////////////////////////////////////////////////////////////////////////
    public static final String AA_RecommendedGraph = "AA/RecommendedGraph";

    public static final String AA_CurrentGraph = "AA/CurrentGraph";

    public static final String AA_ProposedGraph = "AA/ProposedGraph";

    public static final String AA_RecommendedCurrentTable = "AA/RecommendedCurrentTable";

    public static final String AA_RecommendedProposedTable = "AA/RecommendedProposedTable";

    // //////////////////////////////////////////////////////////////////////////
    // Risk Evaluation/ISO – Risk Profile //
    // //////////////////////////////////////////////////////////////////////////
    public static final String InvRisk_QuestionaryTable = "InvRisk/QuestionaryTable";

    public static final String InvRisk_InvRiskProfileClientFullName = "InvRisk/InvRiskProfileClientFullName";

    public static final String InvRisk_RiskRating_RiskProfileRating = "InvRisk/RiskRating/RiskProfileRating";

    public static final String InvRisk_RiskRating_IncomeReturn = "InvRisk/RiskRating/IncomeReturn";

    public static final String InvRisk_RiskRating_CapitalGrowthReturn = "InvRisk/RiskRating/CapitalGrowthReturn";

    public static final String InvRisk_RiskRating_TotalReturn = "InvRisk/RiskRating/TotalReturn";

    public static final String InvRisk_RiskRating_DefensiveAsset = "InvRisk/RiskRating/DefensiveAsset";

    public static final String InvRisk_RiskRating_GrowthAsset = "InvRisk/RiskRating/GrowthAsset";

    public static final String InvRisk_RiskRating_RangeOfOutcomes_1yr = "InvRisk/RiskRating/RangeOfOutcomes_1yr";

    public static final String InvRisk_RiskRating_RangeOfOutcomes_in_2_out_of_3_Years = "InvRisk/RiskRating/RangeOfOutcomes_2in3";

    public static final String InvRisk_AA_Recommended_Cash = "InvRisk/AA/Recommended/Cash";

    public static final String InvRisk_AA_Recommended_FixedInterest = "InvRisk/AA/Recommended/FixedInterest";

    public static final String InvRisk_AA_Recommended_ListedProperty = "InvRisk/AA/Recommended/ListedProperty";

    public static final String InvRisk_AA_Recommended_AustralianShares = "InvRisk/AA/Recommended/AustralianShares";

    public static final String InvRisk_AA_Recommended_InternationalShares = "InvRisk/AA/Recommended/Intnl_Shares";

    public static final String InvRisk_AA_Recommended_Total = "InvRisk/AA/Recommended/Total";

    // //////////////////////////////////////////////////////////////////////////
    // Social Security //
    // //////////////////////////////////////////////////////////////////////////
    public static final String DSS_AssetsTestTable = "Centrelink/AssetsTestTable";

    public static final String DSS_IncomeTestTable = "Centrelink/IncomeTestTable";

    public static final String DSS_MaritalStatus = "Centrelink/MaritalStatus";

    public static final String DSS_HomeOwner = "Centrelink/HomeOwner";

    public static final String DSS_Pharmaceutical_Allowance_Client = "Centrelink/Pharmaceutical_Allowance_Client";

    public static final String DSS_Pharmaceutical_Allowance_Partner = "Centrelink/Pharmaceutical_Allowance_Partner";

    public static final String DSS_Rent_Assistance_Client = "Centrelink/Rent_Assistance_Client";

    public static final String DSS_Rent_Assistance_Partner = "Centrelink/Rent_Assistance_Partner";

    public static final String DSS_Max_Pension_Payable_pa = "Centrelink/Max_Pension_Payable_pa";

    public static final String DSS_Actual_Pension_Payable_pa = "Centrelink/Actual_Pension_Payable_pa";

    public static final String DSS_Actual_Pension_Payable_fortnightly = "Centrelink/Actual_Pension_Payable_fortnightly";

    // //////////////////////////////////////////////////////////////////////////
    // Centrelink //
    // //////////////////////////////////////////////////////////////////////////
    public static final String CentrelinkReport_Table = "Centrelink/Report_Table";

}
