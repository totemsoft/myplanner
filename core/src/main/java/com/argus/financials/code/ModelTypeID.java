/*
 * ModelTypeID.java
 *
 * Created on 13 July 2002, 09:22
 */

package com.argus.financials.code;

import com.argus.util.ReferenceCode;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface ModelTypeID {

    // ModelType 1..17
    public final static int iCURRENT_POSITION_CALC = 1;

    public final static int iINSURANCE_NEEDS = 2;

    public final static int iPREMIUM_CALC = 3;

    public final static int iINVESTMENT_GEARING = 4;

    public final static int iPROJECTED_WEALTH = 5;

    public final static int iINVESTMENT_PROPERTIES = 6;

    public final static int iLOAN_MORTGAGE_CALC = 7;

    public final static int iALLOCATED_PENSION = 8;

    public final static int iETP_ROLLOVER = 9;

    public final static int iSUPERANNUATION_RBL = 10;

    public final static int iRETIREMENT_CALC = 11;

    public final static int iRETIREMENT_HOME = 12;

    public final static int iPAYG_CALC = 13;

    public final static int iCGT_CALC = 14;

    public final static int iSOCIAL_SECURITY_CALC = 15;

    public final static int iCENTRELINK_CALC = 16;

    public final static int iSTRATEGY_CALC = 17;

    public final static Integer CURRENT_POSITION_CALC = new Integer(iCURRENT_POSITION_CALC);

    public final static Integer INSURANCE_NEEDS = new Integer(iINSURANCE_NEEDS);

    public final static Integer PREMIUM_CALC = new Integer(iPREMIUM_CALC);

    public final static Integer INVESTMENT_GEARING = new Integer(
            iINVESTMENT_GEARING);

    public final static Integer PROJECTED_WEALTH = new Integer(
            iPROJECTED_WEALTH);

    public final static Integer INVESTMENT_PROPERTIES = new Integer(
            iINVESTMENT_PROPERTIES);

    public final static Integer LOAN_MORTGAGE_CALC = new Integer(
            iLOAN_MORTGAGE_CALC);

    public final static Integer ALLOCATED_PENSION = new Integer(
            iALLOCATED_PENSION);

    public final static Integer ETP_ROLLOVER = new Integer(iETP_ROLLOVER);

    public final static Integer SUPERANNUATION_RBL = new Integer(
            iSUPERANNUATION_RBL);

    public final static Integer RETIREMENT_CALC = new Integer(iRETIREMENT_CALC);

    public final static Integer RETIREMENT_HOME = new Integer(iRETIREMENT_HOME);

    public final static Integer PAYG_CALC = new Integer(iPAYG_CALC);

    public final static Integer CGT_CALC = new Integer(iCGT_CALC);

    public final static Integer SOCIAL_SECURITY_CALC = new Integer(
            iCENTRELINK_CALC);

    public final static Integer CENTRELINK_CALC = new Integer(iCENTRELINK_CALC);

    public final static Integer STRATEGY_CALC = new Integer(iSTRATEGY_CALC);

    public final static ReferenceCode rcCURRENT_POSITION_CALC = new ReferenceCode(
            CURRENT_POSITION_CALC, "Current Position");

    // codes.add( new ReferenceCode( INSURANCE_NEEDS, "Insurance Needs" ) );
    // codes.add( new ReferenceCode( PREMIUM_CALC, "Premium Calculator" ) );
    public final static ReferenceCode rcINVESTMENT_GEARING = new ReferenceCode(
            INVESTMENT_GEARING, "Investment Gearing");

    // codes.add( new ReferenceCode( PROJECTED_WEALTH, "Projected Wealth" ) );
    // codes.add( new ReferenceCode( INVESTMENT_PROPERTIES, "Investment
    // Properties" ) );
    public final static ReferenceCode rcLOAN_MORTGAGE_CALC = new ReferenceCode(
            LOAN_MORTGAGE_CALC, "Loan & Mortgage Calculator");

    public final static ReferenceCode rcALLOCATED_PENSION = new ReferenceCode(
            ALLOCATED_PENSION, "Allocated Pension");

    public final static ReferenceCode rcETP_ROLLOVER = new ReferenceCode(
            ETP_ROLLOVER, "ETP & Rollover");

    // codes.add( new ReferenceCode( SUPERANNUATION_RBL, "Superannuation RBL" )
    // );
    // codes.add( new ReferenceCode( RETIREMENT_CALC, "Retirement Calculator" )
    // );
    // codes.add( new ReferenceCode( RETIREMENT_HOME, "Retirement Home" ) );
    // codes.add( new ReferenceCode( PAYG_CALC, "PAYG Calculator" ) );
    // codes.add( new ReferenceCode( CGT_CALC, "Capital Gains Tax (CGT)" ) );
    public final static ReferenceCode rcSOCIAL_SECURITY_CALC = new ReferenceCode(
            SOCIAL_SECURITY_CALC, "Social Security");

    public final static ReferenceCode rcCENTRELINK_CALC = new ReferenceCode(
            CENTRELINK_CALC, "Centrelink Calculator");

    public final static ReferenceCode rcSTRATEGY_CALC = new ReferenceCode(
            STRATEGY_CALC, "Strategy Calculator");

}
