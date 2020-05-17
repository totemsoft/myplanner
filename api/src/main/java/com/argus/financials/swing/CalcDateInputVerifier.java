/*
 * CalcDateInputVerifier.java
 *
 * Created on 27 November 2002, 12:35
 */

package com.argus.financials.swing;

/**
 * 
 * @author shibaevv
 */

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import com.argus.financials.api.ETPConstants;
import com.argus.util.DateTimeUtils;

public class CalcDateInputVerifier extends javax.swing.InputVerifier {

    private static CalcDateInputVerifier inputVerifier = new CalcDateInputVerifier();

    public static CalcDateInputVerifier getInstance() {
        return inputVerifier;
    }

    /** Creates a new instance of CalcDateInputVerifier */
    public CalcDateInputVerifier() {
    }

    public boolean verify(JComponent input) {

        if (input instanceof JTextComponent) {
            input.setToolTipText(DateTimeUtils.DEFAULT_INPUT);
            if (!DateTimeUtils.isValidDate(((JTextComponent) input).getText()))
                return false;

            JTextComponent tc = (JTextComponent) input;

            String s = tc.getText();
            if (s == null || s.trim().equals(""))
                return true;

            if (ETPConstants.TAX_1983_COMMENCE_DATE.after(DateTimeUtils
                    .getDate(s))) {
                tc.setText(null);
                JOptionPane.showMessageDialog(input,
                        "Date could never be before 1/7/1983", "Error",
                        JOptionPane.ERROR_MESSAGE);

                return tc.isEditable() && false;
            }

        }

        return true;
    }

}
