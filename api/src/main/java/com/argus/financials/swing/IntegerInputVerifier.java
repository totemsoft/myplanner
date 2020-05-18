/*
 * IntegerInputVerifier.java
 *
 * Created on 1 August 2002, 10:50
 */

package com.argus.financials.swing;

/**
 * 
 * @author shibaevv
 * @version
 */

import javax.swing.text.JTextComponent;

public class IntegerInputVerifier extends javax.swing.InputVerifier {

    private static IntegerInputVerifier inputVerifier = new IntegerInputVerifier();

    public static IntegerInputVerifier getInstance() {
        return inputVerifier;
    }

    /** Creates new IntegerInputVerifier */
    public IntegerInputVerifier() {
    }

    public boolean verify(javax.swing.JComponent input) {

        if (input instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) input;

            String s = tc.getText();
            if (s == null || s.trim().equals(""))
                return true;

            try {
                Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                tc.setText(null);
                return tc.isEditable() && false;
            }

            return true;

        }

        return false;

    }
}
