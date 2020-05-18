/*
 * ComException.java
 *
 * Created on 8 March 2002, 16:56
 */

package com.argus.financials.report;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class ReportException extends java.lang.Exception
{

    /** serialVersionUID */
    private static final long serialVersionUID = -8269285666365019438L;

    /**
     * Constructs an <code>ReportException</code> with the specified detail
     * message.
     * 
     * @param msg the detail message.
     */
    public ReportException(String msg) {
        super(msg);
    }

    /**
     * Creates new <code>ReportException</code> from ComException.
     */
    public ReportException(Exception e) {
        super(e);
    }

}
