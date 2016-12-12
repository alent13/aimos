package com.applexis.aimos.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class KeyExchangeHelper {

    private static Map<String, Key> map = new HashMap<>();

    static public String generateDESKey(String publicKey) {
        Key key = DESCryptoHelper.generateKey();
        map.put(publicKey, key);
        PublicKey pk = RSACryptoHelper.getPublicKey(publicKey);
        byte[] DESKey = key.getEncoded();
        byte[] res = new byte[pk.getEncoded().length - 11];
        System.arraycopy(DESKey, 0, res, 0, DESKey.length);
        return Base64.encodeBase64String(RSACryptoHelper.encrypt(pk, DESKey));
    }

    public static Key getKey(String publicKey) {
        return map.get(publicKey);
    }

}
