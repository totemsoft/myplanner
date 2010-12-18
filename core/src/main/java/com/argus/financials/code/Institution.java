/*
 * Institution.java
 *
 * Created on 16 October 2001, 10:31
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.service.ServiceLocator;

public class Institution extends Code {

    public final static Integer FIDUCIAN = new Integer(1);

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
        codeMap.put(KEY_NONE, VALUE_NONE);

        try {
            Map map = ServiceLocator.getInstance().getUtilityService().getCodes(
                    "Institution", "InstitutionID", "InstitutionName");
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException re) {
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
