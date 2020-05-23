/*
 * FinancialTreeFilters.java
 *
 * Created on 26 March 2003, 16:49
 */

package au.com.totemsoft.myplanner.tree;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.util.Iterator;

import au.com.totemsoft.util.VectorTemplate;

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
