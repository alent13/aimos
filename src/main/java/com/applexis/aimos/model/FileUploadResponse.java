package com.applexis.aimos.model;

import com.applexis.utils.crypto.AESCrypto;

public class FileUploadResponse extends ResponseBase {

    public enum ErrorType {
        DATABASE_ERROR,
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN,
        INCORRECT_FILE_DATA,
        FILE_SAVE_ERROR
    }

    private FileData fileData;

    public FileUploadResponse(AESCrypto aes) {
        super(aes);
    }

    public FileUploadResponse(String errorType, AESCrypto aes) {
        super(errorType, aes);
    }

    public FileUploadResponse(FileData fileData, AESCrypto aes) {
        this.success = aes.encrypt(String.valueOf(true));
        this.fileData = fileData;
    }

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }
}
