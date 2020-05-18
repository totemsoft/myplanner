/*
 * NotEligibleException.java
 *
 * Created on May 2, 2003, 12:14 PM
 */

package com.argus.financials.projection.dss;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class NotEligibleException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>NotEligibleException</code> without
     * detail message.
     */
    public NotEligibleException() {
    }

    /**
     * Constructs an instance of <code>NotEligibleException</code> with the
     * specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public NotEligibleException(String msg) {
        super(msg);
    }
}
