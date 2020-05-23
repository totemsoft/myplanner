/*
 * DuplicateCalcException.java
 *
 * Created on May 29, 2003, 1:17 PM
 */

package au.com.totemsoft.myplanner.projection.dss;

/**
 * 
 */
public class DuplicateCalcException extends java.lang.Exception {

    /** Creates a new instance of DuplicateCalcException */
    public DuplicateCalcException() {
    }

    /**
     * Constructs an instance of <code>NotEligibleException</code> with the
     * specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public DuplicateCalcException(String msg) {
        super(msg);
    }
}
