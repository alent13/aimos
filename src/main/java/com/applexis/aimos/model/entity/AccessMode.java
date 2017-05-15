package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "d_access_mode")
public class AccessMode {

    public static final String MODE_PRIVATE = "MODE_PRIVATE";
    public static final String MODE_PUBLIC = "MODE_PUBLIC";
    public static final String MODE_LINK = "MODE_LINK";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_mode")
    private String accessMode;

    public AccessMode() {
    }

    public AccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }
}
