/*
 * JointTableModel.java
 *
 * Created on 2 April 2003, 14:10
 */

package com.argus.financials.swing.table;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class JointTableModel implements ISmartTableModel {

    private HeaderRow header; // first row

    private ISmartTableModel[] tms;

    private FooterRow footer; // last row

    /** Creates a new instance of JointTableModel */
    public JointTableModel(ISmartTableModel[] tms) {
        this(tms, false, false);
    }

    public JointTableModel(ISmartTableModel[] tms, boolean addHeader,
            boolean addFooter) {

        if (tms == null || tms.length < 2)
            throw new java.lang.IllegalArgumentException(
                    "Has to be at least two tables to join!");
        /*
         * Class c = null; for ( int i = 0; i < tms.length; i++ ) { if ( c !=
         * null && !c.equals( tms[i].getClass() ) ) throw new
         * java.lang.IllegalArgumentException( c.getClass().getName() + "!=" +
         * tms[i].getClass().getName() ); c = tms[i].getClass(); }
         */
        if (addHeader)
            header = new HeaderRow();
        this.tms = tms;
        if (addFooter)
            footer = new FooterRow(header);

    }

    public ISmartTableModel getTableModel(int index) {
        return tms[index];
    }

    public void addTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
        for (int i = 0; i < tms.length; i++)
            tms[i].addTableModelListener(tableModelListener);
    }

    public void removeTableModelListener(
            javax.swing.event.TableModelListener tableModelListener) {
        for (int i = 0; i < tms.length; i++)
            tms[i].removeTableModelListener(tableModelListener);
    }

    public Class getColumnClass(int columnIndex) {
        return tms[0].getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return tms[0].getColumnCount();
    }

    public String getColumnName(int columnIndex) {
        return tms[0].getColumnName(columnIndex);
    }

    public int getRowCount() {
        int count = 0;
        for (int i = 0; i < tms.length; i++)
            count += tms[i].getRowCount();
        if (header != null)
            count++;
        if (footer != null)
            count++;
        return count;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        if (rowIndex == 0 && header != null)
            return header.getValueAt(columnIndex);

        if (header != null)
            rowIndex--;

        for (int i = 0; i < tms.length; i++) {
            int rowCount = tms[i].getRowCount();
            if (rowIndex < rowCount)
                return tms[i].getValueAt(rowIndex, columnIndex);
            rowIndex -= rowCount;
        }
        return footer == null ? null : footer.getValueAt(columnIndex);

    }

    public void setValueAt(Object obj, int rowIndex, int columnIndex) {

        if (rowIndex == 0 && header != null)
            return;

        if (header != null)
            rowIndex--;

        for (int i = 0; i < tms.length; i++) {
            int rowCount = tms[i].getRowCount();
            if (rowIndex < rowCount) {
                tms[i].getValueAt(rowIndex, columnIndex);
                return;
            }
            rowIndex -= rowCount;
        }

    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if (rowIndex == 0 && header != null)
            return false;

        if (header != null)
            rowIndex--;

        for (int i = 0; i < tms.length; i++) {
            int rowCount = tms[i].getRowCount();
            if (rowIndex < rowCount)
                return tms[i].isCellEditable(rowIndex, columnIndex);
            rowIndex -= rowCount;
        }
        return false;

    }

    public void pack() {
        for (int i = 0; i < tms.length; i++)
            tms[i].pack();
    }

    public ISmartTableRow getRowAt(int rowIndex) {

        if (rowIndex == 0 && header != null)
            return header;

        if (header != null)
            rowIndex--;

        for (int i = 0; i < tms.length; i++) {
            int rowCount = tms[i].getRowCount();
            if (rowIndex < rowCount)
                return tms[i].getRowAt(rowIndex);
            rowIndex -= rowCount;
        }

        return footer;

    }

    public void setRowAt(ISmartTableRow aValue, int rowIndex) {

        if (rowIndex == 0 && header != null)
            return;

        if (header != null)
            rowIndex--;

        for (int i = 0; i < tms.length; i++) {
            int rowCount = tms[i].getRowCount();
            if (rowIndex < rowCount) {
                tms[i].setRowAt(aValue, rowIndex);
                return;
            }
            rowIndex -= rowCount;
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract class SmartTableRow implements ISmartTableRow {

        protected SmartTableRow() {
        }

        public void clear() { /* do nothing here */
        }

        public boolean isCellEditable(int columnIndex) {
            return false;
        }

        public void setValueAt(Object aValue, int columnIndex) { /*
                                                                     * do
                                                                     * nothing
                                                                     * here
                                                                     */
        }

    }

    public class HeaderRow extends SmartTableRow {

        protected boolean aggregate = true;

        protected HeaderRow() {
        }

        public int getRowType() {
            return HEADER1;
        }

        public Object getValueAt(int columnIndex) {

            if (!aggregate)
                return columnIndex == 0 ? null : null;

            double d = 0.;
            for (int i = 0; i < tms.length; i++) {
                ISmartTableModel tm = tms[i];
                if (tm.getRowCount() == 0)
                    continue;

                // get last row in table (has to be total value)
                ISmartTableRow r = tm.getRowAt(tm.getRowCount() - 1);

                Object obj = r.getValueAt(columnIndex);
                if (obj == null || obj instanceof java.lang.String)
                    return null;

                if (obj instanceof java.lang.Number)
                    d += ((java.lang.Number) obj).doubleValue();
                else if (obj instanceof com.argus.math.Numeric)
                    d += ((com.argus.math.Numeric) obj).doubleValue();
                else {
                    System.err
                            .println("JointTableModel::HeaderRow::getValueAt( "
                                    + columnIndex + " ), Unhandled class: "
                                    + obj.getClass());
                    return null;
                }

            }
            return new java.math.BigDecimal(d);

        }

    }

    public class FooterRow extends HeaderRow {

        private HeaderRow header;

        protected FooterRow(HeaderRow header) {
            this.header = header;
            if (this.header != null)
                this.header.aggregate = false;
        }

        public int getRowType() {
            return FOOTER1;
        }

        public Object getValueAt(int columnIndex) {
            return columnIndex == 0 ? "Total" : super.getValueAt(columnIndex);
        }

    }

}
