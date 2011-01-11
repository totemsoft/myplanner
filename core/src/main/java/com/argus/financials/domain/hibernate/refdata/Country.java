package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.argus.financials.domain.client.refdata.ICountry;

@Entity
@Table(name = ICountry.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country extends AbstractCode implements ICountry
{
    /** serialVersionUID */
    private static final long serialVersionUID = -5771582515464015957L;

    @Id
    @Column(name = "CountryCodeID", nullable = false)
    private Integer id;

    @Column(name = "CountryCode", nullable = false)
    private String code;

    @Column(name = "CountryCodeDesc", nullable = false)
    private String description;

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return code;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}