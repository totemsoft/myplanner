/*
 * BusinessServiceImpl.java
 *
 * Created on 22 January 2002, 12:03
 */

package com.argus.financials.dao.hibernate;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.argus.dao.SQLHelper;
import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.api.dao.ObjectDao;
import com.argus.financials.dao.BusinessDao;
import com.argus.financials.domain.hibernate.Business;

@Repository
public class BusinessDaoImpl implements BusinessDao {

    @Autowired private SQLHelper sqlHelper;

    @Autowired private ObjectDao objectDao;

    /**
     * 
     */
    public Business load(Integer businessId) throws SQLException, ObjectNotFoundException {
        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;
        try {
            con = sqlHelper.getConnection();
            sql = con.prepareStatement("SELECT b.*" + " FROM Business b"
                    + " WHERE (b.BusinessID=?)", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            sql.setInt(1, businessId);
            rs = sql.executeQuery();
            if (!rs.next()) {
                throw new ObjectNotFoundException("Can not find Business ID: " + businessId);
            }
            //
            Business result = new Business();
            result.setId(businessId);
            result.setParentId((Integer) rs.getObject("ParentBusinessID"));
            result.setIndustryCodeID((Integer) rs.getObject("IndustryCodeID"));
            result.setBusinessStructureCodeID((Integer) rs.getObject("BusinessStructureCodeID"));
            result.setLegalName(rs.getString("LegalName"));
            result.setTradingName(rs.getString("TradingName"));
            result.setBusinessNumber(rs.getString("BusinessNumber"));
            result.setTaxFileNumber(rs.getString("TaxFileNumber"));
            result.setWebSiteName(rs.getString("WebSiteName"));
            result.setDateCreated(rs.getDate("DateCreated"));
            result.setDateModified(rs.getDate("DateModified"));
            //
            return result;
        } finally {
            sqlHelper.close(rs, sql);
            con.close();
        }
    }

    /* (non-Javadoc)
     * @see com.argus.financials.dao.BusinessDao#store(com.argus.financials.api.bean.IBusiness, java.sql.Connection)
     */
    @Override
    public int store(IBusiness business, Connection con) throws SQLException {
        Integer businessId = business.getId();
        if (businessId == null) {
            PreparedStatement sql = null;
            try {
                // get new ObjectID for person
                businessId = objectDao.getNewObjectID(ObjectTypeConstant.BUSINESS, con);
                // add to Business table
                sql = con.prepareStatement("INSERT INTO Business (BusinessID) VALUES (?)");
                sql.setInt(1, businessId);
                sql.executeUpdate();
            } finally {
                sqlHelper.close(null, sql);
            }
        }

        PreparedStatement sql = con
                .prepareStatement(
                        "UPDATE Business SET"
                                + " ParentBusinessID=?, IndustryCodeID=?, BusinessStructureCodeID=?"
                                + ", LegalName=?, TradingName=?, BusinessNumber=?, TaxFileNumber=?, WebSiteName=?"
                                + ", DateModified=getDate()"
                                + " WHERE BusinessID=?",
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        int i = 0;
        sql.setObject(++i, business.getParentId());
        sql.setObject(++i, business.getIndustryCodeID());
        sql.setObject(++i, business.getBusinessStructureCodeID());
        sql.setString(++i, business.getLegalName());
        sql.setString(++i, business.getTradingName());
        sql.setString(++i, business.getBusinessNumber());
        sql.setString(++i, business.getTaxFileNumber());
        sql.setString(++i, business.getWebSiteName());
        sql.setObject(++i, businessId);
        sql.executeUpdate();
        return businessId;
    }

    public Integer find(IBusiness business) throws SQLException {
        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;
        try {
            con = sqlHelper.getConnection();
            sql = con.prepareStatement("SELECT BusinessID"
                + " FROM Business" + " WHERE" + " ( LegalName = ? )"
                + " OR ( TradingName = ? )" + " OR ( BusinessNumber = ? )"
                + " OR ( TaxFileNumber = ? )", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            int i = 0;
            sql.setString(++i, business.getLegalName());
            sql.setString(++i, business.getTradingName());
            sql.setString(++i, business.getBusinessNumber());
            sql.setString(++i, business.getTaxFileNumber());
            rs = sql.executeQuery();
            if (rs.next()) {
                return (Integer) rs.getObject(1);
            }
            return null;
        } finally {
            sqlHelper.close(rs, sql);
            con.close();
        }
    }

}