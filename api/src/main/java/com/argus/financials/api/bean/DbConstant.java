/*
 * DbID.java Created on 22 August 2001, 11:13
 */

package com.argus.financials.api.bean;

import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public interface DbConstant extends FixedObjectConstant, ObjectTypeConstant, LinkObjectTypeConstant
{

    String ALL_USERS_CLIENTS = "ALL_USERS_CLIENTS";

    String ADVISORID = "ADVISORID";

    // GLOBAL PLAN TEMPLATE
    Integer TEMPLATE_PLAN = 1;

    // USER PLAN TEMPLATE
    Integer TEMPLATE_PLAN_USER = 2;

    // CLIENT PLAN TEMPLATE
    Integer TEMPLATE_PLAN_CLIENT = 3;

}