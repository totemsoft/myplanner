/*
 * BusinessService.java
 *
 * Created on 22 January 2002, 11:59
 */

package com.argus.financials.api.service;

import java.sql.Connection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import org.springframework.stereotype.Service;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IBusiness;

@Service
public interface BusinessService {

    IBusiness load(Integer businessId) throws ObjectNotFoundException, ServiceException;

    Integer store(IBusiness business, Connection con) throws ServiceException;

    Integer find(IBusiness business) throws ServiceException;

}