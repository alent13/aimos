package com.applexis.aimos.model.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Column(name = "public")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isPublic;

    @Column(name = "img")
    private String logoPath;

    public Dialog() {
    }

    public Dialog(String name) {
        this.name = name;
        logoPath = "";
        isPublic = false;
        active = true;
    }

    public Dialog(String name, String logoPath) {
        this();
        this.name = name;
        this.logoPath = logoPath;
    }

    @Override
    public boolean equals(Object obj) {
        boolean sameSame = false;

        if (obj != null && obj instanceof Dialog) {
            sameSame = Objects.equals(this.id, ((Dialog) obj).id) &&
                    Objects.equals(this.name, ((Dialog) obj).name) &&
                    Objects.equals(this.active, ((Dialog) obj).active) &&
                    Objects.equals(this.isPublic, ((Dialog) obj).isPublic) &&
                    Objects.equals(this.logoPath, ((Dialog) obj).logoPath);
        }

        return sameSame;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
