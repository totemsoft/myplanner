/*
 * FixedObjectID.java
 *
 * Created on 21 August 2001, 16:29
 */

package au.com.totemsoft.myplanner.api.bean;

public interface FixedObjectConstant {

    // SOME FIXED ObjectIDs
    // Relationship 1..6

    // RelationshipFinance 9..13
    public static final int PARTNER = 9; //RelationshipFinanceCode.rcPARTNER.getCodeID();

    public static final int DEPENDENT = 10; // 10

    public static final int CONTACT = 11; // 11

    // public static final int = 12; // 12 free
    public static final int WILL_EXECUTOR = 13; // 13

    // 9, 14..25 RelationshipFinanceCode

    public static final int GLOBAL_PLAN_TEMPLATE = 26; // 26

    public static final Integer CONNECTION_POOL_HOME_ID = Integer.MAX_VALUE - 1;
    // "au.com.totemsoft.data.ConnectionPoolHome";

}
