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
public class TitleCodeDataProvider implements DataProvider {

    private final String sqlSelect = " SELECT TitleCodeID, TitleCodeDesc FROM TitleCode ";

    /** Creates new UserManagmentDataProvider */
    public TitleCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
