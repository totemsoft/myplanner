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

public class StateCode extends Code {

    public final static Integer NSW = new Integer(1);

    public final static Integer VIC = new Integer(2);

    public final static Integer QLD = new Integer(3);

    public final static Integer ACT = new Integer(4);

    public final static Integer SA = new Integer(5);

    public final static Integer WA = new Integer(6);

    public final static Integer NT = new Integer(7);

    public final static Integer TAS = new Integer(8);

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
             * try { codeMap = RmiParams.getInstance().getUtility().getStates(
             * countryID ); } catch (com.argus.financials.service.ServiceException e) { countryID =
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
