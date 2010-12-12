/*
 * MaritalCode.java
 *
 * Created on 23 July 2000, 12:19
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public final class MaritalCode extends Code {

    public final static Integer SINGLE = new Integer(1);

    public final static Integer DEFACTO = new Integer(2);

    public final static Integer MARRIED = new Integer(3);

    public final static Integer SEPARATED = new Integer(4);

    public final static Integer DIVORCED = new Integer(5);

    public final static Integer WIDOWED = new Integer(6);

    public final static Integer SEPARATED_HEALTH = new Integer(7);

    public final static Integer PARTNERED = new Integer(8);

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

        codeMap.put("Single", SINGLE);
        codeMap.put("De-facto", DEFACTO);
        codeMap.put("Married", MARRIED);
        codeMap.put("Separated", SEPARATED);
        codeMap.put("Divorced", DIVORCED);
        codeMap.put("Widowed", WIDOWED);
        codeMap.put("Separated-Health", SEPARATED_HEALTH);
        codeMap.put("Partnered", PARTNERED);
    }

    public static boolean isSingle(Integer maritalCodeID) {
        return maritalCodeID == null
                || (!DEFACTO.equals(maritalCodeID)
                        && !MARRIED.equals(maritalCodeID) && !PARTNERED
                        .equals(maritalCodeID));
    }

}
