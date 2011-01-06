package com.argus.financials.domain.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.argus.financials.domain.client.IPerson;
import com.argus.financials.domain.client.refdata.ICountry;
import com.argus.financials.domain.hibernate.refdata.Country;

@Entity
@Table(name = "Person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends AbstractAuditable<Integer> implements IPerson
{
    /** serialVersionUID */
    private static final long serialVersionUID = -8960024744697095377L;

    @Id
    @Column(name = "PersonId", nullable = false)
    private Integer id;

    @Column(name = "FirstName")
    private String firstname;

    @Column(name = "FamilyName")
    private String surname;

    @Column(name = "OtherGivenNames")
    private String otherNames;

    @Column(name = "PreferredName")
    private String preferredName;

    @Column(name = "DateOfBirth")
    private Date dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Country.class)
    @JoinColumn(name = "DOBCountryID")
    private ICountry dobCountry;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Country.class)
    @JoinColumn(name = "ResidenceCountryCodeID")
    private ICountry residenceCountry;

//  [SexCodeID] [int] NULL,
//  [TitleCodeID] [int] NULL,
//  [MaritalCodeID] [int] NULL,
//  [ResidenceStatusCodeID] [int] NULL,

    @Column(name = "TaxFileNumber")
    private String taxFileNumber;

    @Column(name = "DSSRecipient")
    @Type(type = "yes_no")
    private Boolean dssRecipient;

    @Column(name = "SupportToAge")
    private Integer supportToAge;

//  [PreferredLanguageID] [int] NULL,
//  [PreferredLanguage] [varchar](20) NULL,
//  [ReferalSourceCodeID] [int] NULL,
    
    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the firstname
     */
    public String getFirstname()
    {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    /**
     * @return the surname
     */
    public String getSurname()
    {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    /**
     * @return the otherNames
     */
    public String getOtherNames()
    {
        return otherNames;
    }

    /**
     * @param otherNames the otherNames to set
     */
    public void setOtherNames(String otherNames)
    {
        this.otherNames = otherNames;
    }

    /**
     * @return the preferredName
     */
    public String getPreferredName()
    {
        return preferredName;
    }

    /**
     * @param preferredName the preferredName to set
     */
    public void setPreferredName(String preferredName)
    {
        this.preferredName = preferredName;
    }

    /**
     * Calculated
     * @return
     */
    public String getShortName()
    {
        return surname + (firstname == null ? "" : ", " + firstname);
    }

    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the dobCountry
     */
    public ICountry getDobCountry()
    {
        return dobCountry;
    }

    /**
     * @param dobCountry the dobCountry to set
     */
    public void setDobCountry(ICountry dobCountry)
    {
        this.dobCountry = dobCountry;
    }

    /**
     * @return the residenceCountry
     */
    public ICountry getResidenceCountry()
    {
        return residenceCountry;
    }

    /**
     * @param residenceCountry the residenceCountry to set
     */
    public void setResidenceCountry(ICountry residenceCountry)
    {
        this.residenceCountry = residenceCountry;
    }

    /**
     * @return the taxFileNumber
     */
    public String getTaxFileNumber()
    {
        return taxFileNumber;
    }

    /**
     * @param taxFileNumber the taxFileNumber to set
     */
    public void setTaxFileNumber(String taxFileNumber)
    {
        this.taxFileNumber = taxFileNumber;
    }

    /**
     * @return the dssRecipient
     */
    public Boolean getDssRecipient()
    {
        return dssRecipient;
    }

    /**
     * @param dssRecipient the dssRecipient to set
     */
    public void setDssRecipient(Boolean dssRecipient)
    {
        this.dssRecipient = dssRecipient;
    }

    /**
     * @return the supportToAge
     */
    public Integer getSupportToAge()
    {
        return supportToAge;
    }

    /**
     * @param supportToAge the supportToAge to set
     */
    public void setSupportToAge(Integer supportToAge)
    {
        this.supportToAge = supportToAge;
    }

}