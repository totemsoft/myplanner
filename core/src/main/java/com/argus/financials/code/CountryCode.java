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
import com.argus.financials.domain.refdata.ICountry;
import com.argus.util.BeanUtils;

public class CountryCode extends Code implements ICountry {

    /* (non-Javadoc)
     * @see com.argus.financials.domain.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Integer getId()
    {
        return null;
    }

    public static boolean isAustralia(Integer countryCodeID) {
        return AUSTRALIA_ID.equals(countryCodeID);
    }

    private static Map codeMap;

    // static {
    public CountryCode() {
        if (codeMap == null) {
            codeMap = new TreeMap(new Comparator() {
                private String defaultCountry = FPSLocale.getInstance().getDisplayCountry();

                public int compare(Object o1, Object o2) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;

                    if (s1.equalsIgnoreCase(s2))
                        return 0;

                    if (s1.equalsIgnoreCase(EMPTY))
                        return -1;
                    if (s2.equalsIgnoreCase(EMPTY))
                        return 1;

                    if (s1.equalsIgnoreCase(defaultCountry))
                        return -1;
                    if (s2.equalsIgnoreCase(defaultCountry))
                        return 1;

                    if (s1.equalsIgnoreCase(AUSTRALIA))
                        return -1;
                    if (s2.equalsIgnoreCase(AUSTRALIA))
                        return 1;

                    if (s1.equalsIgnoreCase(NEW_ZEALAND))
                        return -1;
                    if (s2.equalsIgnoreCase(NEW_ZEALAND))
                        return 1;

                    if (s1.equalsIgnoreCase(UNITED_KINGDOM))
                        return -1;
                    if (s2.equalsIgnoreCase(UNITED_KINGDOM))
                        return 1;

                    if (s1.equalsIgnoreCase(UNITED_STATES))
                        return -1;
                    if (s2.equalsIgnoreCase(UNITED_STATES))
                        return 1;

                    if (s1.equalsIgnoreCase(SINGAPORE))
                        return -1;
                    if (s2.equalsIgnoreCase(SINGAPORE))
                        return 1;

                    if (s1.equalsIgnoreCase(JAPAN))
                        return -1;
                    if (s2.equalsIgnoreCase(JAPAN))
                        return 1;

                    if (s1.equalsIgnoreCase(HONG_KONG))
                        return -1;
                    if (s2.equalsIgnoreCase(HONG_KONG))
                        return 1;

                    if (s1.equalsIgnoreCase(CHINA))
                        return -1;
                    if (s2.equalsIgnoreCase(CHINA))
                        return 1;

                    if (s1.equalsIgnoreCase(CANADA))
                        return -1;
                    if (s2.equalsIgnoreCase(CANADA))
                        return 1;

                    if (s1.equalsIgnoreCase(ITALY))
                        return -1;
                    if (s2.equalsIgnoreCase(ITALY))
                        return 1;

                    if (s1.equalsIgnoreCase(GREECE))
                        return -1;
                    if (s2.equalsIgnoreCase(GREECE))
                        return 1;

                    if (s1.equalsIgnoreCase(FRANCE))
                        return -1;
                    if (s2.equalsIgnoreCase(FRANCE))
                        return 1;

                    return BeanUtils.compareTo(s1, s2);
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
        codeMap.put(EMPTY, VALUE_NONE);

        try {
            Map map = com.argus.financials.service.ServiceLocator.getInstance()
                    .getUtilityService().getCodes(TABLE_NAME);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
            codeMap.put(AUSTRALIA, AUSTRALIA_ID);
        }
    }

}
