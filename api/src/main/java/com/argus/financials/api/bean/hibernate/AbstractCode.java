package com.argus.financials.api.bean.hibernate;

import javax.persistence.MappedSuperclass;

import com.argus.financials.api.bean.ICode;
import com.argus.financials.api.dao.EntityDao;

/**
 * Base class for all codes.
 * @author vchibaev (Valeri SHIBAEV)
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
     * @see com.argus.financials.api.bean.IBase#isModified()
     */
    @Override
    public boolean isModified() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.api.bean.IBase#setModified(boolean)
     */
    @Override
    public void setModified(boolean value) {
        // do nothing
    }

}