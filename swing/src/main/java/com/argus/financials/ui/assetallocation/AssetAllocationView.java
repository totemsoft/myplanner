/*
 * AssetAllocationView.java
 *
 * Created on 10 October 2002, 10:07
 */

package com.argus.financials.ui.assetallocation;

import java.text.DecimalFormat;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.argus.financials.assetallocation.TotalColumnRender;
import com.argus.financials.ui.AbstractPanel;
import com.argus.format.DecimalField;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public abstract class AssetAllocationView
    extends AbstractPanel
{

    protected static final String STRING_EMPTY = "";

    protected static final String STRING_ZERO = "0";

    protected static final String STRING_ZERO_PERCENT = "0.00%";

    protected static final String STRING_ZERO_DOLLAR = "$0.00";

    /**
     * Sets the column size for one columns.
     * 
     * @param table -
     *            a table
     * @param columnID -
     *            a column ID
     * @param maxWidth -
     *            maximum width of the column
     * @param minWidth -
     *            minimum width of the column
     * @param preferredWidth -
     *            preferred width of the column
     */
    protected void setColumnWidth(JTable table, int columnID, int maxWidth,
            int minWidth, int preferredWidth) {
        TableColumn column = table.getColumnModel().getColumn(columnID);
        if (column == null)
            return;
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    /**
     * Initialize the asset table, the methods sets the column size for all
     * columns.
     */
    protected void initTable(JTable jtable) {
        // if ( rowData == null )
        // rowData = new Object[][]{};

        DefaultTableModel tm = (DefaultTableModel) jtable.getModel();
        // tm.setDataVector( rowData, getColumnNames() );

        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_NAME, 1000,
                225, 200);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_AMOUNT, 1000,
                60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_CASH, 1000, 60,
                100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_FIXED_INTEREST,
                1000, 60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_AUST_SHARES,
                1000, 60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_INTNL_SHARES,
                1000, 60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_PROPERTY, 1000,
                60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_OTHER, 1000,
                60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_TOTAL, 1000,
                60, 100);
        setColumnWidth(jtable, AssetAllocationTableModel.COLUMN_INCLUDE, 1000,
                30, 100);
        // setColumnWidth( jtable, AssetAllocationTableModel.COLUMN_CLIENT_ID ,
        // 1000, 50, 100 );
        // setColumnWidth( jtable, AssetAllocationTableModel.COLUMN_FINANCIAL_ID
        // , 1000, 50, 100 );
        // setColumnWidth( jtable,
        // AssetAllocationTableModel.COLUMN_ASSET_ALLOCATION_ID , 1000, 50, 100
        // );

        TotalColumnRender tcr_left = new TotalColumnRender(JLabel.LEFT);
        TotalColumnRender tcr_right = new TotalColumnRender(JLabel.RIGHT);
        TotalPercentColumnRender tpcr = new TotalPercentColumnRender(
                JLabel.RIGHT);

        // set ColumnRender
        jtable.getColumn(
                jtable.getColumnName(AssetAllocationTableModel.COLUMN_NAME))
                .setCellRenderer(tcr_left);
        jtable.getColumn(
                jtable.getColumnName(AssetAllocationTableModel.COLUMN_AMOUNT))
                .setCellRenderer(tcr_right);

        jtable.getColumn(
                jtable.getColumnName(AssetAllocationTableModel.COLUMN_CASH))
                .setCellRenderer(tpcr);
        jtable
                .getColumn(
                        jtable
                                .getColumnName(AssetAllocationTableModel.COLUMN_FIXED_INTEREST))
                .setCellRenderer(tpcr);
        jtable
                .getColumn(
                        jtable
                                .getColumnName(AssetAllocationTableModel.COLUMN_AUST_SHARES))
                .setCellRenderer(tpcr);
        jtable
                .getColumn(
                        jtable
                                .getColumnName(AssetAllocationTableModel.COLUMN_INTNL_SHARES))
                .setCellRenderer(tpcr);
        jtable
                .getColumn(
                        jtable
                                .getColumnName(AssetAllocationTableModel.COLUMN_PROPERTY))
                .setCellRenderer(tpcr);
        jtable.getColumn(
                jtable.getColumnName(AssetAllocationTableModel.COLUMN_OTHER))
                .setCellRenderer(tpcr);
        jtable.getColumn(
                jtable.getColumnName(AssetAllocationTableModel.COLUMN_TOTAL))
                .setCellRenderer(tpcr);

        //
        // set default cell editor for columns with FormatedBigDecimal
        //
        DefaultCellEditor dce_FormatedBigDecimal;
        DecimalFormat decimal_format;

        decimal_format = new DecimalFormat("###.##");
        decimal_format.setMinimumFractionDigits(0);
        decimal_format.setMinimumIntegerDigits(0);
        decimal_format.setMaximumFractionDigits(3);
        decimal_format.setMaximumIntegerDigits(3);

        final DecimalField decimalField = new DecimalField(0.0, 0,
                decimal_format);
        decimalField.setHorizontalAlignment(decimalField.RIGHT);

        dce_FormatedBigDecimal = new DefaultCellEditor(decimalField);

        dce_FormatedBigDecimal.setClickCountToStart(1);
        jtable.setDefaultEditor(com.argus.math.FormatedBigDecimal.class,
                dce_FormatedBigDecimal);
        // ((DefaultCellEditor)jtable.getDefaultEditor(com.argus.financial.assetallocation.FormatedBigDecimal.class)).setClickCountToStart(1);
    }

    /**
     * Deletes the '%' and ',' from a given String.
     * 
     * @param str -
     *            a String
     * @param the
     *            given String without any '%' char
     */
    protected String deletePercentAndCommaSign(String str) {
        String help = "";
        char c;

        if (str != null && str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);
                if (c != '%' && c != ',') {
                    help += c;
                }
            }
        }

        return (help.length() == 0) ? "0.0" : help;
    }

    /**
     * Deletes the '%' from a given String.
     * 
     * @param str -
     *            a String
     * @param the
     *            given String without any '%' char
     */
    protected String deletePercentSign(String str) {
        String help = "";
        char c;

        if (str != null && str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);
                if (c != '%') {
                    help += c;
                }
            }
        }

        return (help.length() == 0) ? "0.0" : help;
    }
}
