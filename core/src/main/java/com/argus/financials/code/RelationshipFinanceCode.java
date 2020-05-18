/*
 * RelationshipFinanceCode.java
 *
 * Created on 23 August 2001, 09:45
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;
import com.argus.util.ReferenceCode;

public class RelationshipFinanceCode extends BaseCode {
    public static final String TABLE_NAME = "RelationshipFinanceCode";

    // 9, 'Partner');
    public final static ReferenceCode rcPARTNER = new ReferenceCode(9,
            "Partner");

    // 14, 'Solicitor');
    public final static ReferenceCode rcSOLICITOR = new ReferenceCode(14,
            "Solicitor");

    // 15, 'Accountant');
    public final static ReferenceCode rcACCOUNTANT = new ReferenceCode(15,
            "Accountant");

    // 16, 'Authorised Agent');
    public final static ReferenceCode rcAUTHORIZED_AGENT = new ReferenceCode(
            16, "Authorised Agent");

    // 17, 'Bank Manager');
    public final static ReferenceCode rcBANK_MANAGER = new ReferenceCode(17,
            "Bank Manager");

    // 18, 'Barrister');
    public final static ReferenceCode rcBARRISTER = new ReferenceCode(18,
            "Barrister");

    // 19, 'Corporate Trustee');
    public final static ReferenceCode rcCORPORATE_TRUSTEE = new ReferenceCode(
            19, "Corporate Trustee");

    // 20, 'Correspondent');
    public final static ReferenceCode rcCORRESPONDENT = new ReferenceCode(20,
            "Correspondent");

    // 21, 'Doctor');
    public final static ReferenceCode rcDOCTOR = new ReferenceCode(21, "Doctor");

    // 22, 'Financial Manager');
    public final static ReferenceCode rcFINANCIAL_MANAGER = new ReferenceCode(
            22, "Financial Manager");

    // 23, 'Legal Adviser');
    public final static ReferenceCode rcLEGAL_ADVISER = new ReferenceCode(23,
            "Legal Adviser");

    // 24, 'Power of Attorney');
    public final static ReferenceCode rcPOWER_OF_ATTORNEY = new ReferenceCode(
            24, "Power of Attorney");

    // 25, 'Secretary');
    public final static ReferenceCode rcSECRETARY = new ReferenceCode(25,
            "Secretary");

    private static Collection codes;

    public RelationshipFinanceCode() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {
        codes.clear();
        codes.add(CODE_NONE);

        codes.add(rcPARTNER);
        codes.add(rcSOLICITOR);
        codes.add(rcACCOUNTANT);
        codes.add(rcAUTHORIZED_AGENT);
        codes.add(rcBANK_MANAGER);
        codes.add(rcBARRISTER);
        codes.add(rcCORPORATE_TRUSTEE);
        codes.add(rcCORRESPONDENT);
        codes.add(rcDOCTOR);
        codes.add(rcFINANCIAL_MANAGER);
        codes.add(rcLEGAL_ADVISER);
        codes.add(rcPOWER_OF_ATTORNEY);
        codes.add(rcSECRETARY);

        /*
         * try { Map map = RmiParams.getInstance().getUtility().getCodes(
         * TABLE_NAME ); if (map == null) return;
         * 
         * Iterator iter = map.entrySet().iterator(); while ( iter.hasNext() ) {
         * Map.Entry entry = (Map.Entry) iter.next(); codes.add( new
         * ReferenceCode( (Integer) entry.getValue(), (String) entry.getKey() ) ); }
         *  } catch (com.argus.financials.service.ServiceException e) { e.printStackTrace( System.err ); }
         */

    }

}
