/*
 * GearingType.java
 *
 * Created on 24 December 2001, 17:20
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class GearingType extends Code {

    // $ Cost Ave - Gearing Model
    public static final Integer INSTALLMENT = new Integer(1);

    public static final Integer LUMP_SUM = new Integer(2);

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
        // codeMap.put( EMPTY, VALUE_NONE );

        codeMap.put("Instalment", INSTALLMENT);
        codeMap.put("Lump Sum", LUMP_SUM);
    }

}
