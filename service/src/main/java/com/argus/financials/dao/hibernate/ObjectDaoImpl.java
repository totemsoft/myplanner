package com.argus.financials.dao.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.argus.dao.SQLHelper;
import com.argus.financials.api.dao.ObjectDao;

public class ObjectDaoImpl implements ObjectDao {

    @Autowired private SQLHelper sqlHelper;

    public int getIdentityID(Connection con) throws SQLException {
        return sqlHelper.getIdentityID(con);
    }

    public int getNewObjectID(int objectTypeID, Connection con) throws SQLException {
        return sqlHelper.getNewObjectID(objectTypeID, con);
    }

}