/*
 * CurrencyInputVerifier.java
 *
 * Created on 19 September 2001, 12:51
 */

package com.argus.financials.swing;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import javax.swing.text.JTextComponent;

import com.argus.format.Currency;
import com.argus.format.Number2;

public class CurrencyInputVerifier extends NumberInputVerifier {

    private static javax.swing.InputVerifier inputVerifier = new CurrencyInputVerifier();

    public static javax.swing.InputVerifier getInstance() {
        return inputVerifier;
    }

    public CurrencyInputVerifier() {
        this(Number2.FRACTION_DIGITS);
    }

    public CurrencyInputVerifier(int fractionDigits) {
        this.fractionDigits = fractionDigits;

        if (this.fractionDigits == Number2.FRACTION_DIGITS)
            number = Currency.getCurrencyInstance();
        else {
            number = Currency.createCurrencyInstance();
            number.setMinimumFractionDigits(this.fractionDigits);
            number.setMaximumFractionDigits(this.fractionDigits);
        }

    }

    protected Number2 getFormatter() {
        return number;
    }

    public boolean verify(javax.swing.JComponent input) {

        if (input instanceof JTextComponent) {

            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            if (s == null || s.length() == 0)
                return true;

            c.setText(getFormatter().toString(getValue(s)));
            return true;

        }

        return false;

    }

}
