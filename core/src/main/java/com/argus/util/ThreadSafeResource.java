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

package com.argus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 */

public abstract class ThreadSafeResource {

	private static final ThreadLocal threadLocal = new ThreadLocal();
	
	private final Class clazz;

	
	/**
	 * 
	 */
	protected ThreadSafeResource() {
		this(null);
	}

	protected ThreadSafeResource(Class clazz) {
		this.clazz = clazz;
	}

	/**
	 * 
	 * @return thread safe object
	 * @see set
	 */
	protected Object get() throws ResourceException {
		synchronized (threadLocal) {
			return threadLocal.get();
		}
	}

	/**
	 * Set new thread safe object and return previously set
	 * @param obj
	 * @return
	 */
	protected Object set(Object obj) throws ResourceException {
		if (clazz != null && obj != null && !clazz.isInstance(obj))
			throw new ResourceException(obj.getClass() + " parameter has to be instance of " + clazz);
		
		synchronized (threadLocal) {
			Object prev = threadLocal.get();
			threadLocal.set(obj);
			return prev;
		}
	}

	/**
	 * get previously set thread safe object and clear storage
	 * @return
	 */
	protected Object clear() throws ResourceException {
		synchronized (threadLocal) {
			Object obj = threadLocal.get();
			threadLocal.set(null);
			return obj;
		}
	}

}

