package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.User;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class DialogResponse extends DialogMinimal {

    public enum ErrorType {
        SUCCESS,
        INCORRECT_ID,
        DATABASE_ERROR,
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN
    }

    private boolean success;

    private String errorType;

    public DialogResponse() {
        this.success = false;
    }

    public DialogResponse(String errorType) {
        if (errorType == DialogResponse.ErrorType.SUCCESS.name()) {
            this.success = true;
        } else {
            this.success = false;
        }
        this.errorType = errorType;
    }

    public DialogResponse(Dialog dialog, List<User> users, Key DESKey) {
        this.id = dialog.getId();
        this.name = dialog.getName();
        this.users = new ArrayList<>();
        this.success = true;
        for (User u : users) {
            this.users.add(new UserMinimalInfo(u));
        }
    }

    public DialogResponse(Dialog dialog, List<User> users) {
        this.id = dialog.getId();
        this.name = dialog.getName();
        this.users = new ArrayList<>();
        this.success = true;
        for (User user : users) {
            this.users.add(new UserMinimalInfo(user));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserMinimalInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserMinimalInfo> users) {
        this.users = users;
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
