/*
 * AssetInvestmentAvailableInvestmentsSelectionDescriptionComperator.java
 *
 * Created on 19 July 2002, 16:36
 */

package com.argus.financials.assetinvestment;

/**
 * Comperator to compare "AvailableInvestmentsTableRow" objects, it's used to
 * sort a vector of these objects. It compares the description attribute of the
 * objects.
 * 
 * @author shibaevv
 * @version 0.01
 * 
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSearch
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsTableRow
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSelectionInvestmentCodeComperator
 */
public class AvailableInvestmentsSelectionDescriptionComperator implements
        java.util.Comparator {

    /** Creates new DescriptionComperator */
    public AvailableInvestmentsSelectionDescriptionComperator() {
        super();
    }

    /**
     * Compares two AssetInvestmentAvailableInvestmentsTableRow objects. The
     * description of each object is compared character by character. When the
     * descriptions are equal the return value is 0. In any other case the
     * return value is calculated like this: character_1 - character_2 or len_1 -
     * len_2
     * 
     * @param o1 -
     *            AssetInvestmentAvailableInvestmentsTableRow
     * @param o2 -
     *            AssetInvestmentAvailableInvestmentsTableRow
     * 
     * @return an int value
     */
    public int compare(Object o1, Object o2) {
        AvailableInvestmentsTableRow aiaitr_1 = (AvailableInvestmentsTableRow) o1;
        AvailableInvestmentsTableRow aiaitr_2 = (AvailableInvestmentsTableRow) o2;

        String s1 = aiaitr_1.description;
        String s2 = aiaitr_2.description;

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
