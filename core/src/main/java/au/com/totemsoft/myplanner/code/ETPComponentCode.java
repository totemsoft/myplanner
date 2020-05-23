/*
 * ETPComponentCode.java
 *
 * Created on 13 November 2001, 14:35
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ETPComponentCode extends Code {

    public final static Integer UNDEDUCTED = new Integer(1);

    public final static Integer PRE_1JULY_1983 = new Integer(2);

    public final static Integer POST_30JUNE_1983_TAXED = new Integer(3);

    public final static Integer POST_30JUNE_1983_UNTAXED = new Integer(4);

    public final static Integer CONCESSIONAL = new Integer(5);

    public final static Integer POST_30JUNE_1994_INVALIDITY = new Integer(6);

    public final static Integer CGT_EXEMPT = new Integer(7);

    public final static Integer NON_QUALIFYING = new Integer(8);

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

        codeMap.put("Undeducted", UNDEDUCTED);
        codeMap.put("Pre 1 July 1983", PRE_1JULY_1983);
        codeMap.put("Post 30 June 1983 (Taxed)", POST_30JUNE_1983_TAXED);
        codeMap.put("Post 30 June 1983 (Un-Taxed)", POST_30JUNE_1983_UNTAXED);
        codeMap.put("Concessional", CONCESSIONAL);
        codeMap
                .put("Post 30 June 1994 Invalidity",
                        POST_30JUNE_1994_INVALIDITY);
        codeMap.put("CGT Exempt", CGT_EXEMPT);
        codeMap.put("Non-Qualifying", NON_QUALIFYING);
    }

}
