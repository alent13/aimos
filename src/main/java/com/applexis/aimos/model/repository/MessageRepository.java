package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
