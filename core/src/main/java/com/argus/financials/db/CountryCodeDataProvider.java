/*
 * UserManagmentDataResolver.java
 *
 * Created on 11 March 2003, 17:51
 */

package com.argus.financials.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public class CountryCodeDataProvider implements DataProvider {

    private final String sqlSelect = " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc = 'Australia'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc ='New Zealand'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc ='United Kingdom'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc = 'United States'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc = 'Singapore'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =  'Japan'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =  'Hong Kong'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =  'China'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =  'Canada'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =  'Italy'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =  'Greece'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode WHERE CountryCodeDesc =   'France'"
            + " UNION ALL"
            + " SELECT 	CountryCodeID,	CountryCode,	CountryCodeDesc FROM CountryCode "
            + " WHERE CountryCodeDesc NOT IN ( "
            + " 'Australia',"
            + " 'New Zealand',"
            + " 'United Kingdom',"
            + " 'United States',"
            + " 'Singapore',"
            + " 'Japan',"
            + " 'Hong Kong',"
            + " 'China',"
            + " 'Canada'," + " 'Italy'," + " 'Greece'," + " 'France')";

    /** Creates new UserManagmentDataProvider */
    public CountryCodeDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
