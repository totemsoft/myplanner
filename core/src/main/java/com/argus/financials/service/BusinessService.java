/*
 * BusinessService.java
 *
 * Created on 22 January 2002, 11:59
 */

package com.argus.financials.service;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.bean.DbConstant;
import com.argus.financials.code.InvalidCodeException;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public interface BusinessService extends DbConstant {

    public Integer getParentBusinessID() throws com.argus.financials.service.ServiceException;

    // public void setParentBusinessID( Integer value )
    // throws com.argus.financials.service.ServiceException;

    public Integer getIndustryCodeID() throws com.argus.financials.service.ServiceException;

    public void setIndustryCodeID(Integer value)
            throws com.argus.financials.service.ServiceException, InvalidCodeException;

    public Integer getBusinessStructureCodeID() throws com.argus.financials.service.ServiceException;

    public void setBusinessStructureCodeID(Integer value)
            throws com.argus.financials.service.ServiceException, InvalidCodeException;

    public String getLegalName() throws com.argus.financials.service.ServiceException;

    public void setLegalName(String value) throws com.argus.financials.service.ServiceException;

    public String getTradingName() throws com.argus.financials.service.ServiceException;

    public void setTradingName(String value) throws com.argus.financials.service.ServiceException;

    public String getBusinessNumber() throws com.argus.financials.service.ServiceException;

    public void setBusinessNumber(String value) throws com.argus.financials.service.ServiceException;

    public String getTaxFileNumber() throws com.argus.financials.service.ServiceException;

    public void setTaxFileNumber(String value) throws com.argus.financials.service.ServiceException;

    public String getWebSiteName() throws com.argus.financials.service.ServiceException;

    public void setWebSiteName(String value) throws com.argus.financials.service.ServiceException;

    public void remove() throws com.argus.financials.service.ServiceException, RemoveException;

    public Object getPrimaryKey();

}
