package com.applexis.aimos.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "id_dialog")
    private Dialog dialog;

    @Column(name = "key")
    private String key;

    @Column(name = "text")
    private String text;

    @Column(name = "datetime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    public Message() {
    }

    public Message(User sender, Dialog dialog, String key, String text, Date datetime) {
        this.sender = sender;
        this.dialog = dialog;
        this.key = key;
        this.text = text;
        this.datetime = datetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
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

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
