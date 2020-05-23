package au.com.totemsoft.myplanner.api.dao;

import java.sql.Connection;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;

public interface IPersistable extends java.io.Serializable {

    void load(Connection con) throws SQLException, ObjectNotFoundException;

    void load(Integer primaryKeyID, Connection con) throws SQLException, ObjectNotFoundException;

    int store(Connection con) throws SQLException;

    Integer find() throws SQLException;

    void remove(Connection con) throws SQLException;

}