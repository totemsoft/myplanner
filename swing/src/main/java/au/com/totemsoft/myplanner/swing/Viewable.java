/*
 * Viewable.java
 *
 * Created on 11 March 2002, 12:24
 */

package au.com.totemsoft.myplanner.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface Viewable {

    public void clearView();

    public Object getObject();

    public void setObject(Object value);

    public void setVisible(boolean value);

}
