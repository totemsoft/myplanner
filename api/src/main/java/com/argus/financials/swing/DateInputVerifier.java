/*
 * DateInputVerifier.java
 *
 * Created on 17 August 2001, 10:44
 */

package com.argus.financials.swing;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.awt.Toolkit;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.argus.util.DateTimeUtils;

public class DateInputVerifier extends InputVerifier {

    private static DateInputVerifier inputVerifier = new DateInputVerifier();

    public static DateInputVerifier getInstance() {
        return inputVerifier;
    }

    // private DateInputVerifier() {}
    public DateInputVerifier() {
    }

    public boolean verify(JComponent input) {

        if (input instanceof JTextField) {
            input.setToolTipText(DateTimeUtils.DEFAULT_INPUT);
            if (!DateTimeUtils.isValidDate(((JTextField) input).getText())) {
                ((JTextField) input).setText(null);
                Toolkit.getDefaultToolkit().beep();
                return false;
            }
        }
        return true;

    }

}
