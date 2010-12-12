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

import com.argus.financials.bean.DbConstant;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.etc.Address;
import com.argus.financials.service.ObjectNotFoundException;

public final class AddressBean extends AbstractPersistable {

    // has to be instanciated in top level (final) derived class
    protected Address address; // aggregation

    private int linkObjectTypeID;

    public AddressBean(int linkObjectTypeID) {
        this(null, linkObjectTypeID);
    }

    public AddressBean(Address address, int linkObjectTypeID) {
        this.address = address;
        this.linkObjectTypeID = linkObjectTypeID;
    }

    /**
     * 
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getPrimaryKeyID(), con);
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

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find address ID: "
                        + primaryKeyID);

            setPrimaryKeyID(primaryKeyID);

            load(rs);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        setPrimaryKeyID((Integer) rs.getObject("AddressID"));

        getAddress().setAddressCodeID((Integer) rs.getObject("AddressCodeID"));
        getAddress().setCountryCodeID((Integer) rs.getObject("CountryCodeID"));
        getAddress().setStreetNumber(rs.getString("StreetNumber"));
        getAddress().setStreetNumber2(rs.getString("StreetNumber2"));
        getAddress().setSuburb(rs.getString("Suburb"));
        getAddress().setPostCodeID((Integer) rs.getObject("Postcode"));
        getAddress().setState(rs.getString("State"));
        getAddress().setStateCodeID((Integer) rs.getObject("StateCodeID"));
        getAddress().setParentAddressID(
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

        if (getPrimaryKeyID() == null) {

            int primaryKeyID = getNewObjectID(DbConstant.ADDRESS, con);

            // do insert into ADDRESS table
            sql = con
                    .prepareStatement("INSERT INTO Address"
                            + " (AddressID,AddressCodeID,StreetNumber,StreetNumber2,Suburb,Postcode,State,StateCodeID,CountryCodeID,ParentAddressID)"
                            + " VALUES" + " (?,?,?,?,?,?,?,?,?,?)");

            sql.setInt(++i, primaryKeyID);
            sql.setObject(++i, getAddress().getAddressCodeID(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getAddress().getStreetNumber());
            sql.setString(++i, getAddress().getStreetNumber2());
            sql.setString(++i, getAddress().getSuburb());
            sql.setObject(++i, getAddress().getPostCodeID(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getAddress().getState());
            sql.setObject(++i, getAddress().getStateCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getCountryCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getParentAddressID(),
                    java.sql.Types.INTEGER);

            sql.executeUpdate();

            // then create link
            FPSLinkObject.getInstance().link(getOwnerPrimaryKeyID().intValue(),
                    primaryKeyID, linkObjectTypeID, con);

            setPrimaryKeyID(new Integer(primaryKeyID));

        } else {

            // do update on ADDRESS table
            sql = con
                    .prepareStatement("UPDATE Address SET"
                            + " StreetNumber=?,StreetNumber2=?,Suburb=?,Postcode=?,State=?,StateCodeID=?,CountryCodeID=?,ParentAddressID=?"
                            + " WHERE AddressID=?");

            sql.setString(++i, getAddress().getStreetNumber());
            sql.setString(++i, getAddress().getStreetNumber2());
            sql.setString(++i, getAddress().getSuburb());
            sql.setObject(++i, getAddress().getPostCodeID(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getAddress().getState());
            sql.setObject(++i, getAddress().getStateCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getCountryCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getAddress().getParentAddressID(),
                    java.sql.Types.INTEGER);

            sql.setInt(++i, getPrimaryKeyID().intValue());

            sql.executeUpdate();

        }

        setModified(false);
        return getPrimaryKeyID().intValue();
    }

    public Integer find() throws SQLException {
        return null;
    }

    public void remove(Connection con) throws SQLException {
    }

    /**
     * get/set methods
     */
    public Address getAddress() {
        if (address == null)
            address = new Address();
        return address;
    }

    public void setAddress(Address value) {
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

    public Integer getPrimaryKeyID() {
        return getAddress().getPrimaryKeyID();
    }

    public void setPrimaryKeyID(Integer value) {
        getAddress().setPrimaryKeyID(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getAddress().getOwnerPrimaryKeyID();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getAddress().setOwnerPrimaryKeyID(value);
    }

    public Integer getAddressCodeID() {
        return getAddress().getAddressCodeID();
    }

}
