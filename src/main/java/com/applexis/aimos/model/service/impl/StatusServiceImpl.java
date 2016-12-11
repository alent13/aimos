package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Status;
import com.applexis.aimos.model.repository.StatusRepository;
import com.applexis.aimos.model.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;

public class StatusServiceImpl implements StatusService {

    @Autowired
    public StatusRepository repository;

    @Override
    public Status getByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public Status getById(Long id) {
        return repository.findOne(id);
    }
}
