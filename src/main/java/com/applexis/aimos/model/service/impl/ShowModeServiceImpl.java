package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.ShowMode;
import com.applexis.aimos.model.repository.ShowModeRepository;
import com.applexis.aimos.model.service.ShowModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowModeServiceImpl implements ShowModeService {

    private final ShowModeRepository repository;

    @Autowired
    public ShowModeServiceImpl(ShowModeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShowMode create(ShowMode mode) {
        return repository.save(mode);
    }

    @Override
    public void delete(ShowMode mode) {
        repository.delete(mode);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public ShowMode update(ShowMode mode) {
        return repository.save(mode);
    }

    @Override
    public ShowMode findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public ShowMode fingByMode(String mode) {
        return repository.findByMode(mode);
    }
}
