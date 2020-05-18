/*
 * WealthTableModel.java
 *
 * Created on 29 November 2002, 17:24
 */

package com.argus.financials.table;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.argus.financials.bean.Assumptions;
import com.argus.financials.bean.Financial;
import com.argus.financials.swing.table.ISmartTableRow;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.math.Numeric;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public class WealthTableModel extends FinancialTableModel {

    // NPV
    public static final double NPV = .03;

    private Assumptions assumptions;

    private GrandGroupFooterRow grandTotal;

    private GroupFooterRowInflated grandTotalAfterInflation;

    private ProxyTableModel proxy;

    public ProxyTableModel getProxy() {
        if (proxy == null)
            proxy = new ProxyTableModel(this, getDisplayColumns());
        return proxy;
    }

    /** Creates a new instance of WealthTableModel */
    private int length;

    public WealthTableModel(java.util.Map financials, Assumptions assumptions) {
        super();

        this.assumptions = assumptions;

        length = assumptions.getYearsMap().length;

        setColumnNames(getColumnNames(length));
        setColumnClasses(getColumnClasses(length));
        setData(initData(financials)); // last !!!

    }

    public GrandGroupFooterRow getGrandTotal() {
        return grandTotal;
    }

    public GroupFooterRowInflated getGrandTotalInflated() {
        return grandTotalAfterInflation;
    }

    public double getLastGrandTotal() {
        double[] temp = grandTotal.getGroupTotals();
        if (temp == null || temp.length == 0)
            return 0.;
        return temp[temp.length - 1];
    }

    public double getNetAssetBeforeRetirement() {
        Object value = grandTotal.getValueAt(getColumnCount() - 1);
        return value instanceof Numeric ? ((Numeric) value).doubleValue() : 0.;
    }

    public double getRealValueBeforeRetirement() {
        Object value = grandTotalAfterInflation
                .getValueAt(getColumnCount() - 1);
        return value instanceof Numeric ? ((Numeric) value).doubleValue() : 0.;
    }

    private static final int offset = 2;

    protected int getColumnIndex(int year) {
        return getPrimaryColumnIndex() + year + offset;
    }

    protected int getYear(int columnIndex) {
        // do calculations annualy (and use proxy)
        return columnIndex - offset;
    }

    public int[] getDisplayColumns() {
        int[] displayColumns = assumptions.getYearsMap(assumptions
                .getRetirementDate(), assumptions.getDisplayMode());
        int[] displayColumns2 = new int[displayColumns.length + offset];

        displayColumns2[0] = 0; // first column = name
        displayColumns2[1] = 1; // second column = current value

        // others = years
        for (int i = 0; i < displayColumns.length; i++) {
            displayColumns2[i + offset] = displayColumns[i] + offset;
        }

        return displayColumns2;
    }

    private Vector getColumnNames(int length) {

        Vector names = new Vector();

        int year = DateTimeUtils.getFinancialYearEnd();

        int mode = assumptions.getShowTermMode();
        names.add("Title");
        names.add("Current Value");
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
        classes.add(java.math.BigDecimal.class);
        for (int i = 0; i < length; i++)
            classes.add(java.math.BigDecimal.class);

        return classes;

    }

    private Vector initData(java.util.Map financials) {

        Vector data = new Vector();
        if (financials == null)
            financials = new java.util.HashMap();

        GroupHeaderRow groupHeader = new GroupHeaderRow(RC_ASSET_CASH);
        data.add(groupHeader); // group title
        addAll(data, (java.util.Map) financials.get(ASSET_CASH));
        GrandGroupFooterRow groupFooter = new GrandGroupFooterRow(
                new ReferenceCode(iASSET_CASH, "Total"), groupHeader);
        data.add(groupFooter); // group total

        groupHeader = new GroupHeaderRow(RC_ASSET_INVESTMENT);
        data.add(groupHeader); // group title
        addAll(data, (java.util.Map) financials.get(ASSET_INVESTMENT));
        groupFooter = new GrandGroupFooterRow(new ReferenceCode(
                iASSET_INVESTMENT, "Total"), groupHeader);
        data.add(groupFooter); // group total

        groupHeader = new GroupHeaderRow(RC_ASSET_SUPERANNUATION);
        data.add(groupHeader); // group title
        addAll(data, (java.util.Map) financials.get(ASSET_SUPERANNUATION));
        groupFooter = new GrandGroupFooterRow(new ReferenceCode(
                iASSET_SUPERANNUATION, "Total"), groupHeader);
        data.add(groupFooter); // group total

        groupHeader = new GroupHeaderRow(RC_INCOME_STREAM);
        data.add(groupHeader); // group title
        addAll(data, (java.util.Map) financials.get(ASSET_INCOME_STREAM));
        groupFooter = new GrandGroupFooterRow(new ReferenceCode(
                iASSET_INCOME_STREAM, "Total"), groupHeader);
        data.add(groupFooter); // group total

        // Liability must there as well
        groupHeader = new GroupHeaderRow(RC_LIABILITY);
        data.add(groupHeader); // group title
        addAll(data, (java.util.Map) financials.get(LIABILITY));
        groupFooter = new GrandGroupFooterRow(new ReferenceCode(iLIABILITY,
                "Total"), groupHeader);
        data.add(groupFooter); // group total

        grandTotal = new GrandGroupFooterRow(new ReferenceCode(0, "Net Assets"));
        if (!assumptions.getDebtRepayment()) {
            // Exclude liability from grand total
            Object[] exclude = { LIABILITY };
            grandTotal.exclude(exclude);
        }
        data.add(grandTotal); // all groups total

        grandTotalAfterInflation = new GroupFooterRowInflated(
                new ReferenceCode(0, "Real Value (today's Dollars)"),
                grandTotal);
        data.add(grandTotalAfterInflation); // after inflation

        return data;

    }

    private void addAll(Vector dest, Map source) {
        if (source == null)
            return;

        Object[] a = source.values().toArray();
        sort(a);

        for (int i = 0; i < a.length; i++) {
            Financial f = (Financial) a[i];
            if (f != null)
                dest.add(new WealthRow(f));
        }

    }

    protected Numeric getNumeric(int columnIndex) {
        return money;
    }

    public Vector getColumnNames() {
        Vector names = new Vector(super.getColumnNames());
        // no title, no current value
        for (int i = 0; i < offset; i++)
            names.remove(0);
        return names;
    }

    public Vector getSeries() {

        Vector series = new Vector();
        for (int r = 0; r < getRowCount(); r++) {
            ISmartTableRow row = getRowAt(r);
            if (row instanceof WealthRow)
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
            if (!(row instanceof WealthRow))
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

    public class WealthRow extends FinancialRow {

        transient private Financial temp;

        private double[] data;

        /** Creates a new instance of WealthRow */
        public WealthRow(Financial f) {
            super(f);

            clear();
        }

        //
        public void clear() {
            data = new double[assumptions.getYearsToProject() + offset + 1];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        public boolean isCellEditable(int columnIndex) {
            return false;
        }

        public Object getValueAt(int columnIndex) {

            if (columnIndex == 0)
                return super.getValueAt(columnIndex);

            double amount = data[columnIndex];
            if (amount == Financial.HOLE) {
                if (columnIndex == 1) {
                    amount = getFinancial().getFinancialYearAmount(true)
                            .doubleValue();

                } else {
                    temp = getFinancial().project(getYear(columnIndex), 0.,
                            temp); // no inflation
                    amount = temp.getAmount(true).doubleValue();
                }

                data[columnIndex] = amount;

            }

            return getNumeric(columnIndex).setValue(amount).round();

        }

    }

    public class GrandGroupFooterRow extends GroupFooterRow {

        private double[] data;

        private List includeList;

        private List excludeList;

        public GrandGroupFooterRow(ReferenceCode rc) {
            this(rc, null);
        }

        public GrandGroupFooterRow(ReferenceCode rc, GroupHeaderRow header) {
            super(rc, header);
            clear();
        }

        public void clear() {
            data = new double[WealthTableModel.this.getColumnCount()];// assumptions.getYearsToProject()
                                                                        // +
                                                                        // offset
                                                                        // + 1
                                                                        // ];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        public void include(Object[] list) {
            if (list != null)
                includeList = java.util.Arrays.asList(list);
        }

        public void exclude(Object[] list) {
            if (list != null)
                excludeList = java.util.Arrays.asList(list);
        }

        // all comumns except the first one (title)
        protected double getGroupTotal(int columnIndex) {

            if (columnIndex == 0)
                return Financial.HOLE;

            List list = getGroup();
            if (list == null)
                return Financial.HOLE;

            double total = 0.;
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                ISmartTableRow row = (ISmartTableRow) iter.next();
                if (row instanceof WealthRow) {

                    WealthRow wrow = (WealthRow) row;
                    boolean isInclusive = includeList == null ? true
                            : includeList.contains(wrow.getFinancial()
                                    .getObjectTypeID());
                    boolean isExclusive = excludeList == null ? false
                            : excludeList.contains(wrow.getFinancial()
                                    .getObjectTypeID());
                    if (isInclusive && !(isExclusive))
                        total += ((Numeric) row.getValueAt(columnIndex))
                                .doubleValue();
                }
            }

            data[columnIndex] = total;
            return total;

        }

        public double[] getGroupTotals() {
            for (int i = 0; i < data.length; i++)
                if (data[i] == Financial.HOLE)
                    getGroupTotal(i);

            double[] ret = new double[data.length - offset];
            System.arraycopy(data, offset, ret, 0, ret.length);
            return ret;

        }

        public double[][] getSelectedGroupTotals(Object[][] obj) {

            double[][] totals = new double[obj.length][];

            for (int j = 0; j < obj.length; j++) {
                totals[j] = new double[data.length - offset];
                Object[] selectedGroups = obj[j];
                for (int i = 0; i < totals[j].length; i++)
                    totals[j][i] = getSelectedGroupTotal(getColumnIndex(i),
                            selectedGroups);

            }

            return totals;

        }

        protected double getSelectedGroupTotal(int columnIndex,
                Object[] groupsToSum) {

            if (columnIndex == 0)
                return Financial.HOLE;

            if (groupsToSum == null)
                return Financial.HOLE;
            List searchableList = java.util.Arrays.asList(groupsToSum);

            List list = getGroup();
            if (list == null)
                return Financial.HOLE;

            double total = 0.;
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                ISmartTableRow row = (ISmartTableRow) iter.next();
                if (row instanceof WealthRow) {
                    Financial f = ((WealthRow) row).getFinancial();
                    if (searchableList.contains(f.getObjectTypeID()))
                        total += ((Numeric) row.getValueAt(columnIndex))
                                .doubleValue();
                }

            }

            return java.lang.Math.abs(total);

        }

    }

    public class GroupFooterRowInflated extends GroupFooterRow {

        private double[] data;

        public GroupFooterRowInflated(ReferenceCode rc, GroupHeaderRow header) {
            super(rc, header);
            clear();
        }

        public void clear() {
            data = new double[WealthTableModel.this.getColumnCount()];// assumptions.getYearsToProject()
                                                                        // +
                                                                        // offset
                                                                        // + 1
                                                                        // ];
            for (int i = 0; i < data.length; i++)
                data[i] = Financial.HOLE;
        }

        // all comumns except the first one (title)
        protected double getGroupTotal(int columnIndex) {

            // title
            if (columnIndex == 0)
                return Financial.HOLE;

            Numeric value = (Numeric) header.getValueAt(columnIndex);

            double d;
            if (columnIndex == 1)
                // current value
                d = value.doubleValue();
            else
                // S.P. value / (1+0.03)^year does not depends on inflation
                d = value.doubleValue()
                        / java.lang.Math
                                .pow(1. + NPV, getYear(columnIndex) + 1);

            data[columnIndex] = d;

            return d;

        }

        public double[] getGroupTotals() {
            for (int i = 0; i < data.length; i++)
                if (data[i] == Financial.HOLE)
                    getGroupTotal(i);

            double[] ret = new double[data.length - offset];
            System.arraycopy(data, offset, ret, 0, ret.length);
            return ret;

        }

    }

}
