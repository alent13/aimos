package com.applexis.aimos.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageResponse {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN,
        INCORRECT_ID,
        DATABASE_ERROR
    }

    class Message {
        Long idUserFrom;
        String eText;
        String eKey;
        Date datetime;

        Message(Long idUserFrom, String eText, String eKey, Date datetime) {
            this.idUserFrom = idUserFrom;
            this.eText = eText;
            this.eKey = eKey;
            this.datetime = datetime;
        }
    }

    private List<Message> messages;

    public boolean success;

    public String errorType;

    public MessageResponse() {
        this.success = false;
    }

    public MessageResponse(String errorType) {
        this.success = false;
        this.errorType = errorType;
    }

    public MessageResponse(List<com.applexis.aimos.model.entity.Message> messages) {
        if (messages != null) {
            this.messages = new ArrayList<>();
            for (com.applexis.aimos.model.entity.Message message : messages) {
                this.messages.add(
                        new Message(message.getId(),
                                message.getText(),
                                message.getKey(),
                                message.getDatetime())
                );
            }
            this.success = true;
        }
    }
}
