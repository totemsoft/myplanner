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

import com.argus.util.StringUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 */

//import com.sun.crypto.provider.SunJCE;

public class CryptoUtils {

    public static final String MD5      = "MD5";

    public static final String DH       = "DH";
    public static final String DH_NAME  = "Diffie-Hellman";

    
//    private static SunJCE jce;
//    static {
//        // Add SunJCE to the list of providers
//        jce = new SunJCE();
//        java.security.Security.addProvider(jce);
//    }

    
    private String algorithm;
    private java.security.spec.AlgorithmParameterSpec aps;
    
    /** Creates new CryptoUtils */
    public CryptoUtils( String algorithm )
            throws
                java.security.NoSuchAlgorithmException
                , java.security.spec.InvalidParameterSpecException
    {
        this.algorithm = algorithm;
        init();
    }

    private static CryptoUtils dh;
    public static CryptoUtils getDHInstance()
            throws
                java.security.NoSuchAlgorithmException
                , java.security.spec.InvalidParameterSpecException
    {
        return dh == null ? (dh = new CryptoUtils(DH) ) : dh;
    }
    
    /***************************************************************************
     *                      
     **************************************************************************/
    private void init()
            throws
                java.security.NoSuchAlgorithmException
                , java.security.spec.InvalidParameterSpecException
    {
        if ( DH.equals( algorithm ) ) {
            aps = generateDHParameterSpec();
            
        } else {
            // use some pre-generated, default DH parameters
            System.out.println("Using SKIP " + DH_NAME + " parameters");
            aps = new javax.crypto.spec.DHParameterSpec( skip1024Modulus, skip1024Base );
            
        }
        
    }

    
    /***************************************************************************
     *                      
     **************************************************************************/
    public java.security.KeyPair generateKeyPair()
            throws
                java.security.NoSuchAlgorithmException
                , java.security.InvalidAlgorithmParameterException
    {
        return generateKeyPair( aps, algorithm );
    }

    public java.security.KeyPair generateKeyPair(
            java.security.spec.AlgorithmParameterSpec aps )
            throws
                java.security.NoSuchAlgorithmException
                , java.security.InvalidAlgorithmParameterException
    {
        return generateKeyPair( aps, algorithm );
    }

    public javax.crypto.KeyAgreement generateAgreement( java.security.KeyPair keyPair )
            throws
                java.security.NoSuchAlgorithmException
                , java.security.InvalidKeyException
    {
        javax.crypto.KeyAgreement ka = javax.crypto.KeyAgreement.getInstance(algorithm);
        ka.init( keyPair.getPrivate() );
        return ka;
    }
    
    public java.security.PublicKey generatePublic( java.security.KeyPair keyPair )
            throws
                java.security.NoSuchAlgorithmException
                , java.security.spec.InvalidKeySpecException
    {
        return
            java.security.KeyFactory.getInstance(algorithm).generatePublic( 
                new java.security.spec.X509EncodedKeySpec( keyPair.getPublic().getEncoded() ) );
    }
    
    
    /***************************************************************************
     *                      static methods
     **************************************************************************/
    public static java.security.KeyPair generateKeyPair( 
            java.security.spec.AlgorithmParameterSpec aps, String algorithm )
            throws
                java.security.NoSuchAlgorithmException
                , java.security.InvalidAlgorithmParameterException
    {
        
        java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance( algorithm );
        kpg.initialize( aps );
        return kpg.generateKeyPair();

    }
    
    public static javax.crypto.spec.DHParameterSpec generateDHParameterSpec()
            throws
                java.security.NoSuchAlgorithmException
                , java.security.spec.InvalidParameterSpecException
    {
        // Some central authority creates new DH parameters
        System.out.println("Creating " + DH_NAME + " parameters (takes VERY long) ...");
        java.security.AlgorithmParameterGenerator apg = 
            java.security.AlgorithmParameterGenerator.getInstance( DH );
        apg.init(512);
        java.security.AlgorithmParameters params = apg.generateParameters();
        
        return (javax.crypto.spec.DHParameterSpec) params.getParameterSpec( javax.crypto.spec.DHParameterSpec.class );
        
    }
    
    public static javax.crypto.Cipher readCipher( String source, String transformation )
            throws
                java.io.IOException
                , java.security.NoSuchAlgorithmException
                , javax.crypto.NoSuchPaddingException
    {
        java.io.FileInputStream fis = new java.io.FileInputStream( source );
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance( transformation );
        javax.crypto.CipherInputStream cis = new javax.crypto.CipherInputStream( fis, cipher );
        cis.close();
        fis.close();
        
        System.out.println( "SUCCES\tread from: " + source );
        
        return cipher;
    }

    public static void writeCipher( String dest, javax.crypto.Cipher cipher )
            throws
                java.io.IOException
    {
        java.io.FileOutputStream fos = new java.io.FileOutputStream( dest );
        javax.crypto.CipherOutputStream cos = new javax.crypto.CipherOutputStream( fos, cipher );
        cos.flush();
        cos.close();
        fos.close();
        
        System.out.println( "SUCCES\twrite to: " + dest );
    }

    public static String digest( String value, String algorithm ) 
            throws
                java.security.NoSuchAlgorithmException
    { 
        java.security.MessageDigest md = java.security.MessageDigest.getInstance( algorithm );
        
        byte [] output = md.digest( value.getBytes() );
        md.reset();

        //System.out.println(value + ":" + toHexString( output ));
        return StringUtils.toHexString( output );
        
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
