package com.applexis.aimos.model.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Column(name = "img")
    private String userLogo;

    @Column(name = "registration_datetime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDatetime;

    @Column(name = "last_action_datetime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActionDatetime;

    public User() {
    }

    public User(String login, String name,
                String surname, String password,
                UserExtraInfo userExtraInfo,
                UserSettings userSettings,
                boolean active, Date registrationDatetime,
                Date lastActionDatetime) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.userExtraInfo = userExtraInfo;
        this.userSettings = userSettings;
        this.active = active;
        this.registrationDatetime = registrationDatetime;
        this.lastActionDatetime = lastActionDatetime;
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

    public Date getRegistrationDatetime() {
        return registrationDatetime;
    }

    public void setRegistrationDatetime(Date registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastActionDatetime() {
        return lastActionDatetime;
    }

    public void setLastActionDatetime(Date lastActionDatetime) {
        this.lastActionDatetime = lastActionDatetime;
    }
}
