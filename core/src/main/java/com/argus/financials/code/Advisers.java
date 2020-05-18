/*
 * Advisers.java
 *
 * Created on 30 October 2002, 13:18
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.code.CodeComparator;
import com.argus.financials.etc.Contact;

public class Advisers extends BaseCode {

    private static Collection codes = new TreeSet(new CodeComparator());

    public static final Contact NONE = new Contact() {
        public Integer getId() {
            return new Integer(0);
        }

        public String toString() {
            return "";
        }
    };

    /** Creates a new instance of Advisers */
    public Advisers() {
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {

        if (codes.size() > 0)
            return;

        codes.clear();
        codes.add(NONE);

        try {
            List<Contact> l = utilityService.findUsers(); // ALL
            if (l == null)
                return;

            codes.addAll(l);

        } catch (ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

    public Object findByPrimaryKey(Object primaryKey) {

        if (primaryKey == null)// || primaryKey.equals(
                                // CODE_NONE.getCodeIDInteger() ) )
            return null;// CODE_NONE;

        Iterator iter = getCodes().iterator();
        while (iter.hasNext()) {
            Contact c = (Contact) iter.next();
            if (primaryKey.equals(c.getId()))
                return c;
        }

        return null;// CODE_NONE;

    }

}
