package au.com.totemsoft.myplanner.bean.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.bean.PersonName;

public interface PersonBeanDao {

    void load(ResultSet rs, PersonName personName) throws SQLException;

}