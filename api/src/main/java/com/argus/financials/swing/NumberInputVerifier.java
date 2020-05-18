/*
 * CurrencyInputVerifier.java
 *
 * Created on 22 August 2001, 16:22
 */

package com.argus.financials.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import javax.swing.text.JTextComponent;

import com.argus.format.Number2;

public class NumberInputVerifier extends javax.swing.InputVerifier {

    private static javax.swing.InputVerifier inputVerifier = new NumberInputVerifier();

    public static javax.swing.InputVerifier getInstance() {
        return inputVerifier;
    }

    protected int fractionDigits;

    protected Number2 number;

    public NumberInputVerifier() {
        this(Number2.FRACTION_DIGITS);
    }

    public NumberInputVerifier(int fractionDigits) {
        this.fractionDigits = fractionDigits;

        if (this.fractionDigits == Number2.FRACTION_DIGITS)
            number = Number2.getNumberInstance();
        else {
            number = Number2.createInstance();
            number.setMinimumFractionDigits(this.fractionDigits);
            number.setMaximumFractionDigits(this.fractionDigits);
        }

    }

    protected Number2 getFormatter() {
        return number;
    }

    protected double getValue(String value) {

        if (getFormatter().isValid(value))
            return getFormatter().doubleValue(value);
        return 0;

    }

    public boolean verify(javax.swing.JComponent input) {

        if (input instanceof JTextComponent) {

            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            if (s == null || s.length() == 0)
                return true;
            // if ( s == null || s.trim().equals("") ) return true;

            c.setText(getFormatter().toString(getValue(s)));
            return true;

        }

        return false;

    }

    protected final static double HOLE = Double.MAX_VALUE; // see
                                                            // MoneyCalc.HOLE

    private double min = HOLE;

    private double max = HOLE;

    public double getMinimumValue() {
        return min;
    }

    public void setMinimumValue(double min) {
        this.min = min;
    }

    public double getMaximumValue() {
        return max;
    }

    public void setMaximumValue(double max) {
        this.max = max;
    }

    protected boolean checkLimits(double value) {
        return getMinimumValue() != HOLE && value >= getMinimumValue()
                && getMaximumValue() != HOLE && value <= getMaximumValue();
    }

}
