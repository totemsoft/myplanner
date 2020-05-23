/*
 * DependentTableModel.java
 *
 * Created on 3 April 2003, 13:59
 */

package au.com.totemsoft.myplanner.etc;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import au.com.totemsoft.myplanner.api.bean.PersonName;
import au.com.totemsoft.myplanner.table.swing.ISmartTableModel;
import au.com.totemsoft.myplanner.table.swing.ISmartTableRow;
import au.com.totemsoft.util.DateUtils;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class DependentTableModel extends AbstractTableModel
        implements ISmartTableModel {
    // columns FROM FINANCIAL CLASS
    public static final int NAME = 0;
    public static final int SEX = 1;
    public static final int DOB = 2;
    public static final int AGE = 3;
    public static final int SUPPORT_AGE = 4;
    public static final int RELATIONSHIP = 5;

    // TOTAL COLUMNS COUNT
    private static int count = 6;

    private static java.util.Vector columnNames;

    private static java.util.Vector columnClasses;
    static {
        columnNames = new java.util.Vector(count);
        columnNames.add(NAME, "Full Name");
        columnNames.add(SEX, "Sex");
        columnNames.add(DOB, "DOB");
        columnNames.add(AGE, "Age");
        columnNames.add(SUPPORT_AGE, "Support to age");
        columnNames.add(RELATIONSHIP, "Relationship");

        columnClasses = new java.util.Vector(count);
        columnClasses.add(NAME, java.lang.String.class);
        columnClasses.add(SEX, java.lang.String.class);
        columnClasses.add(DOB, Date.class);
        columnClasses.add(AGE, java.lang.Integer.class);
        columnClasses.add(SUPPORT_AGE, java.lang.Integer.class);
        columnClasses.add(RELATIONSHIP, java.lang.String.class);

    }

    protected static au.com.totemsoft.format.Number2 number;
    static {
        number = au.com.totemsoft.format.Number2.createInstance();
        number.setMaximumFractionDigits(0);
        number.setMinimumFractionDigits(0);
    }

    private java.util.Vector data;

    /** Creates a new instance of DependentTableModel */
    public DependentTableModel(java.util.Map dependents) {

        data = new java.util.Vector();
        if (dependents == null || dependents.size() == 0)
            return;

        java.util.Iterator iter = dependents.values().iterator();
        while (iter.hasNext()) {
            Dependent d = (Dependent) iter.next();
            if (d != null)
                data.add(new DependentTableRow(d));

        }

    }

    /***************************************************************************
     * TableModel interface
     **************************************************************************/
    public int getColumnCount() {
        return count;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int columnIndex) {
        // System.out.println( "getColumnName( " + columnIndex + " )=" +
        // columnNames.get( columnIndex ) );
        return (String) columnNames.get(columnIndex);
    }

    public Class getColumnClass(int columnIndex) {
        // System.out.println( "getColumnClass( " + columnIndex + " )=" +
        // columnClasses.get( columnIndex ) );
        return (Class) columnClasses.get(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ISmartTableRow row = getRowAt(rowIndex);
        // System.out.println( "row=" + row );
        return row == null ? null : row.getValueAt(columnIndex);
    }

    /***************************************************************************
     * ISmartTableModel interface
     **************************************************************************/
    public ISmartTableRow getRowAt(int rowIndex) {
        return (ISmartTableRow) data.elementAt(rowIndex);
    }

    public void setRowAt(ISmartTableRow aValue, int rowIndex) {
        // not editable
    }

    public void pack() {

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public class DependentTableRow implements ISmartTableRow {

        private Dependent d;

        protected DependentTableRow(Dependent d) {
            this.d = d;
        }

        public int getRowType() {
            return BODY;
        }

        // clear raw data
        public void clear() { /* do nothing here */
        }

        public boolean isCellEditable(int columnIndex) {
            return false;
        }

        public Object getValueAt(int columnIndex) {

            PersonName pn = d.getName();
            switch (columnIndex) {
                case NAME:
                    return pn.getFullName();
                case SEX:
                    return pn.getSexCode();
                case DOB:
                    return DateUtils.format(pn.getDateOfBirth(), DateFormat.MEDIUM);
                case AGE:
                    return number.toString(pn.getAge());
                case SUPPORT_AGE:
                    return number.toString(d.getSupportToAge());
                case RELATIONSHIP:
                    return d.getRelationshipCode();
                default:
                    return null;
            }

        }

        public void setValueAt(Object aValue, int columnIndex) { /*
                                                                     * do
                                                                     * nothing
                                                                     * here
                                                                     */
        }

    }

    /**
     * Gets all dependents as a String (e.g. Joe aged 10 years, ... and Mary
     * aged 5 years.
     * 
     * @return a string which contains all dependents (name and age)
     */
    public String getDependentsNameAge() {
        String result = "you have no dependents.";

        if (data != null) {
            // do we have any dependents?
            if (this.getRowCount() > 0) {
                if (this.getRowCount() == 1) {
                    result = " you have " + this.getRowCount() + " dependent, ";
                } else {
                    result = " you have " + this.getRowCount()
                            + " dependents, ";
                }

                // yes, the get their name and age
                for (int i = 0; i < data.size(); i++) {
                    DependentTableRow dtr = (DependentTableRow) data
                            .elementAt(i);
                    Dependent dependent = (dtr != null) ? dtr.d : null;

                    if (dependent != null) {
                        // get name
                        PersonName pn = dependent.getName();
                        String first_name = pn.getFirstname();
                        String age = pn.getAge() == null ? "<ADVISER AGE UNKNOWN>"
                                : pn.getAge().toString();

                        if (i == 0) {
                            result += first_name + " aged " + age + " years";
                        }
                        // do we process the last dependen?
                        else if (i > 0 && i == data.size() - 1) {
                            // yes, than we have to use "and"
                            result += " and " + first_name + " aged " + age
                                    + " years";
                        } else {
                            // no, use a "'"
                            result += ", " + first_name + " aged " + age
                                    + " years";
                        }
                    } // end if
                } // end for
                result += ".";
            } // end if ( this.getRowCount > 0 )
        } // end if ( data != null )
        return result;
    }
}
