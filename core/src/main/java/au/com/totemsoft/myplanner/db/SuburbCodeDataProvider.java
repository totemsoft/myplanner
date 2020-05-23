/*
 * UserManagmentDataResolver.java
 *
 * Created on 11 March 2003, 17:51
 */

package au.com.totemsoft.myplanner.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public class SuburbCodeDataProvider implements DataProvider {

    private final String sqlSelect = "   SELECT  " + "   '' as Suburb,   "
            + "   null StateCodeID,    " + "   null CountryCodeID   "
            + "   UNION ALL    " + "   SELECT  DISTINCT        "
            + "   Suburb	,       " + "   StateCodeID,    "
            + "   CountryCodeID   " + "   FROM         SuburbPostCode   "
            + "   ORDER BY Suburb";

    /** Creates new UserManagmentDataProvider */
    public SuburbCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
