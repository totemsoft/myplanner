/*
 * ControlItemsComboBoxModel.java
 *
 * Created on 3 June 2003, 17:37
 */

package com.argus.financials.swing.table;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 * 
 * @author shibaevv
 * @version
 */

public class ControlItemsComboBoxModel extends javax.swing.AbstractListModel
        implements javax.swing.ComboBoxModel, javax.swing.ListCellRenderer {
    private Vector items;

    private int selected = -1;

    /** Creates new ControlItemsComboBoxModel */
    public ControlItemsComboBoxModel(JButton[] buttons) {
        items = new Vector();
        if (buttons == null || buttons.length == 0) {
        } else {

            for (int i = 0; i < buttons.length; i++) {
                items.add(buttons[i]);
            }
        }
    }

    public ControlItemsComboBoxModel(JComponent[] buttons) {
        items = new Vector();
        if (buttons == null || buttons.length == 0) {
        } else {

            for (int i = 0; i < buttons.length; i++) {
                items.add(buttons[i]);
            }
        }
    }

    public java.lang.Object getSelectedItem() {
        if (selected >= 0) {
            return items.elementAt(selected);
        }
        return null;
    }

    public void setSelectedItem(java.lang.Object obj) {
        this.selected = items.indexOf(obj);
        this.fireIntervalAdded(this, 0, getSize());
    }

    public int getSize() {
        return items.size();
    }

    public java.lang.Object getElementAt(int index) {
        if (index >= items.size())
            return null;
        return items.elementAt(index);
    }

    public java.awt.Component getListCellRendererComponent(
            javax.swing.JList jList, java.lang.Object value, int index,
            boolean isSelected, boolean hasFocus) {

        JComponent menuItem = null;

        if (value == null)
            value = new JMenuItem("None");
        if (value instanceof JMenuItem)
            menuItem = (JMenuItem) value;
        else if (value instanceof JSeparator)
            menuItem = (JSeparator) value;
        else
            menuItem = new JMenuItem("None");

        if (value == null || value.toString().trim().equals("")) {
            return menuItem;
        }

        if (isSelected) {
            menuItem.setBackground(jList.getSelectionBackground());
            menuItem.setForeground(jList.getSelectionForeground());
        } else {
            menuItem.setBackground(jList.getBackground());
            menuItem.setForeground(jList.getForeground());
        }
        // menuItem.setEnabled(jList.isEnabled());
        // menuItem.setFont(jList.getFont());
        // menuItem.setHorizontalAlignment(menuItem.LEFT);
        // this.fireContentsChanged(this,index,index);

        return menuItem;

    }

}
