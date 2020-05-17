/*
 * IncomeTableModel.java
 *
 * Created on 6 August 2003, 21:36
 */

package com.argus.financials.model;

import com.argus.util.RateUtils;

public class IncomeTableModel extends javax.swing.table.DefaultTableModel {
    
    public static final int DATE    = 0;
    public static final int PAID    = 1;
    public static final int PERIOD  = 2;
    public static final int RATE    = 3;
    public static final int BORROW  = 4;
    public static final int PAY     = 5;
    
    private static final String [] columnNames = new String [] {
        "Date",
        "Paid", // dividends or coupon payments
        "Period",
        "Rate",
        "To borrow",
        "To pay", // the amount owing at the end of the Period
    };

    private static final Class [] columnClasses = new Class [] {
        java.util.Date.class,
        java.lang.Double.class,
        java.lang.Double.class,
        java.lang.Double.class,
        java.lang.Double.class,
        java.lang.Double.class,
    };

    
    /**
     *
     *
    public class RowData {
        java.util.Date  date;
        double          period;
        double          paid;
        double          rate;
        double          borrow;
        double          pay;
    }
    */
    
    /** Creates a new instance of IncomeTableModel */
    public IncomeTableModel() {
        super( new Object [0][0], columnNames );
    }
    
    
    public void addRow() {
        addRow( new Object [ getColumnCount() ] );
    }

    
    /**
     *  javax.swing.table.TableModel interface
     */
    public Class getColumnClass( int columnIndex ) {
        return columnClasses[ columnIndex];
    }

    public Object getValueAt( int rowIndex, int columnIndex) {
        
        Object value = super.getValueAt( rowIndex, columnIndex );
        // do formatting
        
        return value;
        
    }

    public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
System.out.println( "" + aValue + " at rowIndex=" + rowIndex + ", columnIndex=" + columnIndex );

        Object value = super.getValueAt( rowIndex, RATE );
        double rate = value == null ? 0. : ( (Double) value ).doubleValue();
                
        value = super.getValueAt( rowIndex, PERIOD );
        double period = value == null ? 0. : ( (Double) value ).doubleValue();

        switch ( columnIndex ) {
            case DATE:  
            case PAID:  
                break;
                
            case PERIOD:  
            case RATE:  
                setValueAtPAY( rowIndex, rate, period );
                break;
                
            case BORROW: 
                setValueAtPAY( rowIndex, rate, period );
                break;
                
            case PAY:  
                setValueAtBORROW( rowIndex, rate, period );
                break;
                
            default: ;
        }
        
        super.setValueAt( aValue, rowIndex, columnIndex );
        //fireTableRowsUpdated( rowIndex, rowIndex );
        
    }
    
    private void setValueAtPAY( int rowIndex, double rate, double period ) {
        
        Number value = (Number) super.getValueAt( rowIndex, BORROW );
        super.setValueAt( new Double( RateUtils.getCompoundedAmount( value.doubleValue(), rate, period ) ), rowIndex, PAY );
        
    }
    
    private void setValueAtBORROW( int rowIndex, double rate, double period ) {
        
        Number value = (Number) super.getValueAt( rowIndex, PAY );
        super.setValueAt( new Double( RateUtils.getCompoundedAmount( value.doubleValue(), -rate, period ) ), rowIndex, BORROW );
        
    }
    
}
