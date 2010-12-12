/*
 * PersonHealth.java
 *
 * Created on 17 August 2001, 16:25
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
import com.argus.financials.code.BooleanCode;

public class PersonHealth extends AbstractPersistable {

    private Boolean isSmoker;

    private Integer healthStateCodeID;

    private boolean hospitalCover;

    public PersonHealth() {
        super();
    }

    public PersonHealth(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Boolean getIsSmoker() {
        return isSmoker;
    }

    public void setIsSmoker(Boolean value) {
        if (value == null && isSmoker == null)
            return;
        if (value != null && value.equals(isSmoker))
            return;

        isSmoker = value;
        setModified(true);
    }

    public boolean hasHospitalCover() {
        return hospitalCover;
    }

    public void hasHospitalCover(boolean value) {
        if (value == hospitalCover)
            return;

        hospitalCover = value;
        setModified(true);
    }

    public Integer getHealthStateCodeID() {
        return healthStateCodeID;
    }

    public void setHealthStateCodeID(Integer value) {
        if (value == null && healthStateCodeID == null)
            return;
        if (value != null && value.equals(healthStateCodeID))
            return;

        healthStateCodeID = value;
        setModified(true);
    }

    /**
     * 
     */
    public void load(ResultSet rs) throws SQLException {

        setPrimaryKeyID((Integer) rs.getObject("PersonHealthID"));

        String s = rs.getString("IsSmoker");
        if (s == null)
            isSmoker = null;
        else if (BooleanCode.rcNO.getCode().compareToIgnoreCase(s) == 0)
            isSmoker = Boolean.FALSE;
        else if (BooleanCode.rcYES.getCode().compareToIgnoreCase(s) == 0)
            isSmoker = Boolean.TRUE;

        healthStateCodeID = (Integer) rs.getObject("HealthStateCodeID");

        s = rs.getString("HospitalCover");
        if (s == null)
            hospitalCover = false;
        else if (BooleanCode.rcNO.getCode().compareToIgnoreCase(s) == 0)
            hospitalCover = false;
        else if (BooleanCode.rcYES.getCode().compareToIgnoreCase(s) == 0)
            hospitalCover = true;

        setModified(false);
    }

    public int store(Connection con) throws SQLException {

        /**
         * insert new if data changed (update current) PERSON XXX information
         */
        if (getPrimaryKeyID() != null && !isModified())
            return getPrimaryKeyID().intValue();

        PreparedStatement sql = con.prepareStatement("INSERT INTO PersonHealth"
                + " (PersonID, IsSmoker, HealthStateCodeID, HospitalCover)"
                + " VALUES" + " (?, ?, ?, ?)");

        sql.setInt(1, getOwnerPrimaryKeyID().intValue());

        String s;
        if (isSmoker == null)
            s = null;
        else if (isSmoker.booleanValue())
            s = BooleanCode.rcYES.getCode();
        else
            s = BooleanCode.rcNO.getCode();
        sql.setString(2, s);

        sql.setObject(3, healthStateCodeID, java.sql.Types.INTEGER);

        if (hospitalCover)
            s = BooleanCode.rcYES.getCode();
        else
            s = BooleanCode.rcNO.getCode();
        sql.setString(4, s);

        sql.executeUpdate();

        int newID = getIdentityID(con);

        if (getPrimaryKeyID() != null) {
            sql = con.prepareStatement("UPDATE PersonHealth SET" + " NextID=?"
                    + " WHERE PersonHealthID=?");

            sql.setInt(1, newID);
            sql.setInt(2, getPrimaryKeyID().intValue());

            sql.executeUpdate();

        }

        setPrimaryKeyID(new Integer(newID));

        setModified(false);
        return newID;

    }

}
