/*
 * CurrencyRenderer.java
 *
 * Created on 30 May 2002, 14:50
 */

package com.argus.financials.swing.table;

import java.text.NumberFormat;

/**
 * A Specialised cell renderer that formats a number to currency format.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public class CurrencyRenderer extends
        javax.swing.table.DefaultTableCellRenderer {

    /** Creates new CurrencyRenderer */
    public CurrencyRenderer() {
        super();
        this.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    }

    /**
     * Reformat any object that is a subclass of Number to the default local
     * currency format.
     */
    protected void setValue(java.lang.Object value) {
        if ((value != null) && (value instanceof Number)) {
            Number numberValue = (Number) value;
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            value = formatter.format(numberValue.doubleValue());
        }
        super.setValue(value);
    }

}
