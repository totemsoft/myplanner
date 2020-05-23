package au.com.totemsoft.bean;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JCheckBox;

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
 * @version 1. 0
 */

public class FCheckBox extends JCheckBox implements ItemListener, ValueSetter {
    BorderLayout borderLayout1 = new BorderLayout();

    private transient Vector dataChangedListeners;

    private boolean iAmPressed = false;

    public static final String PropertyName = "ComponentName";

    public FCheckBox() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.addItemListener(this);
    }

    public boolean isKeyPressed() {
        return iAmPressed;
    }

    public synchronized void removeFormDataChangedListener(
            FormDataChangedListener l) {
        if (dataChangedListeners != null && dataChangedListeners.contains(l)) {
            Vector v = (Vector) dataChangedListeners.clone();
            v.removeElement(l);
            dataChangedListeners = v;
        }
    }

    public synchronized void addFormDataChangedListener(
            FormDataChangedListener l) {
        Vector v = dataChangedListeners == null ? new Vector(2)
                : (Vector) dataChangedListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            dataChangedListeners = v;
        }
    }

    protected void fireFormDataChanged(FormDataChangedEvent e) {
        if (dataChangedListeners != null) {
            Vector listeners = dataChangedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((FormDataChangedListener) listeners.elementAt(i))
                        .formDataChanged(e);
            }
        }
    }

    public synchronized void registerComponent(String componentName,
            BasePanel parent) {

        setName(componentName);
        parent.registerComponent(componentName, this);
    }

    public void itemStateChanged(ItemEvent e) {

        JCheckBox item = (JCheckBox) e.getItem();
        String value;

        if (e.getStateChange() == e.SELECTED)
            value = "Yes";
        else
            value = "No";
        // if (item.isSelected()) value ="Yes" ;
        // else value ="No" ;

        String title = getName();
        iAmPressed = true;// this.hasFocus() ;//

        fireFormDataChanged(new FormDataChangedEvent(new FormDataChanged(title,
                value, iAmPressed, this)));
        iAmPressed = false;

    }

    public void setValue(Object obj) {

        removeItemListener(this);

        if (obj == null)
            return;
        String value = obj.toString();

        if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1")
                || value.equalsIgnoreCase("true"))
            this.setSelected(true);
        else
            this.setSelected(false);
        addItemListener(this);

    }

}