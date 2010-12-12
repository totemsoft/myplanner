/*
 * BaseCode.java
 *
 * Created on July 9, 2002, 10:46 AM
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.Iterator;

import com.argus.financials.config.FPSLocale;
import com.argus.util.ReferenceCode;

public class BaseCode implements java.io.Serializable {

    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.code.BaseCode

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = -5483416520832305696L;

    protected static boolean DEBUG = false;

    public final static ReferenceCode CODE_NONE = ReferenceCode.CODE_NONE;

    public final static ReferenceCode[] CODES_NONE = { ReferenceCode.CODE_NONE };

    /** Creates new BaseCode */
    public BaseCode() {
        FPSLocale r = FPSLocale.getInstance();
        DEBUG = Boolean.valueOf(System.getProperty("DEBUG")).booleanValue();
    }

    public Collection getCodes() {
        return null;
    }

    public Object findByPrimaryKey(Object primaryKey) {
        return null;
    }

    public ReferenceCode getCode(Integer codeID) {

        if (codeID == null || codeID.equals(CODE_NONE.getCodeIDInteger()))
            return CODE_NONE;

        Iterator iter = getCodes().iterator();
        while (iter.hasNext()) {
            ReferenceCode refCode = (ReferenceCode) iter.next();
            if (codeID.equals(refCode.getCodeIDInteger()))
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
                || code.equalsIgnoreCase(CODE_NONE.getCodeDesc()))
            return CODE_NONE;

        Iterator iter = getCodes().iterator();
        while (iter.hasNext()) {
            ReferenceCode refCode = (ReferenceCode) iter.next();
            if (code.equalsIgnoreCase(refCode.getCode())
                    || code.equalsIgnoreCase(refCode.getCodeDesc()))
                return refCode;
        }

        return CODE_NONE;

    }

    /**
     * implement this methods in derived classes if you want it to be editable
     */
    public void save() throws com.argus.financials.service.ServiceException {
    }

    public javax.swing.table.TableModel asTableModel() {
        return null;
    }

    /**
     * 
     */
    public class CodeComparator extends BaseCodeComparator {
    }

}
