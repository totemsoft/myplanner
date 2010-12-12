package com.argus.beans;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextArea;

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
 * @author unascribed
 * @version 1.0
 */

public class FTextArea extends JTextArea implements ValueSetter, FocusListener {

    BorderLayout borderLayout1 = new BorderLayout();

    private boolean iAmPressed = false;

    private boolean nonKeyBoard = false;

    private transient Vector dataChangedListeners;

    private Font font = null;

    public static final String PropertyName = "ComponentName";

    public FTextArea() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.addFocusListener(this);
        this.getDocument().putProperty(PropertyName, "FTextArea");
        this.setInputVerifier(new CharacterVerifier());
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

    public void setValue(Object obj) {
        if (obj != null)
            super.setText(obj.toString().trim());
    }

    public void setText(String value, boolean notify) {
        setText(value);
        focusLost(new FocusEvent(this, hashCode(), false));
    }

    public void setText(String t) {

        // Processing of programatically generated event
        nonKeyBoard = true;
        super.setText(t);
        nonKeyBoard = false;
    }

    public boolean isKeyPressed() {
        return iAmPressed;
    }

    public synchronized void registerComponent(String componentName,
            BasePanel parent) {

        getDocument().putProperty(PropertyName, componentName);
        parent.registerComponent(componentName, this);
    }

    public String getSourceName() {
        return (String) this.getDocument().getProperty(PropertyName);

    }

    public void focusLost(java.awt.event.FocusEvent focusEvent) {

        if (focusEvent.isTemporary()) {
            nonKeyBoard = true;
            return;
        }

        // Implement validations on Focus Lost Event
        FTextArea comp = (FTextArea) focusEvent.getSource();

        javax.swing.text.Document doc = comp.getDocument();
        String title = (String) doc.getProperty(PropertyName);
        String value = comp.getText();

        iAmPressed = true;
        if (iAmPressed == false)
            iAmPressed = nonKeyBoard;

        fireFormDataChanged(new FormDataChangedEvent(new FormDataChanged(title,
                value, iAmPressed, this)));
        iAmPressed = false;

    }

    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        //

    }

    public class CharacterVerifier extends InputVerifier {

        public CharacterVerifier() {
        }

        public boolean verify(JComponent comp) {
            return true;
        }
    }

}