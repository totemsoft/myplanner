/*
 * AdviserTypeCode.java
 *
 * Created on 11 June 2002, 13:16
 */

package com.argus.financials.code;

/**
 * 
 * @author shibaevv
 * @author valeri chibaev
 * @version see also database table AdviserTypeCode
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;
import com.argus.util.ReferenceCode;

public class AdviserTypeCode extends BaseCode {
    // (1, 'Franchise');
    public final static Integer FRANCHISE = new Integer(1);

    // to look after all clients (of all advisers)
    // (2, 'Dealer');
    public final static Integer DEALER = new Integer(2);

    // (3, 'Proper Authority');
    public final static Integer PROPER_AUTHORITY = new Integer(3);

    // (3, 'Administrator');
    public final static Integer ADMINISTRATOR = new Integer(4);

    // to look after clients of group of advisers
    // (5, 'Adviser Support');
    public final static Integer ADVISER_SUPPORT = new Integer(5);

    private static Collection codes;

    public Collection getCodes() {
        if (codes == null) {
            codes = new TreeSet(new CodeComparator());
            initCodes();
        }
        return codes;
    }

    private void initCodes() {

        // codes.add( CODE_NONE );

        /*
         * codes.add( new ReferenceCode( DEALER, "Dealer" ) );
         * codes.add( new ReferenceCode( PROPER_AUTHORITY, "Proper Authority" ) );
         * codes.add( new ReferenceCode( ADMINISTRATOR, "Administrator" ) );
         * codes.add( new ReferenceCode( ADVISER_SUPPORT, "Adviser Support" ) );
         */

        try {
            Map map = utilityService.getCodes("AdviserTypeCode");
            if (map == null)
                return;

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                codes.add(new ReferenceCode((Integer) entry.getValue(),
                        (String) entry.getKey()));
            }

        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

    public static boolean isSupportPerson(Integer userTypeCodeID) {
        return DEALER.equals(userTypeCodeID)
                || ADVISER_SUPPORT.equals(userTypeCodeID);
    }

    public static boolean isAdminPerson(Integer userTypeCodeID) {
        return ADMINISTRATOR.equals(userTypeCodeID);
    }

}
