/*
 * ExtractException.java
 *
 * Created on 7 June 2002, 14:40
 */

package com.argus.financials.legacy;

/**
 * 
 * @version
 */
public class ExtractException extends java.lang.Exception {

    /**
     * Creates new <code>ExtractException</code> without detail message.
     */
    public ExtractException() {
    }

    /**
     * Constructs an <code>ExtractException</code> with the specified detail
     * message.
     * 
     * @param msg
     *            the detail message.
     */
    public ExtractException(String msg) {
        super(msg);
    }
}
