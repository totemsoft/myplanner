/*
 * ErrorView.java
 *
 * Created on 24 June 2002, 08:53
 */

package com.argus.financials.io;

/**
 * 
 * @author valeri chibaev
 */

import java.io.File;
import java.io.IOException;

import com.argus.io.PrintStreamView;

public class OutputView extends PrintStreamView {

    /** Creates new ErrorView */
    public OutputView(String dest, boolean autoFlush) throws IOException {
        this(new File(dest), autoFlush);
    }

    public OutputView(File dest, boolean autoFlush) throws IOException {
        super(dest, autoFlush);

        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new java.awt.Dimension(500, 300));
        getTextArea().setForeground(java.awt.Color.GREEN);
        // jTextAreaOutput.setFont(new java.awt.Font("Courier New", 0, 12));

    }

    protected boolean printDate() {
        return false;
    }

}