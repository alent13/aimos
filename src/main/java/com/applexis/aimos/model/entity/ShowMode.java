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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShowMode)) return false;

        ShowMode showMode = (ShowMode) o;

        if (id != null ? !id.equals(showMode.id) : showMode.id != null) return false;
        return mode != null ? mode.equals(showMode.mode) : showMode.mode == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
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
