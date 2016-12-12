package com.applexis.aimos.model;

import java.util.List;

public class DialogMinimal {

    protected Long id;

    protected String name;

    protected List<UserMinimalInfo> users;

    public DialogMinimal() {
    }

    public DialogMinimal(Long id, String name, List<UserMinimalInfo> users) {
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
