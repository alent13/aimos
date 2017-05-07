package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Message;
import com.applexis.utils.crypto.AESCrypto;

import java.util.Date;

/**
 * @author applexis
 */

public class MessageMinimal {

    private String idUserFrom;
    private String eText;
    private String key;
    private String signature;
    private String publicKey;
    private String datetime;

    public MessageMinimal(Long idUserFrom, String eText, String key,
                          String signature, String publicKey,
                          Date datetime, AESCrypto aes) {
        this.idUserFrom = aes.encrypt(String.valueOf(idUserFrom));
        this.eText = aes.encrypt(eText);
        this.key = aes.encrypt(key);
        this.signature = aes.encrypt(signature);
        this.publicKey = aes.encrypt(publicKey);
        this.datetime = aes.encrypt(String.valueOf(datetime.getTime()));
    }

    public MessageMinimal(Message message, String signature, String publicKey, AESCrypto aes) {
        this.idUserFrom = aes.encrypt(String.valueOf(message.getSender().getId()));
        this.eText = aes.encrypt(message.getMessageText());
        this.key = aes.encrypt(message.getKey());
        this.signature = aes.encrypt(signature);
        this.publicKey = aes.encrypt(publicKey);
        this.datetime = aes.encrypt(String.valueOf(message.getDatetime().getTime()));
    }

    public Long getIdUserFrom(AESCrypto aes) {
        return Long.getLong(aes.decrypt(idUserFrom));
    }

    public void setIdUserFrom(Long idUserFrom, AESCrypto aes) {
        this.idUserFrom = aes.encrypt(String.valueOf(idUserFrom));
    }

    public String geteText(AESCrypto aes) {
        return aes.decrypt(eText);
    }

    public void seteText(String eText, AESCrypto aes) {
        this.eText = aes.encrypt(eText);
    }

    public String getKey(AESCrypto aes) {
        return aes.decrypt(key);
    }

    public void setKey(String key, AESCrypto aes) {
        this.key = aes.encrypt(key);
    }

    public String getSignature(AESCrypto aes) {
        return aes.decrypt(signature);
    }

    public void setSignature(String signature, AESCrypto aes) {
        this.signature = aes.encrypt(signature);
    }

    public String getPublicKey(AESCrypto aes) {
        return aes.decrypt(publicKey);
    }

    public void setPublicKey(String publicKey, AESCrypto aes) {
        this.publicKey = aes.encrypt(publicKey);
    }

    public Date getDatetime(AESCrypto aes) {
        return new Date(Long.getLong(aes.decrypt(datetime)));
    }

    public void setDatetime(Date datetime, AESCrypto aes) {
        this.datetime = aes.encrypt(String.valueOf(datetime.getTime()));
    }

    public String getIdUserFrom() {
        return idUserFrom;
    }

    public void setIdUserFrom(String idUserFrom) {
        this.idUserFrom = idUserFrom;
    }

    public String geteText() {
        return eText;
    }

    public void seteText(String eText) {
        this.eText = eText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
