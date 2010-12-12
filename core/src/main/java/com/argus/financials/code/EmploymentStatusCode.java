/*
 * EmploymentStatusCode.java
 *
 * Created on 30 August 2001, 13:57
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

// 1 = Employee; 2 = Self Employed; 3 = Unemployed; 4 = Retired
public class EmploymentStatusCode extends Code {

    public final static Integer OTHER = new Integer(0);

    public final static Integer EMPLOYEE = new Integer(1);

    public final static Integer SELF_EMPLOYED = new Integer(2);

    public final static Integer UNEMPLOYED = new Integer(3);

    public final static Integer RETIRED = new Integer(4);

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

        codeMap.put("Employee", EMPLOYEE);
        codeMap.put("Self Employed", SELF_EMPLOYED);
        codeMap.put("Unemployed", UNEMPLOYED);
        codeMap.put("Retired", RETIRED);
    }

}
