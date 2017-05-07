package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.UserToken;
import com.applexis.utils.crypto.AESCrypto;

public class LoginResponse extends ResponseBase {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        USER_ALREADY_EXIST,
        INCORRECT_PASSWORD,
        INCORRECT_TOKEN
    }

    private UserMinimalInfo userMinimalInfo;

    private String token;

    public LoginResponse(AESCrypto aes) {
        super(aes);
    }

    public LoginResponse(String errorType, AESCrypto aes) {
        super(errorType, aes);
    }

    public LoginResponse(UserToken userToken, AESCrypto aes) {
        if(userToken != null) {
            success = aes.encrypt(String.valueOf(true));
            userMinimalInfo = new UserMinimalInfo();
            userMinimalInfo.setId(userToken.getUser().getId(), aes);
            userMinimalInfo.setName(userToken.getUser().getName(), aes);
            userMinimalInfo.setSurname(userToken.getUser().getSurname(), aes);
            userMinimalInfo.setLogin(userToken.getUser().getLogin(), aes);
            this.token = aes.encrypt(userToken.getToken());
        }
        else {
            success = aes.encrypt(String.valueOf(false));
        }
    }

    public String getToken(AESCrypto aes) {
        return aes.decrypt(token);
    }

    public void setToken(String token, AESCrypto aes) {
        this.token = aes.encrypt(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserMinimalInfo getUserMinimalInfo() {
        return userMinimalInfo;
    }

    public void setUserMinimalInfo(UserMinimalInfo userMinimalInfo) {
        this.userMinimalInfo = userMinimalInfo;
    }
}
