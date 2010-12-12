/*
 * DBManager.java
 *
 * Created on 30 May 2002, 14:28
 */

package com.argus.financials.bean.db;

/**
 * 
 * @author shibaevv
 * @version
 */

import java.sql.Connection;

public class DBManager extends AbstractPersistable {

    public static DBManager dbManager;

    /** Creates new DBManager */
    private DBManager() {
    }

    public static DBManager getInstance() {
        if (dbManager == null)
            dbManager = new DBManager();
        return dbManager;
    }

    public Connection getConnection() {
        try {
            return getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
