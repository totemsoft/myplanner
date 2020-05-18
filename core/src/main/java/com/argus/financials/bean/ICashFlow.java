/*
 * ICashFlow.java
 *
 * Created on 17 April 2002, 14:16
 */

package com.argus.financials.bean;

import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public interface ICashFlow {

    public static final Integer OBJECT_TYPE_INCOME = new Integer(
            ObjectTypeConstant.REGULAR_INCOME);

    public static final Integer OBJECT_TYPE_EXPENSE = new Integer(
            ObjectTypeConstant.REGULAR_EXPENSE);

    public static final Integer OBJECT_TAX_OFFSET = new Integer(
            ObjectTypeConstant.TAX_OFFSET);

    public void load(java.util.Map financialMap);

}
