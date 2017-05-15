package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.AccessMode;
import com.applexis.aimos.model.repository.AccessModeRepository;
import com.applexis.aimos.model.service.AccessModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author applexis
 */

@Service
public class AccessModeServiceImpl implements AccessModeService {

    final AccessModeRepository repository;

    @Autowired
    public AccessModeServiceImpl(AccessModeRepository repository) {
        this.repository = repository;
    }

    @Override
    public AccessMode create(AccessMode accessMode) {
        return repository.save(accessMode);
    }

    @Override
    public void delete(AccessMode accessMode) {
        repository.delete(accessMode);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public AccessMode update(AccessMode accessMode) {
        return repository.save(accessMode);
    }

    @Override
    public AccessMode findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public AccessMode findByAccessMode(String accessMode) {
        return repository.findByAccessMode(accessMode);
    }
}
