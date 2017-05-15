package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "directories")
public class Directory implements FSItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_parent")
    private Long parentId;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_access_mode")
    private AccessMode accessMode;

    @Column(name = "hash_code")
    private String hash;

    public Directory() {
    }

    public Directory(String name, Long parentId, User user,
                     AccessMode accessMode, String hash) {
        this.name = name;
        this.parentId = parentId;
        this.user = user;
        this.accessMode = accessMode;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
