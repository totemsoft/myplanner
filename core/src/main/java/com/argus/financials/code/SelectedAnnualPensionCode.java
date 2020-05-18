/*
 * SelectedAnnualPensionCode.java
 *
 * Created on 10 September 2002, 11:33
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

public class SelectedAnnualPensionCode extends BaseCode implements
        SelectedAnnualPensionCodeID {

    private static Collection codes;

    /** Creates new SelectedAnnualPensionCode */
    public SelectedAnnualPensionCode() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {
        codes.add(CODE_NONE);

        codes.add(rcMAX);
        codes.add(rcMIN);
        codes.add(rcOTHER);
    }
}
