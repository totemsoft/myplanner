package com.argus.financials.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.domain.hibernate.Business;

public interface BusinessDao {

    Business load(Integer businessId) throws SQLException, ObjectNotFoundException;

    int store(IBusiness business, Connection con) throws SQLException;

    Integer find(IBusiness business) throws SQLException;

}