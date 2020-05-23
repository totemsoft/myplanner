package au.com.totemsoft.myplanner.dao.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.dao.ObjectDao;

public class ObjectDaoImpl implements ObjectDao {

    @Autowired private SQLHelper sqlHelper;

    public int getIdentityID(Connection con) throws SQLException {
        return sqlHelper.getIdentityID(con);
    }

    public int getNewObjectID(int objectTypeID, Connection con) throws SQLException {
        return sqlHelper.getNewObjectID(objectTypeID, con);
    }

}