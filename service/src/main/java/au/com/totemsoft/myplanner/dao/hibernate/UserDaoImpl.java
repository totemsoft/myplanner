package au.com.totemsoft.myplanner.dao.hibernate;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import au.com.totemsoft.myplanner.dao.UserDao;
import au.com.totemsoft.myplanner.domain.hibernate.User;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Repository
public class UserDaoImpl extends BaseDAOImpl implements UserDao
{

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.dao.UserDao#findByLogin(java.lang.String)
     */
    public User findByLogin(String login)
    {
        try {
            User user = (User) getEntityManager()
                .createQuery("FROM User WHERE login = :login AND password IS NULL")
                .setParameter("login", login)
                .getSingleResult();
            return user;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.dao.UserDao#findByLoginPassword(java.lang.String, java.lang.String)
     */
    public User findByLoginPassword(String login, String password) throws Exception
    {
        try {
            User user = (User) getEntityManager()
                .createQuery("FROM User WHERE login = :login AND password = :password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
            return user;
        }
        catch (NoResultException e) {
            return null;
        }
        catch (Exception e) {
            throw e;
        }
    }

}