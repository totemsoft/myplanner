/*
 * IndustryCode.java
 *
 * Created on 30 August 2001, 14:06
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class IndustryCode extends Code {

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

        try {
            Map map = utilityService.getCodes("IndustryCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.api.ServiceException re) {
        }
    }

}
