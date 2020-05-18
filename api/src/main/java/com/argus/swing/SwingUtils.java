/*
 * SwingUtils.java
 *
 * Created on 22 August 2002, 07:58
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
 * @version 
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.MenuElement;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

public class SwingUtils {

    /**
     *  my local constants/variables
     */
    public static final java.awt.Dimension DIM_SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int SCREEN_HEIGHT = (int) DIM_SCREEN.getHeight();
    public final static int SCREEN_WIDTH = (int) DIM_SCREEN.getWidth();
    
    public static final java.awt.Color EDITABLE = java.awt.Color.white;
    public static final java.awt.Color NON_EDITABLE = java.awt.Color.lightGray;

    
    // application default icon
    private static javax.swing.ImageIcon imageIcon;

        
    /** Creates new SwingUtils */
    private SwingUtils() {
    }

    
    //imageName = "/image/main/xxx.gif";
    private static String _imageName; // temp storage
    public static javax.swing.ImageIcon getImageIcon( String imageName ) {
        
        if ( imageIcon == null || !imageName.equals( _imageName ) ) {
            try { 
                imageIcon = new javax.swing.ImageIcon( new Object().getClass().getResource( imageName ) );
                _imageName = imageName;
            } catch ( Exception e ) {
                e.printStackTrace( System.err ); // no image file available
            }
        }
        
        return imageIcon;
        
    }
    
    public static void setIconImage( Frame frame, String name ) {
        frame.setIconImage( getImageIcon( name ).getImage() );
    }
    
    
    /**
     *
     */
    public static void setWindowTitle( Container window, String title ) {
        
        if ( window instanceof JFrame ) {
            ( ( JFrame ) window ).setTitle( title );
        } else if ( window instanceof JInternalFrame ) {
            ( ( JInternalFrame ) window ).setTitle( title );
        } else if ( window instanceof JDialog ) {
            ( ( JDialog ) window ).setTitle( title );
        } else {
            throw new java.lang.IllegalArgumentException( "setWindowTitle(): Unhandled class: " + window.getClass() );
        }
        
    }
    
    public static void setJMenuBar( Container window, JMenuBar menuBar ) {
        
        if ( window instanceof JFrame ) {
            ( ( JFrame ) window ).setJMenuBar( menuBar );
        } else if ( window instanceof JInternalFrame ) {
            ( ( JInternalFrame ) window ).setJMenuBar( menuBar );
        } else if ( window instanceof JDialog ) {
            ( ( JDialog ) window ).setJMenuBar( menuBar );
        } else {
            throw new java.lang.IllegalArgumentException( "setJMenuBar(): Unhandled class: " + window.getClass() );
        }
        
    }
    
    public static void clear( Component component ) {
        
        if ( !( component instanceof Container ) ) return;
        
        Container container = (Container)component;
        
        if ( container.getComponentCount() == 0 ) {
            
            if ( component instanceof JLabel ) return;
            if ( component.hasFocus() ) return;
            
            if ( component instanceof JTextComponent ) {
                if ( component instanceof JTextField )
                    ( (JTextField) component ).setText(null);
                else if ( component instanceof JTextArea )
                    ( (JTextArea) component ).setText(null);
                else if ( component instanceof JEditorPane ) {
                    if ( component instanceof JTextPane )
                        ( (JTextPane) component ).setText(""); // ???
                    else
                        ( (JEditorPane) component ).setText(null);
                }
            } else if ( component instanceof AbstractButton ) {
                ( (AbstractButton) component ).setSelected( false );
            }
            
        } else if ( component instanceof JComboBox ) { // consists of many other UI, Render, ...
            ( (JComboBox) component ).setSelectedItem( null );
        } else {
            for ( int i = 0; i < container.getComponentCount(); i++ )
                clear( container.getComponent(i) );
        }
        
    }
    
    public static void pack( java.awt.Container container ) {
        
        try {
            java.lang.reflect.Method method = container.getClass().getMethod( "pack", new Class [] {} );
            method.invoke( container, new Object [] {} );
        } catch (Exception e) {
            e.printStackTrace( System.err );
        }
        
    }
    
    public static void setColumnWidth( JTable table, int columnID, int maxWidth, int minWidth, int preferredWidth ) {
        TableColumn column = table.getColumnModel().getColumn( columnID );
        if ( maxWidth >= 0 ) column.setMaxWidth( maxWidth );
        if ( minWidth >= 0 ) column.setMinWidth( minWidth );
        if ( preferredWidth >= 0 ) column.setPreferredWidth( preferredWidth );
    }

    /***************************************************************************
     *      System properties
     **************************************************************************/
    
    // user.name    User's account name
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    // user.dir     User's current working directory
    public static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    // java.io.tmpdir
    public static String getTemporaryDirectory() {
        return System.getProperty("java.io.tmpdir");
    }
    
    /********************************************************************
     *                          Font change                             *
     ********************************************************************/
    private static Font defaultFont = new Font( "Arial", Font.PLAIN, 11 );
    
    public static Font getDefaultFont() {
        return defaultFont;
    }
    public static void setDefaultFont( Font value ) {
        defaultFont = value;
    }
    
    public static void setDefaultFont( Component comp ) {
        
        comp.setFont( defaultFont );
        
        if ( comp instanceof JComponent ) {
            
            //System.out.println(comp);
            
            if ( comp instanceof JMenu ) {
                MenuElement[] me = ( (JMenu) comp ).getSubElements();
                if ( me != null )
                    for ( int j = 0; j < me.length; j++ )
                        setDefaultFont( (Component) me[j] );
                
            } else if ( comp instanceof JTable )  {
                ( (JTable) comp ).getTableHeader().setFont( defaultFont );
                
            } else if ( comp instanceof JTextField )  {
                /*
                final JTextField jTextField = ( JTextField ) comp;

                if ( jTextField.getInputVerifier() instanceof DateInputVerifier )
                    jTextField.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            if ( jTextField.getText().trim().length() == 0 )
                                jTextField.setText( com.smoothlogic.format.DateTime.DEFAULT_INPUT );
                        }
                    });
                */               
            }
            
            // set panel border title font
            Border border = ( (JComponent) comp ).getBorder();
            if ( border instanceof TitledBorder )
                ( (TitledBorder) border ).setTitleFont( defaultFont );
            
        }
        
        if ( comp instanceof Container ) {
            Container container = (Container) comp;
            
            for ( int i = 0; i < container.getComponentCount(); i++ )
                setDefaultFont( container.getComponent(i) );
        }
        
    }
    
    
    /**
     * This methode set a default font for the application
     *
     * @param oFont - the new default font
     */
    public static void setUIDefaultFont()
    {
        java.util.Hashtable oUIDefault = UIManager.getDefaults();
        java.util.Enumeration oKey     = oUIDefault.keys();
        String oStringKey = null;

        while (oKey.hasMoreElements()){
            oStringKey = oKey.nextElement().toString();
            if (oStringKey.endsWith("font") ||
            oStringKey.endsWith("acceleratorFont")){
                UIManager.put(oStringKey, defaultFont);
            }
        }
    }
    
}