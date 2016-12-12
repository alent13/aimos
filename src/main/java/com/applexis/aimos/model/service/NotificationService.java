package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Notification;
import com.applexis.aimos.model.entity.User;

import java.util.List;

/**
 * @author applexis
 */
public interface NotificationService {

    Notification create(Notification notification);

    void delete(Notification notification);
    void delete(Long id);

    Notification update(Notification notification);

    Notification findById(Long id);
    List<Notification> fingByUser(User user);
}
