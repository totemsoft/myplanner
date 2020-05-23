/*
 * PeriodCode.java
 *
 * Created on 13 November 2001, 14:10
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class PeriodCode extends Code {

    // public final static Integer = new Integer(1);
    public final static Integer TWELVE_MONTHS = new Integer(6);

    public final static Integer SEVEN_DAYS = new Integer(1);

    public final static Integer FORTEEN_DAYS = new Integer(2);

    public final static Integer THIRTY_DAYS = new Integer(3);

    public final static Integer NINETY_DAYS = new Integer(4);

    public final static Integer CUSTOM = new Integer(0);

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
            Map map = utilityService.getCodes("PeriodCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (au.com.totemsoft.myplanner.api.ServiceException re) {
        }
    }

}
