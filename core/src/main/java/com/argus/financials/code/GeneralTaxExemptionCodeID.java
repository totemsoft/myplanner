/*
 * GeneralTaxExemptionCodeID.java
 *
 * Created on 2 September 2002, 14:29
 */

package com.argus.financials.code;

import com.argus.util.ReferenceCode;

/**
 * 
 * @author shibaevv
 * @version
 */
public interface GeneralTaxExemptionCodeID {

    public final static int iGENERAL_EXEMPTION = 1;

    public final static int iNO_EXEMPTION = 2;

    public final static int iNON_RESIDENT = 3;

    public final static Integer GENERAL_EXEMPTION = new Integer(
            iGENERAL_EXEMPTION);

    public final static Integer NO_EXEMPTION = new Integer(iNO_EXEMPTION);

    public final static Integer NON_RESIDENT = new Integer(iNON_RESIDENT);

    public final static ReferenceCode rcGENERAL_EXEMPTION = new ReferenceCode(
            GENERAL_EXEMPTION, "General Exemption");

    public final static ReferenceCode rcNO_EXEMPTION = new ReferenceCode(
            NO_EXEMPTION, "No Exemption");

    public final static ReferenceCode rcNON_RESIDENT = new ReferenceCode(
            NON_RESIDENT, "Non Resident");

}
