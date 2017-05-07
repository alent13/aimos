package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "contact_list")
public class ContactList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_user")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "id_friend")
    private User friend;

    @Column(name = "relationship")
    private String relationship;

    public ContactList() {
    }

    public ContactList(Long userId, User friend) {
        this.userId = userId;
        this.friend = friend;
        this.relationship = "";
    }

    public ContactList(Long userId, User friend, String relationship) {
        this.userId = userId;
        this.friend = friend;
        this.relationship = relationship;
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

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
