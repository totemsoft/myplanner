/*
 * OtherAnnualPensionInputVerifier.java
 *
 * Created on 9 October 2002, 16:27
 */

package com.argus.financials.ui.projection;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.argus.financials.projection.AllocatedPensionCalcNew;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.format.Currency;

public class OtherAnnualPensionInputVerifier extends CurrencyInputVerifier {

    private AllocatedPensionCalcNew apCalc;

    // private DateInputVerifier() {}
    public OtherAnnualPensionInputVerifier(AllocatedPensionCalcNew calc) {
        if (calc == null)
            apCalc = new AllocatedPensionCalcNew();
        apCalc = calc;
    }

    public boolean verify(JComponent input) {

        if (input instanceof JTextField) {
            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            if (s == null || s.length() == 0)
                return true;

            boolean b = apCalc
                    .isValidOtherAnnualPensionInputVerifier(getValue(((JTextField) input)
                            .getText()));
            c.setText(Currency.getCurrencyInstance().toString(getValue(s)));
            return b;
        }
        return false;

    }

}
