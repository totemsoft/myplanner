/*
 * PersonTrustDIYStatus.java
 *
 * Created on 17 August 2001, 17:08
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

import com.argus.financials.bean.db.AbstractPersistable;

public class PersonTrustDIYStatus extends AbstractPersistable {

    private Integer trustStatusCodeID;

    private Integer diyStatusCodeID;

    private Integer companyStatusCodeID;

    private String comment;

    public PersonTrustDIYStatus() {
        super();
    }

    public PersonTrustDIYStatus(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Integer getTrustStatusCodeID() {
        return trustStatusCodeID;
    }

    public void setTrustStatusCodeID(Integer value) {
        if (equals(trustStatusCodeID, value))
            return;

        // TODO
        // XXXCode.isValidThrow( value );

        trustStatusCodeID = value;
        setModified(true);
    }

    public Integer getDIYStatusCodeID() {
        return diyStatusCodeID;
    }

    public void setDIYStatusCodeID(Integer value) {
        if (equals(diyStatusCodeID, value))
            return;

        diyStatusCodeID = value;
        setModified(true);
    }

    public Integer getCompanyStatusCodeID() {
        return companyStatusCodeID;
    }

    public void setCompanyStatusCodeID(Integer value) {
        if (equals(companyStatusCodeID, value))
            return;

        companyStatusCodeID = value;
        setModified(true);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        if (equals(comment, value))
            return;

        comment = value.trim();
        setModified(true);
    }

    /**
     * 
     */
    public void load(ResultSet rs) throws SQLException {

        setPrimaryKeyID((Integer) rs.getObject("PersonTrustDIYStatusID"));
        trustStatusCodeID = (Integer) rs.getObject("TrustStatusCodeID");
        diyStatusCodeID = (Integer) rs.getObject("DIYStatusCodeID");
        companyStatusCodeID = (Integer) rs.getObject("CompanyStatusCodeID");

        comment = rs.getString("Comment");
        if (comment != null)
            comment = comment.trim();

        setModified(false);
    }

    public int store(Connection con) throws SQLException {

        /**
         * insert new if data changed (update current) PERSON XXX information
         */
        if (getPrimaryKeyID() != null && !isModified())
            return getPrimaryKeyID().intValue();

        PreparedStatement sql = con
                .prepareStatement("INSERT INTO PersonTrustDIYStatus"
                        + " (PersonID, TrustStatusCodeID, DIYStatusCodeID, CompanyStatusCodeID, Comment)"
                        + " VALUES" + " (?, ?, ?, ?, ?)");

        int i = 0;
        sql.setInt(++i, getOwnerPrimaryKeyID().intValue());
        sql.setObject(++i, trustStatusCodeID, java.sql.Types.INTEGER);
        sql.setObject(++i, diyStatusCodeID, java.sql.Types.INTEGER);
        sql.setObject(++i, companyStatusCodeID, java.sql.Types.INTEGER);
        sql.setString(++i, comment);

        sql.executeUpdate();

        int newID = getIdentityID(con);

        if (getPrimaryKeyID() != null) {
            sql = con.prepareStatement("UPDATE PersonTrustDIYStatus SET"
                    + " NextID=?" + " WHERE PersonTrustDIYStatusID=?");

            sql.setInt(1, newID);
            sql.setInt(2, getPrimaryKeyID().intValue());

            sql.executeUpdate();

        }

        setPrimaryKeyID(new Integer(newID));

        setModified(false);
        return newID;

    }

}
