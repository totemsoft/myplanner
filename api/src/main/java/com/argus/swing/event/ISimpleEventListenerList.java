/*
 * IEventListenerList.java
 *
 * Created on 7 September 2002, 17:47
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

package com.argus.swing.event;

/**
 *
 * @author  valeri chibaev
 * @version 
 *
 * @link see javax.swing.event.EventListenerList
 */

public interface ISimpleEventListenerList {
    
    /**
     *  Adds the listener as a listener of the specified type.  
     */
    public void add(Class t, java.util.EventListener l);
    
    /**
     *  Removes the listener as a listener of the specified type.  
     */
    public void remove(Class t, java.util.EventListener l);
    
}
