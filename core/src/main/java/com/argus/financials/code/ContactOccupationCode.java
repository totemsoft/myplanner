/*
 * ContactOccupationCode.java
 *
 * Created on 30 August 2001, 10:22
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ContactOccupationCode extends Code {

    public final static Integer LEGAL_ADVISER = new Integer(1);

    public final static Integer ACCOUNTANT = new Integer(2);

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

        codeMap.put("Legal Adviser", LEGAL_ADVISER);
        codeMap.put("Accountant", ACCOUNTANT);
    }

}
