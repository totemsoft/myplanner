/*
 * InvalidCodeException.java
 *
 * Created on 23 July 2000, 12:42
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class InvalidCodeException extends java.lang.Exception {

    /**
     * Creates new <code>InvalidCodeException</code> without detail message.
     */
    public InvalidCodeException() {
    }

    /**
     * Constructs an <code>InvalidCodeException</code> with the specified
     * detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public InvalidCodeException(String msg) {
        super(msg);
    }

}
