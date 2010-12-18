/*
 * UserService.java
 *
 * Created on 3 August 2001, 14:41
 */

package com.argus.financials.service;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Vector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface UserService extends PersonService {

    public static final String ALL_USERS_CLIENTS = "ALL_USERS_CLIENTS";

    public static final String ADVISORID = "ADVISORID";

    public static final String TRIALID = "trial"; // asasas

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    PersonService create(Integer ownerPersonID, boolean store) throws ServiceException, CreateException;

    PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException,
        FinderException;

    UserService findByLoginNamePassword(String loginName, String loginPassword)
        throws ServiceException, ObjectNotFoundException;

    public java.util.Vector findClients(java.util.HashMap selectionCriteria)
            throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean removeClient(Integer clientID)
            throws com.argus.financials.service.ServiceException;

    public Integer getAdviserTypeCodeID() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setAdviserTypeCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public String getLoginName() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setLoginName(String value) throws com.argus.financials.service.ServiceException;

    public String getLoginPassword() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setLoginPassword(String value) throws com.argus.financials.service.ServiceException;

    public java.util.List findUsers(java.util.Properties selectionCriteria)
            throws com.argus.financials.service.ServiceException;

    public void downloadData(String webUser, String webPassword, Vector tables,
            java.awt.Window owner) throws ServiceException;

}