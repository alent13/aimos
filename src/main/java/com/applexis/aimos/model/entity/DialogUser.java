package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "dialog_user")
public class DialogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_dialog")
    private Long dialogId;

    @Column(name = "id_user")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "id_status")
    private Status status;

    public DialogUser() {
    }

    public DialogUser(Long dialogId, Long userId, Status status) {
        this.dialogId = dialogId;
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDialogId() {
        return dialogId;
    }

    public void setDialogId(Long dialogId) {
        this.dialogId = dialogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
