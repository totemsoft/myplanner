/*
 * ISmartRow.java
 *
 * Created on 29 November 2002, 13:37
 */

package com.argus.financials.swing.table;

/**
 * 
 * @author valeri chibaev
 */

public interface ISmartTableRow {

    public static final int HEADER1 = 1;

    public static final int HEADER2 = 3;

    public static final int HEADER3 = 5;

    public static final int HEADER4 = 7;

    public static final int HEADER5 = 9;

    public static final int BODY = 0;

    public static final int FOOTER5 = HEADER5 + 1;

    public static final int FOOTER4 = HEADER4 + 1;

    public static final int FOOTER3 = HEADER3 + 1;

    public static final int FOOTER2 = HEADER2 + 1;

    public static final int FOOTER1 = HEADER1 + 1;

    /**
     * 
     */
    public int getRowType();

    /**
     * Returns the value for the cell at columnIndex. Parameters: columnIndex -
     * the column whose value is to be queried Returns: the value Object at the
     * specified cell
     */
    public Object getValueAt(int columnIndex);

    /**
     * Sets the value in the cell at columnIndex to aValue. Parameters: aValue -
     * the new value columnIndex - the column whose value is to be changed
     */
    public void setValueAt(Object aValue, int columnIndex);

    /**
     * Returns true if the cell at columnIndex is editable. Otherwise,
     * setValueAt on the cell will not change the value of that cell.
     * Parameters: columnIndex - the column whose value to be queried Returns:
     * true if the cell is editable
     */
    public boolean isCellEditable(int columnIndex);

}
