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

@Entity
@Table(name = "Address")
public class Address extends AbstractAuditable<Integer> //implements IAddress
{
    /** serialVersionUID */
    private static final long serialVersionUID = -6435531874121406067L;
//    [AddressCodeID] [int] NULL,

    @Id
    @Column(name = "AddressID", nullable = false)
    private Integer id;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentAddressID")
    private Address parent;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "AddressCodeID")
    private AddressCode addressCode;

    @Column(name = "StreetNumber")
    private String streetNumber;

    @Column(name = "StreetNumber2")
    private String streetNumber2;

    @Column(name = "Suburb")
    private String suburb;

    @Column(name = "Postcode")
    private Integer postcode;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "StateCodeID")
    private State state;

    @Column(name = "State")
    private String stateCode;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "CountryCodeID")
    private Country country;

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
    public Address getParentId()
    {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Address parent)
    {
        this.parent = parent;
    }

    /**
     * @return the addressCode
     */
    public AddressCode getAddressCode()
    {
        return addressCode;
    }

    /**
     * @param addressCode the addressCode to set
     */
    public void setAddressCode(AddressCode addressCode)
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
    public State getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state)
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
    public Country getCountry()
    {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country)
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