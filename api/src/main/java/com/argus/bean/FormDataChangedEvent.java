package com.argus.bean;

import java.util.EventObject;

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

public class FormDataChangedEvent extends EventObject {

    public FormDataChangedEvent(Object source) {
        super(source);
    }

    public String getSourceName() {
        Object obj = this.getSource();
        if (obj instanceof FormDataChanged) {

            return ((FormDataChanged) obj).getSourceName();
        }
        return "";
    }

    /*
     * public String getSourceValue(){ Object obj= this.getSource() ; if (obj
     * instanceof FormDataChanged){
     * 
     * return ((FormDataChanged) obj ).getSourceValue() ; } return "" ; }
     */
    public Object getSourceValue() {
        Object obj = this.getSource();
        if (obj instanceof FormDataChanged) {

            return ((FormDataChanged) obj).getSourceValue();
        }
        return "";
    }

    public boolean isKeyPressed() {
        Object obj = this.getSource();
        if (obj instanceof FormDataChanged) {

            return ((FormDataChanged) obj).isKeyPressed();
        }
        return false;
    }

    public Object getComponent() {
        Object obj = this.getSource();
        if (obj instanceof FormDataChanged) {

            return ((FormDataChanged) obj).getComponent();
        }
        return "";

    }
}