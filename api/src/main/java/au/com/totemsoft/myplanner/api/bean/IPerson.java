/*
 * PersonService.java Created on 24 July 2000, 10:32
 */

package au.com.totemsoft.myplanner.api.bean;

import java.util.Date;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface IPerson extends IBase<Long>
{

    // used in search person
    String SURNAME = "FamilyName";
    String FIRST_NAME = "FirstName";
    String DATE_OF_BIRTH = "DateOfBirth";

// TODO: add these...
//  [SexCodeID] [int] NULL,
//  [TitleCodeID] [int] NULL,
//  [MaritalCodeID] [int] NULL,
//  [ResidenceStatusCodeID] [int] NULL,
//  [PreferredLanguageID] [int] NULL,
//  [PreferredLanguage] [varchar](20) NULL,
//  [ReferalSourceCodeID] [int] NULL,
    
    String getFirstname();
    void setFirstname(String firstname);

    String getSurname();
    void setSurname(String surname);

    String getOtherNames();
    void setOtherNames(String otherNames);

    String getPreferredName();
    void setPreferredName(String preferredName);

    String getFullName();
    String getShortName();
    
    Date getDateOfBirth();
    void setDateOfBirth(Date dateOfBirth);

    ICountry getDobCountry();
    void setDobCountry(ICountry dobCountry);

    ICountry getResidenceCountry();
    void setResidenceCountry(ICountry residenceCountry);

    ITitleCode getTitle();
    void setTitle(ITitleCode title);

    ISexCode getSex();
    void setSex(ISexCode sex);

    IMaritalCode getMarital();
    void setMarital(IMaritalCode marital);
    boolean isMarried();

    IPersonHealth getPersonHealth();
    void setPersonHealth(IPersonHealth personHealth);

    IPersonTrustDIYStatus getPersonTrustDIYStatus();
    void setPersonTrustDIYStatus(IPersonTrustDIYStatus personTrustDIYStatus);

    IOccupation getOccupation();
    void setOccupation(IOccupation occupation);

    ILanguage getPreferredLanguage();
    void setPreferredLanguage(ILanguage preferredLanguage);

    Integer getReferalSourceCodeId();
    void setReferalSourceCodeId(Integer referalSourceCodeId);

    Integer getResidenceStatusCodeId();
    void setResidenceStatusCodeId(Integer residenceStatusCodeId);

    String getTaxFileNumber();
    void setTaxFileNumber(String taxFileNumber);

    Boolean getDssRecipient();
    void setDssRecipient(Boolean dssRecipient);
    boolean isDssRecipient();

    Integer getSupportToAge();
    void setSupportToAge(Integer supportToAge);
    Number getAge();

}