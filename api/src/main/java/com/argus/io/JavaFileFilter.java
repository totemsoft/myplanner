/*
 * RTFFileFilter.java
 *
 * Created on 14 March 2002, 10:55
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

public class JavaFileFilter 
        extends javax.swing.filechooser.FileFilter 
        implements java.io.FileFilter
{

    public static final String JAVA = "java";
    public static final String DOT_JAVA = ".java";
    
    public boolean accept(java.io.File file) {

        if ( file.isDirectory() )
            return true;

        String extension = IOUtils.getExtension( file );
        if (extension == null)
            return false;

        if ( extension.equals( JAVA ) )
            return true;

        return false;

    }

    public java.lang.String getDescription() {
        return "JAVA file";
    }

}
