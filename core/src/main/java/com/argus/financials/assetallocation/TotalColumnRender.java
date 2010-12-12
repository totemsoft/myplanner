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

/**
 * Displays the column's text of the last row of a table in blue color. Every
 * other row is displayed in black.
 * 
 * @author shibaevv
 * 
 * @see com.argus.financials.ui.assetallocation.AssetAllocationView
 * @see com.argus.financials.ui.assetallocation.AssetAllocationTableModel
 */
public class TotalColumnRender extends DefaultTableCellRenderer {
    private int _horizontal_alignment = JLabel.LEFT;

    /** Creates a new instance of TotalColumnRender */
    public TotalColumnRender() {
    }

    public TotalColumnRender(int horizontal_alignment) {
        _horizontal_alignment = horizontal_alignment;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        setHorizontalAlignment(_horizontal_alignment);

        // do we process the last row?
        if (row < (table.getRowCount() - 1)) {
            // no, than use black as font color
            setForeground(Color.black);
        } else {
            // yes, than use blue as font color
            setForeground(Color.blue);
        }

        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, col);
    }

}
