/*
 * FinancialTableModel2.java
 *
 * Created on 25 March 2003, 12:31
 */

package au.com.totemsoft.myplanner.table.model;

import au.com.totemsoft.math.AverageFunction;
import au.com.totemsoft.math.SumFunction;
import au.com.totemsoft.myplanner.assetallocation.AssetAllocation;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.IRegularType;
import au.com.totemsoft.myplanner.bean.Regular;
import au.com.totemsoft.myplanner.code.OwnerCode;
import au.com.totemsoft.myplanner.table.swing.ISmartTableModel;
import au.com.totemsoft.myplanner.table.swing.ISmartTableRow;
import au.com.totemsoft.myplanner.tree.FinancialTreeFilter;
import au.com.totemsoft.myplanner.tree.FinancialTreeModel;
import au.com.totemsoft.myplanner.tree.FinancialTreeStructure;

public class FinancialTableModel2 extends javax.swing.table.AbstractTableModel
        implements ISmartTableModel, javax.swing.event.TreeModelListener {

    private static java.util.Vector columnNames;

    private static java.util.Vector columnClasses;
    static {
        columnNames = new java.util.Vector(FinancialColumnID.COUNT);
        columnNames.add(FinancialColumnID.NAME, "Name");
        columnNames.add(FinancialColumnID.OBJECT_TYPE, "Object Type");
        columnNames.add(FinancialColumnID.FINANCIAL_TYPE, "Financial Type");
        columnNames.add(FinancialColumnID.FINANCIAL_CODE, "Financial Code");
        columnNames.add(FinancialColumnID.OWNER, "Owner");
        columnNames.add(FinancialColumnID.AMOUNT, "Current Value");
        columnNames.add(FinancialColumnID.INSTITUTION, "Institution");
        columnNames.add(FinancialColumnID.COUNTRY, "Country");
        columnNames.add(FinancialColumnID.DESCRIPTION, "Description");
        columnNames.add(FinancialColumnID.START_DATE, "Start Date");
        columnNames.add(FinancialColumnID.END_DATE, "End Date");
        columnNames.add(FinancialColumnID.FRANKED, "Franked");
        columnNames.add(FinancialColumnID.TAX_FREE_DEFERRED,
                "Tax Free Deferred");
        columnNames.add(FinancialColumnID.CAPITAL_GROWTH, "Capital Growth (%)");
        columnNames.add(FinancialColumnID.INCOME, "Income (%)");
        columnNames.add(FinancialColumnID.UPFRONT_FEE, "Upfront Fee (%)");
        columnNames.add(FinancialColumnID.ONGOING_FEE, "Ongoing Fee (%)");
        columnNames.add(FinancialColumnID.DEDUCTIBLE, "Deductible");
        columnNames.add(FinancialColumnID.DEDUCTIBLE_DSS, "Deductible DSS");
        columnNames.add(FinancialColumnID.COMPLYING_DSS, "Complying for DSS");
        columnNames.add(FinancialColumnID.INDEXATION, "Indexation");
        columnNames.add(FinancialColumnID.EXPENSE, "Expense");
        columnNames.add(FinancialColumnID.REBATEABLE, "Rebateable");

        columnNames.add(FinancialColumnID.ACCOUNT_NUMBER, "Account Number");
        columnNames.add(FinancialColumnID.FUND_TYPE, "Fund Type");
        columnNames.add(FinancialColumnID.INVESTMENT_TYPE, "Investment Type");
        columnNames.add(FinancialColumnID.UNITS_SHARES, "Units");
        columnNames.add(FinancialColumnID.UNITS_SHARES_PRICE, "Unit Price");
        columnNames.add(FinancialColumnID.UNITS_SHARES_PRICE_DATE,
                "Unit Price Date");
        columnNames.add(FinancialColumnID.PURCHASE_COST, "Purchase Cost");
        columnNames.add(FinancialColumnID.REPLACEMENT_VALUE,
                "Replacement Value");
        columnNames.add(FinancialColumnID.TAX_DEDUCTIBLE, "Tax Deductible");
        columnNames.add(FinancialColumnID.FREQUENCY, "Regular Frequency");
        columnNames.add(FinancialColumnID.REGULAR_AMOUNT, "Regular Amount");
        columnNames.add(FinancialColumnID.ANNUAL_AMOUNT, "Annual Amount");
        columnNames.add(FinancialColumnID.CONTRIBUTION, "Contribution");
        columnNames.add(FinancialColumnID.CONTRIBUTION_INDEXATION,
                "Contribution Indexation");
        columnNames.add(FinancialColumnID.CONTRIBUTION_START_DATE,
                "Contribution Start Date");
        columnNames.add(FinancialColumnID.CONTRIBUTION_END_DATE,
                "Contribution End Date");
        columnNames.add(FinancialColumnID.DRAWDOWN, "Drawdown");
        columnNames.add(FinancialColumnID.DRAWDOWN_INDEXATION,
                "Drawdown Indexation");
        columnNames.add(FinancialColumnID.DRAWDOWN_START_DATE,
                "Drawdown Start Date");
        columnNames.add(FinancialColumnID.DRAWDOWN_END_DATE,
                "Drawdown End Date");

        columnNames.add(FinancialColumnID.ASSOCIATED_FINANCIAL,
                "Associated ...");
        columnNames.add(FinancialColumnID.TAXABLE, "Taxable/Deductible");
        columnNames.add(FinancialColumnID.TAX_TYPE, "Tax Type");

        columnNames.add(FinancialColumnID.INTEREST, "Interest Rate");

        // two extra columns to show client/partner amount
        columnNames.add(FinancialColumnID.AMOUNT_CLIENT, "ClientView's Amount");
        columnNames.add(FinancialColumnID.AMOUNT_PARTNER, "Partner's Amount");

        // three extra columns to show client/partner financial year amount
        columnNames.add(FinancialColumnID.REAL_AMOUNT, "Financial Year Amount");
        columnNames.add(FinancialColumnID.REAL_AMOUNT_CLIENT,
                "ClientView's Financial Year Amount");
        columnNames.add(FinancialColumnID.REAL_AMOUNT_PARTNER,
                "Partner's Financial Year Amount");

        // eight extra columns to show asset allocation
        columnNames.add(FinancialColumnID.AA_CASH, "Cash (%)");
        columnNames.add(FinancialColumnID.AA_FIXED_INTEREST,
                "Fixed Interest (%)");
        columnNames.add(FinancialColumnID.AA_AUST_SHARES, "Aust. Shares (%)");
        columnNames.add(FinancialColumnID.AA_INTNL_SHARES, "Intnl. Shares (%)");
        columnNames.add(FinancialColumnID.AA_PROPERTY, "Property (%)");
        columnNames.add(FinancialColumnID.AA_OTHER, "Other (%)");
        columnNames.add(FinancialColumnID.AA_TOTAL, "Total (%)");
        columnNames.add(FinancialColumnID.AA_INCLUDE, "Include");

        columnNames.add(FinancialColumnID.TOTAL_RETURN,
                "Combined (Income/Growth) (%)");
        columnNames.add(FinancialColumnID.GROSS_INCOME,
                IRegularType.sGROSS_INCOME);

        columnNames.add(FinancialColumnID.FINANCIAL_SERVICE, "Service");

        columnNames.add(FinancialColumnID.GROSS_INCOME_CLIENT,
                "ClientView Gross Income");
        columnNames.add(FinancialColumnID.GROSS_INCOME_PARTNER,
                "Partner Gross Income");

        columnNames.add(FinancialColumnID.UNFRANKED_INCOME,
                IRegularType.sUNFRANKED_INCOME);

        columnNames.add(FinancialColumnID.UNFRANKED_INCOME_CLIENT,
                "ClientView Unfranked Income");
        columnNames.add(FinancialColumnID.UNFRANKED_INCOME_PARTNER,
                "Partner Unfranked Income");

        columnClasses = new java.util.Vector(FinancialColumnID.COUNT);
        columnClasses.add(FinancialColumnID.NAME, java.lang.String.class);
        columnClasses
                .add(FinancialColumnID.OBJECT_TYPE, java.lang.String.class);
        columnClasses.add(FinancialColumnID.FINANCIAL_TYPE,
                java.lang.String.class);
        columnClasses.add(FinancialColumnID.FINANCIAL_CODE,
                java.lang.String.class);
        columnClasses.add(FinancialColumnID.OWNER, java.lang.String.class);
        columnClasses.add(FinancialColumnID.AMOUNT, java.math.BigDecimal.class);
        columnClasses
                .add(FinancialColumnID.INSTITUTION, java.lang.String.class);
        columnClasses.add(FinancialColumnID.COUNTRY, java.lang.String.class);
        columnClasses
                .add(FinancialColumnID.DESCRIPTION, java.lang.String.class);
        columnClasses.add(FinancialColumnID.START_DATE, java.util.Date.class);
        columnClasses.add(FinancialColumnID.END_DATE, java.util.Date.class);
        columnClasses
                .add(FinancialColumnID.FRANKED, java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.TAX_FREE_DEFERRED,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.CAPITAL_GROWTH,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.INCOME, java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.UPFRONT_FEE,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.ONGOING_FEE,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.DEDUCTIBLE,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.DEDUCTIBLE_DSS,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.COMPLYING_DSS,
                java.lang.Boolean.class);
        columnClasses.add(FinancialColumnID.INDEXATION,
                java.math.BigDecimal.class);
        columnClasses
                .add(FinancialColumnID.EXPENSE, java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.REBATEABLE,
                java.math.BigDecimal.class);

        columnClasses.add(FinancialColumnID.ACCOUNT_NUMBER,
                java.lang.String.class);
        columnClasses.add(FinancialColumnID.FUND_TYPE, java.lang.String.class);
        columnClasses.add(FinancialColumnID.INVESTMENT_TYPE,
                java.lang.String.class);
        columnClasses.add(FinancialColumnID.UNITS_SHARES,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.UNITS_SHARES_PRICE,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.UNITS_SHARES_PRICE_DATE,
                java.util.Date.class);
        columnClasses.add(FinancialColumnID.PURCHASE_COST,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.REPLACEMENT_VALUE,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.TAX_DEDUCTIBLE,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.FREQUENCY, java.lang.String.class);
        columnClasses.add(FinancialColumnID.REGULAR_AMOUNT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.ANNUAL_AMOUNT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.CONTRIBUTION,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.CONTRIBUTION_INDEXATION,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.CONTRIBUTION_START_DATE,
                java.util.Date.class);
        columnClasses.add(FinancialColumnID.CONTRIBUTION_END_DATE,
                java.util.Date.class);
        columnClasses.add(FinancialColumnID.DRAWDOWN,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.DRAWDOWN_INDEXATION,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.DRAWDOWN_START_DATE,
                java.util.Date.class);
        columnClasses.add(FinancialColumnID.DRAWDOWN_END_DATE,
                java.util.Date.class);

        columnClasses.add(FinancialColumnID.ASSOCIATED_FINANCIAL,
                java.lang.Object.class);
        columnClasses.add(FinancialColumnID.TAXABLE, java.lang.Boolean.class);
        columnClasses.add(FinancialColumnID.TAX_TYPE, java.lang.String.class);

        columnClasses.add(FinancialColumnID.INTEREST,
                java.math.BigDecimal.class);

        // two extra columns to show client/partner amount
        columnClasses.add(FinancialColumnID.AMOUNT_CLIENT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.AMOUNT_PARTNER,
                java.math.BigDecimal.class);

        // three extra columns to show client/partner financial year amount
        columnClasses.add(FinancialColumnID.REAL_AMOUNT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.REAL_AMOUNT_CLIENT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.REAL_AMOUNT_PARTNER,
                java.math.BigDecimal.class);

        // eight extra columns to show asset allocation
        columnClasses.add(FinancialColumnID.AA_CASH, Double.class);
        columnClasses.add(FinancialColumnID.AA_FIXED_INTEREST, Double.class);
        columnClasses.add(FinancialColumnID.AA_AUST_SHARES, Double.class);
        columnClasses.add(FinancialColumnID.AA_INTNL_SHARES, Double.class);
        columnClasses.add(FinancialColumnID.AA_PROPERTY, Double.class);
        columnClasses.add(FinancialColumnID.AA_OTHER, Double.class);
        columnClasses.add(FinancialColumnID.AA_TOTAL, Double.class);
        columnClasses.add(FinancialColumnID.AA_INCLUDE, Boolean.class);

        columnClasses.add(FinancialColumnID.TOTAL_RETURN,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.GROSS_INCOME,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.FINANCIAL_SERVICE,
                java.lang.String.class);
        columnClasses.add(FinancialColumnID.GROSS_INCOME_CLIENT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.GROSS_INCOME_PARTNER,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.UNFRANKED_INCOME,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.UNFRANKED_INCOME_CLIENT,
                java.math.BigDecimal.class);
        columnClasses.add(FinancialColumnID.UNFRANKED_INCOME_PARTNER,
                java.math.BigDecimal.class);

    }

    protected static final au.com.totemsoft.format.Number2 number4;

    protected static final au.com.totemsoft.math.Money money;

    protected static final au.com.totemsoft.math.Percent percent;
    static {
        number4 = au.com.totemsoft.format.Number2.createInstance();
        number4.setMaximumFractionDigits(4);
        number4.setMinimumFractionDigits(4);

        money = new au.com.totemsoft.math.Money();
        money.setMaximumFractionDigits(0);
        money.setMinimumFractionDigits(0);

        percent = new au.com.totemsoft.math.Percent();

    }

    private FinancialTreeModel model;

    protected java.util.Vector data;

    /** Creates a new instance of FinancialTableModel2 */
    protected FinancialTableModel2() {
    }

    public FinancialTableModel2(java.util.Map financials) {
        this(financials, null, null);
    }

    public FinancialTableModel2(java.util.Map financials, Object[] groups) {
        this(financials, groups, null);
    }

    public FinancialTableModel2(java.util.Map financials, Object[] groups,
            final int[] rules) {
        model = new FinancialTreeModel(financials);
        // init();

        FinancialTreeStructure fts = new FinancialTreeStructure();
        for (int i = 0; groups != null && i < groups.length; i++)
            fts.add(groups[i]);
        model.setStructure(fts);

        model.setFilters(null);

        model.setRules(rules);

        model.build();

        init();
    }

    public FinancialTableModel2(FinancialTreeModel model) {
        setFinancialTreeModel(model);
    }

    public FinancialTreeModel getFinancialTreeModel() {
        return model;
    }

    public void setFinancialTreeModel(FinancialTreeModel model) {
        if (model != null && this.model == model)
            return;

        this.model = model;
        init();
    }

    public void build() {
        model.build();
    }

    public void build(int year) {
        model.build(year);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init() {

        data = new java.util.Vector();

        FinancialTreeModel.INode root = (FinancialTreeModel.INode) model
                .getRoot();
        FinancialTreeModel.INode[] nodes = root.getChildren();
        for (int i = 0; i < nodes.length; i++)
            addNodes(nodes[i]);

    }

    private void addNodes(FinancialTreeModel.INode node) {

        if (node.isLeaf()) {

            Financial f = ((FinancialTreeModel.Node) node).getFinancial();
            data.add(new FinancialRow(f));

        } else {

            HeaderRow headerRow = new HeaderRow(getRowTypeHeader(node
                    .getLevel()), node);
            data.add(headerRow);

            FinancialTreeModel.INode[] nodes = node.getChildren();
            for (int i = 0; i < nodes.length; i++)
                addNodes(nodes[i]);

            FooterRow footerRow = new FooterRow(headerRow);
            data.add(footerRow);

        }

    }

    /***************************************************************************
     * TableModel interface
     **************************************************************************/
    public int getColumnCount() {
        return columnNames.size();
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int columnIndex) {
        // System.out.println( "getColumnName( " + columnIndex + " )=" +
        // columnNames.get( columnIndex ) );
        return (String) columnNames.get(columnIndex);
    }

    public Class getColumnClass(int columnIndex) {
        // System.out.println( "getColumnClass( " + columnIndex + " )=" +
        // columnClasses.get( columnIndex ) );
        return (Class) columnClasses.get(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ISmartTableRow row = getRowAt(rowIndex);
        // System.out.println( "row=" + row );
        return row == null ? null : row.getValueAt(columnIndex);
    }

    /***************************************************************************
     * ISmartTableModel interface
     **************************************************************************/
    public ISmartTableRow getRowAt(int rowIndex) {
        if (rowIndex >= getRowCount()) {
            java.lang.System.err.println("FinancialTableModel2::getRowAt( "
                    + rowIndex + " ), rowIndex >= " + getRowCount());
            return null;
        }
        return (ISmartTableRow) data.elementAt(rowIndex);
    }

    public void setRowAt(ISmartTableRow aValue, int rowIndex) {
        // not editable
    }

    public void pack() {
        ((FinancialTreeModel.INode) model.getRoot()).pack();
    }

    private int getRowIndex(ISmartTableRow row) {

        for (int r = 0; r < getRowCount(); r++)
            if (getRowAt(r) == row)
                return r;
        return -1;

    }

    /***************************************************************************
     * UtilityService functions for Calculators (SP)
     **************************************************************************/
    public Object getValue(int columnIndex, FinancialTreeFilter filter) {

        switch (columnIndex) {
        case FinancialColumnID.NAME:
            return null;
            // SumFunction columns
        case FinancialColumnID.AMOUNT:
        case FinancialColumnID.AMOUNT_CLIENT:
        case FinancialColumnID.AMOUNT_PARTNER:
        case FinancialColumnID.ANNUAL_AMOUNT:
        case FinancialColumnID.REAL_AMOUNT:
        case FinancialColumnID.REAL_AMOUNT_CLIENT:
        case FinancialColumnID.REAL_AMOUNT_PARTNER:
        case FinancialColumnID.GROSS_INCOME:
        case FinancialColumnID.GROSS_INCOME_CLIENT:
        case FinancialColumnID.GROSS_INCOME_PARTNER:
        case FinancialColumnID.UNFRANKED_INCOME:
        case FinancialColumnID.UNFRANKED_INCOME_CLIENT:
        case FinancialColumnID.UNFRANKED_INCOME_PARTNER:
            return new java.math.BigDecimal(new SumFunction(getNumericData(
                    columnIndex, filter)).calculate());
            // AverageFunction columns
        case FinancialColumnID.REGULAR_AMOUNT:
            return new java.math.BigDecimal(new AverageFunction(getNumericData(
                    columnIndex, filter)).calculate());
        case FinancialColumnID.OBJECT_TYPE:
        case FinancialColumnID.FINANCIAL_TYPE:
        case FinancialColumnID.FINANCIAL_CODE:
        case FinancialColumnID.FINANCIAL_SERVICE:
        case FinancialColumnID.OWNER:
        case FinancialColumnID.INSTITUTION:
        case FinancialColumnID.COUNTRY:
        case FinancialColumnID.DESCRIPTION:
        case FinancialColumnID.START_DATE:
        case FinancialColumnID.END_DATE:
        case FinancialColumnID.FRANKED:
        case FinancialColumnID.TAX_FREE_DEFERRED:
        case FinancialColumnID.CAPITAL_GROWTH:
        case FinancialColumnID.INCOME:
        case FinancialColumnID.TOTAL_RETURN:
        case FinancialColumnID.UPFRONT_FEE:
        case FinancialColumnID.ONGOING_FEE:
        case FinancialColumnID.DEDUCTIBLE:
        case FinancialColumnID.DEDUCTIBLE_DSS:
        case FinancialColumnID.COMPLYING_DSS:
        case FinancialColumnID.INDEXATION:
        case FinancialColumnID.EXPENSE:
        case FinancialColumnID.REBATEABLE:
        case FinancialColumnID.ACCOUNT_NUMBER:
        case FinancialColumnID.FUND_TYPE:
        case FinancialColumnID.INVESTMENT_TYPE:
        case FinancialColumnID.UNITS_SHARES:
        case FinancialColumnID.UNITS_SHARES_PRICE:
        case FinancialColumnID.UNITS_SHARES_PRICE_DATE:
        case FinancialColumnID.PURCHASE_COST:
        case FinancialColumnID.REPLACEMENT_VALUE:
        case FinancialColumnID.TAX_DEDUCTIBLE:
        case FinancialColumnID.FREQUENCY:
        case FinancialColumnID.CONTRIBUTION:
        case FinancialColumnID.CONTRIBUTION_INDEXATION:
        case FinancialColumnID.CONTRIBUTION_START_DATE:
        case FinancialColumnID.CONTRIBUTION_END_DATE:
        case FinancialColumnID.DRAWDOWN:
        case FinancialColumnID.DRAWDOWN_INDEXATION:
        case FinancialColumnID.DRAWDOWN_START_DATE:
        case FinancialColumnID.DRAWDOWN_END_DATE:
        case FinancialColumnID.ASSOCIATED_FINANCIAL:
        case FinancialColumnID.TAXABLE:
        case FinancialColumnID.TAX_TYPE:
        case FinancialColumnID.INTEREST:
        default:
            return null;
        }

    }

    /**
     * BODY only (FinancialRow)
     */
    public Object[] getValues(int column, FinancialTreeFilter filter) {

        java.util.Collection _data = new java.util.ArrayList();
        for (int i = 0; i < getRowCount(); i++) {
            ISmartTableRow row = getRowAt(i);
            if (row instanceof FinancialRow
                    && (filter == null || filter
                            .inGroup(((FinancialRow) row).f))) {
                Object value = row.getValueAt(column);
                _data.add(value);
                continue;

            }

        }

        return _data.toArray();

    }

    public Financial[] getFinancials(FinancialTreeFilter filter) {

        java.util.Collection _data = new java.util.ArrayList();
        for (int i = 0; i < getRowCount(); i++) {
            ISmartTableRow row = getRowAt(i);
            if (row instanceof FinancialRow
                    && (filter == null || filter
                            .inGroup(((FinancialRow) row).f)))
                _data.add(((FinancialRow) row).f);
        }
        return (au.com.totemsoft.myplanner.bean.Financial[]) _data
                .toArray(new Financial[0]);

    }

    protected java.lang.Number[] getNumericData(int columnIndex) {
        return getNumericData(columnIndex, null);
    }

    protected java.lang.Number[] getNumericData(int columnIndex,
            FinancialTreeFilter filter) {

        java.util.Collection _data = new java.util.ArrayList();
        for (int i = 0; i < getRowCount(); i++) {
            ISmartTableRow row = getRowAt(i);
            if (row instanceof FinancialRow
                    && (filter == null || filter
                            .inGroup(((FinancialRow) row).f))) {
                Object value = row.getValueAt(columnIndex);

                if (value == null)
                    continue;

                if (value instanceof java.lang.Number) {
                    _data.add(value);
                    continue;

                } else if (value instanceof au.com.totemsoft.math.Numeric) {
                    _data.add(new Double(((au.com.totemsoft.math.Numeric) value)
                            .doubleValue()));
                    continue;

                } else {
                    System.err
                            .println("FinancialTableModel2::getNumericData(...), Unhandled class: "
                                    + value.getClass());
                }

            }

        }

        return (java.lang.Number[]) _data.toArray(new java.lang.Number[0]);

    }

    /***************************************************************************
     * Static UtilityService functions
     **************************************************************************/
    public static int getRowTypeClosing(int rowType) {

        switch (rowType) {
        case ISmartTableRow.HEADER1:
            return ISmartTableRow.FOOTER1;
        case ISmartTableRow.FOOTER1:
            return ISmartTableRow.HEADER1;

        case ISmartTableRow.HEADER2:
            return ISmartTableRow.FOOTER2;
        case ISmartTableRow.FOOTER2:
            return ISmartTableRow.HEADER2;

        case ISmartTableRow.HEADER3:
            return ISmartTableRow.FOOTER3;
        case ISmartTableRow.FOOTER3:
            return ISmartTableRow.HEADER3;

        case ISmartTableRow.HEADER4:
            return ISmartTableRow.FOOTER4;
        case ISmartTableRow.FOOTER4:
            return ISmartTableRow.HEADER4;

        case ISmartTableRow.HEADER5:
            return ISmartTableRow.FOOTER5;
        case ISmartTableRow.FOOTER5:
            return ISmartTableRow.HEADER5;

        default:
            throw new IllegalArgumentException("getRowTypeClosing( " + rowType
                    + " )");
        }

    }

    public static int getRowTypeHeader(int level) {

        switch (level) {
        case 0:
            return ISmartTableRow.HEADER1;
        case 1:
            return ISmartTableRow.HEADER2;
        case 2:
            return ISmartTableRow.HEADER3;
        case 3:
            return ISmartTableRow.HEADER4;
        case 4:
            return ISmartTableRow.HEADER5;

        default:
            throw new IllegalArgumentException("getRowTypeHeader( " + level
                    + " )");
        }

    }

    public static int getRowTypeFooter(int level) {

        switch (level) {
        case 0:
            return ISmartTableRow.FOOTER1;
        case 1:
            return ISmartTableRow.FOOTER2;
        case 2:
            return ISmartTableRow.FOOTER3;
        case 3:
            return ISmartTableRow.FOOTER4;
        case 4:
            return ISmartTableRow.FOOTER5;

        default:
            throw new IllegalArgumentException("getRowTypeFooter( " + level
                    + " )");
        }

    }

    /***************************************************************************
     * javax.swing.event.TreeModelListener interface
     **************************************************************************/
    public void treeNodesChanged(javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTableModel2::treeNodesChanged( "
        // + treeModelEvent + " )" );
        init();
    }

    public void treeNodesInserted(
            javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTableModel2::treeNodesInserted(
        // " + treeModelEvent + " )" );
        init();
    }

    public void treeNodesRemoved(javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTableModel2::treeNodesRemoved( "
        // + treeModelEvent + " )" );
        init();
    }

    public void treeStructureChanged(
            javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println(
        // "*****\tFinancialTableModel2::treeStructureChanged( " +
        // treeModelEvent + " )" );
        init();
        // System.out.println( "******\t4. model.RowCount=" +
        // model.getRowCount() );
        // System.out.println( "******\t4. RowCount=" + getRowCount() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract class SmartTableRow implements ISmartTableRow {

        private int type;

        protected SmartTableRow(int type) {
            this.type = type;
        }

        public int getRowType() {
            return type;
        }

        // clear raw data
        public void clear() { /* do nothing here */
        }

        public boolean isCellEditable(int columnIndex) {
            return false;
        }

        public void setValueAt(Object aValue, int columnIndex) {
            ;
        }

    }

    public class HeaderRow extends SmartTableRow {

        protected boolean aggregate = true;

        private FinancialTreeModel.INode node;

        protected HeaderRow(int type) {
            this(type, null);
        }

        protected HeaderRow(int type, FinancialTreeModel.INode node) {
            super(type);
            this.node = node;
        }

        public String toString() {
            return node.toString();
        }

        public int getRowTypeClosing() {
            return FinancialTableModel2.this.getRowTypeClosing(getRowType());
        }

        protected ISmartTableRow[] getGroupRows() {

            java.util.Collection rows = new java.util.ArrayList();
            int rowType = getRowType();
            int rowType2 = getRowTypeClosing();
            boolean up;
            switch (rowType) {
            case HEADER1:
                up = false;
                break;
            case FOOTER1:
                up = true;
                break;

            case HEADER2:
                up = false;
                break;
            case FOOTER2:
                up = true;
                break;

            case HEADER3:
                up = false;
                break;
            case FOOTER3:
                up = true;
                break;

            case HEADER4:
                up = false;
                break;
            case FOOTER4:
                up = true;
                break;

            case HEADER5:
                up = false;
                break;
            case FOOTER5:
                up = true;
                break;

            default:
                return new ISmartTableRow[0];
            }

            // find this row index
            int r = getRowIndex(this);
            if (up) {
                for (; r >= 0; r--) {
                    ISmartTableRow row = getRowAt(r);
                    rows.add(row);
                    if (row.getRowType() == rowType2)
                        break;
                }

            } else {
                for (; r < getRowCount(); r++) {
                    ISmartTableRow row = getRowAt(r);
                    rows.add(row);
                    if (row.getRowType() == rowType2)
                        break;
                }

            }

            return (ISmartTableRow[]) rows.toArray(new ISmartTableRow[0]);

        }

        protected java.lang.Number[] getNumericData(int columnIndex) {
            return getNumericData(columnIndex, null);
        }

        protected java.lang.Number[] getNumericData(int columnIndex,
                FinancialTreeFilter filter) {

            ISmartTableRow[] rows = getGroupRows();
            java.util.Collection _data = new java.util.ArrayList();
            for (int i = 0; i < rows.length; i++) {
                ISmartTableRow row = rows[i];
                if (row instanceof FinancialRow
                        && (filter == null || filter
                                .inGroup(((FinancialRow) row).f))) {
                    Object value = row.getValueAt(columnIndex);

                    if (value == null)
                        continue;

                    if (value instanceof java.lang.Number) {
                        _data.add(value);
                        continue;

                    } else if (value instanceof au.com.totemsoft.math.Numeric) {
                        _data.add(new Double(((au.com.totemsoft.math.Numeric) value)
                                .doubleValue()));
                        continue;

                    } else {
                        System.err
                                .println("FinancialTableModel2::getNumericData(...), Unhandled class: "
                                        + value.getClass());
                    }

                }

            }

            return (java.lang.Number[]) _data.toArray(new java.lang.Number[0]);

        }

        public Object getValueAt(int columnIndex) {

            if (!aggregate)
                return columnIndex == FinancialColumnID.NAME ? toString()
                        : null;

            switch (columnIndex) {
            case FinancialColumnID.NAME:
                return toString();
                // SumFunction columns
            case FinancialColumnID.AMOUNT:
            case FinancialColumnID.AMOUNT_CLIENT:
            case FinancialColumnID.AMOUNT_PARTNER:
            case FinancialColumnID.ANNUAL_AMOUNT:
            case FinancialColumnID.REAL_AMOUNT:
            case FinancialColumnID.REAL_AMOUNT_CLIENT:
            case FinancialColumnID.REAL_AMOUNT_PARTNER:
            case FinancialColumnID.GROSS_INCOME:
            case FinancialColumnID.GROSS_INCOME_CLIENT:
            case FinancialColumnID.GROSS_INCOME_PARTNER:
            case FinancialColumnID.UNFRANKED_INCOME:
            case FinancialColumnID.UNFRANKED_INCOME_CLIENT:
            case FinancialColumnID.UNFRANKED_INCOME_PARTNER:
                return new java.math.BigDecimal(new SumFunction(
                        getNumericData(columnIndex)).calculate());
                // AverageFunction columns
            case FinancialColumnID.REGULAR_AMOUNT:
                return new java.math.BigDecimal(new AverageFunction(
                        getNumericData(columnIndex)).calculate());
            case FinancialColumnID.OBJECT_TYPE:
            case FinancialColumnID.FINANCIAL_TYPE:
            case FinancialColumnID.FINANCIAL_CODE:
            case FinancialColumnID.FINANCIAL_SERVICE:
            case FinancialColumnID.OWNER:
            case FinancialColumnID.INSTITUTION:
            case FinancialColumnID.COUNTRY:
            case FinancialColumnID.DESCRIPTION:
            case FinancialColumnID.START_DATE:
            case FinancialColumnID.END_DATE:
            case FinancialColumnID.FRANKED:
            case FinancialColumnID.TAX_FREE_DEFERRED:
            case FinancialColumnID.CAPITAL_GROWTH:
            case FinancialColumnID.INCOME:
            case FinancialColumnID.TOTAL_RETURN:
            case FinancialColumnID.UPFRONT_FEE:
            case FinancialColumnID.ONGOING_FEE:
            case FinancialColumnID.DEDUCTIBLE:
            case FinancialColumnID.DEDUCTIBLE_DSS:
            case FinancialColumnID.COMPLYING_DSS:
            case FinancialColumnID.INDEXATION:
            case FinancialColumnID.EXPENSE:
            case FinancialColumnID.REBATEABLE:
            case FinancialColumnID.ACCOUNT_NUMBER:
            case FinancialColumnID.FUND_TYPE:
            case FinancialColumnID.INVESTMENT_TYPE:
            case FinancialColumnID.UNITS_SHARES:
            case FinancialColumnID.UNITS_SHARES_PRICE:
            case FinancialColumnID.UNITS_SHARES_PRICE_DATE:
            case FinancialColumnID.PURCHASE_COST:
            case FinancialColumnID.REPLACEMENT_VALUE:
            case FinancialColumnID.TAX_DEDUCTIBLE:
            case FinancialColumnID.FREQUENCY:
            case FinancialColumnID.CONTRIBUTION:
            case FinancialColumnID.CONTRIBUTION_INDEXATION:
            case FinancialColumnID.CONTRIBUTION_START_DATE:
            case FinancialColumnID.CONTRIBUTION_END_DATE:
            case FinancialColumnID.DRAWDOWN:
            case FinancialColumnID.DRAWDOWN_INDEXATION:
            case FinancialColumnID.DRAWDOWN_START_DATE:
            case FinancialColumnID.DRAWDOWN_END_DATE:
            case FinancialColumnID.ASSOCIATED_FINANCIAL:
            case FinancialColumnID.TAXABLE:
            case FinancialColumnID.TAX_TYPE:
            case FinancialColumnID.INTEREST:
            default:
                return null;
            }

        }

    }

    public class FooterRow extends HeaderRow {

        private HeaderRow header;

        protected FooterRow(HeaderRow header) {
            this(header.getRowTypeClosing(), header);
        }

        protected FooterRow(int type, HeaderRow header) {
            super(type);
            this.header = header;
            if (this.header != null)
                this.header.aggregate = false;
        }

        public String toString() {
            return header == null ? "Total" : header.toString();
        }

    }

    public class FinancialRow extends SmartTableRow {

        private Financial f;

        protected FinancialRow(Financial f) {
            super(BODY);
            this.f = f;
        }

        public Financial getFinancial() {
            return f;
        }

        public Object getValueAt(int columnIndex) {

            switch (columnIndex) {
            case FinancialColumnID.NAME:
                return f.toString();
            case FinancialColumnID.OBJECT_TYPE:
                return f.getObjectTypeID();
            case FinancialColumnID.FINANCIAL_TYPE:
                return f.getFinancialType();
            case FinancialColumnID.FINANCIAL_CODE:
                return f.getFinancialCode();
            case FinancialColumnID.FINANCIAL_SERVICE:
                return f.getFinancialServiceCode();
            case FinancialColumnID.OWNER:
                return f.getOwner();
            case FinancialColumnID.AMOUNT:
                return f.getAmount(true);
                // two extra columns to show client/partner amount
            case FinancialColumnID.AMOUNT_CLIENT:
                if (OwnerCode.CLIENT.equals(f.getOwnerCodeID()))
                    return f.getAmount(true);
                if (OwnerCode.JOINT.equals(f.getOwnerCodeID()))
                    return new java.math.BigDecimal(f.getAmount(true)
                            .doubleValue() / 2);
                return null;
            case FinancialColumnID.AMOUNT_PARTNER:
                if (OwnerCode.PARTNER.equals(f.getOwnerCodeID()))
                    return f.getAmount(true);
                if (OwnerCode.JOINT.equals(f.getOwnerCodeID()))
                    return new java.math.BigDecimal(f.getAmount(true)
                            .doubleValue() / 2);
                return null;
            case FinancialColumnID.REAL_AMOUNT:
                return f.getFinancialYearAmount(true);
            case FinancialColumnID.REAL_AMOUNT_CLIENT:
                if (OwnerCode.CLIENT.equals(f.getOwnerCodeID()))
                    return f.getFinancialYearAmount(true);
                if (OwnerCode.JOINT.equals(f.getOwnerCodeID()))
                    return new java.math.BigDecimal(f.getFinancialYearAmount(
                            true).doubleValue() / 2);
                return null;
            case FinancialColumnID.REAL_AMOUNT_PARTNER:
                if (OwnerCode.PARTNER.equals(f.getOwnerCodeID()))
                    return f.getFinancialYearAmount(true);
                if (OwnerCode.JOINT.equals(f.getOwnerCodeID()))
                    return new java.math.BigDecimal(f.getFinancialYearAmount(
                            true).doubleValue() / 2);
                return null;
            case FinancialColumnID.INSTITUTION:
                return f.getInstitution();
            case FinancialColumnID.COUNTRY:
                return f.getCountry();
            case FinancialColumnID.DESCRIPTION:
                return f.getFinancialCodeDesc();
            case FinancialColumnID.START_DATE:
                return f.getStartDate();
            case FinancialColumnID.END_DATE:
                return f.getEndDate();
            case FinancialColumnID.FRANKED:
                return f.getFranked();
            case FinancialColumnID.TAX_FREE_DEFERRED:
                return f.getTaxFreeDeferred();
            case FinancialColumnID.CAPITAL_GROWTH:
                return percent.toString(f.getCapitalGrowth());
            case FinancialColumnID.INCOME:
                return percent.toString(f.getIncome());
            case FinancialColumnID.TOTAL_RETURN:
                return percent.toString(f.getTotalReturn());
            case FinancialColumnID.UPFRONT_FEE:
                return percent.toString(f.getUpfrontFee());
            case FinancialColumnID.ONGOING_FEE:
                return percent.toString(f.getOngoingFee());
            case FinancialColumnID.DEDUCTIBLE:
                return f.getDeductible();
            case FinancialColumnID.DEDUCTIBLE_DSS:
                return f.getDeductibleDSS();
            case FinancialColumnID.COMPLYING_DSS:
                return f.isComplyingForDSS() ? Boolean.TRUE : Boolean.FALSE;
            case FinancialColumnID.INDEXATION:
                return f.getIndexation();
            case FinancialColumnID.EXPENSE:
                return f.getExpense();
            case FinancialColumnID.REBATEABLE:
                return f.getRebateable();
            case FinancialColumnID.ACCOUNT_NUMBER:
                return f.getAccountNumber();
            case FinancialColumnID.FUND_TYPE:
                return f.getFundTypeDesc();
            case FinancialColumnID.INVESTMENT_TYPE:
                return f.getInvestmentTypeDesc();
            case FinancialColumnID.UNITS_SHARES:
                return number4.toString(f.getUnitsShares());
            case FinancialColumnID.UNITS_SHARES_PRICE:
                return number4.toString(f.getUnitsSharesPrice());
            case FinancialColumnID.UNITS_SHARES_PRICE_DATE:
                return f.getPriceDate();
            case FinancialColumnID.PURCHASE_COST:
                return f.getPurchaseCost();
            case FinancialColumnID.REPLACEMENT_VALUE:
                return f.getReplacementValue();
            case FinancialColumnID.TAX_DEDUCTIBLE:
                return f.getTaxDeductibleAnnualAmount();
            case FinancialColumnID.FREQUENCY:
                return f.getFrequencyCode();
            case FinancialColumnID.REGULAR_AMOUNT:
                return f.getRegularAmount();
            case FinancialColumnID.ANNUAL_AMOUNT:
                return f.getAnnualAmount();
            case FinancialColumnID.CONTRIBUTION:
                return f.getContributionAnnualAmount();
            case FinancialColumnID.CONTRIBUTION_INDEXATION:
                return f.getContributionIndexation();
            case FinancialColumnID.CONTRIBUTION_START_DATE:
                return f.getContributionStartDate();
            case FinancialColumnID.CONTRIBUTION_END_DATE:
                return f.getContributionEndDate();
            case FinancialColumnID.DRAWDOWN:
                return f.getDrawdownAnnualAmount();
            case FinancialColumnID.DRAWDOWN_INDEXATION:
                return f.getDrawdownIndexation();
            case FinancialColumnID.DRAWDOWN_START_DATE:
                return f.getDrawdownStartDate();
            case FinancialColumnID.DRAWDOWN_END_DATE:
                return f.getDrawdownEndDate();
            case FinancialColumnID.ASSOCIATED_FINANCIAL:
                return f.getAssociatedFinancial();
            case FinancialColumnID.TAXABLE:
                return f.isTaxable() ? Boolean.TRUE : Boolean.FALSE;
            case FinancialColumnID.TAX_TYPE:
                return f.getRegularTaxType();
            case FinancialColumnID.INTEREST:
                return f.getInterestRate();

                // eight extra columns to show asset allocation
            case FinancialColumnID.AA_CASH:
                AssetAllocation aa = f.getAssetAllocation();
                return (aa != null) ? aa.inCash : new Double(0.);

            case FinancialColumnID.AA_FIXED_INTEREST:
                aa = f.getAssetAllocation();
                return (aa != null) ? aa.inFixedInterest : new Double(0.);

            case FinancialColumnID.AA_AUST_SHARES:
                aa = f.getAssetAllocation();
                return (aa != null) ? aa.inAustShares : new Double(0.);

            case FinancialColumnID.AA_INTNL_SHARES:
                aa = f.getAssetAllocation();
                return (aa != null) ? aa.inIntnlShares : new Double(0.);

            case FinancialColumnID.AA_PROPERTY:
                aa = f.getAssetAllocation();
                return (aa != null) ? aa.inProperty : new Double(0.);

            case FinancialColumnID.AA_OTHER:
                aa = f.getAssetAllocation();
                return (aa != null) ? aa.inOther : new Double(0.);

            case FinancialColumnID.AA_TOTAL:
                double aa_total = 0.;
                aa = f.getAssetAllocation();
                if (aa != null) {
                    aa_total += (aa.inCash != null) ? aa.inCash.doubleValue()
                            : 0.;
                    aa_total += (aa.inFixedInterest != null) ? aa.inFixedInterest
                            .doubleValue()
                            : 0.;
                    aa_total += (aa.inAustShares != null) ? aa.inAustShares
                            .doubleValue() : 0.;
                    aa_total += (aa.inIntnlShares != null) ? aa.inIntnlShares
                            .doubleValue() : 0.;
                    aa_total += (aa.inProperty != null) ? aa.inProperty
                            .doubleValue() : 0.;
                    aa_total += (aa.inOther != null) ? aa.inOther.doubleValue()
                            : 0.;
                }
                return new Double(aa_total);

            case FinancialColumnID.AA_INCLUDE:
                aa = f.getAssetAllocation();
                return (aa != null) ? aa.include : new Boolean(true);

                // generated regular financials
            case FinancialColumnID.GROSS_INCOME:
                Regular r = f.getRegular(IRegularType.iGROSS_INCOME);
                return r == null ? null : r.getFinancialYearAmount(true);

            case FinancialColumnID.GROSS_INCOME_CLIENT:
                Regular r2 = f.getRegular(IRegularType.iGROSS_INCOME);
                if (r2 == null)
                    return null;
                if (OwnerCode.CLIENT.equals(r2.getOwnerCodeID()))
                    return r2.getFinancialYearAmount(true);
                if (OwnerCode.JOINT.equals(r2.getOwnerCodeID()))
                    return new java.math.BigDecimal(r2.getFinancialYearAmount(
                            true).doubleValue() / 2);
                return null;

            case FinancialColumnID.GROSS_INCOME_PARTNER:
                Regular r1 = f.getRegular(IRegularType.iGROSS_INCOME);
                if (r1 == null)
                    return null;
                if (OwnerCode.PARTNER.equals(r1.getOwnerCodeID()))
                    return r1.getFinancialYearAmount(true);
                if (OwnerCode.JOINT.equals(r1.getOwnerCodeID()))
                    return new java.math.BigDecimal(r1.getFinancialYearAmount(
                            true).doubleValue() / 2);
                return null;
            case FinancialColumnID.UNFRANKED_INCOME:
                Regular r3 = f.getRegular(IRegularType.iUNFRANKED_INCOME);
                return r3 == null ? null : r3.getFinancialYearAmount(true);
            case FinancialColumnID.UNFRANKED_INCOME_CLIENT:
                Regular r4 = f.getRegular(IRegularType.iUNFRANKED_INCOME);
                if (r4 == null)
                    return null;
                if (OwnerCode.CLIENT.equals(r4.getOwnerCodeID()))
                    return r4.getFinancialYearAmount(true);
                if (OwnerCode.JOINT.equals(r4.getOwnerCodeID()))
                    return new java.math.BigDecimal(r4.getFinancialYearAmount(
                            true).doubleValue() / 2);
                return null;

            case FinancialColumnID.UNFRANKED_INCOME_PARTNER:
                Regular r5 = f.getRegular(IRegularType.iUNFRANKED_INCOME);

                if (r5 == null)
                    return null;
                if (OwnerCode.PARTNER.equals(r5.getOwnerCodeID()))
                    return r5.getFinancialYearAmount(true);
                if (OwnerCode.JOINT.equals(r5.getOwnerCodeID()))
                    return new java.math.BigDecimal(r5.getFinancialYearAmount(
                            true).doubleValue() / 2);
                return null;

            default:
                return null;
            }

        }

        public void setValueAt(int columnIndex, Object value) {
            AssetAllocation aa = f.getAssetAllocation();
            // mark Financial as modified, so it will be saved
            f.setModified(true);

            switch (columnIndex) {
            // seven extra columns to set asset allocation
            case FinancialColumnID.AA_CASH:
                aa.inCash = (Double) value;
                break;
            case FinancialColumnID.AA_FIXED_INTEREST:
                aa.inFixedInterest = (Double) value;
                break;
            case FinancialColumnID.AA_AUST_SHARES:
                aa.inAustShares = (Double) value;
                break;
            case FinancialColumnID.AA_INTNL_SHARES:
                aa.inIntnlShares = (Double) value;
                break;
            case FinancialColumnID.AA_PROPERTY:
                aa.inProperty = (Double) value;
                break;
            case FinancialColumnID.AA_OTHER:
                aa.inOther = (Double) value;
                break;
            case FinancialColumnID.AA_INCLUDE:
                aa.include = (Boolean) value;
                break;
            default:
                // nothing to do
            }

        }

    }

}
