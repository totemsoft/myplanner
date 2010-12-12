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

package com.argus.swing;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Hyperlink 
	extends javax.swing.JLabel
	//extends javax.swing.JTextField
	//extends javax.swing.JRadioButton
	//extends javax.swing.JToggleButton
	//extends javax.swing.JButton
{

	/**
	 * 
	 */
	public Hyperlink(String text) {
        super(text);

        //setEditable(false);
        
        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 20, 5, 5)));
        //setBorder(new javax.swing.border.EtchedBorder());
        
        setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    
        //setText(text);
    
	}

	public void reset() {
		setFont( HyperlinkMouseListener.plain );
	}
	public void doClick() {
		setFont( HyperlinkMouseListener.clicked );
	}
	
    public static class HyperlinkMouseListener extends MouseAdapter {

        private static final Font plain = new Font("Dialog", Font.PLAIN, 12);
        private static final Font italic = new Font("Dialog", Font.ITALIC, 12);
        private static final Font bold = new Font("Dialog", Font.BOLD, 12);
        private static final Font clicked = new Font("Dialog", Font.BOLD, 14);

    	private Component c;
    	
        public HyperlinkMouseListener() {

        }
        
        public void mouseEntered(MouseEvent evt) {
            c = evt.getComponent();
            c.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
            if (!clicked.equals(c.getFont()))
            	c.setFont( bold );
        }
        public void mouseExited(MouseEvent evt) {
            c = evt.getComponent();
            c.setCursor( null );
            if (!clicked.equals(c.getFont()))
            	c.setFont( plain );
        }
        public void mouseClicked(MouseEvent evt) {
            c = evt.getComponent();
            c.setFont( clicked );
        }
    };

}

