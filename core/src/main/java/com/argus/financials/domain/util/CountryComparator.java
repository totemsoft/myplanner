package com.argus.financials.domain.util;

import java.util.Comparator;

import com.argus.financials.domain.client.refdata.ICountry;
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.util.BeanUtils;

public class CountryComparator implements Comparator<Country>
{
    private String defaultCountry;

    public CountryComparator(String defaultCountry)
    {
        this.defaultCountry = defaultCountry;
    }

    public int compare(Country c1, Country c2)
    {
        String s1 = c1.getDescription();
        String s2 = c2.getDescription();

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

}