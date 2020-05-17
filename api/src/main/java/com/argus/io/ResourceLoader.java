/*
 * ResourceLoader.java
 *
 * Created on 4 January 2002, 12:51
 */

/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.io;

/**
 *
 * @author  valeri chibaev
 * @version 
 */

public class ResourceLoader {

    /** Creates new ResourceLoader */
    public ResourceLoader() {}

    public static java.io.InputStream loadInputStream( String source ) throws java.io.IOException {

        if ( source == null )
            return null;

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        if ( loader == null )
            return null;

        // null if the resource could not be found or the caller doesn't have adequate privileges to get the resource.
        java.net.URL url = loader.getResource( source );

        if ( url == null )
            url = loader.getResource( "/" + source );
        
        // file not found -- try as URL
        if ( url == null ) { 
            try {
                url = new java.net.URL( getAppletCodebase(), source );
                System.out.println( "File successfully retrived as URL." );
            } catch (Exception e) {
                e.printStackTrace( System.err );
                return null; 
            }
        }
        
        return url.openStream();
        
    }
    
    public static java.util.Properties loadProperties( String source ) throws java.io.IOException {
        
        java.io.InputStream in = loadInputStream( source );
        if ( in == null )
            return null; 
        
        java.util.Properties properties = new java.util.Properties();
        
        try {
            properties.load( in );
        } finally {
            in.close();
        }
        
        System.out.println( "ResourceLoader.loadProperties( " + source + " ):\n" + properties.toString() + '\n' );

        return properties;
        
    }
    
    public static void saveProperties( String source, java.util.Properties properties ) throws java.io.IOException {
        
        if ( source == null ) return;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        if ( loader == null )
            return;

        // null if the resource could not be found or the caller doesn't have adequate privileges to get the resource.
        java.net.URL url = loader.getResource( source );

        if ( url == null )
            url = loader.getResource( "/" + source );
        
        java.io.FileOutputStream dest = null;
        try { 
            dest = new java.io.FileOutputStream( url.getFile() );
        } catch ( java.io.FileNotFoundException e ) { 
            e.printStackTrace( System.err ); // ( e.getMessage() );
            return; 
        }
        
        try { 
            properties.store( dest, source );
            return;
        } catch ( java.io.IOException e ) {
            e.printStackTrace( System.err ); // ( e.getMessage() );
            return; 
        } finally { 
            try { dest.close(); }
            catch ( java.io.IOException e ) { 
                e.printStackTrace( System.err ); // ( e.getMessage() );
                return; 
            } 
        }

    }

    
    public static java.net.URL getAppletCodebase() throws java.net.MalformedURLException {
        String s = System.getProperty("java.applet.codebase");
        return s == null ? null : new java.net.URL(s);
    }
    
}
