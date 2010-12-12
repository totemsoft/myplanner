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
public class PostCodeDataProvider implements DataProvider {

    private final String sqlSelect = "   SELECT  " + "   '' as PostCode,   "
            + "   null StateCodeID,    " + "   null CountryCodeID,   "
            + "   null Suburb  " + "   UNION ALL    "
            + "   SELECT  DISTINCT        " + "   PostCode,	"
            + "   StateCodeID,    " + "   CountryCodeID,   " + "   Suburb  "
            + "   FROM         SuburbPostCode   ";

    /** Creates new UserManagmentDataProvider */
    public PostCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
