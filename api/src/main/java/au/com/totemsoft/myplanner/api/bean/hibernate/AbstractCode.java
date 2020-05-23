package au.com.totemsoft.myplanner.api.bean.hibernate;

import javax.persistence.MappedSuperclass;

import au.com.totemsoft.myplanner.api.bean.ICode;
import au.com.totemsoft.myplanner.api.dao.EntityDao;

/**
 * Base class for all codes.
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
//@Component
@MappedSuperclass
public abstract class AbstractCode extends AbstractBase<Integer> implements ICode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 8470628483943356675L;

    protected static EntityDao entityDao;
    public static void setEntityDao(EntityDao entityDao) {
        AbstractCode.entityDao = entityDao;
    }

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

}