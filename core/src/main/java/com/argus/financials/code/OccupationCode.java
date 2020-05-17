/*
 * OccupationCode.java
 *
 * Created on 30 August 2001, 10:22
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class OccupationCode extends Code {

    public static final String OCCUPATION_TABLE = "OccupationCode";

    // 1 Administration & Secretarial
    // 2 Apprenticeships
    // 3 Arts & Design
    // 4 Auditing
    // 5 Building & Construction
    // 6 BusinessService Analysis
    // 7 Cleaning
    // 8 Community Care
    // 9 Counselling/Social Work
    // 10 Economics & Statistics
    // 11 Education & Teaching
    // 12 Engineering & Surveying
    // 13 Environment/Health & Safety
    // 14 Farming
    // 15 Finance & Accounting
    // 16 Fishery & Marine
    // 17 Health Care
    // 18 Hospitality
    // 19 Human Resources
    // 20 I.T. & Telecoms
    // 21 Legal
    // 22 Library & Information Mgmt
    // 23 Management
    // 24 Marketing & Sales
    // 25 Policy, Planning & Research
    // 26 Property & Real Estate
    // 27 Public Affairs
    // 28 Sciences
    // 29 Tradesmen
    // 30 Traineeships
    // 31 Vehicle/Machine Operation
    // 32 Doctor
    // 33 Other

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
            Map map = utilityService.getCodes(OCCUPATION_TABLE);
            if (map == null)
                return;
            codeMap.putAll(map);
        } catch (com.argus.financials.api.ServiceException re) {
        }
    }

}
