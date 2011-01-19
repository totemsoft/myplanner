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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.argus.crypto.Digest;
import com.argus.financials.bean.DbConstant;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.code.AdviserTypeCode;
import com.argus.financials.dao.ClientDao;
import com.argus.financials.dao.UserDao;
import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.domain.hibernate.User;
import com.argus.financials.domain.hibernate.view.ClientView;
import com.argus.financials.etc.db.PersonNameAddressBean;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.client.ObjectNotFoundException;
import com.argus.financials.service.client.ServiceException;
import com.argus.financials.service.client.UserService;

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
     * @see com.argus.financials.service.UserService#findClient(java.lang.Long)
     */
    public Client findClient(Long clientId) throws ServiceException
    {
        return clientDao.findById(clientId);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findClients(java.util.Map, int, int)
     */
    public List<ClientView> findClients(Map<String, Object> criteria, int start, int length)
        throws ServiceException
    {
        // add user
        User user = getUserPreferences().getUser();
        Integer userTypeId = user == null ? null : user.getTypeId();
        boolean supportPerson = AdviserTypeCode.isSupportPerson(userTypeId);
        if (!supportPerson)
        {
            criteria.put(DbConstant.ADVISORID, user.getId());
        }
        else
        {
            criteria.put(DbConstant.ALL_USERS_CLIENTS, Boolean.TRUE);
        }
        //
        List<ClientView> clients = clientDao.findClients(criteria, start, length);
        return clients;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#persist(com.argus.financials.domain.hibernate.Client)
     */
    public Long persist(Client client) throws ServiceException
    {
        try
        {
            if (client == null || client.getId() == null)
            {
                // TODO: save via hibernate
                User user = getUserPreferences().getUser();
                ClientServiceImpl cs = new ClientServiceImpl();
                cs.setOwnerPrimaryKeyID(user.getId().intValue());
                Integer clientId = cs.create();
                return clientId.longValue();
            }
            else
            {
                // TODO: implement save
                return client.getId();
            }
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#remove(com.argus.financials.domain.hibernate.Client)
     */
    public Long remove(Client client) throws ServiceException
    {
        try {
            Connection con = getConnection();
            Long clientId = client.getId();
            boolean result = FPSLinkObject.getInstance().unlink(getPersonID(),
                clientId.intValue(), LinkObjectTypeConstant.USER_2_CLIENT,
                con) > 0;
            return clientId;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
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

}