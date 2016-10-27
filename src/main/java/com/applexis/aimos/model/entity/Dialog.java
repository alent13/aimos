package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "dialog")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_dialog_users")
    private Long dialogUsersId;

    @Column(name = "name")
    private String name;

    @Column(name = "img")
    private String logoPath;

    public Dialog(){
    }

    public Dialog(Long dialogUsersId, String name, String logoPath) {
        this();
        this.dialogUsersId = dialogUsersId;
        this.name = name;
        this.logoPath = logoPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDialogUsersId() {
        return dialogUsersId;
    }

    public void setDialogUsersId(Long dialogUsersId) {
        this.dialogUsersId = dialogUsersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
