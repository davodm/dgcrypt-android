package com.yourname.dgcrypt;

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
