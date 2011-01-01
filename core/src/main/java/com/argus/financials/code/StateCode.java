/*
 * StateCode.java
 *
 * Created on 1 August 2001, 14:27
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;

import com.argus.financials.domain.refdata.IState;

public class StateCode extends Code implements IState {

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

    private static Map codeMap;

    private static Integer countryID;

    // re-init map for new country
    public StateCode(Integer countryCodeID) {
        if (!equals(countryCodeID, countryID))
            initCodeMap(countryCodeID);
    }

    protected Map getCodeMap() {
        return codeMap;
    }

    private static void initCodeMap(Integer countryCodeID) {
        if (codeMap == null)
            codeMap = new java.util.TreeMap();
        else
            codeMap.clear();
        codeMap.put(NONE, VALUE_NONE);

        countryID = countryCodeID;

        // AUSSIE states
        if (CountryCode.isAustralia(countryID)) {
            codeMap.put("NSW", NSW);
            codeMap.put("VIC", VIC);
            codeMap.put("QLD", QLD);
            codeMap.put("ACT", ACT);
            codeMap.put("SA", SA);
            codeMap.put("WA", WA);
            codeMap.put("NT", NT);
            codeMap.put("TAS", TAS);

        } else {
            /*
             * try { codeMap = ServiseLocator.getInstance().getUtility().getStates(
             * countryID ); } catch (ServiceException e) { countryID =
             * null; e.printStackTrace(); }
             */
        }

    }

    public String[] getCodeDescriptions() {
        if (CountryCode.isAustralia(countryID))
            return super.getCodeDescriptions();
        return EMPTY_KEYS;
    }

}
