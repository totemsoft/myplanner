/*
 * GeneralTaxExemptionCode.java
 *
 * Created on 2 September 2002, 14:46
 */

package au.com.totemsoft.myplanner.code;

import java.util.Collection;
import java.util.TreeSet;

import au.com.totemsoft.myplanner.api.code.CodeComparator;

public class GeneralTaxExemptionCode extends BaseCode implements
        GeneralTaxExemptionCodeID {

    private static Collection codes;

    /** Creates new GeneralTaxExemptionCode */
    public GeneralTaxExemptionCode() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {
        codes.add(CODE_NONE);

        codes.add(rcGENERAL_EXEMPTION);
        codes.add(rcNO_EXEMPTION);
        codes.add(rcNON_RESIDENT);
    }

}
