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

package au.com.totemsoft.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */

import java.io.Serializable;

public final class Pair implements Serializable {

    private static final long serialVersionUID = -6771073512415066381L;

    private Object first;
    private Object second;

    /**
     * 
     * @param first
     * @param second
     */
    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "[" + first + "] " + second;
    }

    public int hashCode() {
        return first.hashCode();
    }

    public boolean equals(Object obj) {
        // System.err.println(this + "=" + obj);
        if (obj instanceof Pair)
            return first.equals(((Pair) obj).first);
        return super.equals(obj);
    }

    /**
     * @return Returns the first.
     */
    public Object getFirst() {
        return first;
    }

    /**
     * @return Returns the second.
     */
    public Object getSecond() {
        return second;
    }

}
