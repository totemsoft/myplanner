/*
 * StatusCode.java
 *
 * Created on 17 August 2001, 17:20
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class StatusCode extends Code {

    public final static Integer IN_PLACE = new Integer(1);

    public final static Integer NOT_IN_PLACE = new Integer(2);

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

        codeMap.put("In Place", IN_PLACE);
        codeMap.put("Not in Place", NOT_IN_PLACE);
    }

}
