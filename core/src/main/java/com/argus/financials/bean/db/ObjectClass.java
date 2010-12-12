/*
 * ObjectClass.java
 *
 * Created on 9 October 2001, 18:23
 */

package com.argus.financials.bean.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.HashMap;
import java.util.Map;

import com.argus.financials.bean.AssetCash;
import com.argus.financials.bean.AssetInvestment;
import com.argus.financials.bean.AssetPersonal;
import com.argus.financials.bean.AssetSuperannuation;
import com.argus.financials.bean.IncomeStream;
import com.argus.financials.bean.Liability;
import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.bean.TaxOffset;

public class ObjectClass implements ObjectTypeConstant {

    private static Map map = new HashMap();

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
        map.put(new Integer(ASSET_SUPERANNUATION),
                AssetSuperannuationBean.class);
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

}
