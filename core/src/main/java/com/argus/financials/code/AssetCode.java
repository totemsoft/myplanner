/*
 * AssetCode.java
 *
 * Created on 13 September 2001, 13:10
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class AssetCode extends Code implements FinancialClassID {

    public final static Integer AUS_FIXED_INTEREST = new Integer(1007);

    public final static Integer INT_FIXED_INTEREST = new Integer(1008);

    public final static Integer CPI_FIXED_INTEREST = new Integer(1009);

    public final static Integer AUS_SHARES = new Integer(1010);

    public final static Integer INT_SHARES = new Integer(1011);

    public final static Integer MORGAGES = new Integer(1012);

    public final static Integer TRADED_POLICIES = new Integer(1013);

    public final static Integer OTHER = new Integer(255);

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
        // codeMap.put( KEY_NONE, VALUE_NONE );

        codeMap.put("Cash", ASSET_CASH);
        codeMap.put("Super", ASSET_SUPERANNUATION);
        /*
         * OLD-VERSION: codeMap.put( "Savings", ASSET_INVESTMENT );
         */
        // BEGIN: BUG FIX 621 - 11/07/2002
        // by shibaevv
        // BUG DESCRIPTION FROM TASKLOGGER:
        // "...Quickview ==> Details
        // 1. In the Assets before Retirement section it should say Cash, Super,
        // Investments.
        // Currently it is Cash, Super, Savings..."
        codeMap.put("Investments", ASSET_INVESTMENT);
        // END: BUG FIX 621

        codeMap.put("Aust. Fixed Interest", AUS_FIXED_INTEREST);
        codeMap.put("Intnl. Fixed Interest", INT_FIXED_INTEREST);
        codeMap.put("CPI Fixed Interest", CPI_FIXED_INTEREST);
        codeMap.put("Aust. Shares", AUS_SHARES);
        codeMap.put("Intnl. Shares", INT_SHARES);
        codeMap.put("Morgages", MORGAGES);
        codeMap.put("Traded Policies", TRADED_POLICIES);

        codeMap.put("Other", OTHER);
    }

}
