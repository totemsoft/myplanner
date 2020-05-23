/*
 * BusinessServiceImpl.java
 *
 * Created on 22 January 2002, 12:03
 */

package au.com.totemsoft.myplanner.service.impl;

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

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.IBusiness;
import au.com.totemsoft.myplanner.api.service.BusinessService;
import au.com.totemsoft.myplanner.dao.BusinessDao;
import au.com.totemsoft.myplanner.service.FinderException;

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