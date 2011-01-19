package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.service.client.ObjectNotFoundException;

public interface IPersistable extends java.io.Serializable {

    void load(Connection con) throws SQLException, ObjectNotFoundException;

    void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException;

    void load(ResultSet rs) throws SQLException;

    int store(Connection con) throws SQLException;

    Integer find() throws SQLException;

    void remove(Connection con) throws SQLException;

}
