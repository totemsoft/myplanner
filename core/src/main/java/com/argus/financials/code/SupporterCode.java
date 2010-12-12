/*
 * SupporterCode.java
 *
 * Created on 22 August 2001, 16:12
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class SupporterCode extends Code {

    public final static Integer CLIENT = new Integer(1);

    public final static Integer NOT_CLIENT = new Integer(2);

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

        codeMap.put("Client", CLIENT);
        codeMap.put("Not Client", NOT_CLIENT);
    }

}
