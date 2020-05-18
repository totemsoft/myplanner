/*
 * LoginFailedException.java
 *
 * Created on 3 August 2001, 15:06
 */

package com.argus.financials.service;

import com.argus.financials.api.ServiceException;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class LoginFailedException extends ServiceException {

    /**
     * Constructs an <code>LoginFailedException</code> with the specified
     * detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public LoginFailedException(String msg) {
        super(msg);
    }

}
