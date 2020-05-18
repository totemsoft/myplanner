/*
 * ICloseDialog.java
 *
 * Created on 20 February 2003, 14:05
 */

package com.argus.financials.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public interface ICloseDialog {

    public void doClose(java.awt.event.ActionEvent evt);

    public void doSave(java.awt.event.ActionEvent evt);

    public boolean isModified();

}
