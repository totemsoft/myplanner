/*
 * SexCode.java
 *
 * Created on 23 July 2000, 12:09
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public final class SexCode extends Code {

    public final static Integer FEMALE = new Integer(1);

    public final static Integer MALE = new Integer(2);

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

        codeMap.put("Female", FEMALE);
        codeMap.put("Male", MALE);
    }

}
