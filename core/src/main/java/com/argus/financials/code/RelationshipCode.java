/*
 * RelationshipCode.java
 *
 * Created on 1 August 2001, 12:12
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class RelationshipCode extends Code {

    // 1 = Wife; 2 = Defacto; 3 = Son; 4= Daughter; 5 = Husband; 6 = Other
    public final static Integer WIFE = new Integer(1);

    public final static Integer DEFACTO = new Integer(2);

    public final static Integer SON = new Integer(3);

    public final static Integer DAUGHTER = new Integer(4);

    public final static Integer HUSBAND = new Integer(5);

    public final static Integer OTHER = new Integer(6);

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

        codeMap.put("Wife", WIFE);
        codeMap.put("Defacto", DEFACTO);
        codeMap.put("Son", SON);
        codeMap.put("Daughter", DAUGHTER);
        codeMap.put("Husband", HUSBAND);
        codeMap.put("Other", OTHER);
    }

}
