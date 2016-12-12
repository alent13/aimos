package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Notification;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.NotificationRepository;
import com.applexis.aimos.model.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    public NotificationRepository repository;

    @Override
    public Notification create(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public void delete(Notification notification) {
        repository.delete(notification);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public Notification update(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public Notification findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Notification> fingByUser(User user) {
        return repository.findByUser(user);
    }
}
