/*
 * FinancialSum.java
 *
 * Created on 8 November 2001, 09:55
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 * 
 * static member/function to keep current total for all active clients
 */

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FinancialSum {

    // Map(
    // ClientID,
    // Map(
    // FinancialObjectTypeID,
    // (BigDecimal) TotalAmount
    // )
    // )
    private static Map totals = new HashMap();

    /** Creates new FinancialSum */
    private FinancialSum() {
    }

    /**
     * PersonService
     */
    public static void removePerson(Integer personID) {
        totals.remove(personID);
        System.out.println("FinancialSum.removePerson() " + personID);
    }

    private static Map getPersonMap(Integer personID) {
        Map map = (Map) totals.get(personID);

        if (map == null) {
            map = new HashMap();
            totals.put(personID, map);
        }

        return map;
    }

    /**
     * ObjectTypeID
     */
    public static BigDecimal getValue(Integer personID, int objectTypeID) {
        return getValue(personID, new Integer(objectTypeID));
    }

    public static BigDecimal getValue(Integer personID, Integer objectTypeID) {
        Map map = getPersonMap(personID);
        // if ( map == null ) return null; // will never hapenned

        if (!map.containsKey(objectTypeID))
            map.put(objectTypeID, new BigDecimal(0));

        return (BigDecimal) map.get(objectTypeID);
    }

    public static BigDecimal add(Integer personID, Integer objectTypeID,
            BigDecimal value) {
        BigDecimal currValue = getValue(personID, objectTypeID);
        if (value == null)
            return currValue;

        Map map = getPersonMap(personID);
        BigDecimal newValue = currValue.add(value);
        map.put(objectTypeID, newValue);
        return newValue;
    }

    public static BigDecimal subtract(Integer personID, Integer objectTypeID,
            BigDecimal value) {
        BigDecimal currValue = getValue(personID, objectTypeID);
        if (value == null)
            return currValue;

        Map map = getPersonMap(personID);
        BigDecimal newValue = currValue.subtract(value);
        map.put(objectTypeID, newValue);
        return newValue;
    }

}
