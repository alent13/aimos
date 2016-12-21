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
    private Boolean active;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (userExtraInfo != null ? !userExtraInfo.equals(user.userExtraInfo) : user.userExtraInfo != null)
            return false;
        if (userSettings != null ? !userSettings.equals(user.userSettings) : user.userSettings != null) return false;
        if (active != null ? !active.equals(user.active) : user.active != null) return false;
        if (userLogo != null ? !userLogo.equals(user.userLogo) : user.userLogo != null) return false;
        if (registrationDatetime != null ? !registrationDatetime.equals(user.registrationDatetime) : user.registrationDatetime != null)
            return false;
        return lastActionDatetime != null ? lastActionDatetime.equals(user.lastActionDatetime) : user.lastActionDatetime == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (userExtraInfo != null ? userExtraInfo.hashCode() : 0);
        result = 31 * result + (userSettings != null ? userSettings.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (userLogo != null ? userLogo.hashCode() : 0);
        result = 31 * result + (registrationDatetime != null ? registrationDatetime.hashCode() : 0);
        result = 31 * result + (lastActionDatetime != null ? lastActionDatetime.hashCode() : 0);
        return result;
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
