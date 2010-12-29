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

import com.argus.financials.domain.hibernate.view.Client;
import com.argus.financials.etc.Contact;
import com.argus.util.Range;

/**
 * @author valeri chibaev
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface UserService extends PersonService
{

    /**
     * 
     * @param criteria
     * @param range - optional
     * @return
     * @throws ServiceException
     */
    List<Client> findClients(Map<String, Object> criteria, Range range) throws ServiceException;
    List<Contact> findClients2(Map<String, Object> criteria, Range range) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    PersonService create(Integer ownerPersonID, boolean store) throws ServiceException,
        CreateException;

    PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException,
        FinderException;

    UserService findByLoginNamePassword(String loginName, String loginPassword)
        throws ServiceException, ObjectNotFoundException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean removeClient(Integer clientID) throws ServiceException;

    public Integer getAdviserTypeCodeID() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setAdviserTypeCodeID(Integer value) throws ServiceException;

    public String getLoginName() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setLoginName(String value) throws ServiceException;

    public String getLoginPassword() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setLoginPassword(String value) throws ServiceException;

    public List findUsers(Properties criteria) throws ServiceException;

}