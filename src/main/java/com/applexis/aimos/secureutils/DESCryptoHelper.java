package com.applexis.aimos.secureutils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author applexis
 */

public class DESCryptoHelper {

    public static Key getKey(String keyString) {
        byte[] encodedKey = Base64.decodeBase64(keyString);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
    }

    public static String getKeyString(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static Key getKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "DES");
    }

    public static Key generateKey() {
        KeyGenerator generator = null;
        Key key = null;
        try {
            generator = KeyGenerator.getInstance("DES");
            generator.init(56, new SecureRandom());
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }

    public static String encrypt(Key key, String message) {
        Cipher cipher = null;
        String str = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] stringBytes = message.getBytes("UTF8");
            byte[] raw = cipher.doFinal(stringBytes);
            str = Base64.encodeBase64String(raw);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String decrypt(Key key, String encrypted) {
        Cipher cipher = null;
        String str = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] raw = Base64.decodeBase64(encrypted);
            byte[] stringBytes = cipher.doFinal(raw);
            str = new String(stringBytes, "UTF8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static byte[] encrypt(Key key, byte[] message) {
        Cipher cipher = null;
        byte[] res = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            res = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] decrypt(Key key, byte[] encrypted) {
        Cipher cipher = null;
        byte[] res = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            res = cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return res;
    }

}
