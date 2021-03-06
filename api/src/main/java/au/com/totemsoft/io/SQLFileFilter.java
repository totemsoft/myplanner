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

public class SQLFileFilter extends javax.swing.filechooser.FileFilter {

    public final static String SQL = "sql";
    public final static String DOT_SQL = "." + SQL;
    
    public boolean accept(java.io.File file) {

        if ( file.isDirectory() )
            return true;

        String extension = IOUtils.getExtension( file );
        return extension != null && extension.equals( SQL );

    }

    public java.lang.String getDescription() {
        return "SQL file";
    }

}
