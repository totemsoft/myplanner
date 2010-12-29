package com.argus.financials.domain.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Address")
public class Address extends AbstractBase<Integer> //implements IAddress
{
    /** serialVersionUID */
    private static final long serialVersionUID = -6435531874121406067L;

    @Id
    @Column(name = "AddressID", nullable = false)
    private Integer id;

    @Column(name = "ParentAddressID")
    private Integer parentId;

    @Column(name = "addressCodeID")
    private Integer codeId;

    @Column(name = "StreetNumber")
    private String streetNumber;

    @Column(name = "StreetNumber2")
    private String streetNumber2;

    @Column(name = "Suburb")
    private String suburb;

    @Column(name = "Postcode")
    private Integer postcode;

    @Column(name = "StateCodeID")
    private Integer stateCodeId;

    @Column(name = "CountryCodeID")
    private Integer countryCodeId;

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
     * @return parentId
     */
    public Integer getParentId()
    {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Integer ownerId)
    {
        this.parentId = ownerId;
    }

    /**
     * @return the codeId
     */
    public Integer getCodeId()
    {
        return codeId;
    }

    /**
     * @param codeId the codeId to set
     */
    public void setCodeId(Integer codeId)
    {
        this.codeId = codeId;
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
     * @return the stateCodeId
     */
    public Integer getStateCodeId()
    {
        return stateCodeId;
    }

    /**
     * @param stateCodeId the stateCodeId to set
     */
    public void setStateCodeId(Integer stateCodeId)
    {
        this.stateCodeId = stateCodeId;
    }

    /**
     * @return the countryCodeId
     */
    public Integer getCountryCodeId()
    {
        return countryCodeId;
    }

    /**
     * @param countryCodeId the countryCodeId to set
     */
    public void setCountryCodeId(Integer countryCodeId)
    {
        this.countryCodeId = countryCodeId;
    }

    public String getDetails()
    {
        return (streetNumber == null ? "" : streetNumber) + " "
            + (streetNumber2 == null ? "" : streetNumber2) + " "
            + (suburb == null ? "" : suburb) + " "
            + (stateCodeId == null ? "" : stateCodeId) + " "
            + (postcode == null ? "" : "" + postcode);
    }

}