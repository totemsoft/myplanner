/*
 * Financials.java
 *
 * Created on August 9, 2002, 1:52 PM
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.BaseCodes;
import com.argus.financials.code.FinancialClassID;

public class Financials extends BaseCodes implements FinancialClassID {
    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.financial.Financials

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 8343200002344089487L;

    /** Creates new Financials collection */
    public Financials() {
        this(null);
    }

    public Financials(java.util.Map financials) {
        super();
        initCodes(financials);
    }

    protected void initCodes(java.util.Map financials, Integer objectTypeID) {
        java.util.Map map = (java.util.Map) financials.get(objectTypeID);
        if (map == null)
            return;

        getCodes().addAll(map.values());

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static java.util.Map deepCopy(java.util.Map financials) {
        return deepCopy(financials, true);
    }

    public static java.util.Map deepCopy(java.util.Map financials,
            boolean resetPK) {

        if (financials == null)
            return null;

        // Map( oldAsset, copyAsset )
        java.util.Map assetMap = new java.util.HashMap();

        // source assets
        Assets assets = new Assets(financials);

        java.util.Map newFinancials = new java.util.HashMap();
        java.util.Iterator iter = financials.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            Object key = entry.getKey();
            java.util.Map map = (java.util.Map) entry.getValue();
            if (map == null)
                continue;

            java.util.Map newMap = new java.util.HashMap();
            newFinancials.put(key, newMap);

            java.util.Iterator iter2 = map.entrySet().iterator();
            while (iter2.hasNext()) {
                java.util.Map.Entry entry2 = (java.util.Map.Entry) iter2.next();
                Integer id = (Integer) entry2.getKey();
                Financial f = (Financial) entry2.getValue();
                if (f == null)
                    continue;

                try {

                    // if (DEBUG) System.out.println( "\nOriginal Financial: " +
                    // f );

                    if (f instanceof Asset) {

                        Asset a2; // copy asset
                        if (!assetMap.containsKey(f)) {
                            // store asset, copy asset
                            a2 = (Asset) f.clone();
                            if (resetPK)
                                a2.setPrimaryKeyID(null);
                            // if (DEBUG) System.out.println( "\tCloned Asset: "
                            // + a2 );
                            assetMap.put(f, a2);
                        } else {
                            a2 = (Asset) assetMap.get(f);
                            // if (DEBUG) System.out.println( "\tAlready Cloned
                            // Asset: " + a2 );
                        }

                        // if (DEBUG) System.out.println( "\t\tAssetAllocation:
                        // " + a2.getAssetAllocation().getInCash() + ", " +
                        // a2.getAssetAllocation().getAssetAllocationID() );

                        f = a2;

                    } else if (f instanceof Liability) {

                        f = (Financial) f.clone();
                        if (resetPK)
                            f.setPrimaryKeyID(null);
                        // if (DEBUG) System.out.println( "\tCloned Liability: "
                        // + f );

                    } else if (f instanceof Regular) { // Income, Expence,
                                                        // TaxOffset

                        // first get original asset (if any)
                        Integer aid = ((Regular) f).getAssetID();
                        // if (DEBUG) System.out.println( "\tAssociated AssetID:
                        // " + aid );

                        Asset a = ((Regular) f).getAsset(assets);
                        // if (DEBUG) System.out.println( "\tAssociated Asset: "
                        // + a );

                        // clone this regular
                        f = (Financial) f.clone();
                        if (resetPK)
                            f.setPrimaryKeyID(null);
                        // if (DEBUG) System.out.println( "\tCloned Regular: " +
                        // f );

                        if (a != null) {
                            Asset a2; // copy asset
                            if (!assetMap.containsKey(a)) {
                                // store asset, copy asset
                                a2 = (Asset) a.clone();
                                if (resetPK)
                                    a2.setPrimaryKeyID(null);
                                // if (DEBUG) System.out.println( "\t\tCloned
                                // Associated Asset: " + a2 );
                                assetMap.put(a, a2);
                            } else {
                                a2 = (Asset) assetMap.get(a);
                                // if (DEBUG) System.out.println( "\t\tAlready
                                // Cloned Associated Asset: " + a2 );
                            }

                            ((Regular) f).setAsset(a2);

                        }

                    }

                } catch (java.lang.CloneNotSupportedException e) {
                    e.printStackTrace(System.err);
                    return null;
                }

                newMap.put(f.getPrimaryKeyID(), f);

            }

        }

        return newFinancials;

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void addGeneratedData(java.util.Collection source,
            final int[] rules) {
        addGeneratedData(source, rules, -1);
    }

    public static void addGeneratedData(java.util.Collection source,
            final int[] rules, int year) {
        if (source == null)
            return;

        // storage for generated data
        java.util.Collection gSource = new java.util.ArrayList();

        removeGeneratedData(source);

        if (rules == null)
            return;

        java.util.Iterator iter = source.iterator();
        while (iter.hasNext()) {
            Financial f = (Financial) iter.next();
            if (f == null)
                continue;

            if (year >= 0 && (f instanceof Asset || f instanceof Liability))
                f = f.project(year, 0.); // no inflation

            for (int i = 0; i < rules.length; i++) {
                Regular r = f.getRegular(rules[i]);
                if (r == null)
                    continue;

                // java.math.BigDecimal amount = r.getRegularAmount();
                // if ( amount == null || amount.doubleValue() == 0. ) continue;

                gSource.add(r);
            }

        }

        source.addAll(gSource);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void addGeneratedData(java.util.Map source, final int[] rules) {
        addGeneratedData(source, rules, -1);
    }

    public static void addGeneratedData(java.util.Map source,
            final int[] rules, int year) {
        if (source == null)
            return;

        // prepare data
        java.util.Map incomes = source == null ? null : (java.util.Map) source
                .get(REGULAR_INCOME);
        if (incomes == null) {
            incomes = new java.util.TreeMap();
            source.put(ICashFlow.OBJECT_TYPE_INCOME, incomes);
        }
        java.util.Map expenses = source == null ? null : (java.util.Map) source
                .get(REGULAR_EXPENSE);
        if (expenses == null) {
            expenses = new java.util.TreeMap();
            source.put(ICashFlow.OBJECT_TYPE_EXPENSE, expenses);
        }
        java.util.Map offsets = source == null ? null : (java.util.Map) source
                .get(TAX_OFFSET);
        if (offsets == null) {
            offsets = new java.util.TreeMap();
            source.put(ICashFlow.OBJECT_TAX_OFFSET, offsets);
        }

        addGeneratedData(source, rules, incomes, expenses, offsets, year); // will
                                                                            // remove
                                                                            // any
                                                                            // generated
                                                                            // financials
                                                                            // first
    }

    public static void addGeneratedData(java.util.Map source,
            final int[] rules, java.util.Map incomes, java.util.Map expenses,
            java.util.Map offsets) {
        addGeneratedData(source, rules, incomes, expenses, offsets, -1);
    }

    public static void addGeneratedData(java.util.Map source,
            final int[] rules, java.util.Map incomes, java.util.Map expenses,
            java.util.Map offsets, int year) {
        if (source == null)
            return;

        removeGeneratedData(incomes, expenses, offsets);

        if (rules == null)
            return;

        java.util.Map cash = (java.util.Map) source.get(ASSET_CASH);
        java.util.Map investment = (java.util.Map) source.get(ASSET_INVESTMENT);
        java.util.Map superannuation = (java.util.Map) source
                .get(ASSET_SUPERANNUATION);
        java.util.Map incomestream = (java.util.Map) source
                .get(ASSET_INCOME_STREAM);
        java.util.Map liability = (java.util.Map) source.get(LIABILITY);

        java.util.ArrayList list = new java.util.ArrayList();
        if (cash != null)
            list.addAll(cash.values());
        if (investment != null)
            list.addAll(investment.values());
        if (superannuation != null)
            list.addAll(superannuation.values());
        if (incomestream != null)
            list.addAll(incomestream.values());
        if (liability != null)
            list.addAll(liability.values());

        java.util.Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Financial f = (Financial) iter.next();
            if (f == null)
                continue;

            if (year >= 0 && (f instanceof Asset || f instanceof Liability))
                f = f.project(year, 0.); // no inflation

            for (int i = 0; i < rules.length; i++) {
                Regular r = f.getRegular(rules[i]);
                if (r == null)
                    continue;

                // java.math.BigDecimal amount = r.getRegularAmount();
                // if ( amount == null || amount.doubleValue() == 0. ) continue;

                Integer objectTypeID = r.getObjectTypeID();
                if (ICashFlow.OBJECT_TYPE_INCOME.equals(objectTypeID))
                    incomes.put(r.getPrimaryKeyID(), r);
                else if (ICashFlow.OBJECT_TYPE_EXPENSE.equals(objectTypeID))
                    expenses.put(r.getPrimaryKeyID(), r);
                else if (ICashFlow.OBJECT_TAX_OFFSET.equals(objectTypeID))
                    offsets.put(r.getPrimaryKeyID(), r);
            }

        }

    }

    public static void removeGeneratedData(java.util.Map map) {
        if (map == null)
            return;

        java.util.Iterator iter = map.values().iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof java.util.Map) {
                removeGeneratedData((java.util.Map) obj);
            } else if (obj instanceof Financial) {
                Financial f = (Financial) obj;
                if (f != null && f.isGenerated()) {
                    // if (DEBUG) System.out.println( "\t*** REMOVED: " + f );
                    iter.remove();
                }

            }

        }

    }

    public static void removeGeneratedData(java.util.Collection c) {
        if (c == null)
            return;

        java.util.Iterator iter = c.iterator();
        while (iter.hasNext()) {
            Financial f = (Financial) iter.next();
            if (f != null && f.isGenerated()) {
                // if (DEBUG) System.out.println( "\t***** REMOVED: " + f );
                iter.remove();
            }

        }

    }

    private static void removeGeneratedData(java.util.Map incomes,
            java.util.Map expenses, java.util.Map offsets) {
        if (incomes != null)
            removeGeneratedData(incomes);
        if (expenses != null)
            removeGeneratedData(expenses);
        if (offsets != null)
            removeGeneratedData(offsets);
    }

}
