/*
 * UpdateableTableModel.java
 *
 * Created on 6 March 2003, 12:14
 */

package com.argus.financials.swing.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author shibaevv
 * 
 * @version
 */
public class UpdateableTableModel extends AbstractTableModel implements
        ISmartTableModel {

    private ResultSetMetaData metaData;

    private ArrayList current = new ArrayList();

    private ArrayList original = new ArrayList();

    private ArrayList filtered = new ArrayList();

    private ArrayList deleted = new ArrayList();

    private int identityColumn = 0;

    private int descriptionColumn = 0;

    private UpdateableTableRow emptyRow;

    /** Identifies current row in the table model. */
    private int pointer = 0;

    /** Creates new UpdateableTableModel */
    public UpdateableTableModel(ResultSet rs) {

        try {

            metaData = rs.getMetaData();

            while (rs.next()) {

                ArrayList dataRow = new ArrayList();
                ArrayList columnName = new ArrayList();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    dataRow.add(rs.getObject(i));
                    // columnClass.add(metaData.getColumnTypeName(i)) ;
                    columnName.add(metaData.getColumnName(i));
                }
                this.addRow(new UpdateableTableRow(dataRow, columnName));

            }
            // Create empty row template

            // rs.insertRow();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }

    public UpdateableTableModel(String[] columns) {

        metaData = new DummyMetaData(columns);
    }

    public void addRow(ISmartTableRow row) {
        current.add(row);
        original.add(row);
        this.pointer = getRowCount() - 1;
    }

    public int addEmptyRow(Object description, Object identity) {
        emptyRow = createEmptyRow(description, identity);
        this.addRow(emptyRow);
        this.fireTableRowsInserted(this.getRowCount() - 1,
                this.getRowCount() - 1);
        return this.getRowCount();
    }

    public UpdateableTableRow createEmptyRow(Object description, Object identity) {
        ArrayList empty = new ArrayList();
        ArrayList columnClass = new ArrayList();
        try {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                empty.add(getNewObject(i - 1));
                // columnClass.add(metaData.getColumnTypeName(i)) ;
                columnClass.add(metaData.getColumnName(i));
            }
        } catch (Exception e) {
        }
        emptyRow = new UpdateableTableRow(empty, columnClass);
        emptyRow.setRowStatus(emptyRow.STATUS_NEW);

        int desc = this.getDescriptionColumn();
        int ident = this.getIdentityColumn();

        if (desc >= 0 && description != null)
            emptyRow.setValueAt(description, desc);
        if (ident >= 0 && identity != null)
            emptyRow.setValueAt(identity, ident);
        emptyRow.setDescriptionColumn(desc);
        emptyRow.setIdentityColumn(ident);

        return emptyRow;
    }

    public java.lang.Object getValueAt(int row, int column) {
        if (current.size() <= row)
            return null;
        return ((ISmartTableRow) current.get(row)).getValueAt(column);
    }

    /**
     * Returns the value for the row at rowIndex. Parameters: rowIndex - the row
     * whose value is to be queried Returns: the value Object at the specified
     * row
     */
    public ISmartTableRow getRowAt(int row) {
        return row < current.size() ? (ISmartTableRow) current.get(row) : null;
    }

    /**
     * Returns the value for the row at rowIndex (deleted buffer). Parameters:
     * rowIndex - the row whose value is to be queried Returns: the value Object
     * at the specified row
     */
    public ISmartTableRow getDeletedRowAt(int row) {
        return row < deleted.size() ? (ISmartTableRow) deleted.get(row) : null;
    }

    public java.lang.String getColumnName(int column) {

        String colName = "Unknown";
        if (metaData != null) {
            try {
                colName = metaData.getColumnLabel(column + 1);
            } catch (Exception e) {
                return colName;
            }
        }
        return colName;
    }

    public int getColumnSize(int column) {

        int colSize = 0;
        if (metaData != null) {
            try {
                colSize = metaData.getColumnDisplaySize(column + 1);
            } catch (Exception e) {
                return colSize;
            }
        }
        return colSize;
    }

    /*
     * columnSize[column] =
     * String.valueOf(metaData.getColumnDisplaySize(column+1));
     * columnType[column] = metaData.getColumnTypeName(column+1);
     * columnPrecision[column] =
     * String.valueOf(metaData.getPrecision(column+1)); columnScale[column] =
     * String.valueOf(metaData.getScale(column+1)); columnTableName[column] =
     * metaData.getTableName(column+1); columnTypeNumeric [column] = new
     * Integer(metaData.getColumnType(column+1));
     */

    public String[] getColumnNames() {

        String[] names = null;

        try {
            int columns = metaData.getColumnCount();
            names = new String[columns];
            for (int i = 1; i <= columns; i++) {
                names[i - 1] = metaData.getColumnLabel(i);
            }

        } catch (Exception e) {
        }

        return names;

    }

    public int getRowCount() {
        if (current != null)
            return current.size();
        return 0;
    }

    public int getDeletedRowCount() {
        if (deleted != null)
            return deleted.size();
        return 0;
    }

    public int getFilteredRowCount() {
        if (filtered != null)
            return filtered.size();
        return 0;
    }

    public void deleteRow(int row) {
        Object rowDeleted = current.remove(row);
        deleted.add(rowDeleted);
        this.fireTableDataChanged();
        // Change the value of the pointer. If there is no records in the
        // database the pointer is -1.
        this.pointer = this.pointer - 1;
        if (getRowCount() == 0)
            this.pointer = -1;
        else if (this.pointer == -1 && getRowCount() > 0)
            this.pointer = 0;
    }

    /**
     * Delete null/empty rows
     */
    public void pack() {
    }

    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void setValueAt(java.lang.Object value, int row, int column) {
        ISmartTableRow iRow = this.getRowAt(row);
        if (iRow == null)
            return;
        iRow.setValueAt(value, column);
    }

    public java.lang.Class getColumnClass(int column) {
        int type;
        try {
            type = metaData.getColumnType(column + 1);
        } catch (SQLException e) {
            return Object.class;
        }

        switch (type) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return String.class;

        case Types.BIT:
            return Boolean.class;

        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
            return Integer.class;

        case Types.BIGINT:
            return Long.class;

        case Types.NUMERIC:
        case Types.DECIMAL:
        case Types.DOUBLE:
            return Double.class;
        case Types.REAL:
        case Types.FLOAT:
            return Float.class;

        case Types.DATE:
            return java.sql.Date.class;

        default:
            return Object.class;
        }
    }

    public Object getNewObject(int column) {
        java.lang.Class classID = getColumnClass(column);
        if (classID.equals(String.class))
            return "";
        if (classID.equals(Boolean.class))
            return new Boolean(false);
        if (classID.equals(Integer.class))
            return new Integer(0);
        if (classID.equals(Long.class))
            return new Long(0);
        if (classID.equals(Double.class))
            return new Double(0);
        if (classID.equals(Float.class))
            return new Float(0);
        if (classID.equals(java.sql.Date.class))
            return new java.sql.Date(0);
        return "";

    }

    // public void addTableModelListener(javax.swing.event.TableModelListener
    // tableModelListener) {
    // }

    /**
     * Sets the value in the row at rowIndex to aValue. Parameters: aValue - the
     * new value rowIndex - the row whose value is to be changed
     */
    public void setRowAt(ISmartTableRow aValue, int rowIndex) {
    }

    public int getColumnCount() {
        int columnCount = 0;
        if (metaData != null) {
            try {
                columnCount = metaData.getColumnCount();
            } catch (Exception e) {
            }

        }

        return columnCount;
    }

    public int getColumnIndex(String name) {

        String[] names = getColumnNames();
        int index = -1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(name))
                return i;
        }

        return index;
    }

    public void removeTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
    }

    public void setDescriptionColumn(String columnName) {
        int column = 0;
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equalsIgnoreCase(columnName)) {
                column = i;
                break;
            }
        }
        this.descriptionColumn = column;
        for (int i = 0; i < getRowCount(); i++) {
            ((UpdateableTableRow) getRowAt(i)).setDescriptionColumn(column);
        }

    }

    public int getDescriptionColumn() {
        return this.descriptionColumn;
    }

    public void setIdentityColumn(String columnName) {
        int column = 0;
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equalsIgnoreCase(columnName)) {
                column = i;
                break;
            }
        }

        this.identityColumn = column;
        for (int i = 0; i < getRowCount(); i++) {
            ((UpdateableTableRow) getRowAt(i)).setIdentityColumn(column);
        }
    }

    public int getIdentityColumn() {
        return this.identityColumn;
    }

    /**
     * Returns the pointer to the current row .
     */

    public int getPointer() {
        return this.pointer;
    }

    /**
     * Initial value of the pointer is 0. If table model doesn’t contain any
     * records value is –1. When the current row is deleted the pointer set to
     * pointer-1. When the row are added the pointer points at new row.
     */

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public void setRowStatus(int row, int status) {

        UpdateableTableRow iRow = (UpdateableTableRow) getRowAt(row);
        if (iRow != null)
            iRow.setRowStatus(status);

    }

    public int getRowStatus(int row) {

        UpdateableTableRow iRow = (UpdateableTableRow) getRowAt(row);
        if (iRow != null)
            return iRow.getRowStatus();
        return -1;

    }

    public ArrayList getModifiedColumns(int row) {
        ArrayList modified = new ArrayList();
        UpdateableTableRow iRow = (UpdateableTableRow) getRowAt(row);
        if (iRow == null)
            return modified;

        for (int i = 0; i < getColumnCount(); i++) {
            if (iRow.getColumnStatus(i) == iRow.STATUS_MODIFIED) {
                modified.add(getColumnName(i));
            }
        }
        return modified;
    }

    public int indexOf(ISmartTableRow row) {
        for (int i = 0; i < getRowCount(); i++) {
            if (((UpdateableTableRow) getRowAt(i)).getRowID() == ((UpdateableTableRow) row)
                    .getRowID())
                return i;
        }
        return -1;
    }

    public Object getItemByIdentity(Object searchCriteria, int index) {

        int currentIndex = -1;
        for (int i = 0; i < getRowCount(); i++) {
            UpdateableTableRow row = (UpdateableTableRow) getRowAt(i);
            int colDestination = row.getIdentityColumn();
            Object valueToCheck = row.getValueAt(colDestination);
            if (valueToCheck != null && valueToCheck.equals(searchCriteria)) {
                currentIndex = currentIndex + 1;
                if (currentIndex == index)
                    return row;
            }
        }
        return null;

    }

    public int getIdentityIndex(Object searchCriteria) {

        int currentIndex = -1;
        for (int i = 0; i < getRowCount(); i++) {
            UpdateableTableRow row = (UpdateableTableRow) getRowAt(i);
            int colDestination = row.getIdentityColumn();
            Object valueToCheck = row.getValueAt(colDestination);
            if (valueToCheck != null && valueToCheck.equals(searchCriteria)) {
                return i;
            }
        }
        return currentIndex;

    }

    public int getRowCountByIdentity(Object searchCriteria) {

        int currentIndex = 0;
        for (int i = 0; i < getRowCount(); i++) {
            UpdateableTableRow row = (UpdateableTableRow) getRowAt(i);
            int colDestination = row.getIdentityColumn();
            Object valueToCheck = row.getValueAt(colDestination);
            if (valueToCheck != null && valueToCheck.equals(searchCriteria)) {
                currentIndex = currentIndex + 1;
            }
        }
        return currentIndex;

    }

    public int setRowsFilter(String[] columns, Object[] values) {

        int filterCount = 0;

        int[] colIndex = new int[columns.length];

        for (int i = 0; i < columns.length; i++) {
            int ind = getColumnIndex(columns[i]);
            if (ind >= 0)
                colIndex[i] = ind;
            // No column found : nothing to do
            else
                return filterCount;
        }

        filtered = new ArrayList();
        current = new ArrayList();
        for (int i = 0; i < original.size(); i++) {
            UpdateableTableRow row = (UpdateableTableRow) original.get(i);
            if (!row.equals(colIndex, values)) {
                filtered.add(row);
            } else {
                current.add(row);
                filterCount = filterCount + 1;
            }
        }
        this.fireTableDataChanged();
        return filterCount;

    }

    /**
     * Reset filter. All data from filtered buffer are removed. Current buffer
     * has values from original buffer
     */
    public void setRowsFilter() {
        filtered = new ArrayList();
        current = new ArrayList(original);
        this.fireTableDataChanged();
    }

    /**
     * Reset deleted buffer. All data from deleted buffer are removed. Also
     * original buffer is refined from deleted rows.
     */
    public void removeDeleted() {
        original.removeAll(deleted);
        deleted = new ArrayList();
        this.fireTableDataChanged();
    }

    /**
     * Filter out all rows from current buffer .
     */
    public void setRowsFilterAll() {
        filtered = new ArrayList(current);
        current = new ArrayList();
        this.fireTableDataChanged();
    }

}
