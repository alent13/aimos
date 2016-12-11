package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.User;

public class LoginResponse extends UserMinimalInfo {

    private boolean success;

    private String errorType;

    private String token;

    public LoginResponse() {
        this.success = false;
    }

    public LoginResponse(User user) {
        if(user != null) {
            success = true;
            setName(user.getName());
            setSurname(user.getSurname());
            setLogin(user.getLogin());
            this.token = user.getToken();
        }
        else {
            success = false;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
