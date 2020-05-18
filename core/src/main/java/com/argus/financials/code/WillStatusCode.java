/*
 * WillStatusCode.java
 *
 * Created on 30 August 2001, 15:45
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class WillStatusCode extends Code {

    // 1 = In Place; 2 = Being Drafted; 3 = Not in Place
    public final static Integer IN_PLACE = new Integer(1);

    public final static Integer BEING_DRAFTED = new Integer(2);

    public final static Integer NOT_IN_PLACE = new Integer(3);

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

        codeMap.put("In Place", IN_PLACE);
        codeMap.put("Being Drafted", BEING_DRAFTED);
        codeMap.put("Not in Place", NOT_IN_PLACE);
    }

}
