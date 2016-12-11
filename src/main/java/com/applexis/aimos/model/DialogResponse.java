package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DialogResponse {

    private Long id;

    private String name;

    private List<UserMinimalInfo> users;

    private boolean success;

    public DialogResponse() {
        success = false;
    }

    public DialogResponse(Dialog dialog, List<User> users) {
        this.id = dialog.getId();
        this.name = dialog.getName();
        this.users = new ArrayList<>();
        for (User u : users) {
            this.users.add(new UserMinimalInfo(u));
        }
    }

    public DialogResponse(Long id, String name, List<UserMinimalInfo> users) {
        this.id = id;
        this.name = name;
        this.users = users;
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
}
