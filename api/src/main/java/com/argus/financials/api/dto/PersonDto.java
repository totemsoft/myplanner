package com.argus.financials.api.dto;

import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.ILanguage;
import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.api.bean.IPersonTrustDIYStatus;
import com.argus.financials.api.bean.PersonName;

public class PersonDto extends PersonName implements IPerson {

    private Long id;

    private ICountry dobCountry;

    private ILanguage preferredLanguage;

    private Integer referalSourceCodeId;

    private Integer residenceStatusCodeId;

    private ICountry residenceCountry;

    private IPersonHealth personHealth;

    private IPersonTrustDIYStatus personTrustDIYStatus;

    private IOccupation occupation;

    private String taxFileNumber;

    private Boolean dssRecipient;

    private Integer supportToAge;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the dobCountry
     */
    public ICountry getDobCountry() {
        return dobCountry;
    }

    public void setDobCountry(ICountry dobCountry) {
        this.dobCountry = dobCountry;
        modified = true;
    }

    /**
     * @return the personHealth
     */
    public IPersonHealth getPersonHealth() {
//        if (personHealth == null) {
//            personHealth = new PersonHealth();
//        }
        return personHealth;
    }

    public void setPersonHealth(IPersonHealth personHealth) {
        this.personHealth = personHealth;
    }

    /**
     * @return the personTrustDIYStatus
     */
    public IPersonTrustDIYStatus getPersonTrustDIYStatus() {
//        if (personTrustDIYStatus == null) {
//            personTrustDIYStatus = new PersonTrustDIYStatus();
//        }
        return personTrustDIYStatus;
    }

    public void setPersonTrustDIYStatus(IPersonTrustDIYStatus personTrustDIYStatus) {
        this.personTrustDIYStatus = personTrustDIYStatus;
    }

    /**
     * @return the occupation
     */
    public IOccupation getOccupation() {
//        if (occupation == null) {
//            occupation = new Occupation();
//        }
        return occupation;
    }

    public void setOccupation(IOccupation occupation) {
        this.occupation = occupation;
    }

    /**
     * @return the preferredLanguage
     */
    public ILanguage getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(ILanguage preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    /**
     * @return the referalSourceCodeId
     */
    public Integer getReferalSourceCodeId() {
        return referalSourceCodeId;
    }

    public void setReferalSourceCodeId(Integer referalSourceCodeId) {
        this.referalSourceCodeId = referalSourceCodeId;
    }

    /**
     * @return the residenceStatusCodeId
     */
    public Integer getResidenceStatusCodeId() {
        return residenceStatusCodeId;
    }

    public void setResidenceStatusCodeId(Integer residenceStatusCodeId) {
        this.residenceStatusCodeId = residenceStatusCodeId;
    }

    /**
     * @return the residenceCountry
     */
    public ICountry getResidenceCountry() {
        return residenceCountry;
    }

    public void setResidenceCountry(ICountry residenceCountry) {
        this.residenceCountry = residenceCountry;
        modified = true;
    }

    /**
     * @return the taxFileNumber
     */
    public String getTaxFileNumber() {
        return taxFileNumber;
    }

    public void setTaxFileNumber(String taxFileNumber) {
        this.taxFileNumber = taxFileNumber;
        modified = true;
    }

    /**
     * @return the dssRecipient
     */
    public Boolean getDssRecipient() {
        return dssRecipient;
    }

    public void setDssRecipient(Boolean dssRecipient) {
        this.dssRecipient = dssRecipient;
        modified = true;
    }

    public boolean isDssRecipient() {
        return Boolean.TRUE.equals(dssRecipient);
    }

    /**
     * @return the supportToAge
     */
    public Integer getSupportToAge() {
        return supportToAge;
    }

    public void setSupportToAge(Integer supportToAge) {
        this.supportToAge = supportToAge;
        modified = true;
    }

}