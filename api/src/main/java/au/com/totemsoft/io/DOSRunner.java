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

package au.com.totemsoft.io;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 
 */

public class DOSRunner {

    private static DOSRunner dosRunner;
    
    /** Creates new DOSRunner */
    private DOSRunner() {
    }

    public static DOSRunner getInstance() {
        if ( dosRunner == null )
            dosRunner = new DOSRunner();
        return dosRunner;
    }
    
    
    /**
     *
     */
    public Process run( String [] cmd ) throws java.io.IOException {
        if ( cmd == null || cmd.length < 3 ) return null;
        return Runtime.getRuntime().exec( cmd );
    }

    public Process run( String cmd, String params ) throws java.io.IOException {
        if ( cmd == null || cmd.length() == 0 ) return null;
        return run( new String [] { "cmd.exe", params, cmd } );
    }

    
    /**
     *
     */
    public void output( Process process, java.io.OutputStream out ) throws java.io.IOException {
        
        if ( process == null || out == null ) return;
        
        java.io.BufferedReader in = 
            new java.io.BufferedReader(
                new java.io.InputStreamReader( process.getInputStream() ) );

        String s = null;
        while( ( s = in.readLine() ) != null ) {
            out.write( s.getBytes() );
        }
        out.flush();
        
    }
    
}
