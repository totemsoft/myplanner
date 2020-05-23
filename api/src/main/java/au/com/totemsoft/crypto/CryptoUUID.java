package au.com.totemsoft.crypto;


// This class will make the ID unique between servers as well 

import java.net.InetAddress;

/**
* This class creates a unique user id (UUID) for a computer.
*
* @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
*/
public class CryptoUUID
{
	private String uuid;	// unique user id

	/** Default constructor */
	public CryptoUUID ()
	{
/*            
            String uid = new java.rmi.server.UID().toString();
            uuid = uid.substring(0, uid.lastIndexOf(":"));            
*/
             
	// uuid = InetAddress.getLocalHost().getAddress() + (new UID().toString());
        // uuid = InetAddress.getLocalHost().getHostName();
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append( InetAddress.getLocalHost().getHostName() );
        } catch (java.net.UnknownHostException e) {
            // do nothing here
        }
        buffer.append( java.lang.System.getProperty( "os.name" ) );
        buffer.append( java.lang.System.getProperty( "os.version" ) );
        buffer.append( java.lang.System.getProperty( "os.arch" ) );
        buffer.append( java.lang.System.getProperty( "sun.cpu.isalist" ) );

        uuid = buffer.toString();            
/*
        System.out.println("java.class.path: " + java.lang.System.getProperty( "java.class.path" ) );
        
        java.io.InputStream is = this.getClass().getResourceAsStream("license.txt");
        java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
        
        int c;
        try {
            while ((c = isr.read()) != -1)
                System.out.write(c);
        } catch (java.io.IOException e) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
        } catch (java.lang.NullPointerException e) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
        }        
*/
/*
os.name Operating system name 
os.arch Operating system architecture 
os.version Operating system version 
file.separator File separator ("/" on UNIX) 
path.separator Path separator (":" on UNIX) 
line.separator Line separator ("\n" on UNIX) 
user.name User's account name 
user.home User's home directory 
user.dir User's current working directory 
*/
/*            
        java.util.Properties properties = java.lang.System.getProperties();
	    for (java.util.Enumeration e = properties.propertyNames(); e.hasMoreElements() ;) {
        	System.out.println(e.nextElement());
     	}
*/
    }


    /**
    * Returns the UUID for the current PC.
    *
    * return the uuid
    */
	public String toString ()
	{
		return uuid;
	}

}
