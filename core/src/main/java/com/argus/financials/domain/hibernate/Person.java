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
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.MaritalCode;
import com.argus.financials.domain.hibernate.refdata.SexCode;
import com.argus.financials.domain.hibernate.refdata.TitleCode;

@Entity
@Table(name = "Person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends AbstractAuditable<Long> implements IPerson
{
    /** serialVersionUID */
    private static final long serialVersionUID = -8960024744697095377L;

    @Id
    @Column(name = "PersonId", nullable = false)
    @Type(type = "com.argus.financials.domain.hibernate.LongType")
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOBCountryID")
    private Country dobCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ResidenceCountryCodeID")
    private Country residenceCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TitleCodeID")
    private TitleCode title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SexCodeID")
    private SexCode sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaritalCodeID")
    private MaritalCode marital;

    @Column(name = "TaxFileNumber")
    private String taxFileNumber;

    @Column(name = "DSSRecipient")
    @Type(type = "yes_no")
    private Boolean dssRecipient;

    @Column(name = "SupportToAge")
    private Integer supportToAge;

//  [ResidenceStatusCodeID] [int] NULL,
//  [PreferredLanguageID] [int] NULL,
//  [PreferredLanguage] [varchar](20) NULL,
//  [ReferalSourceCodeID] [int] NULL,
    
    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
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
    public Country getDobCountry()
    {
        return dobCountry;
    }

    /**
     * @param dobCountry the dobCountry to set
     */
    public void setDobCountry(Country dobCountry)
    {
        this.dobCountry = dobCountry;
    }

    /**
     * @return the residenceCountry
     */
    public Country getResidenceCountry()
    {
        return residenceCountry;
    }

    /**
     * @param residenceCountry the residenceCountry to set
     */
    public void setResidenceCountry(Country residenceCountry)
    {
        this.residenceCountry = residenceCountry;
    }

    /**
     * @return the title
     */
    public TitleCode getTitle()
    {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(TitleCode title)
    {
        this.title = title;
    }

    /**
     * @return the sex
     */
    public SexCode getSex()
    {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(SexCode sex)
    {
        this.sex = sex;
    }

    /**
     * @return the marital
     */
    public MaritalCode getMarital()
    {
        return marital;
    }

    /**
     * @param marital the marital to set
     */
    public void setMarital(MaritalCode marital)
    {
        this.marital = marital;
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