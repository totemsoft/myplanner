/*
 * Assets.java
 *
 * Created on August 9, 2002, 1:52 PM
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class BaseCodes extends BaseCode {

    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.code.BaseCodes

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = -1838542427804470428L;

    private Collection codes;

    /** Creates new Financials collection */
    public BaseCodes() {
        codes = new TreeSet(new CodeComparator());
    }

    public Collection getCodes() {
        return codes;
    }

    public void initCodes(java.util.Map details) {

        codes.clear();
        codes.add(CODE_NONE);

    }

    public Object findByPrimaryKey(Object primaryKey) {

        if (primaryKey == null)
            return null;

        Iterator iter = getCodes().iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();

            if (obj instanceof com.argus.financials.bean.AbstractBase) {
                if (primaryKey
                        .equals(((com.argus.financials.bean.AbstractBase) obj)
                                .getPrimaryKeyID()))
                    return obj;
            } else if (obj instanceof com.argus.util.ReferenceCode) {
                // do nothing
            } else {
                System.out.println("--------------> Unhandled class: " + obj
                        + " <--------------");
            }

        }

        return null;

    }

}
