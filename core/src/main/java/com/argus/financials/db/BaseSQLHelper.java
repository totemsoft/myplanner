/*
 * BaseSQLHelper.java
 *
 * Created on January 6, 2002, 4:31 PM
 */

package com.argus.financials.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.argus.config.ResourceLoader;

public abstract class BaseSQLHelper implements java.io.Serializable {

    public static final String TABLE_OBJECT = "Object";

    /** Creates new BaseSQLHelper */
    protected BaseSQLHelper() {
    }

    /**
     * extra helper method to return connection to the pool/close all resources
     */
    public static synchronized void close(ResultSet rs, Statement sql)
    // Connection con, ConnectionPool connectionPool )
            throws SQLException {

        String msg = "";

        try {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    msg = e.getMessage() + '\n';
                }
            }
            if (sql != null) {
                try {
                    sql.close();
                } catch (SQLException e) {
                    msg += e.getMessage() + '\n';
                }
            }
        } catch (Exception e) {
            msg += e.getMessage();
        }

        if (msg.length() > 0)
            throw new SQLException(msg);
    }

    protected String getFullPath(String file) {
        if (!new java.io.File(file).exists())
            file = com.argus.financials.config.FPSLocale.getUserDirectory()
                    + file;

        if (new java.io.File(file).exists())
            return file;

        return null; // give up
    }

    /***************************************************************************
     * Sequence generator methods
     */
    public abstract int getIdentityID(Connection con, String tableName)
            throws SQLException;

    public synchronized int getIdentityID(Connection con) throws SQLException {
        return getIdentityID(con, null); // default Object table
    }

    public synchronized int getNewObjectID(int objectTypeID, Connection con)
            throws SQLException {
        /*
         * PreparedStatement sql = con.prepareStatement( "INSERT INTO Object
         * (ObjectTypeID) VALUES (?)" ); sql.setInt( 1, objectTypeID );
         * sql.executeUpdate();
         */
        Statement sql = con.createStatement();
        sql.executeUpdate("INSERT INTO Object (ObjectTypeID) VALUES ("
                + objectTypeID + ")");
        close(null, sql); // new

        return getIdentityID(con);

    }

    public String getMasterDBName() {
        return null;
    }

    public boolean checkDBAttached(Connection con, String dbName)
            throws SQLException {
        return true;
    }

    protected File[] getDatabaseFiles(String path, final String dbName) 
        throws SQLException 
    {
        File dir = new File(path);
        if (!dir.exists())
            throw new SQLException("Path to database does not exists: " + path);

        if (!dir.isDirectory())
            throw new SQLException("Path to database is not a directory: "
                    + path);

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.indexOf(dbName) >= 0;
            }
        });

        if (files == null || files.length == 0)
            throw new SQLException(
                    "No database files found in this directory: " + path);

        return files;

    }

    public abstract java.io.File[] attachDatabase(Connection con, String dbLocation, final String dbName) 
        throws SQLException;
    
    public abstract File[] detachDatabase(Connection con, String dbLocation, final String dbName) 
        throws SQLException;

    public boolean changePassword(Connection con, String oldPassword, String newPassword, String login) 
        throws SQLException 
    {
        return false;
    }

    protected void createDBSchema(Connection con, String dbLocation, final String dbName) 
        throws IOException, SQLException
    {
        String createScript = "data/updates/core/0.sql";
    
        List list = parse(createScript);
    
        // add to Map, send to execute
        Statement stmt = null;
        String sql = null;
        try {
            stmt = con.createStatement();
            for (int i = 0; i < list.size(); i++) {
                sql = (String) list.get(i);
                //System.out.println(sql);
                sql = sql.replaceAll("\\$\\{DB_NAME\\}", dbName);
                //sql = sql.replaceAll("(\\$\\{DB_LOCATION\\})", dbLocation);
                int idx;
                while ((idx = sql.indexOf("${DB_LOCATION}")) >= 0)
                    sql = sql.substring(0, idx) + dbLocation + sql.substring(idx + "${DB_LOCATION}".length());
                //System.out.println('\t' + sql);
                int count = stmt.executeUpdate(sql);
            }
            stmt.close();
    
            con.commit();
    
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            con.rollback();
            throw e;
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            con.rollback();
            throw e;
        } finally {
            close(null, stmt);
        }
    
        System.out.println("\tDB create successfully completed: " + createScript);    
    }

    /**
     * 
     * @param source
     * @return
     * @throws IOException
     */
    public static List parse(String source) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                ResourceLoader.loadInputStream(source)));
    
        List list = new ArrayList();
        String sql = "";
        String line;
        while ((line = reader.readLine()) != null) {
            // check for comments
            int index = line.indexOf("--");
            if (index == 0)
                continue;
            if (index > 0)
                line = line.substring(0, index);
    
            sql += " " + line + " ";
    
            // check for end of statement
            index = sql.indexOf(";");
            if (index < 0)
                index = sql.indexOf(" GO ");
            if (index < 0)
                continue;
    
            sql = sql.substring(0, index);
            
            //System.out.println(sql);
            if (sql.indexOf("#ifdef") >= 0) {
                // strip MSSQL
                String s = "MSSQL";
                sql = sql.replaceAll("#ifdef\\s+" + s, "");
                sql = sql.replaceAll("#endif\\s+" + s, "");
                // remove ALL other(s): MSSQL, HSQLDB, etc
                sql = sql.replaceAll("#ifdef\\s+MSSQL.+#endif\\s+MSSQL", "");
                sql = sql.replaceAll("#ifdef\\s+HSQLDB.+#endif\\s+HSQLDB", "");
                sql = sql.replaceAll("#ifdef\\s+MYSQL.+#endif\\s+MYSQL", "");
                //System.out.println(sql);                   
            }
            sql = sql.trim();
            
            if (sql.length() > 0)
                list.add(sql);
    
            sql = "";
        }
        return list;
    }

}
