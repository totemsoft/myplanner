/*
 * TitleCode.java
 *
 * Created on 23 July 2000, 12:19
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public final class TitleCode extends Code {

    public static String TABLE_NAME = "TitleCode";

    public final static Integer MR = new Integer(1);

    public final static Integer MS = new Integer(2);

    public final static Integer MRS = new Integer(3);

    public final static Integer DOCTOR = new Integer(4);

    public final static Integer CAPTAIN = new Integer(5);

    public final static Integer DAME = new Integer(6);

    public final static Integer DIRECTOR = new Integer(7);

    public final static Integer FATHER = new Integer(8);

    public final static Integer JUDGE = new Integer(9);

    public final static Integer LADY = new Integer(10);

    public final static Integer MADAM = new Integer(11);

    public final static Integer MAJ_GEN_ = new Integer(12);

    public final static Integer MANAGER = new Integer(13);

    public final static Integer MASTER = new Integer(14);

    public final static Integer MESSRS = new Integer(15);

    public final static Integer MISS = new Integer(16);

    public final static Integer PASTOR = new Integer(17);

    public final static Integer PAYMASTER = new Integer(18);

    public final static Integer PROFESSOR = new Integer(19);

    public final static Integer REAR_ADMIRAL = new Integer(20);

    public final static Integer REVEREND = new Integer(21);

    public final static Integer SECRETARY = new Integer(22);

    public final static Integer SIR = new Integer(23);

    public final static Integer THE_VEN_ = new Integer(24);

    public final static Integer VENERABLE = new Integer(25);

    public final static String strMR = "Mr";

    public final static String strMS = "Ms";

    public final static String strMRS = "Mrs";

    public final static String strDOCTOR = "Dr";

    private static Map codeMap;

    protected Map getCodeMap() {
        if (codeMap == null) {
            codeMap = new TreeMap(new Comparator() {

                public boolean equals(Object obj) {
                    return this.toString().equalsIgnoreCase(obj.toString());
                }

                public int compare(Object o1, Object o2) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;

                    if (s1.equalsIgnoreCase(s2))
                        return 0;

                    if (s1.equalsIgnoreCase(NONE))
                        return -1;
                    if (s2.equalsIgnoreCase(NONE))
                        return 1;

                    if (s1.equalsIgnoreCase(strMR))
                        return -1;
                    if (s2.equalsIgnoreCase(strMR))
                        return 1;

                    if (s1.equalsIgnoreCase(strMS))
                        return -1;
                    if (s2.equalsIgnoreCase(strMS))
                        return 1;

                    if (s1.equalsIgnoreCase(strMRS))
                        return -1;
                    if (s2.equalsIgnoreCase(strMRS))
                        return 1;

                    if (s1.equalsIgnoreCase(strDOCTOR))
                        return -1;
                    if (s2.equalsIgnoreCase(strDOCTOR))
                        return 1;

                    return _compare(s1, s2);
                }

                private int _compare(String s1, String s2) {
                    if (s1 == null && s2 == null)
                        return 0;
                    if (s1 == null)
                        return -1;
                    if (s2 == null)
                        return 1;

                    int result = s1.compareTo(s2);
                    if (result < 0)
                        return -1;
                    if (result > 0)
                        return 1;
                    return 0;
                }

            });

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

}
