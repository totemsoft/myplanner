/*
 * Assignable.java
 *
 * Created on 22 August 2001, 10:16
 */

package com.argus.financials.api.bean;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface Assignable {

    void assign(IFPSAssignableObject value) throws ClassCastException;

}
