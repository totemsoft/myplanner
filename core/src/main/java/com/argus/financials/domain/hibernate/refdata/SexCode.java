package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.argus.financials.domain.client.refdata.ISexCode;
import com.argus.financials.service.client.EntityService;

@Component
@Entity
@Table(name = ISexCode.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class SexCode extends AbstractCode implements ISexCode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 5701671045822264162L;

    private static EntityService entityService;

    @Id
    @Column(name = "SexCodeID", nullable = false)
    @Type(type = "com.argus.financials.domain.hibernate.LongType")
    private Long id;

    //@Column(name = "SexCode", nullable = false)
    //private String code;

    @Column(name = "SexCodeDesc", nullable = false)
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
        return getDescription();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    ///////////////////////////////////////////////////////////////////////////
    // These methods (findClassName, persist, remove) are required by GWT
    // for class that accessed via ClassNameProxy
    ///////////////////////////////////////////////////////////////////////////

    /**
     * inject via spring configuration
     * @param entityService the entityService to set
     */
    @Autowired
    public void setEntityService(EntityService entityService)
    {
        SexCode.entityService = entityService;
    }

    public static SexCode findSexCode(Long id)
    {
        return SexCode.entityService.findSexCode(id);
    }

    public void persist()
    {
        //SexCode.entityService.persist(this);
    }

    public void remove()
    {
        //SexCode.entityService.remove(this);
    }

}