/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.swing;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1 $
 */

import java.awt.event.KeyEvent;

import javax.swing.ComboBoxModel;

public class ComboBoxKeySelectionManager implements javax.swing.JComboBox.KeySelectionManager {
    
    private static final int TIMEOUT = 3000;
    
    private static ComboBoxKeySelectionManager memComboAgent;
    
    public static ComboBoxKeySelectionManager getInstance() {
        if ( memComboAgent == null )
            memComboAgent = new ComboBoxKeySelectionManager();
        return memComboAgent;
    }

    
    private Thread resetKeyThread;
    
    private String keys = "";
    
    public ComboBoxKeySelectionManager() {
        
        resetKeyThread = new Thread() {

            public void run() {

                while ( !isInterrupted() ) {
                    
                    synchronized (this) {
                        try { 
                            this.wait( TIMEOUT );
                            keys = "";
                        } catch ( InterruptedException e ) {}
                    }
                    
                }
            }

        };
        resetKeyThread.setName( "com.smoothlogic.util.MemComboAgent::javax.swing.JComboBox.KeySelectionManager" );
        resetKeyThread.start();
        
    }
    
    public int selectionForKey( char param, ComboBoxModel comboBoxModel ) {

        synchronized (resetKeyThread) {
            //param = Character.toUpperCase( param );
            if ( Character.isLetterOrDigit( param ) ||
                 param == KeyEvent.VK_SPACE || ",./;=[\\]".indexOf(param) >= 0 ) {

                keys += param;

                for ( int i = 0; i < comboBoxModel.getSize(); i++ ) {
                    // String or com.smoothlogic.code.ReferenceCode
                    String item = comboBoxModel.getElementAt(i).toString();

                    if ( item.length() < keys.length() ) continue; 

                    if ( keys.compareToIgnoreCase( item.substring( 0, keys.length() ) ) == 0 ) {
                        resetKeyThread.interrupt();
                        return i;
                    }

                }

            }

            return -1;
            
        }
        
    }
    
}