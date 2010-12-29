/*
 * PersonService.java Created on 24 July 2000, 10:32
 */

package com.argus.financials.domain;

import java.util.HashMap;
import java.util.Map;

import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.Comment;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Occupation;
import com.argus.financials.etc.PersonName;
import com.argus.financials.etc.Survey;
import com.argus.financials.service.BusinessService;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.financials.service.ServiceException;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IPerson
{

    public Object getPrimaryKey();

    public PersonName getPersonName() throws ServiceException;

    public void setPersonName(PersonName value) throws ServiceException;

    public Integer getDobCountryID() throws ServiceException;

    public void setDobCountryID(Integer value) throws ServiceException;

    public String getTaxFileNumber() throws ServiceException;

    public void setTaxFileNumber(String value) throws ServiceException;

    public Integer getPreferredLanguageID() throws ServiceException;

    public void setPreferredLanguageID(Integer value) throws ServiceException;

    public String getPreferredLanguage() throws ServiceException;

    public void setPreferredLanguage(String value) throws ServiceException;

    public Integer getReferalSourceCodeID() throws ServiceException;

    public void setReferalSourceCodeID(Integer value) throws ServiceException;

    public Integer getResidenceStatusCodeID() throws ServiceException;

    public void setResidenceStatusCodeID(Integer value) throws ServiceException;

    public Integer getResidenceCountryCodeID() throws ServiceException;

    public void setResidenceCountryCodeID(Integer value) throws ServiceException;

    public java.util.Date getDateOfBirth() throws ServiceException;

    public void setDateOfBirth(java.util.Date value) throws ServiceException;

    public Boolean getIsSmoker() throws ServiceException;

    public void setIsSmoker(Boolean value) throws ServiceException;

    public boolean isDSSRecipient() throws ServiceException;

    public void setDSSRecipient(boolean value) throws ServiceException;

    public Integer getHealthStateCodeID() throws ServiceException;

    public void setHealthStateCodeID(Integer value) throws ServiceException;

    public boolean hasHospitalCover() throws ServiceException;

    public void hasHospitalCover(boolean value) throws ServiceException;

    public Integer getTrustStatusCodeID() throws ServiceException;

    public void setTrustStatusCodeID(Integer value) throws ServiceException;

    public Integer getDIYStatusCodeID() throws ServiceException;

    public void setDIYStatusCodeID(Integer value) throws ServiceException;

    public Integer getCompanyStatusCodeID() throws ServiceException;

    public void setCompanyStatusCodeID(Integer value) throws ServiceException;

    public String getTrustDIYStatusComment() throws ServiceException;

    public void setTrustDIYStatusComment(String value) throws ServiceException;

    /**
     * 
     */
    public Occupation getOccupation() throws ServiceException;

    public void setOccupation(Occupation value) throws ServiceException;

    /**
     * 
     */
    public BusinessService getEmployerBusiness() throws ServiceException;

    /**
     * 
     */
    public Address getResidentialAddress() throws ServiceException;

    public void setResidentialAddress(Address value) throws ServiceException;

    public Address getPostalAddress() throws ServiceException;

    public void setPostalAddress(Address value) throws ServiceException;

    /**
     * 
     */
    public java.util.TreeMap getDependents() throws ServiceException;

    public void setDependents(java.util.TreeMap value) throws ServiceException;

    public java.util.TreeMap getContacts() throws ServiceException;

    public void setContacts(java.util.TreeMap value) throws ServiceException;

    /**
     * objectTypeID = null, return ALL otherwise, return ONLY this type
     */
    public Map getFinancials(Integer objectTypeID) throws ServiceException;

    public void setFinancials(Integer objectTypeID, Map value) throws ServiceException;

    public Map getFinancials() throws ServiceException;

    public Financial getFinancial(Integer financialID) throws ServiceException;

    public java.util.Map getStrategyGroupFinancials(Integer strategyGroupID, boolean complex)
        throws ServiceException;

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

}