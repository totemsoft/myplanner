/*
 * CurrencyInputVerifier.java
 *
 * Created on 19 September 2001, 12:51
 */

package au.com.totemsoft.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import javax.swing.text.JTextComponent;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.Number2;

public class CurrencyInputVerifier extends NumberInputVerifier2 {

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
