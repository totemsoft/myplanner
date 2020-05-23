/*
 * ActionEventID.java
 *
 * Created on November 29, 2001, 7:45 AM
 */

package au.com.totemsoft.myplanner.etc;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface ActionEventID {

    // action event IDs
    public static final Integer DATA_READY = new Integer(1);

    public static final Integer DATA_MODIFIED = new Integer(2);

    public static final Integer DATA_ADD = new Integer(3);

    public static final Integer DATA_REMOVE = new Integer(4);

    public static final Integer DATA_UPDATE = new Integer(5);

    public static final Integer UPDATE_TOTAL = new Integer(100);

    public static final Integer UPDATE_ASSETS = new Integer(101);

    public static final Integer UPDATE_CHART = new Integer(200);

}
