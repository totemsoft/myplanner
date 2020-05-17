/*
 * ContactMediaCode.java
 *
 * Created on 2 August 2001, 11:17
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ContactMediaCode extends Code {

    public final static Integer PHONE = new Integer(1);

    public final static Integer PHONE_WORK = new Integer(2);

    public final static Integer FAX = new Integer(3);

    public final static Integer FAX_WORK = new Integer(4);

    public final static Integer MOBILE = new Integer(5);

    public final static Integer MOBILE_WORK = new Integer(6);

    public final static Integer EMAIL = new Integer(7);

    public final static Integer EMAIL_WORK = new Integer(8);

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

        codeMap.put("Phone (home)", PHONE);
        codeMap.put("Phone (work)", PHONE_WORK);
        codeMap.put("Fax (home)", FAX);
        codeMap.put("Fax (work)", FAX_WORK);
        codeMap.put("Mobile", MOBILE);
        codeMap.put("Mobile Work", MOBILE_WORK);
        codeMap.put("E-mail", EMAIL);
        codeMap.put("E-mail Work", EMAIL_WORK);
    }

}
