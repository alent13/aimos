package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "d_notification_type")
public class NotificationType {

    public static final String ADDED_TO_FRIEND_LIST = "ADDED_TO_FRIEND_LIST";
    public static final String GET_MESSAGE = "GET_MESSAGE";
    public static final String INVITED_TO_DIALOG = "INVITED_TO_DIALOG";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    private String type;

    public NotificationType() {
    }

    public NotificationType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
