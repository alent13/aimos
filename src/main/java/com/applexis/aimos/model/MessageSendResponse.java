package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Message;
import com.applexis.utils.crypto.AESCrypto;

public class MessageSendResponse extends ResponseBase {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        BAD_HASH,
        INCORRECT_TOKEN,
        INCORRECT_ID,
        DATABASE_ERROR
    }

    private String id;

    public MessageSendResponse(AESCrypto aes) {
        super(aes);
    }

    public MessageSendResponse(String errorType, AESCrypto aes) {
        super(errorType, aes);
    }

    public MessageSendResponse(Message message, AESCrypto aes) {
        if (message != null) {
            this.id = aes.encrypt(String.valueOf(message.getId()));
            success = aes.encrypt(String.valueOf(true));
        } else {
            success = aes.encrypt(String.valueOf(false));
        }
    }

    public Long getId(AESCrypto aes) {
        return Long.getLong(aes.decrypt(id));
    }

    public void setId(Long id, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(id));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
