/*
 * ResidenceStatusCode.java
 *
 * Created on 15 August 2001, 16:43
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ResidenceStatusCode extends Code {

    public final static Integer RESIDENT = new Integer(1);

    public final static Integer NON_RESIDENT = new Integer(2);

    private static Map codeMap;

    static {
        if (codeMap == null) {
            codeMap = new TreeMap();
            initCodeMap();
        }
    }

    protected Map getCodeMap() {
        return codeMap;
    }

    private static void initCodeMap() {
        codeMap.clear();
        codeMap.put(KEY_NONE, VALUE_NONE);

        codeMap.put("Resident", RESIDENT);
        codeMap.put("Non Resident", NON_RESIDENT);
    }

}
