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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.ILanguage;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.api.bean.IPersonTrustDIYStatus;
import com.argus.financials.api.bean.ISexCode;
import com.argus.financials.api.bean.ITitleCode;
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.MaritalCode;
import com.argus.financials.domain.hibernate.refdata.SexCode;
import com.argus.financials.domain.hibernate.refdata.TitleCode;
import com.argus.financials.etc.db.PersonHealth;
import com.argus.financials.etc.db.PersonTrustDIYStatus;
import com.argus.util.DateTimeUtils;

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

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Country.class)
    @JoinColumn(name = "DOBCountryID")
    private ICountry dobCountry;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Country.class)
    @JoinColumn(name = "ResidenceCountryCodeID")
    private ICountry residenceCountry;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TitleCode.class)
    @JoinColumn(name = "TitleCodeID")
    private ITitleCode title;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SexCode.class)
    @JoinColumn(name = "SexCodeID")
    private ISexCode sex;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MaritalCode.class)
    @JoinColumn(name = "MaritalCodeID")
    private IMaritalCode marital;

    //@ManyToOne(fetch = FetchType.LAZY, targetEntity = PersonHealth.class)
    //@JoinColumn(name = "PersonID")
    @Transient
    private IPersonHealth personHealth;

    //@ManyToOne(fetch = FetchType.LAZY, targetEntity = PersonTrustDIYStatus.class)
    //@JoinColumn(name = "PersonID")
    @Transient
    private IPersonTrustDIYStatus personTrustDIYStatus;

    //@ManyToOne(fetch = FetchType.LAZY, targetEntity = Occupation.class)
    //@JoinColumn(name = "PersonID")
    @Transient
    private IOccupation occupation;

    //@ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    //@JoinColumn(name = "PreferredLanguageID", nullable = true)
    @Transient
    private ILanguage preferredLanguage;

    @Column(name = "ReferalSourceCodeID", nullable = true)
    private Integer referalSourceCodeId;

    @Column(name = "ResidenceStatusCodeID", nullable = true)
    private Integer residenceStatusCodeId;

    @Column(name = "TaxFileNumber")
    private String taxFileNumber;

    @Column(name = "DSSRecipient")
    @Type(type = "yes_no")
    private Boolean dssRecipient;

    @Column(name = "SupportToAge")
    private Integer supportToAge;

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Long getId()
    {
        return id;
    }

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

    public void setPreferredName(String preferredName)
    {
        this.preferredName = preferredName;
    }

    @Transient
    public String getFullName()
    {
        return surname + (firstname == null ? "" : ", " + firstname);
    }

    @Transient
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

    public void setResidenceCountry(ICountry residenceCountry)
    {
        this.residenceCountry = residenceCountry;
    }

    /**
     * @return the title
     */
    public ITitleCode getTitle()
    {
        return title;
    }

    public void setTitle(ITitleCode title)
    {
        this.title = title;
    }

    /**
     * @return the sex
     */
    public ISexCode getSex()
    {
        return sex;
    }

    public void setSex(ISexCode sex)
    {
        this.sex = sex;
    }

    /**
     * @return the marital
     */
    public IMaritalCode getMarital()
    {
        return marital;
    }

    public void setMarital(IMaritalCode marital)
    {
        this.marital = marital;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.api.bean.IPerson#isMarried()
     */
    @Override
    public boolean isMarried() {
        return marital != null && IMaritalCode.isMarried(marital.getId());
    }

    /**
     * @return the personHealth
     */
    public IPersonHealth getPersonHealth() {
        if (personHealth == null) {
            personHealth = new PersonHealth();
        }
        return personHealth;
    }

    public void setPersonHealth(IPersonHealth personHealth) {
        this.personHealth = personHealth;
    }

    /**
     * @return the personTrustDIYStatus
     */
    public IPersonTrustDIYStatus getPersonTrustDIYStatus() {
        if (personTrustDIYStatus == null) {
            personTrustDIYStatus = new PersonTrustDIYStatus();
        }
        return personTrustDIYStatus;
    }

    public void setPersonTrustDIYStatus(IPersonTrustDIYStatus personTrustDIYStatus) {
        this.personTrustDIYStatus = personTrustDIYStatus;
    }

    /**
     * @return the occupation
     */
    public IOccupation getOccupation() {
        if (occupation == null) {
            occupation = new Occupation();
        }
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
     * @return the taxFileNumber
     */
    public String getTaxFileNumber()
    {
        return taxFileNumber;
    }

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

    public void setDssRecipient(Boolean dssRecipient)
    {
        this.dssRecipient = dssRecipient;
    }

    @Transient
    public boolean isDssRecipient() {
        return Boolean.TRUE.equals(dssRecipient);
    }

    /**
     * @return the supportToAge
     */
    public Integer getSupportToAge()
    {
        return supportToAge;
    }

    public void setSupportToAge(Integer supportToAge)
    {
        this.supportToAge = supportToAge;
    }

    @Transient
    public Double getAge() {
        return DateTimeUtils.getAgeDouble(dateOfBirth);
    }

}