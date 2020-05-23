/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.crypto;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 *							  $Date: 2006/02/23 21:46:36 $
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public final class CryptoPassword {

    private static final Logger logger = Logger.getLogger(CryptoPassword.class.getName());

    /*
     * PBEWith<digest>And<encryption> or PBEWith<prf>And<encryption>: 
     * The password-based encryption algorithm (PKCS #5), 
     * using the specified message digest (<digest>) or 
     * pseudo-random function (<prf>) and encryption algorithm (<encryption>).
     */
    
    //PBEWithMD5AndDES: The password-based encryption algorithm as defined in: 
    //RSA Laboratories, "PKCS #5: Password-Based Encryption Standard," version 1.5, Nov 1993. 
    //Note that this algorithm implies CBC as the cipher mode and PKCS5Padding 
    //as the padding scheme and cannot be used with any other cipher modes or padding schemes.
    //PBEWithHmacSHA1AndDESede: The password-based encryption algorithm as defined in: 
    //RSA Laboratories, "PKCS #5: Password-Based Cryptography Standard," version 2.0, March 1999.
    private static final String PBEA = "PBEWithMD5AndDES";
    //private static final String PBEA = "PBEWithHmacSHA1AndDESede";
    
    // Salt
    //private static final byte [] SALT = 
    //	{(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c, (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99};
    private static final byte [] skip1024ModulusBytes = 
    	{(byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58, (byte)0x4E, (byte)0x49, (byte)0xDB, (byte)0xCD};

    // Salt
    private byte [] salt;
    // Iteration count
    private int count;

    /**
     *  
     */
    public CryptoPassword(int count) {
        this(skip1024ModulusBytes, count);
    }
    public CryptoPassword(byte [] salt, int count) {
        this.salt = salt;
        this.count = count;
    }

    /**
     * 
     * @param cleartext
     * @return Encrypt the cleartext
     * @throws CryptoException
     */
    byte [] encrypt(byte [] cleartext) 
    	throws CryptoException
	{
        try {
            System.out.print("Enter password: ");
            System.out.flush();
            char [] password = readPasswd(System.in);
            password = digest(password);
            
            return encrypt(cleartext, password);
        } catch (IOException ex) {
            throw new CryptoException(ex);
        }
	}

    /**
     * encryption password = readPasswd(System.in)
     * @param password
     * @return Encrypt the cleartext
     * @throws CryptoException
     */
    public byte [] encrypt(byte [] cleartext, char [] password) 
    	throws CryptoException 
    {
        try {
            // Create PBE parameter set
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);

            // convert encryption password into a SecretKey object, 
            // using a PBE key factory.
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
            Arrays.fill(password, ' '); // clear password
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance(PBEA);
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

            // Create PBE Cipher
            Cipher pbeCipher = Cipher.getInstance(PBEA);

            // Initialize PBE Cipher with key and parameters
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

            // Encrypt the cleartext
            return pbeCipher.doFinal(cleartext);
            
        } catch (InvalidKeyException ex) {
            throw new CryptoException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex);
        } catch (InvalidKeySpecException ex) {
            throw new CryptoException(ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoException(ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new CryptoException(ex);
        } catch (IllegalStateException ex) {
            throw new CryptoException(ex);
        } catch (IllegalBlockSizeException ex) {
            throw new CryptoException(ex);
        } catch (BadPaddingException ex) {
            throw new CryptoException(ex);
        }    
        
    }
    
    /**
     * 
     * @param ciphertext
     * @return Dencrypt the ciphertext
     * @throws CryptoException
     */
    byte [] decrypt(byte [] ciphertext) 
    	throws CryptoException
	{
        try {
            System.out.print("Enter encryption password: ");
            System.out.flush();
            char [] password = readPasswd(System.in);
            password = digest(password);
            
            return decrypt(ciphertext, password);
        } catch (IOException ex) {
            throw new CryptoException(ex);
        }
	}

    /**
     * decryption password = readPasswd(System.in)
     * @param password
     * @return Dencrypt the ciphertext
     * @throws CryptoException
     */
    public byte [] decrypt(byte [] ciphertext, char [] password) 
    	throws CryptoException 
    {
        try {
	        // Create PBE parameter set
	        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
	
	        // convert encryption password into a SecretKey object, 
	        // using a PBE key factory.
	        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
	        Arrays.fill(password, ' '); // clear password
	        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(PBEA);
	        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
	
	        // Create PBE Cipher
	        Cipher pbeCipher = Cipher.getInstance(PBEA);
	
	        // Initialize PBE Cipher with key and parameters
	        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
	
	        // Encrypt the cleartext
	        return pbeCipher.doFinal(ciphertext);
	        
        } catch (InvalidKeyException ex) {
            throw new CryptoException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex);
        } catch (InvalidKeySpecException ex) {
            throw new CryptoException(ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoException(ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new CryptoException(ex);
        } catch (IllegalStateException ex) {
            throw new CryptoException(ex);
        } catch (IllegalBlockSizeException ex) {
            throw new CryptoException(ex);
        } catch (BadPaddingException ex) {
            throw new CryptoException(ex);
        }    
        
    }
    
    /**
     * The following method is an example of how to collect a user password as a
     * char array: Reads user password from given input stream.
     */
    private char[] readPasswd(InputStream in) 
    	throws IOException 
    {
        char [] lineBuffer = new char[128];
        char [] buf = lineBuffer;

        int room = buf.length;
        int offset = 0;
        int c;

        boolean loop = true;
        while (loop) {
            switch (c = in.read()) {
	            case -1:
	            case '\n': {
	                loop = false;
	                break;// loop;
	            }
	
	            case '\r':
	                int c2 = in.read();
	                if ((c2 != '\n') && (c2 != -1)) {
	                    if (!(in instanceof PushbackInputStream)) {
	                        in = new PushbackInputStream(in);
	                    }
	                    ((PushbackInputStream) in).unread(c2);
	                } else {
	                    loop = false;
	                    break;// loop;
	                }
	
	            default:
	                if (--room < 0) {
	                    buf = new char[offset + 128];
	                    room = buf.length - offset - 1;
	                    System.arraycopy(lineBuffer, 0, buf, 0, offset);
	                    Arrays.fill(lineBuffer, ' ');
	                    lineBuffer = buf;
	                }
	                buf[offset++] = (char) c;
	                break;
            }
        }

        if (offset == 0) {
            return null;
        }

        char[] ret = new char[offset];
        System.arraycopy(buf, 0, ret, 0, offset);
        Arrays.fill(buf, ' ');

        //logger.info("Password: [" + new String(ret) + "]");
        
        return ret;
        
    }

    private char [] digest(char [] text) 
		throws CryptoException
	{
	    try {
	        text = CryptoUtils.digest( new String(text), CryptoUtils.MD5 ).toCharArray();
	        logger.info("Password: [" + new String(text) + "]");
	        return text;
	    } catch (NoSuchAlgorithmException ex) {
	        throw new CryptoException(ex);
	    }
	}

}
