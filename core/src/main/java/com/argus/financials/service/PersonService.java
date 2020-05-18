/*
 * PersonService.java Created on 24 July 2000, 10:32
 */

package com.argus.financials.service;

/**
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.RemoveException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.etc.AddressDto;
import com.argus.financials.etc.Comment;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Survey;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.util.ReferenceCode;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface PersonService
{

    PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException, FinderException;

    public Object getOwnerPrimaryKey() throws ServiceException;

    public Integer findByPrimaryKey(Integer personID) throws ServiceException, FinderException;

    public boolean isModifiedRemote() throws ServiceException;

    public Integer getId();
    void setId(Integer value);

    public IPerson getPersonName();
    public void setPersonName(IPerson value);

    public IBusiness getEmployerBusiness() throws ServiceException;

    public AddressDto getResidentialAddress() throws ServiceException;
    public void setResidentialAddress(AddressDto value) throws ServiceException;

    public AddressDto getPostalAddress() throws ServiceException;
    public void setPostalAddress(AddressDto value) throws ServiceException;

    /**
     * 
     */
    public TreeMap getDependents() throws ServiceException;
    public void setDependents(TreeMap value) throws ServiceException;

    public TreeMap getContacts() throws ServiceException;
    public void setContacts(TreeMap value) throws ServiceException;

    /**
     * objectTypeID = null, return ALL otherwise, return ONLY this type
     */
    public Map getFinancials(Integer objectTypeID) throws ServiceException;
    public Map findFinancials() throws ServiceException;
    public Financial getFinancial(Integer financialID) throws ServiceException;

    public Map getStrategyGroupFinancials(Integer strategyGroupID, boolean complex) throws ServiceException;

    public FinancialGoal getFinancialGoal() throws ServiceException;
    public void setFinancialGoal(FinancialGoal value) throws ServiceException;

    /**
     * contactMediaID = null, return ALL otherwise, return ONLY this type
     */
    public ContactMedia getContactMedia(Integer contactMediaCodeID) throws ServiceException;
    public HashMap getContactMedia(Boolean refresh) throws ServiceException;
    public void setContactMedia(ContactMedia contactMedia) throws ServiceException;
    public void setContactMedia(HashMap value) throws ServiceException;

    /**
     * get comment of link type PERSON$COMMENT_2_objectTypeID2 (most recent one)
     */
    public Comment getComment(Integer linkObjectTypeID) throws ServiceException;
    public void setComment(Integer linkObjectTypeID, Comment value) throws ServiceException;

    /**
     * SURVEY
     */
    public Survey getSurvey(Integer surveyID) throws ServiceException;
    public void setSurvey(Survey survey) throws ServiceException;
    // one of the linkID (e.g. SURVEY_2_RISKPROFILE)
    public Integer getSurveyID(int surveyTypeID) throws ServiceException, ObjectNotFoundException;

    // store the Person details.
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storePerson() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeFinancials(Map financials, FinancialGoal financialGoal) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeComments() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeSurveys() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeModels() throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void storeEmployerBusiness() throws ServiceException;

    public ModelCollection getModels() throws ServiceException;
    public Vector getModels(Integer modelTypeID) throws ServiceException;
    public Model getModel(Integer modelTypeID, String title) throws ServiceException;
    public Model getModel(Integer modelTypeID, Integer modelID) throws ServiceException;
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addModel(Model value) throws ServiceException;
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeModel(Model value) throws ServiceException;

    public Collection getPlans(Integer planTypeID) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int storePlan(ReferenceCode plan, Integer planTypeID) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deletePlan(ReferenceCode plan, Integer planTypeID) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void remove() throws ServiceException, RemoveException;

}