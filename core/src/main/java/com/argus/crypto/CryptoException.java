/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.crypto;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 *							  $Date: 2006/02/23 21:46:36 $
 */

public class CryptoException extends Exception {

    /**
     * 
     */
    public CryptoException() {
        super();
    }

    /**
     * @param message
     */
    public CryptoException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CryptoException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

}

