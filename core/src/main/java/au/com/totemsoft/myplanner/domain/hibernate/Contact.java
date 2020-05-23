package au.com.totemsoft.myplanner.domain.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.com.totemsoft.myplanner.domain.hibernate.refdata.ContactCode;

@Entity
@Table(name = "ContactMedia")
public class Contact extends AbstractAuditable<Long> // implements IContact
{

    /** serialVersionUID */
    private static final long serialVersionUID = 2640200662238849019L;

    @Id
    @Column(name = "ContactMediaID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ContactMediaCodeID")
    private ContactCode contactCode;

    @Column(name = "Value1", length = 16)
    private String value1;

    @Column(name = "Value2", length = 50)
    private String value2;

    @Column(name = "ContactMediaDesc", length = 50)
    private String description;
    
    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.IBase#getId()
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
     * @return the contactCode
     */
    public ContactCode getContactCode()
    {
        return contactCode;
    }

    /**
     * @param contactCode the contactCode to set
     */
    public void setContactCode(ContactCode contactCode)
    {
        this.contactCode = contactCode;
    }

    /**
     * @return the value1
     */
    public String getValue1()
    {
        return value1;
    }

    /**
     * @param value1 the value1 to set
     */
    public void setValue1(String value1)
    {
        this.value1 = value1;
    }

    /**
     * @return the value2
     */
    public String getValue2()
    {
        return value2;
    }

    /**
     * @param value2 the value2 to set
     */
    public void setValue2(String value2)
    {
        this.value2 = value2;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

}