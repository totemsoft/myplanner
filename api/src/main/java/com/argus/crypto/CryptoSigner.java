package com.argus.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * This class is resposible for the creation and validation of signaturs.
 * The signaturs are generated and validated with the DSA algorithm.
 * A public and private key is used by the DSA algorithm to produce a signature.
 *
 * @author  shibaevv
 */
public class CryptoSigner {  
    
    /** 
     * CTOR 
     */
    public CryptoSigner() {
    }


    /**
     * Verifies a DSA signature.
     *
     * @param data - the data to verify
     * @param signature - the signature which is given with the data
     * @param public_key_byte_array - a public key which is used to verify the signature
     * @return true/false, depends on if the calculated signature = given signature
     *
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.NoSuchProviderException
     * @exception java.security.InvalidKeyException
     * @exception java.security.SignatureException
     */    
    public boolean verifySignature(byte[] data, byte[] signature, byte[] public_key_byte_array)
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException,
        java.security.NoSuchProviderException,
        java.security.InvalidKeyException,
        java.security.SignatureException
    {
        X509EncodedKeySpec public_key_spec = new X509EncodedKeySpec(public_key_byte_array);

        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        PublicKey public_key  = keyFactory.generatePublic(public_key_spec);

		// verify data
        return verifySignature(data, signature, public_key);
    }
    
    /**
     * Verifies a DSA signature.
     *
     * @param data - the data to verify
     * @param signature - the signature which is given with the data
     * @param public_key - a public key which is used to verify the signature
     * @return true/false, depends on if the calculated signature = given signature
     *
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.NoSuchProviderException
     * @exception java.security.InvalidKeyException
     * @exception java.security.SignatureException
     */    
    public boolean verifySignature(byte[] data, byte[] signature, PublicKey public_key)
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException,
        java.security.NoSuchProviderException,
        java.security.InvalidKeyException,
        java.security.SignatureException
    {
        /* create a Signature object and initialize it with the public key */
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        sig.initVerify(public_key);

        /* Update and verify the data */
        sig.update(data, 0, data.length);

        return sig.verify( signature );
    }


    /**
     * Create a DSA signature.
     *
     * @param data - the data to sign
     * @param private_key - a private key which is used to create a signature
     * @return the signature for the data
     *
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.NoSuchProviderException
     * @exception java.security.InvalidKeyException
     * @exception java.security.SignatureException
     */
    public byte[] createSignature(byte[] data, byte[] private_key_byte_array)
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException,
        java.security.NoSuchProviderException,
        java.security.InvalidKeyException,
        java.security.SignatureException
    {
        // restore private key from byte array
        PKCS8EncodedKeySpec private_key_spec = new PKCS8EncodedKeySpec(private_key_byte_array);

        KeyFactory keyFactory  = KeyFactory.getInstance("DSA");
        PrivateKey private_key = keyFactory.generatePrivate(private_key_spec);

  		// create signature
        return createSignature(data, private_key);
    }


    /**
     * Create a DSA signature.
     *
     * @param data - the data to sign
     * @param private_key - a private key which is used to create a signature
     * @return the signature for the data
     *
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.NoSuchProviderException
     * @exception java.security.InvalidKeyException
     * @exception java.security.SignatureException
     */
    public byte[] createSignature(byte[] data, PrivateKey private_key)
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException,
        java.security.NoSuchProviderException,
        java.security.InvalidKeyException,
        java.security.SignatureException
    {
        // Create a Signature object and initialize it with the private key 
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign( private_key );

        // Update and sign the data
        dsa.update(data, 0, data.length);

        // Now that all the data to be signed has been read in,
        // generate a signature for it
        byte[] realSig = dsa.sign();
     
        return realSig;
    }


    /**
    * Creates a new DSA key pair (public and private key).
    *
    * @param private_key - object to store the created private key
    * @param public_key - object to store the created public key
    * @param key_length - public and private key length
    *
    * @exception java.security.NoSuchAlgorithmException
    * @exception java.security.spec.InvalidKeySpecException
    * @exception java.security.NoSuchProviderException,
    * @exception java.security.InvalidKeyException,
    * @exception java.security.SignatureException
    */
    public void createKeyPair(PrivateKey private_key, PublicKey public_key, int key_length)
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException,
        java.security.NoSuchProviderException,
        java.security.InvalidKeyException,
        java.security.SignatureException
    {
            /* Generate a key pair */
            KeyPairGenerator 	keyGen = KeyPairGenerator.getInstance( "DSA", "SUN" );
            SecureRandom 		random = SecureRandom.getInstance( "SHA1PRNG", "SUN" );

            keyGen.initialize( key_length, random );

            KeyPair pair = keyGen.generateKeyPair();
            private_key = pair.getPrivate();
            public_key = pair.getPublic();
    }

}
