/*
 * BadArgumentException.java
 *
 * Created on 17 December 2001, 16:56
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class BadArgumentException extends java.lang.Exception {

    public static final double BAD_DOUBLE_VALUE = Double.NaN;

    /**
     * Creates new <code>BadArgumentException</code> without detail message.
     */
    public BadArgumentException() {
    }

    /**
     * Constructs an <code>BadArgumentException</code> with the specified
     * detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public BadArgumentException(String msg) {
        super(msg);
    }
}
