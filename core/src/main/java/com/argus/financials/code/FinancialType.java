/*
 * FinancialType.java
 *
 * Created on 15 October 2001, 13:18
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.argus.financials.service.ServiceLocator;
import com.argus.util.ReferenceCode;

public class FinancialType extends BaseCode implements FinancialTypeID {

    private static Map codes;

    private static FinancialType financialType;

    static {
        financialType = new FinancialType();
    }

    public FinancialType() {
        codes = new TreeMap(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {

        Collection finTypes = new ArrayList();

        Iterator iter = codes.values().iterator();
        while (iter.hasNext()) {
            Map map = (Map) iter.next();
            if (map != null)
                finTypes.addAll(map.keySet());
        }

        return finTypes;
    }

    public Map getCodeMap() {
        return codes;
    }

    /**
     * SEE UtilityService
     * 
     * CONTAINER structure Map( objType, Map( ReferenceCode( finTypeID,
     * finTypeDesc ), Vector( ReferenceCode( finCodeID, finCode, finCodeDesc ) ) ) )
     */
    private static void initCodes() {
        try {
            Map map = ServiceLocator.getInstance().getUtility()
                    .getFinancialObjectTypes();
            if (map == null)
                codes.clear();
            else
                codes.putAll(map);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

    /**
     * helper methodes
     */
    public static Object[] getFinancialTypes(Integer objectTypeID) {
        if (codes == null)
            return CODES_NONE;

        Map finTypes = (Map) codes.get(objectTypeID);
        if (finTypes == null)
            return CODES_NONE;

        return finTypes.keySet().toArray();
    }

    public static Object[] getFinancialCodes(Integer objectTypeID,
            ReferenceCode financialType) {
        if (objectTypeID == null || financialType == null)
            return CODES_NONE;

        Map finTypes = (Map) codes.get(objectTypeID);
        if (finTypes == null)
            return CODES_NONE;

        Vector finCodes = (Vector) finTypes.get(financialType);
        if (finCodes == null)
            return CODES_NONE;

        return finCodes.toArray();
    }

    public static ReferenceCode getFinancialType(Integer objectTypeID,
            Integer financialTypeID) {
        if (objectTypeID == null || financialTypeID == null)
            return null;

        Map finTypes = (Map) codes.get(objectTypeID);
        if (finTypes == null)
            return null;

        Iterator iter = finTypes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            ReferenceCode refCode = (ReferenceCode) entry.getKey();
            if (financialTypeID.equals(refCode.getCodeIDInteger()))
                return refCode;
        }

        return null;

    }

    public static ReferenceCode getFinancialCode(Integer objectTypeID,
            Integer financialTypeID, Integer financialCodeID) {

        if (financialCodeID == null)
            return null;

        // 21/10/2002 - FinancialTypeID in FinancialCode table can be null
        if (financialTypeID == null || financialTypeID.intValue() <= 0
                || INVESTMENT_LISTED_SHARES == financialTypeID.intValue()
                || INVESTMENT_LISTED_UNIT_TRUST == financialTypeID.intValue()
                || INVESTMENT_OTHER == financialTypeID.intValue()
                || INVESTMENT_PROPERTY == financialTypeID.intValue()

                || SUPERANNUATION_ACCOUNT == financialTypeID.intValue()

                || INCOMESTREAM_PENSION_ACCOUNT == financialTypeID.intValue()
                || INCOMESTREAM_ANNUITY_POLICY == financialTypeID.intValue()) {
            financialTypeID = new Integer(0);
            objectTypeID = new Integer(0);
        }

        Map finTypes = (Map) codes.get(objectTypeID);
        if (finTypes == null)
            return null;

        Iterator iter = finTypes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            ReferenceCode refCode = (ReferenceCode) entry.getKey();

            if (financialTypeID.intValue() == refCode.getCodeID()) {
                Iterator iter2 = ((Vector) entry.getValue()).iterator();
                while (iter2.hasNext()) {
                    ReferenceCode refCode2 = (ReferenceCode) iter2.next();
                    if (financialCodeID.intValue() == refCode2.getCodeID())
                        return refCode2;

                }

            }

        }

        return null;

    }

    public static void addFinancialCode(Integer objectTypeID,
            Integer financialTypeID, ReferenceCode financialCode) {

        // 21/10/2002 - FinancialTypeID in FinancialCode table can be null
        if (financialTypeID == null || 0 >= financialTypeID.intValue()
                || INVESTMENT_LISTED_SHARES == financialTypeID.intValue()
                || INVESTMENT_LISTED_UNIT_TRUST == financialTypeID.intValue()
                || INVESTMENT_OTHER == financialTypeID.intValue()
                || INVESTMENT_PROPERTY == financialTypeID.intValue()

                || SUPERANNUATION_ACCOUNT == financialTypeID.intValue()

                || INCOMESTREAM_PENSION_ACCOUNT == financialTypeID.intValue()
                || INCOMESTREAM_ANNUITY_POLICY == financialTypeID.intValue()) {
            financialTypeID = new Integer(0);
            objectTypeID = new Integer(0);
        }

        Map finTypes = (Map) codes.get(objectTypeID);
        if (finTypes == null)
            return;

        Iterator iter = finTypes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            // ReferenceCode finTypeRefCode = (ReferenceCode) entry.getKey();

            // if ( financialTypeID.equals( finTypeRefCode.getCodeIDInteger() )
            // ) {
            Vector financialCodes = (Vector) entry.getValue();
            // TODO: contains...

            if (financialCodes != null) {
                boolean found = false;

                for (int i = 0; i < financialCodes.size() && !found; i++) {
                    ReferenceCode help = (ReferenceCode) financialCodes
                            .elementAt(i);

                    // do we have an entry for the given FinancialCode?
                    if (help != null
                            && help.getCodeID() == financialCode.getCodeID()) {
                        // yes, break here
                        found = true;
                    }
                }
                // do we need to insert a new entry?
                if (!found) {
                    // yes
                    financialCodes.addElement(financialCode);
                }
            }
            break;
            // } // end if

        }

    }

    public static boolean containsFinancialCode(Integer objectTypeID,
            Integer financialTypeID, ReferenceCode financialCode) {
        boolean contains = false;

        if (objectTypeID == null || financialTypeID == null)
            return contains;

        Map finTypes = (Map) codes.get(objectTypeID);
        if (finTypes == null)
            return contains;

        Iterator iter = finTypes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            // ReferenceCode finTypeRefCode = (ReferenceCode) entry.getKey();

            // if ( financialTypeID.equals( finTypeRefCode.getCodeIDInteger() )
            // ) {
            Vector financialCodes = (Vector) entry.getValue();
            // TODO: contains...

            if (financialCodes != null) {
                boolean found = false;

                for (int i = 0; i < financialCodes.size() && !found; i++) {
                    ReferenceCode help = (ReferenceCode) financialCodes
                            .elementAt(i);

                    // do we have an entry for the given FinancialCode?
                    if (help != null
                            && help.getCode() != null
                            && help.getCodeDesc() != null
                            && help.getCode().equals(financialCode.getCode())
                            && help.getCodeDesc().equals(
                                    financialCode.getCodeDesc())) {
                        // yes, break here
                        found = true;

                        // System.err.println( "id: " + help.getCodeID() +
                        // "code: " + help.getCode() + " desc: " +
                        // help.getCodeDesc() );
                    }
                }
            }
            break;
            // } // end if

        }
        return contains;
    }
}