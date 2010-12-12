/*
 * BooleanCode.java
 *
 * Created on 17 August 2001, 12:22
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.TreeSet;

import com.argus.util.ReferenceCode;

public class BooleanCode extends BaseCode {

    public static final ReferenceCode rcNO = new ReferenceCode(1, "N", "No");

    public static final ReferenceCode rcYES = new ReferenceCode(2, "Y", "Yes");

    private static Collection codes;

    /** Creates new BooleanCode */
    public BooleanCode() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {

        codes.add(CODE_NONE);

        codes.add(rcNO);
        codes.add(rcYES);
    }

}
