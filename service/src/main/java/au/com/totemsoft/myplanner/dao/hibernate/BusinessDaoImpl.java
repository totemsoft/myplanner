/*
 * BusinessServiceImpl.java
 *
 * Created on 22 January 2002, 12:03
 */

package au.com.totemsoft.myplanner.dao.hibernate;

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

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.bean.IBusiness;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.api.dao.ObjectDao;
import au.com.totemsoft.myplanner.dao.BusinessDao;
import au.com.totemsoft.myplanner.domain.hibernate.Business;

@Repository
public class BusinessDaoImpl implements BusinessDao {

    @Autowired private SQLHelper sqlHelper;

    @Autowired private ObjectDao objectDao;

    /**
     * 
     */
    public Business load(Integer businessId) throws SQLException, ObjectNotFoundException {
        String sql = "SELECT b.*" + " FROM Business b WHERE (b.BusinessID=?)";
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);) {
            pstmt.setInt(1, businessId);
            ResultSet rs = pstmt.executeQuery();
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
            rs.close();
            //
            return result;
        }
    }

    @Override
    public int store(IBusiness business, Connection con) throws SQLException {
        Integer businessId = business.getId();
        if (businessId == null) {
            // get new ObjectID for person
            businessId = objectDao.getNewObjectID(ObjectTypeConstant.BUSINESS, con);
            // add to Business table
            String sql = "INSERT INTO Business (BusinessID) VALUES (?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql);) {
                pstmt.setInt(1, businessId);
                pstmt.executeUpdate();
            }
        }
        //
        String sql = "UPDATE Business SET"
            + " ParentBusinessID=?, IndustryCodeID=?, BusinessStructureCodeID=?"
            + ", LegalName=?, TradingName=?, BusinessNumber=?, TaxFileNumber=?, WebSiteName=?"
            + ", DateModified=getDate()"
            + " WHERE BusinessID=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);) {
            int i = 0;
            pstmt.setObject(++i, business.getParentId());
            pstmt.setObject(++i, business.getIndustryCodeID());
            pstmt.setObject(++i, business.getBusinessStructureCodeID());
            pstmt.setString(++i, business.getLegalName());
            pstmt.setString(++i, business.getTradingName());
            pstmt.setString(++i, business.getBusinessNumber());
            pstmt.setString(++i, business.getTaxFileNumber());
            pstmt.setString(++i, business.getWebSiteName());
            pstmt.setObject(++i, businessId);
            pstmt.executeUpdate();
        }
        //
        return businessId;
    }

    public Integer find(IBusiness business) throws SQLException {
        String sql = "SELECT BusinessID"
            + " FROM Business" + " WHERE" + " ( LegalName = ? )"
            + " OR ( TradingName = ? )" + " OR ( BusinessNumber = ? )"
            + " OR ( TaxFileNumber = ? )";
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);) {
            int i = 0;
            pstmt.setString(++i, business.getLegalName());
            pstmt.setString(++i, business.getTradingName());
            pstmt.setString(++i, business.getBusinessNumber());
            pstmt.setString(++i, business.getTaxFileNumber());
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    return (Integer) rs.getObject(1);
                }
                return null;
            }
        }
    }

}