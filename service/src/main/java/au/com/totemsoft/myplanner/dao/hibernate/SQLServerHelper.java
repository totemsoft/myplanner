/*
 * SQLServerHelper.java
 *
 * Created on January 6, 2002, 5:00 PM
 */

package au.com.totemsoft.myplanner.dao.hibernate;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

@Component
public class SQLServerHelper extends AbstractSQLHelper {

    /***************************************************************************
     * Sequence generator methods
     */
    // will close Statement(sql) and ResultSet(rs)
    public synchronized int getIdentityID(Connection con, String tableName)
            throws SQLException {

        // tableName is not important for SQL Server (as we are using
        // @@IDENTITY)

        Statement sql = con.createStatement();

        // MS-SQL Server specific !!!
        ResultSet rs = sql.executeQuery("SELECT @@IDENTITY AS NEW_IDENTITY");

        int id = -1;
        try {
            if (!rs.next())
                throw new SQLException("FAILED to get IDENTITY");

            id = rs.getInt("NEW_IDENTITY");
        } finally {
            close(rs, sql); // can throws SQLException !!!
        }
        return id;

    }

    /**
     * check that "FPSsql" attached
     */
    public String getMasterDBName() {
        return "master";
    }

    public void createDBSchema(Connection con, String dbLocation, final String dbName) 
        throws IOException, SQLException
    {
        createDBSchema(con, dbLocation, dbName);
    }

    public boolean checkDBAttached(Connection con, String dbName)
        throws SQLException 
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(
                "SELECT name FROM master.dbo.sysdatabases WHERE name = N'" + dbName + "'");
        
            rs = st.executeQuery();
        
            if (!rs.next()) {
                return false;
            }
        
        } finally {
            close(rs, st);
        }
        
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("USE " + dbName);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            stmt.close();
        }
        detachDatabase(con, null, dbName);
        return false;
    }

    /**
     * Permissions Only members of the sysadmin and dbcreator fixed server roles
     * can execute this procedure.
     * 
     * EXEC sp_attach_db
     * 
     * @dbname = N'FPSSQL',
     * @filename1 = N'c:\dev\database\mssql\MyPlanner_Data.mdf',
     * @filename2 = N'c:\dev\database\mssql\MyPlanner_Log.ldf'
     */
    public java.io.File[] attachDatabase(Connection con, String path, String dbName) 
        throws SQLException 
    {
        path = getFullPath(path);
        if (path == null)
            throw new SQLException("Path to database does not exists: " + path);

        System.out.println("Attaching database " + dbName + " in directory "
                + path + " .......");

        // can throw Exception
        File[] files = getDatabaseFiles(path, dbName); 

        String sp_call = "{call sp_attach_db(?";
        for (int p = 0; p < files.length; p++)
            sp_call += ",?";
        sp_call += ")}";

        CallableStatement cs = con.prepareCall(sp_call);
        try {
            int i = 0;
            cs.setString(++i, dbName);
            for (int p = 0; p < files.length; p++)
                try {
                    cs.setString(++i, files[p].getCanonicalPath());
                } catch (IOException e) {
                    throw new SQLException(e.getMessage());
                }
            cs.execute();
        } finally {
            cs.close();
        }

        System.out.println("....... successfully completed database attachment!");

        return files;

    }


    /**
     * Permissions Only members of the sysadmin and dbcreator fixed server roles
     * can execute this procedure.
     * EXEC sp_detach_db N'MyPlanner';
     * EXEC sp_detach_db @dbname = N'MyPlanner'
     */
    public File[] detachDatabase(Connection con, String path, String dbName) throws SQLException {
        System.out.println("Dettaching database " + dbName + " .......");

//        path = getFullPath(path);
//        if (path == null)
//            throw new SQLException("Path to database does not exists: " + path);
//        // can throw Exception
//        File[] files = getDatabaseFiles(path, dbName); 

        String sp_call = "{call sp_detach_db(?)}";

        CallableStatement cs = con.prepareCall(sp_call);
        try {
            int i = 0;
            cs.setString(++i, dbName);
            cs.execute();
            System.out.println("....... successfully completed database detachment!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            cs.close();
        }

        return null;//files;

    }

    /**
     * A. Change the password of a login without the former password This
     * example changes the password for the login Victoria to ok. EXEC
     * sp_password NULL, 'ok', 'Victoria'
     */
    public boolean changePassword(Connection con, String oldPassword,
            String newPassword, String login) throws SQLException {
        System.out.println("Changing password for " + login + " .......");

        CallableStatement cs = con.prepareCall("{?=call sp_password(?,?,?)}");
        try {
            int i = 0;
            cs.registerOutParameter(++i, java.sql.Types.INTEGER);
            cs.setString(++i, oldPassword);
            cs.setString(++i, newPassword);
            cs.setString(++i, login);
            cs.execute();

            System.out.println("....... password changed!");

            return cs.getInt(1) == 0; // 0 (success) or 1 (failure)

        } finally {
            cs.close();
        }

    }

}
