/*
 * WillChangeCode.java
 *
 * Created on 30 August 2001, 15:50
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class WillChangeCode extends Code {

    // 1 = No Changes Planned; 2 = Include Children; 3 = Remove Partner
    public final static Integer NO_CHANGE = new Integer(1);

    public final static Integer INCLUDE_KIDS = new Integer(2);

    public final static Integer REMOVE_PARTNER = new Integer(3);

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

        codeMap.put("No Changes Planned", NO_CHANGE);
        codeMap.put("Include Children", INCLUDE_KIDS);
        codeMap.put("Remove Partner", REMOVE_PARTNER);
    }

}
