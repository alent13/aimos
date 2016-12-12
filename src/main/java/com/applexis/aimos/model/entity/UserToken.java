package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "token_user")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "browser")
    private String browser;

    @Column(name = "os")
    private String os;

    @Column(name = "token")
    private String token;

    public UserToken() {
    }

    public UserToken(User user, String browser, String os, String token) {
        this.user = user;
        this.browser = browser;
        this.os = os;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
