/*
 * Params.java
 *
 * Created on 18 October 2001, 15:33
 */

package au.com.totemsoft.myplanner.projection.data;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.HashMap;
import java.util.Map;

import au.com.totemsoft.myplanner.service.ServiceAware;

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
        } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
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
