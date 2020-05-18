/*
 * HealthStateCode.java
 *
 * Created on 17 August 2001, 12:15
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class HealthStateCode extends Code {

    // 1 = Excellent; 2 = Very Good; 3 = Good; 4 = Poor; 5 = Very Poor
    public final static Integer OTHER = new Integer(0);

    public final static Integer EXCELLENT = new Integer(1);

    public final static Integer VERY_GOOD = new Integer(2);

    public final static Integer GOOD = new Integer(3);

    public final static Integer POOR = new Integer(4);

    public final static Integer VERY_POOR = new Integer(5);

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

        codeMap.put("Excellent", EXCELLENT);
        codeMap.put("Very Good", VERY_GOOD);
        codeMap.put("Good", GOOD);
        codeMap.put("Poor", POOR);
        codeMap.put("Very Poor", VERY_POOR);
    }

    public String[] getCodeDescriptions() {

        Set set = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                Integer d1 = (Integer) getCodeMap().get((String) o1);
                Integer d2 = (Integer) getCodeMap().get((String) o2);
                return (d1 == null ? new Integer(0) : d1)
                        .compareTo(d2 == null ? new Integer(0) : d2);
            }
        });

        set.addAll(getCodeMap().keySet());

        return (String[]) set.toArray(new String[0]);

    }

}
