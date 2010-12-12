/*
 * TotalColumnRender.java
 *
 * Created on 10 October 2002, 13:01
 */

package com.argus.financials.assetallocation;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.argus.math.FormatedBigDecimal;

/**
 * Displays the text of a column in black, if the value is less or equal 100
 * (%). If it's bigger than 100, than it's displayed in red.
 * 
 * @author shibaevv
 * 
 * @see com.argus.financials.ui.assetallocation.AssetAllocationView
 * @see com.argus.financials.ui.assetallocation.AssetAllocationTableModel
 */

public class TotalPercentColumnRender extends DefaultTableCellRenderer {
    private int _horizontal_alignment = JLabel.LEFT;

    /** Creates a new instance of TotalColumnRender */
    public TotalPercentColumnRender() {
    }

    public TotalPercentColumnRender(int horizontal_alignment) {
        _horizontal_alignment = horizontal_alignment;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        setHorizontalAlignment(_horizontal_alignment);

        if (row < (table.getRowCount() - 1)) { // !!! ignore last row !!!
            // && col == AssetAllocationTableModel.COLUMN_TOTAL
            // && value instanceof Double ) {
            switch (col) {
            case AssetAllocationTableModel.COLUMN_AMOUNT:
            case AssetAllocationTableModel.COLUMN_CASH:
            case AssetAllocationTableModel.COLUMN_FIXED_INTEREST:
            case AssetAllocationTableModel.COLUMN_AUST_SHARES:
            case AssetAllocationTableModel.COLUMN_INTNL_SHARES:
            case AssetAllocationTableModel.COLUMN_PROPERTY:
            case AssetAllocationTableModel.COLUMN_OTHER:
            case AssetAllocationTableModel.COLUMN_TOTAL:
                if (value instanceof FormatedBigDecimal) {
                    double help = ((FormatedBigDecimal) value).doubleValue();

                    if (help > 100.0) {
                        setForeground(Color.red);
                    } else if (col == AssetAllocationTableModel.COLUMN_TOTAL
                            && help < 100.0) {
                        setForeground(new Color(0, 160, 0));
                    } else {
                        setForeground(Color.black);
                    }
                } // end if
                break;

            default:
                break;
            }
        } else {
            setForeground(Color.blue);
        }

        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, col);
    }

}
