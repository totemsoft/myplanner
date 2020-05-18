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
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.3 $
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class IOUtils {

    /** Creates new FileUtils */
    private IOUtils() {}



    /**
     * Return the extension portion of the file's name .
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
    public static String getExtension( java.io.File file ) {
        return file == null ? null : getExtension( file.getName() );
    }

    public static String getExtension( String fileName ) {
        int i = fileName.lastIndexOf('.');
        return ( i <= 0 || i >= fileName.length() - 1 ) ?
            null : fileName.substring( ++i ).toLowerCase();
    }

    /**
     * Atomically creates a new, empty file named by this abstract pathname if
     * and only if a file with this name does not yet exist.
     *
     * @return  <code>true</code> if the named file does not exist and was
     *          successfully created; <code>false</code> if the named file
     *          already exists
     *
     * @throws  IOException
     *          If an I/O error occurred
     */
    public static boolean createNewFile(File file) throws IOException {
        if (file.exists())
            return true;

        // create dir (if does not exists)
        if (!file.getParentFile().exists())
            if (!file.getParentFile().mkdirs())
                return false;
        // create file
        return file.createNewFile();
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static void writeObject( Object obj, String filename ) throws IOException {
        writeObject( obj, new File( filename ) );
    }
    public static void writeObject( Object obj, File file ) throws IOException {
        OutputStream os = new FileOutputStream( file );
        try {
            writeObject( obj, os );
            //System.out.println( "writeObject() to " + file.getCanonicalPath() );
        } finally { os.close(); }
    }
    public static void writeObject( Object obj, OutputStream os ) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream( os );
        try { out.writeObject(obj); }
        finally { out.close(); }
    }

    /**
     * Converts a Java serializable object into a byte array. The object to be
     * transformed must implement the interface java.io.Serializable.
     *
     * @param object - object to be converted into a byte array
     * @return a byte array which contains the object
     *
     * @exception java.io.IOException
    */
    public static byte[] writeObjectToByteArray(Object object)
        throws java.io.IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeObject(object, baos);
        return baos.toByteArray();
    }

    public static Object readObject( String filename ) throws IOException, ClassNotFoundException {
        return readObject( new File( filename ) );
    }
    public static Object readObject( File file ) throws IOException, ClassNotFoundException {
        InputStream is = new FileInputStream( file );
        try {
            Object obj = readObject( is );
            //System.out.println( "readObject() from " + file.getCanonicalPath() );
            return obj;
        } finally {
        	is.close();
        }
    }
    public static Object readObject( InputStream source ) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream( source );
        try {
        	return in.readObject();
        } finally {
        	in.close();
        }
    }


    public static String toString( InputStream source ) throws java.io.IOException {

    	try {
	        int i = 0;
	        //source = new java.io.BufferedInputStream( source );
	        StringBuffer sb = new StringBuffer();
	        while ( ( i = source.read() ) != -1 )
	            sb.append( (char) i );
	        return sb.toString();

    	} finally {
	        source.close();
    	}

    }

	public static void write(InputStream source, OutputStream destination)
		throws IOException
	{
		int len;
		while ( (len = source.available()) > 0 ) {
			byte [] data = new byte [len];
			source.read( data );
			destination.write( data );
		}
		destination.flush();
		destination.close();
		destination = null;

		source.close();
		source = null;

	}

	public static byte [] getDataAsByteArray(InputStream source)
		throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int len;
		while ( ( len = source.available() ) > 0 ) {
			byte [] tmp = new byte [len];
			int bytesRead = source.read( tmp );
			if (bytesRead == -1) break;

			baos.write(tmp, 0, bytesRead);
		}

		source.close();
		source = null;

		return baos.toByteArray();

	}


    public static String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    // user.name User's account name
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    // user.dir User's current working directory
    public static String getUserDirectory() {
        return System.getProperty("user.dir");
    }

    // user.home User's home directory
    public static String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    // java.io.tmpdir
    public static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getCanonicalPath(File f) {
        try {
            return f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return f.getAbsolutePath();
        }
    }

	public static URL getResource( String resource )
		throws IOException
	{
        if ( resource == null )
            return null;

        ClassLoader loader = IOUtils.class.getClassLoader();
        //ClassLoader loader = ClassLoader.getSystemClassLoader();

        URL url = loader.getResource(resource);

        if ( url == null ) {
        	if (resource.charAt(0) == '/' && resource.length() > 1)
        		url = loader.getResource( resource.substring(1) );
        	else
        		url = loader.getResource( "/" + resource );
        }

		if (url == null) {
			try {
				url = new URL("file", "", resource);
			} catch (MalformedURLException ex) {
				throw new IOException(ex.getMessage());
			}
		}

        // null if the resource could not be found
    	// or the caller doesn't have adequate privileges to get the resource.
		if (url == null)
			throw new IOException("Could not resolve resource: " + resource);

		return url;

	}

    public static java.io.InputStream openInputStream( String resource )
    	throws IOException
	{
        return getResource( resource ).openStream();
    }

	public static byte [] read(String fileName) throws IOException {

    	File file = new File(fileName);
    	if (!file.exists()) {
    		throw new IOException("FAILED to find file: " + file.getCanonicalPath());
    	}

    	FileInputStream fis = new FileInputStream(file);
    	try {
    		return getDataAsByteArray(fis);
    	} finally {
    		fis.close();
    	}

	}

	public static void write(String fileName, byte [] data) throws IOException {

    	File file = new File(fileName);
    	if (!file.exists() && !file.createNewFile()) {
    		throw new IOException("FAILED to create file: " + file.getCanonicalPath());
    	}

    	FileOutputStream fos = new FileOutputStream(file);
    	try {
    		fos.write(data);
    	} finally {
    		fos.close();
    	}

	}

}
