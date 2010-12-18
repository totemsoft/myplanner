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

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.bean.DbConstant;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.Comment;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Occupation;
import com.argus.financials.etc.PersonName;
import com.argus.financials.etc.Survey;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public interface PersonService extends DbConstant {

    // GLOBAL PLAN TEMPLATE
    public static final Integer TEMPLATE_PLAN = new Integer(1);

    // USER PLAN TEMPLATE
    public static final Integer TEMPLATE_PLAN_USER = new Integer(2);

    // CLIENT PLAN TEMPLATE
    public static final Integer TEMPLATE_PLAN_CLIENT = new Integer(3);

    PersonService create(Integer ownerPersonID, boolean store) throws ServiceException, CreateException;

    PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException,
        FinderException;

    public Integer create() throws ServiceException, CreateException;

    public Object getOwnerPrimaryKey() throws ServiceException;

    public Integer findByPrimaryKey(Integer personID) throws ServiceException,
            FinderException;

    public boolean isModifiedRemote() throws ServiceException;

    // store the Person details.
    public void storePerson() throws com.argus.financials.service.ServiceException;

    public void storeFinancials() throws com.argus.financials.service.ServiceException;

    public void storeComments() throws com.argus.financials.service.ServiceException;

    public void storeSurveys() throws com.argus.financials.service.ServiceException;

    public void storeModels() throws com.argus.financials.service.ServiceException;

    public void storeEmployerBusiness() throws com.argus.financials.service.ServiceException;

    public PersonName getPersonName() throws com.argus.financials.service.ServiceException;

    public void setPersonName(PersonName value) throws com.argus.financials.service.ServiceException;

    public Integer getDobCountryID() throws com.argus.financials.service.ServiceException;

    public void setDobCountryID(Integer value) throws com.argus.financials.service.ServiceException;

    public String getTaxFileNumber() throws com.argus.financials.service.ServiceException;

    public void setTaxFileNumber(String value) throws com.argus.financials.service.ServiceException;

    public Integer getPreferredLanguageID() throws com.argus.financials.service.ServiceException;

    public void setPreferredLanguageID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public String getPreferredLanguage() throws com.argus.financials.service.ServiceException;

    public void setPreferredLanguage(String value)
            throws com.argus.financials.service.ServiceException;

    public Integer getReferalSourceCodeID() throws com.argus.financials.service.ServiceException;

    public void setReferalSourceCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public Integer getResidenceStatusCodeID() throws com.argus.financials.service.ServiceException;

    public void setResidenceStatusCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public Integer getResidenceCountryCodeID() throws com.argus.financials.service.ServiceException;

    public void setResidenceCountryCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public java.util.Date getDateOfBirth() throws com.argus.financials.service.ServiceException;

    public void setDateOfBirth(java.util.Date value)
            throws com.argus.financials.service.ServiceException;

    public Boolean getIsSmoker() throws com.argus.financials.service.ServiceException;

    public void setIsSmoker(Boolean value) throws com.argus.financials.service.ServiceException;

    public boolean isDSSRecipient() throws com.argus.financials.service.ServiceException;

    public void setDSSRecipient(boolean value) throws com.argus.financials.service.ServiceException;

    public Integer getHealthStateCodeID() throws com.argus.financials.service.ServiceException;

    public void setHealthStateCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public boolean hasHospitalCover() throws com.argus.financials.service.ServiceException;

    public void hasHospitalCover(boolean value) throws com.argus.financials.service.ServiceException;

    public Integer getTrustStatusCodeID() throws com.argus.financials.service.ServiceException;

    public void setTrustStatusCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public Integer getDIYStatusCodeID() throws com.argus.financials.service.ServiceException;

    public void setDIYStatusCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public Integer getCompanyStatusCodeID() throws com.argus.financials.service.ServiceException;

    public void setCompanyStatusCodeID(Integer value)
            throws com.argus.financials.service.ServiceException;

    public String getTrustDIYStatusComment() throws com.argus.financials.service.ServiceException;

    public void setTrustDIYStatusComment(String value)
            throws com.argus.financials.service.ServiceException;

    /**
     * 
     */
    public Occupation getOccupation() throws com.argus.financials.service.ServiceException;

    public void setOccupation(Occupation value) throws com.argus.financials.service.ServiceException;

    /**
     * 
     */
    public BusinessService getEmployerBusiness() throws com.argus.financials.service.ServiceException;

    /**
     * 
     */
    public Address getResidentialAddress() throws com.argus.financials.service.ServiceException;

    public void setResidentialAddress(Address value)
            throws com.argus.financials.service.ServiceException;

    public Address getPostalAddress() throws com.argus.financials.service.ServiceException;

    public void setPostalAddress(Address value) throws com.argus.financials.service.ServiceException;

    /**
     * 
     */
    public java.util.TreeMap getDependents() throws com.argus.financials.service.ServiceException;

    public void setDependents(java.util.TreeMap value)
            throws com.argus.financials.service.ServiceException;

    public java.util.TreeMap getContacts() throws ServiceException;

    public void setContacts(java.util.TreeMap value) throws ServiceException;

    /**
     * objectTypeID = null, return ALL otherwise, return ONLY this type
     */
    public Map getFinancials(Integer objectTypeID)
            throws com.argus.financials.service.ServiceException;

    public void setFinancials(Integer objectTypeID, Map value)
            throws com.argus.financials.service.ServiceException;

    public Map getFinancials() throws com.argus.financials.service.ServiceException;

    public Financial getFinancial(Integer financialID)
            throws com.argus.financials.service.ServiceException;

    public java.util.Map getStrategyGroupFinancials(Integer strategyGroupID,
            boolean complex) throws com.argus.financials.service.ServiceException;

    public FinancialGoal getFinancialGoal() throws com.argus.financials.service.ServiceException;

    public void setFinancialGoal(FinancialGoal value)
            throws com.argus.financials.service.ServiceException;

    /**
     * contactMediaID = null, return ALL otherwise, return ONLY this type
     */
    public ContactMedia getContactMedia(Integer contactMediaCodeID)
            throws com.argus.financials.service.ServiceException;

    public HashMap getContactMedia(Boolean refresh)
            throws com.argus.financials.service.ServiceException;

    public void setContactMedia(ContactMedia contactMedia)
            throws com.argus.financials.service.ServiceException;

    public void setContactMedia(HashMap value) throws com.argus.financials.service.ServiceException;

    /**
     * get comment of link type PERSON$COMMENT_2_objectTypeID2 (most recent one)
     */
    public Comment getComment(Integer linkObjectTypeID)
            throws com.argus.financials.service.ServiceException;

    public void setComment(Integer linkObjectTypeID, Comment value)
            throws com.argus.financials.service.ServiceException;

    /**
     * SURVEY
     */
    public Survey getSurvey(Integer surveyID) throws com.argus.financials.service.ServiceException;

    public void setSurvey(Survey survey) throws com.argus.financials.service.ServiceException;

    // one of the linkID (e.g. SURVEY_2_RISKPROFILE)
    public Integer getSurveyID(int surveyTypeID)
            throws com.argus.financials.service.ServiceException, ObjectNotFoundException;

    /**
     * 
     */
    public ModelCollection getModels() throws com.argus.financials.service.ServiceException;

    public java.util.Vector getModels(Integer modelTypeID)
            throws com.argus.financials.service.ServiceException;

    public Model getModel(Integer modelTypeID, String title)
            throws com.argus.financials.service.ServiceException;

    public Model getModel(Integer modelTypeID, Integer modelID)
            throws com.argus.financials.service.ServiceException;

    public void addModel(Model value) throws com.argus.financials.service.ServiceException;

    public void removeModel(Model value) throws com.argus.financials.service.ServiceException;

    public java.util.Collection getPlans(Integer planTypeID)
            throws ServiceException;

    public int storePlan(com.argus.util.ReferenceCode plan, Integer planTypeID)
            throws ServiceException;

    public boolean deletePlan(com.argus.util.ReferenceCode plan,
            Integer planTypeID) throws ServiceException;

    public void remove() throws com.argus.financials.service.ServiceException, RemoveException;

    public Object getPrimaryKey();

}