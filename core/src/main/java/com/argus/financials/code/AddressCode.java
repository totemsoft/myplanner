/*
 * AddressCode.java
 *
 * Created on 20 August 2001, 12:46
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class AddressCode extends Code {

    public static final Integer RESIDENTIAL = new Integer(1);

    public static final Integer POSTAL = new Integer(2);

    // public static final Integer EMPLOYER = new Integer(3);
    // public static final Integer EXECUTOR = new Integer(4);

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

        codeMap.put("Residential Address", RESIDENTIAL);
        codeMap.put("Postal Address", POSTAL);
        // codeMap.put( "Employer Address", EMPLOYER );
        // codeMap.put( "Will Executor Address", EXECUTOR );
    }

}
