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

import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.IStrategyGroup;
import au.com.totemsoft.myplanner.domain.dto.ClientDto;

@Service
public interface ClientService extends PersonService {

    Long createClient() throws ServiceException, CreateException;

    void saveClient(ClientDto client);

    public void setOwnerPrimaryKey(Object value) throws ServiceException;

    public boolean validatePassword(String password) throws ServiceException;

    public boolean isActive() throws ServiceException;

    public void setActive(boolean value) throws ServiceException;

    public Date getFeeDate() throws ServiceException;

    public void setFeeDate(Date value) throws ServiceException;

    public Date getReviewDate() throws ServiceException;

    public void setReviewDate(Date value) throws ServiceException;

    public PersonService getPartner(boolean create) throws ServiceException;

    public Collection getStrategies() throws ServiceException;

    public void storeStrategy(IStrategyGroup strategy) throws ServiceException;

    public void deleteStrategy(IStrategyGroup strategy) throws ServiceException;

    public void implementStrategy(IStrategyGroup strategy) throws ServiceException;

    public void rollbackStrategy(IStrategyGroup strategy) throws ServiceException;

    public Collection getCategories() throws ServiceException;

    public Collection getSelectedCategories() throws ServiceException;

    public void addCategory(au.com.totemsoft.util.ReferenceCode category) throws ServiceException;

    public void updateCategory(au.com.totemsoft.util.ReferenceCode category) throws ServiceException;

    public boolean removeCategory(au.com.totemsoft.util.ReferenceCode category) throws ServiceException;

    public void addSelectedCategories(Vector selectedCategories) throws ServiceException;

}
