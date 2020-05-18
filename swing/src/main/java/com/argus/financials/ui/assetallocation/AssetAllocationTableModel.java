/*
 * AssetAllocationTableModel.java
 *
 * Created on 20 September 2002, 12:04
 */

package com.argus.financials.ui.assetallocation;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.code.FinancialTypeEnum;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.assetallocation.AssetAllocation;
import com.argus.financials.assetallocation.AssetAllocationAssetNameComperator;
import com.argus.financials.assetallocation.AssetAllocationTableRow;
import com.argus.financials.assetallocation.FinancialAssetAllocation;
import com.argus.financials.assetallocation.IAssetAllocation;
import com.argus.financials.assetallocation.IAssetAllocation2;
import com.argus.financials.bean.AssetCash;
import com.argus.financials.bean.AssetInvestment;
import com.argus.financials.bean.AssetSuperannuation;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.IncomeStream;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.format.Percent;
import com.argus.math.FormatedBigDecimal;

/**
 * Represents the asset allocation as a TableModel, which is used in a JTable to
 * display the asset allocation. The class can load and store the asset
 * allocation for a given person.
 * 
 * @author shibaevv
 * @version
 * 
 * @see com.argus.financials.ui.assetallocation.CurrentAssetAllocationTableModel
 * @see com.argus.financials.ui.assetallocation.NewAssetAllocationTableModel
 */
public class AssetAllocationTableModel extends DefaultTableModel {

    public final static String DEFAULT_MODELN_NAME = "Asset Allocation";

    protected String _model_name = DEFAULT_MODELN_NAME;

    protected boolean _modified = false;

    protected IAssetAllocation view = null;

    protected boolean _filtering = true;

    // stores the totals for each column
    protected double totalInCash = 0.0;

    protected double totalInFixedInterest = 0.0;

    protected double totalInAustShares = 0.0;

    protected double totalInIntnlShares = 0.0;

    protected double totalInProperty = 0.0;

    protected double totalInOther = 0.0;

    protected double totalTotal = 0.0;

    // number of digits after decimal dot
    public final static int NUMBER_OF_DIGITS = 2;

    public final static int COLUMN_NAME = 0;

    public final static int COLUMN_AMOUNT = 1;

    public final static int COLUMN_CASH = 2;

    public final static int COLUMN_FIXED_INTEREST = 3;

    public final static int COLUMN_AUST_SHARES = 4;

    public final static int COLUMN_INTNL_SHARES = 5;

    public final static int COLUMN_PROPERTY = 6;

    public final static int COLUMN_OTHER = 7;

    public final static int COLUMN_TOTAL = 8;

    public final static int COLUMN_INCLUDE = 9;

    public final static int COLUMN_CLIENT_ID = 10;

    public final static int COLUMN_FINANCIAL_ID = 11;

    public final static int COLUMN_ASSET_ALLOCATION_ID = 12;

    protected String[] columnNames = new String[] { "Asset Name", "Value ($)",
            "Cash (%)", "Fixed Interest (%)", "Aust. Shares (%)",
            "Intnl. Shares (%)", "Property (%)", "Other (%)", "Total (%)",
            "Include" // , "OwnerID", "FinancialID", "AssetAllocationID"
    };

    protected Vector _data = null;

    Class[] types = new Class[] { java.lang.String.class, // "Asset Name"
            com.argus.math.FormatedBigDecimal.class, // "Value ($)"
            com.argus.math.FormatedBigDecimal.class, // "Cash (%)"
            com.argus.math.FormatedBigDecimal.class, // "Fixed Interest (%)
            com.argus.math.FormatedBigDecimal.class, // "Aust. Shares (%)"
            com.argus.math.FormatedBigDecimal.class, // "Intnl. Shares (%)"
            com.argus.math.FormatedBigDecimal.class, // "Property (%)"
            com.argus.math.FormatedBigDecimal.class, // "Other (%)"
            com.argus.math.FormatedBigDecimal.class, // "Total (%)"
            java.lang.Boolean.class, // "Include"
            java.lang.Integer.class, // "OwnerID"
            java.lang.Integer.class, // "FinancialID"
            java.lang.Integer.class // "AssetAllocationID"
    };

    boolean[] canEdit = new boolean[] { false, false, true, true, true, true,
            true, true, false, true, false, false, false };

    protected static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        AssetAllocationTableModel.clientService = clientService;
    }

    /** Creates new AssetAllocationTableModel */
    public AssetAllocationTableModel(IAssetAllocation view, Vector new_data) {
        this.view = view;
        this._data = new_data;
        this.sortData();

        this.updateAllTotalColumn();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        int length = 0;

        if (_data != null) {
            length = _data.size();
        }

        return length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Object help = null;

        if (row < _data.size()) {
            AssetAllocationTableRow aatr = (AssetAllocationTableRow) _data
                    .elementAt(row);

            switch (col) {
            case COLUMN_NAME:
                help = aatr.asset_name;
                break;
            case COLUMN_AMOUNT:
                help = aatr.amount;
                break;
            case COLUMN_CASH:
                help = aatr.percent_in_cash;
                break;
            case COLUMN_FIXED_INTEREST:
                help = aatr.percent_in_fixed_interest;
                break;
            case COLUMN_AUST_SHARES:
                help = aatr.percent_in_aust_shares;
                break;
            case COLUMN_INTNL_SHARES:
                help = aatr.percent_in_intnl_shares;
                break;
            case COLUMN_PROPERTY:
                help = aatr.percent_in_property;
                break;
            case COLUMN_OTHER:
                help = aatr.percent_in_other;
                break;
            case COLUMN_TOTAL:
                help = aatr.total_in_percent;
                break;
            case COLUMN_INCLUDE:
                help = aatr.include;
                break;
            case COLUMN_CLIENT_ID:
                help = aatr.client_id;
                break;
            case COLUMN_FINANCIAL_ID:
                help = aatr.financial_id;
                break;
            case COLUMN_ASSET_ALLOCATION_ID:
                help = aatr.asset_allocation_id;
                break;

            default:
                help = null;
            }

        }
        return help;
    }

    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    /*
     * public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // check if we select the last row / total row
        if (_data != null && _data.size() - 1 == rowIndex) {
            return false;
        }
        return canEdit[columnIndex];
    }

    /*
     * Don't need to implement this method unless your table's data can change.
     */
    public void setValueAt(Object value, int row, int col) {

        this._modified = true;

        if (getValueAt(0, col) instanceof FormatedBigDecimal
                && (value instanceof String)) {
            // With JFC/Swing 1.1 and JDK 1.2, we need to create
            // an FormatedBigDecimal from the value; otherwise, the column
            // switches to contain Strings. Starting with v 1.3,
            // the table automatically converts value to an FormatedBigDecimal,
            // so you only need the code in the 'else' part of this
            // 'if' block.
            try {
                setColumnValueAssetAllocationTableRow(row, col,
                        checkNewPercentColumnValue(row, col,
                                new FormatedBigDecimal(value.toString())));
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
                System.err.println("The \"" + getColumnName(col)
                        + "\" column accepts only decimal values.");

                javax.swing.JOptionPane.showMessageDialog(
                        (javax.swing.JComponent) this.view, "The \""
                                + getColumnName(col)
                                + "\" column accepts only decimal values.");
            }
        }

        if (getValueAt(0, col) instanceof FormatedBigDecimal
                && (value instanceof FormatedBigDecimal)) {
            // With JFC/Swing 1.1 and JDK 1.2, we need to create
            // an FormatedBigDecimal from the value; otherwise, the column
            // switches to contain Strings. Starting with v 1.3,
            // the table automatically converts value to an FormatedBigDecimal,
            // so you only need the code in the 'else' part of this
            // 'if' block.
            try {
                // _data[row][col] = checkNewPercentColumnValue( row, col,
                // (FormatedBigDecimal)value );
                setColumnValueAssetAllocationTableRow(row, col,
                        checkNewPercentColumnValue(row, col,
                                (FormatedBigDecimal) value));
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
                System.err.println("The \"" + getColumnName(col)
                        + "\" column accepts only decimal values.");

                javax.swing.JOptionPane.showMessageDialog(
                        (javax.swing.JComponent) this.view, "The \""
                                + getColumnName(col)
                                + "\" column accepts only decimal values.");

            }
        }

        if (getValueAt(0, col) instanceof Boolean && (value instanceof Boolean)) {
            // With JFC/Swing 1.1 and JDK 1.2, we need to create
            // an FormatedBigDecimal from the value; otherwise, the column
            // switches to contain Strings. Starting with v 1.3,
            // the table automatically converts value to an FormatedBigDecimal,
            // so you only need the code in the 'else' part of this
            // 'if' block.
            try {
                setColumnValueAssetAllocationTableRow(row, col, value);
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
                System.err.println("The \"" + getColumnName(col)
                        + "\" column accepts only boolean values.");

                javax.swing.JOptionPane.showMessageDialog(
                        (javax.swing.JComponent) this.view, "The \""
                                + getColumnName(col)
                                + "\" column accepts only boolean values.");
            }
        }

        if (getValueAt(0, col) instanceof String && (value instanceof String)) {
            // With JFC/Swing 1.1 and JDK 1.2, we need to create
            // an FormatedBigDecimal from the value; otherwise, the column
            // switches to contain Strings. Starting with v 1.3,
            // the table automatically converts value to an FormatedBigDecimal,
            // so you only need the code in the 'else' part of this
            // 'if' block.
            try {
                setColumnValueAssetAllocationTableRow(row, col, value);
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
                System.err.println("The \"" + getColumnName(col)
                        + "\" column accepts only string values.");

                javax.swing.JOptionPane.showMessageDialog(
                        (javax.swing.JComponent) this.view, "The \""
                                + getColumnName(col)
                                + "\" column accepts only string values.");
            }
        }

        setColumnValueAssetAllocationTableRow(row, COLUMN_TOTAL,
                addRowPercentValues(row));
        fireTableCellUpdated(row, COLUMN_TOTAL);

        // update total row
        updateTotalAssetAllocationRow();
        // fireTableCellUpdated(_data.size()-1, col);
        // fireTableCellUpdated(_data.size()-1, COLUMN_TOTAL);
        this.fireTableRowsUpdated(_data.size() - 1, _data.size() - 1);

    }

    protected void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + this.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    /**
     * Calculates the total sum of all columns in a row, which contains
     * percentages.
     * 
     * @param row -
     *            table row number
     * @return the total sum of column values in that row
     */
    protected FormatedBigDecimal addRowPercentValues(int row) {
        double sum = 0.0;

        if (this.getValueAt(row, COLUMN_CASH) != null) {
            sum = sum
                    + ((FormatedBigDecimal) this.getValueAt(row, COLUMN_CASH))
                            .doubleValue();
        }
        if (this.getValueAt(row, COLUMN_FIXED_INTEREST) != null) {
            sum = sum
                    + ((FormatedBigDecimal) this.getValueAt(row,
                            COLUMN_FIXED_INTEREST)).doubleValue();
        }
        if (this.getValueAt(row, COLUMN_AUST_SHARES) != null) {
            sum = sum
                    + ((FormatedBigDecimal) this.getValueAt(row,
                            COLUMN_AUST_SHARES)).doubleValue();
        }
        if (this.getValueAt(row, COLUMN_INTNL_SHARES) != null) {
            sum = sum
                    + ((FormatedBigDecimal) this.getValueAt(row,
                            COLUMN_INTNL_SHARES)).doubleValue();
        }
        if (this.getValueAt(row, COLUMN_PROPERTY) != null) {
            sum = sum
                    + ((FormatedBigDecimal) this.getValueAt(row,
                            COLUMN_PROPERTY)).doubleValue();
        }
        if (this.getValueAt(row, COLUMN_OTHER) != null) {
            sum = sum
                    + ((FormatedBigDecimal) this.getValueAt(row, COLUMN_OTHER))
                            .doubleValue();
        }

        return new FormatedBigDecimal(sum);
    }

    /**
     * Checks if the new given value is less or equal 100.0 and if the total sum
     * of the columns doesn't exceed 100.0%. In case the new value is to large,
     * the methode automatically corrects the value so, that the sum is not
     * bigger than 100.0%.
     * 
     * @param row -
     *            table row number
     * @param col -
     *            table col number
     * @param value -
     *            value to check
     * @return verified value
     */
    protected FormatedBigDecimal checkNewPercentColumnValue(int row, int col,
            FormatedBigDecimal value) {
        double help = 0.0;
        double sum = 0.0;

        if (value != null) {
            help = (value.doubleValue() <= 100.0) ? value.doubleValue() : 100.0;

            if (col != COLUMN_CASH) {
                sum = sum
                        + ((FormatedBigDecimal) this.getValueAt(row,
                                COLUMN_CASH)).doubleValue();
            }
            if (col != COLUMN_FIXED_INTEREST) {
                sum = sum
                        + ((FormatedBigDecimal) this.getValueAt(row,
                                COLUMN_FIXED_INTEREST)).doubleValue();
            }
            if (col != COLUMN_AUST_SHARES) {
                sum = sum
                        + ((FormatedBigDecimal) this.getValueAt(row,
                                COLUMN_AUST_SHARES)).doubleValue();
            }
            if (col != COLUMN_INTNL_SHARES) {
                sum = sum
                        + ((FormatedBigDecimal) this.getValueAt(row,
                                COLUMN_INTNL_SHARES)).doubleValue();
            }
            if (col != COLUMN_PROPERTY) {
                sum = sum
                        + ((FormatedBigDecimal) this.getValueAt(row,
                                COLUMN_PROPERTY)).doubleValue();
            }
            if (col != COLUMN_OTHER) {
                sum = sum
                        + ((FormatedBigDecimal) this.getValueAt(row,
                                COLUMN_OTHER)).doubleValue();
            }

            if ((help + sum) > 100.0) {
                help = 100.0 - sum;
            }
        }
        help = (help < 0.0) ? 0.0 : help;

        return new FormatedBigDecimal(help);

    }

    /**
     * Calculates the sum of all total percentages columns in the table.
     */
    public void updateAllTotalColumn() {
        if (_data != null) {
            for (int row = 0; row < _data.size(); row++) {
                // _data[row][COLUMN_TOTAL] = addRowPercentValues(row);
                setColumnValueAssetAllocationTableRow(row, COLUMN_TOTAL,
                        addRowPercentValues(row));
                fireTableCellUpdated(row, COLUMN_TOTAL);
            }
        }
    }

    /**
     * Sets the new value inside the data Vector. It locats the right row, gets
     * the AssetAlocationTableRow object and sets the corretc value in that
     * object.
     * 
     * @param row -
     *            table row number
     * @param col -
     *            table col number
     * @param value -
     *            value to set
     */
    protected void setColumnValueAssetAllocationTableRow(int row, int col,
            Object value) {
        if (row < _data.size()) {
            AssetAllocationTableRow aatr = (AssetAllocationTableRow) _data
                    .elementAt(row);

            switch (col) {
            case COLUMN_NAME:
                aatr.asset_name = (String) value;
                break;
            case COLUMN_AMOUNT:
                aatr.amount = (FormatedBigDecimal) value;
                break;
            case COLUMN_CASH:
                aatr.percent_in_cash = (FormatedBigDecimal) value;
                break;
            case COLUMN_FIXED_INTEREST:
                aatr.percent_in_fixed_interest = (FormatedBigDecimal) value;
                break;
            case COLUMN_AUST_SHARES:
                aatr.percent_in_aust_shares = (FormatedBigDecimal) value;
                break;
            case COLUMN_INTNL_SHARES:
                aatr.percent_in_intnl_shares = (FormatedBigDecimal) value;
                break;
            case COLUMN_PROPERTY:
                aatr.percent_in_property = (FormatedBigDecimal) value;
                break;
            case COLUMN_OTHER:
                aatr.percent_in_other = (FormatedBigDecimal) value;
                break;
            case COLUMN_TOTAL:
                aatr.total_in_percent = (FormatedBigDecimal) value;
                break;
            case COLUMN_INCLUDE:
                aatr.include = (Boolean) value;
                break;
            case COLUMN_CLIENT_ID:
                aatr.client_id = (Integer) value;
                break;
            case COLUMN_FINANCIAL_ID:
                aatr.financial_id = (Integer) value;
                break;
            case COLUMN_ASSET_ALLOCATION_ID:
                aatr.asset_allocation_id = (Integer) value;
                break;

            default:
                ;
            }
            updateAssetAllocationView();
        }
    }

    /**
     * Calculates the total sum of one column, it adds up every column of all
     * rows.
     * 
     * @param column
     *            number
     * @return total sum
     */
    protected double calculateColumTotal(int col) {
        double sum = 0.0;

        if (col >= 0 && col < this.getColumnCount()) {
            if (getValueAt(0, col) instanceof FormatedBigDecimal) {
                for (int row = 0; row < _data.size(); row++) {
                    sum += ((FormatedBigDecimal) getValueAt(row, col))
                            .doubleValue();
                }
            }
        }

        return sum;
    }

    /**
     * Calculates the total sum of one column, it adds up every column of all
     * rows with a "marked" "Include" column.
     * 
     * @param column
     *            number
     * @return total sum
     */
    protected double calculateColumTotalWithInclude(int col) {
        double sum = 0.0;

        if (col >= 0 && col < this.getColumnCount()) {
            if (getValueAt(0, col) instanceof FormatedBigDecimal) {
                for (int row = 0; row < _data.size(); row++) {
                    if (getValueAt(row, COLUMN_INCLUDE) != null
                            && ((Boolean) getValueAt(row, COLUMN_INCLUDE))
                                    .equals(Boolean.TRUE)) {
                        sum += ((FormatedBigDecimal) getValueAt(row, col))
                                .doubleValue();
                    }
                }
            }
        }

        return sum;
    }

    /**
     * Calculates the total sum of one column, it adds up every column of all
     * rows with a "marked" "Include" column. The dollar amount is calaculated
     * dependend on the given percentage and the asset amount.
     * 
     * @param column
     *            number
     * @return total sum
     */
    protected double calculateColumTotalWithIncludeInDollar(int col) {
        double sum = 0.0;

        if (col >= 0 && col < this.getColumnCount()) {
            if (getValueAt(0, col) instanceof FormatedBigDecimal) {
                for (int row = 0; row < _data.size(); row++) {
                    if (getValueAt(row, COLUMN_INCLUDE) != null
                            && ((Boolean) getValueAt(row, COLUMN_INCLUDE))
                                    .equals(Boolean.TRUE)) {
                        sum += ((((FormatedBigDecimal) getValueAt(row,
                                COLUMN_AMOUNT))).doubleValue() * ((FormatedBigDecimal) getValueAt(
                                row, col)).doubleValue()) / 100.0;
                    }
                }
            }
        }

        return sum;
    }

    /**
     * Counts all rows which have the column "Include" marked.
     * 
     * @return number of columns
     */
    protected int numberOfIncludedRows() {
        int selected = 0;

        for (int row = 0; row < _data.size(); row++) {
            if (getValueAt(row, COLUMN_INCLUDE) != null
                    && ((Boolean) getValueAt(row, COLUMN_INCLUDE))
                            .equals(Boolean.TRUE)) {
                selected++;
            }
        }

        return selected;
    }

    /**
     * Calculates totals for the following columns: <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * COLUMN_CASH, COLUMN_FIXED_INTEREST, COLUMN_AUST_SHARES
     * COLUMN_INTNL_SHARES COLUMN_PROPERTY COLUMN_OTHER TOTAL
     * 
     * </PRE>
     * 
     * @return number of columns
     */
    public void updateAssetAllocationView() {
        double column_sum_ca = 0.0;
        double column_sum_fi = 0.0;
        double column_sum_as = 0.0;
        double column_sum_is = 0.0;
        double column_sum_pr = 0.0;
        double column_sum_ot = 0.0;
        double total_value = 0.0;
        double total = 0.0;

        if (_data != null && _data.size() > 0) {
            int last_row = _data.size() - 1;
            // total cash
            column_sum_ca = calculateColumTotalWithIncludeInDollar(COLUMN_CASH);
            // total fixed interest
            column_sum_fi = calculateColumTotalWithIncludeInDollar(COLUMN_FIXED_INTEREST);
            // total aust. shares
            column_sum_as = calculateColumTotalWithIncludeInDollar(COLUMN_AUST_SHARES);
            // total intnl. shares
            column_sum_is = calculateColumTotalWithIncludeInDollar(COLUMN_INTNL_SHARES);
            // total property
            column_sum_pr = calculateColumTotalWithIncludeInDollar(COLUMN_PROPERTY);
            // total other
            column_sum_ot = calculateColumTotalWithIncludeInDollar(COLUMN_OTHER);
            // total
            total_value = calculateColumTotalWithInclude(COLUMN_AMOUNT);

            // division by zero?
            if (total_value > 0) {
                column_sum_ca = (column_sum_ca * 100.0) / total_value;
                column_sum_fi = (column_sum_fi * 100.0) / total_value;
                column_sum_as = (column_sum_as * 100.0) / total_value;
                column_sum_is = (column_sum_is * 100.0) / total_value;
                column_sum_pr = (column_sum_pr * 100.0) / total_value;
                column_sum_ot = (column_sum_ot * 100.0) / total_value;
                total = column_sum_ca + column_sum_fi + column_sum_as
                        + column_sum_is + column_sum_pr + column_sum_ot;
            } else {
                // avoid divison by zero error, so set everything to zero
                column_sum_ca = 0.0;
                column_sum_fi = 0.0;
                column_sum_as = 0.0;
                column_sum_is = 0.0;
                column_sum_pr = 0.0;
                column_sum_ot = 0.0;
                total = 0.0;
            }
        }

        Percent percent = Percent.getPercentInstance();

        // maybe we need to get these values from the "NewAssetAllocationView"
        // to display the current allocation
        this.totalInCash = column_sum_ca;
        this.totalInFixedInterest = column_sum_fi;
        this.totalInAustShares = column_sum_as;
        this.totalInIntnlShares = column_sum_is;
        this.totalInProperty = column_sum_pr;
        this.totalInOther = column_sum_ot;
        this.totalTotal = total;

        // check if we have a view to update
        if (view != null) {
            if (view instanceof IAssetAllocation2) {
                IAssetAllocation2 _aav2 = (IAssetAllocation2) view;
                _aav2.setTotalInCash(column_sum_ca);
                _aav2.setTotalInFixedInterest(column_sum_fi);
                _aav2.setTotalInAustShares(column_sum_as);
                _aav2.setTotalInIntnlShares(column_sum_is);
                _aav2.setTotalInProperty(column_sum_pr);
                _aav2.setTotalInOther(column_sum_ot);
                _aav2.setTotalTotal(total);
            } else if (view instanceof IAssetAllocation) {
                // this is currency/money, not percent !!!
                IAssetAllocation _aav = (IAssetAllocation) view;
                _aav.setTotalInCash(percent.toString(column_sum_ca));
                _aav.setTotalInFixedInterest(percent.toString(column_sum_fi));
                _aav.setTotalInAustShares(percent.toString(column_sum_as));
                _aav.setTotalInIntnlShares(percent.toString(column_sum_is));
                _aav.setTotalInProperty(percent.toString(column_sum_pr));
                _aav.setTotalInOther(percent.toString(column_sum_ot));
                _aav.setTotalTotal(percent.toString(total));
            }

        }

    }

    /**
     * Updates the "Total in $" row of the table, which is always the last row.
     * Each total column contains the total amount of all values in the same
     * column. The "Value" column simple adds every column value. The "Total"
     * column adds up the COLUMN_CASH, COLUMN_FIXED_INTEREST,
     * COLUMN_AUST_SHARES, COLUMN_INTNL_SHARES, COLUMN_PROPERTY and COLUMN_OTHER
     * columns.
     * 
     * Every other column/cell calculates the sum in dollar, which is calculated
     * out of the percent value in each column multiplied by the asset value in
     * each row.
     */
    public void updateTotalAssetAllocationRow() {
        // total cash
        double column_sum_ca = calculateColumTotalWithIncludeInDollar(COLUMN_CASH);
        // total fixed interest
        double column_sum_fi = calculateColumTotalWithIncludeInDollar(COLUMN_FIXED_INTEREST);
        // total aust. shares
        double column_sum_as = calculateColumTotalWithIncludeInDollar(COLUMN_AUST_SHARES);
        // total intnl. shares
        double column_sum_is = calculateColumTotalWithIncludeInDollar(COLUMN_INTNL_SHARES);
        // total property
        double column_sum_pr = calculateColumTotalWithIncludeInDollar(COLUMN_PROPERTY);
        // total property
        double column_sum_ot = calculateColumTotalWithIncludeInDollar(COLUMN_OTHER);
        // total amount
        double column_total_amount = calculateColumTotalWithInclude(COLUMN_AMOUNT);
        // total in percent
        double column_total_in_percent = column_sum_ca + column_sum_fi
                + column_sum_as + column_sum_is + column_sum_pr + column_sum_ot;

        // update last row = total row
        if (_data != null && _data.size() > 0) {
            AssetAllocationTableRow aatr = (AssetAllocationTableRow) _data
                    .elementAt(_data.size() - 1);

            aatr.asset_name = new String("Total in $");
            aatr.amount = new FormatedBigDecimal(rint(column_total_amount, 2));
            aatr.percent_in_cash = new FormatedBigDecimal(
                    rint(column_sum_ca, 2));
            aatr.percent_in_fixed_interest = new FormatedBigDecimal(rint(
                    column_sum_fi, 2));
            aatr.percent_in_aust_shares = new FormatedBigDecimal(rint(
                    column_sum_as, 2));
            aatr.percent_in_intnl_shares = new FormatedBigDecimal(rint(
                    column_sum_is, 2));
            aatr.percent_in_property = new FormatedBigDecimal(rint(
                    column_sum_pr, 2));
            aatr.percent_in_other = new FormatedBigDecimal(rint(column_sum_ot,
                    2));
            aatr.total_in_percent = new FormatedBigDecimal(rint(
                    column_total_in_percent, 2));
            aatr.include = new Boolean(false);
            aatr.client_id = new Integer(0);
            aatr.financial_id = new Integer(0);
            aatr.asset_allocation_id = new Integer(0);
        }
    }

    public boolean isModified() {
        return this._modified;
    }

    public void setModified(boolean value) {
        this._modified = value;
    }

    public Vector getData() {
        return this._data;
    }

    public void setData(Vector value) {
        this._data = value;
        // add row to display totals
        if (this._data != null) {
            sortData();

            AssetAllocationTableRow aatr = new AssetAllocationTableRow();

            aatr.asset_name = new String("Total in $");
            aatr.amount = new FormatedBigDecimal(0.0);
            aatr.percent_in_cash = new FormatedBigDecimal(0.0);
            aatr.percent_in_fixed_interest = new FormatedBigDecimal(0.0);
            aatr.percent_in_aust_shares = new FormatedBigDecimal(0.0);
            aatr.percent_in_intnl_shares = new FormatedBigDecimal(0.0);
            aatr.percent_in_property = new FormatedBigDecimal(0.0);
            aatr.percent_in_other = new FormatedBigDecimal(0.0);
            aatr.include = new Boolean(false);

            this._data.add(aatr);
        }
        // update total percent column
        updateAllTotalColumn();
        // update last row
        updateTotalAssetAllocationRow();

        this.fireTableRowsUpdated(_data.size() - 1, _data.size() - 1);
    }

    // public InterfaceAssetAllocationView getAssetAllocationView() {
    // return this.view;
    // }
    //
    // public void setAssetAllocationView( InterfaceAssetAllocationView value )
    // {
    // this.view = value;
    // }

    public String getModelName() {
        return this._model_name;
    }

    public void setModelName(String value) {
        this._model_name = value;
    }

    public String getDefaultTitle() {
        return DEFAULT_MODELN_NAME;
    }

    public double getTotalInCash() {
        return this.totalInCash;
    }

    public double getTotalInFixedInterest() {
        return this.totalInFixedInterest;
    }

    public double getTotalInAustShares() {
        return this.totalInAustShares;
    }

    public double getTotalInIntnlShares() {
        return this.totalInIntnlShares;
    }

    public double getTotalInProperty() {
        return this.totalInProperty;
    }

    public double getTotalInOther() {
        return this.totalInOther;
    }

    public double getTotalTotal() {
        return this.totalTotal;
    }

    /**
     * Updates the model with the assets of a person. It uses all assets of the
     * following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param person -
     *            a person object
     */
    public void updateModel(PersonService person)
            throws ServiceException {

        /***********************************************************************
         * FINANCIAL (usually client)
         **********************************************************************/
        updateModel(person.findFinancials());
    }

    /**
     * Updates the view with the assets out of a Map object. It uses all assets
     * of the following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param financials -
     *            a Map object of all Financial objects
     */
    public void updateModel(java.util.Map financials) {

        // stores rows for jTable
        Vector new_data = new Vector();

        Double percent_in_cash = null;
        Double percent_in_fixed_interest = null;
        Double percent_in_aust_shares = null;
        Double percent_in_in_intnl_shares = null;
        Double percent_in_property = null;
        Double percent_in_other = null;

        // prevent null pointer exception
        if (financials != null) {

            Integer objectTypeID = new Integer(ObjectTypeConstant.ASSET_CASH);
            java.util.Map assetCash = (java.util.Map) financials
                    .get(objectTypeID);

            if (assetCash != null && assetCash.size() > 0) {
                Iterator iter = assetCash.values().iterator();
                AssetCash f = null;

                while (iter.hasNext()) {
                    try {
                        percent_in_cash = new Double(0.0);
                        percent_in_fixed_interest = new Double(0.0);
                        percent_in_aust_shares = new Double(0.0);
                        percent_in_in_intnl_shares = new Double(0.0);
                        percent_in_property = new Double(0.0);
                        percent_in_other = new Double(0.0);

                        f = (AssetCash) iter.next();

                        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

                        aatr.asset_name = new String(f.toString()); // new
                                                                    // String(
                                                                    // f.getFinancialTypeDesc()
                                                                    // );

                        AssetAllocation aa = null;

                        if (f instanceof AssetCash) {
                            // check if we have a Term Deposit
                            if (FinancialTypeEnum.isTermDeposit(f.getFinancialTypeID())) {
                                percent_in_fixed_interest = new Double(100.0);
                            } else {
                                percent_in_cash = new Double(100.0);
                            }
                        }

                        // do we have information about asset allocation
                        // if( f.getAssetAllocationID() == null ||
                        // f.getAssetAllocationID().intValue() < 0 ) {
                        if (f.getAssetAllocationID() == null) {
                            // no, we habe to create a new item
                            aa = f.getAssetAllocation();
                            // and populate it
                            // aa.setAmount ( ( f.getAmount() == null ) ? new
                            // FormatedBigDecimal( 0.0 ) : new
                            // FormatedBigDecimal( f.getAmount().doubleValue() )
                            // );
                            aa.setInCash(percent_in_cash);
                            aa.setInFixedInterest(percent_in_fixed_interest);
                            aa.setInAustShares(percent_in_aust_shares);
                            aa.setInIntnlShares(percent_in_in_intnl_shares);
                            aa.setInProperty(percent_in_property);
                            aa.setInOther(percent_in_other);
                            aa.setInclude(new Boolean(true));
                            aa.setAssetAllocationID(new Integer(-1));
                        } else {
                            // yes, than use the existing data
                            aa = f.getAssetAllocation();
                        }
                        // get always the current allocated amount from the
                        // Financial object
                        aa.setAmount((f.getAmount() == null) ? new Double(0.0)
                                : new Double(this.rint(f.getAmount()
                                        .doubleValue(), NUMBER_OF_DIGITS)));

                        aatr.amount = new FormatedBigDecimal(aa.getAmount());
                        aatr.percent_in_cash = new FormatedBigDecimal(aa
                                .getInCash());
                        aatr.percent_in_fixed_interest = new FormatedBigDecimal(
                                aa.getInFixedInterest());
                        aatr.percent_in_aust_shares = new FormatedBigDecimal(aa
                                .getInAustShares());
                        aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                aa.getInIntnlShares());
                        aatr.percent_in_property = new FormatedBigDecimal(aa
                                .getInProperty());
                        aatr.percent_in_other = new FormatedBigDecimal(aa
                                .getInOther());
                        aatr.total_in_percent = new FormatedBigDecimal(0.0);
                        aatr.include = aa.getInclude();

                        aatr.client_id = f.getOwnerId();
                        aatr.financial_id = f.getId();
                        aatr.asset_allocation_id = aa.getAssetAllocationID(); // (f.getAssetAllocationID()
                                                                                // ==
                                                                                // null)
                                                                                // ?
                                                                                // new
                                                                                // Integer(-1)
                                                                                // :
                                                                                // f.getAssetAllocationID();

                        new_data.add(aatr);
                    } catch (java.lang.NullPointerException e) {
                        System.err
                                .println("AssetAllocationTableModel::updateModel( java.util.Map financials )\t"
                                        + e.getMessage());
                        // can happen when asset was deleted, but database isn't
                        // updated yet
                        // ignore asset and process next one
                        // e.printStackTrace(System.err);
                    }
                }
            }

            objectTypeID = new Integer(ObjectTypeConstant.ASSET_INVESTMENT);
            java.util.Map assetInvestment = (java.util.Map) financials
                    .get(objectTypeID);

            if (assetInvestment != null && assetInvestment.size() > 0) {
                Iterator iter = assetInvestment.values().iterator();
                AssetInvestment f = null;

                while (iter.hasNext()) {
                    try {
                        f = (AssetInvestment) iter.next();

                        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

                        aatr.asset_name = new String(f.toString()); // new
                                                                    // String(
                                                                    // f.getFinancialTypeDesc()
                                                                    // );

                        AssetAllocation aa = null;
                        // do we have information about asset allocation
                        if (f.getAssetAllocationID() == null) {
                            // no, we habe to create a new item
                            aa = f.getAssetAllocation();

                            // try to find asset allocation in iress or morning
                            // database tables
                            FinancialAssetAllocation faa = new FinancialAssetAllocation();
                            AssetAllocationTableRow faa_aatr = faa
                                    .findAssetAllocation(f);
                            // and populate it
                            // aa.setAmount ( ( f.getAmount() == null ) ? new
                            // FormatedBigDecimal( 0.0 ) : new
                            // FormatedBigDecimal( f.getAmount().doubleValue() )
                            // );
                            aa.setInCash(new Double(faa_aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    faa_aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    faa_aatr.percent_in_aust_shares
                                            .doubleValue()));
                            aa.setInIntnlShares(new Double(
                                    faa_aatr.percent_in_intnl_shares
                                            .doubleValue()));
                            aa
                                    .setInProperty(new Double(
                                            faa_aatr.percent_in_property
                                                    .doubleValue()));
                            aa.setInOther(new Double(faa_aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(new Boolean(true));
                            aa.setAssetAllocationID(new Integer(-1));
                        } else {
                            // yes, than use the existing data
                            aa = f.getAssetAllocation();
                        }

                        // get always the current allocated amount from the
                        // Financial object
                        aa.setAmount((f.getAmount() == null) ? new Double(0.0)
                                : new Double(rint(f.getAmount().doubleValue(),
                                        NUMBER_OF_DIGITS)));

                        aatr.amount = new FormatedBigDecimal(aa.getAmount());
                        aatr.percent_in_cash = new FormatedBigDecimal(aa
                                .getInCash());
                        aatr.percent_in_fixed_interest = new FormatedBigDecimal(
                                aa.getInFixedInterest());
                        aatr.percent_in_aust_shares = new FormatedBigDecimal(aa
                                .getInAustShares());
                        aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                aa.getInIntnlShares());
                        aatr.percent_in_property = new FormatedBigDecimal(aa
                                .getInProperty());
                        aatr.percent_in_other = new FormatedBigDecimal(aa
                                .getInOther());
                        aatr.total_in_percent = new FormatedBigDecimal(0.0);
                        aatr.include = aa.getInclude();

                        aatr.client_id = f.getOwnerId();
                        aatr.financial_id = f.getId();
                        aatr.asset_allocation_id = aa.getAssetAllocationID(); // (f.getAssetAllocationID()
                                                                                // ==
                                                                                // null)
                                                                                // ?
                                                                                // new
                                                                                // Integer(-1)
                                                                                // :
                                                                                // f.getAssetAllocationID();

                        new_data.add(aatr);
                    } catch (java.lang.NullPointerException e) {
                        System.err
                                .println("AssetAllocationTableModel::updateModel( java.util.Map financials )\t"
                                        + e.getMessage());
                        // can happen when asset was deleted, but database isn't
                        // updated yet
                        // ignore asset and process next one
                        // e.printStackTrace(System.err);
                    }
                }
            }

            objectTypeID = new Integer(ObjectTypeConstant.ASSET_SUPERANNUATION);
            java.util.Map assetSuperannuation = (java.util.Map) financials
                    .get(objectTypeID);

            if (assetSuperannuation != null && assetSuperannuation.size() > 0) {
                Iterator iter = assetSuperannuation.values().iterator();
                AssetSuperannuation f = null;

                while (iter.hasNext()) {
                    try {
                        f = (AssetSuperannuation) iter.next();

                        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

                        aatr.asset_name = new String(f.toString()); // new
                                                                    // String(
                                                                    // f.getFinancialTypeDesc()
                                                                    // );

                        AssetAllocation aa = null;
                        // do we have information about asset allocation
                        if (f.getAssetAllocationID() == null) {
                            // no, we habe to create a new item
                            aa = f.getAssetAllocation();
                            // try to find asset allocation in iress or morning
                            // database tables
                            FinancialAssetAllocation faa = new FinancialAssetAllocation();
                            // AssetAllocationTableRow faa_aatr =
                            // faa.findAssetAllocation(f);
                            AssetAllocationTableRow faa_aatr = faa.findByFinancialCode(f.getFinancialCode());
                            // and populate it
                            // aa.setAmount ( ( f.getAmount() == null ) ? new
                            // FormatedBigDecimal( 0.0 ) : new
                            // FormatedBigDecimal( f.getAmount().doubleValue() )
                            // );
                            aa.setInCash(new Double(faa_aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    faa_aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    faa_aatr.percent_in_aust_shares
                                            .doubleValue()));
                            aa.setInIntnlShares(new Double(
                                    faa_aatr.percent_in_intnl_shares
                                            .doubleValue()));
                            aa
                                    .setInProperty(new Double(
                                            faa_aatr.percent_in_property
                                                    .doubleValue()));
                            aa.setInOther(new Double(faa_aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(new Boolean(true));
                            aa.setAssetAllocationID(new Integer(-1));

                        } else {
                            // yes, than use the existing data
                            aa = f.getAssetAllocation();
                        }

                        // get always the current allocated amount from the
                        // Financial object
                        aa.setAmount((f.getAmount() == null) ? new Double(0.0)
                                : new Double(rint(f.getAmount().doubleValue(),
                                        NUMBER_OF_DIGITS)));

                        aatr.amount = new FormatedBigDecimal(aa.getAmount());
                        aatr.percent_in_cash = new FormatedBigDecimal(aa
                                .getInCash());
                        aatr.percent_in_fixed_interest = new FormatedBigDecimal(
                                aa.getInFixedInterest());
                        aatr.percent_in_aust_shares = new FormatedBigDecimal(aa
                                .getInAustShares());
                        aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                aa.getInIntnlShares());
                        aatr.percent_in_property = new FormatedBigDecimal(aa
                                .getInProperty());
                        aatr.percent_in_other = new FormatedBigDecimal(aa
                                .getInOther());
                        aatr.total_in_percent = new FormatedBigDecimal(0.0);
                        aatr.include = aa.getInclude();

                        aatr.client_id = new Integer(f.getOwnerId()
                                .intValue());
                        aatr.financial_id = new Integer(f.getId()
                                .intValue());
                        aatr.asset_allocation_id = aa.getAssetAllocationID(); // (f.getAssetAllocationID()
                                                                                // ==
                                                                                // null)
                                                                                // ?
                                                                                // new
                                                                                // Integer(-1)
                                                                                // :
                                                                                // f.getAssetAllocationID();

                        new_data.add(aatr);
                    } catch (java.lang.NullPointerException e) {
                        System.err
                                .println("AssetAllocationTableModel::updateModel( java.util.Map financials )\t"
                                        + e.getMessage());
                        // can happen when asset was deleted, but database isn't
                        // updated yet
                        // ignore asset and process next one
                        // e.printStackTrace(System.err);
                    }
                }
            }

            objectTypeID = new Integer(ObjectTypeConstant.INCOME_STREAM);
            java.util.Map incomeStream = (java.util.Map) financials
                    .get(objectTypeID);

            if (incomeStream != null && incomeStream.size() > 0) {
                Iterator iter = incomeStream.values().iterator();
                while (iter.hasNext()) {
                    try {
                        IncomeStream f = (IncomeStream) iter.next();

                        AssetAllocationTableRow aatr = new AssetAllocationTableRow();

                        aatr.asset_name = new String(f.toString()); // new
                                                                    // String(
                                                                    // f.getFinancialTypeDesc()
                                                                    // );

                        AssetAllocation aa = f.getAssetAllocation();
                        // do we have information about asset allocation
                        if (f.getAssetAllocationID() == null) {
                            // no, we habe to create a new item
                            // try to find asset allocation in iress or morning
                            // database tables
                            FinancialAssetAllocation faa = new FinancialAssetAllocation();
                            // AssetAllocationTableRow faa_aatr =
                            // faa.findAssetAllocation(f);
                            AssetAllocationTableRow faa_aatr = faa.findByFinancialCode(f.getFinancialCode());
                            // and populate it
                            // aa.setAmount ( ( f.getAmount() == null ) ? new
                            // FormatedBigDecimal( 0.0 ) : new
                            // FormatedBigDecimal( f.getAmount().doubleValue() )
                            // );
                            aa.setInCash(new Double(faa_aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    faa_aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    faa_aatr.percent_in_aust_shares
                                            .doubleValue()));
                            aa.setInIntnlShares(new Double(
                                    faa_aatr.percent_in_intnl_shares
                                            .doubleValue()));
                            aa
                                    .setInProperty(new Double(
                                            faa_aatr.percent_in_property
                                                    .doubleValue()));
                            aa.setInOther(new Double(faa_aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(new Boolean(true));
                            aa.setAssetAllocationID(new Integer(-1));

                        } else {
                            // yes, than use the existing data
                        }

                        // get always the current allocated amount from the
                        // Financial object
                        aa.setAmount((f.getAmount() == null) ? new Double(0.0)
                                : new Double(rint(f.getAmount().doubleValue(),
                                        NUMBER_OF_DIGITS)));

                        aatr.amount = new FormatedBigDecimal(aa.getAmount());
                        aatr.percent_in_cash = new FormatedBigDecimal(aa
                                .getInCash());
                        aatr.percent_in_fixed_interest = new FormatedBigDecimal(
                                aa.getInFixedInterest());
                        aatr.percent_in_aust_shares = new FormatedBigDecimal(aa
                                .getInAustShares());
                        aatr.percent_in_intnl_shares = new FormatedBigDecimal(
                                aa.getInIntnlShares());
                        aatr.percent_in_property = new FormatedBigDecimal(aa
                                .getInProperty());
                        aatr.percent_in_other = new FormatedBigDecimal(aa
                                .getInOther());
                        aatr.total_in_percent = new FormatedBigDecimal(0.0);
                        aatr.include = aa.getInclude();

                        aatr.client_id = new Integer(f.getOwnerId()
                                .intValue());
                        aatr.financial_id = new Integer(f.getId()
                                .intValue());
                        aatr.asset_allocation_id = aa.getAssetAllocationID(); // (f.getAssetAllocationID()
                                                                                // ==
                                                                                // null)
                                                                                // ?
                                                                                // new
                                                                                // Integer(-1)
                                                                                // :
                                                                                // f.getAssetAllocationID();

                        new_data.add(aatr);
                    } catch (java.lang.NullPointerException e) {
                        System.err
                                .println("AssetAllocationTableModel::updateModel( java.util.Map financials )\t"
                                        + e.getMessage());
                        // can happen when asset was deleted, but database isn't
                        // updated yet
                        // ignore asset and process next one
                        // e.printStackTrace(System.err);
                    }
                }
            }
        } // if(...) prevent null pointer exception

        // update table rows
        setData(new_data);
    }

    /**
     * Saves the model with the assets of a person. It uses all assets of the
     * following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param person -
     *            a person object
     */
    public void saveModel(PersonService person)
            throws ServiceException {
        /***********************************************************************
         * FINANCIAL (usually client)
         **********************************************************************/
        Map financials = person.findFinancials();
        saveModel(financials);
        person.storeFinancials(financials, person.getFinancialGoal());
    }

    /**
     * Saves the model with the assets of a person. It uses all assets of the
     * following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param person -
     *            a person object
     */
    public void saveModel(java.util.Map financials) throws ServiceException {
        Integer objectTypeID = new Integer(ObjectTypeConstant.ASSET_CASH);
        java.util.Map assetCash = (java.util.Map) financials.get(objectTypeID);

        // System.err.println( this.getClass().getName() + " saveModel( person )
        // " );

        if (assetCash != null) {
            Iterator iter = assetCash.values().iterator();
            AssetCash f = null;

            while (iter.hasNext()) {
                f = (AssetCash) iter.next();

                if (f != null) {
                    // System.err.println( this.getClass().getName() + " -
                    // updating... " + f.toString() + "<" + f.getId()
                    // + ">" );

                    // search for financial id
                    AssetAllocationTableRow aatr = null;
                    AssetAllocation aa = null;

                    for (int i = 0; i < this._data.size() - 1; i++) { // -1,
                                                                        // because
                                                                        // ignore
                                                                        // last
                                                                        // table
                                                                        // row
                        aatr = (AssetAllocationTableRow) this._data
                                .elementAt(i);

                        if (f.getId().equals(aatr.financial_id)) {
                            // System.err.println( this.getClass().getName() + "
                            // - matching ids... " + f.toString() + "<" +
                            // f.getId() + ">" );
                            // update asset allocation...
                            // get asset allocation
                            aa = ((Financial) f).getAssetAllocation();
                            // System.err.println( "before aa.getInCash()=" +
                            // aa.getInCash() + ", " + aa.getAssetAllocationID()
                            // );
                            // and populate asset allocation
                            aa.setAmount(new Double(aatr.amount.doubleValue()));
                            aa.setInCash(new Double(aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    aatr.percent_in_aust_shares.doubleValue()));
                            aa
                                    .setInIntnlShares(new Double(
                                            aatr.percent_in_intnl_shares
                                                    .doubleValue()));
                            aa.setInProperty(new Double(
                                    aatr.percent_in_property.doubleValue()));
                            aa.setInOther(new Double(aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(aatr.include);
                            aa.setAssetAllocationID(aatr.asset_allocation_id);
                            f.setModified(true);
                        }
                    }
                } // end if
            } // end while
        }

        objectTypeID = new Integer(ObjectTypeConstant.ASSET_INVESTMENT);
        java.util.Map assetInvestment = (java.util.Map) financials
                .get(objectTypeID);

        if (assetInvestment != null) {
            Iterator iter = assetInvestment.values().iterator();
            AssetInvestment f = null;

            while (iter.hasNext()) {
                f = (AssetInvestment) iter.next();

                if (f != null) {
                    // search for financial id
                    AssetAllocationTableRow aatr = null;
                    AssetAllocation aa = null;

                    for (int i = 0; i < this._data.size() - 1; i++) { // -1,
                                                                        // because
                                                                        // ignore
                                                                        // last
                                                                        // table
                                                                        // row
                        aatr = (AssetAllocationTableRow) this._data
                                .elementAt(i);

                        if (f.getId().equals(aatr.financial_id)) {
                            // update asset allocation...
                            // get asset allocation
                            aa = ((Financial) f).getAssetAllocation();
                            // and populate asset allocation
                            aa.setAmount(new Double(aatr.amount.doubleValue()));
                            aa.setInCash(new Double(aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    aatr.percent_in_aust_shares.doubleValue()));
                            aa
                                    .setInIntnlShares(new Double(
                                            aatr.percent_in_intnl_shares
                                                    .doubleValue()));
                            aa.setInProperty(new Double(
                                    aatr.percent_in_property.doubleValue()));
                            aa.setInOther(new Double(aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(aatr.include);
                            aa.setAssetAllocationID(aatr.asset_allocation_id);
                            // f.setAssetAllocationID ( aatr.asset_allocation_id
                            // );
                            f.setModified(true);
                        }
                    }
                } // end if
            } // end while
        }

        objectTypeID = new Integer(ObjectTypeConstant.ASSET_SUPERANNUATION);
        java.util.Map assetSuperannuation = (java.util.Map) financials
                .get(objectTypeID);

        if (assetSuperannuation != null) {
            Iterator iter = assetSuperannuation.values().iterator();
            AssetSuperannuation f = null;

            while (iter.hasNext()) {
                f = (AssetSuperannuation) iter.next();

                if (f != null) {

                    // search for financial id
                    AssetAllocationTableRow aatr = null;
                    AssetAllocation aa = null;

                    for (int i = 0; i < this._data.size() - 1; i++) { // -1,
                                                                        // because
                                                                        // ignore
                                                                        // last
                                                                        // table
                                                                        // row
                        aatr = (AssetAllocationTableRow) this._data
                                .elementAt(i);

                        if (f.getId().equals(aatr.financial_id)) {
                            // update asset allocation...
                            // get asset allocation
                            aa = ((Financial) f).getAssetAllocation();
                            // and populate asset allocation
                            aa.setAmount(new Double(aatr.amount.doubleValue()));
                            aa.setInCash(new Double(aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    aatr.percent_in_aust_shares.doubleValue()));
                            aa
                                    .setInIntnlShares(new Double(
                                            aatr.percent_in_intnl_shares
                                                    .doubleValue()));
                            aa.setInProperty(new Double(
                                    aatr.percent_in_property.doubleValue()));
                            aa.setInOther(new Double(aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(aatr.include);
                            aa.setAssetAllocationID(aatr.asset_allocation_id);
                            // f.setAssetAllocationID ( aatr.asset_allocation_id
                            // );
                            f.setModified(true);
                        }
                    }
                } // end if
            } // end while
        }

        objectTypeID = new Integer(ObjectTypeConstant.INCOME_STREAM);
        java.util.Map incomeStream = (java.util.Map) financials
                .get(objectTypeID);

        if (incomeStream != null) {
            Iterator iter = incomeStream.values().iterator();
            IncomeStream f = null;

            while (iter.hasNext()) {
                f = (IncomeStream) iter.next();

                if (f != null) {

                    // search for financial id
                    AssetAllocationTableRow aatr = null;
                    AssetAllocation aa = null;

                    for (int i = 0; i < this._data.size() - 1; i++) { // -1,
                                                                        // because
                                                                        // ignore
                                                                        // last
                                                                        // table
                                                                        // row
                        aatr = (AssetAllocationTableRow) this._data
                                .elementAt(i);

                        if (f.getId().equals(aatr.financial_id)) {
                            // update asset allocation...
                            // get asset allocation
                            aa = ((Financial) f).getAssetAllocation();
                            // and populate asset allocation
                            aa.setAmount(new Double(aatr.amount.doubleValue()));
                            aa.setInCash(new Double(aatr.percent_in_cash
                                    .doubleValue()));
                            aa.setInFixedInterest(new Double(
                                    aatr.percent_in_fixed_interest
                                            .doubleValue()));
                            aa.setInAustShares(new Double(
                                    aatr.percent_in_aust_shares.doubleValue()));
                            aa
                                    .setInIntnlShares(new Double(
                                            aatr.percent_in_intnl_shares
                                                    .doubleValue()));
                            aa.setInProperty(new Double(
                                    aatr.percent_in_property.doubleValue()));
                            aa.setInOther(new Double(aatr.percent_in_other
                                    .doubleValue()));
                            aa.setInclude(aatr.include);
                            aa.setAssetAllocationID(aatr.asset_allocation_id);
                            // f.setAssetAllocationID ( aatr.asset_allocation_id
                            // );
                            f.setModified(true);
                        }
                    }
                } // end if
            } // end while
        }

        // mark that data was stored
        this.setModified(false);
    }

    protected static double rint(double x, int decimals) { // rounds to the
                                                            // nearest integer
        int factor = 1;
        for (int i = 0; i < Math.abs(decimals); i++)
            factor *= 10;
        if (decimals < 0)
            return factor * Math.rint(x / factor);
        else
            return Math.rint(factor * x) / factor;
    }

    protected void sortData() {
        if (this._data != null) {
            // sort Vector by asset name
            Comparator assetNameCmp = new AssetAllocationAssetNameComperator();
            Collections.sort((List) this._data, (Comparator) assetNameCmp);
        }
    }
}
