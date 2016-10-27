package com.my.db;

public class Users_extra {
  private Long id;
  private String phone;
  private Long phone_show_mode;
  private String email;
  private Long email_show_mode;
  private String about;
  private Long about_show_mode;

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

  public Long getPhone_show_mode() {
    return phone_show_mode;
  }

  public void setPhone_show_mode(Long phone_show_mode) {
    this.phone_show_mode = phone_show_mode;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getEmail_show_mode() {
    return email_show_mode;
  }

  public void setEmail_show_mode(Long email_show_mode) {
    this.email_show_mode = email_show_mode;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public Long getAbout_show_mode() {
    return about_show_mode;
  }

  public void setAbout_show_mode(Long about_show_mode) {
    this.about_show_mode = about_show_mode;
  }
}
