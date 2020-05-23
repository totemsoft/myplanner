/*
 * NoDocumentException.java
 *
 * Created on February 23, 2002, 8:47 AM
 */

package au.com.totemsoft.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class NoDocumentException extends java.lang.Exception {

    /**
     * Creates new <code>NoDocumentException</code> without detail message.
     */
    public NoDocumentException() {
    }

    /**
     * Constructs an <code>NoDocumentException</code> with the specified
     * detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public NoDocumentException(String msg) {
        super(msg);
    }
}
