/*
 * PensionValuationFactor.java
 *
 * Created on 13 December 2001, 15:18
 */

package com.argus.financials.projection.data;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.etc.BadArgumentException;

public class PensionValuation {

    private static Map map;

    static {
        if (map == null) {
            map = new HashMap();
            load();
        }
    }

    private static void load() {
        map.clear();
        Map pvMap = null;

        try {
            pvMap = getPensionValuation();
            // pvMap = RmiParams.getUtility().getPensionValuation(
            // FPSLocale.getCountryCodeID() );
            if (pvMap == null)
                return;
        } catch (Exception e) { // if rmi not configured/started
            // e.printStackTrace();
            System.out.println("LOCAL: getPensionValuation()");
            pvMap = getPensionValuation();
        } catch (Error e) { // if accessed from Applet
            // e.printStackTrace();
            System.out.println("LOCAL: getPensionValuation()");
            pvMap = getPensionValuation();
        }

        map.putAll(pvMap);
    }

    /**
     * 
     */
    private static Map getPensionValuation() {
        Map pvMap = new TreeMap();

        // Age Min Max
        pvMap.put(new Integer(20), new double[] { 28.6, 10 });
        pvMap.put(new Integer(21), new double[] { 28.5, 10 });
        pvMap.put(new Integer(22), new double[] { 28.3, 10 });
        pvMap.put(new Integer(23), new double[] { 28.1, 10 });
        pvMap.put(new Integer(24), new double[] { 28., 10 });
        pvMap.put(new Integer(25), new double[] { 27.8, 10 });
        pvMap.put(new Integer(26), new double[] { 27.6, 10 });
        pvMap.put(new Integer(27), new double[] { 27.5, 10 });
        pvMap.put(new Integer(28), new double[] { 27.3, 10 });
        pvMap.put(new Integer(29), new double[] { 27.1, 10 });
        pvMap.put(new Integer(30), new double[] { 26.9, 10 });
        pvMap.put(new Integer(31), new double[] { 26.7, 10 });
        pvMap.put(new Integer(32), new double[] { 26.5, 10 });
        pvMap.put(new Integer(33), new double[] { 26.3, 10 });
        pvMap.put(new Integer(34), new double[] { 26., 10 });
        pvMap.put(new Integer(35), new double[] { 25.8, 10 });
        pvMap.put(new Integer(36), new double[] { 25.6, 10 });
        pvMap.put(new Integer(37), new double[] { 25.3, 10 });
        pvMap.put(new Integer(38), new double[] { 25.1, 10 });
        pvMap.put(new Integer(39), new double[] { 24.8, 10 });
        pvMap.put(new Integer(40), new double[] { 24.6, 10 });
        pvMap.put(new Integer(41), new double[] { 24.3, 10 });
        pvMap.put(new Integer(42), new double[] { 24., 10 });
        pvMap.put(new Integer(43), new double[] { 23.7, 10 });
        pvMap.put(new Integer(44), new double[] { 23.4, 10 });
        pvMap.put(new Integer(45), new double[] { 23.1, 10 });
        pvMap.put(new Integer(46), new double[] { 22.8, 10 });
        pvMap.put(new Integer(47), new double[] { 22.5, 10 });
        pvMap.put(new Integer(48), new double[] { 22.2, 10 });
        pvMap.put(new Integer(49), new double[] { 21.9, 10 });
        pvMap.put(new Integer(50), new double[] { 21.5, 9.9 });
        pvMap.put(new Integer(51), new double[] { 21.2, 9.9 });
        pvMap.put(new Integer(52), new double[] { 20.9, 9.8 });
        pvMap.put(new Integer(53), new double[] { 20.5, 9.7 });
        pvMap.put(new Integer(54), new double[] { 20.1, 9.7 });
        pvMap.put(new Integer(55), new double[] { 19.8, 9.6 });
        pvMap.put(new Integer(56), new double[] { 19.4, 9.5 });
        pvMap.put(new Integer(57), new double[] { 19., 9.4 });
        pvMap.put(new Integer(58), new double[] { 18.6, 9.3 });
        pvMap.put(new Integer(59), new double[] { 18.2, 9.1 });
        pvMap.put(new Integer(60), new double[] { 17.8, 9. });
        pvMap.put(new Integer(61), new double[] { 17.4, 8.9 });
        pvMap.put(new Integer(62), new double[] { 17., 8.7 });
        pvMap.put(new Integer(63), new double[] { 16.6, 8.5 });
        pvMap.put(new Integer(64), new double[] { 16.2, 8.3 });
        pvMap.put(new Integer(65), new double[] { 15.7, 8.1 });
        pvMap.put(new Integer(66), new double[] { 15.3, 7.9 });
        pvMap.put(new Integer(67), new double[] { 14.9, 7.6 });
        pvMap.put(new Integer(68), new double[] { 14.4, 7.3 });
        pvMap.put(new Integer(69), new double[] { 14., 7. });
        pvMap.put(new Integer(70), new double[] { 13.5, 6.6 });
        pvMap.put(new Integer(71), new double[] { 13.1, 6.2 });
        pvMap.put(new Integer(72), new double[] { 12.6, 5.8 });
        pvMap.put(new Integer(73), new double[] { 12.2, 5.4 });
        pvMap.put(new Integer(74), new double[] { 11.7, 4.8 });
        pvMap.put(new Integer(75), new double[] { 11.3, 4.3 });
        pvMap.put(new Integer(76), new double[] { 10.8, 3.7 });
        pvMap.put(new Integer(77), new double[] { 10.4, 3. });
        pvMap.put(new Integer(78), new double[] { 10., 2.2 });
        pvMap.put(new Integer(79), new double[] { 9.5, 1.4 });
        pvMap.put(new Integer(80), new double[] { 9.1, 1. });
        pvMap.put(new Integer(81), new double[] { 8.7, 1. });
        pvMap.put(new Integer(82), new double[] { 8.3, 1. });
        pvMap.put(new Integer(83), new double[] { 7.9, 1. });
        pvMap.put(new Integer(84), new double[] { 7.5, 1. });
        pvMap.put(new Integer(85), new double[] { 7.1, 1. });
        pvMap.put(new Integer(86), new double[] { 6.8, 1. });
        pvMap.put(new Integer(87), new double[] { 6.4, 1. });
        pvMap.put(new Integer(88), new double[] { 6.1, 1. });
        pvMap.put(new Integer(89), new double[] { 5.8, 1. });
        pvMap.put(new Integer(90), new double[] { 5.5, 1. });
        pvMap.put(new Integer(91), new double[] { 5.3, 1. });
        pvMap.put(new Integer(92), new double[] { 5., 1. });
        pvMap.put(new Integer(93), new double[] { 4.8, 1. });
        pvMap.put(new Integer(94), new double[] { 4.6, 1. });
        pvMap.put(new Integer(95), new double[] { 4.4, 1. });
        pvMap.put(new Integer(96), new double[] { 4.2, 1. });
        pvMap.put(new Integer(97), new double[] { 4., 1. });
        pvMap.put(new Integer(98), new double[] { 3.8, 1. });
        pvMap.put(new Integer(99), new double[] { 3.7, 1. });
        pvMap.put(new Integer(100), new double[] { 3.5, 1. });
        pvMap.put(new Integer(101), new double[] { 3.5, 1. });
        pvMap.put(new Integer(102), new double[] { 3.5, 1. });
        pvMap.put(new Integer(103), new double[] { 3.5, 1. });
        pvMap.put(new Integer(104), new double[] { 3.5, 1. });

        return pvMap;

    }

    /**
     * get
     */
    public static double getValue(int age, boolean min)
            throws BadArgumentException {
        return getValue(new Integer(age), min);
    }

    public static double getValue(Integer age, boolean min)
            throws BadArgumentException {
        double[] pv = (double[]) map.get(age);
        if (pv == null)
            throw new BadArgumentException("There is no value for age "
                    + age.intValue() + " years old.");
        return min ? pv[0] : pv[1];
    }

}
