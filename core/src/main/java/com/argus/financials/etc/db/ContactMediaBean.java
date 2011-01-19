/*
 * ContactMediaBean.java
 *
 * Created on November 3, 2001, 11:03 AM
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
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.service.client.ObjectNotFoundException;

public class ContactMediaBean extends AbstractPersistable {

    // has to be instanciated in top level (final) derived class
    protected ContactMedia contactMedia; // aggregation

    private int linkObjectTypeID;

    /** Creates new ContactMediaBean */
    public ContactMediaBean(ContactMedia contactMedia, int linkObjectTypeID) {
        this.contactMedia = contactMedia;
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

        boolean newConnection = (con == null);
        ResultSet rs = null;

        try {
            if (newConnection)
                con = this.getConnection();

            PreparedStatement sql = con.prepareStatement("SELECT *"
                    + " FROM ContactMedia" + " WHERE ( ContactMediaID=? )",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException(
                        "Can not find ContactMedia ID: " + primaryKeyID);

            setPrimaryKeyID(primaryKeyID);

            load(rs);

        } finally {
            rs.close();
            if (newConnection && (con != null))
                con.close();
        }

    }

    public void load(ResultSet rs) throws SQLException {

        setPrimaryKeyID((Integer) rs.getObject("ContactMediaID"));
        getContactMedia().setContactMediaCodeID(
                (Integer) rs.getObject("ContactMediaCodeID"));
        getContactMedia().setValue1(rs.getString("Value1"));
        getContactMedia().setValue2(rs.getString("Value2"));
        getContactMedia().setDesc(rs.getString("ContactMediaDesc"));
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

            setPrimaryKeyID(new Integer(getNewObjectID(
                    DbConstant.CONTACT_MEDIA, con)));

            // do insert into ADDRESS table
            sql = con
                    .prepareStatement("INSERT INTO ContactMedia"
                            + " (ContactMediaID,ContactMediaCodeID,Value1,Value2,ContactMediaDesc)"
                            + " VALUES" + " (?,?,?,?,?)");

            sql.setInt(++i, getPrimaryKeyID().intValue());
            sql.setObject(++i, getContactMedia().getContactMediaCodeID(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, getContactMedia().getValue1());
            sql.setString(++i, getContactMedia().getValue2());
            sql.setString(++i, getContactMedia().getDesc());

            sql.executeUpdate();

            // then create link
            FPSLinkObject.getInstance().link(getOwnerPrimaryKeyID(),
                    getPrimaryKeyID(), linkObjectTypeID, con);

        } else {

            // do update on ADDRESS table
            sql = con.prepareStatement("UPDATE ContactMedia SET"
                    + " Value1=?,Value2=?,ContactMediaDesc=?"
                    + " WHERE ContactMediaID=?");

            sql.setString(++i, getContactMedia().getValue1());
            sql.setString(++i, getContactMedia().getValue2());
            sql.setString(++i, getContactMedia().getDesc());

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
    public ContactMedia getContactMedia() {
        if (contactMedia == null)
            contactMedia = new ContactMedia();
        return contactMedia;
    }

    public void setContactMedia(ContactMedia value) {
        contactMedia = value;
        setModified(contactMedia != null);
    }

    public boolean isModified() {
        return getContactMedia().isModified();
    }

    public void setModified(boolean value) {
        getContactMedia().setModified(value);
    }

    public Integer getPrimaryKeyID() {
        return getContactMedia().getPrimaryKeyID();
    }

    public void setPrimaryKeyID(Integer value) {
        getContactMedia().setPrimaryKeyID(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getContactMedia().getOwnerPrimaryKeyID();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getContactMedia().setOwnerPrimaryKeyID(value);
    }

    public Integer getContactMediaCodeID() {
        return getContactMedia().getContactMediaCodeID();
    }

}
