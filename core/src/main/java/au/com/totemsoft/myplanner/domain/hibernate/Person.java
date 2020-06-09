package au.com.totemsoft.myplanner.domain.hibernate;

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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.api.bean.ILanguage;
import au.com.totemsoft.myplanner.api.bean.IMaritalCode;
import au.com.totemsoft.myplanner.api.bean.IOccupation;
import au.com.totemsoft.myplanner.api.bean.IPerson;
import au.com.totemsoft.myplanner.api.bean.IPersonHealth;
import au.com.totemsoft.myplanner.api.bean.IPersonTrustDIYStatus;
import au.com.totemsoft.myplanner.api.bean.ISexCode;
import au.com.totemsoft.myplanner.api.bean.ITitleCode;
import au.com.totemsoft.myplanner.domain.hibernate.refdata.Country;
import au.com.totemsoft.myplanner.domain.hibernate.refdata.MaritalCode;
import au.com.totemsoft.myplanner.domain.hibernate.refdata.SexCode;
import au.com.totemsoft.myplanner.domain.hibernate.refdata.TitleCode;
import au.com.totemsoft.myplanner.etc.db.PersonHealth;
import au.com.totemsoft.myplanner.etc.db.PersonTrustDIYStatus;
import au.com.totemsoft.util.DateTimeUtils;

@Entity
@Table(name = "Person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends AbstractAuditable<Long> implements IPerson
{
    /** serialVersionUID */
    private static final long serialVersionUID = -8960024744697095377L;

    @Id
    @Column(name = "PersonId", nullable = false)
    @Type(type = "au.com.totemsoft.myplanner.domain.hibernate.LongType")
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

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
    @JoinColumn(name = "DOBCountryID")
    @NotFound(action = NotFoundAction.IGNORE)
    private ICountry dobCountry;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
    @JoinColumn(name = "ResidenceCountryCodeID")
    @NotFound(action = NotFoundAction.IGNORE)
    private ICountry residenceCountry;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = TitleCode.class)
    @JoinColumn(name = "TitleCodeID")
    @NotFound(action = NotFoundAction.IGNORE)
    private ITitleCode title;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = SexCode.class)
    @JoinColumn(name = "SexCodeID")
    @NotFound(action = NotFoundAction.IGNORE)
    private ISexCode sex;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = MaritalCode.class)
    @JoinColumn(name = "MaritalCodeID")
    @NotFound(action = NotFoundAction.IGNORE)
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

    @Override
    public Long getId()
    {
        return id;
    }

    //@Override
    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public String getFirstname()
    {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    @Override
    public String getSurname()
    {
        return surname;
    }

    @Override
    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    @Override
    public String getOtherNames()
    {
        return otherNames;
    }

    @Override
    public void setOtherNames(String otherNames)
    {
        this.otherNames = otherNames;
    }

    @Override
    public String getPreferredName()
    {
        return preferredName;
    }

    @Override
    public void setPreferredName(String preferredName)
    {
        this.preferredName = preferredName;
    }

    @Override
    @Transient
    public String getFullName() {
        String shortName = getShortName();
        if (title == null) {
            return shortName;
        }
        if (StringUtils.isBlank(shortName)) {
            return null;
        }
        return title.getCode() + " " + shortName;
    }

    @Override
    @Transient
    public String getShortName() {
        if (StringUtils.isBlank(surname) && StringUtils.isBlank(firstname)) {
            return null;
        }
        if (StringUtils.isBlank(surname)) {
            return firstname;
        }
        if (StringUtils.isBlank(firstname)) {
            return surname;
        }
        return surname + ", " + firstname;
    }

    @Override
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public ICountry getDobCountry()
    {
        return dobCountry;
    }

    @Override
    public void setDobCountry(ICountry dobCountry)
    {
        this.dobCountry = dobCountry;
    }

    @Override
    public ICountry getResidenceCountry()
    {
        return residenceCountry;
    }

    @Override
    public void setResidenceCountry(ICountry residenceCountry)
    {
        this.residenceCountry = residenceCountry;
    }

    @Override
    public ITitleCode getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(ITitleCode title)
    {
        this.title = title;
    }

    @Override
    public ISexCode getSex()
    {
        return sex;
    }

    @Override
    public void setSex(ISexCode sex)
    {
        this.sex = sex;
    }

    @Override
    public IMaritalCode getMarital()
    {
        return marital;
    }

    @Override
    public void setMarital(IMaritalCode marital)
    {
        this.marital = marital;
    }

    @Override
    public boolean isMarried() {
        return marital != null && IMaritalCode.isMarried(marital.getId());
    }

    @Override
    public IPersonHealth getPersonHealth() {
        if (personHealth == null) {
            personHealth = new PersonHealth();
        }
        return personHealth;
    }

    @Override
    public void setPersonHealth(IPersonHealth personHealth) {
        this.personHealth = personHealth;
    }

    @Override
    public IPersonTrustDIYStatus getPersonTrustDIYStatus() {
        if (personTrustDIYStatus == null) {
            personTrustDIYStatus = new PersonTrustDIYStatus();
        }
        return personTrustDIYStatus;
    }

    @Override
    public void setPersonTrustDIYStatus(IPersonTrustDIYStatus personTrustDIYStatus) {
        this.personTrustDIYStatus = personTrustDIYStatus;
    }

    @Override
    public IOccupation getOccupation() {
        if (occupation == null) {
            occupation = new Occupation();
        }
        return occupation;
    }

    @Override
    public void setOccupation(IOccupation occupation) {
        this.occupation = occupation;
    }

    @Override
    public ILanguage getPreferredLanguage() {
        return preferredLanguage;
    }

    @Override
    public void setPreferredLanguage(ILanguage preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @Override
    public Integer getReferalSourceCodeId() {
        return referalSourceCodeId;
    }

    @Override
    public void setReferalSourceCodeId(Integer referalSourceCodeId) {
        this.referalSourceCodeId = referalSourceCodeId;
    }

    @Override
    public Integer getResidenceStatusCodeId() {
        return residenceStatusCodeId;
    }

    @Override
    public void setResidenceStatusCodeId(Integer residenceStatusCodeId) {
        this.residenceStatusCodeId = residenceStatusCodeId;
    }

    @Override
    public String getTaxFileNumber()
    {
        return taxFileNumber;
    }

    @Override
    public void setTaxFileNumber(String taxFileNumber)
    {
        this.taxFileNumber = taxFileNumber;
    }

    @Override
    public Boolean getDssRecipient()
    {
        return dssRecipient;
    }

    @Override
    public void setDssRecipient(Boolean dssRecipient)
    {
        this.dssRecipient = dssRecipient;
    }

    @Override
    @Transient
    public boolean dssRecipient() {
        return Boolean.TRUE.equals(dssRecipient);
    }

    @Override
    public Integer getSupportToAge()
    {
        return supportToAge;
    }

    @Override
    public void setSupportToAge(Integer supportToAge)
    {
        this.supportToAge = supportToAge;
    }

    @Override
    @Transient
    public Double getAge() {
        return DateTimeUtils.getAgeDouble(dateOfBirth);
    }

}