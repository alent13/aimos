package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.UserSettings;

/**
 * @author applexis
 */
public interface UserSettingsService {

    UserSettings create(UserSettings settings);

    void delete(UserSettings settings);
    void delete(Long id);

    void update(UserSettings settings);

    UserSettings getById(Long id);

}
