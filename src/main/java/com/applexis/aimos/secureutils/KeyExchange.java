package com.applexis.aimos.secureutils;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class KeyExchange {

    private static Map<String, Key> map = new HashMap<>();

    static public String generateDESKey(String publicKey) {
        Key key = DESCryptoHelper.generateKey();
        map.put(publicKey, key);
        PublicKey pk = RSACryptoHelper.getPublicKey(publicKey);
        return Base64.encodeBase64String(RSACryptoHelper.encrypt(pk, key.getEncoded()));
    }

}
