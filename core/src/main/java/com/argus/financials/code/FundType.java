package com.argus.financials.code;

/**
 *
 * @author  valeri chibaev
 * @version 
 */

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
            Map map = utilityService.getCodes(TABLE_NAME);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.api.ServiceException e) {
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
            if (fundTypeID.intValue() == rcANNUITY.getId())
                return rcANNUITY;
            if (fundTypeID.intValue() == rcANNUITY_LONG.getId())
                return rcANNUITY_LONG;
            if (fundTypeID.intValue() == rcANNUITY_SHORT.getId())
                return rcANNUITY_SHORT;
            if (fundTypeID.intValue() == rcPENSION.getId())
                return rcPENSION;
        }

        return ReferenceCode.CODE_NONE;

    }

}
