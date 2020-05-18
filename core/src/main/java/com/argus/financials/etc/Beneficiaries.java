/*
 * Beneficiaries.java
 *
 * Created on August 9, 2002, 1:54 PM
 */

package com.argus.financials.etc;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.argus.financials.code.BaseCodes;

public class Beneficiaries extends BaseCodes {

    /** Creates new Beneficiaries */
    public Beneficiaries() {
        super();
    }

    public Beneficiaries(java.util.Map details) {
        this();
        initCodes(details);
    }

    public void initCodes(java.util.Map details) {

        super.initCodes(details);

        if (details == null)
            return;

        getCodes().addAll(details.values());

    }

}
