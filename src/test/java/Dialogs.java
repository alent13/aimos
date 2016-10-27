package com.my.db;

public class Dialogs {
  private Long id;
  private Long id_dialog_users;
  private String name;
  private String img;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId_dialog_users() {
    return id_dialog_users;
  }

  public void setId_dialog_users(Long id_dialog_users) {
    this.id_dialog_users = id_dialog_users;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }
}
