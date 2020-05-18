/*
 * AddressBean.java
 *
 * Created on 22 August 2001, 09:53
 */

package com.argus.financials.etc.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.bean.DbConstant;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.etc.AddressDto;

public final class AddressBean extends AbstractPersistable {

    // has to be instanciated in top level (final) derived class
    protected AddressDto address; // aggregation

    private int linkObjectTypeID;

    public AddressBean(int linkObjectTypeID) {
        this(null, linkObjectTypeID);
    }

    public AddressBean(AddressDto address, int linkObjectTypeID) {
        this.address = address;
        this.linkObjectTypeID = linkObjectTypeID;
    }

    /**
     * 
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getId(), con);
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con
                    .prepareStatement(
                            "SELECT "
                                    + " AddressID,AddressCodeID,CountryCodeID,StreetNumber,StreetNumber2,Suburb,Postcode,State,StateCodeID,ParentAddressID"
                                    + " FROM Address"
                                    + " WHERE ( AddressID = ? )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID);
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find address ID: "
                        + primaryKeyID);

            setId(primaryKeyID);

            load(rs);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        setId((Integer) rs.getObject("AddressID"));

        getAddress().setAddressCodeId((Integer) rs.getObject("AddressCodeID"));
        getAddress().setCountryCodeId((Integer) rs.getObject("CountryCodeID"));
        getAddress().setStreetNumber(rs.getString("StreetNumber"));
        getAddress().setStreetNumber2(rs.getString("StreetNumber2"));
        getAddress().setSuburb(rs.getString("Suburb"));
        getAddress().setPostcode((Integer) rs.getObject("Postcode"));
        getAddress().setStateCode(rs.getString("State"));
        getAddress().setStateCodeId((Integer) rs.getObject("StateCodeID"));
        getAddress().setParentAddressId(
                (Integer) rs.getObject("ParentAddressID"));
        // DateCreated datetime NOT NULL DEFAULT getDate(),
        // DateModified datetime NULL

        setModified(false);
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int i = 0;
        PreparedStatement sql;

        if (getId() == null) {

            int primaryKeyID = getNewObjectID(DbConstant.ADDRESS, con);

            // do insert into ADDRESS table
            sql = con
                    .prepareStatement("INSERT INTO Address"
                            + " (AddressID,AddressCodeID,StreetNumber,StreetNumber2,Suburb,Postcode,State,StateCodeID,CountryCodeID,ParentAddressID)"
                            + " VALUES" + " (?,?,?,?,?,?,?,?,?,?)");

            sql.setInt(++i, primaryKeyID);
            sql.setObject(++i, getAddress().getAddressCodeId(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getAddress().getStreetNumber());
            sql.setString(++i, getAddress().getStreetNumber2());
            sql.setString(++i, getAddress().getSuburb());
            sql.setObject(++i, getAddress().getPostcode(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getAddress().getStateCode());
            sql.setObject(++i, getAddress().getStateCodeId(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getCountryCodeId(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getParentAddressId(),
                    java.sql.Types.INTEGER);

            sql.executeUpdate();

            // then create link
            linkObjectDao.link(getOwnerId().intValue(),
                    primaryKeyID, linkObjectTypeID, con);

            setId(primaryKeyID);

        } else {

            // do update on ADDRESS table
            sql = con
                    .prepareStatement("UPDATE Address SET"
                            + " StreetNumber=?,StreetNumber2=?,Suburb=?,Postcode=?,State=?,StateCodeID=?,CountryCodeID=?,ParentAddressID=?"
                            + " WHERE AddressID=?");

            sql.setString(++i, getAddress().getStreetNumber());
            sql.setString(++i, getAddress().getStreetNumber2());
            sql.setString(++i, getAddress().getSuburb());
            sql.setObject(++i, getAddress().getPostcode(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getAddress().getStateCode());
            sql.setObject(++i, getAddress().getStateCodeId(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getCountryCodeId(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getParentAddressId(),
                    java.sql.Types.INTEGER);

            sql.setInt(++i, getId());

            sql.executeUpdate();

        }

        setModified(false);
        return getId();
    }

    public Integer find() throws SQLException {
        return null;
    }

    public void remove(Connection con) throws SQLException {
    }

    /**
     * get/set methods
     */
    public AddressDto getAddress() {
        if (address == null)
            address = new AddressDto();
        return address;
    }

    public void setAddress(AddressDto value) {
        if (equals(address, value))
            return;

        address = value;
        setModified(address != null);
    }

    public boolean isModified() {
        return getAddress().isModified();
    }

    public void setModified(boolean value) {
        getAddress().setModified(value);
    }

    public Integer getId() {
        return getAddress().getId();
    }

    public void setId(Integer value) {
        getAddress().setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getAddress().getOwnerId();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getAddress().setOwnerId(value);
    }

    public Integer getAddressCodeID() {
        return getAddress().getAddressCodeId();
    }

}
