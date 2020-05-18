/*
 * Params.java
 *
 * Created on 18 October 2001, 15:33
 */

package com.argus.financials.projection.data;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.HashMap;
import java.util.Map;

import com.argus.financials.service.ServiceAware;

public class Parameters
    extends ServiceAware
{

    private static Map map = new HashMap();

    /**
     * load other values from db (Parameters table)
     */
    private static void load() {
        try {
            Map paramMap = utilityService.getParameters(null);
            if (paramMap == null)
                return;
            map.putAll(paramMap);
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace();
        }

    }

    private static void store() {
    }

    /**
     * get
     */
    public static Object getValue(String name) {
        if (map == null) {
            map = new HashMap();
            load();
        }
        return map.get(name);
    }

}
