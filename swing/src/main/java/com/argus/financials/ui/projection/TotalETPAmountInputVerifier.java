/*
 * TotalETPAmountInputVerifier.java
 *
 * Created on 18 October 2002, 11:31
 */

package com.argus.financials.ui.projection;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.format.Currency;

public class TotalETPAmountInputVerifier extends CurrencyInputVerifier {

    private ETPCalcNew etpCalc;

    /** Creates a new instance of TotalETPAmountInputVerifier */
    public TotalETPAmountInputVerifier(ETPCalcNew calc) {
        if (calc == null)
            etpCalc = new ETPCalcNew();
        etpCalc = calc;
    }

    public boolean verify(JComponent input) {

        if (input instanceof JTextField) {
            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            // if ( s == null || s.length() == 0 ) return true;

            boolean b = etpCalc.isValidETPTotalInputVerifier(getValue(s));
            c.setText(Currency.getCurrencyInstance().toString(getValue(s)));
            return b;
        }
        return false;

    }
}
