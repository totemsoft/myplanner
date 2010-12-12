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
public class TitleCodeDataProvider implements DataProvider {

    private final String sqlSelect = " SELECT TitleCodeID, TitleCodeDesc FROM TitleCode ";

    /** Creates new UserManagmentDataProvider */
    public TitleCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
