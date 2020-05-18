/*
 * BaseFinancialsView.java
 *
 * Created on 4 August 2003, 15:58
 */

package com.argus.financials.ui;

/**
 *
 * @author  Valeri Shibaev
 */

import com.argus.swing.About;
import com.argus.swing.BasePanel;
import com.argus.swing.NumberInputVerifier;
import com.argus.swing.WindowFactory;

public class BaseFinancialsView extends BasePanel {
    
    public static final String TITLE = "Argus Software Financials";
    
    public static final String LOGO_IMAGE = "/com/argus/images/logo/financials.gif";
    
    //public static final String ABOUT_FILE = "com/argus/financials/about.html";
    public static final String ABOUT_TEXT =
        "<HTML>" +
        "<H1 STYLE=\"text-decoration: none\"><FONT SIZE=4>Argus Software Financials</FONT></H1>" +
        "<P>Release date: 06 August 2003<BR>Release Version: 0.1.1<BR>Release Name: financials.0.1.1</P>" +
        "<H2><FONT SIZE=4><I>Contacts</I></FONT></H2>" +
        "<P>Web Site: <A HREF=\"http://www.argussoftware.net/\">www.argussoftware.net</A>" +
        "<BR>E-Mail: <A HREF=\"mailto:mail@argussoftware.net\">mail@argussoftware.net</A></P>" +
        "</HTML>"
        ;

    protected static final com.argus.swing.NumberInputVerifier nif;
    static {
        WindowFactory.setDefaultApplicationIcon( LOGO_IMAGE );

        nif = (NumberInputVerifier) NumberInputVerifier.getInstance();
        //nif = new NumberInputVerifier(4);
    }

 
    protected static void main( String [] args, BaseFinancialsView view ) {

        java.awt.Frame frame = WindowFactory.createFrame( view );
        
        // set menu bar for stand alone application
        ( ( javax.swing.JFrame ) frame ).setJMenuBar( view.jMenuBar );
        
        frame.setTitle( TITLE );
        
        frame.addWindowListener( new java.awt.event.WindowAdapter() {
            public void windowClosing( java.awt.event.WindowEvent e ) {
                System.exit(0);
            }
        } );

        frame.setVisible( true );
        
    }

    
    /** Creates a new instance of BaseFinancialsView */
    protected BaseFinancialsView() {
        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        //setLayout(new java.awt.GridBagLayout());
        setPreferredSize(new java.awt.Dimension(450, 250));
    }
    
    
    private static About about;
    public java.awt.Component getAbout() {
        
        if ( about == null ) {

            java.awt.Container owner = getTopLevelAncestor();

            about = About.createAbout( (java.awt.Window) owner, true );
            about.setTitle( TITLE );
            //try {
                //about.setHTMLMessage( IOUtils.openInputStream( ABOUT_FILE ), true );
                about.setHTMLMessage( ABOUT_TEXT );
            //} catch ( java.io.IOException e ) {
            //    System.err.println( e.getMessage() );
            //}
            about.pack();

        }
        
        return about;
        
    }

}
