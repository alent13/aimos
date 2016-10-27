package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}
