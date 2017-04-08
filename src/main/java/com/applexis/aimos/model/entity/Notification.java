package com.applexis.aimos.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notification_stack")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_type")
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "id_dialog_from")
    private Dialog dialogFrom;

    @ManyToOne
    @JoinColumn(name = "id_user_from")
    private User userFrom;

    @Column(name = "datetime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    public Notification() {
    }

    public Notification(User user, NotificationType type, Dialog dialogFrom, User userFrom, Date datetime) {
        this.user = user;
        this.type = type;
        this.dialogFrom = dialogFrom;
        this.userFrom = userFrom;
        this.datetime = datetime;
    }

    public Notification(User user, NotificationType type, User userFrom, Date datetime) {
        this.user = user;
        this.type = type;
        this.userFrom = userFrom;
        this.datetime = datetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Dialog getDialogFrom() {
        return dialogFrom;
    }

    public void setDialogFrom(Dialog dialogFrom) {
        this.dialogFrom = dialogFrom;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
