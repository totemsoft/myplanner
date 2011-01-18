package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.argus.financials.domain.client.refdata.IAddressCode;

@Entity
@Table(name = IAddressCode.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class AddressCode extends AbstractCode implements IAddressCode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 4710325054260470545L;

    @Id
    @Column(name = "AddressCodeID", nullable = false)
    @Type(type = "com.argus.financials.domain.hibernate.LongType")
    private Long id;

    //@Column(name = "AddressCode", nullable = false)
    //private String code;

    @Column(name = "AddressCodeDesc", nullable = false)
    private String description;

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Long getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return description;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}