/*
 * RTFFileFilter.java
 *
 * Created on 14 March 2002, 10:55
 */

package au.com.totemsoft.io;



/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 
 */

public class RTFFileFilter extends javax.swing.filechooser.FileFilter {

    public final static String RTF = "rtf";
    public final static String DOT_RTF = "." + RTF;
    
    public boolean accept(java.io.File file) {

        if ( file.isDirectory() )
            return true;

        String extension = IOUtils.getExtension( file );
        return extension != null && extension.equals( RTF );

    }

    public java.lang.String getDescription() {
        return "RTF Document";
    }

}
