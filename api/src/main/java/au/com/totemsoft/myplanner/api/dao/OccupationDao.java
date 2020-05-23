package au.com.totemsoft.myplanner.api.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.bean.IOccupation;

public interface OccupationDao {

    void load(IOccupation occupation, ResultSet rs) throws SQLException;

    int store(IOccupation occupation, Connection con) throws SQLException;

    Integer find(IOccupation occupation) throws SQLException;

}