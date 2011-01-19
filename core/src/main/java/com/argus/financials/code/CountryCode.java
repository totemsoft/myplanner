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
import com.argus.financials.domain.client.refdata.ICountry;
import com.argus.util.BeanUtils;

public class CountryCode extends Code {

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getDescription()
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
        return ICountry.AUSTRALIA_ID.equals(countryCodeID);
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

                    if (s1.equalsIgnoreCase(ICountry.EMPTY))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.EMPTY))
                        return 1;

                    if (s1.equalsIgnoreCase(defaultCountry))
                        return -1;
                    if (s2.equalsIgnoreCase(defaultCountry))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.AUSTRALIA))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.AUSTRALIA))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.NEW_ZEALAND))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.NEW_ZEALAND))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.UNITED_KINGDOM))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.UNITED_KINGDOM))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.UNITED_STATES))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.UNITED_STATES))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.SINGAPORE))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.SINGAPORE))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.JAPAN))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.JAPAN))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.HONG_KONG))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.HONG_KONG))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.CHINA))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.CHINA))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.CANADA))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.CANADA))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.ITALY))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.ITALY))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.GREECE))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.GREECE))
                        return 1;

                    if (s1.equalsIgnoreCase(ICountry.FRANCE))
                        return -1;
                    if (s2.equalsIgnoreCase(ICountry.FRANCE))
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
        codeMap.put(ICountry.EMPTY, VALUE_NONE);

        try {
            Map map = com.argus.financials.service.ServiceLocator.getInstance()
                    .getUtilityService().getCodes(ICountry.TABLE_NAME);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.client.ServiceException e) {
            e.printStackTrace(System.err);
            codeMap.put(ICountry.AUSTRALIA, ICountry.AUSTRALIA_ID);
        }
    }

}
