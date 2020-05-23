package au.com.totemsoft.bean;

import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 1.0
 */

public class FComboBox extends JComboBox implements ItemListener, ValueSetter {

    private transient Vector formDataChangedListeners;

    private boolean iAmPressed = false;

    public static final String PropertyName = "ComponentName";

    public FComboBox() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FComboBox(Object[] values) {
        super(values);
        try {

            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        this.setName("FComboBox");
    }

    public boolean isKeyPressed() {
        return iAmPressed;
    }

    public synchronized void registerComponent(String componentName,
            BasePanel parent) {

        setName(componentName);
        parent.registerComponent(componentName, this);
    }

    public synchronized void removeFormDataChangedListener(
            FormDataChangedListener l) {
        if (formDataChangedListeners != null
                && formDataChangedListeners.contains(l)) {
            Vector v = (Vector) formDataChangedListeners.clone();
            v.removeElement(l);
            formDataChangedListeners = v;
        }
    }

    public synchronized void addFormDataChangedListener(
            FormDataChangedListener l) {
        Vector v = formDataChangedListeners == null ? new Vector(2)
                : (Vector) formDataChangedListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            formDataChangedListeners = v;
        }
    }

    protected void fireFormDataChanged(FormDataChangedEvent e) {
        if (formDataChangedListeners != null) {
            Vector listeners = formDataChangedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((FormDataChangedListener) listeners.elementAt(i))
                        .formDataChanged(e);
            }
        }
    }

    public void setValue(Object value) {
        removeItemListener(this);
        setSelectedItem(value);
        addItemListener(this);
    }

    public void itemStateChanged(ItemEvent e) {

        ItemSelectable obj = (ItemSelectable) e.getItemSelectable();

        if (!obj.equals(this))
            return;
        // we need this validation because combobox generated to events for
        // previously selected value
        // and new one . Condition below captures new value only
        if (e.SELECTED != e.getStateChange())
            return;

        Object item = e.getItem();

        if (item == null)
            return;

        // String value = item.toString();
        // String value = (String)e.getItem();
        String title = getName();
        iAmPressed = true;
        fireFormDataChanged(new FormDataChangedEvent(new FormDataChanged(title,
                item, iAmPressed, this)));
        iAmPressed = false;

    }

}