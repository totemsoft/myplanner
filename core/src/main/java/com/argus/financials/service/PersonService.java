/*
 * PersonService.java
 *
 * Created on 24 July 2000, 10:32
 */

package com.argus.financials.service;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.Vector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.bean.DbConstant;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface PersonService extends IPerson, DbConstant {

    // GLOBAL PLAN TEMPLATE
    public static final Integer TEMPLATE_PLAN = new Integer(1);

    // USER PLAN TEMPLATE
    public static final Integer TEMPLATE_PLAN_USER = new Integer(2);

    // CLIENT PLAN TEMPLATE
    public static final Integer TEMPLATE_PLAN_CLIENT = new Integer(3);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    PersonService create(Integer ownerPersonID, boolean store) throws ServiceException, CreateException;

    PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException,
        FinderException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Integer create() throws ServiceException, CreateException;

    public Object getOwnerPrimaryKey() throws ServiceException;

    public Integer findByPrimaryKey(Integer personID) throws ServiceException,
            FinderException;

    public boolean isModifiedRemote() throws ServiceException;

    // store the Person details.
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storePerson() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeFinancials() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeComments() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeSurveys() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeModels() throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeEmployerBusiness() throws com.argus.financials.service.ServiceException;

    /**
     * 
     */
    public ModelCollection getModels() throws com.argus.financials.service.ServiceException;

    public Vector getModels(Integer modelTypeID)
            throws com.argus.financials.service.ServiceException;

    public Model getModel(Integer modelTypeID, String title)
            throws com.argus.financials.service.ServiceException;

    public Model getModel(Integer modelTypeID, Integer modelID)
            throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addModel(Model value) throws com.argus.financials.service.ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeModel(Model value) throws com.argus.financials.service.ServiceException;

    public Collection getPlans(Integer planTypeID)
            throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int storePlan(com.argus.util.ReferenceCode plan, Integer planTypeID)
            throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deletePlan(com.argus.util.ReferenceCode plan,
            Integer planTypeID) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void remove() throws com.argus.financials.service.ServiceException, RemoveException;

}