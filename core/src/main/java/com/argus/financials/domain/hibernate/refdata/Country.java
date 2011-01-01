package com.argus.financials.domain.hibernate.refdata;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.argus.financials.domain.refdata.ICountry;
import com.argus.util.BeanUtils;

@Entity
@Table(name = ICountry.TABLE_NAME)
public class Country extends AbstractCode implements ICountry
{
    /** serialVersionUID */
    private static final long serialVersionUID = -5771582515464015957L;

    @Id
    @Column(name = "CountryCodeID", nullable = false)
    private Integer id;

    @Column(name = "CountryCode", nullable = false)
    private String code;

    @Column(name = "CountryCodeDesc", nullable = false)
    private String description;

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return code;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    public static class CountryComparator implements Comparator<Country>
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
    }

}