package com.applexis.aimos.model.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by applexis on 08.04.2017.
 */

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "path")
    private String path;

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

    public File() {
    }

    public File(String path, Long size, Date addDatetime,
                Date lastModifiedDatetime, User user,
                AccessMode accessMode) {
        this.path = path;
        this.size = size;
        this.addDatetime = addDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
        this.user = user;
        this.accessMode = accessMode;
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
}
