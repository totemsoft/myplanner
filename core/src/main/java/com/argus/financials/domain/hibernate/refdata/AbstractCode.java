package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.argus.financials.domain.client.refdata.ICode;
import com.argus.financials.domain.hibernate.AbstractBase;

/**
 * Base class for all codes.
 * @author vchibaev (Valeri SHIBAEV)
 */
//@Component
@MappedSuperclass
public abstract class AbstractCode extends AbstractBase<Long> implements ICode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 8470628483943356675L;

    //protected static EntityService entityService;

    @Transient
    //@Version
    //@Column(name = "")
    private Integer version;

    /**
     * @return the version
     */
    public Integer getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version)
    {
        this.version = version;
    }

//    /**
//     * inject via spring configuration
//     * @param entityService the entityService to set
//     */
//    @Autowired
//    public void setEntityService(EntityService entityService)
//    {
//        AbstractCode.entityService = entityService;
//    }

}