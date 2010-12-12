/*
 * WindowFactory.java
 *
 * Created on 1 September 2002, 12:12
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
 * @author  valeri chibaev
 * @version 
 */

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class WindowFactory {

    //public static final java.awt.Dimension SCREEN_SIZE = 
    //    java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    private static WindowFactory windowFactory;

    // default icon settings
    private static String appIconName = null;// "/com/argus/images/general/About16.gif";

    
    public static WindowFactory getInstance() {
        if ( windowFactory == null )
            windowFactory = new WindowFactory();
        return windowFactory;
    }

    /** Creates new WindowFactory */
    private WindowFactory() {}

    
    /**
     *
     */
    public static String getDefaultApplicationIcon() {
        return appIconName;
    }
    public static void setDefaultApplicationIcon( String value ) {
        appIconName = value;
    }
    
    public static void setIconImage( java.awt.Window window, String name ) {
        if ( name == null || name.equals( "" ) )
            name = getDefaultApplicationIcon();
        
        if ( name == null )
            return;
        
        URL url = new Object().getClass().getResource( name );
        if (url == null)
        	return;
        
        Image image = new ImageIcon( url ).getImage();
        if ( window instanceof Dialog )
            ; // TODO: how to set ???
        else if ( window instanceof Frame )
            ( (Frame) window ).setIconImage( image );
    }

    
    /**
     * Returns a toolkit-private, shared, invisible Frame
     * to be the owner for JDialogs and JWindows created with
     * null owners.
     */
    private static Frame sharedOwnerFrame;
    public static Frame getSharedOwnerFrame() {
        
        if ( sharedOwnerFrame == null ) {
            sharedOwnerFrame = new Frame() {
                public void show() {
                    // This frame can never be shown
                }
                public synchronized void dispose() {
                    try {
                        getToolkit().getSystemEventQueue();
                        super.dispose();
                    } catch (Exception e) {
                        // untrusted code not allowed to dispose
                    }
                }
            };
            setIconImage( sharedOwnerFrame, null ); // default Java image
        }
        
        return sharedOwnerFrame;
        
    }
    
    public static java.awt.Frame createFrame( Component comp2add ) {
        
        JFrame window = new JFrame();

        setIconImage( window, null );
        
        //window.setModal( false );
        //window.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        window.getContentPane().add( comp2add );

        window.pack();

        return window;

    }

    public static java.awt.Dialog createDialog( java.awt.Container owner, Component comp2add ) {
        
        if ( owner == null || owner instanceof Dialog || owner instanceof Frame ) {

            JDialog window;
            if ( owner == null  )
                window = new JDialog( getSharedOwnerFrame() );
            else if ( owner instanceof Dialog )
                window = new JDialog( (Dialog) owner );
            else if ( owner instanceof Frame )
                window = new JDialog( (Frame) owner );
            else
                window = null;
            
            if ( window != null ) {
                //window.setModal( false );
                window.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
                window.getContentPane().add( comp2add );

                window.pack();

                return window;

            }
            
        }
        
        throw new java.lang.IllegalArgumentException( "Unhandled owner: " + owner );
        
    }
    
}
