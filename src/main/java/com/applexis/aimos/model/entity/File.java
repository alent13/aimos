package com.applexis.aimos.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "files")
public class File implements FSItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "filename")
    private String name;

    @Column(name = "encrypt_key")
    private String key;

    @Column(name = "size")
    private Long size;

    @Column(name = "add_datetime", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addDatetime;

    @Column(name = "last_modified_datetime", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDatetime;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_access_mode")
    private AccessMode accessMode;

    @ManyToOne
    @JoinColumn(name = "id_directory")
    private Directory parentDirectory;

    @Column(name = "hash_code")
    private String hash;

    public File() {
    }

    public File(String path, String name, String key, Long size,
                Date addDatetime, Date lastModifiedDatetime, User user,
                AccessMode accessMode, Directory parentDirectory, String hash) {
        this.path = path;
        this.name = name;
        this.key = key;
        this.size = size;
        this.addDatetime = addDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
        this.user = user;
        this.accessMode = accessMode;
        this.parentDirectory = parentDirectory;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getAddDatetime() {
        return addDatetime;
    }

    public void setAddDatetime(Date addDatetime) {
        this.addDatetime = addDatetime;
    }

    public Date getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public void setLastModifiedDatetime(Date lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
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

    public Directory getParentDirectory() {
        return parentDirectory;
    }

    public void setParentDirectory(Directory parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
