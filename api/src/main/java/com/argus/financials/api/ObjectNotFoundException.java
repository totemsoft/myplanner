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

package com.argus.financials.api;

/**
 * 
 */
public class ObjectNotFoundException extends ServiceException
{

    /** serialVersionUID */
    private static final long serialVersionUID = -4791962336461311970L;

    public ObjectNotFoundException(String message)
    {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

}