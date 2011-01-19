/*
 * BusinessServiceImpl.java
 *
 * Created on 22 January 2002, 12:03
 */

package com.argus.financials.service.impl;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.service.BusinessService;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.FinderException;
import com.argus.financials.service.client.ObjectNotFoundException;
import com.argus.financials.service.client.ServiceException;

public class BusinessServiceImpl extends AbstractServiceImpl implements BusinessService {

    /**
     * to create a new instance of Person entity bean and therefore insert data
     * into database invoke create(...) method
     */
    public Integer create() throws ServiceException, CreateException {

        Connection con = null;

        try {
            con = getConnection();

            // get new ObjectID for person
            Integer businessID = create(con);

            con.commit();

            return businessID;

        } catch (SQLException e) {
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException e2) {
                throw new CreateException(e.getMessage() + '\n'
                        + e2.getMessage());
            }
            throw new CreateException(e.getMessage());
        }

    }

    public Integer create(Connection con) throws SQLException {

        PreparedStatement sql = null;

        try {

            // get new ObjectID for person
            Integer businessID = new Integer(getNewObjectID(BUSINESS, con));

            // add to Business table
            sql = con
                    .prepareStatement("INSERT INTO Business (BusinessID) VALUES (?)");
            sql.setInt(1, businessID.intValue());
            sql.executeUpdate();

            return businessID;

        } finally {
            close(null, sql);
        }

    }

    public void findByPrimaryKey(Object primaryKey) throws ServiceException,
            FinderException {

        try {
            load((Integer) primaryKey);
            return;
        } catch (ObjectNotFoundException e) {
            throw new FinderException(e.getMessage());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    // BUSINESS SPECIFIC MEMBERS
    private Integer parentBusinessID;

    private Integer industryCodeID;

    private Integer businessStructureCodeID;

    private String legalName;

    private String tradingName;

    private String businessNumber;

    private String taxFileNumber;

    private String webSiteName;

    //

    /**
     * Modified
     */
    public boolean isModified() {
        return super.isModified();
    }

    public void setModified(boolean value) {
        super.setModified(value);
    }

    /**
     * Business
     */
    public Integer getParentBusinessID() throws com.argus.financials.service.client.ServiceException {
        return parentBusinessID;
    }

    private void setParentBusinessID(Integer value)
            throws com.argus.financials.service.client.ServiceException {
        if (equals(parentBusinessID, value))
            return;

        // TODO: do UPDATE sql !!!

        parentBusinessID = value;
        super.setModified(true);
    }

    public Integer getIndustryCodeID() throws com.argus.financials.service.client.ServiceException {
        return industryCodeID;
    }

    public void setIndustryCodeID(Integer value)
            throws com.argus.financials.service.client.ServiceException, InvalidCodeException {
        if (equals(industryCodeID, value))
            return;

        // TODO
        // XXXCode.isValidThrow( industryCodeID );

        industryCodeID = value;
        super.setModified(true);
    }

    public Integer getBusinessStructureCodeID() throws com.argus.financials.service.client.ServiceException {
        return businessStructureCodeID;
    }

    public void setBusinessStructureCodeID(Integer value)
            throws com.argus.financials.service.client.ServiceException, InvalidCodeException {
        if (equals(businessStructureCodeID, value))
            return;

        // TODO
        // XXXCode.isValidThrow( businessStructureCodeID );

        businessStructureCodeID = value;
        super.setModified(true);
    }

    public String getLegalName() throws com.argus.financials.service.client.ServiceException {
        return legalName;
    }

    public void setLegalName(String value) throws com.argus.financials.service.client.ServiceException {
        if (equals(legalName, value))
            return;

        legalName = value;
        if (tradingName == null)
            tradingName = value;
        super.setModified(true);
    }

    public String getTradingName() throws com.argus.financials.service.client.ServiceException {
        return tradingName;
    }

    public void setTradingName(String value) throws com.argus.financials.service.client.ServiceException {
        if (equals(tradingName, value))
            return;

        tradingName = value;
        super.setModified(true);
    }

    public String getBusinessNumber() throws com.argus.financials.service.client.ServiceException {
        return businessNumber;
    }

    public void setBusinessNumber(String value) throws com.argus.financials.service.client.ServiceException {
        if (equals(businessNumber, value))
            return;

        businessNumber = value;
        super.setModified(true);
    }

    public String getTaxFileNumber() throws com.argus.financials.service.client.ServiceException {
        return taxFileNumber;
    }

    public void setTaxFileNumber(String value) throws com.argus.financials.service.client.ServiceException {
        if (equals(taxFileNumber, value))
            return;

        taxFileNumber = value;
        super.setModified(true);
    }

    public String getWebSiteName() throws com.argus.financials.service.client.ServiceException {
        return webSiteName;
    }

    public void setWebSiteName(String value) throws com.argus.financials.service.client.ServiceException {
        if (equals(webSiteName, value))
            return;

        webSiteName = value;
        super.setModified(true);
    }

    /**
     * 
     */
    public void load(Integer primaryKeyID) throws SQLException,
            ObjectNotFoundException {

        Connection con = getConnection();
        load(primaryKeyID, con);
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con.prepareStatement("SELECT b.*" + " FROM Business b"
                    + " WHERE (b.BusinessID=?)", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Business ID: "
                        + primaryKeyID);

            setPrimaryKeyID(primaryKeyID);

            load(rs);

        } finally {
            close(rs, sql);
        }

        setModified(false);

    }

    public void load(ResultSet rs) throws SQLException {

        super.load(rs);

        parentBusinessID = (Integer) rs.getObject("ParentBusinessID");
        industryCodeID = (Integer) rs.getObject("IndustryCodeID");
        businessStructureCodeID = (Integer) rs
                .getObject("BusinessStructureCodeID");

        legalName = rs.getString("LegalName");
        tradingName = rs.getString("TradingName");
        businessNumber = rs.getString("BusinessNumber");
        taxFileNumber = rs.getString("TaxFileNumber");
        webSiteName = rs.getString("WebSiteName");

        setDateCreated(rs.getDate("DateCreated"));
        setDateModified(rs.getDate("DateModified"));

    }

    public int store(Connection con) throws SQLException {

        Integer businessID = null;

        if (getPrimaryKeyID() == null) {
            // businessID = find();
            if (businessID == null)
                businessID = create(con);
        } else {
            businessID = getPrimaryKeyID();
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

        sql.setObject(++i, parentBusinessID, java.sql.Types.INTEGER);
        sql.setObject(++i, industryCodeID, java.sql.Types.INTEGER);
        sql.setObject(++i, businessStructureCodeID, java.sql.Types.INTEGER);

        sql.setString(++i, legalName);
        sql.setString(++i, tradingName);
        sql.setString(++i, businessNumber);
        sql.setString(++i, taxFileNumber);
        sql.setString(++i, webSiteName);

        sql.setObject(++i, businessID, java.sql.Types.INTEGER);

        sql.executeUpdate();

        setPrimaryKeyID(businessID);
        setModified(false);

        return businessID.intValue();

    }

    public Integer find() throws SQLException {
        Connection con = getConnection();
        return find(con);
    }

    private Integer find(Connection con) throws SQLException {

        PreparedStatement sql = con.prepareStatement("SELECT BusinessID"
                + " FROM Business" + " WHERE" + " ( LegalName = ? )"
                + " OR ( TradingName = ? )" + " OR ( BusinessNumber = ? )"
                + " OR ( TaxFileNumber = ? )", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

        ResultSet rs = null;

        try {
            int i = 0;
            sql.setString(++i, legalName == null ? "" : legalName);
            sql.setString(++i, tradingName == null ? "" : tradingName);
            sql.setString(++i, businessNumber == null ? "" : businessNumber);
            sql.setString(++i, taxFileNumber == null ? "" : taxFileNumber);
            rs = sql.executeQuery();

            if (rs.next())
                return (Integer) rs.getObject(1);
            return null;

        } finally {
            close(rs, sql);
        }

    }

}
