/*
 * PersonNameAddressBean.java
 *
 * Created on 29 October 2001, 10:23
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
import com.argus.financials.etc.Address;
import com.argus.financials.etc.PersonName;
import com.argus.financials.service.client.ObjectNotFoundException;

public class PersonNameAddressBean extends AbstractPersistable {

    // has to be instanciated in top level (final) derived class
    // private PersonNameAddress personNameAddress; // aggregation
    private PersonName personName; // aggregation

    private Address personAddress; // aggregation

    /** Creates a new instance of PersonNameAddressBean */
    public PersonNameAddressBean() {
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
                            "SELECT SexCodeID, TitleCodeID, FamilyName, FirstName"
                                    + ", OtherGivenNames, PreferredName, MaritalCodeID, DateOfBirth"
                                    + " FROM PersonService"
                                    + " WHERE ( PersonID = ? )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find PersonID: "
                        + primaryKeyID);

            load(rs);

            setPrimaryKeyID(primaryKeyID);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        getPersonName().setSexCodeID((Integer) rs.getObject("SexCodeID"));
        getPersonName().setTitleCodeID((Integer) rs.getObject("TitleCodeID"));
        getPersonName().setSurname(rs.getString("FamilyName"));
        getPersonName().setFirstName(rs.getString("FirstName"));
        getPersonName().setOtherGivenNames(rs.getString("OtherGivenNames"));
        getPersonName().setPreferredName(rs.getString("PreferredName"));
        getPersonName().setMaritalCodeID(
                (Integer) rs.getObject("MaritalCodeID"));
        getPersonName().setDateOfBirth(rs.getDate("DateOfBirth"));

        setModified(false);
    }

    public int store(Connection con) throws SQLException {
        return -1;
    }

    public Integer find() throws SQLException {
        return null;
    }

    public void remove(Connection con) throws SQLException {

    }

    /**
     * get/set methods
     */
    /*
     * public PersonNameAddress getPersonNameAddress() { if ( personNameAddress ==
     * null ) personNameAddress = new PersonNameAddress(); return
     * personNameAddress; } public void setPersonNameAddress( PersonNameAddress
     * value ) { personNameAddress = value; }
     */

    public PersonName getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName value) {
        personName = value;
        setModified(value != null);
    }

    public Address getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(Address value) {
        personAddress = value;
        setModified(value != null);
    }

}
