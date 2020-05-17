/*
 * Assignable.java
 *
 * Created on 22 August 2001, 10:16
 */

package com.argus.financials.api.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public interface Assignable {

    void assign(IFPSAssignableObject value) throws ClassCastException;

}
