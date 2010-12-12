/*
 * WillChangeCode.java
 *
 * Created on 30 August 2001, 15:50
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class WillChangeCode extends Code {

    // 1 = No Changes Planned; 2 = Include Children; 3 = Remove Partner
    public final static Integer NO_CHANGE = new Integer(1);

    public final static Integer INCLUDE_KIDS = new Integer(2);

    public final static Integer REMOVE_PARTNER = new Integer(3);

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

        codeMap.put("No Changes Planned", NO_CHANGE);
        codeMap.put("Include Children", INCLUDE_KIDS);
        codeMap.put("Remove Partner", REMOVE_PARTNER);
    }

}
