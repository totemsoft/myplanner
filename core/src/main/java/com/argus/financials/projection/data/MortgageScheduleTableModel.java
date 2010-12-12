/*
 * MortgageScheduleData.java
 *
 * Created on 29 May 2002, 21:47
 */

package com.argus.financials.projection.data;

/**
 * 
 * @author thomass
 * @version
 */

import java.util.Vector;

import javax.swing.JLabel;

public class MortgageScheduleTableModel extends
        javax.swing.table.AbstractTableModel {

    private Vector m_model;

    // Specify the attributes of the columns
    // Note: the additonal attributes witdth & alignment are not used by
    // default.
    // to enable the use of these attributes, you will need to set
    // setAutoCreateColumnsFromModel(false) and manually set various table
    // properties
    static final public ColumnData M_COLUMNS[] = {
            new ColumnData("Month", 70, JLabel.RIGHT),
            new ColumnData("Opening Balance", 150, JLabel.RIGHT),
            new ColumnData("Capital Payment", 150, JLabel.RIGHT),
            new ColumnData("Interest Payment", 150, JLabel.RIGHT),
            new ColumnData("Closing Balance", 150, JLabel.RIGHT) };

    /** Creates new MortgageScheduleData */
    public MortgageScheduleTableModel(Vector model) {
        this.m_model = model;
    }

    public int getColumnCount() {
        return M_COLUMNS.length;
    }

    public int getRowCount() {
        return m_model == null ? 0 : m_model.size();
    }

    public java.lang.Object getValueAt(int nRow, int nColumn) {
        if (nRow < 0 || nColumn >= this.getRowCount())
            return "";
        MortgageScheduleData row = (MortgageScheduleData) m_model
                .elementAt(nRow);
        switch (nColumn) {
        case 0:
            return row.month;
        case 1:
            return row.openingBalance;
        case 2:
            return row.capitalPayment;
        case 3:
            return row.interestPayment;
        case 4:
            return row.closingBalance;
        default:
            return "";
        }

    }

    /**
     * Specify the Class of each cell so JTable can apply the associated cell
     * renderer.
     */
    public Class getColumnClass(int nColumn) {
        switch (nColumn) {
        case 0:
            return Integer.class;
        case 1:
            return Double.class;
        case 2:
            return Double.class;
        case 3:
            return Double.class;
        case 4:
            return Double.class;
        default:
            return Object.class;
        }
    }

    public java.lang.String getColumnName(int param) {
        System.out.println("MortgageScheduleTableModel::getColumnNames:-"
                + M_COLUMNS[param].m_title);
        return M_COLUMNS[param].m_title;
    }

    public boolean isCellEditable(int param, int param1) {
        return false;
    }

}
