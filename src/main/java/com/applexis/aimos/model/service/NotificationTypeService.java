package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.NotificationType;

public interface NotificationTypeService {

    NotificationType create(NotificationType type);

    void delete(NotificationType type);
    void delete(Long id);

    NotificationType update(NotificationType type);

    NotificationType findById(Long id);
    NotificationType fingByType(String type);
}
