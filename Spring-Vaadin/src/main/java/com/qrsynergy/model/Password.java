package com.qrsynergy.model;

import org.apache.commons.codec.binary.Base64;


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Used for storing password and salt in db
 */
public  class Password {

    /**
     * Generate salt value
     * @return byte[] of salt
     */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Byte[] to string to save it in the database
     * @param input byte[] of the hashed password
     * @return string representation of hashed password
     */
    public static String bytetoString(byte[] input) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(input);
    }

    /**
     * Hashes password, returns byte[]
     * @param password password to be hashed
     * @param salt generated salt value
     * @return byte[] of hashed password
     */
    public static byte[] hashPassword( final char[] password, final byte[] salt)
    {
        int iterations = 1000;
        int keyLength = 256;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;
        }
        catch( NoSuchAlgorithmException | InvalidKeySpecException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static byte[] stringToByte(String input)
    {
        if (Base64.isBase64(input))
        {
            return Base64.decodeBase64(input);
        }
        else
        {
            return Base64.encodeBase64(input.getBytes());
        }
    }

}
