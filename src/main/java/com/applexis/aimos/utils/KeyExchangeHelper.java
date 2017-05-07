package com.applexis.aimos.utils;

import com.applexis.utils.crypto.AESCrypto;
import com.applexis.utils.crypto.RSACrypto;
import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class KeyExchangeHelper {

    private static Map<String, Key> map;

    private static class InstanceHolder {
        private static final KeyExchangeHelper instance = new KeyExchangeHelper();
    }

    public static KeyExchangeHelper getInstance() {
        return InstanceHolder.instance;
    }

    public KeyExchangeHelper() {
        map = new HashMap<>();
    }

    public String generateAESKey(String publicKey) {
        Key key = new AESCrypto().getKey();
        map.put(publicKey, key);
        PublicKey pk = RSACrypto.getPublicKey(publicKey);
        byte[] AESKey = key.getEncoded();
        return Base64.encodeBase64String(RSACrypto.encrypt(pk, AESKey));
    }

    public Key getKey(String publicKey) {
        return map.get(publicKey);
    }

}
