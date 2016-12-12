package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.NotificationType;
import com.applexis.aimos.model.repository.NotificationTypeRepository;
import com.applexis.aimos.model.service.NotificationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    public final NotificationTypeRepository repository;

    @Autowired
    public NotificationTypeServiceImpl(NotificationTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public NotificationType create(NotificationType type) {
        return repository.save(type);
    }

    @Override
    public void delete(NotificationType type) {
        repository.delete(type);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public NotificationType update(NotificationType type) {
        return repository.save(type);
    }

    @Override
    public NotificationType findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public NotificationType fingByType(String type) {
        return repository.findByType(type);
    }
}
