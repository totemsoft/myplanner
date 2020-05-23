/*
 * FrequencyCode.java
 *
 * Created on 7 November 2001, 10:03
 */

package au.com.totemsoft.util;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 
 */

public interface IFrequencyCode {

    public static final int iONLY_ONCE = 1;
    public static final int iDAILY = 2;
    public static final int iWEEKLY = 3;
    public static final int iFORTNIGHTLY = 4;
    public static final int iTWICE_MONTHLY = 5;
    public static final int iMONTHLY = 6;
    public static final int iEVERY_OTHER_MONTH = 7;
    public static final int iEVERY_THREE_MONTHS = 8;
    public static final int iEVERY_FOUR_MONTHS = 9;
    public static final int iHALF_YEARLY = 10;
    public static final int iYEARLY = 11;
    public static final int iEVERY_OTHER_YEAR = 12;
    public static final int iEVERY_THREE_YEARS = 13;

    public static final double [] NUMBER_OF_PERIODS = {
        0,
        1,      //case iONLY_ONCE: return 1;
        365,    //case iDAILY: return 365;
        52,     //case iWEEKLY: return 52;
        26,     //case iFORTNIGHTLY: return 26;
        24,     //case iTWICE_MONTHLY: return 24;
        12,     //case iMONTHLY: return 12;
        6,      //case iEVERY_OTHER_MONTH: return 6;
        4,      //case iEVERY_THREE_MONTHS: return 4;
        3,      //case iEVERY_FOUR_MONTHS: return 3;
        2,      //case iHALF_YEARLY: return 2;
        1,      //case iYEARLY: return 1;
        .5,     //case iEVERY_OTHER_YEAR: return .5;
        1./3.,  //case iEVERY_THREE_YEARS : return 1./3.;
    };
       
    public static final Integer ONLY_ONCE = new Integer( iONLY_ONCE );
    public static final Integer DAILY = new Integer( iDAILY );
    public static final Integer WEEKLY = new Integer( iWEEKLY );
    public static final Integer FORTNIGHTLY = new Integer( iFORTNIGHTLY );
    public static final Integer TWICE_MONTHLY = new Integer( iTWICE_MONTHLY );
    public static final Integer MONTHLY = new Integer( iMONTHLY );
    public static final Integer EVERY_OTHER_MONTH = new Integer( iEVERY_OTHER_MONTH );
    public static final Integer EVERY_THREE_MONTHS = new Integer( iEVERY_THREE_MONTHS );
    public static final Integer EVERY_FOUR_MONTHS = new Integer( iEVERY_FOUR_MONTHS );
    public static final Integer HALF_YEARLY = new Integer( iHALF_YEARLY );
    public static final Integer YEARLY = new Integer( iYEARLY );
    public static final Integer EVERY_OTHER_YEAR = new Integer( iEVERY_OTHER_YEAR );
    public static final Integer EVERY_THREE_YEARS = new Integer( iEVERY_THREE_YEARS );
    

    public static final ReferenceCode rcONLY_ONCE = new ReferenceCode( iONLY_ONCE, "Only Once" );
    public static final ReferenceCode rcDAILY = new ReferenceCode( iDAILY, "Daily" );
    public static final ReferenceCode rcWEEKLY = new ReferenceCode( iWEEKLY, "Weekly" );
    public static final ReferenceCode rcFORTNIGHTLY = new ReferenceCode( iFORTNIGHTLY, "Fortnightly" );
    public static final ReferenceCode rcTWICE_MONTHLY = new ReferenceCode( iTWICE_MONTHLY, "Twice Monthly" );
    public static final ReferenceCode rcMONTHLY = new ReferenceCode( iMONTHLY, "Monthly" );
    public static final ReferenceCode rcEVERY_OTHER_MONTH = new ReferenceCode( iEVERY_OTHER_MONTH, "Every Other Month" );
    public static final ReferenceCode rcEVERY_THREE_MONTHS = new ReferenceCode( iEVERY_THREE_MONTHS, "Quarterly" ); // "Every Three Months"
    public static final ReferenceCode rcEVERY_FOUR_MONTHS = new ReferenceCode( iEVERY_FOUR_MONTHS, "Every Four Months" );
    public static final ReferenceCode rcHALF_YEARLY = new ReferenceCode( iHALF_YEARLY, "Half Yearly" );
    public static final ReferenceCode rcYEARLY = new ReferenceCode( iYEARLY, "Yearly" );
    public static final ReferenceCode rcEVERY_OTHER_YEAR = new ReferenceCode( iEVERY_OTHER_YEAR, "Every Other Year" );
    public static final ReferenceCode rcEVERY_THREE_YEARS = new ReferenceCode( iEVERY_THREE_YEARS, "Every Three Years" );
    
}
