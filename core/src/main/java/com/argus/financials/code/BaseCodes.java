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

import com.argus.financials.api.code.CodeComparator;
import com.argus.financials.bean.AbstractBase;

public class BaseCodes extends BaseCode {

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

            if (obj instanceof AbstractBase) {
                if (primaryKey
                        .equals(((AbstractBase) obj).getId()))
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
