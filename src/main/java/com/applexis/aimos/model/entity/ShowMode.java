package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "d_show_mode")
public class ShowMode {

    public static final String CONTACTSONLY = "CONTACTSONLY";
    public static final String NOONE = "NOONE";
    public static final String EVERYONE = "EVERYONE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mode")
    private String mode;

    public ShowMode() {
    }

    public ShowMode(String mode) {
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
