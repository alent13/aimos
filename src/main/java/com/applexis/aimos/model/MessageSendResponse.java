package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Message;

public class MessageSendResponse {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        BAD_HASH,
        INCORRECT_TOKEN,
        INCORRECT_ID,
        DATABASE_ERROR
    }

    private Long id;

    private boolean success;

    private String errorType;

    public MessageSendResponse() {
        success = false;
    }

    public MessageSendResponse(String errorType) {
        this.success = false;
        this.errorType = errorType;
    }

    public MessageSendResponse(Message message) {
        if (message != null) {
            this.id = message.getId();
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
