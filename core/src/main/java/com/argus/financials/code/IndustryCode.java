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

import com.argus.financials.service.ServiceLocator;

public class IndustryCode extends Code {

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

        try {
            Map map = ServiceLocator.getInstance().getUtilityService().getCodes(
                    "IndustryCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException re) {
        }
    }

}
