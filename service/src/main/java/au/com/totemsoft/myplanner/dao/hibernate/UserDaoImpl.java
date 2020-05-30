package au.com.totemsoft.myplanner.dao.hibernate;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import au.com.totemsoft.myplanner.dao.UserDao;
import au.com.totemsoft.myplanner.domain.hibernate.User;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Repository
public class UserDaoImpl extends BaseDAOImpl implements UserDao {

    @Override
    public User findByLogin(String login) {
        try {
            return (User) getEntityManager()
                .createQuery("FROM User WHERE login = :login")
                .setParameter("login", login)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User findByLoginNullPassword(String login) {
        try {
            return (User) getEntityManager()
                .createQuery("FROM User WHERE login = :login AND password IS NULL")
                .setParameter("login", login)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User findByLoginPassword(String login, String password) throws Exception {
        try {
            return (User) getEntityManager()
                .createQuery("FROM User WHERE login = :login AND password = :password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

}
