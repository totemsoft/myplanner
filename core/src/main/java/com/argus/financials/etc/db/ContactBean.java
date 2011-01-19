/*
 * ContactBean.java
 *
 * Created on 7 September 2001, 14:20
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
import com.argus.financials.code.AddressCode;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Occupation;
import com.argus.financials.service.client.ObjectNotFoundException;

public class ContactBean extends AbstractPersistable {

    private int contactLinkID = FPSLinkObject.LINK_NOT_FOUND;

    // has to be instanciated in top level (final) derived class
    protected Contact contact; // aggregation

    //
    protected int objectTypeID = -1; // CONTACT or DEPENDENT or WILL_EXECUTOR

    // First level of linkage (e.g. objectTypeID1 = PERSON or CLIENT or
    // BUSINESS), objectTypeID2 = PERSON
    // Second level of linkage will be
    // objectTypeID1 = CONTACT or WILL_EXECUTOR, objectTypeID2 (e.g.
    // RELATIONSHIP_FINANCE)
    // ( but we will supply linkObjectTypeID (e.g.
    // DbID.PERSON_2_RELATIONSHIP_FINANCE) )
    private int[] linkObjectTypes = new int[] { DbConstant.PERSON_2_PERSON,
            DbConstant.PERSON_2_RELATIONSHIP_FINANCE }; // defaults

    /** Creates new ContactBean */
    protected ContactBean() {
        setObjectTypeID(DbConstant.CONTACT);
    }

    public ContactBean(Contact contact) {
        this();
        setContact(contact);
    }

    /**
     * helper methods
     */
    public Class getContactClass() {
        return Contact.class;
    }

    public int getObjectTypeID() {
        return objectTypeID;
    }

    public void setObjectTypeID(int value) {
        objectTypeID = value;
    }

    protected int getLinkObjectTypeID(int level) {
        switch (level) {
        case 1:
            return linkObjectTypes[0];
        case 2:
            return linkObjectTypes[1];
        default:
            throw new RuntimeException(
                    "ContactBean.getLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    public void setLinkObjectTypeID(int level, int value) {
        switch (level) {
        case 1: {
            setModified(linkObjectTypes[0] != value);
            linkObjectTypes[0] = value;
            break;
        }
        case 2: {
            setModified(linkObjectTypes[1] != value);
            linkObjectTypes[1] = value;
            break;
        }
        default:
            throw new RuntimeException(
                    "ContactBean.setLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    /**
     * IPersistable methods
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
            setPrimaryKeyID(primaryKeyID);

            // has to be first, or we have to close rs/sql before
            loadAddress(con);
            loadContactMedia(con);

            // RelationshipFinancial table (via Link table)
            getContact().setContactCodeID(getContactCodeID(con));

            String s = "SELECT p.*"
                + ", po.PersonID AS po_PersonID, po.PersonOccupationID, po.JobDescription, po.EmploymentStatusCodeID, po.IndustryCodeID, po.OccupationCodeID"
                + " FROM Person p LEFT OUTER JOIN PersonOccupation po ON p.PersonID = po.PersonID"
                + " WHERE (p.PersonID = ?)"
                + " AND (po.NextID IS NULL)";
            
            sql = con.prepareStatement(
                s,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find contact ID: "
                        + primaryKeyID);

            load(rs);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        PersonNameAddressBean pnab = new PersonNameAddressBean();
        pnab.setPersonName(getContact().getName());
        pnab.load(rs);

        // load occupation
        Integer id = (Integer) rs.getObject("po_PersonID");
        if (id != null) {
            if (getContact().getOccupation() == null)
                getContact().setOccupation(new Occupation(id.intValue()));
            new OccupationBean(getContact().getOccupation()).load(rs);
        }

        // RelationshipFinancial table (via Link table)
        // getContact().setContactCodeID( getContactCodeID() );

    }

    // check if this contact person already exists in Person table (e.g.
    // Partner)
    // in CLIENT !!! ( use find() return PersonID(s) to setPrimaryKeyID() )
    public int store(Connection con) throws SQLException {

        if (!isModified())
            return getPrimaryKeyID().intValue();

        int i = 0;
        PreparedStatement sql = null;
        Integer contactCodeID = getContact().getContactCodeID();

        try {
            if (getPrimaryKeyID() == null) {

                // insert into Object table new PersonID
                int contactID = getNewObjectID(DbConstant.PERSON, con);

                // do insert into Person table
                sql = con
                        .prepareStatement("INSERT INTO Person"
                                + " (PersonID,SexCodeID,TitleCodeID,FamilyName,FirstName,OtherGivenNames)"
                                + " VALUES" + " (?,?,?,?,?,?)");

                i = 0;
                sql.setInt(++i, contactID);
                sql.setObject(++i, getContact().getName().getSexCodeID(),
                        java.sql.Types.INTEGER);
                sql.setObject(++i, getContact().getName().getTitleCodeID(),
                        java.sql.Types.INTEGER);
                sql.setString(++i, getContact().getName().getSurname());
                sql.setString(++i, getContact().getName().getFirstName());
                sql.setString(++i, getContact().getName().getOtherGivenNames());

                sql.executeUpdate();

                setPrimaryKeyID(new Integer(contactID));

                // person.setLinkedPerson( PERSON_2_PERSON, CONTACT,
                // PERSON_2_RELATIONSHIP_FINANCE, con );
                // link this contact to client/relationship-finance
                int linkID = setLink(getLinkObjectTypeID(1), // DbID.PERSON_2_PERSON,
                        getObjectTypeID(), getLinkObjectTypeID(2), // DbID.PERSON_2_RELATIONSHIP_FINANCE,
                        con);
                contactLinkID = FPSLinkObject.getInstance().link(
                        new Integer(linkID), contactCodeID, // can be null !!!
                        DbConstant.PERSON_2_RELATIONSHIP, con);

            } else { // if ( getContact().getName().isModified() ) {

                // do update on PERSON table
                sql = con.prepareStatement("UPDATE Person SET"
                        + " SexCodeID=?,TitleCodeID=?,FamilyName=?,FirstName=?"
                        + ",OtherGivenNames=?" + " WHERE PersonID=?");

                i = 0;
                sql.setObject(++i, getContact().getName().getSexCodeID(),
                        java.sql.Types.INTEGER);
                sql.setObject(++i, getContact().getName().getTitleCodeID(),
                        java.sql.Types.INTEGER);
                sql.setString(++i, getContact().getName().getSurname());
                sql.setString(++i, getContact().getName().getFirstName());
                sql.setString(++i, getContact().getName().getOtherGivenNames());

                sql.setInt(++i, getPrimaryKeyID().intValue());

                sql.executeUpdate();
                sql.close();

                // update ContactCode link
                if (getContactLinkID(con) != FPSLinkObject.LINK_NOT_FOUND) {
                    FPSLinkObject.getInstance().updateLinkForObject2(
                            new Integer(contactLinkID), contactCodeID, con);
                }

            }

        } finally {
            close(null, sql);
        }

        // store occupation
        Occupation o = getContact().getOccupation();
        if (o != null && o.isModified()) {
            o.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new OccupationBean(o).store(con);
        }

        // store address
        Address a = getContact().getAddress();
        if (a != null && a.isModified()) {
            a.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new AddressBean(a, DbConstant.PERSON_2_ADDRESS).store(con);
        }

        // store phone/fax
        ContactMedia cm = getContact().getPhone();
        if (cm != null && cm.isModified()) {
            cm.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new ContactMediaBean(cm, DbConstant.PERSON_2_CONTACT_MEDIA)
                    .store(con);
        }

        cm = getContact().getMobile();
        if (cm != null && cm.isModified()) {
            cm.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new ContactMediaBean(cm, DbConstant.PERSON_2_CONTACT_MEDIA)
                    .store(con);
        }

        cm = getContact().getFax();
        if (cm != null && cm.isModified()) {
            cm.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new ContactMediaBean(cm, DbConstant.PERSON_2_CONTACT_MEDIA)
                    .store(con);
        }

        cm = getContact().getEMail();
        if (cm != null && cm.isModified()) {
            cm.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new ContactMediaBean(cm, DbConstant.PERSON_2_CONTACT_MEDIA)
                    .store(con);
        }

        setModified(false);
        return getPrimaryKeyID().intValue();

    }

    // remove link only: DbID.PERSON_2_PERSON
    public void remove(Connection con) throws SQLException {

        // remove link only: DbID.PERSON_2_PERSON
        // remove link this contact to person
        int linkID = FPSLinkObject.getInstance().unlink(
                getOwnerPrimaryKeyID().intValue(),
                getPrimaryKeyID().intValue(), getLinkObjectTypeID(1), con);

    }

    /**
     * helper methods
     */
    protected void loadAddress(Connection con) throws SQLException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con.prepareStatement("SELECT a.*" + " FROM Link l, Address a"
                    + " WHERE ( a.AddressCodeID = ? )"
                    + " AND ( l.ObjectID1 = ? ) AND ( l.LinkObjectTypeID = ? )"
                    + " AND ( l.ObjectID2 = a.AddressID )",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            int i = 0;
            sql.setInt(++i, AddressCode.RESIDENTIAL.intValue());
            sql.setInt(++i, getPrimaryKeyID().intValue());
            sql.setInt(++i, DbConstant.PERSON_2_ADDRESS);

            rs = sql.executeQuery();

            if (!rs.next())
                return;

            if (getContact().getAddress() == null)
                getContact().setAddress(
                        new Address(getPrimaryKeyID().intValue()));
            new AddressBean(getContact().getAddress(),
                    DbConstant.PERSON_2_ADDRESS).load(rs);

        } finally {
            close(rs, sql);
        }

    }

    private void loadContactMedia(Connection con) throws SQLException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        int personID = getPrimaryKeyID().intValue();

        try {
            sql = con
                    .prepareStatement(
                            "SELECT cm.*"
                                    + " FROM Link l, ContactMedia cm"
                                    + " WHERE ( l.ObjectID1 = ? ) AND ( l.LinkObjectTypeID = ? )"
                                    + " AND ( l.ObjectID2 = cm.ContactMediaID )"
                                    + " AND ( LogicallyDeleted IS NULL )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            int i = 0;
            sql.setInt(++i, personID);
            sql.setInt(++i, DbConstant.PERSON_2_CONTACT_MEDIA);

            rs = sql.executeQuery();

            ContactMediaBean cmb = null;
            while (rs.next()) {
                if (cmb == null)
                    cmb = new ContactMediaBean(null,
                            DbConstant.PERSON_2_CONTACT_MEDIA);

                cmb.setContactMedia(new ContactMedia(personID));
                cmb.load(rs);

                Integer contactMediaCodeID = cmb.getContactMediaCodeID();
                if (ContactMediaCode.PHONE.equals(contactMediaCodeID))
                    getContact().setPhone(cmb.getContactMedia());
                else if (ContactMediaCode.MOBILE.equals(contactMediaCodeID))
                    getContact().setMobile(cmb.getContactMedia());
                else if (ContactMediaCode.FAX.equals(contactMediaCodeID))
                    getContact().setFax(cmb.getContactMedia());
                else if (ContactMediaCode.EMAIL.equals(contactMediaCodeID))
                    getContact().setEMail(cmb.getContactMedia());
            }

        } finally {
            close(rs, sql);
        }

    }

    private Integer getContactCodeID(Connection con) throws SQLException {

        if (getOwnerPrimaryKeyID() == null)
            return null;

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {

            sql = con
                    .prepareStatement("SELECT *"
                            + " FROM Link"
                            + " WHERE ( ObjectID1 IN ("
                            + " SELECT LinkID"
                            + " FROM Link"
                            + " WHERE ( ObjectID2=? ) AND ( LinkObjectTypeID=? )"
                            + " AND ( ObjectID1 IN"
                            + " (SELECT LinkID"
                            + " FROM Link"
                            + " WHERE ( ObjectID1=? ) AND ( ObjectID2=? ) AND ( LinkObjectTypeID=? )"
                            + " )" + " ) )" + ") AND ( LinkObjectTypeID=? )");

            int i = 0;
            sql.setInt(++i, DbConstant.CONTACT); // 11
            sql.setInt(++i, DbConstant.PERSON_2_RELATIONSHIP_FINANCE); // 
            sql.setInt(++i, getOwnerPrimaryKeyID().intValue());
            sql.setInt(++i, getPrimaryKeyID().intValue());
            sql.setInt(++i, DbConstant.PERSON_2_PERSON); // 
            sql.setInt(++i, DbConstant.PERSON_2_RELATIONSHIP); // 

            rs = sql.executeQuery();
            if (!rs.next())
                return null;

            Integer contactCodeID = (Integer) rs.getObject("ObjectID2");
            if (rs.next())
                throw new SQLException(
                        "------------> More than one ContactCodeID");

            return contactCodeID;

        } finally {
            close(rs, sql);
        }

    }

    /**
     * helper methods
     */
    private int getContactLinkID(Connection con) throws SQLException {

        if (contactLinkID == FPSLinkObject.LINK_NOT_FOUND) {

            int linkID = FPSLinkObject.getInstance().getLinkID(
                    getOwnerPrimaryKeyID(), getPrimaryKeyID(),
                    DbConstant.PERSON_2_PERSON, con);

            if (linkID == FPSLinkObject.LINK_NOT_FOUND)
                return linkID;

            linkID = FPSLinkObject.getInstance().getLinkID(linkID,
                    DbConstant.CONTACT,
                    DbConstant.PERSON_2_RELATIONSHIP_FINANCE, con);

            if (linkID == FPSLinkObject.LINK_NOT_FOUND)
                return linkID;

            contactLinkID = FPSLinkObject.getInstance().getLinkID(linkID, 0, // == 0
                                                                                // do
                                                                                // not
                                                                                // use
                    DbConstant.PERSON_2_RELATIONSHIP, con);

        }

        return contactLinkID;

    }

    /**
     * get/set methods
     */
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact value) {
        contact = value;
        contactLinkID = FPSLinkObject.LINK_NOT_FOUND;
    }

    public boolean isModified() {
        return getContact().isModified();
    }

    public void setModified(boolean value) {
        getContact().setModified(value);
    }

    public Integer getPrimaryKeyID() {
        return getContact().getPrimaryKeyID();
    }

    public void setPrimaryKeyID(Integer value) {
        getContact().setPrimaryKeyID(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getContact().getOwnerPrimaryKeyID();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getContact().setOwnerPrimaryKeyID(value);
    }

}
