package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.User;
import com.applexis.utils.crypto.AESCrypto;

public class UserMinimalInfo {

    private String id;

    private String name;

    private String surname;

    private String login;

    private String img;

    public UserMinimalInfo() {
    }

    public UserMinimalInfo(User user, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(user.getId()));
        this.name = aes.encrypt(user.getName());
        this.surname = aes.encrypt(user.getSurname());
        this.login = aes.encrypt(user.getLogin());
    }

    public UserMinimalInfo(long id, String name, String surname,
                           String login, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(id));
        this.name = aes.encrypt(name);
        this.surname = aes.encrypt(surname);
        this.login = aes.encrypt(login);
    }

    public long getId(AESCrypto aes) {
        return Long.getLong(aes.decrypt(id));
    }

    public void setId(long id, AESCrypto aes) {
        this.id = aes.encrypt(String.valueOf(id));
    }

    public String getName(AESCrypto aes) {
        return aes.decrypt(name);
    }

    public void setName(String name, AESCrypto aes) {
        this.name = aes.encrypt(name);
    }

    public String getSurname(AESCrypto aes) {
        return aes.decrypt(surname);
    }

    public void setSurname(String surname, AESCrypto aes) {
        this.surname = aes.encrypt(surname);
    }

    public String getLogin(AESCrypto aes) {
        return aes.decrypt(login);
    }

    public void setLogin(String login, AESCrypto aes) {
        this.login = aes.encrypt(login);
    }

    public String getImg(AESCrypto aes) {
        return aes.decrypt(img);
    }

    public void setImg(String img, AESCrypto aes) {
        this.img = aes.encrypt(img);
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
