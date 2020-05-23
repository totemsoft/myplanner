/*
 * PurchasePriceInputVerifier.java
 *
 * Created on 18 October 2002, 18:00
 */

package au.com.totemsoft.myplanner.swing.projection;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.swing.CurrencyInputVerifier;

public class PurchasePriceInputVerifier extends CurrencyInputVerifier {

    private MortgageCalc mCalc;

    /** Creates a new instance of PurchasePriceInputVerifier */
    public PurchasePriceInputVerifier(MortgageCalc calc) {
        if (calc == null)
            mCalc = new MortgageCalc();
        mCalc = calc;
    }

    public boolean verify(JComponent input) {

        if (input instanceof JTextField) {
            JTextComponent c = (JTextComponent) input;
            String s = c.getText();

            boolean b = mCalc
                    .isValidPerchasePriceInputVerifier(getValue(((JTextField) input)
                            .getText()));
            c.setText(Currency.getCurrencyInstance().toString(getValue(s)));
            return b;
        }
        return false;

    }
}
