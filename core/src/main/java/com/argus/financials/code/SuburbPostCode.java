/*
 * SuburbPostCode.java
 *
 * Created on 20 August 2001, 13:56
 */

package com.argus.financials.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeSet;

public class SuburbPostCode extends Code {

    public final static String VALUE_NONE = "";// null;//

    /**
     * Map( StateCodeID, Object [] { PostCode2Suburb = Map( PostCode,
     * List(Suburb) ), Suburb2PostCode = Map( Suburb, List(PostCode) ) } )
     */
    private static Map codeMap;

    private static Integer countryID;

    // to access current map
    public SuburbPostCode() {
    }

    // re-init map for new country
    public SuburbPostCode(Integer countryCodeID) {
        if (countryCodeID == null)
            return;
        if (countryCodeID.equals(countryID))
            return;

        initCodeMap(countryCodeID);
    }

    protected Map getCodeMap() {
        return codeMap;
    }

    private static void initCodeMap(Integer countryCodeID) {
        try {
            codeMap = utilityService.getPostCodes(countryCodeID);
            countryID = countryCodeID;
        } catch (com.argus.financials.api.ServiceException e) {
            countryID = null;
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public String[] getPostCodes(Integer stateCodeID) {

        if (codeMap == null || stateCodeID == null)
            return EMPTY_KEYS;

        java.util.Map postCode2Suburbs = (Map) ((Object[]) codeMap
                .get(stateCodeID))[0];
        if (postCode2Suburbs == null)
            return EMPTY_KEYS;

        return (String[]) (new TreeSet(postCode2Suburbs.keySet()))
                .toArray(new String[0]);

    }

    public String[] getPostCodes(Integer stateCodeID, String suburb) {

        if (codeMap == null || stateCodeID == null || suburb == null)
            return EMPTY_KEYS;

        java.util.Map suburb2PostCodes = (Map) ((Object[]) codeMap
                .get(stateCodeID))[1];
        if (suburb2PostCodes == null)
            return EMPTY_KEYS;

        java.util.List list = (java.util.List) suburb2PostCodes.get(suburb);
        if (list == null)
            return EMPTY_KEYS;
        return (String[]) (list.toArray(new String[0]));

    }

    /**
     * 
     */
    public String[] getSuburbs(Integer stateCodeID) {

        if (codeMap == null || stateCodeID == null)
            return EMPTY_KEYS;

        java.util.Map suburb2PostCodes = (Map) ((Object[]) codeMap
                .get(stateCodeID))[1];
        if (suburb2PostCodes == null)
            return EMPTY_KEYS;

        return (String[]) (new TreeSet(suburb2PostCodes.keySet()))
                .toArray(new String[0]);

    }

    public String[] getSuburbs(Integer stateCodeID, String postCode) {

        if (codeMap == null || stateCodeID == null || postCode == null)
            return EMPTY_KEYS;

        java.util.Map postCode2Suburbs = (Map) ((Object[]) codeMap
                .get(stateCodeID))[0];
        if (postCode2Suburbs == null)
            return EMPTY_KEYS;

        java.util.List list = (java.util.List) postCode2Suburbs.get(postCode);
        if (list == null)
            return EMPTY_KEYS;
        return (String[]) (list.toArray(new String[0]));

    }

}
