/*
 * ReportExtractorStatus.java
 *
 * Created on 15 November 2002, 15:16
 */

package com.argus.financials.legacy;

/**
 * 
 * @author shibaevv
 */
public class ReportExtractorStatus implements Runnable {

    int messageType;

    private String msg;

    private java.awt.Component parent;

    public ReportExtractorStatus(String s) {
        this(s, null);
    }

    public ReportExtractorStatus(String s, java.awt.Component parent) {
        msg = s;
        this.parent = parent;
        messageType = javax.swing.JOptionPane.INFORMATION_MESSAGE;
    }

    public ReportExtractorStatus(Exception e) {
        this(e, null);
    }

    public ReportExtractorStatus(Exception e, java.awt.Component parent) {
        msg = e.getMessage();
        this.parent = parent;
        messageType = javax.swing.JOptionPane.ERROR_MESSAGE;
    }

    public void run() {

        javax.swing.JOptionPane.showMessageDialog(parent,
                msg == null ? "Error downloading reference data!" : msg,
                "Report Extractor Status", messageType);

    }
}
