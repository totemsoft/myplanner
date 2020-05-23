/*
 * InvestmentType.java
 *
 * Created on 7 November 2001, 10:18
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class InvestmentType extends Code {

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
            Map map = utilityService.getCodes("InvestmentType");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (au.com.totemsoft.myplanner.api.ServiceException re) {
        }
    }

}
