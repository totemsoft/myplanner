/*
 * AlfaSorter.java
 *
 * Created on 13 June 2003, 11:38
 */

package com.argus.financials.swing.table;

import com.argus.swing.table.MergeSort;

/**
 * 
 * @author valeri chibaev
 * 
 * Sorts the contents, based on alfabetical order.
 * 
 */

public class AlfaSorter extends MergeSort {

    /** Creates a new instance of AlfaSorter */
    public AlfaSorter() {
    }

    public int compareElementsAt(int beginLoc, int endLoc) {
        Object obj1 = toSort[beginLoc];
        Object obj2 = toSort[endLoc];
        return compare(obj1 == null ? null : obj1.toString(),
                obj2 == null ? null : obj2.toString());
    }

    private int compare(String s1, String s2) {
        if (s1 == null && s2 == null)
            return 0;
        if (s1 == null)
            return -1;
        if (s2 == null)
            return 1;

        int result = s1.compareToIgnoreCase(s2);
        if (result < 0)
            return -1;
        if (result > 0)
            return 1;
        return 0;
    }

}
