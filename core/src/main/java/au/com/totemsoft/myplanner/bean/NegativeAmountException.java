/*
 * NegativeAmountException.java
 *
 * Created on 4 April 2002, 12:40
 */

package au.com.totemsoft.myplanner.bean;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class NegativeAmountException extends java.lang.Exception {

    /**
     * Creates new <code>NegativeAmountException</code> without detail
     * message.
     */
    public NegativeAmountException() {
    }

    /**
     * Constructs an <code>NegativeAmountException</code> with the specified
     * detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public NegativeAmountException(String msg) {
        super(msg);
    }

}
