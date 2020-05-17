/*
 * LifeExpectancy.java
 *
 * Created on 18 October 2001, 14:18
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

import com.argus.financials.api.CommonConstants;
import com.argus.financials.code.SexCode;
import com.argus.financials.service.ServiceAware;

public class LifeExpectancy
    extends ServiceAware
{

    private static Map map;

    static {
        if (map == null) {
            map = new HashMap();
            load();
        }
    }

    private static void load() {
        map.clear();
        Map lifeMap = null;

        try {
            Integer countryCodeID = new com.argus.financials.code.CountryCode()
                    .getCodeID(com.argus.financials.config.FPSLocale
                            .getInstance().getDisplayCountry());
            lifeMap = utilityService.getLifeExpectancy(countryCodeID);
            if (lifeMap == null)
                return;
        } catch (Exception e) { // if rmi not configured/started
            // e.printStackTrace();
            System.out.println("LOCAL: getLifeExpectancy(): " + e);
            lifeMap = getLifeExpectancy();
        } catch (Error e) { // if accessed from Applet, and no rmi server
                            // available
            // e.printStackTrace();
            System.out.println("LOCAL: getLifeExpectancy(): " + e);
            lifeMap = getLifeExpectancy();
        }

        map.putAll(lifeMap);
    }

    /**
     * 
     */
    private static Map getLifeExpectancy() {
        Map leMap = new TreeMap();

        // Age Female Male
        leMap.put(new Integer(15), new double[] { 66.97, 61.38 });
        leMap.put(new Integer(16), new double[] { 65.98, 60.41 });
        leMap.put(new Integer(17), new double[] { 65, 59.44 });
        leMap.put(new Integer(18), new double[] { 64.02, 58.49 });
        leMap.put(new Integer(19), new double[] { 63.04, 57.55 });
        leMap.put(new Integer(20), new double[] { 62.07, 56.61 });
        leMap.put(new Integer(21), new double[] { 61.09, 55.68 });
        leMap.put(new Integer(22), new double[] { 60.12, 54.75 });
        leMap.put(new Integer(23), new double[] { 59.14, 53.81 });
        leMap.put(new Integer(24), new double[] { 58.16, 52.88 });
        leMap.put(new Integer(25), new double[] { 57.18, 51.94 });
        leMap.put(new Integer(26), new double[] { 56.21, 51.01 });
        leMap.put(new Integer(27), new double[] { 55.23, 50.07 });
        leMap.put(new Integer(28), new double[] { 54.25, 49.14 });
        leMap.put(new Integer(29), new double[] { 53.27, 48.2 });
        leMap.put(new Integer(30), new double[] { 52.3, 47.26 });
        leMap.put(new Integer(31), new double[] { 51.32, 46.32 });
        leMap.put(new Integer(32), new double[] { 50.35, 45.38 });
        leMap.put(new Integer(33), new double[] { 49.38, 44.44 });
        leMap.put(new Integer(34), new double[] { 48.41, 43.5 });
        leMap.put(new Integer(35), new double[] { 47.44, 42.57 });
        leMap.put(new Integer(36), new double[] { 46.47, 41.63 });
        leMap.put(new Integer(37), new double[] { 45.5, 40.69 });
        leMap.put(new Integer(38), new double[] { 44.53, 39.75 });
        leMap.put(new Integer(39), new double[] { 43.56, 38.81 });
        leMap.put(new Integer(40), new double[] { 42.6, 37.88 });
        leMap.put(new Integer(41), new double[] { 41.64, 36.94 });
        leMap.put(new Integer(42), new double[] { 40.68, 36.01 });
        leMap.put(new Integer(43), new double[] { 39.72, 35.08 });
        leMap.put(new Integer(44), new double[] { 38.76, 34.15 });
        leMap.put(new Integer(45), new double[] { 37.81, 33.22 });
        leMap.put(new Integer(46), new double[] { 36.86, 32.3 });
        leMap.put(new Integer(47), new double[] { 35.92, 31.38 });
        leMap.put(new Integer(48), new double[] { 34.98, 30.46 });
        leMap.put(new Integer(49), new double[] { 34.04, 29.55 });
        leMap.put(new Integer(50), new double[] { 33.11, 28.64 });
        leMap.put(new Integer(51), new double[] { 32.18, 27.74 });
        leMap.put(new Integer(52), new double[] { 31.26, 26.85 });
        leMap.put(new Integer(53), new double[] { 30.34, 25.97 });
        leMap.put(new Integer(54), new double[] { 29.43, 25.09 });
        leMap.put(new Integer(55), new double[] { 28.53, 24.22 });
        leMap.put(new Integer(56), new double[] { 27.63, 23.36 });
        leMap.put(new Integer(57), new double[] { 26.74, 22.52 });
        leMap.put(new Integer(58), new double[] { 25.86, 21.68 });
        leMap.put(new Integer(59), new double[] { 24.98, 20.86 });
        leMap.put(new Integer(60), new double[] { 24.11, 20.05 });
        leMap.put(new Integer(61), new double[] { 23.25, 19.25 });
        leMap.put(new Integer(62), new double[] { 22.39, 18.46 });
        leMap.put(new Integer(63), new double[] { 21.54, 17.7 });
        leMap.put(new Integer(64), new double[] { 20.7, 16.94 });
        leMap.put(new Integer(65), new double[] { 19.88, 16.21 });
        leMap.put(new Integer(66), new double[] { 19.06, 15.49 });
        leMap.put(new Integer(67), new double[] { 18.25, 14.79 });
        leMap.put(new Integer(68), new double[] { 17.46, 14.11 });
        leMap.put(new Integer(69), new double[] { 16.67, 13.44 });
        leMap.put(new Integer(70), new double[] { 15.9, 12.8 });
        leMap.put(new Integer(71), new double[] { 15.14, 12.17 });
        leMap.put(new Integer(72), new double[] { 14.4, 11.56 });
        leMap.put(new Integer(73), new double[] { 13.67, 10.96 });
        leMap.put(new Integer(74), new double[] { 12.96, 10.38 });
        leMap.put(new Integer(75), new double[] { 12.26, 9.82 });
        leMap.put(new Integer(76), new double[] { 11.58, 9.27 });
        leMap.put(new Integer(77), new double[] { 10.92, 8.74 });
        leMap.put(new Integer(78), new double[] { 10.28, 8.24 });
        leMap.put(new Integer(79), new double[] { 9.67, 7.76 });
        leMap.put(new Integer(80), new double[] { 9.09, 7.3 });
        leMap.put(new Integer(81), new double[] { 8.53, 6.87 });
        leMap.put(new Integer(82), new double[] { 8, 6.46 });
        leMap.put(new Integer(83), new double[] { 7.48, 6.08 });
        leMap.put(new Integer(84), new double[] { 7, 5.71 });
        leMap.put(new Integer(85), new double[] { 6.53, 5.4 });
        leMap.put(new Integer(86), new double[] { 6.1, 5.1 });
        leMap.put(new Integer(87), new double[] { 5.69, 4.82 });
        leMap.put(new Integer(88), new double[] { 5.32, 4.57 });
        leMap.put(new Integer(89), new double[] { 4.98, 4.53 });
        leMap.put(new Integer(90), new double[] { 4.67, 4.16 });
        leMap.put(new Integer(91), new double[] { 4.39, 3.99 });
        leMap.put(new Integer(92), new double[] { 4.15, 3.86 });
        leMap.put(new Integer(93), new double[] { 3.93, 3.73 });
        leMap.put(new Integer(94), new double[] { 3.72, 3.62 });
        leMap.put(new Integer(95), new double[] { 3.54, 3.5 });
        leMap.put(new Integer(96), new double[] { 3.37, 3.39 });
        leMap.put(new Integer(97), new double[] { 3.21, 3.28 });
        leMap.put(new Integer(98), new double[] { 3.07, 3.18 });
        leMap.put(new Integer(99), new double[] { 2.93, 3.07 });
        leMap.put(new Integer(100), new double[] { 2.81, 2.98 });
        leMap.put(new Integer(101), new double[] { 2.7, 2.88 });
        leMap.put(new Integer(102), new double[] { 2.59, 2.79 });
        leMap.put(new Integer(103), new double[] { 2.5, 2.71 });
        leMap.put(new Integer(104), new double[] { 2.41, 2.64 });

        return leMap;

    }

    /**
     * get
     */
    public static double getValue(int age, Integer sexCodeID) {
        return getValue(new Integer(age), sexCodeID);
    }

    public static double getValue(Integer age, Integer sexCodeID) {
        if (sexCodeID == null || age == null || age.intValue() < 0)
            return CommonConstants.UNKNOWN_VALUE;
        // throw new IllegalArgumentException( "There is no value for age " +
        // age + " years old ("+sexCodeID+")." );

        double[] le = (double[]) map.get(age);
        if (le == null)
            return CommonConstants.UNKNOWN_VALUE;
        // throw new IllegalArgumentException( "There is no value for age " +
        // age + " years old." );

        return sexCodeID.equals(SexCode.FEMALE) ? le[0] : le[1];
    }

    /**
     * helper methodes
     */

}
