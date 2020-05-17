/*
 * InterestCategoryCode.java
 *
 * Created on 7 January 2003, 14:16
 */

package com.argus.financials.code;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;
import com.argus.util.ReferenceCode;

public class InterestCategoryCode extends BaseCode {

    private static Collection codes;

    /** Creates new InterestCategoryCode */
    public InterestCategoryCode() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    public Object[] getCodeDescriptions() {

        Collection codes = getCodes();
        if (codes != null)
            return codes.toArray();

        return new String[0];

    }

    private void initCodes() {

        try {
            Map map = utilityService.getCodes("Category", "CategoryID", "CategoryName");
            if (map == null)
                return;

            codes.add(BaseCode.CODE_NONE);

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                codes.add(new ReferenceCode((Integer) entry.getValue(),
                        (String) entry.getKey()));
            }

        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

}
