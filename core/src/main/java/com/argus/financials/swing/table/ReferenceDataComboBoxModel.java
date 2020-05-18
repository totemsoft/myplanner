/*
 * ReferenceDataComboBoxModel.java
 *
 * Created on 10 March 2003, 10:35
 */

package com.argus.financials.swing.table;

/**
 * 
 * @author shibaevv
 * @version
 */
public class ReferenceDataComboBoxModel extends javax.swing.AbstractListModel
        implements javax.swing.ComboBoxModel {

    private UpdateableTableModel data;

    private int selected = -1;

    /** Creates new ReferenceDataComboBoxModel */
    public ReferenceDataComboBoxModel(UpdateableTableModel data) {
        this.data = data;
    }

    public java.lang.Object getSelectedItem() {
        if (selected >= 0) {
            return data.getRowAt(selected);
        }
        return null;
    }

    public void setSelectedItem(java.lang.Object item) {

        if (item instanceof UpdateableTableRow)
            this.selected = data.indexOf((UpdateableTableRow) item);
        else if (item instanceof Integer)
            this.selected = data.getIdentityIndex(item);
        else if (item instanceof String) {
            String sitem = (String) item;
            sitem = sitem == null ? "" : sitem.trim();
            this.selected = data.getIdentityIndex(sitem);
        } else if (item == null)
            this.selected = -1;
        // this.fireContentsChanged(this,0,getSize() );
        this.fireIntervalAdded(this, 0, getSize());
    }

    public int getSize() {
        return data.getRowCount();
    }

    public java.lang.Object getElementAt(int index) {
        if (index <= getSize()) {
            return data.getRowAt(index);
        }
        return null;
    }

}
