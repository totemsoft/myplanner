/*
 * ProxyTableModel.java
 *
 * Created on 7 March 2003, 12:09
 */

package au.com.totemsoft.myplanner.table.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class ProxyTableModel implements ISmartTableModel {

    private ISmartTableModel tm;

    private int[][] columnsMap;

    private int[][] rowTypesMap;

    /** Creates a new instance of ProxyTableModel */
    public ProxyTableModel(ISmartTableModel tm, int[] columns) {
        this(tm, columns, null);
    }

    public ProxyTableModel(ISmartTableModel tm, int[] columns, int[] rowTypes) {

        super();

        this.tm = tm;

        setColumns(columns);
        setRowTypes(rowTypes);

    }

    public ISmartTableModel getFullTableModel() {
        return tm;
    }

    public void setColumns(int[] columns) {

        if (columns == null) {
            columnsMap = null;

        } else {
            int length = Math.min(columns.length, tm.getColumnCount());

            columnsMap = new int[length][2];
            for (int i = 0; i < length; i++) {
                columnsMap[i][0] = i;
                columnsMap[i][1] = columns[i];
            }

        }

    }

    public void setRowTypes(int[] rowTypes) {

        if (rowTypes == null || rowTypes.length == 0) {
            rowTypesMap = null;

        } else {
            int length = tm.getRowCount();
            int[][] rowTypesMap2 = new int[length][2];

            int n = 0;
            for (int i = 0; i < length; i++) {
                ISmartTableRow row = tm.getRowAt(i);

                boolean in = false;
                int rowType = row.getRowType();
                for (int j = 0; j < rowTypes.length; j++) {
                    if (in = rowType == rowTypes[j])
                        break;
                }

                if (!in)
                    continue;

                rowTypesMap2[n][0] = n;
                rowTypesMap2[n][1] = i;
                n++;
            }

            rowTypesMap = new int[n][2];
            System.arraycopy(rowTypesMap2, 0, rowTypesMap, 0, n);

        }

    }

    private int mapRowIndex(int rowIndex) {
        if (rowTypesMap == null)
            return rowIndex;

        rowIndex = rowTypesMap[rowIndex][1];
        if (rowIndex >= tm.getRowCount()) {
            System.err.println("\tProxyTableModel::mapRowIndex()=" + rowIndex);
            rowIndex = tm.getRowCount() - 1;
        }
        return rowIndex;
    }

    private int mapColumnIndex(int columnIndex) {
        if (columnsMap == null)
            return columnIndex;

        columnIndex = columnsMap[columnIndex][1];
        if (columnIndex >= tm.getColumnCount()) {
            System.err.println("\tProxyTableModel::mapColumnIndex()="
                    + columnIndex);
            columnIndex = tm.getColumnCount() - 1;
        }
        return columnIndex;
    }

    public void addTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
        tm.addTableModelListener(tableModelListener);
    }

    public void removeTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
        tm.removeTableModelListener(tableModelListener);
    }

    public Class getColumnClass(int columnIndex) {
        return tm.getColumnClass(mapColumnIndex(columnIndex));
    }

    public int getColumnCount() {
        return columnsMap == null ? tm.getColumnCount() : columnsMap.length;
    }

    public String getColumnName(int columnIndex) {
        return tm.getColumnName(mapColumnIndex(columnIndex));
    }

    public int getRowCount() {
        return rowTypesMap == null ? tm.getRowCount() : rowTypesMap.length;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return tm.isCellEditable(mapRowIndex(rowIndex),
                mapColumnIndex(columnIndex));
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return tm
                .getValueAt(mapRowIndex(rowIndex), mapColumnIndex(columnIndex));
    }

    public void setValueAt(Object obj, int rowIndex, int columnIndex) {
        tm.setValueAt(obj, mapRowIndex(rowIndex), mapColumnIndex(columnIndex));
    }

    public ISmartTableRow getRowAt(int rowIndex) {
        return tm.getRowAt(mapRowIndex(rowIndex));
    }

    public void setRowAt(ISmartTableRow value, int rowIndex) {
        tm.setRowAt(value, mapRowIndex(rowIndex));
    }

    public void pack() {
        tm.pack();
    }

}
