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

import com.argus.financials.service.ServiceLocator;

public class Parameters {

    private static java.util.Map map;

    static {
        if (map == null) {
            map = new HashMap();
            load();
        }
    }

    private static void load() {
        map.clear();

        /**
         * load other values from db (Parameters table)
         */
        try {
            Map paramMap = ServiceLocator.getInstance().getUtility()
                    .getParameters(null);
            if (paramMap == null)
                return;
            map.putAll(paramMap);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace();
        }

    }

    private static void store() {
    }

    /**
     * get
     */
    public static Object getValue(String name) {
        return map.get(name);
    }

}
