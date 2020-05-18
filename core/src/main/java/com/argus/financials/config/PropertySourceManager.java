/*
 * PropertyManager.java
 *
 * Created on January 4, 2002, 9:12 AM
 */

package com.argus.financials.config;

import java.io.IOException;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class PropertySourceManager extends BasePropertySource {

    private static PropertySourceManager propertySourceManager;

    /** Creates new PropertyManager */
    private PropertySourceManager() {
    }

    public static synchronized PropertySourceManager getInstance() {
        if (propertySourceManager == null)
            propertySourceManager = new PropertySourceManager();
        return propertySourceManager;
    }

    /**
     * get/set
     */
    protected void setPropertySource(Object value) throws IOException {
        if (value == null || getPropertySource() != null)
            return;

        System.out.println("PropertySourceManager: Loading from file: "
                + value.toString() + '\n');

        // set only once - on application startup
        // load properties with pairs from propertySource
        super.setPropertySource(value);

        String propertyValue = null;
        if (getProperties().containsKey(PLANNER_LOCALE_PROPERTIES)) {
            propertyValue = getProperties().getProperty(PLANNER_LOCALE_PROPERTIES).trim();
            FPSLocale.getInstance().setPropertySource(propertyValue);
        } else
            FPSLocale.getInstance().setPropertySource(null); // default

        if (getProperties().containsKey(PLANNER_WORD_PROPERTIES)) {
            propertyValue = getProperties().getProperty(PLANNER_WORD_PROPERTIES).trim();
            WordSettings.getInstance().setPropertySource(propertyValue);
        }

        if (getProperties().containsKey(PLANNER_VIEW_PROPERTIES)) {
            propertyValue = getProperties().getProperty(PLANNER_VIEW_PROPERTIES).trim();
            ViewSettings.getInstance().setPropertySource(propertyValue);
        }

    }

    public void load(String source) throws java.io.IOException {
        setPropertySource(source);
    }

    /**
     * Unload the connection pools.
     */
    public void unload() {

        if (getProperties().containsKey(PLANNER_LOCALE_PROPERTIES))
            FPSLocale.getInstance().store(); // do nothing

        if (getProperties().containsKey(PLANNER_WORD_PROPERTIES))
            WordSettings.getInstance().store();

        propertySourceManager = null;
    }

}
