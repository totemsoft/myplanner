/*
 * Assignable.java
 *
 * Created on 22 August 2001, 10:16
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public interface Assignable {

    public void assign(FPSAssignableObject value) throws ClassCastException;

}
