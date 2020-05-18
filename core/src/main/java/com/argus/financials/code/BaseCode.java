/*
 * BaseCode.java
 *
 * Created on July 9, 2002, 10:46 AM
 */

package com.argus.financials.code;

import java.io.Serializable;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.Iterator;

import com.argus.financials.service.ServiceAware;
import com.argus.util.ReferenceCode;

public class BaseCode
    extends ServiceAware
    implements Serializable {

    static final long serialVersionUID = -5483416520832305696L;

    public final static ReferenceCode CODE_NONE = ReferenceCode.CODE_NONE;

    public final static ReferenceCode[] CODES_NONE = { ReferenceCode.CODE_NONE };

    public Collection getCodes() {
        return null;
    }

    public Object findByPrimaryKey(Object primaryKey) {
        return null;
    }

    public ReferenceCode getCode(Integer codeID) {

        if (codeID == null || codeID.equals(CODE_NONE.getCodeId()))
            return CODE_NONE;

        Iterator iter = getCodes().iterator();
        while (iter.hasNext()) {
            ReferenceCode refCode = (ReferenceCode) iter.next();
            if (codeID.equals(refCode.getCodeId()))
                return refCode;
        }

        return CODE_NONE;

    }

    public ReferenceCode getCode(int codeID) {
        return codeID < 0 ? CODE_NONE : getCode(new Integer(codeID));
    }

    public ReferenceCode getCode(String code) {

        if (code != null)
            code = code.trim();

        if (code == null || code.equalsIgnoreCase(CODE_NONE.getCode())
                || code.equalsIgnoreCase(CODE_NONE.getDescription()))
            return CODE_NONE;

        Iterator iter = getCodes().iterator();
        while (iter.hasNext()) {
            ReferenceCode refCode = (ReferenceCode) iter.next();
            if (code.equalsIgnoreCase(refCode.getCode())
                    || code.equalsIgnoreCase(refCode.getDescription()))
                return refCode;
        }

        return CODE_NONE;

    }

    /**
     * implement this methods in derived classes if you want it to be editable
     */
    public void save() throws com.argus.financials.api.ServiceException {
    }

    public javax.swing.table.TableModel asTableModel() {
        return null;
    }

}
