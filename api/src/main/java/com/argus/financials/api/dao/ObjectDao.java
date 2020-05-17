package com.argus.financials.api.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ObjectDao {

    int getIdentityID(Connection con) throws SQLException;

    int getNewObjectID(int objectTypeID, Connection con) throws SQLException;

}