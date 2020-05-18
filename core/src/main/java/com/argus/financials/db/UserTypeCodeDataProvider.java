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
public class UserTypeCodeDataProvider implements DataProvider {

    private final String sqlSelect = " SELECT AdviserTypeCodeId, AdviserTypeCodeDesc FROM AdviserTypeCode ORDER BY AdviserTypeCodeID ";

    /** Creates new UserManagmentDataProvider */
    public UserTypeCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
