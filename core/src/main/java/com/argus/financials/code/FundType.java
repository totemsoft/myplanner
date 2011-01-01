package com.argus.financials.code;

/**
 *
 * @author  valeri chibaev
 * @version 
 */

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.service.ServiceLocator;
import com.argus.util.ReferenceCode;

public class FundType extends Code {

    public static String TABLE_NAME = "FundType";

    public final static ReferenceCode rcANNUITY = new ReferenceCode(1,
            "Annuity (Medium Term)");

    public final static ReferenceCode rcANNUITY_LONG = new ReferenceCode(4,
            "Annuity (Long Term)");

    public final static ReferenceCode rcANNUITY_SHORT = new ReferenceCode(5,
            "Annuity (Short Term)");

    public final static ReferenceCode rcPENSION = new ReferenceCode(2,
            "Pension");

    public final static ReferenceCode rcSUPERANNUATION = new ReferenceCode(3,
            "Superannuation");

    private static Map codeMap;

    static {
        if (codeMap == null) {
            codeMap = new TreeMap(
            /*
             * new Comparator() {
             * 
             * public int compare(Object o1, Object o2) { String s1 = (String)
             * o1; String s2 = (String) o2;
             * 
             * if ( s1.equalsIgnoreCase( s2 ) ) return 0;
             * 
             * if ( s1.equalsIgnoreCase( EMPTY ) ) return -1; if (
             * s2.equalsIgnoreCase( EMPTY ) ) return 1;
             * 
             * if ( s1.equalsIgnoreCase( rcANNUITY.getCodeDesc() ) ) return -1;
             * if ( s2.equalsIgnoreCase( rcANNUITY.getCodeDesc() ) ) return 1;
             * 
             * if ( s1.equalsIgnoreCase( rcPENSION.getCodeDesc() ) ) return -1;
             * if ( s2.equalsIgnoreCase( rcPENSION.getCodeDesc() ) ) return 1;
             * 
             * if ( s1.equalsIgnoreCase( rcSUPERANNUATION.getCodeDesc() ) )
             * return -1; if ( s2.equalsIgnoreCase(
             * rcSUPERANNUATION.getCodeDesc() ) ) return 1;
             * 
             * return _compare( s1, s2 ); }
             * 
             * public boolean equals(Object obj) { return
             * this.toString().equalsIgnoreCase( obj.toString() ); }
             * 
             * private int _compare(String s1, String s2) { if ( s1 == null &&
             * s2 == null ) return 0; if ( s1 == null ) return -1; if ( s2 ==
             * null ) return 1;
             * 
             * int result = s1.compareTo(s2); if (result < 0) return -1; if
             * (result > 0) return 1; return 0; }
             *  }
             */
            );
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
                    TABLE_NAME);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void addFundType(String description, Integer id) {
        Iterator iter = codeMap.keySet().iterator();
        String key = null;
        Integer value = null;
        boolean found = false;

        // key = description, value = id
        while (iter.hasNext()) {
            key = (String) iter.next();
            value = (Integer) codeMap.get(key);
            if (key.equals(description) && value.equals(id)) {
                found = true;
                break;
            }
        }

        // new entry?
        if (!found) {
            // yes, than add it
            codeMap.put(description, id);
        }

    }

    public static ReferenceCode getFundType(Integer fundTypeID) {

        if (fundTypeID != null) {
            if (fundTypeID.intValue() == rcANNUITY.getCodeID())
                return rcANNUITY;
            if (fundTypeID.intValue() == rcANNUITY_LONG.getCodeID())
                return rcANNUITY_LONG;
            if (fundTypeID.intValue() == rcANNUITY_SHORT.getCodeID())
                return rcANNUITY_SHORT;
            if (fundTypeID.intValue() == rcPENSION.getCodeID())
                return rcPENSION;
        }

        return ReferenceCode.CODE_NONE;

    }

}
