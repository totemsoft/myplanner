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

package com.argus.format;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1 $
 */

public class LimitedPlainDocument extends PlainDocument {
    private int limitCharacters;
    
    /** Creates new LimitedPlainDocument */
    public LimitedPlainDocument(int limitChars) {
        limitCharacters = limitChars;
    }
   
    public void insertString(int offset, String  str, AttributeSet attr)
                 throws BadLocationException 
    {
        if (str == null) return;
        //This rejects the entire insertion if it would make
        //the contents too long. Another option would be
        //to truncate the inserted string so the contents
        //would be exactly maxCharacters in length.
        if ((getLength() + str.length()) <= limitCharacters) {
         
            super.insertString(offset, str, attr);
        }
        else
            Toolkit.getDefaultToolkit().beep();
    }
    
    public int getLimit() { return limitCharacters; } 
    public void setLimit( int value ) {
        limitCharacters = value < 0 ? 0 : value;
    }
    
}
