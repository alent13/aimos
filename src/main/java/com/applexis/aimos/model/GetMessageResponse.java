package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Message;
import com.applexis.aimos.utils.DSACryptoHelper;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class GetMessageResponse {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN,
        INCORRECT_ID,
        DATABASE_ERROR
    }

    private List<MessageMinimal> messageMinimals;

    public boolean success;

    public String errorType;

    public GetMessageResponse() {
        this.success = false;
    }

    public GetMessageResponse(String errorType) {
        this.success = false;
        this.errorType = errorType;
    }

    public GetMessageResponse(List<Message> messages) {
        this.messageMinimals = new ArrayList<>();
        if (messages != null) {
            for (Message message : messages) {
                KeyPair keyPair = DSACryptoHelper.generateKeyPair();
                String signature = Base64.encodeBase64String(DSACryptoHelper.generateSignature(keyPair.getPrivate(), message.getMessageText().getBytes()));
                this.messageMinimals.add(
                        new MessageMinimal(message.getSender().getId(),
                                message.getMessageText(),
                                message.getKey(),
                                signature,
                                DSACryptoHelper.getPublicKeyString(keyPair.getPublic()),
                                message.getDatetime())
                );
            }
            this.success = true;
        }
    }

    public List<MessageMinimal> getMessageMinimals() {
        return messageMinimals;
    }

    public void setMessageMinimals(List<MessageMinimal> messageMinimals) {
        this.messageMinimals = messageMinimals;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
