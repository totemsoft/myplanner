package com.argus.financials.domain.hibernate;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.argus.financials.domain.IBase;

/**
 * Base class for all objects.
 * @author vchibaev (Valeri SHIBAEV)
 */
@MappedSuperclass
public abstract class AbstractBase<T> implements IBase<T>, Serializable
{

    /** serialVersionUID */
    private static final long serialVersionUID = -3656434288109302301L;

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true; // super implementation
        }
        if (other != null)
        {
            AbstractBase<T> castOther = (AbstractBase<T>) other;
            //use "castOther.getId()" as this instance can be hibernate enhancer
            if (getId() != null && castOther.getId() != null)
            {
                return getId().equals(castOther.getId());
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return getId() == null ? super.hashCode() : getId().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new ToStringBuilder(this).append(ID, getId()).toString();
    }

}