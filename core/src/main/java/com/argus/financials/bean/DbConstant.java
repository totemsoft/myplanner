/*
 * DbID.java Created on 22 August 2001, 11:13
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */
public interface DbConstant extends FixedObjectConstant, ObjectTypeConstant, LinkObjectTypeConstant
{

    String ALL_USERS_CLIENTS = "ALL_USERS_CLIENTS";

    String ADVISORID = "ADVISORID";

    // GLOBAL PLAN TEMPLATE
    Integer TEMPLATE_PLAN = new Integer(1);

    // USER PLAN TEMPLATE
    Integer TEMPLATE_PLAN_USER = new Integer(2);

    // CLIENT PLAN TEMPLATE
    Integer TEMPLATE_PLAN_CLIENT = new Integer(3);

}