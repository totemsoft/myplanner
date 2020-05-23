/*
 * NonDeemedAssetTableModel.java
 *
 * Created on April 30, 2003, 11:34 AM
 */

package au.com.totemsoft.myplanner.projection.dss;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import au.com.totemsoft.myplanner.code.FundType;
import au.com.totemsoft.util.ReferenceCode;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class NonDeemedAssetTableModel extends DefaultTableModel {

    public static final int NAME = 0;

    public static final int TYPE = 1;

    public static final int OWNER = 2;

    public static final int ACTUAL_ASSET = 3;

    public static final int TEST_ASSET = 4;

    public static final int ACTUAL_INCOME = 5;

    public static final int TEST_INCOME = 6;

    private static final Vector columnNames = new Vector();
    static {
        columnNames.add("Name");
        columnNames.add("Type");
        columnNames.add("Owner");
        columnNames.add("Actual Asset");
        columnNames.add("Test Asset");
        columnNames.add("Actual Income");
        columnNames.add("Test Income");
    };

    private static final Class[] types = new Class[] { java.lang.String.class,
            java.lang.Object.class, java.lang.Integer.class,
            java.lang.Double.class, java.lang.Double.class,
            java.lang.Double.class, java.lang.Double.class };

    private static final boolean[] canEdit = new boolean[] { true, true, true,
            true, false, true, true };

    /** Creates a new instance of NonDeemedAssetTableModel */
    public NonDeemedAssetTableModel() {
        super(new Vector(), columnNames);
    }

    public void addRow() {
        addRow(new DSSElement());
    }

    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // if ( dataVector != null && dataVector.size() - 1 == rowIndex)
        // return false;
        return canEdit[columnIndex];
    }

    public void setValueAt(Object value, int row, int col) {

        if (ACTUAL_ASSET == col) {
            if (FundType.rcANNUITY_LONG.equals((ReferenceCode) getValueAt(row,
                    TYPE))) {
                setValueAt(new Double(0.), row, col + 1);
            } else {
                try {
                    setValueAt(value, row, col + 1);
                    // fireTableCellUpdated(row, col+1);
                } catch (NumberFormatException e) {
                    System.err.println("The \"" + getColumnName(col)
                            + "\" column accepts only string values.");

                    javax.swing.JOptionPane.showMessageDialog(null,
                            "The value is wrong type");
                }
            }
        }
        try {
            super.setValueAt(value, row, col);
        } catch (NumberFormatException e) {
            System.err.println("The \"" + getColumnName(col)
                    + "\" column accepts only string values.");

            javax.swing.JOptionPane.showMessageDialog(null,
                    "The value is wrong type");
        }
        // update total row
        // updateTotalRow();
        // fireTableRowsUpdated( getRowCount() - 1 , getRowCount() - 1 );
    }

    /*
     * private void updateTotalRow() { double totalAA = calculateColumnTotal(
     * ACTUAL_ASSET ); double totalTA = calculateColumnTotal( TEST_ASSET );
     * double totalAI = calculateColumnTotal( ACTUAL_INCOME ); double totalTI =
     * calculateColumnTotal( TEST_INCOME ); /* if ( getRowCount() > 0 ) {
     * NonDeemedAssetsTableRow row = (NonDeemedAssetsTableRow)data.elementAt(
     * data.size()-1 ); row.actualAsset = new Double(totalAA); row.testAsset =
     * new Double(totalTA); row.actualIncome = new Double(totalAI);
     * row.testIncome = new Double(totalTI); }
     *  }
     * 
     * private double calculateColumnTotal( int col ) { double sum = 0.0; if (
     * col > 0 && col < getColumnCount() ) for( int row = 0; row < getRowCount() -
     * 1; row++ ) if( getValueAt(row,col) != null ) sum += ((Double)getValueAt(
     * row, col )).doubleValue(); return sum; }
     */
}
