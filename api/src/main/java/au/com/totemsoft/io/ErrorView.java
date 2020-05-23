/*
 * ErrorView.java
 *
 * Created on 24 June 2002, 08:53
 */

package au.com.totemsoft.io;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.io.File;
import java.io.IOException;

public class ErrorView extends PrintStreamView {

    private long lastCalled;

    /** Creates new ErrorView */
    public ErrorView(String dest, boolean autoFlush) throws IOException {
        this(new File(dest), autoFlush);
    }

    public ErrorView(File dest, boolean autoFlush) throws IOException {
        super(dest, autoFlush);

        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new java.awt.Dimension(500, 300));
        getTextArea().setForeground(java.awt.Color.RED);
        // jTextAreaOutput.setFont(new java.awt.Font("Courier New", 0, 12));

    }

    protected boolean printDate() {
        long curCalled = System.currentTimeMillis();
        boolean print = curCalled - lastCalled > 1000L; // 1 sec
        if (print)
            lastCalled = curCalled;
        return print;
    }

}