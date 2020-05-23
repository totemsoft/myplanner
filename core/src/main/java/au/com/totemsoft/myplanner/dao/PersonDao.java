package au.com.totemsoft.myplanner.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.bean.PersonName;

public interface PersonDao {

    void load(ResultSet rs, PersonName personName) throws SQLException;

}