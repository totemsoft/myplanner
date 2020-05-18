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

import com.argus.financials.api.bean.ICode;
import com.argus.financials.api.bean.hibernate.FinancialCode;
import com.argus.financials.api.code.FinancialTypeID;
import com.argus.util.ReferenceCode;

public class FinancialType extends BaseCode {

//    private static final FinancialType financialType = new FinancialType();

//    private static Map codes;

    public FinancialType() {
        super();
//        if (codes == null) {
//            codes = new TreeMap(new CodeComparator());
//            initCodes();
//        }
    }

    public Collection getCodes() {
        Collection finTypes = new ArrayList();
//        Iterator iter = codes.values().iterator();
//        while (iter.hasNext()) {
//            Map map = (Map) iter.next();
//            if (map != null)
//                finTypes.addAll(map.keySet());
//        }
        return finTypes;
    }

//    /**
//     * SEE UtilityService
//     * 
//     * CONTAINER structure Map( objType, Map( ReferenceCode( finTypeID,
//     * finTypeDesc ), Vector( ReferenceCode( finCodeID, finCode, finCodeDesc ) ) ) )
//     */
//    private static void initCodes() {
//        try {
//            Map map = utilityService.getFinancialObjectTypes();
//            if (map == null)
//                codes.clear();
//            else
//                codes.putAll(map);
//        } catch (com.argus.financials.api.ServiceException e) {
//            e.printStackTrace(System.err);
//        }
//    }

//    /**
//     * helper methodes
//     */
//    public static Object[] getFinancialTypes(Integer objectTypeID) {
//        if (codes == null)
//            return CODES_NONE;
//
//        Map finTypes = (Map) codes.get(objectTypeID);
//        if (finTypes == null)
//            return CODES_NONE;
//
//        return finTypes.keySet().toArray();
//    }

//    public static Object[] getFinancialCodes(Integer objectTypeID, ICode financialType) {
//        if (objectTypeID == null || financialType == null)
//            return CODES_NONE;
//
//        Map finTypes = (Map) codes.get(objectTypeID);
//        if (finTypes == null)
//            return CODES_NONE;
//
//        Vector finCodes = (Vector) finTypes.get(financialType);
//        if (finCodes == null)
//            return CODES_NONE;
//
//        return finCodes.toArray();
//    }

//    public static ICode getFinancialType(Integer objectTypeID, Integer financialTypeID) {
//        if (objectTypeID == null || financialTypeID == null)
//            return null;
//        Map finTypes = (Map) codes.get(objectTypeID);
//        if (finTypes == null)
//            return null;
//        Iterator iter = finTypes.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            ICode item = (ICode) entry.getKey();
//            if (financialTypeID.equals(item.getId()))
//                return item;
//        }
//        return null;
//    }

//    public static FinancialCode getFinancialCode(Integer objectTypeId, Integer financialTypeId, Integer financialCodeId) {
//        if (financialCodeId == null) {
//            return null;
//        }
//        // 21/10/2002 - FinancialTypeID in FinancialCode table can be null
//        if (financialTypeId == null || financialTypeId <= 0
//                || FinancialTypeID.INVESTMENT_LISTED_SHARES == financialTypeId.intValue()
//                || FinancialTypeID.INVESTMENT_LISTED_UNIT_TRUST == financialTypeId.intValue()
//                || FinancialTypeID.INVESTMENT_OTHER == financialTypeId.intValue()
//                || FinancialTypeID.INVESTMENT_PROPERTY == financialTypeId.intValue()
//                || FinancialTypeID.SUPERANNUATION_ACCOUNT == financialTypeId.intValue()
//                || FinancialTypeID.INCOMESTREAM_PENSION_ACCOUNT == financialTypeId.intValue()
//                || FinancialTypeID.INCOMESTREAM_ANNUITY_POLICY == financialTypeId.intValue()) {
//            financialTypeId = 0;
//            objectTypeId = 0;
//        }
//        Map finTypes = (Map) codes.get(objectTypeId);
//        if (finTypes == null) {
//            return null;
//        }
//        Iterator iter = finTypes.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            ICode code = (ICode) entry.getKey();
//            if (financialTypeId.equals(code.getId())) {
//                Iterator iter2 = ((Vector) entry.getValue()).iterator();
//                while (iter2.hasNext()) {
//                    FinancialCode item = (FinancialCode) iter2.next();
//                    if (financialCodeId.equals(item.getId())) {
//                        return item;
//                    }
//                }
//            }
//        }
//        return null;
//    }

//    public static boolean addFinancialCode(Integer objectTypeId, Integer financialTypeId, ICode financialCode) {
//        // 21/10/2002 - FinancialTypeID in FinancialCode table can be null
//        if (financialTypeId == null || financialTypeId <= 0
//                || FinancialTypeID.INVESTMENT_LISTED_SHARES == financialTypeId.intValue()
//                || FinancialTypeID.INVESTMENT_LISTED_UNIT_TRUST == financialTypeId.intValue()
//                || FinancialTypeID.INVESTMENT_OTHER == financialTypeId.intValue()
//                || FinancialTypeID.INVESTMENT_PROPERTY == financialTypeId.intValue()
//                || FinancialTypeID.SUPERANNUATION_ACCOUNT == financialTypeId.intValue()
//                || FinancialTypeID.INCOMESTREAM_PENSION_ACCOUNT == financialTypeId.intValue()
//                || FinancialTypeID.INCOMESTREAM_ANNUITY_POLICY == financialTypeId.intValue()) {
//            financialTypeId = 0;
//            objectTypeId = 0;
//        }
//        Map finTypes = (Map) codes.get(objectTypeId);
//        if (finTypes == null) {
//            return false;
//        }
//        Iterator iter = finTypes.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            Vector financialCodes = (Vector) entry.getValue();
//            if (financialCodes != null) {
//                for (int i = 0; i < financialCodes.size(); i++) {
//                    ICode item = (ICode) financialCodes.elementAt(i);
//                    // do we have an entry for the given FinancialCode?
//                    if (item != null && item.getId() == financialCode.getId()) {
//                        // yes, break here
//                        return true;
//                    }
//                }
//                // do we need to insert a new entry?
//                financialCodes.addElement(financialCode);
//            }
//        }
//        return false;
//    }

//    public static boolean containsFinancialCode(Integer objectTypeID, Integer financialTypeID, ICode financialCode) {
//        if (objectTypeID == null || financialTypeID == null)
//            return false;
//
//        Map finTypes = (Map) codes.get(objectTypeID);
//        if (finTypes == null)
//            return false;
//
//        Iterator iter = finTypes.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            Vector financialCodes = (Vector) entry.getValue();
//            if (financialCodes != null) {
//                for (int i = 0; i < financialCodes.size(); i++) {
//                    ICode item = (ICode) financialCodes.elementAt(i);
//                    // do we have an entry for the given FinancialCode?
//                    if (item != null
//                            && item.getCode() != null
//                            && item.getDescription() != null
//                            && item.getCode().equals(financialCode.getCode())
//                            && item.getDescription().equals(financialCode.getDescription())) {
//                        // yes, break here
//                        return true;
//                    }
//                }
//            }
//            break;
//        }
//        return false;
//    }

}