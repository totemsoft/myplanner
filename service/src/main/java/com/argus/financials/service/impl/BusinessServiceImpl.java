/*
 * BusinessServiceImpl.java
 *
 * Created on 22 January 2002, 12:03
 */

package com.argus.financials.service.impl;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.api.service.BusinessService;
import com.argus.financials.dao.BusinessDao;
import com.argus.financials.service.FinderException;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BusinessServiceImpl implements BusinessService {

    //private static final Logger LOG = Logger.getLogger(BusinessServiceImpl.class);

    @Autowired private BusinessDao businessDao;

    public void findByPrimaryKey(Object primaryKey) throws ServiceException, FinderException {
        try {
            load((Integer) primaryKey);
        } catch (ObjectNotFoundException e) {
            throw new FinderException(e.getMessage());
        }
    }

    /**
     * 
     */
    public IBusiness load(Integer businessId) throws ObjectNotFoundException, ServiceException {
        try {
            return businessDao.load(businessId);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    public Integer store(IBusiness business, Connection con) throws ServiceException {
        try {
            return businessDao.store(business, con);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    public Integer find(IBusiness business) throws ServiceException {
        try {
            return businessDao.find(business);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

}