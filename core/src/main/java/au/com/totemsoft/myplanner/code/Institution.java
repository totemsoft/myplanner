/*
 * Institution.java
 *
 * Created on 16 October 2001, 10:31
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Institution extends Code {

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
            Map map = utilityService.getCodes("Institution", "InstitutionID", "InstitutionName");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (au.com.totemsoft.myplanner.api.ServiceException re) {
        }
    }

    public static void addInstitution(String institution, Integer id) {
        Iterator iter = codeMap.keySet().iterator();
        String key = null;
        Integer value = null;
        boolean found = false;

        // key = description, value = id
        while (iter.hasNext()) {
            key = (String) iter.next();
            value = (Integer) codeMap.get(key);
            if (key.equals(institution) && value.equals(id)) {
                found = true;
                break;
            }
        }

        // new entry?
        if (!found) {
            // yes, than add it
            codeMap.put(institution, id);
        }

    }
}
