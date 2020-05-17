/*
 * PaymentType.java
 *
 * Created on 11 December 2001, 19:06
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class PaymentType extends Code {

    public final static Integer NET = new Integer(1);

    public final static Integer GROSS = new Integer(2);

    public final static Integer MINIMUM = new Integer(3);

    public final static Integer MAXIMUM = new Integer(4);

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

        codeMap.put("Net", NET);
        codeMap.put("Gross", GROSS);
        codeMap.put("Minimum", MINIMUM);
        codeMap.put("Maximum", MAXIMUM);
    }

}
