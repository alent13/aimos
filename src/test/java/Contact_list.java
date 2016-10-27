package com.my.db;

public class Contact_list {
  private Long id;
  private Long id_user;
  private Long id_friend;
  private String relationship;

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

  public Long getId_friend() {
    return id_friend;
  }

  public void setId_friend(Long id_friend) {
    this.id_friend = id_friend;
  }

  public String getRelationship() {
    return relationship;
  }

  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }
}
