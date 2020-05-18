/*
 * Viewable.java
 *
 * Created on 11 March 2002, 12:24
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public interface Viewable {

    public void clearView();

    public Object getObject();

    public void setObject(Object value);

    public void setVisible(boolean value);

}
