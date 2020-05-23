/*
 * DependentBean.java
 *
 * Created on 22 August 2001, 16:35
 */

package au.com.totemsoft.myplanner.etc.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.bean.DbConstant;
import au.com.totemsoft.myplanner.api.dao.LinkObjectDao;
import au.com.totemsoft.myplanner.etc.AddressDto;
import au.com.totemsoft.myplanner.etc.Dependent;
import au.com.totemsoft.util.DateTimeUtils;

public class DependentBean extends ContactBean {

    private int relationshipLinkID = LinkObjectDao.LINK_NOT_FOUND;

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
            setId(primaryKeyID);

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
        Dependent dependent = getDependent();
        // Person table
        setId(rs.getInt("PersonID"));
        dependent.setSupportToAge((Integer) rs.getObject("SupportToAge"));

        personDao.load(rs, dependent.getName());

        dependent.setDobCountryID((Integer) rs.getObject("DOBCountryID"));

        // RelationshipFinancial table (via Link table)
        //dependent.setRelationshipCodeID( getRelationshipCodeID() );

    }

    // check if this dependent person already exists in Person table (e.g.
    // Partner)
    // in PERSON !!! ( use find() return PersonID(s) to setId() )
    public int store(Connection con) throws SQLException {

        if (!isModified())
            return getId().intValue();

        int i = 0;
        PreparedStatement sql = null;
        Integer relationshipCodeID = getDependent().getRelationshipCodeID();

        try {
            if (getId() == null) {

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
                sql.setString(++i, getDependent().getName().getFirstname());
                sql.setString(++i, getDependent().getName()
                        .getOtherNames());
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

                setId(new Integer(dependentID));

                // person.setLinkedPerson( PERSON_2_PERSON, DEPENDENT,
                // PERSON_2_RELATIONSHIP_FINANCE, con );
                // link this dependent to client/relationship-finance
                int linkID = setLink(getLinkObjectTypeID(1), // DbID.PERSON_2_PERSON,
                        getObjectTypeID(), getLinkObjectTypeID(2), // DbID.DEPENDENT,
                                                                    // DbID.PERSON_2_RELATIONSHIP_FINANCE,
                        con);

                relationshipLinkID = linkObjectDao.link(
                        new Integer(linkID), relationshipCodeID, // can be null !!!
                        DbConstant.PERSON_2_RELATIONSHIP, con);
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
                    sql.setString(++i, getDependent().getName().getFirstname());
                    sql.setString(++i, getDependent().getName()
                            .getOtherNames());

                    if (getDependent().getDateOfBirth() == null)
                        sql.setNull(++i, java.sql.Types.VARCHAR);
                    else
                        sql.setString(++i, DateTimeUtils
                                .getJdbcDate(getDependent().getDateOfBirth()));
                    sql.setObject(++i, getDependent().getSupportToAge(),
                            java.sql.Types.INTEGER);
                    sql.setObject(++i, getDependent().getDobCountryID(),
                            java.sql.Types.INTEGER);

                    sql.setInt(++i, getId().intValue());

                    sql.executeUpdate();
                    sql.close();

                }

                // update relationshipCode link
                if (getRelationshipLinkID(con) != LinkObjectDao.LINK_NOT_FOUND) {
                    linkObjectDao.updateLinkForObject2(
                            new Integer(relationshipLinkID),
                            relationshipCodeID, con);
                }
            }
        } finally {
            close(null, sql);
        }

        // store address
        AddressDto a = getDependent().getAddress();
        if (a != null && a.isModified()) {
            a.setOwnerId(getId());
            new AddressBean(a, DbConstant.PERSON_2_ADDRESS).store(con);
        }

        setModified(false);
        return getId().intValue();

    }

    // return null - if not found
    // PersonID - if found
    public Integer find() throws SQLException {

        if (getId() != null)
            return getId();

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
            sql.setString(2, getDependent().getName().getFirstname());
            sql.setString(3, getDependent().getName().getOtherNames());

            rs = sql.executeQuery();

            if (!rs.next())
                return null;

            return (Integer) rs.getObject("PersonID");

        } finally {
            close(rs, sql);
            if (con != null)
                con.close();
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
            sql.setInt(++i, getOwnerId().intValue());
            sql.setInt(++i, getId().intValue());
            sql.setInt(++i, DbConstant.PERSON_2_PERSON); // 
            sql.setInt(++i, DbConstant.PERSON_2_RELATIONSHIP); // 

            rs = sql.executeQuery();
            if (!rs.next())
                return null;

            Integer relationshipCodeID = (Integer) rs.getObject("ObjectID2");
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

        if (relationshipLinkID == LinkObjectDao.LINK_NOT_FOUND) {

            int linkID = linkObjectDao.getLinkID(
                    getOwnerId(), getId(),
                    DbConstant.PERSON_2_PERSON, con);
            if (linkID == LinkObjectDao.LINK_NOT_FOUND)
                return linkID;

            linkID = linkObjectDao.getLinkID(linkID,
                    DbConstant.DEPENDENT,
                    DbConstant.PERSON_2_RELATIONSHIP_FINANCE, con);
            if (linkID == LinkObjectDao.LINK_NOT_FOUND)
                return linkID;

            relationshipLinkID = linkObjectDao.getLinkID(linkID,
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
        relationshipLinkID = LinkObjectDao.LINK_NOT_FOUND;
    }

    public void setId(Integer value) {
        super.setId(value);
        relationshipLinkID = LinkObjectDao.LINK_NOT_FOUND;
    }

    public void setOwnerId(Integer value) {
        super.setOwnerId(value);
        relationshipLinkID = LinkObjectDao.LINK_NOT_FOUND;
    }
}
