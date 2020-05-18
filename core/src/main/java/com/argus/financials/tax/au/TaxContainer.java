/*
 * TaxContainer.java
 *
 * Created on 4 September 2002, 21:02
 */

package com.argus.financials.tax.au;

import java.util.ArrayList;

public class TaxContainer {

    /* Collection's identefiers */
    public static final String INCOME = "Income";

    public static final String ASSESSABLE = "Assessable";

    public static final String EXPENSE = "Expense";

    public static final String TAX = "Tax";

    public static final String PERSONAL = "Personal";

    public static final String OFFSET = "Offset";

    /** Creates new TaxContainer */
    public TaxContainer() {

        incomeCollection = new java.util.ArrayList();
        expenseCollection = new java.util.ArrayList();
        personalCollection = new java.util.ArrayList();
        taxCollection = new java.util.ArrayList();
        offsetCollection = new java.util.ArrayList();

        ruleCollection = new java.util.ArrayList();
        taxableCollection = new java.util.ArrayList();
    }

    public void add(String streamType, String elementType, double amount) {

        if (streamType == null || elementType == null)
            return;

        TaxElement newElement = new TaxElement();
        newElement.setType(elementType);
        newElement.setAmount(amount);
        if (streamType.equals(INCOME)) {
            incomeCollection.add(newElement);
        } else if (streamType.equals(EXPENSE)) {
            expenseCollection.add(newElement);
        } else if (streamType.equals(PERSONAL)) {
            personalCollection.add(newElement);
        } else if (streamType.equals(TAX)) {
            taxCollection.add(newElement);
        } else if (streamType.equals(OFFSET)) {
            offsetCollection.add(newElement);
        }

    }

    public void remove(String collectionName, String elementType) {

        ArrayList itemCollection;

        if (collectionName == null || elementType == null)
            return;

        if (collectionName.equals(this.EXPENSE)) {
            itemCollection = this.expenseCollection;
        } else if (collectionName.equals(this.INCOME)) {
            itemCollection = this.incomeCollection;
        } else if (collectionName.equals(this.ASSESSABLE)) {
            itemCollection = this.taxableCollection;
        } else if (collectionName.equals(this.PERSONAL)) {
            itemCollection = this.personalCollection;
        } else if (collectionName.equals(this.TAX)) {
            itemCollection = this.taxCollection;
        } else if (collectionName.equals(this.OFFSET)) {
            itemCollection = this.offsetCollection;
        } else {
            return;
        }

        for (int i = 0; i < itemCollection.size(); i++) {
            TaxElement te = (TaxElement) itemCollection.get(i);
            String name = te.getType();
            if (name.equals(elementType)) {
                itemCollection.remove(i);
            }

        }

    }

    public void add(String streamType, String elementType, double amount,
            double taxWithHeld) {

        if (streamType == null || elementType == null)
            return;

        TaxElement newElement = new TaxElement();
        newElement.setType(elementType);
        newElement.setAmount(amount);
        newElement.setTaxWithHeld(taxWithHeld);

        if (streamType.equals(INCOME)) {
            incomeCollection.add(newElement);
        } else if (streamType.equals(EXPENSE)) {
            expenseCollection.add(newElement);
        } else if (streamType.equals(PERSONAL)) {
            personalCollection.add(newElement);
        } else if (streamType.equals(OFFSET)) {
            offsetCollection.add(newElement);
        } else if (streamType.equals(TAX)) {
            taxCollection.add(newElement);
        }

    }

    public void setIncomeCollection(java.util.ArrayList incomeCollection) {
        this.incomeCollection = incomeCollection;
    }

    public void setExpenseCollection(java.util.ArrayList expenseCollection) {
        this.expenseCollection = expenseCollection;
    }

    public void setPersonalCollection(java.util.ArrayList personalCollection) {
        this.personalCollection = personalCollection;
    }

    public void setOffsetCollection(java.util.ArrayList offsetCollection) {
        this.offsetCollection = offsetCollection;
    }

    public ArrayList getIncomeCollection() {
        return this.incomeCollection;
    }

    public ArrayList getTaxCollection() {
        return this.taxCollection;
    }

    public ArrayList getTaxableCollection() {
        return this.taxableCollection;
    }

    public ArrayList getOffsetCollection() {
        return this.offsetCollection;
    }

    public void addTaxCollection(TaxElement element) {
        this.taxCollection.add(element);
    }

    public void addTaxable(TaxElement element) {
        this.taxableCollection.add(element);
    }

    public void addRule(int index, TaxRule rule) {
        // Register tax contaner with the rule
        ruleCollection.add(index, rule);
        rule.register(this);
    }

    public void removeRule(int index) {
        ruleCollection.remove(index);
    }

    public void calculate() {
        // reflection can be used to load this sequence dynamicaly
        addRule(0, new TaxableIncome());
        addRule(1, new TaxGross());
        addRule(2, new TaxOffsets());
        addRule(3, new TaxPayable());

        for (int i = 0; i < ruleCollection.size(); i++) {
            TaxRule tr = (TaxRule) ruleCollection.get(i);
            tr.apply();

        }

    }

    public double getResult(String resultType) {
        double result = 0.00;

        if (resultType.equals(ITaxConstants.TAX_ON_INVESTMENT_INCOME)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.TAX_PAYABLE })
                    - sumCollection(
                            TAX,
                            new String[] { ITaxConstants.TAX_PAYABLE_NON_INVESTMENT });
        } else if (resultType.equals(ITaxConstants.TAX_PAYABLE)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.TAX_PAYABLE });

        } else if (resultType.equals(ITaxConstants.ASSESSABLE_INCOME)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.TAXABLE_INCOME })
                    - sumCollection(INCOME,
                            new String[] { ITaxConstants.I_OTHER });
        } else if (resultType.equals(ITaxConstants.O_AVERAGE_TAX_RATE)) {

            result = ITaxConstants.MEDICARE_LEVY;
            if (sumCollection(TAX, new String[] { ITaxConstants.MLS }) > 0) {
                result = result + ITaxConstants.MEDICARE_LEVY_SURCHARGE;
            }
            result = result
                    + maxCollection(ASSESSABLE,
                            new String[] { ITaxConstants.ASSESSABLE_INCOME });
        } else if (resultType.equals(ITaxConstants.TAX_ON_TAXABLE_INCOME)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.TAX_ON_TAXABLE_INCOME });
        } else if (resultType.equals(ITaxConstants.ML)) {
            result = sumCollection(TAX, new String[] { ITaxConstants.ML });
        } else if (resultType.equals(ITaxConstants.MLS)) {
            result = sumCollection(TAX, new String[] { ITaxConstants.MLS });
        } else if (resultType.equals(ITaxConstants.TAXABLE_INCOME)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.TAXABLE_INCOME });
        } else if (resultType.equals(ITaxConstants.O_TAX_WITHHELD)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.O_TAX_WITHHELD });
        } else if (resultType.equals(ITaxConstants.O_IMPUTATION_CREDIT)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.O_IMPUTATION_CREDIT });
        } else if (resultType.equals(ITaxConstants.O_LUMP_SUM_ETP)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.O_LUMP_SUM_ETP });
        } else if (resultType.equals(ITaxConstants.O_LOW_INCOME)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.O_LOW_INCOME });

        } else if (resultType.equals(ITaxConstants.I_TOTAL_INCOME)) {
            result = sumCollection(TAX,
                    new String[] { ITaxConstants.I_TOTAL_INCOME });
        } else if (resultType.equals(ITaxConstants.I_ETP_NON_EXCESSIVE)) {
            result = sumCollection(INCOME,
                    new String[] { ITaxConstants.I_ETP_NON_EXCESSIVE });

        }

        return result;
    }

    public String toString() {

        String desc = "IncomeCollection :\n";
        for (int i = 0; i < incomeCollection.size(); i++) {
            TaxElement te = (TaxElement) incomeCollection.get(i);
            desc = desc + te.toString() + "\n";
        }

        desc = desc + "expenseCollection :\n";
        for (int i = 0; i < expenseCollection.size(); i++) {
            TaxElement te = (TaxElement) expenseCollection.get(i);
            desc = desc + te.toString() + "\n";
        }
        desc = desc + "taxableCollection  :\n";
        for (int i = 0; i < taxableCollection.size(); i++) {
            TaxElement te = (TaxElement) taxableCollection.get(i);
            desc = desc + te.toString() + "\n";
        }

        desc = desc + "taxCollection :\n";
        for (int i = 0; i < taxCollection.size(); i++) {
            TaxElement te = (TaxElement) taxCollection.get(i);
            desc = desc + te.toString() + "\n";
        }

        return desc;
    }

    public double sumCollection(String collectionName, String[] items) {

        double sum = 0;

        if (items.length == 0)
            return sum;

        ArrayList itemCollection;

        if (collectionName.equals(this.EXPENSE)) {
            itemCollection = this.expenseCollection;
        } else if (collectionName.equals(this.INCOME)) {
            itemCollection = this.incomeCollection;
        } else if (collectionName.equals(this.ASSESSABLE)) {
            itemCollection = this.taxableCollection;
        } else if (collectionName.equals(this.PERSONAL)) {
            itemCollection = this.personalCollection;
        } else if (collectionName.equals(this.TAX)) {
            itemCollection = this.taxCollection;
        } else if (collectionName.equals(this.OFFSET)) {
            itemCollection = this.offsetCollection;

        } else {
            return sum;
        }

        for (int i = 0; i < itemCollection.size(); i++) {
            TaxElement te = (TaxElement) itemCollection.get(i);
            String name = te.getType();
            if (search(items, name) >= 0) {
                sum = sum + te.getAmount();
            }

        }

        return sum;
    }

    public double maxCollection(String collectionName, String[] items) {

        double max = 0;

        if (items.length == 0)
            return max;

        ArrayList itemCollection;

        if (collectionName.equals(this.EXPENSE)) {
            itemCollection = this.expenseCollection;
        } else if (collectionName.equals(this.INCOME)) {
            itemCollection = this.incomeCollection;
        } else if (collectionName.equals(this.ASSESSABLE)) {
            itemCollection = this.taxableCollection;
        } else if (collectionName.equals(this.PERSONAL)) {
            itemCollection = this.personalCollection;
        } else if (collectionName.equals(this.TAX)) {
            itemCollection = this.taxCollection;
        } else {
            return max;
        }

        for (int i = 0; i < itemCollection.size(); i++) {
            TaxElement te = (TaxElement) itemCollection.get(i);
            String name = te.getType();
            if (search(items, name) >= 0) {

                max = java.lang.Math.max(max, te.getTaxRate());
            }

        }

        return max;
    }

    public int search(String[] items, String key) {
        if (key == null || items == null || items.length == 0)
            return -1;
        for (int i = 0; i < items.length; i++) {
            if (key.equals(items[i]))
                return i;
        }
        return -1;
    }

    public void reset() {

        incomeCollection = new java.util.ArrayList();
        expenseCollection = new java.util.ArrayList();
        // personalCollection = new java.util.ArrayList() ;
        taxCollection = new java.util.ArrayList();
        offsetCollection = new java.util.ArrayList();
        ruleCollection = new java.util.ArrayList();
        taxableCollection = new java.util.ArrayList();
    }

    private ArrayList incomeCollection;

    private ArrayList expenseCollection;

    private ArrayList personalCollection;

    private ArrayList taxCollection;

    private ArrayList ruleCollection;

    private ArrayList offsetCollection;

    private ArrayList taxableCollection;
}
