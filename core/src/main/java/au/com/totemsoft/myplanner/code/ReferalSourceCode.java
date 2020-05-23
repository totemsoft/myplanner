/*
 * ReferalSourceCode.java
 *
 * Created on 15 August 2001, 16:45
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Map;
import java.util.TreeMap;

public class ReferalSourceCode extends Code {

    public final static Integer FAMILY_MEMBER = new Integer(1);

    public final static Integer FRIEND = new Integer(2);

    public final static Integer PROF_ADVISER = new Integer(3);

    public final static Integer OTHER = new Integer(4);

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

        codeMap.put("Family Member", FAMILY_MEMBER);
        codeMap.put("Friend", FRIEND);
        codeMap.put("Professional Adviser", PROF_ADVISER);
        codeMap.put("Other", OTHER);
    }

}
