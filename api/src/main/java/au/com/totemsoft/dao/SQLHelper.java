package au.com.totemsoft.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLHelper {

    Connection getConnection() throws SQLException;

    int getIdentityID(Connection con) throws SQLException;

    int getNewObjectID(int objectTypeID, Connection con) throws SQLException;

}
