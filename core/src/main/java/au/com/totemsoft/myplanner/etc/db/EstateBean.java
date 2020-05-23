/*
 * EstateBean.java
 *
 * Created on 21 November 2001, 09:26
 */

package au.com.totemsoft.myplanner.etc.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.bean.DbConstant;
import au.com.totemsoft.myplanner.bean.db.AbstractPersistable;
import au.com.totemsoft.myplanner.etc.Estate;
import au.com.totemsoft.util.DateTimeUtils;

public class EstateBean extends AbstractPersistable {

    // has to be instanciated in top level (final) derived class
    protected Estate estate; // aggregation

    //
    private int objectTypeID = DbConstant.ESTATE;

    // First level of linkage (e.g. objectTypeID1 = CLIENT), objectTypeID2 =
    // ESTATE
    private int[] linkObjectTypes = new int[] { DbConstant.CLIENT_2_ESTATE };

    /** Creates new EstateBean */
    public EstateBean() {
    }

    public EstateBean(Estate estate) {
        setEstate(estate);
    }

    /**
     * helper methods
     */
    public Class getEstateClass() {
        return Estate.class;
    }

    public int getObjectTypeID() {
        return objectTypeID;
    }

    // public void setObjectTypeID( int value ) { objectTypeID = value; }

    protected int getLinkObjectTypeID(int level) {
        switch (level) {
        case 1:
            return linkObjectTypes[0];
        default:
            throw new RuntimeException(
                    "EstateBean.getLinkObjectTypeID() Invalid linkage level: "
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
        default:
            throw new RuntimeException(
                    "EstateBean.setLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    /**
     * IPersistable methods
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getId(), con);
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        boolean newConnection = (con == null);
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            if (newConnection)
                con = this.getConnection();

            setId(primaryKeyID);

            sql = con.prepareStatement("SELECT pe.*" + " FROM PersonEstate pe"
                    + " WHERE (pe.PersonEstateID = ?)",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException(
                        "Can not find PersonEstate ID: " + primaryKeyID);

            Integer personEstateID = (Integer) rs.getObject("PersonEstateID");
            load(rs);
            setId(personEstateID);

        } finally {
            if (rs != null)
                rs.close();
            if (sql != null)
                sql.close();
            if (newConnection && con != null)
                con.close();
        }

    }

    public void load(ResultSet rs) throws SQLException {

        try {
            getEstate().setStatusCodeID((Integer) rs.getObject("StatusCodeID"));
            getEstate().setDateLastReviewed(rs.getDate("DateLastReviewed"));
            getEstate().setExpectedChangesCodeID(
                    (Integer) rs.getObject("ExpectedChangesCodeID"));
            getEstate().setExecutorStatusCodeID(
                    (Integer) rs.getObject("ExecutorStatusCodeID"));
            getEstate().setAttorneyStatusCodeID(
                    (Integer) rs.getObject("AttorneyStatusCodeID"));
            getEstate().setPrenuptualCodeID(
                    (Integer) rs.getObject("PrenuptualCodeID"));
            getEstate().setInsolvencyRiskCodeID(
                    (Integer) rs.getObject("InsolvencyRiskCodeID"));
        } catch (InvalidCodeException e) { /* impossible */
        }
        // DateCreated datetime NOT NULL DEFAULT getDate(),
        // DateModified datetime NULL

        setModified(false);

    }

    // check if this contact person already exists in Person table (e.g.
    // Partner)
    // in CLIENT !!! ( use find() return PersonID(s) to setId() )
    public int store(Connection con) throws SQLException {

        if (!isModified())
            return getId().intValue();

        int i = 0;
        PreparedStatement sql = null;

        if (getId() == null) {

            // insert into Object table new PersonEstateID
            int estateID = getNewObjectID(DbConstant.ESTATE, con);

            // do insert into PersonEstate table
            sql = con
                    .prepareStatement("INSERT INTO PersonEstate"
                            + " (PersonEstateID,StatusCodeID,DateLastReviewed"
                            + ",ExpectedChangesCodeID,ExecutorStatusCodeID,AttorneyStatusCodeID"
                            + ",PrenuptualCodeID,InsolvencyRiskCodeID)"
                            + " VALUES" + " (?,?,?,?,?,?,?,?)");

            i = 0;
            sql.setInt(++i, estateID);
            sql.setObject(++i, getEstate().getStatusCodeID(),
                    java.sql.Types.INTEGER);

            if (getEstate().getDateLastReviewed() == null)
                sql.setNull(++i, java.sql.Types.VARCHAR);
            else
                sql.setString(++i, DateTimeUtils.getJdbcDate(getEstate()
                        .getDateLastReviewed()));

            sql.setObject(++i, getEstate().getExpectedChangesCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getExecutorStatusCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getAttorneyStatusCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getPrenuptualCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getInsolvencyRiskCodeID(),
                    java.sql.Types.INTEGER);

            sql.executeUpdate();

            // link this estate to client
            int linkID = linkObjectDao.link(
                    getOwnerId(), new Integer(estateID),
                    getLinkObjectTypeID(1),// DbID.CLIENT_2_ESTATE,
                    con);

            setId(new Integer(estateID));

        } else if (isModified()) {

            // do update on PersonEstate table
            sql = con
                    .prepareStatement("UPDATE PersonEstate SET"
                            + " StatusCodeID=?,DateLastReviewed=?"
                            + ",ExpectedChangesCodeID=?,ExecutorStatusCodeID=?,AttorneyStatusCodeID=?"
                            + ",PrenuptualCodeID=?,InsolvencyRiskCodeID=?"
                            + " WHERE PersonEstateID=?");

            i = 0;
            sql.setObject(++i, getEstate().getStatusCodeID(),
                    java.sql.Types.INTEGER);

            if (getEstate().getDateLastReviewed() == null)
                sql.setNull(++i, java.sql.Types.VARCHAR);
            else
                sql.setString(++i, DateTimeUtils.getJdbcDate(getEstate()
                        .getDateLastReviewed()));

            sql.setObject(++i, getEstate().getExpectedChangesCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getExecutorStatusCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getAttorneyStatusCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getPrenuptualCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, getEstate().getInsolvencyRiskCodeID(),
                    java.sql.Types.INTEGER);

            sql.setInt(++i, getId().intValue());

            sql.executeUpdate();

        }

        setModified(false);
        return getId().intValue();

    }

    // remove link only: DbID.CLIENT_2_PERSON
    public void remove(Connection con) throws SQLException {

        boolean newConnection = (con == null);

        try {
            if (newConnection)
                con = this.getConnection();

            // remove link this contact to client
            int linkID = linkObjectDao.unlink(
                    getOwnerId().intValue(),
                    getId().intValue(), getLinkObjectTypeID(1), con);

        } finally {
            if (newConnection && con != null)
                con.close();
        }

    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * helper methods
     */

    /**
     * get/set methods
     */
    public Estate getEstate() {
        if (estate == null)
            estate = new Estate();
        return estate;
    }

    public void setEstate(Estate value) {
        if (value != null && !value.getClass().equals(Estate.class))
            throw new RuntimeException(
                    "EstateBean.setContact( incompatible Class )");

        // boolean modified = ;

        estate = value;
        setModified(estate != null);
    }

    public boolean isModified() {
        return getEstate().isModified();
    }

    public void setModified(boolean value) {
        getEstate().setModified(value);
    }

    public Integer getId() {
        return getEstate().getId();
    }

    public void setId(Integer value) {
        getEstate().setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getEstate().getOwnerId();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getEstate().setOwnerId(value);
    }

}
