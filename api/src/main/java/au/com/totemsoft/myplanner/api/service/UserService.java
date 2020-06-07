/*
 * UserService.java Created on 3 August 2001, 14:41
 */

package au.com.totemsoft.myplanner.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.IClient;
import au.com.totemsoft.myplanner.api.bean.IClientView;
import au.com.totemsoft.myplanner.api.bean.IUser;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Service
public interface UserService
{

    /**
     * Will set password for the first time (if blank in database)
     * @param login
     * @param password
     * @return
     * @throws ServiceException
     * @throws ObjectNotFoundException
     */
    IUser login(String login, String password) throws ServiceException,
        ObjectNotFoundException;

    /**
     * 
     * @throws ServiceException
     */
    void logout() throws ServiceException;

    /**
     * 
     * @param clientId
     * @return
     * @throws ServiceException
     */
    IClient findClient(Long clientId) throws ServiceException;

    /**
     * 
     * @param criteria - can be empty on null
     * @param start - default 0
     * @param length - default 0
     * @return
     * @throws ServiceException
     */
    List<IClientView> findClients(Map<String, Object> criteria, int start, int length) throws ServiceException;

    /**
     * 
     * @param client
     * @return
     * @throws ServiceException
     */
    Long persist(IClient client) throws ServiceException;

    /**
     * 
     * @param client
     * @return
     * @throws ServiceException
     */
    Long remove(IClient client) throws ServiceException;

}