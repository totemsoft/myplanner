/*
 * StateCode.java
 *
 * Created on 1 August 2001, 14:27
 */

package com.argus.financials.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;

import com.argus.financials.api.bean.IState;

public class StateCode extends Code //implements IState
{

    private static Map codeMap;

    private static Integer countryID;

    // re-init map for new country
    public StateCode(Integer countryCodeID) {
        if (!equals(countryCodeID, countryID))
            initCodeMap(countryCodeID);
    }

    public Long getId()
    {
        return null;
    }

    public String getCode()
    {
        return null;
    }

    public String getDescription()
    {
        return null;
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
            codeMap.put("NSW", IState.NSW);
            codeMap.put("VIC", IState.VIC);
            codeMap.put("QLD", IState.QLD);
            codeMap.put("ACT", IState.ACT);
            codeMap.put("SA", IState.SA);
            codeMap.put("WA", IState.WA);
            codeMap.put("NT", IState.NT);
            codeMap.put("TAS", IState.TAS);

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
