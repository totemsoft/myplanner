/*
 * ResourceLoader.java
 *
 * Created on 4 January 2002, 12:51
 */

package com.argus.config;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class ResourceLoader {

    /** Creates new ResourceLoader */
    private ResourceLoader() {
    }

    public static URL getResource(String source)
    {
        if (source == null)
            return null;

        // null if the resource could not be found or the caller doesn't have
        // adequate privileges to get the resource.
        URL url = ResourceLoader.class.getResource(source);

        // for images
        if (url == null)
            url = ResourceLoader.class.getResource("/" + source);
        
        if (url == null)
            throw new IllegalArgumentException("Could not load resource: [" + source + "]");
        
        return url;

    }

    public static InputStream loadInputStream(String source)
        throws java.io.IOException 
    {
        return getResource(source).openStream();
    }

    public static java.util.Properties loadProperties(String source)
            throws java.io.IOException {

        java.io.InputStream in = loadInputStream(source);
        if (in == null)
            return null;

        java.util.Properties properties = new java.util.Properties();

        try {
            properties.load(in);
        } finally {
            in.close();
        }

        System.out.println("ResourceLoader.loadProperties( " + source + " ):\n"
                + properties.toString() + '\n');

        return properties;

    }

    public static void saveProperties(String source,
            java.util.Properties properties) throws java.io.IOException {
        saveProperties(source, properties, null);
    }

    public static void saveProperties(String source,
            java.util.Properties properties, String header)
            throws java.io.IOException {

        if (source == null)
            return;

        // throw if the resource could not be found or the caller doesn't have
        // adequate privileges to get the resource.
        URL url = getResource(source);

        java.io.FileOutputStream dest = null;
        try {
            dest = new java.io.FileOutputStream(url.getFile());
        } catch ( java.io.FileNotFoundException e ) {
            e.printStackTrace();
            return;
        }

        try {
            properties.store(dest, header);
        } catch ( java.io.IOException e ) {
            e.printStackTrace();
            return;
        } finally {
            try {
                dest.close();
            } catch (java.io.IOException e) {
                // e.printStackTrace( System.err );
                System.err.println(e.getMessage());
            }
        }

    }

}
