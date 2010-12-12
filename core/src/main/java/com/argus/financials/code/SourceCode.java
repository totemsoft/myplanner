/*
 * SourceCode.java
 *
 * Created on 13 November 2001, 16:52
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

public class SourceCode extends Code {

    // public final static Integer = new Integer(1);

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
            Map map = ServiceLocator.getInstance().getUtility().getCodes(
                    "SourceCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException re) {
        }
    }

}
