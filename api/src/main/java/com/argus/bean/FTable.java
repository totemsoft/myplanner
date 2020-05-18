/*
 * FTable.java
 *
 * Created on 5 May 2003, 12:00
 */

package com.argus.bean;

import java.util.Vector;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class FTable extends javax.swing.JTable implements
        com.argus.bean.ValueSetter, javax.swing.event.TableModelListener {

    private String componentName;

    private transient Vector dataChangedListeners;

    /** Creates a new instance of FTable */
    public FTable() {
    }

    public FTable(javax.swing.table.TableModel tm) {
        super(tm);
    }

    public synchronized void registerComponent(String componentName,
            BasePanel parent) {
        // System.out.println( "\nFTable::registerComponent( " + componentName +
        // ", ... )" );
        // getDocument().putProperty( PropertyName, componentName );
        setName(componentName);
        parent.registerComponent(componentName, this);
        this.componentName = componentName;
    }

    public synchronized void removeFormDataChangedListener(
            FormDataChangedListener l) {
        if (dataChangedListeners != null && dataChangedListeners.contains(l)) {
            Vector v = (Vector) dataChangedListeners.clone();
            v.removeElement(l);
            dataChangedListeners = v;
        }
    }

    public synchronized void addFormDataChangedListener(
            FormDataChangedListener l) {
        Vector v = dataChangedListeners == null ? new Vector(2)
                : (Vector) dataChangedListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            dataChangedListeners = v;
        }
    }

    protected void fireFormDataChanged(FormDataChangedEvent e) {
        if (dataChangedListeners != null) {
            Vector listeners = dataChangedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((FormDataChangedListener) listeners.elementAt(i))
                        .formDataChanged(e);
            }
        }
    }

    /***************************************************************************
     * com.argus.financialsjavabeans.ValueSetter interface
     **************************************************************************/
    public boolean isKeyPressed() {
        return false;
    }

    public void setValue(Object value) {
        // System.out.println( value );
        // System.out.println( "\nFTable::setValue( " + value + " )" );
        if (!(value instanceof Vector))
            return;

        if (getModel() instanceof javax.swing.table.DefaultTableModel) {
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) getModel();

            Object[] data = ((Vector) value).toArray();
            /*
             * if ( data.length == 0 ) { System.out.println(
             * "\nFTable::setValue() data.length == 0" ); return; }
             */
            Vector v = dtm.getDataVector();
            v.clear();

            for (int i = 0; i < data.length; i++) {
                // System.out.println( "FTable::setValue::addRow( " + data[i] +
                // " )" );
                v.add(data[i]);
            }

            // this.validate();
            // this.repaint();
            this.invalidate();

        }

    }

    public java.util.Vector getColumnNames() {
        javax.swing.table.TableModel tm = getModel();
        Vector columns = new Vector(tm.getColumnCount());
        for (int i = 0; i < tm.getColumnCount(); i++)
            columns.add(tm.getColumnName(i));
        return columns;
    }

    public void tableChanged(javax.swing.event.TableModelEvent e) {
        super.tableChanged(e);
        String title = getName();

        boolean iAmPressed = true;

        if (e.getSource() instanceof javax.swing.table.DefaultTableModel) {

            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) e
                    .getSource();
            Vector data = dtm.getDataVector();
            fireFormDataChanged(new FormDataChangedEvent(new FormDataChanged(
                    title, data, iAmPressed, this)));

        }

    }

}
