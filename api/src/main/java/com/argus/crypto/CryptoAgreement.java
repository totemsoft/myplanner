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

package com.argus.crypto;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 *							  $Date: 2006/02/23 21:46:36 $
 */

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public final class CryptoAgreement {
	
	private static final Logger logger = Logger.getLogger(CryptoAgreement.class.getName());

	
    private static final String DIFFIE_HELLMAN = "DH";

    
    private String algorithm;
    private KeyAgreement keyAgree;
    private SecretKey secretKey;
    private Cipher cipher;
    
//    static {
//        // Add SunJCE to the list of providers
//        java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
//    }


    /**
     * 
     * @return
     */
    public static CryptoAgreement newDHInstance() 
	    throws
		    NoSuchAlgorithmException
			, NoSuchPaddingException
    {
        return new CryptoAgreement(DIFFIE_HELLMAN);
    }

    
    /**
     * 
     * @param algorithm
     */
    public CryptoAgreement( String algorithm )
	    throws
		    NoSuchAlgorithmException
			, NoSuchPaddingException
	{
	    this.algorithm = algorithm;
	    this.cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	}
    
    
    /***************************************************************************
     * Algorithm specific utility methods
     **************************************************************************/
    private AlgorithmParameterSpec getParameterSpec() {
        // use some pre-generated, default DH parameters
        logger.info("Using SKIP " + algorithm + " parameters");

        if ( DIFFIE_HELLMAN.equals( algorithm ) ) {
            return new DHParameterSpec( skip1024Modulus, skip1024Base );
        }
        
        return null;
    }
    
    private AlgorithmParameterSpec getParameterSpec(PublicKey pubKey) {
        // Gets the DH parameters associated with public key. 
        // We must use the same parameters

    	if ( DIFFIE_HELLMAN.equals( algorithm ) ) {
            return ((DHPublicKey) pubKey).getParams();
        }
        
        return null;
    }
    
    private AlgorithmParameterSpec generateParameterSpec()
	    throws
	        NoSuchAlgorithmException
	        , InvalidParameterSpecException
	{
	    if ( DIFFIE_HELLMAN.equals( algorithm ) ) {
	        // Some central authority creates new DH parameters
	        logger.info("Creating " + algorithm + " parameters ...");
	        AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance( DIFFIE_HELLMAN );
	        apg.init(512);
	        AlgorithmParameters params = apg.generateParameters();
	        logger.info("Creating " + algorithm + " parameters succesfully completed.");
	        
	        return params.getParameterSpec( DHParameterSpec.class );
	    }
	    
	    return null;
	}

    /**
     * Encode public key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws ShortBufferException
     */
    PublicKey getPublicKey()
	    throws
		    NoSuchAlgorithmException
		    , InvalidAlgorithmParameterException
	        , InvalidParameterSpecException
			, InvalidKeySpecException
			, InvalidKeyException
			, ShortBufferException
	{
	    AlgorithmParameterSpec parameterSpec = getParameterSpec();
    	return generatePublicKey(parameterSpec);
	}
    
    /**
     * Encode public key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidParameterSpecException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws ShortBufferException
     */
    PublicKey generatePublicKey()
	    throws
		    NoSuchAlgorithmException
		    , InvalidAlgorithmParameterException
	        , InvalidParameterSpecException
			, InvalidKeySpecException
			, InvalidKeyException
			, ShortBufferException
	{
	    AlgorithmParameterSpec parameterSpec = generateParameterSpec();
		return generatePublicKey(parameterSpec);
	}
    
    /**
     * Encode public key
     * @param paramSpec
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws ShortBufferException
     */
    private PublicKey generatePublicKey(AlgorithmParameterSpec paramSpec)
	    throws
	        NoSuchAlgorithmException
	        , InvalidAlgorithmParameterException
			, InvalidKeySpecException
			, InvalidKeyException
			, ShortBufferException
	{
        // Create our own key pair
        logger.info("Generate keypair ...");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(paramSpec);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        
        // Creates and initialize our DH KeyAgreement object
        logger.info("Initialization ...");
        keyAgree = KeyAgreement.getInstance(algorithm);
        keyAgree.init(keyPair.getPrivate());

        // We encode our public key
        return keyPair.getPublic();
		
	}

    /**
     * 
     * @param pubKeyEnc
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeySpecException
     */
    PublicKey getPublicKey(byte [] pubKeyEnc)
	    throws
	        NoSuchAlgorithmException
	        , InvalidAlgorithmParameterException
			, InvalidKeySpecException
	{
	    // We received public key in encoded format.
	    // We instantiate a DH public key from the encoded key material.
	    KeyFactory keyFac = KeyFactory.getInstance(algorithm);
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyEnc);
	    return keyFac.generatePublic(x509KeySpec);
	}

    /**
     * Instantiate a DH public key from the encoded key material.
     * @param pubKeyEnc
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws ShortBufferException
     */
    PublicKey generatePublicKey(byte [] pubKeyEnc)
	    throws
	        NoSuchAlgorithmException
	        , InvalidAlgorithmParameterException
			, InvalidKeySpecException
			, InvalidKeyException
			, ShortBufferException
	{
        // We received public key in encoded format.
        // We instantiate a DH public key from the encoded key material.
        PublicKey pubKey = getPublicKey(pubKeyEnc);
	
        // Gets the parameters associated with public key. 
        // We must use the same parameters
        AlgorithmParameterSpec paramSpec = getParameterSpec(pubKey);

		return generatePublicKey(paramSpec);
	}

    /**
     * 
     * @param pubKeyEnc
     * @param alg
     * @return wrapped generated secretKey
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     */
    byte [] generateSecret(byte [] pubKeyEnc) 
	    throws
		    NoSuchAlgorithmException
		    , InvalidAlgorithmParameterException
			, InvalidKeySpecException
			, InvalidKeyException
			, IllegalBlockSizeException
			, NoSuchPaddingException
    {
    	// uses public key for the first (and only) phase
        // of version of the algorithm protocol.
        PublicKey pubKey = getPublicKey(pubKeyEnc);
        keyAgree.doPhase(pubKey, true);

        // we completed the DH key agreement protocol.
        // generate the shared secret.
        secretKey = keyAgree.generateSecret("DES");
        
	    cipher.init(Cipher.WRAP_MODE, secretKey);
        return cipher.wrap(secretKey);
        
    }
    
    /**
     * 
     * @param cleartext
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     */
    public byte [] encrypt(byte [] cleartext)
	    throws
	        NoSuchAlgorithmException
			, InvalidKeyException
			, IllegalStateException
			, IllegalBlockSizeException
			, NoSuchPaddingException
			, BadPaddingException
	{
	    // encrypt, using DES in ECB mode
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	    // ciphertext
	    return cipher.doFinal(cleartext);

	}

    /**
     * 
     * @param ciphertext
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     */
    public byte [] decrypt(byte [] ciphertext)
	    throws
	        NoSuchAlgorithmException
			, InvalidKeyException
			, IllegalBlockSizeException
			, NoSuchPaddingException
			, BadPaddingException
	{
        // decrypt, using DES in ECB mode
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(ciphertext);
	
	}
    
    /**
     * 
     * @param ciphertext
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     */
    public byte [] decrypt(byte [] ciphertext, byte [] wrappedKey)
	    throws
	        NoSuchAlgorithmException
			, InvalidKeyException
			, IllegalBlockSizeException
			, NoSuchPaddingException
			, BadPaddingException
	{
//    	SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DES");
//    	KeySpec keySpec = new DESKeySpec(wrappedKey);
//        SecretKey secKey = keyFac.generateSecret(keySpec);
        
    	// first initialize a Cipher object for UNWRAP_MODE
        cipher.init(Cipher.UNWRAP_MODE, secretKey);
        secretKey = (SecretKey) cipher.unwrap(wrappedKey, cipher.getAlgorithm(), Cipher.SECRET_KEY);
        
        return decrypt(ciphertext);
	
	}
        
    /***************************************************************************
     *                      private CONSTANTS
     **************************************************************************/
    
    // The 1024 bit Diffie-Hellman modulus values used by SKIP
    private static final byte skip1024ModulusBytes[] = {
        (byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58,
        (byte)0x4E, (byte)0x49, (byte)0xDB, (byte)0xCD,
        (byte)0x20, (byte)0xB4, (byte)0x9D, (byte)0xE4,
        (byte)0x91, (byte)0x07, (byte)0x36, (byte)0x6B,
        (byte)0x33, (byte)0x6C, (byte)0x38, (byte)0x0D,
        (byte)0x45, (byte)0x1D, (byte)0x0F, (byte)0x7C,
        (byte)0x88, (byte)0xB3, (byte)0x1C, (byte)0x7C,
        (byte)0x5B, (byte)0x2D, (byte)0x8E, (byte)0xF6,
        (byte)0xF3, (byte)0xC9, (byte)0x23, (byte)0xC0,
        (byte)0x43, (byte)0xF0, (byte)0xA5, (byte)0x5B,
        (byte)0x18, (byte)0x8D, (byte)0x8E, (byte)0xBB,
        (byte)0x55, (byte)0x8C, (byte)0xB8, (byte)0x5D,
        (byte)0x38, (byte)0xD3, (byte)0x34, (byte)0xFD,
        (byte)0x7C, (byte)0x17, (byte)0x57, (byte)0x43,
        (byte)0xA3, (byte)0x1D, (byte)0x18, (byte)0x6C,
        (byte)0xDE, (byte)0x33, (byte)0x21, (byte)0x2C,
        (byte)0xB5, (byte)0x2A, (byte)0xFF, (byte)0x3C,
        (byte)0xE1, (byte)0xB1, (byte)0x29, (byte)0x40,
        (byte)0x18, (byte)0x11, (byte)0x8D, (byte)0x7C,
        (byte)0x84, (byte)0xA7, (byte)0x0A, (byte)0x72,
        (byte)0xD6, (byte)0x86, (byte)0xC4, (byte)0x03,
        (byte)0x19, (byte)0xC8, (byte)0x07, (byte)0x29,
        (byte)0x7A, (byte)0xCA, (byte)0x95, (byte)0x0C,
        (byte)0xD9, (byte)0x96, (byte)0x9F, (byte)0xAB,
        (byte)0xD0, (byte)0x0A, (byte)0x50, (byte)0x9B,
        (byte)0x02, (byte)0x46, (byte)0xD3, (byte)0x08,
        (byte)0x3D, (byte)0x66, (byte)0xA4, (byte)0x5D,
        (byte)0x41, (byte)0x9F, (byte)0x9C, (byte)0x7C,
        (byte)0xBD, (byte)0x89, (byte)0x4B, (byte)0x22,
        (byte)0x19, (byte)0x26, (byte)0xBA, (byte)0xAB,
        (byte)0xA2, (byte)0x5E, (byte)0xC3, (byte)0x55,
        (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC7
    };

    // The SKIP 1024 bit modulus
    private static final java.math.BigInteger skip1024Modulus = 
        new java.math.BigInteger(1, skip1024ModulusBytes);

    // The base used with the SKIP 1024 bit modulus
    private static final java.math.BigInteger skip1024Base = 
        java.math.BigInteger.valueOf(2);

}
