package com.argus.financials.bean;

import java.util.Iterator;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.argus.format.Currency;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public abstract class AbstractBase extends Object implements java.io.Serializable,
        javax.swing.event.ChangeListener {

    /** serialVersionUID */
    private static final long serialVersionUID = -3720308501036851309L;

    /** Logger. */
    protected final Logger LOG = Logger.getLogger(getClass());

    protected static Currency curr = Currency.getCurrencyInstance();

    public static final int MONEY_SCALE = 4;

    private Integer primaryKeyID;

    private boolean modified = false;

    protected AbstractBase() {

    }

    /***************************************************************************
     * javax.swing.event.ChangeListener
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        // default = DO NOTHING
        // derived classes, who is interested in receiving this event
        // has to override this method
    }

    transient private java.util.Collection changeListeners;

    // transient private javax.swing.event.EventListenerList changeListeners;

    private boolean notify = true;

    public boolean isNotify() {
        return notify;
    }

    public void disableNotify() {
        notify = false;
    }

    public void enableNotify() {
        notify = true;
        notifyChangeListeners();
    }

    public void addChangeListener(javax.swing.event.ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new java.util.ArrayList();
        // changeListeners = new javax.swing.event.EventListenerList();
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
            // to update this listener state
            // listener.stateChanged( new javax.swing.event.ChangeEvent(this) );
        }
    }

    public void removeChangeListener(javax.swing.event.ChangeListener listener) {
        if (changeListeners == null)
            return;
        if (listener == null) // remove all
            changeListeners.clear();
        else if (changeListeners.contains(listener))
            changeListeners.remove(listener);
    }

    protected void notifyChangeListeners() {
        if (changeListeners == null || !notify)
            return;
        final ChangeEvent changeEvent = new ChangeEvent(this);
        Iterator iter = changeListeners.iterator();
        while (iter.hasNext()) {
            final ChangeListener listener = (ChangeListener) iter.next();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.stateChanged(changeEvent);
                }
            });
        }
    }

    /***************************************************************************
     * helper methods
     */
    protected void clear() {
        // primaryKeyID = null;
        // modified = false;

        modified = true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AbstractBase)
            return getPrimaryKeyID() != null
                    && ((AbstractBase) obj).getPrimaryKeyID() != null
                    && equals(getPrimaryKeyID(), ((AbstractBase) obj)
                            .getPrimaryKeyID());
        return super.equals(obj);
    }

    protected boolean equals(String oldValue, String newValue) {
        if (newValue != null && newValue.trim().length() == 0)
            newValue = null;
        return equals((Object) oldValue, (Object) newValue);
    }

    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    public static boolean empty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static Object createNewInstance(Integer objectTypeID,
            Class[] parameterTypes, Object[] initargs) {
        // get Class types for this object id (XXXBean)
        Class objClass = com.argus.financials.bean.db.ObjectClass
                .getObjectClass(objectTypeID);
        try {
            return objClass.getConstructor(parameterTypes)
                    .newInstance(initargs);
        } catch (Exception e) {
            System.err.println("-----> No constructor found for objectTypeID: "
                    + objectTypeID + " <-----");
            // e.printStackTrace( System.err );
            return null;
        }
    }

    public static Object createNewInstance(Integer objectTypeID) {
        return createNewInstance(objectTypeID, null, null);
    }

    /***************************************************************************
     * get/set methods
     */
    public boolean isModified() {
        return modified || primaryKeyID == null;
    }

    public void setModified(boolean value) {
        modified = value;
        if (value)
            notifyChangeListeners();
    }

    public Integer getPrimaryKeyID() {
        return primaryKeyID;
    }

    public void setPrimaryKeyID(Integer value) {
        if (equals(primaryKeyID, value))
            return;

        primaryKeyID = value;
        // we load new data into this object (this is not a modification)
        // modified = true;
    }

}
