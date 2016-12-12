package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author applexis
 */
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    NotificationType findByType(String type);
}
