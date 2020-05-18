/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.format;

import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTextField;

/**
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1 $
 */

public class DecimalField extends JTextField {
    private NumberFormat format;

    public DecimalField(double value, int columns, NumberFormat f) {
        super(columns);
        setDocument(new FormattedDocument(f));
        format = f;
        setValue(value);
    }

    public double getValue() {
        double retVal = 0.0;

        try {            
            retVal = format.parse(getText()).doubleValue();
        } catch (ParseException e) {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            Toolkit.getDefaultToolkit().beep();
            System.err.println("getValue: could not parse: " + getText());
        }
        return retVal;
    }

    public void setValue(double value) {
        setText(format.format(value));
    }
}
