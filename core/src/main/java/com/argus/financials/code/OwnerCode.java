/*
 * OwnerCode.java
 *
 * Created on 14 September 2001, 12:35
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

// (1, 'ClientView')
// (2, 'Partner')
// (3, 'Joint')
// (4, 'ClientView's Company')
// (5, 'Partner's Company')
// (6, 'Joint Company')
// (7, 'Family Trust')

public class OwnerCode extends Code {

    public final static Integer CLIENT = new Integer(1);

    public final static Integer PARTNER = new Integer(2);

    public final static Integer JOINT = new Integer(3);

    public final static Integer CLIENT_COMPANY = new Integer(4);

    public final static Integer PARTNER_COMPANY = new Integer(5);

    public final static Integer JOINT_COMPANY = new Integer(6);

    public final static Integer FAMILY_TRUST = new Integer(7);

    public final static Integer TENNANTS_IN_COMMON = new Integer(8);

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

        codeMap.put("ClientView", CLIENT);
        codeMap.put("Partner", PARTNER);
        codeMap.put("Joint", JOINT);
        codeMap.put("ClientView's Company", CLIENT_COMPANY);
        codeMap.put("Partner's Company", PARTNER_COMPANY);
        codeMap.put("Joint Company", JOINT_COMPANY);
        codeMap.put("Family Trust", FAMILY_TRUST);
        codeMap.put("Tennants in Common", TENNANTS_IN_COMMON);
        /*
         * try { Map map = RmiParams.getInstance().getUtility().getCodes(
         * "OwnerCode" ); if (map == null) return;
         * 
         * Iterator iter = map.entrySet().iterator(); while ( iter.hasNext() ) {
         * Map.Entry entry = (Map.Entry) iter.next(); codes.add( new
         * ReferenceCode( (Integer) entry.getValue(), (String) entry.getKey() ) ); }
         *  } catch (com.argus.financials.service.ServiceException e) { e.printStackTrace( System.err ); }
         */
    }

}
