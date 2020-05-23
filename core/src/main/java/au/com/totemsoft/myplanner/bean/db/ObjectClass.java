/*
 * ObjectClass.java
 *
 * Created on 9 October 2001, 18:23
 */

package au.com.totemsoft.myplanner.bean.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.HashMap;
import java.util.Map;

import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.bean.AssetCash;
import au.com.totemsoft.myplanner.bean.AssetInvestment;
import au.com.totemsoft.myplanner.bean.AssetPersonal;
import au.com.totemsoft.myplanner.bean.AssetSuperannuation;
import au.com.totemsoft.myplanner.bean.IncomeStream;
import au.com.totemsoft.myplanner.bean.Liability;
import au.com.totemsoft.myplanner.bean.RegularExpense;
import au.com.totemsoft.myplanner.bean.RegularIncome;
import au.com.totemsoft.myplanner.bean.TaxOffset;

public class ObjectClass implements ObjectTypeConstant {

    private static Map<Integer, Class> map = new HashMap<Integer, Class>();

    static {
        // map.put( new Integer( PERSON ), null );
        // map.put( new Integer( USER_PERSON ), null );
        // map.put( new Integer( CLIENT_PERSON ), null );
        // map.put( new Integer( BUSINESS ), null );
        // map.put( new Integer( ADDRESS ), null );
        // map.put( new Integer( CONTACT_MEDIA ), null );
        // map.put( new Integer( RELATIONSHIP ), null );
        // map.put( new Integer( RELATIONSHIP_FINANCE ), null );
        // map.put( new Integer( DEPENDENT_PERSON ), null );

        map.put(new Integer(ASSET_CASH), AssetCashBean.class);
        map.put(new Integer(-ASSET_CASH), AssetCash.class);
        map.put(new Integer(ASSET_INVESTMENT), AssetInvestmentBean.class);
        map.put(new Integer(-ASSET_INVESTMENT), AssetInvestment.class);
        map.put(new Integer(ASSET_PERSONAL), AssetPersonalBean.class);
        map.put(new Integer(-ASSET_PERSONAL), AssetPersonal.class);
        map.put(new Integer(ASSET_SUPERANNUATION), AssetSuperannuationBean.class);
        map.put(new Integer(-ASSET_SUPERANNUATION), AssetSuperannuation.class);
        map.put(new Integer(INCOME_STREAM), IncomeStreamBean.class);
        map.put(new Integer(-INCOME_STREAM), IncomeStream.class);
        map.put(new Integer(REGULAR_INCOME), RegularIncomeBean.class);
        map.put(new Integer(-REGULAR_INCOME), RegularIncome.class);
        map.put(new Integer(REGULAR_EXPENSE), RegularExpenseBean.class);
        map.put(new Integer(-REGULAR_EXPENSE), RegularExpense.class);
        map.put(new Integer(LIABILITY), LiabilityBean.class);
        map.put(new Integer(-LIABILITY), Liability.class);
        map.put(new Integer(TAX_OFFSET), TaxOffsetBean.class);
        map.put(new Integer(-TAX_OFFSET), TaxOffset.class);

        // abstract classes
        // map.put( new Integer( FINANCIAL ), null );
        // map.put( new Integer( ASSET ), null );
        // map.put( new Integer( REGULAR ), null );

    }

    public static Class getObjectClass(Integer objectTypeID) {
        return (Class) map.get(objectTypeID);
    }

    public static Class getObjectClass(int objectTypeID) {
        return getObjectClass(new Integer(objectTypeID));
    }

    @SuppressWarnings("unchecked")
    public static <T> T createNewInstance(Integer objectTypeID, Class[] parameterTypes, Object[] initargs) {
        // get Class types for this object id (XXXBean)
        Class objClass = getObjectClass(objectTypeID);
        try {
            return (T) objClass.getConstructor(parameterTypes).newInstance(initargs);
        } catch (Exception e) {
            System.err.println("-----> No constructor found for objectTypeID: " + objectTypeID + " <-----");
            // e.printStackTrace( System.err );
            return null;
        }
    }

    public static <T> T createNewInstance(Integer objectTypeID) {
        return createNewInstance(objectTypeID, null, null);
    }

}