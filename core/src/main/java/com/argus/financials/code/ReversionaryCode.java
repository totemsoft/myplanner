/*
 * ReversionaryCode.java
 *
 * Created on 12 December 2001, 09:54
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ReversionaryCode extends Code {

    public final static Integer NO = new Integer(1);

    public final static Integer YES = new Integer(2);

    public final static Integer SPOUSE = new Integer(3);

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

        codeMap.put("Non-Reversionary", NO);
        codeMap.put("Reversionary", YES);
        codeMap.put("Surviving Spouse", SPOUSE);
    }

}
