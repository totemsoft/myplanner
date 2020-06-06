package au.com.totemsoft.myplanner.domain.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import au.com.totemsoft.myplanner.api.bean.IClientView;

@Entity
@Table(name = "vUserPersonClients")
@PrimaryKeyJoinColumn(name = "PersonId")
public class ClientView extends Person implements IClientView
{

    /** serialVersionUID */
    private static final long serialVersionUID = -136081306255824768L;

    @Column(name = "AdviserID", nullable = false)
    @Type(type = "au.com.totemsoft.myplanner.domain.hibernate.LongType")
    private Long ownerId;

    @Column(name = "AdvisorFirstName")
    private String ownerFirstname;

    @Column(name = "AdvisorFamilyName")
    private String ownerSurname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addressID")
    @NotFound(action = NotFoundAction.IGNORE)
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

    @Override
    public Long getOwnerId()
    {
        return ownerId;
    }

    @Override
    public String getOwnerFirstname()
    {
        return ownerFirstname;
    }

    @Override
    public String getOwnerSurname()
    {
        return ownerSurname;
    }

    @Override
    public String getOwnerShortName()
    {
        return ownerSurname + (ownerFirstname == null ? "" : ", " + ownerFirstname);
    }

    //@Override
    public Address getAddress()
    {
        return address;
    }

    @Override
    public String getEMail()
    {
        return email;
    }

    @Override
    public String getEmailWork()
    {
        return emailWork;
    }

    @Override
    public String getFax()
    {
        return fax;
    }

    @Override
    public String getFaxWork()
    {
        return faxWork;
    }

    @Override
    public String getPhone()
    {
        return phone;
    }

    @Override
    public String getPhoneWork()
    {
        return phoneWork;
    }

    @Override
    public String getDetails()
    {
        String result = "Address: " + (address == null ? "" : address.getDetails());
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
     * @see au.com.totemsoft.myplanner.domain.hibernate.AbstractBase#toString()
     */
    @Override
    public String toString()
    {
        return getShortName();
    }

}