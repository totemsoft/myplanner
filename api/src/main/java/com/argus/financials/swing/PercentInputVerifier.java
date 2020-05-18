/*
 * PercentInputVerifier.java
 *
 * Created on 19 September 2001, 12:51
 */

package com.argus.financials.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import com.argus.format.Number2;
import com.argus.format.Percent;

public class PercentInputVerifier extends NumberInputVerifier {

    private static javax.swing.InputVerifier inputVerifier = new PercentInputVerifier();

    public static javax.swing.InputVerifier getInstance() {
        return inputVerifier;
    }

    public PercentInputVerifier() {
        this(Number2.FRACTION_DIGITS);
    }

    public PercentInputVerifier(int fractionDigits) {
        this.fractionDigits = fractionDigits;

        if (this.fractionDigits == Number2.FRACTION_DIGITS)
            number = Percent.getPercentInstance();
        else {
            number = Percent.createPercentInstance();
            number.setMinimumFractionDigits(this.fractionDigits);
            number.setMaximumFractionDigits(this.fractionDigits);
        }

        setMinimumValue(0.);
        setMaximumValue(100.);

    }

    protected Number2 getFormatter() {
        return number;
    }

    public boolean verify(javax.swing.JComponent input) {

        String s = null;

        if (input instanceof JTextComponent) {

            JTextComponent c = (JTextComponent) input;
            s = c.getText();
            if (s == null || s.length() == 0)
                return true;

            double value = getValue(c.getText());
            if (!checkLimits(value)) {
                c.setText(getFormatter().toString(0.));
                return false;
            }

            c.setText(getFormatter().toString(value));
            return true;

        } else if (input instanceof JLabel) {

            JLabel c = (JLabel) input;
            s = c.getText();
            if (s == null || s.length() == 0)
                return true;

            double value = getValue(c.getText());
            if (!checkLimits(value)) {
                c.setText(getFormatter().toString(0.));
                return false;
            }

            c.setText(getFormatter().toString(value));
            return true;

        }

        return false;

    }

}
