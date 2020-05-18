/*
 * FinancialTreeFilters.java
 *
 * Created on 26 March 2003, 16:49
 */

package com.argus.financials.tree;

/**
 * 
 * @author valeri chibaev
 */

import java.util.Iterator;

import com.argus.util.VectorTemplate;

public class FinancialTreeFilters {

    private VectorTemplate filters = new VectorTemplate(
            FinancialTreeFilter.class);

    /** Creates a new instance of FinancialTreeFilters */
    public FinancialTreeFilters() {
    }

    public FinancialTreeFilters(FinancialTreeFilters ftfs) {

        if (ftfs != null) {
            java.util.Iterator iter = ftfs.filters.iterator();
            while (iter.hasNext())
                add(new FinancialTreeFilter((FinancialTreeFilter) iter.next()));
        }

    }

    /**
     * 
     */
    public int size() {
        return filters.size();
    }

    public void clear() {
        filters.clear();
    }

    public void add(FinancialTreeFilter value) {
        if (value != null)
            filters.add(value);
    }

    public boolean contains(FinancialTreeFilter value) {
        return filters.contains(value);
    }

    public void remove(FinancialTreeFilter value) {
        if (value != null)
            filters.remove(value);
    }

    public Iterator iterator() {
        return filters.iterator();
    }

    public Object[] toArray() {
        return filters.toArray();
    }

}
