/*
 * BasePropertySource.java
 *
 * Created on 4 January 2002, 13:21
 */

package com.argus.financials.config;

import com.argus.config.ResourceLoader;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class BasePropertySource implements PropertySourceName {

    /**
     * String (fileName) or java.net.URL (for applet codeBase) Main Storage for
     * ALL *.properties file name / url
     */
    private java.util.Properties properties;

    /** Creates new BasePropertySource */
    protected BasePropertySource() {
    }

    /**
     * 
     */
    public Object getPropertySource() {
        return getProperties().getProperty(SOURCE_NAME_PROPERTY);
    }

    protected void setPropertySource(Object value) throws java.io.IOException {
        // if ( value == null || getPropertySource() != null ) return;

        if (value instanceof String) {
            properties = ResourceLoader.loadProperties((String) value);
            properties.setProperty(SOURCE_NAME_PROPERTY, (String) value);
            initProperties(properties);
            // System.out.println( "properties: " + properties.toString() + '\n'
            // );
        } else
            throw new RuntimeException("Unhandled Source class: "
                    + value.getClass().getName());

    }

    protected void initProperties(java.util.Properties value) {
    }

    protected java.util.Properties getProperties() {
        if (properties == null)
            properties = new java.util.Properties();
        return properties;
    }

    protected void setProperties(java.util.Properties value) {
        properties = value;
    }

}
