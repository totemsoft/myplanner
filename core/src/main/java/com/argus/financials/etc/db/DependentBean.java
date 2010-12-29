/*
 * DependentBean.java
 *
 * Created on 22 August 2001, 16:35
 */

package com.argus.financials.etc.db;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.bean.DbConstant;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.Dependent;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.util.DateTimeUtils;

public class DependentBean extends ContactBean {

    private int relationshipLinkID = FPSLinkObject.LINK_NOT_FOUND;

    protected DependentBean() {
        setObjectTypeID(DbConstant.DEPENDENT);
    }

    public DependentBean(Dependent dependent) {
        this();
        setDependent(dependent);
    }

    /**
     * IPersistable methods
     */
    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            setPrimaryKeyID(primaryKeyID);

            // has to be first, or we have to close rs/sql before
            loadAddress(con);

            // RelationshipFinancial table (via Link table)
            getDependent().setRelationshipCodeID(getRelationshipCodeID(con));

            sql = con.prepareStatement("SELECT p.*" + " FROM Person p"
                    + " WHERE (p.PersonID = ?)", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find dependent ID: "
                        + primaryKeyID);

            load(rs);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        // Person table
        setPrimaryKeyID(new Integer(rs.getInt("PersonID")));
        getDependent().setSupportToAge((Integer) rs.getObject("SupportToAge"));

        PersonNameAddressBean pnab = new PersonNameAddressBean();
        pnab.setPersonName(getDependent().getName());
        pnab.load(rs);

        getDependent().setDobCountryID((Integer) rs.getObject("DOBCountryID"));

        // RelationshipFinancial table (via Link table)
        // getDependent().setRelationshipCodeID( getRelationshipCodeID() );

    }

    // check if this dependent person already exists in Person table (e.g.
    // Partner)
    // in PERSON !!! ( use find() return PersonID(s) to setPrimaryKeyID() )
    public int store(Connection con) throws SQLException {

        if (!isModified())
            return getPrimaryKeyID().intValue();

        int i = 0;
        PreparedStatement sql = null;
        Integer relationshipCodeID = getDependent().getRelationshipCodeID();

        try {
            if (getPrimaryKeyID() == null) {

                // insert into Object table new PersonID
                int dependentID = getNewObjectID(DbConstant.PERSON, con);

                // do insert into Person table
                sql = con
                        .prepareStatement("INSERT INTO Person"
                                + " (PersonID,SexCodeID,TitleCodeID,FamilyName,FirstName,OtherGivenNames,DateOfBirth,SupportToAge,DOBCountryID)"
                                + " VALUES" + " (?,?,?,?,?,?,?,?,?)");

                i = 0;
                sql.setInt(++i, dependentID);
                sql.setObject(++i, getDependent().getName().getSexCodeID(),
                        java.sql.Types.INTEGER);
                sql.setObject(++i, getDependent().getName().getTitleCodeID(),
                        java.sql.Types.INTEGER);
                sql.setString(++i, getDependent().getName().getSurname());
                sql.setString(++i, getDependent().getName().getFirstName());
                sql.setString(++i, getDependent().getName()
                        .getOtherGivenNames());
                if (getDependent().getDateOfBirth() == null)
                    sql.setNull(++i, java.sql.Types.VARCHAR);
                else
                    sql.setString(++i, DateTimeUtils.getJdbcDate(getDependent()
                            .getDateOfBirth()));

                sql.setObject(++i, getDependent().getSupportToAge(),
                        java.sql.Types.INTEGER);
                sql.setObject(++i, getDependent().getDobCountryID(),
                        java.sql.Types.INTEGER);
                sql.executeUpdate();
                sql.close();

                setPrimaryKeyID(new Integer(dependentID));

                // person.setLinkedPerson( PERSON_2_PERSON, DEPENDENT,
                // PERSON_2_RELATIONSHIP_FINANCE, con );
                // link this dependent to client/relationship-finance
                int linkID = setLink(getLinkObjectTypeID(1), // DbID.PERSON_2_PERSON,
                        getObjectTypeID(), getLinkObjectTypeID(2), // DbID.DEPENDENT,
                                                                    // DbID.PERSON_2_RELATIONSHIP_FINANCE,
                        con);

                relationshipLinkID = FPSLinkObject.getInstance().link(
                        new Integer(linkID), relationshipCodeID, // can be
                                                                    // null !!!
                        DbConstant.PERSON_2_RELATIONSHIP, con);

                // if (DEBUG) System.out.println( "------------>INSERT OwnerPK:
                // " + getOwnerPrimaryKeyID() + ", PK: " + getPrimaryKeyID() );
                // if (DEBUG) System.out.println( "------------>INSERT
                // relationshipLinkID: " + relationshipLinkID + ",
                // relationshipCodeID: " + relationshipCodeID + ", " + new
                // RelationshipCode().getCodeDescription( relationshipCodeID )
                // );

            } else {
                if (getDependent().getName().isModified()) {

                    // do update on PERSON table
                    sql = con
                            .prepareStatement("UPDATE Person SET"
                                    + " SexCodeID=?,TitleCodeID=?,FamilyName=?,FirstName=?"
                                    + ",OtherGivenNames=?,DateOfBirth=?,SupportToAge=?,DOBCountryID=?"
                                    + " WHERE PersonID=?");

                    i = 0;
                    sql.setObject(++i, getDependent().getName().getSexCodeID(),
                            java.sql.Types.INTEGER);
                    sql.setObject(++i, getDependent().getName()
                            .getTitleCodeID(), java.sql.Types.INTEGER);
                    sql
                            .setString(++i, getDependent().getName()
                                    .getSurname());
                    sql.setString(++i, getDependent().getName().getFirstName());
                    sql.setString(++i, getDependent().getName()
                            .getOtherGivenNames());

                    if (getDependent().getDateOfBirth() == null)
                        sql.setNull(++i, java.sql.Types.VARCHAR);
                    else
                        sql.setString(++i, DateTimeUtils
                                .getJdbcDate(getDependent().getDateOfBirth()));
                    sql.setObject(++i, getDependent().getSupportToAge(),
                            java.sql.Types.INTEGER);
                    sql.setObject(++i, getDependent().getDobCountryID(),
                            java.sql.Types.INTEGER);

                    sql.setInt(++i, getPrimaryKeyID().intValue());

                    sql.executeUpdate();
                    sql.close();

                }

                // update relationshipCode link
                if (getRelationshipLinkID(con) != FPSLinkObject.LINK_NOT_FOUND) {
                    FPSLinkObject.getInstance().updateLinkForObject2(
                            new Integer(relationshipLinkID),
                            relationshipCodeID, con);
                }

                // if (DEBUG) System.out.println( "------------>UPDATE OwnerPK:
                // " + getOwnerPrimaryKeyID() + ", PK: " + getPrimaryKeyID() );
                // if (DEBUG) System.out.println( "------------>UPDATE
                // relationshipLinkID: " + relationshipLinkID + ",
                // relationshipCodeID: " + relationshipCodeID + ", " + new
                // RelationshipCode().getCodeDescription( relationshipCodeID )
                // );
            }
        } finally {
            close(null, sql);
        }

        // store address
        Address a = getDependent().getAddress();
        if (a != null && a.isModified()) {
            a.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new AddressBean(a, DbConstant.PERSON_2_ADDRESS).store(con);
        }

        setModified(false);
        return getPrimaryKeyID().intValue();

    }

    // return null - if not found
    // PersonID - if found
    public Integer find() throws SQLException {

        if (getPrimaryKeyID() != null)
            return getPrimaryKeyID();

        // check if this person known as not dependent (e.g. partner)
        Connection con = getConnection();
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con
                    .prepareStatement("SELECT PersonID FROM Person WHERE"
                            + " (FamilyName=?) AND (FirstName=?) AND (OtherGivenNames=?)");

            // ??? what if IS NULL ???
            sql.setString(1, getDependent().getName().getSurname());
            sql.setString(2, getDependent().getName().getFirstName());
            sql.setString(3, getDependent().getName().getOtherGivenNames());

            rs = sql.executeQuery();

            if (!rs.next())
                return null;

            return (Integer) rs.getObject("PersonID");

        } finally {
            close(rs, sql);
        }

    }

    /**
     * 
     */
    private Integer getRelationshipCodeID(Connection con) throws SQLException {

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
            sql.setInt(++i, DbConstant.DEPENDENT); // 10
            sql.setInt(++i, DbConstant.PERSON_2_RELATIONSHIP_FINANCE); // 
            sql.setInt(++i, getOwnerPrimaryKeyID().intValue());
            sql.setInt(++i, getPrimaryKeyID().intValue());
            sql.setInt(++i, DbConstant.PERSON_2_PERSON); // 
            sql.setInt(++i, DbConstant.PERSON_2_RELATIONSHIP); // 

            rs = sql.executeQuery();
            // if (DEBUG) System.out.println( "------------> OwnerPK: " +
            // getOwnerPrimaryKeyID() + ", PK: " + getPrimaryKeyID() );
            if (!rs.next())
                return null;

            Integer relationshipCodeID = (Integer) rs.getObject("ObjectID2");
            // if (DEBUG) System.out.println( "------------> RelationshipCodeID:
            // " + relationshipCodeID + ", " + new
            // RelationshipCode().getCodeDescription( relationshipCodeID ) );
            if (rs.next())
                throw new SQLException(
                        "------------> More than one RelationshipCodeID");

            return relationshipCodeID;

        } finally {
            close(rs, sql);
        }

    }

    /**
     * helper methods
     */
    private int getRelationshipLinkID(Connection con) throws SQLException {

        if (relationshipLinkID == FPSLinkObject.LINK_NOT_FOUND) {

            int linkID = FPSLinkObject.getInstance().getLinkID(
                    getOwnerPrimaryKeyID(), getPrimaryKeyID(),
                    DbConstant.PERSON_2_PERSON, con);
            if (linkID == FPSLinkObject.LINK_NOT_FOUND)
                return linkID;

            linkID = FPSLinkObject.getInstance().getLinkID(linkID,
                    DbConstant.DEPENDENT,
                    DbConstant.PERSON_2_RELATIONSHIP_FINANCE, con);
            if (linkID == FPSLinkObject.LINK_NOT_FOUND)
                return linkID;

            relationshipLinkID = FPSLinkObject.getInstance().getLinkID(linkID,
                    0, // == 0 do not use
                    DbConstant.PERSON_2_RELATIONSHIP, con);

        }

        return relationshipLinkID;

    }

    /**
     * get/set methods
     */
    public Dependent getDependent() {
        return (Dependent) getContact();
    }

    public void setDependent(Dependent value) {
        setContact(value);
        relationshipLinkID = FPSLinkObject.LINK_NOT_FOUND;
    }

    public void setPrimaryKeyID(Integer value) {
        super.setPrimaryKeyID(value);
        relationshipLinkID = FPSLinkObject.LINK_NOT_FOUND;
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        super.setOwnerPrimaryKeyID(value);
        relationshipLinkID = FPSLinkObject.LINK_NOT_FOUND;
    }
}
