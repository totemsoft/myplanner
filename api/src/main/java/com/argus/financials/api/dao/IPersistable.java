package com.argus.financials.api.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;

public interface IPersistable extends java.io.Serializable {

    void load(Connection con) throws SQLException, ObjectNotFoundException;

    void load(Integer primaryKeyID, Connection con) throws SQLException, ObjectNotFoundException;

    int store(Connection con) throws SQLException;

    Integer find() throws SQLException;

    void remove(Connection con) throws SQLException;

}