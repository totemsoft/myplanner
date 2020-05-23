/*
 * FinancialTableModel.java
 *
 * Created on 3 December 2002, 10:42
 */

package au.com.totemsoft.myplanner.table.model;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import au.com.totemsoft.math.Numeric;
import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.table.swing.ISmartTableRow;
import au.com.totemsoft.myplanner.table.swing.SmartTableModel;
import au.com.totemsoft.util.ReferenceCode;

public abstract class FinancialTableModel extends SmartTableModel implements
        FinancialClassID {

    protected static au.com.totemsoft.math.Money money;

    protected static au.com.totemsoft.math.Percent percent;

    protected static au.com.totemsoft.math.Decimal decimal;
    static {
        money = new au.com.totemsoft.math.Money();
        money.setMaximumFractionDigits(0);
        money.setMinimumFractionDigits(0);

        percent = new au.com.totemsoft.math.Percent();
        percent.setMaximumFractionDigits(0);
        percent.setMinimumFractionDigits(0);

        decimal = new au.com.totemsoft.math.Decimal();
        decimal.setMaximumFractionDigits(0);
        decimal.setMinimumFractionDigits(0);
    }

    /** Creates a new instance of FinancialTableModel */
    protected FinancialTableModel() {
    }

    protected FinancialTableModel(Vector data, Vector columnNames,
            Vector columnClasses) {
        super(data, columnNames, columnClasses);
    }

    protected int getPrimaryColumnIndex() {
        return 0;
    }

    public double getInflation() {
        return 0.;
    }

    protected Numeric getNumeric(int columnIndex) {
        return decimal;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract class FinancialRow extends AbstractSmartTableRow {

        private Financial f;

        /** Creates a new instance of FinancialRow */
        public FinancialRow(Financial f) {
            super(BODY);

            this.f = f;
        }

        public String toString() {
            return f == null ? null : f.toString();
        }

        protected Financial getFinancial() {
            return f;
        }

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return toString();
            return null;
        }
        /*
         * public void setValueAt(Object aValue, int columnIndex) {
         * super.setValueAt(aValue, columnIndex); // set modified flag
         * 
         * if ( columnIndex == getPrimaryColumnIndex() && aValue instanceof
         * Financial ) f = (Financial) aValue; }
         */
        // protected abstract double getTotal();
    }

    public class GroupHeaderRow extends AbstractSmartTableRow {

        private ReferenceCode rc;

        /** Creates a new instance of FinancialRow */
        public GroupHeaderRow(ReferenceCode rc) {
            this(rc, HEADER1);
        }

        protected GroupHeaderRow(ReferenceCode rc, int type) {
            super(type);

            this.rc = rc;
        }

        public String toString() {
            return rc.toString();
        }

        public ReferenceCode getReferenceCode() {
            return rc;
        }

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return toString();
            return null;
        }

        public void setValueAt(Object aValue, int columnIndex) {
            super.setValueAt(aValue, columnIndex); // set modified flag

            if (columnIndex == getPrimaryColumnIndex()
                    && aValue instanceof ReferenceCode)
                rc = (ReferenceCode) aValue;
        }

    }

    public class GroupFooterRow extends GroupHeaderRow {

        protected GroupHeaderRow header;

        /** Creates a new instance of GroupFooterRow */
        public GroupFooterRow(ReferenceCode rc) {
            this(rc, null);
        }

        public GroupFooterRow(ReferenceCode rc, GroupHeaderRow header) {
            super(rc, FOOTER1);
            this.header = header;
        }

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return super.getValueAt(columnIndex);

            double value = getGroupTotal(columnIndex);
            if (value == Financial.HOLE)
                return null;

            return getNumeric(columnIndex).setValue(value).round();

        }

        protected double getGroupTotal(int columnIndex) {

            java.util.List list = getGroup();
            if (list == null)
                return Financial.HOLE;

            double total = 0.;
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                ISmartTableRow row = (ISmartTableRow) iter.next();
                if (row.getRowType() != BODY)
                    continue;

                Object value = row.getValueAt(columnIndex);
                if (!(value instanceof Numeric))
                    return Financial.HOLE;

                total += ((Numeric) value).doubleValue();

            }

            return total;

        }

        protected double[] getGroupTotals() {
            return null;
        }

        // do not include header/footer
        protected List getGroup() {
            Vector data = getData();
            int fromIndex = header == null ? 0 : data.indexOf(header) + 1; // inclusive
            int toIndex = data == null ? 0 : data.indexOf(this); // exclusive
            return (fromIndex > 0 && fromIndex >= toIndex) ? null : data
                    .subList(fromIndex, toIndex);
        }

    }

}
