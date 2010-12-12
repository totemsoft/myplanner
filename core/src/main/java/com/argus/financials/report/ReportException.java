/*
 * ComException.java
 *
 * Created on 8 March 2002, 16:56
 */

package com.argus.financials.report;

import com.argus.activex.ComException;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class ReportException extends java.lang.Exception
{

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
    public ReportException(ComException e) {
        super("COM Exception:\t" + Long.toHexString((e.getHResult())) + '\n'
                + e.getMessage());
    }

}
