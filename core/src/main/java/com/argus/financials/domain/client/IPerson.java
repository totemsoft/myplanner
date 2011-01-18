/*
 * PersonService.java Created on 24 July 2000, 10:32
 */

package com.argus.financials.domain.client;

import java.util.Date;

import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.MaritalCode;
import com.argus.financials.domain.hibernate.refdata.SexCode;
import com.argus.financials.domain.hibernate.refdata.TitleCode;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IPerson extends IBase<Long>
{
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

    Date getDateOfBirth();

    void setDateOfBirth(Date dateOfBirth);

    Country getDobCountry();

    void setDobCountry(Country dobCountry);

    Country getResidenceCountry();

    void setResidenceCountry(Country residenceCountry);

    TitleCode getTitle();

    void setTitle(TitleCode title);

    SexCode getSex();

    void setSex(SexCode sex);

    MaritalCode getMarital();

    void setMarital(MaritalCode marital);
    
    String getTaxFileNumber();

    void setTaxFileNumber(String taxFileNumber);

    Boolean getDssRecipient();

    void setDssRecipient(Boolean dssRecipient);

    Integer getSupportToAge();

    void setSupportToAge(Integer supportToAge);

}