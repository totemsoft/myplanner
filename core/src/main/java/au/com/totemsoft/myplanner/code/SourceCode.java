/*
 * SourceCode.java
 *
 * Created on 13 November 2001, 16:52
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class SourceCode extends Code {

    // public final static Integer = new Integer(1);

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

        try {
            Map map = utilityService.getCodes("SourceCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (au.com.totemsoft.myplanner.api.ServiceException re) {
        }
    }

}
