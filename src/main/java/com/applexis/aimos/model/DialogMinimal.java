package com.applexis.aimos.model;

import com.applexis.utils.crypto.AESCrypto;

import java.util.List;

public class DialogMinimal {

    protected String id;

    protected String name;

    private UserMinimalInfo lastSender;

    private MessageMinimal lastMessage;

    protected List<UserMinimalInfo> users;

    public DialogMinimal() {
    }

    public DialogMinimal(Long id, String name, UserMinimalInfo lastSender, MessageMinimal lastMessage, List<UserMinimalInfo> users, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(id));
        this.name = name;
        this.lastSender = lastSender;
        this.lastMessage = lastMessage;
        this.users = users;
    }

    public DialogMinimal(Long id, String name, List<UserMinimalInfo> users, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(id));
        this.name = aes.encrypt(name);
        this.users = users;
    }

    public Long getId(AESCrypto aes) {
        return Long.getLong(aes.decrypt(id));
    }

    public void setId(Long id, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(id));
    }

    public String getName(AESCrypto aes) {
        return aes.decrypt(name);
    }

    public void setName(String name, AESCrypto aes) {
        this.name = aes.encrypt(name);
    }

    public List<UserMinimalInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserMinimalInfo> users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserMinimalInfo getLastSender() {
        return lastSender;
    }

    public void setLastSender(UserMinimalInfo lastSender) {
        this.lastSender = lastSender;
    }

    public MessageMinimal getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageMinimal lastMessage) {
        this.lastMessage = lastMessage;
    }
}
