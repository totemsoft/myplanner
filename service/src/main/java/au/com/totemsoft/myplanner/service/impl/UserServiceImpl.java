/*
 * UserServiceImpl.java
 *
 * Created on 3 August 2001, 15:08
 */

package au.com.totemsoft.myplanner.service.impl;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.DbConstant;
import au.com.totemsoft.myplanner.api.bean.IClient;
import au.com.totemsoft.myplanner.api.bean.IClientView;
import au.com.totemsoft.myplanner.api.bean.IUser;
import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.api.dao.LinkObjectDao;
import au.com.totemsoft.myplanner.api.dao.ObjectDao;
import au.com.totemsoft.myplanner.api.service.UserService;
import au.com.totemsoft.myplanner.code.AdviserTypeCode;
import au.com.totemsoft.myplanner.dao.ClientDao;
import au.com.totemsoft.myplanner.dao.UserDao;
import au.com.totemsoft.myplanner.domain.hibernate.User;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.CreateException;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    @Inject private PasswordEncoder passwordEncoder;

    @Inject private ClientDao clientDao;

    @Inject private UserDao userDao;

    @Inject private SQLHelper sqlHelper;

    @Inject private ObjectDao objectDao;
    @Inject private LinkObjectDao linkObjectDao;

    @Inject private UserPreferences userPreferences;

    @Inject private ClientService clientService;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UserService#findUserByLoginPassword(java.lang.String, java.lang.String)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public IUser login(String login, String password) throws ServiceException, ObjectNotFoundException
    {
        // validate
        if (StringUtils.isBlank(login)) {
            return null;
        }
        //
        try {
            String passwordEncoded = passwordEncoder.encode(password);
            User user = userDao.findByLoginPassword(login, passwordEncoded);
            if (user == null) {
                user = userDao.findByLoginNullPassword(login);
                if (user == null) {
                    throw new ObjectNotFoundException("There is no user \"" + login
                        + "\" registered in the system.");
                }
                if (StringUtils.isBlank(user.getPassword())) {
                    // save new password (if reset to blank, eg via sql)
                    user.setPassword(passwordEncoded);
                    userDao.save(user);
                }
            }
            userPreferences.setUser(user);
            LOG.info("New user logged in: " + userPreferences.getUser());
            return user;
        }
        catch (ObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UserService#logout()
     */
    public void logout() throws ServiceException
    {
        LOG.info("User logged out: " + userPreferences.getUser());
        userPreferences.setUser(null);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UserService#findClient(java.lang.Long)
     */
    public IClient findClient(Long clientId) throws ServiceException
    {
        return clientDao.findById(clientId);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UserService#findClients(java.util.Map, int, int)
     */
    public List<IClientView> findClients(Map<String, Object> criteria, int start, int length)
        throws ServiceException
    {
        // add user
        IUser user = userPreferences.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        //
        if (criteria == null) {
            criteria = new HashMap<>();
        }
        //
        boolean supportPerson = AdviserTypeCode.isSupportPerson(user.getTypeId());
        if (!supportPerson) {
            criteria.put(DbConstant.ADVISORID, user.getId());
        } else {
            criteria.put(DbConstant.ALL_USERS_CLIENTS, Boolean.TRUE);
        }
        //
        return Collections.unmodifiableList(clientDao.findClients(criteria, start, length));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Long saveClient(IClient client) throws ServiceException
    {
        try {
            if (client == null || client.getId() == null) {
                // TODO: save via hibernate
                IUser user = userPreferences.getUser();
                Integer clientId = clientService.create(user.getId().intValue());
                return clientId.longValue();
            } else  {
                // TODO: implement save
                return client.getId();
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UserService#remove(au.com.totemsoft.myplanner.domain.hibernate.Client)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Long removeClient(IClient client) throws ServiceException
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