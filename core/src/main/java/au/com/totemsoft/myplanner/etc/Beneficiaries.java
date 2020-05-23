/*
 * Beneficiaries.java
 *
 * Created on August 9, 2002, 1:54 PM
 */

package au.com.totemsoft.myplanner.etc;

import au.com.totemsoft.myplanner.code.BaseCodes;

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
