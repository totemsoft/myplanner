/*
 * WordSettings.java
 *
 * Created on 28 October 2002, 13:21
 */

package com.argus.financials.config;

/**
 * 
 * @author valeri chibaev
 */

public class ViewSettings extends BasePropertySource {

    private static ViewSettings instance;

    public static synchronized ViewSettings getInstance() {
        if (instance == null)
            instance = new ViewSettings();
        return instance;
    }

    /** Creates a new instance of WordSettings */
    private ViewSettings() {
    }

    /**
     * load/save these values from/to property file
     */
    public void setPropertySource(Object value) throws java.io.IOException {
        if (getPropertySource() != null)
            return;
        if (value == null)
            return;

        // set only once - on application startup
        // load getProperties() with pairs from propertySource
        super.setPropertySource(value);

    }

    // default values
    protected void initProperties(java.util.Properties value) {

    }

    public void store() {

    }

    public String getViewImage(String view) {
        return getProperties().getProperty(view);
    }

}
