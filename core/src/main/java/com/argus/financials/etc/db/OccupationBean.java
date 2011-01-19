/*
 * OccupationBean.java
 *
 * Created on 30 August 2001, 14:21
 */

package com.argus.financials.etc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.etc.Occupation;
import com.argus.financials.service.client.ObjectNotFoundException;

public final class OccupationBean
// extends Occupation
        // implements IPersistable
        extends AbstractPersistable {

    // has to be instanciated in top level (final) derived class
    protected Occupation occupation; // aggregation

    public OccupationBean() {
    }

    // public OccupationBean( int ownerPrimaryKeyID ) {
    // super( ownerPrimaryKeyID );
    // }

    public OccupationBean(Occupation occupation) {
        setOccupation(occupation);
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
                            "SELECT PersonOccupationID, JobDescription, EmploymentStatusCodeID, IndustryCodeID, OccupationCodeID"
                                    + " FROM PersonOccupation"
                                    + " WHERE ( PersonOccupationID=? )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException(
                        "Can not find Person Occupation ID: " + primaryKeyID);

            setPrimaryKeyID(primaryKeyID);

            load(rs);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        setPrimaryKeyID((Integer) rs.getObject("PersonOccupationID"));

        getOccupation().setJobDescription(rs.getString("JobDescription"));
        getOccupation().setEmploymentStatusCodeID(
                (Integer) rs.getObject("EmploymentStatusCodeID"));
        getOccupation().setIndustryCodeID(
                (Integer) rs.getObject("IndustryCodeID"));
        getOccupation().setOccupationCodeID(
                (Integer) rs.getObject("OccupationCodeID"));

        setModified(false);
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return getPrimaryKeyID().intValue();

        int i = 0;
        PreparedStatement sql = null;

        // do insert into Person table
        sql = con.prepareStatement("INSERT INTO PersonOccupation"
                + " (PersonID,JobDescription,EmploymentStatusCodeID"
                + " ,IndustryCodeID,OccupationCodeID)" + " VALUES"
                + " (?,?,?,?,?)");

        i = 0;
        sql.setInt(++i, getOwnerPrimaryKeyID().intValue());
        sql.setString(++i, getOccupation().getJobDescription());
        sql.setObject(++i, getOccupation().getEmploymentStatusCodeID(),
                java.sql.Types.INTEGER);
        sql.setObject(++i, getOccupation().getIndustryCodeID(),
                java.sql.Types.INTEGER);
        sql.setObject(++i, getOccupation().getOccupationCodeID(),
                java.sql.Types.INTEGER);

        sql.executeUpdate();

        int newID = getIdentityID(con);

        if (getPrimaryKeyID() != null) {
            sql = con.prepareStatement("UPDATE PersonOccupation SET"
                    + " NextID=?" + " WHERE PersonOccupationID=?");

            sql.setInt(1, newID);
            sql.setInt(2, getPrimaryKeyID().intValue());

            sql.executeUpdate();

        }

        setPrimaryKeyID(new Integer(newID));

        setModified(false);
        return getPrimaryKeyID().intValue();

    }

    // do not delete from PERSON table (can be other links to this person)
    public void remove(Connection con) throws SQLException {

    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * get/set methods
     */
    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation value) {
        if (value != null && !value.getClass().equals(Occupation.class))
            throw new RuntimeException(
                    "OccupationBean.setOccupation( incompatible Class )");

        // boolean modified = ;

        occupation = value;
        // setModified( occupation != null );
    }

    public boolean isModified() {
        return occupation.isModified();
    }

    public void setModified(boolean value) {
        if (occupation != null)
            occupation.setModified(value);
    }

    public Integer getPrimaryKeyID() {
        return occupation.getPrimaryKeyID();
    }

    public void setPrimaryKeyID(Integer value) {
        if (occupation != null)
            occupation.setPrimaryKeyID(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return occupation.getOwnerPrimaryKeyID();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        if (occupation != null)
            occupation.setOwnerPrimaryKeyID(value);
    }

}
