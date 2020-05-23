/*
 * DeathBenefitCode.java
 *
 * Created on 2 September 2002, 15:27
 */

package au.com.totemsoft.myplanner.code;

import java.util.Collection;
import java.util.TreeSet;

import au.com.totemsoft.myplanner.api.code.CodeComparator;

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
