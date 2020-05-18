package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.argus.financials.api.bean.IContactCode;
import com.argus.financials.api.bean.hibernate.AbstractCode;

@Entity
@Table(name = IContactCode.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class ContactCode extends AbstractCode implements IContactCode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 4075870871770958268L;

    @Id
    @Column(name = "ContactMediaCodeID", nullable = false)
    @Type(type = "com.argus.financials.domain.hibernate.IntegerType")
    private Integer id;

    //@Column(name = "ContactMediaCode", nullable = false)
    //private String code;

    @Column(name = "ContactMediaCodeDesc", nullable = false)
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