/*
 * DuplicateException.java
 *
 * Created on June 25, 2002, 10:48 AM
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */
public class DuplicateException extends java.lang.Exception {

    /**
     * Creates new <code>DuplicateException</code> without detail message.
     */
    public DuplicateException() {
    }

    /**
     * Constructs an <code>DuplicateException</code> with the specified detail
     * message.
     * 
     * @param msg
     *            the detail message.
     */
    public DuplicateException(String msg) {
        super(msg);
    }
}
