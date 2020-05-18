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
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 */

public class Enumeration implements IEnumeration {

	// selected value
	private Object value;
	// all possible values
	private final Object [] values;
	
	/**
	 * 
	 */
	public Enumeration(final Object [] values) {
		//assert values == null;
		
		this.values = values;
		
		//int length = values.length;
		//this.values = new Object [length];
		//System.arraycopy(values, 0, this.values, 0, length);
	}

	public Enumeration(final Object [] values, Object value) {
		this(values);
		this.value = value;
	}

	public String toString() {
		return value == null ? null : value.toString();
	}

	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
}

