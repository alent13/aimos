package com.applexis.aimos.model.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "login_log")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_user")
    private Long userId;

    @Column(name = "device")
    private String device;

    @Column(name = "os")
    private String os;

    @Column(name = "data_time")
    private Timestamp datetime;

    public LoginLog() {
    }

    public LoginLog(Long userId, String device, String os, Timestamp datetime) {
        this.userId = userId;
        this.device = device;
        this.os = os;
        this.datetime = datetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}
