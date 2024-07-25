package com.davodm.dgcrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

/**
 * Dgcrypt class
 * Provides methods to securely encrypt and decrypt strings using AES-256-CBC.
 */
public class Dgcrypt {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private String iv;
    private String key;

    /**
     * Sets the secret key for encryption and decryption.
     * 
     * @param key The secret key (must be 32 characters)
     * @throws Exception if the key length is not 32 characters
     */
    public void setKey(String key) throws Exception {
        if (key.length() != 32) {
            throw new Exception("Secret key should be 32 characters");
        }
        this.key = key;
    }

    /**
     * Sets the initialization vector (IV) for encryption.
     * If no IV is provided, a secure random IV is generated.
     * 
     * @param iv The IV (must be 16 bytes)
     * @throws Exception if the IV length is not 16 bytes
     */
    public void setIV(String iv) throws Exception {
        if (iv == null || iv.isEmpty()) {
            this.iv = generateRandomIV();
        } else {
            if (iv.length() != 16) {
                throw new Exception("IV should be 16 bytes");
            }
            this.iv = iv;
        }
    }

    /**
     * Encrypts a given string using AES-256-CBC.
     * 
     * @param data The input string to encrypt
     * @param secretKey Optional secret key for encryption
     * @param resetIV Whether to reset the IV after encryption
     * @return The encrypted string, base64 encoded
     * @throws Exception if the secret key is not defined or encryption fails
     */
    public String encrypt(String data, String secretKey, boolean resetIV) throws Exception {
        if (secretKey != null && !secretKey.isEmpty()) {
            setKey(secretKey);
        } else if (key == null || key.isEmpty()) {
            throw new Exception("Secret key is not defined");
        }

        if (iv == null || iv.isEmpty()) {
            setIV(null);
        }

        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        String encryptedString = Base64.encodeToString(iv.getBytes(), Base64.DEFAULT) + Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        if (resetIV) {
            iv = null;
        }

        return encryptedString;
    }

    /**
     * Decrypts a given string using AES-256-CBC.
     * 
     * @param encrypted The encrypted string to decrypt (base64 encoded)
     * @param secretKey Optional secret key for decryption
     * @return The decrypted string
     * @throws Exception if the secret key is not defined, the encoded string is corrupted, or decryption fails
     */
    public String decrypt(String encrypted, String secretKey) throws Exception {
        if (secretKey != null && !secretKey.isEmpty()) {
            setKey(secretKey);
        } else if (key == null || key.isEmpty()) {
            throw new Exception("Key for decrypting is not defined");
        }

        byte[] decodedBytes = Base64.decode(encrypted, Base64.DEFAULT);
        if (decodedBytes.length <= 16) {
            throw new Exception("Encoded string is manipulated or corrupted");
        }

        byte[] ivBytes = new byte[16];
        System.arraycopy(decodedBytes, 0, ivBytes, 0, 16);
        byte[] encryptedBytes = new byte[decodedBytes.length - 16];
        System.arraycopy(decodedBytes, 16, encryptedBytes, 0, decodedBytes.length - 16);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    /**
     * Generates a secure random IV.
     * 
     * @return A secure random IV
     * @throws Exception if the IV generation fails
     */
    private String generateRandomIV() throws Exception {
        byte[] ivBytes = new byte[16];
        java.security.SecureRandom.getInstanceStrong().nextBytes(ivBytes);
        return new String(ivBytes);
    }
}
