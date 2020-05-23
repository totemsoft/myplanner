/*
 * SmartTableModel.java
 *
 * Created on 29 November 2002, 14:00
 */

package au.com.totemsoft.myplanner.table.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.util.Vector;

public abstract class SmartTableModel extends
        javax.swing.table.AbstractTableModel implements ISmartTableModel {
    private Vector data;

    private Vector columnNames;

    private Vector columnClasses;

    /**
     * Creates a new instance of SmartTableModel Parameters: data - Vector<ISmartTableRow>
     */
    protected SmartTableModel() {
        this(null, null, null);
    }

    protected SmartTableModel(Vector data, Vector columnNames,
            Vector columnClasses) {
        this.data = data;
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
    }

    protected void setData(Vector data) {
        this.data = data;
    }

    protected Vector getData() {
        return data;
    }

    protected void setColumnNames(Vector columnNames) {
        this.columnNames = columnNames;
    }

    public Vector getColumnNames() {
        return columnNames;
    }

    // public abstract Vector getSeries();

    protected void setColumnClasses(Vector columnClasses) {
        this.columnClasses = columnClasses;
    }

    /***************************************************************************
     * javax.swing.table.TableModel
     **************************************************************************/
    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public String getColumnName(int columnIndex) {
        return (String) columnNames.get(columnIndex);
    }

    public Class getColumnClass(int columnIndex) {
        return (Class) columnClasses.get(columnIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        ISmartTableRow row = getRowAt(rowIndex);
        return row == null ? false : row.isCellEditable(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ISmartTableRow row = getRowAt(rowIndex);
        return row == null ? null : row.getValueAt(columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ISmartTableRow row = getRowAt(rowIndex);
        if (row != null)
            row.setValueAt(aValue, columnIndex);
        fireTableDataChanged();
    }

    /***************************************************************************
     * au.com.totemsoft.swing.table.ISmartTableModel
     **************************************************************************/
    public ISmartTableRow getRowAt(int rowIndex) {
        return (ISmartTableRow) data.get(rowIndex);
    }

    public void setRowAt(ISmartTableRow aValue, int rowIndex) {
        data.set(rowIndex, aValue);
    }

    public void pack() {

    }

    /***************************************************************************
     * Sorts the contents, based on alfabetical order.
     **************************************************************************/
    protected void sort(Object[] a) {
        new AlfaSorter().sort(a);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract class AbstractSmartTableRow implements ISmartTableRow {

        // TODO:
        // set true when row data changed (require re-calculation)
        // set false when calculations for this row completed
        private boolean modified = true;

        private int type;

        protected AbstractSmartTableRow(int type) {
            this.type = type;
        }

        public int getRowType() {
            return type;
        }

        protected boolean isModified() {
            return modified;
        }

        // clear raw data
        public void clear() { /* do nothing here */
        }

        public boolean isCellEditable(int columnIndex) {
            return false;
        }

        public Object getValueAt(int columnIndex) {
            return toString();
        }

        public void setValueAt(Object aValue, int columnIndex) {
            modified = true;
        }

    }

}
