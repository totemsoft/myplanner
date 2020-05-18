/*
 * About.java
 *
 * Created on 14 March 2003, 15:08
 */

/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.swing;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import com.argus.io.IOUtils;

public class About extends MessageDialog {
    
    public static About createAbout( java.awt.Window parent, boolean modal ) {
    
        if ( parent instanceof java.awt.Dialog )
            return new About( (java.awt.Dialog) parent, modal);
        if ( parent instanceof java.awt.Frame )
            return new About( (java.awt.Frame) parent, modal);
        return null;
        
    }
    
    public static About createAbout() {
        return new About();
    }

    /** Creates new form About */
    private About() {
        super();
        initComponents();
    }
    private About(java.awt.Dialog parent, boolean modal) {
        super( parent, modal);
        initComponents();
    }
    private About(java.awt.Frame parent, boolean modal) {
        super( parent, modal);
        initComponents();
    }
    
    
    private void initComponents() {
        jButtonCancel.setVisible( false );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(470, 330));
        setLocation((screenSize.width-470)/2,(screenSize.height-330)/2);
        
    }
    
    public void setHTMLMessage( java.io.InputStream is, boolean closeStream ) throws java.io.IOException {
        try {
            setHTMLMessage( IOUtils.toString( is ) );
        } finally { 
            if (closeStream) is.close(); 
        }
    }
    
    public void setHTMLMessage( java.io.File file ) throws java.io.IOException {
        setHTMLMessage( new java.io.FileInputStream( file ), true );
    }
    
}
