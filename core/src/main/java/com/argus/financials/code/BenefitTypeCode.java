/*
 * BenefitTypeCode.java
 *
 * Created on 12 March 2002, 15:21
 */

package com.argus.financials.code;

/**
 * 
 * @author shibaevv
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class BenefitTypeCode extends Code {

    public final static Integer NONE = new Integer(0);

    public final static Integer AGE_PENSION = new Integer(1);

    public final static Integer NEW_START_ALLOWANCE = new Integer(2);

    public final static Integer DISABILITY_SUPPORT_PENSION = new Integer(3);

    public final static Integer MATURE_AGE_ALLOWANCE = new Integer(4);

    public final static Integer PARTNER_ALLOWANCE = new Integer(5);

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

        codeMap.put("Partner Allowance", PARTNER_ALLOWANCE);
        codeMap.put("Mature Age Allowance", MATURE_AGE_ALLOWANCE);
        codeMap.put("Disability Support Pension", DISABILITY_SUPPORT_PENSION);

        codeMap.put("Age Pension", AGE_PENSION);
        codeMap.put("Newstart Allowance", NEW_START_ALLOWANCE);
        codeMap.put("None", NONE);

    }

}
