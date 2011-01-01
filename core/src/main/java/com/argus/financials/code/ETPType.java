/*
 * ETPType.java
 *
 * Created on 11 December 2001, 16:40
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ETPType extends Code {

    public final static Integer SUPER_ETP_TAXED = new Integer(1);

    public final static Integer SUPER_ETP_UNTAXED = new Integer(2);

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
        codeMap.put(NONE, VALUE_NONE);

        codeMap.put("Super ETP Taxed Source", SUPER_ETP_TAXED);
        codeMap.put("Super ETP Un-Taxed Source", SUPER_ETP_UNTAXED);
    }

}
