/*
 * PersonService.java Created on 24 July 2000, 10:32
 */

package com.argus.financials.domain;

import java.util.Date;

import com.argus.financials.domain.refdata.ICountry;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IPerson
{
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

    Date getDateOfBirth();

    void setDateOfBirth(Date dateOfBirth);

    ICountry getDobCountry();

    void setDobCountry(ICountry dobCountry);

    ICountry getResidenceCountry();

    void setResidenceCountry(ICountry residenceCountry);

    String getTaxFileNumber();

    void setTaxFileNumber(String taxFileNumber);

    Boolean getDssRecipient();

    void setDssRecipient(Boolean dssRecipient);

    Integer getSupportToAge();

    void setSupportToAge(Integer supportToAge);

}