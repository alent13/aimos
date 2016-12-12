package com.applexis.aimos.model;

import com.applexis.aimos.model.entity.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationResponse {

    public enum ErrorType {
        BAD_PUBLIC_KEY,
        INCORRECT_TOKEN,
        DATABASE_ERROR
    }

    public enum Type {
        ADDED_TO_FRIEND_LIST,
        GET_MESSAGE,
        INVITED_TO_DIALOG,
        DIALOG_EXCLUDE,
        DIALOG_RENAMED
    }

    class NotificationMinimal {
        private Long id;
        private String notificationType;
        private Long idDialogFrom;
        private String dialogName;
        private UserMinimalInfo userFrom;
        private Date datetime;

        public NotificationMinimal(Long id, String notificationType, Long idDialogFrom, String dialogName, UserMinimalInfo userFrom, Date datetime) {
            this.id = id;
            this.notificationType = notificationType;
            this.idDialogFrom = idDialogFrom;
            this.dialogName = dialogName;
            this.userFrom = userFrom;
            this.datetime = datetime;
        }
    }

    private List<NotificationMinimal> notifications;

    private boolean success;

    private String errorType;

    public NotificationResponse() {
        this.success = false;
    }

    public NotificationResponse(String errorType) {
        this.success = false;
        this.errorType = errorType;
    }

    public NotificationResponse(List<Notification> notifications) {
        this();
        if(notifications != null) {
            this.notifications = new ArrayList<>(notifications.size());
            for (Notification notification : notifications) {
                this.notifications.add(
                        new NotificationMinimal(notification.getId(),
                                notification.getType().getType(),
                                notification.getDialogFrom().getId(),
                                notification.getDialogFrom().getName(),
                                new UserMinimalInfo(notification.getUserFrom()),
                                notification.getDatetime())
                );
            }
            this.success = true;
        }
    }
}
