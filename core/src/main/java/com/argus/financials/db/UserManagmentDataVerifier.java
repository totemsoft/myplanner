/*
 * UserManagmentDataVerifier.java
 *
 * Created on 14 April 2003, 13:08
 */

package com.argus.financials.db;

/**
 * 
 * @author shibaevv
 * @version
 */
public class UserManagmentDataVerifier implements DataVerifier {

    /** Creates new UserManagmentDataVerifier */
    public UserManagmentDataVerifier() {
    }

    /**
     * Public inteface designed to provide SQL query to test data
     * avalability/correctness
     * 
     */
    public String getVerificationRequest(int reguestID, Object[] criteria) {

        String sqlRequest = "";
        // for the request below we are expecting one argument
        if (reguestID == ADVISER_HAS_CLIENTS && criteria != null
                && criteria.length > 0) {
            sqlRequest = R_ADVISER_HAS_CLIENTS + criteria[0];
        }
        return sqlRequest;
    }

    public static int ADVISER_HAS_CLIENTS = 1;

    public static String R_ADVISER_HAS_CLIENTS = "SELECT     ObjectID2   "
            + "FROM         Link      "
            + "WHERE     (LinkObjectTypeID = 2003) AND LogicallyDeleted IS NULL "
            + "AND ObjectID1 = ";

}
