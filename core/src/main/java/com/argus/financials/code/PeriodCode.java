/*
 * PeriodCode.java
 *
 * Created on 13 November 2001, 14:10
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.service.ServiceLocator;

public class PeriodCode extends Code {

    // public final static Integer = new Integer(1);
    public final static Integer TWELVE_MONTHS = new Integer(6);

    public final static Integer SEVEN_DAYS = new Integer(1);

    public final static Integer FORTEEN_DAYS = new Integer(2);

    public final static Integer THIRTY_DAYS = new Integer(3);

    public final static Integer NINETY_DAYS = new Integer(4);

    public final static Integer CUSTOM = new Integer(0);

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
        codeMap.put(NONE, VALUE_NONE);

        try {
            Map map = ServiceLocator.getInstance().getUtilityService().getCodes(
                    "PeriodCode");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException re) {
        }
    }

}
