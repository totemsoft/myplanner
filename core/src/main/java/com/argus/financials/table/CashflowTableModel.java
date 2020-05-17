/*
 * CashflowTableModel.java
 *
 * Created on 29 November 2002, 17:24
 */

package com.argus.financials.table;

/**
 * 
 * @author valeri chibaev
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.bean.Assumptions;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.Financials;
import com.argus.financials.bean.IRegularType;
import com.argus.financials.bean.Regular;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.code.TableDisplayMode;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.ISmartTableRow;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.financials.tax.au.ITaxConstants;
import com.argus.financials.tax.au.TaxContainer;
import com.argus.math.Numeric;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public class CashflowTableModel extends FinancialTableModel {

    private static final int CLIENT = 1;

    private static final int PARTNER = 2;

    private static final int[] rulesCash = IRegularType.RULES[IRegularType.iCASHFLOW];

    private static final int[] rulesTax = IRegularType.RULES[IRegularType.iTAXANALYSIS];

    private Assumptions assumptions;

    private TaxRow taxClient;

    private TaxRow taxPartner;

    private GrandGroupFooterRow diffTotal;

    private ProxyTableModel proxy;

    public ProxyTableModel getProxy() {
        if (proxy == null)
            proxy = new ProxyTableModel(this, getDisplayColumns());
        return proxy;
    }

    protected transient static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        CashflowTableModel.clientService = clientService;
    }

    public CashflowTableModel(final java.util.Map financials,
            final Assumptions assumptions) {
        super();
        update(financials, assumptions);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void update(final java.util.Map financials,
            final Assumptions assumptions) {

        this.assumptions = assumptions;

        int length = assumptions.getYearsToProject();

        setColumnNames(getColumnNames(length));
        setColumnClasses(getColumnClasses(length));
        update(financials);

    }

    public void update(java.util.Map financials) {
        setData(initData(financials));
        // refresh
        for (int r = 0; r < getRowCount(); r++)
            for (int c = 0; c < getColumnCount(); c++) {
                getValueAt(r, c);
            }
    }

    public GrandGroupFooterRow getGrandTotal() {
        return diffTotal;
    }

    private static final int offset = 1;

    protected int getColumnIndex(int year) {
        return getPrimaryColumnIndex() + year + offset;
    }

    protected int getYear(int columnIndex) {
        return columnIndex - offset;
    }

    public int[] getDisplayColumns() {
        int[] displayColumns = assumptions.getYearsMap(assumptions
                .getYearsToProject(), assumptions.getDisplayMode());
        int[] displayColumns2 = new int[displayColumns.length + 1];

        displayColumns2[0] = 0; // first column = name, others = years
        for (int i = 0; i < displayColumns.length; i++) {
            displayColumns2[i + 1] = displayColumns[i] + 1;
        }

        return displayColumns2;
    }

    private Vector getColumnNames(int length) {

        Vector names = new Vector();

        int year = DateTimeUtils.getFinancialYearEnd();

        names.add("Title");
        int mode = assumptions.getShowTermMode();

        for (int i = 0; i < length; i++) {
            long age = java.lang.Math
                    .round(assumptions.getClientAgeAt(DateTimeUtils
                            .getDate("30/6/" + (year + i))));
            String title;
            if (mode == 0) {
                title = (age >= 1 ? "" + age : "");
            } else {
                title = "June " + (year + i);
            }

            names.add(title);

        }

        return names;

    }

    private Vector getColumnClasses(int length) {

        Vector classes = new Vector();
        classes.add(java.lang.String.class);
        for (int i = 0; i < length; i++)
            classes.add(java.math.BigDecimal.class);

        return classes;

    }

    private Vector initData(java.util.Map financials) {

        if (financials == null)
            financials = new java.util.TreeMap();

        // prepare data
        java.util.Map incomes = (java.util.Map) financials.get(REGULAR_INCOME);
        if (incomes == null) {
            incomes = new java.util.TreeMap();
            financials.put(REGULAR_INCOME, incomes);
        }
        java.util.Map expenses = (java.util.Map) financials
                .get(REGULAR_EXPENSE);
        if (expenses == null) {
            expenses = new java.util.TreeMap();
            financials.put(REGULAR_EXPENSE, expenses);
        }
        java.util.Map offsets = (java.util.Map) financials.get(TAX_OFFSET);
        if (offsets == null) {
            offsets = new java.util.TreeMap();
            financials.put(TAX_OFFSET, offsets);
        }

        Financials.addGeneratedData(financials, rulesCash, incomes, expenses,
                offsets); // will remove any generated financials first

        Vector data = new Vector();

        // incomes
        GroupHeaderRow incomeGroupHeader = new GroupHeaderRow(RC_REGULAR_INCOME);
        data.add(incomeGroupHeader); // group title
        addAll(data, incomes);
        GrandGroupFooterRow incomeGroupFooter = new GrandGroupFooterRow(
                new ReferenceCode(iREGULAR_INCOME, "Total"), incomeGroupHeader);
        data.add(incomeGroupFooter); // group total

        // expenses
        GroupHeaderRow expenseGroupHeader = new GroupHeaderRow(
                RC_REGULAR_EXPENSE);
        data.add(expenseGroupHeader); // group title
        addAll(data, expenses);

        // taxes regenerate
        Financials.addGeneratedData(financials, rulesTax, incomes, expenses,
                offsets); // will remove any generated financials first
        // create
        java.util.Collection taxRegulars = new java.util.Vector();
        // add
        taxRegulars.addAll(incomes.values());
        taxRegulars.addAll(expenses.values());
        taxRegulars.addAll(offsets.values());

        GroupHeaderRow taxGroupHeader = new GroupHeaderRow(new ReferenceCode(0,
                "Taxes"));
        data.add(taxGroupHeader);
        ClientService client = clientService;
        try {
            taxClient = new TaxRow(new ReferenceCode(CLIENT, "ClientView's tax"),
                    client, taxRegulars);
            data.add(taxClient);

            PersonService partner = client == null ? null : client.getPartner(false);
            if (partner != null) {
                taxPartner = new TaxRow(new ReferenceCode(PARTNER,
                        "Partner's tax"), partner, taxRegulars);

                data.add(taxPartner);
            }

        } catch (ServiceException e) {
            e.printStackTrace(System.err);
            // no personal data will be initialized !!! (impossible)
        }

        GrandGroupFooterRow expenseGroupFooter = new GrandGroupFooterRow(
                new ReferenceCode(iREGULAR_EXPENSE, "Total"),
                expenseGroupHeader);
        data.add(expenseGroupFooter); // group total

        // diff total
        diffTotal = new GrandGroupFooterRow(new ReferenceCode(0,
                "Surplus Cashflow (Shortfall)"));
        data.add(diffTotal); // all groups total

        // Potential Savings
        PotentialSavingsRow potentialGroupFooter = new PotentialSavingsRow(
                new ReferenceCode(0, "Accumulated Surplus (Shortfall)"));
        data.add(potentialGroupFooter);

        return data;

    }

    private void addAll(Vector dest, Map source) {
        if (source == null)
            return;

        Object[] a = source.values().toArray();
        sort(a);

        for (int i = 0; i < a.length; i++) {
            Financial f = (Financial) a[i];
            if (f != null && f instanceof Regular) {
                int financialYearEnd = DateTimeUtils.getFinancialYearEnd();
                CashflowRow row = new CashflowRow((Regular) f);
                if (row.getTotal() != 0.)
                    dest.add(row);

            }

        }

    }

    protected Numeric getNumeric(int columnIndex) {
        return money;
    }

    public Vector getColumnNames() {
        Vector names = new Vector(super.getColumnNames());
        names.remove(0); // no title
        return names;
    }

    public Vector getSeries() {

        Vector series = new Vector();
        for (int r = 0; r < getRowCount(); r++) {
            ISmartTableRow row = getRowAt(r);
            if (row instanceof CashflowRow)
                series.add(row.toString());
        }
        return series;

    }

    public double[][] getRowData() {

        int columnCount = getColumnCount();
        double[][] rowData = new double[getSeries().size()][columnCount - 1]; // no
                                                                                // title

        int i = 0;
        for (int r = 0; r < getRowCount(); r++) {
            ISmartTableRow row = getRowAt(r);
            if (!(row instanceof CashflowRow))
                continue;

            for (int c = 1; c < columnCount; c++) { // no title
                Numeric d = (Numeric) row.getValueAt(c);
                rowData[i][c - 1] = d.doubleValue();
            }
            i++;
        }
        return rowData;

    }

    public double getInflation() {
        return assumptions.getInflation();
    }

    private static int refCountCashflow = 0;

    public class CashflowRow extends FinancialRow {

        private int id = 0;

        private double[] data;

        /** Creates a new instance of CashflowRow */
        public CashflowRow(Regular r) {
            super(r);

            clear();
        }

        protected void finalize() throws Throwable {
            super.finalize();
        }

        protected Regular getRegular() {
            return (Regular) getFinancial();
        }

        //
        public void clear() {
            data = new double[assumptions.getYearsToProject()];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        private Regular temp;

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return super.getValueAt(columnIndex);

            // search for cached data first
            int year = getYear(columnIndex);
            double amount = data[year];
            if (amount == Financial.HOLE) {

                Regular r = getRegular(); // Regular
                if (r.isGenerated()) {
                    temp = (Regular) r.project(year, 0., temp); // no inflation
                    amount = temp.getFinancialYearAmount(true).doubleValue();
                } else {
                    r.setGeneratedYear(year);
                    amount = r.getFinancialYearAmount(true).doubleValue();
                    double indexation = r.getIndexation() == null ? 0. : r
                            .getIndexation().doubleValue();
                    amount *= java.lang.Math.pow((100. + indexation) / 100.,
                            year);
                    r.setGeneratedYear(0);

                }

                data[year] = amount;

            }

            return getNumeric(columnIndex).setValue(amount).round();

        }

        protected double getTotal() {
            double total = 0.;
            for (int i = 0; i < data.length; i++) {
                getValueAt(getColumnIndex(i)); // refresh
                total += data[i];
            }
            // for ( int c = 1; c <= assumptions.getYearsToProject(); c++ )
            // total += ( (Numeric) getValueAt(c) ).doubleValue();
            return total;
        }

    }

    private static int refCountTaxRow = 0;

    public class TaxRow extends AbstractSmartTableRow {

        private ReferenceCode rc;

        private TaxContainer tc;

        private java.util.Collection regulars;

        private int id = 0;

        private double[] data;

        public TaxRow(ReferenceCode rc, PersonService person,
                java.util.Collection regulars) throws ServiceException {
            super(BODY);

            this.rc = rc;
            this.regulars = regulars;

            tc = new TaxContainer();
            initTaxContainer(tc, person);

            clear();
        }

        protected void finalize() throws Throwable {
            super.finalize();
        }

        public String toString() {
            return rc.toString();
        }

        protected ReferenceCode getReferenceCode() {
            return rc;
        }

        //
        public void clear() {
            data = new double[assumptions.getYearsToProject()];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        private Regular temp;

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return toString();

            // search for cached data first
            int year = getYear(columnIndex);
            double tax = data[year];
            if (tax == Financial.HOLE) {

                // reset financial info ONLY !!! (keep personal info)
                tc.reset();

                int ownerType = rc.getId();
                double spouseIncome = 0.;

                Iterator iter = regulars.iterator();
                while (iter.hasNext()) {
                    // current year Regular
                    Regular r = (Regular) iter.next();

                    // item removed or can be ignored
                    if (r == null || !r.isTaxable())
                        continue;

                    // we do not change start/end dates
                    // projected Regular
                    double amount;
                    r.setGeneratedYear(year);
                    if (r.isGenerated()) {
                        temp = (Regular) r.project(year, 0., temp); // no inflation
                        // amount = temp.getAmount().doubleValue();
                        amount = temp.getFinancialYearAmount().doubleValue();

                    } else {
                        amount = r.getFinancialYearAmount().doubleValue();
                        double indexation = r.getIndexation() == null ? 0. : r
                                .getIndexation().doubleValue();
                        amount *= java.lang.Math.pow(
                                (100. + indexation) / 100., year);

                    }
                    r.setGeneratedYear(0);

                    double clientAmount = 0.;
                    double partnerAmount = 0.;
                    Integer ownerCodeID = r.getOwnerCodeID();
                    if (OwnerCode.CLIENT.equals(ownerCodeID)) {
                        clientAmount = amount;
                    } else if (OwnerCode.PARTNER.equals(ownerCodeID)) {
                        partnerAmount = amount;
                    } else if (OwnerCode.JOINT.equals(ownerCodeID)) {
                        clientAmount = amount / 2;
                        partnerAmount = amount / 2;
                    } else
                        continue;

                    if (ownerType == CLIENT) {
                        amount = clientAmount;
                        spouseIncome += partnerAmount;
                    } else if (ownerType == PARTNER) {
                        amount = partnerAmount;
                        spouseIncome += clientAmount;
                    }

                    String type;
                    Integer objectType = r.getObjectTypeID();
                    if (REGULAR_INCOME.equals(objectType))
                        type = TaxContainer.INCOME;
                    else if (REGULAR_EXPENSE.equals(objectType))
                        type = TaxContainer.EXPENSE;
                    else if (TAX_OFFSET.equals(objectType))
                        type = TaxContainer.OFFSET;
                    else
                        continue;

                    tc.add(type, r.getRegularTaxType(), amount);

                }

                tc.add(TaxContainer.PERSONAL, ITaxConstants.P_SPOUSE_INCOME,
                        spouseIncome);

                tc.calculate();

                tax = tc.getResult(ITaxConstants.TAX_PAYABLE);
                tax = tax == 0. ? 0. : -1 * tax;
                data[year] = tax;

            }

            return getNumeric(columnIndex).setValue(tax).round();

        }

        private void initTaxContainer(TaxContainer tc, PersonService person) throws ServiceException {
            IPerson personName = person.getPersonName();
            IPersonHealth personHealth = personName == null ? null : personName.getPersonHealth();
            boolean hospitalCover = personHealth == null ? false : personHealth.isHospitalCover();
            tc.add(TaxContainer.PERSONAL, ITaxConstants.P_HOSPITAL_COVER, hospitalCover ? 1. : 0.); // 0. = false, 1. = true
            boolean married = person == null ? false : personName.isMarried();
            tc.add(TaxContainer.PERSONAL, ITaxConstants.P_MARITAL_STATUS, married ? 1. : 0.); // 0. = false, 1. = true
            java.util.TreeMap dependants = person == null ? null : person.getDependents();
            tc.add(TaxContainer.PERSONAL, ITaxConstants.P_DEPENDANTS, dependants == null ? 0. : dependants.size());
            Number age = person == null ? null : personName.getAge();
            tc.add(TaxContainer.PERSONAL, ITaxConstants.P_AGE, age == null ? 0. : age.doubleValue());

        }

    }

    public class GrandGroupFooterRow extends GroupFooterRow {

        private double[] data;

        public GrandGroupFooterRow(ReferenceCode rc) {
            this(rc, null);
        }

        public GrandGroupFooterRow(ReferenceCode rc, GroupHeaderRow header) {
            super(rc, header);
            clear();
        }

        public void clear() {
            data = new double[assumptions.getYearsToProject()];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        // all comumns except the first one (title)
        protected double getGroupTotal(int columnIndex) {

            List list = getGroup();
            if (list == null)
                return Financial.HOLE;

            double total = 0.;
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                ISmartTableRow row = (ISmartTableRow) iter.next();
                if (row instanceof CashflowRow || row instanceof TaxRow)
                    total += ((Numeric) row.getValueAt(columnIndex))
                            .doubleValue();
            }
            data[getYear(columnIndex)] = total;

            return total;

        }

        public double[] getGroupTotals() {
            for (int i = 0; i < data.length; i++)
                if (data[i] == Financial.HOLE)
                    getGroupTotal(getColumnIndex(i));
            return data;
        }

    }

    public class PotentialSavingsRow extends GroupFooterRow {

        private double[] data;

        /** Creates a new instance of FinancialRow */
        public PotentialSavingsRow(ReferenceCode rc) {
            super(rc);
            clear();
        }

        public void clear() {
            data = new double[assumptions.getYearsToProject()];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return super.getValueAt(columnIndex);

            int year = getYear(columnIndex);
            if (data[year] == Financial.HOLE) { // re-calculate for this column
                                                // ONLY

                // This mess just to calculate total for potential savings
                // double [] diff = yearlyCTM.getGrandTotal().getGroupTotals();
                double[] diff = getGrandTotal().getGroupTotals();

                double value = 0.;
                int mode = assumptions.getDisplayMode();

                for (int i = 0; i < diff.length; i++) {

                    int modeIndex = TableDisplayMode.toModeIndex(mode, i);

                    if (modeIndex > columnIndex - 1)
                        break;
                    value += diff[i];

                }

                data[year] = value;

            }

            return getNumeric(columnIndex).setValue(data[year]);

        }

    }

}
