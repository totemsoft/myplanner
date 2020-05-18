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
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 1.0
 */

public class ModelDataChangedEvent extends EventObject {

    public ModelDataChangedEvent(Object source) {
        super(source);
    }

    public Object getParentEvent() {
        Object obj = this.getSource();
        if (obj instanceof ModelDataChanged) {

            return ((ModelDataChanged) obj).getParentEvent();
        }
        return "";

    }

    public String getSourceName() {
        Object obj = this.getSource();
        if (obj instanceof ModelDataChanged) {

            return ((ModelDataChanged) obj).getSourceName();
        }
        return "";
    }

    public String getSourceValue() {
        Object obj = this.getSource();
        if (obj instanceof ModelDataChanged) {

            return ((ModelDataChanged) obj).getSourceValue();
        }
        return "";
    }

    public boolean isKeyPressed() {
        Object obj = this.getSource();
        if (obj instanceof ModelDataChanged) {

            return ((ModelDataChanged) obj).isKeyPressed();
        }
        return false;
    }
}