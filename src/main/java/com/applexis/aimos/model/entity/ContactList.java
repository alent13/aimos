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

    @Column(name = "id_friend")
    private Long friendId;

    public ContactList() {
    }

    public ContactList(Long userId, Long friendId) {
        this();
        this.userId = userId;
        this.friendId = friendId;
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

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
