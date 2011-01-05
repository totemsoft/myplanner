package com.argus.financials.domain.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.argus.financials.domain.hibernate.refdata.AddressCode;
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.State;
import com.argus.financials.domain.refdata.IAddressCode;
import com.argus.financials.domain.refdata.ICountry;
import com.argus.financials.domain.refdata.IState;

@Entity
@Table(name = "Address")
public class Address extends AbstractAuditable<Integer> implements IAddress
{
    /** serialVersionUID */
    private static final long serialVersionUID = -6435531874121406067L;

    @Id
    @Column(name = "AddressID", nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = Address.class)//(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentAddressID")
    private IAddress parent;

    @ManyToOne(targetEntity = AddressCode.class)//(fetch = FetchType.LAZY)
    @JoinColumn(name = "AddressCodeID")
    private IAddressCode addressCode;

    @Column(name = "StreetNumber")
    private String streetNumber;

    @Column(name = "StreetNumber2")
    private String streetNumber2;

    @Column(name = "Suburb")
    private String suburb;

    @Column(name = "Postcode")
    private Integer postcode;

    @ManyToOne(targetEntity = State.class)//(fetch = FetchType.LAZY)
    @JoinColumn(name = "StateCodeID")
    private IState state;

    @Column(name = "State")
    private String stateCode;

    @ManyToOne(targetEntity = Country.class)//(fetch = FetchType.LAZY)
    @JoinColumn(name = "CountryCodeID")
    private ICountry country;

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
     * @return parent
     */
    public IAddress getParentId()
    {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(IAddress parent)
    {
        this.parent = parent;
    }

    /**
     * @return the addressCode
     */
    public IAddressCode getAddressCode()
    {
        return addressCode;
    }

    /**
     * @param addressCode the addressCode to set
     */
    public void setAddressCode(IAddressCode addressCode)
    {
        this.addressCode = addressCode;
    }

    /**
     * @return the streetNumber
     */
    public String getStreetNumber()
    {
        return streetNumber;
    }

    /**
     * @param streetNumber the streetNumber to set
     */
    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }

    /**
     * @return the streetNumber2
     */
    public String getStreetNumber2()
    {
        return streetNumber2;
    }

    /**
     * @param streetNumber2 the streetNumber2 to set
     */
    public void setStreetNumber2(String streetNumber2)
    {
        this.streetNumber2 = streetNumber2;
    }

    /**
     * @return the suburb
     */
    public String getSuburb()
    {
        return suburb;
    }

    /**
     * @param suburb the suburb to set
     */
    public void setSuburb(String suburb)
    {
        this.suburb = suburb;
    }

    /**
     * @return the postcode
     */
    public Integer getPostcode()
    {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(Integer postcode)
    {
        this.postcode = postcode;
    }

    /**
     * @return the state
     */
    public IState getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(IState state)
    {
        this.state = state;
    }

    /**
     * @return the stateCode
     */
    public String getStateCode()
    {
        return stateCode;
    }

    /**
     * @param stateCode the stateCode to set
     */
    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }

    /**
     * @return the country
     */
    public ICountry getCountry()
    {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public String getDetails()
    {
        return (streetNumber == null ? "" : streetNumber) + " "
            + (streetNumber2 == null ? "" : streetNumber2) + " "
            + (suburb == null ? "" : suburb) + " "
            + (state == null ? (stateCode == null ? "" : stateCode) : state.getCode()) + " "
            + (postcode == null ? "" : "" + postcode) + " "
            + (country == null ? "" : country.getCode())
            ;
    }

}