package com.argus.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.argus.util.StringUtils;

 
/**
 * This class is responsible for general encryption and decryption of data.
 * It uses the Triple DES Encryption algorithm (DES-EDE). 
 *
 * @author  shibaevv
 **/
public class CryptoCipher {

    private Cipher              _desCipher;
    private KeyGenerator        _keygen;
    private SecretKey           _secret_key;        
    private SecretKeyFactory    _factory;
    private DESedeKeySpec       _keySpec;

    /**
     * Default constructor. Creates a new cipher and a new secret key.
     * 
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.InvalidKeyException
     * @exception javax.crypto.NoSuchPaddingExceptio
     */
    public CryptoCipher() 
    throws 
        java.security.NoSuchAlgorithmException, 
        java.security.NoSuchAlgorithmException, 
        java.security.spec.InvalidKeySpecException, 
        java.security.InvalidKeyException, 
        javax.crypto.NoSuchPaddingException        
    {
        // JCE security provider
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());        
        // Create the cipher
        _desCipher     = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        _factory = SecretKeyFactory.getInstance( "DESede" );

        // init key generator to create a new key
        _keygen        = KeyGenerator.getInstance("DESede");
        // create a secret key
        _secret_key    = _keygen.generateKey();        
        // create secret key key specification 
        _keySpec = (DESedeKeySpec)_factory.getKeySpec( _secret_key, DESedeKeySpec.class );
    }


    /**
     * This constructor creates a new cipher and sets en-/decryption key to the 
     * given secret key. 
     *
     * @param secret_key - a secret key stored in a hexadecimal encoded string
     *
     * @see com.argus.financials.license.CryptoHelper
     *
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.InvalidKeyException
     * @exception javax.crypto.NoSuchPaddingExceptio
     */
    public CryptoCipher(String secret_key) 
    throws 
        java.security.NoSuchAlgorithmException, 
        java.security.NoSuchAlgorithmException, 
        java.security.spec.InvalidKeySpecException, 
        java.security.InvalidKeyException, 
        javax.crypto.NoSuchPaddingException        
    {
        // JCE security provider
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());        
        // Create the cipher
        _desCipher     = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        _factory = SecretKeyFactory.getInstance( "DESede" );

        set_secret_key( secret_key );
    }
    
    
    /**
     * Transforms the currently used secret key object into a hexadecimal string.
     *
     * @return secret_key - a cipher key transformed into a hexadecimal string
     * 
     * @see com.argus.financials.license.CryptoHelper
     *
     * @exception java.security.NoSuchAlgorithmException,
     * @exception java.security.spec.InvalidKeySpecException
     **/
    public String get_secret_key()
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException
    {
        _keySpec = (DESedeKeySpec)_factory.getKeySpec( _secret_key, DESedeKeySpec.class );

        byte[] keyBytes = _keySpec.getKey();
        
        return StringUtils.toHexString( keyBytes );
    }    

    /**
     * Transforms a given secret key string from hexadecimal into a SecretKey and sets it.
     * The secret key is used to encrypt and decrypt data.
     *
     * @param secret_key - a hexadecimal string which contains a cipher key
     *
     * @see com.argus.financials.license.CryptoHelper
     *
     * @exception java.security.NoSuchAlgorithmException
     * @exception java.security.spec.InvalidKeySpecException
     * @exception java.security.InvalidKeyException
     */
    public void set_secret_key(String secret_key)
    throws
        java.security.NoSuchAlgorithmException,
        java.security.spec.InvalidKeySpecException,
        java.security.InvalidKeyException
    {   
        // transform hexadecimal String into byte array
        byte[] keyBytes = StringUtils.fromHexString( secret_key );
               
        // reproduce DESede key from byte array
        _keySpec        = new DESedeKeySpec( keyBytes );
        _secret_key     = _factory.generateSecret( _keySpec );
    }

    
    /**
     * Sets the secret key which is used to encrypt and decrypt data.
     *
     * @param secret_key - a hexadecimal string which contains a cipher key
     */
     public void set_secret_key(SecretKey secret_key)
     {
         _secret_key = secret_key;
     }

     
    /**
     * Generates a new secret key to en- and decrypt licence keys.
     *
     * @return secret_key - a cipher key
     *
     * @see com.argus.financials.license.CryptoHelper
     *
     * @exception java.security.NoSuchAlgorithmException
     */
    public SecretKey createSecretKey()
    throws
        java.security.NoSuchAlgorithmException    
    {  
        _keygen        = KeyGenerator.getInstance("DESede");
        _secret_key    = _keygen.generateKey();        
        
        return _secret_key;
    }

    
    /**
     * Encrypt data with Triple DES algorithm.
     *
     * @param data - a byte array which contains 'cleartext' data
     * @return the encrypted data as a byte array
     *
     * @exception javax.crypto.IllegalBlockSizeException,
     * @exception javax.crypto.BadPaddingException,
     * @exception java.security.InvalidKeyException
     */    
    public byte[] encrypt(byte[] data)
    throws
        javax.crypto.IllegalBlockSizeException,
        javax.crypto.BadPaddingException,
        java.security.InvalidKeyException        
    {
        // Initialize the cipher for encryption
        _desCipher.init(Cipher.ENCRYPT_MODE, _secret_key);        
        
        // encrypt the data
        return _desCipher.doFinal( data );
    }
    

    /**
     * Decrypt data with Triple DES algorithm.
     *
     * @param data - a byte array which contains the encrypted data
     * @return the decrypted data as a byte array
     *
     * @exception javax.crypto.IllegalBlockSizeException,
     * @exception javax.crypto.BadPaddingException,
     * @exception java.security.InvalidKeyException
     */
    public byte[] decrypt(byte[] data)
    throws
        javax.crypto.IllegalBlockSizeException,
        javax.crypto.BadPaddingException,
        java.security.InvalidKeyException
    {
        // Initialize the same cipher for decryption
        _desCipher.init(Cipher.DECRYPT_MODE, _secret_key);        
        
        // encrypt the data
        return _desCipher.doFinal( data );
    }       
}
