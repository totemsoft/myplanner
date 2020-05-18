/*
 * AssetInvestmentAvailableInvestmentsTableModel.java
 *
 * Created on 19 July 2002, 08:59
 */

package com.argus.financials.assetinvestment;

import java.util.Vector;

/**
 * Table model for displaying the available assets and investments. The
 * following fields are displayed: Investment Code, Investment Description. <BR>
 * Internal all rows are stored in a vector object. Each row is an instance of
 * the "AvailableInvestmentsTableRow" class.
 * 
 * @author shibaevv
 * @version 0.01
 * 
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSearch
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsTableRow
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSelectionInvestmentCodeComperator
 * @see com.argus.financials.assetinvestment.AvailableInvestmentsSelectionDescriptionComperator
 */
public class AvailableInvestmentsTableModel extends
        javax.swing.table.AbstractTableModel {

    /** Creates new AssetInvestmentAvailableInvestmentsTableModel */
    public AvailableInvestmentsTableModel() {
        /* BEGIN: only for testing */
        /*
         * AvailableInvestmentsTableRow row_1 = new
         * AvailableInvestmentsTableRow( new String("<dummy>") , new
         * String("1000 1000") , new String("AAA - A....") );
         * 
         * AvailableInvestmentsTableRow row_2 = new
         * AvailableInvestmentsTableRow( new String("<dummy>") , new
         * String("2000 2000") , new String("AAA - B....") );
         * 
         * _table_rows = new Vector();
         * 
         * for( int i=0; i<20; i++ ) { _table_rows.add( row_1 );
         * _table_rows.add( row_2 ); }
         */
        /* END: only for testing */
    }

    /** Creates new AssetInvestmentAvailableInvestmentsTableModel */
    public AvailableInvestmentsTableModel(Vector rows) {
        _table_rows = rows;
    }

    private Vector _table_rows;

    private final String[] columnNames = new String[] { "Investment Code",
            "Investment Description", };

    /*
     * private final Object[][] data = new Object [][] { {"AAA - A....", "1000
     * 1000"}, {"AAA - B...", "12345678"} };
     */

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        if (_table_rows != null) {
            return _table_rows.size();
        } else {
            return 0;
        }
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        if (_table_rows == null || col < 0 || col >= this.getColumnCount()
                || row < 0 || row >= this.getRowCount())
            return "";

        AvailableInvestmentsTableRow table_row = (AvailableInvestmentsTableRow) _table_rows
                .elementAt(row);

        switch (col) {
        case 0:
            return table_row.investmentCode;
        case 1:
            return table_row.description;
        default:
            return "";
        }
    }

    public Class getColumnClass(int c) {
        Object valueAt = getValueAt(0, c);

        if (valueAt != null) {
            return valueAt.getClass();
        }

        return null;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

}
