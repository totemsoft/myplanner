/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.financials.ui;

import java.util.Arrays;
import java.util.List;

import com.argus.util.Pair;


/*
 * Created on 30/06/2006
 * 
 */

public final class FinancialPlannerPermission
    //extends java.security.Permission
{
    private static final String MINIMAL = "MINIMAL";
    private static final List MINIMAL_PERMISSIONS = Arrays.asList(
        new Pair [] {
            IMenuCommand.EXPORT,
            IMenuCommand.IMPORT,
            IMenuCommand.CURRENT_POSITION_CALC,
            IMenuCommand.LOAN_AND_MORTGAGE_CALC,
            //IMenuCommand.PAYG_CALC,
        }
    );
    
    private static final String MAXIMUM = "MAXIMUM";
    private static final List MAXIMUM_PERMISSIONS = Arrays.asList(
        new Pair [] {
            IMenuCommand.EXPORT,
            IMenuCommand.IMPORT,
            IMenuCommand.ADD_CLIENT,
            IMenuCommand.RISK_EVALUATION_CLIENT,
            IMenuCommand.RISK_EVALUATION_PARTNER,
            IMenuCommand.STRATEGIC_ADVICE,
            IMenuCommand.CURRENT_POSITION_CALC,
            IMenuCommand.ETP_ROLLOVER_CALC,
            IMenuCommand.ALOCATED_PENSION_CALC,
            IMenuCommand.INVESTMENT_GEARING_CALC,
            IMenuCommand.CENTRELINK_CALC,
            IMenuCommand.LOAN_AND_MORTGAGE_CALC,
            IMenuCommand.PAYG_CALC,
            IMenuCommand.PLAN_WIZARD,
            IMenuCommand.CONTENTS_INDEX,
        }
    );
    
    private final static FinancialPlannerPermission instance = 
        //new FinancialPlannerPermission(MINIMAL);
        new FinancialPlannerPermission(MAXIMUM);
    public static FinancialPlannerPermission getInstance() {
        return instance;
    }
    
    private String permissions;

    private FinancialPlannerPermission(String name) {
        permissions = name;
    }

    public final boolean checkPermissions(final Pair name) {
        if (MINIMAL.equals(permissions)) {
            return MINIMAL_PERMISSIONS.contains(name);
        }
        if (MAXIMUM.equals(permissions)) {
            return MAXIMUM_PERMISSIONS.contains(name);
        }
        return false;
    }
    
//    /*
//     * java.security.Permission
//     */
//    public boolean implies(Permission permission) {
//        return false;
//    }
//
//    public boolean equals(Object obj) {
//        return false;
//    }
//
//    public int hashCode() {
//        return 0;
//    }
//
//    public String getActions() {
//        return null;
//    }

}

