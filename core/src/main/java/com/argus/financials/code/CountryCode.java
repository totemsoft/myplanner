/*
 * CountryCode.java
 *
 * Created on 2 August 2001, 15:56
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.config.FPSLocale;

public class CountryCode extends Code {

    public static String TABLE_NAME = "CountryCode";

    public final static Integer AUSTRALIA = new Integer(13);

    private final static String strAUSTRALIA = "Australia";

    private final static String strNEW_ZEALAND = "New Zealand";

    private final static String strUNITED_KINGDOM = "United Kingdom";

    private final static String strUNITED_STATES = "United States";

    private final static String strSINGAPORE = "Singapore";

    private final static String strJAPAN = "Japan";

    private final static String strHONG_KONG = "Hong Kong";

    private final static String strCHINA = "China";

    private final static String strCANADA = "Canada";

    private final static String strITALY = "Italy";

    private final static String strGREECE = "Greece";

    private final static String strFRANCE = "France";

    public static boolean isAustralia(Integer countryCodeID) {
        return AUSTRALIA.equals(countryCodeID);
    }

    private static Map codeMap;

    // static {
    public CountryCode() {
        final String defaultCountry = FPSLocale.getInstance()
                .getDisplayCountry();

        if (codeMap == null) {
            codeMap = new TreeMap(new Comparator() {

                public int compare(Object o1, Object o2) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;

                    if (s1.equalsIgnoreCase(s2))
                        return 0;

                    if (s1.equalsIgnoreCase(KEY_NONE))
                        return -1;
                    if (s2.equalsIgnoreCase(KEY_NONE))
                        return 1;

                    if (s1.equalsIgnoreCase(defaultCountry))
                        return -1;
                    if (s2.equalsIgnoreCase(defaultCountry))
                        return 1;

                    if (s1.equalsIgnoreCase(strAUSTRALIA))
                        return -1;
                    if (s2.equalsIgnoreCase(strAUSTRALIA))
                        return 1;

                    if (s1.equalsIgnoreCase(strNEW_ZEALAND))
                        return -1;
                    if (s2.equalsIgnoreCase(strNEW_ZEALAND))
                        return 1;

                    if (s1.equalsIgnoreCase(strUNITED_KINGDOM))
                        return -1;
                    if (s2.equalsIgnoreCase(strUNITED_KINGDOM))
                        return 1;

                    if (s1.equalsIgnoreCase(strUNITED_STATES))
                        return -1;
                    if (s2.equalsIgnoreCase(strUNITED_STATES))
                        return 1;

                    if (s1.equalsIgnoreCase(strSINGAPORE))
                        return -1;
                    if (s2.equalsIgnoreCase(strSINGAPORE))
                        return 1;

                    if (s1.equalsIgnoreCase(strJAPAN))
                        return -1;
                    if (s2.equalsIgnoreCase(strJAPAN))
                        return 1;

                    if (s1.equalsIgnoreCase(strHONG_KONG))
                        return -1;
                    if (s2.equalsIgnoreCase(strHONG_KONG))
                        return 1;

                    if (s1.equalsIgnoreCase(strCHINA))
                        return -1;
                    if (s2.equalsIgnoreCase(strCHINA))
                        return 1;

                    if (s1.equalsIgnoreCase(strCANADA))
                        return -1;
                    if (s2.equalsIgnoreCase(strCANADA))
                        return 1;

                    if (s1.equalsIgnoreCase(strITALY))
                        return -1;
                    if (s2.equalsIgnoreCase(strITALY))
                        return 1;

                    if (s1.equalsIgnoreCase(strGREECE))
                        return -1;
                    if (s2.equalsIgnoreCase(strGREECE))
                        return 1;

                    if (s1.equalsIgnoreCase(strFRANCE))
                        return -1;
                    if (s2.equalsIgnoreCase(strFRANCE))
                        return 1;

                    return _compare(s1, s2);
                }

                public boolean equals(Object obj) {
                    return this.toString().equalsIgnoreCase(obj.toString());
                }

                private int _compare(String s1, String s2) {
                    if (s1 == null && s2 == null)
                        return 0;
                    if (s1 == null)
                        return -1;
                    if (s2 == null)
                        return 1;

                    int result = s1.compareTo(s2);
                    if (result < 0)
                        return -1;
                    if (result > 0)
                        return 1;
                    return 0;
                }

            });
            initCodeMap();
        }
    }

    protected Map getCodeMap() {
        return codeMap;
    }

    private static void initCodeMap() {
        codeMap.clear();
        codeMap.put(KEY_NONE, VALUE_NONE);

        try {
            Map map = com.argus.financials.service.ServiceLocator.getInstance()
                    .getUtilityService().getCodes(TABLE_NAME);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
            codeMap.put(strAUSTRALIA, AUSTRALIA);
        }
    }

}
