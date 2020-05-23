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

package au.com.totemsoft.myplanner.service;

import au.com.totemsoft.myplanner.api.ServiceException;

/*
 * Created on 21/05/2006
 * 
 */

public class FinderException extends ServiceException {

    public FinderException(String message) {
        super(message);
    }

    public FinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinderException(Throwable cause) {
        super(cause);
    }

}
