/*
 * InvestmentStrategyCode.java
 *
 * Created on 14 September 2001, 12:46
 */

package com.argus.financials.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.argus.financials.api.CommonConstants;
import com.argus.financials.etc.GrowthRate;
import com.argus.financials.service.UtilityService;

public class InvestmentStrategyCode extends Code {

    protected static double HOLE = CommonConstants.HOLE;

    // strategies
    public final static Integer ULTRA_CONSERVATIVE = new Integer(1);

    public final static Integer CONSERVATIVE = new Integer(2);

    public final static Integer CONSERVATIVE_BALANCED = new Integer(3);

    public final static Integer BALANCED_GROWTH = new Integer(4);

    public final static Integer GROWTH = new Integer(5);

    public final static Integer AGGRESSIVE_GROWTH = new Integer(6);

    public final static Integer ULTRA_GROWTH = new Integer(7);

    private final static Integer INCOME_ID = new Integer(1);

    private final static Integer GROWTH_ID = new Integer(2);

    private final static Integer DEFFENSIVE_ID = new Integer(3);

    private static GrowthRate ultraConservativeGrowthRate;

    private static GrowthRate conservativeRate;

    private static GrowthRate conservativeBalancedRate;

    private static GrowthRate balancedGrowthRate;

    private static GrowthRate growthRate;

    private static GrowthRate aggressiveGrowthRate;

    private static GrowthRate ultraGrowthRate;

    private static Map codeMap;

    protected static java.util.Map params;

    protected Map getCodeMap() {
        if (codeMap == null) {
            codeMap = new HashMap();
            initCodeMap();
            initGrowthRates();
        }
        return codeMap;
    }

    private static void initCodeMap() {
        codeMap.clear();
        codeMap.put(NONE, VALUE_NONE);

        codeMap.put("Ultra Conservative", ULTRA_CONSERVATIVE);
        codeMap.put("Conservative", CONSERVATIVE);
        codeMap.put("Conservative Balanced", CONSERVATIVE_BALANCED);
        codeMap.put("Balanced", BALANCED_GROWTH);
        codeMap.put("Growth", GROWTH);
        codeMap.put("Strong Growth", AGGRESSIVE_GROWTH);
        codeMap.put("Ultra Growth", ULTRA_GROWTH);
    }

    protected static double parseDouble(String s) {
        try {
            return s == null || s.trim().length() == 0 ? HOLE : Double
                    .parseDouble(s);
        } catch (java.lang.NumberFormatException e) {
            return HOLE; // probably NULL value
        }

    }

    private static void initGrowthRates() {

        try {
            params = utilityService.getParameters(
                    UtilityService.PARAM_INVESTMENT_STRATEGY);
            if (params == null)
                return;
            System.out.println(params);

            ultraConservativeGrowthRate = new GrowthRate(parseDouble(""
                    + params.get("" + ULTRA_CONSERVATIVE + "." + INCOME_ID)),
                    parseDouble(""
                            + params.get("" + ULTRA_CONSERVATIVE + "."
                                    + GROWTH_ID)), parseDouble(""
                            + params.get("" + ULTRA_CONSERVATIVE + "."
                                    + DEFFENSIVE_ID)));
            conservativeRate = new GrowthRate(parseDouble(""
                    + params.get("" + CONSERVATIVE + "." + INCOME_ID)),
                    parseDouble(""
                            + params.get("" + CONSERVATIVE + "." + GROWTH_ID)),
                    parseDouble(""
                            + params.get("" + CONSERVATIVE + "."
                                    + DEFFENSIVE_ID)));
            conservativeBalancedRate = new GrowthRate(
                    parseDouble(""
                            + params.get("" + CONSERVATIVE_BALANCED + "."
                                    + INCOME_ID)), parseDouble(""
                            + params.get("" + CONSERVATIVE_BALANCED + "."
                                    + GROWTH_ID)), parseDouble(""
                            + params.get("" + CONSERVATIVE_BALANCED + "."
                                    + DEFFENSIVE_ID)));
            balancedGrowthRate = new GrowthRate(
                    parseDouble(""
                            + params
                                    .get("" + BALANCED_GROWTH + "." + INCOME_ID)),
                    parseDouble(""
                            + params
                                    .get("" + BALANCED_GROWTH + "." + GROWTH_ID)),
                    parseDouble(""
                            + params.get("" + BALANCED_GROWTH + "."
                                    + DEFFENSIVE_ID)));
            growthRate = new GrowthRate(
                    parseDouble("" + params.get("" + GROWTH + "." + INCOME_ID)),
                    parseDouble("" + params.get("" + GROWTH + "." + GROWTH_ID)),
                    parseDouble(""
                            + params.get("" + GROWTH + "." + DEFFENSIVE_ID)));
            aggressiveGrowthRate = new GrowthRate(parseDouble(""
                    + params.get("" + AGGRESSIVE_GROWTH + "." + INCOME_ID)),
                    parseDouble(""
                            + params.get("" + AGGRESSIVE_GROWTH + "."
                                    + GROWTH_ID)), parseDouble(""
                            + params.get("" + AGGRESSIVE_GROWTH + "."
                                    + DEFFENSIVE_ID)));
            ultraGrowthRate = new GrowthRate(parseDouble(""
                    + params.get("" + ULTRA_GROWTH + "." + INCOME_ID)),
                    parseDouble(""
                            + params.get("" + ULTRA_GROWTH + "." + GROWTH_ID)),
                    parseDouble(""
                            + params.get("" + ULTRA_GROWTH + "."
                                    + DEFFENSIVE_ID)));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    public static GrowthRate getGrowthRate(Integer investmentStrategyCodeID) {
        if (CONSERVATIVE.equals(investmentStrategyCodeID))
            return conservativeRate;
        if (CONSERVATIVE_BALANCED.equals(investmentStrategyCodeID))
            return conservativeBalancedRate;
        if (BALANCED_GROWTH.equals(investmentStrategyCodeID))
            return balancedGrowthRate;
        if (GROWTH.equals(investmentStrategyCodeID))
            return growthRate;
        if (AGGRESSIVE_GROWTH.equals(investmentStrategyCodeID))
            return aggressiveGrowthRate;
        // BEGIN: BUG FIX 479 - 18/07/2002
        // by shibaevv
        if (ULTRA_CONSERVATIVE.equals(investmentStrategyCodeID))
            return ultraConservativeGrowthRate;
        if (ULTRA_GROWTH.equals(investmentStrategyCodeID))
            return ultraGrowthRate;
        // END: BUG FIX 479 - 18/07/2002
        return null;
    }

    public String[] getCodeDescriptions() {

        Set set = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                Integer d1 = (Integer) getCodeMap().get((String) o1);
                Integer d2 = (Integer) getCodeMap().get((String) o2);
                return (d1 == null ? new Integer(0) : d1)
                        .compareTo(d2 == null ? new Integer(0) : d2);
            }
        });

        set.addAll(getCodeMap().keySet());

        return (String[]) set.toArray(new String[0]);

    }
}
