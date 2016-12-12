package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
