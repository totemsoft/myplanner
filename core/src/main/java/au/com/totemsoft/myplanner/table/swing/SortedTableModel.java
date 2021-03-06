/*
 * TableSorter.java
 *
 * Created on 6 February 2002, 16:07
 */

package au.com.totemsoft.myplanner.table.swing;

/**
 * NOTE: current implementation has very limited support for real
 * DefaultTableModel. Full implementation is required to satisfy the broad
 * requirements.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 * 
 * A sorter for TableModels. The sorter has a model (conforming to TableModel)
 * and itself implements TableModel. TableSorter does not store or copy the data
 * in the TableModel, instead it maintains an array of integers which it keeps
 * the same size as the number of rows in its model. When the model changes it
 * notifies the sorter that something has changed eg. "rowsAdded" so that its
 * internal array of integers can be reallocated. As requests are made of the
 * sorter (like getValueAt(row, col) it redirects them to its model via the
 * mapping array. That way the TableSorter appears to hold another copy of the
 * table with the rows in a different order. The sorting algorthm used is stable
 * which means that it does not move around rows when its comparison function
 * returns 0 to denote that they are equivalent.
 * 
 * @version 1.5 12/17/97
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class SortedTableModel implements TableModelListener, TableModel

{

    // DefaultTableModel

    private int[] indexes = new int[0];

    private Vector sortingColumns = new Vector();

    private boolean ascending = true;

    private int compares;

    // private TableModel tableModel;

    private DefaultTableModel tableModel;

    public SortedTableModel(DefaultTableModel tableModel) {

        setModel(tableModel);

        this.tableModel.addTableModelListener(this);
    }

    public void setModel(DefaultTableModel model) {
        this.tableModel = model;
        reallocateIndexes();
    }

    public TableModel getModel() {
        return this.tableModel;
    }

    /**
     * compare functions
     */
    private int compareAsNull(Object o1, Object o2) {
        if (o1 == null && o2 == null)
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;
        return Integer.MAX_VALUE; // not good !!!
    }

    private int compare(Boolean b1, Boolean b2) {
        if (b1 == null && b2 == null)
            return 0;
        if (b1 == null)
            return -1;
        if (b2 == null)
            return 1;

        if (b1.booleanValue() == b2.booleanValue())
            return 0;
        if (b1.booleanValue())
            return 1; // Define false < true
        return -1;
    }

    private int compare(double d1, double d2) {
        if (d1 < d2)
            return -1;
        if (d1 > d2)
            return 1;
        return 0;
    }

    private int compare(Number n1, Number n2) {
        if (n1 == null && n2 == null)
            return 0;
        if (n1 == null)
            return -1;
        if (n2 == null)
            return 1;

        return compare(n1.doubleValue(), n2.doubleValue());
    }

    private int compare(Date d1, Date d2) {
        if (d1 == null && d2 == null)
            return 0;
        if (d1 == null)
            return -1;
        if (d2 == null)
            return 1;

        int result = d1.compareTo(d2);
        if (result < 0)
            return -1;
        if (result > 0)
            return 1;
        return 0;
    }

    private int compare(String s1, String s2) {
        if (s1 == null && s2 == null)
            return 0;
        if (s1 == null)
            return -1;
        if (s2 == null)
            return 1;

        int result = s1.compareTo(s2);
        if (result < 0)
            return -1;
        if (result > 0)
            return 1;
        return 0;
    }

    public int compareRowsByColumn(int row1, int row2, int column) {
        Class type = this.getColumnClass(column);

        // Check for nulls.
        Object o1 = tableModel.getValueAt(row1, column);
        Object o2 = tableModel.getValueAt(row2, column);

        // If both values are null, return 0.
        if (o1 == null && o2 == null)
            return 0;
        if (o1 == null)
            return -1; // Define null less than everything.
        if (o2 == null)
            return 1;

        /*
         * We copy all returned values from the getValue call in case an
         * optimised model is reusing one object to return many values. The
         * Number subclasses in the JDK are immutable and so will not be used in
         * this way but other subclasses of Number might want to do this to save
         * space and avoid unnecessary heap allocation.
         */

        if (type.getSuperclass() == java.lang.Number.class) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            return compare(n1, n2);
        } else if (type == java.util.Date.class) {
            Date d1 = (Date) o1;
            Date d2 = (Date) o2;
            return compare(d1, d2);
        } else if (type == String.class) {
            String s1 = o1.toString();
            String s2 = o2.toString();
            return compare(s1, s2);
        } else if (type == Boolean.class) {
            Boolean b1 = (Boolean) o1;
            Boolean b2 = (Boolean) o2;
            return compare(b1, b2);
        } else if (type == au.com.totemsoft.format.Number2.class) {
            au.com.totemsoft.format.Number2 number2 = au.com.totemsoft.format.Number2
                    .getNumberInstance();

            Double d1 = number2.getDoubleValue((String) o1);
            Double d2 = number2.getDoubleValue((String) o2);
            return compare(d1, d2);
        } else if (type == au.com.totemsoft.format.Currency.class) {
            au.com.totemsoft.format.Currency currency = au.com.totemsoft.format.Currency
                    .getCurrencyInstance();

            Double d1 = currency.getDoubleValue((String) o1);
            Double d2 = currency.getDoubleValue((String) o2);
            return compare(d1, d2);
        } else if (type == au.com.totemsoft.format.Percent.class) {
            au.com.totemsoft.format.Percent percent = au.com.totemsoft.format.Percent
                    .getPercentInstance();

            Double d1 = percent.getDoubleValue((String) o1);
            Double d2 = percent.getDoubleValue((String) o2);
            return compare(d1, d2);
        } else {
            String s1 = o1.toString();
            String s2 = o2.toString();
            return compare(s1, s2);
        }
    }

    public int compare(int row1, int row2) {
        compares++;
        for (int level = 0; level < sortingColumns.size(); level++) {
            Integer column = (Integer) sortingColumns.elementAt(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            if (result != 0) {
                return ascending ? result : -result;
            }
        }
        return 0;
    }

    public void reallocateIndexes() {
        int rowCount = this.getRowCount();

        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];

        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }

    public void tableChanged(TableModelEvent e) {
        reallocateIndexes();
        // fireTableChanged(e); That causes loop exception
    }

    public void checkModel() {
        if (indexes.length != this.getRowCount()) {
            System.err.println("Sorter not informed of a change in model.");
        }
    }

    public void sort(Object sender) {
        checkModel();

        compares = 0;
        // n2sort();
        // qsort(0, indexes.length-1);
        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
        // System.out.println("Compares: "+compares);
    }

    public void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i + 1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }

    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    public void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        /*
         * This is an optional short-cut; at each recursive call, check to see
         * if the elements in this subset are already ordered. If so, no further
         * comparisons are needed; the sub-array can just be copied. The array
         * must be copied rather than assigned otherwise sister calls in the
         * recursion might get out of sinc. When the number of elements is three
         * they are partitioned so that the first set, [low, mid), has one
         * element and and the second, [mid, high), has two. We skip the
         * optimisation when the number of elements is three or less as the
         * first compare in the normal merge will produce the same sequence of
         * steps. This optimisation seems to be worthwhile for partially ordered
         * lists but some analysis is needed to find out how the performance
         * drops to Nlog(N) as the initial order diminishes - it may drop very
         * quickly.
         */

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge.

        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }

    public void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "indexes".

    public void removeRow(int aRow) {
        checkModel();
        tableModel.removeRow(indexes[aRow]);
    }

    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        return tableModel.getValueAt(indexes[aRow], aColumn);
    }

    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        tableModel.setValueAt(aValue, indexes[aRow], aColumn);
    }

    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }

    public void sortByColumn(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(column));
        sort(this);
        // tableModel.fireTableDataChanged();
        // fireTableChanged(new TableModelEvent(this));
        // tableChanged(new TableModelEvent(this));
    }

    // There is no-where else to put this.
    // Add a mouse listener to the Table to trigger a table sort
    // when a column heading is clicked in the JTable.
    public void addMouseListenerToHeaderInTable(JTable table) {
        final SortedTableModel sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    // System.out.println("Sorting ...");
                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
                    boolean ascending = (shiftPressed == 0);
                    sorter.sortByColumn(column, ascending);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }

    public void sortTable(JTable table) {
        final SortedTableModel sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        TableColumnModel columnModel = tableView.getColumnModel();
        boolean ascending = true;
        sorter.sortByColumn(1, ascending);

    }

    public java.lang.String getColumnName(int param) {
        return tableModel.getColumnName(param);
    }

    public int getRowCount() {
        return tableModel.getRowCount();
    }

    public boolean isCellEditable(int param, int param1) {
        return tableModel.isCellEditable(param, param1);
    }

    public java.lang.Class getColumnClass(int param) {
        return tableModel.getColumnClass(param);
    }

    public void addTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
        tableModel.addTableModelListener(tableModelListener);
    }

    public int getColumnCount() {
        return tableModel.getColumnCount();
    }

    public void removeTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
        tableModel.removeTableModelListener(tableModelListener);
    }

}