/*
 * ItemsSortComperator.java
 *
 * Created on 19 November 2002, 08:13
 */

package com.argus.financials.report.data;

/**
 * 
 * @author shibaevv
 */
public class FinancialDataItemComperator implements java.util.Comparator {

    /** Creates a new instance of ItemsSortComperator */
    public FinancialDataItemComperator() {
    }

    /**
     * Compares two AssetAllocationTableRow objects. The asset name of each
     * object is compared character by character. When the descriptions are
     * equal the return value is 0. In any other case the return value is
     * calculated like this: character_1 - character_2 or len_1 - len_2
     * 
     * @param o1 -
     *            InterfaceSortItems
     * @param o2 -
     *            InterfaceSortItems
     * 
     * @return an int value
     */
    public int compare(Object o1, Object o2) {
        InterfaceSortItem aiaitr_1 = (InterfaceSortItem) o1;
        InterfaceSortItem aiaitr_2 = (InterfaceSortItem) o2;

        String s1 = aiaitr_1.getKeyString();
        String s2 = aiaitr_2.getKeyString();

        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }

        int len1 = s1.length();
        int len2 = s2.length();

        for (int i = 0, n = Math.min(len1, len2); i < n; i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 != c2)
                return c1 - c2;
        }

        return len1 - len2;
    }

}
