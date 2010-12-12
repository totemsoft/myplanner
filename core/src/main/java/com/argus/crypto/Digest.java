/*
 * Digest.java
 *
 * Created on 4 August 2004, 08:27
 */

package com.argus.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.argus.util.StringUtils;

/**
 *
 * @author  Administrator
 */
public class Digest {
    
    public static final String MD5 = "MD5";


    /** Creates a new instance of Digest */
    private Digest() {}
    
    public static String digest( String value ) 
	{ 
		try {
            return digest( value, MD5 );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
	}

    public static String digest( String value, String algorithm ) 
        throws
            NoSuchAlgorithmException
    { 
        MessageDigest md = MessageDigest.getInstance( algorithm );
        
        byte [] output = md.digest( value.getBytes() );
        md.reset();
        
        //logger.info(value + ":" + toHexString( output ));
        return StringUtils.toHexString( output );
    
    }

}
