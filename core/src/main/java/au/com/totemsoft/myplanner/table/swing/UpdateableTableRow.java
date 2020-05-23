/*
 * UpdateableTableRow.java
 *
 * Created on 6 March 2003, 09:29
 */

package au.com.totemsoft.myplanner.table.swing;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public class UpdateableTableRow implements ISmartTableRow {

    public static final int STATUS_ORIGINAL = 0;

    public static final int STATUS_NEW = 1;

    public static final int STATUS_MODIFIED = 2;

    public static final int STATUS_DELETED = 3;

    private int rowStatus = STATUS_ORIGINAL;

    private ArrayList data = new ArrayList();

    private ArrayList columnStatus = new ArrayList();

    private ArrayList columnName = new ArrayList();

    private int rowID = 0;

    private int descriptionColumn = -1;

    private int identityColumn = -1;

    private int rowType = BODY;

    /** Creates new UpdateableTableRow */
    public UpdateableTableRow(ArrayList values, ArrayList columnsName) {
        if (values == null || columnsName == null
                || columnsName.size() != values.size())
            return;
        setRowStatus(STATUS_ORIGINAL);
        data.addAll(values);
        columnName.addAll(columnsName);
        rowID = hashCode();

        for (int i = 0; i < values.size(); i++) {
            columnStatus.add(i, String.valueOf(STATUS_ORIGINAL));
        }

    }

    public void setDescriptionColumn(int descriptionColumn) {
        this.descriptionColumn = descriptionColumn;
    }

    public int getDescriptionColumn() {
        return this.descriptionColumn;
    }

    public void setIdentityColumn(int identityColumn) {
        this.identityColumn = identityColumn;
    }

    public int getIdentityColumn() {
        return this.identityColumn;
    }

    public Object getIdentityValue() {
        if (data == null || identityColumn < 0)
            return null;
        return getValueAt(identityColumn);
    }

    public int getRowID() {
        return rowID;
    }

    public String toString() {
        if (descriptionColumn >= 0) {
            Object obj = getValueAt(descriptionColumn);
            if (obj instanceof String)
                return (String) obj;
            else if (obj != null)
                return obj.toString();
        }
        return "No description";
    }

    public void setRowStatus(int rowStatus) {
        this.rowStatus = rowStatus;
    }

    public int getRowStatus() {
        return this.rowStatus;
    }

    public void setColumnStatus(int columnInd, int columnStatus) {

    }

    public int getColumnStatus(int columnInd) {
        if (columnInd < 0 || columnInd > columnStatus.size())
            return -1;
        String stat = (String) columnStatus.get(columnInd);
        int status = Integer.parseInt(stat);
        return status;
    }

    /**
     * Returns the value for the cell at columnIndex. Parameters: columnIndex -
     * the column whose value is to be queried Returns: the value Object at the
     * specified cell
     */
    public Object getValueAt(int columnIndex) {
        if (data == null || columnIndex >= data.size())
            return null;
        return data.get(columnIndex);
    }

    /**
     * Sets the value in the cell at columnIndex to aValue. Parameters: aValue -
     * the new value columnIndex - the column whose value is to be changed
     */
    public void setValueAt(Object aValue, int columnIndex) {
        if (data != null)
            data.set(columnIndex, aValue);
        if (columnStatus != null) {
            columnStatus.set(columnIndex, String.valueOf(STATUS_MODIFIED));
            if (getRowStatus() != STATUS_NEW)
                setRowStatus(STATUS_MODIFIED);
        }
    }

    /**
     * Returns true if the cell at columnIndex is editable. Otherwise,
     * setValueAt on the cell will not change the value of that cell.
     * Parameters: columnIndex - the column whose value to be queried Returns:
     * true if the cell is editable
     */
    public boolean isCellEditable(int columnIndex) {
        return true;
    }

    public int getRowType() {
        return rowType;
    }

    public HashMap getValuesAsHashMap() {
        HashMap hm = new HashMap();
        for (int i = 0; i < data.size(); i++) {
            hm.put(columnName.get(i), data.get(i));
        }

        return hm;
    }

    public void setRowType(int type) {
        rowType = type;
    }

    /**
     * Compare set of values with actial data in the current row. Return true if
     * all column's values satisfy search condition. Otherwise return false.
     * Used in setFilterXXX method.
     * 
     */
    public boolean equals(int[] colIndex, Object[] values) {

        if (colIndex == null || values == null)
            return false;
        for (int i = 0; i < colIndex.length; i++) {
            if (values[i] == null || !values[i].equals(getValueAt(colIndex[i])))
                return false;
        }
        return true;
    }

    public int getColumnIndex(String name) {

        int index = -1;
        if (columnName == null || name == null)
            return index;
        for (int i = 0; i < columnName.size(); i++) {
            String pattern = columnName.get(i) instanceof String ? (String) columnName
                    .get(i)
                    : "NotString";
            if (name.equalsIgnoreCase(pattern))
                return i;
        }

        return index;
    }

}
