/*
 * LinkObjectType.java
 *
 * Created on 21 August 2001, 16:20
 */

package com.argus.financials.api.code;

/**
 * 
 * @author valeri chibaev
 * @version
 * 
 * XXXXXXYYY ( 6(ObjectTypeID1) + 3(ObjectTypeID2) = 9 digits max for int)
 * 
 */

public interface LinkObjectTypeConstant {

    // LinkObjectTypeID
    // (1001, 1, 1, 'Link person to address')
    public static final int PERSON_2_PERSON = 1001;

    // (1005, 1, 5, 'Link person to address')
    public static final int PERSON_2_ADDRESS = 1005;

    // (1006, 1, 6, 'Link person to ContactMedia')
    public static final int PERSON_2_CONTACT_MEDIA = 1006;

    // (1005006, 1005, 6, 'Link person-address to ContactMedia')
    public static final int PERSON$ADDRESS_2_CONTACT_MEDIA = 1005006;

    // (1004, 1, 4, 'Link person to business')
    public static final int PERSON_2_BUSINESS = 1004;

    // (1004030, 1004, 30, 'Link person_business to occupation')
    public static final int PERSON$BUSINESS_2_OCCUPATION = 1004030;

    // (1007, 1, 7, 'Link Person to Relationship')
    public static final int PERSON_2_RELATIONSHIP = 1007;

    // (1008, 1, 8, 'Link Person to RelationshipFinance')
    public static final int PERSON_2_RELATIONSHIP_FINANCE = 1008;

    // (1020, 1, 20, 'Link table (person to financial)')
    public static final int PERSON_2_FINANCIAL = 1020;

    // (1021, 1, 21, 'Link table (person to asset)')
    public static final int PERSON_2_ASSET = 1021;

    // (1022, 1, 22, 'Link table (person to regular)')
    public static final int PERSON_2_REGULAR = 1022;

    // (1023, 1, 23, 'Link table (person to anticipated)')
    // public static final int PERSON_2_ANTICIPATED = 1023;

    // (1010, 1, 10, 'Link table (person to AssetCash)')
    public static final int PERSON_2_ASSETCASH = 1010;

    // (1011, 1, 11, 'Link table (person to AssetInvestment)')
    public static final int PERSON_2_ASSETINVESTMENT = 1011;

    // (1012, 1, 12, 'Link table (person to AssetPersonal)')
    public static final int PERSON_2_ASSETPERSONAL = 1012;

    // (1013, 1, 13, 'Link table (person to AssetSuperannuation)')
    public static final int PERSON_2_ASSETSUPERANNUATION = 1013;

    // (1014, 1, 14, 'Link table (person to Regular(Expence))')
    public static final int PERSON_2_REGULAREXPENCE = 1014;

    // (1015, 1, 15, 'Link table (person to Regular(Income))')
    public static final int PERSON_2_REGULARINCOME = 1015;

    // (1016, 1, 16, 'Link table (person to Regular(Liability))')
    public static final int PERSON_2_LIABILITY = 1016;

    // (1017, 1, 17, 'Link table (person to Insurance)')
    public static final int PERSON_2_INSURANCE = 1017;

    // (1018, 1, 18, 'Link table (person to Regular(Tax Offsets & Credits))');
    public static final int PERSON_2_TAXOFFSET = 1018;

    // (1018, 1, 19, 'Link table (person to Asset(Income Stream))');
    public static final int PERSON_2_INCOMESTREAM = 1019;

    // (1024, 1, 24, 'Link Person to Survey')
    public static final int PERSON_2_SURVEY = 1024;

    // (1025, 1, 25, 'Link Person to Comment')
    public static final int PERSON_2_COMMENT = 1025;

    // (1025026, 1025, 26, 'Link Person-Comment to FinancialGoal')
    public static final int PERSON$COMMENT_2_FINANCIALGOAL = 1025026;

    // (1026, 1, 26, 'Link Person to FinancialGoal')
    public static final int PERSON_2_FINANCIALGOAL = 1026;

    // (1029, 1, 29, 'Link Person to Model');
    public static final int PERSON_2_MODEL = 1029;

    // 1031, 1, 31, 'Link table (Person to PlanData)';
    public static final int PERSON_2_PLAN = 1031;

    // (1032, 1, 32, 'Link Person to StrategyGroup');
    public static final int PERSON_2_STRATEGYGROUP = 1032;

    // (2003, 2, 3, 'Link User to ClientView')
    public static final int USER_2_CLIENT = 2003;

    // (3001, 3, 1, 'Link ClientView to Person')
    public static final int CLIENT_2_PERSON = 3001;

    // (3003, 3, 3, 'Link ClientView to ClientView')
    public static final int CLIENT_2_CLIENT = 3003;

    // (3028, 3, 28, 'Link ClientView to Estate')
    public static final int CLIENT_2_ESTATE = 3028;

    // (3001007, 3001, 7, 'Link ClientService-Person to Relationship')
    public static final int CLIENT$PERSON_2_RELATIONSHIP = 3001007;

    // (3001008, 3001, 8, 'Link ClientService-Person to RelationshipFinance')
    public static final int CLIENT$PERSON_2_RELATIONSHIP_FINANCE = 3001008;

    // (24027, 24, 27, 'Link Survey to RiskProfile')
    public static final int SURVEY_2_RISKPROFILE = 24027;

}
