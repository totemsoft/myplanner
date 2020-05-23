package au.com.totemsoft.myplanner.dao;

import java.sql.Connection;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.bean.IBusiness;
import au.com.totemsoft.myplanner.domain.hibernate.Business;

public interface BusinessDao {

    Business load(Integer businessId) throws SQLException, ObjectNotFoundException;

    int store(IBusiness business, Connection con) throws SQLException;

    Integer find(IBusiness business) throws SQLException;

}