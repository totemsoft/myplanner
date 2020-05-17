package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.argus.financials.api.bean.ISexCode;
import com.argus.financials.api.bean.hibernate.AbstractCode;

@Entity
@Table(name = ISexCode.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class SexCode extends AbstractCode implements ISexCode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 5701671045822264162L;

    @Id
    @Column(name = "SexCodeID", nullable = false)
    @Type(type = "com.argus.financials.domain.hibernate.IntegerType")
    private Integer id;

    //@Column(name = "SexCode", nullable = false)
    //private String code;

    @Column(name = "SexCodeDesc", nullable = false)
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
        return getDescription();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    public static ISexCode findSexCode(Integer id)
    {
        return entityDao.findSexCode(id);
    }

}