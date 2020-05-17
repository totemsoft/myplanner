/*
 * DataChanged.java
 *
 * Created on 29 August 2002, 17:11
 */

package com.argus.bean;

/**
 * 
 * @version
 */
public class FormDataChanged {

    private String sourceName = "";

    // private String sourceValue = "";
    private Object sourceValue = new Object();

    private boolean keyPressed = false;

    private Object component = null;

    /** Creates new DataChanged */
    // public FormDataChanged(String sourceName, String sourceValue, boolean
    // keyPressed, Object comp) {
    public FormDataChanged(String sourceName, Object sourceValue,
            boolean keyPressed, Object comp) {
        this.sourceName = sourceName;
        this.sourceValue = sourceValue;
        this.keyPressed = keyPressed;
        this.component = comp;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    // public String getSourceValue(){ return this.sourceValue;}
    public Object getSourceValue() {
        return this.sourceValue;
    }

    public boolean isKeyPressed() {
        return keyPressed;
    }

    public Object getComponent() {
        return this.component;
    }
}
