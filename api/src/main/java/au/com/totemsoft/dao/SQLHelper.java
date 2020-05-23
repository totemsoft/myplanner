package au.com.totemsoft.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface SQLHelper {

    Connection getConnection() throws SQLException;

    void close(Connection con) throws SQLException;
    void close(ResultSet rs, Statement sql) throws SQLException;
    void close(ResultSet rs, Statement sql, Connection con) throws SQLException;

    /**
     * Prints the SQLException's messages, SQLStates and ErrorCode to System.err
     * @param e
     */
    void printSQLException(SQLException e);

    int getIdentityID(Connection con) throws SQLException;

    int getNewObjectID(int objectTypeID, Connection con) throws SQLException;

}