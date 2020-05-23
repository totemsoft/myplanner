package au.com.totemsoft.myplanner.api.bean.hibernate;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import au.com.totemsoft.myplanner.api.bean.IBase;

/**
 * Base class for all objects.
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@MappedSuperclass
public abstract class AbstractBase<T> implements IBase<T>, Serializable
{

    /** serialVersionUID */
    private static final long serialVersionUID = -3656434288109302301L;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.bean.IBase#isModified()
     */
    @Override
    public boolean isModified() {
        return false;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.bean.IBase#setModified(boolean)
     */
    @Override
    public void setModified(boolean value) {
        // do nothing
    }

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
        return ID + ":" + getId();
    }

}