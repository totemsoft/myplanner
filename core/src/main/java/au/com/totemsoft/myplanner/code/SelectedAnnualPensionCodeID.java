/*
 * SelectedAnnualPensionCodeID.java
 *
 * Created on 10 September 2002, 11:33
 */

package au.com.totemsoft.myplanner.code;

import au.com.totemsoft.util.ReferenceCode;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public interface SelectedAnnualPensionCodeID {

    public final static int iMAX = 1;

    public final static int iMIN = 2;

    public final static int iOTHER = 3;

    public final static Integer MAX = new Integer(iMAX);

    public final static Integer MIN = new Integer(iMIN);

    public final static Integer OTHER = new Integer(iOTHER);

    public final static ReferenceCode rcMAX = new ReferenceCode(MAX, "Max");

    public final static ReferenceCode rcMIN = new ReferenceCode(MIN, "Min");

    public final static ReferenceCode rcOTHER = new ReferenceCode(OTHER,
            "Other");

}
