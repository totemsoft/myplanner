/*
 * UserServiceImpl.java
 *
 * Created on 3 August 2001, 15:08
 */

package com.argus.financials.service.impl;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.argus.crypto.Digest;
import com.argus.financials.bean.DbConstant;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.dao.ClientDao;
import com.argus.financials.domain.hibernate.view.Client;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.PersonName;
import com.argus.financials.etc.db.ContactBean;
import com.argus.financials.etc.db.PersonNameAddressBean;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.FinderException;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.financials.service.ServiceException;
import com.argus.financials.service.UserService;
import com.argus.util.Range;

public class UserServiceImpl extends PersonServiceImpl implements UserService {

    @Autowired
    private ClientDao clientDao;

    private Integer advisorTypeCodeID;

    private String loginName;

    private String loginPassword;

    private boolean flag;

    public UserService findByLoginNamePassword(String loginName, String loginPassword)
        throws ServiceException, ObjectNotFoundException {
        Integer personID = findPrimaryKeyByLoginNamePassword(loginName, loginPassword);
        return this;
    }

    public Integer create() throws ServiceException, CreateException {
        PreparedStatement sql = null;
        try {
            // advisorTypeCodeID has to have a value
            if (advisorTypeCodeID == null) {
                advisorTypeCodeID = new Integer(1);
            }
            // LoginName has to have a value
            if (loginName == null) {
                loginName = "loginName";
            }
            Connection con = this.getConnection();
            Integer personID = new Integer(getNewObjectID(USER_PERSON, con));

            sql = con.prepareStatement("INSERT INTO person (PersonID) VALUES (?)");
            sql.setInt(1, personID.intValue());
            sql.executeUpdate();

            sql = con.prepareStatement("INSERT INTO UserPerson"
                            + " (UserPersonID, AdviserTypeCodeID, LoginName, LoginPassword )"
                            + " VALUES (?, ?, ?, ?)");

            sql.setInt(1, personID.intValue());
            sql.setObject(2, advisorTypeCodeID, java.sql.Types.INTEGER);
            sql.setString(3, loginName);
            sql.setString(4, null);

            sql.executeUpdate();

            /*
             * // link XXX and user if ( getOwnerPrimaryKeyID() != null )
             * FPSLinkObject.getInstance().link( getOwnerPrimaryKeyID(),
             * personID, XXX_2_USER, con );
             */
            setPrimaryKeyID(personID);
            return personID;
        } catch (SQLException e) {
            throw new CreateException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(null, sql);
            } catch (SQLException sqle) {
                throw new ServiceException(sqle.getMessage());
            }
        }
    }

    public Object getOwnerPrimaryKey() throws ServiceException {
        return null;
    }

    public Integer findByPrimaryKey(Integer personID) throws ServiceException,
            FinderException {

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            con = this.getConnection();

            sql = con
                    .prepareStatement("SELECT * FROM UserPerson WHERE ( UserPersonID = ?)");

            sql.setInt(1, personID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find user ID: "
                        + personID);

            setAdviserTypeCodeID(new Integer(rs.getInt("AdviserTypeCodeID")));
            setLoginName(new String(rs.getString("LoginName")));
            setLoginPassword(new String(rs.getString("LoginPassword")));
            close(rs, sql);
            rs = null;
            sql = null;

            // we need adviser personal information for plan writer
            this.load(personID, con);

            setPrimaryKeyID(personID);

            return personID;

        } catch (ObjectNotFoundException e) {
            throw new FinderException(e.getMessage());
        } catch (SQLException sqle) {
            throw new FinderException(sqle);
        }

    }

    public void load(Integer personID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {

            sql = con.prepareStatement("SELECT p.*" + " FROM Person p"
                    + " WHERE (p.PersonID = ?)", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, personID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Person ID: "
                        + personID);

            setPrimaryKeyID(personID);

            load(rs);
            close(rs, sql);
            rs = null;
            sql = null;

            // loadAddresses( con );

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        int personID = rs.getInt("PersonID");

        // PERSON
        PersonNameAddressBean pnab = new PersonNameAddressBean();
        pnab.setPersonName(personName);
        pnab.load(rs);

        dobCountryID = (Integer) rs.getObject("DOBCountryID");
        taxFileNumber = rs.getString("TaxFileNumber");
        preferredLanguageID = (Integer) rs.getObject("PreferredLanguageID");
        preferredLanguage = rs.getString("PreferredLanguage");
        residenceCountryCodeID = (Integer) rs
                .getObject("ResidenceCountryCodeID");
        residenceStatusCodeID = (Integer) rs.getObject("ResidenceStatusCodeID");
        referalSourceCodeID = (Integer) rs.getObject("ReferalSourceCodeID");

        setDateCreated(rs.getDate("DateCreated"));
        setDateModified(rs.getDate("DateModified"));

    }

    public Integer findPrimaryKeyByLoginNamePassword(String name, String password)
            throws ServiceException, ObjectNotFoundException {

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        Integer personID = null;

        try {
            // System.out.println("Getting Connection...");
            con = this.getConnection();
            // System.out.println("...got Connection!");

            sql = con
                    .prepareStatement("SELECT UserPersonID FROM UserPerson"
                            + " WHERE ( LoginName = ? ) AND ( LoginPassword = ? ) AND (ActiveUser <> 'N' OR ActiveUser IS NULL)");
            sql.setString(1, name);
            sql.setString(2, Digest.digest(password));

            rs = sql.executeQuery();

            if (rs.next()) {
                personID = new Integer(rs.getInt("UserPersonID"));
            } else {
                close(rs, sql);
                rs = null;
                sql = null;

                personID = findByLoginName(con, name);

                sql = con
                        .prepareStatement("UPDATE UserPerson SET LoginPassword=? WHERE UserPersonID=?");
                sql.setString(1, password);
                sql.setInt(2, personID.intValue());
                sql.executeUpdate();
            }
            close(rs, sql);
            rs = null;
            sql = null;
            con = null;

            System.out.println("New user logged in: " + personID);

            return findByPrimaryKey(personID);

        } catch (SQLException sqle) {
            throw new ObjectNotFoundException(sqle);
        } catch (Exception e) {
            throw new ObjectNotFoundException(e);
        } finally {
            try {
                close(rs, sql);
            } catch (SQLException sqle) {
                throw new ServiceException(sqle.getMessage());
            }
        }

    }

    private Integer findByLoginName(Connection con, String name)
            throws SQLException, ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            // search first time login
            sql = con.prepareStatement("SELECT UserPersonID FROM UserPerson"
                    + " WHERE ( LoginName = ? ) AND ( LoginPassword IS NULL )");
            sql.setString(1, name);

            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("There is no user \"" + name
                        + "\" registered in the system.");

            return new Integer(rs.getInt("UserPersonID"));

        } finally {
            close(rs, sql);
            rs = null;
            sql = null;
        }

    }

    public boolean removeClient(Integer clientID)
            throws com.argus.financials.service.ServiceException {

        if (clientID == null)
            return false;

        Connection con = null;
        try {
            con = getConnection();
            return FPSLinkObject.getInstance().unlink(getPersonID(),
                    clientID.intValue(), LinkObjectTypeConstant.USER_2_CLIENT,
                    con) > 0;

        } catch (SQLException e) {
            throw new com.argus.financials.service.ServiceException(e.getMessage());
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Integer getAdviserTypeCodeID() throws com.argus.financials.service.ServiceException {
        return advisorTypeCodeID;
    }

    public void setAdviserTypeCodeID(Integer value)
            throws com.argus.financials.service.ServiceException {
        advisorTypeCodeID = value;
    }

    public String getLoginName() throws com.argus.financials.service.ServiceException {
        return loginName;
    }

    public void setLoginName(String value) throws com.argus.financials.service.ServiceException {
        loginName = value;
    }

    public String getLoginPassword() throws com.argus.financials.service.ServiceException {
        return loginPassword;
    }

    public void setLoginPassword(String value) throws com.argus.financials.service.ServiceException {
        loginPassword = value;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findClients(java.util.Map, com.argus.util.Range)
     */
    public List<Client> findClients(Map<String, Object> criteria, Range range)
        throws ServiceException
    {
        return clientDao.findClients(criteria, range);
        //return findClients2(criteria, range);
    }

    public List<Contact> findClients2(Map<String, Object> criteria, Range range) throws ServiceException {

        if (getPrimaryKeyID() == null)
            return Collections.emptyList();

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = this.getConnection();
            String sql = "SELECT * FROM vUserPersonClients WHERE 1=1";
            String selectionOptions = "";
            if (criteria != null) {
                String fn = PersonName.SURNAME;
                if (criteria.containsKey(fn)) {
                    String surname = (String) criteria.get(fn);
                    selectionOptions += " AND ( " + fn + " LIKE '" + surname + "%')";
                }
                fn = PersonName.FIRST_NAME;
                if (criteria.containsKey(fn)) {
                    String firstname = (String) criteria.get(fn);
                    selectionOptions += " AND ( " + fn + " LIKE '" + firstname + "%')";
                }
                fn = PersonName.DATE_OF_BIRTH;
                if (criteria.containsKey(fn)) {
                    String dob = (String) criteria.get(fn);
                    selectionOptions += " AND ( " + fn + " = '" + dob + "')";
                }
                fn = Address.COUNTRY;
                if (criteria.containsKey(fn)) {
                    String countryID = criteria.get(fn).toString();
                    selectionOptions += " AND countrycodeid = " + countryID;
                }
                fn = Address.STATE;
                if (criteria.containsKey(fn)) {
                    String stateID = criteria.get(fn).toString();
                    selectionOptions = selectionOptions + " AND statecodeid = " + stateID.toString();
                }
                fn = Address.POSTCODE;
                if (criteria.containsKey(fn)) {
                    String postCode = (String) criteria.get(fn);
                    selectionOptions += " AND postcode = " + postCode;
                }
                if (criteria.containsKey(DbConstant.ALL_USERS_CLIENTS)
                    && Boolean.TRUE.equals(criteria.get(DbConstant.ALL_USERS_CLIENTS)))
                {
                    // add nothing
                }
                else
                {
                    fn = DbConstant.ADVISORID;
                    if (criteria.containsKey(fn))
                    {
                        String advisorID = criteria.get(fn).toString();
                        selectionOptions += " AND AdviserID = " + advisorID;
                    }
                }
            }
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            List<Contact> data = new ArrayList<Contact>();
            while (rs.next()) {
                ContactMedia pPhone = new ContactMedia();
                pPhone.setValue2(rs.getString("Phone"));

                ContactMedia pFax = new ContactMedia();
                pFax.setValue2(rs.getString("Fax"));

                ContactMedia pEmail = new ContactMedia();
                pEmail.setValue2(rs.getString("Email"));

                ContactMedia pPhoneWork = new ContactMedia();
                pPhoneWork.setValue2(rs.getString("PhoneWork"));

                ContactMedia pFaxWork = new ContactMedia();
                pFaxWork.setValue2(rs.getString("FaxWork"));

                ContactMedia pEmailWork = new ContactMedia();
                pEmailWork.setValue2(rs.getString("EmailWork"));

                PersonName pOwner = new PersonName();
                pOwner.setSurname(rs.getString("AdvisorFamilyName"));
                pOwner.setFirstName(rs.getString("AdvisorFirstName"));

                PersonName pName = new PersonName();
                pName.setSurname(rs.getString("FamilyName"));
                pName.setFirstName(rs.getString("FirstName"));

                Address pAddress = new Address();
                pAddress.setStreetNumber(rs.getString("StreetNumber"));
                pAddress.setStreetNumber2(rs.getString("StreetNumber2"));
                pAddress.setSuburb(rs.getString("Suburb"));
                String pc = rs.getString("PostCode");
                pAddress.setPostCodeID(NumberUtils.createInteger(pc));
                String cc = rs.getString("CountryCodeid");
                pAddress.setCountryCodeID(NumberUtils.createInteger(cc));
                String sc = rs.getString("StateCodeId");
                pAddress.setStateCodeID(NumberUtils.createInteger(sc));

                Contact c = new Contact(rs.getInt(1)); // ObjectID1  (UserPersonID)
                c.setPrimaryKeyID((Integer) rs.getObject(2)); // ClientPersonID
                c.setOwnerName(pOwner);
                c.setName(pName);
                c.setAddress(pAddress);
                c.setPhone(pPhone);
                c.setFax(pFax);
                c.setEMail(pEmail);
                c.setPhoneWork(pPhoneWork);
                c.setFaxWork(pFaxWork);
                c.setEmailWork(pEmailWork);
                data.add(c);
            }
            close(rs, stmt);
            return data;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, stmt);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    public java.util.List findUsers(java.util.Properties selectionCriteria)
            throws ServiceException {

        if (getPrimaryKeyID() == null)
            return null;

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            con = this.getConnection();

            StringBuffer sb = new StringBuffer("SELECT UserPersonID"
                    + " FROM UserPerson");

            if (selectionCriteria != null) {

            }

            sql = con.prepareStatement(sb.toString()
            // , ResultSet.TYPE_FORWARD_ONLY
                    // , ResultSet.CONCUR_READ_ONLY
                    );

            rs = sql.executeQuery();

            Vector data = null;
            while (rs.next()) {
                if (data == null)
                    data = new Vector();

                Contact c = new Contact();
                c.setPrimaryKeyID((Integer) rs.getObject(1)); // UserPersonID

                data.addElement(c);
            }

            close(rs, sql);
            rs = null;
            sql = null;

            if (data == null)
                return null;

            Iterator iter = data.iterator();
            ArrayList toRemove = new ArrayList();
            while (iter.hasNext()) {
                Contact c = (Contact) iter.next();
                new ContactBean(c).load(con);

                if (c.toString().trim().length() == 0) {
                    //System.err.println("UserServiceImpl::findUsers(), c.toString().trim().length() == 0 for ID:" + c.getPrimaryKeyID());
                    // mark for remove
                    toRemove.add(c);
                }

            }

            iter = toRemove.iterator();
            while (iter.hasNext()) {
                Contact c = (Contact) iter.next();
                data.remove(c);
            }

            return data;

        } catch (SQLException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, sql);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }

    }

}