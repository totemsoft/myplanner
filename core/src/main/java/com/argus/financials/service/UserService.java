/*
 * UserService.java Created on 3 August 2001, 14:41
 */

package com.argus.financials.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.domain.hibernate.User;
import com.argus.financials.domain.hibernate.view.ClientView;

/**
 * @author valeri chibaev
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface UserService extends PersonService
{

    /**
     * Will set password for the first time (if blank in database)
     * @param login
     * @param password
     * @return
     * @throws ServiceException
     * @throws ObjectNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    User login(String login, String password) throws ServiceException,
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
    Client findClient(Long clientId) throws ServiceException;

    /**
     * 
     * @param criteria
     * @param start
     * @param length
     * @return
     * @throws ServiceException
     */
    List<ClientView> findClients(Map<String, Object> criteria, int start, int length) throws ServiceException;
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    PersonService create(Integer ownerPersonID, boolean store) throws ServiceException,
        CreateException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean removeClient(Integer clientID) throws ServiceException;

    public List findUsers(Properties criteria) throws ServiceException;

}