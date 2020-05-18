/*
 * RunJB.java
 *
 * Created on 29 August 2002, 14:28
 */

/**
 *
 */
package com.argus.bean;

import java.awt.Component;
import java.util.HashMap;
import java.util.Vector;

import com.argus.financials.swing.SwingUtil;

public abstract class BasePanel extends javax.swing.JPanel implements
        FormDataChangedListener, ModelDataChangedListener, MessageSentListener {

    public void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    public synchronized void addFormDataModel(AbstractComponentModel acm) {

        Vector v = formDataModels == null ? new Vector(2)
                : (Vector) formDataModels.clone();
        if (!v.contains(acm)) {
            v.addElement(acm);
            formDataModels = v;
        }
    }

    public void registerComponent(String componentName, Component comp) {
        registredComponents.put(componentName, comp);
        // Check if there is an entry in Data Model for this Component

        for (int i = 0; formDataModels != null & i < formDataModels.size(); i++) {
            AbstractComponentModel acm = (AbstractComponentModel) formDataModels
                    .get(i);
            if (!acm.containsKey(componentName))
                acm.putValue(componentName, "");
        }

    }

    public java.util.HashMap getRegisteredComponents() {
        return registredComponents;
    }

    public void formDataChanged(FormDataChangedEvent e) {

        String sourceName = e.getSourceName();
        // String sourceValue = e.getSourceValue() ;
        Object sourceValue = e.getSourceValue();
        Object source = e.getComponent();
        // get the mnemoninc name of the component triggered this event
        if (!e.isKeyPressed())
            return;
        AbstractComponentModel acm = null;
        // Perform calculation as a result of changing data
        for (int i = 0; formDataModels != null & i < formDataModels.size(); i++) {
            acm = (AbstractComponentModel) formDataModels.get(i);
            if (acm != null) {
                acm.putValue(sourceName, sourceValue);
                if (acm.validate(sourceName))
                    acm.calculate(sourceName);
            }
        }

        // Inform all interested parties that data model has been updated
        if (acm != null)
            acm.sendNotification(source);

    }

    public void modelDataChanged(ModelDataChangedEvent e) {

        AbstractComponentModel acm;

        // Get the object initiated this event
        Object parent = e.getParentEvent();

        // Perform calculation as a result of changing data
        for (int j = 0; formDataModels != null & j < formDataModels.size(); j++) {
            acm = (AbstractComponentModel) formDataModels.get(j);
            java.util.HashMap values = acm.getValues();

            // Update view with calculated values
            for (java.util.Iterator i = registredComponents.keySet().iterator(); i
                    .hasNext();) {
                Object key = i.next();
                Object rc = registredComponents.get(key);
                ValueSetter vs = (ValueSetter) rc;

                Component comp = (Component) rc;

                // String value = (String)values.get(key) ;
                Object value = values.get(key);
                if (value == null)
                    value = "";

                if (!parent.equals(vs) && !comp.hasFocus()
                        && !vs.isKeyPressed())
                    vs.setValue(value);

            }
            acm.postCalculate();
        }

    }

    public void messageSent(MessageSentEvent e) {
    }

    // private AbstractComponentModel acm ;
    private transient Vector formDataModels;

    private java.util.HashMap registredComponents = new HashMap();
}
