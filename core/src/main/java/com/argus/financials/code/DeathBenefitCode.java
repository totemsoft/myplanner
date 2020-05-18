/*
 * DeathBenefitCode.java
 *
 * Created on 2 September 2002, 15:27
 */

package com.argus.financials.code;

import java.util.Collection;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;

public class DeathBenefitCode extends BaseCode implements DeathBenefitCodeID {

    private static Collection codes;

    /** Creates new DeathBenefitCode */
    public DeathBenefitCode() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {
        codes.add(CODE_NONE);

        codes.add(rcLUMP_SUM);
        codes.add(rcREVERSIONARY);
        codes.add(rcSURVIVING_DEPENDENT);
    }

}
