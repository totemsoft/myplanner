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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.argus.crypto.Digest;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.code.AdviserTypeCode;
import com.argus.financials.dao.ClientDao;
import com.argus.financials.dao.UserDao;
import com.argus.financials.domain.hibernate.User;
import com.argus.financials.domain.hibernate.view.Client;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.db.ContactBean;
import com.argus.financials.etc.db.PersonNameAddressBean;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.financials.service.ServiceException;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.service.UserService;
import com.argus.util.Range;

public class UserServiceImpl extends PersonServiceImpl implements UserService {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private UserDao userDao;

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findUserByLoginPassword(java.lang.String, java.lang.String)
     */
    public User login(String login, String password) throws ServiceException,
        ObjectNotFoundException
    {
        try
        {
            String passwordDigest = Digest.digest(password);
            User user = userDao.findByLoginPassword(login, passwordDigest);
            if (user == null)
            {
                user = userDao.findByLogin(login);
                if (user == null)
                {
                    throw new ObjectNotFoundException("There is no user \"" + login
                        + "\" registered in the system.");
                }
                else if (StringUtils.isBlank(user.getPassword()))
                {
                    // save new password (if reset to blank, eg via sql)
                    user.setPassword(passwordDigest);
                    userDao.save(user);
                }
            }
            getUserPreferences().setUser(user);
            System.out.println("New user logged in: " + getUserPreferences().getUser());
            return user;
        }
        catch (ObjectNotFoundException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#logout()
     */
    public void logout() throws ServiceException
    {
        System.out.println("User logged out: " + getUserPreferences().getUser());
        getUserPreferences().setUser(null);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findClients(java.util.Map, com.argus.util.Range)
     */
    public List<Client> findClients(Map<String, Object> criteria, Range range)
        throws ServiceException
    {
        // add user
        User user = ServiceLocator.getInstance().getUserPreferences().getUser();
        Integer userTypeId = user == null ? null : user.getTypeId();
        boolean supportPerson = AdviserTypeCode.isSupportPerson(userTypeId);
        if (!supportPerson)
        {
            criteria.put(UserService.ADVISORID, user.getId());
        }
        else
        {
            criteria.put(UserService.ALL_USERS_CLIENTS, Boolean.TRUE);
        }
        //
        return clientDao.findClients(criteria, range);
    }

    public Integer create() throws ServiceException, CreateException {
        PreparedStatement sql = null;
        try {
            // advisorTypeCodeID has to have a value
            Integer advisorTypeCodeID = 1;
            // LoginName has to have a value
            String loginName = "loginName";
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

    public List findUsers(java.util.Properties selectionCriteria)
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