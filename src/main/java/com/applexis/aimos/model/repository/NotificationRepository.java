package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Notification;
import com.applexis.aimos.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);

}
