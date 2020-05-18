package com.argus.financials.domain.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.argus.financials.api.bean.hibernate.AbstractBase;

/**
 * Base class for all auditable objects.
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@MappedSuperclass
public abstract class AbstractAuditable<T> extends AbstractBase<T>
{

    /** serialVersionUID */
    private static final long serialVersionUID = -2227122059371221112L;

    @Column(name = "DateCreated")
    private Date dateCreated;

    @Column(name = "DateModified")
    private Date dateModified;

    @Transient
    //@Version
    //@Column(name = "")
    private Integer version;

    /**
     * @return the dateCreated
     */
    public Date getDateCreated()
    {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the dateModified
     */
    public Date getDateModified()
    {
        return dateModified;
    }

    /**
     * @param dateModified the dateModified to set
     */
    public void setDateModified(Date dateModified)
    {
        this.dateModified = dateModified;
    }

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

}