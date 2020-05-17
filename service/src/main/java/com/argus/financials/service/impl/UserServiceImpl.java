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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.crypto.Digest;
import com.argus.dao.SQLHelper;
import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.DbConstant;
import com.argus.financials.api.bean.IClient;
import com.argus.financials.api.bean.IClientView;
import com.argus.financials.api.bean.IUser;
import com.argus.financials.api.bean.UserPreferences;
import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.api.dao.LinkObjectDao;
import com.argus.financials.api.dao.ObjectDao;
import com.argus.financials.api.service.UserService;
import com.argus.financials.code.AdviserTypeCode;
import com.argus.financials.dao.ClientDao;
import com.argus.financials.dao.UserDao;
import com.argus.financials.domain.hibernate.User;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.CreateException;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    @Autowired private ClientDao clientDao;

    @Autowired private UserDao userDao;

    @Autowired private SQLHelper sqlHelper;

    @Autowired private ObjectDao objectDao;
    @Autowired private LinkObjectDao linkObjectDao;

    @Autowired private UserPreferences userPreferences;

    @Autowired private ClientService clientService;

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findUserByLoginPassword(java.lang.String, java.lang.String)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public IUser login(String login, String password) throws ServiceException, ObjectNotFoundException
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
            userPreferences.setUser(user);
            System.out.println("New user logged in: " + userPreferences.getUser());
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
        System.out.println("User logged out: " + userPreferences.getUser());
        userPreferences.setUser(null);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findClient(java.lang.Long)
     */
    public IClient findClient(Long clientId) throws ServiceException
    {
        return clientDao.findById(clientId);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#findClients(java.util.Map, int, int)
     */
    public List<? extends IClientView> findClients(Map<String, Object> criteria, int start, int length)
        throws ServiceException
    {
        // add user
        IUser user = userPreferences.getUser();
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
        return clientDao.findClients(criteria, start, length);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.UserService#persist(com.argus.financials.domain.hibernate.Client)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Long persist(IClient client) throws ServiceException
    {
        try
        {
            if (client == null || client.getId() == null)
            {
                // TODO: save via hibernate
                IUser user = userPreferences.getUser();
                Integer clientId = clientService.create(user.getId().intValue());
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Long remove(IClient client) throws ServiceException
    {
        Connection con = null;
        try {
            IUser user = userPreferences.getUser();
            con = sqlHelper.getConnection();
            Long clientId = client.getId();
            boolean result = linkObjectDao.unlink(user.getId().intValue(),
                clientId.intValue(), LinkObjectTypeConstant.USER_2_CLIENT,
                con) > 0;
            return clientId;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ServiceException(e);
            }
        }
    }

    public Integer create() throws ServiceException, CreateException {
        PreparedStatement sql = null;
        try {
            // advisorTypeCodeID has to have a value
            Integer advisorTypeCodeID = 1;
            // LoginName has to have a value
            String loginName = "loginName";
            Connection con = sqlHelper.getConnection();
            Integer personId = objectDao.getNewObjectID(ObjectTypeConstant.USER_PERSON, con);

            sql = con.prepareStatement("INSERT INTO person (PersonID) VALUES (?)");
            sql.setInt(1, personId);
            sql.executeUpdate();

            sql = con.prepareStatement("INSERT INTO UserPerson"
                            + " (UserPersonID, AdviserTypeCodeID, LoginName, LoginPassword)"
                            + " VALUES (?, ?, ?, ?)");

            sql.setInt(1, personId);
            sql.setObject(2, advisorTypeCodeID, java.sql.Types.INTEGER);
            sql.setString(3, loginName);
            sql.setString(4, null);

            sql.executeUpdate();

            /*
             * // link XXX and user if ( getOwnerPrimaryKeyID() != null )
             * FPSLinkObject.getInstance().link( getOwnerPrimaryKeyID(),
             * personID, XXX_2_USER, con );
             */
//            setId(personID);
            con.close();
            return personId;
        } catch (SQLException e) {
            throw new CreateException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                sqlHelper.close(null, sql);
            } catch (SQLException sqle) {
                throw new ServiceException(sqle.getMessage());
            }
        }
    }

    public Object getOwnerPrimaryKey() throws ServiceException {
        return null;
    }
/*
    public void load(Integer personId, Connection con) throws SQLException, ObjectNotFoundException {
        PreparedStatement sql = null;
        ResultSet rs = null;
        try {
            sql = con.prepareStatement("SELECT p.*" + " FROM Person p"
                    + " WHERE (p.PersonID = ?)", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            sql.setInt(1, personId);
            rs = sql.executeQuery();
            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Person ID: " + personId);
            setId(personId);
            load(rs);
            sqlHelper.close(rs, sql);
            rs = null;
            sql = null;
            // loadAddresses( con );
        } finally {
            sqlHelper.close(rs, sql);
        }
    }

    private void load(ResultSet rs) throws SQLException {
        int personId = rs.getInt("PersonID");

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
*/
}