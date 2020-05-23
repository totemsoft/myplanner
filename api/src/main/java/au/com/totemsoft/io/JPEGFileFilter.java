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

public class JPEGFileFilter extends javax.swing.filechooser.FileFilter {

    public final static String JPEG = "jpeg";
    public final static String DOT_JPEG = "." + JPEG;
    public final static String JPG = "jpg";
    public final static String DOT_JPG = "." + JPG;
    
    public boolean accept(java.io.File file) {

        if ( file.isDirectory() )
            return true;

        String extension = IOUtils.getExtension( file );
        if (extension == null)
            return false;

        if ( extension.equals( JPEG ) )
            return true;
        if ( extension.equals( JPG ) )
            return true;

        return false;

    }

    public java.lang.String getDescription() {
        return "JPEG Image";
    }

}
