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

package com.argus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 * 							  $Date: 2006/03/06 21:43:06 $
 */

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodComparator implements Comparator {

    /**
     * 
     */
    public MethodComparator() {
        super();
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Method && o2 instanceof Method) {
            Method m1 = (Method) o1;
            Method m2 = (Method) o2;
            // m.getReturnType().getName() N/A
            return 
            	(m1.getName() + StringUtils.toString(m1.getParameterTypes()))
            	.compareTo(
            	(m2.getName() + StringUtils.toString(m2.getParameterTypes())));
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see boolean#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
}

