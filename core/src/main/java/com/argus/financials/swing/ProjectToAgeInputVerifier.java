/*
 * ProjectToAgeInputVerifier.java
 *
 * Created on 22 January 2003, 10:41
 */

package com.argus.financials.swing;

import javax.swing.text.JTextComponent;

public class ProjectToAgeInputVerifier extends javax.swing.InputVerifier {

    private static ProjectToAgeInputVerifier inputVerifier = new ProjectToAgeInputVerifier();

    public static ProjectToAgeInputVerifier getInstance() {
        return inputVerifier;
    }

    /** Creates a new instance of ProjectToAgeInputVerifier */
    public ProjectToAgeInputVerifier() {
    }

    public boolean verify(javax.swing.JComponent input) {

        if (input instanceof javax.swing.JTextField) {
            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            if (s == null || s.trim().equals("")) {
                c.setText("90");
                return true;
            }

            int value = 0;
            try {
                value = Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                c.setText(null);
                return c.isEditable() && false;
            }

            if (value < 5)
                c.setText("5");
            return true;
        }
        return true;

    }
}
