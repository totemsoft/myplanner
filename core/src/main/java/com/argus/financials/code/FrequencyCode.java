/*
 * FrequencyCode.java
 *
 * Created on 7 November 2001, 10:03
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

import com.argus.util.IFrequencyCode;
import com.argus.util.ReferenceCode;

public class FrequencyCode extends Code implements IFrequencyCode {

    public static ReferenceCode getCode(Integer frequencyCodeID) {

        if (frequencyCodeID == null)
            return ReferenceCode.CODE_NONE;

        switch (frequencyCodeID.intValue()) {
        case (iONLY_ONCE):
            return rcONLY_ONCE;
        case (iDAILY):
            return rcDAILY;
        case (iWEEKLY):
            return rcWEEKLY;
        case (iFORTNIGHTLY):
            return rcFORTNIGHTLY;
        case (iTWICE_MONTHLY):
            return rcTWICE_MONTHLY;
        case (iMONTHLY):
            return rcMONTHLY;
        case (iEVERY_OTHER_MONTH):
            return rcEVERY_OTHER_MONTH;
        case (iEVERY_THREE_MONTHS):
            return rcEVERY_THREE_MONTHS;
        case (iEVERY_FOUR_MONTHS):
            return rcEVERY_FOUR_MONTHS;
        case (iHALF_YEARLY):
            return rcHALF_YEARLY;
        case (iYEARLY):
            return rcYEARLY;
        case (iEVERY_OTHER_YEAR):
            return rcEVERY_OTHER_YEAR;
        case (iEVERY_THREE_YEARS):
            return rcEVERY_THREE_YEARS;
        default:
            return ReferenceCode.CODE_NONE;
        }

    }

    private static Map codeMap;

    protected Map getCodeMap() {
        if (codeMap == null) {
            codeMap = new TreeMap();
            initCodeMap();
        }
        return codeMap;
    }

    private static void initCodeMap() {
        codeMap.clear();
        codeMap.put(NONE, VALUE_NONE);

        try {
            Map map = utilityService.getCodes("FrequencyCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.api.ServiceException re) {
        }
    }

    // in 1 year
    public static double getNumberOfPeriods(Integer frequencyCodeID) {
        return frequencyCodeID == null ? 0 : NUMBER_OF_PERIODS[frequencyCodeID
                .intValue()];
    }

    public static java.math.BigDecimal getAnnualAmount(Integer frequencyCodeID,
            java.math.BigDecimal regularAmount) {

        if (frequencyCodeID == null || regularAmount == null)
            return null;

        return regularAmount.multiply(new java.math.BigDecimal(
                getNumberOfPeriods(frequencyCodeID)));

    }

    public static java.math.BigDecimal getAnnualAmount(Integer frequencyCodeID,
            double regularAmount) {
        return getAnnualAmount(frequencyCodeID, new java.math.BigDecimal(
                regularAmount));
    }

    public static java.math.BigDecimal getPeriodAmount(Integer frequencyCodeID,
            java.math.BigDecimal regularAmount) {

        if (frequencyCodeID == null || regularAmount == null)
            return null;

        // ROUND_UP, ROUND_DOWN, ROUND_CEILING, ROUND_FLOOR, ROUND_HALF_UP,
        // ROUND_HALF_DOWN, ROUND_HALF_EVEN, ROUND_UNNECESSARY
        return regularAmount.divide(new java.math.BigDecimal(
                getNumberOfPeriods(frequencyCodeID)),
                java.math.BigDecimal.ROUND_DOWN);
    }

    public static java.math.BigDecimal getPeriodAmount(Integer frequencyCodeID,
            double regularAmount) {
        return getPeriodAmount(frequencyCodeID, new java.math.BigDecimal(
                regularAmount));
    }

    public static java.util.Date getNextDate(java.util.Date nextDate,
            Integer frequencyCodeID) {
        return getNextDate(nextDate, frequencyCodeID, new java.util.Date());
    }

    public static java.util.Date getNextDate(java.util.Date nextDate,
            Integer frequencyCodeID, java.util.Date today) {
        // try to re-calculate new nextDate
        if (nextDate == null || today == null || frequencyCodeID == null
                || FrequencyCode.ONLY_ONCE.equals(frequencyCodeID))
            return nextDate;

        // break condition
        if (today.before(nextDate))
            return nextDate;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(nextDate);
        switch (frequencyCodeID.intValue()) {
        case iDAILY:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.DAY_OF_YEAR, 1);
            break;
        case iWEEKLY:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.DAY_OF_YEAR, 7);
            break;
        case iFORTNIGHTLY:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.DAY_OF_YEAR, 14);
            break;
        case iTWICE_MONTHLY:
            while (today.after(calendar.getTime())) {
                if (calendar.get(calendar.DAY_OF_MONTH) < 15) {
                    calendar.set(calendar.DAY_OF_MONTH, 15);
                } else {
                    calendar.set(calendar.DAY_OF_MONTH, 1); // 0
                    calendar.add(calendar.MONTH, 1);
                }
            }
            break;
        case iMONTHLY:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.MONTH, 1);
            break;
        case iEVERY_OTHER_MONTH:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.MONTH, 2);
            break;
        case iEVERY_THREE_MONTHS:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.MONTH, 3);
            break;
        case iEVERY_FOUR_MONTHS:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.MONTH, 4);
            break;
        case iHALF_YEARLY:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.MONTH, 6);
            break;
        case iYEARLY:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.YEAR, 1);
            break;
        case iEVERY_OTHER_YEAR:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.YEAR, 2);
            break;
        case iEVERY_THREE_YEARS:
            while (today.after(calendar.getTime()))
                calendar.add(calendar.YEAR, 3);
            break;
        // case iONLY_ONCE: ;
        default:
            return nextDate;// BadArgumentException.BAD_DOUBLE_VALUE;
        }

        return calendar.getTime();

    }

}
