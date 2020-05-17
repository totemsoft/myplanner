/*
 * APRelationshipCode.java
 *
 * Created on 5 September 2002, 16:53
 */

package com.argus.financials.code;

import java.util.Collection;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;

public class APRelationshipCode extends BaseCode implements
        APRelationshipCodeID {
    private static Collection codes;

    /** Creates new APRelationshipCode */
    public APRelationshipCode() {
        if (codes == null) {
            codes = new TreeSet(new CodeComparator());
            initCodes();
        }
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {
        codes.add(CODE_NONE);

        codes.add(rcSPOUSE);
        codes.add(rcDEPENDANT);
        codes.add(rcCHILD);
        // codes.add( rcMISTRESS );
    }

}
