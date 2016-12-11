package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Message;

public class MessageResponse {

    private Long id;

    private boolean success;

    private String errorType;

    public MessageResponse() {
        success = false;
    }

    public MessageResponse(Message message) {
        if(message != null) {
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
