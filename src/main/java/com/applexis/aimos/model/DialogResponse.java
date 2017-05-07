package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.User;
import com.applexis.utils.crypto.AESCrypto;

import java.util.ArrayList;
import java.util.List;

public class DialogResponse extends ResponseBase {

    public enum ErrorType {
        SUCCESS,
        INCORRECT_ID,
        DATABASE_ERROR,
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN
    }

    DialogMinimal dialog;

    public DialogResponse(AESCrypto aes) {
        super(aes);
    }

    public DialogResponse(String errorType, AESCrypto aes) {
        super(errorType, aes);
    }

    public DialogResponse(Dialog dialog, List<User> users, AESCrypto aes) {
        this.dialog = new DialogMinimal();
        this.dialog.setId(dialog.getId(), aes);
        this.dialog.setName(dialog.getName(), aes);
        this.dialog.setUsers(new ArrayList<>());
        this.success = aes.encrypt(String.valueOf(true));
        List<UserMinimalInfo> infoList = new ArrayList<>();
        for (User user : users) {
            infoList.add(new UserMinimalInfo(user, aes));
        }
        this.dialog.setUsers(infoList);
    }

    public DialogMinimal getDialog() {
        return dialog;
    }

    public void setDialog(DialogMinimal dialog) {
        this.dialog = dialog;
    }
}
