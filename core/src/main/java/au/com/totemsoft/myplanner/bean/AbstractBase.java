package au.com.totemsoft.myplanner.bean;

import java.util.Iterator;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.myplanner.api.bean.IBase;
import au.com.totemsoft.myplanner.service.ServiceAware;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public abstract class AbstractBase
    extends ServiceAware
    implements IBase<Integer>, java.io.Serializable, javax.swing.event.ChangeListener {

    /** serialVersionUID */
    private static final long serialVersionUID = -3720308501036851309L;

    /** Logger. */
    protected final transient Logger LOG = Logger.getLogger(getClass());

    protected static Currency curr = Currency.getCurrencyInstance();

    public static final int MONEY_SCALE = 4;

    private Integer id;

    private boolean modified = false;

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
    public void clear() {
        // id = null;
        // modified = false;

        modified = true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AbstractBase) {
            AbstractBase other = (AbstractBase) obj;
            return id != null && other.id != null && equals(id, other.id);
        }
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

    /***************************************************************************
     * get/set methods
     */
    public boolean isModified() {
        return modified || id == null;
    }

    public void setModified(boolean value) {
        modified = value;
        if (value)
            notifyChangeListeners();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer value) {
        if (equals(id, value))
            return;

        id = value;
        // we load new data into this object (this is not a modification)
        // modified = true;
    }

}
