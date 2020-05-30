package au.com.totemsoft.myplanner.dao;

import au.com.totemsoft.myplanner.domain.hibernate.User;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface UserDao extends BaseDAO {

    /**
     * 
     * @param login
     * @return
     */
    User findByLogin(String login);

    /**
     * 
     * @param login - null password
     * @return
     */
    User findByLoginNullPassword(String login);

    /**
     * 
     * @param login
     * @param password
     * @return
     */
    User findByLoginPassword(String login, String password) throws Exception;

}
