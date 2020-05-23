/*
 * DataChanged.java
 *
 * Created on 29 August 2002, 17:11
 */

package au.com.totemsoft.bean;

/**
 * 
 * @version
 */
public class ModelDataChanged {

    private String sourceName = "";

    private Object source = null;

    private String sourceValue = "";

    private boolean keyPressed = false;

    /** Creates new DataChanged */
    public ModelDataChanged(Object source) {

        this.source = source;
    }

    public Object getParentEvent() {
        return this.source;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public String getSourceValue() {
        return this.sourceValue;
    }

    public boolean isKeyPressed() {
        return keyPressed;
    }
}
