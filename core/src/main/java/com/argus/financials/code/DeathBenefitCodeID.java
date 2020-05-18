/*
 * DeathBenefitCodeID.java
 *
 * Created on 2 September 2002, 15:07
 */

package com.argus.financials.code;

import com.argus.util.ReferenceCode;

/**
 * 
 * @author shibaevv
 * @version
 */
public interface DeathBenefitCodeID {

    public final static int iLUMP_SUM = 1;

    public final static int iREVERSIONARY = 2;

    public final static int iSURVIVING_DEPENDENT = 3;

    public final static Integer LUMP_SUM = new Integer(iLUMP_SUM);

    public final static Integer REVERSIONARY = new Integer(iREVERSIONARY);

    public final static Integer SURVIVING_DEPENDENT = new Integer(
            iSURVIVING_DEPENDENT);

    public final static ReferenceCode rcLUMP_SUM = new ReferenceCode(LUMP_SUM,
            "Lump Sum");

    public final static ReferenceCode rcREVERSIONARY = new ReferenceCode(
            REVERSIONARY, "Reversionary");

    public final static ReferenceCode rcSURVIVING_DEPENDENT = new ReferenceCode(
            SURVIVING_DEPENDENT, "Surviving Dependent");

}
