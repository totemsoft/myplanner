/*
 * FinancialServiceCode.java
 *
 * Created on 16 May 2003, 12:49
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import com.argus.financials.api.code.CodeComparator;
import com.argus.financials.service.UtilityService;
import com.argus.util.ReferenceCode;

public class FinancialServiceCode extends BaseCode {

    public static final int CODE = 0;

    public static final int DESC = 1;

    public static final int DATE = 2;

    public static final int DELETED = 3;

    private static String TABLE = "FinancialService";

    private static String[] columnName = new String[] { "FinancialServiceCode",
            "FinancialServiceDesc", "DateCreated", "LogicallyDeleted" };

    private static Class[] columnClass = new Class[] { String.class,
            String.class, String.class, Boolean.class };

    private static Collection allCodes;

    private static Collection codes;

    public String toString() {
        return "Financial Services";
    }

    public Collection getCodes() {
        if (codes == null) {
            initCodes();
        }
        return codes;
    }

    private void initCodes() {

        codes = new TreeSet(new CodeComparator());
        allCodes = new ArrayList();

        codes.add(CODE_NONE);

        try {
            Map map = utilityService.getCodes(
                    TABLE,
                    columnName[CODE] + ", " + columnName[DESC] + ", "
                            + columnName[DATE] + ", " + columnName[DELETED],
                    columnName[CODE] // fieldKey
                    );
            if (map == null)
                return;

            int i = 0;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String financialServiceCode = (String) entry.getKey();
                Collection values = (Collection) entry.getValue();

                allCodes.add(values);
                if ("Y".equalsIgnoreCase((String) values.toArray()[DELETED]))
                    continue;

                // Integer, FinancialServiceCode, FinancialServiceDesc
                ReferenceCode rc = new ReferenceCode(new Integer(--i),
                        financialServiceCode.trim(),
                        ((String) values.toArray()[DESC]).trim());
                rc.setObject(values);
                codes.add(rc);
            }

        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public void save() throws com.argus.financials.api.ServiceException {

        if (tm == null)
            return;

        // save to db
        UtilityService utility = utilityService;
        for (int r = 0; r < tm.getRowCount(); r++) {

            if (tm.isCellEditable(r, CODE)) {
                java.util.Map addData = new java.util.HashMap();

                addData.put(columnName[CODE], tm.getValueAt(r, CODE));
                addData.put(columnName[DESC], tm.getValueAt(r, DESC));
                addData.put(columnName[DELETED], ((Boolean) tm.getValueAt(r,
                        DELETED)).booleanValue() ? "Y" : null);

                utility.addCode(TABLE, addData);

            } else {
                java.util.Map updateData = new java.util.HashMap();
                updateData.put(columnName[DESC], tm.getValueAt(r, DESC));
                updateData.put(columnName[DELETED], ((Boolean) tm.getValueAt(r,
                        DELETED)).booleanValue() ? "Y" : null);

                java.util.Map whereData = new java.util.HashMap();
                whereData.put(columnName[CODE], tm.getValueAt(r, CODE));

                utility.updateCode(TABLE, updateData, whereData);

            }

        }

        // re-init codes
        codes = null;
        tm = null;
        initCodes();

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private static javax.swing.table.DefaultTableModel tm;

    public javax.swing.table.TableModel asTableModel() {

        if (tm == null) {
            Object[][] data = new Object[allCodes.size()][4];

            int i = 0;
            java.util.Iterator iter = allCodes.iterator();
            while (iter.hasNext()) {
                Object[] allValues = ((Collection) iter.next()).toArray();

                data[i][CODE] = allValues[CODE];
                data[i][DESC] = allValues[DESC];
                data[i][DATE] = allValues[DATE];
                data[i][DELETED] = "Y"
                        .equalsIgnoreCase((String) allValues[DELETED]) ? Boolean.TRUE
                        : Boolean.FALSE;

                i++;
            }

            tm = new javax.swing.table.DefaultTableModel(data, columnName) {

                public Class getColumnClass(int columnIndex) {
                    return columnClass[columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == DESC
                            || columnIndex == DELETED
                            || (columnIndex == CODE && getValueAt(rowIndex,
                                    DATE) == null);
                }

                public void addRow(Vector rowData) {
                    if (rowData == null) {
                        rowData = new java.util.Vector();
                        rowData.add(CODE, null);
                        rowData.add(DESC, null);
                        rowData.add(DATE, null);
                        rowData.add(DELETED, Boolean.TRUE);
                    }
                    super.addRow(rowData);
                }

            };

        }

        return tm;

    }

}
