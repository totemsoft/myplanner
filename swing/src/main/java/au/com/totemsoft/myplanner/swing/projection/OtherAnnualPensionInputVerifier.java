/*
 * OtherAnnualPensionInputVerifier.java
 *
 * Created on 9 October 2002, 16:27
 */

package au.com.totemsoft.myplanner.swing.projection;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.myplanner.projection.AllocatedPensionCalcNew;
import au.com.totemsoft.swing.CurrencyInputVerifier;

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
