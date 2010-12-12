/*
 * ObjectType.java
 *
 * Created on 21 August 2001, 16:18
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public interface ObjectTypeConstant {

    // ObjectTypeID
    // (1, 'PersonService table')
    public static final int PERSON = 1;

    // (2, 'UserService table')
    public static final int USER_PERSON = 2;

    // (3, 'ClientService table')
    public static final int CLIENT_PERSON = 3;

    // (4, 'BusinessService table')
    public static final int BUSINESS = 4;

    // (5, 'Address table')
    public static final int ADDRESS = 5;

    // (6, 'ContactMedia table')
    public static final int CONTACT_MEDIA = 6;

    // (7, 'RelationshipCode table')
    public static final int RELATIONSHIP = 7;

    // (8, 'RelationshipFinanceCode table')
    public static final int RELATIONSHIP_FINANCE = 8;

    // (9, 'DependentPerson table') gone now
    // public static final int DEPENDENT_PERSON = 9;
    // (10, 'Asset table')
    public static final int ASSET_CASH = 10;

    // (11, 'Assett table')
    public static final int ASSET_INVESTMENT = 11;

    // (12, 'Asset table')
    public static final int ASSET_PERSONAL = 12;

    // (13, 'Asset table')
    public static final int ASSET_SUPERANNUATION = 13;

    // (14, 'Regular table')
    public static final int REGULAR_INCOME = 14;

    // (15, 'Regular table')
    public static final int REGULAR_EXPENSE = 15;

    // (16, 'Liability table')
    public static final int LIABILITY = 16;

    // (17, 'Insurance table')
    public static final int INSURANCE = 17;

    // (18, 'Regular table')
    public static final int TAX_OFFSET = 18;

    // (19, 'CashFlow table') does not exists (calculated financial item)
    // public static final int CASHFLOW = 19;
    // (19, 'asset(IncomeStream) table')
    public static final int INCOME_STREAM = 19;

    // (20, 'Financial table')
    public static final int FINANCIAL = 20;

    // (21, 'Asset table')
    public static final int ASSET = 21;

    // (22, 'Regular table')
    public static final int REGULAR = 22;

    // (23, 'Anticipated table') gone now
    // public static final int ANTICIPATED = 23;
    // (24, 'Survey table')
    public static final int SURVEY = 24;

    // (25, 'Comment table')
    public static final int COMMENT = 25;

    // (26, 'FinancialGoal table')
    public static final int FINANCIAL_GOAL = 26;

    // (27, 'RiskProfile table')
    public static final int RISK_PROFILE = 27;

    // (28, 'PersonEstate table')
    public static final int ESTATE = 28;

    // (29, 'Model table');
    public static final int MODEL = 29;

    // (30, 'PersonOccupation table');
    public static final int OCCUPATION = 30;

    // (31, 'PlanData table');
    public static final int PLAN = 31;

    // (32, 'StrategyModel table');
    public static final int STRATEGYGROUP = 32;

}
