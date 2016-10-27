package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.UserSettings;
import com.applexis.aimos.model.repository.UserSettingsRepository;
import com.applexis.aimos.model.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserSettingsRepository repository;

    @Autowired
    public UserSettingsServiceImpl(UserSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserSettings create(UserSettings settings) {
        return repository.save(settings);
    }

    @Override
    public void delete(UserSettings settings) {
        repository.delete(settings);
    }

    @Override
    public void delete(Long id) {
        repository.delete(repository.findOne(id));
    }

    @Override
    public void update(UserSettings settings) {
        repository.save(settings);
    }

    @Override
    public UserSettings getById(Long id) {
        return repository.findOne(id);
    }
}
