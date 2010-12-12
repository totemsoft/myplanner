/*
 * UserManagmentDataResolver.java
 *
 * Created on 11 March 2003, 17:51
 */

package com.argus.financials.db;

/**
 * 
 * @author shibaevv
 * @version
 */
public class StateCodeDataProvider implements DataProvider {

    private final String sqlSelect = "   SELECT  "
            + "   null as StateCodeID,   " + "   '' StateCode,    "
            + "   null StateCodeDesc   " + "   UNION ALL    "
            + "   SELECT          " + "   StateCodeID,    "
            + "   StateCode,      " + "   StateCodeDesc   "
            + "   FROM         StateCode	"

    ;

    /** Creates new UserManagmentDataProvider */
    public StateCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
