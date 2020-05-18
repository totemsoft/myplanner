/*
 * AssetAllocationAssetNameComperator.java
 *
 * Created on 10 October 2002, 16:25
 */

package com.argus.financials.assetallocation;

/**
 * Comperator to compare "AssetAllocationTableRow" objects, it's used to sort a
 * vector of these objects. It compares the asset name of the objects.
 * 
 * @author shibaevv
 * @version 0.01
 * 
 * @see com.argus.financials.assetallocation.AssetAllocationTableRow
 */
public class AssetAllocationAssetNameComperator implements java.util.Comparator {

    /** Creates a new instance of AssetAllocationNameComperator */
    public AssetAllocationAssetNameComperator() {
        super();
    }

    /**
     * Compares two AssetAllocationTableRow objects. The asset name of each
     * object is compared character by character. When the descriptions are
     * equal the return value is 0. In any other case the return value is
     * calculated like this: character_1 - character_2 or len_1 - len_2
     * 
     * @param o1 -
     *            AssetInvestmentAvailableInvestmentsTableRow
     * @param o2 -
     *            AssetInvestmentAvailableInvestmentsTableRow
     * 
     * @return an int value
     */
    public int compare(Object o1, Object o2) {
        AssetAllocationTableRow aiaitr_1 = (AssetAllocationTableRow) o1;
        AssetAllocationTableRow aiaitr_2 = (AssetAllocationTableRow) o2;

        String s1 = aiaitr_1.asset_name;
        String s2 = aiaitr_2.asset_name;

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
