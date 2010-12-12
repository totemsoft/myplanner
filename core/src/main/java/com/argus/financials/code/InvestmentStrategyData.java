/*
 * InvestmentStrategyData.java
 *
 * Created on 28 September 2001, 10:18
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class InvestmentStrategyData extends InvestmentStrategyCode {

    //
    private static double DELTA = 0;// .1;

    private final static Integer CASH_ID = new Integer(4);

    private final static Integer FIXED_ID = new Integer(5);

    private final static Integer LISTED_ID = new Integer(6);

    private final static Integer AUSSIE_ID = new Integer(7);

    private final static Integer INTNL_ID = new Integer(8);

    protected final static Integer ANY_1_YEAR_MIN_ID = new Integer(9);

    protected final static Integer ANY_1_YEAR_MAX_ID = new Integer(10);

    protected final static Integer ANY_3_YEAR_MIN_ID = new Integer(11);

    protected final static Integer ANY_3_YEAR_MAX_ID = new Integer(12);

    protected final static Integer LONG_TERM_MIN_ID = new Integer(13);

    protected final static Integer LONG_TERM_MAX_ID = new Integer(14);

    protected final static Integer YEAR_1_ID = new Integer(15);

    protected final static Integer YEAR_2_ID = new Integer(16);

    protected final static Integer YEAR_3_ID = new Integer(17);

    protected final static Integer YEAR_4_ID = new Integer(18);

    protected final static Integer YEAR_5_ID = new Integer(19);

    protected final static Integer YEAR_6_ID = new Integer(20);

    protected final static Integer YEAR_7_ID = new Integer(21);

    //
    private static Object[][] rawAllocationData;

    //
    private static String[] allocationHistLabels = new String[] {
            "<html><body>In any<br>1 year</body></html>",
            "<html><body>In any<br>2 out of<br>3 years</body></html>",
            "<html><body>Long Term<br>Return p.a.</body></html>" };

    //
    private static Object[][] rawAllocationHistData;

    // 7 years cycle
    private static Object[][] rawAllocationHistDataFull;

    /**
     * 2002/06/12 valeri chibaev default Fiducian color scheme for investments
     */
    public static final Color COLOR_CASH = new Color(0xff, 0x00, 0x00);

    public static final Color COLOR_AUS_FIXED = new Color(0xff, 0x80, 0x80);

    public static final Color COLOR_INTNL_FIXED = new Color(0xff, 0xff, 0xcc);

    public static final Color COLOR_CPI_FIXED = new Color(0xcc, 0x99, 0xff);

    public static final Color COLOR_AUS_SHARES = new Color(0x00, 0xff, 0x00);

    public static final Color COLOR_INTNL_SHARES = new Color(0xcc, 0xff, 0xcc);

    public static final Color COLOR_PROPERTY = new Color(0x33, 0xcc, 0xcc);

    public static final Color COLOR_MORTGAGES = new Color(0xff, 0x99, 0x00);

    public static final Color COLOR_TRADED_POLICIES = new Color(0xff, 0xff,
            0x00);

    private static Color[] colors = new Color[] { COLOR_CASH,
            COLOR_INTNL_FIXED, COLOR_PROPERTY, COLOR_AUS_SHARES,
            COLOR_INTNL_SHARES, COLOR_AUS_FIXED, COLOR_CPI_FIXED,
            COLOR_INTNL_SHARES, COLOR_MORTGAGES, COLOR_TRADED_POLICIES };

    //
    private static String[] rawAllocationDataLabels = new String[] { "Cash",
            "Fixed Interest", "Listed Property", "Aust. Shares",
            "Intnl. Shares", };

    private static Map allocationData;

    private static Vector allocationDataLabels;

    private static void initData() throws java.lang.NumberFormatException {

        if (params == null)
            return;

        try {
            rawAllocationData = new Object[][] {
                    {
                            ULTRA_CONSERVATIVE,
                            new double[] {
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + CASH_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + FIXED_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + LISTED_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + INTNL_ID)), } },
                    {
                            CONSERVATIVE,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + CASH_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + FIXED_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + LISTED_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + INTNL_ID)), } },
                    {
                            CONSERVATIVE_BALANCED,
                            new double[] {
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + CASH_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + FIXED_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + LISTED_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + INTNL_ID)), } },
                    {
                            BALANCED_GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + CASH_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + FIXED_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + LISTED_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + INTNL_ID)), } },
                    {
                            GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + CASH_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + FIXED_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + LISTED_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + INTNL_ID)), } },
                    {
                            AGGRESSIVE_GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + CASH_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + FIXED_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + LISTED_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + INTNL_ID)), } },
                    {
                            ULTRA_GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + CASH_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + FIXED_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + LISTED_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + AUSSIE_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + INTNL_ID)), } }, };

            rawAllocationHistData = new Object[][] {
                    {
                            ULTRA_CONSERVATIVE,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_CONSERVATIVE
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_CONSERVATIVE
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_CONSERVATIVE
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_CONSERVATIVE
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_CONSERVATIVE
                                                                    + "."
                                                                    + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_CONSERVATIVE
                                                                    + "."
                                                                    + LONG_TERM_MAX_ID))
                                                    + DELTA }, } },
                    {
                            CONSERVATIVE,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params.get(""
                                                            + CONSERVATIVE
                                                            + "."
                                                            + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params.get(""
                                                            + CONSERVATIVE
                                                            + "."
                                                            + LONG_TERM_MAX_ID))
                                                    + DELTA }, } },
                    {
                            CONSERVATIVE_BALANCED,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE_BALANCED
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE_BALANCED
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE_BALANCED
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE_BALANCED
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE_BALANCED
                                                                    + "."
                                                                    + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + CONSERVATIVE_BALANCED
                                                                    + "."
                                                                    + LONG_TERM_MAX_ID))
                                                    + DELTA }, } },
                    {
                            BALANCED_GROWTH,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + BALANCED_GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + BALANCED_GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + BALANCED_GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + BALANCED_GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params.get(""
                                                            + BALANCED_GROWTH
                                                            + "."
                                                            + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params.get(""
                                                            + BALANCED_GROWTH
                                                            + "."
                                                            + LONG_TERM_MAX_ID))
                                                    + DELTA }, } },
                    {
                            GROWTH,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params.get("" + GROWTH
                                                            + "."
                                                            + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params.get("" + GROWTH
                                                            + "."
                                                            + LONG_TERM_MAX_ID))
                                                    + DELTA }, } },
                    {
                            AGGRESSIVE_GROWTH,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + AGGRESSIVE_GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + AGGRESSIVE_GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + AGGRESSIVE_GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + AGGRESSIVE_GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params.get(""
                                                            + AGGRESSIVE_GROWTH
                                                            + "."
                                                            + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params.get(""
                                                            + AGGRESSIVE_GROWTH
                                                            + "."
                                                            + LONG_TERM_MAX_ID))
                                                    + DELTA }, } },
                    {
                            ULTRA_GROWTH,
                            new double[][] {
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_GROWTH
                                                                    + "."
                                                                    + ANY_1_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MIN_ID)),
                                            parseDouble(""
                                                    + params
                                                            .get(""
                                                                    + ULTRA_GROWTH
                                                                    + "."
                                                                    + ANY_3_YEAR_MAX_ID)) },
                                    {
                                            parseDouble(""
                                                    + params.get(""
                                                            + ULTRA_GROWTH
                                                            + "."
                                                            + LONG_TERM_MIN_ID))
                                                    - DELTA,
                                            parseDouble(""
                                                    + params.get(""
                                                            + ULTRA_GROWTH
                                                            + "."
                                                            + LONG_TERM_MAX_ID))
                                                    + DELTA }, } }, };

            rawAllocationHistDataFull = new Object[][] {
                    {
                            ULTRA_CONSERVATIVE,
                            new double[] {
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + ULTRA_CONSERVATIVE + "."
                                                    + YEAR_7_ID)), } },
                    {
                            CONSERVATIVE,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get("" + CONSERVATIVE
                                                    + "." + YEAR_7_ID)), } },
                    {
                            CONSERVATIVE_BALANCED,
                            new double[] {
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get(""
                                                    + CONSERVATIVE_BALANCED
                                                    + "." + YEAR_7_ID)), } },
                    {
                            BALANCED_GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get("" + BALANCED_GROWTH
                                                    + "." + YEAR_7_ID)), } },
                    {
                            GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get("" + GROWTH + "."
                                                    + YEAR_7_ID)), } },
                    {
                            AGGRESSIVE_GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get("" + AGGRESSIVE_GROWTH
                                                    + "." + YEAR_7_ID)), } },
                    {
                            ULTRA_GROWTH,
                            new double[] {
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_1_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_2_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_3_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_4_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_5_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_6_ID)),
                                    parseDouble(""
                                            + params.get("" + ULTRA_GROWTH
                                                    + "." + YEAR_7_ID)), } }, };

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    public static Color[] getColors() {
        return colors;
    }

    static {
        initData();

        allocationData = new HashMap();
        allocationDataLabels = new Vector();
        initAllocationDataAndLabels();
    }

    private static void initAllocationDataAndLabels() {
        allocationData.clear();
        allocationDataLabels.clear();

        for (int i = 0; i < rawAllocationData.length; i++) {
            Vector row = new Vector();
            double[] data = (double[]) rawAllocationData[i][1];
            for (int j = 0; j < data.length; j++)
                row.addElement(new Double(data[j]));
            allocationData.put(rawAllocationData[i][0], row);
        }

        for (int i = 0; i < rawAllocationDataLabels.length; i++)
            allocationDataLabels.addElement(rawAllocationDataLabels[i]);
    }

    public static double[] getAllocationData(Integer investmentStrategyCodeID) {
        for (int i = 0; i < rawAllocationData.length; i++) {
            if (!investmentStrategyCodeID.equals(rawAllocationData[i][0]))
                continue;
            return (double[]) rawAllocationData[i][1];
        }
        return null;
    }

    public static double[][] getAllocationData2(Integer investmentStrategyCodeID) {
        double[] data = getAllocationData(investmentStrategyCodeID);
        double[][] data2 = new double[data.length][1];

        for (int i = 0; i < data.length; i++)
            data2[i][0] = data[i];

        return data2;
    }

    public static Vector getAllocationData3(Integer investmentStrategyCodeID) {
        return (Vector) (allocationData.get(investmentStrategyCodeID));
    }

    public static String[] getAllocationDataLabels() {
        return rawAllocationDataLabels;
    }

    public static Vector getAllocationDataLabels3() {
        return allocationDataLabels;
    }

    // public String getAllocationLegend( Integer investmentStrategyCodeID ) {
    // return getCodeDescription( investmentStrategyCodeID );
    // }

    public static double[][] getAllocationHistData(
            Integer investmentStrategyCodeID) {
        for (int i = 0; i < rawAllocationHistData.length; i++) {
            if (!investmentStrategyCodeID.equals(rawAllocationHistData[i][0]))
                continue;
            return (double[][]) rawAllocationHistData[i][1];
        }
        return null;
    }

    // new double [][] { {-7.4, 23.6}, {0.4, 15.8}, {8.1, 8.1} } },
    public static double[][] getAllocationHistData2(
            Integer investmentStrategyCodeID) {
        double[][] data = getAllocationHistData(investmentStrategyCodeID);
        double[][] data2 = new double[2][data.length];

        for (int i = 0; i < data.length; i++) {
            data2[0][i] = data[i][0];
            data2[1][i] = data[i][1];
        }

        return data2;
    }

    public static double[] getAllocationHistDataFull(
            Integer investmentStrategyCodeID) {
        for (int i = 0; i < rawAllocationHistDataFull.length; i++) {
            if (!investmentStrategyCodeID
                    .equals(rawAllocationHistDataFull[i][0]))
                continue;
            return (double[]) rawAllocationHistDataFull[i][1];
        }
        return null;
    }

    public static String[] getAllocationHistDataLabels() {
        return allocationHistLabels;
    }

}
