package com.applexis.aimos.model.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_user")
    private Long senderId;

    @Column(name = "id_dialog")
    private Long dialogId;

    @Column(name = "key")
    private String key;

    @Column(name = "text")
    private String text;

    @Column(name = "is_readed")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean readed;

    public Message() {
    }

    public Message(Long senderId, Long dialogId,
                   String key, String text, boolean readed) {
        this();
        this.senderId = senderId;
        this.dialogId = dialogId;
        this.key = key;
        this.text = text;
        this.readed = readed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getDialogId() {
        return dialogId;
    }

    public void setDialogId(Long dialogId) {
        this.dialogId = dialogId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}
