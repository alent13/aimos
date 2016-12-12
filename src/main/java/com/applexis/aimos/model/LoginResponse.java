package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.UserToken;

public class LoginResponse extends UserMinimalInfo {

    public enum ErrorType {
        USER_ALREADY_EXIST,
        BAD_PUBLIC_KEY,
        INCORRECT_PASSWORD,
        INCORRECT_TOKEN
    }

    private boolean success;

    private String errorType;

    private String token;

    public LoginResponse() {
        this.success = false;
    }

    public LoginResponse(String errorType) {
        this.success = false;
        this.errorType = errorType;
    }

    public LoginResponse(UserToken userToken) {
        if(userToken != null) {
            success = true;
            setName(userToken.getUser().getName());
            setSurname(userToken.getUser().getSurname());
            setLogin(userToken.getUser().getLogin());
            this.token = userToken.getToken();
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
