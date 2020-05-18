/*
 * CodeComparator.java
 *
 * Created on July 19, 2002, 12:11 PM
 */

package com.argus.financials.api.code;

import java.io.Serializable;
import java.util.Comparator;

/**
 * TODO: @see BeanUtils 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 * @param <T>
 */

public class CodeComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = -9080869357095753483L;

    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null)
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;
        return _compare(o1.toString(), o2.toString());
    }

    public boolean equals(Object obj) {
        return obj == null ? false : this.toString().equalsIgnoreCase(obj.toString());
    }

    private int _compare(String s1, String s2) {
//        if (s1 == null && s2 == null)
//            return 0;
//        if (s1 == null)
//            return -1;
//        if (s2 == null)
//            return 1;
        int result = s1.compareToIgnoreCase(s2);
        if (result < 0)
            return -1;
        if (result > 0)
            return 1;
        return 0;
    }

}