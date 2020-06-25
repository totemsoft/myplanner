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

    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
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
                    userDao.persist(user);
                }
            }
            //
            userPreferences.clear();
            userPreferences.user(user);
            LOG.info("New user logged in: " + userPreferences.user());
            return user;
        }
        catch (ObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Deprecated
    @Override
    public void logout() throws ServiceException
    {
        LOG.info("User logged out: " + userPreferences.user());
        userPreferences.clear();
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
        IUser user = userPreferences.user();
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

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UserService#remove(au.com.totemsoft.myplanner.domain.hibernate.Client)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Long removeClient(IClient client) throws ServiceException
    {
        Connection con = null;
        try {
            IUser user = userPreferences.user();
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
        try (Connection con = sqlHelper.getConnection();) {
            // advisorTypeCodeID has to have a value
            Integer advisorTypeCodeID = 1;
            // LoginName has to have a value
            String loginName = "loginName";
            Integer personId = objectDao.getNewObjectID(ObjectTypeConstant.USER_PERSON, con);

            String sql = "INSERT INTO person (PersonID) VALUES (?)";
            try (PreparedStatement pstm = con.prepareStatement(sql);) {
                pstm.setInt(1, personId);
                pstm.executeUpdate();
            }

            sql = "INSERT INTO UserPerson"
                + " (UserPersonID, AdviserTypeCodeID, LoginName, LoginPassword)"
                + " VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstm = con.prepareStatement(sql);) {
                pstm.setInt(1, personId);
                pstm.setObject(2, advisorTypeCodeID, java.sql.Types.INTEGER);
                pstm.setString(3, loginName);
                pstm.setString(4, null);
                pstm.executeUpdate();
            }

            /*
             * // link XXX and user if ( getOwnerPrimaryKeyID() != null )
             * FPSLinkObject.getInstance().link( getOwnerPrimaryKeyID(),
             * personID, XXX_2_USER, con );
             */
//            setId(personID);
            return personId;
        } catch (SQLException e) {
            throw new CreateException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
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