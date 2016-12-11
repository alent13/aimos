package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "notifications")
public class Notifcation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_page_show")
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

    public Notifcation() {
    }

    public Notifcation(User user, NotificationType type, Dialog dialogFrom, User userFrom) {
        this.user = user;
        this.type = type;
        this.dialogFrom = dialogFrom;
        this.userFrom = userFrom;
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
}
