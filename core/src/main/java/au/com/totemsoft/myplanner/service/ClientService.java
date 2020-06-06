/*
 * ClientService.java Created on 24 July 2001, 13:10
 */

package au.com.totemsoft.myplanner.service;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.IStrategyGroup;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface ClientService extends PersonService
{

    Integer create(Integer ownerId) throws ServiceException, CreateException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setOwnerPrimaryKey(Object value) throws ServiceException;

    public boolean validatePassword(String password) throws ServiceException;

    public boolean isActive() throws ServiceException;

    public void setActive(boolean value) throws ServiceException;

    public Date getFeeDate() throws ServiceException;

    public void setFeeDate(Date value) throws ServiceException;

    public Date getReviewDate() throws ServiceException;

    public void setReviewDate(Date value) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PersonService getPartner(boolean create) throws ServiceException;

    public Collection getStrategies() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeStrategy(IStrategyGroup strategy) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteStrategy(IStrategyGroup strategy) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void implementStrategy(IStrategyGroup strategy) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void rollbackStrategy(IStrategyGroup strategy) throws ServiceException;

    public Collection getCategories() throws ServiceException;

    public Collection getSelectedCategories() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addCategory(au.com.totemsoft.util.ReferenceCode category) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateCategory(au.com.totemsoft.util.ReferenceCode category) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean removeCategory(au.com.totemsoft.util.ReferenceCode category) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addSelectedCategories(Vector selectedCategories) throws ServiceException;

}