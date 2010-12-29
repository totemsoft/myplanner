package com.argus.financials.domain.hibernate.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.argus.financials.domain.hibernate.Address;
import com.argus.financials.domain.hibernate.Person;

@Entity
@Table(name = "vUserPersonClients")
@PrimaryKeyJoinColumn(name = "PersonId")
public class Client extends Person
{

    /** serialVersionUID */
    private static final long serialVersionUID = -136081306255824768L;

    @Column(name = "AdviserID", nullable = false)
    private Integer ownerId;

    @Column(name = "AdvisorFirstName")
    private String ownerFirstname;

    @Column(name = "AdvisorFamilyName")
    private String ownerSurname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addressID")
    private Address address;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Fax")
    private String fax;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneWork")
    private String phoneWork;

    @Column(name = "FaxWork")
    private String faxWork;

    @Column(name = "EmailWork")
    private String emailWork;
    
    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getOwnerId()
     */
    public Integer getOwnerId()
    {
        return ownerId;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getOwnerFirstname()
     */
    public String getOwnerFirstname()
    {
        return ownerFirstname;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getOwnerSurname()
     */
    public String getOwnerSurname()
    {
        return ownerSurname;
    }

    public String getOwnerShortName()
    {
        return ownerSurname + (ownerFirstname == null ? "" : ", " + ownerFirstname);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getAddress()
     */
    public Address getAddress()
    {
        return address;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getEMail()
     */
    public String getEMail()
    {
        return email;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getEmailWork()
     */
    public String getEmailWork()
    {
        return emailWork;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getFax()
     */
    public String getFax()
    {
        return fax;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getFaxWork()
     */
    public String getFaxWork()
    {
        return faxWork;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getPhone()
     */
    public String getPhone()
    {
        return phone;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IClient#getPhoneWork()
     */
    public String getPhoneWork()
    {
        return phoneWork;
    }

    public String getDetails()
    {
        String result = "Address: " + address.getDetails();
        result += "\n\nPhone: " + phone;
        result += "\nPhone Work: " + phoneWork;
        result += "\nFax: " + fax;
        result += "\nFax Work: " + faxWork;
        result += "\nEmail: " + email;
        result += "\nEmail Work: " + emailWork;
        //result += "\nMobile: " + mobile;
        return result;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.hibernate.AbstractBase#toString()
     */
    @Override
    public String toString()
    {
        return getShortName();
    }

}