package com.my.db;

public class Messages {
  private Long id;
  private Long id_user;
  private Long id_dialog;
  private String key;
  private String text;
  private Long is_read;

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

  public Long getId_dialog() {
    return id_dialog;
  }

  public void setId_dialog(Long id_dialog) {
    this.id_dialog = id_dialog;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getIs_read() {
    return is_read;
  }

  public void setIs_read(Long is_read) {
    this.is_read = is_read;
  }
}
