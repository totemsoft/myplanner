/*
 * ErrorView.java
 *
 * Created on 24 June 2002, 08:53
 */

package com.argus.io;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.io.File;
import java.io.IOException;

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