package com.applexis.aimos.model;

import com.applexis.utils.crypto.AESCrypto;

public abstract class ResponseBase {

    public static final String FALSE = "false";
    public static final String BAD_PUBLIC_KEY = "BAD_PUBLIC_KEY";

    protected String success;

    protected String errorType;

    public ResponseBase() {
    }

    public ResponseBase(AESCrypto aes) {
        this.success = aes.encrypt(FALSE);
    }

    public ResponseBase(String errorType, AESCrypto aes) {
        if (errorType.equals(BAD_PUBLIC_KEY)) {
            this.success = FALSE;
            this.errorType = errorType;
        } else {
            this.success = aes.encrypt(FALSE);
            this.errorType = aes.encrypt(errorType);
        }
    }

    public boolean isSuccess(AESCrypto aes) {
        return Boolean.getBoolean(aes.decrypt(success));
    }

    public void setSuccess(boolean success, AESCrypto aes) {
        this.success = aes.encrypt(String.valueOf(success));
    }

    public String getErrorType(AESCrypto aes) {
        return aes.decrypt(errorType);
    }

    public void setErrorType(String errorType, AESCrypto aes) {
        this.errorType = aes.encrypt(errorType);
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
