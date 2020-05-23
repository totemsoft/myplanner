/*
 * FinancialTreeStructure.java
 *
 * Created on 26 March 2003, 16:30
 */

package au.com.totemsoft.myplanner.tree;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.util.Iterator;
import java.util.Vector;

import au.com.totemsoft.myplanner.code.CountryCode;
import au.com.totemsoft.myplanner.code.FinancialClass;
import au.com.totemsoft.myplanner.code.FinancialServiceCode;
import au.com.totemsoft.myplanner.code.FinancialType;
import au.com.totemsoft.myplanner.code.FundType;
import au.com.totemsoft.myplanner.code.OwnerCode;

public class FinancialTreeStructure {

    public static final Object FINANCIAL_CLASS = new FinancialClass();

    public static final Object FINANCIAL_TYPE = new FinancialType();
    public static final Object ASSOCIATED_FINANCIAL_TYPE = new FinancialType();

    public static final Object FUND_TYPE = new FundType();

    public static final Object ASSOCIATED_FINANCIAL_CLASS = new FinancialClass();

    public static final Object ASSOCIATED_FUND_TYPE = new FundType();

    public static final Object OWNER_CODE = new OwnerCode();

    public static final Object COUNTRY_CODE = new CountryCode();

    public static final Object TAXABLE = new Object();

    public static final Object SERVICE_CODE = new FinancialServiceCode();

    public static final Object GENERATED = new Object();

    private Vector structure;

    /** Creates a new instance of FinancialTreeStructure */
    public FinancialTreeStructure() {
        structure = new Vector();
    }

    public FinancialTreeStructure(FinancialTreeStructure fts) {
        structure = fts == null ? new Vector() : new Vector(fts.structure);
    }

    /**
     * 
     */
    public int size() {
        return structure.size();
    }

    public void clear() {
        structure.clear();
    }

    public void add(Object value) {
        if (value != null && !contains(value))
            structure.add(value);
    }

    public boolean contains(Object value) {
        return structure.contains(value);
    }

    public void remove(Object value) {
        if (value != null)
            structure.remove(value);
    }

    public Iterator iterator() {
        return structure.iterator();
    }

    public Object[] toArray() {
        return structure.toArray();
    }

}
