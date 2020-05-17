package com.argus.financials.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.bean.PersonName;

public interface PersonDao {

    void load(ResultSet rs, PersonName personName) throws SQLException;

}