package com.applexis.aimos.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author applexis
 */

public class DSACryptoHelper {

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            keyGen.initialize(1024, random);

            return keyGen.generateKeyPair();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] generateSignature(PrivateKey priv, byte[] info) {
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA");
            dsa.initSign(priv);
            dsa.update(info);
            return dsa.sign();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean verifySignature(PublicKey pub, byte[] data, byte[] signature) {
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA");
            dsa.initVerify(pub);
            dsa.update(data);
            return dsa.verify(signature);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String getPublicKeyString(PublicKey key) {
        KeyFactory kf = null;
        String str = null;
        try {
            kf = KeyFactory.getInstance("DSA");
            str = Base64.encode(kf.getKeySpec(key, X509EncodedKeySpec.class).getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static PublicKey getPublicKey(String key) {
        PublicKey publicKey = null;
        try {
            byte[] byteKey = Base64.decode(key);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("DSA");
            publicKey = kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

}
