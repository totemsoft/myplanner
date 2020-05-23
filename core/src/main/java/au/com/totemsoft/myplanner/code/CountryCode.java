/*
 * CountryCode.java
 *
 * Created on 2 August 2001, 15:56
 */

package au.com.totemsoft.myplanner.code;

import java.util.Map;
import java.util.TreeMap;

import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.config.FPSLocale;
import au.com.totemsoft.myplanner.util.CountryStringComparator;

public class CountryCode extends Code {

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.IBase#getId()
     */
    public Integer getId()
    {
        return null;
    }

    public static boolean isAustralia(Integer countryCodeID) {
        return ICountry.AUSTRALIA_ID.equals(countryCodeID);
    }

    private static Map codeMap;

    protected Map getCodeMap() {
        if (codeMap == null) {
            codeMap = new TreeMap(new CountryStringComparator(FPSLocale.getInstance().getDisplayCountry()));
            initCodeMap();
        }
        return codeMap;
    }

    private static void initCodeMap() {
        codeMap.clear();
        codeMap.put(ICountry.EMPTY, VALUE_NONE);

        try {
            Map map = utilityService.getCodes(ICountry.TABLE_NAME);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (ServiceException e) {
            e.printStackTrace(System.err);
            codeMap.put(ICountry.AUSTRALIA, ICountry.AUSTRALIA_ID);
        }
    }

}
