/*
 * ClientService.java Created on 24 July 2001, 13:10
 */

package com.argus.financials.service;

/**
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.service.client.ServiceException;
import com.argus.financials.strategy.StrategyGroup;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface ClientService extends PersonService
{

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setOwnerPrimaryKey(Object value) throws ServiceException;

    public boolean validatePassword(String password) throws ServiceException;

    public boolean isActive() throws ServiceException;

    public void setActive(boolean value) throws ServiceException;

    public java.util.Date getFeeDate() throws ServiceException;

    public void setFeeDate(java.util.Date value) throws ServiceException;

    public java.util.Date getReviewDate() throws ServiceException;

    public void setReviewDate(java.util.Date value) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PersonService getPartner(boolean create) throws ServiceException;

    public Collection getStrategies() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeStrategy(StrategyGroup strategy) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteStrategy(StrategyGroup strategy) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void implementStrategy(StrategyGroup strategy) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void rollbackStrategy(StrategyGroup strategy) throws ServiceException;

    public java.util.Collection getCategories() throws ServiceException;

    public java.util.Collection getSelectedCategories() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addCategory(com.argus.util.ReferenceCode category) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateCategory(com.argus.util.ReferenceCode category) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean removeCategory(com.argus.util.ReferenceCode category) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addSelectedCategories(java.util.Vector selectedCategories) throws ServiceException;

}