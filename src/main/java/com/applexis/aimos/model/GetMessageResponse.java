package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Message;
import com.applexis.utils.crypto.AESCrypto;
import com.applexis.utils.crypto.DSASign;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class GetMessageResponse extends ResponseBase {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN,
        INCORRECT_ID,
        DATABASE_ERROR
    }

    private List<MessageMinimal> messageMinimals;

    public GetMessageResponse(AESCrypto aes) {
        super(aes);
    }

    public GetMessageResponse(String errorType, AESCrypto aes) {
        super(errorType, aes);
    }

    public GetMessageResponse(List<Message> messages, AESCrypto aes) {
        this.messageMinimals = new ArrayList<>();
        if (messages != null) {
            for (Message message : messages) {
                KeyPair keyPair = DSASign.generateKeyPair();
                String signature = Base64.encodeBase64String(DSASign.generateSignature(keyPair.getPrivate(), message.getMessageText().getBytes()));
                this.messageMinimals.add(
                        new MessageMinimal(message.getSender().getId(),
                                message.getMessageText(),
                                message.getKey(),
                                signature,
                                DSASign.getPublicKeyString(keyPair.getPublic()),
                                message.getDatetime(), aes)
                );
            }
            this.success = aes.encrypt(String.valueOf(true));
        }
    }

    public List<MessageMinimal> getMessageMinimals() {
        return messageMinimals;
    }

    public void setMessageMinimals(List<MessageMinimal> messageMinimals) {
        this.messageMinimals = messageMinimals;
    }
}
