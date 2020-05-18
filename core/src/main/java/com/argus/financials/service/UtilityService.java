/*
 * UtilityService.java
 *
 * Created on 16 August 2001, 18:32
 */

package com.argus.financials.service;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.api.ServiceException;
import com.argus.financials.etc.Contact;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface UtilityService {

    //String REQUIRED_DBVERSION = "FPS.00.00";
    String REQUIRED_DBVERSION = "FPS.02.00"; //MsSql

    // Parameter Type IDs
    Integer PARAM_INVESTMENT_STRATEGY = new Integer(1);


    public List<Contact> findUsers() throws ServiceException;

    
    public java.util.TreeMap getCodes(String tableName) throws ServiceException;

    public java.util.TreeMap getCodes(String tableName, String fieldKeyValues,
            String fieldKey) throws ServiceException;

    public java.util.Map getPostCodes(Integer countryCodeID)
            throws ServiceException;

    /**
     * CONTAINER structure Map( objType, Map( ReferenceCode( finTypeID,
     * finTypeDesc ), Vector( ReferenceCode( finCodeID, finCode, finCodeDesc ) ) ) )
     */
    public java.util.Map getFinancialObjectTypes() throws ServiceException;

    public java.util.HashMap getLifeExpectancy(Integer countryCodeID)
            throws ServiceException;

    public java.util.HashMap getParameters(Integer paramTypeID)
            throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Integer addCode(String tableName, String codeValue)
            throws ServiceException,
            com.argus.financials.api.InvalidCodeException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Integer addCode(String tableName, String idFieldName,
            String descFieldName, String codeValue) throws ServiceException,
            com.argus.financials.api.InvalidCodeException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addCode(String tableName, java.util.Map addData)
            throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateCode(String tableName, java.util.Map updateData,
            java.util.Map whereData) throws ServiceException;

    /**
     * 
     */
    public String getDBServerVersion() throws ServiceException;

    public String getDBVersion() throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public void syncDBSchema() throws Exception;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public void syncDBSchema(String curr, String req) throws Exception;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    int syncDBSchema(Connection con, int i, String update) throws Exception;
    
}
