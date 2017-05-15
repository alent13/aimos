package com.applexis.aimos.model;

import com.applexis.utils.crypto.AESCrypto;

public class FolderCreateResponse extends ResponseBase {

    public enum ErrorType {
        DATABASE_ERROR,
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN
    }

    private FileData fileData;

    public FolderCreateResponse(AESCrypto aes, FileData fileData) {
        this.success = aes.encrypt(String.valueOf(true));
        this.fileData = fileData;
    }

    public FolderCreateResponse(AESCrypto aes) {
        super(aes);
    }

    public FolderCreateResponse(String errorType, AESCrypto aes) {
        super(errorType, aes);
    }

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }
}
