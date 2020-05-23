/*
 * ICashFlow.java
 *
 * Created on 17 April 2002, 14:16
 */

package au.com.totemsoft.myplanner.bean;

import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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
