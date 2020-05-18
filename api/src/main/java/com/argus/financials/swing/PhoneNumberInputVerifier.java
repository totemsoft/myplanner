/*
 * PhoneNumberInputVerifier.java
 *
 * Created on 8 August 2002, 15:01
 */

package com.argus.financials.swing;

/**
 * 
 * @author shibaevv
 * @version
 */

import javax.swing.text.JTextComponent;

import com.argus.format.Number2;

public class PhoneNumberInputVerifier extends javax.swing.InputVerifier {

    private static PhoneNumberInputVerifier inputVerifier = new PhoneNumberInputVerifier();

    public static PhoneNumberInputVerifier getInstance() {
        return inputVerifier;
    }

    /** Creates new PhoneNumberInputVerifier */
    public PhoneNumberInputVerifier() {
    }

    public boolean verify(javax.swing.JComponent input) {

        if (input instanceof JTextComponent) {
            input.setToolTipText(" Numbers, () and - only!! ");
            JTextComponent tc = (JTextComponent) input;

            String s = tc.getText();

            if (s == null || s.length() == 0)
                return true;
            int trueNum = 0;
            for (int i = 0; i < s.length(); i++) {
                String c = String.valueOf(s.charAt(i));

                if (c.equals(" ") || c.equals("") || c.equals(",")
                        || c.equals("-") || c.equals("(") || c.equals(")")
                        || Number2.getNumberInstance().isValid(c)) {
                    trueNum++;
                }
            }

            if (trueNum == s.length()) {

                return true;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Numbers, () and - only!!", "Error Message",
                        javax.swing.JOptionPane.ERROR_MESSAGE);

                return tc.isEditable() && false;
            }
        }

        return true;

    }
}
