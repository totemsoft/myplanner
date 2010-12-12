/*
 * InsolvencyRiskCode.java
 *
 * Created on 30 August 2001, 16:08
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class InsolvencyRiskCode extends Code {

    // 1 = No Concerns; 2 = Pending Investigation; 3 = Declared Insolvent
    public final static Integer NO_CONCERNS = new Integer(1);

    public final static Integer PENDING_INVESTIGATION = new Integer(2);

    public final static Integer DECLARED_INSOLVEMENT = new Integer(3);

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

        codeMap.put("No Concerns", NO_CONCERNS);
        codeMap.put("Pending Investigation", PENDING_INVESTIGATION);
        codeMap.put("Declared Insolvent", DECLARED_INSOLVEMENT);
    }

}
