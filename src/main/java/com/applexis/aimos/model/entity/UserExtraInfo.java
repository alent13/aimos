package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "users_extra")
public class UserExtraInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "about")
    private String about;

    public UserExtraInfo() {
    }

    public UserExtraInfo(String phone, ShowMode phoneShowMode,
                         String email, ShowMode emailShowMode,
                         String about, ShowMode aboutShowMode) {
        this.phone = phone;
        this.email = email;
        this.about = about;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
