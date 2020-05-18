/*
 * BooleanCode.java
 *
 * Created on 17 August 2001, 12:22
 */

package com.argus.financials.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Collection;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;
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

    public static ReferenceCode convert(boolean b) {
        return b ? rcYES : rcNO;
    }

}