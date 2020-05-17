/*
 * UserService.java Created on 3 August 2001, 14:41
 */

package com.argus.financials.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IClient;
import com.argus.financials.api.bean.IClientView;
import com.argus.financials.api.bean.IUser;

/**
 * @author valeri chibaev
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
     * @param criteria
     * @param start
     * @param length
     * @return
     * @throws ServiceException
     */
    List<? extends IClientView> findClients(Map<String, Object> criteria, int start, int length) throws ServiceException;

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