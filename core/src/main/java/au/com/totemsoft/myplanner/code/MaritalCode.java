/*
 * MaritalCode.java
 *
 * Created on 23 July 2000, 12:19
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

import au.com.totemsoft.myplanner.api.bean.IMaritalCode;

public final class MaritalCode extends Code {

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

        codeMap.put("Single", IMaritalCode.SINGLE);
        codeMap.put("De-facto", IMaritalCode.DEFACTO);
        codeMap.put("Married", IMaritalCode.MARRIED);
        codeMap.put("Separated", IMaritalCode.SEPARATED);
        codeMap.put("Divorced", IMaritalCode.DIVORCED);
        codeMap.put("Widowed", IMaritalCode.WIDOWED);
        codeMap.put("Separated-Health", IMaritalCode.SEPARATED_HEALTH);
        codeMap.put("Partnered", IMaritalCode.PARTNERED);
    }

}
