/*
 * APRelationshipCodeID.java
 *
 * Created on 5 September 2002, 16:53
 */

package com.argus.financials.code;

import com.argus.util.ReferenceCode;

/**
 * 
 * @version
 */
public interface APRelationshipCodeID {

    public final static int iSPOUSE = 1;

    public final static int iDEPENDANT = 2;

    public final static int iCHILD = 3;

    public final static int iMISTRESS = 4;

    public final static Integer SPOUSE = new Integer(iSPOUSE);

    public final static Integer DEPENDANT = new Integer(iDEPENDANT);

    public final static Integer CHILD = new Integer(iCHILD);

    public final static Integer MISTRESS = new Integer(iMISTRESS);

    public final static ReferenceCode rcSPOUSE = new ReferenceCode(SPOUSE,
            "Spouse");

    public final static ReferenceCode rcDEPENDANT = new ReferenceCode(
            DEPENDANT, "Dependent");

    public final static ReferenceCode rcCHILD = new ReferenceCode(CHILD,
            "Child");

    public final static ReferenceCode rcMISTRESS = new ReferenceCode(MISTRESS,
            "Mistress");

}
