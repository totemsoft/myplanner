/*
 * ResidenceStatusCode.java
 *
 * Created on 15 August 2001, 16:43
 */

package com.argus.financials.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ResidenceStatusCode extends Code {

    public final static Integer RESIDENT = new Integer(1);

    public final static Integer NON_RESIDENT = new Integer(2);

    private static Map codeMap;

    protected Map getCodeMap() {
        if (codeMap == null) {
            codeMap = new TreeMap();
            initCodeMap();
        }
        return codeMap;
    }

    private static void initCodeMap() {
        codeMap.clear();
        codeMap.put(NONE, VALUE_NONE);

        codeMap.put("Resident", RESIDENT);
        codeMap.put("Non Resident", NON_RESIDENT);
    }

}
