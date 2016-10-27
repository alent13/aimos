package com.my.db;

public class Login_log {
  private Long id;
  private Long id_user;
  private String device;
  private String os;
  private java.sql.Timestamp date_time;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId_user() {
    return id_user;
  }

  public void setId_user(Long id_user) {
    this.id_user = id_user;
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

  public java.sql.Timestamp getDate_time() {
    return date_time;
  }

  public void setDate_time(java.sql.Timestamp date_time) {
    this.date_time = date_time;
  }
}
