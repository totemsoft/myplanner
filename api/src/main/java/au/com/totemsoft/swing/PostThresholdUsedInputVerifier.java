/*
 * PostThresholdUsedInputVerifier.java
 *
 * Created on February 24, 2003, 3:06 PM
 */

package au.com.totemsoft.swing;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.myplanner.api.ETPConstants;

public class PostThresholdUsedInputVerifier extends CurrencyInputVerifier {

    /** Creates a new instance of PostThresholdUsedInputVerifier */
    public PostThresholdUsedInputVerifier() {
    }

    public boolean verify(JComponent input) {

        if (input instanceof JTextField) {
            JTextComponent c = (JTextComponent) input;
            String s = c.getText();
            // if ( s == null || s.length() == 0 ) return true;

            if (getValue(s) > ETPConstants.TAX_FREE_THRESHOLD)
                c.setText(Currency.getCurrencyInstance().toString(
                        ETPConstants.TAX_FREE_THRESHOLD));
            else
                c.setText(Currency.getCurrencyInstance().toString(getValue(s)));
            return true;
        }
        return false;

    }
}
