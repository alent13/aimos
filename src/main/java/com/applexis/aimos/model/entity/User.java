package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_extra")
    private UserExtraInfo userExtraInfo;

    @ManyToOne
    @JoinColumn(name = "id_user_settings")
    private UserSettings userSettings;

    @Column(name = "img")
    private String userLogo;

    public User() {
    }

    public User(String login, String name,
                String surname, String password,
                UserExtraInfo userExtraInfo, String userLogo) {
        this();
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.userExtraInfo = userExtraInfo;
        this.userLogo = userLogo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserExtraInfo getUserExtraInfo() {
        return userExtraInfo;
    }

    public void setUserExtraInfo(UserExtraInfo userExtraInfo) {
        this.userExtraInfo = userExtraInfo;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
}
